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
import com.d2s.framework.action.IActionHandler;

/**
 * Default handler implementation to fully read the file inputstream into a byte
 * array and setting it in the context.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class FileToByteArrayCallback implements IFileOpenCallback {

  /**
   * {@inheritDoc}
   */
  public void fileChosen(InputStream in, @SuppressWarnings("unused")
  String filePath, @SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
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
      IActionHandler actionHandler, @SuppressWarnings("unused")
      Map<String, Object> context) {
    // NO-OP
  }

}
