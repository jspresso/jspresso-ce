/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.application.IController;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.datatransfer.ComponentTransferStructure;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityDirtAware;
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
public interface IBackendController extends IController, IEntityDirtAware,
    IEntityLifecycleHandler {

  /**
   * Checks authorization for workspace access. It shoud throw a
   * SecurityException whenever access should not be granted.
   * 
   * @param workspaceName
   *          the id of the workspace access to check.
   */
  void checkWorkspaceAccess(String workspaceName);

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
  IValueConnector createModelConnector(String id,
      IModelDescriptor modelDescriptor);

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
   * Given a workspace identifier, this method returns the composite connector
   * used as model connector for the associated workspace.
   * 
   * @param workspaceName
   *          the workspace identifier.
   * @return the associated workspace connector.
   */
  IValueConnector getWorkspaceConnector(String workspaceName);

  /**
   * Installs the passed in workspaces into the backend controller.
   * 
   * @param workspaces
   *          the workspaces to install.
   */
  void installWorkspaces(Map<String, Workspace> workspaces);

  /**
   * Acts as a clipboard for retrieving previously stored component references
   * along with their descriptors.
   * 
   * @return components the component transfer structure to retrieve.
   */
  ComponentTransferStructure<? extends IComponent> retrieveComponents();

  /**
   * Asks this backend controller to perform any necessary action upon startup.
   * One of this action should be to construct the root connector based on the
   * root model descriptor.
   * 
   * @param startingLocale
   *          the locale this backend controller should start with.
   * @return true if the controller successfully started.
   */
  boolean start(Locale startingLocale);

  /**
   * Acts as a clipboard for storing component references along with their
   * descriptors.
   * 
   * @param components
   *          the component transfer structure to store.
   */
  void storeComponents(
      ComponentTransferStructure<? extends IComponent> components);

  /**
   * Begins the current unit of work.
   * 
   * @see org.jspresso.framework.application.backend.session.IEntityUnitOfWork#begin()
   */
  void beginUnitOfWork();

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
   * Commits the current unit of work.
   * 
   * @see org.jspresso.framework.application.backend.session.IEntityUnitOfWork#commit()
   */
  void commitUnitOfWork();

  /**
   * Whenever a property might not be fully initialized, this method performs
   * all necessary complementary initializations..
   * 
   * @param componentOrEntity
   *          the component or entity holding the property.
   * @param propertyName
   *          the name of the property to initialize.
   */
  void initializePropertyIfNeeded(IComponent componentOrEntity,
      String propertyName);

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
   * @param entity
   *          the entity to merge.
   * @param mergeMode
   *          the merge mmode to be used.
   * @return the entity registered in the application session.
   */
  IEntity merge(IEntity entity, EMergeMode mergeMode);

  /**
   * Merges a list of entities in this application session. If the application
   * session already contains an entity with this id, the state of the entity
   * passed as parameter is merged into the registered entity depending on the
   * merge mode used. If not, a copy of the entity is registered into the
   * application session. The entity passed as parameter is considered not dirty
   * so the application dirty states are updated accordingly.
   * 
   * @param entities
   *          the list of entities to merge.
   * @param mergeMode
   *          the merge mmode to be used.
   * @return the merged entity list.
   */
  List<IEntity> merge(List<IEntity> entities, EMergeMode mergeMode);

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
   * Rollbacks the current unit of work.
   * 
   * @see org.jspresso.framework.application.backend.session.IEntityUnitOfWork#rollback()
   */
  void rollbackUnitOfWork();

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
   * Gets a previously registered entity in this application session.
   * 
   * @param entityContract
   *          the entity contract.
   * @param entityId
   *          the identifier of the looked-up entity.
   * @return the registered entity or null.
   */
  IEntity getRegisteredEntity(Class<? extends IEntity> entityContract,
      Object entityId);

  /**
   * Gets the transactionTemplate.
   * 
   * @return the transactionTemplate.
   */
  TransactionTemplate getTransactionTemplate();

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
   * Gets wether any of the entities or if any of the entities they can reach
   * are dirty (has changes that need to be updated to the persistent store).
   * 
   * @param elements
   *          the elements to test. Only entities are actually tested.
   * @return true if any of the entities is dirty in depth.
   */
  boolean isAnyDirtyInDepth(Collection<?> elements);
}
