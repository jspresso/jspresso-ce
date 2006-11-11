/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import org.springframework.ldap.LdapTemplate;

/**
 * Root class of ldap actions.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractLdapAction extends AbstractBackendAction implements ILdapAction {

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
