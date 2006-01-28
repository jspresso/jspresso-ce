/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor;

/**
 * This interface is implemented by descriptors of a subtree.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ITreeLevelDescriptor {

  /**
   * Gets the list descriptor describing the collection of nodes composing the
   * tree level this subtree starts at.
   * 
   * @return the list view descriptor describing the node collection this
   *         subtree is headed by.
   */
  IListViewDescriptor getNodeGroupDescriptor();
}
