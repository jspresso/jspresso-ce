/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.backend.session;

import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.security.auth.Subject;

import org.jspresso.framework.security.UserPrincipal;

/**
 * This interface establishes the contract of an application session. This
 * application session represents the backend application state.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IApplicationSession {

  /**
   * Gets a custom value from the session.
   * 
   * @param key
   *          the key used to lookup the custom value.
   * @return the value previously stored into the session or null if none.
   */
  Object getCustomValue(String key);

  /**
   * Lists the custom values from the application session.
   * 
   * @return the session custom values map.
   */
  Map<String, Object> getCustomValues();

  /**
   * Gets the session locale.
   * 
   * @return the session locale.
   */
  Locale getLocale();

  /**
   * Gets the session timezone.
   * 
   * @return the session timezone.
   */
  TimeZone getTimezone();

  /**
   * Gets the session principal as a JAAS principal.
   * 
   * @return the session owner.
   */
  UserPrincipal getPrincipal();

  /**
   * Gets the session owner as a JAAS subject.
   * 
   * @return the session owner.
   */
  Subject getSubject();

  /**
   * Puts a custom value into the session.
   * 
   * @param key
   *          the key under which to store the custom property.
   * @param value
   *          the value to store into the session.
   */
  void putCustomValue(String key, Object value);

  /**
   * Sets the session locale.
   * 
   * @param locale
   *          the session locale.
   */
  void setLocale(Locale locale);

  /**
   * Sets the session timezone.
   * 
   * @param timezone
   *          the session timezone.
   */
  void setTimezone(TimeZone timezone);

  /**
   * Sets the session owner as a JAAS subject.
   * 
   * @param sessionOwner
   *          the session owner.
   */
  void setSubject(Subject sessionOwner);
}
