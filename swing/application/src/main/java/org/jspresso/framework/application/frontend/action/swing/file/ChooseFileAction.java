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
package org.jspresso.framework.application.frontend.action.swing.file;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.jspresso.framework.application.frontend.action.swing.AbstractSwingAction;
import org.jspresso.framework.application.frontend.file.IFileCallback;

/**
 * Initiates a file choosing action.
 *
 * @author Vincent Vandenschrick
 */
public abstract class ChooseFileAction extends AbstractSwingAction {

  /**
   * {@code FILE_CALLBACK} is "FILE_CALLBACK".
   */
  public static final String FILE_CALLBACK = "FILE_CALLBACK";

  private String                    defaultFileName;
  private Map<String, List<String>> fileFilter;
  private IFileCallback             fileCallback;

  /**
   * Sets the defaultFileName.
   *
   * @param defaultFileName
   *     the defaultFileName to set.
   */
  public void setDefaultFileName(String defaultFileName) {
    this.defaultFileName = defaultFileName;
  }

  /**
   * Sets the fileFilter. Filter file types are a map of descriptions keying file extension arrays.
   *
   * @param fileFilter
   *     the fileFilter to set.
   */
  public void setFileFilter(Map<String, List<String>> fileFilter) {
    this.fileFilter = fileFilter;
  }

  /**
   * Gets the file chooser.
   *
   * @param context
   *     the action context.
   * @return the file chooser.
   */
  protected JFileChooser createFileChooser(Map<String, Object> context) {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle(getI18nName(getTranslationProvider(context),
        getLocale(context)));
    Map<String, List<String>> executionFileFilter = getFileFilter(context);
    if (executionFileFilter != null) {
      for (Map.Entry<String, List<String>> fileTypeEntry : executionFileFilter
          .entrySet()) {
        StringBuilder extensionsDescription = new StringBuilder(" (");
        for (String fileExtension : fileTypeEntry.getValue()) {
          extensionsDescription.append("*").append(fileExtension).append(" ");
        }
        extensionsDescription.append(")");
        fileChooser.addChoosableFileFilter(new FileFilterAdapter(fileTypeEntry
            .getValue(), getTranslationProvider(context).getTranslation(
            fileTypeEntry.getKey(), getLocale(context))
            + extensionsDescription.toString()));
      }
    }
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    String fileName = getFileName(context);
    if (fileName != null) {
      fileChooser.setSelectedFile(new File(fileName));
    }
    return fileChooser;
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
   *     the action context.
   * @return the fileFilter.
   */
  protected Map<String, List<String>> getFileFilter(Map<String, Object> context) {
    return fileFilter;
  }

  /**
   * Computes a file name to save the file. Defaults to the action default file name parametrized in the action.
   *
   * @param context
   *     the action context.
   * @return the file name to save the file under.
   */
  protected String getFileName(Map<String, Object> context) {
    return getDefaultFileName();
  }

  /**
   * Sets the file callback.
   *
   * @param fileCallback
   *     the file callback.
   */
  protected void setFileCallback(IFileCallback fileCallback) {
    this.fileCallback = fileCallback;
  }

  /**
   * Gets the fileCallback.
   *
   * @param context
   *     the action context.
   * @return the fileCallback.
   */
  protected IFileCallback getFileCallback(Map<String, Object> context) {
    IFileCallback callback = (IFileCallback) context.get(FILE_CALLBACK);
    if (callback == null) {
      callback = fileCallback;
    }
    return callback;
  }

  private static class FileFilterAdapter extends FileFilter {

    private final Collection<String> allowedExtensions;
    private final String             description;

    /**
     * Constructs a new {@code FileFilterAdapter} instance.
     *
     * @param description
     *     the filter description.
     * @param allowedExtensions
     *     the filter allowed extensions.
     */
    public FileFilterAdapter(Collection<String> allowedExtensions,
        String description) {
      this.allowedExtensions = new HashSet<>(allowedExtensions);
      this.description = description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accept(File f) {
      if (f.isDirectory()) {
        return true;
      }
      String ext = null;
      String s = f.getName();
      int i = s.lastIndexOf('.');
      if (i > 0 && i < s.length() - 1) {
        ext = s.substring(i).toLowerCase();
      }
      return ext != null && allowedExtensions.contains(ext.toLowerCase());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
      return description;
    }

  }
}
