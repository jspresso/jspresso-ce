/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.file;

import java.io.InputStream;
import java.util.Map;

import com.d2s.framework.action.IAction;
import com.d2s.framework.action.IActionHandler;

/**
 * A callback handler which triggers an arbitrary action.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ActionExecutorCallback extends FileToByteArrayCallback {

  private IAction action;

  /**
   * {@inheritDoc}
   */
  @Override
  public void fileChosen(InputStream in, String filePath,
      IActionHandler actionHandler, Map<String, Object> context) {
    super.fileChosen(in, filePath, actionHandler, context);
    actionHandler.execute(action, context);
  }

  /**
   * Sets the action.
   * 
   * @param action
   *          the action to set.
   */
  public void setAction(IAction action) {
    this.action = action;
  }
}
