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
package org.jspresso.framework.security;

import java.security.Principal;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;

import javax.security.auth.Subject;

import org.jboss.security.SimplePrincipal;

/**
 * Helper class for security management.
 *
 * @author Vincent Vandenschrick
 */
public final class SecurityHelper {

  /**
   * {@code ANONYMOUS_USER_NAME}.
   */
  public static final String ANONYMOUS_USER_NAME = "anonymous";

  /**
   * {@code ROLES_GROUP_NAME}.
   */
  public static final String ROLES_GROUP_NAME    = "Roles";

  private SecurityHelper() {
    // private constructor for helper class
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

  /**
   * Tests whether the passed subject has sufficient roles.
   *
   * @param subject
   *          the subject to test.
   * @param securable
   *          the securable to test.
   * @return true if the subject has sufficient privilege.
   */
  public static boolean isSubjectGranted(Subject subject, ISecurable securable) {
    if (securable == null) {
      return true;
    }
    Collection<String> grantedRoles = securable.getGrantedRoles();
    if (grantedRoles == null) {
      return true;
    }
    if (subject == null) {
      return false;
    }

    grantedRoles = new HashSet<>(grantedRoles);
    Collection<String> ungrantedRoles = new HashSet<>();
    for (String role : new HashSet<>(grantedRoles)) {
      if (role.startsWith("!")) {
        grantedRoles.remove(role);
        ungrantedRoles.add(role.substring(1));
      }
    }

    Group subjectRoles = getRolesGroup(subject);
    if (subjectRoles != null) {
      boolean granted = false;
      if (!grantedRoles.isEmpty()) {
        for (String grantedRole : grantedRoles) {
          if (subjectRoles.isMember(new SimplePrincipal(grantedRole))) {
            granted = true;
            break;
          }
        }
      }
      if (!granted && !ungrantedRoles.isEmpty()) {
        granted = true;
        for (String ungrantedRole : ungrantedRoles) {
          if (subjectRoles.isMember(new SimplePrincipal(ungrantedRole))) {
            granted = false;
            break;
          }
        }
      }
      return granted;
    }
    return false;
  }

  private static Group getRolesGroup(Subject subject) {
    Group subjectRoles = null;
    if (subject != null) {
      for (Principal p : subject.getPrincipals()) {
        if (p instanceof Group
            && ROLES_GROUP_NAME.equalsIgnoreCase(p.getName())) {
          subjectRoles = (Group) p;
        }
      }
    }
    return subjectRoles;
  }

  /**
   * Extracts the role names contained in this JAAS subject.
   *
   * @param subject
   *          the subject to extract the roles for.
   * @return the roles list.
   */
  public static List<String> getRoles(Subject subject) {
    List<String> roles = new ArrayList<>();
    Group subjectRoles = getRolesGroup(subject);
    if (subjectRoles != null) {
      for (Enumeration<? extends Principal> rolesEnum = subjectRoles.members(); rolesEnum
          .hasMoreElements();) {
        roles.add(rolesEnum.nextElement().getName());
      }
    }
    return roles;
  }
}
