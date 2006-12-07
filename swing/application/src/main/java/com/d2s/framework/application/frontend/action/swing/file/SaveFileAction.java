/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.swing.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Map;

import javax.swing.JFileChooser;

import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.file.IFileSaveCallback;
import com.d2s.framework.util.swing.SwingUtil;

/**
 * Initiates a file save action.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
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
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the fileSaveCallback.
   * 
   * @param fileSaveCallback
   *          the fileSaveCallback to set.
   */
  public void setFileSaveCallback(IFileSaveCallback fileSaveCallback) {
    this.fileSaveCallback = fileSaveCallback;
  }
}
