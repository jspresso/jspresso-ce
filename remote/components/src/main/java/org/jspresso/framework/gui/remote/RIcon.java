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

import org.jspresso.framework.util.remote.RemoteServerPeer;

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
public class RIcon extends RemoteServerPeer {

  private static final long serialVersionUID = -6387488983512746220L;

  private String imageUrlSpec;
  private int width;
  private int height;
  
  /**
   * Gets the width.
   * 
   * @return the width.
   */
  public int getWidth() {
    return width;
  }
  
  /**
   * Sets the width.
   * 
   * @param width the width to set.
   */
  public void setWidth(int width) {
    this.width = width;
  }
  
  /**
   * Gets the height.
   * 
   * @return the height.
   */
  public int getHeight() {
    return height;
  }
  
  /**
   * Sets the height.
   * 
   * @param height the height to set.
   */
  public void setHeight(int height) {
    this.height = height;
  }

  /**
   * Sets the imageUrlSpec.
   * 
   * @param imageUrlSpec the imageUrlSpec to set.
   */
  public void setImageUrlSpec(String imageUrlSpec) {
    this.imageUrlSpec = imageUrlSpec;
  }

  /**
   * Gets the imageUrlSpec.
   * 
   * @return the imageUrlSpec.
   */
  public String getImageUrlSpec() {
    return imageUrlSpec;
  }
  
}
