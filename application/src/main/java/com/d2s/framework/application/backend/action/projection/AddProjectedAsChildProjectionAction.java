/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.projection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.backend.action.AbstractCollectionAction;
import com.d2s.framework.application.backend.projection.BeanProjection;
import com.d2s.framework.application.backend.projection.ChildProjection;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.util.bean.IPropertyChangeCapable;
import com.d2s.framework.view.descriptor.projection.IChildProjectionViewDescriptor;
import com.d2s.framework.view.descriptor.projection.ISimpleChildProjectionViewDescriptor;

/**
 * This action adds the selected objects as child projections.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class AddProjectedAsChildProjectionAction extends
    AbstractCollectionAction {

  /**
   * Adds the selected objects as child projections.
   * <p>
   * {@inheritDoc}
   */
  public Map<String, Object> execute(@SuppressWarnings("unused")
  IActionHandler actionHandler) {
    int[] selectedIndices = getSelectedIndices();

    if (selectedIndices == null || selectedIndices.length == 0) {
      return null;
    }
    ICompositeValueConnector projectionConnector = getProjectionConnector();
    ICollectionConnector collectionConnector = getModelConnector();
    ChildProjection parentProjection = (ChildProjection) projectionConnector
        .getConnectorValue();
    List<ChildProjection> children;
    if (parentProjection.getChildren() == null) {
      children = new ArrayList<ChildProjection>(selectedIndices.length);
    } else {
      children = new ArrayList<ChildProjection>(parentProjection.getChildren());
    }
    int[] childSelectedIndices = new int[selectedIndices.length];
    for (int i = 0; i < selectedIndices.length; i++) {
      IPropertyChangeCapable nextselectedProjectedObject = (IPropertyChangeCapable) collectionConnector
          .getChildConnector(selectedIndices[i]).getConnectorValue();
      BeanProjection nextChildProjection = new BeanProjection();
      nextChildProjection
          .setViewDescriptor((IChildProjectionViewDescriptor) ((ISimpleChildProjectionViewDescriptor) parentProjection
              .getViewDescriptor()).getChildDescriptor());
      nextChildProjection.setProjectedObject(nextselectedProjectedObject);
      nextChildProjection.setName(String.valueOf(nextselectedProjectedObject));
      int nextChildProjectionIndex = children.indexOf(nextChildProjection);
      if (nextChildProjectionIndex < 0) {
        childSelectedIndices[i] = children.size();
        children.add(nextChildProjection);
      } else {
        childSelectedIndices[i] = nextChildProjectionIndex;
      }
    }
    parentProjection.setChildren(children);
    Map<String, Object> executionResult = new HashMap<String, Object>();
    executionResult.put(ActionContextConstants.SELECTED_INDICES,
        childSelectedIndices);
    return executionResult;
  }
}
