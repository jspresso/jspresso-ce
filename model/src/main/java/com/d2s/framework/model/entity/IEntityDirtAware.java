/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity;

import java.util.Map;

/**
 * This interface is implemented by classes awre of entities states dirtyness.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
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
