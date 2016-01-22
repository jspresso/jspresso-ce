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
package org.jspresso.framework.util.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import foxtrot.Job;
import foxtrot.Worker;

import org.jspresso.framework.util.exception.NestedRuntimeException;

/**
 * A helper class for Swing.
 *
 * @author Vincent Vandenschrick
 */
public final class SwingUtil {

  private static final double  DARKER_COLOR_FACTOR                         = 0.93;

  private static final boolean DISABLE_THREADING                           = false;
  private static final String  FORMATTED_TEXTFIELD_FONT_KEY                = "FormattedTextField.font";
  private static final String  FORMATTED_TEXTFIELD_INACTIVE_BACKGROUND_KEY = "FormattedTextField.inactiveBackground";
  private static final String  PASSWORDFIELD_FONT_KEY                      = "PasswordField.font";
  private static final String  TEXTFIELD_FONT_KEY                          = "TextField.font";

  private static final String  TEXTFIELD_INACTIVE_BACKGROUND_KEY           = "TextField.inactiveBackground";

  private SwingUtil() {
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
        return SwingUtil.getScaledColor(background, DARKER_COLOR_FACTOR);
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
  public static void centerInParent(Window w) {
    Container parent = w.getParent();
    if (parent != null) {
      Dimension parentSize = parent.getSize();
      w.setLocation(parent.getX() + (parentSize.width - w.getWidth()) / 2,
          parent.getY() + (parentSize.height - w.getHeight()) / 2);
    }
  }

  /**
   * Center a window on screen.
   *
   * @param w
   *          the window to center on screen.
   */
  public static void centerOnScreen(Window w) {
    Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
    w.setLocation((screenDim.width - w.getWidth()) / 2,
        (screenDim.height - w.getHeight()) / 2);
  }

  /**
   * Configures a jButton with default behaviour like the multi-click threshold.
   *
   * @param button
   *          the button to work on.
   */
  public static void configureButton(JButton button) {
    button.setMultiClickThreshhold(500);
  }

  /**
   * Configures a text field so that it selects its content when getting focus by
   * another mean than the mouse.
   *
   * @param textField
   *          the text field to work on.
   */
  public static void enableSelectionOnFocusGained(final JTextField textField) {

    textField.addFocusListener(new FocusListener() {

      @Override
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

      @Override
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

    textField.getDocument().addDocumentListener(
        new TfDocumentListener(textField));
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
    }
    if (component instanceof Container) {
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
  public static Window getVisibleWindow(Component component) {
    if (component instanceof JWindow) {
      return (JWindow) component;
    }
    Window w = SwingUtilities.getWindowAncestor(component);
    if (w != null && !w.isVisible() && w.getParent() != null) {
      return getVisibleWindow(w.getParent());
    }
    return w;
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
    }
    if (component != null) {
      return getWindowOrInternalFrame(component.getParent());
    }
    return null;
  }

  /**
   * Workaround for Bug#5063999.
   */
  public static void installDefaults() {
    String defaultlaf = System.getProperty("swing.defaultlaf");
    if (defaultlaf == null || defaultlaf.length() == 0) {
      try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException ex) {
        // NO-OP
      }
    }
    JFrame.setDefaultLookAndFeelDecorated(true);
    JDialog.setDefaultLookAndFeelDecorated(true);
    UIManager.put(FORMATTED_TEXTFIELD_FONT_KEY,
        UIManager.get(TEXTFIELD_FONT_KEY));
    UIManager.put(FORMATTED_TEXTFIELD_INACTIVE_BACKGROUND_KEY,
        UIManager.get(TEXTFIELD_INACTIVE_BACKGROUND_KEY));
    UIManager.put(PASSWORDFIELD_FONT_KEY, UIManager.get(TEXTFIELD_FONT_KEY));
//    try {
//      UIManager.put(LafWidget.TABBED_PANE_PREVIEW_PAINTER,
//          new DefaultTabPreviewPainter() {
//
//            /**
//             * {@inheritDoc}
//             */
//            @Override
//            public TabOverviewKind getOverviewKind(JTabbedPane tabPane) {
//              return TabOverviewKind.ROUND_CAROUSEL;
//              // return TabOverviewKind.MENU_CAROUSEL;
//            }
//          });
//      UIManager.put(LafWidget.COMPONENT_PREVIEW_PAINTER,
//          new DefaultPreviewPainter());
//      UIManager.put(LafWidget.TEXT_EDIT_CONTEXT_MENU, Boolean.TRUE);
//      // UIManager.put(LafWidget.COMBO_BOX_NO_AUTOCOMPLETION, Boolean.TRUE);
//    } catch (Throwable ignored) {
//      // substance may not be available.
//    }
  }

  /**
   * Tests whether the component passed in parameter is used as an editor.
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
   * Tests whether in swing event dispatch thread. If not, use SwingUtilities to
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
          if (ex.getCause() instanceof RuntimeException) {
            throw (RuntimeException) ex.getCause();
          }
          throw new NestedRuntimeException(ex.getCause());
        }
        // SwingUtilities.invokeLater(runnable);
      }
    }
  }

  private static final class FocusGainedTask implements Runnable {

    private final JTextField textField;

    /**
     * Constructs a new {@code FocusGainedTask} instance.
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
    @Override
    public void run() {
      if (!isUsedAsEditor(textField)) {
        // textField.selectAll();
        if (textField.getDocument() != null) {
          textField.setCaretPosition(textField.getDocument().getLength());
          textField.moveCaretPosition(0);
        }
      }
    }
  }

  private static final class FocusLostTask implements Runnable {

    private final JTextField textField;

    /**
     * Constructs a new {@code FocusLostTask} instance.
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
    @Override
    public void run() {
      if (!isUsedAsEditor(textField)) {
        if (textField.getText().length() > 0) {
          // textField.setCaretPosition(textField.getText().length());
          textField.setCaretPosition(0);
        }
      }
    }
  }

  private static class TfDocumentListener implements DocumentListener {

    private final JTextField textField;

    /**
     * Constructs a new {@code TfDocumentListener} instance.
     *
     * @param textField
     *          the text field to run on.
     */
    protected TfDocumentListener(JTextField textField) {
      this.textField = textField;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changedUpdate(DocumentEvent e) {
      if (!textField.hasFocus()) {
        SwingUtilities.invokeLater(new FocusLostTask(textField));
      }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
      if (!textField.hasFocus()) {
        SwingUtilities.invokeLater(new FocusLostTask(textField));
      }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeUpdate(DocumentEvent e) {
      if (!textField.hasFocus()) {
        SwingUtilities.invokeLater(new FocusLostTask(textField));
      }
    }
  }
}
