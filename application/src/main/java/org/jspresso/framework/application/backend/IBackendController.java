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
package org.jspresso.framework.application.backend;

import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionTemplate;

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

/**
 * This interface establishes the contract of the backend controllers. Backend
 * controllers are controllers which act on the application domain model (as
 * opposed to frontend controllers which act on the application view).
 *
 * @author Vincent Vandenschrick
 */
public interface IBackendController
    extends IController, IEntityLifecycleHandler, TransactionSynchronization, IBackendControllerFactory {

  /**
   * Begins the current unit of work.
   *
   * @see org.jspresso.framework.application.backend.session.IEntityUnitOfWork#begin()
   */
  void beginUnitOfWork();

  /**
   * Begins A nested unit of work.
   *
   * @see org.jspresso.framework.application.backend.session.IEntityUnitOfWork#beginNested()
   */
  void beginNestedUnitOfWork();

  /**
   * Joins the transaction, beginning unit of work if not already begun.
   *
   * @see org.jspresso.framework.application.backend.session.IEntityUnitOfWork#begin()
   */
  void joinTransaction();

  /**
   * Joins the transaction, beginning unit of work if not already begun.
   *
   * @param nested
   *     indicates whether this TX should be a nested new transaction.
   * @see org.jspresso.framework.application.backend.session.IEntityUnitOfWork#begin()
   */
  void joinTransaction(boolean nested);

  /**
   * Registers an entity (actually a clone of it) and all its graph as taking
   * part in the unit of work.
   *
   * @param <E>
   *     the actual entity type.
   * @param entity
   *     the entity to make part of the unit of work.
   * @return the entity (clone of the original one) actually registered in the
   * unit of work.
   */
  <E extends IEntity> E cloneInUnitOfWork(E entity);

  /**
   * Registers an list of entities (actually a clone of it) and all their graphs
   * as taking part in the unit of work.
   *
   * @param <E>
   *     the actual entity type.
   * @param entities
   *     the entities to make part of the unit of work.
   * @return the entity (clone of the original one) actually registered in the
   * unit of work.
   */
  <E extends IEntity> List<E> cloneInUnitOfWork(List<E> entities);

  /**
   * Commits the current unit of work.
   *
   * @see org.jspresso.framework.application.backend.session.IEntityUnitOfWork#commit()
   */
  void commitUnitOfWork();

  /**
   * Creates a model connector out of a model descriptor. It should be either a
   * bean connector or a bean collection connector depending on the type of
   * model descriptor.
   *
   * @param id
   *     the connector id.
   * @param modelDescriptor
   *     the model descriptor to create the connector for.
   * @return the created model connector.
   */
  IValueConnector createModelConnector(String id, IModelDescriptor modelDescriptor);

  /**
   * Gets the appropriate accessor factory based on the targeted object.
   *
   * @return the appropriate accessor factory.
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
   *     the entity contract.
   * @param entityId
   *     the identifier of the looked-up entity.
   * @return the registered entity or null.
   */
  IEntity getRegisteredEntity(Class<? extends IEntity> entityContract, Serializable entityId);

  /**
   * Gets all previously registered entities in the unit of work.
   *
   * @return the registered entities.
   */
  Map<Class<? extends IEntity>, Map<Serializable, IEntity>> getUnitOfWorkEntities();

  /**
   * Gets a previously registered entity in the unit of work.
   *
   * @param entityContract
   *     the entity contract.
   * @param entityId
   *     the identifier of the looked-up entity.
   * @return the registered entity or null.
   */
  IEntity getUnitOfWorkEntity(Class<? extends IEntity> entityContract, Serializable entityId);

  /**
   * Gets unit of work or registered entity.
   *
   * @param entityType the entity type
   * @param id the id
   * @return the unit of work or registered entity
   */
  IEntity getUnitOfWorkOrRegisteredEntity(Class<? extends IEntity> entityType, Serializable id);

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
   *     the workspace identifier.
   * @return the associated workspace connector.
   */
  IValueConnector getWorkspaceConnector(String workspaceName);

  /**
   * Lazily creates a module connector.
   *
   * @param module
   *     the module to create (or get) the connector for.
   * @return the module connector.
   */
  IValueConnector getModuleConnector(Module module);

  /**
   * Whenever a property might not be fully initialized, this method performs
   * all necessary complementary initializations..
   *
   * @param componentOrEntity
   *     the component or entity holding the property.
   * @param propertyName
   *     the name of the property to initialize.
   */
  void initializePropertyIfNeeded(IComponent componentOrEntity, String propertyName);

  /**
   * Installs the passed in workspaces into the backend controller.
   *
   * @param workspaces
   *     the workspaces to install.
   */
  void installWorkspaces(Map<String, Workspace> workspaces);

  /**
   * Gets whether any of the entities or if any of the entities they can reach
   * are dirty (has changes that need to be updated to the persistent store).
   * Computed properties are also scanned for modification.
   *
   * @param elements
   *     the elements to test. Only entities are actually tested.
   * @return true if any of the entities is dirty in depth.
   */
  boolean isAnyDirtyInDepth(Collection<?> elements);

  /**
   * Gets whether the entity or if one of the entities it can reach is dirty (has
   * changes that need to be updated to the persistent store). Computed
   * properties are also scanned for modification.
   *
   * @param entity
   *     the entity to test.
   * @return true if the entity is dirty in depth.
   */
  boolean isDirtyInDepth(IEntity entity);

  /**
   * Gets whether the entity is dirty (has changes that need to be updated to the
   * persistent store). Computed properties are also scanned for modification.
   *
   * @param entity
   *     the entity to test.
   * @return true if the entity is dirty.
   */
  boolean isDirty(IEntity entity);

  /**
   * Gets whether any of the entities or if any of the entities they can reach
   * are dirty (has changes that need to be updated to the persistent store).
   *
   * @param elements
   *     the elements to test. Only entities are actually tested.
   * @param includeComputed
   *     are computed properties also scanned ?
   * @return true if any of the entities is dirty in depth.
   */
  boolean isAnyDirtyInDepth(Collection<?> elements, boolean includeComputed);

  /**
   * Gets whether the entity or if one of the entities it can reach is dirty (has
   * changes that need to be updated to the persistent store).
   *
   * @param entity
   *     the entity to test.
   * @param includeComputed
   *     are computed properties also scanned ?
   * @return true if the entity is dirty in depth.
   */
  boolean isDirtyInDepth(IEntity entity, boolean includeComputed);

  /**
   * Gets whether the entity is dirty (has changes that need to be updated to the
   * persistent store).
   *
   * @param entity
   *     the entity to test.
   * @param includeComputed
   *     are computed properties also scanned ?
   * @return true if the entity is dirty.
   */
  boolean isDirty(IEntity entity, boolean includeComputed);

  /**
   * Gets whether the entity property is dirty (has changes that need to be
   * updated to the persistent store).
   *
   * @param entity
   *     the entity to test.
   * @param propertyName
   *     the name of the property to test.
   * @return true if the entity property is dirty.
   */
  boolean isDirty(IEntity entity, String propertyName);

  /**
   * Whether the object is fully initialized.
   *
   * @param objectOrProxy
   *     the object to test.
   * @return true if the object is fully initialized.
   */
  boolean isInitialized(Object objectOrProxy);

  /**
   * Gets whether a transactional unit of work has been started in the
   * application session.
   *
   * @return true if a transactional unit of work has been started in the
   * application session.
   */
  boolean isUnitOfWorkActive();

  /**
   * Tests whether the passed entity already updated in the current unit of work and waits
   * for commit.
   *
   * @param entity
   *     the entity to test.
   * @return true if the passed entity already updated in the current unit of
   * work and waits for commit.
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
   *     the actual entity subclass.
   * @param entity
   *     the entity to merge.
   * @param mergeMode
   *     the merge mode to be used.
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
   *     the actual entity subclass.
   * @param entities
   *     the list of entities to merge.
   * @param mergeMode
   *     the merge mode to be used.
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
   *     the flushed entity.
   */
  void recordAsSynchronized(IEntity flushedEntity);

  /**
   * Registers an entity in this application session.
   *
   * @param entity
   *     the entity to register.
   */
  void registerEntity(IEntity entity);

  /**
   * Acts as a clipboard for retrieving previously stored component references
   * along with their descriptors.
   *
   * @return components the component transfer structure to retrieve.
   */
  ComponentTransferStructure<IComponent> retrieveComponents();

  /**
   * Rollbacks the current unit of work.
   *
   * @see org.jspresso.framework.application.backend.session.IEntityUnitOfWork#rollback()
   */
  void rollbackUnitOfWork();

  /**
   * Asks this backend controller to perform any necessary action upon startup.
   * One of this action should be to construct the root connector based on the
   * root model descriptor.
   *
   * @param startingLocale
   *     the locale this backend controller should start with.
   * @param clientTimeZone
   *     the client timezone.
   * @return true if the controller successfully started.
   */
  boolean start(Locale startingLocale, TimeZone clientTimeZone);

  /**
   * Acts as a clipboard for storing component references along with their
   * descriptors.
   *
   * @param components
   *     the component transfer structure to store.
   */
  void storeComponents(ComponentTransferStructure<IComponent> components);

  /**
   * Cleans-up request-scoped resources.
   */
  void cleanupRequestResources();

  /**
   * Gives chance to implementors to perform sanity checks and eventually
   * substitute the passed param by an other one when it's technically
   * necessary. This is here tat all sanity checks are made regarding UOW
   * isolation.
   *
   * @param target
   *     the target being modified.
   * @param propertyDescriptor
   *     the descriptor of the property being modified.
   * @param param
   *     the modifier parameter.
   * @return the parameter to actually pass to the modifier
   */
  Object sanitizeModifierParam(Object target, IPropertyDescriptor propertyDescriptor, Object param);

  /**
   * Returns the list of currently active asynchronous action executors.
   *
   * @return the list of currently active asynchronous action executors.
   */
  Set<AsyncActionExecutor> getRunningExecutors();

  /**
   * Returns the list of completed asynchronous action executors.
   *
   * @return the list of completed asynchronous action executors.
   */
  Set<AsyncActionExecutor> getCompletedExecutors();

  /**
   * Purges completed asynchronous action executors.
   */
  void purgeCompletedExecutors();

  /**
   * Gets the entity dirty properties (changed properties that need to be
   * updated to the persistent store as well as computed properties if
   * includeComputed is set to {@code true}).
   *
   * @param entity
   *     the entity to get the dirty properties of.
   * @param includeComputed
   *     are computed properties also returned ?
   * @return null or an empty map if the entity is not dirty. The collection of
   * dirty properties with their original values.
   */
  Map<String, Object> getDirtyProperties(IEntity entity, boolean includeComputed);

  /**
   * Adds a new dirt interceptor that will be notified every time an entity is made dirty.
   *
   * @param interceptor
   *     the interceptor.
   */
  void addDirtInterceptor(PropertyChangeListener interceptor);

  /**
   * Removes a dirt interceptor that was previously added.
   *
   * @param interceptor
   *     the interceptor.
   */
  void removeDirtInterceptor(PropertyChangeListener interceptor);

  /**
   * Sets client time zone.
   *
   * @param clientTimeZone
   *     the client time zone
   */
  void setClientTimeZone(TimeZone clientTimeZone);
}
