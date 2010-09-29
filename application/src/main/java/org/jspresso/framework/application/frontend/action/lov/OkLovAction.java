/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.frontend.action.lov;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.entity.IEntity;

/**
 * This action augments the context by setting the action parameter to the
 * selected entity of the LOV result list (or null if none is selected).
 * 
 * @version $LastChangedRevision: 1442 $
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class OkLovAction<E, F, G> extends FrontendAction<E, F, G> {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    ICollectionConnector resultConnector;
    IValueConnector viewConnector = getViewConnector(context);
    // to support double click on the table
    if (viewConnector instanceof ICollectionConnector) {
      // this is from the table itself
      resultConnector = (ICollectionConnector) viewConnector;
    } else {
      // this is from the dialog.
      resultConnector = (ICollectionConnector) ((ICompositeValueConnector) viewConnector)
          .getChildConnector(IQueryComponent.QUERIED_COMPONENTS);
    }
    int[] resultSelectedIndices = resultConnector.getSelectedIndices();
    if (resultSelectedIndices != null && resultSelectedIndices.length > 0) {
      Object selectedElement = resultConnector.getChildConnector(
          resultSelectedIndices[0]).getConnectorValue();
      if (selectedElement != null && selectedElement instanceof IEntity) {
        selectedElement = getController(context).getBackendController().merge(
            (IEntity) selectedElement, EMergeMode.MERGE_CLEAN_LAZY);
      }
      setActionParameter(selectedElement, context);
    } else {
      setActionParameter(null, context);
    }
    return super.execute(actionHandler, context);
  }
}
