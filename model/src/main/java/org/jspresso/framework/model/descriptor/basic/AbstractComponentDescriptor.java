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
package org.jspresso.framework.model.descriptor.basic;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TLinkedHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.service.IComponentService;
import org.jspresso.framework.model.component.service.ILifecycleInterceptor;
import org.jspresso.framework.model.descriptor.DescriptorException;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IObjectPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.IStringPropertyDescriptor;
import org.jspresso.framework.model.descriptor.ITextPropertyDescriptor;
import org.jspresso.framework.model.entity.EntityHelper;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.collection.ESort;
import org.jspresso.framework.util.descriptor.DefaultIconDescriptor;
import org.jspresso.framework.util.exception.NestedRuntimeException;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.util.lang.ObjectUtils;
import org.jspresso.framework.util.lang.StringUtils;
import org.jspresso.framework.util.sql.SqlHelper;

/**
 * This is the abstract base descriptor for all component-like part of the
 * domain model. All the properties included in this base descriptor can of
 * course be used in concrete sub-types.
 * <p/>
 * These sub-types include :
 * <ul>
 * <li><i>BasicEntityDescriptor</i> for defining a persistent entity</li>
 * <li><i>BasicInterfaceDescriptor</i> for defining a common interface that will
 * be implemented by other entities, components or even sub-interfaces.</li>
 * <li><i>BasicComponentDescriptor</i> for defining reusable structures that can
 * be inline in an entity. It also allows to describe an arbitrary POJO and
 * make use of it in Jspresso UIs.</li>
 * </ul>
 *
 * @param <E>
 *     the concrete type of components.
 * @author Vincent Vandenschrick
 */
