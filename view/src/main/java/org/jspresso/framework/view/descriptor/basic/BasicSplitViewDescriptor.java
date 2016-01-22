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
import java.util.List;

import org.jspresso.framework.view.descriptor.EOrientation;
import org.jspresso.framework.view.descriptor.ISplitViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * This composite view arranges its children in a container split either
 * horizontally or vertically. An horizontal split disposes its 2 children
 * <i>left</i> and <i>right</i> whereas a vertical split disposes its 2 children
 * <i>top</i> and <i>bottom</i>. The dividing bar can typically be moved by the
 * user to distribute the available space.
 * <p>
 * Default cascading order for master-detail is :
 * <p>
 * left -> right or top -> bottom depending on the split orientation.
 *
 * @author Vincent Vandenschrick
 */
public class BasicSplitViewDescriptor extends BasicCompositeViewDescriptor
    implements ISplitViewDescriptor {

  private IViewDescriptor leftTopViewDescriptor;
  private EOrientation    orientation = EOrientation.VERTICAL;
  private IViewDescriptor rightBottomViewDescriptor;

  /**
   * {@inheritDoc}
   */
  @Override
  public List<IViewDescriptor> getChildViewDescriptors() {
    List<IViewDescriptor> children = new ArrayList<>();
    if (getLeftTopViewDescriptor() != null) {
      children.add(getLeftTopViewDescriptor());
    }
    if (getRightBottomViewDescriptor() != null) {
      children.add(getRightBottomViewDescriptor());
    }
    return children;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IViewDescriptor getLeftTopViewDescriptor() {
    completeChildDescriptor(leftTopViewDescriptor, null);
    return leftTopViewDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EOrientation getOrientation() {
    return orientation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IViewDescriptor getRightBottomViewDescriptor() {
    completeChildDescriptor(rightBottomViewDescriptor,
        getLeftTopViewDescriptor());
    return rightBottomViewDescriptor;
  }

  /**
   * Sets the <i>left</i> (horizontal split) of <i>top</i> (vertical split)
   * nested view.
   *
   * @param leftTopViewDescriptor
   *          the leftTopViewDescriptor to set.
   */
  public void setLeftTopViewDescriptor(IViewDescriptor leftTopViewDescriptor) {
    this.leftTopViewDescriptor = leftTopViewDescriptor;
  }

  /**
   * Configures the split orientation of the container. This is either a value
   * of the {@code EOrientation} enum or its equivalent string
   * representation :
   * <ul>
   * <li>{@code VERTICAL} for splitting the container vertically and
   * arranging the views top and bottom</li>
   * <li>{@code HORIZONTAL} for splitting the container horizontally and
   * arranging the views left and right</li>
   * </ul>
   * Default value is {@code EOrientation.VERTICAL}, i.e. the container is
   * split vertically.
   *
   * @param orientation
   *          the orientation to set.
   */
  public void setOrientation(EOrientation orientation) {
    this.orientation = orientation;
  }

  /**
   * Sets the <i>right</i> (horizontal split) of <i>bottom</i> (vertical split)
   * nested view.
   *
   * @param rightBottomViewDescriptor
   *          the rightBottomViewDescriptor to set.
   */
  public void setRightBottomViewDescriptor(
      IViewDescriptor rightBottomViewDescriptor) {
    this.rightBottomViewDescriptor = rightBottomViewDescriptor;
  }
}
