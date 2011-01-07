/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.frontend.action.wings.file;

import java.util.List;
import java.util.Map;

import org.jspresso.framework.application.frontend.action.wings.AbstractWingsAction;

/**
 * Initiates a file choosing action.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class ChooseFileAction extends AbstractWingsAction {

  private String                    defaultFileName;
  private Map<String, List<String>> fileFilter;

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
   * Sets the fileFilter. Filter file types are a map of descriptions keying
   * file extension arays.
   * 
   * @param fileFilter
   *          the fileFilter to set.
   */
  public void setFileFilter(Map<String, List<String>> fileFilter) {
    this.fileFilter = fileFilter;
  }

  /**
   * Gets the defaultFileName.
   * 
   * @return the defaultFileName.
   */
  protected String getDefaultFileName() {
    return defaultFileName;
  }

  /**
   * Gets the fileFilter.
   * 
   * @return the fileFilter.
   */
  protected Map<String, List<String>> getFileFilter() {
    return fileFilter;
  }

  /**
   * Computes a file name to save the file. Defaults to the action default file
   * name parameterized in the action.
   * 
   * @param context
   *          the action context.
   * @return the file name to save the file under.
   */
  protected String getFileName(Map<String, Object> context) {
    return getDefaultFileName();
  }
}
