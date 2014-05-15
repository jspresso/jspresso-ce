/*
 * Copyright (c) 2005-2014 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.view.descriptor.EHorizontalPosition;
import org.jspresso.framework.view.descriptor.basic.BasicMapViewDescriptor;

/**
 * A mobile map view descriptor.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision$
 */
public class MobileMapViewDescriptor extends BasicMapViewDescriptor
    implements IMobilePageSectionViewDescriptor {

  private EHorizontalPosition horizontalPosition;

  /**
   * Instantiates a new Mobile map view descriptor.
   */
  public MobileMapViewDescriptor() {
    this.horizontalPosition = EHorizontalPosition.RIGHT;
  }

  /**
   * Gets horizontal position.
   *
   * @return the horizontal position
   */
  @Override
  public EHorizontalPosition getHorizontalPosition() {
    return horizontalPosition;
  }

  /**
   * Sets horizontal position.
   *
   * @param horizontalPosition
   *     the horizontal position
   */
  public void setHorizontalPosition(EHorizontalPosition horizontalPosition) {
    this.horizontalPosition = horizontalPosition;
  }
}
