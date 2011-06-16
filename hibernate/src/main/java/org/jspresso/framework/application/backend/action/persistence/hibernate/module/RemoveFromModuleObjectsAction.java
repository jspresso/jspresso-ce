/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.backend.action.persistence.hibernate.module;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.backend.action.persistence.hibernate.AbstractHibernateCollectionAction;
import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.application.model.BeanModule;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.model.entity.IEntity;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

/**
 * This action, which is to be used on bean collection modules, removes the
 * selected objects from the module's projected collection <b>and deletes them
 * from the persistent store</b>. If one (or more) of the removed objects are
 * also used in children bean modules, the corresponding children bean modules
 * are also removed accordingly.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RemoveFromModuleObjectsAction extends
    AbstractHibernateCollectionAction {

  private static void removeFromSubModules(Module parentModule,
      Object removedObject) {
    if (parentModule.getSubModules() != null) {
      for (Module module : new ArrayList<Module>(parentModule.getSubModules())) {
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
  public boolean execute(IActionHandler actionHandler,
      final Map<String, Object> context) {
    int[] selectedIndices = getSelectedIndices(context);
    ICollectionConnector collectionConnector = getModelConnector(context);

    if (selectedIndices == null || selectedIndices.length == 0
        || collectionConnector == null) {
      return false;
    }

    BeanCollectionModule module = (BeanCollectionModule) getModule(context);

    List<Object> projectedCollection;
    if (module.getModuleObjects() == null) {
      projectedCollection = new ArrayList<Object>();
    } else {
      projectedCollection = new ArrayList<Object>(module.getModuleObjects());
    }

    final List<Object> moduleObjectsToRemove = new ArrayList<Object>();
    for (int i = 0; i < selectedIndices.length; i++) {
      moduleObjectsToRemove.add(collectionConnector.getChildConnector(
          selectedIndices[i]).getConnectorValue());
    }
    getTransactionTemplate(context).execute(
        new TransactionCallbackWithoutResult() {

          @Override
          protected void doInTransactionWithoutResult(
              @SuppressWarnings("unused") TransactionStatus status) {
            List<Object> uowClones = new ArrayList<Object>();
            IBackendController controller = getController(context);
            for (Object moduleObjectToRemove : moduleObjectsToRemove) {
              if (moduleObjectToRemove instanceof IEntity) {
                uowClones.add(controller
                    .cloneInUnitOfWork((IEntity) moduleObjectToRemove));
              } else {
                uowClones.add(moduleObjectToRemove);
              }
            }
            for (Object moduleObjectToRemove : uowClones) {
              if (moduleObjectToRemove instanceof IEntity) {
                try {
                  deleteEntity((IEntity) moduleObjectToRemove, context);
                } catch (IllegalAccessException ex) {
                  throw new ActionException(ex);
                } catch (InvocationTargetException ex) {
                  if (ex.getCause() instanceof RuntimeException) {
                    throw (RuntimeException) ex.getCause();
                  }
                  throw new ActionException(ex.getCause());
                } catch (NoSuchMethodException ex) {
                  throw new ActionException(ex);
                }
              }
            }
            try {
              getController(context).performPendingOperations();
            } catch (RuntimeException ex) {
              getController(context).clearPendingOperations();
              throw ex;
            }
          }
        });
    for (Object moduleObjectToRemove : moduleObjectsToRemove) {
      projectedCollection.remove(moduleObjectToRemove);
      removeFromSubModules(module, moduleObjectToRemove);
    }
    module.setModuleObjects(projectedCollection);
    collectionConnector.setConnectorValue(projectedCollection);
    setActionParameter(moduleObjectsToRemove, context);
    return super.execute(actionHandler, context);
  }

  /**
   * Deletes the entity from the persistent store.
   * 
   * @param entity
   *          the entity to remove
   * @param context
   *          the action context.
   * @throws NoSuchMethodException
   *           whenever this exception occurs.
   * @throws InvocationTargetException
   *           whenever this exception occurs.
   * @throws IllegalAccessException
   *           whenever this exception occurs.
   */
  protected void deleteEntity(IEntity entity, Map<String, Object> context)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    cleanRelationshipsOnDeletion(entity, context, true);
    cleanRelationshipsOnDeletion(entity, context, false);
    // Now handled in cleanRelationshipsOnDeletion when dryRun=false.
    // getController(context).registerForDeletion(entity);
  }
}
