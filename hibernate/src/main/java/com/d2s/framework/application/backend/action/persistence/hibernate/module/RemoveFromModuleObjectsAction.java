/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.persistence.hibernate.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.backend.action.persistence.hibernate.AbstractHibernateCollectionAction;
import com.d2s.framework.application.model.BeanCollectionModule;
import com.d2s.framework.application.model.BeanModule;
import com.d2s.framework.application.model.Module;
import com.d2s.framework.application.model.SubModule;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.model.entity.IEntity;

/**
 * This action removes the selected objects from the projected collection.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RemoveFromModuleObjectsAction extends
    AbstractHibernateCollectionAction {

  private static void removeFromSubModules(Module parentModule,
      Object removedObject) {
    if (parentModule.getSubModules() != null) {
      for (SubModule subModule : new ArrayList<SubModule>(parentModule
          .getSubModules())) {
        if (subModule instanceof BeanModule
            && removedObject.equals(((BeanModule) subModule).getModuleObject())) {
          parentModule.removeSubModule(subModule);
        }
      }
    }
  }

  /**
   * Deletes the entity from the persistent store.
   * 
   * @param entity
   *          the entity to remove
   * @param session
   *          the session to use.
   * @param context
   *          the action context.
   */
  protected void deleteEntity(IEntity entity, Session session,
      @SuppressWarnings("unused")
      Map<String, Object> context) {
    session.delete(entity);
  }

  /**
   * Removes the selected objects from the projected collection.
   * <p>
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, final Map<String, Object> context) {
    int[] selectedIndices = getSelectedIndices(context);
    ICollectionConnector collectionConnector = getModelConnector(context);

    if (selectedIndices == null || selectedIndices.length == 0
        || collectionConnector == null) {
      return false;
    }

    ICompositeValueConnector moduleConnector = getModuleConnector(context);
    BeanCollectionModule module = (BeanCollectionModule) moduleConnector
        .getConnectorValue();

    Collection<Object> projectedCollection;
    if (module.getModuleObjects() == null) {
      projectedCollection = new ArrayList<Object>();
    } else {
      projectedCollection = new ArrayList<Object>(module.getModuleObjects());
    }

    final List<IEntity> projectedObjectsToRemove = new ArrayList<IEntity>();
    for (int i = 0; i < selectedIndices.length; i++) {
      projectedObjectsToRemove.add((IEntity) collectionConnector
          .getChildConnector(selectedIndices[i]).getConnectorValue());
    }
    getTransactionTemplate(context).execute(new TransactionCallback() {

      public Object doInTransaction(@SuppressWarnings("unused")
      TransactionStatus status) {
        getHibernateTemplate(context).execute(new HibernateCallback() {

          public Object doInHibernate(Session session) {
            List<IEntity> mergedCollection = mergeInHibernate(
                projectedObjectsToRemove, session, context);
            for (IEntity entityToRemove : mergedCollection) {
              if (entityToRemove.isPersistent()) {
                deleteEntity(entityToRemove, session, context);
              }
            }
            return null;
          }
        });
        return null;
      }
    });
    for (IEntity entityToRemove : projectedObjectsToRemove) {
      projectedCollection.remove(entityToRemove);
      removeFromSubModules(module, entityToRemove);
    }
    module.setModuleObjects(projectedCollection);
    collectionConnector.setConnectorValue(projectedCollection);
    context.put(ActionContextConstants.ACTION_PARAM, projectedObjectsToRemove);
    return super.execute(actionHandler, context);
  }
}
