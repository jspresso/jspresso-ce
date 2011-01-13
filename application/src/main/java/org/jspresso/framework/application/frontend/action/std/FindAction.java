/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.frontend.action.std;

import java.util.Map;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.CreateQueryComponentAction;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.util.collection.ESort;

/**
 * This action will climb the connector hierarchy to retrieve a query component
 * used as QBE filter. It will then tailor paging status on this query component
 * before continuing execution. This action is meant to be chained with an
 * actual backend action to perform the query (like
 * <code>QueryEntitiesAction</code>).
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
public class FindAction<E, F, G> extends FrontendAction<E, F, G> {

  private IAction queryAction;

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    IQueryComponent queryComponent = getQueryComponent(context);
    if (queryComponent != null) {
      context.put(IQueryComponent.QUERY_COMPONENT, queryComponent);
      if (context.containsKey(IQueryComponent.ORDERING_PROPERTIES)) {
        queryComponent.setOrderingProperties((Map<String, ESort>) context
            .get(IQueryComponent.ORDERING_PROPERTIES));
      }
      Integer pageOffset = (Integer) context.get(PageOffsetAction.PAGE_OFFSET);
      if (pageOffset == null || pageOffset.intValue() == 0) {
        // This is a plain first query.
        queryComponent.setPage(null);
        queryComponent.setRecordCount(null);
      } else {
        if (queryComponent.getRecordCount() == null
            || queryComponent.getPageSize() == null) {
          // do not navigate into pages unless a 1st query has been done or
          // pagination is disabled.
          return false;
        }
        if (queryComponent.getPage() != null
            && queryComponent.getPage().intValue() + pageOffset.intValue() >= 0
            && queryComponent.getPage().intValue() + pageOffset.intValue() < queryComponent
                .getPageCount().intValue()) {
          queryComponent.setPage(new Integer(queryComponent.getPage()
              .intValue() + pageOffset.intValue()));
        } else {
          // We are off limits
          return false;
        }
      }
    }
    actionHandler.execute(getQueryAction(), context);
    return super.execute(actionHandler, context);
  }

  /**
   * Retrieves the query component out of the context.
   * 
   * @param context
   *          the action context.
   * @return the query component.
   */
  protected IQueryComponent getQueryComponent(Map<String, Object> context) {
    IValueConnector queryEntityConnector = (IValueConnector) context
        .get(CreateQueryComponentAction.QUERY_MODEL_CONNECTOR);
    if (queryEntityConnector == null) {
      queryEntityConnector = getViewConnector(context).getModelConnector();
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
      queryComponent = ((IQueryComponent) queryEntityConnector
          .getConnectorValue());
    }
    return queryComponent;
  }

  /**
   * Gets the queryAction.
   * 
   * @return the queryAction.
   */
  protected IAction getQueryAction() {
    return queryAction;
  }

  /**
   * Configures the query action used to actually perform the entity query.
   * 
   * @param queryAction
   *          the queryAction to set.
   */
  public void setQueryAction(IAction queryAction) {
    this.queryAction = queryAction;
  }
}
