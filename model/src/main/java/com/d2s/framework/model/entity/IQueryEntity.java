/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.entity;

import java.util.List;

/**
 * A simple adapter to wrap an entity used as selection criteria and a list of
 * entities. It only serve as a placeholder for the result of the query.
 * instances of this calss do not perform queries by themselves.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IQueryEntity extends IEntity {

  /**
   * "queriedEntities" string constant.
   */
  String QUERIED_ENTITIES = "queriedEntities";

  /**
   * Gets the list of entities result of the query.
   * 
   * @return the list of entities result of the query.
   */
  List<IEntity> getQueriedEntities();

  /**
   * Sets the list of entities result of the query.
   * 
   * @param queriedEntities
   *            the list of entities result of the query.
   */
  void setQueriedEntities(List<IEntity> queriedEntities);
}
