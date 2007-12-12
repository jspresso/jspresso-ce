/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.swing.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.file.IFileSaveCallback;
import com.d2s.framework.util.i18n.Messages;
import com.d2s.framework.util.swing.SwingUtil;

/**
 * Initiates a file save action.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SaveFileAction extends ChooseFileAction {

  private IFileSaveCallback fileSaveCallback;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {

    JFileChooser currentFileChooser = getFileChooser(context);

    int returnVal = currentFileChooser.showSaveDialog(SwingUtil
        .getVisibleWindow(getSourceComponent(context)));
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = currentFileChooser.getSelectedFile();
      if (file != null) {
        if (file.getName() != null && file.getName().indexOf(".") == -1) { //$NON-NLS-1$
          Map<String, List<String>> fileFilter = getFileFilter();
          if (fileFilter != null && !fileFilter.isEmpty()) {
            List<String> extensions = fileFilter.values().iterator().next();
            if (extensions != null && !extensions.isEmpty()) {
              file = new File(file.getAbsolutePath() + extensions.get(0));
            }
          }
        }
        if (file.exists()) {
          if (JOptionPane.showConfirmDialog(null, Messages
              .getString("confirm.override.description"), //$NON-NLS-1$
              Messages.getString("confirm.override.name"), //$NON-NLS-1$
              JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
            file = null;
          }
        }
        if (file != null) {
          try {
            fileSaveCallback.fileChosen(new FileOutputStream(file), context);
            fileSaveCallback.fileWritten(file.getAbsolutePath(), context);
          } catch (FileNotFoundException ex) {
            fileSaveCallback.cancel(context);
          }
        } else {
          fileSaveCallback.cancel(context);
        }
      } else {
        fileSaveCallback.cancel(context);
      }
    } else {
      fileSaveCallback.cancel(context);
    }
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the fileSaveCallback.
   * 
   * @param fileSaveCallback
   *            the fileSaveCallback to set.
   */
  public void setFileSaveCallback(IFileSaveCallback fileSaveCallback) {
    this.fileSaveCallback = fileSaveCallback;
  }
}
