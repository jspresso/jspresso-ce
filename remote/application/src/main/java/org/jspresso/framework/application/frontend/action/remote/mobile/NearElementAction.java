/*
 * Copyright (c) 2005-2014 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.remote.AbstractRemoteAction;
import org.jspresso.framework.application.frontend.command.remote.RemoteEditCommand;
import org.jspresso.framework.application.frontend.controller.remote.mobile.MobileRemoteController;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorProvider;

/**
 * Selects next / previous element.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision$
 */
public class NearElementAction extends AbstractRemoteAction {

  /**
   * The constant NAVIGATION_CONNECTOR_KEY.
   */
  public static final String  NAVIGATION_CONNECTOR_KEY = "NAVIGATION_CONNECTOR_KEY";
  private             boolean reverse                  = false;

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
    if (index >= 0 && index < collectionConnector.getChildConnectorCount()) {
      collectionConnector.setSelectedIndices(index);
    }
    return super.execute(actionHandler, context);
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
   * @param reverse the reverse
   */
  public void setReverse(boolean reverse) {
    this.reverse = reverse;
  }
}
