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
package org.jspresso.framework.application.backend.session.basic;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.security.auth.Subject;

import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.security.UserPrincipal;

/**
 * Basic implementation of an application session.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicApplicationSession implements IApplicationSession {

  private Map<String, Object> customValues;
  private Locale              locale;
  private Subject             subject;

  /**
   * Constructs a new <code>BasicApplicationSession</code> instance.
   */
  public BasicApplicationSession() {
    customValues = new HashMap<String, Object>();
  }

  /**
   * {@inheritDoc}
   */
  public Object getCustomValue(String key) {
    return customValues.get(key);
  }

  /**
   * Gets the locale.
   * 
   * @return the locale.
   */
  public Locale getLocale() {
    return locale;
  }

  /**
   * {@inheritDoc}
   */
  public UserPrincipal getPrincipal() {
    if (subject != null && !subject.getPrincipals().isEmpty()) {
      return (UserPrincipal) subject.getPrincipals().iterator().next();
    }
    return null;
  }

  /**
   * Gets the owner.
   * 
   * @return the owner.
   */
  public Subject getSubject() {
    return subject;
  }

  /**
   * {@inheritDoc}
   */
  public void putCustomValue(String key, Object value) {
    customValues.put(key, value);
  }

  /**
   * Sets the locale.
   * 
   * @param locale
   *          the locale to set.
   */
  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  /**
   * Sets the owner.
   * 
   * @param subject
   *          the owner to set.
   */
  public void setSubject(Subject subject) {
    this.subject = subject;
  }

  /**
   * Gets the customValues.
   * 
   * @return the customValues.
   */
  public Map<String, Object> getCustomValues() {
    // return a defensive copy
    return new HashMap<String, Object>(customValues);
  }
}
