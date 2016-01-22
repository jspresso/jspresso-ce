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
package org.jspresso.framework.gui.remote;

import java.math.BigDecimal;

/**
 * A remote number field component.
 *
 * @author Vincent Vandenschrick
 */
public abstract class RNumericComponent extends RComponent {

  private static final long serialVersionUID = 2168149225835180375L;

  private Number            maxValue;
  private Number            minValue;
  private String            horizontalAlignment;
  private boolean           thousandsGroupingUsed;

  /**
   * Constructs a new {@code RIntegerField} instance.
   *
   * @param guid
   *          the guid.
   */
  public RNumericComponent(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RNumericComponent} instance. Only used for
   * serialization support.
   */
  public RNumericComponent() {
    // For serialization support
  }

  /**
   * Gets the maxValue.
   *
   * @return the maxValue.
   */
  public Number getMaxValue() {
    return maxValue;
  }

  /**
   * Gets the minValue.
   *
   * @return the minValue.
   */
  public Number getMinValue() {
    return minValue;
  }

  /**
   * Sets the maxValue.
   *
   * @param maxValue
   *          the maxValue to set.
   */
  public void setMaxValue(Number maxValue) {
    if (maxValue instanceof BigDecimal) {
      this.maxValue = maxValue.doubleValue();
    } else {
      this.maxValue = maxValue;
    }
  }

  /**
   * Sets the minValue.
   *
   * @param minValue
   *          the minValue to set.
   */
  public void setMinValue(Number minValue) {
    if (minValue instanceof BigDecimal) {
      this.minValue = minValue.doubleValue();
    } else {
      this.minValue = minValue;
    }
  }

  /**
   * Sets the horizontalAlignment.
   *
   * @param horizontalAlignment
   *          the horizontalAlignment to set.
   */
  public void setHorizontalAlignment(String horizontalAlignment) {
    this.horizontalAlignment = horizontalAlignment;
  }

  /**
   * Gets the horizontalAlignment.
   *
   * @return the horizontalAlignment.
   */
  public String getHorizontalAlignment() {
    return horizontalAlignment;
  }

  /**
   * Is thousands grouping used.
   *
   * @return the boolean
   */
  public boolean isThousandsGroupingUsed() {
    return thousandsGroupingUsed;
  }

  /**
   * Sets thousands grouping used.
   *
   * @param thousandsGroupingUsed the thousands grouping used
   */
  public void setThousandsGroupingUsed(boolean thousandsGroupingUsed) {
    this.thousandsGroupingUsed = thousandsGroupingUsed;
  }
}
