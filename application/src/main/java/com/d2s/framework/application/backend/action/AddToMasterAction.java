/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.ActionException;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.binding.ConnectorHelper;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.util.bean.ICollectionAccessor;

/**
 * An action used in master/detail views to create and add a new detail to a
 * master domain object.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class AddToMasterAction extends AbstractCollectionAction {

  /**
   * Retrieves the master and its managed collection from the model connector
   * then creates a new detail and adds it to the managed collection.
   * <p>
   * {@inheritDoc}
   */
  public Map<String, Object> execute(@SuppressWarnings("unused")
  IActionHandler actionHandler) {
    ICollectionConnector collectionConnector = getModelConnector();
    if (collectionConnector == null) {
      return null;
    }
    Map<String, Object> executionResult = new HashMap<String, Object>();
    ICollectionPropertyDescriptor collectionDescriptor = (ICollectionPropertyDescriptor) getModelDescriptor();
    Object master = collectionConnector.getParentConnector()
        .getConnectorValue();
    String property = collectionDescriptor.getName();
    ICollectionAccessor collectionAccessor = getAccessorFactory()
        .createCollectionPropertyAccessor(
            property,
            master.getClass(),
            collectionDescriptor.getReferencedDescriptor()
                .getElementDescriptor().getComponentContract());
    IEntity newEntity = getEntityFactory().createEntityInstance(
        collectionDescriptor.getReferencedDescriptor().getElementDescriptor()
            .getComponentContract());
    try {
      collectionAccessor.addToValue(master, newEntity);
    } catch (IllegalAccessException ex) {
      throw new ActionException(ex);
    } catch (InvocationTargetException ex) {
      throw new ActionException(ex);
    } catch (NoSuchMethodException ex) {
      throw new ActionException(ex);
    }
    executionResult.put(ActionContextConstants.SELECTED_INDICES,
        ConnectorHelper.getIndicesOf(collectionConnector, Collections
            .singleton(newEntity)));
    return executionResult;
  }
}
