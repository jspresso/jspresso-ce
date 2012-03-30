/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.frontend.action;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.ICollectionConnector;

/**
 * This is a very generic action that takes an array of indices (
 * <code>ActionContextConstants.SELECTED_INDICES</code>) out of the action
 * context and selects these indices on the view. This action is, obviously,
 * designed to be used in an action chained installed on a collection view.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class ConnectorSelectionAction<E, F, G> extends FrontendAction<E, F, G> {

  private boolean editSelection = false;

  /**
   * Selects indices on the view collection connector based on the
   * <code>ActionContextConstants.SELECTED_INDICES</code> context value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    ICollectionConnector collectionConnector = (ICollectionConnector) getViewConnector(context);
    if (collectionConnector != null) {
      int[] connectorSelection = getSelectedIndices(context);
      collectionConnector.setSelectedIndices(connectorSelection);
      if (editSelection && connectorSelection != null
          && connectorSelection.length == 1) {
        E component = getView(context).getPeer();
        getFrontendController(context).edit(component);
      }
    }
    return super.execute(actionHandler, context);
  }

  /**
   * Gets the editSelection.
   * 
   * @return the editSelection.
   */
  public boolean isEditSelection() {
    return editSelection;
  }

  /**
   * Sets the editSelection.
   * 
   * @param editSelection
   *          the editSelection to set.
   */
  public void setEditSelection(boolean editSelection) {
    this.editSelection = editSelection;
  }
}
