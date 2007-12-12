/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend;

import org.springframework.ldap.LdapTemplate;

/**
 * A dao used to access ldap to retreive objects.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractLdapDao {

  private LdapTemplate ldapTemplate;

  /**
   * Sets the ldapTemplate.
   * 
   * @param ldapTemplate
   *            the ldapTemplate to set.
   */
  public void setLdapTemplate(LdapTemplate ldapTemplate) {
    this.ldapTemplate = ldapTemplate;
  }

  /**
   * Gets the ldapTemplate.
   * 
   * @return the ldapTemplate.
   */
  protected LdapTemplate getLdapTemplate() {
    return ldapTemplate;
  }

}
