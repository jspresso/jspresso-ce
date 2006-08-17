/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity.basic;

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
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.IReferencePropertyDescriptor;
import com.d2s.framework.model.descriptor.IRelationshipEndPropertyDescriptor;
import com.d2s.framework.model.descriptor.entity.IEntityDescriptor;
import com.d2s.framework.model.entity.EntityException;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IEntityCollectionFactory;
import com.d2s.framework.model.entity.IEntityExtension;
import com.d2s.framework.model.entity.IEntityExtensionFactory;
import com.d2s.framework.model.entity.IEntityLifecycle;
import com.d2s.framework.model.integrity.ICollectionIntegrityProcessor;
import com.d2s.framework.model.integrity.IPropertyIntegrityProcessor;
import com.d2s.framework.model.integrity.IntegrityException;
import com.d2s.framework.model.service.IComponentService;
import com.d2s.framework.model.service.ILifecycleInterceptor;
import com.d2s.framework.util.accessor.IAccessor;
import com.d2s.framework.util.accessor.IAccessorFactory;
import com.d2s.framework.util.accessor.ICollectionAccessor;
import com.d2s.framework.util.bean.AccessorInfo;
import com.d2s.framework.util.collection.CollectionHelper;

/**
 * This is the core implementation of all entities in the application. Instances
 * of this class serve as handlers for proxies representing the entities.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicEntityInvocationHandler implements InvocationHandler,
    Serializable {

  private static final long            serialVersionUID = 6078989823404409653L;

  private IEntityDescriptor            entityDescriptor;
  private PropertyChangeSupport        changeSupport;
  private Map<String, Object>          properties;
  private Map<Class, IEntityExtension> entityExtensions;
  private IEntityCollectionFactory     collectionFactory;
  private IAccessorFactory             accessorFactory;
  private IEntityExtensionFactory      extensionFactory;

  private Set<String>                  modifierMonitors;

  /**
   * Constructs a new <code>BasicEntityInvocationHandler</code> instance.
   * 
   * @param entityDescriptor
   *          The descriptor of the proxy entity.
   * @param collectionFactory
   *          The factory used to create empty entity collections from
   *          collection getters.
   * @param accessorFactory
   *          The factory used to access proxy properties.
   * @param extensionFactory
   *          The factory used to create entity extensions based on their
   *          classes.
   */
  protected BasicEntityInvocationHandler(IEntityDescriptor entityDescriptor,
      IEntityCollectionFactory collectionFactory,
      IAccessorFactory accessorFactory, IEntityExtensionFactory extensionFactory) {
    this.properties = createPropertyMap();
    this.entityDescriptor = entityDescriptor;
    this.collectionFactory = collectionFactory;
    this.accessorFactory = accessorFactory;
    this.extensionFactory = extensionFactory;
  }

  /**
   * Handles methods invocations on the entity proxy. Either :
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
      return new Integer(hashCode((IEntity) proxy));
    } else if ("equals".equals(methodName)) {
      return new Boolean(equals((IEntity) proxy, args[0]));
    } else if ("toString".equals(methodName)) {
      return toString((IEntity) proxy);
    } else if ("getContract".equals(methodName)) {
      return entityDescriptor.getComponentContract();
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
    } else if ("isPersistent".equals(methodName)) {
      return new Boolean(((IEntity) proxy).getVersion() != null);
    } else {
      boolean isLifecycleMethod = false;
      try {
        isLifecycleMethod = IEntityLifecycle.class.getMethod(methodName, method
            .getParameterTypes()) != null;
      } catch (NoSuchMethodException ignored) {
        // this is certainly normal.
      }
      if (isLifecycleMethod) {
        return new Boolean(invokeLifecycleInterceptors(proxy, method, args));
      }
      AccessorInfo accessorInfo = new AccessorInfo(method);
      int accessorType = accessorInfo.getAccessorType();
      if (accessorType != AccessorInfo.NONE) {
        String accessedPropertyName = accessorInfo.getAccessedPropertyName();
        if (accessedPropertyName != null) {
          IPropertyDescriptor propertyDescriptor = entityDescriptor
              .getPropertyDescriptor(accessedPropertyName);
          if (propertyDescriptor != null) {
            Class extensionClass = propertyDescriptor.getDelegateClass();
            if (extensionClass != null) {
              IEntityExtension extensionDelegate = getExtensionInstance(
                  extensionClass, proxy);
              return invokeExtensionMethod(extensionDelegate, method, args);
            }
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
                        (ICollectionPropertyDescriptor) propertyDescriptor,
                        args[0]);
                  }
                  return null;
                case AccessorInfo.REMOVER:
                  removeFromProperty(proxy,
                      (ICollectionPropertyDescriptor) propertyDescriptor,
                      args[0]);
                  return null;
                default:
                  break;
              }
            } finally {
              if (modifierMonitors != null && accessorInfo.isModifier()) {
                modifierMonitors.remove(methodName);
              }
            }
          }
        }
      } else {
        IComponentService service = entityDescriptor.getServiceDelegate(method);
        if (service != null) {
          return invokeServiceMethod(service, proxy, method, args);
        }
      }
    }
    throw new EntityException(method.toString()
        + " is not supported on the entity "
        + entityDescriptor.getComponentContract());
  }

  /**
   * Wether the object is fully initialized.
   * 
   * @param objectOrProxy
   *          the object to test.
   * @return true if the object is fully initialized.
   */
  protected boolean isInitialized(@SuppressWarnings("unused")
  Object objectOrProxy) {
    return true;
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
  protected Object getProperty(Object proxy,
      IPropertyDescriptor propertyDescriptor) {
    if (propertyDescriptor instanceof ICollectionPropertyDescriptor) {
      return getCollectionProperty(proxy,
          (ICollectionPropertyDescriptor) propertyDescriptor);
    } else if (propertyDescriptor instanceof IReferencePropertyDescriptor) {
      return getReferenceProperty(proxy,
          (IReferencePropertyDescriptor) propertyDescriptor);
    }
    return properties.get(propertyDescriptor.getName());
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
  protected Object getCollectionProperty(@SuppressWarnings("unused")
  Object proxy, ICollectionPropertyDescriptor propertyDescriptor) {
    Object property = properties.get(propertyDescriptor.getName());
    if (property == null) {
      property = collectionFactory.createEntityCollection(propertyDescriptor
          .getReferencedDescriptor().getCollectionInterface());
      storeProperty(propertyDescriptor.getName(), property);
    }
    return property;
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
  protected Object getReferenceProperty(@SuppressWarnings("unused")
  Object proxy, IReferencePropertyDescriptor propertyDescriptor) {
    return properties.get(propertyDescriptor.getName());
  }

  @SuppressWarnings("unchecked")
  private void setProperty(Object proxy,
      IPropertyDescriptor propertyDescriptor, Object newProperty)
      throws IntegrityException {
    String propertyName = propertyDescriptor.getName();

    Object oldProperty = null;
    try {
      oldProperty = accessorFactory.createPropertyAccessor(propertyName,
          entityDescriptor.getComponentContract()).getValue(proxy);
    } catch (IllegalAccessException ex) {
      throw new EntityException(ex);
    } catch (InvocationTargetException ex) {
      throw new EntityException(ex.getCause());
    } catch (NoSuchMethodException ex) {
      throw new EntityException(ex);
    }
    if (ObjectUtils.equals(oldProperty, newProperty)) {
      return;
    }
    preprocessSetter(proxy, propertyName, oldProperty, newProperty);
    if (propertyDescriptor instanceof IRelationshipEndPropertyDescriptor) {
      // It's a relation end
      IRelationshipEndPropertyDescriptor reversePropertyDescriptor = ((IRelationshipEndPropertyDescriptor) propertyDescriptor)
          .getReverseRelationEnd();
      try {
        if (propertyDescriptor instanceof IReferencePropertyDescriptor) {
          // It's a 'one' relation end
          storeProperty(propertyName, newProperty);
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
              if (newProperty != null) {
                reversePropertyAccessor.setValue(newProperty, proxy);
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
              if (oldProperty != null) {
                reversePropertyAccessor.removeFromValue(oldProperty, proxy);
              }
              if (newProperty != null) {
                reversePropertyAccessor.addToValue(newProperty, proxy);
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
          if (newProperty != null) {
            newPropertyElementsToAdd.addAll((Collection<?>) newProperty);
          }
          propertyElementsToKeep.retainAll(newPropertyElementsToAdd);
          oldPropertyElementsToRemove.removeAll(propertyElementsToKeep);
          newPropertyElementsToAdd.removeAll(propertyElementsToKeep);
          ICollectionAccessor propertyAccessor = accessorFactory
              .createCollectionPropertyAccessor(propertyDescriptor.getName(),
                  entityDescriptor.getComponentContract(),
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
          if (newProperty instanceof List) {
            Collection currentProperty = propertyAccessor.getValue(proxy);
            if (currentProperty instanceof List) {
              // Just check the only order differs
              Set<Object> temp = new HashSet<Object>(currentProperty);
              temp.removeAll((List<?>) newProperty);
              currentProperty.clear();
              currentProperty.addAll((List<?>) newProperty);
              currentProperty.addAll(temp);
            }
          }
        }
      } catch (IllegalAccessException ex) {
        throw new EntityException(ex);
      } catch (InvocationTargetException ex) {
        throw new EntityException(ex.getCause());
      } catch (NoSuchMethodException ex) {
        throw new EntityException(ex);
      }
    } else {
      storeProperty(propertyName, newProperty);
    }
    firePropertyChange(propertyName, oldProperty, newProperty);
    postprocessSetter(proxy, propertyName, oldProperty, newProperty);
  }

  @SuppressWarnings("unchecked")
  private void addToProperty(Object proxy,
      ICollectionPropertyDescriptor propertyDescriptor, Object value)
      throws IntegrityException {
    addToProperty(proxy, propertyDescriptor, -1, value);
  }

  @SuppressWarnings("unchecked")
  private void addToProperty(Object proxy,
      ICollectionPropertyDescriptor propertyDescriptor, int index, Object value)
      throws IntegrityException {
    String propertyName = propertyDescriptor.getName();
    try {
      Collection collectionProperty = (Collection) accessorFactory
          .createPropertyAccessor(propertyName,
              entityDescriptor.getComponentContract()).getValue(proxy);
      preprocessAdder(proxy, propertyName, collectionProperty, value);
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
          accessorFactory.createCollectionPropertyAccessor(
              reversePropertyDescriptor.getName(),
              propertyDescriptor.getReferencedDescriptor()
                  .getElementDescriptor().getComponentContract(),
              ((ICollectionPropertyDescriptor) reversePropertyDescriptor)
                  .getCollectionDescriptor().getElementDescriptor()
                  .getComponentContract()).addToValue(value, proxy);
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
        postprocessAdder(proxy, propertyName, collectionProperty, value);
      }
    } catch (IllegalAccessException ex) {
      // This cannot happen but throw anyway.
      throw new EntityException(ex);
    } catch (InvocationTargetException ex) {
      // This cannot happen but throw anyway.
      throw new EntityException(ex.getCause());
    } catch (NoSuchMethodException ex) {
      // This cannot happen but throw anyway.
      throw new EntityException(ex);
    }
  }

  private void removeFromProperty(Object proxy,
      ICollectionPropertyDescriptor propertyDescriptor, Object value)
      throws IntegrityException {
    String propertyName = propertyDescriptor.getName();
    if (!isInitialized(straightGetProperty(propertyName))) {
      return;
    }
    try {
      Collection collectionProperty = (Collection) accessorFactory
          .createPropertyAccessor(propertyName,
              entityDescriptor.getComponentContract()).getValue(proxy);
      preprocessRemover(proxy, propertyName, collectionProperty, value);
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
            accessorFactory.createCollectionPropertyAccessor(
                reversePropertyDescriptor.getName(),
                propertyDescriptor.getReferencedDescriptor()
                    .getElementDescriptor().getComponentContract(),
                ((ICollectionPropertyDescriptor) reversePropertyDescriptor)
                    .getCollectionDescriptor().getElementDescriptor()
                    .getComponentContract()).removeFromValue(value, proxy);
          }
        }
        Collection oldCollectionSnapshot = CollectionHelper
            .cloneCollection((Collection<?>) collectionProperty);
        if (collectionProperty.remove(value)) {
          firePropertyChange(propertyName, oldCollectionSnapshot,
              collectionProperty);
          postprocessRemover(proxy, propertyName, collectionProperty, value);
        }
      }
    } catch (IllegalAccessException ex) {
      // This cannot happen but throw anyway.
      throw new EntityException(ex);
    } catch (InvocationTargetException ex) {
      // This cannot happen but throw anyway.
      throw new EntityException(ex.getCause());
    } catch (NoSuchMethodException ex) {
      // This cannot happen but throw anyway.
      throw new EntityException(ex);
    }
  }

  private synchronized IEntityExtension getExtensionInstance(
      Class extensionClass, Object proxy) {
    IEntityExtension extension;
    if (entityExtensions == null) {
      entityExtensions = new HashMap<Class, IEntityExtension>();
      extension = null;
    } else {
      extension = entityExtensions.get(extensionClass);
    }
    if (extension == null) {
      extension = extensionFactory.createEntityExtension(extensionClass,
          entityDescriptor.getComponentContract(), (IEntity) proxy);
      entityExtensions.put(extensionClass, extension);
    }
    return extension;
  }

  private Object invokeExtensionMethod(IEntityExtension entityExtension,
      Method method, Object[] args) {
    try {
      return MethodUtils.invokeMethod(entityExtension, method.getName(), args,
          method.getParameterTypes());
    } catch (IllegalAccessException ex) {
      throw new EntityException(ex);
    } catch (InvocationTargetException ex) {
      throw new EntityException(ex.getCause());
    } catch (NoSuchMethodException ex) {
      throw new EntityException(ex);
    }
  }

  private Object invokeServiceMethod(IComponentService service, Object proxy,
      Method method, Object[] args) {
    int signatureSize = method.getParameterTypes().length + 1;
    Class[] parameterTypes = new Class[signatureSize];
    Object[] parameters = new Object[signatureSize];

    parameterTypes[0] = entityDescriptor.getComponentContract();
    parameters[0] = proxy;

    for (int i = 1; i < signatureSize; i++) {
      parameterTypes[i] = method.getParameterTypes()[i - 1];
      parameters[i] = args[i - 1];
    }
    try {
      return MethodUtils.invokeMethod(service, method.getName(), parameters,
          parameterTypes);
    } catch (IllegalAccessException ex) {
      throw new EntityException(ex);
    } catch (InvocationTargetException ex) {
      throw new EntityException(ex.getCause());
    } catch (NoSuchMethodException ex) {
      throw new EntityException(ex);
    }
  }

  private String toString(IEntity proxy) {
    try {
      String toStringPropertyName = entityDescriptor.getToStringProperty();
      Object toStringValue = accessorFactory.createPropertyAccessor(
          toStringPropertyName, entityDescriptor.getComponentContract())
          .getValue(proxy);
      if (toStringValue == null) {
        return "";
      }
      return toStringValue.toString();
    } catch (IllegalAccessException ex) {
      throw new EntityException(ex);
    } catch (InvocationTargetException ex) {
      throw new EntityException(ex);
    } catch (NoSuchMethodException ex) {
      throw new EntityException(ex);
    }
  }

  private int hashCode(IEntity proxy) {
    if (proxy.getId() == null) {
      throw new NullPointerException(
          "Id must be assigned on the entity before its hashcode can be used.");
    }
    return new HashCodeBuilder(3, 17).append(proxy.getId()).toHashCode();
  }

  private boolean equals(IEntity proxy, Object another) {
    if (proxy == another) {
      return true;
    }
    if (proxy.getId() == null) {
      return false;
    }
    if (another instanceof IEntity) {
      return new EqualsBuilder().append(proxy.getContract(),
          ((IEntity) another).getContract()).append(proxy.getId(),
          ((IEntity) another).getId()).isEquals();
    }
    return false;
  }

  private synchronized void addPropertyChangeListener(Object proxy,
      String propertyName, PropertyChangeListener listener) {
    if (listener == null) {
      return;
    }
    if (changeSupport == null) {
      changeSupport = new PropertyChangeSupport(proxy);
    }
    changeSupport.addPropertyChangeListener(propertyName, listener);
  }

  private synchronized void removePropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    if (listener == null || changeSupport == null) {
      return;
    }
    changeSupport.removePropertyChangeListener(propertyName, listener);
  }

  private synchronized void addPropertyChangeListener(Object proxy,
      PropertyChangeListener listener) {
    if (listener == null) {
      return;
    }
    if (changeSupport == null) {
      changeSupport = new PropertyChangeSupport(proxy);
    }
    changeSupport.addPropertyChangeListener(listener);
  }

  private synchronized void removePropertyChangeListener(
      PropertyChangeListener listener) {
    if (listener == null || changeSupport == null) {
      return;
    }
    changeSupport.removePropertyChangeListener(listener);
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

  /**
   * Sets the collectionFactory.
   * 
   * @param collectionFactory
   *          the collectionFactory to set.
   */
  public void setCollectionFactory(IEntityCollectionFactory collectionFactory) {
    this.collectionFactory = collectionFactory;
  }

  private List<IPropertyIntegrityProcessor> getIntegrityProcessors(
      String propertyName) {
    return entityDescriptor.getPropertyDescriptor(propertyName)
        .getIntegrityProcessors();
  }

  private void preprocessSetter(Object proxy, String propertyName,
      Object oldValue, Object newValue) throws IntegrityException {
    List<IPropertyIntegrityProcessor> integrityProcessors = getIntegrityProcessors(propertyName);
    if (integrityProcessors == null) {
      return;
    }
    for (IPropertyIntegrityProcessor<Object, Object> processor : integrityProcessors) {
      processor.preprocessSetterIntegrity(proxy, oldValue, newValue);
    }
  }

  private void postprocessSetter(Object proxy, String propertyName,
      Object oldValue, Object newValue) throws IntegrityException {
    List<IPropertyIntegrityProcessor> integrityProcessors = getIntegrityProcessors(propertyName);
    if (integrityProcessors == null) {
      return;
    }
    for (IPropertyIntegrityProcessor<Object, Object> processor : integrityProcessors) {
      processor.postprocessSetterIntegrity(proxy, oldValue, newValue);
    }
  }

  private void preprocessAdder(Object proxy, String propertyName,
      Collection collection, Object addedValue) throws IntegrityException {
    List<IPropertyIntegrityProcessor> integrityProcessors = getIntegrityProcessors(propertyName);
    if (integrityProcessors == null) {
      return;
    }
    for (IPropertyIntegrityProcessor propertyIntegrityProcessor : integrityProcessors) {
      ICollectionIntegrityProcessor processor = (ICollectionIntegrityProcessor) propertyIntegrityProcessor;
      processor.preprocessAdderIntegrity(proxy, collection, addedValue);
    }
  }

  private void postprocessAdder(Object proxy, String propertyName,
      Collection collection, Object addedValue) throws IntegrityException {
    List<IPropertyIntegrityProcessor> integrityProcessors = getIntegrityProcessors(propertyName);
    if (integrityProcessors == null) {
      return;
    }
    for (IPropertyIntegrityProcessor propertyIntegrityProcessor : integrityProcessors) {
      ICollectionIntegrityProcessor processor = (ICollectionIntegrityProcessor) propertyIntegrityProcessor;
      processor.postprocessAdderIntegrity(proxy, collection, addedValue);
    }
  }

  private void preprocessRemover(Object proxy, String propertyName,
      Collection collection, Object removedValue) throws IntegrityException {
    List<IPropertyIntegrityProcessor> integrityProcessors = getIntegrityProcessors(propertyName);
    if (integrityProcessors == null) {
      return;
    }
    for (IPropertyIntegrityProcessor propertyIntegrityProcessor : integrityProcessors) {
      ICollectionIntegrityProcessor processor = (ICollectionIntegrityProcessor) propertyIntegrityProcessor;
      processor.preprocessRemoverIntegrity(proxy, collection, removedValue);
    }
  }

  private void postprocessRemover(Object proxy, String propertyName,
      Collection collection, Object removedValue) throws IntegrityException {
    List<IPropertyIntegrityProcessor> integrityProcessors = getIntegrityProcessors(propertyName);
    if (integrityProcessors == null) {
      return;
    }
    for (IPropertyIntegrityProcessor propertyIntegrityProcessor : integrityProcessors) {
      ICollectionIntegrityProcessor processor = (ICollectionIntegrityProcessor) propertyIntegrityProcessor;
      processor.postprocessRemoverIntegrity(proxy, collection, removedValue);
    }
  }

  @SuppressWarnings("unchecked")
  private void straightSetProperty(String propertyName, Object newPropertyValue) {
    Object currentPropertyValue = properties.get(propertyName);
    storeProperty(propertyName, newPropertyValue);
    if (entityDescriptor.getPropertyDescriptor(propertyName) instanceof ICollectionPropertyDescriptor) {
      ICollectionPropertyDescriptor propertyDescriptor = (ICollectionPropertyDescriptor) entityDescriptor
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

  /**
   * Directly get a property value out of the property store without any other
   * operation.
   * 
   * @param propertyName
   *          the name of the property.
   * @return the property value or null.
   */
  protected Object straightGetProperty(String propertyName) {
    return properties.get(propertyName);
  }

  private void straightSetProperties(Map<String, Object> backendProperties) {
    for (Map.Entry<String, Object> propertyEntry : backendProperties.entrySet()) {
      straightSetProperty(propertyEntry.getKey(), propertyEntry.getValue());
    }
  }

  private Map straightGetProperties() {
    return new HashMap<String, Object>(properties);
  }

  /**
   * Gets the interface class being the contract of this entity.
   * 
   * @return the entity interface contract.
   */
  public Class<? extends IEntity> getEntityContract() {
    return entityDescriptor.getComponentContract();
  }

  // private void writeObject(java.io.ObjectOutputStream out) throws IOException
  // {
  // out.writeObject(entityDescriptor.getComponentContract());
  // out.writeObject(properties);
  // out.writeObject(entityExtensions);
  // }
  //
  // @SuppressWarnings("unchecked")
  // private void readObject(java.io.ObjectInputStream in) throws IOException,
  // ClassNotFoundException {
  // entityDescriptor = proxyEntityFactory.getEntityDescriptor((Class) in
  // .readObject());
  // properties = (Map<String, Object>) in.readObject();
  // entityExtensions = (Map<Class, IEntityExtension>) in.readObject();
  //
  // collectionFactory = proxyEntityFactory.getEntityCollectionFactory();
  // accessorFactory = proxyEntityFactory.getAccessorFactory();
  // extensionFactory = proxyEntityFactory.getEntityExtensionFactory();
  // }

  private Map<String, Object> createPropertyMap() {
    return new HashMap<String, Object>();
  }

  /**
   * Direct write access to the properties map without any other operation. Use
   * with caution only in subclasses.
   * 
   * @param propertyName
   *          the property name.
   * @param propertyValue
   *          the property value.
   */
  protected void storeProperty(String propertyName, Object propertyValue) {
    properties.put(propertyName, propertyValue);
  }

  private boolean invokeLifecycleInterceptors(Object proxy,
      Method lifecycleMethod, Object[] args) {
    boolean interceptorResults = false;
    for (ILifecycleInterceptor lifecycleInterceptor : entityDescriptor
        .getLifecycleInterceptors()) {
      int signatureSize = lifecycleMethod.getParameterTypes().length + 1;
      Class[] parameterTypes = new Class[signatureSize];
      Object[] parameters = new Object[signatureSize];

      parameterTypes[0] = entityDescriptor.getComponentContract();
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
        throw new EntityException(ex);
      } catch (InvocationTargetException ex) {
        throw new EntityException(ex.getCause());
      } catch (NoSuchMethodException ex) {
        throw new EntityException(ex);
      }
    }
    return interceptorResults;
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
