/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.file;

import java.io.InputStream;
import java.util.Map;

import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.descriptor.IStringPropertyDescriptor;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;

/**
 * Default handler implementation to deal with setting binary properties using
 * files.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
