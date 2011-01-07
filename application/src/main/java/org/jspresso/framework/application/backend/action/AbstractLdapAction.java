/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action;

import org.springframework.ldap.core.LdapTemplate;

/**
 * Root abstrat class of actions that deal with LDAP directory. It's only
 * purpose is to standardize the use of Spring's <code>LdapTemplate</code>.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractLdapAction extends BackendAction implements
    ILdapAction {

  private LdapTemplate ldapTemplate;

  /**
   * Configures the Spring's LDAP template to use with this action.
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
