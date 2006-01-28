/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import java.util.Map;

import com.d2s.framework.view.descriptor.IConstrainedGridViewDescriptor;
import com.d2s.framework.view.descriptor.IViewDescriptor;
import com.d2s.framework.view.descriptor.ViewConstraints;

/**
 * Default implementation of a constrained grid view descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicConstrainedGridViewDescriptor extends BasicGridViewDescriptor
    implements IConstrainedGridViewDescriptor {

  private Map<IViewDescriptor, ViewConstraints> viewConstraints;

  /**
   * {@inheritDoc}
   */
  public ViewConstraints getViewConstraints(IViewDescriptor viewDescriptor) {
    if (viewConstraints != null) {
      return viewConstraints.get(viewDescriptor);
    }
    return null;
  }

  /**
   * Sets the viewConstraints.
   * 
   * @param viewConstraints
   *          the viewConstraints to set.
   */
  public void setViewConstraints(
      Map<IViewDescriptor, ViewConstraints> viewConstraints) {
    this.viewConstraints = viewConstraints;
  }
}
