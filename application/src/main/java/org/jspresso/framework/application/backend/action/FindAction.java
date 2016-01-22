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

import java.util.List;
import java.util.Map;

import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.component.IQueryComponent;

/**
 * This action will climb the model connector hierarchy to retrieve a query
 * component used as QBE filter. It will then tailor paging status on this query
 * component before continuing execution. This action is meant to be chained
 * with an actual backend action to perform the query (like
 * {@code QueryEntitiesAction}).
 *
 * @author Vincent Vandenschrick
 */
public class FindAction extends AbstractQbeAction {

  /**
   * Retrieves the query component out of the context.
   *
   * @param context
   *          the action context.
   * @return the query component.
   */
  @Override
  protected IQueryComponent getQueryComponent(Map<String, Object> context) {
    IValueConnector queryEntityConnector = (IValueConnector) context
        .get(CreateQueryComponentAction.QUERY_MODEL_CONNECTOR);
    if (queryEntityConnector == null) {
      queryEntityConnector = getModelConnector(context);
      while (queryEntityConnector != null
          && !(queryEntityConnector.getConnectorValue() instanceof IQueryComponent)) {
        // climb the connector hierarchy to retrieve the IQueryComponent
        // connector.
        queryEntityConnector = queryEntityConnector.getParentConnector();
      }
    }
    IQueryComponent queryComponent = null;
    if (queryEntityConnector != null
        && queryEntityConnector.getConnectorValue() != null) {
      queryComponent = queryEntityConnector
          .getConnectorValue();
    }
    return queryComponent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected List<?> getExistingResultList(IQueryComponent queryComponent,
      Map<String, Object> context) {
    return queryComponent.getQueriedComponents();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void queryPerformed(IQueryComponent queryComponent,
      Map<String, Object> context) {
    // NO-OP.
  }
}
