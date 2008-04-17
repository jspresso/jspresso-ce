/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.file;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;


/**
 * Default handler implementation to fully read the file inputstream into a byte
 * array and setting it in the context.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class FileToByteArrayCallback implements IFileOpenCallback {

  /**
   * {@inheritDoc}
   */
  public void cancel(@SuppressWarnings("unused")
  IActionHandler actionHandler, @SuppressWarnings("unused")
  Map<String, Object> context) {
    context.remove(ActionContextConstants.ACTION_PARAM);
  }

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
      try {
        baos.close();
      } catch (IOException ex) {
        // NO-OP.
      }
    }
  }

}
