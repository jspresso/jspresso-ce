/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.persistence.hibernate;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IQueryEntity;

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
  @SuppressWarnings("unchecked")
  public void execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, final Map<String, Object> context) {
    final IQueryEntity queryEntity = (IQueryEntity) ((IValueConnector) context
    .get(ActionContextConstants.QUERY_MODEL_CONNECTOR))
        .getConnectorValue();

    getTransactionTemplate(context).execute(new TransactionCallback() {

      public Object doInTransaction(@SuppressWarnings("unused")
      TransactionStatus status) {
        DetachedCriteria criteria = DetachedCriteria.forEntityName(queryEntity
            .getContract().getName());
        criteria.add(Example.create(queryEntity).ignoreCase().enableLike(
            MatchMode.START));
        List<IEntity> queriedEntities = getHibernateTemplate(context).findByCriteria(
            criteria);
        queryEntity.setQueriedEntities(queriedEntities);
        return null;
      }
    });
  }

}
