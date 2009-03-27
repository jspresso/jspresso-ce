/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.frontend.action.remote.file;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.application.frontend.action.remote.AbstractRemoteAction;
import org.jspresso.framework.application.frontend.file.IFileCallback;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * Initiates a file choosing action.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
public class ChooseFileAction extends AbstractRemoteAction {

  private String                    defaultFileName;
  private FileCancelCallbackAction  fileCancelCallbackAction;
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
   * Creates a cancel callback action.
   * 
   * @param fileCallback
   *          the file callback to cancel.
   * @return the cancel callback action.
   */
  protected IDisplayableAction createCancelCallbackAction(
      IFileCallback fileCallback) {
    if (fileCancelCallbackAction == null) {
      fileCancelCallbackAction = new FileCancelCallbackAction(fileCallback);
    }
    return fileCancelCallbackAction;
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
   * @param context
   *          the action context.
   * @return the fileFilter.
   */
  protected Map<String, List<String>> getFileFilter(Map<String, Object> context) {
    return fileFilter;
  }

  /**
   * Translates the file filter for usage in remote commands.
   * 
   * @param executionFileFilter
   *          the file filter to translate.
   * @param context
   *          the action context.
   * @return the translated file filter.
   */
  protected Map<String, String[]> translateFilter(
      Map<String, List<String>> executionFileFilter, Map<String, Object> context) {
    if (executionFileFilter == null) {
      return null;
    }
    Map<String, String[]> translatedFileFilter = new HashMap<String, String[]>();
    for (Map.Entry<String, List<String>> filterEntry : executionFileFilter
        .entrySet()) {
      translatedFileFilter.put(getTranslationProvider(context).getTranslation(
          filterEntry.getKey(), getLocale(context)), filterEntry.getValue()
          .toArray(new String[0]));
    }
    return translatedFileFilter;
  }
}
