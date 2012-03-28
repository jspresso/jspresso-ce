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
package org.jspresso.framework.model.descriptor.basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IDatePropertyDescriptor;
import org.jspresso.framework.model.descriptor.IDurationPropertyDescriptor;
import org.jspresso.framework.model.descriptor.INumberPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.ITimePropertyDescriptor;
import org.jspresso.framework.model.descriptor.query.ComparableQueryStructureDescriptor;

/**
 * An implementation used for query components.
 * 
 * @internal
 * @version $LastChangedRevision: 5787 $
 * @author Vincent Vandenschrick
 * @param <E>
 *          the concrete type of components.
 */
public class RefQueryComponentDescriptor<E> extends
    AbstractComponentDescriptor<E> {

  private Class<E>                                   componentContract;
  private IComponentDescriptor<? extends IComponent> queriedComponentsDescriptor;

  /**
   * Constructs a new <code>BasicQueryComponentDescriptor</code> instance.
   * 
   * @param componentDescriptorProvider
   *          the provider for delegate entity descriptor.
   * @param componentContract
   *          the actual query component contract.
   */
  public RefQueryComponentDescriptor(
      IComponentDescriptorProvider<IComponent> componentDescriptorProvider,
      Class<E> componentContract) {
    super(componentDescriptorProvider.getComponentDescriptor()
        .getComponentContract().getName());
    this.queriedComponentsDescriptor = componentDescriptorProvider
        .getComponentDescriptor();
    this.componentContract = componentContract;
    Collection<IPropertyDescriptor> propertyDescriptors = new ArrayList<IPropertyDescriptor>();
    for (IPropertyDescriptor propertyDescriptor : getQueriedComponentsDescriptor()
        .getPropertyDescriptors()) {
      propertyDescriptors.add(propertyDescriptor.createQueryDescriptor());
    }

    propertyDescriptors.addAll(getExtraPropertyDescriptors());

    setPropertyDescriptors(propertyDescriptors);
    setDescription(getQueriedComponentsDescriptor().getDescription());
    setIconImageURL(getQueriedComponentsDescriptor().getIconImageURL());
    List<String> qProperties = new ArrayList<String>();
    for (String queryableProperty : componentDescriptorProvider
        .getQueryableProperties()) {
      IPropertyDescriptor propertyDescriptor = getPropertyDescriptor(queryableProperty);
      if (propertyDescriptor instanceof ComparableQueryStructureDescriptor) {
        for (String nestedRenderedProperty : ((ComparableQueryStructureDescriptor) propertyDescriptor)
            .getRenderedProperties()) {
          qProperties.add(propertyDescriptor.getName() + "."
              + nestedRenderedProperty);
        }
      } else {
        qProperties.add(propertyDescriptor.getName());
      }
    }
    setRenderedProperties(qProperties);
    setToStringProperty(getQueriedComponentsDescriptor().getToStringProperty());
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
    if (propertyDescriptor != null
        && propertyDescriptor.getName() != null
        && (propertyDescriptor.getName().endsWith(
            ComparableQueryStructureDescriptor.INF_VALUE) || propertyDescriptor
            .getName().endsWith(ComparableQueryStructureDescriptor.SUP_VALUE))) {
      return propertyDescriptor;
    }
    IPropertyDescriptor refinedPropertyDescriptor;
    if (propertyDescriptor instanceof BasicPropertyDescriptor
        && isPropertyFilterComparable(propertyDescriptor)) {
      refinedPropertyDescriptor = new ComparableQueryStructureDescriptor(
          ((BasicPropertyDescriptor) propertyDescriptor)
              .createQueryDescriptor());
    } else if ((propertyDescriptor instanceof IReferencePropertyDescriptor<?>)
        && !(((IReferencePropertyDescriptor<?>) propertyDescriptor)
            .getReferencedDescriptor() instanceof RefQueryComponentDescriptor<?>)) {
      Class<IComponent> refType = (Class<IComponent>) ((IReferencePropertyDescriptor<?>) propertyDescriptor)
          .getReferencedDescriptor().getComponentContract();
      ((BasicReferencePropertyDescriptor<IComponent>) propertyDescriptor)
          .setReferencedDescriptor(new RefQueryComponentDescriptor<IComponent>(
              (IReferencePropertyDescriptor<IComponent>) propertyDescriptor,
              refType));
      refinedPropertyDescriptor = propertyDescriptor;
    } else {
      refinedPropertyDescriptor = propertyDescriptor;
    }
    return refinedPropertyDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<E> getComponentContract() {
    return componentContract;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getQueryComponentContract() {
    return getQueriedComponentsDescriptor().getComponentContract();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEntity() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPurelyAbstract() {
    return false;
  }

  /**
   * Wether we need to create a comparable query structure for this property.
   * 
   * @param propertyDescriptor
   *          the property descriptor to test.
   * @return true if we need to create a comparable query structure for this
   *         property.
   */
  protected boolean isPropertyFilterComparable(
      IPropertyDescriptor propertyDescriptor) {
    return propertyDescriptor instanceof INumberPropertyDescriptor
        || propertyDescriptor instanceof IDatePropertyDescriptor
        || propertyDescriptor instanceof ITimePropertyDescriptor
        || propertyDescriptor instanceof IDurationPropertyDescriptor;
  }

  /**
   * Gets the queriedComponentsDescriptor.
   * 
   * @return the queriedComponentsDescriptor.
   */
  protected IComponentDescriptor<? extends IComponent> getQueriedComponentsDescriptor() {
    return queriedComponentsDescriptor;
  }
}
