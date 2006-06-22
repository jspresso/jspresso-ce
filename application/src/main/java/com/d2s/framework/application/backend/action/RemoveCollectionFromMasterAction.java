/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.d2s.framework.action.ActionException;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.backend.session.IApplicationSession;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.IReferencePropertyDescriptor;
import com.d2s.framework.model.descriptor.entity.IEntityDescriptor;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IEntityDescriptorRegistry;
import com.d2s.framework.model.entity.IEntityFactory;
import com.d2s.framework.util.accessor.IAccessorFactory;
import com.d2s.framework.util.accessor.ICollectionAccessor;

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
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    ICollectionConnector collectionConnector = getModelConnector(context);
    if (collectionConnector == null) {
      return false;
    }
    int deletionCount = 0;
    if (getSelectedIndices(context) != null) {
      IEntityFactory entityFactory = getEntityFactory(context);
      for (int selectedIndex : getSelectedIndices(context)) {
        IEntity nextDetailToRemove = (IEntity) collectionConnector
            .getChildConnector(selectedIndex - deletionCount)
            .getConnectorValue();
        try {
          cleanRelationshipsOnDeletion(nextDetailToRemove, entityFactory,
              getAccessorFactory(context), getApplicationSession(context));
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
    return true;
  }

  /**
   * Performs necessary cleanings when an entity is deleted.
   * 
   * @param entity
   *          the deleted entity.
   * @param entityDescriptorRegistry
   *          the entity descriptor registry.
   * @param accessorFactory
   *          the accessor factory.
   * @param applicationSession
   *          the application session.
   * @throws IllegalAccessException
   *           whenever this kind of exception occurs.
   * @throws InvocationTargetException
   *           whenever this kind of exception occurs.
   * @throws NoSuchMethodException
   *           whenever this kind of exception occurs.
   */
  @SuppressWarnings("unchecked")
  public static void cleanRelationshipsOnDeletion(IEntity entity,
      IEntityDescriptorRegistry entityDescriptorRegistry,
      IAccessorFactory accessorFactory, IApplicationSession applicationSession)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    IEntityDescriptor entityDescriptor = entityDescriptorRegistry
        .getEntityDescriptor(entity.getContract());
    for (Map.Entry<String, Object> property : entity.straightGetProperties()
        .entrySet()) {
      if (property.getValue() != null) {
        IPropertyDescriptor propertyDescriptor = entityDescriptor
            .getPropertyDescriptor(property.getKey());
        if (propertyDescriptor instanceof IReferencePropertyDescriptor) {
          accessorFactory.createPropertyAccessor(property.getKey(),
              entity.getContract()).setValue(entity, null);
        } else if (propertyDescriptor instanceof ICollectionPropertyDescriptor) {
          if (((ICollectionPropertyDescriptor) propertyDescriptor)
              .isComposition()) {
            applicationSession.initializePropertyIfNeeded(entity,
                propertyDescriptor);
            for (IEntity composedEntity : new ArrayList<IEntity>(
                (Collection<IEntity>) property.getValue())) {
              cleanRelationshipsOnDeletion(composedEntity,
                  entityDescriptorRegistry, accessorFactory, applicationSession);
            }
          } else {
            ICollectionAccessor collectionAccessor = accessorFactory
                .createCollectionPropertyAccessor(property.getKey(), entity
                    .getContract());
            collectionAccessor.setValue(entity, null);
          }
        }
      }
    }
    applicationSession.deleteEntity(entity);
  }
}
