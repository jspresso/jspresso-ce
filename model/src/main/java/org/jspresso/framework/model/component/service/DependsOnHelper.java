/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.model.component.service;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.hibernate.Hibernate;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.accessor.IAccessorFactoryProvider;
import org.jspresso.framework.util.accessor.bean.BeanAccessorFactory;
import org.jspresso.framework.util.bean.AccessorInfo;
import org.jspresso.framework.util.bean.EAccessorType;
import org.jspresso.framework.util.bean.IPropertyChangeCapable;
import org.jspresso.framework.util.exception.NestedRuntimeException;

/**
 * DependsOn notification forwarding helper.
 *
 * @author Vincent Vandenschrick
 */
public class DependsOnHelper {

  private DependsOnHelper() {
    // private helper constructor.
  }

  /**
   * Register depends on listeners.
   *
   * @param annotatedClass
   *     the annotated class
   * @param sourceBean
   *     the source bean
   * @param accessorFactoryProvider
   *     the accessor factory
   */
  public static void registerDependsOnListeners(Class<?> annotatedClass, IPropertyChangeCapable sourceBean,
                                                IAccessorFactoryProvider accessorFactoryProvider) {
    Method[] methods = annotatedClass.getMethods();
    for (Method method : methods) {
      DependsOn dependsOn = method.getAnnotation(DependsOn.class);
      if (dependsOn != null) {
        IAccessorFactory accessorFactory = accessorFactoryProvider.getAccessorFactory();
        AccessorInfo accessorInfo = accessorFactory.getAccessorInfo(method);
        String targetProperty = accessorInfo.getAccessedPropertyName();
        EAccessorType accessorType = accessorInfo.getAccessorType();
        String[] sourceProperties = dependsOn.value();
        if (accessorType != EAccessorType.NONE && targetProperty != null) {
          if (sourceProperties != null && sourceProperties.length > 0) {
            String sourceCollectionProperty = dependsOn.sourceCollection();
            if (sourceCollectionProperty != null && sourceCollectionProperty.length() > 0) {
              for (String sourceProperty : sourceProperties) {
                registerNotificationCollectionForwarding(sourceBean, accessorFactoryProvider, sourceCollectionProperty,
                    sourceProperty, targetProperty);
              }
            } else {
              for (String sourceProperty : sourceProperties) {
                registerNotificationForwarding(sourceBean, accessorFactoryProvider, sourceProperty, targetProperty);
              }
            }
          }
        } else if (method.getParameterTypes().length == 0) {
          if (sourceProperties != null && sourceProperties.length > 0) {
            String sourceCollectionProperty = dependsOn.sourceCollection();
            if (sourceCollectionProperty != null && sourceCollectionProperty.length() > 0) {
              for (String sourceProperty : sourceProperties) {
                registerNotificationCollectionForwarding(sourceBean, accessorFactoryProvider, sourceCollectionProperty,
                    sourceProperty, method);
              }
            } else {
              for (String sourceProperty : sourceProperties) {
                registerNotificationForwarding(sourceBean, accessorFactoryProvider, sourceProperty, method);
              }
            }
          }
        }
      }
    }
  }

  /**
   * Registers a property change listener to forward property changes.
   *
   * @param sourceBean
   *     the source bean.
   * @param accessorFactoryProvider
   *     the accessor factory
   * @param sourceProperty
   *     the name of the source property.
   * @param forwardedProperty
   *     the name of the forwarded property.
   */
  public static void registerNotificationForwarding(IPropertyChangeCapable sourceBean,
                                                    IAccessorFactoryProvider accessorFactoryProvider,
                                                    String sourceProperty, String forwardedProperty) {
    registerNotificationForwarding(sourceBean, accessorFactoryProvider, sourceProperty,
        new String[]{forwardedProperty});
  }

  /**
   * Registers a property change listener to forward property changes.
   *
   * @param sourceBean
   *     the source bean.
   * @param accessorFactoryProvider
   *     the accessor factory
   * @param sourceProperty
   *     the name of the source property.
   * @param forwardedMethod
   *     the name of the forwarded method.
   */
  public static void registerNotificationForwarding(IPropertyChangeCapable sourceBean,
                                                    IAccessorFactoryProvider accessorFactoryProvider,
                                                    String sourceProperty, Method forwardedMethod) {
    registerNotificationForwarding(sourceBean, accessorFactoryProvider, sourceProperty, new Method[]{forwardedMethod});
  }

