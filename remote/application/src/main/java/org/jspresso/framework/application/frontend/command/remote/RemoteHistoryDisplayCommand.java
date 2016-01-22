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

/**
 * A command to exchange history navigation on both sides.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteHistoryDisplayCommand extends RemoteCommand {

  private static final long serialVersionUID = 961045267213497189L;

  private String            snapshotId;
  private String            name;

  /**
   * Gets the snapshotId.
   *
   * @return the snapshotId.
   */
  public String getSnapshotId() {
    return snapshotId;
  }

  /**
   * Sets the snapshotId.
   *
   * @param snapshotId
   *          the snapshotId to set.
   */
  public void setSnapshotId(String snapshotId) {
    this.snapshotId = snapshotId;
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
   * Sets the name.
   *
   * @param name
   *          the name to set.
   */
  public void setName(String name) {
    this.name = name;
  }
}
