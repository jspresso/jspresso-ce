/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor.module.basic;

import com.d2s.framework.view.descriptor.IViewDescriptor;
import com.d2s.framework.view.descriptor.basic.BasicCompositeTreeLevelDescriptor;
import com.d2s.framework.view.descriptor.module.ICompositeSubModuleDescriptor;

/**
 * This is the default implementation of a mutable module view descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicCompositeSubModuleDescriptor extends
    BasicCompositeTreeLevelDescriptor implements ICompositeSubModuleDescriptor {

  private IViewDescriptor viewDescriptor;

  /**
   * Gets the viewDescriptor.
   * 
   * @return the viewDescriptor.
   */
  public IViewDescriptor getViewDescriptor() {
    return viewDescriptor;
  }

  /**
   * Sets the viewDescriptor.
   * 
   * @param viewDescriptor
   *          the viewDescriptor to set.
   */
  public void setViewDescriptor(IViewDescriptor viewDescriptor) {
    this.viewDescriptor = viewDescriptor;
  }
}
