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
 * A command to update the remote application frame status bar.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteUpdateStatusCommand extends RemoteCommand {

  private static final long serialVersionUID = -5258269933081414873L;

  private String            status;

  /**
   * Sets the status.
   *
   * @param status
   *          the status to set.
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Gets the status.
   *
   * @return the status.
   */
  public String getStatus() {
    return status;
  }

}
