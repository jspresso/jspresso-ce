/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.util.ulc;

import com.ulcjava.base.application.ClientContext;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.ULCContainer;
import com.ulcjava.base.application.ULCWindow;
import com.ulcjava.base.application.UlcUtilities;
import com.ulcjava.base.application.util.Color;
import com.ulcjava.base.application.util.Dimension;

/**
 * A helper class for Ulc.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class UlcUtil {

  private static final double DARKER_COLOR_FACTOR = 0.93;

  private UlcUtil() {
    // Helper class private constructor.
  }

  /**
   * Make even and odd rows background colors slightly different in collection
   * component (table, list, ...).
   * 
   * @param background
   *          the base background color of the collection component (table,
   *          list, ...) on which this renderer is used.
   * @param isSelected
   *          is the row selected ?
   * @param row
   *          the row to render.
   * @return the computed color
   */
  public static Color computeEvenOddBackground(Color background,
      boolean isSelected, int row) {
    if (!isSelected) {
      if (row % 2 == 1) {
        return getScaledColor(background, DARKER_COLOR_FACTOR);
      }
      return background;
    }
    return background.brighter();
  }

  /**
   * Center a window on screen.
   * 
   * @param w
   *          the window to center on screen.
   */
  public static void centerInParent(ULCWindow w) {
    ULCWindow parent = (ULCWindow) w.getParent();
    if (parent != null) {
      Dimension parentSize = parent.getSize();
      w.setLocation(parent.getX() + (parentSize.getWidth() - w.getWidth()) / 2,
          parent.getY() + (parentSize.getHeight() - w.getHeight()) / 2);
    }
  }

  /**
   * Center a window on screen.
   * 
   * @param w
   *          the window to center on screen.
   */
  public static void centerOnScreen(ULCWindow w) {
    w.setLocation((ClientContext.getScreenWidth() - w.getWidth()) / 2,
        (ClientContext.getScreenHeight() - w.getHeight()) / 2);
  }

  /**
   * Retrieves the first contained component of a certain type.
   * 
   * @param component
   *          the component to start from.
   * @param childComponentType
   *          the type of the component to look for.
   * @return the first contained component of the looked for type or null if
   *         none.
   */
  public static ULCComponent getFirstChildComponentOfType(
      ULCComponent component, Class<? extends ULCComponent> childComponentType) {
    if (childComponentType.isAssignableFrom(component.getClass())) {
      return component;
    } else if (component instanceof ULCContainer) {
      ULCComponent[] children = ((ULCContainer) component).getComponents();
      for (ULCComponent child : children) {
        ULCComponent childResult = getFirstChildComponentOfType(child,
            childComponentType);
        if (childResult != null) {
          return childResult;
        }
      }
    }
    return null;
  }

  /**
   * Make a color scaled using a defined factor.
   * 
   * @param color
   *          the color to scale.
   * @param factor
   *          the factor to use.
   * @return the scaled color.
   */
  public static Color getScaledColor(Color color, double factor) {
    if (color == null) {
      return null;
    }
    if (factor <= 1) {
      return new Color(Math.max((int) (color.getRed() * factor), 0), Math.max(
          (int) (color.getGreen() * factor), 0), Math.max(
          (int) (color.getBlue() * factor), 0), color.getAlpha());
    }
    int r = color.getRed();
    int g = color.getGreen();
    int b = color.getBlue();

    int i = (int) (1.0 / (factor - 1));
    if (r == 0 && g == 0 && b == 0) {
      return new Color(i, i, i);
    }
    if (r > 0 && r < i) {
      r = i;
    }
    if (g > 0 && g < i) {
      g = i;
    }
    if (b > 0 && b < i) {
      b = i;
    }

    return new Color(Math.min((int) (r / (2 - factor)), 255), Math.min(
        (int) (g / (2 - factor)), 255), Math.min((int) (b / (2 - factor)), 255));

  }

  /**
   * Gets the visible parent window.
   * 
   * @param component
   *          the component to start from
   * @return the visible parent window or null.
   */
  public static ULCWindow getVisibleWindow(ULCComponent component) {
    if (component instanceof ULCWindow) {
      return (ULCWindow) component;
    }
    ULCWindow w = UlcUtilities.getWindowAncestor(component);
    if (w != null && !w.isVisible() && w.getParent() != null) {
      return getVisibleWindow(w.getParent());
    }
    return w;
  }
}
