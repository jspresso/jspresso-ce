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

import java.util.Map;

/**
 * This interface is implemented by classes aware of entities states dirtiness.
 *
 * @author Vincent Vandenschrick
 */
public interface IEntityDirtAware {

  /**
   * Gets the entity dirty properties (changed properties that need to be
   * updated to the persistent store as well as computed properties).
   *
   * @param entity
   *          the entity to get the dirty properties of.
   * @return an empty map if the entity is not dirty. The collection of dirty
   *         properties with their original values. null if dirty recording has
   *         not been started for this entity instance. In the latter case, the
   *         dirty state is unknown.
   */
  Map<String, Object> getDirtyProperties(IEntity entity);

  /**
   * Is dirty tracking enabled.
   *
   * @return {@code true} if dirty tracking is enabled.
   */
  boolean isDirtyTrackingEnabled();

  /**
   * Sets dirty tracking enabled.
   *
   * @param enabled
   *          {@code true} if enabled, {@code false} otherwise.
   */
  void setDirtyTrackingEnabled(boolean enabled);
}
