/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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

import java.math.BigDecimal;

/**
 * This interface is the super-interface of all number property descriptors.
 * 
 * @author Vincent Vandenschrick
 */
public interface INumberPropertyDescriptor extends IScalarPropertyDescriptor {

  /**
   * Gets the maximum value this property can have.
   * 
   * @return the maximum admissible value
   */
  BigDecimal getMaxValue();

  /**
   * Gets the minimum value this property can have.
   * 
   * @return the minimum admissible value.
   */
  BigDecimal getMinValue();

  /**
   * Gets whether thousands grouping is used.
   * @return {@code true} if thousands grouping is used
   */
  boolean isThousandsGroupingUsed();

  /**
   * Allows to override the default format pattern.
   *
   * @return the overridden format pattern
   */
  String getFormatPattern();

}
