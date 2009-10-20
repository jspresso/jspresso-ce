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
package org.jspresso.framework.model.component.basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IComponentFactory;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.bean.BeanComparator;
import org.jspresso.framework.util.collection.ESort;

/**
 * Base class for component factories.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractComponentFactory implements IComponentFactory {

  private IAccessorFactory accessorFactory;

  /**
   * Gets the accessorFactory.
   * 
   * @return the accessorFactory.
   */
  protected IAccessorFactory getAccessorFactory() {
    return accessorFactory;
  }

  /**
   * Sets the accessorFactory used by this entity factory.
   * 
   * @param accessorFactory
   *          the accessorFactory to set.
   */
  public void setAccessorFactory(IAccessorFactory accessorFactory) {
    this.accessorFactory = accessorFactory;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public void sortCollectionProperty(IComponent component, String propertyName) {
    Collection<Object> propertyValue = (Collection<Object>) component
        .straightGetProperty(propertyName);
    ICollectionPropertyDescriptor propertyDescriptor = (ICollectionPropertyDescriptor) getComponentDescriptor(
        component.getComponentContract()).getPropertyDescriptor(propertyName);
    if (propertyValue != null
        && !propertyValue.isEmpty()
        && !List.class.isAssignableFrom(propertyDescriptor
            .getCollectionDescriptor().getCollectionInterface())) {
      Map<String, ESort> orderingProperties = propertyDescriptor
          .getOrderingProperties();
      if (orderingProperties != null && !orderingProperties.isEmpty()) {
        List<IAccessor> orderingAccessors = new ArrayList<IAccessor>();
        List<ESort> orderingDirections = new ArrayList<ESort>();
        Class collectionElementContract = propertyDescriptor
            .getCollectionDescriptor().getElementDescriptor()
            .getComponentContract();
        for (Map.Entry<String, ESort> orderingProperty : orderingProperties
            .entrySet()) {
          orderingAccessors.add(accessorFactory.createPropertyAccessor(
              orderingProperty.getKey(), collectionElementContract));
          orderingDirections.add(orderingProperty.getValue());
        }
        BeanComparator comparator = new BeanComparator(orderingAccessors,
            orderingDirections);
        List<Object> collectionCopy = new ArrayList<Object>(propertyValue);
        Collections.sort(collectionCopy, comparator);
        Collection<Object> collectionProperty = propertyValue;
        collectionProperty.clear();
        collectionProperty.addAll(collectionCopy);
      }
    }
  }
}
