/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
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
   *          the subject to check access for.
   * @param securable
   *          the secured resource to check access to.
   * @param translationProvider
   *          the translation provider to translate the potential error message.
   * @param locale
   *          the locale to translate the potential error message.
   */
  public static void checkAccess(Subject subject, ISecurable securable,
      ITranslationProvider translationProvider, Locale locale) {
    Collection<String> grantedRoles = securable.getGrantedRoles();
    if (isSubjectGranted(subject, grantedRoles)) {
      return;
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
   * Do the passed subject has sufficient roles ?
   * 
   * @param subject
   *          the subject to test.
   * @param grantedRoles
   *          the roles granted
   * @return true if the subject has sufficient priviledge.
   */
  private static boolean isSubjectGranted(Subject subject,
      Collection<String> grantedRoles) {
    if (grantedRoles == null) {
      return true;
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
          return true;
        }
      }
    }
    return false;
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
