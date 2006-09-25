/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.persistence.hibernate;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.backend.session.IApplicationSession;
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
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, final Map<String, Object> context) {
    final IQueryEntity queryEntity = (IQueryEntity) ((IValueConnector) context
        .get(ActionContextConstants.QUERY_MODEL_CONNECTOR)).getConnectorValue();

    // Not using a transaction fixes a bug of registering twice an object
    // in the
    // session.
    // getTransactionTemplate(context).execute(new TransactionCallback() {
    //
    // public Object doInTransaction(@SuppressWarnings("unused")
    // TransactionStatus status) {
    DetachedCriteria criteria = DetachedCriteria.forEntityName(queryEntity
        .getContract().getName());
    criteria.add(Example.create(queryEntity).ignoreCase().enableLike(
        MatchMode.START));
    for (Map.Entry<String, Object> property : queryEntity
        .straightGetProperties().entrySet()) {
      if (property.getValue() instanceof IEntity) {
        criteria.add(Restrictions.eq(property.getKey(), property.getValue()));
      }
    }
    List<IEntity> queriedEntities = getHibernateTemplate(context)
        .findByCriteria(criteria);
    IApplicationSession session = getApplicationSession(context);
    for (Iterator<IEntity> ite = queriedEntities.iterator(); ite.hasNext();) {
      if (session.isEntityRegisteredForDeletion(ite.next())) {
        ite.remove();
      }
    }
    queryEntity.setQueriedEntities(queriedEntities);
    // return null;
    // }
    // });
    return true;
  }

}
