/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action;

import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.ICollectionConnector;


/**
 * A simple action which selects indices on ath view connector.
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
public class ConnectorSelectionAction<E, F, G> extends
    AbstractChainedAction<E, F, G> {

  /**
   * Selects indices on the view collection connector based on the
   * <code>ActionContextConstants.SELECTED_INDICES</code> context value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    ICollectionConnector collectionConnector = (ICollectionConnector) getSourceViewConnector(context);
    if (collectionConnector == null) {
      collectionConnector = (ICollectionConnector) getViewConnector(context);
    }
    int[] connectorSelection = (int[]) context
        .get(ActionContextConstants.SELECTED_INDICES);
    collectionConnector.setSelectedIndices(connectorSelection);
    return super.execute(actionHandler, context);
  }
}
