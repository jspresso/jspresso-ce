/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor.projection.basic;

import com.d2s.framework.view.descriptor.IViewDescriptor;
import com.d2s.framework.view.descriptor.basic.BasicCompositeTreeLevelDescriptor;
import com.d2s.framework.view.descriptor.projection.ICompositeChildProjectionViewDescriptor;

/**
 * This is the default implementation of a mutable projection view descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicCompositeChildProjectionViewDescriptor extends
    BasicCompositeTreeLevelDescriptor implements
    ICompositeChildProjectionViewDescriptor {

  private IViewDescriptor projectedViewDescriptor;

  /**
   * Gets the projectedViewDescriptor.
   * 
   * @return the projectedViewDescriptor.
   */
  public IViewDescriptor getProjectedViewDescriptor() {
    return projectedViewDescriptor;
  }

  /**
   * Sets the projectedViewDescriptor.
   * 
   * @param projectedViewDescriptor
   *          the projectedViewDescriptor to set.
   */
  public void setProjectedViewDescriptor(IViewDescriptor projectedViewDescriptor) {
    this.projectedViewDescriptor = projectedViewDescriptor;
  }
}
