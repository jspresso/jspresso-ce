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
package org.jspresso.framework.view.descriptor;

import java.util.Set;

import org.jspresso.framework.util.gui.Dimension;

/**
 * A property view descriptor used to refine (filter) values that are displayed
 * in enumeration property views.
 *
 * @author Vincent Vandenschrick
 */
public interface IEnumerationPropertyViewDescriptor extends
    IPropertyViewDescriptor {

  /**
   * Returns an optional allowed set of values to restrict the model ones. Only
   * values belonging to the allowed ones should actually be made available as a
   * choice.
   *
   * @return an optional allowed set of values to restrict the model ones.
   */
  Set<String> getAllowedValues();

  /**
   * Returns an optional forbidden set of values to restrict the model ones.
   * Only values not belonging to the forbidden ones should actually be made
   * available as a choice.
   *
   * @return an optional forbidden set of values to restrict the model ones.
   */
  Set<String> getForbiddenValues();

  /**
   * Should values be rendered separately using radio buttons.
   *
   * @return {@code true} if values should be rendered separately using
   *         radio buttons.
   */
  boolean isRadio();

  /**
   * Should radio values be rendered horizontally or vertically.
   *
   * @return {@code HORIZONTAL} if radio values should be rendered
   *         horizontally and {@code VERTICAL} otherwise.
   */
  EOrientation getOrientation();

  /**
   * Gets enumeration icon dimension.
   *
   * @return the enumeration icon dimension
   */
  Dimension getEnumIconDimension();

}
