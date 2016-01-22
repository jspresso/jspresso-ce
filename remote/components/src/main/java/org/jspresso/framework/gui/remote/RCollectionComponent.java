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
 * A collection component.
 *
 * @author Vincent Vandenschrick
 */
public class RCollectionComponent extends RComponent {

  private static final long serialVersionUID = -5154116400205068483L;

  private RAction           rowAction;
  private String            selectionMode;

  /**
   * Constructs a new {@code RCollectionComponent} instance.
   *
   * @param guid
   *          the guid
   */
  public RCollectionComponent(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RCollectionComponent} instance. Only used for
   * GWT serialization support.
   */
  public RCollectionComponent() {
    // For serialization support
  }

  /**
   * Gets the rowAction.
   *
   * @return the rowAction.
   */
  public RAction getRowAction() {
    return rowAction;
  }

  /**
   * Gets the selectionMode.
   *
   * @return the selectionMode.
   */
  public String getSelectionMode() {
    return selectionMode;
  }

  /**
   * Sets the rowAction.
   *
   * @param rowAction
   *          the rowAction to set.
   */
  public void setRowAction(RAction rowAction) {
    this.rowAction = rowAction;
  }

  /**
   * Sets the selectionMode.
   *
   * @param selectionMode
   *          the selectionMode to set.
   */
  public void setSelectionMode(String selectionMode) {
    this.selectionMode = selectionMode;
  }
}
