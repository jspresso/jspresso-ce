/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.swing.file;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.d2s.framework.application.frontend.action.swing.AbstractSwingAction;

/**
 * Initiates a file choosing action.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ChooseFileAction extends AbstractSwingAction {

  private Map<String, List<String>> fileFilter;
  private JFileChooser              fileChooser;
  private String                    defaultFileName;

  /**
   * Gets the file chooser.
   * 
   * @param context
   *          the action context.
   * @return the file chooser.
   */
  protected JFileChooser getFileChooser(Map<String, Object> context) {
    if (fileChooser == null) {
      fileChooser = new JFileChooser();
      fileChooser.setDialogTitle(getI18nName(getTranslationProvider(context),
          getLocale(context)));
      if (fileFilter != null) {
        for (Map.Entry<String, List<String>> fileTypeEntry : fileFilter
            .entrySet()) {
          StringBuffer extensionsDescription = new StringBuffer("( ");
          for (String fileExtension : fileTypeEntry.getValue()) {
            extensionsDescription.append("*").append(fileExtension).append(" ");
          }
          extensionsDescription.append(" )");
          fileChooser.addChoosableFileFilter(new FileFilterAdapter(
              fileTypeEntry.getValue(), getTranslationProvider(context)
                  .getTranslation(fileTypeEntry.getKey(), getLocale(context))
                  + extensionsDescription.toString()));
        }
      }
      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      if (defaultFileName != null) {
        fileChooser.setSelectedFile(new File(defaultFileName));
      }
    }
    return fileChooser;
  }

  /**
   * Sets the fileFilter. Filter file types are a map of descriptions keying
   * file extension arays.
   * 
   * @param fileFilter
   *          the fileFilter to set.
   */
  public void setFileFilter(Map<String, List<String>> fileFilter) {
    Map<String, List<String>> oldFileFilter = this.fileFilter;
    this.fileFilter = fileFilter;
    if (oldFileFilter != this.fileFilter) {
      fileChooser = null;
    }
  }

  private static class FileFilterAdapter extends FileFilter {

    private String             description;
    private Collection<String> allowedExtensions;

    /**
     * Constructs a new <code>FileFilterAdapter</code> instance.
     * 
     * @param description
     * @param allowedExtensions
     */
    public FileFilterAdapter(Collection<String> allowedExtensions,
        String description) {
      this.allowedExtensions = new HashSet<String>(allowedExtensions);
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

  /**
   * Sets the defaultFileName.
   * 
   * @param defaultFileName
   *          the defaultFileName to set.
   */
  public void setDefaultFileName(String defaultFileName) {
    this.defaultFileName = defaultFileName;
  }
}
