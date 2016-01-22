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

import org.jspresso.framework.util.remote.RemotePeer;

/**
 * This class is the generic server peer of a client GUI action list.
 *
 * @author Vincent Vandenschrick
 */
public class RActionList extends RemotePeer {

  private static final long serialVersionUID = 2147520367941975885L;

  private RAction[]         actions;
  private String            description;
  private RIcon             icon;
  private String            name;
  private boolean           collapsable;

  /**
   * Constructs a new {@code RActionList} instance.
   *
   * @param guid
   *          the guid.
   */
  public RActionList(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RActionList} instance. Only used for
   * serialization support.
   */
  public RActionList() {
    // For serialization support
  }

  /**
   * Gets the actions.
   *
   * @return the actions.
   */
  public RAction[] getActions() {
    return actions;
  }

  /**
   * Gets the description.
   *
   * @return the description.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Gets the icon.
   *
   * @return the icon.
   */
  public RIcon getIcon() {
    return icon;
  }

  /**
   * Gets the name.
   *
   * @return the name.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the actions.
   *
   * @param actions
   *          the actions to set.
   */
  public void setActions(RAction... actions) {
    this.actions = actions;
  }

  /**
   * Sets the description.
   *
   * @param description
   *          the description to set.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Sets the icon.
   *
   * @param icon
   *          the icon to set.
   */
  public void setIcon(RIcon icon) {
    this.icon = icon;
  }

  /**
   * Sets the name.
   *
   * @param name
   *          the name to set.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the collapsable.
   *
   * @return the collapsable.
   */
  public boolean isCollapsable() {
    return collapsable;
  }

  /**
   * Sets the collapsable.
   *
   * @param collapsable
   *          the collapsable to set.
   */
  public void setCollapsable(boolean collapsable) {
    this.collapsable = collapsable;
  }
}