  /**
   * Registers a property change listener to forward property changes.
   *
   * @param sourceBean
   *     the source bean.
   * @param accessorFactoryProvider
   *     the accessor factory
   * @param sourceProperty
   *     the name of the source property.
   * @param forwardedProperty
   *     the name of the forwarded property.
   */
  public static void registerNotificationForwarding(IPropertyChangeCapable sourceBean,
                                                    IAccessorFactoryProvider accessorFactoryProvider,
                                                    String sourceProperty, String... forwardedProperty) {
    registerNotificationForwarding(sourceBean, sourceBean, accessorFactoryProvider, sourceProperty, forwardedProperty);
  }

  private static void registerNotificationForwarding(IPropertyChangeCapable sourceBean,
                                                     IPropertyChangeCapable targetBean,
                                                     IAccessorFactoryProvider accessorFactoryProvider,
                                                     String sourceProperty, String... forwardedProperty) {
    if (sourceBean == null) {
      return;
    }
    if (sourceBean == targetBean && Arrays.binarySearch(forwardedProperty, sourceProperty) >= 0) {
      throw new IllegalArgumentException("Forwarded properties " + Arrays.asList(forwardedProperty)
          + " cannot contain source property " + sourceProperty + " when registering notification forwarding");
    }
    sourceBean.addPropertyChangeListener(sourceProperty, new ForwardingPropertyChangeListener(targetBean,
        accessorFactoryProvider, forwardedProperty));
  }

  /**
   * Registers a property change listener to forward property changes.
   *
   * @param sourceBean
   *     the source bean.
   * @param accessorFactoryProvider
   *     the accessor factory
   * @param sourceProperty
   *     the name of the source property.
   * @param forwardedMethod
   *     the name of the forwarded method.
   */
  public static void registerNotificationForwarding(IPropertyChangeCapable sourceBean,
                                                    IAccessorFactoryProvider accessorFactoryProvider,
                                                    String sourceProperty, Method... forwardedMethod) {
    registerNotificationForwarding(sourceBean, sourceBean, accessorFactoryProvider, sourceProperty, forwardedMethod);
  }

  private static void registerNotificationForwarding(IPropertyChangeCapable sourceBean,
                                                     IPropertyChangeCapable targetBean,
                                                     IAccessorFactoryProvider accessorFactoryProvider,
                                                     String sourceProperty, Method... forwardedMethod) {
    if (sourceBean == null) {
      return;
    }
    sourceBean.addPropertyChangeListener(sourceProperty, new ForwardingPropertyChangeListener(targetBean,
        accessorFactoryProvider, forwardedMethod));
  }

  /**
   * Registers notification forwarding from a collection's child property.
   *
   * @param sourceBean
   *     the source bean.
   * @param accessorFactoryProvider
   *     the accessor factory
   * @param sourceCollectionProperty
   *     the collection property to listen to.
   * @param sourceElementProperty
   *     the collection elements property to listen to.
   * @param forwardedProperty
   *     the name of the forwarded property.
   */
  public static void registerNotificationCollectionForwarding(IPropertyChangeCapable sourceBean,
                                                              IAccessorFactoryProvider accessorFactoryProvider,
                                                              String sourceCollectionProperty,
                                                              String sourceElementProperty, String forwardedProperty) {
    registerNotificationCollectionForwarding(sourceBean, accessorFactoryProvider, sourceCollectionProperty,
        sourceElementProperty, new String[]{forwardedProperty});
  }

  /**
   * Registers notification forwarding from a collection's child property.
   *
   * @param sourceBean
   *     the source bean.
   * @param accessorFactoryProvider
   *     the accessor factory
   * @param sourceCollectionProperty
   *     the collection property to listen to.
   * @param sourceElementProperty
   *     the collection elements property to listen to.
   * @param forwardedMethod
   *     the name of the forwarded method.
   */
  public static void registerNotificationCollectionForwarding(IPropertyChangeCapable sourceBean,
                                                              IAccessorFactoryProvider accessorFactoryProvider,
                                                              String sourceCollectionProperty,
                                                              String sourceElementProperty, Method forwardedMethod) {
    registerNotificationCollectionForwarding(sourceBean, accessorFactoryProvider, sourceCollectionProperty,
        sourceElementProperty, new Method[]{forwardedMethod});
  }

  /**
   * Registers notification forwarding from a collection's child property.
   *
   * @param sourceBean
   *     the source bean.
   * @param accessorFactoryProvider
   *     the accessor factory
   * @param sourceCollectionProperty
   *     the collection property to listen to.
   * @param sourceElementProperty
   *     the collection elements property to listen to.
   * @param forwardedProperty
   *     the name of the forwarded property.
   */
  @SuppressWarnings("unchecked")
  public static void registerNotificationCollectionForwarding(final IPropertyChangeCapable sourceBean,
                                                              final IAccessorFactoryProvider accessorFactoryProvider,
                                                              final String sourceCollectionProperty,
                                                              final String sourceElementProperty,
                                                              final String... forwardedProperty) {
    registerNotificationCollectionForwarding(sourceBean, accessorFactoryProvider, sourceCollectionProperty,
        sourceElementProperty, forwardedProperty, null);
  }

