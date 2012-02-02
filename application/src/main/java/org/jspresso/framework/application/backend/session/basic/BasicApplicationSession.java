/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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

import java.security.Principal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

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
  private TimeZone            timeZone;
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
  @Override
  public Object getCustomValue(String key) {
    return customValues.get(key);
  }

  /**
   * Gets the locale.
   * 
   * @return the locale.
   */
  @Override
  public Locale getLocale() {
    return locale;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserPrincipal getPrincipal() {
    if (subject != null) {
      for (Principal principal : subject.getPrincipals()) {
        if (principal instanceof UserPrincipal) {
          return (UserPrincipal) principal;
        }
      }
    }
    return null;
  }

  /**
   * Gets the owner.
   * 
   * @return the owner.
   */
  @Override
  public Subject getSubject() {
    return subject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void putCustomValue(String key, Object value) {
    customValues.put(key, value);
  }

  /**
   * Sets the locale.
   * 
   * @param locale
   *          the locale to set.
   */
  @Override
  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  /**
   * Sets the owner.
   * 
   * @param subject
   *          the owner to set.
   */
  @Override
  public void setSubject(Subject subject) {
    this.subject = subject;
  }

  /**
   * Gets the customValues.
   * 
   * @return the customValues.
   */
  @Override
  public Map<String, Object> getCustomValues() {
    // return a defensive copy
    return new HashMap<String, Object>(customValues);
  }

  /**
   * Gets the timeZone.
   * 
   * @return the timeZone.
   */
  public TimeZone getTimeZone() {
    return timeZone;
  }

  /**
   * Sets the timeZone.
   * 
   * @param timeZone
   *          the timeZone to set.
   */
  public void setTimeZone(TimeZone timeZone) {
    this.timeZone = timeZone;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getId() {
    return Integer.toHexString(hashCode());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clear() {
    if (customValues != null) {
      customValues.clear();
    }
    locale = null;
    timeZone = null;
    subject = null;
  }
}
