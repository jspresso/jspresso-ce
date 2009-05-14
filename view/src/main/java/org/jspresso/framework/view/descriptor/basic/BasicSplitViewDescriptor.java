/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
 * Default implementation of a split view descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
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
  public List<IViewDescriptor> getChildViewDescriptors() {
    List<IViewDescriptor> children = new ArrayList<IViewDescriptor>();
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
  public IViewDescriptor getLeftTopViewDescriptor() {
    completeChildDescriptor(leftTopViewDescriptor);
    return leftTopViewDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public EOrientation getOrientation() {
    return orientation;
  }

  /**
   * {@inheritDoc}
   */
  public IViewDescriptor getRightBottomViewDescriptor() {
    completeChildDescriptor(rightBottomViewDescriptor);
    return rightBottomViewDescriptor;
  }

  /**
   * Sets the leftTopViewDescriptor.
   * 
   * @param leftTopViewDescriptor
   *          the leftTopViewDescriptor to set.
   */
  public void setLeftTopViewDescriptor(IViewDescriptor leftTopViewDescriptor) {
    this.leftTopViewDescriptor = leftTopViewDescriptor;
  }

  /**
   * Sets the orientation.
   * 
   * @param orientation
   *          the orientation to set.
   */
  public void setOrientation(EOrientation orientation) {
    this.orientation = orientation;
  }

  /**
   * Sets the rightBottomViewDescriptor.
   * 
   * @param rightBottomViewDescriptor
   *          the rightBottomViewDescriptor to set.
   */
  public void setRightBottomViewDescriptor(
      IViewDescriptor rightBottomViewDescriptor) {
    this.rightBottomViewDescriptor = rightBottomViewDescriptor;
  }
}
