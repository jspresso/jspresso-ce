/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.util.List;

import com.d2s.framework.model.descriptor.IEnumerationPropertyDescriptor;

/**
 * Default implementation of an enumerationValues descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicEnumerationPropertyDescriptor extends
    BasicScalarPropertyDescriptor implements IEnumerationPropertyDescriptor {

  private String       enumerationName;
  private List<String> enumerationValues;
  private Integer      maxLength;

  /**
   * {@inheritDoc}
   */
  public List<String> getEnumerationValues() {
    return enumerationValues;
  }

  /**
   * Sets the enumerationValues property.
   * 
   * @param enumerationValues
   *          the enumerationValues to set.
   */
  public void setEnumerationValues(List<String> enumerationValues) {
    this.enumerationValues = enumerationValues;
  }

  /**
   * {@inheritDoc}
   */
  public String getEnumerationName() {
    return enumerationName;
  }

  /**
   * Sets the enumerationName.
   * 
   * @param enumerationName
   *          the enumerationName to set.
   */
  public void setEnumerationName(String enumerationName) {
    this.enumerationName = enumerationName;
  }

  /**
   * {@inheritDoc}
   */
  public Class getPropertyClass() {
    return String.class;
  }

  /**
   * Gets the maxLength.
   * 
   * @return the maxLength.
   */
  public Integer getMaxLength() {
    return maxLength;
  }

  /**
   * Sets the maxLength.
   * 
   * @param maxLength
   *          the maxLength to set.
   */
  public void setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
  }
}
