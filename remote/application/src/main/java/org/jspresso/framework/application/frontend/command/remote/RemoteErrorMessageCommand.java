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
 * This command is used pop-up an error message with potentially detailed trace to the end user.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteErrorMessageCommand extends RemoteMessageCommand {

  private static final long serialVersionUID = 4918227710715366643L;

  private String detailMessage;

  /**
   * Gets detail message.
   *
   * @return the detail message
   */
  public String getDetailMessage() {
    return detailMessage;
  }

  /**
   * Sets detail message.
   *
   * @param detailMessage the detail message
   */
  public void setDetailMessage(String detailMessage) {
    this.detailMessage = detailMessage;
  }
}
