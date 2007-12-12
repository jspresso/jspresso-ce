/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor;

import java.util.List;

/**
 * This is a helper class for tree descriptor management.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class TreeDescriptorHelper {

  private TreeDescriptorHelper() {
    // protected constructor of utility class.
  }

  /**
   * From a tree path of descriptor names, it retrieves the list descriptor of
   * the node collection.
   * 
   * @param treeLevelDescriptor
   *            the tree level descriptor to start from.
   * @param treePath
   *            a tree path of descriptor names.
   * @return the node group list descriptor.
   */
  public static ITreeLevelDescriptor getSubtreeDescriptorFromPath(
      ITreeLevelDescriptor treeLevelDescriptor, List<String> treePath) {
    if (treePath != null && !treePath.isEmpty()) {
      if (treeLevelDescriptor instanceof ICompositeTreeLevelDescriptor) {
        ITreeLevelDescriptor nextSubtreeDescriptor = ((ICompositeTreeLevelDescriptor) treeLevelDescriptor)
            .getChildDescriptor(treePath.get(0));
        if (treePath.size() == 1) {
          return nextSubtreeDescriptor;
        }
        return getSubtreeDescriptorFromPath(nextSubtreeDescriptor, treePath
            .subList(1, treePath.size()));
      } else if (treeLevelDescriptor instanceof ISimpleTreeLevelDescriptor) {
        if (treePath.size() == 1) {
          return treeLevelDescriptor;
        }
        ITreeLevelDescriptor nextSubtreeDescriptor = ((ISimpleTreeLevelDescriptor) treeLevelDescriptor)
            .getChildDescriptor();
        return getSubtreeDescriptorFromPath(nextSubtreeDescriptor, treePath
            .subList(1, treePath.size()));
      }
    }
    return null;
  }
}
