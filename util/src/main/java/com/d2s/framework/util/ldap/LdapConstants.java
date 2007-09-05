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

  /**
   * <code>DESCRIPTION_ATTRIBUTE</code>.
   */
  public static final String DESCRIPTION_ATTRIBUTE   = "description";

  /**
   * <code>DISPLAY_NAME_ATTRIBUTE</code>.
   */
  public static final String DISPLAY_NAME_ATTRIBUTE  = "displayName";

  /**
   * <code>MAIL_ATTRIBUTE</code>.
   */
  public static final String MAIL_ATTRIBUTE          = "mail";

  /**
   * <code>PASSWORD_ATTIBUTE</code>.
   */
  public static final String PASSWORD_ATTIBUTE       = "userPassword";

  /**
   * <code>SERIAL_NUMBER_ATTRIBUTE</code>.
   */
  public static final String SERIAL_NUMBER_ATTRIBUTE = "serialNumber";

  /**
   * <code>TOP_OBJECTCLASS</code>.
   */
  public static final String TOP_OBJECTCLASS         = "top";

  private LdapConstants() {
    // Empty constructor for utility class
  }
}
