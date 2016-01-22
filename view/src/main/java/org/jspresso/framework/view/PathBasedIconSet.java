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
package org.jspresso.framework.view;

import org.jspresso.framework.util.gui.Dimension;

/**
 * Implements a icon set that can be variabilized using a root path.
 *
 * @author Vincent Vandenschrick
 */
public class PathBasedIconSet implements IIconSet {

  private static final String PNG_EXTENSION       = ".png";
  private static final String GIF_EXTENSION       = ".gif";
  private static final String JPG_EXTENSION       = ".jpg";
  private static final String JPEG_EXTENSION      = ".jpeg";

  private String              rootPath;
  private String[]            supportedExtensions = {
      PNG_EXTENSION, GIF_EXTENSION, JPG_EXTENSION, JPEG_EXTENSION
                                                  };
  private String              defaultExtension    = PNG_EXTENSION;

  /**
   * {@inheritDoc}
   */
  @Override
  public String getIconImageURL(String iconName, Dimension dim) {
    String[] imageNameParts = splitIconNameParts(iconName);
    StringBuilder url = new StringBuilder();
    if (getRootPath() != null) {
      url.append(getRootPath()).append("/");
    }
    url.append(imageNameParts[0]);
    if (dim != null) {
      url.append("-");
      url.append(dim.getWidth());
      url.append("x");
      url.append(dim.getHeight());
    }
    url.append(imageNameParts[1]);
    return url.toString();
  }

  /**
   * Splits an icon name in base name / extension.
   *
   * @param iconName
   *          the iconName to parse and split.
   * @return a 2 length array containing the base name and the extension of the
   *         icon image file.
   */
  protected String[] splitIconNameParts(String iconName) {
    for (String supportedExtension : getSupportedExtensions()) {
      if (iconName.length() > supportedExtension.length()
          && iconName.toLowerCase().endsWith(supportedExtension)) {
        return new String[] {
            iconName.substring(0,
                iconName.length() - supportedExtension.length()),
            iconName.substring(iconName.length() - supportedExtension.length())
        };
      }
    }
    return new String[] {
        iconName, getDefaultExtension()
    };
  }

  /**
   * Gets the rootPath.
   *
   * @return the rootPath.
   */
  protected String getRootPath() {
    return rootPath;
  }

  /**
   * Sets the rootPath.
   *
   * @param rootPath
   *          the rootPath to set.
   */
  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  /**
   * Gets the supportedExtensions.
   *
   * @return the supportedExtensions.
   */
  protected String[] getSupportedExtensions() {
    return supportedExtensions;
  }

  /**
   * Sets the supportedExtensions. Defaults to
   * {@code [&quot;.png&quot;,&quot;.gif&quot;,&quot;.jpg&quot;,&quot;.jpeg&quot;]}
   * .
   *
   * @param supportedExtensions
   *          the supportedExtensions to set.
   */
  public void setSupportedExtensions(String... supportedExtensions) {
    this.supportedExtensions = supportedExtensions;
  }

  /**
   * Gets the defaultExtension.
   *
   * @return the defaultExtension.
   */
  protected String getDefaultExtension() {
    return defaultExtension;
  }

  /**
   * Sets the defaultExtension. Defaults to {@code &quot;.png&quot;}.
   *
   * @param defaultExtension
   *          the defaultExtension to set.
   */
  public void setDefaultExtension(String defaultExtension) {
    this.defaultExtension = defaultExtension;
  }

}
