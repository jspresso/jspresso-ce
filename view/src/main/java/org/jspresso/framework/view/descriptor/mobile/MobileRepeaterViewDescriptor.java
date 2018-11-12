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
package org.jspresso.framework.view.descriptor.mobile;

import java.util.List;

import org.jspresso.framework.util.gui.EPosition;
import org.jspresso.framework.view.descriptor.basic.AbstractRepeaterViewDescriptor;

/**
 * This descriptor is used to implement a repeater view. A repeater view displays a
 * collection of components, each one in an arbitrary view that is repeated as necessary.
 *
 * @author Vincent Vandenschrick
 */
public class MobileRepeaterViewDescriptor extends AbstractRepeaterViewDescriptor implements IMobileViewDescriptor {

  private EPosition    position;
  private List<String> forClientTypes;

  /**
   * Instantiates a new Mobile repeater view descriptor.
   */
  public MobileRepeaterViewDescriptor() {
    this.position = EPosition.LEFT;
  }

  /**
   * Gets position.
   *
   * @return the position
   */
  @Override
  public EPosition getPosition() {
    return position;
  }

  /**
   * Sets position.
   *
   * @param position
   *     the position
   */
  public void setPosition(EPosition position) {
    this.position = position;
  }

  /**
   * Gets for client types.
   *
   * @return the for client types
   */
  @Override
  public List<String> getForClientTypes() {
    return forClientTypes;
  }

  /**
   * Sets for client types.
   *
   * @param forClientTypes
   *     the for client types
   */
  public void setForClientTypes(List<String> forClientTypes) {
    this.forClientTypes = forClientTypes;
  }

  /**
   * Always false in mobile environment.
   * <p>
   * {@inheritDoc}
   *
   * @return the boolean
   */
  @Override
  public boolean isAutoSelectFirstRow() {
    return false;
  }

  /**
   * Not supported in mobile environment.
   * <p>
   * {@inheritDoc}
   *
   * @param autoSelectFirstRow
   *     the auto select first row
   */
  @Override
  public void setAutoSelectFirstRow(boolean autoSelectFirstRow) {
    throw new UnsupportedOperationException("Not supported in mobile environment.");
  }


}
