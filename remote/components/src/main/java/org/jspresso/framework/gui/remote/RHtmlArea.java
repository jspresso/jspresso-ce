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
 * A remote html area component.
 *
 * @author Vincent Vandenschrick
 */
public class RHtmlArea extends RTextComponent {

  private static final long serialVersionUID = 5482012658466216934L;

  private boolean           readOnly;
  private boolean           verticallyScrollable;
  private boolean           horizontallyScrollable;

  /**
   * Constructs a new {@code RHtmlArea} instance.
   *
   * @param guid
   *          the guid.
   */
  public RHtmlArea(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RHtmlArea} instance. Only used for
   * serialization support.
   */
  public RHtmlArea() {
    // For serialization support
  }

  /**
   * Gets the readOnly.
   *
   * @return the readOnly.
   */
  public boolean isReadOnly() {
    return readOnly;
  }

  /**
   * Sets the readOnly.
   *
   * @param readOnly
   *          the readOnly to set.
   */
  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
  }

  /**
   * Gets the verticallyScrollable.
   *
   * @return the verticallyScrollable.
   */
  public boolean isVerticallyScrollable() {
    return verticallyScrollable;
  }

  /**
   * Sets the verticallyScrollable.
   *
   * @param verticallyScrollable
   *          the verticallyScrollable to set.
   */
  public void setVerticallyScrollable(boolean verticallyScrollable) {
    this.verticallyScrollable = verticallyScrollable;
  }

  /**
   * Gets the horizontallyScrollable.
   *
   * @return the horizontallyScrollable.
   */
  public boolean isHorizontallyScrollable() {
    return horizontallyScrollable;
  }

  /**
   * Sets the horizontallyScrollable.
   *
   * @param horizontallyScrollable
   *          the horizontallyScrollable to set.
   */
  public void setHorizontallyScrollable(boolean horizontallyScrollable) {
    this.horizontallyScrollable = horizontallyScrollable;
  }
}
