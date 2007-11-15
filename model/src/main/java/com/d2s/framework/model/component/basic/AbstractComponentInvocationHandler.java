/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.component.basic;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang.ObjectUtils;

import com.d2s.framework.model.component.ComponentException;
import com.d2s.framework.model.component.IComponent;
import com.d2s.framework.model.component.IComponentCollectionFactory;
import com.d2s.framework.model.component.IComponentExtension;
import com.d2s.framework.model.component.IComponentExtensionFactory;
import com.d2s.framework.model.component.IComponentFactory;
import com.d2s.framework.model.component.ILifecycleCapable;
import com.d2s.framework.model.component.service.IComponentService;
import com.d2s.framework.model.component.service.ILifecycleInterceptor;
import com.d2s.framework.model.descriptor.IBooleanPropertyDescriptor;
import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IModelDescriptorAware;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.IReferencePropertyDescriptor;
import com.d2s.framework.model.descriptor.IRelationshipEndPropertyDescriptor;
import com.d2s.framework.model.descriptor.IStringPropertyDescriptor;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.util.accessor.IAccessor;
import com.d2s.framework.util.accessor.IAccessorFactory;
import com.d2s.framework.util.accessor.ICollectionAccessor;
import com.d2s.framework.util.bean.AccessorInfo;
import com.d2s.framework.util.bean.SinglePropertyChangeSupport;
import com.d2s.framework.util.collection.CollectionHelper;

