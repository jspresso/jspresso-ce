/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.frontend.action.std;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.binding.IValueConnector;

/**
 * This action retrieves the action parameter from the action context and
 * assigns it as value to the targeted connector. The connector to target is
 * itself retrieved from the action context using a parametrized key.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class SetConnectorValueAction<E, F, G> extends FrontendAction<E, F, G> {

  private String connectorActionContextKey;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    Object previousActionResult = getActionParameter(context);
    IValueConnector connector = (IValueConnector) context
        .get(connectorActionContextKey);
    // the following will force a connector value change event.
    // connector.setConnectorValue(null);
    connector.setConnectorValue(previousActionResult);
    return super.execute(actionHandler, context);
  }

  /**
   * Configures the key to look for the target connector in the context, e.g.
   * VIEW_CONNECTOR.
   *
   * @param connectorActionContextKey
   *          the connectorActionContextKey to set.
   */
  public void setConnectorActionContextKey(String connectorActionContextKey) {
    this.connectorActionContextKey = connectorActionContextKey;
  }

}
