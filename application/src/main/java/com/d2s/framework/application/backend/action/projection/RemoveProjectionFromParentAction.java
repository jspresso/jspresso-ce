/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.projection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.backend.action.AbstractCollectionAction;
import com.d2s.framework.application.backend.projection.ChildProjection;
import com.d2s.framework.application.backend.projection.Projection;
import com.d2s.framework.binding.ConnectorHelper;
import com.d2s.framework.binding.ICollectionConnectorProvider;
import com.d2s.framework.binding.ICompositeValueConnector;

/**
 * This action removes the selected projections from their parent.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RemoveProjectionFromParentAction extends AbstractCollectionAction {

  /**
   * Removes the selected projections from their parent.
   * <p>
   * {@inheritDoc}
   */
  public Map<String, Object> execute(@SuppressWarnings("unused")
  IActionHandler actionHandler) {
    ICompositeValueConnector parentProjectionConnector = (ICompositeValueConnector) getProjectionConnector()
        .getParentConnector().getParentConnector();

    int[] selectedIndices = getParentProjectionSelectedIndices();

    if (selectedIndices == null || selectedIndices.length == 0) {
      return null;
    }
    Projection parentProjection = (Projection) parentProjectionConnector
        .getConnectorValue();
    Collection<ChildProjection> childrenToRemove = new ArrayList<ChildProjection>();

    for (int i = 0; i < selectedIndices.length; i++) {
      childrenToRemove.add(parentProjection.getChildren().get(i));
    }
    parentProjection.removeChildren(childrenToRemove);
    Map<String, Object> executionResult = new HashMap<String, Object>();
    executionResult.put(ActionContextConstants.SELECTED_INDICES,
        ConnectorHelper.getIndicesOf(
            ((ICollectionConnectorProvider) parentProjectionConnector
                .getParentConnector()).getCollectionConnector(), Collections
                .singleton(parentProjectionConnector.getConnectorValue())));
    return executionResult;
  }
}
