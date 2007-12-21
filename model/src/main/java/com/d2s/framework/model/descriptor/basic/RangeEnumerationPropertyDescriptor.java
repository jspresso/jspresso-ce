/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of an enumerationValues descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RangeEnumerationPropertyDescriptor extends
    AbstractEnumerationPropertyDescriptor {

  private List<String> enumerationValues;
  private Integer      maxValue;
  private Integer      minValue;

  private Integer      rangeStep;

  /**
   * {@inheritDoc}
   */
  public List<String> getEnumerationValues() {
    if (enumerationValues == null) {
      int min = 0;
      int max = 10;
      int step = 1;
      if (minValue != null) {
        min = minValue.intValue();
      }
      if (maxValue != null) {
        max = maxValue.intValue();
      }
      if (rangeStep != null) {
        step = rangeStep.intValue();
      }
      enumerationValues = new ArrayList<String>();
      for (int i = min; i <= max; i += step) {
        enumerationValues.add(Integer.toString(i));
      }
    }
    return enumerationValues;
  }

  /**
   * {@inheritDoc}
   */
  public String getIconImageURL(@SuppressWarnings("unused")
  String value) {
    return null;
  }

  /**
   * Gets the maxValue.
   * 
   * @return the maxValue.
   */
  public Integer getMaxValue() {
    if (maxValue != null) {
      return maxValue;
    }
    if (getParentDescriptor() != null) {
      return ((RangeEnumerationPropertyDescriptor) getParentDescriptor())
          .getMaxValue();
    }
    return maxValue;
  }

  /**
   * Gets the minValue.
   * 
   * @return the minValue.
   */
  public Integer getMinValue() {
    if (minValue != null) {
      return minValue;
    }
    if (getParentDescriptor() != null) {
      return ((RangeEnumerationPropertyDescriptor) getParentDescriptor())
          .getMinValue();
    }
    return minValue;
  }

  /**
   * Gets the rangeStep.
   * 
   * @return the rangeStep.
   */
  public Integer getRangeStep() {
    if (rangeStep != null) {
      return rangeStep;
    }
    if (getParentDescriptor() != null) {
      return ((RangeEnumerationPropertyDescriptor) getParentDescriptor())
          .getRangeStep();
    }
    return rangeStep;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isTranslated() {
    return false;
  }

  /**
   * Sets the maxValue.
   * 
   * @param maxValue
   *            the maxValue to set.
   */
  public void setMaxValue(Integer maxValue) {
    this.maxValue = maxValue;
  }

  /**
   * Sets the minValue.
   * 
   * @param minValue
   *            the minValue to set.
   */
  public void setMinValue(Integer minValue) {
    this.minValue = minValue;
  }

  /**
   * Sets the rangeStep.
   * 
   * @param rangeStep
   *            the rangeStep to set.
   */
  public void setRangeStep(Integer rangeStep) {
    this.rangeStep = rangeStep;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RangeEnumerationPropertyDescriptor clone() {
    RangeEnumerationPropertyDescriptor clonedDescriptor = (RangeEnumerationPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }
}
