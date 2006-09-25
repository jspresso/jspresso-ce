/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.view.descriptor.basic;

import java.util.Collection;

import com.d2s.framework.action.IAction;
import com.d2s.framework.application.view.descriptor.IModuleDescriptor;
import com.d2s.framework.view.descriptor.basic.BasicTreeViewDescriptor;

/**
 * This is a default implementation of a simple module view descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicModuleDescriptor extends BasicTreeViewDescriptor implements
    IModuleDescriptor {

  private IAction            startupAction;
  private Collection<String> grantedRoles;

  /**
   * Constructs a new <code>BasicModuleDescriptor</code> instance.
   */
  public BasicModuleDescriptor() {
    BasicSubModuleDescriptor subModuleDescriptor = new BasicSubModuleDescriptor();
    subModuleDescriptor.setChildDescriptor(subModuleDescriptor);
    setRootSubtreeDescriptor(subModuleDescriptor);
  }

  /**
   * {@inheritDoc}
   */
  public IAction getStartupAction() {
    return startupAction;
  }

  /**
   * Sets the startupAction.
   * 
   * @param startupAction
   *          the startupAction to set.
   */
  public void setStartupAction(IAction startupAction) {
    this.startupAction = startupAction;
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
