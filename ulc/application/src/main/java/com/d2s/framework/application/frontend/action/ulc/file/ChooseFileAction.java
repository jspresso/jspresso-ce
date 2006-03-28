/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.ulc.file;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.d2s.framework.application.frontend.action.ulc.AbstractUlcAction;
import com.d2s.framework.util.i18n.ITranslationProvider;
import com.d2s.framework.view.action.ActionContextConstants;
import com.d2s.framework.view.action.ActionException;
import com.d2s.framework.view.action.IActionHandler;
import com.ulcjava.base.application.ClientContext;
import com.ulcjava.base.application.util.serializable.IFileLoadHandler;
import com.ulcjava.base.shared.FileChooserConfig;

/**
 * Initiates a file choosintg action. Then the file content is passed as a byte
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
  private Map<String, List<String>> fileTypes;

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, Object> execute(IActionHandler actionHandler) {
    FileChooserConfig fcConfig = new FileChooserConfig();
    fcConfig.setDialogTitle(labelTranslator.getTranslation(getName(),
        getLocale()));
    if (fileTypes != null) {
      for (Map.Entry<String, List<String>> fileTypeEntry : fileTypes.entrySet()) {
        fcConfig.addFileFilterConfig(new FileChooserConfig.FileFilterConfig(
            fileTypeEntry.getValue().toArray(new String[0]), labelTranslator
                .getTranslation(fileTypeEntry.getKey(), getLocale())
                + " (" + fileTypeEntry.getValue() + ")"));
      }
    } else {
      fcConfig.addFileFilterConfig(new FileChooserConfig.FileFilterConfig(
          new String[] {"*.*"}, labelTranslator.getTranslation("ALL_FILES",
              getLocale())
              + " (*.*)"));
    }
    ClientContext.loadFile(new IFileLoadHandler() {

      private static final long serialVersionUID = -1025629868916915262L;

      @SuppressWarnings("unused")
      public void onSuccess(InputStream in, String filePath) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
          InputStream is = new BufferedInputStream(in);
          int b = is.read();
          while (b != -1) {
            baos.write(b);
          }
          baos.flush();
          getContext().put(ActionContextConstants.ACTION_RESULT,
              baos.toByteArray());
        } catch (IOException ex) {
          throw new ActionException(ex);
        } finally {
          if (baos != null) {
            try {
              baos.close();
            } catch (IOException ex) {
              // NO-OP.
            }
          }
        }
      }

      @SuppressWarnings("unused")
      public void onFailure(int reason, String description) {
        // NO-OP.
      }
    }, fcConfig);
    return super.execute(actionHandler);
  }

  /**
   * Sets the fileTypes. Filter file types are a map of descriptions keying file
   * extension arays.
   * 
   * @param fileTypes
   *          the fileTypes to set.
   */
  public void setFileTypes(Map<String, List<String>> fileTypes) {
    this.fileTypes = fileTypes;
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
}
