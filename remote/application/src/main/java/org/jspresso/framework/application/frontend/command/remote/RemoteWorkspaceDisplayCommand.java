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

import org.jspresso.framework.gui.remote.RComponent;

/**
 * A command to trigger workspace display on the remote peer.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteWorkspaceDisplayCommand extends RemoteCommand {

  private static final long serialVersionUID = -3110747731728731120L;

  private String            workspaceName;
  private RComponent        workspaceView;

  /**
   * Gets the workspaceName.
   *
   * @return the workspaceName.
   */
  public String getWorkspaceName() {
    return workspaceName;
  }

  /**
   * Gets the workspaceView.
   *
   * @return the workspaceView.
   */
  public RComponent getWorkspaceView() {
    return workspaceView;
  }

  /**
   * Sets the workspaceName.
   *
   * @param workspaceName
   *          the workspaceName to set.
   */
  public void setWorkspaceName(String workspaceName) {
    this.workspaceName = workspaceName;
  }

  /**
   * Sets the workspaceView.
   *
   * @param workspaceView
   *          the workspaceView to set.
   */
  public void setWorkspaceView(RComponent workspaceView) {
    this.workspaceView = workspaceView;
  }

}
