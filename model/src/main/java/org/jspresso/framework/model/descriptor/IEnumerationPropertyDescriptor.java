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

import java.util.List;
import java.util.Locale;

import org.jspresso.framework.util.descriptor.IDescriptor;
import org.jspresso.framework.util.i18n.ITranslationProvider;

/**
 * This interface is implemented by descriptors of enumeration properties. This
 * type of properties is constrained by a fixed value enumeration like
 * MALE/FEMALE or MONDAY/TUESDAY/WEDNESDAY/...
 *
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
   * Gets the admissible values of the enumeration.
   *
   * @return the list of values contained in the underlying enumeration.
   */
  List<String> getEnumerationValues();

  /**
   * Gets the icon image url to use to render a enumeration value.
   *
   * @param value
   *          the value to render.
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
   * Gets whether the enumeration values should be presented translated (using
   * the i18n keys enumerationName.value) or not.
   *
   * @return true if the enumeration values should be presented translated.
   */
  boolean isTranslated();

  /**
   * Gets the internationalized value of an enumeration element.
   *
   * @param value
   *          the enum value to translate.
   * @param translationProvider
   *          the translation provider which can be used by the descriptor to
   *          compute its internationalized name.
   * @param locale
   *          the locale in which the descriptor must compute its
   *          internationalized name.
   * @return The internationalized value.
   */
  String getI18nValue(String value, ITranslationProvider translationProvider,
      Locale locale);

  /**
   * Should this enumeration be presented as an LOV.
   *
   * @return the boolean
   */
  boolean isLov();

  /**
   * Create LOV reference descriptor.
   *
   * @return the reference property descriptor
   */
  IReferencePropertyDescriptor<IDescriptor> createLovReferenceDescriptor();
}
