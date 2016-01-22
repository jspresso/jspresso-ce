/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.backend.action.persistence.module;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.AbstractCollectionAction;
import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.application.model.BeanModule;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.model.entity.IEntity;

/**
 * This action, which is to be used on bean modules, <b>deletes the module
 * object from the persistent store</b>. The bean module is also removed from
 * it's parent accordingly.
 *
 * @author Vincent Vandenschrick
 */
public class RemoveModuleObjectAction extends AbstractCollectionAction {

  private static void removeFromSubModules(Module parentModule,
      Object removedObject) {
    if (parentModule != null && parentModule.getSubModules() != null) {
      for (Module module : new ArrayList<>(parentModule.getSubModules())) {
        if (module instanceof BeanModule
            && removedObject.equals(((BeanModule) module).getModuleObject())) {
          parentModule.removeSubModule(module);
        }
      }
    }
    if (parentModule instanceof BeanCollectionModule) {
      ((BeanCollectionModule) parentModule)
          .removeFromModuleObjects(removedObject);
    }
  }

  /**
   * Removes the module object.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      final Map<String, Object> context) {
    BeanModule module = (BeanModule) getModule(context);
    final IEntity entityToRemove = (IEntity) module.getModuleObject();
    getTransactionTemplate(context).execute(
        new TransactionCallbackWithoutResult() {

          @Override
          protected void doInTransactionWithoutResult(TransactionStatus status) {
            IEntity entityClone = getController(context).cloneInUnitOfWork(
                entityToRemove);
            try {
              deleteEntity(entityClone, context);
            } catch (IllegalAccessException | NoSuchMethodException ex) {
              throw new ActionException(ex);
            } catch (InvocationTargetException ex) {
              if (ex.getCause() instanceof RuntimeException) {
                throw (RuntimeException) ex.getCause();
              }
              throw new ActionException(ex.getCause());
            }
            try {
              getController(context).performPendingOperations();
            } catch (RuntimeException ex) {
              getController(context).clearPendingOperations();
              throw ex;
            }
          }
        });
    removeFromSubModules(module.getParent(), entityToRemove);
    setActionParameter(entityToRemove, context);
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
   * @throws java.lang.reflect.InvocationTargetException
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
