/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.file;

import java.io.InputStream;
import java.util.Map;

import com.d2s.framework.action.IActionHandler;

/**
 * This interface is used react to file open.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IFileOpenCallback {

  /**
   * Called whenever the file opening is cancelled.
   * 
   * @param actionHandler
   *            the action handler.
   * @param context
   *            the action context.
   */
  void cancel(IActionHandler actionHandler, Map<String, Object> context);

  /**
   * Called whenever a file is opened.
   * 
   * @param in
   *            the input stream to read the file bytes.
   * @param filePath
   *            the file path.
   * @param actionHandler
   *            the action handler.
   * @param context
   *            the action context.
   */
  void fileChosen(InputStream in, String filePath,
      IActionHandler actionHandler, Map<String, Object> context);
}
