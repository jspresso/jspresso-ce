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
package org.jspresso.framework.security;

import java.io.Serializable;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.jspresso.framework.util.bean.AbstractPropertyChangeCapable;

/**
 * A principal to represent a application user. This principal is able to store
 * some extra properties.
 *
 * @author Vincent Vandenschrick
 */
public class UserPrincipal extends AbstractPropertyChangeCapable implements
    Principal, Serializable {

  /**
   * {@code LANGUAGE_PROPERTY}.
   */
  public static final String  LANGUAGE_PROPERTY = "language";

  /**
   * {@code OWNER_PROPERTY}.
   */
  public static final String  OWNER_PROPERTY    = "owner";
  /**
   * {@code USER_PROPERTY}.
   */
  public static final String  USERDN_PROPERTY   = "userDn";

  private static final long   serialVersionUID  = 360589456903648696L;

  private final Map<String, Object> customProperties;

  private final String              name;

  /**
   * Constructs a new {@code UserPrincipal} instance.
   *
   * @param name
   *          the distinguished name.
   */
  public UserPrincipal(String name) {
    this.name = name;
    customProperties = new HashMap<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object another) {
    if (!(another instanceof UserPrincipal)) {
      return false;
    }
    String anotherDn = ((UserPrincipal) another).getName();
    boolean equals;
    if (name == null) {
      equals = anotherDn == null;
    } else {
      equals = name.equals(anotherDn);
    }
    return equals;
  }

  /**
   * Gets the customProperties.
   *
   * @return the customProperties.
   */
  public Map<String, Object> getCustomProperties() {
    // return a defensive copy
    return new HashMap<>(customProperties);
  }

  /**
   * Retrieves a custom property for this user.
   *
   * @param propertyName
   *          the name of the custom property.
   * @return the value of the custom property or null if none exists.
   */
  public Object getCustomProperty(String propertyName) {
    return customProperties.get(propertyName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    if (name == null) {
      return 0;
    }
    return name.hashCode();
  }

  /**
   * Registers a custom property for this user.
   *
   * @param propertyName
   *          the name of the custom property.
   * @param propertyValue
   *          the value of the custom property.
   */
  public void putCustomProperty(String propertyName, Object propertyValue) {
    Object oldValue = customProperties.put(propertyName, propertyValue);
    firePropertyChange(propertyName, oldValue, propertyValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return name;
  }
}
