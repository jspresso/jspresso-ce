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
package org.jspresso.framework.application.backend.action.persistence.mongo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import org.jspresso.framework.application.backend.action.AbstractQueryComponentsAction;
import org.jspresso.framework.model.persistence.mongo.criterion.IQueryFactory;
import org.jspresso.framework.application.backend.persistence.mongo.MongoBackendController;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.util.accessor.IAccessorFactory;

/**
 * This action is used to Mongo query entities by example. It is used behind
 * the scene in several places in Jspresso based applications, as in filtered
 * bean collection modules, list of values, ... The principles are to tailor an
 * Mongo Criteria based on the Jspresso &quot;{@code IQueryComponent}
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
 * Out of this query component, the action will build an Mongo
 * query by constructing all join sub-query whenever necessary.
 * <p/>
 * Once the query is complete, the action will perform the Mongo
 * query while using paging information taken from the query component as well
 * as custom sorting properties.
 * <p/>
 * Whenever the query is successful, the result is merged back to the
 * application session and assigned to the query component
 * {@code queriedComponents} property.
 * <p/>
 * Note that there are 2 hooks that can be configured by injection to fine-tune
 * the performed query : {@code queryComponentRefiner} and
 * {@code queryRefiner}.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision : 9166 $
 */
public class QueryEntitiesAction extends AbstractQueryComponentsAction {

  private static final Logger LOG = LoggerFactory.getLogger(QueryEntitiesAction.class);

  private static final String QUERY_FACTORY = "QUERY_FACTORY";
  private static final String QUERY_REFINER = "QUERY_REFINER";
  private IQueryFactory queryFactory;
  private IQueryRefiner queryRefiner;

  /**
   * Create a in list criteria potentially using disjunction to overcome the
   * size limitation of certain DBs in restriction (e.g. Oracle is 1000).
   *
   * @param entityIds
   *     the list of entity ids.
   * @param chunkSize
   *     the size of disjunctions parts.
   * @return the criteria.
   */
  public static Criteria createEntityIdsInCriteria(Collection<Serializable> entityIds, int chunkSize) {
    if (entityIds.size() < chunkSize) {
      return Criteria.where(IEntity.ID).in(entityIds);
    }
    int i = 0;
    Criteria splittedInlist = Criteria.where(IEntity.ID);
    Set<Serializable> currentEntityIds = new LinkedHashSet<>();
    boolean complete = false;
    for (Iterator<Serializable> ite = entityIds.iterator(); ite.hasNext(); i++) {
      currentEntityIds.add(ite.next());
      if (i % chunkSize == (chunkSize - 1)) {
        splittedInlist.orOperator(Criteria.where(IEntity.ID).in(currentEntityIds));
        currentEntityIds = new LinkedHashSet<>();
        complete = true;
      } else {
        complete = false;
      }
    }
    if (!complete) {
      splittedInlist.orOperator(Criteria.where(IEntity.ID).in(currentEntityIds));
    }
    return splittedInlist;
  }

  /**
   * Performs actual Query. This method can be overridden by subclasses in order
   * to deal with non-Mongo searches.
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
    MongoTemplate mongo = ((MongoBackendController) getController(context)).getMongoTemplate();
    IQueryFactory qFactory = (IQueryFactory) queryComponent.get(QUERY_FACTORY);
    if (qFactory == null) {
      qFactory = getQueryFactory(context);
      queryComponent.put(QUERY_FACTORY, qFactory);
    }
    Query query = qFactory.createQuery(queryComponent, context);
    List<? extends IEntity> entities;
    Class<? extends IEntity> entityClass = queryComponent.getQueryContract();
    if (query == null) {
      entities = new ArrayList<>();
      queryComponent.setRecordCount(0);
    } else {
      IQueryRefiner critRefiner = (IQueryRefiner) queryComponent.get(QUERY_REFINER);
      if (critRefiner == null) {
        critRefiner = getQueryRefiner(context);
        if (critRefiner != null) {
          queryComponent.put(QUERY_REFINER, critRefiner);
        }
      }
      if (critRefiner != null) {
        critRefiner.refineQuery(query, queryComponent, context);
      }
      Integer totalCount = null;
      Integer pageSize = queryComponent.getPageSize();
      Integer page = queryComponent.getPage();

      if (pageSize != null) {
        if (page == null) {
          page = 0;
          queryComponent.setPage(page);
        }
        if (queryComponent.getRecordCount() == null) {
          if (isUseCountForPagination()) {
            totalCount = (int) mongo.count(query, entityClass);
          } else {
            totalCount = IQueryComponent.UNKNOWN_COUNT;
          }
        }
        qFactory.completeQueryWithOrdering(query, queryComponent, context);
        entities = mongo.find(query.skip(page * pageSize).limit(pageSize), entityClass);
      } else {
        qFactory.completeQueryWithOrdering(query, queryComponent, context);
        entities = mongo.find(query, entityClass);
        totalCount = entities.size();
      }
      if (totalCount != null) {
        queryComponent.setRecordCount(totalCount);
      }
    }
    List<String> prefetchProperties = queryComponent.getPrefetchProperties();
    if (prefetchProperties != null && entities != null) {
      // Will load the prefetch properties in the same transaction in order to leverage
      // Mongo batch fetching.
      IAccessorFactory accessorFactory = getAccessorFactory(context);
      for (String prefetchProperty : prefetchProperties) {
        for (IEntity entity : entities) {
          try {
            accessorFactory.createPropertyAccessor(prefetchProperty, entityClass).getValue(
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
   * Configures a query refiner that will be called before the Mongo
   * query is actually used to perform the query. It allows to
   * complement the query with arbitrary complex clauses that cannot be
   * simply expressed in a &quot;<i>Query by Example</i>&quot; semantics.
   *
   * @param queryRefiner
   *     the queryRefiner to set.
   */
  public void setQueryRefiner(IQueryRefiner queryRefiner) {
    this.queryRefiner = queryRefiner;
  }

  /**
   * Retrieves the configured query refiner.
   *
   * @param context
   *     the action context.
   * @return the configured query refiner.
   */
  public IQueryRefiner getQueryRefiner(Map<String, Object> context) {
    if (context.containsKey(QUERY_REFINER)) {
      return (IQueryRefiner) context.get(QUERY_REFINER);
    }
    return this.queryRefiner;
  }

  /**
   * Gets the queryFactory.
   *
   * @param context
   *     the action context.
   * @return the queryFactory.
   */
  protected IQueryFactory getQueryFactory(Map<String, Object> context) {
    if (context.containsKey(QUERY_FACTORY)) {
      return (IQueryFactory) context.get(QUERY_FACTORY);
    }
    return queryFactory;
  }

  /**
   * Sets the queryFactory.
   *
   * @param queryFactory
   *     the queryFactory to set.
   */
  public void setQueryFactory(IQueryFactory queryFactory) {
    this.queryFactory = queryFactory;
  }
}
