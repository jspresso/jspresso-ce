/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import java.util.ArrayList;
import java.util.List;
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
public class BasicConstrainedGridViewDescriptor extends
    BasicCompositeViewDescriptor implements IConstrainedGridViewDescriptor {

  private Map<IViewDescriptor, ViewConstraints> constrainedViews;

  /**
   * {@inheritDoc}
   */
  public ViewConstraints getViewConstraints(IViewDescriptor viewDescriptor) {
    if (constrainedViews != null) {
      return constrainedViews.get(viewDescriptor);
    }
    return null;
  }

  /**
   * Sets the constrainedViews.
   * 
   * @param constrainedViews
   *          the constrainedViews to set.
   */
  public void setConstrainedViews(
      Map<IViewDescriptor, ViewConstraints> constrainedViews) {
    this.constrainedViews = constrainedViews;
  }

  /**
   * {@inheritDoc}
   */
  public List<IViewDescriptor> getChildViewDescriptors() {
    return new ArrayList<IViewDescriptor>(constrainedViews.keySet());
  }
}
