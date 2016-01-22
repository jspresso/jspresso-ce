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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.util.bean.BeanComparator;
import org.jspresso.framework.util.collection.ESort;

/**
 * Abstract base class for QBE find actions.
 *
 * @author Vincent Vandenschrick
 */
public abstract class AbstractQbeAction extends BackendAction {

  /**
   * Pagination action constant.
   */
  public static final String PAGINATE = "PAGINATE";

  private IAction queryAction;
  private boolean sortOnly = false;

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    if (getQueryAction() != null) {
      IQueryComponent queryComponent = getQueryComponent(context);
      if (queryComponent != null) {
        context.put(IQueryComponent.QUERY_COMPONENT, queryComponent);
        if (context.containsKey(IQueryComponent.ORDERING_PROPERTIES)) {
          queryComponent.setOrderingProperties((Map<String, ESort>) context.get(IQueryComponent.ORDERING_PROPERTIES));
        }
        if (isSortOnly()) {
          if (queryComponent.getPageCount() != null) {
            if (queryComponent.getPageCount() == 1) {
              List<?> existingResultList = getExistingResultList(queryComponent, context);
              if (existingResultList != null && !existingResultList.isEmpty()) {
                List<Object> sortedList = new ArrayList<>(existingResultList);
                Comparator<Object> beanComparator = new BeanComparator(queryComponent.getOrderingProperties(),
                    getBackendController(context).getAccessorFactory(), queryComponent.getQueryContract());
                Collections.sort(sortedList, beanComparator);
                queryComponent.setQueriedComponents(sortedList);
                queryPerformed(queryComponent, context);
              }
            } else {
              if (actionHandler.execute(getQueryAction(), context)) {
                queryPerformed(queryComponent, context);
              }
            }
          }
        } else {
          if (!context.containsKey(AbstractQbeAction.PAGINATE)) {
            // This is a plain first query.
            queryComponent.setPage(null);
            queryComponent.setRecordCount(null);
          } else {
            if (queryComponent.getRecordCount() == null || queryComponent.getPageSize() == null) {
              // do not navigate into pages unless a 1st query has been done or
              // pagination is disabled.
              return false;
            }
          }
          if (actionHandler.execute(getQueryAction(), context)) {
            queryPerformed(queryComponent, context);
          }
        }
      }
    }
    return super.execute(actionHandler, context);
  }

  /**
   * Retrieves the query component to use out of the context.
   *
   * @param context
   *     the action context.
   * @return the query component to use for QBE.
   */
  protected abstract IQueryComponent getQueryComponent(Map<String, Object> context);

  /**
   * Retrieves the existing result list out of the action context.
   *
   * @param queryComponent
   *     the query component.
   * @param context
   *     the action context.
   * @return the existing result list if any.
   */
  protected abstract List<?> getExistingResultList(IQueryComponent queryComponent, Map<String, Object> context);

  /**
   * post query hook. The result list is contained by the query component.
   *
   * @param queryComponent
   *     the query component.
   * @param context
   *     the action context.
   */
  protected abstract void queryPerformed(IQueryComponent queryComponent, Map<String, Object> context);

  /**
   * Gets the queryAction.
   *
   * @return the queryAction.
   */
  public IAction getQueryAction() {
    return queryAction;
  }

  /**
   * Configures the query action used to actually perform the entity query.
   *
   * @param queryAction
   *     the queryAction to set.
   */
  public void setQueryAction(IAction queryAction) {
    this.queryAction = queryAction;
  }

  /**
   * Gets the sortOnly.
   *
   * @return the sortOnly.
   */
  protected boolean isSortOnly() {
    return sortOnly;
  }

  /**
   * Sets the sortOnly.
   *
   * @param sortOnly
   *     the sortOnly to set.
   */
  public void setSortOnly(boolean sortOnly) {
    this.sortOnly = sortOnly;
  }

}
