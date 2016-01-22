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
package org.jspresso.framework.application.backend.action.persistence;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.AbstractCollectionAction;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IModelDescriptorAware;
import org.jspresso.framework.util.accessor.ICollectionAccessor;

/**
 * An action used in master/detail views to remove selected details from a
 * master domain object. More than just removing the selected details from their
 * owning collection, this action &quot;<i>cuts</i>&quot; the existing links
 * between the entities to remove and the rest of the domain then registers them
 * for deletion on next save operation.
 * <p/>
 * Note that cleaning of relationships is a 2 pass process. The 1st one is a dry
 * run that checks that no functional exception is thrown by the business rules.
 * The second one performs the actual cleaning.
 *
 * @author Vincent Vandenschrick
 */
public class RemoveCollectionFromMasterAction extends AbstractCollectionAction {

  /**
   * Constructs a new {@code RemoveCollectionFromMasterAction} instance.
   */
  public RemoveCollectionFromMasterAction() {
    // Disable bad frontend access checks.
    setBadFrontendAccessChecked(false);
  }

  /**
   * Retrieves the master and its managed collection from the model connector
   * then removes selected details from the managed collection.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    ICollectionConnector collectionConnector = getModelConnector(context);
    if (collectionConnector == null) {
      return false;
    }
    Class<?> elementComponentContract = getRemovedElementDescriptor(context).getComponentContract();
    int[] selectedIndices = getSelectedIndices(context);
    if (selectedIndices != null) {
      Object master = collectionConnector.getParentConnector().getConnectorValue();
      Class<?> targetContract;
      if (master instanceof IComponent) {
        targetContract = ((IComponent) master).getComponentContract();
      } else {
        targetContract = master.getClass();
      }
      ICollectionAccessor collectionAccessor = getAccessorFactory(context).createCollectionPropertyAccessor(
          collectionConnector.getId(), targetContract
          // Do not use the view model descriptor. It does not work for map models
          /*collectionConnector.getModelProvider().getModelDescriptor()
              .getComponentDescriptor().getComponentContract()*/,
          elementComponentContract);
      if (collectionAccessor instanceof IModelDescriptorAware) {
        ((IModelDescriptorAware) collectionAccessor).setModelDescriptor(getModelDescriptor(context));
      }
      try {
        Collection<?> existingCollection = collectionAccessor.getValue(master);
        // Traverse the collection reversely for performance reasons.
        for (int i = selectedIndices.length - 1; i >= 0; i--) {
          int selectedIndex = selectedIndices[i];
          IComponent nextDetailToRemove = collectionConnector.getChildConnector(selectedIndex).getConnectorValue();
          cleanRelationshipsOnDeletion(nextDetailToRemove, context, true);
          cleanRelationshipsOnDeletion(nextDetailToRemove, context, false);
          if (existingCollection.contains(nextDetailToRemove)) {
            collectionAccessor.removeFromValue(master, nextDetailToRemove);
          }
          // Now handled in cleanRelationshipsOnDeletion when dryRun=false
          // if (nextDetailToRemove instanceof IEntity) {
          // getController(context).registerForDeletion(
          // (IEntity) nextDetailToRemove);
          // }
        }
      } catch (IllegalAccessException | NoSuchMethodException ex) {
        throw new ActionException(ex);
      } catch (InvocationTargetException ex) {
        if (ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        throw new ActionException(ex.getCause());
      }
    }
    return super.execute(actionHandler, context);
  }

  /**
   * Gets added element descriptor.
   *
   * @param context
   *     the context
   * @return the added element descriptor
   */
  protected IComponentDescriptor<?> getRemovedElementDescriptor(Map<String, Object> context) {
    IComponentDescriptor<?> elementDescriptor;
    String collectionPropertyName = getModelDescriptor(context).getName();
    Object master = getModelConnector(context).getModelProvider().getModel();
    if (master instanceof IComponent) {
      // Component type should be refined depending on concrete master. See property translations for instance.
      elementDescriptor = ((ICollectionPropertyDescriptor<?>) getEntityFactory(context).getComponentDescriptor(
          ((IComponent) master).getComponentContract()).getPropertyDescriptor(collectionPropertyName))
          .getReferencedDescriptor().getElementDescriptor();
    } else {
      elementDescriptor = ((ICollectionPropertyDescriptor<?>) getModelDescriptor(context)).getReferencedDescriptor()
                                                                                          .getElementDescriptor();
    }
    return elementDescriptor;
  }
}
