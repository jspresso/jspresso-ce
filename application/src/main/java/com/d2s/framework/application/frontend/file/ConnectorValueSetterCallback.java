/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.file;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.ActionException;
import com.d2s.framework.binding.IValueConnector;

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

  /**
   * {@inheritDoc}
   */
  public void fileChosen(InputStream in, @SuppressWarnings("unused")
  String filePath, Map<String, Object> context) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      InputStream is = new BufferedInputStream(in);
      int b;
      while ((b = is.read()) != -1) {
        baos.write(b);
      }
      baos.flush();
      byte[] fileContent = baos.toByteArray();
      context.put(ActionContextConstants.ACTION_PARAM, fileContent);
      ((IValueConnector) context.get(ActionContextConstants.VIEW_CONNECTOR))
          .setConnectorValue(fileContent);
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
  public void cancel(@SuppressWarnings("unused")
  Map<String, Object> context) {
    // NO-OP
  }

}
