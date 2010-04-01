/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.frontend.action.ulc.file;

import java.util.List;
import java.util.Map;

import org.jspresso.framework.application.frontend.action.ulc.AbstractUlcAction;

import com.ulcjava.base.shared.FileChooserConfig;

/**
 * Initiates a file choosing action.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class ChooseFileAction extends AbstractUlcAction {

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
   * Gets the file chooser configuration used to build this file chooser.
   * 
   * @param context
   *          the action context.
   * @return the file chooser configuration.
   */
  protected FileChooserConfig createFileChooser(Map<String, Object> context) {
    FileChooserConfig fileChooser = new FileChooserConfig();
    fileChooser.setDialogTitle(getI18nName(getTranslationProvider(context),
        getLocale(context)));
    Map<String, List<String>> executionFileFilter = getFileFilter(context);
    if (executionFileFilter != null) {
      for (Map.Entry<String, List<String>> fileTypeEntry : executionFileFilter
          .entrySet()) {
        StringBuffer extensionsDescription = new StringBuffer(" (");
        String[] allowedExtensions = new String[fileTypeEntry.getValue().size() * 2];
        int i = 0;
        for (String fileExtension : fileTypeEntry.getValue()) {
          extensionsDescription.append("*").append(fileExtension).append(" ");
          allowedExtensions[i++] = fileExtension.toLowerCase();
          allowedExtensions[i++] = fileExtension.toUpperCase();
        }
        extensionsDescription.append(")");
        fileChooser.addFileFilterConfig(new FileChooserConfig.FileFilterConfig(
            allowedExtensions, getTranslationProvider(context).getTranslation(
                fileTypeEntry.getKey(), getLocale(context))
                + extensionsDescription.toString()));
        String fileName = getFileName(context);
        if (fileName != null) {
          fileChooser.setSelectedFile(fileName);
        }
      }
    }
    return fileChooser;
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
   * Gets the defaultFileName.
   * 
   * @return the defaultFileName.
   */
  protected String getDefaultFileName() {
    return defaultFileName;
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
