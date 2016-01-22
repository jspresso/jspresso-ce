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
package org.jspresso.framework.view.swing;

/**
 * Interface implemented by dynamic style capable renderers.
 *
 * @author Vincent Vandenschrick
 */
public interface DynamicStyleRenderer {

  /**
   * Sets the toolTipProperty.
   *
   * @param toolTipProperty
   *          the toolTipProperty to set.
   */
  void setToolTipProperty(String toolTipProperty);

  /**
   * Sets the backgroundProperty.
   *
   * @param backgroundProperty
   *          the backgroundProperty to set.
   */
  void setBackgroundProperty(String backgroundProperty);

  /**
   * Sets the foregroundProperty.
   *
   * @param foregroundProperty
   *          the foregroundProperty to set.
   */
  void setForegroundProperty(String foregroundProperty);

  /**
   * Sets the fontProperty.
   *
   * @param fontProperty
   *          the fontProperty to set.
   */
  void setFontProperty(String fontProperty);

}
