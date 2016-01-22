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
package org.jspresso.framework.binding;

/**
 * This public interface is implemented by connectors whose value can be parsed
 * and formatted as string.
 *
 * @author Vincent Vandenschrick
 */
public interface IFormattedValueConnector extends IValueConnector {

  /**
   * Gets the connector value as its formatted representation.
   *
   * @return the formatted value, generally a string representation.
   */
  Object getFormattedValue();

  /**
   * Sets the connector value using the formatted value representation.
   *
   * @param formattedValue
   *          the formatted value, generally the string representation string
   *          representation.
   */
  void setFormattedValue(Object formattedValue);
}
