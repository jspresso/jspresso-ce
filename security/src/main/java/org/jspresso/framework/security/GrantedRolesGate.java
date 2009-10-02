/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.security;

import java.util.Collection;

import javax.security.auth.Subject;

import org.jspresso.framework.util.gate.AbstractGate;

/**
 * A role based gate.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class GrantedRolesGate extends AbstractGate implements ISecurable,
    ISubjectAware {

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
    this.open = SecurityHelper.isSubjectGranted(subject, this);
    firePropertyChange(OPEN_PROPERTY, oldOpen, isOpen());
  }

  /**
   * Gets the grantedRoles.
   * 
   * @return the grantedRoles.
   */
  public Collection<String> getGrantedRoles() {
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
