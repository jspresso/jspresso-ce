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
 * This command is used to download a file to the client peer.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteFileDownloadCommand extends RemoteFileCommand {

  private static final long serialVersionUID = -3785240582390598709L;

  private String            defaultFileName;
  private String            resourceId;

  /**
   * Gets the defaultFileName.
   *
   * @return the defaultFileName.
   */
  public String getDefaultFileName() {
    return defaultFileName;
  }

  /**
   * Gets the resourceId.
   *
   * @return the resourceId.
   */
  public String getResourceId() {
    return resourceId;
  }

  /**
   * Sets the defaultFileName.
   *
   * @param defaultFileName
   *          the defaultFileName to set.
   */
  public void setDefaultFileName(String defaultFileName) {
    this.defaultFileName = defaultFileName;
  }

  /**
   * Sets the resourceId.
   *
   * @param resourceId
   *          the resourceId to set.
   */
  public void setResourceId(String resourceId) {
    this.resourceId = resourceId;
  }
}
