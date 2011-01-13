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
package org.jspresso.framework.application.backend.action.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.frontend.action.std.PageOffsetAction;
import org.jspresso.framework.application.model.FilterableBeanCollectionModule;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.util.bean.BeanComparator;
import org.jspresso.framework.util.collection.ESort;

/**
 * Retrieves the filter of a module and queries the persistent store to populate
 * the module objects. The actual query is delegated to another backend action
 * (defaulted to <code>QueryEntitiesAction</code>) that can be configured
 * through the <code>queryAction</code> property.
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
public class QueryModuleFilterAction<E, F, G> extends FrontendAction<E, F, G> {

  private IAction queryAction;
  private boolean sortOnly = false;

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    if (getQueryAction() != null) {
      FilterableBeanCollectionModule module = (FilterableBeanCollectionModule) getModule(context);
      IQueryComponent queryComponent = getQueryComponent(context);
      context.put(IQueryComponent.QUERY_COMPONENT, queryComponent);
      if (context.containsKey(IQueryComponent.ORDERING_PROPERTIES)) {
        queryComponent.setOrderingProperties((Map<String, ESort>) context
            .get(IQueryComponent.ORDERING_PROPERTIES));
      }
      if (isSortOnly() && module.getPageCount() != null
          && module.getPageCount().intValue() == 1) {
        List<?> moduleObjects = module.getModuleObjects();
        if (moduleObjects != null && !moduleObjects.isEmpty()) {
          List<Object> sortedList = new ArrayList<Object>(moduleObjects);
          Comparator<Object> beanComparator = new BeanComparator(
              queryComponent.getOrderingProperties(), getBackendController(
                  context).getAccessorFactory(), module
                  .getElementComponentDescriptor().getComponentContract());
          Collections.sort(sortedList, beanComparator);
          module.setModuleObjects(sortedList);
        }
      } else {
        Integer pageOffset = (Integer) context
            .get(PageOffsetAction.PAGE_OFFSET);
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
            // We are of limits
            return false;
          }
        }
        if (actionHandler.execute(getQueryAction(), context)) {
          module.setModuleObjects(queryComponent.getQueriedComponents());
          queryComponent.setQueriedComponents(null);
        }
      }
    }
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
    FilterableBeanCollectionModule module = (FilterableBeanCollectionModule) getModule(context);
    return module.getFilter();
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
   *          the sortOnly to set.
   */
  public void setSortOnly(boolean sortOnly) {
    this.sortOnly = sortOnly;
  }
}
