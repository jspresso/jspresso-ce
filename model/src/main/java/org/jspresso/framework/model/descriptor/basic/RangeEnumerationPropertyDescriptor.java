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

import java.util.ArrayList;
import java.util.List;

/**
 * This is a special enumeration descriptor that allows to build the enumeration
 * values out of a list of integer values. Obviously, no icon is provided for a
 * given value.
 *
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
  @Override
  public RangeEnumerationPropertyDescriptor clone() {
    RangeEnumerationPropertyDescriptor clonedDescriptor = (RangeEnumerationPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getEnumerationValues() {
    if (enumerationValues == null) {
      int min = 0;
      int max = 10;
      int step = 1;
      if (minValue != null) {
        min = minValue;
      }
      if (maxValue != null) {
        max = maxValue;
      }
      if (rangeStep != null) {
        step = rangeStep;
      }
      enumerationValues = new ArrayList<>();
      for (int i = min; i <= max; i += step) {
        enumerationValues.add(Integer.toString(i));
      }
    }
    return enumerationValues;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getIconImageURL(String value) {
    return null;
  }

  /**
   * Gets the maxValue.
   *
   * @return the maxValue.
   */
  public Integer getMaxValue() {
    return maxValue;
  }

  /**
   * Gets the minValue.
   *
   * @return the minValue.
   */
  public Integer getMinValue() {
    return minValue;
  }

  /**
   * Gets the rangeStep.
   *
   * @return the rangeStep.
   */
  public Integer getRangeStep() {
    return rangeStep;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isTranslated() {
    return false;
  }

  /**
   * The enumeration maximum bound. Default value is <i>10</i>.
   *
   * @param maxValue
   *          the maxValue to set.
   */
  public void setMaxValue(Integer maxValue) {
    this.maxValue = maxValue;
  }

  /**
   * The enumeration minimum bound. Default value is <i>0</i>.
   *
   * @param minValue
   *          the minValue to set.
   */
  public void setMinValue(Integer minValue) {
    this.minValue = minValue;
  }

  /**
   * The step to use for constructing the enumeration values, starting from
   * {@code minValue} up to {@code maxValue}. Default value is
   * <i>1</i>.
   *
   * @param rangeStep
   *          the rangeStep to set.
   */
  public void setRangeStep(Integer rangeStep) {
    this.rangeStep = rangeStep;
  }

  /**
   * Returns true.
   * @return {@code true}
   */
  @Override
  public boolean isLov() {
    return true;
  }
}
