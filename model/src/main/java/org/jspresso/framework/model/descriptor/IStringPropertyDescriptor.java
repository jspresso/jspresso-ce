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
package org.jspresso.framework.model.descriptor;

/**
 * This interface is implemented by descriptors of string properties.
 *
 * @author Vincent Vandenschrick
 */
public interface IStringPropertyDescriptor extends IScalarPropertyDescriptor {

  /**
   * Gets the maximum length of the underlying string property.
   *
   * @return the string property maximum length.
   */
  Integer getMaxLength();

  /**
   * Gets the regular expression pattern this string property must conform to.
   *
   * @return the regular expression pattern this string property must conform
   *         to.
   */
  String getRegexpPattern();

  /**
   * Gets the regular expression pattern sample this string property must
   * conform to. This property might be used to inform the end-user of a
   * erroneous value.
   *
   * @return the regular expression pattern sample this string property must
   *         conform to.
   */
  String getRegexpPatternSample();

  /**
   * Gets whether the underlying string property should be made uppercase
   * automatically.
   *
   * @return true if the underlying string property should be made uppercase
   *         automatically.
   */
  boolean isUpperCase();

  /**
   * Gets whether the underlying string property should be translated when displayed in
   * languages with non-european character sets.
   *
   * @return true if the underlying string property should be translated when displayed in
   * languages with non-european character sets.
   */
  boolean isTranslatable();
}
