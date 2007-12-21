/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.ulc.file;

import java.io.OutputStream;
import java.util.Map;

import com.d2s.framework.action.ActionException;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.file.IFileSaveCallback;
import com.ulcjava.base.application.ClientContext;
import com.ulcjava.base.application.util.serializable.IFileStoreHandler;
import com.ulcjava.base.shared.FileChooserConfig;

/**
 * Initiates a file save action.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
      final Map<String, Object> context) {
    try {
      ClientContext.storeFile(new IFileStoreHandler() {

        private static final long serialVersionUID = -1025629868916915262L;

        @SuppressWarnings("unused")
        public void onFailure(int reason, String description) {
          if (fileSaveCallback != null) {
            fileSaveCallback.cancel(context);
          }
        }

        public void onSuccess(String filePath) {
          getFileChooser(context).setCurrentDirectory(filePath);
          if (fileSaveCallback != null) {
            fileSaveCallback.fileWritten(filePath, context);
          }
        }

        public void prepareFile(OutputStream out) {
          if (fileSaveCallback != null) {
            fileSaveCallback.fileChosen(out, context);
          }
        }
      }, getFileChooser(context));
    } catch (Exception ex) {
      throw new ActionException(ex);
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
}
