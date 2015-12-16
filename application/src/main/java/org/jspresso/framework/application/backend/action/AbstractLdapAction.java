/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action;

import org.springframework.ldap.core.LdapTemplate;

/**
 * Root abstract class of actions that deal with LDAP directory. It's only
 * purpose is to standardize the use of Spring {@code LdapTemplate}.
 *
 * @author Vincent Vandenschrick
 */
public abstract class AbstractLdapAction extends BackendAction implements
    ILdapAction {

  private LdapTemplate ldapTemplate;

  /**
   * Configures the Spring LDAP template to use with this action.
   * 
   * @param ldapTemplate
   *          the ldapTemplate to set.
   */
  @Override
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
