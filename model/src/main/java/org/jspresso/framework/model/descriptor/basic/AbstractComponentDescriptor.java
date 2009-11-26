/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.model.descriptor.basic;

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

import org.jspresso.framework.model.component.service.IComponentService;
import org.jspresso.framework.model.component.service.ILifecycleInterceptor;
import org.jspresso.framework.model.descriptor.DescriptorException;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.IStringPropertyDescriptor;
import org.jspresso.framework.model.descriptor.ITextPropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.collection.ESort;
import org.jspresso.framework.util.descriptor.DefaultIconDescriptor;
import org.jspresso.framework.util.exception.NestedRuntimeException;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.util.lang.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * This is the abstract base descriptor for all component-like part of the
 * domain model. All the properties included in this base descriptor can of
 * course be used in concrete sub-types.
 * <p>
 * These sub-types include :
 * <ul>
 * <li><i>BasicEntityDescriptor</i> for defining a persistent entity</li>
 * <li><i>BasicInterfaceDescriptor</i> for defining a common interface that will
 * be implemented by other entities, components or even sub-interfaces.</li>
 * <li><i>BasicComponentDescriptor</i> for defining reusable structures that can
 * be inlined in an entity. It also allows to describe an arbitrary POJO and
 * make use of it in Jspresso UIs.</li>
 * </ul>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the concrete type of components.
 */
