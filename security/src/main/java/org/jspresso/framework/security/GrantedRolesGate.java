/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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

import java.util.Collection;

import javax.security.auth.Subject;

import org.jspresso.framework.util.gate.AbstractGate;

/**
 * A role based gate.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class GrantedRolesGate extends AbstractGate implements ISubjectAware {

  private boolean            open;
  private Collection<String> grantedRoles;

  /**
   * {@inheritDoc}
   */
  public boolean isOpen() {
    return open;
  }

  /**
   * {@inheritDoc}
   */
  public void setSubject(Subject subject) {
    boolean oldOpen = isOpen();
    this.open = SecurityHelper.isSubjectGranted(subject, new ISecurable() {

      public Collection<String> getGrantedRoles() {
        return GrantedRolesGate.this.getGrantedRoles();
      }
    });
    firePropertyChange(OPEN_PROPERTY, oldOpen, isOpen());
  }

  /**
   * Gets the grantedRoles.
   * 
   * @return the grantedRoles.
   */
  protected Collection<String> getGrantedRoles() {
    return grantedRoles;
  }

  /**
   * Sets the grantedRoles.
   * 
   * @param grantedRoles
   *          the grantedRoles to set.
   */
  public void setGrantedRoles(Collection<String> grantedRoles) {
    this.grantedRoles = grantedRoles;
  }

}
