/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action;

import java.util.Map;

import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.view.action.ActionContextConstants;
import com.d2s.framework.view.action.IActionHandler;

/**
 * A simple action which selects indices on ath view connector.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ConnectorSelectionAction extends AbstractChainedAction {

  /**
   * Selects indices on the view collection connector based on the
   * <code>ActionContextConstants.SELECTED_INDICES</code> context value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void execute(IActionHandler actionHandler, Map<String, Object> context) {
    ICollectionConnector collectionConnector = (ICollectionConnector) getViewConnector(context);
    int[] connectorSelection = (int[]) context.get(
        ActionContextConstants.SELECTED_INDICES);
    collectionConnector.setSelectedIndices(connectorSelection);
    super.execute(actionHandler, context);
  }
}