  /**
   * Registers notification forwarding from a collection's child property.
   *
   * @param sourceBean
   *     the source bean.
   * @param accessorFactoryProvider
   *     the accessor factory
   * @param sourceCollectionProperty
   *     the collection property to listen to.
   * @param sourceElementProperty
   *     the collection elements property to listen to.
   * @param forwardedMethod
   *     the name of the forwarded method.
   */
  @SuppressWarnings("unchecked")
  public static void registerNotificationCollectionForwarding(final IPropertyChangeCapable sourceBean,
                                                              final IAccessorFactoryProvider accessorFactoryProvider,
                                                              final String sourceCollectionProperty,
                                                              final String sourceElementProperty,
                                                              final Method... forwardedMethod) {
    registerNotificationCollectionForwarding(sourceBean, accessorFactoryProvider, sourceCollectionProperty,
        sourceElementProperty, null, forwardedMethod);
  }

  /**
   * Registers notification forwarding from a collection's child property.
   *
   * @param sourceBean
   *     the source bean.
   * @param accessorFactoryProvider
   *     the accessor factory
   * @param sourceCollectionProperty
   *     the collection property to listen to.
   * @param sourceElementProperty
   *     the collection elements property to listen to.
   * @param forwardedProperties
   *     the name of the forwarded properties.
   * @param forwardedMethods
   *     the forwarded methods
   */
  @SuppressWarnings({"unchecked", "MethodCanBeVariableArityMethod"})
  public static void registerNotificationCollectionForwarding(final IPropertyChangeCapable sourceBean,
                                                              final IAccessorFactoryProvider accessorFactoryProvider,
                                                              final String sourceCollectionProperty,
                                                              final String sourceElementProperty,
                                                              final String[] forwardedProperties,
                                                              final Method[] forwardedMethods) {
    if (sourceBean == null) {
      return;
    }
    // listen normally to collection changes
    if (forwardedProperties != null) {
      registerNotificationForwarding(sourceBean, accessorFactoryProvider, sourceCollectionProperty,
          forwardedProperties);
    }
    if (forwardedMethods != null) {
      registerNotificationForwarding(sourceBean, accessorFactoryProvider, sourceCollectionProperty, forwardedMethods);
    }

    // setup collection listener to attach / detach property change listeners on
    // elements
    sourceBean.addPropertyChangeListener(sourceCollectionProperty, new PropertyChangeListener() {

      @SuppressWarnings({"rawtypes", "unchecked", "SuspiciousMethodCalls"})
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        // add listeners
        if (evt.getNewValue() != null && evt.getNewValue() instanceof Collection<?> && Hibernate.isInitialized(
            evt.getNewValue())) {
          Collection<IPropertyChangeCapable> newChildren = new HashSet<>(
              (Collection<IPropertyChangeCapable>) evt.getNewValue());
          if (evt.getOldValue() != null && evt.getOldValue() instanceof Collection<?> && Hibernate.isInitialized(
              evt.getOldValue())) {
            newChildren.removeAll((Collection<?>) evt.getOldValue());
          }
          for (IPropertyChangeCapable child : newChildren) {
            if (child != null) {
              if (forwardedProperties != null) {
                registerNotificationForwarding(child, sourceBean, accessorFactoryProvider, sourceElementProperty,
                    forwardedProperties);
              }
              if (forwardedMethods != null) {
                registerNotificationForwarding(child, sourceBean, accessorFactoryProvider, sourceElementProperty,
                    forwardedMethods);
              }
            }
          }
        }
        // remove listeners
        if (evt.getOldValue() != null && evt.getOldValue() instanceof Collection<?> && Hibernate.isInitialized(
            evt.getOldValue())) {
          Collection<IPropertyChangeCapable> removedChildren = new HashSet<>(
              (Collection<IPropertyChangeCapable>) evt.getOldValue());

          if (evt.getNewValue() != null && evt.getNewValue() instanceof Collection<?> && Hibernate.isInitialized(
              evt.getNewValue())) {
            removedChildren.removeAll((Collection<?>) evt.getNewValue());
          }
          for (IPropertyChangeCapable child : removedChildren) {
            if (child != null) {
              for (PropertyChangeListener listener : child.getPropertyChangeListeners(sourceElementProperty)) {
                if (listener instanceof DependsOnHelper.ForwardingPropertyChangeListener) {
                  if (Arrays.equals(
                      ((DependsOnHelper.ForwardingPropertyChangeListener) listener).getForwardedProperties(),
                      forwardedProperties) || Arrays.equals(
                      ((DependsOnHelper.ForwardingPropertyChangeListener) listener).getForwardedMethods(),
                      forwardedMethods)) {
                    child.removePropertyChangeListener(sourceElementProperty, listener);
                  }
                }
              }
            }
          }
        }
      }
    });

