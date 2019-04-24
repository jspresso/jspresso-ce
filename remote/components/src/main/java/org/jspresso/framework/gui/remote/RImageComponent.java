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
package org.jspresso.framework.gui.remote;

/**
 * A remote image component.
 *
 * @author Vincent Vandenschrick
 */
public class RImageComponent extends RComponent implements RActionable {

  private static final long serialVersionUID = 7583953076670140848L;

  private boolean scrollable;
  private boolean keepRatio;
  private RAction action;
  private String  horizontalAlignment;


  /**
   * Constructs a new {@code RImageComponent} instance.
   *
   * @param guid
   *     the guid.
   */
  public RImageComponent(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RImageComponent} instance. Only used for
   * serialization support.
   */
  public RImageComponent() {
    // For serialization support
  }

  /**
   * Gets the scrollable.
   *
   * @return the scrollable.
   */
  public boolean isScrollable() {
    return scrollable;
  }

  /**
   * Sets the scrollable.
   *
   * @param scrollable
   *     the scrollable to set.
   */
  public void setScrollable(boolean scrollable) {
    this.scrollable = scrollable;
  }

  /**
   * Gets the action.
   *
   * @return the action.
   */
  public RAction getAction() {
    return action;
  }

  /**
   * Sets the action.
   *
   * @param action
   *     the action to set.
   */
  @Override
  public void setAction(RAction action) {
    this.action = action;
  }

  /**
   * Gets horizontal alignment.
   *
   * @return the horizontal alignment
   */
  public String getHorizontalAlignment() {
    return horizontalAlignment;
  }

  /**
   * Sets horizontal alignment.
   *
   * @param horizontalAlignment
   *     the horizontal alignment
   */
  public void setHorizontalAlignment(String horizontalAlignment) {
    this.horizontalAlignment = horizontalAlignment;
  }

  /**
   * Is keep ratio boolean.
   *
   * @return the boolean
   */
  public boolean isKeepRatio() {
    return keepRatio;
  }

  /**
   * Sets keep ratio.
   *
   * @param keepRatio
   *     the keep ratio
   */
  public void setKeepRatio(boolean keepRatio) {
    this.keepRatio = keepRatio;
  }
}
