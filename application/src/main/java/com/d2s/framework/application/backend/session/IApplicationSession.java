/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.session;

import java.util.List;

import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IEntityDirtAware;

/**
 * This interface establishes the contract of an application session. This
 * application session represents the backend application state.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IApplicationSession extends IEntityDirtAware {

  /**
   * Registers an entity in this application session.
   * 
   * @param entity
   *          the entity to register.
   * @param isEntityTransient
   *          wether this entity has to be considered as a transient one. It is
   *          not safe to rely on entity.isPersistent() to determine it.
   */
  void registerEntity(IEntity entity, boolean isEntityTransient);

  /**
   * Merges an entity in this application session. If the application session
   * already contains an entity with this id, the state of the entity passed as
   * parameter is merged into the registered entity depending on the merge mode
   * used. If not, a copy of the entity is registered into the application
   * session. The entity passed as parameter is considered not dirty so the
   * application dirty states are updated accordingly.
   * 
   * @param entity
   *          the entity to merge.
   * @param mergeMode
   *          the merge mmode to be used.
   * @return the entity registered in the application session.
   */
  IEntity merge(IEntity entity, MergeMode mergeMode);

  /**
   * Begins the current unit of work.
   * 
   * @see IEntityUnitOfWork#begin()
   */
  void beginUnitOfWork();

  /**
   * Commits the current unit of work.
   * 
   * @see IEntityUnitOfWork#commit()
   */
  void commitUnitOfWork();

  /**
   * Rollbacks the current unit of work.
   * 
   * @see IEntityUnitOfWork#rollback()
   */
  void rollbackUnitOfWork();

  /**
   * Records an entity as having been flushed to the persistent store.
   * 
   * @param flushedEntity
   *          the flushed entity.
   */
  void recordAsSynchronized(IEntity flushedEntity);

  /**
   * Registers an entity (actually a clone of it) and all its graph as taking
   * part in the unit of work.
   * 
   * @param entity
   *          the entity to make part of the unit of work.
   * @return the entity (clone of the original one) actually registered in the
   *         unit of work.
   */
  IEntity cloneInUnitOfWork(IEntity entity);

  /**
   * Registers an list of entities (actually a clone of it) and all their graphs
   * as taking part in the unit of work.
   * 
   * @param entities
   *          the entities to make part of the unit of work.
   * @return the entity (clone of the original one) actually registered in the
   *         unit of work.
   */
  List<IEntity> cloneInUnitOfWork(List<IEntity> entities);

  /**
   * Whenever a property might not be fully initialized, this method performs
   * all necessary complementary initializations..
   * 
   * @param entity
   *          the entity holding the property.
   * @param propertyName
   *          the property name.
   */
  void initializePropertyIfNeeded(IEntity entity, String propertyName);

  /**
   * Gets wether a transactional unit of work has been started in the
   * application session.
   * 
   * @return true if a transactional unit of work has been started in the
   *         application session.
   */
  boolean isUnitOfWorkActive();

  /**
   * Gets a previously registered entity in this application session.
   * 
   * @param entityContract
   *          the entity contract.
   * @param entityId
   *          the identifier of the looked-up entity.
   * @return the registered entity or null.
   */
  IEntity getRegisteredEntity(Class entityContract, Object entityId);
}
