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

import org.jspresso.framework.view.descriptor.ISimpleTreeLevelDescriptor;
import org.jspresso.framework.view.descriptor.ITreeLevelDescriptor;

/**
 * This descriptor is used to describe a collection of sibling nodes that only
 * nest a single subtree. The child subtree is directly placed under each node
 * without any intermediary grouping node. For instance, given a simple tree
 * level mapping a collection of <i>A</i>s and whose child is a tree level
 * mapping a collection of <i>B</i>s, the tree would look like :
 *
 * <pre>
 * parent
 *   <b>A</b>-1
 *     <b>B</b>-1.1
 *     <b>B</b>-1.2
 *     <b>B</b>-1.3
 *   <b>A</b>-2
 *     <b>B</b>-2.1
 *   <b>A</b>-3
 *     <b>B</b>-3.1
 *     <b>B</b>-3.2
 * </pre>
 *
 * @author Vincent Vandenschrick
 */
public class BasicSimpleTreeLevelDescriptor extends BasicTreeLevelDescriptor
    implements ISimpleTreeLevelDescriptor {

  private ITreeLevelDescriptor childDescriptor;

  /**
   * {@inheritDoc}
   */
  @Override
  public ITreeLevelDescriptor getChildDescriptor() {
    return childDescriptor;
  }

  /**
   * Assigns the single child tree level descriptor. A {@code null} value
   * (default) makes this tree level a leaf tree level and is strictly
   * equivalent to declaring a {@code BasicTreeLevelDescriptor} instead.
   *
   * @param childDescriptor
   *          the childDescriptor to set.
   */
  public void setChildDescriptor(ITreeLevelDescriptor childDescriptor) {
    this.childDescriptor = childDescriptor;
  }
}
