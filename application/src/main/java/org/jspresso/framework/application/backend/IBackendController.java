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
package org.jspresso.framework.application.backend;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.jspresso.framework.application.IController;
import org.jspresso.framework.application.backend.async.AsyncActionExecutor;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.datatransfer.ComponentTransferStructure;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.model.entity.IEntityLifecycleHandler;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * This interface establishes the contract of the backend controllers. Backend
 * controllers are controllers which act on the application domain model (as
 * oposed to frontend controllers which act on the application view).
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IBackendController extends IController, IEntityLifecycleHandler {

  /**
   * Begins the current unit of work.
   * 
   * @param transaction
   *          the underlying transaction or null if none.
   * @see org.jspresso.framework.application.backend.session.IEntityUnitOfWork#begin()
   */
  void beginUnitOfWork(Object transaction);

  /**
   * Joins the transaction, beginning unit of work if not already begun.
   * 
   * @param transaction
   *          the underlying transaction or null if none.
   * @see org.jspresso.framework.application.backend.session.IEntityUnitOfWork#begin()
   */
  void joinTransaction(Object transaction);

  /**
   * Registers an entity (actually a clone of it) and all its graph as taking
   * part in the unit of work.
   * 
   * @param <E>
   *          the actual entity type.
   * @param entity
   *          the entity to make part of the unit of work.
   * @return the entity (clone of the original one) actually registered in the
   *         unit of work.
   */
  <E extends IEntity> E cloneInUnitOfWork(E entity);

  /**
   * Registers an entity (actually a clone of it) and all its graph as taking
   * part in the unit of work. The second parameter allows in-session
   * modification of clones without prior registration. This parameter must be
   * set to true in order to disable sanitization checks on modification of the
   * clones not being registered in session after the end of the UOW, so that
   * in-memory only transactions can be implemented.
   * 
   * @param <E>
   *          the actual entity type.
   * @param entity
   *          the entity to make part of the unit of work.
   * @param allowOuterScopeUpdate
   *          when set to true, this third parameter disables sanitization
   *          checks on modification of the clones not being registered in
   *          session after the end of the UOW, so that in-memory only
   *          transactions can be implemented.
   * @return the entity (clone of the original one) actually registered in the
   *         unit of work.
   */
  <E extends IEntity> E cloneInUnitOfWork(E entity, boolean allowOuterScopeUpdate);

  /**
   * Registers an list of entities (actually a clone of it) and all their graphs
   * as taking part in the unit of work.
   * 
   * @param <E>
   *          the actual entity type.
   * @param entities
   *          the entities to make part of the unit of work.
   * @return the entity (clone of the original one) actually registered in the
   *         unit of work.
   */
  <E extends IEntity> List<E> cloneInUnitOfWork(List<E> entities);

  /**
   * Registers an list of entities (actually a clone of it) and all their graphs
   * as taking part in the unit of work. The second parameter allows in-session
   * modification of clones without prior registration. This parameter must be
   * set to true in order to disable sanitization checks on modification of the
   * clones not being registered in session after the end of the UOW, so that
   * in-memory only transactions can be implemented.
   * 
   * @param <E>
   *          the actual entity type.
   * @param entities
   *          the entities to make part of the unit of work.
   * @param allowOuterScopeUpdate
   *          when set to true, this third parameter disables sanitization
   *          checks on modification of the clones not being registered in
   *          session after the end of the UOW, so that in-memory only
   *          transactions can be implemented.
   * @return the entity (clone of the original one) actually registered in the
   *         unit of work.
   */
  <E extends IEntity> List<E> cloneInUnitOfWork(List<E> entities, boolean allowOuterScopeUpdate);

  /**
   * Commits the current unit of work.
   * 
   * @param transaction
   *          the underlying transaction or null if none.
   * @see org.jspresso.framework.application.backend.session.IEntityUnitOfWork#commit()
   */
  void commitUnitOfWork(Object transaction);

  /**
   * Creates a model connector out of a model descriptor. It should be either a
   * bean connector or a bean collection connector depending on the type of
   * model descriptor.
   * 
   * @param id
   *          the connector id.
   * @param modelDescriptor
   *          the model descriptor to create the connector for.
   * @return the created model connector.
   */
  IValueConnector createModelConnector(String id, IModelDescriptor modelDescriptor);

  /**
   * Gets the appropriate accessor factory based on the targetted object.
   * 
   * @return the approriate accessor factory.
   */
  IAccessorFactory getAccessorFactory();

  /**
   * Gets the entityFactory for this backend controller.
   * 
   * @return the entityFactory for this backend controller.
   */
  IEntityFactory getEntityFactory();

  /**
   * Gets a previously registered entity in this application session.
   * 
   * @param entityContract
   *          the entity contract.
   * @param entityId
   *          the identifier of the looked-up entity.
   * @return the registered entity or null.
   */
  IEntity getRegisteredEntity(Class<? extends IEntity> entityContract, Serializable entityId);

  /**
   * Gets the transactionTemplate.
   * 
   * @return the transactionTemplate.
   */
  TransactionTemplate getTransactionTemplate();

  /**
   * Given a workspace identifier, this method returns the composite connector
   * used as model connector for the associated workspace.
   * 
   * @param workspaceName
   *          the workspace identifier.
   * @return the associated workspace connector.
   */
  IValueConnector getWorkspaceConnector(String workspaceName);

  /**
   * Lazily creates a module connector.
   * 
   * @param module
   *          the module to create (or get) the connector for.
   * @return the module connector.
   */
  IValueConnector getModuleConnector(Module module);

  /**
   * Whenever a property might not be fully initialized, this method performs
   * all necessary complementary initializations..
   * 
   * @param componentOrEntity
   *          the component or entity holding the property.
   * @param propertyName
   *          the name of the property to initialize.
   */
  void initializePropertyIfNeeded(IComponent componentOrEntity, String propertyName);

  /**
   * Installs the passed in workspaces into the backend controller.
   * 
   * @param workspaces
   *          the workspaces to install.
   */
  void installWorkspaces(Map<String, Workspace> workspaces);

  /**
   * Gets wether any of the entities or if any of the entities they can reach
   * are dirty (has changes that need to be updated to the persistent store).
   * 
   * @param elements
   *          the elements to test. Only entities are actually tested.
   * @return true if any of the entities is dirty in depth.
   */
  boolean isAnyDirtyInDepth(Collection<?> elements);

  /**
   * Gets wether the entity or if one of the entities it can reach is dirty (has
   * changes that need to be updated to the persistent store).
   * 
   * @param entity
   *          the entity to test.
   * @return true if the entity is dirty in depth.
   */
  boolean isDirtyInDepth(IEntity entity);

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
   * @param propertyName
   *          the name of the property to test.
   * @return true if the entity property is dirty.
   */
  boolean isDirty(IEntity entity, String propertyName);

  /**
   * Wether the object is fully initialized.
   * 
   * @param objectOrProxy
   *          the object to test.
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
   * Is the passed entity already updated in the current unit of work and waits
   * for commit ?
   * 
   * @param entity
   *          the entity to test.
   * @return true if the passed entity already updated in the current unit of
   *         work and waits for commit.
   */
  boolean isUpdatedInUnitOfWork(IEntity entity);

  /**
   * Merges an entity in this application session. If the application session
   * already contains an entity with this id, the state of the entity passed as
   * parameter is merged into the registered entity depending on the merge mode
   * used. If not, a copy of the entity is registered into the application
   * session. The entity passed as parameter is considered not dirty so the
   * application dirty states are updated accordingly.
   * 
   * @param <E>
   *          the actual entity subclass.
   * @param entity
   *          the entity to merge.
   * @param mergeMode
   *          the merge mmode to be used.
   * @return the entity registered in the application session.
   */
  <E extends IEntity> E merge(E entity, EMergeMode mergeMode);

  /**
   * Merges a list of entities in this application session. If the application
   * session already contains an entity with this id, the state of the entity
   * passed as parameter is merged into the registered entity depending on the
   * merge mode used. If not, a copy of the entity is registered into the
   * application session. The entity passed as parameter is considered not dirty
   * so the application dirty states are updated accordingly.
   * 
   * @param <E>
   *          the actual entity subclass.
   * @param entities
   *          the list of entities to merge.
   * @param mergeMode
   *          the merge mmode to be used.
   * @return the merged entity list.
   */
  <E extends IEntity> List<E> merge(List<E> entities, EMergeMode mergeMode);

  /**
   * Gives a chance to the session to perform any pending operation.
   */
  void performPendingOperations();

  /**
   * Records an entity as having been flushed to the persistent store.
   * 
   * @param flushedEntity
   *          the flushed entity.
   */
  void recordAsSynchronized(IEntity flushedEntity);

  /**
   * Registers an entity in this application session.
   * 
   * @param entity
   *          the entity to register.
   * @param isEntityTransient
   *          wether this entity has to be considered as a transient one. It is
   *          not safe to rely on entity.isPersistent() to determine it.
   * @return the registered entity which is either the entity itself or a clone
   *         in unit of work.
   */
  <T extends IEntity> T registerEntity(T entity, boolean isEntityTransient);

  /**
   * Acts as a clipboard for retrieving previously stored component references
   * along with their descriptors.
   * 
   * @return components the component transfer structure to retrieve.
   */
  ComponentTransferStructure<? extends IComponent> retrieveComponents();

  /**
   * Rollbacks the current unit of work.
   * 
   * @param transaction
   *          the underlying transaction or null if none.
   * @see org.jspresso.framework.application.backend.session.IEntityUnitOfWork#rollback()
   */
  void rollbackUnitOfWork(Object transaction);

  /**
   * Asks this backend controller to perform any necessary action upon startup.
   * One of this action should be to construct the root connector based on the
   * root model descriptor.
   * 
   * @param startingLocale
   *          the locale this backend controller should start with.
   * @param clientTimeZone
   *          the client timezone.
   * @return true if the controller successfully started.
   */
  boolean start(Locale startingLocale, TimeZone clientTimeZone);

  /**
   * Acts as a clipboard for storing component references along with their
   * descriptors.
   * 
   * @param components
   *          the component transfer structure to store.
   */
  void storeComponents(ComponentTransferStructure<? extends IComponent> components);

  /**
   * Cleans-up request-scoped resources.
   */
  void cleanupRequestResources();

  /**
   * Wether the backend controller should throw or not an exception whenever a
   * bad usage is detected like manually merging a dirty entity from an ongoing
   * UOW.
   * 
   * @return true if an exception should be thrown.
   */
  boolean isThrowExceptionOnBadUsage();

  /**
   * Gives chance to implementors to perform sanity checks and eventually
   * substitute the passed param by an other one when it's technically
   * necessary. This is here tat all sanity checks are made regarding UOW
   * isolation.
   * 
   * @param target
   *          the target being modified.
   * @param propertyDescriptor
   *          the descriptor of the property being modified.
   * @param param
   *          the modifier parameter.
   * @return the parameter to actually pass to the modifier
   */
  Object sanitizeModifierParam(Object target, IPropertyDescriptor propertyDescriptor, Object param);

  /**
   * Returns the list of currently active asynchronous action executors.
   * 
   * @return the list of currently active asynchronous action executors.
   */
  Set<AsyncActionExecutor> getRunningExecutors();
}
