/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.model.entity;

import java.io.Serializable;

import org.apache.commons.lang3.ObjectUtils;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IRelationshipEndPropertyDescriptor;

/**
 * Does a "carbon" copy of the entity including its id and version. This factory
 * is used only for technical purposes.
 *
 * @author Vincent Vandenschrick
 */
public class CarbonEntityCloneFactory implements IEntityCloneFactory {

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public <E extends IComponent> E cloneComponent(E componentToClone, IEntityFactory entityFactory) {
    E clonedComponent = (E) entityFactory.createComponentInstance(componentToClone.getComponentContract());
    carbonCopyComponent(componentToClone, clonedComponent, entityFactory);
    return clonedComponent;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public <E extends IEntity> E cloneEntity(E entityToClone, IEntityFactory entityFactory) {
    E clonedEntity = (E) entityFactory.createEntityInstance(entityToClone.getComponentContract(),
        (Serializable) entityToClone.straightGetProperty(IEntity.ID));

    carbonCopyComponent(entityToClone, clonedEntity, entityFactory);
    return clonedEntity;
  }

  /**
   * Carbon copies all scalar properties.
   *
   * @param componentToClone
   *          the source.
   * @param clonedComponent
   *          the copy.
   * @param entityFactory
   *          the entity factory to use.
   */
  public static void carbonCopyComponent(IComponent componentToClone, IComponent clonedComponent,
      IEntityFactory entityFactory) {
    if (componentToClone == clonedComponent) {
      return;
    }
    IComponentDescriptor<?> componentDescriptor = entityFactory.getComponentDescriptor(componentToClone
        .getComponentContract());

    for (IPropertyDescriptor propertyDescriptor : componentDescriptor.getPropertyDescriptors()) {
      if (!(propertyDescriptor instanceof IRelationshipEndPropertyDescriptor)
          && /* propertyDescriptor.isModifiable() */!(propertyDescriptor.isComputed() && propertyDescriptor
              .getPersistenceFormula() == null)) {
        String propertyName = propertyDescriptor.getName();
        clonedComponent.straightSetProperty(propertyName,
            ObjectUtils.cloneIfPossible(componentToClone.straightGetProperty(propertyName)));
      }
    }
  }
}
