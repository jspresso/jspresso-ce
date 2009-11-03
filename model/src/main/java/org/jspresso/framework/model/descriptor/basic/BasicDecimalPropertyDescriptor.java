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

import java.math.BigDecimal;

import org.jspresso.framework.model.descriptor.IDecimalPropertyDescriptor;

/**
 * Default implementation of a decimal descriptor.
 * 
 * @version $LastChangedRevision$
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
  public Integer getMaxFractionDigit() {
    return maxFractionDigit;
  }

  /**
   * {@inheritDoc}
   */
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
  public boolean isUsingBigDecimal() {
    if (usingBigDecimal != null) {
      return usingBigDecimal.booleanValue();
    }
    return false;
  }

  /**
   * Sets the maxFractionDigit property.
   * 
   * @param maxFractionDigit
   *          the maxFractionDigit to set.
   */
  public void setMaxFractionDigit(Integer maxFractionDigit) {
    this.maxFractionDigit = maxFractionDigit;
  }

  /**
   * Sets the usingBigDecimal.
   * 
   * @param usingBigDecimal
   *          the usingBigDecimal to set.
   */
  public void setUsingBigDecimal(boolean usingBigDecimal) {
    this.usingBigDecimal = new Boolean(usingBigDecimal);
  }
}
