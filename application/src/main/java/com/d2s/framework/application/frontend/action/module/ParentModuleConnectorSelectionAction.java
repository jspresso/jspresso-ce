/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.module;

import java.util.Collections;
import java.util.Map;

import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.AbstractChainedAction;
import com.d2s.framework.binding.ConnectorHelper;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICollectionConnectorProvider;
import com.d2s.framework.binding.ICompositeValueConnector;

/**
 * A simple action which selects indices on a parent module view connector.
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
public class ParentModuleConnectorSelectionAction<E, F, G> extends
    AbstractChainedAction<E, F, G> {

  /**
   * Selects indices on the parent module view collection connector based on the
   * <code>ActionContextConstants.SELECTED_INDICES</code> context value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void execute(IActionHandler actionHandler, Map<String, Object> context) {
    ICompositeValueConnector parentModuleConnector = getParentModuleConnector(context);
    ICompositeValueConnector grandParentModuleConnector = parentModuleConnector
        .getParentConnector();
    if (grandParentModuleConnector instanceof ICollectionConnectorProvider) {
      ICollectionConnector collectionConnector = ((ICollectionConnectorProvider) grandParentModuleConnector)
          .getCollectionConnector();
      collectionConnector.setSelectedIndices(ConnectorHelper.getIndicesOf(
          collectionConnector, Collections.singleton(parentModuleConnector
              .getConnectorValue())));
    }
    super.execute(actionHandler, context);
  }
}
