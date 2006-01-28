/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import com.d2s.framework.view.descriptor.ISplitViewDescriptor;
import com.d2s.framework.view.descriptor.IViewDescriptor;

/**
 * Default implementation of a split view descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicSplitViewDescriptor extends BasicCompositeViewDescriptor
    implements ISplitViewDescriptor {

  private IViewDescriptor leftTopViewDescriptor;
  private IViewDescriptor rightBottomViewDescriptor;
  private int             orientation = VERTICAL;

  /**
   * {@inheritDoc}
   */
  public IViewDescriptor getLeftTopViewDescriptor() {
    return leftTopViewDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public IViewDescriptor getRightBottomViewDescriptor() {
    return rightBottomViewDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public int getOrientation() {
    return orientation;
  }

  /**
   * Sets the leftTopViewDescriptor.
   * 
   * @param leftTopViewDescriptor
   *          the leftTopViewDescriptor to set.
   */
  public void setLeftTopViewDescriptor(IViewDescriptor leftTopViewDescriptor) {
    this.leftTopViewDescriptor = leftTopViewDescriptor;
  }

  /**
   * Sets the orientation.
   * 
   * @param orientation
   *          the orientation to set.
   */
  public void setOrientation(int orientation) {
    this.orientation = orientation;
  }

  /**
   * Sets the rightBottomViewDescriptor.
   * 
   * @param rightBottomViewDescriptor
   *          the rightBottomViewDescriptor to set.
   */
  public void setRightBottomViewDescriptor(
      IViewDescriptor rightBottomViewDescriptor) {
    this.rightBottomViewDescriptor = rightBottomViewDescriptor;
  }
}
