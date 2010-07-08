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
package org.jspresso.framework.application.backend.action.persistence.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Projections;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.IBackendController;
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

  private static final String    CRITERIA_FACTORY  = "CRITERIA_FACTORY";
  private static final String    CRITERIA_REFINER  = "CRITERIA_REFINER";
  private static final String    COMPONENT_REFINER = "COMPONENT_REFINER";

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean execute(IActionHandler actionHandler,
      final Map<String, Object> context) {
    final IQueryComponent queryComponent = getQueryComponent(context);

    IQueryComponentRefiner compRefiner = (IQueryComponentRefiner) queryComponent
        .get(COMPONENT_REFINER);

    if (compRefiner == null && queryComponentRefiner != null) {
      queryComponent.put(COMPONENT_REFINER, queryComponentRefiner);
      compRefiner = queryComponentRefiner;
    }
    if (compRefiner != null) {
      compRefiner.refineQueryComponent(queryComponent, context);
    }

    List<IEntity> queriedEntities = (List<IEntity>) getTransactionTemplate(
        context).execute(new TransactionCallback() {

      public Object doInTransaction(TransactionStatus status) {
        ICriteriaFactory critFactory = (ICriteriaFactory) queryComponent
            .get(CRITERIA_FACTORY);
        if (critFactory == null) {
          queryComponent.put(CRITERIA_FACTORY, getCriteriaFactory());
          critFactory = getCriteriaFactory();
        }
        EnhancedDetachedCriteria criteria = critFactory
            .createCriteria(queryComponent);
        List<IEntity> entities;
        if (criteria == null) {
          entities = new ArrayList<IEntity>();
          queryComponent.setRecordCount(new Integer(0));
        } else {
          ICriteriaRefiner critRefiner = (ICriteriaRefiner) queryComponent
              .get(CRITERIA_REFINER);
          if (critRefiner == null && criteriaRefiner != null) {
            queryComponent.put(CRITERIA_REFINER, criteriaRefiner);
            critRefiner = criteriaRefiner;
          }
          if (critRefiner != null) {
            critRefiner.refineCriteria(criteria, queryComponent, context);
          }
          Integer totalCount = null;
          Integer pageSize = queryComponent.getPageSize();
          Integer page = queryComponent.getPage();
          if (pageSize != null) {
            if (page == null) {
              page = new Integer(0);
              queryComponent.setPage(page);
            }
            if (queryComponent.getRecordCount() == null) {
              criteria.setProjection(Projections.rowCount());
              totalCount = (Integer) getHibernateTemplate(context)
                  .findByCriteria(criteria).get(0);
            }
            critFactory.completeCriteriaWithOrdering(criteria, queryComponent);
            entities = getHibernateTemplate(context).findByCriteria(criteria,
                page.intValue() * pageSize.intValue(), pageSize.intValue());
          } else {
            critFactory.completeCriteriaWithOrdering(criteria, queryComponent);
            entities = getHibernateTemplate(context).findByCriteria(criteria);
            totalCount = new Integer(entities.size());
          }
          if (totalCount != null) {
            queryComponent.setRecordCount(totalCount);
          }
        }
        status.setRollbackOnly();
        return entities;
      }

    });
    IBackendController controller = getController(context);
    for (Iterator<IEntity> ite = queriedEntities.iterator(); ite.hasNext();) {
      if (controller.isEntityRegisteredForDeletion(ite.next())) {
        ite.remove();
      }
    }
    queryComponent.setQueriedComponents(controller.merge(queriedEntities,
        EMergeMode.MERGE_KEEP));
    return super.execute(actionHandler, context);
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
}
