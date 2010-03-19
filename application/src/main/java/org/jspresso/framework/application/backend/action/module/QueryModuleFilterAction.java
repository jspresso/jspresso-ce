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
package org.jspresso.framework.application.backend.action.module;

import java.util.Map;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.BackendAction;
import org.jspresso.framework.application.frontend.action.std.PageOffsetAction;
import org.jspresso.framework.application.model.FilterableBeanCollectionModule;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.util.collection.ESort;

/**
 * Retrieves the filter of a module and queries the persistent store to populate
 * the module objects.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class QueryModuleFilterAction extends BackendAction {

  private IAction queryAction;

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    if (queryAction != null) {
      FilterableBeanCollectionModule module = (FilterableBeanCollectionModule) getModule(context);
      context.put(IQueryComponent.QUERY_COMPONENT, module.getFilter());
      IQueryComponent queryComponent = module.getFilter();
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
      if (actionHandler.execute(queryAction, context)) {
        module.setModuleObjects(queryComponent.getQueriedComponents());
        queryComponent.setQueriedComponents(null);
      }
    }
    return super.execute(actionHandler, context);
  }

  /**
   * Gets the queryAction.
   * 
   * @return the queryAction.
   */
  public IAction getQueryAction() {
    return queryAction;
  }

  /**
   * Sets the queryAction.
   * 
   * @param queryAction
   *          the queryAction to set.
   */
  public void setQueryAction(IAction queryAction) {
    this.queryAction = queryAction;
  }

}
