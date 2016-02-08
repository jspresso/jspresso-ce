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
package org.jspresso.framework.model.component.basic;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TLinkedHashSet;
import org.apache.commons.beanutils.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jspresso.framework.model.component.ComponentException;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IComponentCollectionFactory;
import org.jspresso.framework.model.component.IComponentExtension;
import org.jspresso.framework.model.component.IComponentExtensionFactory;
import org.jspresso.framework.model.component.IComponentFactory;
import org.jspresso.framework.model.component.IComponentFactoryAware;
import org.jspresso.framework.model.component.ILifecycleCapable;
import org.jspresso.framework.model.component.IPropertyTranslation;
import org.jspresso.framework.model.component.service.AbstractComponentServiceDelegate;
import org.jspresso.framework.model.component.service.DependsOnHelper;
import org.jspresso.framework.model.component.service.IComponentService;
import org.jspresso.framework.model.component.service.ILifecycleInterceptor;
import org.jspresso.framework.model.descriptor.IBooleanPropertyDescriptor;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IModelDescriptorAware;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.IRelationshipEndPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IStringPropertyDescriptor;
import org.jspresso.framework.model.descriptor.MandatoryPropertyException;
import org.jspresso.framework.model.descriptor.basic.AbstractComponentDescriptor;
import org.jspresso.framework.model.entity.EntityHelper;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.model.entity.IEntityLifecycleHandler;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.accessor.ICollectionAccessor;
import org.jspresso.framework.util.bean.AccessorInfo;
import org.jspresso.framework.util.bean.BeanPropertyChangeRecorder;
import org.jspresso.framework.util.bean.EAccessorType;
import org.jspresso.framework.util.bean.IPropertyChangeCapable;
import org.jspresso.framework.util.bean.SinglePropertyChangeSupport;
import org.jspresso.framework.util.bean.SingleWeakPropertyChangeSupport;
import org.jspresso.framework.util.collection.CollectionHelper;
import org.jspresso.framework.util.lang.ObjectUtils;

/**
 * This is the core implementation of all components in the application.
 * Instances of this class serve as handlers for proxies representing the
 * components.
 *
 * @author Vincent Vandenschrick
 */
