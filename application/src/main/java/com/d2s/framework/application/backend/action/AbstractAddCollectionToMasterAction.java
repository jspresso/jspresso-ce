/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.ActionException;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.binding.ConnectorHelper;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.model.IModelValueConnector;
import com.d2s.framework.binding.model.ModelPropertyConnector;
import com.d2s.framework.util.accessor.ICollectionAccessor;
import com.d2s.framework.util.accessor.IListAccessor;
import com.d2s.framework.util.bean.IPropertyChangeCapable;

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
public abstract class AbstractAddCollectionToMasterAction extends
    AbstractCollectionAction {

  /**
   * Retrieves the master and its managed collection from the model connector
   * then creates a new detail and adds it to the managed collection.
   * <p>
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    ICollectionConnector collectionConnector = getModelConnector(context);
    if (collectionConnector == null) {
      return false;
    }

    List<?> newComponents = getAddedComponents(context);
    if (newComponents != null && newComponents.size() > 0) {
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
      try {
        int index = -1;
        if (collectionAccessor instanceof IListAccessor) {
          if (getSelectedIndices(context) != null
              && getSelectedIndices(context).length > 0) {
            index = getSelectedIndices(context)[getSelectedIndices(context).length - 1];
          }
        }
        for (int i = 0; i < newComponents.size(); i++) {
          if (index >= 0) {
            ((IListAccessor) collectionAccessor).addToValue(master, index + 1
                + i, newComponents.get(i));
          } else {
            collectionAccessor.addToValue(master, newComponents.get(i));
          }
        }
        if (!(master instanceof IPropertyChangeCapable)
            && collectionConnector instanceof ModelPropertyConnector) {
          ((ModelPropertyConnector) collectionConnector).propertyChange(null);
        }
      } catch (IllegalAccessException ex) {
        throw new ActionException(ex);
      } catch (InvocationTargetException ex) {
        throw new ActionException(ex);
      } catch (NoSuchMethodException ex) {
        throw new ActionException(ex);
      }
      context.put(ActionContextConstants.ACTION_PARAM, newComponents);
      context.put(ActionContextConstants.SELECTED_INDICES, ConnectorHelper
          .getIndicesOf(collectionConnector, newComponents));
    }
    return super.execute(actionHandler, context);
  }

  /**
   * Gets the new entity to add. It is created using the informations contained
   * in the context.
   * 
   * @param context
   *          the action context.
   * @return the entity to add to the collection.
   */
  @SuppressWarnings("unchecked")
  protected abstract List<?> getAddedComponents(Map<String, Object> context);
}
