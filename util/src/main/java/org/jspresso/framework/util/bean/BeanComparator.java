/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
import java.util.Comparator;
import java.util.List;

import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.collection.ESort;

/**
 * A bean comparator which compare beans based on a list of properties using
 * their natural order.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BeanComparator implements Comparator<Object> {

  private static final Comparator<Object> NATURAL_COMPARATOR = new NaturalComparator();
  private List<IAccessor>                 orderingAccessors;
  private List<ESort>                     orderingDirections;

  /**
   * Constructs a new <code>BeanComparator</code> instance.
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
   * {@inheritDoc}
   */
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
      Object o1Val = null;
      Object o2Val = null;
      try {
        o1Val = orderingAccessor.getValue(o1);
        o2Val = orderingAccessor.getValue(o2);
      } catch (IllegalAccessException ex) {
        throw new MissingPropertyException(ex.getMessage());
      } catch (InvocationTargetException ex) {
        throw new MissingPropertyException(ex.getMessage());
      } catch (NoSuchMethodException ex) {
        throw new MissingPropertyException(ex.getMessage());
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
