/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.swing.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.file.IFileOpenCallback;

/**
 * Initiates a file open action.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class OpenFileAction extends ChooseFileAction {

  private IFileOpenCallback fileOpenCallback;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {

    JFileChooser currentFileChooser = getFileChooser(context);

    int returnVal = currentFileChooser.showOpenDialog(SwingUtilities
        .getWindowAncestor(getSourceComponent(context)));
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = currentFileChooser.getSelectedFile();
      if (file != null) {
        try {
          fileOpenCallback.fileChosen(new FileInputStream(file), file
              .getAbsolutePath(), actionHandler, context);
        } catch (FileNotFoundException ex) {
          fileOpenCallback.cancel(actionHandler, context);
        }
      } else {
        fileOpenCallback.cancel(actionHandler, context);
      }
    } else {
      fileOpenCallback.cancel(actionHandler, context);
    }
    return super.execute(actionHandler, context);
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
}
