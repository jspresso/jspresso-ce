/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

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
  public void execute(@SuppressWarnings("unused")
  IActionHandler actionHandler) {
    ICollectionConnector collectionConnector = getModelConnector();
    if (collectionConnector == null) {
      return;
    }
    Object master = collectionConnector.getParentConnector()
        .getConnectorValue();
    ICollectionAccessor collectionAccessor = getAccessorFactory()
        .createCollectionPropertyAccessor(collectionConnector.getId(), master.getClass());

    IEntity newEntity = getNewEntity();

    if (newEntity != null) {
      try {
        int index = -1;
        if (collectionAccessor instanceof IListAccessor) {
          if (getSelectedIndices() != null && getSelectedIndices().length > 0) {
            index = getSelectedIndices()[getSelectedIndices().length - 1];
          }
        }
        if (index >= 0) {
          ((IListAccessor) collectionAccessor).addToValue(master, index + 1,
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
      getContext().put(ActionContextConstants.SELECTED_INDICES,
          ConnectorHelper.getIndicesOf(collectionConnector, Collections
              .singleton(newEntity)));
    }
  }

  /**
   * Gets the new entity to add. It is createdusing the informations contained
   * in the context.
   * 
   * @return the entity to add to the collection.
   */
  protected IEntity getNewEntity() {
    IComponentDescriptor elementDescriptor = (IComponentDescriptor) getContext()
        .get(ActionContextConstants.ELEMENT_DESCRIPTOR);
    if (elementDescriptor == null) {
      elementDescriptor = ((ICollectionPropertyDescriptor) getModelDescriptor())
          .getReferencedDescriptor().getElementDescriptor();
    }

    IEntity newEntity = getEntityFactory().createEntityInstance(
        elementDescriptor.getComponentContract());
    return newEntity;
  }
}
