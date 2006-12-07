/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.file;

import java.io.InputStream;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
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
public class ConnectorValueSetterCallback extends FileToByteArrayCallback {

  /**
   * {@inheritDoc}
   */
  @Override
  public void fileChosen(InputStream in, String filePath,
      IActionHandler actionHandler, Map<String, Object> context) {
    super.fileChosen(in, filePath, actionHandler, context);
    if (context.containsKey(ActionContextConstants.ACTION_PARAM)) {
      ((IValueConnector) context.get(ActionContextConstants.VIEW_CONNECTOR))
          .setConnectorValue(context.get(ActionContextConstants.ACTION_PARAM));
    }
  }
}
