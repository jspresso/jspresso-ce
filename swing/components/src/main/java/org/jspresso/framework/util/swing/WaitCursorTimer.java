/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.swing;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.MenuComponent;
import java.awt.MenuContainer;

import javax.swing.RootPaneContainer;

/**
 * This class source code was submitted at JavaWorld by Kyle Davis. It
 * implements a automated hourglass management.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Kyle Davis
 */
public class WaitCursorTimer extends Thread {

  private int       delay;
  private Component parent;
  private Object    source;

  /**
   * Constructs a new <code>WaitCursorTimer</code> instance.
   * 
   * @param delay
   *            the delay in milliseconds to wait before setting the hourglass
   *            cursor.
   */
  public WaitCursorTimer(int delay) {
    super("WaitCursorTimer");
    this.delay = delay;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void run() {
    while (true) {
      try {
        // wait for notification from startTimer()
        wait();

        // wait for event processing to reach the threshold, or
        // interruption from stopTimer()
        wait(delay);

        if (source instanceof Component) {
          parent = SwingUtil.getWindowOrInternalFrame((Component) source);
        } else if (source instanceof MenuComponent) {
          MenuContainer mParent = ((MenuComponent) source).getParent();
          if (mParent instanceof Component) {
            parent = SwingUtil.getWindowOrInternalFrame((Component) mParent);
          }
        }

        if (parent != null && parent.isShowing()) {
          if (parent instanceof RootPaneContainer) {
            ((RootPaneContainer) parent).getGlassPane().setCursor(
                Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
          }
          // parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        }
      } catch (InterruptedException ie) {
        // just finish.
      }
    }
  }

  /**
   * Starts the timer.
   * 
   * @param sourceComponent
   *            the source component.
   */
  public synchronized void startTimer(Object sourceComponent) {
    this.source = sourceComponent;
    notify();
  }

  /**
   * Stops the timer.
   */
  public synchronized void stopTimer() {
    if (parent == null) {
      interrupt();
    } else {
      if (parent != null && parent.isShowing()) {
        if (parent instanceof RootPaneContainer) {
          ((RootPaneContainer) parent).getGlassPane().setCursor(null);
        }
        parent.setCursor(null);
      }
      parent = null;
    }
  }
}
