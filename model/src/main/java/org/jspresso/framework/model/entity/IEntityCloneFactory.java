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

import org.jspresso.framework.model.component.IComponent;

/**
 * A factory to clone entities.
 *
 * @author Vincent Vandenschrick
 */
public interface IEntityCloneFactory {

  /**
   * Clones a component.
   *
   * @param <E>
   *          the component contract (class or interface) to clone.
   * @param componentToClone
   *          the component to clone.
   * @param entityFactory
   *          the entity factory to use to create new entities.
   * @return the cloned component.
   */
  <E extends IComponent> E cloneComponent(E componentToClone,
      IEntityFactory entityFactory);

  /**
   * Clones an entity.
   *
   * @param <E>
   *          the entity contract (class or interface) to clone.
   * @param entityToClone
   *          the entity to clone.
   * @param entityFactory
   *          the entity factory to use to create the new entities.
   * @return the cloned entity.
   */
  <E extends IEntity> E cloneEntity(E entityToClone,
      IEntityFactory entityFactory);
}
