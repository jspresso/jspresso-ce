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

import org.jspresso.framework.view.descriptor.EPosition;
import org.jspresso.framework.view.descriptor.basic.BasicMapViewDescriptor;

/**
 * A mobile map view descriptor.
 *
 * @author Vincent Vandenschrick
 */
public class MobileMapViewDescriptor extends BasicMapViewDescriptor implements IMobilePageSectionViewDescriptor {

  private EPosition    position;
  private List<String> forClientTypes;

  /**
   * Instantiates a new Mobile map view descriptor.
   */
  public MobileMapViewDescriptor() {
    this.position = EPosition.RIGHT;
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

}
