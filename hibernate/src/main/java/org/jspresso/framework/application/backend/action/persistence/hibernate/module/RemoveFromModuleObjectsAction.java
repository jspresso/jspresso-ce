/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action.persistence.hibernate.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.persistence.hibernate.AbstractHibernateCollectionAction;
import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.application.model.BeanModule;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.model.entity.IEntity;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;


/**
 * This action removes the selected objects from the projected collection.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
      for (Module module : new ArrayList<Module>(parentModule
          .getSubModules())) {
        if (module instanceof BeanModule
            && removedObject.equals(((BeanModule) module).getModuleObject())) {
          parentModule.removeSubModule(module);
        }
      }
    }
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

    final List<IEntity> moduleObjectsToRemove = new ArrayList<IEntity>();
    for (int i = 0; i < selectedIndices.length; i++) {
      moduleObjectsToRemove.add((IEntity) collectionConnector
          .getChildConnector(selectedIndices[i]).getConnectorValue());
    }
    getTransactionTemplate(context).execute(new TransactionCallback() {

      public Object doInTransaction(@SuppressWarnings("unused")
      TransactionStatus status) {
        getHibernateTemplate(context).execute(new HibernateCallback() {

          public Object doInHibernate(Session session) {
            List<IEntity> mergedCollection = mergeInHibernate(
                moduleObjectsToRemove, session, context);
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
    for (IEntity entityToRemove : moduleObjectsToRemove) {
      projectedCollection.remove(entityToRemove);
      removeFromSubModules(module, entityToRemove);
    }
    module.setModuleObjects(projectedCollection);
    collectionConnector.setConnectorValue(projectedCollection);
    context.put(ActionContextConstants.ACTION_PARAM, moduleObjectsToRemove);
    return super.execute(actionHandler, context);
  }

  /**
   * Deletes the entity from the persistent store.
   * 
   * @param entity
   *            the entity to remove
   * @param session
   *            the session to use.
   * @param context
   *            the action context.
   */
  protected void deleteEntity(IEntity entity, Session session,
      @SuppressWarnings("unused")
      Map<String, Object> context) {
    session.delete(entity);
  }
}
