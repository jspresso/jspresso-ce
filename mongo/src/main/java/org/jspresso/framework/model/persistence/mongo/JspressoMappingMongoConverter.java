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
package org.jspresso.framework.model.persistence.mongo;

import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;

/**
 * Custom mapping converter.
 *
 * @author Vincent Vandenschrick
 */
public class JspressoMappingMongoConverter extends MappingMongoConverter {

  /**
   * Instantiates a new Jspresso mapping mongo converter.
   *
   * @param dbRefResolver the db ref resolver
   * @param mappingContext the mapping context
   */
  public JspressoMappingMongoConverter(DbRefResolver dbRefResolver,
                                       MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty>
                                           mappingContext) {
    super(dbRefResolver, mappingContext);
  }
}
