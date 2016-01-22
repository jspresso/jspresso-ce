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
package org.jspresso.framework.application.frontend.action.remote.mobile;

import java.util.Map;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.remote.AbstractRemoteAction;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorProvider;

/**
 * Selects next / previous element.
 *
 * @author Vincent Vandenschrick
 */
public class NearElementAction extends AbstractRemoteAction {

  /**
   * The constant NAVIGATION_CONNECTOR_KEY.
   */
  public static final String  NAVIGATION_CONNECTOR_KEY = "NAVIGATION_CONNECTOR_KEY";
  /**
   * The constant FETCH_ACTION_KEY.
   */
  public static final String  FETCH_ACTION_KEY         = "FETCH_ACTION_KEY";
  private             boolean reverse                  = false;

  private IAction onSuccessAction;
  private IAction onFailureAction;

  /**
   * Execute boolean.
   *
   * @param actionHandler      the action handler
   * @param context      the context
   * @return the boolean
   */
  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    ICollectionConnector collectionConnector = ((ICollectionConnectorProvider) context.get(NAVIGATION_CONNECTOR_KEY))
        .getCollectionConnector();
    int index = -1;
    int[] selectedIndices = collectionConnector.getSelectedIndices();
    if (selectedIndices != null && selectedIndices.length > 0) {
      index = selectedIndices[0];
      if (isReverse()) {
        index--;
      } else {
        index++;
      }
    }
    if (index >= collectionConnector.getChildConnectorCount()) {
      IAction fetchAction = getFetchAction(context);
      if (fetchAction != null) {
        actionHandler.execute(fetchAction, context);
      }
    }
    if (index >= 0 && index < collectionConnector.getChildConnectorCount()) {
      collectionConnector.setSelectedIndices(index);
      actionHandler.execute(getOnSuccessAction(), context);
    } else {
      actionHandler.execute(getOnFailureAction(), context);
    }
    return super.execute(actionHandler, context);
  }

  /**
   * Gets fetch action.
   *
   * @param context the context
   * @return the fetch action
   */
  protected IAction getFetchAction(Map<String, Object> context) {
    return (IAction) context.get(FETCH_ACTION_KEY);
  }

  /**
   * Is reverse.
   *
   * @return the boolean
   */
  protected boolean isReverse() {
    return reverse;
  }

  /**
   * Sets reverse.
   *
   * @param reverse      the reverse
   */
  public void setReverse(boolean reverse) {
    this.reverse = reverse;
  }

  /**
   * Gets on success action.
   *
   * @return the on success action
   */
  protected IAction getOnSuccessAction() {
    return onSuccessAction;
  }

  /**
   * Sets on success action.
   *
   * @param onSuccessAction      the on success action
   */
  public void setOnSuccessAction(IAction onSuccessAction) {
    this.onSuccessAction = onSuccessAction;
  }

  /**
   * Gets on failure action.
   *
   * @return the on failure action
   */
  protected IAction getOnFailureAction() {
    return onFailureAction;
  }

  /**
   * Sets on failure action.
   *
   * @param onFailureAction      the on failure action
   */
  public void setOnFailureAction(IAction onFailureAction) {
    this.onFailureAction = onFailureAction;
  }
}
