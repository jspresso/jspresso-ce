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
package org.jspresso.framework.view.descriptor;

/**
 * This public interface is implemented by container view descriptors which are
 * organizing their contained view descriptors in a north / south / east / west
 * / center manner. This kind of described view can typically be implemented
 * using a swing JPanel with a BorderLayout.
 *
 * @author Vincent Vandenschrick
 */
public interface IBorderViewDescriptor extends ICompositeViewDescriptor {

  /**
   * Gets the contained view descriptor located at the center position.
   *
   * @return the contained view descriptor or null.
   */
  IViewDescriptor getCenterViewDescriptor();

  /**
   * Gets the contained view descriptor located at the east position.
   *
   * @return the contained view descriptor or null.
   */
  IViewDescriptor getEastViewDescriptor();

  /**
   * Gets the contained view descriptor located at the north position.
   *
   * @return the contained view descriptor or null.
   */
  IViewDescriptor getNorthViewDescriptor();

  /**
   * Gets the contained view descriptor located at the south position.
   *
   * @return the contained view descriptor or null.
   */
  IViewDescriptor getSouthViewDescriptor();

  /**
   * Gets the contained view descriptor located at the west position.
   *
   * @return the contained view descriptor or null.
   */
  IViewDescriptor getWestViewDescriptor();
}