public abstract class AbstractComponentInvocationHandler implements
    InvocationHandler, Serializable {

  // @formatter:off
  private static final Logger LOG = LoggerFactory.getLogger(AbstractComponentInvocationHandler.class);
  // @formatter:on

  private static final long serialVersionUID = -8332414648339056836L;

  private final IAccessorFactory accessorFactory;

  private SinglePropertyChangeSupport     propertyChangeSupport;
  private SingleWeakPropertyChangeSupport weakPropertyChangeSupport;
  private List<PropertyChangeEvent>       delayedEvents;

  private       IComponentCollectionFactory                                                  collectionFactory;
  private final IComponentDescriptor<? extends IComponent>                                   componentDescriptor;
  private       Map<Class<IComponentExtension<IComponent>>, IComponentExtension<IComponent>> componentExtensions;
  private final IComponentExtensionFactory                                                   extensionFactory;
  private final IComponentFactory                                                            inlineComponentFactory;
  private       Set<String>                                                                  modifierMonitors;

  private boolean propertyProcessorsEnabled;
  private boolean propertyChangeEnabled;
  private boolean collectionSortEnabled;

  private       Map<String, NestedReferenceTracker> referenceTrackers;

  private       Map<String, Object> computedPropertiesCache;

  private static final Collection<String>     LIFECYCLE_METHOD_NAMES;
  private              IComponent             owningComponent;
  private              IPropertyDescriptor    owningPropertyDescriptor;
  private              Map<String, Set<String>> fakePclAttachements;
  private              Map<String, Set<String>> delayedFakePclAttachements;
  // Fake PCL cannot be static, because there must be 1 registration on
  // referent per owning instance, i.e. 2 different instances must not share
  // the same fake PCL that will be removed when the referent is detached.
  private              PropertyChangeListener fakePcl;

  static {
    Collection<String> methodNames = new THashSet<>(6);
    for (Method m : ILifecycleCapable.class.getMethods()) {
      methodNames.add(m.getName());
    }
    LIFECYCLE_METHOD_NAMES = methodNames;
  }

  /**
   * Constructs a new {@code BasicComponentInvocationHandler} instance.
   *
   * @param componentDescriptor
   *     The descriptor of the proxy component.
   * @param inlineComponentFactory
   *     the factory used to create inline components.
   * @param collectionFactory
   *     The factory used to create empty component collections from     collection getters.
   * @param accessorFactory
   *     The factory used to access proxy properties.
   * @param extensionFactory
   *     The factory used to create component extensions based on their     classes.
   */
  protected AbstractComponentInvocationHandler(IComponentDescriptor<? extends IComponent> componentDescriptor,
                                               IComponentFactory inlineComponentFactory,
                                               IComponentCollectionFactory collectionFactory,
                                               IAccessorFactory accessorFactory,
                                               IComponentExtensionFactory extensionFactory) {
    this.componentDescriptor = componentDescriptor;
    this.inlineComponentFactory = inlineComponentFactory;
    this.collectionFactory = collectionFactory;
    this.accessorFactory = accessorFactory;
    this.extensionFactory = extensionFactory;
    this.propertyProcessorsEnabled = true;
    this.propertyChangeEnabled = true;
    this.collectionSortEnabled = true;
  }

  /**
   * Gets the interface class being the contract of this component.
   *
   * @return the component interface contract.
   */
  public Class<?> getComponentContract() {
    return componentDescriptor.getComponentContract();
  }

  /**
   * Handles methods invocations on the component proxy. Either : <li>delegates
   * to one of its extension if the accessed property is registered as being
   * part of an extension <li>handles property access internally
   * <p/>
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public synchronized Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    String methodName = method.getName()/* .intern() */;
    switch (methodName) {
      case "hashCode":
        return computeHashCode((IComponent) proxy);
      case "equals":
        return computeEquals((IComponent) proxy, args[0]);
      case "toString":
        return toString(proxy);
      case "getComponentContract":
        return componentDescriptor.getComponentContract();
      case "addPropertyChangeListener":
        if (args.length == 1) {
          addPropertyChangeListener(proxy, (PropertyChangeListener) args[0]);
          return null;
        }
        addPropertyChangeListener(proxy, (String) args[0], (PropertyChangeListener) args[1]);
        return null;
      case "addWeakPropertyChangeListener":
        if (args.length == 1) {
          addWeakPropertyChangeListener(proxy, (PropertyChangeListener) args[0]);
          return null;
        }
        addWeakPropertyChangeListener(proxy, (String) args[0], (PropertyChangeListener) args[1]);
        return null;
      case "removePropertyChangeListener":
        if (args.length == 1) {
          removePropertyChangeListener((PropertyChangeListener) args[0]);
          return null;
        }
        removePropertyChangeListener((String) args[0], (PropertyChangeListener) args[1]);
        return null;
      case "hasListeners":
        return hasListeners(proxy, (String) args[0]);
      case "getPropertyChangeListeners":
        if (args != null && args.length > 0) {
          return getPropertyChangeListeners(proxy, (String) args[0]);
        }
        return getPropertyChangeListeners(proxy);
      case "firePropertyChange":
        firePropertyChange(proxy, (String) args[0], args[1], args[2]);
        return null;
      case "blockEvents":
        return blockEvents();
      case "releaseEvents":
        releaseEvents();
        return null;
      case "straightSetProperty":
        straightSetProperty(proxy, (String) args[0], args[1]);
        return null;
      case "straightGetProperty":
        return straightGetProperty(proxy, (String) args[0]);
      case "straightSetProperties":
        straightSetProperties(proxy, (Map<String, Object>) args[0]);
        return null;
      case "straightGetProperties":
        return straightGetProperties(proxy);
      case "setPropertyProcessorsEnabled":
        propertyProcessorsEnabled = (Boolean) args[0];
        return null;
      case "getOwningComponent":
        return owningComponent;
      case "getOwningPropertyDescriptor":
        return owningPropertyDescriptor;
      case "setOwningComponent":
        owningComponent = (IComponent) args[0];
        owningPropertyDescriptor = (IPropertyDescriptor) args[1];
        return null;
      case "checkIntegrity":
        checkIntegrity(proxy);
        return null;
      case "checkMandatoryProperties":
        checkMandatoryProperties(proxy);
        return null;
      default:
        if (isLifecycleMethod(method)) {
          return invokeLifecycleInterceptors(proxy, method, args);
        }
        AccessorInfo accessorInfo = getAccessorFactory().getAccessorInfo(method);
        EAccessorType accessorType = accessorInfo.getAccessorType();
        IPropertyDescriptor propertyDescriptor = null;
        if (accessorType != EAccessorType.NONE) {
          String accessedPropertyName = accessorInfo.getAccessedPropertyName();
          if (accessedPropertyName != null) {
            propertyDescriptor = componentDescriptor.getPropertyDescriptor(accessedPropertyName);
          }
        }
        if (propertyDescriptor != null) {
          Class<IComponentExtension<IComponent>> extensionClass = (Class<IComponentExtension<IComponent>>)
              propertyDescriptor
              .getDelegateClass();
          if (extensionClass != null) {
            return accessComputedProperty(propertyDescriptor, accessorInfo, extensionClass, proxy, method, args);
          } else if (!propertyDescriptor.isComputed()) {
            if (accessorInfo.isModifier()) {
              if (modifierMonitors != null && modifierMonitors.contains(methodName)) {
                return null;
              }
              if (modifierMonitors == null) {
                modifierMonitors = new THashSet<>(1);
              }
              modifierMonitors.add(methodName);
            }
            try {
              Object param;
              switch (accessorType) {
                case GETTER:
                  return getProperty(proxy, propertyDescriptor);
                case SETTER:
                  param = sanitizeModifierParam(proxy, propertyDescriptor, args[0]);
                  setProperty(proxy, propertyDescriptor, param);
                  return null;
                case ADDER:
                  if (args.length == 2) {
                    param = sanitizeModifierParam(proxy, propertyDescriptor, args[1]);
                    addToProperty(proxy, (ICollectionPropertyDescriptor<?>) propertyDescriptor, (Integer) args[0],
                        param);
                  } else {
                    param = sanitizeModifierParam(proxy, propertyDescriptor, args[0]);
                    addToProperty(proxy, (ICollectionPropertyDescriptor<?>) propertyDescriptor, param);
                  }
                  return null;
                case REMOVER:
                  param = sanitizeModifierParam(proxy, propertyDescriptor, args[0]);
                  removeFromProperty(proxy, (ICollectionPropertyDescriptor<?>) propertyDescriptor, param);
                  return null;
                default:
                  break;
              }
            } finally {
              if (modifierMonitors != null && accessorInfo.isModifier()) {
                modifierMonitors.remove(methodName);
              }
            }
          } else if (propertyDescriptor instanceof IStringPropertyDescriptor
              && ((IStringPropertyDescriptor) propertyDescriptor).isTranslatable()) {
            if (accessorInfo.isModifier()) {
              if (propertyDescriptor.getName().endsWith(IComponentDescriptor.NLS_SUFFIX)) {
                invokeNlsSetter(proxy, (IStringPropertyDescriptor) propertyDescriptor, (String) args[0]);
              } else {
                invokeNlsOrRawSetter(proxy, (IStringPropertyDescriptor) propertyDescriptor, (String) args[0]);
              }
              return null;
            } else {
              if (propertyDescriptor.getName().endsWith(IComponentDescriptor.NLS_SUFFIX)) {
                return invokeNlsGetter(proxy, (IStringPropertyDescriptor) propertyDescriptor);
              } else {
                return invokeNlsOrRawGetter(proxy, (IStringPropertyDescriptor) propertyDescriptor);
              }
            }
          } else {
            try {
              return invokeServiceMethod(proxy, method, args);
            } catch (NoSuchMethodException ignored) {
              // it will fall back in the general case.
            }
            throw new ComponentException("The '" + propertyDescriptor.getName()
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
        break;
    }
    throw new ComponentException(method.toString()
        + " is not supported on the component "
        + componentDescriptor.getComponentContract().getName());
  }

  /**
   * Invoke nls getter.
   *
   * @param proxy
   *     the proxy
   * @param propertyDescriptor
   *     the property descriptor
   * @return the translated value
   */
  protected String invokeNlsGetter(Object proxy, IStringPropertyDescriptor propertyDescriptor) {
    return (String) straightGetProperty(proxy, propertyDescriptor.getName() + IComponentDescriptor.RAW_SUFFIX);
  }

  /**
   * Invoke nls or raw getter.
   *
   * @param proxy
   *     the proxy
   * @param propertyDescriptor
   *     the property descriptor
   * @return the translated value or raw if non-existent.
   */
  protected String invokeNlsOrRawGetter(Object proxy, IStringPropertyDescriptor propertyDescriptor) {
    String nlsOrRawValue = invokeNlsGetter(proxy, propertyDescriptor);
    if (nlsOrRawValue == null) {
      nlsOrRawValue = (String) straightGetProperty(proxy, propertyDescriptor.getName() + IComponentDescriptor
          .RAW_SUFFIX);
    }
    return nlsOrRawValue;
  }

  /**
   * Invoke nls setter.
   *
   * @param proxy
   *     the proxy
   * @param propertyDescriptor
   *     the property descriptor
   * @param translatedValue
   *     the translated value
   */
  protected void invokeNlsSetter(Object proxy, IStringPropertyDescriptor propertyDescriptor, String translatedValue) {
    straightSetProperty(proxy, propertyDescriptor.getName() + IComponentDescriptor.RAW_SUFFIX, translatedValue);
  }

  /**
   * Invoke nls or raw setter.
   *
   * @param proxy
   *     the proxy
   * @param propertyDescriptor
   *     the property descriptor
   * @param translatedValue
   *     the translated value
   */
  protected void invokeNlsOrRawSetter(Object proxy, IStringPropertyDescriptor propertyDescriptor,
                                   String translatedValue) {
    String oldTranslation = invokeNlsOrRawGetter(proxy, propertyDescriptor);
    invokeNlsSetter(proxy, propertyDescriptor, translatedValue);
    String propertyName = propertyDescriptor.getName();
    storeProperty(propertyName, translatedValue);
    firePropertyChange(proxy, propertyName, oldTranslation, translatedValue);
  }

  private boolean isLifecycleMethod(Method method) {
    String methodName = method.getName();
    if (LIFECYCLE_METHOD_NAMES.contains(methodName)) {
      try {
        return ILifecycleCapable.class.getMethod(methodName,
            method.getParameterTypes()) != null;
      } catch (NoSuchMethodException ignored) {
        // this is certainly normal.
      }
    }
    return false;
  }

  /**
   * Gives chance to subclasses to perform sanity checks and eventually
   * substitute the passed param by an other one when it's technically
   * necessary.
   *
   * @param target
   *     the target being modified.
   * @param propertyDescriptor
   *     the descriptor of the property being modified.
   * @param param
   *     the modifier parameter.
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
   *     the collectionFactory to set.
   */
  public void setCollectionFactory(IComponentCollectionFactory collectionFactory) {
    this.collectionFactory = collectionFactory;
  }

  /**
   * Delegate method to compute object equality.
   *
   * @param proxy
   *     the target component to compute equality of.
   * @param another
   *     the object to compute equality against.
   * @return the computed equality.
   */
  protected abstract boolean computeEquals(IComponent proxy, Object another);

  /**
   * Delegate method to compute hashcode.
   *
   * @param proxy
   *     the target component to compute hashcode for.
   * @return the computed hashcode.
   */
  protected abstract int computeHashCode(IComponent proxy);

  /**
   * Gives a chance to configure created extensions.
   *
   * @param extension
   *     the extension to configure.
   */
  protected void configureExtension(IComponentExtension<IComponent> extension) {
    if (extension instanceof IComponentFactoryAware) {
      ((IComponentFactoryAware) extension)
          .setComponentFactory(getInlineComponentFactory());
    }
    extension.postCreate();
  }

  /**
   * Gives a chance to the implementor to decorate a component reference before
   * returning it when fetching association ends.
   *
   * @param referent
   *     the component reference to decorate.
   * @param referentDescriptor
   *     the component descriptor of the referent.
   * @return the decorated component.
   */
  protected abstract IComponent decorateReferent(IComponent referent,
      IComponentDescriptor<? extends IComponent> referentDescriptor);

  /**
   * An empty hook that gets called whenever an entity is detached from a parent
   * one.
   *
   * @param parent
   *     the parent entity.
   * @param child
   *     the child entity.
   * @param propertyDescriptor
   *     the property descriptor this entity was detached from.
   */
  @SuppressWarnings("UnusedParameters")
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
   *     the proxy to get the property of.
   * @param propertyDescriptor
   *     the property descriptor to get the value for.
   * @return the property value.
   */
  @SuppressWarnings({"unchecked", "ConstantConditions"})
  protected Object getCollectionProperty(Object proxy,
      ICollectionPropertyDescriptor<? extends IComponent> propertyDescriptor) {
    String propertyName = propertyDescriptor.getName();
    try {
      Object property = straightGetProperty(proxy, propertyName);
      if (property == null) {
        property = collectionFactory
            .createComponentCollection(propertyDescriptor.getReferencedDescriptor().getCollectionInterface());
        storeProperty(propertyName, property);
      }
      if (property instanceof List<?>) {
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
            if (proxy instanceof IEntity) {
              LOG.warn(
                  "A null element was detected in indexed list [{}] on {}, id {} at index {}", propertyName,
                  ((IEntity) proxy).getComponentContract().getName(), ((IEntity) proxy).getId(), i);
              LOG.warn("This might be normal but sometimes it reveals a mis-use of indexed collection property accessors.");
            }
          } else if(EntityHelper.isInlineComponentReference(
              propertyDescriptor.getReferencedDescriptor().getElementDescriptor())) {
            if (decorated != null) {
              decorated.setOwningComponent((IComponent) proxy, propertyDescriptor);
            }
          }
        }
      } else if (property instanceof Set<?>) {
        Set<IComponent> propertyAsSet = (Set<IComponent>) property;
        for (IComponent referent : new THashSet<>(propertyAsSet)) {
          IComponent decorated = decorateReferent(referent, propertyDescriptor
              .getReferencedDescriptor().getElementDescriptor()
              .getComponentDescriptor());
          if (decorated != referent) {
            propertyAsSet.add(decorated);
          }
          if (EntityHelper.isInlineComponentReference(
              propertyDescriptor.getReferencedDescriptor().getElementDescriptor())) {
            if (decorated != null) {
              decorated.setOwningComponent((IComponent) proxy, propertyDescriptor);
            }
          }
        }
      }
      if (isCollectionSortOnReadEnabled() && collectionSortEnabled) {
        inlineComponentFactory.sortCollectionProperty((IComponent) proxy,
            propertyName);
      }
      if (property instanceof ICollectionWrapper<?>) {
        return property;
      }
      List<Class<?>> implementedInterfaces = new ArrayList<>();
      implementedInterfaces.add(ICollectionWrapper.class);
      implementedInterfaces.addAll(Arrays.asList(property.getClass().getInterfaces()));
      return Proxy.newProxyInstance(AbstractComponentInvocationHandler.class.getClassLoader(),
          implementedInterfaces.toArray(new Class[implementedInterfaces.size()]),
          new PersistentCollectionWrapper<>((Collection<IComponent>) property, (IComponent) proxy, propertyName,
              propertyDescriptor.getCollectionDescriptor().getElementDescriptor().getComponentContract(),
              accessorFactory));
    } catch (RuntimeException re) {
      LOG.error("Error when retrieving [{}] collection property on {}",
          propertyName, proxy);
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
   * Is dirty tracking enabled.
   *
   * @return {@code true} if dirty tracking is enabled.
   */
  protected boolean isDirtyTrackingEnabled() {
    return true;
  }

  /**
   * Sets dirty tracking enabled.
   *
   * @param enabled
   *     {@code true} if enabled, {@code false} otherwise.
   */
  protected void setDirtyTrackingEnabled(boolean enabled) {
    // NO-OP;
  }

  /**
   * Creates and registers an extension instance.
   *
   * @param extensionClass
   *     the extension class.
   * @param proxy
   *     the proxy to register the extension on.
   * @return the component extension.
   */
  protected synchronized IComponentExtension<? extends IComponent> getExtensionInstance(
      Class<IComponentExtension<IComponent>> extensionClass, IComponent proxy) {
    IComponentExtension<IComponent> extension;
    if (componentExtensions == null) {
      componentExtensions = new THashMap<>(1, 1.0f);
      extension = null;
    } else {
      extension = componentExtensions.get(extensionClass);
    }
    if (extension == null) {
      extension = extensionFactory.createComponentExtension(extensionClass,
          componentDescriptor.getComponentContract(), proxy);
      componentExtensions.put(extensionClass, extension);
      configureExtension(extension);
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
   *     the proxy to get the property of.
   * @param propertyDescriptor
   *     the property descriptor to get the value for.
   * @return the property value.
   */
  @SuppressWarnings("unchecked")
  protected Object getProperty(Object proxy,
      IPropertyDescriptor propertyDescriptor) {
    if (propertyDescriptor instanceof ICollectionPropertyDescriptor) {
      return getCollectionProperty(
          proxy,
          (ICollectionPropertyDescriptor<? extends IComponent>) propertyDescriptor);
    }
    if (propertyDescriptor instanceof IReferencePropertyDescriptor) {
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
   *     the proxy to get the property of.
   * @param propertyDescriptor
   *     the property descriptor to get the value for.
   * @return the property value.
   */
  @SuppressWarnings("unchecked")
  protected Object getReferenceProperty(Object proxy,
      final IReferencePropertyDescriptor<IComponent> propertyDescriptor) {
    String propertyName = propertyDescriptor.getName();
    Object referent = straightGetProperty(proxy, propertyName);
    if (referent instanceof IPropertyChangeCapable) {
      initializeInlineTrackerIfNeeded((IPropertyChangeCapable) referent, propertyName, true);
      Set<String> delayedNestedPropertyListening = null;
      if (delayedFakePclAttachements != null) {
        delayedNestedPropertyListening = delayedFakePclAttachements
            .remove(propertyName);
      }
      if (delayedNestedPropertyListening != null) {
        Set<String> nestedPropertyListening = null;
        if (fakePclAttachements != null) {
          nestedPropertyListening = fakePclAttachements
              .get(propertyName);
        }
        if (nestedPropertyListening == null) {
          nestedPropertyListening = new THashSet<>(1);
          if (fakePclAttachements == null) {
            fakePclAttachements = new THashMap<>(1, 1.0f);
          }
          fakePclAttachements.put(propertyName, nestedPropertyListening);
        }
        for (String nestedPropertyName : delayedNestedPropertyListening) {
          ((IPropertyChangeCapable) referent).addWeakPropertyChangeListener(nestedPropertyName, createOrGetFakePcl());
          nestedPropertyListening.add(nestedPropertyName);
        }
      }
    }
    IComponentDescriptor<IComponent> referencedDescriptor = (IComponentDescriptor<IComponent>) propertyDescriptor
        .getReferencedDescriptor();
    if (referent == null
        && EntityHelper.isInlineComponentReference(propertyDescriptor)
        && !propertyDescriptor.isComputed() && propertyDescriptor.isMandatory()) {
      boolean wasDirtyTrackingEnabled = isDirtyTrackingEnabled();
      try {
        setDirtyTrackingEnabled(false);
        referent = inlineComponentFactory
            .createComponentInstance(referencedDescriptor.getComponentContract());
        storeReferenceProperty(proxy, propertyDescriptor, null, referent);
      } finally {
        setDirtyTrackingEnabled(wasDirtyTrackingEnabled);
      }
    }
    if (referent instanceof IComponent) {
      return decorateReferent((IComponent) referent, referencedDescriptor);
    }
    return referent;
  }

  /**
   * Invokes a service method on the component.
   *
   * @param proxy
   *     the component to invoke the service on.
   * @param method
   *     the method implemented by the component.
   * @param args
   *     the arguments of the method implemented by the component.
   * @return the value returned by the method execution if any.
   *
   * @throws NoSuchMethodException
   *     if no mean could be found to service the method.
   */
  @SuppressWarnings("unchecked")
  protected Object invokeServiceMethod(Object proxy, Method method,
      Object... args) throws NoSuchMethodException {
    IComponentService service = componentDescriptor.getServiceDelegate(method);
    if (service != null) {
      try {
        if (service instanceof AbstractComponentServiceDelegate<?>) {
          Method refinedMethod = service.getClass().getMethod(method.getName(),
            method.getParameterTypes());
          if (refinedMethod != null) {
            return ((AbstractComponentServiceDelegate<Object>) service)
                .executeWith(proxy, refinedMethod, args);
          }
        }
        int signatureSize = method.getParameterTypes().length + 1;
        Class<?>[] parameterTypes = new Class<?>[signatureSize];
        Object[] parameters = new Object[signatureSize];

        parameterTypes[0] = componentDescriptor.getComponentContract();
        parameters[0] = proxy;

        for (int i = 1; i < signatureSize; i++) {
          parameterTypes[i] = method.getParameterTypes()[i - 1];
          parameters[i] = args[i - 1];
        }
        return MethodUtils.invokeMethod(service, method.getName(), parameters,
            parameterTypes);
      } catch (IllegalAccessException | NoSuchMethodException ex) {
        throw new ComponentException(ex);
      } catch (InvocationTargetException ex) {
        if (ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        throw new ComponentException(ex.getCause());
      }
    }
    throw new NoSuchMethodException(method.toString());
  }

  /**
   * Whether the object is fully initialized.
   *
   * @param objectOrProxy
   *     the object to test.
   * @return true if the object is fully initialized.
   */
  protected boolean isInitialized(Object objectOrProxy) {
    return true;
  }

  /**
   * An empty hook that gets called when an component is created (still transient).
   *
   * @param proxy
   *     the proxy
   * @param entityFactory
   *     an entity factory instance which can be used to complete the     lifecycle step.
   * @param principal
   *     the principal triggering the action.
   * @param entityLifecycleHandler
   *     entityLifecycleHandler.
   */
  protected void onCreate(Object proxy, IEntityFactory entityFactory,
                          UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler) {
    registerServicesForwardingListenersIfNecessary(proxy);
  }

  /**
   * An empty hook that gets called whenever an entity is to be persisted.
   *
   * @param proxy
   *     the proxy
   * @param entityFactory
   *     an entity factory instance which can be used to complete the     lifecycle step.
   * @param principal
   *     the principal triggering the action.
   * @param entityLifecycleHandler
   *     entityLifecycleHandler.
   */
  protected void onPersist(Object proxy, IEntityFactory entityFactory,
                          UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler) {
    // defaults to no-op.
  }

  /**
   * An empty hook that gets called whenever an entity is to be updated.
   *
   * @param proxy
   *     the proxy
   * @param entityFactory
   *     an entity factory instance which can be used to complete the     lifecycle step.
   * @param principal
   *     the principal triggering the action.
   * @param entityLifecycleHandler
   *     entityLifecycleHandler.
   */
  protected void onUpdate(Object proxy, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler) {
    // defaults to no-op.
  }

  /**
   * An empty hook that gets called just before an component is deleted (delete).
   *
   * @param proxy
   *     the proxy
   * @param entityFactory
   *     an entity factory instance which can be used to complete the     lifecycle step.
   * @param principal
   *     the principal triggering the action.
   * @param entityLifecycleHandler
   *     entityLifecycleHandler.
   * @return true if the state of the component has been updated.
   */
  protected boolean onDelete(Object proxy, IEntityFactory entityFactory, UserPrincipal principal,
                   IEntityLifecycleHandler entityLifecycleHandler) {
    // defaults to no-op.
    return false;
  }

  /**
   * An empty hook that gets called when an component is loaded from the persistent store or merged back
   * from the unit of work.
   *
   * @param proxy
   *     the proxy
   */
  protected void onLoad(Object proxy) {
    registerServicesForwardingListenersIfNecessary(proxy);
  }

  /**
   * An empty hook that gets called whenever an entity is cloned to the unit of work.
   *
   * @param <E>
   *     tha actual component type.
   * @param proxy
   *     the proxy
   * @param sourceComponent
   *     the component that is the source of the cloning.
   */
  protected <E extends IComponent> void onClone(Object proxy, E sourceComponent) {
    registerServicesForwardingListenersIfNecessary(proxy);
  }

  private boolean servicesForwardingListenersRegistered = false;
  private void registerServicesForwardingListenersIfNecessary(Object proxy) {
    if (!servicesForwardingListenersRegistered) {
      servicesForwardingListenersRegistered = true;
      Collection<Class<?>> serviceContracts = componentDescriptor.getServiceContracts();
      if (serviceContracts != null) {
        for (Class<?> serviceContract : serviceContracts) {
          DependsOnHelper.registerDependsOnListeners(serviceContract, (IPropertyChangeCapable) proxy, accessorFactory);
        }
      }
    }
  }

  /**
   * Direct read access to the properties map without any other operation. Use
   * with caution only in subclasses.
   *
   * @param propertyName
   *     the property name.
   * @return the property value.
   */
  protected abstract Object retrievePropertyValue(String propertyName);

  /**
   * Refine property to store object.
   *
   * @param propertyValue
   *     the property value
   * @return the object
   */
  protected Object refinePropertyToStore(Object propertyValue) {
    if (propertyValue instanceof ICollectionWrapper<?>) {
      return ((ICollectionWrapper) propertyValue).getWrappedCollection();
    }
    return propertyValue;
  }

  /**
   * Direct write access to the properties map without any other operation. Use
   * with caution only in subclasses.
   *
   * @param propertyName
   *     the property name.
   * @param propertyValue
   *     the property value.
   */
  protected abstract void storeProperty(String propertyName, Object propertyValue);

  /**
   * Store collection property.
   *
   * @param proxy
   *     the proxy
   * @param propertyDescriptor
   *     the property descriptor
   * @param oldPropertyValue
   *     the old property value
   * @param newPropertyValue
   *     the new property value
   */
  @SuppressWarnings("unchecked")
  protected void storeCollectionProperty(Object proxy, ICollectionPropertyDescriptor<?> propertyDescriptor,
                                        Object oldPropertyValue, Object newPropertyValue) {
    String propertyName = propertyDescriptor.getName();
    if (EntityHelper.isInlineComponentReference(propertyDescriptor.getReferencedDescriptor().getElementDescriptor())) {
      if (oldPropertyValue instanceof Collection<?> && isInitialized(oldPropertyValue)) {
        for (IComponent component : (Collection<IComponent>) oldPropertyValue) {
          if (component != null) {
            component.setOwningComponent(null, null);
          }
        }
      }
      if (newPropertyValue instanceof Collection<?> && isInitialized(newPropertyValue)) {
        for (IComponent component : (Collection<IComponent>) newPropertyValue) {
          if (component != null) {
            component.setOwningComponent((IComponent) proxy, propertyDescriptor);
          }
        }
      }
    }
    storeProperty(propertyName, newPropertyValue);
  }

  /**
   * Performs necessary registration on inline components before actually
   * storing them.
   *
   * @param proxy
   *     the proxy to store the reference property for.
   * @param propertyDescriptor
   *     the reference property descriptor.
   * @param oldPropertyValue
   *     the old reference property value.
   * @param newPropertyValue
   *     the new reference property value.
   */
  protected void storeReferenceProperty(Object proxy, IReferencePropertyDescriptor<?> propertyDescriptor, Object
      oldPropertyValue, Object newPropertyValue) {
    String propertyName = propertyDescriptor.getName();

    NestedReferenceTracker referenceTracker = null;
    if (referenceTrackers != null) {
      referenceTracker = referenceTrackers.get(propertyName);
    }

    // Handle owning component.
    if (oldPropertyValue instanceof IComponent && EntityHelper.isInlineComponentReference(propertyDescriptor)
        && isInitialized(oldPropertyValue)) {
      ((IComponent) oldPropertyValue).setOwningComponent(null, null);
    }
    if (newPropertyValue instanceof IComponent && EntityHelper.isInlineComponentReference(propertyDescriptor)
        && isInitialized(newPropertyValue)) {
      ((IComponent) newPropertyValue).setOwningComponent((IComponent) proxy, propertyDescriptor);
    }

    if (oldPropertyValue instanceof IPropertyChangeCapable) {
      if (isInitialized(oldPropertyValue)) {
        if (referenceTracker != null) {
          ((IPropertyChangeCapable) oldPropertyValue).removePropertyChangeListener(referenceTracker);
        }
        Set<String> nestedPropertyListening = null;
        if (fakePclAttachements != null) {
          nestedPropertyListening = fakePclAttachements.get(propertyName);
        }
        if (nestedPropertyListening != null) {
          for (String nestedPropertyName : nestedPropertyListening) {
            ((IPropertyChangeCapable) oldPropertyValue).removePropertyChangeListener(nestedPropertyName,
                createOrGetFakePcl());
          }
        }
        if (delayedFakePclAttachements != null) {
          delayedFakePclAttachements.remove(propertyName);
        }
      }
    }
    storeProperty(propertyName, newPropertyValue);
    if (newPropertyValue instanceof IPropertyChangeCapable) {
      Set<String> nestedPropertyListening = null;
      if (fakePclAttachements != null) {
        nestedPropertyListening = fakePclAttachements.get(propertyName);
      }
      if (nestedPropertyListening != null) {
        if (isInitialized(newPropertyValue)) {
          for (String nestedPropertyName : nestedPropertyListening) {
            ((IPropertyChangeCapable) newPropertyValue).addWeakPropertyChangeListener(nestedPropertyName,
                createOrGetFakePcl());
          }
        } else {
          if (delayedFakePclAttachements == null) {
            delayedFakePclAttachements = new THashMap<>(1, 1.0f);
          }
          delayedFakePclAttachements.put(propertyName, nestedPropertyListening);
        }
      }
      if (referenceTracker == null) {
        referenceTracker = new NestedReferenceTracker(proxy, propertyName, EntityHelper.isInlineComponentReference(
            propertyDescriptor) && !propertyDescriptor.isComputed());
        if (referenceTrackers == null) {
          referenceTrackers = new THashMap<>(1, 1.0f);
        }
        referenceTrackers.put(propertyName, referenceTracker);
      }
      referenceTracker.setInitialized(false);
      initializeInlineTrackerIfNeeded((IPropertyChangeCapable) newPropertyValue, propertyName,
          // To avoid breaking lazy initialization of oldPropertyValue
          !isInitialized(oldPropertyValue) || (isInitialized(newPropertyValue) && !ObjectUtils.equals(oldPropertyValue,
              newPropertyValue)));
    } else if (referenceTracker != null) {
      if (oldPropertyValue instanceof IComponent
          // To avoid breaking lazy initialization optimisation
          && isInitialized(oldPropertyValue)) {
        for (Map.Entry<String, Object> property : ((IComponent) oldPropertyValue).straightGetProperties().entrySet()) {
          referenceTracker.propertyChange(new PropertyChangeEvent(oldPropertyValue, property.getKey(), property.getValue(), null));
        }
      }
    }
  }

  /**
   * Performs (potentially delayed due to lazy initialization) inline tracker
   * attachment.
   *
   * @param referenceProperty
   *     the reference to link the tracker to.
   * @param propertyName
   *     the property name of the tracker.
   * @param fireNestedPropertyChange
   *     Whenever the initialization is performed, does a first set of
   *     property change events be fired ?
   */
  private void initializeInlineTrackerIfNeeded(IPropertyChangeCapable referenceProperty, String propertyName, boolean fireNestedPropertyChange) {
    if (referenceProperty != null && isInitialized(referenceProperty)) {
      NestedReferenceTracker storedTracker = null;
      if (referenceTrackers != null) {
        storedTracker = referenceTrackers.get(propertyName);
      }
      if (storedTracker != null && !storedTracker.isInitialized()) {
        storedTracker.setInitialized(true);
        referenceProperty.addWeakPropertyChangeListener(storedTracker);
        if (fireNestedPropertyChange && referenceProperty instanceof IComponent) {
          for (Map.Entry<String, Object> property : ((IComponent) referenceProperty).straightGetProperties()
                                                                                    .entrySet()) {
            storedTracker.propertyChange(new PropertyChangeEvent(referenceProperty, property.getKey(),
                IPropertyChangeCapable.UNKNOWN, property.getValue()));
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
   *     the proxy to straight get the properties from.
   * @return The map of properties.
   */
  protected Map<String, Object> straightGetProperties(Object proxy) {
    Map<String, Object> allProperties = new HashMap<>();
    for (IPropertyDescriptor propertyDescriptor : componentDescriptor.getPropertyDescriptors()) {
      String propertyName = propertyDescriptor.getName();
      if (!(propertyDescriptor.isComputed() && propertyDescriptor.getPersistenceFormula() == null)) {
        allProperties.put(propertyName, straightGetProperty(proxy, propertyName));
      }
    }
    return allProperties;
  }

  /**
   * Directly get a property value out of the property store without any other
   * operation.
   *
   * @param proxy
   *     the proxy to straight get the property from.
   * @param propertyName
   *     the name of the property.
   * @return the property value or null.
   */
  protected Object straightGetProperty(Object proxy, String propertyName) {
    IPropertyDescriptor propertyDescriptor = componentDescriptor.getPropertyDescriptor(propertyName);
    if (propertyDescriptor == null || (propertyDescriptor.isComputed() && propertyDescriptor.getPersistenceFormula() == null)) {

      return null;
    }
    Object propertyValue = retrievePropertyValue(propertyName);
    if (propertyValue == null && propertyDescriptor instanceof IBooleanPropertyDescriptor) {
      return Boolean.FALSE;
    }
    return propertyValue;
  }

  /**
   * Directly set a property value to the property store without any other
   * operation.
   *
   * @param proxy
   *     the proxy to straight set the property to.
   * @param propertyName
   *     the name of the property.
   * @param newPropertyValue
   *     the property value or null.
   */
  protected void straightSetProperty(Object proxy, String propertyName, Object newPropertyValue) {
    IPropertyDescriptor propertyDescriptor = componentDescriptor.getPropertyDescriptor(propertyName);
    if (propertyDescriptor == null || (propertyDescriptor.isComputed() && propertyDescriptor.getPersistenceFormula() == null)) {
      return;
    }
    Object currentPropertyValue = straightGetProperty(proxy, propertyName);
    if (propertyDescriptor instanceof IReferencePropertyDescriptor) {
      // reference must change sometimes even if entities are equal.
      if (/* !ObjectUtils.equals(currentPropertyValue, newPropertyValue) */currentPropertyValue != newPropertyValue) {
        storeReferenceProperty(proxy, (IReferencePropertyDescriptor<?>) propertyDescriptor, currentPropertyValue,
            newPropertyValue);
      }
    } else if (propertyDescriptor instanceof ICollectionPropertyDescriptor) {
      storeCollectionProperty(proxy, (ICollectionPropertyDescriptor<?>) propertyDescriptor, currentPropertyValue,
          newPropertyValue  );
      if (currentPropertyValue != null && currentPropertyValue == newPropertyValue && isInitialized(currentPropertyValue)) {
        currentPropertyValue = Proxy.newProxyInstance
            (Thread.currentThread().getContextClassLoader(), new Class<?>[]{
                ((ICollectionPropertyDescriptor<?>) propertyDescriptor).getReferencedDescriptor().getCollectionInterface()},
            new NeverEqualsInvocationHandler(CollectionHelper.cloneCollection((Collection<?>) currentPropertyValue)));
      }
    } else {
      storeProperty(propertyName, newPropertyValue);
    }
    doFirePropertyChange(proxy, propertyName, currentPropertyValue, newPropertyValue);
  }

  private synchronized void addPropertyChangeListener(Object proxy, PropertyChangeListener listener) {
    if (listener == null) {
      return;
    }
    if (propertyChangeSupport == null) {
      propertyChangeSupport = new SinglePropertyChangeSupport(proxy);
    }
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  private synchronized void addWeakPropertyChangeListener(Object proxy, PropertyChangeListener listener) {
    if (listener == null) {
      return;
    }
    if (weakPropertyChangeSupport == null) {
      weakPropertyChangeSupport = new SingleWeakPropertyChangeSupport(proxy);
    }
    weakPropertyChangeSupport.addPropertyChangeListener(listener);
  }

  private synchronized void addPropertyChangeListener(Object proxy, String propertyName, PropertyChangeListener listener) {
    if (listener == null) {
      return;
    }
    if (propertyChangeSupport == null) {
      propertyChangeSupport = new SinglePropertyChangeSupport(proxy);
    }
    handleNestedPropertyChangeListening(proxy, propertyName);
    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  private synchronized void addWeakPropertyChangeListener(Object proxy, String propertyName, PropertyChangeListener listener) {
    if (listener == null) {
      return;
    }
    if (weakPropertyChangeSupport == null) {
      weakPropertyChangeSupport = new SingleWeakPropertyChangeSupport(proxy);
    }
    handleNestedPropertyChangeListening(proxy, propertyName);
    weakPropertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  private void handleNestedPropertyChangeListening(Object proxy, String propertyName) {
    int nestedDelimIndex = propertyName.indexOf(IAccessor.NESTED_DELIM);
    if (nestedDelimIndex >= 0) {
      String rootProperty = propertyName.substring(0, nestedDelimIndex);
      String nestedPropertyName = propertyName.substring(nestedDelimIndex + 1);
      NestedReferenceTracker referenceTracker = null;
      if (referenceTrackers != null) {
        referenceTracker = referenceTrackers.get(rootProperty);
      }
      if (referenceTracker == null) {
        IReferencePropertyDescriptor<?> rootPropertyDescriptor = (IReferencePropertyDescriptor<?>) componentDescriptor
            .getPropertyDescriptor(rootProperty);
        referenceTracker = new NestedReferenceTracker(proxy, rootProperty, EntityHelper.isInlineComponentReference(
            rootPropertyDescriptor) && !rootPropertyDescriptor.isComputed());
        if (referenceTrackers == null) {
          referenceTrackers = new THashMap<>(1, 1.0f);
        }
        referenceTrackers.put(rootProperty, referenceTracker);
      }
      Object currentRootProperty = straightGetProperty(proxy, rootProperty);
      if (currentRootProperty instanceof IPropertyChangeCapable) {
        if (isInitialized(currentRootProperty)) {
          ((IPropertyChangeCapable) currentRootProperty).addWeakPropertyChangeListener(nestedPropertyName,
              createOrGetFakePcl());
          Set<String> nestedPropertyListening = null;
          if (fakePclAttachements != null) {
            nestedPropertyListening = fakePclAttachements.get(propertyName);
          }
          if (nestedPropertyListening == null) {
            nestedPropertyListening = new THashSet<>(1);
            if (fakePclAttachements == null) {
              fakePclAttachements = new THashMap<>(1, 1.0f);
            }
            fakePclAttachements.put(rootProperty, nestedPropertyListening);
          }
          nestedPropertyListening.add(nestedPropertyName);
        } else {
          Set<String> delayedNestedPropertyListening = null;
          if (delayedFakePclAttachements != null) {
            delayedNestedPropertyListening = delayedFakePclAttachements.get(propertyName);
          }
          if (delayedNestedPropertyListening == null) {
            delayedNestedPropertyListening = new THashSet<>(1);
            if (delayedFakePclAttachements == null) {
              delayedFakePclAttachements = new THashMap<>(1, 1.0f);
            }
            delayedFakePclAttachements.put(rootProperty, delayedNestedPropertyListening);
          }
          delayedNestedPropertyListening.add(nestedPropertyName);
        }
      }
      referenceTracker.addToTrackedProperties(nestedPropertyName);
    }
  }

  @SuppressWarnings("unchecked")
  protected void addToProperty(Object proxy, ICollectionPropertyDescriptor<?> propertyDescriptor, int index, Object value) {
    String propertyName = propertyDescriptor.getName();
    Collection<Object> collectionProperty = (Collection<Object>) straightGetProperty(proxy, propertyName);
    if (value instanceof IEntity && collectionProperty.contains(value)) {
      if (collectionProperty instanceof Set<?>) {
        LOG.warn(
            "You have added twice the same element to the following collection property : {}.{}" + componentDescriptor
                .getComponentContract().getName(), propertyName);
      } else {
        throw new ComponentException(
            "Collection property does not allow duplicates : " + componentDescriptor.getComponentContract().getName() + "."
                + propertyName);
      }
    }
    try {
      if (propertyProcessorsEnabled) {
        propertyDescriptor.preprocessAdder(proxy, collectionProperty, value);
      }
      IRelationshipEndPropertyDescriptor reversePropertyDescriptor = propertyDescriptor.getReverseRelationEnd();
      if (reversePropertyDescriptor != null) {
        if (reversePropertyDescriptor instanceof IReferencePropertyDescriptor<?>) {
          accessorFactory.createPropertyAccessor(reversePropertyDescriptor.getName(),
              propertyDescriptor.getReferencedDescriptor().getElementDescriptor().getComponentContract()).setValue(
              value, proxy);
        } else if (reversePropertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
          ICollectionAccessor collectionAccessor = accessorFactory.createCollectionPropertyAccessor(
              reversePropertyDescriptor.getName(),
              propertyDescriptor.getReferencedDescriptor().getElementDescriptor().getComponentContract(),
              ((ICollectionPropertyDescriptor<?>) reversePropertyDescriptor).getCollectionDescriptor()
                                                                            .getElementDescriptor()
                                                                            .getComponentContract());
          if (collectionAccessor instanceof IModelDescriptorAware) {
            ((IModelDescriptorAware) collectionAccessor).setModelDescriptor(reversePropertyDescriptor);
          }
          collectionAccessor.addToValue(value, proxy);
        }
      }
      Collection<?> oldCollectionSnapshot = CollectionHelper.cloneCollection((Collection<?>) collectionProperty);
      boolean inserted;
      if (collectionProperty instanceof List<?> && index >= 0 && index < collectionProperty.size()) {
        ((List<Object>) collectionProperty).add(index, value);
        inserted = true;
      } else {
        inserted = collectionProperty.add(value);
      }
      if (inserted) {
        if (EntityHelper.isInlineComponentReference(propertyDescriptor.getReferencedDescriptor().getElementDescriptor())) {
          if (value != null) {
            ((IComponent) value).setOwningComponent((IComponent) proxy, propertyDescriptor);
          }
        }
        if (collectionSortEnabled) {
          inlineComponentFactory.sortCollectionProperty((IComponent) proxy, propertyName);
        }
        doFirePropertyChange(proxy, propertyName, oldCollectionSnapshot, collectionProperty);
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
    } catch (IllegalAccessException | NoSuchMethodException ex) {
      throw new ComponentException(ex);
    }
  }

  private void addToProperty(Object proxy, ICollectionPropertyDescriptor<?> propertyDescriptor, Object value) {
    addToProperty(proxy, propertyDescriptor, -1, value);
  }

  private void checkIntegrity(Object proxy) {
    checkMandatoryProperties(proxy);
    if (propertyProcessorsEnabled) {
      for (IPropertyDescriptor propertyDescriptor : componentDescriptor.getPropertyDescriptors()) {
        if (!propertyDescriptor.isComputed()) {
          propertyDescriptor.preprocessSetter(proxy, straightGetProperty(proxy, propertyDescriptor.getName()));
        }
      }
    }
  }

  private void checkMandatoryProperties(Object proxy) {
    if (propertyProcessorsEnabled) {
      for (IPropertyDescriptor propertyDescriptor : componentDescriptor.getPropertyDescriptors()) {
        if (!propertyDescriptor.isComputed()) {
          if (propertyDescriptor.isMandatory()) {
            Object newValue = straightGetProperty(proxy, propertyDescriptor.getName());
            if (newValue == null || (isInitialized(newValue) && newValue instanceof Collection<?>
                && ((Collection<?>) newValue).isEmpty())) {
              throw new MandatoryPropertyException(propertyDescriptor, proxy);
            }
          }
          if (propertyDescriptor instanceof ICollectionPropertyDescriptor<?>
              && !((ICollectionPropertyDescriptor<?>) propertyDescriptor).getReferencedDescriptor()
                                                                         .isNullElementAllowed()) {
            Object newValue = straightGetProperty(proxy, propertyDescriptor.getName());
            if (isInitialized(newValue) && newValue instanceof Collection<?>) {
              for (Object element : ((Collection<?>) newValue)) {
                if (element == null) {
                  throw new MandatoryPropertyException(propertyDescriptor, proxy);
                }
              }
            }
          }
        }
      }
    }
  }

  @SuppressWarnings({"unused", "UnusedParameters"})
  private boolean hasListeners(Object proxy, String propertyName) {
    if (computedPropertiesCache != null && computedPropertiesCache.containsKey(propertyName)) {
      // this is necessary in order to force cache re-computation
      computedPropertiesCache.remove(propertyName);
    }
    if (propertyChangeSupport != null && propertyChangeSupport.hasListeners(propertyName)) {
      PropertyChangeListener[] listeners = propertyChangeSupport.getPropertyChangeListeners(propertyName);
      if (listeners != null && listeners.length > 0) {
        return true;
      }
      listeners = propertyChangeSupport.getPropertyChangeListeners();
      for (PropertyChangeListener listener : listeners) {
        // Avoid single property change listeners and dirt trackers
        if (!(listener instanceof PropertyChangeListenerProxy || listener instanceof BeanPropertyChangeRecorder)) {
          return true;
        }
      }
    }
    if (weakPropertyChangeSupport != null && weakPropertyChangeSupport.hasListeners(propertyName)) {
      PropertyChangeListener[] listeners = weakPropertyChangeSupport.getPropertyChangeListeners(propertyName);
      if (listeners != null && listeners.length > 0) {
        return true;
      }
      listeners = weakPropertyChangeSupport.getPropertyChangeListeners();
      for (PropertyChangeListener listener : listeners) {
        if (listener instanceof NestedReferenceTracker
            && ((NestedReferenceTracker) listener).source instanceof IComponent) {
          if (!propertyName.contains(((NestedReferenceTracker) listener).referencePropertyName)
              && ((IComponent) ((NestedReferenceTracker) listener).source).hasListeners(
              ((NestedReferenceTracker) listener).referencePropertyName + "." + propertyName)) {
            // Query nested component but prevent
            // stack overflows with 1-1 relationships
            return true;
          }
        } else {
          return true;
        }
      }
    }
    return false;
  }

  @SuppressWarnings({"unused", "UnusedParameters"})
  private PropertyChangeListener[] getPropertyChangeListeners(Object proxy) {
    List<PropertyChangeListener> listeners = new ArrayList<>();
    if (propertyChangeSupport != null) {
      for (PropertyChangeListener pcl : propertyChangeSupport.getPropertyChangeListeners()) {
        // Avoid single property change listeners
        if (!(pcl instanceof PropertyChangeListenerProxy)) {
          listeners.add(pcl);
        }
      }
    }
    if (weakPropertyChangeSupport != null) {
      Collections.addAll(listeners, weakPropertyChangeSupport.getPropertyChangeListeners());
    }
    return listeners.toArray(new PropertyChangeListener[listeners.size()]);
  }

  @SuppressWarnings({"unused", "UnusedParameters"})
  private PropertyChangeListener[] getPropertyChangeListeners(Object proxy, String propertyName) {
    List<PropertyChangeListener> listeners = new ArrayList<>();
    if (propertyChangeSupport != null) {
      Collections.addAll(listeners, propertyChangeSupport.getPropertyChangeListeners(propertyName));
    }
    if (weakPropertyChangeSupport != null) {
      Collections.addAll(listeners, weakPropertyChangeSupport.getPropertyChangeListeners(propertyName));
    }
    return listeners.toArray(new PropertyChangeListener[listeners.size()]);
  }

  /**
   * Fire property change.
   *
   * @param proxy
   *     the proxy
   * @param propertyName
   *     the property name
   * @param oldValue
   *     the old value
   * @param newValue
   *     the new value
   */
  protected void firePropertyChange(Object proxy, String propertyName, Object oldValue, Object newValue) {
    Object actualNewValue = newValue;
    if (computedPropertiesCache != null && computedPropertiesCache.containsKey(propertyName)
        && oldValue != IPropertyChangeCapable.UNKNOWN) {
      computedPropertiesCache.remove(propertyName);
      actualNewValue = IPropertyChangeCapable.UNKNOWN;
    }
    doFirePropertyChange(proxy, propertyName, oldValue, actualNewValue);
    // This method supports firing nested property changes
    if (propertyName != null) {
      int lastIndexOfDelim = propertyName.lastIndexOf(IAccessor.NESTED_DELIM);
      if (lastIndexOfDelim > 0) {
        Object propertyHolder;
        try {
          propertyHolder = getAccessorFactory().createPropertyAccessor(propertyName.substring(0, lastIndexOfDelim),
              getComponentContract()).getValue(proxy);
          if (propertyHolder != null && propertyHolder instanceof IComponent) {
            ((IComponent) propertyHolder).firePropertyChange(propertyName.substring(lastIndexOfDelim + 1), oldValue,
                actualNewValue);
          }
        } catch (IllegalAccessException | NoSuchMethodException ex) {
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

  private void doFirePropertyChange(Object proxy, String propertyName, Object oldValue, Object newValue) {
    if (propertyChangeEnabled) {
      if ((oldValue == null && newValue == null) || (oldValue == newValue)) {
        return;
      }
      if (!isInitialized(oldValue) || !isInitialized(newValue)) {
        doFirePropertyChange(new PropertyChangeEvent(proxy, propertyName, IPropertyChangeCapable.UNKNOWN, newValue));
      } else {
        doFirePropertyChange(new PropertyChangeEvent(proxy, propertyName, oldValue, newValue));
      }
    }
  }

  private void doFirePropertyChange(PropertyChangeEvent evt) {
    if (propertyChangeEnabled) {
      if (delayedEvents != null) {
        if (isDirtyTrackingEnabled()) {
          delayedEvents.add(evt);
        } else {
          delayedEvents.add(new DirtyFreePropertyChangeEvent(evt));
        }
      } else {
        if (propertyChangeSupport != null) {
          propertyChangeSupport.firePropertyChange(evt);
        }
        if (weakPropertyChangeSupport != null) {
          weakPropertyChangeSupport.firePropertyChange(evt);
        }
      }
    }
  }

  private static class DirtyFreePropertyChangeEvent extends PropertyChangeEvent {

    private static final long serialVersionUID = -3661229785535176973L;

    /**
     * Instantiates a new Dirty free property change event.
     *
     * @param pce
     *     the pce
     */
    public DirtyFreePropertyChangeEvent(PropertyChangeEvent pce) {
      super(pce.getSource(), pce.getPropertyName(), pce.getOldValue(), pce.getNewValue());
    }
  }

  private boolean blockEvents() {
    if (delayedEvents == null) {
      delayedEvents = new ArrayList<>();
      return true;
    }
    return false;
  }

  private void releaseEvents() {
    if (delayedEvents != null) {
      List<PropertyChangeEvent> delayedEventsCopy = new ArrayList<>(delayedEvents);
      delayedEvents = null;
      for (PropertyChangeEvent evt : delayedEventsCopy) {
        boolean wasDirtyTrackingEnabled = isDirtyTrackingEnabled();
        try {
          if (evt instanceof DirtyFreePropertyChangeEvent) {
            setDirtyTrackingEnabled(false);
          }
          doFirePropertyChange(evt);
        } finally {
          setDirtyTrackingEnabled(wasDirtyTrackingEnabled);
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  private synchronized Object accessComputedProperty(IPropertyDescriptor propertyDescriptor, AccessorInfo accessorInfo,
                                                     Class<IComponentExtension<IComponent>> extensionClass, Object proxy,
                                                     Method method, Object... args) {
    try {
      String propertyName = propertyDescriptor.getName();
      Object computedPropertyValue = null;
      if (accessorInfo.isModifier()) {
        computedPropertyValue = getAccessorFactory().createPropertyAccessor(propertyDescriptor.getName(),
            getComponentContract()).getValue(proxy);
        Object interceptedValue = args[args.length - 1];
        if (propertyProcessorsEnabled) {
          switch (accessorInfo.getAccessorType()) {
            case SETTER:
              interceptedValue = propertyDescriptor.interceptSetter(proxy, interceptedValue);
              propertyDescriptor.preprocessSetter(proxy, interceptedValue);
              break;
            case ADDER:
              ((ICollectionPropertyDescriptor<?>) propertyDescriptor).preprocessAdder(proxy,
                  (Collection<Object>) computedPropertyValue, interceptedValue);
              break;
            case REMOVER:
              ((ICollectionPropertyDescriptor<?>) propertyDescriptor).preprocessRemover(proxy,
                  (Collection<Object>) computedPropertyValue, interceptedValue);
              break;
            default:
              break;
          }
          args[args.length - 1] = interceptedValue;
        }
      } else if (propertyDescriptor.isCacheable()) {
        if (computedPropertiesCache != null && computedPropertiesCache.containsKey(propertyName)) {
          computedPropertyValue = computedPropertiesCache.get(propertyName);
          return computedPropertyValue;
        }
      }
      IComponentExtension<? extends IComponent> extensionDelegate = getExtensionInstance(extensionClass, (IComponent) proxy);
      if (accessorInfo.isModifier()) {
        // do not change computed property value
        invokeExtensionMethod(extensionDelegate, method, args);
      } else {
        computedPropertyValue = invokeExtensionMethod(extensionDelegate, method, args);
      }
      if (accessorInfo.isModifier()) {
        Object newComputedPropertyValue = getAccessorFactory().createPropertyAccessor(propertyDescriptor.getName(),
            getComponentContract()).getValue(proxy);
        switch (accessorInfo.getAccessorType()) {
          case SETTER:
            propertyDescriptor.postprocessSetter(proxy, computedPropertyValue, newComputedPropertyValue);
            break;
          case ADDER:
            ((ICollectionPropertyDescriptor<?>) propertyDescriptor).postprocessAdder(proxy,
                (Collection<Object>) newComputedPropertyValue, args[args.length - 1]);
            break;
          case REMOVER:
            ((ICollectionPropertyDescriptor<?>) propertyDescriptor).postprocessRemover(proxy,
                (Collection<Object>) newComputedPropertyValue, args[args.length - 1]);
            break;
          default:
            break;
        }
      } else if (propertyDescriptor.isCacheable()) {
        if (computedPropertiesCache == null) {
          computedPropertiesCache = new THashMap<>(1, 1.0f);
        }
        computedPropertiesCache.put(propertyName, computedPropertyValue);
      }
      return computedPropertyValue;
    } catch (IllegalAccessException | NoSuchMethodException ex) {
      throw new ComponentException(ex);
    } catch (InvocationTargetException ex) {
      if (ex.getCause() instanceof RuntimeException) {
        throw (RuntimeException) ex.getCause();
      }
      throw new ComponentException(ex.getCause());
    }
  }

  private Object invokeExtensionMethod(IComponentExtension<? extends IComponent> componentExtension, Method method,
                                       Object... args)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    return MethodUtils.invokeMethod(componentExtension, method.getName(), args, method.getParameterTypes());
  }

  @SuppressWarnings({"unchecked", "ConstantConditions"})
  private boolean invokeLifecycleInterceptors(Object proxy, Method lifecycleMethod, Object... args) {
    String methodName = lifecycleMethod.getName()/* .intern() */;
    if (ILifecycleCapable.ON_CREATE_METHOD_NAME.equals(methodName)) {
      onCreate(proxy, (IEntityFactory) args[0], (UserPrincipal) args[1], (IEntityLifecycleHandler) args[2]);
    } else if (ILifecycleCapable.ON_PERSIST_METHOD_NAME.equals(methodName)) {
      onPersist(proxy, (IEntityFactory) args[0], (UserPrincipal) args[1], (IEntityLifecycleHandler) args[2]);
    } else if (ILifecycleCapable.ON_UPDATE_METHOD_NAME.equals(methodName)) {
      onUpdate(proxy, (IEntityFactory) args[0], (UserPrincipal) args[1], (IEntityLifecycleHandler) args[2]);
    } else if (ILifecycleCapable.ON_LOAD_METHOD_NAME.equals(methodName)) {
      onLoad(proxy);
    } else if (ILifecycleCapable.ON_CLONE_METHOD_NAME.equals(methodName)) {
      onClone(proxy, (IComponent) args[0]);
    } else if (ILifecycleCapable.ON_DELETE_METHOD_NAME.equals(methodName)) {
      onDelete(proxy, (IEntityFactory) args[0], (UserPrincipal) args[1], (IEntityLifecycleHandler) args[2]);
    }
    boolean interceptorResults = false;
    for (ILifecycleInterceptor<?> lifecycleInterceptor : componentDescriptor.getLifecycleInterceptors()) {
      int signatureSize = lifecycleMethod.getParameterTypes().length + 1;
      Class<?>[] parameterTypes = new Class<?>[signatureSize];
      Object[] parameters = new Object[signatureSize];

      parameterTypes[0] = componentDescriptor.getComponentContract();
      parameters[0] = proxy;

      for (int i = 1; i < signatureSize; i++) {
        parameterTypes[i] = lifecycleMethod.getParameterTypes()[i - 1];
        parameters[i] = args[i - 1];
      }
      try {
        Object interceptorResult = MethodUtils.invokeMethod(lifecycleInterceptor, methodName, parameters, parameterTypes);
        if (interceptorResult instanceof Boolean) {
          interceptorResults = interceptorResults || (Boolean) interceptorResult;
        }
      } catch (IllegalAccessException | NoSuchMethodException ex) {
        throw new ComponentException(ex);
      } catch (InvocationTargetException ex) {
        if (ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        throw new ComponentException(ex.getCause());
      }
    }
    // invoke lifecycle method on inline components
    for (IPropertyDescriptor propertyDescriptor : componentDescriptor
        .getPropertyDescriptors()) {
      if (propertyDescriptor instanceof IReferencePropertyDescriptor<?>
          && EntityHelper
          .isInlineComponentReference((IReferencePropertyDescriptor<IComponent>) propertyDescriptor)
          && !propertyDescriptor.isComputed()) {
        Object inlineComponent = getProperty(proxy, propertyDescriptor);
        if (inlineComponent instanceof ILifecycleCapable) {
          try {
            Object[] compArgs = null;
            if (args != null) {
              compArgs = new Object[args.length];
              for (int j = 0; j < args.length; j++) {
                // certainly onClone argument
                if (args[j] != null
                    && ((IComponent) proxy).getComponentContract().isAssignableFrom(args[j].getClass())) {
                  // we must retrieve the original inline component
                  compArgs[j] = ((IComponent) args[j]).straightGetProperty(propertyDescriptor.getName());
                } else {
                  compArgs[j] = args[j];
                }
              }
            }
            Object interceptorResult = MethodUtils.invokeMethod(
                inlineComponent, methodName, compArgs,
                lifecycleMethod.getParameterTypes());
            if (interceptorResult instanceof Boolean) {
              interceptorResults = interceptorResults
                  || (Boolean) interceptorResult;
            }
          } catch (NoSuchMethodException | IllegalAccessException ex) {
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
    switch (methodName) {
      case ILifecycleCapable.ON_PERSIST_METHOD_NAME:
        // Important to check for not null values.
        checkIntegrity(proxy);
        break;
      case ILifecycleCapable.ON_UPDATE_METHOD_NAME:
        // Important to check for not null values
        // since the checking has been delayed until persistence.
        checkMandatoryProperties(proxy);
        break;
      case ILifecycleCapable.ON_DELETE_METHOD_NAME:
        // Performs any necessary operation on internal state to mark the proxy
        // deleted and thus unusable.
        markDeleted(proxy);
        break;
      default:
        break;
    }
    return interceptorResults;
  }

  /**
   * Performs any necessary operation on internal state to mark the proxy
   * deleted and thus unusable.
   *
   * @param proxy
   *     the proxy.
   */
  protected void markDeleted(Object proxy) {
    // NO-OP.
  }

  @SuppressWarnings("unchecked")
  protected void removeFromProperty(Object proxy,
      ICollectionPropertyDescriptor<?> propertyDescriptor, Object value) {
    String propertyName = propertyDescriptor.getName();
    // The following optimization breaks bidirectional N-N relationship persistence
    // if (!isInitialized(straightGetProperty(proxy, propertyName))) {
    // return;
    // }
    Collection<Object> collectionProperty = (Collection<Object>) straightGetProperty(proxy, propertyName);
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
                .createCollectionPropertyAccessor(reversePropertyDescriptor.getName(),
                    propertyDescriptor.getReferencedDescriptor().getElementDescriptor().getComponentContract(),
                    ((ICollectionPropertyDescriptor<?>) reversePropertyDescriptor).getCollectionDescriptor()
                                                                                  .getElementDescriptor()
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
          if (EntityHelper.isInlineComponentReference(propertyDescriptor.getReferencedDescriptor().getElementDescriptor())) {
            if (value != null) {
              ((IComponent) value).setOwningComponent(null, null);
            }
          }
          doFirePropertyChange(proxy, propertyName, oldCollectionSnapshot,
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
    } catch (IllegalAccessException | NoSuchMethodException ex) {
      throw new ComponentException(ex);
    }
  }

  private synchronized void removePropertyChangeListener(
      PropertyChangeListener listener) {
    if (listener == null) {
      return;
    }
    if (propertyChangeSupport != null) {
      propertyChangeSupport.removePropertyChangeListener(listener);
    }
    if (weakPropertyChangeSupport != null) {
      weakPropertyChangeSupport.removePropertyChangeListener(listener);
    }
  }

  private synchronized void removePropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    if (listener == null) {
      return;
    }
    if (propertyChangeSupport != null) {
      propertyChangeSupport
          .removePropertyChangeListener(propertyName, listener);
    }
    if (weakPropertyChangeSupport != null) {
      weakPropertyChangeSupport.removePropertyChangeListener(propertyName,
          listener);
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

    Object oldProperty;
    try {
      oldProperty = accessorFactory.createPropertyAccessor(propertyName,
          componentDescriptor.getComponentContract()).getValue(proxy);
    } catch (IllegalAccessException | NoSuchMethodException ex) {
      throw new ComponentException(ex);
    } catch (InvocationTargetException ex) {
      if (ex.getCause() instanceof RuntimeException) {
        throw (RuntimeException) ex.getCause();
      }
      throw new ComponentException(ex.getCause());
    }
    Object actualNewProperty;
    if (propertyProcessorsEnabled) {
      actualNewProperty = propertyDescriptor
          .interceptSetter(proxy, newProperty);
    } else {
      actualNewProperty = newProperty;
    }
    if (isInitialized(oldProperty) && isInitialized(actualNewProperty)
        && ObjectUtils.equals(oldProperty, actualNewProperty)) {
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
          storeReferenceProperty(proxy,
              (IReferencePropertyDescriptor<?>) propertyDescriptor,
              oldProperty, actualNewProperty);
          if (reversePropertyDescriptor != null) {
            // It is bidirectional, so we are going to update the other end.
            if (reversePropertyDescriptor instanceof IReferencePropertyDescriptor) {
              // It's a one-to-one relationship
              if (proxy instanceof IEntity && oldProperty instanceof IEntity) {
                entityDetached((IEntity) proxy, (IEntity) oldProperty,
                    ((IRelationshipEndPropertyDescriptor) propertyDescriptor));
              }
              IAccessor reversePropertyAccessor = accessorFactory
                  .createPropertyAccessor(reversePropertyDescriptor.getName(),
                      ((IReferencePropertyDescriptor<?>) propertyDescriptor).getReferencedDescriptor()
                                                                            .getComponentContract());
              if (oldProperty != null) {
                reversePropertyAccessor.setValue(oldProperty, null);
              }
              if (actualNewProperty != null) {
                reversePropertyAccessor.setValue(actualNewProperty, proxy);
              }
            } else if (reversePropertyDescriptor instanceof ICollectionPropertyDescriptor) {
              // It's a one-to-many relationship
              ICollectionAccessor reversePropertyAccessor = accessorFactory
                  .createCollectionPropertyAccessor(reversePropertyDescriptor.getName(),
                      ((IReferencePropertyDescriptor<?>) propertyDescriptor).getReferencedDescriptor()
                                                                            .getComponentContract(),
                      ((ICollectionPropertyDescriptor<?>) reversePropertyDescriptor).getCollectionDescriptor()
                                                                                    .getElementDescriptor()
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
          Collection<?> oldCollectionSnapshot = CollectionHelper
              .cloneCollection((Collection<?>) oldProperty);
          // It's a 'many' relation end
          Collection<Object> oldPropertyElementsToRemove = new THashSet<>(1);
          Collection<Object> newPropertyElementsToAdd = new TLinkedHashSet<>(1);
          Collection<Object> propertyElementsToKeep = new THashSet<>(1);

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
              .createCollectionPropertyAccessor(propertyName, componentDescriptor.getComponentContract(),
                  ((ICollectionPropertyDescriptor<?>) propertyDescriptor).getCollectionDescriptor()
                                                                         .getElementDescriptor().getComponentContract());
          boolean oldCollectionSortEnabled = collectionSortEnabled;
          boolean oldPropertyChangeEnabled = propertyChangeEnabled;
          boolean oldPropertyProcessorsEnabled = propertyProcessorsEnabled;
          try {
            // Delay sorting for performance reasons.
            collectionSortEnabled = false;
            // Block property changes for performance reasons;
            propertyChangeEnabled = false;
            // Block property processors
            propertyProcessorsEnabled = false;
            for (Object element : oldPropertyElementsToRemove) {
              propertyAccessor.removeFromValue(proxy, element);
            }
            for (Object element : newPropertyElementsToAdd) {
              propertyAccessor.addToValue(proxy, element);
            }
            inlineComponentFactory.sortCollectionProperty((IComponent) proxy,
                propertyName);
          } finally {
            collectionSortEnabled = oldCollectionSortEnabled;
            propertyChangeEnabled = oldPropertyChangeEnabled;
            propertyProcessorsEnabled = oldPropertyProcessorsEnabled;
          }
          // if the property is a list we may restore the element order and be
          // careful not to miss one...
          if (actualNewProperty instanceof List) {
            Collection<Object> currentProperty = (Collection<Object>) oldProperty;
            if (currentProperty instanceof List) {
              // Just check that only order differs
              Set<Object> temp = new THashSet<>(currentProperty);
              temp.removeAll((List<?>) actualNewProperty);
              if (currentProperty instanceof ICollectionWrapper) {
                currentProperty = ((ICollectionWrapper) currentProperty).getWrappedCollection();
              }
              currentProperty.clear();
              currentProperty.addAll((List<?>) actualNewProperty);
              currentProperty.addAll(temp);
            }
          }
          oldProperty = oldCollectionSnapshot;
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
      } catch (IllegalAccessException | NoSuchMethodException ex) {
        throw new ComponentException(ex);
      }
    } else {
      storeProperty(propertyName, actualNewProperty);
    }
    doFirePropertyChange(proxy, propertyName, oldProperty, actualNewProperty);
    if (propertyProcessorsEnabled) {
      propertyDescriptor.postprocessSetter(proxy, oldProperty,
          actualNewProperty);
    }
  }

  private void straightSetProperties(Object proxy,
      Map<String, Object> backendProperties) {
    boolean eventsBlocked = blockEvents();
    try {
      for (Map.Entry<String, Object> propertyEntry : backendProperties
          .entrySet()) {
        straightSetProperty(proxy, propertyEntry.getKey(),
            propertyEntry.getValue());
      }
    } finally {
      if (eventsBlocked) {
        releaseEvents();
      }
    }
  }

  private PropertyChangeListener createOrGetFakePcl() {
    if (fakePcl == null) {
      fakePcl = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
          // It's fake
        }
      };
    }
    return fakePcl;
  }

  /**
   * To string.
   *
   * @param proxy
   *     the proxy
   * @return the to string
   */
  protected String toString(Object proxy) {
    try {
      String toStringPropertyName = componentDescriptor.getToStringProperty();
      Object toStringValue = accessorFactory.createPropertyAccessor(
          toStringPropertyName, componentDescriptor.getComponentContract())
          .getValue(proxy);
      if (toStringValue == null) {
        return "";
      }
      return toStringValue.toString();
    } catch (IllegalAccessException | NoSuchMethodException ex) {
      throw new ComponentException(ex);
    } catch (InvocationTargetException ex) {
      if (ex.getCause() instanceof RuntimeException) {
        throw (RuntimeException) ex.getCause();
      }
      throw new ComponentException(ex.getCause());
    }
  }

  /**
   * Gets component descriptor.
   *
   * @return the component descriptor
   */
  protected IComponentDescriptor<? extends IComponent> getComponentDescriptor() {
    return componentDescriptor;
  }

  /**
   * Gets translated property value.
   *
   * @param proxy
   *     the proxy
   * @param propertyDescriptor
   *     the property descriptor
   * @param locale
   *     the locale
   * @return the translated property value
   */
  @SuppressWarnings("unchecked")
  protected String getNlsPropertyValue(Object proxy, IStringPropertyDescriptor propertyDescriptor, Locale locale) {
    if (locale != null) {
      String barePropertyName = propertyDescriptor.getName();
      if (barePropertyName.endsWith(IComponentDescriptor.NLS_SUFFIX)) {
        barePropertyName = barePropertyName.substring(0,
            barePropertyName.length() - IComponentDescriptor.NLS_SUFFIX.length());
      }
      String nlsPropertyName = barePropertyName + IComponentDescriptor.NLS_SUFFIX;
      Set<IPropertyTranslation> translations = (Set<IPropertyTranslation>) straightGetProperty(proxy,
          AbstractComponentDescriptor.getComponentTranslationsDescriptorTemplate().getName());
      if (translations != null && isInitialized(translations)) {
        String sessionLanguage = locale.getLanguage();
        if (sessionLanguage != null) {
          for (IPropertyTranslation translation : translations) {
            if (sessionLanguage.equalsIgnoreCase(translation.getLanguage()) && barePropertyName.equals(
                translation.getPropertyName())) {
              return translation.getTranslatedValue();
            }
          }
        }
        return null;
      }
      return (String) straightGetProperty(proxy, nlsPropertyName);
    }
    return null;
  }

  /**
   * Sets translated property value.
   *
   * @param proxy
   *     the proxy
   * @param propertyDescriptor
   *     the property descriptor
   * @param translatedValue
   *     the translated value
   * @param entityFactory
   *     the entity factory
   * @param locale
   *     the locale
   */
  @SuppressWarnings("unchecked")
  protected void setNlsPropertyValue(Object proxy, IStringPropertyDescriptor propertyDescriptor,
                                     String translatedValue,
                                     IEntityFactory entityFactory, Locale locale) {
    if (locale != null) {
      String actualTranslatedValue = (String) propertyDescriptor.interceptSetter(proxy, translatedValue);
      // manually trigger interceptors
      propertyDescriptor.preprocessSetter(proxy, translatedValue);
      String barePropertyName = propertyDescriptor.getName();
      if (barePropertyName.endsWith(IComponentDescriptor.NLS_SUFFIX)) {
        barePropertyName = barePropertyName.substring(0,
            barePropertyName.length() - IComponentDescriptor.NLS_SUFFIX.length());
      }
      String nlsPropertyName = barePropertyName + IComponentDescriptor.NLS_SUFFIX;
      String oldTranslation = invokeNlsGetter(proxy, propertyDescriptor);
      Set<IPropertyTranslation> translations;
      String translationsPropertyName = AbstractComponentDescriptor.getComponentTranslationsDescriptorTemplate().getName();

      Class<? extends IComponent> translationContract = ((ICollectionPropertyDescriptor<IComponent>)
      getComponentDescriptor()
          .getPropertyDescriptor(translationsPropertyName)).getReferencedDescriptor().getElementDescriptor()
                                                           .getComponentContract();
      ICollectionAccessor translationsAccessor = getAccessorFactory().
          createCollectionPropertyAccessor(translationsPropertyName, getComponentContract(), translationContract);
      try {
        translations = translationsAccessor.getValue(proxy);
      } catch (IllegalAccessException | NoSuchMethodException ex) {
        throw new ComponentException(ex);
      } catch (InvocationTargetException ex) {
        if (ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        throw new ComponentException(ex.getCause());
      }
      String sessionLanguage = locale.getLanguage();
      IPropertyTranslation oldSessionTranslation = null;
      if (sessionLanguage != null) {
        for (IPropertyTranslation translation : translations) {
          if (sessionLanguage.equalsIgnoreCase(translation.getLanguage()) && barePropertyName.equals(
              translation.getPropertyName())) {
            oldSessionTranslation = translation;
          }
        }
      }
      // Cannot simply update the old session translation or Hibernate will not manage persistence correctly.
      if (oldSessionTranslation != null) {
        try {
          translationsAccessor.removeFromValue(proxy, oldSessionTranslation);
        } catch (IllegalAccessException | NoSuchMethodException ex) {
          throw new ComponentException(ex);
        } catch (InvocationTargetException ex) {
          if (ex.getCause() instanceof RuntimeException) {
            throw (RuntimeException) ex.getCause();
          }
          throw new ComponentException(ex.getCause());
        }
      }
      IPropertyTranslation sessionTranslation = (IPropertyTranslation) entityFactory.createComponentInstance(
          translationContract);
      sessionTranslation.setLanguage(locale.getLanguage());
      sessionTranslation.setPropertyName(barePropertyName);
      sessionTranslation.setTranslatedValue(actualTranslatedValue);
      try {
        translationsAccessor.addToValue(proxy, sessionTranslation);
      } catch (IllegalAccessException | NoSuchMethodException ex) {
        throw new ComponentException(ex);
      } catch (InvocationTargetException ex) {
        if (ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        throw new ComponentException(ex.getCause());
      }
      firePropertyChange(proxy, nlsPropertyName, oldTranslation, actualTranslatedValue);

      propertyDescriptor.postprocessSetter(proxy, oldTranslation, actualTranslatedValue);
    }
  }

  private class NestedReferenceTracker implements PropertyChangeListener {

    private final Object      source;
    private final String      referencePropertyName;
    private       boolean     enabled;
    private       boolean     initialized;
    private final boolean     referencesInlinedComponent;
    private final Set<String> trackedProperties;

    /**
     * Constructs a new {@code InlineReferenceTracker} instance.
     *
     * @param source
     *     the proxy holding the reference tracker.
     * @param referencePropertyName
     *     the name of the component to track the properties.
     * @param referencesInlineComponent
     *     is it tracking an inline component or an entity ref ?
     */
    public NestedReferenceTracker(Object source, String referencePropertyName,
        boolean referencesInlineComponent) {
      this.source = source;
      this.referencePropertyName = referencePropertyName;
      this.referencesInlinedComponent = referencesInlineComponent;
      this.enabled = true;
      this.initialized = false;
      this.trackedProperties = new THashSet<>(1);
    }

    /**
     * Adds a property to the list of tracked nested properties for this nested
     * reference tracker instance.
     *
     * @param nestedPropertyName
     *     the nested property name.
     */
    public void addToTrackedProperties(String nestedPropertyName) {
      trackedProperties.add(nestedPropertyName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      if (enabled) {
        try {
          enabled = false;
          if (source instanceof IEntity && referencesInlinedComponent) {
            // for dirtiness notification.
            // must check if the actual property change does not come from a
            // nested entity. In that case, the persistent state has not
            // changed.
            boolean chainHasEntity = false;
            String[] chain = evt.getPropertyName().split(
                "\\" + IAccessor.NESTED_DELIM);
            IComponent sourceComponent = (IComponent) evt.getSource();
            if (chain.length > 1) {
              StringBuilder chainPart = new StringBuilder();
              for (int i = 0; i < chain.length - 1 && !chainHasEntity; i++) {
                if (chainPart.length() > 0) {
                  chainPart.append(IAccessor.NESTED_DELIM);
                }
                chainPart.append(chain[i]);
                if (sourceComponent
                    .straightGetProperty(chainPart.toString()) instanceof IEntity) {
                  chainHasEntity = true;
                }
              }
            }
            if (!chainHasEntity) {
              IComponent oldComponentValue = getInlineComponentFactory().createComponentInstance(
                  sourceComponent.getComponentContract());
              oldComponentValue.straightSetProperties(sourceComponent.straightGetProperties());
              oldComponentValue.straightSetProperty(evt.getPropertyName(), evt.getOldValue());
              doFirePropertyChange(source, referencePropertyName, oldComponentValue, evt.getSource());
            }
          }
          // for ui notification
          if (!(evt.getOldValue() instanceof IComponent && (!AbstractComponentInvocationHandler.this.isInitialized(
              evt.getOldValue())
              || ((IComponent) evt.getOldValue()).getOwningComponent() == null))) { // FAKE OLD COMPONENT VALUE
            for (String trackedProperty : trackedProperties) {
              if (trackedProperty.equals(evt.getPropertyName())) {
                doFirePropertyChange(source, referencePropertyName
                    + IAccessor.NESTED_DELIM + trackedProperty,
                    evt.getOldValue(), evt.getNewValue());
              } else if (trackedProperty.startsWith(evt.getPropertyName())) {
                String remainderProperty = trackedProperty.substring(evt
                    .getPropertyName().length() + 1);
                if (remainderProperty.indexOf(IAccessor.NESTED_DELIM) >= 0) {
                  // If the remainder is a nested property, we have to take
                  // care of manually firing.
                  if (evt.getNewValue() != null) {
                    try {
                      Object newValue = getAccessorFactory()
                          .createPropertyAccessor(remainderProperty, evt.getNewValue().getClass()).getValue(
                              evt.getNewValue());
                      doFirePropertyChange(source, referencePropertyName
                          + IAccessor.NESTED_DELIM + trackedProperty,
                          IPropertyChangeCapable.UNKNOWN, newValue);
                    } catch (IllegalAccessException | NoSuchMethodException ex) {
                      throw new ComponentException(ex);
                    } catch (InvocationTargetException ex) {
                      if (ex.getTargetException() instanceof RuntimeException) {
                        throw (RuntimeException) ex.getTargetException();
                      }
                      throw new ComponentException(ex);
                    }
                  } else {
                    doFirePropertyChange(source, referencePropertyName
                        + IAccessor.NESTED_DELIM + trackedProperty,
                        IPropertyChangeCapable.UNKNOWN, null);
                  }
                }
              }
            }
          }
        } finally {
          enabled = true;
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
     *     the initialized to set.
     */
    public void setInitialized(boolean initialized) {
      this.initialized = initialized;
    }
  }

  private static final class NeverEqualsInvocationHandler implements
      InvocationHandler {

    private final Object delegate;

    private NeverEqualsInvocationHandler(Object delegate) {
      this.delegate = delegate;
    }

    /**
     * Just 'overrides' the equals method to always return false.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable {
      if (method.getName().equals("equals") && args.length == 1) {
        return false;
      }
      return method.invoke(delegate, args);
    }
  }
}
