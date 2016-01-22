/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.model.component.IComponent;

/**
 * This action can be declared on views that are backed by collections with list
 * semantics (indexed collections). It allows to take a the selected elements
 * and move them in the collection using a configured offset. It allows for
 * re-ordering the list.
 *
 * @author Vincent Vandenschrick
 */
public class CollectionElementMoveAction extends AbstractCollectionAction {

  private boolean toTop;
  private boolean toBottom;
  private int     offset;

  /**
   * Constructs a new {@code CollectionElementMoveAction} instance.
   */
  public CollectionElementMoveAction() {
    toTop = false;
    toBottom = false;
    offset = 0;
  }

  /**
   * Retrieves the master and its managed collection from the model connector
   * then it moves the selected element of the offset.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {

    if (toTop && toBottom) {
      throw new ActionException(
          "Illegal use of toTop AND toBottom at the same time");
    }

    if (!List.class.isAssignableFrom(getModelDescriptor(context)
        .getCollectionDescriptor().getCollectionInterface())) {
      return false;
    }

    ICollectionConnector collectionConnector = getModelConnector(context);
    if (collectionConnector == null) {
      return false;
    }

    int[] indicesToMove = getSelectedIndices(context);
    if (indicesToMove == null || indicesToMove.length == 0) {
      return false;
    }

    List<?> elementsToMove = getSelectedModels(context);
    if (elementsToMove == null || elementsToMove.size() == 0) {
      return false;
    }

    List<Object> targetList = new ArrayList<>(
        (List<?>) collectionConnector.getConnectorValue());
    int size = targetList.size();
    List<Object> headList = new ArrayList<>();
    List<Object> tailList = new ArrayList<>();

    int executeOffset = offset;
    if (toTop) {
      executeOffset = -size;
    } else if (toBottom) {
      executeOffset = size;
    }

    int[] targetIndices = new int[indicesToMove.length];
    for (int i = indicesToMove.length - 1; i >= 0; i--) {
      targetIndices[i] = indicesToMove[i] + executeOffset;
    }

    for (int i = indicesToMove.length - 1; i >= 0; i--) {
      targetList.remove(indicesToMove[i]);
    }
    for (int i = 0; i < indicesToMove.length; i++) {
      if (targetIndices[i] < 0) {
        headList.add(elementsToMove.get(i));
      } else if (targetIndices[i] > size - 1) {
        tailList.add(elementsToMove.get(i));
      } else {
        targetList.add(targetIndices[i], elementsToMove.get(i));
      }
    }

    targetList.addAll(0, headList);
    targetList.addAll(tailList);

    Object master = collectionConnector.getParentConnector()
        .getConnectorValue();
    Class<?> targetContract;
    if (master instanceof IComponent) {
      targetContract = ((IComponent) master).getComponentContract();
    } else {
      targetContract = master.getClass();
    }
    try {
      getAccessorFactory(context).createPropertyAccessor(
          collectionConnector.getId(), targetContract).setValue(master,
          targetList);
    } catch (IllegalAccessException | NoSuchMethodException ex) {
      throw new ActionException(ex);
    } catch (InvocationTargetException ex) {
      if (ex.getTargetException() instanceof RuntimeException) {
        throw (RuntimeException) ex.getTargetException();
      }
      throw new ActionException(ex);
    }
    setSelectedModels(elementsToMove, context);

    return super.execute(actionHandler, context);
  }

  /**
   * Configures the offset to use when moving the selected elements inside the
   * list. A configured offset of <b>1</b> will increase (move down) by one the
   * selected elements indices whereas an offset of <b>-1</b> will decrease
   * (move up) the selected elements indices.
   *
   * @param offset
   *          the offset to set.
   */
  public void setOffset(int offset) {
    this.offset = offset;
    if (offset != 0) {
      this.toTop = false;
      this.toBottom = false;
    }
  }

  /**
   * Configures this action to move the selected elements to the top of the
   * list.
   *
   * @param toTop
   *          the toTop to set.
   */
  public void setToTop(boolean toTop) {
    this.toTop = toTop;
    if (toTop) {
      this.toBottom = false;
      this.offset = 0;
    }
  }

  /**
   * Configures this action to move the selected elements to the bottom of the
   * list.
   *
   * @param toBottom
   *          the toBottom to set.
   */
  public void setToBottom(boolean toBottom) {
    this.toBottom = toBottom;
    if (toBottom) {
      this.toTop = false;
      this.offset = 0;
    }
  }
}
