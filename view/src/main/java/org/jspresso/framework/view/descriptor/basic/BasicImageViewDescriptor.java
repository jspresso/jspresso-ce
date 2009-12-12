/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.view.descriptor.IImageViewDescriptor;

/**
 * This type of view descriptor is used to display a binary property or a string
 * property containing an URL as an image. By default, binary properties are
 * rendered as button fields that allow to upload, download and query size of
 * the binary content. This button field visually indicate whether the binary
 * property is empty or not. Whenever you know that the underlying property is
 * used to store image content, you can explicitely define an image view backed
 * by the binary property descriptor and use it in your UI. Jspresso will then
 * display the image whose content is stored in the binary property directly in
 * the UI.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicImageViewDescriptor extends BasicViewDescriptor implements
    IImageViewDescriptor {

  private boolean scrollable;

  /**
   * Constructs a new <code>BasicImageViewDescriptor</code> instance.
   */
  protected BasicImageViewDescriptor() {
    scrollable = true;
  }

  /**
   * Configures the image view to be either croped or scrollable when the
   * display area is too small to display it. A value of <code>true</code>
   * (default) means that the image view will be made scrollable.
   * 
   * @param scrollable
   *          the scrollable to set.
   */
  public void setScrollable(boolean scrollable) {
    this.scrollable = scrollable;
  }

  /**
   * Gets the scrollable.
   * 
   * @return the scrollable.
   */
  public boolean isScrollable() {
    return scrollable;
  }

}