public abstract class AbstractComponentDescriptor<E> extends DefaultIconDescriptor
    implements IComponentDescriptor<E>, BeanFactoryAware {

  /**
   * IInterface descriptor for IComponent {@code COMPONENT_DESCRIPTOR}.
   */
  protected static final IComponentDescriptor<IComponent> COMPONENT_DESCRIPTOR = createComponentDescriptor();

  private static final Logger LOG = LoggerFactory.getLogger(AbstractComponentDescriptor.class);

  private List<IComponentDescriptor<?>> ancestorDescriptors;

  private BeanFactory beanFactory;
  private Class<?>    componentContract;

  private Collection<String> grantedRoles;
  private List<String>       lifecycleInterceptorBeanNames;
  private List<String>       lifecycleInterceptorClassNames;

  private List<ILifecycleInterceptor<?>>   lifecycleInterceptors;
  private Map<String, ESort>               orderingProperties;
  private Integer                          pageSize;
  private Map<String, IPropertyDescriptor> propertyDescriptorsMap;

  private List<String>            queryableProperties;
  private List<String>            renderedProperties;
  private IComponentDescriptor<E> queryDescriptor;
  private boolean                 isQueryDescriptor;
  private Collection<IGate>       readabilityGates;

  private Set<Class<?>>                  serviceContracts;
  private Map<String, String>            serviceDelegateBeanNames;
  private Map<String, String>            serviceDelegateClassNames;
  private Map<String, IComponentService> serviceDelegates;

  private String                    sqlName;
  private List<IPropertyDescriptor> tempPropertyBuffer;

  private String toStringProperty;
  private String toHtmlProperty;
  private String autoCompleteProperty;

  private Collection<String> unclonedProperties;

  private Collection<IGate> writabilityGates;

  private Map<String, IPropertyDescriptor> propertyDescriptorsCache;
  private Collection<IPropertyDescriptor>  allPropertyDescriptorsCache;

  private static BasicCollectionPropertyDescriptor<IComponent> componentTranslationsDescriptorTemplate;

  /**
   * Constructs a new {@code AbstractComponentDescriptor} instance.
   *
   * @param name
   *     the name of the descriptor which has to be the fully-qualified
   *     class name of its contract.
   */
  public AbstractComponentDescriptor(String name) {
    setName(name);
    propertyDescriptorsCache = new ConcurrentHashMap<>();
    // Force initialization of ancestor descriptors
    setAncestorDescriptors(null);
    isQueryDescriptor = false;
  }

  private static IComponentDescriptor<IComponent> createComponentDescriptor() {
    BasicInterfaceDescriptor<IComponent> componentDescriptor = new BasicInterfaceDescriptor<>(
        IComponent.class.getName());
    return componentDescriptor;
  }

  private final Object queryDescriptorLock = new Object();

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public IComponentDescriptor<E> createQueryDescriptor() {
    synchronized (queryDescriptorLock) {
      if (isQueryDescriptor) {
        queryDescriptor = this;
      }
      if (queryDescriptor == null) {
        queryDescriptor = (AbstractComponentDescriptor<E>) super.clone();
        ((AbstractComponentDescriptor<E>)queryDescriptor).isQueryDescriptor = true;

        List<IComponentDescriptor<?>> ancestorDescs = getAncestorDescriptors();
        if (ancestorDescs != null) {
          List<IComponentDescriptor<?>> queryAncestorDescriptors = new ArrayList<>();
          for (IComponentDescriptor<?> ancestorDescriptor : ancestorDescs) {
            queryAncestorDescriptors.add(ancestorDescriptor.createQueryDescriptor());
          }
          ((AbstractComponentDescriptor<E>) queryDescriptor).setAncestorDescriptors(queryAncestorDescriptors);
        }

        Collection<IPropertyDescriptor> declaredPropertyDescs = getDeclaredPropertyDescriptors();
        if (declaredPropertyDescs != null) {
          Collection<IPropertyDescriptor> queryPropertyDescriptors = new ArrayList<>();
          for (IPropertyDescriptor propertyDescriptor : declaredPropertyDescs) {
            queryPropertyDescriptors.add(propertyDescriptor.createQueryDescriptor());
          }
          ((AbstractComponentDescriptor<E>) queryDescriptor).setPropertyDescriptors(queryPropertyDescriptors);
        }
      }
    }
    return queryDescriptor;
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
  @Override
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
  @Override
  public IComponentDescriptor<E> getComponentDescriptor() {
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
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
  @Override
  public Collection<String> getGrantedRoles() {
    return grantedRoles;
  }

  /**
   * Gets the lifecycleInterceptors.
   *
   * @return the lifecycleInterceptors.
   */
  @Override
  public List<ILifecycleInterceptor<?>> getLifecycleInterceptors() {
    List<ILifecycleInterceptor<?>> allInterceptors = new ArrayList<>();
    List<IComponentDescriptor<?>> ancestorDescs = getAncestorDescriptors();
    if (ancestorDescs != null) {
      for (IComponentDescriptor<?> ancestorDescriptor : ancestorDescs) {
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
  @Override
  public Class<?> getModelType() {
    return getComponentContract();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getModelTypeName() {
    return getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, ESort> getOrderingProperties() {
    // use a set to avoid duplicates.
    Map<String, ESort> properties = new LinkedHashMap<>();
    if (orderingProperties != null) {
      properties.putAll(orderingProperties);
    }
    List<IComponentDescriptor<?>> ancestorDescs = getAncestorDescriptors();
    if (ancestorDescs != null) {
      for (IComponentDescriptor<?> ancestorDescriptor : ancestorDescs) {
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
  @Override
  public Integer getPageSize() {
    return pageSize;
  }

  private static final BasicObjectPropertyDescriptor NULL_PROPERTY_DESCRIPTOR = new BasicObjectPropertyDescriptor();

  /**
   * {@inheritDoc}
   */
  @Override
  public IPropertyDescriptor getPropertyDescriptor(String propertyName) {
    if (propertyName == null) {
      return null;
    }
    IPropertyDescriptor descriptor = propertyDescriptorsCache.get(propertyName);
    if (descriptor != null) {
      if (descriptor == NULL_PROPERTY_DESCRIPTOR) {
        return null;
      }
      return descriptor;
    }
    int nestedDotIndex = propertyName.indexOf(IAccessor.NESTED_DELIM);
    if (nestedDotIndex > 0) {
      IPropertyDescriptor rootProp = getPropertyDescriptor(propertyName.substring(0, nestedDotIndex));
      if (rootProp instanceof IComponentDescriptorProvider<?>) {
        IComponentDescriptor<?> componentDescriptor = ((IComponentDescriptorProvider<?>) rootProp)
            .getComponentDescriptor();
        descriptor = componentDescriptor.getPropertyDescriptor(propertyName.substring(nestedDotIndex + 1));
        if (descriptor != null) {
          descriptor = descriptor.clone();
          if (descriptor instanceof BasicPropertyDescriptor) {
            ((BasicPropertyDescriptor) descriptor).setName(propertyName);
          }
        }
      }
    } else {
      descriptor = getDeclaredPropertyDescriptor(propertyName);
      List<IComponentDescriptor<?>> ancestorDescs = getAncestorDescriptors();
      // Ancestor descriptors must be walked the reverse order
      // in order to match the getPropertyDescriptors() method.
      if (descriptor == null && ancestorDescs != null) {
        for (int i = ancestorDescs.size() - 1; i >= 0 && descriptor == null; i--) {
          IComponentDescriptor<?> ancestorDescriptor = ancestorDescs.get(i);
          descriptor = ancestorDescriptor.getPropertyDescriptor(propertyName);
        }
      }
    }
    descriptor = refinePropertyDescriptor(descriptor);
    if (descriptor == null) {
      propertyDescriptorsCache.put(propertyName, NULL_PROPERTY_DESCRIPTOR);
    } else {
      propertyDescriptorsCache.put(propertyName, descriptor);
    }
    return descriptor;
  }

  private final Object allPropertyDescriptorsLock = new Object();

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<IPropertyDescriptor> getPropertyDescriptors() {
    synchronized (allPropertyDescriptorsLock) {
      if (allPropertyDescriptorsCache == null) {
        // A map is used instead of a set since a set does not replace an
        // element it already contains.
        Map<String, IPropertyDescriptor> allDescriptors = new LinkedHashMap<>();
        List<IComponentDescriptor<?>> ancestorDescs = getAncestorDescriptors();
        if (ancestorDescs != null) {
          for (IComponentDescriptor<?> ancestorDescriptor : ancestorDescs) {
            for (IPropertyDescriptor propertyDescriptor : ancestorDescriptor.getPropertyDescriptors()) {
              allDescriptors.put(propertyDescriptor.getName(), propertyDescriptor);
            }
          }
        }
        Collection<IPropertyDescriptor> declaredPropertyDescriptors = getDeclaredPropertyDescriptors();
        if (declaredPropertyDescriptors != null) {
          for (IPropertyDescriptor propertyDescriptor : declaredPropertyDescriptors) {
            propertyDescriptor = refinePropertyDescriptor(propertyDescriptor);
            allDescriptors.put(propertyDescriptor.getName(), propertyDescriptor);
          }
        }
        allPropertyDescriptorsCache = new ArrayList<>();
        for (IPropertyDescriptor propertyDescriptor : allDescriptors.values()) {
          allPropertyDescriptorsCache.add(propertyDescriptor);
        }
      }
      return allPropertyDescriptorsCache;
    }
  }

  private final Object queryablePropertiesLock = new Object();

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getQueryableProperties() {
    synchronized (queryablePropertiesLock) {
      if (queryableProperties == null) {
        Set<String> queryablePropertiesSet = new TLinkedHashSet<>(1);
        List<IComponentDescriptor<?>> ancestorDescs = getAncestorDescriptors();
        if (ancestorDescs != null) {
          for (IComponentDescriptor<?> ancestorDescriptor : ancestorDescs) {
            for (String propertyName : ancestorDescriptor.getQueryableProperties()) {
              queryablePropertiesSet.add(propertyName);
            }
          }
        }
        for (String renderedProperty : getRenderedProperties()) {
          IPropertyDescriptor declaredPropertyDescriptor = getDeclaredPropertyDescriptor(renderedProperty);
          if (declaredPropertyDescriptor != null && declaredPropertyDescriptor.isQueryable()) {
            queryablePropertiesSet.add(renderedProperty);
          }
        }
        queryableProperties = new ArrayList<>(queryablePropertiesSet);
      }
    }
    return explodeComponentReferences(this, queryableProperties);
  }

  /**
   * Gets the readabilityGates.
   *
   * @return the readabilityGates.
   */
  @Override
  public Collection<IGate> getReadabilityGates() {
    Set<IGate> gates = new THashSet<>(1);
    if (readabilityGates != null) {
      gates.addAll(readabilityGates);
    }
    List<IComponentDescriptor<?>> ancestorDescs = getAncestorDescriptors();
    if (ancestorDescs != null) {
      for (IComponentDescriptor<?> ancestorDescriptor : ancestorDescs) {
        gates.addAll(ancestorDescriptor.getReadabilityGates());
      }
    }
    return gates;
  }

  private final Object renderedPropertiesLock = new Object();

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getRenderedProperties() {
    synchronized (renderedPropertiesLock) {
      if (renderedProperties == null) {
        Set<String> renderedPropertiesSet = new TLinkedHashSet<>(1);
        List<IComponentDescriptor<?>> ancestorDescs = getAncestorDescriptors();
        if (ancestorDescs != null) {
          for (IComponentDescriptor<?> ancestorDescriptor : ancestorDescs) {
            for (String propertyName : ancestorDescriptor.getRenderedProperties()) {
              renderedPropertiesSet.add(propertyName);
            }
          }
        }
        Collection<IPropertyDescriptor> declaredPropertyDescriptors = getDeclaredPropertyDescriptors();
        if (declaredPropertyDescriptors != null) {
          for (IPropertyDescriptor propertyDescriptor : declaredPropertyDescriptors) {
            if (!(propertyDescriptor instanceof ICollectionPropertyDescriptor<?>)
                && !(propertyDescriptor instanceof ITextPropertyDescriptor)
                && !(propertyDescriptor instanceof IObjectPropertyDescriptor)) {
              String propertyName = propertyDescriptor.getName();
              if (!propertyName.endsWith(RAW_SUFFIX) && !propertyName.endsWith(NLS_SUFFIX)) {
                renderedPropertiesSet.add(propertyName);
              }
            }
          }
        }
        renderedProperties = new ArrayList<>(renderedPropertiesSet);
      }
    }
    return explodeComponentReferences(this, renderedProperties);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<String> getServiceContractClassNames() {
    Set<String> serviceContractClassNames = new TLinkedHashSet<>(1);
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
  @Override
  public Collection<Class<?>> getServiceContracts() {
    registerDelegateServicesIfNecessary();
    if (serviceContracts != null) {
      return new ArrayList<>(serviceContracts);
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IComponentService getServiceDelegate(Method targetMethod) {
    registerDelegateServicesIfNecessary();
    IComponentService service = null;
    if (serviceDelegates != null) {
      service = serviceDelegates.get(targetMethod.getName());
    }
    List<IComponentDescriptor<?>> ancestorDescs = getAncestorDescriptors();
    if (service == null && ancestorDescs != null) {
      for (Iterator<IComponentDescriptor<?>> ite = ancestorDescs.iterator(); service == null && ite.hasNext(); ) {
        IComponentDescriptor<?> ancestorDescriptor = ite.next();
        service = ancestorDescriptor.getServiceDelegate(targetMethod);
      }
    }
    return service;
  }

  /**
   * Gets the sqlName.
   *
   * @return the sqlName.
   */
  public String getSqlName() {
    return sqlName;
  }

  private final Object toStringLock = new Object();

  /**
   * Gets the toStringProperty.
   *
   * @return the toStringProperty.
   */
  @Override
  public String getToStringProperty() {
    synchronized (toStringLock) {
      if (toStringProperty == null) {
        List<String> rp = getRenderedProperties();
        if (rp != null && !rp.isEmpty()) {
          for (String renderedProperty : rp) {
            if (getPropertyDescriptor(renderedProperty) instanceof IStringPropertyDescriptor) {
              toStringProperty = renderedProperty;
              break;
            }
          }
          if (toStringProperty == null) {
            toStringProperty = rp.get(0);
          }
        } else if (getPropertyDescriptor("id") != null) {
          toStringProperty = "id";
        } else {
          toStringProperty = getPropertyDescriptors().iterator().next().getName();
        }
      }
    }
    return toStringProperty;
  }

  /**
   * Gets the toStringProperty.
   *
   * @return the toStringProperty.
   */
  @Override
  public String getToHtmlProperty() {
    if (toHtmlProperty == null) {
      return getToStringProperty();
    }
    return toHtmlProperty;
  }

  private final Object autoCompleteLock = new Object();

  /**
   * Gets the autocomplete property.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public String getAutoCompleteProperty() {
    synchronized (autoCompleteLock) {
      if (autoCompleteProperty == null) {
        IPropertyDescriptor lpd = getPropertyDescriptor(getToStringProperty());
        if (lpd != null && !lpd.isComputed()) {
          autoCompleteProperty = lpd.getName();
        } else {
          List<String> rp = getRenderedProperties();
          if (rp != null && !rp.isEmpty()) {
            for (String renderedProperty : rp) {
              if (getPropertyDescriptor(renderedProperty) instanceof IStringPropertyDescriptor) {
                autoCompleteProperty = renderedProperty;
                break;
              }
            }
            if (autoCompleteProperty == null) {
              Collection<IPropertyDescriptor> allProps = getPropertyDescriptors();
              for (IPropertyDescriptor pd : allProps) {
                if (pd instanceof IStringPropertyDescriptor && !IEntity.ID.equals(pd.getName())) {
                  autoCompleteProperty = pd.getName();
                  break;
                }
              }
            }
            if (autoCompleteProperty == null) {
              autoCompleteProperty = IEntity.ID;
            }
          }
        }
      }
    }
    return autoCompleteProperty;
  }

  /**
   * The properties returned include the uncloned properties of the ancestors.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public Collection<String> getUnclonedProperties() {
    Set<String> properties = new THashSet<>(1);
    if (unclonedProperties != null) {
      properties.addAll(unclonedProperties);
    }
    List<IComponentDescriptor<?>> ancestorDescs = getAncestorDescriptors();
    if (ancestorDescs != null) {
      for (IComponentDescriptor<?> ancestorDescriptor : ancestorDescs) {
        properties.addAll(ancestorDescriptor.getUnclonedProperties());
      }
    }
    return properties;
  }

  /**
   * Gets the writabilityGates.
   *
   * @return the writabilityGates.
   */
  @Override
  public Collection<IGate> getWritabilityGates() {
    Set<IGate> gates = new THashSet<>(1);
    if (writabilityGates != null) {
      gates.addAll(writabilityGates);
    }
    List<IComponentDescriptor<?>> ancestorDescs = getAncestorDescriptors();
    if (ancestorDescs != null) {
      for (IComponentDescriptor<?> ancestorDescriptor : ancestorDescs) {
        gates.addAll(ancestorDescriptor.getWritabilityGates());
      }
    }
    return gates;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isReadOnly() {
    return false;
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
   *     The list of ancestor component descriptors.
   */
  public void setAncestorDescriptors(List<IComponentDescriptor<?>> ancestorDescriptors) {
    this.ancestorDescriptors = ancestorDescriptors;
  }

  /**
   * {@inheritDoc}
   *
   * @internal
   */
  @Override
  public void setBeanFactory(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  /**
   * Assigns the roles that are authorized to manipulate components backed by
   * this descriptor. It supports &quot;<b>!</b>&quot; prefix to negate the
   * role(s). This will directly influence the UI behaviour and even
   * composition. Setting the collection of granted roles to {@code null}
   * (default value) disables role based authorization on this component level.
   * Note that this authorization enforcement does not prevent programmatic
   * access that is of the developer responsibility.
   *
   * @param grantedRoles
   *     the grantedRoles to set.
   */
  public void setGrantedRoles(Collection<String> grantedRoles) {
    this.grantedRoles = StringUtils.ensureSpaceFree(grantedRoles);
  }

  /**
   * Registers a list of lifecycle interceptor instances that will be triggered
   * on the different phases of the component lifecycle, i.e. :
   * <ul>
   * <li>when the component is <i>instantiated</i> in memory</li>
   * <li>when the component is <i>created</i> in the data store</li>
   * <li>when the component is <i>updated</i> in the data store</li>
   * <li>when the component is <i>loaded</i> from the data store</li>
   * <li>when the component is <i>deleted</i> from the data store</li>
   * </ul>
   * This property must be set with Spring bean names (i.e. Spring ids). When
   * needed, Jspresso will query the Spring application context to retrieve the
   * interceptors instances. This property is equivalent to setting
   * {@code lifecycleInterceptorClassNames} except that it allows to
   * register interceptor instances that are configured externally in the Spring
   * context. lifecycle interceptor instances must implement the
   * {@code ILifecycleInterceptor&lt;E&gt;} interface where &lt;E&gt; is a
   * type assignable from the component type.
   *
   * @param lifecycleInterceptorBeanNames
   *     the lifecycleInterceptorBeanNames to set. They are used to
   *     retrieve interceptor instances from the Spring bean factory this
   *     descriptor comes from if any.
   */
  public void setLifecycleInterceptorBeanNames(List<String> lifecycleInterceptorBeanNames) {
    this.lifecycleInterceptorBeanNames = StringUtils.ensureSpaceFree(lifecycleInterceptorBeanNames);
  }

  /**
   * Much the same as {@code lifecycleInterceptorBeanNames} except that
   * instead of providing a list of Spring bean names, you provide a list of
   * fully qualified class names. These classes must :
   * <ul>
   * <li>provide a default constructor</li>
   * <li>implement the {@code IPropertyProcessor&lt;E, F&gt;} interface.</li>
   * </ul>
   * When needed, Jspresso will create the property processor instances.
   *
   * @param lifecycleInterceptorClassNames
   *     the lifecycleInterceptorClassNames to set.
   */
  public void setLifecycleInterceptorClassNames(List<String> lifecycleInterceptorClassNames) {
    this.lifecycleInterceptorClassNames = StringUtils.ensureSpaceFree(lifecycleInterceptorClassNames);
  }

  /**
   * Ordering properties are used to sort un-indexed collections of instances of
   * components backed by this descriptor. This sort order can be overridden on
   * the finer collection property level to change the way a specific collection
   * is sorted. This property consist of a {@code Map} whose entries are
   * composed with :
   * <ul>
   * <li>the property name as key</li>
   * <li>the sort order for this property as value. This is either a value of
   * the {@code ESort} enum (<i>ASCENDING</i> or <i>DESCENDING</i>) or its
   * equivalent string representation.</li>
   * </ul>
   * Ordering properties are considered following their order in the map
   * iterator. A {@code null} value (default) will not give any indication
   * for the collection sort order.
   *
   * @param untypedOrderingProperties
   *     the orderingProperties to set.
   */
  public void setOrderingProperties(Map<String, ?> untypedOrderingProperties) {
    if (untypedOrderingProperties != null) {
      orderingProperties = new LinkedHashMap<>();
      for (Map.Entry<String, ?> untypedOrderingProperty : untypedOrderingProperties.entrySet()) {
        if (untypedOrderingProperty.getValue() instanceof ESort) {
          orderingProperties.put(untypedOrderingProperty.getKey(), (ESort) untypedOrderingProperty.getValue());
        } else if (untypedOrderingProperty.getValue() instanceof String) {
          orderingProperties.put(untypedOrderingProperty.getKey(), ESort.valueOf(
              (String) untypedOrderingProperty.getValue()));
        } else {
          orderingProperties.put(untypedOrderingProperty.getKey(), ESort.ASCENDING);
        }
      }
    } else {
      orderingProperties = null;
    }
  }

  /**
   * Whenever a collection of this component type is presented in a pageable UI,
   * this property gives the size (number of component instances) of one page.
   * This size can usually be refined at a lower level (e.g. at reference
   * property descriptor for &quot;lists of values&quot;). A {@code null}
   * value (default) disables paging for this component.
   *
   * @param pageSize
   *     the pageSize to set.
   */
  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  /**
   * This property allows to describe the properties of the components backed by
   * this descriptor. Like in classic OO programming, the actual set of
   * properties available to a component is the union of its properties and of
   * its ancestors' ones. Jspresso also allows you to refine a property
   * descriptor in a child component descriptor exactly as you would do it in a
   * subclass. In that case, the attributes of the property defined in the child
   * descriptor prevails over the definition of its ancestors. Naturally,
   * properties are keyed by their names.
   *
   * @param descriptors
   *     the propertyDescriptors to set.
   */
  public void setPropertyDescriptors(Collection<IPropertyDescriptor> descriptors) {
    // This is important to use an intermediate structure since all descriptors
    // may not have their names fully initialized.
    if (descriptors != null) {
      tempPropertyBuffer = new ArrayList<>(descriptors);
      propertyDescriptorsMap = null;
    } else {
      tempPropertyBuffer = null;
      propertyDescriptorsMap = null;
    }
    resetPropertyDescriptorCaches();
  }

  private void resetPropertyDescriptorCaches() {
    propertyDescriptorsCache = new ConcurrentHashMap<>();
    allPropertyDescriptorsCache = null;
  }

  /**
   * This property allows to define which of the component properties are to be
   * used in the filter UIs that are based on this component family (a QBE
   * screen for instance). Since this is a {@code List} queryable
   * properties are rendered in the same order.
   * <p/>
   * Whenever this this property is {@code null} (default value), Jspresso
   * chooses the default set of queryable properties based on their type. For
   * instance, collection properties and binary properties are not used but
   * string, numeric, reference, ... properties are. A computed property cannot
   * be used since it has no data store existence and thus cannot be queried
   * upon.
   * <p/>
   * Note that this property is not inherited by children descriptors, i.e. even
   * if an ancestor defines an explicit set of queryable properties, its
   * children ignore this setting.
   *
   * @param queryableProperties
   *     the queryableProperties to set.
   */
  public void setQueryableProperties(List<String> queryableProperties) {
    this.queryableProperties = StringUtils.ensureSpaceFree(queryableProperties);
  }

  /**
   * Sets the readabilityGates.
   *
   * @param readabilityGates
   *     the readabilityGates to set.
   * @internal
   */
  public void setReadabilityGates(Collection<IGate> readabilityGates) {
    this.readabilityGates = readabilityGates;
  }

  /**
   * This property allows to define which of the component properties are to be
   * rendered by default when displaying a UI based on this component family.
   * For instance, a table will render 1 column per rendered property of the
   * component. Any type of property can be used except collection properties.
   * Since this is a {@code List} queryable properties are rendered in the
   * same order.
   * <p/>
   * Whenever this property is {@code null} (default value) Jspresso
   * determines the default set of properties to render based on their types,
   * e.g. ignores collection properties.
   * <p/>
   * Note that this property is not inherited by children descriptors, i.e. even
   * if an ancestor defines an explicit set of rendered properties, its children
   * ignore this setting.
   *
   * @param renderedProperties
   *     the renderedProperties to set.
   */
  public void setRenderedProperties(List<String> renderedProperties) {
    this.renderedProperties = StringUtils.ensureSpaceFree(renderedProperties);
  }

  /**
   * Registers the collection of service delegate instances attached to this
   * component. These delegate instances will automatically be triggered
   * whenever a method of the service interface it implements get executed. For
   * instance :
   * <ul>
   * <li>the component interface is {@code MyBeanClass}. It implements the
   * service interface {@code MyService}.</li>
   * <li>the service interface {@code MyService} contains method
   * {@code int foo(String)}.</li>
   * <li>the service delegate class, e.g. {@code MyServiceImpl} must
   * implement the method {@code int foo(MyBeanClass,String)}. Note that
   * the parameter list is augmented with the owing component type as 1st
   * parameter. This allows to have stateless implementation for delegates, thus
   * sharing instances of delegates among instances of components.</li>
   * <li>when {@code foo(String)} is executed on an instance of
   * {@code MyBeanClass}, the framework will trigger the delegate
   * implementation, passing the instance of the component itself as parameter.</li>
   * </ul>
   * This property must be set with a map keyed by service interfaces and valued
   * by Spring bean names (i.e. Spring ids). Each bean name corresponds to an
   * instance of service delegate. When needed, Jspresso will query the Spring
   * application context to retrieve the delegate instances. This property is
   * equivalent to setting {@code serviceDelegateClassNames} except that it
   * allows to register delegate instances that are configured externally in the
   * Spring context. lifecycle interceptor instances must implement the
   * {@code IComponentService} marker interface.
   *
   * @param serviceDelegateBeanNames
   *     the serviceDelegateBeanNames to set. They are used to retrieve
   *     delegate instances from the Spring bean factory this descriptor
   *     comes from if any.
   */
  public void setServiceDelegateBeanNames(Map<String, String> serviceDelegateBeanNames) {
    this.serviceDelegateBeanNames = StringUtils.ensureSpaceFree(serviceDelegateBeanNames);
  }

  /**
   * Much the same as {@code serviceDelegateBeanNames} except that instead
   * of providing a map valued with Spring bean names, you provide a map valued
   * with fully qualified class names. These class must :
   * <ul>
   * <li>provide a default constructor</li>
   * <li>implement the {@code IComponentService} marker interface.</li>
   * </ul>
   * When needed, Jspresso will create service delegate instances.
   *
   * @param serviceDelegateClassNames
   *     the component services to be registered keyed by their contract. A
   *     service contract is an interface class defining the service
   *     methods to be registered as implemented by the service delegate.
   *     Map values must be instances of {@code IComponentService}.
   */
  public void setServiceDelegateClassNames(Map<String, String> serviceDelegateClassNames) {
    this.serviceDelegateClassNames = StringUtils.ensureSpaceFree(serviceDelegateClassNames);
  }

  /**
   * Instructs Jspresso to use this name when translating this component type
   * name to the data store namespace. This includes , but is not limited to,
   * database table names.
   * <p/>
   * Default value is {@code null} so that Jspresso uses its default naming
   * policy.
   *
   * @param sqlName
   *     the sqlName to set.
   */
  public void setSqlName(String sqlName) {
    this.sqlName = sqlName;
  }

  /**
   * Allows to customize the string representation of a component instance. The
   * property name assigned will be used when displaying the component instance
   * as a string. It may be a computed property that composes several other
   * properties in a human friendly format.
   * <p/>
   * Whenever this property is {@code null}, the following rule apply to
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
   *     the toStringProperty to set.
   */
  public void setToStringProperty(String toStringProperty) {
    this.toStringProperty = toStringProperty;
  }

  /**
   * Allows to customize the HTML representation of a component instance. The
   * property name assigned will be used when displaying the component instance
   * as HTML. It may be a computed property that composes several other
   * properties in a human friendly format.
   * <p/>
   * Whenever this property is {@code null}, the
   * {@code toStringProperty} is used. Note that this property is not
   * inherited by children descriptors, i.e. even if an ancestor defines an
   * explicit <i>toHtmlProperty</i> property, its children ignore this setting.
   *
   * @param toHtmlProperty
   *     the toHtmlProperty to set.
   */
  public void setToHtmlProperty(String toHtmlProperty) {
    this.toHtmlProperty = toHtmlProperty;
  }

  /**
   * Allows to customize the property used to autocomplete reference fields on
   * this component.
   * <p/>
   * Whenever this property is {@code null}, the following rule apply to
   * determine the <i>lovProperty</i> :
   * <ol>
   * <li>the toString property if not a computed one</li>
   * <li>the first string property from the rendered property</li>
   * <li>the first rendered property if no string property is found among them</li>
   * </ol>
   * Note that this property is not inherited by children descriptors, i.e. even
   * if an ancestor defines an explicit <i>lovProperty</i>, its children ignore
   * this setting.
   *
   * @param autoCompleteProperty
   *     the autoCompleteProperty to set.
   */
  public void setAutoCompleteProperty(String autoCompleteProperty) {
    this.autoCompleteProperty = autoCompleteProperty;
  }

  /**
   * Configures the properties that must not be cloned when this component is
   * duplicated. For instance, tracing information like a created timestamp
   * should not be cloned; a SSN neither. For a given component, the uncloned
   * properties are the ones it defines augmented by the ones its ancestors
   * define. There is no mean to make a component property cloneable if one of
   * the ancestor declares it un-cloneable.
   *
   * @param unclonedProperties
   *     the unclonedProperties to set.
   */
  public void setUnclonedProperties(Collection<String> unclonedProperties) {
    this.unclonedProperties = StringUtils.ensureSpaceFree(unclonedProperties);
  }

  /**
   * Assigns a collection of gates to determine component <i>writability</i>. A
   * component will be considered writable if and only if all gates are open.
   * This mechanism is mainly used for dynamic UI authorization based on model
   * state, e.g. a validated invoice should not be editable anymore.
   * <p/>
   * Descriptor assigned gates will be cloned for each component instance
   * created and backed by this descriptor. So basically, each component
   * instance will have its own, unshared collection of writability gates.
   * <p/>
   * Jspresso provides a useful set of gate types, like the binary property gate
   * that open/close based on the value of a boolean property of owning
   * component.
   * <p/>
   * By default, component descriptors are not assigned any gates collection,
   * i.e. there is no writability restriction. Note that gates do not enforce
   * programmatic writability of a component; only UI is impacted.
   *
   * @param writabilityGates
   *     the writabilityGates to set.
   */
  public void setWritabilityGates(Collection<IGate> writabilityGates) {
    this.writabilityGates = writabilityGates;
  }

  private static ThreadLocal<Collection<Class<?>>> sofeWatchdog = new ThreadLocal<>();

  static List<String> explodeComponentReferences(IComponentDescriptor<?> componentDescriptor,
                                                 List<String> propertyNames) {
    boolean createdWatchDog = false;
    Collection<Class<?>> watchDog = sofeWatchdog.get();
    if (watchDog == null) {
      createdWatchDog = true;
      watchDog = new LinkedHashSet<>();
      sofeWatchdog.set(watchDog);
    }
    try {
      List<String> explodedProperties = new ArrayList<>();
      for (String propertyName : propertyNames) {
        IPropertyDescriptor propertyDescriptor = componentDescriptor.getPropertyDescriptor(propertyName);
        if (propertyDescriptor instanceof IReferencePropertyDescriptor<?> && EntityHelper.isInlineComponentReference(
            (IReferencePropertyDescriptor<?>) propertyDescriptor)) {
          if (watchDog.contains(componentDescriptor.getComponentContract())) {
            // Whenever there are circular references between inline components, do not try to resolve them since it's
            // impossible, but log the warning.
            LOG.warn("A circular reference has been detected on inline {} components. You should explicitly declare "
                    + "their rendered properties since they cannot be computed automatically.", watchDog);
          } else {
            watchDog.add(componentDescriptor.getComponentContract());
            List<String> nestedProperties = new ArrayList<>();
            List<String> nestedRenderedProperties;
            nestedRenderedProperties = ((IReferencePropertyDescriptor<?>) propertyDescriptor).getReferencedDescriptor()
                                                                                             .getRenderedProperties();
            for (String nestedRenderedProperty : nestedRenderedProperties) {
              nestedProperties.add(propertyName + "." + nestedRenderedProperty);
            }
            explodedProperties.addAll(explodeComponentReferences(componentDescriptor, nestedProperties));
          }
        } else {
          explodedProperties.add(propertyName);
        }
      }
      return explodedProperties;
    } finally {
      if (createdWatchDog) {
        sofeWatchdog.remove();
      }
    }
  }

  private IPropertyDescriptor getDeclaredPropertyDescriptor(String propertyName) {
    processPropertiesBufferIfNecessary();
    if (propertyDescriptorsMap != null) {
      return propertyDescriptorsMap.get(propertyName);
    }
    return null;
  }

  private final Object propertiesBufferLock = new Object();

  @SuppressWarnings("unchecked")
  private void processPropertiesBufferIfNecessary() {
    synchronized (propertiesBufferLock) {
      if (tempPropertyBuffer != null) {
        propertyDescriptorsMap = new LinkedHashMap<>();
        for (IPropertyDescriptor descriptor : tempPropertyBuffer) {
          if (descriptor instanceof IStringPropertyDescriptor) {
            if (((IStringPropertyDescriptor) descriptor).isTranslatable()) {
              String rawSqlName = ((BasicStringPropertyDescriptor) descriptor).getSqlName();
              if (rawSqlName == null) {
                rawSqlName = new SqlHelper().transformToSql(descriptor.getName(), null);
              }
              ((BasicStringPropertyDescriptor) descriptor).setSqlName(rawSqlName);
              if (!descriptor.getName().endsWith(RAW_SUFFIX)) {
                ((BasicStringPropertyDescriptor) descriptor).setName(descriptor.getName() + RAW_SUFFIX);
              }
            }
          }
          propertyDescriptorsMap.put(descriptor.getName(), descriptor);
        }
        tempPropertyBuffer = null;
        if (isTranslatable()) {
          for (IPropertyDescriptor translatablePropertyDescriptor : getPropertyDescriptors()) {
            if (translatablePropertyDescriptor instanceof IStringPropertyDescriptor
                && ((IStringPropertyDescriptor) translatablePropertyDescriptor).isTranslatable()
                && !translatablePropertyDescriptor.getName().endsWith(NLS_SUFFIX)) {
              completeWithComputedNlsDescriptors(translatablePropertyDescriptor);
            }
          }
          if (!isPurelyAbstract()) {
            BasicCollectionPropertyDescriptor<IComponent> translationsPropertyDescriptor =
                getComponentTranslationsDescriptorTemplate()
                .clone();
            translationsPropertyDescriptor.setSqlName("T");
            BasicCollectionDescriptor<IComponent> translationsCollectionDescriptor =
                (BasicCollectionDescriptor<IComponent>) ((BasicCollectionDescriptor<IComponent>)
                    translationsPropertyDescriptor
                .getReferencedDescriptor()).clone();
            BasicComponentDescriptor<IComponent> translationDescriptor = (BasicComponentDescriptor<IComponent>) (
                (BasicComponentDescriptor<IComponent>) translationsCollectionDescriptor
                .getElementDescriptor()).clone();
            translationsPropertyDescriptor.setReferencedDescriptor(translationsCollectionDescriptor);
            translationsCollectionDescriptor.setElementDescriptor(translationDescriptor);
            translationDescriptor.setName(getName() + "$Translation");
            propertyDescriptorsMap.put(translationsPropertyDescriptor.getName(), translationsPropertyDescriptor);
          }
        }
        resetPropertyDescriptorCaches();
      }
    }
  }

  private void completeWithComputedNlsDescriptors(IPropertyDescriptor rawDescriptor) {
    String barePropertyName = rawDescriptor.getName();
    if (barePropertyName.endsWith(RAW_SUFFIX)) {
      barePropertyName = barePropertyName.substring(0, barePropertyName.length() - RAW_SUFFIX.length());
    }
    BasicStringPropertyDescriptor nlsDescriptor = (BasicStringPropertyDescriptor) rawDescriptor.clone();
    nlsDescriptor.setName(barePropertyName + NLS_SUFFIX);
    nlsDescriptor.setDelegateWritable(true);
    nlsDescriptor.setComputed(true);
    if (!isPurelyAbstract()) {
      nlsDescriptor.setSqlName("(SELECT T.TRANSLATED_VALUE FROM {tableName}_T T WHERE T." +
          "T_{tableName}_ID = ID AND T.LANGUAGE = :JspressoSessionGlobals.language AND " +
          "T.PROPERTY_NAME = '" + barePropertyName + "')");
    }
    BasicStringPropertyDescriptor rawOrNlsDescriptor = (BasicStringPropertyDescriptor) rawDescriptor.clone();
    rawOrNlsDescriptor.setName(barePropertyName);
    rawOrNlsDescriptor.setDelegateWritable(true);
    rawOrNlsDescriptor.setComputed(true);
    if (!isPurelyAbstract()) {
      rawOrNlsDescriptor.setSqlName("CASE WHEN " +
          nlsDescriptor.getSqlName() +
          " IS NULL THEN " +
          ((BasicPropertyDescriptor) rawDescriptor).getSqlName() +
          " ELSE " +
          nlsDescriptor.getSqlName() +
          " END");
    }
    propertyDescriptorsMap.put(nlsDescriptor.getName(), nlsDescriptor);
    propertyDescriptorsMap.put(rawOrNlsDescriptor.getName(), rawOrNlsDescriptor);
  }

  private final Object delegateServicesLock = new Object();

  private void registerDelegateServicesIfNecessary() {
    synchronized (delegateServicesLock) {
      if (serviceDelegateClassNames != null) {
        for (Entry<String, String> nextEntry : serviceDelegateClassNames.entrySet()) {
          try {
            IComponentService delegate = null;
            String serviceClassName = nextEntry.getKey();
            String serviceDelegateClassName = nextEntry.getValue();
            if (!(serviceDelegateClassName == null || "".equals(serviceDelegateClassName) || "null".equalsIgnoreCase(
                serviceDelegateClassName))) {
              delegate = (IComponentService) Class.forName(serviceDelegateClassName).newInstance();
            }
            registerService(Class.forName(ObjectUtils.extractRawClassName(serviceClassName)), delegate);
          } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
            throw new DescriptorException(ex);
          }
        }
        serviceDelegateClassNames = null;
      }
    }
    synchronized (delegateServicesLock) {
      if (serviceDelegateBeanNames != null && beanFactory != null) {
        for (Entry<String, String> nextEntry : serviceDelegateBeanNames.entrySet()) {
          try {
            String serviceClassName = nextEntry.getKey();
            String serviceDelegateBeanName = nextEntry.getValue();
            if (!(serviceDelegateBeanName == null || "".equals(serviceDelegateBeanName) || "null".equalsIgnoreCase(
                serviceDelegateBeanName))) {
              registerService(Class.forName(ObjectUtils.extractRawClassName(serviceClassName)), beanFactory.getBean(
                  serviceDelegateBeanName, IComponentService.class));
            }
          } catch (ClassNotFoundException ex) {
            throw new DescriptorException(ex);
          }
        }
        serviceDelegateBeanNames = null;
      }
    }
  }

  private void registerLifecycleInterceptor(ILifecycleInterceptor<?> lifecycleInterceptor) {
    if (lifecycleInterceptors == null) {
      lifecycleInterceptors = new ArrayList<>();
    }
    lifecycleInterceptors.add(lifecycleInterceptor);
  }

  private final Object lifecycleInterceptorsLock = new Object();

  private void registerLifecycleInterceptorsIfNecessary() {
    synchronized (lifecycleInterceptorsLock) {
      // process creation of lifecycle interceptors.
      if (lifecycleInterceptorClassNames != null) {
        for (String lifecycleInterceptorClassName : lifecycleInterceptorClassNames) {
          try {
            registerLifecycleInterceptor((ILifecycleInterceptor<?>) Class.forName(lifecycleInterceptorClassName)
                                                                         .newInstance());
          } catch (InstantiationException | ClassNotFoundException | IllegalAccessException ex) {
            throw new DescriptorException(ex);
          }
        }
        lifecycleInterceptorClassNames = null;
      }
    }
    synchronized (lifecycleInterceptorsLock) {
      if (lifecycleInterceptorBeanNames != null && beanFactory != null) {
        for (String lifecycleInterceptorBeanName : lifecycleInterceptorBeanNames) {
          registerLifecycleInterceptor(beanFactory.getBean(lifecycleInterceptorBeanName, ILifecycleInterceptor.class));
        }
        lifecycleInterceptorBeanNames = null;
      }
    }
  }

  private void registerService(Class<?> serviceContract, IComponentService service) {
    if (serviceDelegates == null) {
      serviceDelegates = new THashMap<>(1, 1.0f);
      serviceContracts = new THashSet<>(1);
    }
    serviceContracts.add(serviceContract);
    Method[] contractServices = serviceContract.getMethods();
    for (Method serviceMethod : contractServices) {
      serviceDelegates.put(serviceMethod.getName(), service);
    }
  }

  /**
   * Returns the component contract class name.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public String getPermId() {
    return getName();
  }

  /**
   * A component permanent id is forced to be its fully-qualified class name.
   * Trying to set it to another value will raise an exception.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public void setPermId(String permId) {
    throw new UnsupportedOperationException();
  }

  /**
   * Allow subclasses to hook up and potentially transform the actual property
   * descriptor.
   *
   * @param propertyDescriptor
   *     the original property descriptor.
   * @return the transformed property descriptor.
   */
  protected IPropertyDescriptor refinePropertyDescriptor(IPropertyDescriptor propertyDescriptor) {
    return propertyDescriptor;
  }

  /**
   * Is translated.
   *
   * @return the boolean
   */
  @Override
  public boolean isTranslatable() {
    if (isDeclaredTranslatable()) {
      return true;
    }
    if (getAncestorDescriptors() != null) {
      for (IComponentDescriptor<?> ancestorDescriptor : getAncestorDescriptors()) {
        if (ancestorDescriptor.isTranslatable()) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Is declared translatable.
   *
   * @return the boolean
   */
  protected boolean isDeclaredTranslatable() {
    Collection<IPropertyDescriptor> propertyDescriptors = getDeclaredPropertyDescriptors();
    if (propertyDescriptors != null) {
      for (IPropertyDescriptor pDesc : propertyDescriptors) {
        if (pDesc instanceof IStringPropertyDescriptor) {
          if (((IStringPropertyDescriptor) pDesc).isTranslatable()) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Sets component translations descriptor template.
   *
   * @param template
   *     the template
   */
  public static synchronized void setComponentTranslationsDescriptorTemplate(
      BasicCollectionPropertyDescriptor<IComponent> template) {
    componentTranslationsDescriptorTemplate = template;
  }

  /**
   * Gets component translations descriptor template.
   *
   * @return the component translation descriptor template
   */
  public static synchronized BasicCollectionPropertyDescriptor<IComponent> getComponentTranslationsDescriptorTemplate
  () {
    return componentTranslationsDescriptorTemplate;
  }
}
