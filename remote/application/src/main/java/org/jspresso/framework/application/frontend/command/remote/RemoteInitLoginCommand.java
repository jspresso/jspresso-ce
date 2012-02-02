/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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
import org.jspresso.framework.gui.remote.RIcon;

/**
 * This command initiates the login phase.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RemoteInitLoginCommand extends RemoteCommand {

  private static final long serialVersionUID = 5672752293218754196L;

  private RComponent        loginView;
  private String            message;
  private RIcon             okIcon;
  private String            okLabel;
  private String            title;

  /**
   * Gets the loginView.
   * 
   * @return the loginView.
   */
  public RComponent getLoginView() {
    return loginView;
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
   * Gets the okIcon.
   * 
   * @return the okIcon.
   */
  public RIcon getOkIcon() {
    return okIcon;
  }

  /**
   * Gets the okLabel.
   * 
   * @return the okLabel.
   */
  public String getOkLabel() {
    return okLabel;
  }

  /**
   * Gets the title.
   * 
   * @return the title.
   */
  public String getTitle() {
    return title;
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
   * Sets the message.
   * 
   * @param message
   *          the message to set.
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Sets the okIcon.
   * 
   * @param okIcon
   *          the okIcon to set.
   */
  public void setOkIcon(RIcon okIcon) {
    this.okIcon = okIcon;
  }

  /**
   * Sets the okLabel.
   * 
   * @param okLabel
   *          the okLabel to set.
   */
  public void setOkLabel(String okLabel) {
    this.okLabel = okLabel;
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
}
