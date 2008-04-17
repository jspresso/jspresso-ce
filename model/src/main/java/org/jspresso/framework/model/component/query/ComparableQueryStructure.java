/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.component.query;

import org.jspresso.framework.util.bean.AbstractPropertyChangeCapable;

/**
 * A simple query structure which holds a comparator, and inf value and a sup
 * value.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
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
   * Gets the infValue.
   * 
   * @return the infValue.
   */
  public Object getInfValue() {
    return infValue;
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
   * Gets the supValue.
   * 
   * @return the supValue.
   */
  public Object getSupValue() {
    return supValue;
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
