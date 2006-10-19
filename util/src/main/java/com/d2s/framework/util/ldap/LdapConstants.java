/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.ldap;

/**
 * Constants for Design2See LDAP attributes naming.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class LdapConstants {

  private LdapConstants() {
    // Empty constructor for utility class
  }

  /**
   * <code>TOP_OBJECTCLASS</code>.
   */
  public static final String TOP_OBJECTCLASS       = "top";

  /**
   * <code>PASSWORD_ATTIBUTE</code>.
   */
  public static final String PASSWORD_ATTIBUTE     = "userPassword";

  /**
   * <code>DESCRIPTION_ATTRIBUTE</code>.
   */
  public static final String DESCRIPTION_ATTRIBUTE = "description";
}
