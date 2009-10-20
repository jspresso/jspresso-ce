/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.application.backend.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.ConnectorHelper;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.model.entity.IEntity;


/**
 * An action used in list components to move a detail up and down.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
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
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
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
