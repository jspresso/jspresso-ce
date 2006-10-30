/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import org.springframework.ldap.LdapTemplate;

/**
 * Configuration contract for an ldap action.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
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
