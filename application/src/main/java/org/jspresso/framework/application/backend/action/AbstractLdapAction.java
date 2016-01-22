/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
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
