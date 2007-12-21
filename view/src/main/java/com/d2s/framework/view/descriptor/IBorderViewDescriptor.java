/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view.descriptor;

/**
 * This public interface is implemented by container view descriptors which are
 * organizing their contained view descriptors in a north / south / east / west /
 * center manner. This kind of described view can typically be implemented using
 * a swing JPanel with a BorderLayout.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IBorderViewDescriptor extends ICompositeViewDescriptor {

  /**
   * Gets the contained view descriptor located at the center position.
   * 
   * @return the contained view descriptor or null.
   */
  IViewDescriptor getCenterViewDescriptor();

  /**
   * Gets the contained view descriptor located at the east position.
   * 
   * @return the contained view descriptor or null.
   */
  IViewDescriptor getEastViewDescriptor();

  /**
   * Gets the contained view descriptor located at the north position.
   * 
   * @return the contained view descriptor or null.
   */
  IViewDescriptor getNorthViewDescriptor();

  /**
   * Gets the contained view descriptor located at the south position.
   * 
   * @return the contained view descriptor or null.
   */
  IViewDescriptor getSouthViewDescriptor();

  /**
   * Gets the contained view descriptor located at the west position.
   * 
   * @return the contained view descriptor or null.
   */
  IViewDescriptor getWestViewDescriptor();
}
