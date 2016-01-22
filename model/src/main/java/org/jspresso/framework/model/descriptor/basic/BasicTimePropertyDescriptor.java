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

import java.util.Date;

import org.jspresso.framework.model.descriptor.ITimePropertyDescriptor;

/**
 * Describes a property used to hold time only values. These properties use a
 * {@code Date} to store their value but only the time part of the value is
 * relevant.
 *
 * @author Vincent Vandenschrick
 */
public class BasicTimePropertyDescriptor extends BasicTimeAwarePropertyDescriptor implements ITimePropertyDescriptor {

  private String formatPattern;

  /**
   * Constructs a new {@code BasicTimePropertyDescriptor} instance.
   */
  public BasicTimePropertyDescriptor() {
    super();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicTimePropertyDescriptor clone() {
    BasicTimePropertyDescriptor clonedDescriptor = (BasicTimePropertyDescriptor) super.clone();

    return clonedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getModelType() {
    return Date.class;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getFormatPattern() {
    return formatPattern;
  }

  /**
   * Sets format pattern. Allows to override the default one.
   *
   * @param formatPattern
   *     the format pattern
   */
  public void setFormatPattern(String formatPattern) {
    this.formatPattern = formatPattern;
  }

  /**
   * Is default filter comparable.
   *
   * @return {@code true}
   */
  @Override
  protected boolean isDefaultFilterComparable() {
    return true;
  }
}
