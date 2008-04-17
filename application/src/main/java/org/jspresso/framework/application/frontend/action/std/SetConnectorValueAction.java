/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.std;

import java.util.Map;

import org.jspresso.framework.application.frontend.action.AbstractFrontendAction;
import org.jspresso.framework.binding.IValueConnector;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;

/**
 * Sets the object registered as ACTION_RESULT in the action context as the
 * connector value. The connector used is also retrieved from the action context
 * using the key parametrized.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            the actual gui component type used.
 * @param <F>
 *            the actual icon type used.
 * @param <G>
 *            the actual action type used.
 */
public class SetConnectorValueAction<E, F, G> extends
    AbstractFrontendAction<E, F, G> {

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
   *            the connectorActionContextKey to set.
   */
  public void setConnectorActionContextKey(String connectorActionContextKey) {
    this.connectorActionContextKey = connectorActionContextKey;
  }

}
