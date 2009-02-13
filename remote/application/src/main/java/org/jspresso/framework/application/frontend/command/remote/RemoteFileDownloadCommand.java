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

import java.util.Map;

/**
 * This command is used to download a file to the client peer.
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
public class RemoteFileDownloadCommand extends RemoteCommand {

  private String                defaultFileName;
  private Map<String, String[]> fileFilter;
  private String                downloadUrl;
  private String                resourceId;

  /**
   * Gets the defaultFileName.
   * 
   * @return the defaultFileName.
   */
  public String getDefaultFileName() {
    return defaultFileName;
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
   * Gets the fileFilter.
   * 
   * @return the fileFilter.
   */
  public Map<String, String[]> getFileFilter() {
    return fileFilter;
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
   * Gets the downloadUrl.
   * 
   * @return the downloadUrl.
   */
  public String getDownloadUrl() {
    return downloadUrl;
  }

  
  /**
   * Sets the downloadUrl.
   * 
   * @param downloadUrl the downloadUrl to set.
   */
  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
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
   * Sets the resourceId.
   * 
   * @param resourceId the resourceId to set.
   */
  public void setResourceId(String resourceId) {
    this.resourceId = resourceId;
  }

}
