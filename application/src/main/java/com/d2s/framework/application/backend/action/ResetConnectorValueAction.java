/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import java.util.Map;

import com.d2s.framework.action.IActionHandler;

/**
 * Resets the model connector value to null.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ResetConnectorValueAction extends AbstractBackendAction {

  /**
   * Resets the model connector value to null.
   * <p>
   * {@inheritDoc}
   */
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    getModelConnector(context).setConnectorValue(null);
    return true;
  }

}
