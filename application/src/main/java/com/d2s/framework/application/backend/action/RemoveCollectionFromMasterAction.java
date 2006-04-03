/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;
import com.d2s.framework.util.bean.ICollectionAccessor;
import com.d2s.framework.view.action.ActionException;
import com.d2s.framework.view.action.IActionHandler;

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
  public void execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    ICollectionConnector collectionConnector = getModelConnector(context);
    if (collectionConnector == null) {
      return;
    }
    ICollectionPropertyDescriptor collectionDescriptor = (ICollectionPropertyDescriptor) getModelDescriptor(context);
    Object master = collectionConnector.getParentConnector()
        .getConnectorValue();
    String property = collectionDescriptor.getName();
    ICollectionAccessor collectionAccessor = getAccessorFactory()
        .createCollectionPropertyAccessor(property, master.getClass());
    int deletionCount = 0;
    if (getSelectedIndices(context) != null) {
      for (int selectedIndex : getSelectedIndices(context)) {
        Object nextDetailToRemove = collectionConnector.getChildConnector(
            selectedIndex - deletionCount).getConnectorValue();
        try {
          collectionAccessor.removeFromValue(master, nextDetailToRemove);
          deletionCount++;
        } catch (IllegalAccessException ex) {
          throw new ActionException(ex);
        } catch (InvocationTargetException ex) {
          throw new ActionException(ex);
        } catch (NoSuchMethodException ex) {
          throw new ActionException(ex);
        }
      }
    }
  }

}
