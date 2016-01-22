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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.component.query.ComparableQueryStructure;
import org.jspresso.framework.model.component.query.EnumQueryStructure;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IQueryComponentDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.query.ComparableQueryStructureDescriptor;
import org.jspresso.framework.model.descriptor.query.EnumQueryStructureDescriptor;
import org.jspresso.framework.model.entity.IEntity;

/**
 * An implementation used for query components.
 *
 * @internal
 * @author Vincent Vandenschrick
 * @param <E>
 *          the concrete type of components.
 */
public class RefQueryComponentDescriptor<E> extends
    AbstractComponentDescriptor<E> implements IQueryComponentDescriptor {

  private static final Logger LOG = LoggerFactory.getLogger(BasicQueryComponentDescriptorFactory.class);

  private final Class<? extends E>                                 componentContract;
  private final IComponentDescriptorProvider<? extends IComponent> queryComponentsDescriptorProvider;

  private final Map<Class<? extends IComponent>, IComponentDescriptor<? extends IComponent>> registry;

  /**
   * Constructs a new {@code BasicQueryComponentDescriptor} instance.
   *
   * @param componentDescriptorProvider
   *          the provider for delegate entity descriptor.
   * @param componentContract
   *          the actual query component contract.
   * @param registry
   *          the shared registry to store / retrieve referenced query
   *          descriptors.
   */
  @SuppressWarnings("unchecked")
  protected RefQueryComponentDescriptor(
      IComponentDescriptorProvider<? extends IComponent> componentDescriptorProvider,
      Class<? extends E> componentContract,
      Map<Class<? extends IComponent>, IComponentDescriptor<? extends IComponent>> registry) {
    super(IQueryComponent.class.getName());
    this.registry = registry;
    this.registry.put((Class<IComponent>) componentContract, (IComponentDescriptor<IComponent>) this);
    this.queryComponentsDescriptorProvider = componentDescriptorProvider;
    this.componentContract = componentContract;
    if (getI18nNameKey() == null && componentContract != null) {
      setI18nNameKey(componentContract.getName());
    }
    Collection<IPropertyDescriptor> propertyDescriptors = new ArrayList<>();
    for (IPropertyDescriptor propertyDescriptor : getQueriedComponentsDescriptor().getPropertyDescriptors()) {
      propertyDescriptors.add(propertyDescriptor.createQueryDescriptor());
    }

    propertyDescriptors.addAll(getExtraPropertyDescriptors());

    setPropertyDescriptors(propertyDescriptors);
  }

  /**
   * Performs delayed configuration.
   */
  protected void finishConfiguration() {
    setDescription(getQueriedComponentsDescriptor().getDescription());
    setIcon(getQueriedComponentsDescriptor().getIcon());
    List<String> qProperties = new ArrayList<>();
    for (String queryableProperty : queryComponentsDescriptorProvider.getQueryableProperties()) {
      IPropertyDescriptor propertyDescriptor = getPropertyDescriptor(queryableProperty);
      if (propertyDescriptor instanceof ComparableQueryStructureDescriptor) {
        for (String nestedRenderedProperty : ((ComparableQueryStructureDescriptor) propertyDescriptor)
            .getRenderedProperties()) {
          qProperties.add(propertyDescriptor.getName() + "." + nestedRenderedProperty);
        }
      } else {
        qProperties.add(propertyDescriptor.getName());
      }
    }
    setRenderedProperties(qProperties);
    setQueryableProperties(queryComponentsDescriptorProvider.getQueryableProperties());
    setToStringProperty(getQueriedComponentsDescriptor().getToStringProperty());
    setToHtmlProperty(getQueriedComponentsDescriptor().getToHtmlProperty());
    setAutoCompleteProperty(getQueriedComponentsDescriptor()
        .getAutoCompleteProperty());
    setUnclonedProperties(getQueriedComponentsDescriptor()
        .getUnclonedProperties());
    setPageSize(getQueriedComponentsDescriptor().getPageSize());
    setOrderingProperties(getQueriedComponentsDescriptor()
        .getOrderingProperties());
  }

  /**
   * Allows subclasses to specify extra property descriptors.
   *
   * @return none.
   */
  protected Collection<IPropertyDescriptor> getExtraPropertyDescriptors() {
    return Collections.emptyList();
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected IPropertyDescriptor refinePropertyDescriptor(
      IPropertyDescriptor propertyDescriptor) {
    // Only refine properties that belong to the original entity
    if (propertyDescriptor == null
        || getQueriedComponentsDescriptor().getPropertyDescriptor(
            propertyDescriptor.getName()) == null) {
      return propertyDescriptor;
    }
    IPropertyDescriptor refinedPropertyDescriptor;
    if (propertyDescriptor instanceof AbstractEnumerationPropertyDescriptor
        && ((AbstractEnumerationPropertyDescriptor) propertyDescriptor)
            .isQueryMultiselect()) {
      refinedPropertyDescriptor = new EnumQueryStructureDescriptor(
          (AbstractEnumerationPropertyDescriptor) propertyDescriptor);
    } else if (propertyDescriptor instanceof BasicPropertyDescriptor
        && isPropertyFilterComparable(propertyDescriptor)) {
      refinedPropertyDescriptor = new ComparableQueryStructureDescriptor(
          ((BasicPropertyDescriptor) propertyDescriptor)
              .createQueryDescriptor());
    } else if ((propertyDescriptor instanceof IReferencePropertyDescriptor<?>)) {
      IComponentDescriptor<? extends IComponent> referencedDescriptor;
      referencedDescriptor = ((IReferencePropertyDescriptor<IComponent>) propertyDescriptor)
          .getReferencedDescriptor();
      Class<? extends IComponent> referencedType;
      try {
        referencedType = referencedDescriptor.getComponentContract();
      } catch (RuntimeException ex) {
        LOG.debug("An exception occurred while trying to load the class {}. This might be normal during compilation.",
            referencedDescriptor.getName(), ex);
        referencedType = null;
      }
      if (referencedType != null) {
        if (propertyDescriptor instanceof BasicReferencePropertyDescriptor<?> && !(ComparableQueryStructure.class
            .isAssignableFrom(referencedType)) && !(EnumQueryStructure.class.isAssignableFrom(referencedType))
            && !(referencedDescriptor instanceof RefQueryComponentDescriptor<?>)) {
          BasicReferencePropertyDescriptor<IComponent> basicRefPropDesc = (
              (BasicReferencePropertyDescriptor<IComponent>) propertyDescriptor);
          // List<String> savedRenderedProperties = basicRefPropDesc
          // .getRenderedProperties();
          basicRefPropDesc.setReferencedDescriptor(createOrGetRefQueryDescriptor(referencedDescriptor, referencedType));
          // basicRefPropDesc.setRenderedProperties(savedRenderedProperties);
        }
      }
      refinedPropertyDescriptor = propertyDescriptor;
    } else {
      refinedPropertyDescriptor = propertyDescriptor;
    }
    return refinedPropertyDescriptor;
  }

  private IComponentDescriptor<? extends IComponent> createOrGetRefQueryDescriptor(
      IComponentDescriptor<? extends IComponent> referencedDescriptor,
      Class<? extends IComponent> referencedType) {
    IComponentDescriptor<? extends IComponent> refQueryDescriptor;
    synchronized (registry) {
      refQueryDescriptor = registry.get(referencedType);
      if (refQueryDescriptor == null) {
        refQueryDescriptor = new RefQueryComponentDescriptor<>(
            referencedDescriptor, referencedType, registry);
        ((RefQueryComponentDescriptor<?>) refQueryDescriptor)
            .finishConfiguration();
      }
    }
    return refQueryDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<? extends E> getComponentContract() {
    return componentContract;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEntity() {
    return IEntity.class.isAssignableFrom(getComponentContract());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPurelyAbstract() {
    return false;
  }

  /**
   * Whether we need to create a comparable query structure for this property.
   *
   * @param propertyDescriptor
   *          the property descriptor to test.
   * @return true if we need to create a comparable query structure for this
   *         property.
   */
  protected boolean isPropertyFilterComparable(
      IPropertyDescriptor propertyDescriptor) {
    return propertyDescriptor.isFilterComparable();
  }

  /**
   * Gets the queriedComponentsDescriptor.
   *
   * @return the queriedComponentsDescriptor.
   */
  @Override
  public IComponentDescriptor<? extends IComponent> getQueriedComponentsDescriptor() {
    return queryComponentsDescriptorProvider.getComponentDescriptor();
  }
}
