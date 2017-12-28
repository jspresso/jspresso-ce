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
package org.jspresso.framework.application.frontend.action.module;

import java.util.Map;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.AbstractQueryComponentsAction;
import org.jspresso.framework.application.backend.action.IQueryComponentRefiner;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.model.FilterableBeanCollectionModule;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.model.component.IQueryComponent;

/**
 * Queries filter module and notify user of empty record set.
 *
 * @param <E>
 *     the actual gui component type used.
 * @param <F>
 *     the actual icon type used.
 * @param <G>
 *     the actual action type used.
 * @author Vincent Vandenschrick
 */
public class QueryFilterModuleAction<E, F, G> extends FrontendAction<E, F, G> {

  private IAction                emptyResultAction;
  private IQueryComponentRefiner queryComponentRefiner;
  private Object                 criteriaRefiner;
  private Object                 criteriaFactory;

  /**
   * Execute boolean.
   *
   * @param actionHandler
   *     the action handler
   * @param context
   *     the context
   * @return the boolean
   */
  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    Module module = getModule(context);
    if (module instanceof FilterableBeanCollectionModule) {
      IQueryComponent filter = ((FilterableBeanCollectionModule) module).getFilter();
      if (filter != null) {
        if (queryComponentRefiner != null) {
          filter.put(AbstractQueryComponentsAction.COMPONENT_REFINER, queryComponentRefiner);
        }
        if (criteriaFactory != null) {
          filter.put(AbstractQueryComponentsAction.CRITERIA_FACTORY, criteriaFactory);
        }
        if (criteriaRefiner != null) {
          filter.put(AbstractQueryComponentsAction.CRITERIA_REFINER, criteriaRefiner);
        }
      }
    }
    boolean success = super.execute(actionHandler, context);
    if (module instanceof FilterableBeanCollectionModule) {
      if (((FilterableBeanCollectionModule) module).getModuleObjects() == null
          || ((FilterableBeanCollectionModule) module).getModuleObjects().isEmpty()) {
        actionHandler.execute(emptyResultAction, context);
      }
    }
    return success;
  }

  /**
   * Gets empty result action.
   *
   * @return the empty result action
   */
  protected IAction getEmptyResultAction() {
    return emptyResultAction;
  }

  /**
   * Sets empty result action.
   *
   * @param emptyResultAction
   *     the empty result action
   */
  public void setEmptyResultAction(IAction emptyResultAction) {
    this.emptyResultAction = emptyResultAction;
  }


  /**
   * Sets query component refiner.
   *
   * @param queryComponentRefiner
   *     the query component refiner
   */
  public void setQueryComponentRefiner(IQueryComponentRefiner queryComponentRefiner) {
    this.queryComponentRefiner = queryComponentRefiner;
  }

  /**
   * Sets criteria factory. Depending on the persistence layer used, it should be an instance of :
   * <ul>
   * <li>org.jspresso.framework.model.persistence.hibernate.criterion.ICriteriaFactory</li>
   * <li>org.jspresso.framework.model.persistence.mongo.criterion.IQueryFactory</li>
   * </ul>
   *
   * @param criteriaFactory
   *     the criteria factory
   */
  public void setCriteriaFactory(Object criteriaFactory) {
    this.criteriaFactory = criteriaFactory;
  }

  /**
   * Sets criteria refiner. Depending on the persistence layer used, it should be an instance of :
   * <ul>
   * <li>org.jspresso.framework.application.backend.action.persistence.hibernate.ICriteriaRefiner</li>
   * <li>org.jspresso.framework.application.backend.action.persistence.mongo.IQueryRefiner</li>
   * </ul>
   *
   * @param criteriaRefiner
   *     the criteria refiner
   */
  public void setCriteriaRefiner(Object criteriaRefiner) {
    this.criteriaRefiner = criteriaRefiner;
  }

}
