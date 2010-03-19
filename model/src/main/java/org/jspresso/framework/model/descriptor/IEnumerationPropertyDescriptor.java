/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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

import java.util.List;

/**
 * This interface is implemented by descriptors of enumeration properties. This
 * type of properties is constrained by a fixed value enumeration like
 * MALE/FEMALE or MONDAY/TUESDAY/WEDNESDAY/...
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IEnumerationPropertyDescriptor extends
    IScalarPropertyDescriptor {

  /**
   * Gets the name of the enumeration (like GENDER for a MALE/FEMALE
   * enumeration).
   * 
   * @return name of the underlying enumeration.
   */
  String getEnumerationName();

  /**
   * Gets the admisible values of the enumeration.
   * 
   * @return the list of values contained in the underlying enumeration.
   */
  List<String> getEnumerationValues();

  /**
   * Gets the icon image url to use to render a enumeration value.
   * 
   * @param value
   *            the value to render.
   * @return the image url to use.
   */
  String getIconImageURL(String value);

  /**
   * Gets the maximum length of the underlying string property.
   * 
   * @return the string property maximum length.
   */
  Integer getMaxLength();

  /**
   * Gets wether the enumeration values should be presented translated (using
   * the i18n keys enumerationName.value) or not.
   * 
   * @return true if the enumeration values should be presented translated.
   */
  boolean isTranslated();

}
