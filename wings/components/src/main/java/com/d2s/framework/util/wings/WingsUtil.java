/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.util.wings;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.wings.SComponent;
import org.wings.SContainer;
import org.wings.SDialog;
import org.wings.SDimension;
import org.wings.SRootContainer;

import com.d2s.framework.util.exception.NestedRuntimeException;

/**
 * A helper class for Swing.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class WingsUtil {

  private static final double    DARKER_COLOR_FACTOR = 0.93;

  private static final boolean   DISABLE_THREADING   = false;

  /**
   * <code>FULL_DIM_PERCENT</code>.
   */
  public static final String    FULL_DIM_PERCENT    = "100%";

  /**
   * <code>FULLAREA</code>.
   */
  public static final SDimension FULLAREA            = new SDimension(
                                                         FULL_DIM_PERCENT,
                                                         FULL_DIM_PERCENT);
  /**
   * <code>FULLWIDTH</code>.
   */
  public static final SDimension FULLWIDTH           = new SDimension(
                                                         FULL_DIM_PERCENT, null);
  /**
   * <code>FULLHEIGHT</code>.
   */
  public static final SDimension FULLHEIGHT          = new SDimension(null,
                                                         FULL_DIM_PERCENT);

  private WingsUtil() {
    // Helper class private constructor.
  }

  /**
   * Make even and odd rows background colors slightly different in collection
   * component (table, list, ...).
   * 
   * @param renderer
   *            the renderer to work on.
   * @param collectionSComponent
   *            the collection component (table, list, ...) on which this
   *            renderer is used.
   * @param isSelected
   *            is the row selected ?
   * @param row
   *            the row to render.
   */
  public static void alternateEvenOddBackground(SComponent renderer,
      SComponent collectionSComponent, boolean isSelected, int row) {
    if (!isSelected) {
      if (row % 2 == 1) {
        renderer.setBackground(WingsUtil.getScaledColor(collectionSComponent
            .getBackground(), DARKER_COLOR_FACTOR));
      } else {
        renderer.setBackground(collectionSComponent.getBackground());
      }
    }
  }

  /**
   * Retrieves the first contained component of a certain type.
   * 
   * @param component
   *            the component to start from.
   * @param childSComponentType
   *            the type of the component to look for.
   * @return the first contained component of the looked for type or null if
   *         none.
   */
  public static SComponent getFirstChildSComponentOfType(SComponent component,
      Class<? extends SComponent> childSComponentType) {
    if (childSComponentType.isAssignableFrom(component.getClass())) {
      return component;
    } else if (component instanceof SContainer) {
      SComponent[] children = ((SContainer) component).getComponents();
      for (SComponent child : children) {
        SComponent childResult = getFirstChildSComponentOfType(child,
            childSComponentType);
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
   *            the color to scale.
   * @param factor
   *            the factor to use.
   * @return the scaled color.
   */
  public static Color getScaledColor(Color color, double factor) {
    if (color == null) {
      return null;
    }
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
   * Gets the visible parent window.
   * 
   * @param component
   *            the component to start from
   * @return the visible parent window or null.
   */
  public static SContainer getVisibleWindow(SComponent component) {
    return getWindowOrInternalFrame(component);
  }

  /**
   * Gets the window or the internal frame holding the component.
   * 
   * @param component
   *            the component to look the window or internal frame for.
   * @return the window (frame or dialog) or the internal frame in the component
   *         hierarchy.
   */
  public static SContainer getWindowOrInternalFrame(SComponent component) {
    if ((component instanceof SRootContainer) || (component instanceof SDialog)) {
      return (SContainer) component;
    } else if (component != null) {
      return getWindowOrInternalFrame(component.getParent());
    }
    return null;
  }

  /**
   * Tests wether in swing event dispatch thread. If not, use SwingUtilities to
   * invoke runnable and wait.
   * 
   * @param runnable
   *            the runnable operation which updates the GUI.
   */
  public static void updateSwingGui(Runnable runnable) {
    if (DISABLE_THREADING) {
      runnable.run();
    } else {
      if (SwingUtilities.isEventDispatchThread()) {
        runnable.run();
      } else {
        try {
          SwingUtilities.invokeAndWait(runnable);
        } catch (InterruptedException ex) {
          throw new NestedRuntimeException(ex);
        } catch (InvocationTargetException ex) {
          throw new NestedRuntimeException(ex);
        }
        // SwingUtilities.invokeLater(runnable);
      }
    }
  }
}
