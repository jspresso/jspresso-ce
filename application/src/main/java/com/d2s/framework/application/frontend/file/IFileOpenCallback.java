/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.file;

import java.io.InputStream;
import java.util.Map;

/**
 * This interface is used react to file openings.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IFileOpenCallback {

  /**
   * Called whenever a file is opened.
   * 
   * @param in
   *          the input stream to read the file bytes.
   * @param filePath
   *          the file path.
   * @param context
   *          the action context.
   */
  void fileOpened(InputStream in, String filePath, Map<String, Object> context);

  /**
   * Called whenever the file opening is cancelled.
   * 
   * @param context
   *          the action context.
   */
  void cancel(Map<String, Object> context);
}
