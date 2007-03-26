/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.persistence.hibernate;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.model.descriptor.ICollectionDescriptorProvider;
import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.IReferencePropertyDescriptor;
import com.d2s.framework.model.entity.IEntity;

/**
 * Base class for backend actions acting on collections.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractHibernateCollectionAction extends
    AbstractHibernateAction {

  /**
   * refined to return a collection connector.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public ICollectionConnector getModelConnector(Map<String, Object> context) {
    return (ICollectionConnector) super.getModelConnector(context);
  }

  /**
   * Gets the selected indices from the context. it uses the
   * <code>ActionContextConstants.SELECTED_INDICES</code> key.
   * 
   * @param context
   *          the action context.
   * @return the selected indices if any.
   */
  public int[] getSelectedIndices(Map<String, Object> context) {
    return (int[]) context.get(ActionContextConstants.SELECTED_INDICES);
  }

  /**
   * Refined to return a collection descriptor.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public ICollectionDescriptorProvider getModelDescriptor(
      Map<String, Object> context) {
    return (ICollectionDescriptorProvider) super.getModelDescriptor(context);
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
