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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
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

  private PropertyChangeListener propertyChangeListener;

  /**
   * Instantiates a new Depends on helper.
   */
  public DependsOnHelper() {
    this(null);
  }

  /**
   * Instantiates a new Depends on helper.
   *
   * @param propertyChangeListener
   *     the property change listener
   */
  public DependsOnHelper(PropertyChangeListener propertyChangeListener) {
    this.propertyChangeListener = propertyChangeListener;
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
  public void registerDependsOnListeners(Class<?> annotatedClass, IPropertyChangeCapable sourceBean,
                                         IAccessorFactoryProvider accessorFactoryProvider) {
    Method[] methods = annotatedClass.getMethods();
    for (Method method : methods) {
      registerDependsOnListeners(method, sourceBean, accessorFactoryProvider);
    }
  }

  /**
   * Unregister depends on listeners.
   *
   * @param annotatedClass
   *     the annotated class
   * @param sourceBean
   *     the source bean
   * @param accessorFactoryProvider
   *     the accessor factory provider
   */
  public void unregisterDependsOnListeners(Class<?> annotatedClass, IPropertyChangeCapable sourceBean,
                                           IAccessorFactoryProvider accessorFactoryProvider) {
    Method[] methods = annotatedClass.getMethods();
    for (Method method : methods) {
      unregisterDependsOnListeners(method, sourceBean, accessorFactoryProvider);
    }
  }

  /**
   * Register depends on listeners.
   *
   * @param method
   *     the method
   * @param sourceBean
   *     the source bean
   * @param accessorFactoryProvider
   *     the accessor factory provider
   */
  public void registerDependsOnListeners(Method method, IPropertyChangeCapable sourceBean,
                                         IAccessorFactoryProvider accessorFactoryProvider) {
    DependsOn dependsOn = method.getAnnotation(DependsOn.class);
    if (dependsOn != null) {
      registerDependsOnListeners(dependsOn, sourceBean, method, accessorFactoryProvider);
    }
    DependsOnGroup dependsOnGroup = method.getAnnotation(DependsOnGroup.class);
    if (dependsOnGroup != null) {
      for (DependsOn groupedDependsOn : dependsOnGroup.value()) {
        registerDependsOnListeners(groupedDependsOn, sourceBean, method, accessorFactoryProvider);
      }
    }
  }

  /**
   * Unregister depends on listeners.
   *
   * @param method
   *     the method
   * @param sourceBean
   *     the source bean
   * @param accessorFactoryProvider
   *     the accessor factory provider
   */
  public void unregisterDependsOnListeners(Method method, IPropertyChangeCapable sourceBean,
                                           IAccessorFactoryProvider accessorFactoryProvider) {
    DependsOn dependsOn = method.getAnnotation(DependsOn.class);
    if (dependsOn != null) {
      unregisterDependsOnListeners(dependsOn, sourceBean, method, accessorFactoryProvider);
    }
    DependsOnGroup dependsOnGroup = method.getAnnotation(DependsOnGroup.class);
    if (dependsOnGroup != null) {
      for (DependsOn groupedDependsOn : dependsOnGroup.value()) {
        unregisterDependsOnListeners(groupedDependsOn, sourceBean, method, accessorFactoryProvider);
      }
    }
  }

  private void registerDependsOnListeners(DependsOn dependsOn, IPropertyChangeCapable sourceBean, Method method,
                                          IAccessorFactoryProvider accessorFactoryProvider) {
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
    } else if (method.getParameterTypes().length == 0 || propertyChangeListener != null) {
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

  private void unregisterDependsOnListeners(DependsOn dependsOn, IPropertyChangeCapable sourceBean, Method method,
                                            IAccessorFactoryProvider accessorFactoryProvider) {
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
            unregisterNotificationCollectionForwarding(sourceBean, accessorFactoryProvider, sourceCollectionProperty,
                sourceProperty, targetProperty);
          }
        } else {
          for (String sourceProperty : sourceProperties) {
            unregisterNotificationForwarding(sourceBean, accessorFactoryProvider, sourceProperty, targetProperty);
          }
        }
      }
    } else if (method.getParameterTypes().length == 0 || propertyChangeListener != null) {
      if (sourceProperties != null && sourceProperties.length > 0) {
        String sourceCollectionProperty = dependsOn.sourceCollection();
        if (sourceCollectionProperty != null && sourceCollectionProperty.length() > 0) {
          for (String sourceProperty : sourceProperties) {
            unregisterNotificationCollectionForwarding(sourceBean, accessorFactoryProvider, sourceCollectionProperty,
                sourceProperty, method);
          }
        } else {
          for (String sourceProperty : sourceProperties) {
            unregisterNotificationForwarding(sourceBean, accessorFactoryProvider, sourceProperty, method);
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
  public void registerNotificationForwarding(IPropertyChangeCapable sourceBean,
                                             IAccessorFactoryProvider accessorFactoryProvider, String sourceProperty,
                                             String forwardedProperty) {
    registerNotificationForwarding(sourceBean, accessorFactoryProvider, sourceProperty,
        new String[]{forwardedProperty});
  }

  /**
   * Unregister notification forwarding.
   *
   * @param sourceBean
   *     the source bean
   * @param accessorFactoryProvider
   *     the accessor factory provider
   * @param sourceProperty
   *     the source property
   * @param forwardedProperty
   *     the forwarded property
   */
  public void unregisterNotificationForwarding(IPropertyChangeCapable sourceBean,
                                               IAccessorFactoryProvider accessorFactoryProvider, String sourceProperty,
                                               String forwardedProperty) {
    unregisterNotificationForwarding(sourceBean, accessorFactoryProvider, sourceProperty,
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
  public void registerNotificationForwarding(IPropertyChangeCapable sourceBean,
                                             IAccessorFactoryProvider accessorFactoryProvider, String sourceProperty,
                                             Method forwardedMethod) {
    registerNotificationForwarding(sourceBean, accessorFactoryProvider, sourceProperty, new Method[]{forwardedMethod});
  }

  /**
   * Uregister notification forwarding.
   *
   * @param sourceBean
   *     the source bean
   * @param accessorFactoryProvider
   *     the accessor factory provider
   * @param sourceProperty
   *     the source property
   * @param forwardedMethod
   *     the forwarded method
   */
  public void uregisterNotificationForwarding(IPropertyChangeCapable sourceBean,
                                              IAccessorFactoryProvider accessorFactoryProvider, String sourceProperty,
                                              Method forwardedMethod) {
    unregisterNotificationForwarding(sourceBean, accessorFactoryProvider, sourceProperty,
        new Method[]{forwardedMethod});
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
  public void registerNotificationForwarding(IPropertyChangeCapable sourceBean,
                                             IAccessorFactoryProvider accessorFactoryProvider, String sourceProperty,
                                             String... forwardedProperty) {
    registerNotificationForwarding(sourceBean, sourceBean, accessorFactoryProvider, sourceProperty, forwardedProperty);
  }

  /**
   * Unregister notification forwarding.
   *
   * @param sourceBean
   *     the source bean
   * @param accessorFactoryProvider
   *     the accessor factory provider
   * @param sourceProperty
   *     the source property
   * @param forwardedProperty
   *     the forwarded property
   */
  public void unregisterNotificationForwarding(IPropertyChangeCapable sourceBean,
                                               IAccessorFactoryProvider accessorFactoryProvider, String sourceProperty,
                                               String... forwardedProperty) {
    unregisterNotificationForwarding(sourceBean, sourceBean, accessorFactoryProvider, sourceProperty,
        forwardedProperty);
  }

  private void registerNotificationForwarding(IPropertyChangeCapable sourceBean, IPropertyChangeCapable targetBean,
                                              IAccessorFactoryProvider accessorFactoryProvider, String sourceProperty,
                                              String... forwardedProperty) {
    if (sourceBean == null) {
      return;
    }
    if (sourceBean == targetBean && Arrays.binarySearch(forwardedProperty, sourceProperty) >= 0) {
      throw new IllegalArgumentException(
          "Forwarded properties " + Arrays.asList(forwardedProperty) + " cannot contain source property "
              + sourceProperty + " when registering notification forwarding");
    }
    sourceBean.addPropertyChangeListener(sourceProperty,
        createOrGetPropertyChangeListener(targetBean, accessorFactoryProvider, forwardedProperty));
  }

  private void unregisterNotificationForwarding(IPropertyChangeCapable sourceBean, IPropertyChangeCapable targetBean,
                                                IAccessorFactoryProvider accessorFactoryProvider, String sourceProperty,
                                                String... forwardedProperty) {
    if (sourceBean == null) {
      return;
    }
    if (sourceBean == targetBean && Arrays.binarySearch(forwardedProperty, sourceProperty) >= 0) {
      throw new IllegalArgumentException(
          "Forwarded properties " + Arrays.asList(forwardedProperty) + " cannot contain source property "
              + sourceProperty + " when registering notification forwarding");
    }
    sourceBean.removePropertyChangeListener(sourceProperty,
        createOrGetPropertyChangeListener(targetBean, accessorFactoryProvider, forwardedProperty));
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
  public void registerNotificationForwarding(IPropertyChangeCapable sourceBean,
                                             IAccessorFactoryProvider accessorFactoryProvider, String sourceProperty,
                                             Method... forwardedMethod) {
    registerNotificationForwarding(sourceBean, sourceBean, accessorFactoryProvider, sourceProperty, forwardedMethod);
  }

  /**
   * Unregister notification forwarding.
   *
   * @param sourceBean
   *     the source bean
   * @param accessorFactoryProvider
   *     the accessor factory provider
   * @param sourceProperty
   *     the source property
   * @param forwardedMethod
   *     the forwarded method
   */
  public void unregisterNotificationForwarding(IPropertyChangeCapable sourceBean,
                                               IAccessorFactoryProvider accessorFactoryProvider, String sourceProperty,
                                               Method... forwardedMethod) {
    unregisterNotificationForwarding(sourceBean, sourceBean, accessorFactoryProvider, sourceProperty, forwardedMethod);
  }

  private void registerNotificationForwarding(IPropertyChangeCapable sourceBean, IPropertyChangeCapable targetBean,
                                              IAccessorFactoryProvider accessorFactoryProvider, String sourceProperty,
                                              Method... forwardedMethod) {
    if (sourceBean == null) {
      return;
    }
    sourceBean.addPropertyChangeListener(sourceProperty,
        createOrGetPropertyChangeListener(targetBean, accessorFactoryProvider, forwardedMethod));
  }

  private void unregisterNotificationForwarding(IPropertyChangeCapable sourceBean, IPropertyChangeCapable targetBean,
                                                IAccessorFactoryProvider accessorFactoryProvider, String sourceProperty,
                                                Method... forwardedMethod) {
    if (sourceBean == null) {
      return;
    }
    sourceBean.removePropertyChangeListener(sourceProperty,
        createOrGetPropertyChangeListener(targetBean, accessorFactoryProvider, forwardedMethod));
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
  public void registerNotificationCollectionForwarding(IPropertyChangeCapable sourceBean,
                                                       IAccessorFactoryProvider accessorFactoryProvider,
                                                       String sourceCollectionProperty, String sourceElementProperty,
                                                       String forwardedProperty) {
    registerNotificationCollectionForwarding(sourceBean, accessorFactoryProvider, sourceCollectionProperty,
        sourceElementProperty, new String[]{forwardedProperty});
  }

  /**
   * Unregister notification collection forwarding.
   *
   * @param sourceBean
   *     the source bean
   * @param accessorFactoryProvider
   *     the accessor factory provider
   * @param sourceCollectionProperty
   *     the source collection property
   * @param sourceElementProperty
   *     the source element property
   * @param forwardedProperty
   *     the forwarded property
   */
  public void unregisterNotificationCollectionForwarding(IPropertyChangeCapable sourceBean,
                                                         IAccessorFactoryProvider accessorFactoryProvider,
                                                         String sourceCollectionProperty, String sourceElementProperty,
                                                         String forwardedProperty) {
    unregisterNotificationCollectionForwarding(sourceBean, accessorFactoryProvider, sourceCollectionProperty,
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
  public void registerNotificationCollectionForwarding(IPropertyChangeCapable sourceBean,
                                                       IAccessorFactoryProvider accessorFactoryProvider,
                                                       String sourceCollectionProperty, String sourceElementProperty,
                                                       Method forwardedMethod) {
    registerNotificationCollectionForwarding(sourceBean, accessorFactoryProvider, sourceCollectionProperty,
        sourceElementProperty, new Method[]{forwardedMethod});
  }

  /**
   * Unregister notification collection forwarding.
   *
   * @param sourceBean
   *     the source bean
   * @param accessorFactoryProvider
   *     the accessor factory provider
   * @param sourceCollectionProperty
   *     the source collection property
   * @param sourceElementProperty
   *     the source element property
   * @param forwardedMethod
   *     the forwarded method
   */
  public void unregisterNotificationCollectionForwarding(IPropertyChangeCapable sourceBean,
                                                         IAccessorFactoryProvider accessorFactoryProvider,
                                                         String sourceCollectionProperty, String sourceElementProperty,
                                                         Method forwardedMethod) {
    unregisterNotificationCollectionForwarding(sourceBean, accessorFactoryProvider, sourceCollectionProperty,
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
  public void registerNotificationCollectionForwarding(final IPropertyChangeCapable sourceBean,
                                                       final IAccessorFactoryProvider accessorFactoryProvider,
                                                       final String sourceCollectionProperty,
                                                       final String sourceElementProperty,
                                                       final String... forwardedProperty) {
    registerNotificationCollectionForwarding(sourceBean, accessorFactoryProvider, sourceCollectionProperty,
        sourceElementProperty, forwardedProperty, null);
  }

  /**
   * Unregister notification collection forwarding.
   *
   * @param sourceBean
   *     the source bean
   * @param accessorFactoryProvider
   *     the accessor factory provider
   * @param sourceCollectionProperty
   *     the source collection property
   * @param sourceElementProperty
   *     the source element property
   * @param forwardedProperty
   *     the forwarded property
   */
  public void unregisterNotificationCollectionForwarding(final IPropertyChangeCapable sourceBean,
                                                         final IAccessorFactoryProvider accessorFactoryProvider,
                                                         final String sourceCollectionProperty,
                                                         final String sourceElementProperty,
                                                         final String... forwardedProperty) {
    unregisterNotificationCollectionForwarding(sourceBean, accessorFactoryProvider, sourceCollectionProperty,
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
  public void registerNotificationCollectionForwarding(final IPropertyChangeCapable sourceBean,
                                                       final IAccessorFactoryProvider accessorFactoryProvider,
                                                       final String sourceCollectionProperty,
                                                       final String sourceElementProperty,
                                                       final Method... forwardedMethod) {
    registerNotificationCollectionForwarding(sourceBean, accessorFactoryProvider, sourceCollectionProperty,
        sourceElementProperty, null, forwardedMethod);
  }

  /**
   * Unregister notification collection forwarding.
   *
   * @param sourceBean
   *     the source bean
   * @param accessorFactoryProvider
   *     the accessor factory provider
   * @param sourceCollectionProperty
   *     the source collection property
   * @param sourceElementProperty
   *     the source element property
   * @param forwardedMethod
   *     the forwarded method
   */
  public void unregisterNotificationCollectionForwarding(final IPropertyChangeCapable sourceBean,
                                                         final IAccessorFactoryProvider accessorFactoryProvider,
                                                         final String sourceCollectionProperty,
                                                         final String sourceElementProperty,
                                                         final Method... forwardedMethod) {
    unregisterNotificationCollectionForwarding(sourceBean, accessorFactoryProvider, sourceCollectionProperty,
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
  public void registerNotificationCollectionForwarding(final IPropertyChangeCapable sourceBean,
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
    sourceBean.addPropertyChangeListener(sourceCollectionProperty,
        new CollectionPropertyChangeListener(forwardedProperties, sourceBean, accessorFactoryProvider,
            sourceElementProperty, forwardedMethods));

    // Setup listener for initial collection if it exists
    Collection<IPropertyChangeCapable> initialChildren;
    initialChildren = (Collection<IPropertyChangeCapable>) retrieveInitialPropertyValue(sourceBean,
        sourceCollectionProperty);
    if (initialChildren != null && Hibernate.isInitialized(initialChildren)) {
      for (IPropertyChangeCapable child : initialChildren) {
        if (child != null) {
          registerNotificationForwarding(child, sourceBean, accessorFactoryProvider, sourceElementProperty,
              forwardedProperties);
        }
      }
    }
  }

  /**
   * Unregister notification collection forwarding.
   *
   * @param sourceBean
   *     the source bean
   * @param accessorFactoryProvider
   *     the accessor factory provider
   * @param sourceCollectionProperty
   *     the source collection property
   * @param sourceElementProperty
   *     the source element property
   * @param forwardedProperties
   *     the forwarded properties
   * @param forwardedMethods
   *     the forwarded methods
   */
  public void unregisterNotificationCollectionForwarding(final IPropertyChangeCapable sourceBean,
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
      unregisterNotificationForwarding(sourceBean, accessorFactoryProvider, sourceCollectionProperty,
          forwardedProperties);
    }
    if (forwardedMethods != null) {
      unregisterNotificationForwarding(sourceBean, accessorFactoryProvider, sourceCollectionProperty, forwardedMethods);
    }

    // setup collection listener to attach / detach property change listeners on
    // elements
    sourceBean.removePropertyChangeListener(sourceCollectionProperty,
        new CollectionPropertyChangeListener(forwardedProperties, sourceBean, accessorFactoryProvider,
            sourceElementProperty, forwardedMethods));

    // Remove listener for current collection if it exists
    Collection<IPropertyChangeCapable> initialChildren;
    initialChildren = (Collection<IPropertyChangeCapable>) retrieveInitialPropertyValue(sourceBean,
        sourceCollectionProperty);
    if (initialChildren != null && Hibernate.isInitialized(initialChildren)) {
      for (IPropertyChangeCapable child : initialChildren) {
        if (child != null) {
          unregisterNotificationForwarding(child, sourceBean, accessorFactoryProvider, sourceElementProperty,
              forwardedProperties);
        }
      }
    }
  }

  /**
   * Retrieve initial property value object.
   *
   * @param sourceBean
   *     the source bean
   * @param sourceProperty
   *     the source property
   * @return the object
   */
  protected Object retrieveInitialPropertyValue(Object sourceBean, String sourceProperty) {
    int dotIndex = sourceProperty.lastIndexOf(".");
    if (dotIndex > 0) {
      String rootProperty = sourceProperty.substring(0, dotIndex);
      String remainderProperty = sourceProperty.substring(dotIndex + 1);
      Object root = retrieveInitialPropertyValue(sourceBean, rootProperty);
      if (root != null && Hibernate.isInitialized(root)) {
        return retrieveInitialPropertyValue(root, remainderProperty);
      } else {
        return null;
      }
    } else {
      Object initialValue;
      if (sourceBean instanceof IComponent) {
        initialValue = ((IComponent) sourceBean).straightGetProperty(sourceProperty);
      } else {
        try {
          initialValue = new BeanAccessorFactory().createPropertyAccessor(sourceProperty, sourceBean.getClass())
                                                  .getValue(sourceBean);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
          throw new NestedRuntimeException(ex);
        }
      }
      return initialValue;
    }
  }

  private PropertyChangeListener createOrGetPropertyChangeListener(IPropertyChangeCapable targetBean,
                                                                   IAccessorFactoryProvider accessorFactoryProvider,
                                                                   String... forwardedProperties) {
    if (propertyChangeListener != null) {
      return propertyChangeListener;
    }
    return new ForwardingPropertyChangeListener(targetBean, accessorFactoryProvider, forwardedProperties);
  }

  private PropertyChangeListener createOrGetPropertyChangeListener(IPropertyChangeCapable targetBean,
                                                                   IAccessorFactoryProvider accessorFactoryProvider,
                                                                   Method... forwardedMethods) {
    if (propertyChangeListener != null) {
      return propertyChangeListener;
    }
    return new ForwardingPropertyChangeListener(targetBean, accessorFactoryProvider, forwardedMethods);
  }

  private class ForwardingPropertyChangeListener implements PropertyChangeListener {

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

    /**
     * Equals boolean.
     *
     * @param obj
     *     the obj
     * @return the boolean
     */
    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof ForwardingPropertyChangeListener)) {
        return false;
      }
      if (this == obj) {
        return true;
      }
      ForwardingPropertyChangeListener rhs = (ForwardingPropertyChangeListener) obj;
      EqualsBuilder equalsBuilder = new EqualsBuilder();
      equalsBuilder.append(targetBean, rhs.targetBean);
      if (forwardedProperties != null) {
        equalsBuilder.append(forwardedProperties, rhs.forwardedProperties);
      }
      if (forwardedMethods != null) {
        equalsBuilder.append(forwardedMethods, rhs.forwardedMethods);
      }
      return equalsBuilder.isEquals();
    }

    /**
     * Hash code int.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
      HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
      hashCodeBuilder.append(targetBean);
      if (forwardedProperties != null) {
        hashCodeBuilder.append(forwardedProperties);
      }
      if (forwardedMethods != null) {
        hashCodeBuilder.append(forwardedMethods);
      }
      return hashCodeBuilder.toHashCode();
    }

    /**
     * Property change.
     *
     * @param evt
     *     the evt
     */
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

  private class CollectionPropertyChangeListener implements PropertyChangeListener {

    private final String[]                 forwardedProperties;
    private final IPropertyChangeCapable   sourceBean;
    private final IAccessorFactoryProvider accessorFactoryProvider;
    private final String                   sourceElementProperty;
    private final Method[]                 forwardedMethods;

    /**
     * Instantiates a new Collection property change listener.
     *
     * @param forwardedProperties
     *     the forwarded properties
     * @param sourceBean
     *     the source bean
     * @param accessorFactoryProvider
     *     the accessor factory provider
     * @param sourceElementProperty
     *     the source element property
     * @param forwardedMethods
     *     the forwarded methods
     */
    public CollectionPropertyChangeListener(String[] forwardedProperties, IPropertyChangeCapable sourceBean,
                                            IAccessorFactoryProvider accessorFactoryProvider,
                                            String sourceElementProperty, Method[] forwardedMethods) {
      this.forwardedProperties = forwardedProperties;
      this.sourceBean = sourceBean;
      this.accessorFactoryProvider = accessorFactoryProvider;
      this.sourceElementProperty = sourceElementProperty;
      this.forwardedMethods = forwardedMethods;
    }

    /**
     * Equals boolean.
     *
     * @param obj
     *     the obj
     * @return the boolean
     */
    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof CollectionPropertyChangeListener)) {
        return false;
      }
      if (this == obj) {
        return true;
      }
      CollectionPropertyChangeListener rhs = (CollectionPropertyChangeListener) obj;
      EqualsBuilder equalsBuilder = new EqualsBuilder();
      equalsBuilder.append(sourceBean, rhs.sourceBean);
      equalsBuilder.append(sourceElementProperty, rhs.sourceElementProperty);
      if (forwardedProperties != null) {
        equalsBuilder.append(forwardedProperties, rhs.forwardedProperties);
      }
      if (forwardedMethods != null) {
        equalsBuilder.append(forwardedMethods, rhs.forwardedMethods);
      }
      return equalsBuilder.isEquals();
    }

    /**
     * Hash code int.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
      HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
      hashCodeBuilder.append(sourceBean);
      hashCodeBuilder.append(sourceElementProperty);
      if (forwardedProperties != null) {
        hashCodeBuilder.append(forwardedProperties);
      }
      if (forwardedMethods != null) {
        hashCodeBuilder.append(forwardedMethods);
      }
      return hashCodeBuilder.toHashCode();
    }

    /**
     * Property change.
     *
     * @param evt
     *     the evt
     */
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
              if (listener instanceof ForwardingPropertyChangeListener) {
                if (Arrays.equals(((ForwardingPropertyChangeListener) listener).getForwardedProperties(),
                    forwardedProperties) || Arrays.equals(
                    ((ForwardingPropertyChangeListener) listener).getForwardedMethods(), forwardedMethods)) {
                  child.removePropertyChangeListener(sourceElementProperty, listener);
                }
              }
            }
          }
        }
      }
    }
  }
}
