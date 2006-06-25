/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity;

import java.io.Serializable;
import java.util.Map;

import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.IRelationshipEndPropertyDescriptor;

/**
 * Does a "carbon" copy of the entity including its id and version. This factory
 * is used only for technical purposes.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class CarbonEntityCloneFactory implements IEntityCloneFactory {

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public <E extends IEntity> E cloneEntity(E entityToClone,
      IEntityFactory entityFactory) {
    E clonedEntity = (E) entityFactory.createEntityInstance(entityToClone
        .getContract(), (Serializable) entityToClone
        .straightGetProperty(IEntity.ID));

    IComponentDescriptor componentDescriptor = entityFactory
        .getComponentDescriptor(entityToClone.getContract());

    for (Map.Entry<String, Object> propertyEntry : entityToClone
        .straightGetProperties().entrySet()) {
      if (propertyEntry.getValue() != null) {
        IPropertyDescriptor propertyDescriptor = componentDescriptor
            .getPropertyDescriptor(propertyEntry.getKey());
        if (!(propertyDescriptor instanceof IRelationshipEndPropertyDescriptor)) {
          clonedEntity.straightSetProperty(propertyEntry.getKey(),
              propertyEntry.getValue());
        }
      }
    }
    return clonedEntity;
  }
}
