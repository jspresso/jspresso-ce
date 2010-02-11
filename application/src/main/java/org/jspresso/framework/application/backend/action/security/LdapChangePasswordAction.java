/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
 * Changes a user password in an ldap directory.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class LdapChangePasswordAction extends AbstractChangePasswordAction {

  private String ldapUrl;

  /**
   * Sets the ldapUrl.
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
    contextSource.setPassword(new String(currentPassword));
    LdapTemplate ldapTemplate = new LdapTemplate(contextSource);
    try {
      contextSource.afterPropertiesSet();
      ldapTemplate.afterPropertiesSet();
    } catch (Exception ex) {
      throw new ActionException(ex);
    }
    List<ModificationItem> mods = new ArrayList<ModificationItem>();
    try {
      mods.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
          new BasicAttribute(LdapConstants.PASSWORD_ATTIBUTE,
              digest(newPassword.toCharArray()))));
      ldapTemplate.modifyAttributes(userDn, mods
          .toArray(new ModificationItem[0]));
    } catch (NoSuchAlgorithmException ex) {
      throw new ActionException(ex);
    } catch (UnsupportedEncodingException ex) {
      throw new ActionException(ex);
    } catch (IOException ex) {
      throw new ActionException(ex);
    } catch (AuthenticationException ex) {
      throw new ActionBusinessException("Current password is not valid.",
          "password.current.invalid");
    }
    return true;
  }
}