public abstract class AbstractComponentDescriptor<E> extends
    DefaultIconDescriptor implements IComponentDescriptor<E>, BeanFactoryAware {

  private BeanFactory                      beanFactory;

  private List<IComponentDescriptor<?>>    ancestorDescriptors;
  private Class<?>                         componentContract;

  private Collection<String>               grantedRoles;
  private List<String>                     lifecycleInterceptorClassNames;
  private List<String>                     lifecycleInterceptorBeanNames;

  private List<ILifecycleInterceptor<?>>   lifecycleInterceptors;
  private Map<String, IPropertyDescriptor> nestedPropertyDescriptors;
  private Map<String, ESort>               orderingProperties;
  private Map<String, IPropertyDescriptor> propertyDescriptorsMap;
  private List<String>                     queryableProperties;

  private List<String>                     renderedProperties;
  private Set<Class<?>>                    serviceContracts;
  private Map<String, String>              serviceDelegateClassNames;
  private Map<String, String>              serviceDelegateBeanNames;

  private Map<Method, IComponentService>   serviceDelegates;
  private List<IPropertyDescriptor>        tempPropertyBuffer;
  private String                           toStringProperty;
  private Collection<String>               unclonedProperties;

  private Collection<IGate>                readabilityGates;
  private Collection<IGate>                writabilityGates;

  private Integer                          pageSize;

  private String                           sqlName;

  /**
   * Constructs a new <code>AbstractComponentDescriptor</code> instance.
   * 
   * @param name
   *          the name of the descriptor which has to be the fully-qualified
   *          class name of its contract.
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
  @SuppressWarnings("unchecked")
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
    if (propertyDescriptorsMap != null) {
      return propertyDescriptorsMap.values();
    }
    return null;
  }

  /**
   * Gets the grantedRoles.
   * 
   * @return the grantedRoles.
   */
  public Collection<String> getGrantedRoles() {
    return grantedRoles;
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
    registerLifecycleInterceptorsIfNecessary();
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
  public Map<String, ESort> getOrderingProperties() {
    // use a set to avoid duplicates.
    Map<String, ESort> properties = new LinkedHashMap<String, ESort>();
    if (orderingProperties != null) {
      properties.putAll(orderingProperties);
    }
    if (getAncestorDescriptors() != null) {
      for (IComponentDescriptor<?> ancestorDescriptor : getAncestorDescriptors()) {
        if (ancestorDescriptor.getOrderingProperties() != null) {
          properties.putAll(ancestorDescriptor.getOrderingProperties());
        }
      }
    }
    if (properties.isEmpty()) {
      return null;
    }
    return properties;
  }

  /**
   * {@inheritDoc}
   */
  public IPropertyDescriptor getPropertyDescriptor(String propertyName) {
    IPropertyDescriptor descriptor = null;
    int nestedDotIndex = propertyName.indexOf(IAccessor.NESTED_DELIM);
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
      queryableProperties = new ArrayList<String>();
      for (String renderedProperty : getRenderedProperties()) {
        IPropertyDescriptor propertyDescriptor = getPropertyDescriptor(renderedProperty);
        if (propertyDescriptor.isQueryable()) {
          queryableProperties.add(renderedProperty);
        }
      }
    }
    return explodeComponentReferences(queryableProperties);
  }

  /**
   * {@inheritDoc}
   */
  public Class<?> getQueryComponentContract() {
    return getComponentContract();
  }

  /**
   * {@inheritDoc}
   */
  public List<String> getRenderedProperties() {
    if (renderedProperties == null) {
      renderedProperties = new ArrayList<String>();
      for (IPropertyDescriptor propertyDescriptor : getPropertyDescriptors()) {
        if (!(propertyDescriptor instanceof ICollectionPropertyDescriptor<?>)
            && !(propertyDescriptor instanceof ITextPropertyDescriptor)) {
          renderedProperties.add(propertyDescriptor.getName());
        }
      }
    }
    return explodeComponentReferences(renderedProperties);
  }

  /**
   * {@inheritDoc}
   */
  public Collection<String> getServiceContractClassNames() {
    Set<String> serviceContractClassNames = new LinkedHashSet<String>();
    if (serviceContracts != null) {
      for (Class<?> serviceContract : serviceContracts) {
        serviceContractClassNames.add(serviceContract.getName());
      }
    } else {
      if (serviceDelegateClassNames != null) {
        serviceContractClassNames.addAll(serviceDelegateClassNames.keySet());
      }
      if (serviceDelegateBeanNames != null) {
        serviceContractClassNames.addAll(serviceDelegateBeanNames.keySet());
      }
    }
    return serviceContractClassNames;
  }

  /**
   * {@inheritDoc}
   */
  public Collection<Class<?>> getServiceContracts() {
    registerDelegateServicesIfNecessary();
    if (serviceContracts != null) {
      return new ArrayList<Class<?>>(serviceContracts);
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public IComponentService getServiceDelegate(Method targetMethod) {
    registerDelegateServicesIfNecessary();
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
   * ancestors which means that complex multiple-inheritance hierarchy can be
   * mapped.
   * 
   * @param ancestorDescriptors
   *          The list of ancestor component descriptors.
   */
  public void setAncestorDescriptors(
      List<IComponentDescriptor<?>> ancestorDescriptors) {
    this.ancestorDescriptors = ancestorDescriptors;
  }

  /**
   * Assigns the roles that are authorized to manipulate components backed by
   * this descriptor. This will directly influence the UI behaviour and even
   * composition. Setting the collection of granted roles to <code>null</code>
   * (default value) disables role based authorization on this component level.
   * 
   * @param grantedRoles
   *          the grantedRoles to set.
   */
  public void setGrantedRoles(Collection<String> grantedRoles) {
    this.grantedRoles = StringUtils.ensureSpaceFree(grantedRoles);
  }

  /**
   * Much the same as <code>lifecycleInterceptorBeanNames</code> except that
   * instead of providing a list of Spring bean names, you provide a list of
   * fully qualified class names. These class must :
   * <ul>
   * <li>provide a default constructor</li>
   * <li>implement the <code>ILifecycleInterceptor</code> interface.</li>
   * </ul>
   * When needed, Jspresso will create lifecycle interceptor instances.
   * 
   * @param lifecycleInterceptorClassNames
   *          the lifecycleInterceptorClassNames to set.
   */
  public void setLifecycleInterceptorClassNames(
      List<String> lifecycleInterceptorClassNames) {
    this.lifecycleInterceptorClassNames = StringUtils
        .ensureSpaceFree(lifecycleInterceptorClassNames);
  }

  /**
   * Ordering properties are used to sort un-indexed collections of instances of
   * components backed by this descriptor. This sort order can be overridden on
   * the finer collection property level to change the way a specific collection
   * is sorted. This property consist of a <code>Map</code> whose entries are
   * composed with :
   * <ul>
   * <li>the property name as key</li>
   * <li>the sort order for this property as value. This is either a value of
   * the <code>ESort</code> enum or their equivalent string representation.</li>
   * </ul>
   * Ordering properties are considered following their order in the map
   * iterator. A <code>null</code> value (default) will not give any indication
   * for the collection sort order.
   * 
   * @param untypedOrderingProperties
   *          the orderingProperties to set.
   */
  public void setOrderingProperties(Map<String, ?> untypedOrderingProperties) {
    if (untypedOrderingProperties != null) {
      orderingProperties = new LinkedHashMap<String, ESort>();
      for (Map.Entry<String, ?> untypedOrderingProperty : untypedOrderingProperties
          .entrySet()) {
        if (untypedOrderingProperty.getValue() instanceof ESort) {
          orderingProperties.put(untypedOrderingProperty.getKey(),
              (ESort) untypedOrderingProperty.getValue());
        } else if (untypedOrderingProperty.getValue() instanceof String) {
          orderingProperties.put(untypedOrderingProperty.getKey(), ESort
              .valueOf((String) untypedOrderingProperty.getValue()));
        } else {
          orderingProperties.put(untypedOrderingProperty.getKey(),
              ESort.ASCENDING);
        }
      }
    } else {
      orderingProperties = null;
    }
  }

  /**
   * This property allows to describe the properties of the components backed by
   * this descriptor. Like in classic OO programming, the actual set of
   * properties available to a component is the union of its properties and of
   * its ancestors' ones. Jspresso also alows you to refine a property
   * descriptor in a child component descriptor exactly as you would do it in a
   * subclass. In that case, the atributes of the property defined in the child
   * descriptor prevails over the definition of its ancestors. Naturally,
   * properties are keyed by their names.
   * 
   * @param descriptors
   *          the propertyDescriptors to set.
   */
  public void setPropertyDescriptors(Collection<IPropertyDescriptor> descriptors) {
    // This is important to use an intermediate structure since all descriptors
    // may not have their names fully initialized.
    if (descriptors != null) {
      tempPropertyBuffer = new ArrayList<IPropertyDescriptor>(descriptors);
      propertyDescriptorsMap = null;
    } else {
      tempPropertyBuffer = null;
      propertyDescriptorsMap = null;
    }
  }

  /**
   * This property allows to define which of the component properties are to be
   * used in the filter UIs that are based on this component family (a QBE
   * screen for instance). Since this is a <code>List</code> queriable
   * properties are rendered in the same order.
   * <p>
   * Whenever this this property is <code>null</code> (default value), Jspresso
   * chooses the default set of queryable properties based on their type. For
   * instance, collection properties and binary properties are not used but
   * string, numeric, reference, ... properties are. A computed property cannot
   * be used since it has no data store existance and thus cannot be queried
   * upon.
   * <p>
   * Note that this property is not inherited by children descriptors, i.e. even
   * if an ancestor defines an explicit set of queryable properties, its
   * children ignore this setting.
   * 
   * @param queryableProperties
   *          the queryableProperties to set.
   */
  public void setQueryableProperties(List<String> queryableProperties) {
    this.queryableProperties = StringUtils.ensureSpaceFree(queryableProperties);
  }

  /**
   * This property allows to define which of the component properties are to be
   * rendered by default when displaying a UI based on this component family.
   * For instance, a table will render 1 column per rendered property of the
   * component. Any type of property can be used except collection properties.
   * Since this is a <code>List</code> queriable properties are rendered in the
   * same order.
   * <p>
   * Whenever this property is <code>null</code> (default value) Jspresso
   * determines the default set of properties to render based on their types,
   * e.g. ignores collection properties.
   * <p>
   * Note that this property is not inherited by children descriptors, i.e. even
   * if an ancestor defines an explicit set of rendered properties, its children
   * ignore this setting.
   * 
   * @param renderedProperties
   *          the renderedProperties to set.
   */
  public void setRenderedProperties(List<String> renderedProperties) {
    this.renderedProperties = StringUtils.ensureSpaceFree(renderedProperties);
  }

  /**
   * Much the same as <code>serviceDelegateBeanNames</code> except that instead
   * of providing a map valued with Spring bean names, you provide a map valued
   * with fully qualified class names. These class must :
   * <ul>
   * <li>provide a default constructor</li>
   * <li>implement the <code>IComponentService</code> marker interface.</li>
   * </ul>
   * When needed, Jspresso will create service delegate instances.
   * 
   * @param serviceDelegateClassNames
   *          the component services to be registered keyed by their contract. A
   *          service contract is an interface class defining the service
   *          methods to be registered as implemented by the service delegate.
   *          Map values must be instances of <code>IComponentService</code>.
   */
  public void setServiceDelegateClassNames(
      Map<String, String> serviceDelegateClassNames) {
    this.serviceDelegateClassNames = StringUtils
        .ensureSpaceFree(serviceDelegateClassNames);
  }

  /**
   * Allows to customize the string representation of a component instance. The
   * property name assigned will be used when displaying the component instance
   * as a string. It may be a computed property that composes several other
   * properties in a human friendly format.
   * <p>
   * Whenever this property is <code>null</code>, the following rule apply to
   * determine the <i>toString</i> property :
   * <ol>
   * <li>the first string property from the rendered property</li>
   * <li>the first rendered property if no string property is found among them</li>
   * </ol>
   * Note that this property is not inherited by children descriptors, i.e. even
   * if an ancestor defines an explicit <i>toString</i> property, its children
   * ignore this setting.
   * 
   * @param toStringProperty
   *          the toStringProperty to set.
   */
  public void setToStringProperty(String toStringProperty) {
    this.toStringProperty = toStringProperty;
  }

  /**
   * Configures the properties that must not be cloned when this component is
   * duplicated. For instance, tracing informations like a created timestamp
   * should not be cloned; a SSN neither. For a given component, the uncloned
   * properties are the ones it defines augmented by the ones its ancestors
   * define. There is no mean to make a component property clonable if one of
   * the ancestor declares it un-clonable.
   * 
   * @param unclonedProperties
   *          the unclonedProperties to set.
   */
  public void setUnclonedProperties(Collection<String> unclonedProperties) {
    this.unclonedProperties = StringUtils.ensureSpaceFree(unclonedProperties);
  }

  private List<String> explodeComponentReferences(List<String> propertyNames) {
    List<String> explodedProperties = new ArrayList<String>();
    for (String propertyName : propertyNames) {
      IPropertyDescriptor propertyDescriptor = getPropertyDescriptor(propertyName);
      if ((propertyDescriptor instanceof IReferencePropertyDescriptor<?> && !IEntity.class
          .isAssignableFrom(((IReferencePropertyDescriptor<?>) propertyDescriptor)
              .getReferencedDescriptor().getComponentContract()))) {
        List<String> nestedProperties = new ArrayList<String>();
        for (String nestedRenderedProperty : ((IReferencePropertyDescriptor<?>) propertyDescriptor)
            .getReferencedDescriptor().getRenderedProperties()) {
          nestedProperties.add(propertyName + "." + nestedRenderedProperty);
        }
        explodedProperties.addAll(explodeComponentReferences(nestedProperties));
      } else {
        explodedProperties.add(propertyName);
      }
    }
    return explodedProperties;
  }

  private IPropertyDescriptor getDeclaredPropertyDescriptor(String propertyName) {
    processPropertiesBufferIfNecessary();
    if (propertyDescriptorsMap != null) {
      return propertyDescriptorsMap.get(propertyName);
    }
    return null;
  }

  private synchronized void processPropertiesBufferIfNecessary() {
    if (tempPropertyBuffer != null) {
      propertyDescriptorsMap = new LinkedHashMap<String, IPropertyDescriptor>();
      for (IPropertyDescriptor descriptor : tempPropertyBuffer) {
        propertyDescriptorsMap.put(descriptor.getName(), descriptor);
      }
      tempPropertyBuffer = null;
    }
  }

  private synchronized void registerDelegateServicesIfNecessary() {
    if (serviceDelegateClassNames != null) {
      for (Entry<String, String> nextPair : serviceDelegateClassNames
          .entrySet()) {
        try {
          IComponentService delegate = null;
          if (!("".equals(nextPair.getValue()) || "null"
              .equalsIgnoreCase(nextPair.getValue()))) {
            delegate = (IComponentService) Class.forName(nextPair.getValue())
                .newInstance();
          }
          registerService(Class.forName(nextPair.getKey()), delegate);
        } catch (ClassNotFoundException ex) {
          throw new DescriptorException(ex);
        } catch (InstantiationException ex) {
          throw new DescriptorException(ex);
        } catch (IllegalAccessException ex) {
          throw new DescriptorException(ex);
        }
      }
      serviceDelegateClassNames = null;
    }
    if (serviceDelegateBeanNames != null && beanFactory != null) {
      for (Entry<String, String> nextPair : serviceDelegateBeanNames.entrySet()) {
        try {
          registerService(Class.forName(nextPair.getKey()),
              (IComponentService) beanFactory.getBean(nextPair.getValue(),
                  IComponentService.class));
        } catch (ClassNotFoundException ex) {
          throw new DescriptorException(ex);
        }
      }
      serviceDelegateBeanNames = null;
    }
  }

  private synchronized void registerLifecycleInterceptorsIfNecessary() {
    // process creation of lifecycle interceptors.
    if (lifecycleInterceptorClassNames != null) {
      for (String lifecycleInterceptorClassName : lifecycleInterceptorClassNames) {
        try {
          registerLifecycleInterceptor((ILifecycleInterceptor<?>) Class
              .forName(lifecycleInterceptorClassName).newInstance());
        } catch (InstantiationException ex) {
          throw new DescriptorException(ex);
        } catch (IllegalAccessException ex) {
          throw new DescriptorException(ex);
        } catch (ClassNotFoundException ex) {
          throw new DescriptorException(ex);
        }
      }
      lifecycleInterceptorClassNames = null;
    }
    if (lifecycleInterceptorBeanNames != null && beanFactory != null) {
      for (String lifecycleInterceptorBeanName : lifecycleInterceptorBeanNames) {
        registerLifecycleInterceptor((ILifecycleInterceptor<?>) beanFactory
            .getBean(lifecycleInterceptorBeanName, ILifecycleInterceptor.class));
      }
      lifecycleInterceptorBeanNames = null;
    }
  }

  private void registerLifecycleInterceptor(
      ILifecycleInterceptor<?> lifecycleInterceptor) {
    if (lifecycleInterceptors == null) {
      lifecycleInterceptors = new ArrayList<ILifecycleInterceptor<?>>();
    }
    lifecycleInterceptors.add(lifecycleInterceptor);
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

  /**
   * {@inheritDoc}
   */
  public Integer getPageSize() {
    return pageSize;
  }

  /**
   * Whenever a collection of this component type is presented in a pageable UI,
   * this property gives the size (number of componet instances) of one page.
   * This size can usually be refined at a finer level. A <code>null</code>
   * value (default) disables general paging for this component.
   * 
   * @param pageSize
   *          the pageSize to set.
   */
  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  /**
   * Registers the collection of service delegate instances attached to this
   * component. These delegate instances will automatically be triggered
   * whenever a method of the service interface it implements get executed. For
   * instance :
   * <ul>
   * <li>the component interface is <code>MyBeanClass</code>. It implements the
   * service interface <code>MyService</code>.</li>
   * <li>the service interface <code>MyService</code> contains method
   * <code>int foo(String)</code>.</li>
   * <li>the service delegate class, e.g. <code>MyServiceImpl</code> must
   * implement the method <code>int foo(MyBeanClass,String)</code>. Note that
   * the parameter list is augmented with the owing component type as 1st
   * parameter. This allows to have stateless implementation for delegates, thus
   * sharing instances of delegates among instances of components.</li>
   * <li>when <code>foo(String)</code> is executed on an instance of
   * <code>MyBeanClass</code>, the framework will trigger the delegate
   * implementation, passing the instance of the component itself as parameter.</li>
   * </ul>
   * This property must be set with a map keyed by service interfaces and valued
   * by Spring bean names (i.e. Spring ids). Each bean name corresponds to an
   * instance of service delegate. When needed, Jspresso will query the Spring
   * application context to retrieve the delegate instances. This property is
   * equivalent to setting <code>serviceDelegateClassNames</code> except that it
   * allows to register delegate instances that are configured externally in the
   * Spring context. lifecycle interceptor instances must implement the
   * <code>IComponentService</code> marker interface.
   * 
   * @param serviceDelegateBeanNames
   *          the serviceDelegateBeanNames to set. They are used to retrieve
   *          delegate instances from the Spring bean factory this descriptor
   *          comes from if any.
   */
  public void setServiceDelegateBeanNames(
      Map<String, String> serviceDelegateBeanNames) {
    this.serviceDelegateBeanNames = StringUtils
        .ensureSpaceFree(serviceDelegateBeanNames);
  }

  /**
   * {@inheritDoc}
   * 
   * @internal
   */
  public void setBeanFactory(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  /**
   * Registers a list of lifecycle interceptor instances that will be triggered
   * on the different phases of tha component lifecycle, i.e. :
   * <ul>
   * <li>when the component is <i>instanciated</i> in memory</li>
   * <li>when the component is <i>created</i> in the data store</li>
   * <li>when the component is <i>updated</i> in the data store</li>
   * <li>when the component is <i>loaded</i> from the data store</li>
   * <li>when the component is <i>deleted</i> from the data store</li>
   * </ul>
   * This property must be set with Spring bean names (i.e. Spring ids). When
   * needed, Jspresso will query the Spring application context to retrieve the
   * interceptors instances. This property is equivalent to setting
   * <code>lifecycleInterceptorClassNames</code> except that it allows to
   * register interceptor instances that are configured externally in the Spring
   * context. lifecycle interceptor instances must implement the
   * <code>ILifecycleInterceptor</code> interface.
   * 
   * @param lifecycleInterceptorBeanNames
   *          the lifecycleInterceptorBeanNames to set. They are used to
   *          retrieve interceptor instances from the Spring bean factory this
   *          descriptor comes from if any.
   */
  public void setLifecycleInterceptorBeanNames(
      List<String> lifecycleInterceptorBeanNames) {
    this.lifecycleInterceptorBeanNames = StringUtils
        .ensureSpaceFree(lifecycleInterceptorBeanNames);
  }

  /**
   * Gets the readabilityGates.
   * 
   * @return the readabilityGates.
   */
  public Collection<IGate> getReadabilityGates() {
    Set<IGate> gates = new HashSet<IGate>();
    if (readabilityGates != null) {
      gates.addAll(readabilityGates);
    }
    if (getAncestorDescriptors() != null) {
      for (IComponentDescriptor<?> ancestorDescriptor : getAncestorDescriptors()) {
        gates.addAll(ancestorDescriptor.getReadabilityGates());
      }
    }
    return gates;
  }

  /**
   * Sets the readabilityGates.
   * 
   * @param readabilityGates
   *          the readabilityGates to set.
   * @internal
   */
  public void setReadabilityGates(Collection<IGate> readabilityGates) {
    this.readabilityGates = readabilityGates;
  }

  /**
   * Gets the writabilityGates.
   * 
   * @return the writabilityGates.
   */
  public Collection<IGate> getWritabilityGates() {
    Set<IGate> gates = new HashSet<IGate>();
    if (writabilityGates != null) {
      gates.addAll(writabilityGates);
    }
    if (getAncestorDescriptors() != null) {
      for (IComponentDescriptor<?> ancestorDescriptor : getAncestorDescriptors()) {
        gates.addAll(ancestorDescriptor.getWritabilityGates());
      }
    }
    return gates;
  }

  /**
   * Assigns a collection of gates to determine component <i>writability</i>. A
   * component will be considered writable if and only if all gates are open.
   * This mecanism is mainly used for dynamic UI authorization based on model
   * state, e.g. a validated invoice should not be editable anymore.
   * <p>
   * Descriptor assigned gates will be cloned for each component instance
   * created and backed by this descriptor. So basically, each component
   * instance will have its own, unshared collection of writability gates.
   * <p>
   * Jspresso provides a useful set of gate types, like the binary property gate
   * that open/close based on the value of a boolean property of owning
   * component.
   * <p>
   * By default, component descriptors are not assigned any gates collection,
   * i.e. there is no writability restriction. Note that gates do not enforce
   * programatic writability of a component; only UI is impacted.
   * 
   * @param writabilityGates
   *          the writabilityGates to set.
   */
  public void setWritabilityGates(Collection<IGate> writabilityGates) {
    this.writabilityGates = writabilityGates;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isReadOnly() {
    return false;
  }

  /**
   * Instructs Jspresso to use this name when translating this component type
   * name to the data store namespace. This includes , but is not limited to,
   * database table names. A counter example is a component that can be inlined
   * in an entity, thus not assigned a table individually. In that case, the
   * component SQL name serves for composing the component column names.
   * 
   * @param sqlName
   *          the sqlName to set.
   */
  public void setSqlName(String sqlName) {
    this.sqlName = sqlName;
  }

  /**
   * Gets the sqlName.
   * 
   * @return the sqlName.
   */
  public String getSqlName() {
    return sqlName;
  }

}
