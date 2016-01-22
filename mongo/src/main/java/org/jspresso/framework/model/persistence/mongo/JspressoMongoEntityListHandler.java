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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;

import org.jspresso.framework.model.entity.IEntity;

/**
 * List proxy invocation handler.
 *
 * @author Vincent Vandenschrick
 */
public class JspressoMongoEntityListHandler extends JspressoMongoEntityCollectionHandler {

  /**
   * Instantiates a new Jspresso mongo entity list invocation handler.
   *
   * @param ids
   *     the ids
   * @param entityContract
   *     the entity contract
   * @param mongo
   *     the mongo
   */
  public JspressoMongoEntityListHandler(Collection<Serializable> ids, Class<IEntity> entityContract,
                                        MongoTemplate mongo) {
    super(ids, entityContract, mongo);
  }

  @Override
  protected Collection<IEntity> createTargetCollection(Collection<IEntity> sourceCollection) {
    return new ArrayList<>(sourceCollection);
  }

  /**
   * Gets collection interface.
   *
   * @return the collection interface
   */
  @Override
  protected Class<? extends Collection> getCollectionInterface() {
    return List.class;
  }
}
