/*
 * Copyright (c) 2005-2014 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.model.persistence.mongo;

import org.springframework.data.mongodb.core.mapping.BasicMongoPersistentEntity;
import org.springframework.data.util.TypeInformation;

/**
 * Jspresso specialized Mongo persistent entity.
 *
 * @param <T>  the type parameter
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision$
 */
public class JspressoMongoPersistentEntity<T> extends BasicMongoPersistentEntity<T> {


  /**
   * Creates a new {@link org.springframework.data.mongodb.core.mapping.BasicMongoPersistentEntity} with the given
   * {@link org.springframework.data.util.TypeInformation}. Will default the
   * collection name to the entities simple type name.
   *
   * @param typeInformation the type information
   */
  public JspressoMongoPersistentEntity(TypeInformation<T> typeInformation) {
    super(typeInformation);
  }
}
