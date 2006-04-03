/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.module;

import java.util.Collections;

import com.d2s.framework.application.frontend.action.AbstractChainedAction;
import com.d2s.framework.binding.ConnectorHelper;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICollectionConnectorProvider;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.view.action.IActionHandler;

/**
 * A simple action which selects indices on a parent module view connector.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ParentModuleConnectorSelectionAction extends AbstractChainedAction {

  /**
   * Selects indices on the parent module view collection connector based on the
   * <code>ActionContextConstants.SELECTED_INDICES</code> context value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void execute(IActionHandler actionHandler) {
    ICompositeValueConnector parentModuleConnector = getParentModuleConnector();
    ICompositeValueConnector grandParentModuleConnector = (ICompositeValueConnector) parentModuleConnector
        .getParentConnector();
    if (grandParentModuleConnector instanceof ICollectionConnectorProvider) {
      ICollectionConnector collectionConnector = ((ICollectionConnectorProvider) grandParentModuleConnector)
          .getCollectionConnector();
      collectionConnector.setSelectedIndices(ConnectorHelper.getIndicesOf(
          collectionConnector, Collections.singleton(parentModuleConnector
              .getConnectorValue())));
    }
    super.execute(actionHandler);
  }
}
