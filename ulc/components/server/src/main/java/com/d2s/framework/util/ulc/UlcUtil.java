/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.ulc;

import com.ulcjava.base.application.ClientContext;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.ULCContainer;
import com.ulcjava.base.application.ULCWindow;
import com.ulcjava.base.application.util.Color;

/**
 * A helper class for Ulc.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
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
    if (factor <= 1) {
      return new Color(Math.max((int) (color.getRed() * factor), 0), Math.max(
          (int) (color.getGreen() * factor), 0), Math.max((int) (color
          .getBlue() * factor), 0), color.getAlpha());
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
   * Make even and odd rows background colors slightly different in collection
   * component (table, list, ...).
   * 
   * @param renderer
   *          the renderer to work on.
   * @param collectionComponent
   *          the collection component (table, list, ...) on which this renderer
   *          is used.
   * @param isSelected
   *          is the row selected ?
   * @param row
   *          the row to render.
   */
  public static void alternateEvenOddBackground(ULCComponent renderer,
      ULCComponent collectionComponent, boolean isSelected, int row) {
    if (!isSelected) {
      if (row % 2 == 1) {
        renderer.setBackground(getScaledColor(collectionComponent
            .getBackground(), DARKER_COLOR_FACTOR));
      } else {
        renderer.setBackground(collectionComponent.getBackground());
      }
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
}
