/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.persistence.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.backend.session.IApplicationSession;
import com.d2s.framework.application.backend.session.MergeMode;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IQueryEntity;
import com.d2s.framework.util.bean.PropertyHelper;

/**
 * An action to hibernate query entities by example.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
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
    final IQueryEntity queryEntity = (IQueryEntity) ((IValueConnector) context
        .get(ActionContextConstants.QUERY_MODEL_CONNECTOR)).getConnectorValue();

    List<IEntity> queriedEntities = (List<IEntity>) getTransactionTemplate(
        context).execute(new TransactionCallback() {

      public Object doInTransaction(@SuppressWarnings("unused")
      TransactionStatus status) {
        DetachedCriteria criteria = DetachedCriteria.forEntityName(queryEntity
            .getContract().getName());
        Example example = Example.create(queryEntity).ignoreCase().enableLike(
            MatchMode.START);
        boolean abort = false;
        for (Map.Entry<String, Object> property : queryEntity
            .straightGetProperties().entrySet()) {
          if (property.getValue() instanceof IEntity) {
            if (!((IEntity) property.getValue()).isPersistent()) {
              abort = true;
            } else {
              criteria.add(Restrictions.eq(property.getKey(), property
                  .getValue()));
            }
          } else if (Boolean.TYPE.isAssignableFrom(PropertyHelper
              .getPropertyType(queryEntity.getContract(), property.getKey()))
              && (property.getValue() == null || !((Boolean) property
                  .getValue()).booleanValue())) {
            example.excludeProperty(property.getKey());
          }
        }
        criteria.add(example);
        List entities;
        if (abort) {
          entities = new ArrayList<IEntity>();
        } else {
          entities = getHibernateTemplate(context).findByCriteria(criteria);
        }
        status.setRollbackOnly();
        return entities;
      }
    });
    IApplicationSession session = getApplicationSession(context);
    for (Iterator<IEntity> ite = queriedEntities.iterator(); ite.hasNext();) {
      if (session.isEntityRegisteredForDeletion(ite.next())) {
        ite.remove();
      }
    }
    queryEntity.setQueriedEntities(session.merge(queriedEntities,
        MergeMode.MERGE_KEEP));
    return super.execute(actionHandler, context);
  }
}
