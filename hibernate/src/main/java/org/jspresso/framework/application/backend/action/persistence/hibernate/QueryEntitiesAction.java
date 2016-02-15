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
package org.jspresso.framework.application.backend.action.persistence.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jspresso.framework.application.backend.action.AbstractQueryComponentsAction;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.persistence.hibernate.criterion.EnhancedDetachedCriteria;
import org.jspresso.framework.model.persistence.hibernate.criterion.ICriteriaFactory;
import org.jspresso.framework.util.accessor.IAccessorFactory;

/**
 * This action is used to Hibernate query entities by example. It is used behind
 * the scene in several places in Jspresso based applications, as in filtered
 * bean collection modules, list of values, ... The principles are to tailor an
 * Hibernate Criterion based on the Jspresso &quot;{@code IQueryComponent}
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
 * Out of this query component, the action will build an Hibernate detached
 * criteria by constructing all join sub-criteria whenever necessary.
 * <p/>
 * Once the detached criteria is complete, the action will perform the Hibernate
 * query while using paging information taken from the query component as well
 * as custom sorting properties.
 * <p/>
 * Whenever the query is successful, the result is merged back to the
 * application session and assigned to the query component
 * {@code queriedComponents} property.
 * <p/>
 * Note that there are 2 hooks that can be configured by injection to fine-tune
 * the performed query : {@code queryComponentRefiner} and
 * {@code criteriaRefiner}.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision : 9166 $
 */
public class QueryEntitiesAction extends AbstractQueryComponentsAction {

  private static final Logger LOG = LoggerFactory.getLogger(QueryEntitiesAction.class);

  private static final String CRITERIA_FACTORY    = "CRITERIA_FACTORY";
  private static final String CRITERIA_REFINER    = "CRITERIA_REFINER";
  private ICriteriaFactory criteriaFactory;
  private ICriteriaRefiner criteriaRefiner;
  private boolean          useInListForPagination;

  /**
   * Constructs a new {@code QueryEntitiesAction} instance.
   */
  public QueryEntitiesAction() {
    useInListForPagination = true;
  }

  /**
   * Create a in list criterion potentially using disjunction to overcome the
   * size limitation of certain DBs in restriction (e.g. Oracle is 1000).
   *
   * @param entityIds
   *     the list of entity ids.
   * @param chunkSize
   *     the size of disjunctions parts.
   * @return the criterion.
   */
  public static Criterion createEntityIdsInCriterion(Collection<Serializable> entityIds, int chunkSize) {
    if (entityIds.size() < chunkSize) {
      return Restrictions.in(IEntity.ID, entityIds);
    }
    int i = 0;
    Disjunction splittedInlist = Restrictions.disjunction();
    Set<Serializable> currentEntityIds = new LinkedHashSet<>();
    boolean complete = false;
    for (Iterator<Serializable> ite = entityIds.iterator(); ite.hasNext(); i++) {
      currentEntityIds.add(ite.next());
      if (i % chunkSize == (chunkSize - 1)) {
        splittedInlist.add(Restrictions.in(IEntity.ID, currentEntityIds));
        currentEntityIds = new LinkedHashSet<>();
        complete = true;
      } else {
        complete = false;
      }
    }
    if (!complete) {
      splittedInlist.add(Restrictions.in(IEntity.ID, currentEntityIds));
    }
    return splittedInlist;
  }

