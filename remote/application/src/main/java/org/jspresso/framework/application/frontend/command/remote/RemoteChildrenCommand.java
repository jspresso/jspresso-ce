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
package org.jspresso.framework.application.frontend.command.remote;

import java.util.List;

import org.jspresso.framework.state.remote.RemoteValueState;

/**
 * A command to update the children of a remote peer.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteChildrenCommand extends RemoteCommand {

  private static final long      serialVersionUID = 5963387363740451053L;

  private List<RemoteValueState> children;
  private boolean                remove;

  /**
   * Gets the children.
   *
   * @return the children.
   */
  public List<RemoteValueState> getChildren() {
    return children;
  }

  /**
   * Sets the children.
   *
   * @param children
   *          the children to set.
   */
  public void setChildren(List<RemoteValueState> children) {
    this.children = children;
  }

  /**
   * Gets the remove.
   *
   * @return the remove.
   */
  public boolean isRemove() {
    return remove;
  }

  /**
   * Sets the remove.
   *
   * @param remove the remove to set.
   */
  public void setRemove(boolean remove) {
    this.remove = remove;
  }

}
