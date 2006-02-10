/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.projection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.d2s.framework.application.backend.action.AbstractCollectionAction;
import com.d2s.framework.binding.ConnectorHelper;
import com.d2s.framework.binding.ICollectionConnectorProvider;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.view.action.ActionContextConstants;
import com.d2s.framework.view.action.IActionHandler;
import com.d2s.framework.view.projection.SubModule;
import com.d2s.framework.view.projection.Module;

/**
 * This action removes the selected projections from their parent.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RemoveSubModuleFromParentAction extends AbstractCollectionAction {

  /**
   * Removes the selected projections from their parent.
   * <p>
   * {@inheritDoc}
   */
  public Map<String, Object> execute(@SuppressWarnings("unused")
  IActionHandler actionHandler) {
    ICompositeValueConnector parentProjectionConnector = (ICompositeValueConnector) getModuleConnector()
        .getParentConnector().getParentConnector();

    int[] selectedIndices = getParentModuleSelectedIndices();

    if (selectedIndices == null || selectedIndices.length == 0) {
      return null;
    }
    Module parentProjection = (Module) parentProjectionConnector
        .getConnectorValue();
    Collection<SubModule> childrenToRemove = new ArrayList<SubModule>();

    for (int i = 0; i < selectedIndices.length; i++) {
      childrenToRemove.add(parentProjection.getSubModules().get(i));
    }
    parentProjection.removeSubModules(childrenToRemove);
    Map<String, Object> executionResult = new HashMap<String, Object>();
    executionResult.put(ActionContextConstants.SELECTED_INDICES,
        ConnectorHelper.getIndicesOf(
            ((ICollectionConnectorProvider) parentProjectionConnector
                .getParentConnector()).getCollectionConnector(), Collections
                .singleton(parentProjectionConnector.getConnectorValue())));
    return executionResult;
  }
}
