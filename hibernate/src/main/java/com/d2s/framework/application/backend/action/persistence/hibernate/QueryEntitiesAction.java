/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.backend.action.persistence.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.backend.session.IApplicationSession;
import com.d2s.framework.application.backend.session.MergeMode;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.model.component.IQueryComponent;
import com.d2s.framework.model.entity.IEntity;

/**
 * An action to hibernate query entities by example.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, final Map<String, Object> context) {
    final IQueryComponent queryComponent = (IQueryComponent) ((IValueConnector) context
        .get(ActionContextConstants.QUERY_MODEL_CONNECTOR)).getConnectorValue();

    List<IEntity> queriedEntities = (List<IEntity>) getTransactionTemplate(
        context).execute(new TransactionCallback() {

      public Object doInTransaction(@SuppressWarnings("unused")
      TransactionStatus status) {
        DetachedCriteria criteria = DetachedCriteria
            .forEntityName(queryComponent.getQueryContract().getName());
        boolean abort = completeCriteria(criteria, null, queryComponent);
        List entities;
        if (abort) {
          entities = new ArrayList<IEntity>();
        } else {
          entities = getHibernateTemplate(context).findByCriteria(criteria);
        }
        status.setRollbackOnly();
        return entities;
      }

      private boolean completeCriteria(DetachedCriteria criteria, String path,
          IQueryComponent aQueryComponent) {
        boolean abort = false;
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
            criteria.add(Restrictions
                .eq(prefixedProperty, property.getValue()));
          } else if (property.getValue() instanceof String) {
            criteria.add(Restrictions.like(prefixedProperty,
                (String) property.getValue(), MatchMode.START).ignoreCase());
          } else if (property.getValue() instanceof Number
              || property.getValue() instanceof Date) {
            criteria.add(Restrictions
                .eq(prefixedProperty, property.getValue()));
          } else if (property.getValue() instanceof IQueryComponent) {
            abort = abort
                || completeCriteria(criteria, prefixedProperty,
                    (IQueryComponent) property.getValue());
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
        MergeMode.MERGE_KEEP));
    return super.execute(actionHandler, context);
  }
}
