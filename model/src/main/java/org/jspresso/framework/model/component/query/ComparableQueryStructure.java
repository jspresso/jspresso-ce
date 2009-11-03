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
package org.jspresso.framework.model.component.query;

import org.jspresso.framework.util.bean.AbstractPropertyChangeCapable;

/**
 * A simple query structure which holds a comparator, and inf value and a sup
 * value.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ComparableQueryStructure extends
    AbstractPropertyChangeCapable {

  private String comparator;
  private Object infValue;
  private Object supValue;

  /**
   * Gets the comparator.
   * 
   * @return the comparator.
   */
  public String getComparator() {
    return comparator;
  }

  /**
   * Gets the infValue.
   * 
   * @return the infValue.
   */
  public Object getInfValue() {
    return infValue;
  }

  /**
   * Gets the supValue.
   * 
   * @return the supValue.
   */
  public Object getSupValue() {
    return supValue;
  }

  /**
   * Sets the comparator.
   * 
   * @param comparator
   *            the comparator to set.
   */
  public void setComparator(String comparator) {
    String oldComparator = this.comparator;
    this.comparator = comparator;
    firePropertyChange("comparator", oldComparator, this.comparator);
  }

  /**
   * Sets the infValue.
   * 
   * @param infValue
   *            the infValue to set.
   */
  public void setInfValue(Object infValue) {
    Object oldInfValue = this.infValue;
    this.infValue = infValue;
    firePropertyChange("infValue", oldInfValue, this.infValue);
  }

  /**
   * Sets the supValue.
   * 
   * @param supValue
   *            the supValue to set.
   */
  public void setSupValue(Object supValue) {
    Object oldSupValue = this.supValue;
    this.supValue = supValue;
    firePropertyChange("supValue", oldSupValue, this.supValue);
  }
}