/**
 * This is the core implementation of all components in the application.
 * Instances of this class serve as handlers for proxies representing the
 * components.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractComponentInvocationHandler implements
    InvocationHandler, Serializable {

  private static final long                                                            serialVersionUID = -8332414648339056836L;

  private IAccessorFactory                                                             accessorFactory;
  private PropertyChangeSupport                                                        changeSupport;
  private IComponentFactory                                                            inlineComponentFactory;
  private IComponentCollectionFactory<IComponent>                                      collectionFactory;
  private IComponentDescriptor<? extends IComponent>                                   componentDescriptor;
  private Map<Class<IComponentExtension<IComponent>>, IComponentExtension<IComponent>> componentExtensions;
  private IComponentExtensionFactory                                                   extensionFactory;

  private Set<String>                                                                  modifierMonitors;

  /**
   * Constructs a new <code>BasicComponentInvocationHandler</code> instance.
   * 
   * @param componentDescriptor
   *            The descriptor of the proxy component.
   * @param inlineComponentFactory
   *            the factory used to create inline components.
   * @param collectionFactory
   *            The factory used to create empty component collections from
   *            collection getters.
   * @param accessorFactory
   *            The factory used to access proxy properties.
   * @param extensionFactory
   *            The factory used to create component extensions based on their
   *            classes.
   */
  protected AbstractComponentInvocationHandler(
      IComponentDescriptor<IComponent> componentDescriptor,
      IComponentFactory inlineComponentFactory,
      IComponentCollectionFactory<IComponent> collectionFactory,
      IAccessorFactory accessorFactory,
      IComponentExtensionFactory extensionFactory) {
    this.componentDescriptor = componentDescriptor;
    this.inlineComponentFactory = inlineComponentFactory;
    this.collectionFactory = collectionFactory;
    this.accessorFactory = accessorFactory;
    this.extensionFactory = extensionFactory;
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
   * Handles methods invocations on the component proxy. Either :
   * <li>delegates to one of its extension if the accessed property is
   * registered as being part of an extension
   * <li>handles property access internally
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public synchronized Object invoke(Object proxy, Method method, Object[] args)
      throws Throwable {
    String methodName = method.getName();
    if ("hashCode".equals(methodName)) {
      return new Integer(computeHashCode());
    } else if ("equals".equals(methodName)) {
      return new Boolean(computeEquals((IComponent) proxy, args[0]));
    } else if ("toString".equals(methodName)) {
      return toString(proxy);
    } else if ("getContract".equals(methodName)) {
      return componentDescriptor.getComponentContract();
    } else if ("addPropertyChangeListener".equals(methodName)) {
      if (args.length == 1) {
        addPropertyChangeListener(proxy, (PropertyChangeListener) args[0]);
        return null;
      }
      addPropertyChangeListener(proxy, (String) args[0],
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
      firePropertyChange((String) args[0], args[1], args[2]);
      return null;
    } else if ("straightSetProperty".equals(methodName)) {
      straightSetProperty((String) args[0], args[1]);
      return null;
    } else if ("straightGetProperty".equals(methodName)) {
      return straightGetProperty((String) args[0]);
    } else if ("straightSetProperties".equals(methodName)) {
      straightSetProperties((Map<String, Object>) args[0]);
      return null;
    } else if ("straightGetProperties".equals(methodName)) {
      return straightGetProperties();
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
      int accessorType = accessorInfo.getAccessorType();
      IPropertyDescriptor propertyDescriptor = null;
      if (accessorType != AccessorInfo.NONE) {
        String accessedPropertyName = accessorInfo.getAccessedPropertyName();
        if (accessedPropertyName != null) {
          propertyDescriptor = componentDescriptor
              .getPropertyDescriptor(accessedPropertyName);
        }
      }
      if (propertyDescriptor != null) {
        Class extensionClass = propertyDescriptor.getDelegateClass();
        if (extensionClass != null) {
          IComponentExtension extensionDelegate = getExtensionInstance(
              extensionClass, (IComponent) proxy);
          return invokeExtensionMethod(extensionDelegate, method, args);
        }
        if (accessorInfo.isModifier()) {
          if (modifierMonitors != null && modifierMonitors.contains(methodName)) {
            return null;
          }
          if (modifierMonitors == null) {
            modifierMonitors = new HashSet<String>();
          }
          modifierMonitors.add(methodName);
        }
        try {
          switch (accessorType) {
            case AccessorInfo.GETTER:
              return getProperty(proxy, propertyDescriptor);
            case AccessorInfo.SETTER:
              setProperty(proxy, propertyDescriptor, args[0]);
              return null;
            case AccessorInfo.ADDER:
              if (args.length == 2) {
                addToProperty(proxy,
                    (ICollectionPropertyDescriptor) propertyDescriptor,
                    ((Integer) args[0]).intValue(), args[1]);
              } else {
                addToProperty(proxy,
                    (ICollectionPropertyDescriptor) propertyDescriptor, args[0]);
              }
              return null;
            case AccessorInfo.REMOVER:
              removeFromProperty(proxy,
                  (ICollectionPropertyDescriptor) propertyDescriptor, args[0]);
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
      }
    }
    throw new ComponentException(method.toString()
        + " is not supported on the component "
        + componentDescriptor.getComponentContract());
  }

  /**
   * Sets the collectionFactory.
   * 
   * @param collectionFactory
   *            the collectionFactory to set.
   */
  public void setCollectionFactory(
      IComponentCollectionFactory<IComponent> collectionFactory) {
    this.collectionFactory = collectionFactory;
  }

  /**
   * Delegate method to compute object equality.
   * 
   * @param proxy
   *            the target component to compute equality of.
   * @param another
   *            the object to compute equality against.
   * @return the computed equality.
   */
  protected abstract boolean computeEquals(IComponent proxy, Object another);

  /**
   * Delegate method to compute hashcode.
   * 
   * @return the computed hashcode.
   */
  protected abstract int computeHashCode();

  /**
   * Gives a chance to the implementor to decorate a component reference before
   * returning it when fetching association ends.
   * 
   * @param referent
   *            the component reference to decorate.
   * @param referentDescriptor
   *            the component descriptor of the referent.
   * @return the decorated component.
   */
  protected abstract IComponent decorateReferent(IComponent referent,
      IComponentDescriptor<? extends IComponent> referentDescriptor);

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
   *            the proxy to get the property of.
   * @param propertyDescriptor
   *            the property descriptor to get the value for.
   * @return the property value.
   */
  @SuppressWarnings("unchecked")
  protected Object getCollectionProperty(@SuppressWarnings("unused")
  Object proxy,
      ICollectionPropertyDescriptor<? extends IComponent> propertyDescriptor) {
    Object property = straightGetProperty(propertyDescriptor.getName());
    if (property == null) {
      property = collectionFactory.createEntityCollection(propertyDescriptor
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
    return property;
  }

  /**
   * Gets a property value.
   * 
   * @param proxy
   *            the proxy to get the property of.
   * @param propertyDescriptor
   *            the property descriptor to get the value for.
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
    Object propertyValue = straightGetProperty(propertyDescriptor.getName());
    return propertyValue;
  }

  /**
   * Gets wether this reference descriptor points to an inline component.
   * 
   * @param propertyDescriptor
   *            the reference descriptor to test.
   * @return true if this reference descriptor points to an inline component.
   */
  protected boolean isInlineComponentReference(
      IReferencePropertyDescriptor<IComponent> propertyDescriptor) {
    return !IEntity.class.isAssignableFrom(propertyDescriptor
        .getReferencedDescriptor().getComponentContract())
        && !propertyDescriptor.getReferencedDescriptor().isPurelyAbstract();
  }

  /**
   * Gets a reference property value.
   * 
   * @param proxy
   *            the proxy to get the property of.
   * @param propertyDescriptor
   *            the property descriptor to get the value for.
   * @return the property value.
   */
  protected Object getReferenceProperty(@SuppressWarnings("unused")
  Object proxy,
      final IReferencePropertyDescriptor<IComponent> propertyDescriptor) {
    IComponent property = (IComponent) straightGetProperty(propertyDescriptor
        .getName());
    if (property == null && isInlineComponentReference(propertyDescriptor)) {
      property = inlineComponentFactory
          .createComponentInstance(propertyDescriptor.getReferencedDescriptor()
              .getComponentContract());

      property.addPropertyChangeListener(new PropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent evt) {
          firePropertyChange(propertyDescriptor.getName(), null, evt
              .getSource());
          firePropertyChange(propertyDescriptor.getName() + "."
              + evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        }
      });
      storeProperty(propertyDescriptor.getName(), property);
    }
    return decorateReferent(property, propertyDescriptor
        .getReferencedDescriptor());
  }

  /**
   * Invokes a service method on the component.
   * 
   * @param proxy
   *            the component to invoke the service on.
   * @param method
   *            the method implemented by the component.
   * @param args
   *            the arguments of the method implemented by the component.
   * @return the value returned by the method execution if any.
   * @throws NoSuchMethodException
   *             if no mean could be found to service the method.
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
   *            the object to test.
   * @return true if the object is fully initialized.
   */
  protected boolean isInitialized(@SuppressWarnings("unused")
  Object objectOrProxy) {
    return true;
  }

  /**
   * Direct read access to the properties map without any other operation. Use
   * with caution only in subclasses.
   * 
   * @param propertyName
   *            the property name.
   * @return the property value.
   */
  protected abstract Object retrievePropertyValue(String propertyName);

  /**
   * Direct write access to the properties map without any other operation. Use
   * with caution only in subclasses.
   * 
   * @param propertyName
   *            the property name.
   * @param propertyValue
   *            the property value.
   */
  protected abstract void storeProperty(String propertyName,
      Object propertyValue);

  /**
   * Directly get a property value out of the property store without any other
   * operation.
   * 
   * @param propertyName
   *            the name of the property.
   * @return the property value or null.
   */
  protected Object straightGetProperty(String propertyName) {
    Object propertyValue = retrievePropertyValue(propertyName);
    if (propertyValue == null
        && componentDescriptor.getPropertyDescriptor(propertyName) instanceof IBooleanPropertyDescriptor) {
      return Boolean.FALSE;
    }
    return propertyValue;
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

  @SuppressWarnings("unchecked")
  private void addToProperty(Object proxy,
      ICollectionPropertyDescriptor propertyDescriptor, int index, Object value) {
    String propertyName = propertyDescriptor.getName();
    try {
      Collection collectionProperty = (Collection) accessorFactory
          .createPropertyAccessor(propertyName,
              componentDescriptor.getComponentContract()).getValue(proxy);
      propertyDescriptor.preprocessAdder(proxy, collectionProperty, value);
      IRelationshipEndPropertyDescriptor reversePropertyDescriptor = propertyDescriptor
          .getReverseRelationEnd();
      if (reversePropertyDescriptor != null) {
        if (reversePropertyDescriptor instanceof IReferencePropertyDescriptor) {
          accessorFactory.createPropertyAccessor(
              reversePropertyDescriptor.getName(),
              propertyDescriptor.getReferencedDescriptor()
                  .getElementDescriptor().getComponentContract()).setValue(
              value, proxy);
        } else if (reversePropertyDescriptor instanceof ICollectionPropertyDescriptor) {
          ICollectionAccessor collectionAccessor = accessorFactory
              .createCollectionPropertyAccessor(reversePropertyDescriptor
                  .getName(), propertyDescriptor.getReferencedDescriptor()
                  .getElementDescriptor().getComponentContract(),
                  ((ICollectionPropertyDescriptor) reversePropertyDescriptor)
                      .getCollectionDescriptor().getElementDescriptor()
                      .getComponentContract());
          if (collectionAccessor instanceof IModelDescriptorAware) {
            ((IModelDescriptorAware) collectionAccessor)
                .setModelDescriptor(reversePropertyDescriptor);
          }
          collectionAccessor.addToValue(value, proxy);
        }
      }
      Collection oldCollectionSnapshot = CollectionHelper
          .cloneCollection((Collection<?>) collectionProperty);
      boolean inserted = false;
      if (collectionProperty instanceof List && index >= 0
          && index < collectionProperty.size()) {
        ((List) collectionProperty).add(index, value);
        inserted = true;
      } else {
        inserted = collectionProperty.add(value);
      }
      if (inserted) {
        firePropertyChange(propertyName, oldCollectionSnapshot,
            collectionProperty);
        propertyDescriptor.postprocessAdder(proxy, collectionProperty, value);
      }
    } catch (IllegalAccessException ex) {
      // This cannot happen but throw anyway.
      throw new ComponentException(ex);
    } catch (InvocationTargetException ex) {
      // This cannot happen but throw anyway.
      throw new ComponentException(ex.getCause());
    } catch (NoSuchMethodException ex) {
      // This cannot happen but throw anyway.
      throw new ComponentException(ex);
    }
  }

  @SuppressWarnings("unchecked")
  private void addToProperty(Object proxy,
      ICollectionPropertyDescriptor propertyDescriptor, Object value) {
    addToProperty(proxy, propertyDescriptor, -1, value);
  }

  private void checkIntegrity(Object proxy) {
    for (IPropertyDescriptor propertyDescriptor : componentDescriptor
        .getPropertyDescriptors()) {
      propertyDescriptor.checkValueIntegrity(proxy,
          straightGetProperty(propertyDescriptor.getName()));
    }
  }

  private void firePropertyChange(String propertyName, Object oldValue,
      Object newValue) {
    if (changeSupport == null || (oldValue == null && newValue == null)) {
      return;
    }
    if (!isInitialized(oldValue)) {
      changeSupport.firePropertyChange(propertyName, null, newValue);
    } else {
      changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
  }

  private synchronized IComponentExtension<? extends IComponent> getExtensionInstance(
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
      componentExtensions.put(extensionClass, extension);
    }
    return extension;
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
      throw new ComponentException(ex.getCause());
    } catch (NoSuchMethodException ex) {
      throw new ComponentException(ex);
    }
  }

  @SuppressWarnings("unchecked")
  private boolean invokeLifecycleInterceptors(Object proxy,
      Method lifecycleMethod, Object[] args) {
    if (ILifecycleCapable.ON_PERSIST_METHOD_NAME.equals(lifecycleMethod
        .getName())
        || ILifecycleCapable.ON_UPDATE_METHOD_NAME.equals(lifecycleMethod
            .getName())) {
      checkIntegrity(proxy);
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
        interceptorResults = interceptorResults
            || ((Boolean) MethodUtils.invokeMethod(lifecycleInterceptor,
                lifecycleMethod.getName(), parameters, parameterTypes))
                .booleanValue();
      } catch (IllegalAccessException ex) {
        throw new ComponentException(ex);
      } catch (InvocationTargetException ex) {
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
        if (inlineComponent != null) {
          try {
            interceptorResults = interceptorResults
                || ((Boolean) MethodUtils.invokeMethod(inlineComponent,
                    lifecycleMethod.getName(), args, lifecycleMethod
                        .getParameterTypes())).booleanValue();
          } catch (NoSuchMethodException ex) {
            throw new ComponentException(ex);
          } catch (IllegalAccessException ex) {
            throw new ComponentException(ex);
          } catch (InvocationTargetException ex) {
            throw new ComponentException(ex);
          }
        }
      }
    }
    return interceptorResults;
  }

  private void removeFromProperty(Object proxy,
      ICollectionPropertyDescriptor<?> propertyDescriptor, Object value) {
    String propertyName = propertyDescriptor.getName();
    if (!isInitialized(straightGetProperty(propertyName))) {
      return;
    }
    try {
      Collection<?> collectionProperty = (Collection<?>) accessorFactory
          .createPropertyAccessor(propertyName,
              componentDescriptor.getComponentContract()).getValue(proxy);
      propertyDescriptor.preprocessRemover(proxy, collectionProperty, value);
      if (collectionProperty.contains(value)) {
        IRelationshipEndPropertyDescriptor reversePropertyDescriptor = propertyDescriptor
            .getReverseRelationEnd();
        if (reversePropertyDescriptor != null) {
          if (reversePropertyDescriptor instanceof IReferencePropertyDescriptor) {
            accessorFactory.createPropertyAccessor(
                reversePropertyDescriptor.getName(),
                propertyDescriptor.getReferencedDescriptor()
                    .getElementDescriptor().getComponentContract()).setValue(
                value, null);
          } else if (reversePropertyDescriptor instanceof ICollectionPropertyDescriptor) {
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
          propertyDescriptor.postprocessRemover(proxy, collectionProperty,
              value);
        }
      }
    } catch (IllegalAccessException ex) {
      // This cannot happen but throw anyway.
      throw new ComponentException(ex);
    } catch (InvocationTargetException ex) {
      // This cannot happen but throw anyway.
      throw new ComponentException(ex.getCause());
    } catch (NoSuchMethodException ex) {
      // This cannot happen but throw anyway.
      throw new ComponentException(ex);
    }
  }

  private synchronized void removePropertyChangeListener(
      PropertyChangeListener listener) {
    if (listener == null || changeSupport == null) {
      return;
    }
    changeSupport.removePropertyChangeListener(listener);
  }

  private synchronized void removePropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    if (listener == null || changeSupport == null) {
      return;
    }
    changeSupport.removePropertyChangeListener(propertyName, listener);
  }

  @SuppressWarnings("unchecked")
  private void setProperty(Object proxy,
      IPropertyDescriptor propertyDescriptor, Object newProperty) {
    String propertyName = propertyDescriptor.getName();

    Object actualNewProperty = newProperty;
    if (newProperty != null
        && propertyDescriptor instanceof IStringPropertyDescriptor
        && ((IStringPropertyDescriptor) propertyDescriptor).isUpperCase()) {
      actualNewProperty = ((String) newProperty).toUpperCase();
    }

    Object oldProperty = null;
    try {
      oldProperty = accessorFactory.createPropertyAccessor(propertyName,
          componentDescriptor.getComponentContract()).getValue(proxy);
    } catch (IllegalAccessException ex) {
      throw new ComponentException(ex);
    } catch (InvocationTargetException ex) {
      throw new ComponentException(ex.getCause());
    } catch (NoSuchMethodException ex) {
      throw new ComponentException(ex);
    }
    if (ObjectUtils.equals(oldProperty, actualNewProperty)) {
      return;
    }
    propertyDescriptor.preprocessSetter(proxy, oldProperty, actualNewProperty);
    if (propertyDescriptor instanceof IRelationshipEndPropertyDescriptor) {
      // It's a relation end
      IRelationshipEndPropertyDescriptor reversePropertyDescriptor = ((IRelationshipEndPropertyDescriptor) propertyDescriptor)
          .getReverseRelationEnd();
      try {
        if (propertyDescriptor instanceof IReferencePropertyDescriptor) {
          // It's a 'one' relation end
          storeProperty(propertyName, actualNewProperty);
          if (reversePropertyDescriptor != null) {
            // It is bidirectionnal, so we are going to update the other end.
            if (reversePropertyDescriptor instanceof IReferencePropertyDescriptor) {
              // It's a one-to-one relationship
              IAccessor reversePropertyAccessor = accessorFactory
                  .createPropertyAccessor(reversePropertyDescriptor.getName(),
                      ((IReferencePropertyDescriptor) propertyDescriptor)
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
                      ((IReferencePropertyDescriptor) propertyDescriptor)
                          .getReferencedDescriptor().getComponentContract(),
                      ((ICollectionPropertyDescriptor) reversePropertyDescriptor)
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
          Collection<Object> newPropertyElementsToAdd = new HashSet<Object>();
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
                  ((ICollectionPropertyDescriptor) propertyDescriptor)
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
            Collection currentProperty = propertyAccessor.getValue(proxy);
            if (currentProperty instanceof List) {
              // Just check the only order differs
              Set<Object> temp = new HashSet<Object>(currentProperty);
              temp.removeAll((List<?>) actualNewProperty);
              currentProperty.clear();
              currentProperty.addAll((List<?>) actualNewProperty);
              currentProperty.addAll(temp);
            }
          }
        }
      } catch (IllegalAccessException ex) {
        throw new ComponentException(ex);
      } catch (InvocationTargetException ex) {
        throw new ComponentException(ex.getCause());
      } catch (NoSuchMethodException ex) {
        throw new ComponentException(ex);
      }
    } else {
      storeProperty(propertyName, actualNewProperty);
    }
    firePropertyChange(propertyName, oldProperty, actualNewProperty);
    propertyDescriptor.postprocessSetter(proxy, oldProperty, actualNewProperty);
  }

  private Map<String, Object> straightGetProperties() {
    Map<String, Object> allProperties = new HashMap<String, Object>();
    for (IPropertyDescriptor propertyDescriptor : componentDescriptor
        .getPropertyDescriptors()) {
      allProperties.put(propertyDescriptor.getName(),
          retrievePropertyValue(propertyDescriptor.getName()));
    }
    return allProperties;
  }

  private void straightSetProperties(Map<String, Object> backendProperties) {
    for (Map.Entry<String, Object> propertyEntry : backendProperties.entrySet()) {
      straightSetProperty(propertyEntry.getKey(), propertyEntry.getValue());
    }
  }

  @SuppressWarnings("unchecked")
  private void straightSetProperty(String propertyName, Object newPropertyValue) {
    Object currentPropertyValue = straightGetProperty(propertyName);
    storeProperty(propertyName, newPropertyValue);
    if (componentDescriptor.getPropertyDescriptor(propertyName) instanceof ICollectionPropertyDescriptor) {
      ICollectionPropertyDescriptor propertyDescriptor = (ICollectionPropertyDescriptor) componentDescriptor
          .getPropertyDescriptor(propertyName);
      if (currentPropertyValue != null) {
        currentPropertyValue = Proxy.newProxyInstance(Thread.currentThread()
            .getContextClassLoader(), new Class[] {propertyDescriptor
            .getReferencedDescriptor().getCollectionInterface()},
            new NeverEqualsInvocationHandler(currentPropertyValue));
      }
    }
    firePropertyChange(propertyName, currentPropertyValue, newPropertyValue);
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
      throw new ComponentException(ex);
    } catch (NoSuchMethodException ex) {
      throw new ComponentException(ex);
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
    public Object invoke(@SuppressWarnings("unused")
    Object proxy, Method method, Object[] args) throws Throwable {
      if (method.getName().equals("equals") && args.length == 1) {
        return new Boolean(false);
      }
      return method.invoke(delegate, args);
    }
  }
}
