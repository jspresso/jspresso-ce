/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.security;

import java.io.Serializable;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * A principal to represent a application user. This principal is able to store
 * some extra properties.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UserPrincipal implements Principal, Serializable {

  private static final long   serialVersionUID = 360589456903648696L;

  private String              name;
  private Map<String, Object> customProperties;
  
  /**
   * <code>OWNER_PROPERTY</code>.
   */
  public static final String OWNER_PROPERTY = "owner";

  /**
   * <code>LANGUAGE_PROPERTY</code>.
   */
  public static final String LANGUAGE_PROPERTY = "language";

  /**
   * Constructs a new <code>UserPrincipal</code> instance.
   * 
   * @param name
   *          the distinguished name.
   */
  public UserPrincipal(String name) {
    this.name = name;
    customProperties = new HashMap<String, Object>();
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
    boolean equals = false;
    if (name == null) {
      equals = anotherDn == null;
    } else {
      equals = name.equals(anotherDn);
    }
    return equals;
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
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return name;
  }

  /**
   * {@inheritDoc}
   */
  public String getName() {
    return name;
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
    customProperties.put(propertyName, propertyValue);
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
   * Gets the customProperties.
   * 
   * @return the customProperties.
   */
  public Map<String, Object> getCustomProperties() {
    return customProperties;
  }
}
