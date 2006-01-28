/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.persistence.hibernate;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.hibernate.LockMode;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.ActionException;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.util.bean.ICollectionAccessor;

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
  public Map<String, Object> execute(@SuppressWarnings("unused")
  IActionHandler actionHandler) {
    final ICollectionConnector collectionConnector = getModelConnector();
    if (collectionConnector == null) {
      return null;
    }
    final Collection<Object> removedObjects = new HashSet<Object>();
    getTransactionTemplate().execute(new TransactionCallback() {

      public Object doInTransaction(@SuppressWarnings("unused")
      TransactionStatus status) {
        getHibernateTemplate().execute(new HibernateCallback() {

          public Object doInHibernate(Session session) {
            ICollectionPropertyDescriptor collectionDescriptor = (ICollectionPropertyDescriptor) getModelDescriptor();
            Object master = collectionConnector.getParentConnector()
                .getConnectorValue();
            IEntity mergedMaster = mergeInHibernate((IEntity) master, session);
            String property = collectionDescriptor.getName();
            ICollectionAccessor collectionAccessor = getAccessorFactory()
                .createCollectionPropertyAccessor(
                    property,
                    mergedMaster.getClass(),
                    collectionDescriptor.getReferencedDescriptor()
                        .getElementDescriptor().getComponentContract());
            if (getSelectedIndices() != null) {
              for (int selectedIndex : getSelectedIndices()) {
                IEntity nextDetailToRemove = (IEntity) collectionConnector
                    .getChildConnector(selectedIndex).getConnectorValue();
                try {
                  Object mergedDetail = session.get(nextDetailToRemove
                      .getContract().getName(), nextDetailToRemove.getId(),
                      LockMode.NONE);
                  collectionAccessor
                      .removeFromValue(mergedMaster, mergedDetail);
                  removedObjects.add(nextDetailToRemove);
                } catch (IllegalAccessException ex) {
                  throw new ActionException(ex);
                } catch (InvocationTargetException ex) {
                  throw new ActionException(ex);
                } catch (NoSuchMethodException ex) {
                  throw new ActionException(ex);
                }
              }
            }
            return null;
          }
        });
        return null;
      }
    });
    if (removedObjects.size() != 0) {
      Map<String, Object> actionResult = new HashMap<String, Object>();
      actionResult.put(ActionContextConstants.ACTION_RESULT, removedObjects);
      return actionResult;
    }
    return null;
  }

}
