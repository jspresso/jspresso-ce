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

import java.io.Serializable;

import org.jspresso.framework.model.component.IComponentFactory;
import org.jspresso.framework.model.descriptor.IComponentDescriptorRegistry;


/**
 * This interface defines the contract of an entities factory.
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
public interface IEntityFactory extends IComponentFactory,
    IComponentDescriptorRegistry {

  /**
   * Creates a new entity instance based on the entity descriptor. The entity
   * will be initialized with any necessary starting state.
   * 
   * @param <T>
   *            the concrete class of the created entity.
   * @param entityContract
   *            the class of the entity to create.
   * @return the entity instance.
   */
  <T extends IEntity> T createEntityInstance(Class<T> entityContract);

  /**
   * Creates a new entity instance based on the entity descriptor.
   * 
   * @param <T>
   *            the concrete class of the created entity.
   * @param entityContract
   *            the class of the entity to create.
   * @param id
   *            the identifier to set on the entity.
   * @return the entity instance.
   */
  <T extends IEntity> T createEntityInstance(Class<T> entityContract,
      Serializable id);

}
