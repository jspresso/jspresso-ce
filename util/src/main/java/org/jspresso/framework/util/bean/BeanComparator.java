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
package org.jspresso.framework.util.bean;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.collection.ESort;

/**
 * A bean comparator which compare beans based on a list of properties using
 * their natural order.
 *
 * @author Vincent Vandenschrick
 */
public class BeanComparator implements Comparator<Object> {

  /**
   * {@code NATURAL_COMPARATOR}.
   */
  public static final Comparator<Object> NATURAL_COMPARATOR = new NaturalComparator();
  private final List<IAccessor>                orderingAccessors;
  private final List<ESort>                    orderingDirections;

  /**
   * Constructs a new {@code BeanComparator} instance.
   *
   * @param orderingAccessors
   *          the ordering accessors.
   * @param orderingDirections
   *          the ordering directions.
   */
  public BeanComparator(List<IAccessor> orderingAccessors,
      List<ESort> orderingDirections) {
    this.orderingAccessors = orderingAccessors;
    this.orderingDirections = orderingDirections;
  }

  /**
   * Constructs a new {@code BeanComparator} instance.
   *
   * @param orderingProperties
   *          a map of (propertyName,direction) to define the sort.
   * @param accessorFactory
   *          the accessor factory to access the bean instances properties.
   * @param beanClass
   *          the bean type.
   */
  public BeanComparator(Map<String, ESort> orderingProperties,
      IAccessorFactory accessorFactory, Class<?> beanClass) {
    orderingAccessors = new ArrayList<>();
    orderingDirections = new ArrayList<>();
    if (orderingProperties != null) {
      for (Map.Entry<String, ESort> orderingProperty : orderingProperties
          .entrySet()) {
        orderingAccessors.add(accessorFactory.createPropertyAccessor(
            orderingProperty.getKey(), beanClass));
        orderingDirections.add(orderingProperty.getValue());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compare(Object o1, Object o2) {
    if (o1 == o2) {
      return 0;
    }
    if (o1 == null) {
      return -1;
    }
    if (o2 == null) {
      return 1;
    }
    for (int i = 0; i < orderingAccessors.size(); i++) {
      IAccessor orderingAccessor = orderingAccessors.get(i);
      ESort direction = orderingDirections.get(i);
      Object o1Val;
      Object o2Val;
      try {
        o1Val = orderingAccessor.getValue(o1);
        o2Val = orderingAccessor.getValue(o2);
      } catch (IllegalAccessException | NoSuchMethodException ex) {
        throw new MissingPropertyException(ex.getMessage());
      } catch (InvocationTargetException ex) {
        if (ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        throw new RuntimeException(ex.getCause());
      }
      int result = NATURAL_COMPARATOR.compare(o1Val, o2Val);
      if (result != 0) {
        if (direction == ESort.DESCENDING) {
          result *= (-1);
        }
        return result;
      }
    }
    return 0;
  }

  private static final class NaturalComparator implements Comparator<Object> {

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public int compare(Object o1, Object o2) {
      if (o1 == o2) {
        return 0;
      }
      if (o1 == null) {
        return -1;
      }
      if (o2 == null) {
        return 1;
      }
      if (o1 instanceof Comparable<?>) {
        return ((Comparable<Object>) o1).compareTo(o2);
      }
      return o1.toString().compareTo(o2.toString());
    }

  }
}
