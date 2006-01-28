/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.projection;

import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.AbstractChainedAction;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICollectionConnectorProvider;
import com.d2s.framework.binding.ICompositeValueConnector;

/**
 * A simple action which selects indices on a projection view connector.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ProjectionConnectorSelectionAction extends AbstractChainedAction {

  /**
   * Selects indices on the projection view collection connector based on the
   * <code>ActionContextConstants.SELECTED_INDICES</code> context value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public Map<String, Object> execute(IActionHandler actionHandler) {
    ICompositeValueConnector projectionConnector = getProjectionConnector();
    ICollectionConnector parentProjectionCollectionConnector = ((ICollectionConnectorProvider) projectionConnector
        .getParentConnector().getParentConnector()).getCollectionConnector();
    parentProjectionCollectionConnector.setSelectedIndices(new int[0]);
    int[] connectorSelection = (int[]) getContext().get(
        ActionContextConstants.SELECTED_INDICES);
    if (projectionConnector instanceof ICollectionConnectorProvider) {
      ICollectionConnector collectionConnector = ((ICollectionConnectorProvider) projectionConnector)
          .getCollectionConnector();
      collectionConnector.setSelectedIndices(connectorSelection);
    }
    return super.execute(actionHandler);
  }
}
