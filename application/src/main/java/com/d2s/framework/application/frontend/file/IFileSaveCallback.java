/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.file;

import java.io.OutputStream;
import java.util.Map;

/**
 * This interface is used react to file save.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IFileSaveCallback {

  /**
   * Called whenever the file opening is cancelled.
   * 
   * @param context
   *            the action context.
   */
  void cancel(Map<String, Object> context);

  /**
   * Called whenever a file is chosen as save destination.
   * 
   * @param out
   *            the output stream to write to the file.
   * @param context
   *            the action context.
   */
  void fileChosen(OutputStream out, Map<String, Object> context);

  /**
   * Called whenever a file is finished writing.
   * 
   * @param filePath
   *            the file path.
   * @param context
   *            the action context.
   */
  void fileWritten(String filePath, Map<String, Object> context);
}