    // Setup listener for initial collection if it exists
    Collection<IPropertyChangeCapable> initialChildren;
    if (sourceBean instanceof IComponent) {
      initialChildren = (Collection<IPropertyChangeCapable>) ((IComponent) sourceBean).straightGetProperty(
          sourceCollectionProperty);
    } else {
      try {
        initialChildren = new BeanAccessorFactory().createPropertyAccessor(sourceCollectionProperty,
            sourceBean.getClass()).getValue(sourceBean);
      } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
        throw new NestedRuntimeException(ex);
      }
    }
    if (initialChildren != null && Hibernate.isInitialized(initialChildren)) {
      for (IPropertyChangeCapable child : initialChildren) {
        if (child != null) {
          registerNotificationForwarding(child, sourceBean, accessorFactoryProvider, sourceElementProperty,
              forwardedProperties);
        }
      }
    }
  }

  private static class ForwardingPropertyChangeListener implements PropertyChangeListener {

    private IPropertyChangeCapable   targetBean;
    private String[]                 forwardedProperties;
    private Method[]                 forwardedMethods;
    private IAccessorFactoryProvider accessorFactoryProvider;

    /**
     * Constructs a new {@code ForwardingPropertyChangeListener} instance.
     *
     * @param targetBean
     *     the target bean
     * @param accessorFactoryProvider
     *     the accessor factory
     * @param forwardedProperties
     *     the list of forwarded property names.
     */
    public ForwardingPropertyChangeListener(IPropertyChangeCapable targetBean,
                                            IAccessorFactoryProvider accessorFactoryProvider,
                                            String... forwardedProperties) {
      this.targetBean = targetBean;
      this.accessorFactoryProvider = accessorFactoryProvider;
      this.forwardedProperties = forwardedProperties;
    }

    /**
     * Constructs a new {@code ForwardingPropertyChangeListener} instance.
     *
     * @param targetBean
     *     the target bean
     * @param accessorFactoryProvider
     *     the accessor factory
     * @param forwardedMethods
     *     the list of forwarded methods.
     */
    public ForwardingPropertyChangeListener(IPropertyChangeCapable targetBean,
                                            IAccessorFactoryProvider accessorFactoryProvider,
                                            Method... forwardedMethods) {
      this.targetBean = targetBean;
      this.accessorFactoryProvider = accessorFactoryProvider;
      this.forwardedMethods = forwardedMethods;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      if (forwardedProperties != null) {
        if (accessorFactoryProvider != null) {
          for (String prop : forwardedProperties) {
            if (targetBean.hasListeners(prop)) {
              Class<?> targetBeanComponentContract;
              if (targetBean instanceof IComponent) {
                targetBeanComponentContract = ((IComponent) targetBean).getComponentContract();
              } else {
                targetBeanComponentContract = targetBean.getClass();
              }
              try {
                IAccessorFactory accessorFactory = accessorFactoryProvider.getAccessorFactory();
                Object newValue = accessorFactory.createPropertyAccessor(prop, targetBeanComponentContract).getValue(
                    targetBean);
                targetBean.firePropertyChange(prop, IPropertyChangeCapable.UNKNOWN, newValue);
              } catch (IllegalAccessException | NoSuchMethodException ex) {
                throw new NestedRuntimeException(ex);
              } catch (InvocationTargetException ex) {
                if (ex.getTargetException() instanceof RuntimeException) {
                  throw (RuntimeException) ex.getTargetException();
                }
                throw new NestedRuntimeException(ex);
              }
            }
          }
        }
      }
      if (forwardedMethods != null) {
        for (Method method : forwardedMethods) {
          try {
            method.invoke(targetBean);
          } catch (IllegalAccessException ex) {
            throw new NestedRuntimeException(ex);
          } catch (InvocationTargetException ex) {
            if (ex.getTargetException() instanceof RuntimeException) {
              throw (RuntimeException) ex.getTargetException();
            }
            throw new NestedRuntimeException(ex);
          }
        }
      }
    }

    /**
     * Gets the forwardedProperties.
     *
     * @return the forwardedProperties.
     */
    public String[] getForwardedProperties() {
      return forwardedProperties;
    }

    /**
     * Get forwarded methods method [ ].
     *
     * @return the method [ ]
     */
    public Method[] getForwardedMethods() {
      return forwardedMethods;
    }
  }
}
