/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.ulc.file;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.d2s.framework.application.frontend.action.ulc.AbstractUlcAction;
import com.d2s.framework.application.frontend.file.IFileOpenCallback;
import com.d2s.framework.util.i18n.ITranslationProvider;
import com.d2s.framework.view.action.IActionHandler;
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

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, Object> execute(IActionHandler actionHandler) {
    FileChooserConfig fcConfig = new FileChooserConfig();
    fcConfig.setDialogTitle(labelTranslator.getTranslation(getName(),
        getLocale()));
    if (fileFilter != null) {
      for (Map.Entry<String, List<String>> fileTypeEntry : fileFilter
          .entrySet()) {
        StringBuffer extensionsDescription = new StringBuffer("( ");
        for (String fileExtension : fileTypeEntry.getValue()) {
          extensionsDescription.append("*").append(fileExtension).append(" ");
        }
        extensionsDescription.append(" )");
        fcConfig.addFileFilterConfig(new FileChooserConfig.FileFilterConfig(
            fileTypeEntry.getValue().toArray(new String[0]), labelTranslator
                .getTranslation(fileTypeEntry.getKey(), getLocale())
                + extensionsDescription.toString()));
      }
    }
    ClientContext.loadFile(new IFileLoadHandler() {

      private static final long serialVersionUID = -1025629868916915262L;

      @SuppressWarnings("unused")
      public void onSuccess(InputStream in, String filePath) {
        if (fileOpenCallback != null) {
          fileOpenCallback.fileOpened(in, filePath);
        }
      }

      @SuppressWarnings("unused")
      public void onFailure(int reason, String description) {
        if (fileOpenCallback != null) {
          fileOpenCallback.cancel();
        }
      }
    }, fcConfig);
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
}
