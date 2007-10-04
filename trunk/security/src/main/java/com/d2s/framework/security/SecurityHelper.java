/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.security;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Collection;
import java.util.Locale;

import javax.security.auth.Subject;

import org.jboss.security.SimplePrincipal;

import com.d2s.framework.util.descriptor.IDescriptor;
import com.d2s.framework.util.i18n.ITranslationProvider;

/**
 * Helper class for security management.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class SecurityHelper {

  private SecurityHelper() {
    // private constructor for helper class
  }

  /**
   * Check access to a secured resource. Whenever acces might not be granted, a
   * Security exception is thrown.
   * 
   * @param subject
   *            the subject to check access for.
   * @param securable
   *            the secured resource to check access to.
   * @param translationProvider
   *            the translation provider to translate the potential error
   *            message.
   * @param locale
   *            the locale to translate the potential error message.
   */
  public static void checkAccess(Subject subject, ISecurable securable,
      ITranslationProvider translationProvider, Locale locale) {
    Collection<String> grantedRoles = securable.getGrantedRoles();
    if (grantedRoles == null) {
      return;
    }
    Group subjectRoles = null;
    for (Principal p : subject.getPrincipals()) {
      if (p instanceof Group && "Roles".equalsIgnoreCase(p.getName())) {
        subjectRoles = (Group) p;
      }
    }
    if (subjectRoles != null) {
      for (String grantedRole : grantedRoles) {
        if (subjectRoles.isMember(new SimplePrincipal(grantedRole))) {
          return;
        }
      }
    }
    if (securable instanceof IDescriptor) {
      throw new SecurityException(translationProvider.getTranslation(
          "access.denied.object", new Object[] {((IDescriptor) securable)
              .getI18nName(translationProvider, locale)}, locale));
    }
    throw new SecurityException(translationProvider.getTranslation(
        "access.denied", locale));
  }
}
