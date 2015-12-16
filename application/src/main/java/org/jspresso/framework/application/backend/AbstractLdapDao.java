/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend;

import org.springframework.ldap.core.LdapTemplate;

/**
 * A dao used to access ldap to retrieve objects.
 * 
 * @author Vincent Vandenschrick
 */
public abstract class AbstractLdapDao {

  private LdapTemplate ldapTemplate;

  /**
   * Sets the ldapTemplate.
   * 
   * @param ldapTemplate
   *          the ldapTemplate to set.
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
