/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.persistence.hibernate.AbstractHibernateCollectionAction;
import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.application.model.BeanModule;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.model.entity.IEntity;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

/**
 * This action removes the selected objects from the projected collection.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
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

      public Object doInTransaction(
          @SuppressWarnings("unused") TransactionStatus status) {
        for (IEntity entityToRemove : moduleObjectsToRemove) {
          try {
            deleteEntity(entityToRemove, context);
          } catch (IllegalAccessException ex) {
            throw new ActionException(ex);
          } catch (InvocationTargetException ex) {
            throw new ActionException(ex);
          } catch (NoSuchMethodException ex) {
            throw new ActionException(ex);
          }
        }
        getApplicationSession(context).performPendingOperations();
        return null;
      }
    });
    for (IEntity entityToRemove : moduleObjectsToRemove) {
      projectedCollection.remove(entityToRemove);
      removeFromSubModules(module, entityToRemove);
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
    getApplicationSession(context).registerForDeletion(entity);
  }
}
