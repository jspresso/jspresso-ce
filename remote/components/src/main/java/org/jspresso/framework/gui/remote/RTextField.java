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
 * A remote text field component.
 *
 * @author Vincent Vandenschrick
 */
public class RTextField extends RTextComponent {

  private static final long serialVersionUID = -7482939529748984634L;
  private String            horizontalAlignment;
  private RAction           characterAction;

  /**
   * Constructs a new {@code RTextField} instance.
   *
   * @param guid
   *          the guid.
   */
  public RTextField(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RTextField} instance. Only used for
   * serialization support.
   */
  public RTextField() {
    // For serialization support
  }

  /**
   * Sets the horizontalAlignment.
   *
   * @param horizontalAlignment
   *          the horizontalAlignment to set.
   */
  public void setHorizontalAlignment(String horizontalAlignment) {
    this.horizontalAlignment = horizontalAlignment;
  }

  /**
   * Gets the horizontalAlignment.
   *
   * @return the horizontalAlignment.
   */
  public String getHorizontalAlignment() {
    return horizontalAlignment;
  }

  /**
   * Gets character action.
   *
   * @return the character action
   */
  public RAction getCharacterAction() {
    return characterAction;
  }

  /**
   * Sets character action.
   *
   * @param characterAction the character action
   */
  public void setCharacterAction(RAction characterAction) {
    this.characterAction = characterAction;
  }
}
