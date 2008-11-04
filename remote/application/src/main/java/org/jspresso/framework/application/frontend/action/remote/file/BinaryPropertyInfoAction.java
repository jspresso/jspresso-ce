/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.frontend.action.remote.file;

import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.remote.flow.InfoAction;


/**
 * A frontend action to display all usefull informations about a binary
 * property.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BinaryPropertyInfoAction extends InfoAction {

  /**
   * Displays the size of the binary property.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    Integer size = new Integer(0);
    String unit = "Ko";
    byte[] content = getBinaryContent(context);
    if (content != null) {
      if (content.length < 1024) {
        size = new Integer(content.length);
        unit = "o";
      } else if (content.length >= 1024 && content.length < 1024 * 1024) {
        size = new Integer(content.length / 1024);
        unit = "Ko";
      } else if (content.length >= 1024 * 1024
          && content.length < 1024 * 1024 * 1024) {
        size = new Integer(content.length / (1024 * 1024));
        unit = "Mo";
      } else if (content.length >= 1024 * 1024 * 1024) {
        size = new Integer(content.length / (1024 * 1024 * 1024));
        unit = "Go";
      }
    }
    context.put(ActionContextConstants.ACTION_PARAM, getTranslationProvider(
        context).getTranslation("binary.info.message",
        new Object[] {size, unit}, getLocale(context)));
    return super.execute(actionHandler, context);
  }

  /**
   * Retrieve the binary content to display the infos on.
   * 
   * @param context
   *            the action context.
   * @return The binary content to display the infos on or null.
   */
  protected byte[] getBinaryContent(Map<String, Object> context) {
    Object connectorValue = getViewConnector(context).getConnectorValue();
    byte[] content;
    if (connectorValue instanceof String) {
      content = ((String) connectorValue).getBytes();
    } else {
      content = (byte[]) connectorValue;
    }
    return content;
  }
}
