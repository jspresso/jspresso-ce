/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.file;

import java.io.InputStream;

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
   */
  void fileOpened(InputStream in, String filePath);

  /**
   * Called whenever the file opening is cancelled.
   */
  void cancel();
}
