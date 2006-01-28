/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor;

/**
 * This public interface is implemented by a composite view descriptor which
 * organizes 2 view descriptors in a splitted manner. The described view can
 * typically be implemented using a swing JSplitPane.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ISplitViewDescriptor extends ICompositeViewDescriptor {

  /**
   * <code>HORIZONTAL</code> location constant.
   */
  int HORIZONTAL = 1;

  /**
   * <code>VERTICAL</code> location constant.
   */
  int VERTICAL   = 2;

  /**
   * Gets the left / top sub view descriptor of this split composite view
   * descriptor.
   * 
   * @return the left / top sub view descriptor.
   */
  IViewDescriptor getLeftTopViewDescriptor();

  /**
   * Gets the right / bottom sub view descriptor of this split composite view
   * descriptor.
   * 
   * @return the right / bottom sub view descriptor.
   */
  IViewDescriptor getRightBottomViewDescriptor();

  /**
   * Gets the orientation of the split described view.
   * 
   * @return the split orientation. The admissible values are :
   *         <li>VERTICAL
   *         <li>HORIZONTAL
   */
  int getOrientation();
}
