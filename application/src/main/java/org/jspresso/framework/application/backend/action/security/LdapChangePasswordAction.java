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
package org.jspresso.framework.application.backend.action.security;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

import org.jspresso.framework.action.ActionBusinessException;
import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.framework.util.ldap.LdapConstants;
import org.springframework.ldap.AuthenticationException;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.DirContextSource;

/**
 * Concrete backend implementation of a change password action where password is
 * stored in an LDAP directory. The user DN to use to connect to the LDAP
 * directory is the one stored in the user principal from the login process.
 *
 * @author Vincent Vandenschrick
 */
public class LdapChangePasswordAction extends AbstractChangePasswordAction {

  private String ldapUrl;

  /**
   * Configures the LDAP url (e.g. <i>http://localhost:389</i>) of the LDAP
   * directory. The user must be authorized to change its own password in the
   * LDAP backend.
   *
   * @param ldapUrl
   *          the ldapUrl to set.
   */
  public void setLdapUrl(String ldapUrl) {
    this.ldapUrl = ldapUrl;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean changePassword(UserPrincipal userPrincipal,
      String currentPassword, String newPassword) {

    String userDn = (String) userPrincipal
        .getCustomProperty(UserPrincipal.USERDN_PROPERTY);

    DirContextSource contextSource = new DirContextSource();
    contextSource.setUrl(ldapUrl);
    contextSource.setUserDn(userDn);
    contextSource.setPassword(currentPassword);
    LdapTemplate ldapTemplate = new LdapTemplate(contextSource);
    try {
      contextSource.afterPropertiesSet();
      ldapTemplate.afterPropertiesSet();
    } catch (Exception ex) {
      throw new ActionException(ex);
    }
    List<ModificationItem> mods = new ArrayList<>();
    try {
      mods.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
          new BasicAttribute(LdapConstants.PASSWORD_ATTRIBUTE,
              digestAndEncode(newPassword.toCharArray()))));
      ldapTemplate.modifyAttributes(userDn,
          mods.toArray(new ModificationItem[mods.size()]));
    } catch (NoSuchAlgorithmException | IOException ex) {
      throw new ActionException(ex);
    } catch (AuthenticationException ex) {
      throw new ActionBusinessException("Current password is not valid.",
          "password.current.invalid");
    }
    return true;
  }

  /**
   * Returns a prefix to use before storing a password. An example usage is to
   * prefix the password hash with the type of hash, e.g. {MD5}.
   *
   * @return a prefix to use before storing a password.
   */
  @Override
  protected String getPasswordStorePrefix() {
    if (getDigestAlgorithm() != null) {
      return "{" + getDigestAlgorithm() + "}";
    }
    return super.getPasswordStorePrefix();
  }
}
