/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor;

/**
 * This public interface is implemented by any tree view descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ITreeViewDescriptor extends IViewDescriptor {

  /**
   * Gets the root tree level descriptor of this tree view.
   * 
   * @return the root tree level descriptor of this tree view.
   */
  ITreeLevelDescriptor getRootSubtreeDescriptor();

  /**
   * It gets the maximum depth of the tree structure whichis mandatory in case
   * of a recursive one.
   * 
   * @return the maximum tree structure depth.
   */
  int getMaxDepth();

}
