/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.model.module;

import org.apache.commons.lang.ObjectUtils;

import com.d2s.framework.application.view.descriptor.module.ISubModuleDescriptor;

/**
 * A child module is a non-root module (it has a parent). A child module uses a
 * view to render its projected artifact.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SubModule extends Module {

  private Module               parent;
  private ISubModuleDescriptor descriptor;

  /**
   * Gets the module's parent module.
   * 
   * @return the parent module or null if none.
   */
  public Module getParent() {
    return parent;
  }

  /**
   * Sets the parent module. It will fire a "parent" property change event.
   * 
   * @param parent
   *          the parent module to set or null if none.
   */
  public void setParent(Module parent) {
    if (ObjectUtils.equals(this.parent, parent)) {
      return;
    }
    Module oldParent = getParent();
    if (getParent() != null) {
      getParent().removeSubModule(this);
    }
    this.parent = parent;
    if (getParent() != null && !getParent().getSubModules().contains(this)) {
      getParent().addSubModule(this);
    }
    firePropertyChange("parent", oldParent, getParent());
  }

  /**
   * Gets the descriptor.
   * 
   * @return the descriptor.
   */
  public ISubModuleDescriptor getDescriptor() {
    return descriptor;
  }

  /**
   * Sets the descriptor.
   * 
   * @param descriptor
   *          the descriptor to set.
   */
  public void setDescriptor(ISubModuleDescriptor descriptor) {
    this.descriptor = descriptor;
  }
}
