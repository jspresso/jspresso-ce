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
package org.jspresso.framework.gui.remote;

import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.remote.RemotePeer;

/**
 * This class is the generic server peer of a client GUI icon.
 *
 * @author Vincent Vandenschrick
 */
public class RIcon extends RemotePeer {

  /**
   * {@code DEFAULT_DIM}.
   */
  public static final Dimension DEFAULT_DIM      = new Dimension(24, 24);

  private static final long     serialVersionUID = 4846497088272546437L;

  private Dimension             dimension;
  private String                imageUrlSpec;

  /**
   * Constructs a new {@code RIcon} instance.
   *
   * @param guid
   *          the guid
   */
  public RIcon(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RIcon} instance. Only used for serialization
   * support.
   */
  public RIcon() {
    // For serialization support
  }

  /**
   * Gets the dimension.
   *
   * @return the dimension.
   */
  public Dimension getDimension() {
    return dimension;
  }

  /**
   * Gets the imageUrlSpec.
   *
   * @return the imageUrlSpec.
   */
  public String getImageUrlSpec() {
    return imageUrlSpec;
  }

  /**
   * Sets the dimension.
   *
   * @param dimension
   *          the dimension to set.
   */
  public void setDimension(Dimension dimension) {
    this.dimension = dimension;
  }

  /**
   * Sets the imageUrlSpec.
   *
   * @param imageUrlSpec
   *          the imageUrlSpec to set.
   */
  public void setImageUrlSpec(String imageUrlSpec) {
    this.imageUrlSpec = imageUrlSpec;
  }

}
