/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.security;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Collection;
import java.util.Locale;

import javax.security.auth.Subject;

import org.jboss.security.SimplePrincipal;
import org.jspresso.framework.util.descriptor.IDescriptor;
import org.jspresso.framework.util.i18n.ITranslationProvider;


/**
 * Helper class for security management.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class SecurityHelper {

  /**
   * <code>ANONYMOUS_USER_NAME</code>.
   */
  public static final String ANONYMOUS_USER_NAME = "anonymous";

  /**
   * <code>ROLES_GROUP_NAME</code>.
   */
  public static final String ROLES_GROUP_NAME    = "Roles";

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
      if (p instanceof Group && ROLES_GROUP_NAME.equalsIgnoreCase(p.getName())) {
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

  /**
   * Creates an anonymous subject.
   * 
   * @return a new anonymous subject.
   */
  public static Subject createAnonymousSubject() {
    Subject anonymousSubject = new Subject();
    UserPrincipal userPrincipal = new UserPrincipal(ANONYMOUS_USER_NAME);
    if (!anonymousSubject.getPrincipals().contains(userPrincipal)) {
      anonymousSubject.getPrincipals().add(userPrincipal);
    }
    return anonymousSubject;
  }
}
