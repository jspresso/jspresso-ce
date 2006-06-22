/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.ActionException;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.binding.ConnectorHelper;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
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
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    ICollectionConnector collectionConnector = getModelConnector(context);
    if (collectionConnector == null) {
      return false;
    }
    Object master = collectionConnector.getParentConnector()
        .getConnectorValue();
    ICollectionAccessor collectionAccessor = getAccessorFactory(context)
        .createCollectionPropertyAccessor(collectionConnector.getId(),
            master.getClass());

    IEntity newEntity = getNewEntity(context);

    if (newEntity != null) {
      try {
        // int index = -1;
        // if (collectionAccessor instanceof IListAccessor) {
        // if (getSelectedIndices(context) != null
        // && getSelectedIndices(context).length > 0) {
        // index =
        // getSelectedIndices(context)[getSelectedIndices(context).length - 1];
        // }
        // }
        // if (index >= 0) {
        // ((IListAccessor) collectionAccessor).addToValue(master, index + 1,
        // newEntity);
        // } else {
        collectionAccessor.addToValue(master, newEntity);
        // }
      } catch (IllegalAccessException ex) {
        throw new ActionException(ex);
      } catch (InvocationTargetException ex) {
        throw new ActionException(ex);
      } catch (NoSuchMethodException ex) {
        throw new ActionException(ex);
      }
      context.put(ActionContextConstants.SELECTED_INDICES, ConnectorHelper
          .getIndicesOf(collectionConnector, Collections.singleton(newEntity)));
    }
    return true;
  }

  /**
   * Gets the new entity to add. It is createdusing the informations contained
   * in the context.
   * 
   * @param context
   *          the action context.
   * @return the entity to add to the collection.
   */
  @SuppressWarnings("unchecked")
  protected IEntity getNewEntity(Map<String, Object> context) {
    IComponentDescriptor elementDescriptor = (IComponentDescriptor) context
        .get(ActionContextConstants.ELEMENT_DESCRIPTOR);
    if (elementDescriptor == null) {
      elementDescriptor = ((ICollectionPropertyDescriptor) getModelDescriptor(context))
          .getReferencedDescriptor().getElementDescriptor();
    }

    IEntity newEntity = getEntityFactory(context).createEntityInstance(
        (Class<? extends IEntity>) elementDescriptor.getComponentContract());
    return newEntity;
  }
}
