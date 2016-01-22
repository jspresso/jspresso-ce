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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jspresso.framework.model.descriptor.ICollectionDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.util.collection.ESort;
import org.jspresso.framework.util.descriptor.DefaultDescriptor;

/**
 * This descriptor is used to describe a collection of components (entities,
 * interfaces or components). This descriptor is mainly used to qualify the
 * collection referenced by a collection property descriptor. As of now,
 * Jspresso supports :
 * <ul>
 * <li>collections with {@code Set} semantic: do not allow for duplicates
 * and do not preserve the order of the elements in the data store</li>
 * <li>collections with {@code List} semantic: allows for duplicates and
 * preserves the order of the elements in the data store through an implicit
 * index column</li>
 * </ul>
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the concrete collection component element type.
 */
public class BasicCollectionDescriptor<E> extends DefaultDescriptor implements
    ICollectionDescriptor<E> {

  private Class<?>                          collectionInterface;
  private IComponentDescriptor<? extends E> elementDescriptor;
  private Map<String, ESort>                orderingProperties;
  private boolean                           nullElementAllowed;

  /**
   * {@inheritDoc}
   */
  @Override
  public ICollectionDescriptor<E> getCollectionDescriptor() {
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getCollectionInterface() {
    if (Set.class.equals(collectionInterface) || List.class.equals(collectionInterface)) {
      return collectionInterface;
    }
    return Set.class;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IComponentDescriptor<? extends E> getElementDescriptor() {
    return elementDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getModelType() {
    return getCollectionInterface();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getModelTypeName() {
    return getModelType().getName();
  }

  /**
   * Gets the orderingProperties.
   *
   * @return the orderingProperties.
   */
  @Override
  public Map<String, ESort> getOrderingProperties() {
    if (orderingProperties != null) {
      return orderingProperties;
    }
    if (getElementDescriptor() != null) {
      return getElementDescriptor().getOrderingProperties();
    }
    return null;
  }

  /**
   * Allows to choose between the supported collection semantics. The incoming
   * class property value must be one of :
   * <ul>
   * <li>{@code java.util.Set}</li>
   * <li>{@code java.util.List}</li>
   * </ul>
   * Any other value is not supported and make the descriptor fall back to its
   * default. A {@code null} value (default) is equivalent to setting
   * {@code java.util.Set}. Alternatively, you can use descriptor
   * sub-types, i.e. {@code BasicSetDescriptor} and
   * {@code BasicListDescriptor} that make this property usage useless
   * since they enforce their collection interface.
   *
   * @param collectionInterface
   *          the collectionInterface to set.
   */
  public void setCollectionInterface(Class<?> collectionInterface) {
    this.collectionInterface = collectionInterface;
  }

  /**
   * Describes the elements contained in this collection. It can be any of
   * entity, interface or component descriptor.
   *
   * @param elementDescriptor
   *          the elementDescriptor to set.
   */
  public void setElementDescriptor(
      IComponentDescriptor<? extends E> elementDescriptor) {
    this.elementDescriptor = elementDescriptor;
  }

  /**
   * Ordering properties are used to sort this collection if and only if it is
   * un-indexed (not a {@code List}). The sort order set on the collection
   * can refine the default one that might have been set on the element type
   * level. This property consist of a {@code Map} whose entries are
   * composed with :
   * <ul>
   * <li>the property name as key</li>
   * <li>the sort order for this property as value. This is either a value of
   * the {@code ESort} enum (<i>ASCENDING</i> or <i>DESCENDING</i>) or its
   * equivalent string representation.</li>
   * </ul>
   * Ordering properties are considered following their order in the map
   * iterator. A {@code null} value (default) will not give any indication
   * for the collection sort order and thus, will delegate to higher
   * specification levels (e.g. the element type sort order).
   *
   * @param untypedOrderingProperties
   *          the orderingProperties to set.
   */
  public void setOrderingProperties(Map<String, ?> untypedOrderingProperties) {
    if (untypedOrderingProperties != null) {
      orderingProperties = new LinkedHashMap<>();
      for (Map.Entry<String, ?> untypedOrderingProperty : untypedOrderingProperties
          .entrySet()) {
        if (untypedOrderingProperty.getValue() instanceof ESort) {
          orderingProperties.put(untypedOrderingProperty.getKey(),
              (ESort) untypedOrderingProperty.getValue());
        } else if (untypedOrderingProperty.getValue() instanceof String) {
          orderingProperties.put(untypedOrderingProperty.getKey(),
              ESort.valueOf((String) untypedOrderingProperty.getValue()));
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
   * Is null element allowed.
   *
   * @return the boolean
   */
  @Override
  public boolean isNullElementAllowed() {
    return nullElementAllowed;
  }

  /**
   * Configures the collection to accept null element values or not. If the collection does not allows for null
   * values, it forbids to have holes in lists, i.e. all elements have consecutive indices.
   *
   * @param nullElementAllowed {@code true} if the collection accepts {@code null} elements.
   */
  public void setNullElementAllowed(boolean nullElementAllowed) {
    this.nullElementAllowed = nullElementAllowed;
  }
}
