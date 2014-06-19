/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RActionList;
import org.jspresso.framework.gui.remote.RComponent;

/**
 * This command initiates the login phase.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RemoteInitLoginCommand extends RemoteCommand {

  private static final long serialVersionUID = 5672752293218754196L;

  private RComponent loginView;
  private RAction[]  loginActions;
  private RAction[]  secondaryLoginActions;

  /**
   * Gets the loginView.
   *
   * @return the loginView.
   */
  public RComponent getLoginView() {
    return loginView;
  }

  /**
   * Sets the loginView.
   *
   * @param loginView
   *          the loginView to set.
   */
  public void setLoginView(RComponent loginView) {
    this.loginView = loginView;
  }

  /**
   * Get login actions.
   *
   * @return the r action [ ]
   */
  public RAction[] getLoginActions() {
    return loginActions;
  }

  /**
   * Sets login actions.
   *
   * @param loginActions the login actions
   */
  public void setLoginActions(RAction[] loginActions) {
    this.loginActions = loginActions;
  }

  /**
   * Get secondary login actions.
   *
   * @return the r action [ ]
   */
  public RAction[] getSecondaryLoginActions() {
    return secondaryLoginActions;
  }

  /**
   * Sets secondary login actions.
   *
   * @param secondaryLoginActions the secondary login actions
   */
  public void setSecondaryLoginActions(RAction[] secondaryLoginActions) {
    this.secondaryLoginActions = secondaryLoginActions;
  }
}
