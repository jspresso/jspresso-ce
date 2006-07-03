/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.std;

import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.AbstractFrontendAction;
import com.d2s.framework.binding.IValueConnector;

/**
 * Sets the object registered as ACTION_RESULT in the action context as the
 * connector value. The connector used is also retrieved from the action context
 * using the key parametrized.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SetConnectorValueAction extends AbstractFrontendAction {

  private String connectorActionContextKey;

  /**
   * {@inheritDoc}
   */
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    Object previousActionResult = context
        .get(ActionContextConstants.ACTION_PARAM);
    IValueConnector connector = (IValueConnector) context
        .get(connectorActionContextKey);
    // the following will force a connector value change event.
    // connector.setConnectorValue(null);
    connector.setConnectorValue(previousActionResult);
    return true;
  }

  /**
   * Sets the connectorActionContextKey.
   * 
   * @param connectorActionContextKey
   *          the connectorActionContextKey to set.
   */
  public void setConnectorActionContextKey(String connectorActionContextKey) {
    this.connectorActionContextKey = connectorActionContextKey;
  }

}
