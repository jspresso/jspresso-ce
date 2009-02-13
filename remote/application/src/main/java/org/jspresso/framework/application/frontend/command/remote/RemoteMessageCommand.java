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

import org.jspresso.framework.gui.remote.RIcon;

/**
 * This command is used pop-up a message to the end user.
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
public class RemoteMessageCommand extends RemoteCommand {

  private String title;
  private String message;
  private RIcon  titleIcon;
  private RIcon  messageIcon;

  /**
   * Gets the title.
   * 
   * @return the title.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title.
   * 
   * @param title
   *          the title to set.
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Gets the message.
   * 
   * @return the message.
   */
  public String getMessage() {
    return message;
  }

  /**
   * Sets the message.
   * 
   * @param message
   *          the message to set.
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Gets the titleIcon.
   * 
   * @return the titleIcon.
   */
  public RIcon getTitleIcon() {
    return titleIcon;
  }

  /**
   * Sets the titleIcon.
   * 
   * @param titleIcon
   *          the titleIcon to set.
   */
  public void setTitleIcon(RIcon titleIcon) {
    this.titleIcon = titleIcon;
  }

  /**
   * Gets the messageIcon.
   * 
   * @return the messageIcon.
   */
  public RIcon getMessageIcon() {
    return messageIcon;
  }

  /**
   * Sets the messageIcon.
   * 
   * @param messageIcon
   *          the messageIcon to set.
   */
  public void setMessageIcon(RIcon messageIcon) {
    this.messageIcon = messageIcon;
  }
}
