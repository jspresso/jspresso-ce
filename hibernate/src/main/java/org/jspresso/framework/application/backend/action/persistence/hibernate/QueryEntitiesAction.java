/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.backend.action.persistence.hibernate;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Subqueries;
import org.hibernate.transform.ResultTransformer;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.backend.action.IQueryComponentRefiner;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.persistence.hibernate.criterion.EnhancedDetachedCriteria;
import org.jspresso.framework.model.persistence.hibernate.criterion.ICriteriaFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

/**
 * This action is used to Hibernate query entities by example. It is used behind
 * the scene in several places in Jspresso based applications, as in filtered
 * bean collection modules, list of values, ... The principles are to tailor an
 * Hibernate Criterion based on the Jspresso &quot;<code>IQueryComponent</code>
 * &quot;. A Jspresso query component is a hierarchical datastructure that
 * mimics a portion of the domain model headed by an entity. It is essentially a
 * set of property/value pairs where values can be :
 * <ol>
 * <li>a scalar value</li>
 * <li>a comparable query structure (operator, inf and sup value) to place a
 * constraint on a comparable property (date, number, ...)</li>
 * <li>a sub query component</li>
 * </ol>
 * <p>
 * Out of this query component, the action will build an Hibernate detached
 * criteria by constructing all join sub-criteria whenever necessary.
 * <p>
 * Once the detached criteria is complete, the action will perform the Hibernate
 * query while using paging informations taken from the query component as well
 * as custom sorting properties.
 * <p>
 * Whenever the query is successful, the result is merged back to the
 * application session and assigned to the query component
 * <code>queriedComponents</code> property.
 * <p>
 * Note that there are 2 hooks that can be configured by injection to fine-tune
 * the performed query : <code>queryComponentRefiner</code> and
 * <code>criteriaRefiner</code>.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class QueryEntitiesAction extends AbstractHibernateAction {

  private ICriteriaFactory       criteriaFactory;
  private ICriteriaRefiner       criteriaRefiner;
  private IQueryComponentRefiner queryComponentRefiner;
  private EMergeMode             mergeMode;

  private static final String    CRITERIA_FACTORY  = "CRITERIA_FACTORY";
  private static final String    CRITERIA_REFINER  = "CRITERIA_REFINER";
  private static final String    COMPONENT_REFINER = "COMPONENT_REFINER";

  /**
   * Constructs a new <code>QueryEntitiesAction</code> instance.
   */
  public QueryEntitiesAction() {
    mergeMode = EMergeMode.MERGE_LAZY;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      final Map<String, Object> context) {
    final IQueryComponent queryComponent = getQueryComponent(context);
    Set<Object> queriedComponents;

    if (getController(context).isUnitOfWorkActive()) {
      // Ignore merge mode since we are in a TX
      queriedComponents = doQuery(queryComponent, context, null);
      // The queried components are assigned to the query component inside the
      // TX since they are not merged.
      queryComponent.setQueriedComponents(new ArrayList<Object>(
          queriedComponents));
    } else {
      final EMergeMode localMergeMode = getMergeMode();
      queriedComponents = getTransactionTemplate(context).execute(
          new TransactionCallback<Set<Object>>() {

            @Override
            public Set<Object> doInTransaction(TransactionStatus status) {
              Set<Object> txQueriedComponents = doQuery(queryComponent,
                  context, localMergeMode);
              status.setRollbackOnly();
              if (localMergeMode == null) {
                // The queried components are assigned to the query component
                // inside
                // the
                // TX since they are not merged.
                queryComponent.setQueriedComponents(new ArrayList<Object>(
                    txQueriedComponents));
              }
              return txQueriedComponents;
            }
          });
      if (localMergeMode != null) {
        // The queried components are assigned to the query component outside of
        // the
        // TX since they have been merged into the session.
        queryComponent.setQueriedComponents(new ArrayList<Object>(
            queriedComponents));
      }
    }
    return super.execute(actionHandler, context);
  }

  private Set<Object> doQuery(IQueryComponent queryComponent,
      Map<String, Object> context, EMergeMode localMergeMode) {

    IQueryComponentRefiner compRefiner = (IQueryComponentRefiner) queryComponent
        .get(COMPONENT_REFINER);

    if (compRefiner == null && queryComponentRefiner != null) {
      queryComponent.put(COMPONENT_REFINER, queryComponentRefiner);
      compRefiner = queryComponentRefiner;
    }
    if (compRefiner != null) {
      compRefiner.refineQueryComponent(queryComponent, context);
    }

    List<?> queriedComponents = performQuery(queryComponent, context);
    Set<Object> mergedComponents = new LinkedHashSet<Object>();

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
            mergedComponents.add(controller.merge((IEntity) nextComponent,
                localMergeMode));
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
   * Performs actual Query. This method can be overriden by subclasses in order
   * to deal with non-Hibernate searches.
   * 
   * @param queryComponent
   *          the query component.
   * @param context
   *          the action context
   * @return the liste of retrieved components.
   */
  public List<?> performQuery(final IQueryComponent queryComponent,
      final Map<String, Object> context) {
    Session hibernateSession = getHibernateSession(context);
    ICriteriaFactory critFactory = (ICriteriaFactory) queryComponent
        .get(CRITERIA_FACTORY);
    if (critFactory == null) {
      queryComponent.put(CRITERIA_FACTORY, getCriteriaFactory());
      critFactory = getCriteriaFactory();
    }
    EnhancedDetachedCriteria criteria = critFactory.createCriteria(
        queryComponent, context);
    List<?> entities;
    if (criteria == null) {
      entities = new ArrayList<IEntity>();
      queryComponent.setRecordCount(Integer.valueOf(0));
    } else {
      ICriteriaRefiner critRefiner = (ICriteriaRefiner) queryComponent
          .get(CRITERIA_REFINER);
      if (critRefiner == null && getCriteriaRefiner() != null) {
        queryComponent.put(CRITERIA_REFINER, getCriteriaRefiner());
        critRefiner = getCriteriaRefiner();
      }
      if (critRefiner != null) {
        critRefiner.refineCriteria(criteria, queryComponent, context);
      }
      Integer totalCount = null;
      Integer pageSize = queryComponent.getPageSize();
      Integer page = queryComponent.getPage();

      ResultTransformer refinerResultTransformer = criteria
          .getResultTransformer();
      List<Order> refinerOrders = criteria.getOrders();
      if (refinerOrders != null) {
        criteria.removeAllOrders();
      }

      if (queryComponent.isDistinctEnforced()) {
        criteria.setProjection(Projections.distinct(Projections.id()));
        EnhancedDetachedCriteria outerCriteria = EnhancedDetachedCriteria
            .forEntityName(queryComponent.getQueryContract().getName());
        outerCriteria.add(Subqueries.propertyIn(IEntity.ID, criteria));
        criteria = outerCriteria;
      }

      if (pageSize != null) {
        if (page == null) {
          page = Integer.valueOf(0);
          queryComponent.setPage(page);
        }
        if (queryComponent.getRecordCount() == null) {
          criteria.setProjection(Projections.rowCount());
          totalCount = Integer.valueOf(((Number) criteria
              .getExecutableCriteria(hibernateSession).list().get(0))
              .intValue());
        }
        if (refinerOrders != null) {
          for (Order order : refinerOrders) {
            criteria.addOrder(order);
          }
        }
        critFactory.completeCriteriaWithOrdering(criteria, queryComponent,
            context);
        if (refinerResultTransformer != null) {
          criteria.setResultTransformer(refinerResultTransformer);
        }
        entities = criteria.getExecutableCriteria(hibernateSession)
            .setFirstResult(page.intValue() * pageSize.intValue())
            .setMaxResults(pageSize.intValue()).list();
      } else {
        if (refinerOrders != null) {
          for (Order order : refinerOrders) {
            criteria.addOrder(order);
          }
        }
        critFactory.completeCriteriaWithOrdering(criteria, queryComponent,
            context);
        if (refinerResultTransformer != null) {
          criteria.setResultTransformer(refinerResultTransformer);
        }
        entities = criteria.getExecutableCriteria(hibernateSession).list();
        totalCount = Integer.valueOf(entities.size());
      }
      if (totalCount != null) {
        queryComponent.setRecordCount(totalCount);
      }
    }
    return entities;
  }

  /**
   * Sets the criteriaFactory.
   * 
   * @param criteriaFactory
   *          the criteriaFactory to set.
   */
  public void setCriteriaFactory(ICriteriaFactory criteriaFactory) {
    this.criteriaFactory = criteriaFactory;
  }

  /**
   * Configures a criteria refiner that will be called before the Hibernate
   * detached criteria is actually used to perform the query. It allows to
   * complement the criteria with arbitrary complex clauses that cannot be
   * simply expressed in a &quot;<i>Query by Example</i>&quot; semantics.
   * 
   * @param criteriaRefiner
   *          the criteriaRefiner to set.
   */
  public void setCriteriaRefiner(ICriteriaRefiner criteriaRefiner) {
    this.criteriaRefiner = criteriaRefiner;
  }

  /**
   * Retrieves the configured criteria refiner.
   * 
   * @return the configured criteria refiner.
   */
  public ICriteriaRefiner getCriteriaRefiner() {
    return this.criteriaRefiner;
  }

  /**
   * Configures a query component refiner that will be called before the query
   * component is processed to extract the Hibernate detached criteria. This
   * allows for instance to force query values.
   * 
   * @param queryComponentRefiner
   *          the queryComponentRefiner to set.
   */
  public void setQueryComponentRefiner(
      IQueryComponentRefiner queryComponentRefiner) {
    this.queryComponentRefiner = queryComponentRefiner;
  }

  /**
   * Gets the criteriaFactory.
   * 
   * @return the criteriaFactory.
   */
  protected ICriteriaFactory getCriteriaFactory() {
    return criteriaFactory;
  }

  /**
   * Retrieves the query component from the context.
   * 
   * @param context
   *          the action context.
   * @return the query component.
   */
  protected IQueryComponent getQueryComponent(Map<String, Object> context) {
    IQueryComponent queryComponent = (IQueryComponent) context
        .get(IQueryComponent.QUERY_COMPONENT);
    return queryComponent;
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
   * filter query component. A <code>null</code> value means that the queried
   * components will assigned without being merged at all. In that case, the
   * merging has to be performed later on in the action chain. Forgetting to do
   * so will lead to unexpected results. Default value is
   * <code>EMergeMode.MERGE_CLEAN_LAZY</code>.
   * 
   * @param mergeMode
   *          the mergeMode to set.
   */
  public void setMergeMode(EMergeMode mergeMode) {
    this.mergeMode = mergeMode;
  }
}