  /**
   * Performs actual Query. This method can be overridden by subclasses in order
   * to deal with non-Hibernate searches.
   *
   * @param queryComponent
   *     the query component.
   * @param context
   *     the action context
   * @return the list of retrieved components.
   */
  @Override
  @SuppressWarnings({"unchecked", "ConstantConditions"})
  public List<?> performQuery(final IQueryComponent queryComponent, final Map<String, Object> context) {
    Session hibernateSession = ((HibernateBackendController) getController(context)).getHibernateSession();
    ICriteriaFactory critFactory = (ICriteriaFactory) queryComponent.get(CRITERIA_FACTORY);
    if (critFactory == null) {
      critFactory = getCriteriaFactory(context);
      queryComponent.put(CRITERIA_FACTORY, critFactory);
    }
    EnhancedDetachedCriteria criteria = critFactory.createCriteria(queryComponent, context);
    List<IEntity> entities;
    if (criteria == null) {
      entities = new ArrayList<>();
      queryComponent.setRecordCount(0);
    } else {
      ICriteriaRefiner critRefiner = (ICriteriaRefiner) queryComponent.get(CRITERIA_REFINER);
      if (critRefiner == null) {
        critRefiner = getCriteriaRefiner(context);
        if (critRefiner != null) {
          queryComponent.put(CRITERIA_REFINER, critRefiner);
        }
      }
      if (critRefiner != null) {
        critRefiner.refineCriteria(criteria, queryComponent, context);
      }
      Integer totalCount = null;
      Integer pageSize = queryComponent.getPageSize();
      Integer page = queryComponent.getPage();

      ResultTransformer refinerResultTransformer = criteria.getResultTransformer();
      List<Order> refinerOrders = criteria.getOrders();
      if (refinerOrders != null) {
        criteria.removeAllOrders();
      }

      if (queryComponent.isDistinctEnforced() || queryComponent.getQueryDescriptor().isTranslatable()) {
        criteria.setProjection(Projections.distinct(Projections.id()));
        EnhancedDetachedCriteria outerCriteria = EnhancedDetachedCriteria.forEntityName(
            queryComponent.getQueryContract().getName());
        outerCriteria.add(Subqueries.propertyIn(IEntity.ID, criteria));
        criteria = outerCriteria;
      }

      if (pageSize != null) {
        if (page == null) {
          page = 0;
          queryComponent.setPage(page);
        }
        if (queryComponent.getRecordCount() == null) {
          if (isUseCountForPagination()) {
            criteria.setProjection(Projections.rowCount());
            totalCount = ((Number) criteria.getExecutableCriteria(hibernateSession).list().get(0)).intValue();
          } else {
            totalCount = IQueryComponent.UNKNOWN_COUNT;
          }
        }
        if (refinerOrders != null) {
          for (Order order : refinerOrders) {
            criteria.addOrder(order);
          }
        }
        critFactory.completeCriteriaWithOrdering(criteria, queryComponent, context);
        if (refinerResultTransformer != null) {
          criteria.setResultTransformer(refinerResultTransformer);
        }
        if (useInListForPagination) {
          criteria.setProjection(Projections.id());
          List<Serializable> entityIds = criteria.getExecutableCriteria(hibernateSession).setFirstResult(
              page * pageSize).setMaxResults(pageSize).list();
          if (entityIds.isEmpty()) {
            entities = new ArrayList<>();
          } else {
            criteria = EnhancedDetachedCriteria.forEntityName(queryComponent.getQueryContract().getName());
            entities = criteria.add(createEntityIdsInCriterion(entityIds, 500)).getExecutableCriteria(hibernateSession)
                               .list();
            Map<Serializable, IEntity> entitiesById = new HashMap<>();
            for (IEntity entity : entities) {
              entitiesById.put(entity.getId(), entity);
            }
            entities = new ArrayList<>();
            for (Serializable id : entityIds) {
              IEntity entity = entitiesById.get(id);
              if (entity != null) {
                entities.add(entity);
              }
            }
          }
        } else {
          entities = criteria.getExecutableCriteria(hibernateSession).setFirstResult(page * pageSize).setMaxResults(
              pageSize).list();
        }
      } else {
        if (refinerOrders != null) {
          for (Order order : refinerOrders) {
            criteria.addOrder(order);
          }
        }
        critFactory.completeCriteriaWithOrdering(criteria, queryComponent, context);
        if (refinerResultTransformer != null) {
          criteria.setResultTransformer(refinerResultTransformer);
        }
        entities = criteria.getExecutableCriteria(hibernateSession).list();
        totalCount = entities.size();
      }
      if (totalCount != null) {
        queryComponent.setRecordCount(totalCount);
      }
    }
    List<String> prefetchProperties = queryComponent.getPrefetchProperties();
    if (prefetchProperties != null && entities != null) {
      // Will load the prefetch properties in the same transaction in order to leverage
      // Hibernate batch fetching.
      IAccessorFactory accessorFactory = getAccessorFactory(context);
      for (String prefetchProperty : prefetchProperties) {
        for (IEntity entity : entities) {
          try {
            accessorFactory.createPropertyAccessor(prefetchProperty, queryComponent.getQueryContract()).getValue(
                entity);
          } catch (Exception e) {
            LOG.warn("An unexpected exception occurred when pre-fetching property {}", prefetchProperty, e);
          }
        }
      }
    }
    return entities;
  }

  /**
   * Configures a criteria refiner that will be called before the Hibernate
   * detached criteria is actually used to perform the query. It allows to
   * complement the criteria with arbitrary complex clauses that cannot be
   * simply expressed in a &quot;<i>Query by Example</i>&quot; semantics.
   *
   * @param criteriaRefiner
   *     the criteriaRefiner to set.
   */
  public void setCriteriaRefiner(ICriteriaRefiner criteriaRefiner) {
    this.criteriaRefiner = criteriaRefiner;
  }

  /**
   * Retrieves the configured criteria refiner.
   *
   * @param context
   *     the action context.
   * @return the configured criteria refiner.
   */
  public ICriteriaRefiner getCriteriaRefiner(Map<String, Object> context) {
    if (context.containsKey(CRITERIA_REFINER)) {
      return (ICriteriaRefiner) context.get(CRITERIA_REFINER);
    }
    return this.criteriaRefiner;
  }

  /**
   * Gets the criteriaFactory.
   *
   * @param context
   *     the action context.
   * @return the criteriaFactory.
   */
  protected ICriteriaFactory getCriteriaFactory(Map<String, Object> context) {
    if (context.containsKey(CRITERIA_FACTORY)) {
      return (ICriteriaFactory) context.get(CRITERIA_FACTORY);
    }
    return criteriaFactory;
  }

  /**
   * Sets the criteriaFactory.
   *
   * @param criteriaFactory
   *     the criteriaFactory to set.
   */
  public void setCriteriaFactory(ICriteriaFactory criteriaFactory) {
    this.criteriaFactory = criteriaFactory;
  }

  /**
   * Sets the useInListForPagination.
   *
   * @param useInListForPagination
   *     the useInListForPagination to set.
   */
  public void setUseInListForPagination(boolean useInListForPagination) {
    this.useInListForPagination = useInListForPagination;
  }
}
