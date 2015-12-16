/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action;

import org.springframework.ldap.core.LdapTemplate;

/**
 * Configuration contract for an ldap action.
 * 
 * @author Vincent Vandenschrick
 */
public interface ILdapAction {

  /**
   * Sets the ldapTemplate.
   * 
   * @param ldapTemplate
   *          the ldapTemplate to set.
   */
  void setLdapTemplate(LdapTemplate ldapTemplate);

}
