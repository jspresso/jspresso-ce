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
package org.jspresso.framework.application.backend.action.persistence.hibernate;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.descriptor.IModelDescriptorAware;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.util.accessor.ICollectionAccessor;

/**
 * An action used in master/detail views to remove selected details from a
 * master domain object.
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
public class RemoveCollectionFromMasterAction extends
    AbstractHibernateCollectionAction {

  /**
   * Retrieves the master and its managed collection from the model connector
   * then removes selected details from the managed collection.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    ICollectionConnector collectionConnector = getModelConnector(context);
    if (collectionConnector == null) {
      return false;
    }
    Class<?> elementComponentContract = getModelDescriptor(context)
        .getCollectionDescriptor().getElementDescriptor()
        .getComponentContract();
    int[] selectedIndices = getSelectedIndices(context);
    if (selectedIndices != null) {
      Object master = collectionConnector.getParentConnector()
          .getConnectorValue();
      ICollectionAccessor collectionAccessor = getAccessorFactory(context)
          .createCollectionPropertyAccessor(
              collectionConnector.getId(),
              collectionConnector.getModelProvider().getModelDescriptor()
                  .getComponentDescriptor().getComponentContract(),
              elementComponentContract);
      if (collectionAccessor instanceof IModelDescriptorAware) {
        ((IModelDescriptorAware) collectionAccessor)
            .setModelDescriptor(getModelDescriptor(context));
      }
      try {
        Collection<?> existingCollection = collectionAccessor.getValue(master);
        // Traverse the collection reversly for performance reasons.
        for (int i = selectedIndices.length - 1; i >= 0; i--) {
          int selectedIndex = selectedIndices[i];
          IComponent nextDetailToRemove = (IComponent) collectionConnector
              .getChildConnector(selectedIndex).getConnectorValue();
          cleanRelationshipsOnDeletion(nextDetailToRemove, context, true);
          cleanRelationshipsOnDeletion(nextDetailToRemove, context, false);
          if (existingCollection.contains(nextDetailToRemove)) {
            collectionAccessor.removeFromValue(master, nextDetailToRemove);
          }
          if (nextDetailToRemove instanceof IEntity) {
            getApplicationSession(context).registerForDeletion(
                (IEntity) nextDetailToRemove);
          }
        }
      } catch (IllegalAccessException ex) {
        throw new ActionException(ex);
      } catch (InvocationTargetException ex) {
        throw new ActionException(ex);
      } catch (NoSuchMethodException ex) {
        throw new ActionException(ex);
      }
    }
    return super.execute(actionHandler, context);
  }
}
