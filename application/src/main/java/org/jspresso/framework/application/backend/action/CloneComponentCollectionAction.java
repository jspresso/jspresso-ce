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
package org.jspresso.framework.application.backend.action;

import java.util.Map;

import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.ILifecycleCapable;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityCloneFactory;

/**
 * An action used duplicate a collection of entities or components. This action
 * is parametrized with a clone factory ({@code IEntityCloneFactory}) to
 * perform the actual component cloning. Executing this action will result in
 * adding the cloned component(s) to the underlying model collection.
 *
 * @author Vincent Vandenschrick
 */
public class CloneComponentCollectionAction extends
    AbstractCloneCollectionAction {

  private IEntityCloneFactory entityCloneFactory;

  /**
   * Configures the entity clone factory to use to clone the components or
   * entities.
   *
   * @param entityCloneFactory
   *          the entityCloneFactory to set.
   */
  public void setEntityCloneFactory(IEntityCloneFactory entityCloneFactory) {
    this.entityCloneFactory = entityCloneFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object cloneElement(Object element, Map<String, Object> context) {
    if (element instanceof IComponent) {
      IComponent clone;
      if (element instanceof IEntity) {
        clone = entityCloneFactory.cloneEntity((IEntity) element,
            getEntityFactory(context));
      } else {
        clone = entityCloneFactory.cloneComponent((IComponent) element,
            getEntityFactory(context));
      }
      if (clone instanceof ILifecycleCapable) {
        ((ILifecycleCapable) clone).onClone((IComponent) element);
      }
      return clone;
    }
    throw new ActionException(
        "Only components are supported by default clone action.");
  }

}
