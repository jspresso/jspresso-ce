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

import org.jspresso.framework.model.component.IComponentFactory;

/**
 * This interface defines the contract of an entities factory.
 *
 * @author Vincent Vandenschrick
 */
public interface IEntityFactory extends IComponentFactory {

  /**
   * Creates a new entity instance based on the entity descriptor. The entity
   * will be initialized with any necessary starting state. Lifecycle
   * interceptor are also created.
   *
   * @param <T>
   *          the concrete class of the created entity.
   * @param entityContract
   *          the class of the entity to create.
   * @return the entity instance.
   */
  <T extends IEntity> T createEntityInstance(Class<T> entityContract);

  /**
   * Instantiates an entity using a given ID. No lifecycle interceptor nor
   * registration of any kind is performed.
   *
   * @param <T>
   *          the concrete class of the created entity.
   * @param entityContract
   *          the class of the entity to create.
   * @param id
   *          the identifier to set on the entity.
   * @return the entity instance.
   */
  <T extends IEntity> T createEntityInstance(Class<T> entityContract,
      Serializable id);

  /**
   * Instantiates an entity using a given ID. No lifecycle interceptor nor
   * registration of any kind is performed.
   *
   * @param <T>
   *          the concrete class of the created entity.
   * @param entityContract
   *          the class of the entity to create.
   * @param id
   *          the identifier to set on the entity.
   * @param performInitialization
   *          should we perform initialization ?
   * @return the entity instance.
   */
  <T extends IEntity> T createEntityInstance(Class<T> entityContract,
      Serializable id, boolean performInitialization);

}
