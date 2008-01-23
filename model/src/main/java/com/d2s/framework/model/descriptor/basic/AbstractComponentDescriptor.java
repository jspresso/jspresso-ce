/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.d2s.framework.model.component.service.IComponentService;
import com.d2s.framework.model.component.service.ILifecycleInterceptor;
import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptorProvider;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.IReferencePropertyDescriptor;
import com.d2s.framework.model.descriptor.IStringPropertyDescriptor;
import com.d2s.framework.model.descriptor.ITextPropertyDescriptor;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.util.descriptor.DefaultIconDescriptor;
import com.d2s.framework.util.exception.NestedRuntimeException;

/**
 * Abstract implementation of a component descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            the concrete type of components.
 */
public abstract class AbstractComponentDescriptor<E> extends DefaultIconDescriptor
    implements IComponentDescriptor<E> {

  private List<IComponentDescriptor<?>>    ancestorDescriptors;
  private Class<?>                         componentContract;
  private List<ILifecycleInterceptor<?>>   lifecycleInterceptors;
  private List<String>                     orderingProperties;
  private Map<String, IPropertyDescriptor> propertyDescriptors;
  private Map<String, IPropertyDescriptor> nestedPropertyDescriptors;
  private List<String>                     queryableProperties;
  private List<String>                     renderedProperties;
  private Set<Class<?>>                    serviceContracts;
  private Map<Method, IComponentService>   serviceDelegates;
  private List<IPropertyDescriptor>        tempPropertyBuffer;
  private String                           toStringProperty;
  private Collection<String>               unclonedProperties;

  /**
   * Constructs a new <code>AbstractComponentDescriptor</code> instance.
   * 
   * @param name
   *            the name of the descriptor which has to be the fully-qualified
   *            class name of its contract.
   */
  public AbstractComponentDescriptor(String name) {
    setName(name);
  }

  /**
   * Gets the descriptor ancestors collection. It directly translates the
   * components inheritance hierarchy since the component property descriptors
   * are the union of the declared property descriptors of the component and of
   * its ancestors one. A component may have multiple ancestors which means that
   * complex multi-inheritance hierarchy can be mapped.
   * 
   * @return ancestorDescriptors The list of ancestor entity descriptors.
   */
  public List<IComponentDescriptor<?>> getAncestorDescriptors() {
    return ancestorDescriptors;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings({ "cast", "unchecked" })
  public Class<? extends E> getComponentContract() {
    if (componentContract == null && getName() != null) {
      try {
        componentContract = Class.forName(getName());
      } catch (ClassNotFoundException ex) {
        throw new NestedRuntimeException(ex);
      }
    }
    return (Class<? extends E>) componentContract;
  }

  /**
   * {@inheritDoc}
   */
  public IComponentDescriptor<E> getComponentDescriptor() {
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public Collection<IPropertyDescriptor> getDeclaredPropertyDescriptors() {
    processPropertiesBufferIfNecessary();
    if (propertyDescriptors != null) {
      return propertyDescriptors.values();
    }
    return null;
  }

  /**
   * Gets the lifecycleInterceptors.
   * 
   * @return the lifecycleInterceptors.
   */
  public List<ILifecycleInterceptor<?>> getLifecycleInterceptors() {
    List<ILifecycleInterceptor<?>> allInterceptors = new ArrayList<ILifecycleInterceptor<?>>();
    if (getAncestorDescriptors() != null) {
      for (IComponentDescriptor<?> ancestorDescriptor : getAncestorDescriptors()) {
        allInterceptors.addAll(ancestorDescriptor.getLifecycleInterceptors());
      }
    }
    if (lifecycleInterceptors != null) {
      allInterceptors.addAll(lifecycleInterceptors);
    }
    return allInterceptors;
  }

  /**
   * {@inheritDoc}
   */
  public Class<?> getModelType() {
    return getComponentContract();
  }

  /**
   * {@inheritDoc}
   */
  public List<String> getOrderingProperties() {
    // use a set to avoid duplicates.
    Set<String> properties = new LinkedHashSet<String>();
    if (orderingProperties != null) {
      properties.addAll(orderingProperties);
    }
    if (getAncestorDescriptors() != null) {
      for (IComponentDescriptor<?> ancestorDescriptor : getAncestorDescriptors()) {
        if (ancestorDescriptor.getOrderingProperties() != null) {
          properties.addAll(ancestorDescriptor.getOrderingProperties());
        }
      }
    }
    if (properties.isEmpty()) {
      return null;
    }
    return new ArrayList<String>(properties);
  }

  /**
   * {@inheritDoc}
   */
  public IPropertyDescriptor getPropertyDescriptor(String propertyName) {
    IPropertyDescriptor descriptor = null;
    int nestedDotIndex = propertyName.indexOf('.');
    if (nestedDotIndex > 0) {
      if (nestedPropertyDescriptors == null) {
        nestedPropertyDescriptors = new HashMap<String, IPropertyDescriptor>();
      }
      descriptor = nestedPropertyDescriptors.get(propertyName);
      if (descriptor == null) {
        IComponentDescriptor<?> componentDescriptor = ((IComponentDescriptorProvider<?>) getPropertyDescriptor(propertyName
            .substring(0, nestedDotIndex))).getComponentDescriptor();
        descriptor = componentDescriptor.getPropertyDescriptor(
            propertyName.substring(nestedDotIndex + 1)).clone();
        if (descriptor instanceof BasicPropertyDescriptor) {
          ((BasicPropertyDescriptor) descriptor).setName(propertyName);
        }
        nestedPropertyDescriptors.put(propertyName, descriptor);
      }
    } else {
      descriptor = getDeclaredPropertyDescriptor(propertyName);
      if (descriptor == null && getAncestorDescriptors() != null) {
        for (Iterator<IComponentDescriptor<?>> ite = getAncestorDescriptors()
            .iterator(); descriptor == null && ite.hasNext();) {
          IComponentDescriptor<?> ancestorDescriptor = ite.next();
          descriptor = ancestorDescriptor.getPropertyDescriptor(propertyName);
        }
      }
    }
    return descriptor;
  }

  /**
   * {@inheritDoc}
   */
  public Collection<IPropertyDescriptor> getPropertyDescriptors() {
    // A map is used instead of a set since a set does not replace an element it
    // already contains.
    Map<String, IPropertyDescriptor> allDescriptors = new LinkedHashMap<String, IPropertyDescriptor>();
    if (getAncestorDescriptors() != null) {
      for (IComponentDescriptor<?> ancestorDescriptor : getAncestorDescriptors()) {
        for (IPropertyDescriptor propertyDescriptor : ancestorDescriptor
            .getPropertyDescriptors()) {
          allDescriptors.put(propertyDescriptor.getName(), propertyDescriptor);
        }
      }
    }
    Collection<IPropertyDescriptor> declaredPropertyDescriptors = getDeclaredPropertyDescriptors();
    if (declaredPropertyDescriptors != null) {
      for (IPropertyDescriptor propertyDescriptor : declaredPropertyDescriptors) {
        allDescriptors.put(propertyDescriptor.getName(), propertyDescriptor);
      }
    }
    return allDescriptors.values();
  }

  /**
   * {@inheritDoc}
   */
  public List<String> getQueryableProperties() {
    if (queryableProperties == null) {
      return getRenderedProperties();
    }
    return queryableProperties;
  }

  /**
   * {@inheritDoc}
   */
  public List<String> getRenderedProperties() {
    if (renderedProperties == null) {
      List<String> allProperties = new ArrayList<String>();
      for (IPropertyDescriptor propertyDescriptor : getPropertyDescriptors()) {
        if ((propertyDescriptor instanceof IReferencePropertyDescriptor<?> && !IEntity.class
            .isAssignableFrom(((IReferencePropertyDescriptor<?>) propertyDescriptor)
                .getReferencedDescriptor().getComponentContract()))) {
          for (String nestedRenderedProperty : ((IReferencePropertyDescriptor<?>) propertyDescriptor)
              .getReferencedDescriptor().getRenderedProperties()) {
            allProperties.add(propertyDescriptor.getName() + "."
                + nestedRenderedProperty);
          }
        } else if (!(propertyDescriptor instanceof ICollectionPropertyDescriptor)
            && !(propertyDescriptor instanceof ITextPropertyDescriptor)) {
          allProperties.add(propertyDescriptor.getName());
        }
      }
      return allProperties;
    }
    return new ArrayList<String>(renderedProperties);
  }

  /**
   * {@inheritDoc}
   */
  public Collection<Class<?>> getServiceContracts() {
    if (serviceContracts != null) {
      return new ArrayList<Class<?>>(serviceContracts);
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public IComponentService getServiceDelegate(Method targetMethod) {
    IComponentService service = null;
    if (serviceDelegates != null) {
      service = serviceDelegates.get(targetMethod);
    }
    if (service == null && getAncestorDescriptors() != null) {
      for (Iterator<IComponentDescriptor<?>> ite = getAncestorDescriptors()
          .iterator(); service == null && ite.hasNext();) {
        IComponentDescriptor<?> ancestorDescriptor = ite.next();
        service = ancestorDescriptor.getServiceDelegate(targetMethod);
      }
    }
    return service;
  }

  /**
   * Gets the toStringProperty.
   * 
   * @return the toStringProperty.
   */
  public String getToStringProperty() {
    if (toStringProperty == null) {
      for (String renderedProperty : getRenderedProperties()) {
        if (getPropertyDescriptor(renderedProperty) instanceof IStringPropertyDescriptor) {
          toStringProperty = renderedProperty;
          break;
        }
      }
      if (toStringProperty == null) {
        toStringProperty = getRenderedProperties().get(0);
      }
    }
    return toStringProperty;
  }

  /**
   * The properties returned include the uncloned properties of the ancestors.
   * <p>
   * {@inheritDoc}
   */
  public Collection<String> getUnclonedProperties() {
    Set<String> properties = new HashSet<String>();
    if (unclonedProperties != null) {
      properties.addAll(unclonedProperties);
    }
    if (getAncestorDescriptors() != null) {
      for (IComponentDescriptor<?> ancestorDescriptor : getAncestorDescriptors()) {
        properties.addAll(ancestorDescriptor.getUnclonedProperties());
      }
    }
    return properties;
  }

  /**
   * Registers this descriptor with a collection of ancestors. It directly
   * translates the components inheritance hierarchy since the component
   * property descriptors are the union of the declared property descriptors of
   * the component and of its ancestors one. A component may have multiple
   * ancestors which means that complex multi-inheritance hierarchy can be
   * mapped.
   * 
   * @param ancestorDescriptors
   *            The list of ancestor component descriptors.
   */
  public void setAncestorDescriptors(
      List<IComponentDescriptor<?>> ancestorDescriptors) {
    this.ancestorDescriptors = ancestorDescriptors;
  }

  /**
   * Sets the lifecycleInterceptors.
   * 
   * @param lifecycleInterceptors
   *            the lifecycleInterceptors to set.
   */
  public void setLifecycleInterceptors(
      List<ILifecycleInterceptor<?>> lifecycleInterceptors) {
    this.lifecycleInterceptors = lifecycleInterceptors;
  }

  /**
   * Sets the orderingProperties.
   * 
   * @param orderingProperties
   *            the orderingProperties to set.
   */
  public void setOrderingProperties(List<String> orderingProperties) {
    this.orderingProperties = orderingProperties;
  }

  /**
   * Sets the propertyDescriptors property.
   * 
   * @param descriptors
   *            the propertyDescriptors to set.
   */
  public void setPropertyDescriptors(Collection<IPropertyDescriptor> descriptors) {
    // This is important to use an intermediate structure since all descriptors
    // may not have their names fully initialized.
    if (descriptors != null) {
      tempPropertyBuffer = new ArrayList<IPropertyDescriptor>(descriptors);
      propertyDescriptors = null;
    } else {
      tempPropertyBuffer = null;
      propertyDescriptors = null;
    }
  }

  /**
   * Sets the queryableProperties.
   * 
   * @param queryableProperties
   *            the queryableProperties to set.
   */
  public void setQueryableProperties(List<String> queryableProperties) {
    this.queryableProperties = queryableProperties;
  }

  /**
   * Sets the renderedProperties.
   * 
   * @param renderedProperties
   *            the renderedProperties to set.
   */
  public void setRenderedProperties(List<String> renderedProperties) {
    this.renderedProperties = renderedProperties;
  }

  /**
   * Registers the service delegates which help the component to implement the
   * services defined by its contract.
   * 
   * @param servicesByServiceContracts
   *            the component services to be registered keyed by their contract.
   *            A service contract is an interface class defining the service
   *            methods to be registered as implemented by the service delegate.
   *            Map values must be instances of <code>IComponentService</code>.
   * @throws ClassNotFoundException
   *             if the declared service class is not found.
   */
  public void setServiceDelegates(
      Map<String, IComponentService> servicesByServiceContracts)
      throws ClassNotFoundException {
    for (Entry<String, IComponentService> nextPair : servicesByServiceContracts
        .entrySet()) {
      registerService(Class.forName(nextPair.getKey()), nextPair.getValue());
    }
  }

  /**
   * Sets the toStringProperty.
   * 
   * @param toStringProperty
   *            the toStringProperty to set.
   */
  public void setToStringProperty(String toStringProperty) {
    this.toStringProperty = toStringProperty;
  }

  /**
   * Sets the unclonedProperties.
   * 
   * @param unclonedProperties
   *            the unclonedProperties to set.
   */
  public void setUnclonedProperties(Collection<String> unclonedProperties) {
    this.unclonedProperties = unclonedProperties;
  }

  private IPropertyDescriptor getDeclaredPropertyDescriptor(String propertyName) {
    processPropertiesBufferIfNecessary();
    if (propertyDescriptors != null) {
      return propertyDescriptors.get(propertyName);
    }
    return null;
  }

  private synchronized void processPropertiesBufferIfNecessary() {
    if (tempPropertyBuffer != null) {
      propertyDescriptors = new LinkedHashMap<String, IPropertyDescriptor>();
      for (IPropertyDescriptor descriptor : tempPropertyBuffer) {
        propertyDescriptors.put(descriptor.getName(), descriptor);
      }
      tempPropertyBuffer = null;
    }
  }

  private synchronized void registerService(Class<?> serviceContract,
      IComponentService service) {
    if (serviceDelegates == null) {
      serviceDelegates = new HashMap<Method, IComponentService>();
      serviceContracts = new HashSet<Class<?>>();
    }
    serviceContracts.add(serviceContract);
    Method[] contractServices = serviceContract.getMethods();
    for (Method serviceMethod : contractServices) {
      serviceDelegates.put(serviceMethod, service);
    }
  }
}
