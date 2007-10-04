/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor;

/**
 * A grid view descriptor which organises its components in constrained cells.
 * This kind of described container view might typically be implemented by a
 * swing JPanel with a GridBagLayout.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IConstrainedGridViewDescriptor extends IGridViewDescriptor {

  /**
   * Gets the constraint applied to a contained view descriptor.
   * 
   * @param viewDescriptor
   *            the contained view descriptor.
   * @return the constraints applied to the contained view or null if none.
   */
  ViewConstraints getViewConstraints(IViewDescriptor viewDescriptor);
}
