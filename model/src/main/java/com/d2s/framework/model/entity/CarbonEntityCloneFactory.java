/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity;

import java.io.Serializable;
import java.util.Map;

import com.d2s.framework.model.component.IComponent;
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

    carbonCopyComponent(entityToClone, clonedEntity, entityFactory);
    return clonedEntity;
  }

  private void carbonCopyComponent(IComponent componentToClone,
      IComponent clonedComponent, IEntityFactory entityFactory) {
    IComponentDescriptor<?> componentDescriptor = entityFactory
        .getComponentDescriptor(componentToClone.getContract());

    for (Map.Entry<String, Object> propertyEntry : componentToClone
        .straightGetProperties().entrySet()) {
      if (propertyEntry.getValue() != null) {
        IPropertyDescriptor propertyDescriptor = componentDescriptor
            .getPropertyDescriptor(propertyEntry.getKey());
        if (propertyDescriptor instanceof IRelationshipEndPropertyDescriptor) {
          if (propertyEntry.getValue() instanceof IComponent
              && !(propertyEntry.getValue() instanceof IEntity)) {
            clonedComponent.straightSetProperty(propertyEntry.getKey(),
                cloneComponent((IComponent) propertyEntry.getValue(),
                    entityFactory));
          }
        } else {
          clonedComponent.straightSetProperty(propertyEntry.getKey(),
              propertyEntry.getValue());
        }
      }
    }
  }

  private IComponent cloneComponent(IComponent componentToClone,
      IEntityFactory entityFactory) {
    IComponent clonedComponent = entityFactory
        .createComponentInstance(componentToClone.getContract());
    carbonCopyComponent(componentToClone, clonedComponent, entityFactory);
    return clonedComponent;
  }
}
