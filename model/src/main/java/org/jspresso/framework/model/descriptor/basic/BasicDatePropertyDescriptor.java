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

import org.jspresso.framework.model.descriptor.EDateType;
import org.jspresso.framework.model.descriptor.IDatePropertyDescriptor;

/**
 * Describes a date based property. Whether the date property should include time
 * information or not, can be configured using the type property.
 *
 * @author Vincent Vandenschrick
 */
public class BasicDatePropertyDescriptor extends BasicTimeAwarePropertyDescriptor implements IDatePropertyDescriptor {

  private EDateType type;
  private boolean   timeZoneAware;
  private String    formatPattern;

  /**
   * Constructs a new {@code BasicDatePropertyDescriptor} instance.
   */
  public BasicDatePropertyDescriptor() {
    type = EDateType.DATE;
    timeZoneAware = false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicDatePropertyDescriptor clone() {
    BasicDatePropertyDescriptor clonedDescriptor = (BasicDatePropertyDescriptor) super.clone();

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
  public EDateType getType() {
    return type;
  }

  /**
   * Sets whether this property should contain time information or not. The
   * incoming value must be part of the {@code EDateType} enum, i.e. :
   * <ul>
   * <li>{@code DATE} if the property should only contain the date
   * information</li>
   * <li>{@code DATE_TIME} if the property should contain both date and
   * time information</li>
   * </ul>
   * Default value is {@code EDateType.DATE}.
   *
   * @param type
   *     the type to set.
   */
  public void setType(EDateType type) {
    this.type = type;
  }

  /**
   * Sets whether this date property should have its string representation vary
   * depending on the client timezone.
   * <p/>
   * Default value is {@code false}, meaning that the date is considered as
   * a string. It is in fact expressed in the server timezone.
   *
   * @param timeZoneAware
   *     the timeZoneAware to set.
   */
  public void setTimeZoneAware(boolean timeZoneAware) {
    this.timeZoneAware = timeZoneAware;
  }

  /**
   * Gets the timeZoneAware.
   *
   * @return the timeZoneAware.
   */
  @Override
  public boolean isTimeZoneAware() {
    return timeZoneAware;
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
