/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.ulc.file;

import java.io.InputStream;
import java.util.Map;

import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.file.IFileOpenCallback;
import com.ulcjava.base.application.ClientContext;
import com.ulcjava.base.application.util.serializable.IFileLoadHandler;
import com.ulcjava.base.shared.FileChooserConfig;

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
  public boolean execute(final IActionHandler actionHandler,
      final Map<String, Object> context) {
    ClientContext.loadFile(new IFileLoadHandler() {

      private static final long serialVersionUID = -1025629868916915262L;

      @SuppressWarnings("unused")
      public void onFailure(int reason, String description) {
        if (fileOpenCallback != null) {
          fileOpenCallback.cancel(actionHandler, context);
        }
      }

      @SuppressWarnings("unused")
      public void onSuccess(InputStream in, String filePath) {
        if (fileOpenCallback != null) {
          getFileChooser(context).setCurrentDirectory(filePath);
          fileOpenCallback.fileChosen(in, filePath, actionHandler, context);
        }
      }
    }, getFileChooser(context));
    return super.execute(actionHandler, context);
  }

  /**
   * Completes the file chooser configuration with behaviour specific to open
   * action.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected FileChooserConfig getFileChooser(Map<String, Object> context) {
    FileChooserConfig fileChooser = super.getFileChooser(context);
    return fileChooser;
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
