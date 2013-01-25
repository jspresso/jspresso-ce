/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.model.component.query;

import org.jspresso.framework.model.component.IComponentFactory;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.query.ComparableQueryStructureDescriptor;

/**
 * A simple query structure which holds a comparator, and inf value and a sup
 * value.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ComparableQueryStructure extends QueryComponent {

  /**
   * Constructs a new <code>ComparableQueryStructure</code> instance.
   * 
   * @param componentDescriptor
   *          the query componentDescriptor.
   * @param componentFactory
   *          the component factory.
   */
  public ComparableQueryStructure(IComponentDescriptor<?> componentDescriptor,
      IComponentFactory componentFactory) {
    super(componentDescriptor, componentFactory);
    setComparator(ComparableQueryStructureDescriptor.EQ);
  }

  /**
   * Gets the comparator.
   * 
   * @return the comparator.
   */
  public String getComparator() {
    return (String) get(ComparableQueryStructureDescriptor.COMPARATOR);
  }

  /**
   * Gets the infValue.
   * 
   * @return the infValue.
   */
  public Object getInfValue() {
    return get(ComparableQueryStructureDescriptor.INF_VALUE);
  }

  /**
   * Gets the supValue.
   * 
   * @return the supValue.
   */
  public Object getSupValue() {
    return get(ComparableQueryStructureDescriptor.SUP_VALUE);
  }

  /**
   * Sets the comparator.
   * 
   * @param comparator
   *          the comparator to set.
   */
  public void setComparator(String comparator) {
    put(ComparableQueryStructureDescriptor.COMPARATOR, comparator);
  }

  /**
   * Sets the infValue.
   * 
   * @param infValue
   *          the infValue to set.
   */
  public void setInfValue(Object infValue) {
    put(ComparableQueryStructureDescriptor.INF_VALUE, infValue);
  }

  /**
   * Sets the supValue.
   * 
   * @param supValue
   *          the supValue to set.
   */
  public void setSupValue(Object supValue) {
    put(ComparableQueryStructureDescriptor.SUP_VALUE, supValue);
  }

  /**
   * Wether the comparable query structure actually holds a restriction.
   * 
   * @return <code>true</code> if the comparable query structure actually holds
   *         a restriction.
   */
  public boolean isRestricting() {
    return getInfValue() != null || getSupValue() != null;
  }

  /**
   * Wether the value passed as parameter matches the query structure.
   * 
   * @param value
   *          the value to test.
   * @return <code>true</code> if the value passed as parameter matches the
   *         query structure.
   */
  public boolean matches(Comparable<Object> value) {
    if (isRestricting()) {
      if (value == null) {
        return false;
      }
      String comparator = getComparator();
      Object infValue = getInfValue();
      Object supValue = getSupValue();
      Object compareValue = infValue;
      if (compareValue == null) {
        compareValue = supValue;
      }
      if (ComparableQueryStructureDescriptor.EQ.equals(comparator)) {
        return value.compareTo(compareValue) == 0;
      } else if (ComparableQueryStructureDescriptor.GT.equals(comparator)) {
        return value.compareTo(compareValue) > 0;
      } else if (ComparableQueryStructureDescriptor.GE.equals(comparator)) {
        return value.compareTo(compareValue) >= 0;
      } else if (ComparableQueryStructureDescriptor.LT.equals(comparator)) {
        return value.compareTo(compareValue) < 0;
      } else if (ComparableQueryStructureDescriptor.LE.equals(comparator)) {
        return value.compareTo(compareValue) <= 0;
      } else if (ComparableQueryStructureDescriptor.BE.equals(comparator)) {
        if (infValue != null && supValue != null) {
          return value.compareTo(infValue) >= 0
              && value.compareTo(supValue) <= 0;
        } else if (infValue != null) {
          return value.compareTo(infValue) >= 0;
        } else {
          return value.compareTo(supValue) <= 0;
        }
      }
    }
    return true;
  }
}
