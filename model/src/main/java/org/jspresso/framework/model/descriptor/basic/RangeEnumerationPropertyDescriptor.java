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
package org.jspresso.framework.model.descriptor.basic;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of an enumerationValues descriptor.
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
  @Override
  public RangeEnumerationPropertyDescriptor clone() {
    RangeEnumerationPropertyDescriptor clonedDescriptor = (RangeEnumerationPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

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
  public String getIconImageURL(@SuppressWarnings("unused") String value) {
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
  public boolean isTranslated() {
    return false;
  }

  /**
   * Sets the maxValue.
   * 
   * @param maxValue
   *          the maxValue to set.
   */
  public void setMaxValue(Integer maxValue) {
    this.maxValue = maxValue;
  }

  /**
   * Sets the minValue.
   * 
   * @param minValue
   *          the minValue to set.
   */
  public void setMinValue(Integer minValue) {
    this.minValue = minValue;
  }

  /**
   * Sets the rangeStep.
   * 
   * @param rangeStep
   *          the rangeStep to set.
   */
  public void setRangeStep(Integer rangeStep) {
    this.rangeStep = rangeStep;
  }
}
