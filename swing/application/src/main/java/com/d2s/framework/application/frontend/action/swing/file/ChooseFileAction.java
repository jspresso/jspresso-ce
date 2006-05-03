/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.swing.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.swing.AbstractSwingAction;
import com.d2s.framework.application.frontend.file.IFileOpenCallback;

/**
 * Initiates a file choosing action. Then the file content is passed as a byte
 * array to the next action through its context entry ACTION_RESULT.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ChooseFileAction extends AbstractSwingAction {

  private Map<String, List<String>> fileFilter;
  private IFileOpenCallback         fileOpenCallback;
  private JFileChooser              fileChooser;

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute(IActionHandler actionHandler, Map<String, Object> context) {

    JFileChooser currentFileChooser = getFileChooser(context);

    int returnVal = currentFileChooser.showOpenDialog(SwingUtilities
        .getWindowAncestor(getSourceComponent(context)));
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = currentFileChooser.getSelectedFile();
      if (file != null) {
        try {
          fileOpenCallback.fileOpened(new FileInputStream(file), file
              .getAbsolutePath(), context);
        } catch (FileNotFoundException ex) {
          fileOpenCallback.cancel(context);
        }
      } else {
        fileOpenCallback.cancel(context);
      }
    } else {
      fileOpenCallback.cancel(context);
    }
    super.execute(actionHandler, context);
  }

  private JFileChooser getFileChooser(Map<String, Object> context) {
    if (fileChooser == null) {
      fileChooser = new JFileChooser();
      fileChooser.setDialogTitle(getI18nName(getTranslationProvider(context), getLocale(context)));
      if (fileFilter != null) {
        for (Map.Entry<String, List<String>> fileTypeEntry : fileFilter
            .entrySet()) {
          StringBuffer extensionsDescription = new StringBuffer("( ");
          for (String fileExtension : fileTypeEntry.getValue()) {
            extensionsDescription.append("*").append(fileExtension).append(" ");
          }
          extensionsDescription.append(" )");
          fileChooser.addChoosableFileFilter(new FileFilterAdapter(
              fileTypeEntry.getValue(), getTranslationProvider(context).getTranslation(
                  fileTypeEntry.getKey(), getLocale(context))
                  + extensionsDescription.toString()));
        }
      }
      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
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

  /**
   * Sets the fileOpenCallback.
   * 
   * @param fileOpenCallback
   *          the fileOpenCallback to set.
   */
  public void setFileOpenCallback(IFileOpenCallback fileOpenCallback) {
    this.fileOpenCallback = fileOpenCallback;
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
}
