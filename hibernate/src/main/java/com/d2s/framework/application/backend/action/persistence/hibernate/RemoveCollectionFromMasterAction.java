/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.persistence.hibernate;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.d2s.framework.action.ActionException;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.IReferencePropertyDescriptor;
import com.d2s.framework.model.entity.IEntity;

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
public class RemoveCollectionFromMasterAction extends
    AbstractHibernateCollectionAction {

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
      // Traverse the collection reversly for performance reasons.
      for (int i = selectedIndices.length - 1; i >= 0; i--) {
        int selectedIndex = selectedIndices[i];
        IEntity nextDetailToRemove = (IEntity) collectionConnector
            .getChildConnector(selectedIndex).getConnectorValue();
        try {
          cleanRelationshipsOnDeletion(nextDetailToRemove, context);
        } catch (IllegalAccessException ex) {
          throw new ActionException(ex);
        } catch (InvocationTargetException ex) {
          throw new ActionException(ex);
        } catch (NoSuchMethodException ex) {
          throw new ActionException(ex);
        }
      }
    }
    return super.execute(actionHandler, context);
  }

  /**
   * Performs necessary cleanings when an entity is deleted.
   * 
   * @param entity
   *          the deleted entity.
   * @param context
   *          The action context.
   * @throws IllegalAccessException
   *           whenever this kind of exception occurs.
   * @throws InvocationTargetException
   *           whenever this kind of exception occurs.
   * @throws NoSuchMethodException
   *           whenever this kind of exception occurs.
   */
  @SuppressWarnings("unchecked")
  protected void cleanRelationshipsOnDeletion(IEntity entity,
      Map<String, Object> context) throws IllegalAccessException,
      InvocationTargetException, NoSuchMethodException {
    IComponentDescriptor entityDescriptor = getEntityFactory(context)
        .getComponentDescriptor(entity.getContract());
    for (Map.Entry<String, Object> property : entity.straightGetProperties()
        .entrySet()) {
      if (property.getValue() != null) {
        IPropertyDescriptor propertyDescriptor = entityDescriptor
            .getPropertyDescriptor(property.getKey());
        if (propertyDescriptor instanceof IReferencePropertyDescriptor) {
          getAccessorFactory(context).createPropertyAccessor(property.getKey(),
              entity.getContract()).setValue(entity, null);
        } else if (propertyDescriptor instanceof ICollectionPropertyDescriptor) {
          if (((ICollectionPropertyDescriptor) propertyDescriptor)
              .isComposition()) {
            getApplicationSession(context).initializePropertyIfNeeded(entity,
                propertyDescriptor);
            for (IEntity composedEntity : new ArrayList<IEntity>(
                (Collection<IEntity>) property.getValue())) {
              cleanRelationshipsOnDeletion(composedEntity, context);
            }
          } else {
            getAccessorFactory(context).createPropertyAccessor(
                property.getKey(), entity.getContract()).setValue(entity, null);
          }
        }
      }
    }
    getApplicationSession(context).deleteEntity(entity);
  }
}
