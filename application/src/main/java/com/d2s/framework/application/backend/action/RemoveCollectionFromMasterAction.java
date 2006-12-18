/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import com.d2s.framework.action.ActionException;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.model.IModelValueConnector;
import com.d2s.framework.binding.model.ModelPropertyConnector;
import com.d2s.framework.model.descriptor.IModelDescriptorAware;
import com.d2s.framework.util.accessor.ICollectionAccessor;
import com.d2s.framework.util.bean.IPropertyChangeCapable;

/**
 * An action used in master/detail views to remove selected details from a
 * master domain object.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
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
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    ICollectionConnector collectionConnector = getModelConnector(context);
    if (collectionConnector == null) {
      return false;
    }
    int[] selectedIndices = getSelectedIndices(context);
    if (selectedIndices != null) {
      Class newComponentContract = getModelDescriptor(context)
          .getCollectionDescriptor().getElementDescriptor()
          .getComponentContract();
      Object master = collectionConnector.getParentConnector()
          .getConnectorValue();
      ICollectionAccessor collectionAccessor = getAccessorFactory(context)
          .createCollectionPropertyAccessor(
              collectionConnector.getId(),
              ((IModelValueConnector) collectionConnector).getModelProvider()
                  .getModelDescriptor().getComponentDescriptor()
                  .getComponentContract(), newComponentContract);
      if (collectionAccessor instanceof IModelDescriptorAware) {
        ((IModelDescriptorAware) collectionAccessor)
            .setModelDescriptor(getModelDescriptor(context));
      }
      // Traverse the collection reversly for performance reasons.
      for (int i = selectedIndices.length - 1; i >= 0; i--) {
        int selectedIndex = selectedIndices[i];
        Object nextDetailToRemove = collectionConnector.getChildConnector(
            selectedIndex).getConnectorValue();
        try {
          collectionAccessor.removeFromValue(master, nextDetailToRemove);
        } catch (IllegalAccessException ex) {
          throw new ActionException(ex);
        } catch (InvocationTargetException ex) {
          throw new ActionException(ex);
        } catch (NoSuchMethodException ex) {
          throw new ActionException(ex);
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
