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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.entity.IEntity;

/**
 * This action is the base abstract class to query components by example. It is used behind
 * the scene in several places in Jspresso based applications, as in filtered
 * bean collection modules, list of values, ... The principles are to perform a search
 * based on the Jspresso &quot;{@code IQueryComponent}
 * &quot;. A Jspresso query component is a hierarchical data structure that
 * mimics a portion of the domain model headed by an entity. It is essentially a
 * set of property/value pairs where values can be :
 * <ol>
 * <li>a scalar value</li>
 * <li>a comparable query structure (operator, inf and sup value) to place a
 * constraint on a comparable property (date, number, ...)</li>
 * <li>a sub query component</li>
 * </ol>
 * <p/>
 * Whenever the query is successful, the result is merged back to the
 * application session and assigned to the query component
 * {@code queriedComponents} property.
 * <p/>
 * Note that there is 1 hook that can be configured by injection to fine-tune
 * the performed query : {@code queryComponentRefiner}.
 *
 * @author Vincent Vandenschrick
 */
public abstract class AbstractQueryComponentsAction extends BackendAction {

  private static final String COMPONENT_REFINER = "COMPONENT_REFINER";
  private IQueryComponentRefiner queryComponentRefiner;
  private EMergeMode             mergeMode;
  private boolean                useCountForPagination;

  /**
   * Constructs a new {@code AbstractQueryComponentsAction} instance.
   */
  public AbstractQueryComponentsAction() {
    mergeMode = EMergeMode.MERGE_LAZY;
    useCountForPagination = true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler, final Map<String, Object> context) {
    final IQueryComponent queryComponent = getQueryComponent(context);
    Set<Object> queriedComponents;

    if (getController(context).isUnitOfWorkActive()) {
      // Ignore merge mode since we are in a TX
      queriedComponents = doQuery(queryComponent, context, null);
      // The queried components are assigned to the query component inside the
      // TX since they are not merged.
      queryComponent.setQueriedComponents(new ArrayList<>(queriedComponents));
    } else {
      final EMergeMode localMergeMode = getMergeMode();
      queriedComponents = getTransactionTemplate(context).execute(new TransactionCallback<Set<Object>>() {

        @Override
        public Set<Object> doInTransaction(TransactionStatus status) {
          Set<Object> txQueriedComponents = doQuery(queryComponent, context, localMergeMode);
          status.setRollbackOnly();
          if (localMergeMode == null) {
            // The queried components are assigned to the query component
            // inside
            // the
            // TX since they are not merged.
            queryComponent.setQueriedComponents(new ArrayList<>(txQueriedComponents));
          }
          return txQueriedComponents;
        }
      });
      if (localMergeMode != null) {
        // The queried components are assigned to the query component outside of
        // the
        // TX since they have been merged into the session.
        queryComponent.setQueriedComponents(new ArrayList<>(queriedComponents));
      }
    }
    return super.execute(actionHandler, context);
  }

  private Set<Object> doQuery(IQueryComponent queryComponent, Map<String, Object> context, EMergeMode localMergeMode) {

    IQueryComponentRefiner compRefiner = (IQueryComponentRefiner) queryComponent.get(COMPONENT_REFINER);

    if (compRefiner == null) {
      compRefiner = getQueryComponentRefiner(context);
      if (compRefiner != null) {
        queryComponent.put(COMPONENT_REFINER, compRefiner);
      }
    }
    if (compRefiner != null) {
      compRefiner.refineQueryComponent(queryComponent, context);
    }

    List<?> queriedComponents = performQuery(queryComponent, context);
    Set<Object> mergedComponents = new LinkedHashSet<>();

    List<?> stickyResults = queryComponent.getStickyResults();
    if (stickyResults != null) {
      for (Object nextComponent : stickyResults) {
        mergedComponents.add(nextComponent);
      }
    }

    IBackendController controller = getController(context);
    for (Object nextComponent : queriedComponents) {
      if (nextComponent instanceof IEntity) {
        if (!controller.isEntityRegisteredForDeletion((IEntity) nextComponent)) {
          if (localMergeMode != null) {
            mergedComponents.add(controller.merge((IEntity) nextComponent, localMergeMode));
          } else {
            mergedComponents.add(nextComponent);
          }
        }
      } else {
        mergedComponents.add(nextComponent);
      }
    }
    return mergedComponents;
  }

  /**
   * Performs actual Query. This method must be overridden by subclasses in order
   * to deal with any type of searches.
   *
   * @param queryComponent
   *     the query component.
   * @param context
   *     the action context
   * @return the list of retrieved components.
   */
  public abstract List<?> performQuery(final IQueryComponent queryComponent, final Map<String, Object> context);

  /**
   * Retrieves the query component from the context.
   *
   * @param context
   *     the action context.
   * @return the query component.
   */
  protected IQueryComponent getQueryComponent(Map<String, Object> context) {
    IQueryComponent queryComponent = (IQueryComponent) context.get(IQueryComponent.QUERY_COMPONENT);
    return queryComponent;
  }

  /**
   * Configures a query component refiner that will be called before the query
   * component is processed to extract the Hibernate detached criteria. This
   * allows for instance to force query values.
   *
   * @param queryComponentRefiner
   *     the queryComponentRefiner to set.
   */
  public void setQueryComponentRefiner(IQueryComponentRefiner queryComponentRefiner) {
    this.queryComponentRefiner = queryComponentRefiner;
  }

  /**
   * Gets query component refiner.
   *
   * @param context
   *     the action context.
   * @return the query component refiner.
   */
  public IQueryComponentRefiner getQueryComponentRefiner(Map<String, Object> context) {
    if (context.get(COMPONENT_REFINER) != null) {
      return (IQueryComponentRefiner) context.get(COMPONENT_REFINER);
    }
    return queryComponentRefiner;
  }

  /**
   * Gets the mergeMode.
   *
   * @return the mergeMode.
   */
  protected EMergeMode getMergeMode() {
    return mergeMode;
  }

  /**
   * Sets the mergeMode to use when assigning the queried components to the
   * filter query component. A {@code null} value means that the queried
   * components will assigned without being merged at all. In that case, the
   * merging has to be performed later on in the action chain. Forgetting to do
   * so will lead to unexpected results. Default value is
   * {@code EMergeMode.MERGE_CLEAN_LAZY}.
   *
   * @param mergeMode
   *     the mergeMode to set.
   */
  public void setMergeMode(EMergeMode mergeMode) {
    this.mergeMode = mergeMode;
  }

  /**
   * Sets use count for pagination.
   *
   * @param useCountForPagination the use count for pagination
   */
  public void setUseCountForPagination(boolean useCountForPagination) {
    this.useCountForPagination = useCountForPagination;
  }

  /**
   * Is use count for pagination.
   *
   * @return the boolean
   */
  protected boolean isUseCountForPagination() {
    return useCountForPagination;
  }
}
