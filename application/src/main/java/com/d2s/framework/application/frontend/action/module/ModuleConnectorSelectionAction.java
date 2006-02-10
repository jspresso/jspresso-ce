/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.module;

import java.util.Map;

import com.d2s.framework.application.frontend.action.AbstractChainedAction;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICollectionConnectorProvider;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.view.action.ActionContextConstants;
import com.d2s.framework.view.action.IActionHandler;

/**
 * A simple action which selects indices on a module view connector.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ModuleConnectorSelectionAction extends AbstractChainedAction {

  /**
   * Selects indices on the module view collection connector based on the
   * <code>ActionContextConstants.SELECTED_INDICES</code> context value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public Map<String, Object> execute(IActionHandler actionHandler) {
    ICompositeValueConnector moduleConnector = getModuleConnector();
    ICollectionConnector parentProjectionCollectionConnector = ((ICollectionConnectorProvider) moduleConnector
        .getParentConnector().getParentConnector()).getCollectionConnector();
    parentProjectionCollectionConnector.setSelectedIndices(new int[0]);
    int[] connectorSelection = (int[]) getContext().get(
        ActionContextConstants.SELECTED_INDICES);
    if (moduleConnector instanceof ICollectionConnectorProvider) {
      ICollectionConnector collectionConnector = ((ICollectionConnectorProvider) moduleConnector)
          .getCollectionConnector();
      collectionConnector.setSelectedIndices(connectorSelection);
    }
    return super.execute(actionHandler);
  }
}
