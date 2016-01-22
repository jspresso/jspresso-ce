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

import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.component.query.QueryComponentMatcher;

/**
 * This action filters an arbitrary component list against the query component using a query component matcher.
 *
 * @author Vincent Vandenschrick
 */
public class StaticQueryComponentsAction extends AbstractQueryComponentsAction {

  /**
   * The constant COMPONENT_STORE_KEY.
   */
  public static final String COMPONENT_STORE_KEY = "COMPONENT_STORE";

  private List<?> componentStore;

  /**
   * Filters the configured collection using a query component matcher.
   *
   * @param queryComponent
   *     the query component.
   * @param context
   *     the action context
   * @return the list of retrieved components.
   */
  @Override
  public List<?> performQuery(IQueryComponent queryComponent, Map<String, Object> context) {
    QueryComponentMatcher matcher = new QueryComponentMatcher(queryComponent, getAccessorFactory(context), false, true);
    return matcher.filterCollection(getComponentStore(context));
  }

  /**
   * Gets component store.
   *
   * @param context the context
   * @return the component store
   */
  protected List<?> getComponentStore(Map<String, Object> context) {
    if (context.containsKey(COMPONENT_STORE_KEY)) {
      return (List<?>) context.get(COMPONENT_STORE_KEY);
    }
    return componentStore;
  }

  /**
   * Sets component store.
   *
   * @param componentStore
   *     the component store
   */
  public void setComponentStore(List<?> componentStore) {
    this.componentStore = componentStore;
  }
}
