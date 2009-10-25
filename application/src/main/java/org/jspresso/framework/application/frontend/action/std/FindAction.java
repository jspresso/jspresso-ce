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
package org.jspresso.framework.application.frontend.action.std;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.CreateQueryComponentAction;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.util.collection.ESort;

/**
 * A standard find action. Since it is a chained action, it can be chained with
 * another action.
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
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class FindAction<E, F, G> extends FrontendAction<E, F, G> {

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
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
      context.put(CreateQueryComponentAction.QUERY_MODEL_CONNECTOR,
          queryEntityConnector);
    }
    if (queryEntityConnector != null
        && queryEntityConnector.getConnectorValue() != null) {
      IQueryComponent queryComponent = ((IQueryComponent) queryEntityConnector
          .getConnectorValue());
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
              .intValue()
              + pageOffset.intValue()));
        } else {
          // We are of limits
          return false;
        }
      }
    }
    return super.execute(actionHandler, context);
  }
}
