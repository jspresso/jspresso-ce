/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.IValueConnector;

/**
 * This class can be used as parent class for actions that can be registered
 * either on collection or singleton models.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
 * @version $LastChangedRevision: 1671 $
 * @author Vincent Vandenschrick
 */
public abstract class AbstractPolymorphicAction extends AbstractBackendAction {

  /**
   * Gets the selected indices from the context. it uses the
   * <code>ActionContextConstants.SELECTED_INDICES</code> key.
   * 
   * @param context
   *          the action context.
   * @return the selected indices if any.
   */
  public int[] getSelectedIndices(Map<String, Object> context) {
    return (int[]) context.get(ActionContextConstants.SELECTED_INDICES);
  }

  /**
   * Gets the selected objects from the backend connector and the context. If
   * the action is backed by a collection connector the method will use the
   * context selected indices to compute the selected objects collection.
   * Otherwise, it will simply return a singleton list holding the value of the
   * backend connector.
   * 
   * @param context
   *          the action context.
   * @return the list of selected objects.
   */
  public List<?> getSelectedObjects(Map<String, Object> context) {
    IValueConnector modelConnector = getModelConnector(context);
    List<Object> selectedObjects;
    if (modelConnector == null) {
      return null;
    } else if (modelConnector instanceof ICollectionConnector) {
      int[] selectedIndices = getSelectedIndices(context);
      ICollectionConnector collectionConnector = (ICollectionConnector) modelConnector;
      if (selectedIndices == null || selectedIndices.length == 0) {
        return null;
      }
      selectedObjects = new ArrayList<Object>();
      for (int i = 0; i < selectedIndices.length; i++) {
        Object element = collectionConnector.getChildConnector(
            selectedIndices[i]).getConnectorValue();
        selectedObjects.add(element);
      }
    } else {
      selectedObjects = new ArrayList<Object>();
      selectedObjects.add(modelConnector.getConnectorValue());
    }
    return selectedObjects;
  }
}
