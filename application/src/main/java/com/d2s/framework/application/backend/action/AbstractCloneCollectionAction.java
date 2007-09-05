/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.binding.ConnectorHelper;
import com.d2s.framework.binding.ICollectionConnector;

/**
 * An action used duplicate a collection of domain objects. Cloning an entity
 * should result in adding it to the collection the action was triggered on.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractCloneCollectionAction extends
    AbstractCollectionAction {

  /**
   * Clones an element.
   * 
   * @param element
   *          the element to clone.
   * @param context
   *          the action context.
   * @return the cloned element.
   */
  protected abstract Object cloneElement(Object element,
      Map<String, Object> context);

  /**
   * Retrieves the managed collection from the model connector then clones the
   * selected elements.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    int[] selectedIndices = getSelectedIndices(context);
    ICollectionConnector collectionConnector = getModelConnector(context);
    if (selectedIndices == null || selectedIndices.length == 0
        || collectionConnector == null) {
      return false;
    }
    Collection<Object> elementClones = new ArrayList<Object>();
    for (int i = 0; i < selectedIndices.length; i++) {
      Object element = collectionConnector
          .getChildConnector(selectedIndices[i]).getConnectorValue();
      elementClones.add(cloneElement(element, context));
    }
    context.put(ActionContextConstants.SELECTED_INDICES, ConnectorHelper
        .getIndicesOf(collectionConnector, elementClones));
    return super.execute(actionHandler, context);
  }

}
