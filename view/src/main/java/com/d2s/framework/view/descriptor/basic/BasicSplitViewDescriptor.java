/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import com.d2s.framework.view.descriptor.ISplitViewDescriptor;
import com.d2s.framework.view.descriptor.IViewDescriptor;

/**
 * Default implementation of a split view descriptor.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicSplitViewDescriptor extends BasicCompositeViewDescriptor
    implements ISplitViewDescriptor {

  private IViewDescriptor leftTopViewDescriptor;
  private int             orientation = VERTICAL;
  private IViewDescriptor rightBottomViewDescriptor;

  /**
   * {@inheritDoc}
   */
  public IViewDescriptor getLeftTopViewDescriptor() {
    return leftTopViewDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public int getOrientation() {
    return orientation;
  }

  /**
   * {@inheritDoc}
   */
  public IViewDescriptor getRightBottomViewDescriptor() {
    return rightBottomViewDescriptor;
  }

  /**
   * Sets the leftTopViewDescriptor.
   * 
   * @param leftTopViewDescriptor
   *            the leftTopViewDescriptor to set.
   */
  public void setLeftTopViewDescriptor(IViewDescriptor leftTopViewDescriptor) {
    this.leftTopViewDescriptor = leftTopViewDescriptor;
  }

  /**
   * Sets the orientation.
   * 
   * @param orientation
   *            the orientation to set.
   */
  public void setOrientation(int orientation) {
    this.orientation = orientation;
  }

  /**
   * Sets the rightBottomViewDescriptor.
   * 
   * @param rightBottomViewDescriptor
   *            the rightBottomViewDescriptor to set.
   */
  public void setRightBottomViewDescriptor(
      IViewDescriptor rightBottomViewDescriptor) {
    this.rightBottomViewDescriptor = rightBottomViewDescriptor;
  }
}
