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
package org.jspresso.framework.gui.remote;

import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.remote.RemotePeer;

/**
 * This class is the generic server peer of a client GUI icon.
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
public class RIcon extends RemotePeer {

  private static final long serialVersionUID = 4846497088272546437L;

  private Dimension         dimension;
  private String            imageUrlSpec;

  /**
   * Constructs a new <code>RIcon</code> instance. Only used for GWT
   * serialization support.
   */
  protected RIcon() {
    // For GWT support
  }

  /**
   * Constructs a new <code>RIcon</code> instance.
   * 
   * @param guid
   *          the guid
   */
  public RIcon(String guid) {
    super(guid);
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
   * Sets the imageUrlSpec.
   * 
   * @param imageUrlSpec
   *          the imageUrlSpec to set.
   */
  public void setImageUrlSpec(String imageUrlSpec) {
    this.imageUrlSpec = imageUrlSpec;
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
   * Sets the dimension.
   * 
   * @param dimension
   *          the dimension to set.
   */
  public void setDimension(Dimension dimension) {
    this.dimension = dimension;
  }

}
