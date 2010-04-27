/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
import java.util.List;

import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IDatePropertyDescriptor;
import org.jspresso.framework.model.descriptor.IDurationPropertyDescriptor;
import org.jspresso.framework.model.descriptor.INumberPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.ITimePropertyDescriptor;
import org.jspresso.framework.model.descriptor.query.ComparableQueryStructureDescriptor;
import org.jspresso.framework.util.collection.IPageable;

/**
 * An implementation used for query components.
 * 
 * @internal
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicQueryComponentDescriptor extends
    AbstractComponentDescriptor<IQueryComponent> {

  private IComponentDescriptor<Object> componentDescriptor;

  /**
   * Constructs a new <code>BasicQueryComponentDescriptor</code> instance.
   * 
   * @param componentDescriptor
   *          the delegate entity descriptor.
   */
  public BasicQueryComponentDescriptor(
      IComponentDescriptor<Object> componentDescriptor) {
    super(componentDescriptor.getComponentContract().getName());
    this.componentDescriptor = componentDescriptor;
    Collection<IPropertyDescriptor> propertyDescriptors = new ArrayList<IPropertyDescriptor>();
    for (IPropertyDescriptor propertyDescriptor : componentDescriptor
        .getPropertyDescriptors()) {
      if (propertyDescriptor instanceof BasicPropertyDescriptor
          && isPropertyFilterComparable(propertyDescriptor)) {
        propertyDescriptors.add(new ComparableQueryStructureDescriptor(
            ((BasicPropertyDescriptor) propertyDescriptor)
                .createQueryDescriptor()));
      } else {
        propertyDescriptors.add(propertyDescriptor.createQueryDescriptor());
      }
    }
    BasicListDescriptor<Object> queriedEntitiesCollectionDescriptor = new BasicListDescriptor<Object>();
    queriedEntitiesCollectionDescriptor
        .setElementDescriptor(componentDescriptor);
    queriedEntitiesCollectionDescriptor
        .setName(IQueryComponent.QUERIED_COMPONENTS);
    queriedEntitiesCollectionDescriptor
        .setDescription("queriedEntities.description");
    BasicCollectionPropertyDescriptor<Object> qCPDescriptor = new BasicCollectionPropertyDescriptor<Object>();
    qCPDescriptor.setName(IQueryComponent.QUERIED_COMPONENTS);
    qCPDescriptor.setReferencedDescriptor(queriedEntitiesCollectionDescriptor);

    propertyDescriptors.add(qCPDescriptor);

    BasicIntegerPropertyDescriptor pagePropertyDescripror = new BasicIntegerPropertyDescriptor();
    pagePropertyDescripror.setName(IPageable.PAGE);
    pagePropertyDescripror.setReadOnly(true);
    propertyDescriptors.add(pagePropertyDescripror);

    BasicIntegerPropertyDescriptor pageSizePropertyDescripror = new BasicIntegerPropertyDescriptor();
    pageSizePropertyDescripror.setName(IPageable.PAGE_SIZE);
    pageSizePropertyDescripror.setReadOnly(true);
    propertyDescriptors.add(pageSizePropertyDescripror);

    BasicIntegerPropertyDescriptor pageCountPropertyDescripror = new BasicIntegerPropertyDescriptor();
    pageCountPropertyDescripror.setName(IPageable.PAGE_COUNT);
    pageCountPropertyDescripror.setReadOnly(true);
    propertyDescriptors.add(pageCountPropertyDescripror);

    BasicIntegerPropertyDescriptor recordCountPropertyDescripror = new BasicIntegerPropertyDescriptor();
    recordCountPropertyDescripror.setName(IPageable.RECORD_COUNT);
    recordCountPropertyDescripror.setReadOnly(true);
    propertyDescriptors.add(recordCountPropertyDescripror);

    setPropertyDescriptors(propertyDescriptors);
    setDescription(componentDescriptor.getDescription());
    setIconImageURL(componentDescriptor.getIconImageURL());
    List<String> qProperties = new ArrayList<String>();
    for (String queryableProperty : componentDescriptor
        .getQueryableProperties()) {
      IPropertyDescriptor propertyDescriptor = getPropertyDescriptor(queryableProperty);
      if (propertyDescriptor instanceof ComparableQueryStructureDescriptor) {
        for (String nestedRenderedProperty : ((IReferencePropertyDescriptor<?>) propertyDescriptor)
            .getReferencedDescriptor().getRenderedProperties()) {
          qProperties.add(propertyDescriptor.getName() + "."
              + nestedRenderedProperty);
        }
      } else {
        qProperties.add(propertyDescriptor.getName());
      }
    }
    setRenderedProperties(qProperties);
    setToStringProperty(componentDescriptor.getToStringProperty());
    setUnclonedProperties(componentDescriptor.getUnclonedProperties());
    setPageSize(componentDescriptor.getPageSize());
    setOrderingProperties(componentDescriptor.getOrderingProperties());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<IQueryComponent> getComponentContract() {
    return IQueryComponent.class;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getQueryComponentContract() {
    return componentDescriptor.getComponentContract();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isEntity() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
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
}
