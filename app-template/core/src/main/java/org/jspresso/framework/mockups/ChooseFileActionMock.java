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
package org.jspresso.framework.mockups;

import java.util.List;
import java.util.Map;

import org.jspresso.framework.application.frontend.action.FrontendAction;

/**
 * A mock for SaveFileAction.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class ChooseFileActionMock<E, F, G> extends FrontendAction<E, F, G> {

  private String                    defaultFileName;
  private Map<String, List<String>> fileFilter;

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
  public Map<String, List<String>> getFileFilter() {
    return fileFilter;
  }

  /**
   * Sets the fileFilter.
   *
   * @param fileFilter
   *          the fileFilter to set.
   */
  public void setFileFilter(Map<String, List<String>> fileFilter) {
    this.fileFilter = fileFilter;
  }

}
