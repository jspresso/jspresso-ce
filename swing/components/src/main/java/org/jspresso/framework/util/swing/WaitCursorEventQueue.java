/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.swing;

import java.awt.AWTEvent;
import java.awt.EventQueue;

/**
 * This class source code was submitted at JavaWorld by Kyle Davis. It
 * implements a automated hourglass management.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Kyle Davis
 */
public class WaitCursorEventQueue extends EventQueue {

  private WaitCursorTimer waitTimer;

  /**
   * Constructs a new <code>WaitCursorEventQueue</code> instance.
   * 
   * @param delay
   *            the time in milliseconds the event queue will wait before
   *            installing the hourglass cursor.
   */
  public WaitCursorEventQueue(int delay) {
    waitTimer = new WaitCursorTimer(delay);
    waitTimer.setDaemon(true);
    waitTimer.start();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void dispatchEvent(AWTEvent event) {
    waitTimer.startTimer(event.getSource());
    try {
      super.dispatchEvent(event);
    } finally {
      waitTimer.stopTimer();
    }
  }
}
