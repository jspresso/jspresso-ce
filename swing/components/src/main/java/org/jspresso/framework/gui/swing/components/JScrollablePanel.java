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
package org.jspresso.framework.gui.swing.components;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.Scrollable;

/**
 * Scrollable panel.
 *
 * @author Vincent Vandenschrick
 */
public class JScrollablePanel extends JPanel implements Scrollable {

  private static final long serialVersionUID = 8374827247203629308L;

  private boolean           scrollableTracksViewportHeight;
  private boolean           scrollableTracksViewportWidth;

  /**
   * {@inheritDoc}
   */
  @Override
  public Dimension getPreferredScrollableViewportSize() {
    return getPreferredSize();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation,
      int direction) {
    return 10;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getScrollableBlockIncrement(Rectangle visibleRect,
      int orientation, int direction) {
    return 50;
  }

  /**
   * Gets the scrollableTracksViewportHeight.
   *
   * @return the scrollableTracksViewportHeight.
   */
  @Override
  public boolean getScrollableTracksViewportHeight() {
    return scrollableTracksViewportHeight;
  }

  /**
   * Sets the scrollableTracksViewportHeight.
   *
   * @param scrollableTracksViewportHeight
   *          the scrollableTracksViewportHeight to set.
   */
  public void setScrollableTracksViewportHeight(
      boolean scrollableTracksViewportHeight) {
    this.scrollableTracksViewportHeight = scrollableTracksViewportHeight;
  }

  /**
   * Gets the scrollableTracksViewportWidth.
   *
   * @return the scrollableTracksViewportWidth.
   */
  @Override
  public boolean getScrollableTracksViewportWidth() {
    return scrollableTracksViewportWidth;
  }

  /**
   * Sets the scrollableTracksViewportWidth.
   *
   * @param scrollableTracksViewportWidth
   *          the scrollableTracksViewportWidth to set.
   */
  public void setScrollableTracksViewportWidth(
      boolean scrollableTracksViewportWidth) {
    this.scrollableTracksViewportWidth = scrollableTracksViewportWidth;
  }
}
