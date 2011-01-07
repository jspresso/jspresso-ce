/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.application.frontend.file;

import java.io.InputStream;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.descriptor.IStringPropertyDescriptor;

/**
 * Default handler implementation to deal with setting binary properties using
 * files.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ConnectorValueSetterCallback extends FileToByteArrayCallback {

  /**
   * {@inheritDoc}
   */
  @Override
  public void fileChosen(String name, InputStream in,
      IActionHandler actionHandler, Map<String, Object> context) {
    super.fileChosen(name, in, actionHandler, context);
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
