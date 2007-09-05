/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.session;

import java.util.Map;

import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IEntityDirtAware;

/**
 * This interface is implemented by unit of works on entities. A uneit of work
 * is a structure which will generally synchronize with a transaction to keep
 * track of entity creations, updates, deletions and which is able to either
 * commit the changes or rollback them upon work completion. A unit of work is
 * reusable.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IEntityUnitOfWork extends IEntityDirtAware {

  /**
   * Begins a new unit of work.
   */
  void begin();

  /**
   * Clears the dirty state of the entity in this unit of work.
   * 
   * @param flushedEntity
   *          the entity that was flushed and cleaned.
   */
  void clearDirtyState(IEntity flushedEntity);

  /**
   * Commits the unit of work. It should clear it state and be ready for another
   * work.
   */
  void commit();

  /**
   * Tests wether this unit of work is currently in use.
   * 
   * @return true if the unit of work is active.
   */
  boolean isActive();

  /**
   * Registers an entity in the unit of work.
   * 
   * @param entity
   *          the entity to register.
   * @param initialChangedProperties
   *          the map of dirty properties the entity has before entering the
   *          unit of work along with their original values.
   */
  void register(IEntity entity, Map<String, Object> initialChangedProperties);

  /**
   * Rollbacks the unit of work. It should clear it state, restore the entity
   * states to the one before the unit of work begining and be ready for another
   * work.
   */
  void rollback();
}
