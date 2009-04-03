/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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

/**
 * Defaines the contract of any component able to handle entities lifecycle.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IEntityLifecycleHandler {

  /**
   * Clears the pending operations.
   */
  void clearPendingOperations();

  /**
   * Tests wether an entity has been registered for deletion.
   * 
   * @param entity
   *            the entity to test.
   * @return true if the entity has been registered for deletion.
   */
  boolean isEntityRegisteredForDeletion(IEntity entity);

  /**
   * Tests wether an entity has been registered for update.
   * 
   * @param entity
   *            the entity to test.
   * @return true if the entity has been registered for update.
   */
  boolean isEntityRegisteredForUpdate(IEntity entity);

  /**
   * Registers an entity for deletion.
   * 
   * @param entity
   *            the entity to register.
   */
  void registerForDeletion(IEntity entity);

  /**
   * Registers an entity for update.
   * 
   * @param entity
   *            the entity to register.
   */
  void registerForUpdate(IEntity entity);
}
