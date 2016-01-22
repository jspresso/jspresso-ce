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
package org.jspresso.framework.model.descriptor;

/**
 * This interface is implemented by descriptors of decimal properties.
 *
 * @author Vincent Vandenschrick
 */
public interface IDecimalPropertyDescriptor extends INumberPropertyDescriptor {

  /**
   * {@code DEFAULT_MAX_FRACTION_DIGIT} is 2.
   */
  Integer DEFAULT_MAX_FRACTION_DIGIT = 2;

  /**
   * Returns the maximum number of fraction digits allowed in this decimal
   * property. It is guaranteed to return a non-null value which defaults to
   * {@code 2}.
   *
   * @return the number of fraction digits
   */
  Integer getMaxFractionDigit();

  /**
   * Returns true if this property is based on java.math.BigDecimal instead of
   * java.lang.Double.
   *
   * @return true if this property is based on java.math.BigDecimal instead of
   *         java.lang.Double.
   */
  boolean isUsingBigDecimal();

}
