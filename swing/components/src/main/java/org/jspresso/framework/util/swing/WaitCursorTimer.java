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
 * @author Kyle Davis
 */
public class WaitCursorTimer extends Thread {

  private final int       delay;
  private Component parent;
  private Object    source;

  /**
   * Constructs a new {@code WaitCursorTimer} instance.
   *
   * @param delay
   *          the delay in milliseconds to wait before setting the hourglass
   *          cursor.
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
    //noinspection InfiniteLoopStatement
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
   *          the source component.
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
      if (parent.isShowing()) {
        if (parent instanceof RootPaneContainer) {
          ((RootPaneContainer) parent).getGlassPane().setCursor(null);
        }
        parent.setCursor(null);
      }
      parent = null;
    }
  }
}
