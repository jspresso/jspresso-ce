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
package org.jspresso.framework.view.descriptor.basic;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.view.descriptor.ICompositeTreeLevelDescriptor;
import org.jspresso.framework.view.descriptor.ITreeLevelDescriptor;

/**
 * This descriptor is used to describe a collection of sibling nodes that each
 * nest multiple subtrees. The children subtrees are each placed under an
 * intermediary grouping node. For instance, given a composite tree level
 * mapping a collection of <i>A</i>s and whose children are 2 tree levels
 * mapping respectively a collection <i>Y</i>s and <i>Z</i>s, the tree would
 * look like :
 *
 * <pre>
 * parent
 *   <b>A</b>-1
 *     <b>collY</b>
 *       <b>Y</b>-1.1
 *       <b>Y</b>-1.2
 *     <b>collZ</b>
 *       <b>Z</b>-1.1
 *       <b>Z</b>-1.2
 *       <b>Z</b>-1.3
 *   <b>A</b>-2
 *     <b>collY</b>
 *       <b>Y</b>-2.1
 *       <b>Y</b>-2.2
 *       <b>Y</b>-2.3
 *     <b>collZ</b>
 *       <b>Z</b>-2.1
 *   <b>A</b>-3
 *     <b>collY</b>
 *       <b>Y</b>-3.1
 *       <b>Y</b>-3.2
 *     <b>collZ</b>
 *       <b>Z</b>-3.1
 *       <b>Z</b>-3.2
 * </pre>
 *
 * You can notice the intermediary grouping nodes that are installed to visually
 * separate the 2 collection families (<i>Y</i> and <i>Z</i>).
 *
 * @author Vincent Vandenschrick
 */
public class BasicCompositeTreeLevelDescriptor extends BasicTreeLevelDescriptor
    implements ICompositeTreeLevelDescriptor {

  private Map<String, ITreeLevelDescriptor> childrenDescriptorsMap;

  /**
   * {@inheritDoc}
   */
  @Override
  public ITreeLevelDescriptor getChildDescriptor(String name) {
    if (childrenDescriptorsMap != null) {
      return childrenDescriptorsMap.get(name);
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ITreeLevelDescriptor> getChildrenDescriptors() {
    if (childrenDescriptorsMap != null) {
      return new ArrayList<>(
          childrenDescriptorsMap.values());
    }
    return null;
  }

  /**
   * Assigns the list children tree level descriptors. A {@code null} value
   * (default) makes this tree level a leaf tree level and is strictly
   * equivalent to declaring a {@code BasicTreeLevelDescriptor} instead.
   *
   * @param childrenDescriptors
   *          the childrenDescriptors to set.
   */
  public void setChildrenDescriptors(
      List<ITreeLevelDescriptor> childrenDescriptors) {
    this.childrenDescriptorsMap = new LinkedHashMap<>();
    for (ITreeLevelDescriptor descriptor : childrenDescriptors) {
      String nodeGroupDescriptorName = descriptor.getNodeGroupDescriptor()
          .getModelDescriptor().getName();
      this.childrenDescriptorsMap.put(nodeGroupDescriptorName, descriptor);
    }
  }
}
