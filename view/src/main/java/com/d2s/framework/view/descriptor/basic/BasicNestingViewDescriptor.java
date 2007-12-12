/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import com.d2s.framework.view.descriptor.INestingViewDescriptor;
import com.d2s.framework.view.descriptor.IViewDescriptor;

/**
 * Default implementation of a nesting view descriptor.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicNestingViewDescriptor extends BasicViewDescriptor implements
    INestingViewDescriptor {

  private IViewDescriptor nestedViewDescriptor;

  /**
   * {@inheritDoc}
   */
  public IViewDescriptor getNestedViewDescriptor() {
    return nestedViewDescriptor;
  }

  /**
   * Sets the nestedViewDescriptor.
   * 
   * @param nestedViewDescriptor
   *            the nestedViewDescriptor to set.
   */
  public void setNestedViewDescriptor(IViewDescriptor nestedViewDescriptor) {
    this.nestedViewDescriptor = nestedViewDescriptor;
  }

}
