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

import org.jspresso.framework.view.descriptor.IBorderViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * Default implementation of a border view descriptor.
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
  public IViewDescriptor getCenterViewDescriptor() {
    completeChildDescriptor(centerViewDescriptor);
    return centerViewDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public List<IViewDescriptor> getChildViewDescriptors() {
    List<IViewDescriptor> children = new ArrayList<IViewDescriptor>();
    if (getEastViewDescriptor() != null) {
      children.add(getEastViewDescriptor());
    }
    if (getNorthViewDescriptor() != null) {
      children.add(getNorthViewDescriptor());
    }
    if (getCenterViewDescriptor() != null) {
      children.add(getCenterViewDescriptor());
    }
    if (getWestViewDescriptor() != null) {
      children.add(getWestViewDescriptor());
    }
    if (getSouthViewDescriptor() != null) {
      children.add(getSouthViewDescriptor());
    }
    return children;
  }

  /**
   * {@inheritDoc}
   */
  public IViewDescriptor getEastViewDescriptor() {
    completeChildDescriptor(eastViewDescriptor);
    return eastViewDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public IViewDescriptor getNorthViewDescriptor() {
    completeChildDescriptor(northViewDescriptor);
    return northViewDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public IViewDescriptor getSouthViewDescriptor() {
    completeChildDescriptor(southViewDescriptor);
    return southViewDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public IViewDescriptor getWestViewDescriptor() {
    completeChildDescriptor(westViewDescriptor);
    return westViewDescriptor;
  }

  /**
   * Sets the centerViewDescriptor.
   * 
   * @param centerViewDescriptor
   *          the centerViewDescriptor to set.
   */
  public void setCenterViewDescriptor(IViewDescriptor centerViewDescriptor) {
    this.centerViewDescriptor = centerViewDescriptor;
  }

  /**
   * Sets the eastViewDescriptor.
   * 
   * @param eastViewDescriptor
   *          the eastViewDescriptor to set.
   */
  public void setEastViewDescriptor(IViewDescriptor eastViewDescriptor) {
    this.eastViewDescriptor = eastViewDescriptor;
  }

  /**
   * Sets the northViewDescriptor.
   * 
   * @param northViewDescriptor
   *          the northViewDescriptor to set.
   */
  public void setNorthViewDescriptor(IViewDescriptor northViewDescriptor) {
    this.northViewDescriptor = northViewDescriptor;
  }

  /**
   * Sets the southViewDescriptor.
   * 
   * @param southViewDescriptor
   *          the southViewDescriptor to set.
   */
  public void setSouthViewDescriptor(IViewDescriptor southViewDescriptor) {
    this.southViewDescriptor = southViewDescriptor;
  }

  /**
   * Sets the westViewDescriptor.
   * 
   * @param westViewDescriptor
   *          the westViewDescriptor to set.
   */
  public void setWestViewDescriptor(IViewDescriptor westViewDescriptor) {
    this.westViewDescriptor = westViewDescriptor;
  }
}
