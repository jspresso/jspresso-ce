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

import java.awt.AWTEvent;
import java.awt.EventQueue;

/**
 * This class source code was submitted at JavaWorld by Kyle Davis. It
 * implements a automated hourglass management.
 * <p>
 *
 * @author Kyle Davis
 */
public class WaitCursorEventQueue extends EventQueue {

  private final WaitCursorTimer waitTimer;

  /**
   * Constructs a new {@code WaitCursorEventQueue} instance.
   *
   * @param delay
   *          the time in milliseconds the event queue will wait before
   *          installing the hourglass cursor.
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
