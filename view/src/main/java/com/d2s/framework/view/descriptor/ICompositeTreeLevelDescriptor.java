/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor;

import java.util.List;

/**
 * This interface is implemented by descriptors of composite tree levels.
 * composite tree levels are tree levels where children are organised into sub
 * groups.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ICompositeTreeLevelDescriptor extends ITreeLevelDescriptor {

  /**
   * Gets the subtree descriptor from its name.
   * 
   * @param name
   *            the name of the child tree level descriptor.
   * @return the subtree descriptor.
   */
  ITreeLevelDescriptor getChildDescriptor(String name);

  /**
   * Gets the subtree descriptors.
   * 
   * @return the subtree descriptors.
   */
  List<ITreeLevelDescriptor> getChildrenDescriptors();
}
