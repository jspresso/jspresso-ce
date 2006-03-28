/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.file;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.d2s.framework.application.frontend.action.AbstractFrontendAction;
import com.d2s.framework.view.action.ActionContextConstants;
import com.d2s.framework.view.action.ActionException;

/**
 * Default handler implementation to deal with setting binary properties using
 * files.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ConnectorValueSetterCallback implements IFileOpenCallback {

  private AbstractFrontendAction chooseFileAction;

  /**
   * Constructs a new <code>ConnectorValueSetterCallback</code> instance.
   * 
   * @param chooseFileAction
   *          the action this callback is tied to.
   */
  public ConnectorValueSetterCallback(AbstractFrontendAction chooseFileAction) {
    this.chooseFileAction = chooseFileAction;
  }

  /**
   * {@inheritDoc}
   */
  public void fileOpened(InputStream in, @SuppressWarnings("unused")
  String filePath) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      InputStream is = new BufferedInputStream(in);
      int b = is.read();
      while (b != -1) {
        baos.write(b);
        b = is.read();
      }
      baos.flush();
      byte[] fileContent = baos.toByteArray();
      chooseFileAction.getContext().put(ActionContextConstants.ACTION_RESULT,
          fileContent);
      chooseFileAction.getViewConnector().setConnectorValue(fileContent);
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

  /**
   * {@inheritDoc}
   */
  public void cancel() {
    // NO-OP
  }

}
