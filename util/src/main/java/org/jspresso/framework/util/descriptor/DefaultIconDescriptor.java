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
package org.jspresso.framework.util.descriptor;

import org.jspresso.framework.util.gui.Icon;

/**
 * This is a utility class from which most displayable descriptors inherit for
 * factorization purpose. It provides an icon image URL.
 *
 * @author Vincent Vandenschrick
 */
public class DefaultIconDescriptor extends DefaultDescriptor implements
    IIconDescriptor {

  private Icon icon;

  /**
   * {@inheritDoc}
   */
  @Override
  public DefaultIconDescriptor clone() {
    DefaultIconDescriptor clone = (DefaultIconDescriptor) super.clone();
    if (icon != null) {
      clone.icon = icon.clone();
    }
    return clone;
  }

  /**
   * Gets the iconImageURL.
   *
   * @return the iconImageURL.
   */
  @Override
  public Icon getIcon() {
    return icon;
  }

  /**
   * Sets the icon image URL of this descriptor. Supported URL protocols include
   * :
   * <ul>
   * <li>all JVM supported protocols</li>
   * <li>the <b>jar:/</b> pseudo URL protocol</li>
   * <li>the <b>classpath:/</b> pseudo URL protocol</li>
   * </ul>
   *
   * @param iconImageURL
   *          the iconImageURL to set.
   */
  public void setIconImageURL(String iconImageURL) {
    if (icon == null) {
      icon = new Icon();
    }
    icon.setIconImageURL(iconImageURL);
  }

  /**
   * Sets the icon preferred width.
   *
   * @param iconPreferredWidth
   *          the iconPreferredWidth to set.
   */
  public void setIconPreferredWidth(int iconPreferredWidth) {
    if (icon == null) {
      icon = new Icon();
    }
    icon.setWidth(iconPreferredWidth);
  }

  /**
   * Sets the icon preferred height.
   *
   * @param iconPreferredHeight
   *          the iconPreferredHeight to set.
   */
  public void setIconPreferredHeight(int iconPreferredHeight) {
    if (icon == null) {
      icon = new Icon();
    }
    icon.setHeight(iconPreferredHeight);
  }

  /**
   * Sets the icon.
   *
   * @param icon
   *          the icon to set.
   */
  public void setIcon(Icon icon) {
    this.icon = icon;
  }
}
