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

import org.jspresso.framework.gui.remote.RAction;

/**
 * Yes - No remote flow action.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteYesNoCommand extends RemoteMessageCommand {

  private static final long serialVersionUID = -4902691344620205704L;

  private RAction           noAction;
  private RAction           yesAction;

  /**
   * Gets the noAction.
   *
   * @return the noAction.
   */
  public RAction getNoAction() {
    return noAction;
  }

  /**
   * Gets the yesAction.
   *
   * @return the yesAction.
   */
  public RAction getYesAction() {
    return yesAction;
  }

  /**
   * Sets the noAction.
   *
   * @param noAction
   *          the noAction to set.
   */
  public void setNoAction(RAction noAction) {
    this.noAction = noAction;
  }

  /**
   * Sets the yesAction.
   *
   * @param yesAction
   *          the yesAction to set.
   */
  public void setYesAction(RAction yesAction) {
    this.yesAction = yesAction;
  }

}
