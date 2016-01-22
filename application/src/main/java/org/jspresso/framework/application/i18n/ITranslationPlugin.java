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
package org.jspresso.framework.application.i18n;

import java.util.Locale;

import org.jspresso.framework.application.backend.session.IApplicationSession;

/**
 * This is the contract for the application i18n extension point. It allows to
 * implement custom i18n schemes that are much more dynamic than the default,
 * bundle-based, static one. For instance, one can implement different i18n
 * schemes based on the logged-in user (or its memberships).
 *
 * @author Vincent Vandenschrick
 */
@SuppressWarnings("UnusedParameters")
public interface ITranslationPlugin {

  /**
   * Gets a translated string based on a key.
   *
   * @param key
   *          the i18n key.
   * @param locale
   *          the locale the string must be translated into.
   * @param session
   *          the application session that can be used to customize the
   *          translation algorithm based, for instance, on the logged-in user.
   * @return the translated string.
   */
  String getTranslation(String key, Locale locale, IApplicationSession session);

  /**
   * Gets a translated message based on a key.
   *
   * @param key
   *          the i18n key.
   * @param args
   *          the message arguments used in message format.
   * @param locale
   *          the locale the string must be translated into.
   * @param session
   *          the application session that can be used to customize the
   *          translation algorithm based, for instance, on the logged-in user.
   * @return the translated string.
   */
  String getTranslation(String key, Object[] args, Locale locale,
      IApplicationSession session);
}
