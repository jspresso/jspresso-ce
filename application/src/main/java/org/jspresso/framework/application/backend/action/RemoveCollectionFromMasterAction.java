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
package org.jspresso.framework.application.backend.action;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.model.ModelPropertyConnector;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.descriptor.IModelDescriptorAware;
import org.jspresso.framework.util.accessor.ICollectionAccessor;
import org.jspresso.framework.util.bean.IPropertyChangeCapable;

/**
 * An action used in master/detail views to remove selected details from a
 * master domain object. No further operation (like actual removal from a
 * persistent store) is performed by this action.
 *
 * @author Vincent Vandenschrick
 */
public class RemoveCollectionFromMasterAction extends AbstractCollectionAction {

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
    int[] selectedIndices = getSelectedIndices(context);
    if (selectedIndices != null) {
      Class<?> elementComponentContract = getModelDescriptor(context)
          .getCollectionDescriptor().getElementDescriptor()
          .getComponentContract();
      Object master = collectionConnector.getParentConnector()
          .getConnectorValue();
      Class<?> targetContract;
      if (master instanceof IComponent) {
        targetContract = ((IComponent) master).getComponentContract();
      } else {
        targetContract = master.getClass();
      }
      ICollectionAccessor collectionAccessor = getAccessorFactory(context)
          .createCollectionPropertyAccessor(
              collectionConnector.getId(),
              targetContract
              // Do not use the view model descriptor. It does not work for map models
              /*collectionConnector.getModelProvider().getModelDescriptor()
                  .getComponentDescriptor().getComponentContract()*/,
              elementComponentContract);
      if (collectionAccessor instanceof IModelDescriptorAware) {
        ((IModelDescriptorAware) collectionAccessor)
            .setModelDescriptor(getModelDescriptor(context));
      }
      // Traverse the collection reversely for performance reasons.
      for (int i = selectedIndices.length - 1; i >= 0; i--) {
        int selectedIndex = selectedIndices[i];
        Object nextDetailToRemove = collectionConnector.getChildConnector(
            selectedIndex).getConnectorValue();
        try {
          collectionAccessor.removeFromValue(master, nextDetailToRemove);
        } catch (IllegalAccessException | NoSuchMethodException ex) {
          throw new ActionException(ex);
        } catch (InvocationTargetException ex) {
          if (ex.getCause() instanceof RuntimeException) {
            throw (RuntimeException) ex.getCause();
          }
          throw new ActionException(ex.getCause());
        }
      }
      if (!(master instanceof IPropertyChangeCapable)
          && collectionConnector instanceof ModelPropertyConnector) {
        ((ModelPropertyConnector) collectionConnector).propertyChange(null);
      }
    }
    return super.execute(actionHandler, context);
  }
}
