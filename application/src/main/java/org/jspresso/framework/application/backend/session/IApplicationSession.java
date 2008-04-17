/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.session;

import java.util.List;
import java.util.Locale;

import javax.security.auth.Subject;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityDirtAware;
import org.jspresso.framework.model.entity.IEntityLifecycleHandler;
import org.jspresso.framework.security.UserPrincipal;


/**
 * This interface establishes the contract of an application session. This
 * application session represents the backend application state.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IApplicationSession extends IEntityDirtAware,
    IEntityLifecycleHandler {

  /**
   * Begins the current unit of work.
   * 
   * @see IEntityUnitOfWork#begin()
   */
  void beginUnitOfWork();

  /**
   * Clears the pending operations.
   */
  void clearPendingOperations();

  /**
   * Registers an entity (actually a clone of it) and all its graph as taking
   * part in the unit of work.
   * 
   * @param entity
   *            the entity to make part of the unit of work.
   * @return the entity (clone of the original one) actually registered in the
   *         unit of work.
   */
  IEntity cloneInUnitOfWork(IEntity entity);

  /**
   * Registers an list of entities (actually a clone of it) and all their graphs
   * as taking part in the unit of work.
   * 
   * @param entities
   *            the entities to make part of the unit of work.
   * @return the entity (clone of the original one) actually registered in the
   *         unit of work.
   */
  List<IEntity> cloneInUnitOfWork(List<IEntity> entities);

  /**
   * Is the passed entity already updated in the current unit of work and waits
   * for commit ?
   * 
   * @param entity
   *            the entity to test.
   * @return true if the passed entity already updated in the current unit of
   *         work and waits for commit.
   */
  boolean isUpdatedInUnitOfWork(IEntity entity);

  /**
   * Commits the current unit of work.
   * 
   * @see IEntityUnitOfWork#commit()
   */
  void commitUnitOfWork();

  /**
   * Gets the session locale.
   * 
   * @return the session locale.
   */
  Locale getLocale();

  /**
   * Gets the session principal as a JAAS principal.
   * 
   * @return the session owner.
   */
  UserPrincipal getPrincipal();

  /**
   * Gets a previously registered entity in this application session.
   * 
   * @param entityContract
   *            the entity contract.
   * @param entityId
   *            the identifier of the looked-up entity.
   * @return the registered entity or null.
   */
  IEntity getRegisteredEntity(Class<? extends IEntity> entityContract,
      Object entityId);

  /**
   * Gets the session owner as a JAAS subject.
   * 
   * @return the session owner.
   */
  Subject getSubject();

  /**
   * Whenever a property might not be fully initialized, this method performs
   * all necessary complementary initializations..
   * 
   * @param componentOrEntity
   *            the component or entity holding the property.
   * @param propertyDescriptor
   *            the property descriptor.
   */
  void initializePropertyIfNeeded(IComponent componentOrEntity,
      IPropertyDescriptor propertyDescriptor);

  /**
   * Wether the object is fully initialized.
   * 
   * @param objectOrProxy
   *            the object to test.
   * @return true if the object is fully initialized.
   */
  boolean isInitialized(Object objectOrProxy);

  /**
   * Gets wether a transactional unit of work has been started in the
   * application session.
   * 
   * @return true if a transactional unit of work has been started in the
   *         application session.
   */
  boolean isUnitOfWorkActive();

  /**
   * Merges an entity in this application session. If the application session
   * already contains an entity with this id, the state of the entity passed as
   * parameter is merged into the registered entity depending on the merge mode
   * used. If not, a copy of the entity is registered into the application
   * session. The entity passed as parameter is considered not dirty so the
   * application dirty states are updated accordingly.
   * 
   * @param entity
   *            the entity to merge.
   * @param mergeMode
   *            the merge mmode to be used.
   * @return the entity registered in the application session.
   */
  IEntity merge(IEntity entity, MergeMode mergeMode);

  /**
   * Merges a list of entities in this application session. If the application
   * session already contains an entity with this id, the state of the entity
   * passed as parameter is merged into the registered entity depending on the
   * merge mode used. If not, a copy of the entity is registered into the
   * application session. The entity passed as parameter is considered not dirty
   * so the application dirty states are updated accordingly.
   * 
   * @param entities
   *            the list of entities to merge.
   * @param mergeMode
   *            the merge mmode to be used.
   * @return the merged entity list.
   */
  List<IEntity> merge(List<IEntity> entities, MergeMode mergeMode);

  /**
   * Gives a chance to the session to perform any pending operation.
   */
  void performPendingOperations();

  /**
   * Records an entity as having been flushed to the persistent store.
   * 
   * @param flushedEntity
   *            the flushed entity.
   */
  void recordAsSynchronized(IEntity flushedEntity);

  /**
   * Registers an entity in this application session.
   * 
   * @param entity
   *            the entity to register.
   * @param isEntityTransient
   *            wether this entity has to be considered as a transient one. It
   *            is not safe to rely on entity.isPersistent() to determine it.
   */
  void registerEntity(IEntity entity, boolean isEntityTransient);

  /**
   * Rollbacks the current unit of work.
   * 
   * @see IEntityUnitOfWork#rollback()
   */
  void rollbackUnitOfWork();

  /**
   * Sets the session locale.
   * 
   * @param locale
   *            the session locale.
   */
  void setLocale(Locale locale);

  /**
   * Sets the session owner as a JAAS subject.
   * 
   * @param sessionOwner
   *            the session owner.
   */
  void setSubject(Subject sessionOwner);
}
