/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.file;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.ActionException;
import com.d2s.framework.binding.IValueConnector;

/**
 * Default handler implementation to deal with getting binary properties storing
 * them in files.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ConnectorValueGetterCallback implements IFileSaveCallback {

  /**
   * {@inheritDoc}
   */
  public void fileChosen(OutputStream out, Map<String, Object> context) {
    OutputStream os = new BufferedOutputStream(out);
    try {
      Object connectorValue = ((IValueConnector) context
          .get(ActionContextConstants.VIEW_CONNECTOR)).getConnectorValue();
      byte[] content;
      if (connectorValue instanceof String) {
        content = ((String) connectorValue).getBytes();
      } else {
        content = (byte[]) connectorValue;
      }
      if (connectorValue != null) {
        os.write(content);
        os.flush();
      }
    } catch (IOException ex) {
      throw new ActionException(ex);
    } finally {
      if (os != null) {
        try {
          os.close();
        } catch (IOException ex) {
          // NO-OP.
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public void fileWritten(@SuppressWarnings("unused")
  String filePath, @SuppressWarnings("unused")
  Map<String, Object> context) {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  public void cancel(@SuppressWarnings("unused")
  Map<String, Object> context) {
    // NO-OP
  }
}
