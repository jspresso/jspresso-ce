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
package org.jspresso.framework.gui.remote.mobile;

import org.jspresso.framework.gui.remote.RImageComponent;
import org.jspresso.framework.util.gui.Dimension;

/**
 * A remote mobile image component.
 * 
 * @author Vincent Vandenschrick
 */
public class RMobileImageComponent extends RImageComponent {

  private static final long serialVersionUID = -8099319637631348345L;

  private String    submitUrl;
  private Dimension imageSize;
  private String    formatName;

  /**
   * Constructs a new {@code RMobileImageComponent} instance.
   *
   * @param guid
   *     the guid.
   */
  public RMobileImageComponent(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RMobileImageComponent} instance. Only used for
   * serialization support.
   */
  public RMobileImageComponent() {
    // For serialization support
  }

  /**
   * Gets submit url.
   *
   * @return the submit url
   */
  public String getSubmitUrl() {
    return submitUrl;
  }

  /**
   * Sets submit url.
   *
   * @param submitUrl
   *     the submit url
   */
  public void setSubmitUrl(String submitUrl) {
    this.submitUrl = submitUrl;
  }

  /**
   * Gets image size.
   *
   * @return the image size
   */
  public Dimension getImageSize() {
    return imageSize;
  }

  /**
   * Sets image size.
   *
   * @param imageSize
   *     the image size
   */
  public void setImageSize(Dimension imageSize) {
    this.imageSize = imageSize;
  }

  /**
   * Gets format name.
   *
   * @return the format name
   */
  public String getFormatName() {
    return formatName;
  }

  /**
   * Sets format name.
   *
   * @param formatName
   *     the format name
   */
  public void setFormatName(String formatName) {
    this.formatName = formatName;
  }
}
