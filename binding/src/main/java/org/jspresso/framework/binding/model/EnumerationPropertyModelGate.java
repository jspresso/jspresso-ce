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
package org.jspresso.framework.binding.model;

import java.util.Collection;

/**
 * This gate opens and closes based on the value of an enumeration property
 * matching a set of allowed values.
 *
 * @author Vincent Vandenschrick
 */
public class EnumerationPropertyModelGate extends
    AbstractPropertyModelGate<String> {

  private Collection<String> openingValues;

  /**
   * Configures the enumeration values for which the gate is to be open, unless
   * the {@code openOnTrue} property is set to {@code false}.
   *
   * @param openingValues
   *          the openingValues to set.
   */
  public void setOpeningValues(Collection<String> openingValues) {
    this.openingValues = openingValues;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean shouldOpen(String propertyValue) {
    return propertyValue != null && openingValues != null
        && openingValues.contains(propertyValue);
  }
}
