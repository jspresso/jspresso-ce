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

import java.util.List;

import org.jspresso.framework.view.descriptor.EHorizontalPosition;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicTabViewDescriptor;

/**
 * A composite view descriptor that aggregates mobile views.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision$
 */
public class MobileTabViewDescriptor extends BasicTabViewDescriptor
    implements IMobilePageSectionViewDescriptor {

  private boolean             carouselMode;
  private EHorizontalPosition horizontalPosition;
  private List<String>        forClientTypes;

  /**
   * Instantiates a new Mobile tab view descriptor.
   */
  public MobileTabViewDescriptor() {
    this.carouselMode = false;
    this.horizontalPosition = EHorizontalPosition.LEFT;
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
   * Not supported in mobile environment.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public void setCascadingModels(boolean cascadingModels) {
    throw new UnsupportedOperationException("Not supported in mobile environment.");
  }

  /**
   * Always {@code false}.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public boolean isCascadingModels() {
    return false;
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
   * @param horizontalPosition the horizontal position
   */
  public void setHorizontalPosition(EHorizontalPosition horizontalPosition) {
    this.horizontalPosition = horizontalPosition;
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
