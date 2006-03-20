/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.d2s.framework.binding.ConnectorHelper;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.util.bean.ICollectionAccessor;
import com.d2s.framework.util.bean.IListAccessor;
import com.d2s.framework.view.action.ActionContextConstants;
import com.d2s.framework.view.action.ActionException;
import com.d2s.framework.view.action.IActionHandler;

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
        .createCollectionPropertyAccessor(property, master.getClass());

    IComponentDescriptor elementDescriptor = (IComponentDescriptor) getContext()
        .get(ActionContextConstants.ELEMENT_DESCRIPTOR);

    if (elementDescriptor == null) {
      elementDescriptor = collectionDescriptor.getReferencedDescriptor()
          .getElementDescriptor();
    }

    if (elementDescriptor.isPurelyAbstract()) {
      throw new ActionException(elementDescriptor.getName()
          + "is purely abstract. It cannot be instanciated.");
    }

    IEntity newEntity = getEntityFactory().createEntityInstance(
        elementDescriptor.getComponentContract());
    try {
      int index = -1;
      if (collectionAccessor instanceof IListAccessor) {
        if (getSelectedIndices() != null && getSelectedIndices().length > 0) {
          index = getSelectedIndices()[getSelectedIndices().length - 1];
        }
      }
      if (index >= 0) {
        ((IListAccessor) collectionAccessor).addToValue(master, index,
            newEntity);
      } else {
        collectionAccessor.addToValue(master, newEntity);
      }
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
