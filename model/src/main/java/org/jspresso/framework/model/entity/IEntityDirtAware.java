/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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

import java.util.Map;

/**
 * This interface is implemented by classes awre of entities states dirtyness.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IEntityDirtAware {

  /**
   * Gets the entity dirty properties (changed properties that need to be
   * updated to the persistent store).
   * 
   * @param entity
   *          the entity to get the dirty properties of.
   * @return null or an empty map if the entity is not dirty. The collection of
   *         dirty properties with their original values.
   */
  Map<String, Object> getDirtyProperties(IEntity entity);

  /**
   * Gets wether the entity is dirty (has changes that need to be updated to the
   * persistent store).
   * 
   * @param entity
   *          the entity to test.
   * @return true if the entity is dirty.
   */
  boolean isDirty(IEntity entity);

  /**
   * Gets wether the entity property is dirty (has changes that need to be
   * updated to the persistent store).
   * 
   * @param entity
   *          the entity to test.
   * @param property
   *          the entity property to test.
   * @return true if the entity is dirty.
   */
  boolean isDirty(IEntity entity, String property);

}
