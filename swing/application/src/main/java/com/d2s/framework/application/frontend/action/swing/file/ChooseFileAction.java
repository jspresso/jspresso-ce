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

import com.d2s.framework.application.frontend.action.swing.AbstractSwingAction;
import com.d2s.framework.application.frontend.file.IFileOpenCallback;
import com.d2s.framework.util.i18n.ITranslationProvider;
import com.d2s.framework.view.action.IActionHandler;

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

  private ITranslationProvider      labelTranslator;
  private Map<String, List<String>> fileFilter;
  private IFileOpenCallback         fileOpenCallback;

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, Object> execute(IActionHandler actionHandler) {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle(labelTranslator.getTranslation(getName(),
        getLocale()));
    if (fileFilter != null) {
      for (Map.Entry<String, List<String>> fileTypeEntry : fileFilter
          .entrySet()) {
        StringBuffer extensionsDescription = new StringBuffer("( ");
        for (String fileExtension : fileTypeEntry.getValue()) {
          extensionsDescription.append("*").append(fileExtension).append(" ");
        }
        extensionsDescription.append(" )");
        fileChooser.addChoosableFileFilter(new FileFilterAdapter(fileTypeEntry
            .getValue(), labelTranslator.getTranslation(fileTypeEntry.getKey(),
            getLocale())
            + extensionsDescription.toString()));
      }
    }

    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

    int returnVal = fileChooser.showOpenDialog(SwingUtilities
        .getWindowAncestor(getSourceComponent()));
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = fileChooser.getSelectedFile();
      if (file != null) {
        try {
          fileOpenCallback.fileOpened(new FileInputStream(file), file
              .getAbsolutePath());
        } catch (FileNotFoundException ex) {
          fileOpenCallback.cancel();
        }
      } else {
        fileOpenCallback.cancel();
      }
    } else {
      fileOpenCallback.cancel();
    }
    return super.execute(actionHandler);
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
   * Sets the labelTranslator.
   * 
   * @param labelTranslator
   *          the labelTranslator to set.
   */
  public void setLabelTranslator(ITranslationProvider labelTranslator) {
    this.labelTranslator = labelTranslator;
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
