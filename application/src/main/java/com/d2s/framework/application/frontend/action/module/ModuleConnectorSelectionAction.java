/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.module;

import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.AbstractChainedAction;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICollectionConnectorProvider;
import com.d2s.framework.binding.ICompositeValueConnector;

/**
 * A simple action which selects indices on a module view connector.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class ModuleConnectorSelectionAction<E, F, G> extends
    AbstractChainedAction<E, F, G> {

  /**
   * Selects indices on the module view collection connector based on the
   * <code>ActionContextConstants.SELECTED_INDICES</code> context value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    ICompositeValueConnector moduleConnector = getModuleConnector(context);
    ICollectionConnector parentModuleCollectionConnector = ((ICollectionConnectorProvider) moduleConnector
        .getParentConnector().getParentConnector()).getCollectionConnector();
    parentModuleCollectionConnector.setSelectedIndices(new int[0]);
    int[] connectorSelection = (int[]) context
        .get(ActionContextConstants.SELECTED_INDICES);
    if (moduleConnector instanceof ICollectionConnectorProvider) {
      ICollectionConnector collectionConnector = ((ICollectionConnectorProvider) moduleConnector)
          .getCollectionConnector();
      collectionConnector.setSelectedIndices(connectorSelection);
    }
    return super.execute(actionHandler, context);
  }
}
