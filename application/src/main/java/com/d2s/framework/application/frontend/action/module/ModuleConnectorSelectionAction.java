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
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 */
public class ModuleConnectorSelectionAction<E, F> extends
    AbstractChainedAction<E, F> {

  /**
   * Selects indices on the module view collection connector based on the
   * <code>ActionContextConstants.SELECTED_INDICES</code> context value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void execute(IActionHandler actionHandler, Map<String, Object> context) {
    ICompositeValueConnector moduleConnector = getModuleConnector(context);
    ICollectionConnector parentModuleCollectionConnector = ((ICollectionConnectorProvider) moduleConnector
        .getParentConnector().getParentConnector()).getCollectionConnector();
    parentModuleCollectionConnector.setSelectedIndices(new int[0]);
    int[] connectorSelection = (int[]) context
        .get(ActionContextConstants.SELECTED_INDICES);
    if (moduleConnector instanceof ICollectionConnectorProvider) {
      ICollectionConnector collectionConnector = ((ICollectionConnectorProvider) moduleConnector)
          .getCollectionConnector();
      collectionConnector.setAllowLazyChildrenLoading(false);
      collectionConnector.setSelectedIndices(connectorSelection);
    }
    super.execute(actionHandler, context);
  }
}
