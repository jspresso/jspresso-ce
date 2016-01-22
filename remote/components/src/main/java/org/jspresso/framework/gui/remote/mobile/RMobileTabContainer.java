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
package org.jspresso.framework.gui.remote.mobile;

import org.jspresso.framework.gui.remote.RTabContainer;

/**
 * A remote mobile tab component.
 *
 * @author Vincent Vandenschrick
 */
public class RMobileTabContainer extends RTabContainer {

  private static final long serialVersionUID = -6422873290771274158L;

  private boolean carouselMode;
  private String  position;

  /**
   * Constructs a new {@code RMobileTabContainer} instance.
   *
   * @param guid           the guid.
   */
  public RMobileTabContainer(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RMobileTabContainer} instance. Only used for
   * serialization support.
   */
  public RMobileTabContainer() {
    // For serialization support
  }

  /**
   * Is carousel mode.
   *
   * @return the boolean
   */
  public boolean isCarouselMode() {
    return carouselMode;
  }

  /**
   * Sets carousel mode.
   *
   * @param carouselMode the carousel mode
   */
  public void setCarouselMode(boolean carouselMode) {
    this.carouselMode = carouselMode;
  }

  /**
   * Get position.
   *
   * @return the boolean
   */
  public String getPosition() {
    return position;
  }

  /**
   * Sets position.
   *
   * @param position the position
   */
  public void setPosition(String position) {
    this.position = position;
  }
}
