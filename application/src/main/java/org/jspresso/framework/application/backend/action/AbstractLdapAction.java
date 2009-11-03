/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action;

import org.springframework.ldap.core.LdapTemplate;

/**
 * Root class of ldap actions.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractLdapAction extends BackendAction
    implements ILdapAction {

  private LdapTemplate ldapTemplate;

  /**
   * {@inheritDoc}
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
