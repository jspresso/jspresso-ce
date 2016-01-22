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

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.jspresso.framework.model.descriptor.IDecimalPropertyDescriptor;

/**
 * Describes a decimal property. Property value is either stored as a
 * {@code Double} or as a {@code BigDecimal} depending on the
 * {@code usingBigDecimal} property.
 *
 * @author Vincent Vandenschrick
 */
public class BasicDecimalPropertyDescriptor extends
    BasicNumberPropertyDescriptor implements IDecimalPropertyDescriptor {

  private Integer maxFractionDigit;
  private Boolean usingBigDecimal;

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicDecimalPropertyDescriptor clone() {
    BasicDecimalPropertyDescriptor clonedDescriptor = (BasicDecimalPropertyDescriptor) super
        .clone();
    return clonedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getMaxFractionDigit() {
    if (maxFractionDigit == null) {
      return DEFAULT_MAX_FRACTION_DIGIT;
    }
    return maxFractionDigit;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getModelType() {
    if (isUsingBigDecimal()) {
      return BigDecimal.class;
    }
    return Double.class;
  }

  /**
   * Returns false by default.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean isUsingBigDecimal() {
    if (usingBigDecimal != null) {
      return usingBigDecimal;
    }
    return false;
  }

  /**
   * Configures the precision of the decimal property. Default value is
   * {@code 2}.
   *
   * @param maxFractionDigit
   *          the maxFractionDigit to set.
   */
  public void setMaxFractionDigit(Integer maxFractionDigit) {
    this.maxFractionDigit = maxFractionDigit;
  }

  /**
   * Configures the property to be managed using
   * {@code java.math.BigDecimal}. Default value is {@code false}
   * which means {@code java.lang.Double} will be used.
   *
   * @param usingBigDecimal
   *          the usingBigDecimal to set.
   */
  public void setUsingBigDecimal(boolean usingBigDecimal) {
    this.usingBigDecimal = usingBigDecimal;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BigDecimal getMinValue() {
    BigDecimal min = super.getMinValue();
    if (min == null) {
      min = new BigDecimal(-Double.MAX_VALUE);
    }
    return min;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BigDecimal getMaxValue() {
    BigDecimal max = super.getMaxValue();
    if (max == null) {
      max = new BigDecimal(Double.MAX_VALUE);
    }
    return max;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean isDefault(BigDecimal boundValue) {
    if (boundValue != null) {
      return boundValue.doubleValue() == Double.MAX_VALUE
          || boundValue.doubleValue() == -Double.MAX_VALUE;
    }
    return super.isDefault(null);
  }

  /**
   * Handle BigDecimal precision.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public Object interceptSetter(Object component, Object newValue) {
    Object actualNewValue = newValue;
    if (getMaxFractionDigit() != null) {
      if (actualNewValue instanceof Double) {
        actualNewValue = new BigDecimal(actualNewValue.toString())
            .setScale(getMaxFractionDigit(), RoundingMode.HALF_EVEN)
            .doubleValue();
      } else if (actualNewValue instanceof BigDecimal) {
        actualNewValue = ((BigDecimal) actualNewValue).setScale(
            getMaxFractionDigit(), RoundingMode.HALF_EVEN);
      }
    }
    return super.interceptSetter(component, actualNewValue);
  }

}
