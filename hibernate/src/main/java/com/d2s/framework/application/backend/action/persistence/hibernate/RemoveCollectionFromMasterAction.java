/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.persistence.hibernate;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.util.bean.ICollectionAccessor;
import com.d2s.framework.view.action.ActionContextConstants;
import com.d2s.framework.view.action.ActionException;
import com.d2s.framework.view.action.IActionHandler;

/**
 * An action used in master/detail views to remove selected details from a
 * master domain object and save the removal.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RemoveCollectionFromMasterAction extends
    AbstractHibernateCollectionAction {

  /**
   * Retrieves the master and its managed collection from the model connector
   * then removes selected details from the managed collection and save the
   * removal.
   * <p>
   * {@inheritDoc}
   */
  public void execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, final Map<String, Object> context) {
    final ICollectionConnector collectionConnector = getModelConnector(context);
    if (collectionConnector == null) {
      return;
    }
    getTransactionTemplate(context).execute(new TransactionCallback() {

      public Object doInTransaction(@SuppressWarnings("unused")
      TransactionStatus status) {
        getHibernateTemplate(context).execute(new HibernateCallback() {

          public Object doInHibernate(Session session) {
            ICollectionPropertyDescriptor collectionDescriptor = (ICollectionPropertyDescriptor) getModelDescriptor(context);
            Object master = collectionConnector.getParentConnector()
                .getConnectorValue();
            IEntity mergedMaster = mergeInHibernate((IEntity) master, session,
                context);
            String property = collectionDescriptor.getName();
            ICollectionAccessor collectionAccessor = getAccessorFactory(context)
                .createCollectionPropertyAccessor(property,
                    mergedMaster.getClass());
            if (getSelectedIndices(context) != null) {
              Collection<IEntity> detailsToRemove = new HashSet<IEntity>();
              for (int selectedIndex : getSelectedIndices(context)) {
                detailsToRemove.add((IEntity) collectionConnector
                    .getChildConnector(selectedIndex).getConnectorValue());
              }
              for (IEntity nextDetailToRemove : detailsToRemove) {
                try {
                  collectionAccessor.removeFromValue(mergedMaster,
                      nextDetailToRemove);
                } catch (IllegalAccessException ex) {
                  throw new ActionException(ex);
                } catch (InvocationTargetException ex) {
                  throw new ActionException(ex);
                } catch (NoSuchMethodException ex) {
                  throw new ActionException(ex);
                }
                if (nextDetailToRemove.isPersistent()) {
                  IEntity sessionEntity = (IEntity) session.get(
                      nextDetailToRemove.getContract(), nextDetailToRemove
                          .getId());
                  if (sessionEntity == null) {
                    sessionEntity = nextDetailToRemove;
                  }
                  session.delete(sessionEntity);
                }
              }
              if (detailsToRemove.size() != 0) {
                context.put(ActionContextConstants.ACTION_RESULT,
                    detailsToRemove);
              }
            }
            return null;
          }
        });
        return null;
      }
    });
  }

}
