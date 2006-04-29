/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.ulc.file;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.ulc.AbstractUlcAction;
import com.d2s.framework.application.frontend.file.IFileOpenCallback;
import com.d2s.framework.util.i18n.ITranslationProvider;
import com.ulcjava.base.application.ClientContext;
import com.ulcjava.base.application.util.serializable.IFileLoadHandler;
import com.ulcjava.base.shared.FileChooserConfig;

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
public class ChooseFileAction extends AbstractUlcAction {

  private ITranslationProvider      labelTranslator;
  private Map<String, List<String>> fileFilter;
  private IFileOpenCallback         fileOpenCallback;
  private FileChooserConfig         fileChooser;

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute(IActionHandler actionHandler,
      final Map<String, Object> context) {
    ClientContext.loadFile(new IFileLoadHandler() {

      private static final long serialVersionUID = -1025629868916915262L;

      @SuppressWarnings("unused")
      public void onSuccess(InputStream in, String filePath) {
        if (fileOpenCallback != null) {
          getFileChooser(context).setCurrentDirectory(filePath);
          fileOpenCallback.fileOpened(in, filePath, context);
        }
      }

      @SuppressWarnings("unused")
      public void onFailure(int reason, String description) {
        if (fileOpenCallback != null) {
          fileOpenCallback.cancel(context);
        }
      }
    }, getFileChooser(context));
    super.execute(actionHandler, context);
  }

  private FileChooserConfig getFileChooser(Map<String, Object> context) {
    if (fileChooser == null) {
      fileChooser = new FileChooserConfig();
      fileChooser.setDialogTitle(labelTranslator.getTranslation(getName(),
          getLocale(context)));
      if (fileFilter != null) {
        for (Map.Entry<String, List<String>> fileTypeEntry : fileFilter
            .entrySet()) {
          StringBuffer extensionsDescription = new StringBuffer("( ");
          for (String fileExtension : fileTypeEntry.getValue()) {
            extensionsDescription.append("*").append(fileExtension).append(" ");
          }
          extensionsDescription.append(" )");
          fileChooser
              .addFileFilterConfig(new FileChooserConfig.FileFilterConfig(
                  fileTypeEntry.getValue().toArray(new String[0]),
                  labelTranslator.getTranslation(fileTypeEntry.getKey(),
                      getLocale(context))
                      + extensionsDescription.toString()));
        }
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
}
