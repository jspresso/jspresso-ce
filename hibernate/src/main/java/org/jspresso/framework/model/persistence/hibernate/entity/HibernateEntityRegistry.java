/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.model.persistence.hibernate.entity;

import org.jspresso.framework.application.backend.persistence.hibernate.HibernateHelper;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.basic.BasicEntityRegistry;


/**
 * An entity registru that knows how to deal with Hibernate proxies.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class HibernateEntityRegistry extends BasicEntityRegistry {

  /**
   * Constructs a new <code>HibernateEntityRegistry</code> instance.
   * 
   * @param name the name of the registry.
   */
  public HibernateEntityRegistry(String name) {
    super(name);
  }
  
  /**
   * Knows how to deal with hibernate proxies.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected boolean checkUnicity(IEntity entity,
      IEntity existingRegisteredEntity) {
    return HibernateHelper.objectEquals(entity, existingRegisteredEntity);
  }

}
