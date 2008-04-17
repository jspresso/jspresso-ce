/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.file;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.binding.IValueConnector;


/**
 * Default handler implementation to deal with getting binary properties storing
 * them in files.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ConnectorValueGetterCallback implements IFileSaveCallback {

  /**
   * {@inheritDoc}
   */
  public void cancel(@SuppressWarnings("unused")
  Map<String, Object> context) {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  public void fileChosen(OutputStream out, Map<String, Object> context) {
    OutputStream os = new BufferedOutputStream(out);
    try {
      Object connectorValue = ((IValueConnector) context
          .get(ActionContextConstants.VIEW_CONNECTOR)).getConnectorValue();
      byte[] content;
      if (connectorValue instanceof String) {
        content = ((String) connectorValue).getBytes();
      } else {
        content = (byte[]) connectorValue;
      }
      if (connectorValue != null) {
        os.write(content);
        os.flush();
      }
    } catch (IOException ex) {
      throw new ActionException(ex);
    } finally {
      try {
        os.close();
      } catch (IOException ex) {
        // NO-OP.
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public void fileWritten(@SuppressWarnings("unused")
  String filePath, @SuppressWarnings("unused")
  Map<String, Object> context) {
    // NO-OP
  }
}
