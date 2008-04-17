/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.ulc.file;

import java.util.List;
import java.util.Map;

import org.jspresso.framework.application.frontend.action.ulc.AbstractUlcAction;

import com.ulcjava.base.shared.FileChooserConfig;

/**
 * Initiates a file choosing action.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class ChooseFileAction extends AbstractUlcAction {

  private String                    defaultFileName;
  private FileChooserConfig         fileChooser;
  private Map<String, List<String>> fileFilter;

  /**
   * Sets the defaultFileName.
   * 
   * @param defaultFileName
   *            the defaultFileName to set.
   */
  public void setDefaultFileName(String defaultFileName) {
    this.defaultFileName = defaultFileName;
  }

  /**
   * Sets the fileFilter. Filter file types are a map of descriptions keying
   * file extension arays.
   * 
   * @param fileFilter
   *            the fileFilter to set.
   */
  public void setFileFilter(Map<String, List<String>> fileFilter) {
    Map<String, List<String>> oldFileFilter = this.fileFilter;
    this.fileFilter = fileFilter;
    if (oldFileFilter != this.fileFilter) {
      fileChooser = null;
    }
  }

  /**
   * Gets the file chooser configuration used to build this file chooser.
   * 
   * @param context
   *            the action context.
   * @return the file chooser configuration.
   */
  protected FileChooserConfig getFileChooser(Map<String, Object> context) {
    if (fileChooser == null) {
      fileChooser = new FileChooserConfig();
      fileChooser.setDialogTitle(getI18nName(getTranslationProvider(context),
          getLocale(context)));
      if (fileFilter != null) {
        for (Map.Entry<String, List<String>> fileTypeEntry : fileFilter
            .entrySet()) {
          StringBuffer extensionsDescription = new StringBuffer(" (");
          String[] allowedExtensions = new String[fileTypeEntry.getValue()
              .size() * 2];
          int i = 0;
          for (String fileExtension : fileTypeEntry.getValue()) {
            extensionsDescription.append("*").append(fileExtension).append(" ");
            allowedExtensions[i++] = fileExtension.toLowerCase();
            allowedExtensions[i++] = fileExtension.toUpperCase();
          }
          extensionsDescription.append(")");
          fileChooser
              .addFileFilterConfig(new FileChooserConfig.FileFilterConfig(
                  allowedExtensions, getTranslationProvider(context)
                      .getTranslation(fileTypeEntry.getKey(),
                          getLocale(context))
                      + extensionsDescription.toString()));
          if (defaultFileName != null) {
            fileChooser.setSelectedFile(defaultFileName);
          }
        }
      }
    }
    return fileChooser;
  }
}
