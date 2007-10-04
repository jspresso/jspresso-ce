/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.file;

import java.io.InputStream;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.model.descriptor.IStringPropertyDescriptor;

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
      Object valueToSet = context.get(ActionContextConstants.ACTION_PARAM);
      IModelDescriptor modelDescriptor = (IModelDescriptor) context
          .get(ActionContextConstants.MODEL_DESCRIPTOR);
      if (modelDescriptor instanceof IStringPropertyDescriptor) {
        valueToSet = new String((byte[]) valueToSet);
      }
      IValueConnector viewConnector = (IValueConnector) context
          .get(ActionContextConstants.VIEW_CONNECTOR);
      viewConnector.setConnectorValue(valueToSet);
    }
  }
}
