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

/**
 * This gate opens and closes based on the value of a boolean property of the
 * assigned model.
 *
 * @author Vincent Vandenschrick
 */
public class BooleanPropertyModelGate extends AbstractPropertyModelGate<Object> {

  private boolean negatedByName;

  /**
   * Configures the boolean property name.
   *
   * @param booleanPropertyName
   *          the propertyName to set.
   * @deprecated use setPropertyName instead.
   */
  @Deprecated
  public void setBooleanPropertyName(String booleanPropertyName) {
    setPropertyName(booleanPropertyName);
  }

  /**
   * Configures the boolean property name to use. Unless the
   * {@code openOnTrue} property is set to {@code false}, the state of
   * the gate will follow the boolean property value. It supports
   * &quot;<b>!</b>&quot; prefix to negate the property value. It also supports
   * non-boolean properties. In that case, the test is performed against the
   * {@code property != null} condition.
   * @param propertyName the name of the property.
   */
  @Override
  public void setPropertyName(String propertyName) {
    if (propertyName != null && propertyName.startsWith("!")) {
      super.setPropertyName(propertyName.substring(1));
      negatedByName = true;
    } else {
      super.setPropertyName(propertyName);
      negatedByName = false;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean shouldOpen(Object propertyValue) {
    boolean shouldOpen = propertyValue != null;
    if (propertyValue instanceof Boolean) {
      shouldOpen = (Boolean) propertyValue;
    }
    if (negatedByName) {
      return !shouldOpen;
    }
    return shouldOpen;
  }
}
