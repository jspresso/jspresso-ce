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

import org.jspresso.framework.view.descriptor.IBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * A border view is a composite view that arranges its children to the
 * <i>north</i>, <i>west</i>, <i>east</i>, <i>south</i> and <i>center</i>.
 * Depending its position in the container, the resizing rules apply differently
 * :
 * <ul>
 * <li><i>north</i> and <i>south</i> are resized horizontally and kept to their
 * preferred size vertically</li>
 * <li><i>west</i> and <i>east</i> are resized vertically and kept to their
 * preferred size horizontally</li>
 * <li><i>center</i> is resized both horizontally and vertically</li>
 * </ul>
 * Default cascading order for master-detail is :
 * <p>
 * north -> west -> center -> east -> south
 *
 * @author Vincent Vandenschrick
 */
public class BasicBorderViewDescriptor extends BasicCompositeViewDescriptor
    implements IBorderViewDescriptor {

  private IViewDescriptor centerViewDescriptor;
  private IViewDescriptor eastViewDescriptor;
  private IViewDescriptor northViewDescriptor;
  private IViewDescriptor southViewDescriptor;
  private IViewDescriptor westViewDescriptor;

  /**
   * {@inheritDoc}
   */
  @Override
  public IViewDescriptor getCenterViewDescriptor() {
    IViewDescriptor previousViewDescriptor = getWestViewDescriptor();
    if (previousViewDescriptor == null) {
      previousViewDescriptor = getWestViewDescriptor();
    }
    completeChildDescriptor(centerViewDescriptor, previousViewDescriptor);
    return centerViewDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<IViewDescriptor> getChildViewDescriptors() {
    List<IViewDescriptor> children = new ArrayList<>();
    if (getNorthViewDescriptor() != null) {
      children.add(getNorthViewDescriptor());
    }
    if (getWestViewDescriptor() != null) {
      children.add(getWestViewDescriptor());
    }
    if (getCenterViewDescriptor() != null) {
      children.add(getCenterViewDescriptor());
    }
    if (getEastViewDescriptor() != null) {
      children.add(getEastViewDescriptor());
    }
    if (getSouthViewDescriptor() != null) {
      children.add(getSouthViewDescriptor());
    }
    return children;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IViewDescriptor getEastViewDescriptor() {
    IViewDescriptor previousViewDescriptor = getCenterViewDescriptor();
    if (previousViewDescriptor == null) {
      previousViewDescriptor = getWestViewDescriptor();
    }
    if (previousViewDescriptor == null) {
      previousViewDescriptor = getNorthViewDescriptor();
    }
    completeChildDescriptor(eastViewDescriptor, previousViewDescriptor);
    return eastViewDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IViewDescriptor getNorthViewDescriptor() {
    completeChildDescriptor(northViewDescriptor, null);
    return northViewDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IViewDescriptor getSouthViewDescriptor() {
    IViewDescriptor previousViewDescriptor = getEastViewDescriptor();
    if (previousViewDescriptor == null) {
      previousViewDescriptor = getCenterViewDescriptor();
    }
    if (previousViewDescriptor == null) {
      previousViewDescriptor = getWestViewDescriptor();
    }
    if (previousViewDescriptor == null) {
      previousViewDescriptor = getNorthViewDescriptor();
    }
    completeChildDescriptor(southViewDescriptor, previousViewDescriptor);
    return southViewDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IViewDescriptor getWestViewDescriptor() {
    IViewDescriptor previousViewDescriptor = getNorthViewDescriptor();
    completeChildDescriptor(westViewDescriptor, previousViewDescriptor);
    return westViewDescriptor;
  }

  /**
   * Sets the child view to layout in the <i>center</i> zone. The child view
   * will be resized both horizontally and vertically.
   *
   * @param centerViewDescriptor
   *          the centerViewDescriptor to set.
   */
  public void setCenterViewDescriptor(IViewDescriptor centerViewDescriptor) {
    this.centerViewDescriptor = centerViewDescriptor;
  }

  /**
   * Sets the child view to layout in the <i>east</i> zone. The child view will
   * be resized vertically.
   *
   * @param eastViewDescriptor
   *          the eastViewDescriptor to set.
   */
  public void setEastViewDescriptor(IViewDescriptor eastViewDescriptor) {
    this.eastViewDescriptor = eastViewDescriptor;
  }

  /**
   * Sets the child view to layout in the <i>north</i> zone. The child view will
   * be resized horizontally.
   *
   * @param northViewDescriptor
   *          the northViewDescriptor to set.
   */
  public void setNorthViewDescriptor(IViewDescriptor northViewDescriptor) {
    this.northViewDescriptor = northViewDescriptor;
  }

  /**
   * Sets the child view to layout in the <i>south</i> zone. The child view will
   * be resized horizontally.
   *
   * @param southViewDescriptor
   *          the southViewDescriptor to set.
   */
  public void setSouthViewDescriptor(IViewDescriptor southViewDescriptor) {
    this.southViewDescriptor = southViewDescriptor;
  }

  /**
   * Sets the child view to layout in the <i>west</i> zone. The child view will
   * be resized vertically.
   *
   * @param westViewDescriptor
   *          the westViewDescriptor to set.
   */
  public void setWestViewDescriptor(IViewDescriptor westViewDescriptor) {
    this.westViewDescriptor = westViewDescriptor;
  }
}
