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
 * OK - Cancel remote flow action.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteOkCancelCommand extends RemoteMessageCommand {

  private static final long serialVersionUID = -2288892078616905225L;

  private RAction           cancelAction;
  private RAction           okAction;

  /**
   * Gets the cancelAction.
   *
   * @return the cancelAction.
   */
  public RAction getCancelAction() {
    return cancelAction;
  }

  /**
   * Gets the okAction.
   *
   * @return the okAction.
   */
  public RAction getOkAction() {
    return okAction;
  }

  /**
   * Sets the cancelAction.
   *
   * @param cancelAction
   *          the cancelAction to set.
   */
  public void setCancelAction(RAction cancelAction) {
    this.cancelAction = cancelAction;
  }

  /**
   * Sets the okAction.
   *
   * @param okAction
   *          the okAction to set.
   */
  public void setOkAction(RAction okAction) {
    this.okAction = okAction;
  }

}
