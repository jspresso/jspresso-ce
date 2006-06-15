/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity;

/**
 * A factory to clone entities.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IEntityCloneFactory {

  /**
   * Clones an entity.
   * 
   * @param <E>
   *          the entity contract (class or interface) to clone.
   * @param entityToClone
   *          the entity to clone.
   * @param entityFactory
   *          the entity factory to use to create the new entities.
   * @return the cloned entity.
   */
  <E extends IEntity> E cloneEntity(E entityToClone,
      IEntityFactory entityFactory);
}
