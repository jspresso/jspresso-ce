/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.component.query.ComparableQueryStructure;
import org.jspresso.framework.model.descriptor.query.ComparableQueryStructureDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

/**
 * An action to hibernate query entities by example.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class QueryEntitiesAction extends AbstractHibernateAction {

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean execute(IActionHandler actionHandler,
      final Map<String, Object> context) {
    final IQueryComponent queryComponent = (IQueryComponent) ((IValueConnector) context
        .get(ActionContextConstants.QUERY_MODEL_CONNECTOR)).getConnectorValue();

    List<IEntity> queriedEntities = (List<IEntity>) getTransactionTemplate(
        context).execute(new TransactionCallback() {

      public Object doInTransaction(TransactionStatus status) {
        DetachedCriteria criteria = DetachedCriteria
            .forEntityName(queryComponent.getQueryContract().getName());
        boolean abort = completeCriteria(criteria, null, queryComponent);
        List entities;
        if (abort) {
          entities = new ArrayList<IEntity>();
          queryComponent.setRecordCount(new Integer(0));
        } else {
          Integer totalCount = null;
          Integer pageSize = queryComponent.getPageSize();
          Integer page = queryComponent.getPage();
          if (pageSize != null) {
            if (page == null) {
              page = new Integer(0);
              queryComponent.setPage(page);
            }
            entities = getHibernateTemplate(context).findByCriteria(criteria,
                page.intValue() * pageSize.intValue(), pageSize.intValue());
            if (queryComponent.getRecordCount() == null) {
              criteria.setProjection(Projections.rowCount());
              totalCount = (Integer) getHibernateTemplate(context)
                  .findByCriteria(criteria).get(0);
            }
          } else {
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

      private boolean completeCriteria(DetachedCriteria criteria, String path,
          IQueryComponent aQueryComponent) {
        boolean abort = false;
        if (ComparableQueryStructure.class.isAssignableFrom(aQueryComponent
            .getQueryContract())) {
          try {
            String comparator = (String) getAccessorFactory(context)
                .createPropertyAccessor(
                    ComparableQueryStructureDescriptor.COMPARATOR,
                    ComparableQueryStructure.class).getValue(aQueryComponent);
            Object infValue = getAccessorFactory(context)
                .createPropertyAccessor(
                    ComparableQueryStructureDescriptor.INF_VALUE,
                    ComparableQueryStructure.class).getValue(aQueryComponent);
            Object supValue = getAccessorFactory(context)
                .createPropertyAccessor(
                    ComparableQueryStructureDescriptor.SUP_VALUE,
                    ComparableQueryStructure.class).getValue(aQueryComponent);
            if (infValue != null) {
              if (ComparableQueryStructureDescriptor.EQ.equals(comparator)) {
                criteria.add(Restrictions.eq(path, infValue));
              } else if (ComparableQueryStructureDescriptor.GT
                  .equals(comparator)) {
                criteria.add(Restrictions.gt(path, infValue));
              } else if (ComparableQueryStructureDescriptor.GE
                  .equals(comparator)) {
                criteria.add(Restrictions.ge(path, infValue));
              } else if (ComparableQueryStructureDescriptor.LT
                  .equals(comparator)) {
                criteria.add(Restrictions.lt(path, infValue));
              } else if (ComparableQueryStructureDescriptor.LE
                  .equals(comparator)) {
                criteria.add(Restrictions.le(path, infValue));
              } else if (ComparableQueryStructureDescriptor.BE
                  .equals(comparator)) {
                if (supValue != null) {
                  criteria.add(Restrictions.between(path, infValue, supValue));
                } else {
                  criteria.add(Restrictions.ge(path, infValue));
                }
              }
            } else if (supValue != null) {
              if (ComparableQueryStructureDescriptor.BE.equals(comparator)) {
                criteria.add(Restrictions.le(path, supValue));
              }
            }
          } catch (IllegalAccessException ex) {
            throw new ActionException(ex);
          } catch (InvocationTargetException ex) {
            throw new ActionException(ex);
          } catch (NoSuchMethodException ex) {
            throw new ActionException(ex);
          }
        } else {
          for (Map.Entry<String, Object> property : aQueryComponent.entrySet()) {
            String prefixedProperty;
            if (path != null) {
              prefixedProperty = path + "." + property.getKey();
            } else {
              prefixedProperty = property.getKey();
            }
            if (property.getValue() instanceof IEntity) {
              if (!((IEntity) property.getValue()).isPersistent()) {
                abort = true;
              } else {
                criteria.add(Restrictions.eq(prefixedProperty, property
                    .getValue()));
              }
            } else if (property.getValue() instanceof Boolean
                && ((Boolean) property.getValue()).booleanValue()) {
              criteria.add(Restrictions.eq(prefixedProperty, property
                  .getValue()));
            } else if (property.getValue() instanceof String) {
              criteria.add(Restrictions.like(prefixedProperty,
                  (String) property.getValue(), MatchMode.START).ignoreCase());
            } else if (property.getValue() instanceof Number
                || property.getValue() instanceof Date) {
              criteria.add(Restrictions.eq(prefixedProperty, property
                  .getValue()));
            } else if (property.getValue() instanceof IQueryComponent) {
              abort = abort
                  || completeCriteria(criteria, prefixedProperty,
                      (IQueryComponent) property.getValue());
            }
          }
        }
        return abort;
      }
    });
    IApplicationSession session = getApplicationSession(context);
    for (Iterator<IEntity> ite = queriedEntities.iterator(); ite.hasNext();) {
      if (session.isEntityRegisteredForDeletion(ite.next())) {
        ite.remove();
      }
    }
    queryComponent.setQueriedComponents(session.merge(queriedEntities,
        EMergeMode.MERGE_KEEP));
    return super.execute(actionHandler, context);
  }
}
