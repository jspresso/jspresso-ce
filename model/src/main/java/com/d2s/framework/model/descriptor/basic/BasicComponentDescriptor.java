/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.ITextPropertyDescriptor;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.service.IComponentService;
import com.d2s.framework.util.descriptor.DefaultIconDescriptor;
import com.d2s.framework.util.exception.NestedRuntimeException;

/**
 * Default implementation of a component descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicComponentDescriptor extends DefaultIconDescriptor implements
    IComponentDescriptor {

  private Class                            componentContract;
  private Map<String, IPropertyDescriptor> propertyDescriptors;
  private Collection<String>               unclonedProperties;
  private Map<Method, IComponentService>   serviceDelegates;
  private List<IComponentDescriptor>       ancestorDescriptors;
  private Set<Class>                       serviceContracts;
  private List<String>                     orderingProperties;
  private List<String>                     renderedProperties;
  private List<String>                     queryableProperties;
  private String                           toStringProperty;

  private List<IPropertyDescriptor>        tempPropertyBuffer;

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
   * {@inheritDoc}
   */
  public Collection<IPropertyDescriptor> getPropertyDescriptors() {
    List<IPropertyDescriptor> allDescriptors = new ArrayList<IPropertyDescriptor>();
    if (ancestorDescriptors != null) {
      for (IComponentDescriptor ancestorDescriptor : ancestorDescriptors) {
        allDescriptors.addAll(ancestorDescriptor.getPropertyDescriptors());
      }
    }
    allDescriptors.addAll(getDeclaredPropertyDescriptors());
    return allDescriptors;
  }

  private IPropertyDescriptor getDeclaredPropertyDescriptor(String propertyName) {
    processPropertiesBufferIfNecessary();
    return propertyDescriptors.get(propertyName);
  }

  /**
   * {@inheritDoc}
   */
  public IPropertyDescriptor getPropertyDescriptor(String propertyName) {
    IPropertyDescriptor descriptor = getDeclaredPropertyDescriptor(propertyName);
    if (descriptor == null && ancestorDescriptors != null) {
      for (Iterator<IComponentDescriptor> ite = ancestorDescriptors.iterator(); descriptor == null
          && ite.hasNext();) {
        IComponentDescriptor ancestorDescriptor = ite.next();
        descriptor = ancestorDescriptor.getPropertyDescriptor(propertyName);
      }
    }
    return descriptor;
  }

  /**
   * Sets the propertyDescriptors property.
   * 
   * @param descriptors
   *          the propertyDescriptors to set.
   */
  public void setPropertyDescriptors(Collection<IPropertyDescriptor> descriptors) {
    if (tempPropertyBuffer == null) {
      tempPropertyBuffer = new ArrayList<IPropertyDescriptor>();
    }
    tempPropertyBuffer.addAll(descriptors);
  }

  /**
   * {@inheritDoc}
   */
  public Class getComponentContract() {
    if (componentContract == null) {
      try {
        componentContract = Class.forName(getName());
      } catch (ClassNotFoundException ex) {
        throw new NestedRuntimeException(ex);
      }
    }
    return componentContract;
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

  /**
   * {@inheritDoc}
   */
  public IComponentService getServiceDelegate(Method targetMethod) {
    IComponentService service = null;
    if (serviceDelegates != null) {
      service = serviceDelegates.get(targetMethod);
    }
    if (service == null && ancestorDescriptors != null) {
      for (Iterator<IComponentDescriptor> ite = ancestorDescriptors.iterator(); service == null
          && ite.hasNext();) {
        IComponentDescriptor ancestorDescriptor = ite.next();
        service = ancestorDescriptor.getServiceDelegate(targetMethod);
      }
    }
    return service;
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
   *          The list of ancestor component descriptors.
   */
  public void setAncestorDescriptors(
      List<IComponentDescriptor> ancestorDescriptors) {
    this.ancestorDescriptors = ancestorDescriptors;
  }

  /**
   * {@inheritDoc}
   */
  public List<IComponentDescriptor> getAncestorDescriptors() {
    return ancestorDescriptors;
  }

  /**
   * Registers the service delegates which help the component to implement the
   * services defined by its contract.
   * 
   * @param servicesByServiceContracts
   *          the component services to be registered keyed by their contract. A
   *          service contract is an interface class defining the service
   *          methods to be registered as implemented by the service delegate.
   *          Map values must be instances of <code>IComponentService</code>.
   * @throws ClassNotFoundException
   *           if the declared service class is not found.
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
   * The properties returned include the uncloned properties of the ancestors.
   * <p>
   * {@inheritDoc}
   */
  public Collection<String> getUnclonedProperties() {
    Set<String> properties = new HashSet<String>();
    if (unclonedProperties != null) {
      properties.addAll(unclonedProperties);
    }
    if (ancestorDescriptors != null) {
      for (IComponentDescriptor ancestorDescriptor : ancestorDescriptors) {
        properties.addAll(ancestorDescriptor.getUnclonedProperties());
      }
    }
    return properties;
  }

  /**
   * {@inheritDoc}
   */
  public List<String> getOrderingProperties() {
    List<String> properties = new ArrayList<String>();
    if (orderingProperties != null) {
      properties.addAll(orderingProperties);
    }
    if (ancestorDescriptors != null) {
      for (IComponentDescriptor ancestorDescriptor : ancestorDescriptors) {
        if (ancestorDescriptor.getOrderingProperties() != null) {
          properties.addAll(ancestorDescriptor.getOrderingProperties());
        }
      }
    }
    return properties;
  }

  private synchronized void registerService(Class serviceContract,
      IComponentService service) {
    if (serviceDelegates == null) {
      serviceDelegates = new HashMap<Method, IComponentService>();
      serviceContracts = new HashSet<Class>();
    }
    serviceContracts.add(serviceContract);
    Method[] contractServices = serviceContract.getMethods();
    for (Method serviceMethod : contractServices) {
      serviceDelegates.put(serviceMethod, service);
    }
  }

  /**
   * {@inheritDoc}
   */
  public Collection<Class> getServiceContracts() {
    if (serviceContracts != null) {
      return new ArrayList<Class>(serviceContracts);
    }
    return null;
  }

  /**
   * Sets the unclonedProperties.
   * 
   * @param unclonedProperties
   *          the unclonedProperties to set.
   */
  public void setUnclonedProperties(Collection<String> unclonedProperties) {
    this.unclonedProperties = unclonedProperties;
  }

  /**
   * Sets the orderingProperties.
   * 
   * @param orderingProperties
   *          the orderingProperties to set.
   */
  public void setOrderingProperties(List<String> orderingProperties) {
    this.orderingProperties = orderingProperties;
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
   * Sets the queryableProperties.
   * 
   * @param queryableProperties
   *          the queryableProperties to set.
   */
  public void setQueryableProperties(List<String> queryableProperties) {
    this.queryableProperties = queryableProperties;
  }

  /**
   * {@inheritDoc}
   */
  public List<String> getRenderedProperties() {
    if (renderedProperties == null) {
      List<String> allProperties = new ArrayList<String>();
      for (IPropertyDescriptor propertyDescriptor : getPropertyDescriptors()) {
        if (!(propertyDescriptor instanceof ICollectionPropertyDescriptor)
            && !(propertyDescriptor instanceof ITextPropertyDescriptor)
            && !IEntity.ID.equals(propertyDescriptor.getName())) {
          allProperties.add(propertyDescriptor.getName());
        }
      }
      return allProperties;
    }
    return renderedProperties;
  }

  /**
   * Sets the renderedProperties.
   * 
   * @param renderedProperties
   *          the renderedProperties to set.
   */
  public void setRenderedProperties(List<String> renderedProperties) {
    this.renderedProperties = renderedProperties;
  }

  /**
   * Gets the toStringProperty.
   * 
   * @return the toStringProperty.
   */
  public String getToStringProperty() {
    if (toStringProperty == null) {
      return getRenderedProperties().get(0);
    }
    return toStringProperty;
  }

  /**
   * Sets the toStringProperty.
   * 
   * @param toStringProperty
   *          the toStringProperty to set.
   */
  public void setToStringProperty(String toStringProperty) {
    this.toStringProperty = toStringProperty;
  }

}
