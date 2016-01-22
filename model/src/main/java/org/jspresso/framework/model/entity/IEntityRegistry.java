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
import java.util.Map;

/**
 * This interface defines the contract of an application session entity
 * registry. An entity registry is used at the application session level to help
 * in keeping object identity across the whole JVM. Entity registries must be
 * memory friendly. This means that the registry must not prevent an object from
 * being garbage collected. Given this point implementations should leverage on
 * weak references.
 *
 * @author Vincent Vandenschrick
 */
public interface IEntityRegistry {

  /**
   * Get an entity from the registry.
   *
   * @param entityContract
   *          the entity contract.
   * @param id
   *          the entity identifier.
   * @return the registered entity or <tt>null</tt>
   */
  IEntity get(Class<? extends IEntity> entityContract, Serializable id);

  /**
   * Registers an entity in the registry.
   *
   * @param entityContract
   *          the entity contract.
   * @param id
   *          the entity identifier.
   * @param entity
   *          the entity to register.
   */
  void register(Class<? extends IEntity> entityContract, Serializable id,
      IEntity entity);

  /**
   * Clears the registry.
   */
  void clear();

  /**
   * Gets registered entities.
   *
   * @return the registered entities
   */
  Map<Class<? extends IEntity>,Map<Serializable,IEntity>> getRegisteredEntities();
}
