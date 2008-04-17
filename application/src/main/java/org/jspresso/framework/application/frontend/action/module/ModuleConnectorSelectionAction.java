/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.module;

import java.util.Map;

import org.jspresso.framework.application.frontend.action.AbstractChainedAction;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.ICompositeValueConnector;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;

/**
 * A simple action which selects indices on a module view connector.
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
public class ModuleConnectorSelectionAction<E, F, G> extends
    AbstractChainedAction<E, F, G> {

  /**
   * Selects indices on the module view collection connector based on the
   * <code>ActionContextConstants.SELECTED_INDICES</code> context value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
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
