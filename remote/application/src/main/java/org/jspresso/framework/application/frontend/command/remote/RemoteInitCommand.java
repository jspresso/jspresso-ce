/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.gui.remote.RActionList;

/**
 * Displays the application frame on the client peer.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RemoteInitCommand extends RemoteCommand {

  private RActionList[] actions;
  private RActionList[] helpActions;
  private RActionList[] workspaceActions;

  
  /**
   * Gets the actions.
   * 
   * @return the actions.
   */
  public RActionList[] getActions() {
    return actions;
  }

  
  /**
   * Gets the helpActions.
   * 
   * @return the helpActions.
   */
  public RActionList[] getHelpActions() {
    return helpActions;
  }

  /**
   * Gets the workspaceActions.
   * 
   * @return the workspaceActions.
   */
  public RActionList[] getWorkspaceActions() {
    return workspaceActions;
  }

  /**
   * Sets the actions.
   * 
   * @param actions
   *          the actions to set.
   */
  public void setActions(RActionList[] actions) {
    this.actions = actions;
  }

  /**
   * Sets the helpActions.
   * 
   * @param helpActions
   *          the helpActions to set.
   */
  public void setHelpActions(RActionList[] helpActions) {
    this.helpActions = helpActions;
  }

  /**
   * Sets the workspaceActions.
   * 
   * @param workspaceActions the workspaceActions to set.
   */
  public void setWorkspaceActions(RActionList[] workspaceActions) {
    this.workspaceActions = workspaceActions;
  }
}
