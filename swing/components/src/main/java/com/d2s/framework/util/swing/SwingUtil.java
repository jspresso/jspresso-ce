/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.d2s.framework.util.exception.NestedRuntimeException;

import foxtrot.Job;
import foxtrot.Worker;

/**
 * A helper class for Swing.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class SwingUtil {

  private static final double DARKER_COLOR_FACTOR                         = 0.93;

  private static final String TEXTFIELD_FONT_KEY                          = "TextField.font";
  private static final String FORMATTED_TEXTFIELD_FONT_KEY                = "FormattedTextField.font";
  private static final String TEXTFIELD_INACTIVE_BACKGROUND_KEY           = "TextField.inactiveBackground";
  private static final String FORMATTED_TEXTFIELD_INACTIVE_BACKGROUND_KEY = "FormattedTextField.inactiveBackground";
  
  private static final boolean DISABLE_THREADING = true;

  private SwingUtil() {
    // Helper class private constructor.
  }

  /**
   * Tests wether in swing event dispatch thread. If not, use SwingUtilities to
   * invoke runnable and wait.
   * 
   * @param runnable
   *          the runnable operation which updates the GUI.
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

  /**
   * Gets the window or the internal frame holding the component.
   * 
   * @param component
   *          the component to look the window or internal frame for.
   * @return the window (frame or dialog) or the internal frame in the component
   *         hierarchy.
   */
  public static Component getWindowOrInternalFrame(Component component) {
    if ((component instanceof Window) || (component instanceof JInternalFrame)) {
      return component;
    } else if (component != null) {
      return getWindowOrInternalFrame(component.getParent());
    }
    return null;
  }

  /**
   * Executes a job avoiding the common swing UI freeze.
   * 
   * @param foxtrotJob
   *          the potentially long running job to execute.
   * @return the job execution result.
   */
  public static Object performLongOperation(Job foxtrotJob) {
    if (DISABLE_THREADING) {
      return foxtrotJob.run();
    }
    return Worker.post(foxtrotJob);
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
  public static Component getFirstChildComponentOfType(Component component,
      Class<? extends JComponent> childComponentType) {
    if (childComponentType.isAssignableFrom(component.getClass())) {
      return component;
    } else if (component instanceof Container) {
      Component[] children = ((Container) component).getComponents();
      for (Component child : children) {
        Component childResult = getFirstChildComponentOfType(child,
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
  public static void alternateEvenOddBackground(Component renderer,
      Component collectionComponent, boolean isSelected, int row) {
    if (!isSelected) {
      if (row % 2 == 1) {
        renderer.setBackground(SwingUtil.getScaledColor(collectionComponent
            .getBackground(), DARKER_COLOR_FACTOR));
      } else {
        renderer.setBackground(collectionComponent.getBackground());
      }
    }
  }

  /**
   * Workaround for Bug#5063999.
   */
  public static void installDefaults() {
    UIManager.put(FORMATTED_TEXTFIELD_FONT_KEY, UIManager
        .get(TEXTFIELD_FONT_KEY));
    UIManager.put(FORMATTED_TEXTFIELD_INACTIVE_BACKGROUND_KEY, UIManager
        .get(TEXTFIELD_INACTIVE_BACKGROUND_KEY));
  }

  /**
   * Is the component passed in parameter used as an editor ?
   * 
   * @param comp
   *          the component to test.
   * @return true if the component is currently used as an editor.
   */
  public static boolean isUsedAsEditor(Component comp) {
    boolean usedAsEditor = false;
    Container parent = comp.getParent();
    while (parent != null && !usedAsEditor) {
      if (parent instanceof JTable) {
        usedAsEditor = true;
      }
      parent = parent.getParent();
    }
    return usedAsEditor;
  }

  /**
   * configures a textfield so that it selects its content when getting focus by
   * another mean than the mouse.
   * 
   * @param textField
   *          the textfield to work on.
   */
  public static void enableSelectionOnFocusGained(final JTextField textField) {

    textField.addFocusListener(new FocusListener() {

      public void focusGained(FocusEvent fe) {
        if (fe.getOppositeComponent() != null) {
          FocusGainedTask task = new FocusGainedTask(textField);
          if (textField instanceof JFormattedTextField) {
            SwingUtilities.invokeLater(task);
          } else {
            task.run();
          }
        }
      }

      public void focusLost(FocusEvent fe) {
        if (!fe.isTemporary()) {
          FocusLostTask task = new FocusLostTask(textField);
          if (textField instanceof JFormattedTextField) {
            SwingUtilities.invokeLater(task);
          } else {
            task.run();
          }
        }
      }
    });
  }

  private static final class FocusGainedTask implements Runnable {

    private JTextField textField;

    /**
     * Constructs a new <code>FocusGainedTask</code> instance.
     * 
     * @param textField
     *          the text field to run on.
     */
    public FocusGainedTask(JTextField textField) {
      this.textField = textField;
    }

    /**
     * {@inheritDoc}
     */
    public void run() {
      if (!isUsedAsEditor(textField)) {
        textField.selectAll();
      }
    }
  }

  private static final class FocusLostTask implements Runnable {

    private JTextField textField;

    /**
     * Constructs a new <code>FocusLostTask</code> instance.
     * 
     * @param textField
     *          the text field to run on.
     */
    public FocusLostTask(JTextField textField) {
      this.textField = textField;
    }

    /**
     * {@inheritDoc}
     */
    public void run() {
      if (!isUsedAsEditor(textField)) {
        if (textField.getText().length() > 0) {
          textField.getCaret().setDot(textField.getText().length());
        }
      }
    }
  }
}
