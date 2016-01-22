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

import org.jspresso.framework.model.descriptor.IIntegerPropertyDescriptor;

/**
 * Describes an integer property. The property is either represented as an
 * {@code Integer} or a {@code Long} depending on the
 * {@code usingLong} property.
 *
 * @author Vincent Vandenschrick
 */
public class BasicIntegerPropertyDescriptor extends
    BasicNumberPropertyDescriptor implements IIntegerPropertyDescriptor {

  private Boolean usingLong;

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicIntegerPropertyDescriptor clone() {
    BasicIntegerPropertyDescriptor clonedDescriptor = (BasicIntegerPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * Returns false by default.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean isUsingLong() {
    if (usingLong != null) {
      return usingLong;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getModelType() {
    if (isUsingLong()) {
      return Long.class;
    }
    return Integer.class;
  }

  /**
   * Configures the property to be managed using {@code java.lang.Long}.
   * Default value is {@code false} which means
   * {@code java.lang.Integer} will be used.
   *
   * @param usingLong
   *          the usingLong to set.
   */
  public void setUsingLong(boolean usingLong) {
    this.usingLong = usingLong;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BigDecimal getMinValue() {
    BigDecimal min = super.getMinValue();
    if (min == null) {
      if (isUsingLong()) {
        min = new BigDecimal(Long.MIN_VALUE);
      } else {
        min = new BigDecimal(Integer.MIN_VALUE);
      }
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
      if (isUsingLong()) {
        max = new BigDecimal(Long.MAX_VALUE);
      } else {
        max = new BigDecimal(Integer.MAX_VALUE);
      }
    }
    return max;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean isDefault(BigDecimal boundValue) {
    if (boundValue != null) {
      if (isUsingLong()) {
        return boundValue.longValue() == Long.MAX_VALUE
            || boundValue.longValue() == Long.MIN_VALUE;
      }
      return boundValue.intValue() == Integer.MAX_VALUE
          || boundValue.intValue() == Integer.MIN_VALUE;
    }
    return super.isDefault(null);
  }

}
