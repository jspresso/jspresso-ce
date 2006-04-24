/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

  private String              enumerationName;
  private Integer             maxLength;
  private Map<String, String> valuesAndIconImageUrls;

  /**
   * {@inheritDoc}
   */
  public List<String> getEnumerationValues() {
    return new ArrayList<String>(valuesAndIconImageUrls.keySet());
  }

  /**
   * Sets the valuesAndIconImageUrls property.
   * 
   * @param valuesAndIconImageUrls
   *          the valuesAndIconImageUrls to set.
   */
  public void setValuesAndIconImageUrls(
      Map<String, String> valuesAndIconImageUrls) {
    this.valuesAndIconImageUrls = valuesAndIconImageUrls;
  }

  /**
   * {@inheritDoc}
   */
  public String getEnumerationName() {
    if (enumerationName != null) {
      return enumerationName;
    }
    if (getParentDescriptor() != null) {
      return ((IEnumerationPropertyDescriptor) getParentDescriptor())
          .getEnumerationName();
    }
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
    if (maxLength != null) {
      return maxLength;
    }
    if (getParentDescriptor() != null) {
      return ((IEnumerationPropertyDescriptor) getParentDescriptor())
          .getMaxLength();
    }
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

  /**
   * {@inheritDoc}
   */
  public String getIconImageURL(String value) {
    if (valuesAndIconImageUrls != null) {
      return valuesAndIconImageUrls.get(value);
    }
    return null;
  }
}
