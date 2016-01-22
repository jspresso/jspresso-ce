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
package org.jspresso.framework.util.descriptor;

import java.util.Locale;

import org.jspresso.framework.util.i18n.ITranslationProvider;

/**
 * This interface is implemented by anything which can be described.
 *
 * @author Vincent Vandenschrick
 */
public interface IDescriptor {

  /**
   * {@code DESCRIPTION}="description".
   */
  String DESCRIPTION  = "description";
  /**
   * {@code NAME}="name".
   */
  String NAME         = "name";
  /**
   * {@code LAST_UPDATED}="lastUpdated".
   */
  String LAST_UPDATED = "lastUpdated";

  /**
   * Gets the end-user understandable description.
   *
   * @return The user-friendly description
   */
  String getDescription();

  /**
   * Gets the internationalized end-user understandable description.
   *
   * @param translationProvider
   *          the translation provider which can be used by the descriptor to
   *          compute its internationalized description.
   * @param locale
   *          the locale in which the descriptor must compute its
   *          internationalized description.
   * @return The user-friendly description
   */
  String getI18nDescription(ITranslationProvider translationProvider,
      Locale locale);

  /**
   * Gets the internationalized name of this descriptor.
   *
   * @param translationProvider
   *          the translation provider which can be used by the descriptor to
   *          compute its internationalized name.
   * @param locale
   *          the locale in which the descriptor must compute its
   *          internationalized name.
   * @return The internationalized name of this described object
   */
  String getI18nName(ITranslationProvider translationProvider, Locale locale);

  /**
   * Gets the name of this descriptor. Depending on the implementation, this
   * name can be technically meaningful (e.g. a method name, a property name,
   * ...).
   *
   * @return The name of this described object
   */
  String getName();

  /**
   * Gets the timestamp of the last update of this descriptor or -1 if no
   * information is available.
   *
   * @return the timestamp of the last update of this descriptor or -1 if no
   *         information is available.
   */
  long getLastUpdated();
}
