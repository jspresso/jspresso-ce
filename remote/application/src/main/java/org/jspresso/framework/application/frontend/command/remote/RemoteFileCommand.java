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

import java.util.Map;

import org.jspresso.framework.gui.remote.RAction;

/**
 * This command is the base class for file commands.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteFileCommand extends RemoteCommand {

  private static final long     serialVersionUID = -3016618436886031446L;

  private RAction               cancelCallbackAction;
  private Map<String, String[]> fileFilter;
  private String                fileUrl;

  /**
   * Gets the cancelCallbackAction.
   *
   * @return the cancelCallbackAction.
   */
  public RAction getCancelCallbackAction() {
    return cancelCallbackAction;
  }

  /**
   * Gets the fileFilter.
   *
   * @return the fileFilter.
   */
  public Map<String, String[]> getFileFilter() {
    return fileFilter;
  }

  /**
   * Gets the fileUrl.
   *
   * @return the fileUrl.
   */
  public String getFileUrl() {
    return fileUrl;
  }

  /**
   * Sets the cancelCallbackAction.
   *
   * @param cancelCallbackAction
   *          the cancelCallbackAction to set.
   */
  public void setCancelCallbackAction(RAction cancelCallbackAction) {
    this.cancelCallbackAction = cancelCallbackAction;
  }

  /**
   * Sets the fileFilter.
   *
   * @param fileFilter
   *          the fileFilter to set.
   */
  public void setFileFilter(Map<String, String[]> fileFilter) {
    this.fileFilter = fileFilter;
  }

  /**
   * Sets the fileUrl.
   *
   * @param fileUrl
   *          the fileUrl to set.
   */
  public void setFileUrl(String fileUrl) {
    this.fileUrl = fileUrl;
  }

}
