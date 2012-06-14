/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.model.component.basic;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.MethodUtils;
import org.jspresso.framework.model.component.ComponentException;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IComponentCollectionFactory;
import org.jspresso.framework.model.component.IComponentExtension;
import org.jspresso.framework.model.component.IComponentExtensionFactory;
import org.jspresso.framework.model.component.IComponentFactory;
import org.jspresso.framework.model.component.IComponentFactoryAware;
import org.jspresso.framework.model.component.ILifecycleCapable;
import org.jspresso.framework.model.component.service.IComponentService;
import org.jspresso.framework.model.component.service.ILifecycleInterceptor;
import org.jspresso.framework.model.descriptor.IBooleanPropertyDescriptor;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IModelDescriptorAware;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.IRelationshipEndPropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.model.entity.IEntityLifecycleHandler;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.accessor.ICollectionAccessor;
import org.jspresso.framework.util.bean.AccessorInfo;
import org.jspresso.framework.util.bean.EAccessorType;
import org.jspresso.framework.util.bean.IPropertyChangeCapable;
import org.jspresso.framework.util.bean.SinglePropertyChangeSupport;
import org.jspresso.framework.util.bean.SingleWeakPropertyChangeSupport;
import org.jspresso.framework.util.collection.CollectionHelper;
import org.jspresso.framework.util.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the core implementation of all components in the application.
 * Instances of this class serve as handlers for proxies representing the
 * components.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractComponentInvocationHandler implements
    InvocationHandler, Serializable {

  private static final Logger       LOG              = LoggerFactory
                                                         .getLogger(AbstractComponentInvocationHandler.class);

  private static final long                                                            serialVersionUID = -8332414648339056836L;

  private IAccessorFactory                                                             accessorFactory;
  private SinglePropertyChangeSupport                                                  changeSupport;
  private SingleWeakPropertyChangeSupport                                              weakChangeSupport;
  private IComponentCollectionFactory<IComponent>                                      collectionFactory;
  private IComponentDescriptor<? extends IComponent>                                   componentDescriptor;
  private Map<Class<IComponentExtension<IComponent>>, IComponentExtension<IComponent>> componentExtensions;
  private IComponentExtensionFactory                                                   extensionFactory;
  private IComponentFactory                                                            inlineComponentFactory;
  private Set<String>                                                                  modifierMonitors;
  private boolean                                                                      propertyProcessorsEnabled;

  private Map<String, InlineReferenceTracker>                                          referenceTrackers;

  /**
   * Constructs a new <code>BasicComponentInvocationHandler</code> instance.
   * 
   * @param componentDescriptor
   *          The descriptor of the proxy component.
   * @param inlineComponentFactory
   *          the factory used to create inline components.
   * @param collectionFactory
   *          The factory used to create empty component collections from
   *          collection getters.
   * @param accessorFactory
   *          The factory used to access proxy properties.
   * @param extensionFactory
   *          The factory used to create component extensions based on their
   *          classes.
   */
  protected AbstractComponentInvocationHandler(
      IComponentDescriptor<? extends IComponent> componentDescriptor,
      IComponentFactory inlineComponentFactory,
      IComponentCollectionFactory<IComponent> collectionFactory,
      IAccessorFactory accessorFactory,
      IComponentExtensionFactory extensionFactory) {
    this.componentDescriptor = componentDescriptor;
    this.inlineComponentFactory = inlineComponentFactory;
    this.collectionFactory = collectionFactory;
    this.accessorFactory = accessorFactory;
    this.extensionFactory = extensionFactory;
    this.propertyProcessorsEnabled = true;
    this.referenceTrackers = new HashMap<String, InlineReferenceTracker>();
  }

  /**
   * Gets the interface class being the contract of this component.
   * 
   * @return the component interface contract.
   */
  public Class<? extends Object> getComponentContract() {
    return componentDescriptor.getComponentContract();
  }

  /**
   * Handles methods invocations on the component proxy. Either : <li>delegates
   * to one of its extension if the accessed property is registered as being
   * part of an extension <li>handles property access internally
   * <p>
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public synchronized Object invoke(Object proxy, Method method, Object[] args)
      throws Throwable {
    String methodName = method.getName();
    if ("hashCode".equals(methodName)) {
      return new Integer(computeHashCode((IComponent) proxy));
    } else if ("equals".equals(methodName)) {
      return new Boolean(computeEquals((IComponent) proxy, args[0]));
    } else if ("toString".equals(methodName)) {
      return toString(proxy);
    } else if ("getComponentContract".equals(methodName)) {
      return componentDescriptor.getComponentContract();
    } else if ("addPropertyChangeListener".equals(methodName)) {
      if (args.length == 1) {
        addPropertyChangeListener(proxy, (PropertyChangeListener) args[0]);
        return null;
      }
      addPropertyChangeListener(proxy, (String) args[0],
          (PropertyChangeListener) args[1]);
      return null;
    } else if ("addWeakPropertyChangeListener".equals(methodName)) {
      if (args.length == 1) {
        addWeakPropertyChangeListener(proxy, (PropertyChangeListener) args[0]);
        return null;
      }
      addWeakPropertyChangeListener(proxy, (String) args[0],
          (PropertyChangeListener) args[1]);
      return null;
    } else if ("removePropertyChangeListener".equals(methodName)) {
      if (args.length == 1) {
        removePropertyChangeListener((PropertyChangeListener) args[0]);
        return null;
      }
      removePropertyChangeListener((String) args[0],
          (PropertyChangeListener) args[1]);
      return null;
    } else if ("firePropertyChange".equals(methodName)) {
      firePropertyChange(proxy, (String) args[0], args[1], args[2]);
      return null;
    } else if ("straightSetProperty".equals(methodName)) {
      straightSetProperty(proxy, (String) args[0], args[1]);
      return null;
    } else if ("straightGetProperty".equals(methodName)) {
      return straightGetProperty(proxy, (String) args[0]);
    } else if ("straightSetProperties".equals(methodName)) {
      straightSetProperties(proxy, (Map<String, Object>) args[0]);
      return null;
    } else if ("straightGetProperties".equals(methodName)) {
      return straightGetProperties(proxy);
    } else if ("setPropertyProcessorsEnabled".equals(methodName)) {
      propertyProcessorsEnabled = ((Boolean) args[0]).booleanValue();
      return null;
    } else {
      boolean isLifecycleMethod = false;
      try {
        isLifecycleMethod = ILifecycleCapable.class.getMethod(methodName,
            method.getParameterTypes()) != null;
      } catch (NoSuchMethodException ignored) {
        // this is certainly normal.
      }
      if (isLifecycleMethod) {
        return new Boolean(invokeLifecycleInterceptors(proxy, method, args));
      }
      AccessorInfo accessorInfo = new AccessorInfo(method);
      EAccessorType accessorType = accessorInfo.getAccessorType();
      IPropertyDescriptor propertyDescriptor = null;
      if (accessorType != EAccessorType.NONE) {
        String accessedPropertyName = accessorInfo.getAccessedPropertyName();
        if (accessedPropertyName != null) {
          propertyDescriptor = componentDescriptor
              .getPropertyDescriptor(accessedPropertyName);
        }
      }
      if (propertyDescriptor != null) {
        Class<IComponentExtension<IComponent>> extensionClass = (Class<IComponentExtension<IComponent>>) propertyDescriptor
            .getDelegateClass();
        if (extensionClass != null) {
          IComponentExtension<? extends IComponent> extensionDelegate = getExtensionInstance(
              extensionClass, (IComponent) proxy);
          return invokeExtensionMethod(extensionDelegate, method, args);
        } else if (!propertyDescriptor.isComputed()) {
          if (accessorInfo.isModifier()) {
            if (modifierMonitors != null
                && modifierMonitors.contains(methodName)) {
              return null;
            }
            if (modifierMonitors == null) {
              modifierMonitors = new HashSet<String>();
            }
            modifierMonitors.add(methodName);
          }
          try {
            Object param;
            switch (accessorType) {
              case GETTER:
                return getProperty(proxy, propertyDescriptor);
              case SETTER:
                param = sanitizeModifierParam(proxy, propertyDescriptor,
                    args[0]);
                setProperty(proxy, propertyDescriptor, param);
                return null;
              case ADDER:
                if (args.length == 2) {
                  param = sanitizeModifierParam(proxy, propertyDescriptor,
                      args[1]);
                  addToProperty(proxy,
                      (ICollectionPropertyDescriptor<?>) propertyDescriptor,
                      ((Integer) args[0]).intValue(), param);
                } else {
                  param = sanitizeModifierParam(proxy, propertyDescriptor,
                      args[0]);
                  addToProperty(proxy,
                      (ICollectionPropertyDescriptor<?>) propertyDescriptor,
                      param);
                }
                return null;
              case REMOVER:
                param = sanitizeModifierParam(proxy, propertyDescriptor,
                    args[0]);
                removeFromProperty(proxy,
                    (ICollectionPropertyDescriptor<?>) propertyDescriptor,
                    param);
                return null;
              default:
                break;
            }
          } finally {
            if (modifierMonitors != null && accessorInfo.isModifier()) {
              modifierMonitors.remove(methodName);
            }
          }
        } else {
          try {
            return invokeServiceMethod(proxy, method, args);
          } catch (NoSuchMethodException ignored) {
            // it will fall back in the general case.
          }
          throw new ComponentException(
              "The '"
                  + propertyDescriptor.getName()
                  + "' property is described as computed but we couldn't determine a way to compute it,"
                  + " either through an extension or a service delegate on the following component : \n"
                  + componentDescriptor.getComponentContract().getName());
        }
      } else {
        try {
          return invokeServiceMethod(proxy, method, args);
        } catch (NoSuchMethodException ignored) {
          // it will fall back in the general case.
        }
      }
    }
    throw new ComponentException(method.toString()
        + " is not supported on the component "
        + componentDescriptor.getComponentContract().getName());
  }

  /**
   * Gives chance to subclasses to perform sanity checks and eventually
   * substitute the passed param by an other one when it's technically
   * necessary.
   * 
   * @param target
   *          the target being modified.
   * @param propertyDescriptor
   *          the descriptor of the property being modified.
   * @param param
   *          the modifier parameter.
   * @return the parameter to actually pass to the modifier
   */
  protected Object sanitizeModifierParam(Object target,
      IPropertyDescriptor propertyDescriptor, Object param) {
    return param;
  }

  /**
   * Sets the collectionFactory.
   * 
   * @param collectionFactory
   *          the collectionFactory to set.
   */
  public void setCollectionFactory(
      IComponentCollectionFactory<IComponent> collectionFactory) {
    this.collectionFactory = collectionFactory;
  }

  /**
   * Delegate method to compute object equality.
   * 
   * @param proxy
   *          the target component to compute equality of.
   * @param another
   *          the object to compute equality against.
   * @return the computed equality.
   */
  protected abstract boolean computeEquals(IComponent proxy, Object another);

  /**
   * Delegate method to compute hashcode.
   * 
   * @param proxy
   *          the target component to compute hashcode for.
   * @return the computed hashcode.
   */
  protected abstract int computeHashCode(IComponent proxy);

  /**
   * Gives a chance to configure created extensions.
   * 
   * @param extension
   *          the extension to configure.
   */
  protected void configureExtension(IComponentExtension<IComponent> extension) {
    if (extension instanceof IComponentFactoryAware) {
      ((IComponentFactoryAware) extension)
          .setComponentFactory(getInlineComponentFactory());
    }
  }

  /**
   * Gives a chance to the implementor to decorate a component reference before
   * returning it when fetching association ends.
   * 
   * @param referent
   *          the component reference to decorate.
   * @param referentDescriptor
   *          the component descriptor of the referent.
   * @return the decorated component.
   */
  protected abstract IComponent decorateReferent(IComponent referent,
      IComponentDescriptor<? extends IComponent> referentDescriptor);

  /**
   * An empty hook that gets called whenever an entity is detached from a parent
   * one.
   * 
   * @param parent
   *          the parent entity.
   * @param child
   *          the child entity.
   * @param propertyDescriptor
   *          the property descriptor this entity was detached from.
   */
  protected void entityDetached(IEntity parent, IEntity child,
      IRelationshipEndPropertyDescriptor propertyDescriptor) {
    // defaults to no-op.
  }

  /**
   * Gets the accessorFactory.
   * 
   * @return the accessorFactory.
   */
  protected IAccessorFactory getAccessorFactory() {
    return accessorFactory;
  }

  /**
   * Gets a collection property value.
   * 
   * @param proxy
   *          the proxy to get the property of.
   * @param propertyDescriptor
   *          the property descriptor to get the value for.
   * @return the property value.
   */
  @SuppressWarnings("unchecked")
  protected Object getCollectionProperty(Object proxy,
      ICollectionPropertyDescriptor<? extends IComponent> propertyDescriptor) {
    try {
      Object property = straightGetProperty(proxy, propertyDescriptor.getName());
      if (property == null) {
        property = collectionFactory
            .createComponentCollection(propertyDescriptor
                .getReferencedDescriptor().getCollectionInterface());
        storeProperty(propertyDescriptor.getName(), property);
      }
      if (property instanceof List) {
        List<IComponent> propertyAsList = (List<IComponent>) property;
        for (int i = 0; i < propertyAsList.size(); i++) {
          IComponent referent = propertyAsList.get(i);
          IComponent decorated = decorateReferent(referent, propertyDescriptor
              .getReferencedDescriptor().getElementDescriptor()
              .getComponentDescriptor());
          if (decorated != referent) {
            propertyAsList.set(i, decorated);
          }
          if (referent == null) {
            LOG.warn(
                "A null element was detected in [{}] indexed list on {} at index "
                    + i + ".", propertyDescriptor.getName(), proxy);
            LOG.warn("This might be normal but sometimes it reveals a mis-use of indexed collection property accessors.");
          }
        }
      } else if (property instanceof Set) {
        Set<IComponent> propertyAsSet = (Set<IComponent>) property;
        for (IComponent referent : new HashSet<IComponent>(propertyAsSet)) {
          IComponent decorated = decorateReferent(referent, propertyDescriptor
              .getReferencedDescriptor().getElementDescriptor()
              .getComponentDescriptor());
          if (decorated != referent) {
            propertyAsSet.add(decorated);
          }
        }
      }
      if (isCollectionSortOnReadEnabled()) {
        inlineComponentFactory.sortCollectionProperty((IComponent) proxy,
            propertyDescriptor.getName());
      }
      return property;
    } catch (RuntimeException re) {
      LOG.error("Error when retrieving [{}] collection property on {}",
          propertyDescriptor.getName(), proxy);
      throw (re);
    }
  }

  /**
   * Allow to disable collection property sorting on read.
   * 
   * @return true if collection sorting is enabled on read access.
   */
  protected boolean isCollectionSortOnReadEnabled() {
    return true;
  }

  /**
   * Creates and registers an extension instance.
   * 
   * @param extensionClass
   *          the extension class.
   * @param proxy
   *          the proxy to register the extension on.
   * @return the component extension.
   */
  protected synchronized IComponentExtension<? extends IComponent> getExtensionInstance(
      Class<IComponentExtension<IComponent>> extensionClass, IComponent proxy) {
    IComponentExtension<IComponent> extension;
    if (componentExtensions == null) {
      componentExtensions = new HashMap<Class<IComponentExtension<IComponent>>, IComponentExtension<IComponent>>();
      extension = null;
    } else {
      extension = componentExtensions.get(extensionClass);
    }
    if (extension == null) {
      extension = extensionFactory.createComponentExtension(extensionClass,
          componentDescriptor.getComponentContract(), proxy);
      configureExtension(extension);
      componentExtensions.put(extensionClass, extension);
    }
    return extension;
  }

  /**
   * Gets the inlineComponentFactory.
   * 
   * @return the inlineComponentFactory.
   */
  protected IComponentFactory getInlineComponentFactory() {
    return inlineComponentFactory;
  }

  /**
   * Gets a property value.
   * 
   * @param proxy
   *          the proxy to get the property of.
   * @param propertyDescriptor
   *          the property descriptor to get the value for.
   * @return the property value.
   */
  @SuppressWarnings("unchecked")
  protected Object getProperty(Object proxy,
      IPropertyDescriptor propertyDescriptor) {
    if (propertyDescriptor instanceof ICollectionPropertyDescriptor) {
      return getCollectionProperty(
          proxy,
          (ICollectionPropertyDescriptor<? extends IComponent>) propertyDescriptor);
    } else if (propertyDescriptor instanceof IReferencePropertyDescriptor) {
      return getReferenceProperty(proxy,
          (IReferencePropertyDescriptor<IComponent>) propertyDescriptor);
    }
    Object propertyValue = straightGetProperty(proxy,
        propertyDescriptor.getName());
    return propertyValue;
  }

  /**
   * Gets a reference property value.
   * 
   * @param proxy
   *          the proxy to get the property of.
   * @param propertyDescriptor
   *          the property descriptor to get the value for.
   * @return the property value.
   */
  protected Object getReferenceProperty(Object proxy,
      final IReferencePropertyDescriptor<IComponent> propertyDescriptor) {
    IComponent property = (IComponent) straightGetProperty(proxy,
        propertyDescriptor.getName());
    if (property == null && isInlineComponentReference(propertyDescriptor)) {
      property = inlineComponentFactory
          .createComponentInstance(propertyDescriptor.getReferencedDescriptor()
              .getComponentContract());
      storeReferenceProperty(propertyDescriptor, null, property);
    }
    return decorateReferent(property,
        propertyDescriptor.getReferencedDescriptor());
  }

  /**
   * Invokes a service method on the component.
   * 
   * @param proxy
   *          the component to invoke the service on.
   * @param method
   *          the method implemented by the component.
   * @param args
   *          the arguments of the method implemented by the component.
   * @return the value returned by the method execution if any.
   * @throws NoSuchMethodException
   *           if no mean could be found to service the method.
   */
  protected Object invokeServiceMethod(Object proxy, Method method,
      Object[] args) throws NoSuchMethodException {
    IComponentService service = componentDescriptor.getServiceDelegate(method);
    if (service != null) {
      int signatureSize = method.getParameterTypes().length + 1;
      Class<?>[] parameterTypes = new Class[signatureSize];
      Object[] parameters = new Object[signatureSize];

      parameterTypes[0] = componentDescriptor.getComponentContract();
      parameters[0] = proxy;

      for (int i = 1; i < signatureSize; i++) {
        parameterTypes[i] = method.getParameterTypes()[i - 1];
        parameters[i] = args[i - 1];
      }
      try {
        return MethodUtils.invokeMethod(service, method.getName(), parameters,
            parameterTypes);
      } catch (IllegalAccessException ex) {
        throw new ComponentException(ex);
      } catch (InvocationTargetException ex) {
        if (ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        throw new ComponentException(ex.getCause());
      } catch (NoSuchMethodException ex) {
        throw new ComponentException(ex);
      }
    }
    throw new NoSuchMethodException(method.toString());
  }

  /**
   * Wether the object is fully initialized.
   * 
   * @param objectOrProxy
   *          the object to test.
   * @return true if the object is fully initialized.
   */
  protected boolean isInitialized(Object objectOrProxy) {
    return true;
  }

  /**
   * Gets wether this reference descriptor points to an inline component.
   * 
   * @param propertyDescriptor
   *          the reference descriptor to test.
   * @return true if this reference descriptor points to an inline component.
   */
  protected boolean isInlineComponentReference(
      IReferencePropertyDescriptor<?> propertyDescriptor) {
    return !IEntity.class.isAssignableFrom(propertyDescriptor
        .getReferencedDescriptor().getComponentContract())
        && !propertyDescriptor.getReferencedDescriptor().isPurelyAbstract()
        && !propertyDescriptor.isComputed();
  }

  /**
   * An empty hook that gets called whenever an entity is to be updated.
   * 
   * @param entityFactory
   *          an entity factory instance which can be used to complete the
   *          lifecycle step.
   * @param principal
   *          the principal triggering the action.
   * @param entityLifecycleHandler
   *          entityLifecycleHandler.
   */
  protected void onUpdate(IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler) {
    // defaults to no-op.
  }

  /**
   * Direct read access to the properties map without any other operation. Use
   * with caution only in subclasses.
   * 
   * @param propertyName
   *          the property name.
   * @return the property value.
   */
  protected abstract Object retrievePropertyValue(String propertyName);

  /**
   * Direct write access to the properties map without any other operation. Use
   * with caution only in subclasses.
   * 
   * @param propertyName
   *          the property name.
   * @param propertyValue
   *          the property value.
   */
  protected abstract void storeProperty(String propertyName,
      Object propertyValue);

  /**
   * Performs necessary registration on inline components before actually
   * storing them.
   * 
   * @param propertyDescriptor
   *          the reference property descriptor.
   * @param oldPropertyValue
   *          the old reference property value.
   * @param newPropertyValue
   *          the new reference property value.
   */
  protected void storeReferenceProperty(
      IReferencePropertyDescriptor<?> propertyDescriptor,
      Object oldPropertyValue, Object newPropertyValue) {
    String propertyName = propertyDescriptor.getName();
    InlineReferenceTracker oldTracker = null;
    if (oldPropertyValue != null) {
      oldTracker = referenceTrackers.get(propertyName);
      if (oldTracker != null && isInitialized(oldPropertyValue)) {
        ((IPropertyChangeCapable) oldPropertyValue)
            .removePropertyChangeListener(oldTracker);
      }
      referenceTrackers.remove(propertyName);
    }
    storeProperty(propertyName, newPropertyValue);
    if (newPropertyValue instanceof IPropertyChangeCapable) {
      InlineReferenceTracker newTracker = new InlineReferenceTracker(
          propertyName, isInlineComponentReference(propertyDescriptor));
      referenceTrackers.put(propertyName, newTracker);
      initializeInlineTrackerIfNeeded(
          (IPropertyChangeCapable) newPropertyValue, propertyName);
    } else if (oldTracker != null) {
      if (oldPropertyValue instanceof IComponent
          && /* To avoid breaking lazy initialization optim */isInitialized(oldPropertyValue)) {
        for (Map.Entry<String, Object> property : ((IComponent) oldPropertyValue)
            .straightGetProperties().entrySet()) {
          oldTracker.propertyChange(new PropertyChangeEvent(oldPropertyValue,
              property.getKey(), property.getValue(), null));
        }
      }
    }
  }

  /**
   * Performs (potentially delayed due to lazy initialization) inline tracker
   * attachement.
   * 
   * @param referenceProperty
   *          the reference to link the tracker to.
   * @param propertyName
   *          the property name of the tracker.
   */
  protected void initializeInlineTrackerIfNeeded(
      IPropertyChangeCapable referenceProperty, String propertyName) {
    if (/* To avoid breaking lazy initialization optim */isInitialized(referenceProperty)) {
      InlineReferenceTracker storedTracker = referenceTrackers
          .get(propertyName);
      if (storedTracker != null && !storedTracker.isInitialized()) {
        storedTracker.setInitialized(true);
        referenceProperty.addWeakPropertyChangeListener(storedTracker);
        if (referenceProperty instanceof IComponent) {
          for (Map.Entry<String, Object> property : ((IComponent) referenceProperty)
              .straightGetProperties().entrySet()) {
            storedTracker
                .propertyChange(new PropertyChangeEvent(referenceProperty,
                    property.getKey(), null, property.getValue()));
          }
        }
      }
    }
  }

  /**
   * Directly gets all property values out of the property store without any
   * other operation.
   * 
   * @param proxy
   *          the proxy to straight get the properties from.
   * @return The map of properties.
   */
  protected Map<String, Object> straightGetProperties(Object proxy) {
    Map<String, Object> allProperties = new HashMap<String, Object>();
    for (IPropertyDescriptor propertyDescriptor : componentDescriptor
        .getPropertyDescriptors()) {
      String propertyName = propertyDescriptor.getName();
      if (!(propertyDescriptor.isComputed() && propertyDescriptor
          .getPersistenceFormula() == null)) {
        allProperties.put(propertyName,
            straightGetProperty(proxy, propertyName));
      }
    }
    return allProperties;
  }

  /**
   * Directly get a property value out of the property store without any other
   * operation.
   * 
   * @param proxy
   *          the proxy to straight get the property from.
   * @param propertyName
   *          the name of the property.
   * @return the property value or null.
   */
  protected Object straightGetProperty(Object proxy, String propertyName) {
    IPropertyDescriptor propertyDescriptor = componentDescriptor
        .getPropertyDescriptor(propertyName);
    if (propertyDescriptor == null
        || (propertyDescriptor.isComputed() && propertyDescriptor
            .getPersistenceFormula() == null)) {
      return null;
    }
    Object propertyValue = retrievePropertyValue(propertyName);
    if (propertyValue == null
        && propertyDescriptor instanceof IBooleanPropertyDescriptor) {
      return Boolean.FALSE;
    }
    return propertyValue;
  }

  /**
   * Directly set a property value to the property store without any other
   * operation.
   * 
   * @param proxy
   *          the proxy to straight set the property to.
   * @param propertyName
   *          the name of the property.
   * @param newPropertyValue
   *          the property value or null.
   */
  protected void straightSetProperty(Object proxy, String propertyName,
      Object newPropertyValue) {
    IPropertyDescriptor propertyDescriptor = componentDescriptor
        .getPropertyDescriptor(propertyName);
    if (propertyDescriptor == null
        || (propertyDescriptor.isComputed() && propertyDescriptor
            .getPersistenceFormula() == null)) {
      return;
    }
    Object currentPropertyValue = straightGetProperty(proxy, propertyName);
    if (propertyDescriptor instanceof IReferencePropertyDescriptor) {
      // reference must change sometimes even if entities are equal.
      if (/* !ObjectUtils.equals(currentPropertyValue, newPropertyValue) */currentPropertyValue != newPropertyValue) {
        storeReferenceProperty(
            (IReferencePropertyDescriptor<?>) propertyDescriptor,
            currentPropertyValue, newPropertyValue);
      }
    } else {
      storeProperty(propertyName, newPropertyValue);
    }
    if (propertyDescriptor instanceof ICollectionPropertyDescriptor) {
      if (currentPropertyValue != null
          && currentPropertyValue == newPropertyValue
          && isInitialized(currentPropertyValue)) {
        currentPropertyValue = Proxy.newProxyInstance(
            Thread.currentThread().getContextClassLoader(),
            new Class[] {
              ((ICollectionPropertyDescriptor<?>) propertyDescriptor)
                  .getReferencedDescriptor().getCollectionInterface()
            },
            new NeverEqualsInvocationHandler(CollectionHelper
                .cloneCollection((Collection<?>) currentPropertyValue)));
      }
    }
    firePropertyChange(propertyName, currentPropertyValue, newPropertyValue);
  }

  private synchronized void addPropertyChangeListener(Object proxy,
      PropertyChangeListener listener) {
    if (listener == null) {
      return;
    }
    if (changeSupport == null) {
      changeSupport = new SinglePropertyChangeSupport(proxy);
    }
    changeSupport.addPropertyChangeListener(listener);
  }

  private synchronized void addWeakPropertyChangeListener(Object proxy,
      PropertyChangeListener listener) {
    if (listener == null) {
      return;
    }
    if (weakChangeSupport == null) {
      weakChangeSupport = new SingleWeakPropertyChangeSupport(proxy);
    }
    weakChangeSupport.addPropertyChangeListener(listener);
  }

  private synchronized void addPropertyChangeListener(Object proxy,
      String propertyName, PropertyChangeListener listener) {
    if (listener == null) {
      return;
    }
    if (changeSupport == null) {
      changeSupport = new SinglePropertyChangeSupport(proxy);
    }
    changeSupport.addPropertyChangeListener(propertyName, listener);
  }

  private synchronized void addWeakPropertyChangeListener(Object proxy,
      String propertyName, PropertyChangeListener listener) {
    if (listener == null) {
      return;
    }
    if (weakChangeSupport == null) {
      weakChangeSupport = new SingleWeakPropertyChangeSupport(proxy);
    }
    weakChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  @SuppressWarnings("unchecked")
  private void addToProperty(Object proxy,
      ICollectionPropertyDescriptor<?> propertyDescriptor, int index,
      Object value) {
    String propertyName = propertyDescriptor.getName();
    Collection<?> collectionProperty = null;
    try {
      collectionProperty = (Collection<?>) accessorFactory
          .createPropertyAccessor(propertyName,
              componentDescriptor.getComponentContract()).getValue(proxy);
    } catch (IllegalAccessException ex) {
      throw new ComponentException(ex);
    } catch (InvocationTargetException ex) {
      if (ex.getCause() instanceof RuntimeException) {
        throw (RuntimeException) ex.getCause();
      }
      throw new ComponentException(ex.getCause());
    } catch (NoSuchMethodException ex) {
      throw new ComponentException(ex);
    }
    try {
      if (propertyProcessorsEnabled) {
        propertyDescriptor.preprocessAdder(proxy, collectionProperty, value);
      }
      IRelationshipEndPropertyDescriptor reversePropertyDescriptor = propertyDescriptor
          .getReverseRelationEnd();
      if (reversePropertyDescriptor != null) {
        if (reversePropertyDescriptor instanceof IReferencePropertyDescriptor<?>) {
          accessorFactory.createPropertyAccessor(
              reversePropertyDescriptor.getName(),
              propertyDescriptor.getReferencedDescriptor()
                  .getElementDescriptor().getComponentContract()).setValue(
              value, proxy);
        } else if (reversePropertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
          ICollectionAccessor collectionAccessor = accessorFactory
              .createCollectionPropertyAccessor(
                  reversePropertyDescriptor.getName(),
                  propertyDescriptor.getReferencedDescriptor()
                      .getElementDescriptor().getComponentContract(),
                  ((ICollectionPropertyDescriptor<?>) reversePropertyDescriptor)
                      .getCollectionDescriptor().getElementDescriptor()
                      .getComponentContract());
          if (collectionAccessor instanceof IModelDescriptorAware) {
            ((IModelDescriptorAware) collectionAccessor)
                .setModelDescriptor(reversePropertyDescriptor);
          }
          collectionAccessor.addToValue(value, proxy);
        }
      }
      Collection<?> oldCollectionSnapshot = CollectionHelper
          .cloneCollection((Collection<?>) collectionProperty);
      boolean inserted = false;
      if (collectionProperty instanceof List<?> && index >= 0
          && index < collectionProperty.size()) {
        ((List<Object>) collectionProperty).add(index, value);
        inserted = true;
      } else {
        inserted = ((Collection<Object>) collectionProperty).add(value);
      }
      if (inserted) {
        inlineComponentFactory.sortCollectionProperty((IComponent) proxy,
            propertyName);
        firePropertyChange(propertyName, oldCollectionSnapshot,
            collectionProperty);
        if (propertyProcessorsEnabled) {
          propertyDescriptor.postprocessAdder(proxy, collectionProperty, value);
        }
      }
    } catch (RuntimeException ex) {
      rollbackProperty(proxy, propertyDescriptor, collectionProperty);
      throw ex;
    } catch (InvocationTargetException ex) {
      rollbackProperty(proxy, propertyDescriptor, collectionProperty);
      if (ex.getCause() instanceof RuntimeException) {
        throw (RuntimeException) ex.getCause();
      }
      throw new ComponentException(ex.getCause());
    } catch (IllegalAccessException ex) {
      throw new ComponentException(ex);
    } catch (NoSuchMethodException ex) {
      throw new ComponentException(ex);
    }
  }

  private void addToProperty(Object proxy,
      ICollectionPropertyDescriptor<?> propertyDescriptor, Object value) {
    addToProperty(proxy, propertyDescriptor, -1, value);
  }

  private void checkIntegrity(Object proxy) {
    if (propertyProcessorsEnabled) {
      for (IPropertyDescriptor propertyDescriptor : componentDescriptor
          .getPropertyDescriptors()) {
        if (!propertyDescriptor.isComputed()) {
          propertyDescriptor.preprocessSetter(proxy,
              straightGetProperty(proxy, propertyDescriptor.getName()));
        }
      }
    }
  }

  private void firePropertyChange(Object proxy, String propertyName,
      Object oldValue, Object newValue) {
    firePropertyChange(propertyName, oldValue, newValue);
    // This method supports firing nested property changes
    if (propertyName != null) {
      int lastIndexOfDelim = propertyName.lastIndexOf(IAccessor.NESTED_DELIM);
      if (lastIndexOfDelim > 0) {
        Object propertyHolder;
        try {
          propertyHolder = getAccessorFactory().createPropertyAccessor(
              propertyName.substring(0, lastIndexOfDelim),
              getComponentContract()).getValue(proxy);
          if (propertyHolder != null && propertyHolder instanceof IComponent) {
            ((IComponent) propertyHolder).firePropertyChange(
                propertyName.substring(lastIndexOfDelim + 1), oldValue,
                newValue);
          }
        } catch (IllegalAccessException ex) {
          throw new ComponentException(ex);
        } catch (InvocationTargetException ex) {
          if (ex.getCause() instanceof RuntimeException) {
            throw (RuntimeException) ex.getCause();
          }
          throw new ComponentException(ex.getCause());
        } catch (NoSuchMethodException ex) {
          throw new ComponentException(ex);
        }
      }
    }
  }

  private void firePropertyChange(String propertyName, Object oldValue,
      Object newValue) {
    if ((oldValue == null && newValue == null) || (oldValue == newValue)) {
      return;
    }
    if (!isInitialized(oldValue) || !isInitialized(newValue)) {
      if (changeSupport != null) {
        changeSupport.firePropertyChange(propertyName, null, newValue);
      }
      if (weakChangeSupport != null) {
        weakChangeSupport.firePropertyChange(propertyName, null, newValue);
      }
    } else {
      if (changeSupport != null) {
        changeSupport.firePropertyChange(propertyName, oldValue, newValue);
      }
      if (weakChangeSupport != null) {
        weakChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
      }
    }
  }

  private Object invokeExtensionMethod(
      IComponentExtension<? extends IComponent> componentExtension,
      Method method, Object[] args) {
    try {
      return MethodUtils.invokeMethod(componentExtension, method.getName(),
          args, method.getParameterTypes());
    } catch (IllegalAccessException ex) {
      throw new ComponentException(ex);
    } catch (InvocationTargetException ex) {
      if (ex.getCause() instanceof RuntimeException) {
        throw (RuntimeException) ex.getCause();
      }
      throw new ComponentException(ex.getCause());
    } catch (NoSuchMethodException ex) {
      throw new ComponentException(ex);
    }
  }

  @SuppressWarnings("unchecked")
  private boolean invokeLifecycleInterceptors(Object proxy,
      Method lifecycleMethod, Object[] args) {
    if (ILifecycleCapable.ON_UPDATE_METHOD_NAME.equals(lifecycleMethod
        .getName())) {
      onUpdate((IEntityFactory) args[0], (UserPrincipal) args[1],
          (IEntityLifecycleHandler) args[2]);
    }
    boolean interceptorResults = false;
    for (ILifecycleInterceptor<?> lifecycleInterceptor : componentDescriptor
        .getLifecycleInterceptors()) {
      int signatureSize = lifecycleMethod.getParameterTypes().length + 1;
      Class<?>[] parameterTypes = new Class[signatureSize];
      Object[] parameters = new Object[signatureSize];

      parameterTypes[0] = componentDescriptor.getComponentContract();
      parameters[0] = proxy;

      for (int i = 1; i < signatureSize; i++) {
        parameterTypes[i] = lifecycleMethod.getParameterTypes()[i - 1];
        parameters[i] = args[i - 1];
      }
      try {
        Object interceptorResult = MethodUtils.invokeMethod(
            lifecycleInterceptor, lifecycleMethod.getName(), parameters,
            parameterTypes);
        if (interceptorResult instanceof Boolean) {
          interceptorResults = interceptorResults
              || ((Boolean) interceptorResult).booleanValue();
        }
      } catch (IllegalAccessException ex) {
        throw new ComponentException(ex);
      } catch (InvocationTargetException ex) {
        if (ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        throw new ComponentException(ex.getCause());
      } catch (NoSuchMethodException ex) {
        throw new ComponentException(ex);
      }
    }
    // invoke lifecycle method on inlined components
    for (IPropertyDescriptor propertyDescriptor : componentDescriptor
        .getPropertyDescriptors()) {
      if (propertyDescriptor instanceof IReferencePropertyDescriptor<?>
          && isInlineComponentReference((IReferencePropertyDescriptor<IComponent>) propertyDescriptor)) {
        Object inlineComponent = getProperty(proxy, propertyDescriptor);
        if (inlineComponent instanceof ILifecycleCapable) {
          try {
            Object interceptorResult = MethodUtils.invokeMethod(
                inlineComponent, lifecycleMethod.getName(), args,
                lifecycleMethod.getParameterTypes());
            if (interceptorResult instanceof Boolean) {
              interceptorResults = interceptorResults
                  || ((Boolean) interceptorResult).booleanValue();
            }
          } catch (NoSuchMethodException ex) {
            throw new ComponentException(ex);
          } catch (IllegalAccessException ex) {
            throw new ComponentException(ex);
          } catch (InvocationTargetException ex) {
            if (ex.getCause() instanceof RuntimeException) {
              throw (RuntimeException) ex.getCause();
            }
            throw new ComponentException(ex.getCause());
          }
        }
      }
    }
    if (ILifecycleCapable.ON_PERSIST_METHOD_NAME.equals(lifecycleMethod
        .getName())) {
      // Important to check for not null values.
      checkIntegrity(proxy);
    }
    return interceptorResults;
  }

  private void removeFromProperty(Object proxy,
      ICollectionPropertyDescriptor<?> propertyDescriptor, Object value) {
    String propertyName = propertyDescriptor.getName();
    // The following optim breaks bidi N-N relationship persistence
    // if (!isInitialized(straightGetProperty(proxy, propertyName))) {
    // return;
    // }
    Collection<?> collectionProperty = null;
    try {
      collectionProperty = (Collection<?>) accessorFactory
          .createPropertyAccessor(propertyName,
              componentDescriptor.getComponentContract()).getValue(proxy);
    } catch (IllegalAccessException ex) {
      throw new ComponentException(ex);
    } catch (InvocationTargetException ex) {
      if (ex.getCause() instanceof RuntimeException) {
        throw (RuntimeException) ex.getCause();
      }
      throw new ComponentException(ex.getCause());
    } catch (NoSuchMethodException ex) {
      throw new ComponentException(ex);
    }
    try {
      if (propertyProcessorsEnabled) {
        propertyDescriptor.preprocessRemover(proxy, collectionProperty, value);
      }
      if (collectionProperty.contains(value)) {
        IRelationshipEndPropertyDescriptor reversePropertyDescriptor = propertyDescriptor
            .getReverseRelationEnd();
        if (reversePropertyDescriptor != null) {
          if (reversePropertyDescriptor instanceof IReferencePropertyDescriptor<?>) {
            accessorFactory.createPropertyAccessor(
                reversePropertyDescriptor.getName(),
                propertyDescriptor.getReferencedDescriptor()
                    .getElementDescriptor().getComponentContract()).setValue(
                value, null);
          } else if (reversePropertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
            ICollectionAccessor collectionAccessor = accessorFactory
                .createCollectionPropertyAccessor(
                    reversePropertyDescriptor.getName(),
                    propertyDescriptor.getReferencedDescriptor()
                        .getElementDescriptor().getComponentContract(),
                    ((ICollectionPropertyDescriptor<?>) reversePropertyDescriptor)
                        .getCollectionDescriptor().getElementDescriptor()
                        .getComponentContract());
            if (collectionAccessor instanceof IModelDescriptorAware) {
              ((IModelDescriptorAware) collectionAccessor)
                  .setModelDescriptor(reversePropertyDescriptor);
            }
            collectionAccessor.removeFromValue(value, proxy);
          }
        }
        Collection<?> oldCollectionSnapshot = CollectionHelper
            .cloneCollection((Collection<?>) collectionProperty);
        if (collectionProperty.remove(value)) {
          firePropertyChange(propertyName, oldCollectionSnapshot,
              collectionProperty);
          if (propertyProcessorsEnabled) {
            propertyDescriptor.postprocessRemover(proxy, collectionProperty,
                value);
          }
          if (proxy instanceof IEntity && value instanceof IEntity) {
            entityDetached((IEntity) proxy, (IEntity) value, propertyDescriptor);
          }
        }
      }
    } catch (RuntimeException ex) {
      rollbackProperty(proxy, propertyDescriptor, collectionProperty);
      throw ex;
    } catch (InvocationTargetException ex) {
      rollbackProperty(proxy, propertyDescriptor, collectionProperty);
      if (ex.getCause() instanceof RuntimeException) {
        throw (RuntimeException) ex.getCause();
      }
      throw new ComponentException(ex.getCause());
    } catch (IllegalAccessException ex) {
      throw new ComponentException(ex);
    } catch (NoSuchMethodException ex) {
      throw new ComponentException(ex);
    }
  }

  private synchronized void removePropertyChangeListener(
      PropertyChangeListener listener) {
    if (listener == null) {
      return;
    }
    if (changeSupport != null) {
      changeSupport.removePropertyChangeListener(listener);
    }
    if (weakChangeSupport != null) {
      weakChangeSupport.removePropertyChangeListener(listener);
    }
  }

  private synchronized void removePropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    if (listener == null) {
      return;
    }
    if (changeSupport != null) {
      changeSupport.removePropertyChangeListener(propertyName, listener);
    }
    if (weakChangeSupport != null) {
      weakChangeSupport.removePropertyChangeListener(propertyName, listener);
    }
  }

  private void rollbackProperty(Object proxy,
      IPropertyDescriptor propertyDescriptor, Object oldProperty) {
    // boolean wasPropertyProcessorsEnabled = propertyProcessorsEnabled;
    // try {
    // propertyProcessorsEnabled = false;
    // setProperty(proxy, propertyDescriptor, oldProperty);
    // } finally {
    // propertyProcessorsEnabled = wasPropertyProcessorsEnabled;
    // }
    straightSetProperty(proxy, propertyDescriptor.getName(), oldProperty);
  }

  @SuppressWarnings("unchecked")
  private void setProperty(Object proxy,
      IPropertyDescriptor propertyDescriptor, Object newProperty) {
    String propertyName = propertyDescriptor.getName();

    Object oldProperty = null;
    try {
      oldProperty = accessorFactory.createPropertyAccessor(propertyName,
          componentDescriptor.getComponentContract()).getValue(proxy);
    } catch (IllegalAccessException ex) {
      throw new ComponentException(ex);
    } catch (InvocationTargetException ex) {
      if (ex.getCause() instanceof RuntimeException) {
        throw (RuntimeException) ex.getCause();
      }
      throw new ComponentException(ex.getCause());
    } catch (NoSuchMethodException ex) {
      throw new ComponentException(ex);
    }
    Object actualNewProperty;
    if (propertyProcessorsEnabled) {
      actualNewProperty = propertyDescriptor
          .interceptSetter(proxy, newProperty);
    } else {
      actualNewProperty = newProperty;
    }
    if (ObjectUtils.equals(oldProperty, actualNewProperty)) {
      return;
    }
    if (propertyProcessorsEnabled) {
      propertyDescriptor.preprocessSetter(proxy, actualNewProperty);
    }
    if (propertyDescriptor instanceof IRelationshipEndPropertyDescriptor) {
      // It's a relation end
      IRelationshipEndPropertyDescriptor reversePropertyDescriptor = ((IRelationshipEndPropertyDescriptor) propertyDescriptor)
          .getReverseRelationEnd();
      try {
        if (propertyDescriptor instanceof IReferencePropertyDescriptor) {
          // It's a 'one' relation end
          storeReferenceProperty(
              (IReferencePropertyDescriptor<?>) propertyDescriptor,
              oldProperty, actualNewProperty);
          if (reversePropertyDescriptor != null) {
            // It is bidirectionnal, so we are going to update the other end.
            if (reversePropertyDescriptor instanceof IReferencePropertyDescriptor) {
              // It's a one-to-one relationship
              if (proxy instanceof IEntity && oldProperty instanceof IEntity) {
                entityDetached((IEntity) proxy, (IEntity) oldProperty,
                    ((IRelationshipEndPropertyDescriptor) propertyDescriptor));
              }
              IAccessor reversePropertyAccessor = accessorFactory
                  .createPropertyAccessor(reversePropertyDescriptor.getName(),
                      ((IReferencePropertyDescriptor<?>) propertyDescriptor)
                          .getReferencedDescriptor().getComponentContract());
              if (oldProperty != null) {
                reversePropertyAccessor.setValue(oldProperty, null);
              }
              if (actualNewProperty != null) {
                reversePropertyAccessor.setValue(actualNewProperty, proxy);
              }
            } else if (reversePropertyDescriptor instanceof ICollectionPropertyDescriptor) {
              // It's a one-to-many relationship
              ICollectionAccessor reversePropertyAccessor = accessorFactory
                  .createCollectionPropertyAccessor(
                      reversePropertyDescriptor.getName(),
                      ((IReferencePropertyDescriptor<?>) propertyDescriptor)
                          .getReferencedDescriptor().getComponentContract(),
                      ((ICollectionPropertyDescriptor<?>) reversePropertyDescriptor)
                          .getCollectionDescriptor().getElementDescriptor()
                          .getComponentContract());
              if (reversePropertyAccessor instanceof IModelDescriptorAware) {
                ((IModelDescriptorAware) reversePropertyAccessor)
                    .setModelDescriptor(reversePropertyDescriptor);
              }
              if (oldProperty != null) {
                reversePropertyAccessor.removeFromValue(oldProperty, proxy);
              }
              if (actualNewProperty != null) {
                reversePropertyAccessor.addToValue(actualNewProperty, proxy);
              }
            }
          }
        } else if (propertyDescriptor instanceof ICollectionPropertyDescriptor) {
          // It's a 'many' relation end
          Collection<Object> oldPropertyElementsToRemove = new HashSet<Object>();
          Collection<Object> newPropertyElementsToAdd = new LinkedHashSet<Object>();
          Collection<Object> propertyElementsToKeep = new HashSet<Object>();

          if (oldProperty != null) {
            oldPropertyElementsToRemove.addAll((Collection<?>) oldProperty);
            propertyElementsToKeep.addAll((Collection<?>) oldProperty);
          }
          if (actualNewProperty != null) {
            newPropertyElementsToAdd.addAll((Collection<?>) actualNewProperty);
          }
          propertyElementsToKeep.retainAll(newPropertyElementsToAdd);
          oldPropertyElementsToRemove.removeAll(propertyElementsToKeep);
          newPropertyElementsToAdd.removeAll(propertyElementsToKeep);
          ICollectionAccessor propertyAccessor = accessorFactory
              .createCollectionPropertyAccessor(propertyDescriptor.getName(),
                  componentDescriptor.getComponentContract(),
                  ((ICollectionPropertyDescriptor<?>) propertyDescriptor)
                      .getCollectionDescriptor().getElementDescriptor()
                      .getComponentContract());
          for (Object element : oldPropertyElementsToRemove) {
            propertyAccessor.removeFromValue(proxy, element);
          }
          for (Object element : newPropertyElementsToAdd) {
            propertyAccessor.addToValue(proxy, element);
          }
          // if the property is a list we may restore the element order and be
          // careful not to miss one...
          if (actualNewProperty instanceof List) {
            Collection<Object> currentProperty = (Collection<Object>) oldProperty;
            List<Object> snapshot = new ArrayList<Object>(currentProperty);
            if (currentProperty instanceof List) {
              // Just check that only order differs
              Set<Object> temp = new HashSet<Object>(currentProperty);
              temp.removeAll((List<?>) actualNewProperty);
              currentProperty.clear();
              currentProperty.addAll((List<?>) actualNewProperty);
              currentProperty.addAll(temp);
              oldProperty = snapshot;
            }
          }
        }
      } catch (RuntimeException ex) {
        rollbackProperty(proxy, propertyDescriptor, oldProperty);
        throw ex;
      } catch (InvocationTargetException ex) {
        rollbackProperty(proxy, propertyDescriptor, oldProperty);
        if (ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        throw new ComponentException(ex.getCause());
      } catch (IllegalAccessException ex) {
        throw new ComponentException(ex);
      } catch (NoSuchMethodException ex) {
        throw new ComponentException(ex);
      }
    } else {
      storeProperty(propertyName, actualNewProperty);
    }
    firePropertyChange(propertyName, oldProperty, actualNewProperty);
    if (propertyProcessorsEnabled) {
      propertyDescriptor.postprocessSetter(proxy, oldProperty,
          actualNewProperty);
    }
  }

  private void straightSetProperties(Object proxy,
      Map<String, Object> backendProperties) {
    for (Map.Entry<String, Object> propertyEntry : backendProperties.entrySet()) {
      straightSetProperty(proxy, propertyEntry.getKey(),
          propertyEntry.getValue());
    }
  }

  private String toString(Object proxy) {
    try {
      String toStringPropertyName = componentDescriptor.getToStringProperty();
      Object toStringValue = accessorFactory.createPropertyAccessor(
          toStringPropertyName, componentDescriptor.getComponentContract())
          .getValue(proxy);
      if (toStringValue == null) {
        return "";
      }
      return toStringValue.toString();
    } catch (IllegalAccessException ex) {
      throw new ComponentException(ex);
    } catch (InvocationTargetException ex) {
      if (ex.getCause() instanceof RuntimeException) {
        throw (RuntimeException) ex.getCause();
      }
      throw new ComponentException(ex.getCause());
    } catch (NoSuchMethodException ex) {
      throw new ComponentException(ex);
    }
  }

  private class InlineReferenceTracker implements PropertyChangeListener {

    private String  componentName;
    private boolean enabled;
    private boolean initialized;
    private boolean inlinedComponent;

    /**
     * Constructs a new <code>InnerComponentTracker</code> instance.
     * 
     * @param componentName
     *          the name of the component to track the properties.
     * @param inlinedComponent
     *          is it tracking an inlined component or an entity ref ?
     */
    public InlineReferenceTracker(String componentName, boolean inlinedComponent) {
      this.componentName = componentName;
      this.inlinedComponent = inlinedComponent;
      this.enabled = true;
      this.initialized = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      if (enabled) {
        boolean wasEnabled = enabled;
        String nestedPropertyName = componentName + "." + evt.getPropertyName();
        try {
          enabled = false;
          if (inlinedComponent) {
            // for dirtyness notification
            firePropertyChange(componentName, null, evt.getSource());
          }
          // for ui notification
          if ((changeSupport != null && changeSupport
              .hasListeners(nestedPropertyName))
              || (weakChangeSupport != null && weakChangeSupport
                  .hasListeners(nestedPropertyName))) {
            firePropertyChange(nestedPropertyName, evt.getOldValue(),
                evt.getNewValue());
          }
        } finally {
          enabled = wasEnabled;
        }
      }
    }

    /**
     * Gets the initialized.
     * 
     * @return the initialized.
     */
    public boolean isInitialized() {
      return initialized;
    }

    /**
     * Sets the initialized.
     * 
     * @param initialized
     *          the initialized to set.
     */
    public void setInitialized(boolean initialized) {
      this.initialized = initialized;
    }
  }

  private static final class NeverEqualsInvocationHandler implements
      InvocationHandler {

    private Object delegate;

    private NeverEqualsInvocationHandler(Object delegate) {
      this.delegate = delegate;
    }

    /**
     * Just 'overrides' the equals method to always return false.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public Object invoke(@SuppressWarnings("unused") Object proxy,
        Method method, Object[] args) throws Throwable {
      if (method.getName().equals("equals") && args.length == 1) {
        return new Boolean(false);
      }
      return method.invoke(delegate, args);
    }
  }
}
