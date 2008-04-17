/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.entity;

import java.io.Serializable;
import java.util.Map;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IRelationshipEndPropertyDescriptor;


/**
 * Does a "carbon" copy of the entity including its id and version. This factory
 * is used only for technical purposes.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
  public <E extends IComponent> E cloneComponent(IComponent componentToClone,
      IEntityFactory entityFactory) {
    E clonedComponent = (E) entityFactory
        .createComponentInstance(componentToClone.getContract());
    carbonCopyComponent(componentToClone, clonedComponent, entityFactory);
    return clonedComponent;
  }

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
        if (!(propertyDescriptor instanceof IRelationshipEndPropertyDescriptor)) {
          clonedComponent.straightSetProperty(propertyEntry.getKey(),
              propertyEntry.getValue());
        }
      }
    }
  }
}
