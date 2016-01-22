/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.view.descriptor;

import java.util.List;

/**
 * This is a helper class for tree descriptor management.
 *
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
   *          the tree level descriptor to start from.
   * @param treePath
   *          a tree path of descriptor names.
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
        return getSubtreeDescriptorFromPath(nextSubtreeDescriptor,
            treePath.subList(1, treePath.size()));
      } else if (treeLevelDescriptor instanceof ISimpleTreeLevelDescriptor) {
        if (treePath.size() == 1) {
          return treeLevelDescriptor;
        }
        ITreeLevelDescriptor nextSubtreeDescriptor = ((ISimpleTreeLevelDescriptor) treeLevelDescriptor)
            .getChildDescriptor();
        return getSubtreeDescriptorFromPath(nextSubtreeDescriptor,
            treePath.subList(1, treePath.size()));
      }
    }
    return null;
  }
}
