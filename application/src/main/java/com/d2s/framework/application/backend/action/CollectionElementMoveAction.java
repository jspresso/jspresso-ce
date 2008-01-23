/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.binding.ConnectorHelper;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.model.entity.IEntity;

/**
 * An action used in list components to move a detail up and down.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class CollectionElementMoveAction extends AbstractCollectionAction {

  private int offset;

  /**
   * Retrieves the master and its managed collection from the model connector
   * then it moves the selected element of the offset.
   * <p>
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    int[] indicesToMove = getSelectedIndices(context);
    ICollectionConnector collectionConnector = getModelConnector(context);
    if (indicesToMove == null || indicesToMove.length == 0
        || collectionConnector == null) {
      return false;
    }
    if (!List.class.isAssignableFrom(getModelDescriptor(context)
        .getCollectionDescriptor().getCollectionInterface())) {
      return false;
    }

    List originalList = (List) collectionConnector.getConnectorValue();
    List targetList = new ArrayList<Object>(originalList);
    List<Object> elementsToMove = new ArrayList<Object>();
    for (int indexToMove : indicesToMove) {
      elementsToMove.add(targetList.get(indexToMove));
    }

    int[] targetIndices = new int[indicesToMove.length];
    for (int i = indicesToMove.length - 1; i >= 0; i--) {
      targetIndices[i] = indicesToMove[i] + offset;
    }
    if (targetIndices[0] >= 0
        && targetIndices[targetIndices.length - 1] < targetList.size()) {
      for (int i = indicesToMove.length - 1; i >= 0; i--) {
        targetList.remove(indicesToMove[i]);
      }
      for (int i = 0; i < indicesToMove.length; i++) {
        targetList.add(targetIndices[i], elementsToMove.get(i));
      }
      ((IEntity) collectionConnector.getParentConnector().getConnectorValue())
          .straightSetProperty(collectionConnector.getId(), null);
      originalList.clear();
      originalList.addAll(targetList);
      ((IEntity) collectionConnector.getParentConnector().getConnectorValue())
          .straightSetProperty(collectionConnector.getId(), originalList);
      context.put(ActionContextConstants.SELECTED_INDICES, ConnectorHelper
          .getIndicesOf(collectionConnector, elementsToMove));
    }
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the offset.
   * 
   * @param offset
   *            the offset to set.
   */
  public void setOffset(int offset) {
    this.offset = offset;
  }
}
