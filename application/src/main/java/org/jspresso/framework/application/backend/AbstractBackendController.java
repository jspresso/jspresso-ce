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
import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.security.auth.Subject;

import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import org.jspresso.framework.action.ActionBusinessException;
import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.application.AbstractController;
import org.jspresso.framework.application.backend.action.Asynchronous;
import org.jspresso.framework.application.backend.action.Transactional;
import org.jspresso.framework.application.backend.async.AsyncActionExecutor;
import org.jspresso.framework.application.backend.entity.ControllerAwareProxyEntityFactory;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.application.backend.session.IEntityUnitOfWork;
import org.jspresso.framework.application.backend.session.basic.BasicEntityUnitOfWork;
import org.jspresso.framework.application.i18n.ITranslationPlugin;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.application.model.descriptor.ModuleDescriptor;
import org.jspresso.framework.application.model.descriptor.WorkspaceDescriptor;
import org.jspresso.framework.application.security.ISecurityPlugin;
import org.jspresso.framework.application.security.SecurityContextBuilder;
import org.jspresso.framework.application.security.SecurityContextConstants;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.model.IModelConnectorFactory;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IComponentCollectionFactory;
import org.jspresso.framework.model.component.ILifecycleCapable;
import org.jspresso.framework.model.datatransfer.ComponentTransferStructure;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.IRelationshipEndPropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityCloneFactory;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.model.entity.IEntityRegistry;
import org.jspresso.framework.model.entity.basic.BasicEntityRegistry;
import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.security.ISecurityContextBuilder;
import org.jspresso.framework.security.SecurityHelper;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.util.preferences.IPreferencesStore;

/**
 * Base class for backend application controllers. Backend controllers are
 * responsible for :
 * <ul>
 * <li>keeping a reference to the application session</li>
 * <li>keeping a reference to the application workspaces and their state</li>
 * <li>keeping a reference to the application clipboard</li>
 * <li>keeping a reference to the entity registry that guarantees the in-memory
 * entity reference unicity in the user session</li>
 * <li>keeping a reference to the entity dirt recorder that keeps track of
 * entity changes to afterwards optimize the ORM operations</li>
 * <li>keeping a reference to the Spring transaction template and its peer
 * &quot;Unit of Work&quot; -aka UOW- that is responsible to manage application
 * transactions and adapt the underlying transaction system (Hibernate, JTA,
 * ...)</li>
 * </ul>
 * Moreover, the backend controller will provide several model related factories
 * that can be configured to customize default, built-in behaviour. Most of
 * these configured properties will be accessible using the corresponding
 * getters. Those getters should be used by the service layer.
 *
 * @author Vincent Vandenschrick
 */
public abstract class AbstractBackendController extends AbstractController implements IBackendController {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractBackendController.class);
  private final IEntityUnitOfWork                      unitOfWork;
  private final IEntityUnitOfWork                      sessionUnitOfWork;
  private final LRUMap<Module, IValueConnector>        moduleConnectors;
  private final ISecurityContextBuilder                securityContextBuilder;
  private final Set<AsyncActionExecutor>               asyncExecutors;
  private       ThreadGroup                            asyncActionsThreadGroup;
  private       ThreadGroup                            controllerAsyncActionsThreadGroup;
  private       IApplicationSession                    applicationSession;
  private       IEntityCloneFactory                    carbonEntityCloneFactory;
  private       IComponentCollectionFactory            collectionFactory;
  private       IEntityFactory                         entityFactory;
  private       IModelConnectorFactory                 modelConnectorFactory;
  private       TransactionTemplate                    transactionTemplate;
  private       ComponentTransferStructure<IComponent> transferStructure;
  private       Map<String, IValueConnector>           workspaceConnectors;
  private       IPreferencesStore                      userPreferencesStore;
  private       ITranslationProvider                   translationProvider;
  private       ISecurityPlugin                        customSecurityPlugin;
  private       ITranslationPlugin                     customTranslationPlugin;
  private       TimeZone                               referenceTimeZone;
  private       TimeZone                               clientTimeZone;
  private       boolean                                throwExceptionOnBadUsage;
  private       IBackendControllerFactory              slaveControllerFactory;
  private       int                                    asyncExecutorsMaxCount;
  private       IBackendController                     masterController;

  /**
   * Constructs a new {@code AbstractBackendController} instance.
   */
  @SuppressWarnings("unchecked")
  protected AbstractBackendController() {
    unitOfWork = createUnitOfWork();
    sessionUnitOfWork = createUnitOfWork();
    sessionUnitOfWork.begin();
    moduleConnectors = new LRUMap<>(20);
    securityContextBuilder = new SecurityContextBuilder();
    throwExceptionOnBadUsage = true;
    asyncExecutors = new LinkedHashSet<>();
    setAsyncExecutorsMaxCount(10);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void joinTransaction() {
    joinTransaction(false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void joinTransaction(boolean nested) {
    TransactionSynchronizationManager.registerSynchronization(this);
    if (!isUnitOfWorkActive()) {
      beginUnitOfWork();
    } else if (nested) {
      beginNestedUnitOfWork();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void performPendingOperations() {
    Collection<IEntity> entitiesToUpdate = sessionUnitOfWork.getEntitiesRegisteredForUpdate();
    Collection<IEntity> entitiesToDelete = sessionUnitOfWork.getEntitiesRegisteredForDeletion();
    if (entitiesToUpdate != null) {
      List<IEntity> uowEntitiesToUpdate = cloneInUnitOfWork(new ArrayList<>(entitiesToUpdate));
      for (IEntity uowEntity : uowEntitiesToUpdate) {
        // Must force insertion
        registerForUpdate(uowEntity);
      }
    }
    if (entitiesToDelete != null) {
      List<IEntity> uowEntitiesToDelete = cloneInUnitOfWork(new ArrayList<>(entitiesToDelete));
      for (IEntity uowEntity : uowEntitiesToDelete) {
        // Must force deletion
        registerForDeletion(uowEntity);
      }
    }
    clearPendingOperations();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void beginUnitOfWork() {
    if (isUnitOfWorkActive()) {
      throw new BackendException("Cannot begin a new unit of work. Another one is already active.");
    }
    doBeginUnitOfWork();
  }

  /**
   * Performs actual UOW begin.
   */
  protected void doBeginUnitOfWork() {
    unitOfWork.begin();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void beginNestedUnitOfWork() {
    doBeginNestedUnitOfWork();
  }

  /**
   * Performs actual UOW begin.
   */
  protected void doBeginNestedUnitOfWork() {
    unitOfWork.beginNested();
  }

  /**
   * Clears the pending operations.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public void clearPendingOperations() {
    sessionUnitOfWork.clearPendingOperations();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final <E extends IEntity> E cloneInUnitOfWork(E entity) {
    return cloneInUnitOfWork(Collections.singletonList(entity)).get(0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <E extends IEntity> List<E> cloneInUnitOfWork(List<E> entities) {
    if (!isUnitOfWorkActive()) {
      throw new BackendException("Cannot use a unit of work that has not begun.");
    }
    List<E> uowEntities = new ArrayList<>();
    Map<Class<? extends IEntity>, Map<Serializable, IEntity>> uowExistingEntities = unitOfWork.getRegisteredEntities();
    IEntityRegistry alreadyCloned = createEntityRegistry("cloneInUnitOfWork", uowExistingEntities);
    Set<IEntity> eventsToRelease = new LinkedHashSet<>();
    boolean wasDirtyTrackingEnabled = isDirtyTrackingEnabled();
    try {
      setDirtyTrackingEnabled(false);
      for (E entity : entities) {
        uowEntities.add(cloneInUnitOfWork(entity, alreadyCloned, eventsToRelease));
      }
    } finally {
      for (IEntity entity : eventsToRelease) {
        try {
          entity.releaseEvents();
        } catch (Throwable t) {
          LOG.error("An unexpected exception occurred when releasing events after a merge", t);
        }
      }
      setDirtyTrackingEnabled(wasDirtyTrackingEnabled);
    }
    return uowEntities;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void commitUnitOfWork() {
    if (!isUnitOfWorkActive()) {
      throw new BackendException("Cannot commit a unit of work that has not begun.");
    }
    if (unitOfWork.hasNested()) {
      unitOfWork.commit();
    } else {
      doCommitUnitOfWork();
    }
  }

  /**
   * Performs actual UOW commit.
   */
  protected void doCommitUnitOfWork() {
    try {
      Collection<IEntity> uowEntitiesRegisteredForDeletion = unitOfWork.getEntitiesRegisteredForDeletion();
      if (uowEntitiesRegisteredForDeletion != null) {
        // manually trigger the fact that deleted entities are synchronized since flush interceptor does not do it.
        for (IEntity uowEntityRegisteredForDeletion : uowEntitiesRegisteredForDeletion) {
          recordAsSynchronized(uowEntityRegisteredForDeletion);
        }
      }
      Collection<IEntity> flushedEntities = new LinkedHashSet<>();
      Collection<IEntity> updatedEntities = unitOfWork.getUpdatedEntities();
      if (updatedEntities != null) {
        flushedEntities.addAll(updatedEntities);
      }
      Collection<IEntity> deletedEntities = unitOfWork.getDeletedEntities();
      if (deletedEntities != null) {
        flushedEntities.addAll(deletedEntities);
      }
      mergeBackFlushedEntities(flushedEntities);
    } finally {
      unitOfWork.clearPendingOperations();
      unitOfWork.commit();
    }
  }

  /**
   * Merge back flushed entities.
   *
   * @param updatedEntities the updated entities
   */
  protected void mergeBackFlushedEntities(Collection<IEntity> updatedEntities) {
    if (updatedEntities != null) {
      List<IEntity> mergedEntities = merge(new ArrayList<>(updatedEntities), EMergeMode.MERGE_CLEAN_LAZY);
      if (recordedMergedEntities != null) {
        recordedMergedEntities.addAll(mergedEntities);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IValueConnector createModelConnector(String id, IModelDescriptor modelDescriptor) {
    return modelConnectorFactory.createModelConnector(id, modelDescriptor, this);
  }

  /**
   * Directly delegates execution to the action after having completed its
   * execution context with the controller's initial context.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IAction action, Map<String, Object> context) {
    if (action == null) {
      return true;
    }
    if (!action.isBackend()) {
      throw new ActionException(
          "The backend controller is executing a frontend action. Please check the action chaining : " + action
              .toString());
    }
    // Should be handled before getting there.
    // checkAccess(action);
    final Map<String, Object> actionContext = getInitialActionContext();
    if (context != null) {
      context.putAll(actionContext);
    }
    if (action.getClass().isAnnotationPresent(Asynchronous.class)) {
      int currentExecutorsCount = getRunningExecutors().size();
      int maxExecutorsCount = getAsyncExecutorsMaxCount(context);
      if (maxExecutorsCount >= 0 && currentExecutorsCount >= maxExecutorsCount) {
        throw new ActionBusinessException(
            "The number of concurrent asynchronous actions has exceeded the allowed max value : "
                + currentExecutorsCount, "async.count.exceeded", currentExecutorsCount);
      }
      executeAsynchronously(action, context);
      return true;
    }
    if (action.getClass().isAnnotationPresent(Transactional.class)) {
      return executeTransactionally(action, context);
    }
    return action.execute(this, context);
  }

  /**
   * Executes an action asynchronously, i.e. when
   * the action is annotated with {@link org.jspresso.framework.application.backend.action.Asynchronous}.
   *
   * @param action
   *     the action to execute.
   * @param context
   *     the context
   * @return the slave thread executing the action.
   */
  public AsyncActionExecutor executeAsynchronously(IAction action, Map<String, Object> context) {
    AbstractBackendController slaveBackendController = createBackendController();
    AsyncActionExecutor slaveExecutor = new AsyncActionExecutor(action, context, getControllerAsyncActionsThreadGroup(),
        slaveBackendController);
    asyncExecutors.add(slaveExecutor);
    Set<AsyncActionExecutor> oldRunningExecutors = new LinkedHashSet<>(getRunningExecutors());
    firePropertyChange("runningExecutors", oldRunningExecutors, getRunningExecutors());
    slaveExecutor.start();
    if (LOG.isDebugEnabled()) {
      LOG.debug("List of running executors :");
      for (AsyncActionExecutor executor : getRunningExecutors()) {
        LOG.debug("  --> Executor {} has completed {}", executor.getName(), NumberFormat.getPercentInstance().format(
            executor.getProgress()));
      }
    }
    return slaveExecutor;
  }

  private synchronized ThreadGroup getControllerAsyncActionsThreadGroup() {
    if (controllerAsyncActionsThreadGroup == null || controllerAsyncActionsThreadGroup.isDestroyed()) {
      controllerAsyncActionsThreadGroup = new ThreadGroup(asyncActionsThreadGroup, toString());
    }
    return controllerAsyncActionsThreadGroup;
  }

  private synchronized void cleanupControllerAsyncActionsThreadGroup() {
    if (controllerAsyncActionsThreadGroup != null && controllerAsyncActionsThreadGroup.activeCount() == 0
        && !controllerAsyncActionsThreadGroup.isDestroyed()) {
      controllerAsyncActionsThreadGroup.destroy();
    }
  }

  /**
   * Creates a slave backend controller, starts it and assign it the same
   * application session.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public AbstractBackendController createBackendController() {
    AbstractBackendController slaveBackendController = (AbstractBackendController) getSlaveControllerFactory()
        .createBackendController();
    // Start the slave controller
    slaveBackendController.start(getLocale(), getClientTimeZone());
    // Use the same application session
    slaveBackendController.setApplicationSession(getApplicationSession());
    slaveBackendController.masterController = this;
    return slaveBackendController;
  }

  /**
   * Executes an action transactionally, e.g. when the @Transactional annotation
   * is present.
   *
   * @param action
   *     the action to execute.
   * @param context
   *     the context
   * @return the action outcome
   */
  public boolean executeTransactionally(final IAction action, final Map<String, Object> context) {
    Boolean ret = getTransactionTemplate().execute(new TransactionCallback<Boolean>() {

      @Override
      public Boolean doInTransaction(TransactionStatus status) {
        boolean executionStatus = action.execute(AbstractBackendController.this, context);
        if (!executionStatus) {
          status.setRollbackOnly();
        }
        return executionStatus;
      }
    });
    return ret;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IAccessorFactory getAccessorFactory() {
    return modelConnectorFactory.getAccessorFactory();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IApplicationSession getApplicationSession() {
    return applicationSession;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, Object> getDirtyProperties(IEntity entity) {
    return getDirtyProperties(entity, true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, Object> getDirtyProperties(IEntity entity, boolean includeComputed) {
    Map<String, Object> dirtyProperties;
    if (isUnitOfWorkActive()) {
      dirtyProperties = unitOfWork.getDirtyProperties(entity);
    } else {
      dirtyProperties = sessionUnitOfWork.getDirtyProperties(entity);
    }
    if (dirtyProperties != null) {
      for (Iterator<Map.Entry<String, Object>> ite = dirtyProperties.entrySet().iterator(); ite.hasNext();) {
        Map.Entry<String, Object> property = ite.next();
        boolean include = true;
        if (!includeComputed) {
          IComponentDescriptor<?> entityDescriptor = getEntityFactory().getComponentDescriptor(getComponentContract(
              entity));
          IPropertyDescriptor propertyDescriptor = entityDescriptor.getPropertyDescriptor(property.getKey());
          include = (propertyDescriptor != null && !propertyDescriptor.isComputed());
        }
        if (include) {
          Object propertyValue = property.getValue();
          Object currentProperty = entity.straightGetProperty(property.getKey());
          if ((currentProperty != null && !(currentProperty instanceof Collection<?>) && areEqualWithoutInitializing(
              currentProperty, property.getValue())) || (currentProperty == null && propertyValue == null)) {
            // Unfortunately, we cannot ignore collections that have been
            // changed but reset to their original state. This prevents the
            // entity to be merged back into the session while the session state
            // might be wrong.
            clearPropertyDirtyState(currentProperty);
            ite.remove(); // actually removes the mapping from the map.
          }
        } else {
          ite.remove();
        }
      }
    }
    return dirtyProperties;
  }

  private boolean areEqualWithoutInitializing(Object obj1, Object obj2) {
    if (obj1 == obj2) {
      return true;
    }
    if (obj1 == null || obj2 == null) {
      return false;
    }
    if (obj1 instanceof IEntity) {
      // To prevent lazy initialization.
      if (obj2 instanceof IEntity) {
        return ((IEntity) obj1).getId().equals(((IEntity) obj2).getId());
      }
      return false;
    }
    return obj1.equals(obj2);
  }

  /**
   * Resets the property technical dirty state. Gives a chance to subclasses to
   * reset technical dirty state. Useful in Hibernate for resetting collection
   * dirty states when their state is identical to the original one after
   * several modifications.
   *
   * @param property
   *     the property to reset the dirty state for.
   */
  protected void clearPropertyDirtyState(Object property) {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IEntityFactory getEntityFactory() {
    return entityFactory;
  }

  /**
   * Contains the current backend controller.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public Map<String, Object> getInitialActionContext() {
    Map<String, Object> initialActionContext = new HashMap<>();
    initialActionContext.put(ActionContextConstants.BACK_CONTROLLER, this);
    return initialActionContext;
  }

  /**
   * Gets the locale used by this controller. The locale is actually held by the
   * session.
   *
   * @return locale used by this controller.
   */
  @Override
  public Locale getLocale() {
    return applicationSession.getLocale();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IEntity getRegisteredEntity(Class<? extends IEntity> entityContract, Serializable entityId) {
    return sessionUnitOfWork.getRegisteredEntity(entityContract, entityId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<Class<? extends IEntity>, Map<Serializable, IEntity>> getUnitOfWorkEntities() {
    if (!isUnitOfWorkActive()) {
      throw new BackendException("Cannot query a unit of work that has not begun.");
    }
    return unitOfWork.getRegisteredEntities();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IEntity getUnitOfWorkEntity(Class<? extends IEntity> entityContract, Serializable entityId) {
    if (!isUnitOfWorkActive()) {
      throw new BackendException("Cannot query a unit of work that has not begun.");
    }
    return unitOfWork.getRegisteredEntity(entityContract, entityId);
  }

  /**
   * Gets unit of work or registered entity.
   *
   * @param entityType
   *     the entity type
   * @param id
   *     the id
   * @return the unit of work or registered entity
   */
  @Override
  public IEntity getUnitOfWorkOrRegisteredEntity(Class<? extends IEntity> entityType, Serializable id) {
    IEntity entity;
    if (isUnitOfWorkActive()) {
      entity = getUnitOfWorkEntity(entityType, id);
    } else {
      entity = getRegisteredEntity(entityType, id);
    }
    return entity;
  }

  /**
   * Gets the transactionTemplate.
   *
   * @return the transactionTemplate.
   */
  @Override
  public TransactionTemplate getTransactionTemplate() {
    return transactionTemplate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IValueConnector getWorkspaceConnector(String workspaceName) {
    return workspaceConnectors.get(workspaceName);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public IValueConnector getModuleConnector(Module module) {
    if (module == null) {
      return null;
    }
    // we must rehash entries in case in case modules hashcode have changed and
    // still preserve LRU order.
    Map<Module, IValueConnector> buff = new LinkedHashMap<>();
    buff.putAll(moduleConnectors);
    moduleConnectors.clear();
    moduleConnectors.putAll(buff);
    IValueConnector moduleConnector = moduleConnectors.get(module);
    if (moduleConnector == null) {
      moduleConnector = createModelConnector(module.getName(), ModuleDescriptor.MODULE_DESCRIPTOR);
      moduleConnectors.put(module, moduleConnector);
    }
    moduleConnector.setConnectorValue(module);
    return moduleConnector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public abstract void initializePropertyIfNeeded(IComponent componentOrEntity, String propertyName);

  /**
   * Sets the model controller workspaces. These workspaces are not kept as-is.
   * Their connectors are.
   *
   * @param workspaces
   *     A map containing the workspaces indexed by a well-known key used
   *     to bind them with their views.
   */
  @Override
  public void installWorkspaces(Map<String, Workspace> workspaces) {
    workspaceConnectors = new HashMap<>();
    for (Map.Entry<String, Workspace> workspaceEntry : workspaces.entrySet()) {
      String workspaceName = workspaceEntry.getKey();
      Workspace workspace = workspaceEntry.getValue();
      IModelDescriptor workspaceDescriptor;
      workspaceDescriptor = WorkspaceDescriptor.WORKSPACE_DESCRIPTOR;
      IValueConnector nextWorkspaceConnector = modelConnectorFactory.createModelConnector(workspaceName,
          workspaceDescriptor, this);
      nextWorkspaceConnector.setConnectorValue(workspace);
      workspaceConnectors.put(workspaceName, nextWorkspaceConnector);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAnyDirtyInDepth(Collection<?> elements) {
    return isAnyDirtyInDepth(elements, false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAnyDirtyInDepth(Collection<?> elements, boolean includeComputed) {
    IEntityRegistry alreadyTraversed = createEntityRegistry("isAnyDirtyInDepth");
    if (elements != null) {
      for (Object element : elements) {
        if (element instanceof IEntity) {
          if (isDirtyInDepth((IEntity) element, includeComputed, alreadyTraversed)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDirtyInDepth(IEntity entity) {
    return isDirtyInDepth(entity, false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDirtyInDepth(IEntity entity, boolean includeComputed) {
    return isAnyDirtyInDepth(Collections.singleton(entity), includeComputed);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEntityRegisteredForDeletion(IEntity entity) {
    if (isUnitOfWorkActive()) {
      return unitOfWork.isEntityRegisteredForDeletion(entity);
    } else {
      return sessionUnitOfWork.isEntityRegisteredForDeletion(entity);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEntityRegisteredForUpdate(IEntity entity) {
    if (isUnitOfWorkActive()) {
      return unitOfWork.isEntityRegisteredForUpdate(entity);
    } else {
      return sessionUnitOfWork.isEntityRegisteredForUpdate(entity);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public abstract boolean isInitialized(Object objectOrProxy);

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isUnitOfWorkActive() {
    return unitOfWork.isActive();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isUpdatedInUnitOfWork(IEntity entity) {
    if (!isUnitOfWorkActive()) {
      throw new BackendException("Cannot access unit of work.");
    }
    return unitOfWork.isUpdated(entity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <E extends IEntity> E merge(E entity, EMergeMode mergeMode) {
    return merge(Collections.singletonList(entity), mergeMode).get(0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <E extends IEntity> List<E> merge(List<E> entities, EMergeMode mergeMode) {
    IEntityRegistry alreadyMerged = createEntityRegistry("merge");
    Set<IEntity> eventsToRelease = new LinkedHashSet<>();
    List<E> mergedList = new ArrayList<>();
    boolean wasDirtyTrackingEnabled = isDirtyTrackingEnabled();
    try {
      setDirtyTrackingEnabled(false);
      for (E entity : entities) {
        mergedList.add(merge(entity, mergeMode, alreadyMerged, eventsToRelease));
      }
    } finally {
      boolean suspendUnitOfWork = isUnitOfWorkActive();
      try {
        if (suspendUnitOfWork) {
          suspendUnitOfWork();
        }
        for (IEntity entity : eventsToRelease) {
          try {
            entity.releaseEvents();
          } catch (Throwable t) {
            LOG.error("An unexpected exception occurred when releasing events after a merge", t);
          }
        }
      } finally {
        if (suspendUnitOfWork) {
          resumeUnitOfWork();
        }
        setDirtyTrackingEnabled(wasDirtyTrackingEnabled);
      }
    }
    return mergedList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void recordAsSynchronized(IEntity flushedEntity) {
    if (isUnitOfWorkActive()) {
      Map<String, Object> hasActuallyBeenFlushed = getDirtyProperties(flushedEntity, false);
      if (hasActuallyBeenFlushed == null) {
        LOG.error("*BAD UOW USAGE* You are flushing an entity ({})[{}] that you have not cloned before in the UOW.\n"
                + "You should only work on entities copies you obtain using the "
                + "backendController.cloneInUnitOfWork(...) method.",
            new Object[]{flushedEntity, flushedEntity.getComponentContract().getSimpleName()});
        throw new BackendException(
            "An entity has been flushed to the persistent store without being first registered" + " in the UOW : "
                + flushedEntity);
      }
      unitOfWork.clearDirtyState(flushedEntity);
      if (isEntityRegisteredForDeletion(flushedEntity)) {
        if (IEntity.DELETED_VERSION.equals(flushedEntity.getVersion())) {
          unitOfWork.addDeletedEntity(flushedEntity);
        } else {
          // To support in-memory TX deletions
          sessionUnitOfWork.registerForDeletion(flushedEntity);
        }
      } else if (!hasActuallyBeenFlushed.isEmpty()) {
        unitOfWork.addUpdatedEntity(flushedEntity);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerEntity(IEntity entity) {
    Map<String, Object> initialDirtyProperties = null;
    if (!entity.isPersistent()) {
      initialDirtyProperties = new HashMap<>();
      for (Map.Entry<String, Object> property : entity.straightGetProperties().entrySet()) {
        String propertyName = property.getKey();
        Object propertyValue = property.getValue();
        if (propertyValue != null && !(propertyValue instanceof Collection<?> && ((Collection<?>) property.getValue())
            .isEmpty())) {
          initialDirtyProperties.put(propertyName, null);
        }
      }
    }
    if (isUnitOfWorkActive()) {
      unitOfWork.register(entity, initialDirtyProperties);
    } else {
      sessionUnitOfWork.register(entity, initialDirtyProperties);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerForDeletion(IEntity entity) {
    if (entity == null) {
      throw new IllegalArgumentException("Passed entity cannot be null");
    }
    if (isUnitOfWorkActive()) {
      unitOfWork.registerForDeletion(entity);
    } else {
      sessionUnitOfWork.registerForDeletion(entity);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerForUpdate(IEntity entity) {
    if (entity == null) {
      throw new IllegalArgumentException("Passed entity cannot be null");
    }
    if (isUnitOfWorkActive()) {
      unitOfWork.registerForUpdate(entity);
    } else {
      sessionUnitOfWork.registerForUpdate(entity);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComponentTransferStructure<IComponent> retrieveComponents() {
    return transferStructure;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void rollbackUnitOfWork() {
    if (!isUnitOfWorkActive()) {
      throw new BackendException("Cannot rollback a unit of work that has not begun.");
    }
    if (unitOfWork.hasNested()) {
      unitOfWork.rollback();
    } else {
      doRollbackUnitOfWork();
    }
  }

  /**
   * Performs actual UOW rollback.
   */
  protected void doRollbackUnitOfWork() {
    unitOfWork.rollback();
  }

  /**
   * Assigns the application session to this backend controller. This property
   * can only be set once and should only be used by the DI container. It will
   * rarely be changed from built-in defaults unless you need to specify a
   * custom implementation instance to be used.
   *
   * @param applicationSession
   *     the applicationSession to set.
   */
  public void setApplicationSession(IApplicationSession applicationSession) {
    this.applicationSession = applicationSession;
  }

  /**
   * Configures the entity clone factory used to carbon-copy entities. An entity
   * carbon-copy is an technical copy of an entity, including id and version but
   * excluding relationship properties. This mechanism is used by the controller
   * when duplicating entities into the UOW to allow for memory state aware
   * transactions. This property should only be used by the DI container. It
   * will rarely be changed from built-in defaults unless you need to specify a
   * custom implementation instance to be used.
   *
   * @param carbonEntityCloneFactory
   *     the carbonEntityCloneFactory to set.
   */
  public void setCarbonEntityCloneFactory(IEntityCloneFactory carbonEntityCloneFactory) {
    this.carbonEntityCloneFactory = carbonEntityCloneFactory;
  }

  /**
   * Configures the factory responsible for creating entities (or components)
   * collections that are held by domain relationship properties. This property
   * should only be used by the DI container. It will rarely be changed from
   * built-in defaults unless you need to specify a custom implementation
   * instance to be used.
   *
   * @param collectionFactory
   *     the collectionFactory to set.
   */
  public void setCollectionFactory(IComponentCollectionFactory collectionFactory) {
    this.collectionFactory = collectionFactory;
  }

  /**
   * Configures the entity factory to use to create new entities. Backend
   * controllers only accept instances of
   * {@code ControllerAwareProxyEntityFactory} or a subclass. This is
   * because the backend controller must keep track of created entities.
   * Jspresso entity implementations also use the controller from which they
   * were created behind the scene.
   *
   * @param entityFactory
   *     the entityFactory to set.
   */
  public void setEntityFactory(IEntityFactory entityFactory) {
    if (entityFactory != null && !(entityFactory instanceof ControllerAwareProxyEntityFactory)) {
      throw new IllegalArgumentException(
          "entityFactory must be a " + ControllerAwareProxyEntityFactory.class.getSimpleName());
    }
    this.entityFactory = entityFactory;
  }

  /**
   * Configures the model connector factory to use to create new model
   * connectors. Connectors are adapters used by the binding layer to access
   * domain model values.
   *
   * @param modelConnectorFactory
   *     the modelConnectorFactory to set.
   */
  public void setModelConnectorFactory(IModelConnectorFactory modelConnectorFactory) {
    this.modelConnectorFactory = modelConnectorFactory;
  }

  /**
   * Assigns the Spring transaction template to this backend controller. This
   * property can only be set once and should only be used by the DI container.
   * It will rarely be changed from built-in defaults unless you need to specify
   * a custom implementation instance to be used.
   * <p/>
   * The configured instance is the one that will be returned by the
   * controller's {@code getTransactionTemplate()} method that should be
   * used by the service layer for transaction management.
   *
   * @param transactionTemplate
   *     the transactionTemplate to set.
   */
  public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
    if (this.transactionTemplate != null) {
      throw new IllegalArgumentException("Spring transaction template can only be configured once.");
    }
    if (transactionTemplate != null && !(transactionTemplate instanceof ControllerAwareTransactionTemplate)) {
      throw new IllegalArgumentException("You have configured a transaction template that is not a controller "
          + "aware transaction template. This is not legal since this prevents "
          + "the Unit of Work to be synchronized with the current transaction.");
    }
    this.transactionTemplate = transactionTemplate;
  }

  /**
   * Creates a &quot;Unit of Work&quot; to be used by this controller.
   *
   * @return the created UOW.
   */
  protected IEntityUnitOfWork createUnitOfWork() {
    return new BasicEntityUnitOfWork();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean start(Locale startingLocale, TimeZone theClientTimeZone) {
    applicationSession.setLocale(startingLocale);
    setClientTimeZone(theClientTimeZone);
    return true;
  }

  /**
   * Sets client time zone.
   *
   * @param clientTimeZone
   *     the client time zone
   */
  @Override
  public void setClientTimeZone(TimeZone clientTimeZone) {
    this.clientTimeZone = clientTimeZone;
  }

  /**
   * Sets reference time zone id.
   *
   * @param referenceTimeZoneId
   *     the reference time zone id
   */
  public void setReferenceTimeZoneId(String referenceTimeZoneId) {
    if (referenceTimeZoneId != null) {
      this.referenceTimeZone = TimeZone.getTimeZone(referenceTimeZoneId);
    } else {
      this.referenceTimeZone = null;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean stop() {
    // The application session can now be shared across async slave
    // controllers.
    // if (applicationSession != null) {
    // applicationSession.clear();
    // }
    if (getUserPreferencesStore() != null) {
      getUserPreferencesStore().setStorePath(IPreferencesStore.GLOBAL_STORE);
    }
    if (sessionUnitOfWork != null) {
      sessionUnitOfWork.clear();
      sessionUnitOfWork.begin();
    }
    if (unitOfWork != null) {
      unitOfWork.clear();
    }
    if (workspaceConnectors != null) {
      workspaceConnectors.clear();
    }
    if (moduleConnectors != null) {
      moduleConnectors.clear();
    }
    transferStructure = null;
    cleanupControllerAsyncActionsThreadGroup();
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void storeComponents(ComponentTransferStructure<IComponent> components) {
    this.transferStructure = components;
  }

  /**
   * Creates a transient collection instance, in respect to the type of
   * collection passed as parameter.
   *
   * @param <E>  the type parameter
   * @param collection      the collection to take the type from (List, Set, ...)
   * @return a transient collection instance with the same interface type as the
   * parameter.
   */
  protected <E> Collection<E> createTransientEntityCollection(Collection<E> collection) {
    Collection<E> uowEntityCollection = null;
    if (collection instanceof Set<?>) {
      uowEntityCollection = collectionFactory.createComponentCollection(Set.class);
    } else if (collection instanceof List<?>) {
      uowEntityCollection = collectionFactory.createComponentCollection(List.class);
    }
    return uowEntityCollection;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDirty(IEntity entity) {
    return isDirty(entity, false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDirty(IEntity entity, boolean includeComputed) {
    if (entity == null) {
      return false;
    }
    Map<String, Object> entityDirtyProperties = getDirtyProperties(entity, includeComputed);
    if (entityDirtyProperties != null) {
      entityDirtyProperties.remove(IEntity.VERSION);
    }
    return entityDirtyProperties != null && !entityDirtyProperties.isEmpty();
  }

  /**
   * Gets whether the entity property is dirty (has changes that need to be
   * updated to the persistent store).
   *
   * @param entity
   *     the entity to test.
   * @param propertyName
   *     the entity property to test.
   * @return true if the entity is dirty.
   */
  @Override
  public boolean isDirty(IEntity entity, String propertyName) {
    if (entity == null) {
      return false;
    }
    Map<String, Object> entityDirtyProperties = getDirtyProperties(entity);
    if (entityDirtyProperties != null && entityDirtyProperties.containsKey(propertyName)) {
      IPropertyDescriptor propertyDescriptor = getEntityFactory().getComponentDescriptor(getComponentContract(entity))
                                                                 .getPropertyDescriptor(propertyName);
      return propertyDescriptor != null && !propertyDescriptor.isComputed();
    }
    return false;
  }

  /**
   * Gives a chance to the session to wrap a collection before making it part of
   * the unit of work.
   *
   * @param <E>  the type parameter
   * @param owner      the entity the collection belongs to.
   * @param detachedCollection      the transient collection to make part of the unit of work.
   * @param snapshotCollection      the original collection state as reported by the dirt recorder.
   * @param role      the name of the property represented by the collection in its
   *     owner.
   * @return the wrapped collection if any (it may be the collection itself as
   * in this implementation).
   */
  protected <E> Collection<E> wrapDetachedCollection(IEntity owner, Collection<E> detachedCollection,
                                                          Collection<E> snapshotCollection, String role) {
    return detachedCollection;
  }

  private IComponent cloneComponentInUnitOfWork(IComponent component, IEntityRegistry alreadyCloned,
                                                Set<IEntity> eventsToRelease) {
    IComponent uowComponent = carbonEntityCloneFactory.cloneComponent(component, entityFactory);
    Map<String, Object> componentProperties = component.straightGetProperties();
    for (Map.Entry<String, Object> property : componentProperties.entrySet()) {
      String propertyName = property.getKey();
      Object propertyValue = property.getValue();
      if (propertyValue instanceof IEntity) {
        if (isInitialized(propertyValue)) {
          uowComponent.straightSetProperty(propertyName, cloneInUnitOfWork((IEntity) propertyValue, alreadyCloned,
              eventsToRelease));
        } else {
          uowComponent.straightSetProperty(propertyName, cloneUninitializedProperty(uowComponent, propertyValue));
        }
      } else if (propertyValue instanceof IComponent) {
        uowComponent.straightSetProperty(propertyName, cloneComponentInUnitOfWork((IComponent) propertyValue,
            alreadyCloned, eventsToRelease));
      }
    }
    IComponent owningComponent = component.getOwningComponent();
    if (owningComponent != null) {
      IComponent uowOwningComponent;
      if (owningComponent instanceof IEntity) {
        uowOwningComponent = cloneInUnitOfWork((IEntity) owningComponent, alreadyCloned, eventsToRelease);
      } else {
        uowOwningComponent = cloneComponentInUnitOfWork(owningComponent, alreadyCloned, eventsToRelease);
      }
      uowComponent.setOwningComponent(uowOwningComponent, owningComponent.getOwningPropertyDescriptor());
    }
    return uowComponent;
  }

  @SuppressWarnings({"unchecked", "ConstantConditions"})
  private <E extends IEntity> E cloneInUnitOfWork(E entity, IEntityRegistry alreadyCloned,
                                                  Set<IEntity> eventsToRelease) {
    if (entity == null) {
      return null;
    }
    Class<? extends IEntity> entityContract = getComponentContract(entity);
    IComponentDescriptor<?> entityDescriptor = getEntityFactory().getComponentDescriptor(entityContract);
    E uowEntity = (E) alreadyCloned.get(entityContract, entity.getId());
    if (uowEntity != null) {
      return uowEntity;
    }
    uowEntity = performUowEntityCloning(entity);
    boolean eventsBlocked = false;
    try {
      if (isInitialized(uowEntity)) {
        eventsBlocked = uowEntity.blockEvents();
      }
      Map<String, Object> dirtyProperties;
      if (isInitialized(entity)) {
        dirtyProperties = unitOfWork.getParentDirtyProperties(entity, sessionUnitOfWork);
        if (dirtyProperties == null) {
          dirtyProperties = new HashMap<>();
        }
      } else {
        dirtyProperties = new HashMap<>();
      }
      alreadyCloned.register(entityContract, entity.getId(), uowEntity);
      if (isInitialized(entity)) {
        Map<String, Object> entityProperties = entity.straightGetProperties();
        for (Map.Entry<String, Object> property : entityProperties.entrySet()) {
          String propertyName = property.getKey();
          Object propertyValue = property.getValue();
          IPropertyDescriptor propertyDescriptor = entityDescriptor.getPropertyDescriptor(propertyName);
          if (propertyValue instanceof IEntity) {
            if (isInitialized(propertyValue)) {
              uowEntity.straightSetProperty(propertyName, cloneInUnitOfWork((IEntity) propertyValue, alreadyCloned,
                  eventsToRelease));
            } else {
              uowEntity.straightSetProperty(propertyName, cloneUninitializedProperty(uowEntity, propertyValue));
            }
          } else if (propertyValue instanceof Collection<?>
              // to support collections stored as java serializable blob.
              // and detachedEntities (see bug # 1130)
              && (propertyDescriptor == null || propertyDescriptor instanceof ICollectionPropertyDescriptor<?>)) {
            if (isInitialized(propertyValue)) {
              Collection<Object> uowCollection = createTransientEntityCollection(
                  (Collection<Object>) property.getValue());
              for (Object collectionElement : (Collection<?>) property.getValue()) {
                if (collectionElement != null) {
                  if (collectionElement instanceof IEntity) {
                    uowCollection.add(cloneInUnitOfWork((IEntity) collectionElement, alreadyCloned, eventsToRelease));
                  } else if (collectionElement instanceof IComponent) {
                    uowCollection.add(cloneComponentInUnitOfWork((IComponent) collectionElement, alreadyCloned,
                        eventsToRelease));
                  } else {
                    uowCollection.add(collectionElement);
                  }
                } else {
                  uowCollection.add(null);
                }
              }
              if (propertyDescriptor == null || !propertyDescriptor.isComputed()) {
                Collection<Object> snapshotCollection = null;
                Object originalProperty = dirtyProperties.get(propertyName);
                // Workaround bug #1148
                if (originalProperty != null && originalProperty instanceof Collection<?>) {
                  snapshotCollection = (Collection<Object>) originalProperty;
                  Collection<Object> clonedSnapshotCollection = createTransientEntityCollection(snapshotCollection);
                  for (Object snapshotCollectionElement : snapshotCollection) {
                    if (snapshotCollectionElement != null) {
                      if (snapshotCollectionElement instanceof IEntity) {
                        clonedSnapshotCollection.add(cloneInUnitOfWork((IEntity) snapshotCollectionElement,
                            alreadyCloned, eventsToRelease));
                      } else if (snapshotCollectionElement instanceof IComponent) {
                        clonedSnapshotCollection.add(cloneComponentInUnitOfWork((IComponent) snapshotCollectionElement,
                            alreadyCloned, eventsToRelease));
                      } else {
                        clonedSnapshotCollection.add(snapshotCollectionElement);
                      }
                    } else {
                      clonedSnapshotCollection.add(null);
                    }
                  }
                  snapshotCollection = clonedSnapshotCollection;
                }
                if (entity.isPersistent()) {
                  uowCollection = wrapDetachedCollection(entity, uowCollection, snapshotCollection, propertyName);
                }
              }
              uowEntity.straightSetProperty(propertyName, uowCollection);
            } else {
              uowEntity.straightSetProperty(propertyName, cloneUninitializedProperty(uowEntity, propertyValue));
            }
          } else if (propertyValue instanceof IEntity[]) {
            IEntity[] uowArray = new IEntity[((IEntity[]) propertyValue).length];
            for (int i = 0; i < uowArray.length; i++) {
              uowArray[i] = cloneInUnitOfWork(((IEntity[]) propertyValue)[i], alreadyCloned, eventsToRelease);
            }
            uowEntity.straightSetProperty(propertyName, uowArray);
          } else if (propertyValue instanceof IComponent[]) {
            IComponent[] uowArray = new IComponent[((IComponent[]) property.getValue()).length];
            for (int i = 0; i < uowArray.length; i++) {
              uowArray[i] = cloneComponentInUnitOfWork(((IComponent[]) propertyValue)[i], alreadyCloned,
                  eventsToRelease);
            }
            uowEntity.straightSetProperty(propertyName, uowArray);
          } else if (propertyValue instanceof IComponent) {
            uowEntity.straightSetProperty(propertyName, cloneComponentInUnitOfWork((IComponent) propertyValue,
                alreadyCloned, eventsToRelease));
          }
        }
        if (eventsBlocked && uowEntity != null && isInitialized(uowEntity)) {
          eventsToRelease.add(uowEntity);
        }
        unitOfWork.register(uowEntity, new HashMap<>(dirtyProperties));
        if (uowEntity instanceof ILifecycleCapable) {
          ((ILifecycleCapable) uowEntity).onClone(entity);
        }
      }
    } finally {
      if (eventsBlocked && uowEntity != null && isInitialized(uowEntity)) {
        eventsToRelease.add(uowEntity);
      }
    }
    return uowEntity;
  }

  /**
   * Performs the actual entity cloning in unit of work. Gives a chance to
   * subclasses to override and take a better decision than just a deep carbon
   * copy.
   *
   * @param <E>
   *     the actual entity type.
   * @param entity
   *     the source entity.
   * @return the cloned entity.
   */
  protected <E extends IEntity> E performUowEntityCloning(E entity) {
    E uowEntity = carbonEntityCloneFactory.cloneEntity(entity, entityFactory);
    return uowEntity;
  }

  private boolean isDirtyInDepth(IEntity entity, boolean includeComputed, IEntityRegistry alreadyTraversed) {
    alreadyTraversed.register(getComponentContract(entity), entity.getId(), entity);
    if (isDirty(entity, includeComputed)) {
      return true;
    }
    Map<String, Object> entityProps = entity.straightGetProperties();
    for (Map.Entry<String, Object> property : entityProps.entrySet()) {
      Object propertyValue = property.getValue();
      if (propertyValue instanceof IEntity) {
        if (isInitialized(propertyValue) && alreadyTraversed.get(getComponentContract((IEntity) propertyValue),
            ((IEntity) propertyValue).getId()) == null) {
          if (isDirtyInDepth((IEntity) propertyValue, includeComputed, alreadyTraversed)) {
            return true;
          }
        }
      } else if (propertyValue instanceof Collection<?>) {
        if (isInitialized(propertyValue)) {
          for (Object elt : ((Collection<?>) propertyValue)) {
            if (elt instanceof IEntity && alreadyTraversed.get(getComponentContract((IEntity) elt),
                ((IEntity) elt).getId()) == null) {
              if (isDirtyInDepth((IEntity) elt, includeComputed, alreadyTraversed)) {
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  }

  @SuppressWarnings({"unchecked", "ConstantConditions"})
  private <E extends IEntity> E merge(E entity, final EMergeMode mergeMode, IEntityRegistry alreadyMerged,
                                      Set<IEntity> eventsToRelease) {
    if (entity == null) {
      return null;
    }
    Class<? extends IEntity> entityContract = getComponentContract(entity);
    E alreadyMergedEntity = (E) alreadyMerged.get(entityContract, entity.getId());
    if (alreadyMergedEntity != null) {
      return alreadyMergedEntity;
    }
    // Allow for merging eagerly dirty entities
    if (mergeMode != EMergeMode.MERGE_EAGER) {
      checkBadMergeUsage(entity);
    }
    E registeredEntity = null;
    boolean eventsBlocked = false;
    try {
      registeredEntity = (E) getRegisteredEntity(getComponentContract(entity), entity.getId());
      boolean newlyRegistered = false;
      if (registeredEntity == null) {
        if (!isInitialized(entity)) {
          return mergeUninitializedEntity(entity);
        }
        registeredEntity = carbonEntityCloneFactory.cloneEntity(entity, entityFactory);
        if (mergeMode == EMergeMode.MERGE_EAGER) {
          sessionUnitOfWork.register(registeredEntity, getDirtyProperties(entity));
        } else {
          sessionUnitOfWork.register(registeredEntity, null);
        }
        newlyRegistered = true;
      } else if (mergeMode == EMergeMode.MERGE_KEEP || (
          (mergeMode == EMergeMode.MERGE_LAZY || mergeMode == EMergeMode.MERGE_CLEAN_LAZY) && !isInitialized(entity))) {
        alreadyMerged.register(entityContract, entity.getId(), registeredEntity);
        return registeredEntity;
      } else if (mergeMode == EMergeMode.MERGE_EAGER) {
        sessionUnitOfWork.register(registeredEntity, getDirtyProperties(entity));
      }
      if (isInitialized(registeredEntity)) {
        eventsBlocked = registeredEntity.blockEvents();
      }
      alreadyMerged.register(entityContract, entity.getId(), registeredEntity);
      if (newlyRegistered || (mergeMode != EMergeMode.MERGE_CLEAN_LAZY && mergeMode != EMergeMode.MERGE_LAZY)
          || registeredEntity.getVersion() == null || !registeredEntity.getVersion().equals(entity.getVersion())) {
        if (mergeMode == EMergeMode.MERGE_CLEAN_EAGER || mergeMode == EMergeMode.MERGE_CLEAN_LAZY
            || mergeMode == EMergeMode.MERGE_LAZY) {
          sessionUnitOfWork.clearDirtyState(entity);
        }
        IComponentDescriptor<?> entityDescriptor = getEntityFactory().getComponentDescriptor(getComponentContract(
            entity));
        Map<String, Object> entityProperties = entity.straightGetProperties();
        Map<String, Object> registeredEntityProperties = registeredEntity.straightGetProperties();
        Map<String, Object> mergedProperties = new LinkedHashMap<>();
        Set<String> propertiesToSort = new HashSet<>();
        for (Map.Entry<String, Object> property : entityProperties.entrySet()) {
          String propertyName = property.getKey();
          Object propertyValue = property.getValue();
          IPropertyDescriptor propertyDescriptor = entityDescriptor.getPropertyDescriptor(propertyName);
          if (propertyValue instanceof IEntity) {
            // Do not take the registeredProperty from the
            // registeredEntityProperties.
            // It might be a different ID from the one we are trying to merge
            // Object registeredProperty = registeredEntityProperties
            // .get(propertyName);
            Object registeredProperty = getRegisteredEntity(getComponentContract((IEntity) propertyValue),
                ((IEntity) propertyValue).getId());
            if (registeredProperty == null) {
              mergedProperties.put(propertyName, merge((IEntity) propertyValue, mergeMode, alreadyMerged,
                  eventsToRelease));
            } else {
              if (mergeMode == EMergeMode.MERGE_EAGER || mergeMode == EMergeMode.MERGE_LAZY) {
                if (isInitialized(propertyValue)) {
                  initializePropertyIfNeeded(registeredEntity, propertyName);
                } else if (isInitialized(registeredProperty)) {
                  initializePropertyIfNeeded(entity, propertyName);
                }
              }
              // Fix for bug #1023 : the referenced entity might have changed
              // int the TX. Merge must happen unconditionally.
              // if (isInitialized(registeredProperty)) {
              mergedProperties.put(propertyName, merge((IEntity) propertyValue, mergeMode, alreadyMerged,
                  eventsToRelease));
            }
          } else if (propertyValue instanceof Collection<?>
              // to support collections stored as java serializable blob.
              // and detachedEntities (see bug # 1130)
              && (propertyDescriptor == null || propertyDescriptor instanceof ICollectionPropertyDescriptor<?>)) {
            Collection<Object> registeredCollection = (Collection<Object>) registeredEntityProperties.get(
                propertyName);
            if (!newlyRegistered && (mergeMode == EMergeMode.MERGE_EAGER || mergeMode == EMergeMode.MERGE_LAZY)) {
              if (isInitialized(propertyValue)) {
                initializePropertyIfNeeded(registeredEntity, propertyName);
              } else if (isInitialized(registeredCollection)) {
                initializePropertyIfNeeded(entity, propertyName);
              }
            }
            if (isInitialized(registeredCollection)) {
              if (newlyRegistered && !isInitialized(propertyValue)) {
                // Must have another collection instance.
                // See bug #902
                registeredCollection = cloneUninitializedProperty(registeredEntity,
                    (Collection<Object>) propertyValue);
              } else {
                if (propertyValue instanceof List) {
                  registeredCollection = collectionFactory.createComponentCollection(List.class);
                } else {
                  registeredCollection = collectionFactory.createComponentCollection(Set.class);
                }
                initializePropertyIfNeeded(entity, propertyName);
                List<?> reusedComponentInstances = null;
                if (registeredEntityProperties.get(propertyName) != null) {
                  reusedComponentInstances = new ArrayList<>(
                      (Collection<?>) registeredEntityProperties.get(propertyName));
                }
                int i = 0;
                for (Object collectionElement : (Collection<?>) propertyValue) {
                  if (collectionElement instanceof IEntity) {
                    registeredCollection.add(merge((IEntity) collectionElement, mergeMode, alreadyMerged,
                        eventsToRelease));
                  } else if (collectionElement instanceof IComponent) {
                    IComponent registeredComponent = null;
                    // We must reuse component instances for binding to operate correctly.
                    if (reusedComponentInstances != null && i < reusedComponentInstances.size()) {
                      registeredComponent = (IComponent) reusedComponentInstances.get(i);
                    }
                    registeredCollection.add(mergeComponent((IComponent) collectionElement, registeredComponent, mergeMode,
                        alreadyMerged, eventsToRelease));
                    i++;
                  } else {
                    registeredCollection.add(collectionElement);
                  }
                }
              }
              Collection<?> mergedCollection = mergeCollection(propertyName, propertyValue, registeredEntity,
                  registeredCollection);
              mergedProperties.put(propertyName, mergedCollection);
              if (isInitialized(registeredCollection)) {
                propertiesToSort.add(propertyName);
              }
            }
          } else if (propertyValue instanceof IComponent) {
            IComponent registeredComponent = (IComponent) registeredEntityProperties.get(propertyName);
            mergedProperties.put(propertyName, mergeComponent((IComponent) propertyValue, registeredComponent,
                mergeMode, alreadyMerged, eventsToRelease));
          } else {
            mergedProperties.put(propertyName, propertyValue);
          }
        }
        registeredEntity.straightSetProperties(mergedProperties);
        for (String propertyToSort : propertiesToSort) {
          getEntityFactory().sortCollectionProperty(registeredEntity, propertyToSort);
        }
      } else if (mergeMode == EMergeMode.MERGE_CLEAN_LAZY) {
        // version has not evolved but we must still reset dirty properties in
        // case only versionControl false properties have changed.
        sessionUnitOfWork.clearDirtyState(entity);
      }
      if (registeredEntity instanceof ILifecycleCapable) {
        ((ILifecycleCapable) registeredEntity).onClone(entity);
      }
      return registeredEntity;
    } finally {
      if (eventsBlocked && registeredEntity != null && isInitialized(registeredEntity)) {
        eventsToRelease.add(registeredEntity);
      }
    }
  }

  /**
   * Merge non initialized entity.
   *
   * @param <E>
   *     the actual entity type
   * @param entity
   *     the entity
   * @return the merged entity
   */
  protected <E extends IEntity> E mergeUninitializedEntity(E entity) {
    return entity;
  }

  /**
   * Merge collection.
   *
   * @param <E>
   *     the actual entity type.
   * @param propertyName
   *     the property name
   * @param propertyValue
   *     the property value
   * @param registeredEntity
   *     the registered entity
   * @param registeredCollection
   *     the registered collection
   * @return the collection
   */
  protected <E extends IEntity> Collection<?> mergeCollection(String propertyName, Object propertyValue,
                                                                       E registeredEntity,
                                                                       Collection<?> registeredCollection) {
    return registeredCollection;
  }

  private <E extends IEntity> void checkBadMergeUsage(E entity) {
    if (isUnitOfWorkActive()) {
      if (isInitialized(entity) && entity.isPersistent() && isDirty(entity)) {
        LOG.error(
            "*BAD MERGE USAGE* An attempt is made to merge a UOW dirty entity ({})[{}] to the application session.\n"
                + "This will break transaction isolation since, if the transaction is rolled back,"
                + " the UOW dirty state will be kept.\n"
                + "Dirty UOW entities will be automatically merged whenever the transaction is committed.", entity,
            getComponentContract(entity).getSimpleName());
        if (isThrowExceptionOnBadUsage()) {
          throw new BackendException("A bad usage has been detected on the backend controller."
              + "This is certainly an application coding problem. Please check the logs.");
        }
      }
    }
  }

  private IComponent mergeComponent(IComponent componentToMerge, IComponent registeredComponent, EMergeMode mergeMode,
                                    IEntityRegistry alreadyMerged, Set<IEntity> eventsToRelease) {
    IComponent varRegisteredComponent = registeredComponent;
    if (componentToMerge == null) {
      return null;
    }
    if (varRegisteredComponent == null) {
      varRegisteredComponent = carbonEntityCloneFactory.cloneComponent(componentToMerge, entityFactory);
      IComponent owningComponent = componentToMerge.getOwningComponent();
      if (owningComponent != null) {
        IComponent uowOwningComponent;
        if (owningComponent instanceof IEntity) {
          uowOwningComponent = merge((IEntity) owningComponent, mergeMode, alreadyMerged, eventsToRelease);
        } else {
          uowOwningComponent = mergeComponent(owningComponent, null, mergeMode, alreadyMerged, eventsToRelease);
        }
        varRegisteredComponent.setOwningComponent(uowOwningComponent, owningComponent.getOwningPropertyDescriptor());
      }
    } else if (mergeMode == EMergeMode.MERGE_KEEP) {
      return varRegisteredComponent;
    }
    Map<String, Object> componentPropertiesToMerge = componentToMerge.straightGetProperties();
    Map<String, Object> registeredComponentProperties = varRegisteredComponent.straightGetProperties();
    Map<String, Object> mergedProperties = new HashMap<>();
    for (Map.Entry<String, Object> property : componentPropertiesToMerge.entrySet()) {
      String propertyName = property.getKey();
      Object propertyValue = property.getValue();
      if (propertyValue instanceof IEntity) {
        // Do not take the registeredProperty from the
        // registeredEntityProperties.
        // It might be a different ID from the one we are trying to merge
        // Object registeredProperty = registeredEntityProperties
        // .get(propertyName);
        Object registeredProperty = getRegisteredEntity(getComponentContract((IEntity) propertyValue),
            ((IEntity) propertyValue).getId());
        if (registeredProperty == null) {
          mergedProperties.put(propertyName, merge((IEntity) propertyValue, mergeMode, alreadyMerged, eventsToRelease));
        } else {
          if (mergeMode == EMergeMode.MERGE_EAGER || mergeMode == EMergeMode.MERGE_LAZY) {
            if (isInitialized(propertyValue)) {
              initializePropertyIfNeeded(varRegisteredComponent, propertyName);
            } else if (isInitialized(registeredProperty)) {
              initializePropertyIfNeeded(componentToMerge, propertyName);
            }
          }
          // Fix for bug #1023 : the referenced entity might have changed
          // int the TX. Merge must happen unconditionally.
          // if (isInitialized(registeredProperty)) {
          mergedProperties.put(propertyName, merge((IEntity) propertyValue, mergeMode, alreadyMerged, eventsToRelease));
        }
      } else if (propertyValue instanceof IComponent) {
        IComponent registeredSubComponent = (IComponent) registeredComponentProperties.get(propertyName);
        mergedProperties.put(propertyName, mergeComponent((IComponent) propertyValue, registeredSubComponent, mergeMode,
            alreadyMerged, eventsToRelease));
      } else {
        mergedProperties.put(propertyName, propertyValue);
      }
    }
    varRegisteredComponent.straightSetProperties(mergedProperties);
    return varRegisteredComponent;
  }

  /**
   * Performs necessary cleanings when an entity or component is deleted.
   *
   * @param component
   *     the deleted entity or component.
   * @param dryRun
   *     set to true to simulate before actually doing it.
   * @throws IllegalAccessException
   *     whenever this kind of exception occurs.
   * @throws InvocationTargetException
   *     whenever this kind of exception occurs.
   * @throws NoSuchMethodException
   *     whenever this kind of exception occurs.
   */
  @Override
  public void cleanRelationshipsOnDeletion(IComponent component, boolean dryRun)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    Set<IComponent> clearedEntities = new HashSet<>();
    Map<IComponent, RuntimeException> integrityViolations = new HashMap<>();
    cleanRelationshipsOnDeletion(component, dryRun, clearedEntities, integrityViolations);
    // Throw exceptions for entities that have not been cleared during the
    // process.
    for (Map.Entry<IComponent, RuntimeException> integrityViolation : integrityViolations.entrySet()) {
      if (!(clearedEntities.contains(integrityViolation.getKey()))) {
        throw integrityViolation.getValue();
      }
    }
  }

  @SuppressWarnings({"unchecked", "ThrowableResultOfMethodCallIgnored", "ConstantConditions", "SuspiciousMethodCalls"})
  private void cleanRelationshipsOnDeletion(IComponent componentOrProxy, boolean dryRun,
                                            Set<IComponent> clearedEntities,
                                            Map<IComponent, RuntimeException> integrityViolations)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    if (componentOrProxy == null) {
      return;
    }
    IComponent component;
    component = (IComponent) unwrapProxy(componentOrProxy);
    Class<? extends IComponent> componentContract = getComponentContract(component);
    if (clearedEntities.contains(component)) {
      return;
    }
    clearedEntities.add(component);
    if (!dryRun) {
      if (component instanceof IEntity) {
        registerForDeletion((IEntity) component);
      }
    }
    try {
      component.setPropertyProcessorsEnabled(false);
      IComponentDescriptor<?> componentDescriptor = getEntityFactory().getComponentDescriptor(componentContract);
      for (Map.Entry<String, Object> property : component.straightGetProperties().entrySet()) {
        String propertyName = property.getKey();
        Object propertyValue = property.getValue();
        if (propertyValue != null) {
          IPropertyDescriptor propertyDescriptor = componentDescriptor.getPropertyDescriptor(propertyName);
          if (propertyDescriptor instanceof IRelationshipEndPropertyDescriptor) {
            // force initialization of relationship property.
            getAccessorFactory().createPropertyAccessor(propertyName, componentContract).getValue(component);
            if (propertyDescriptor instanceof IReferencePropertyDescriptor && propertyValue instanceof IEntity) {
              if (((IRelationshipEndPropertyDescriptor) propertyDescriptor).isComposition()) {
                cleanRelationshipsOnDeletion((IEntity) propertyValue, dryRun, clearedEntities, integrityViolations);
              } else {
                if (((IRelationshipEndPropertyDescriptor) propertyDescriptor).getReverseRelationEnd() != null) {
                  IPropertyDescriptor reversePropertyDescriptor = ((IReferencePropertyDescriptor<?>) propertyDescriptor)
                      .getReverseRelationEnd();
                  //noinspection SuspiciousMethodCalls
                  if (!clearedEntities.contains(propertyValue)) {
                    try {
                      if (reversePropertyDescriptor instanceof IReferencePropertyDescriptor) {
                        if (dryRun) {
                          // manually trigger reverse relations preprocessors.
                          reversePropertyDescriptor.preprocessSetter(propertyValue, null);
                        } else {
                          getAccessorFactory().createPropertyAccessor(reversePropertyDescriptor.getName(),
                              getComponentContract(((IComponent) propertyValue))).setValue(propertyValue, null);
                          // Technically reset to original value to avoid
                          // Hibernate not-null checks
                          component.straightSetProperty(propertyName, propertyValue);
                        }
                      } else if (reversePropertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
                        if (dryRun) {
                          // manually trigger reverse relations preprocessors.
                          Collection<Object> reverseCollection = getAccessorFactory().createPropertyAccessor(
                              reversePropertyDescriptor.getName(), getComponentContract(((IComponent) propertyValue)))
                                                                                     .getValue(propertyValue);
                          ((ICollectionPropertyDescriptor<?>) reversePropertyDescriptor).preprocessRemover(
                              propertyValue, reverseCollection, component);
                        } else {
                          getAccessorFactory().createCollectionPropertyAccessor(reversePropertyDescriptor.getName(),
                              getComponentContract(((IComponent) propertyValue)), componentContract).removeFromValue(
                              propertyValue, component);
                          // but technically reset to original value to avoid
                          // Hibernate not-null checks
                          component.straightSetProperty(propertyName, propertyValue);
                        }
                      }
                    } catch (RuntimeException ex) {
                      integrityViolations.put((IComponent) propertyValue, ex);
                    }
                  }
                }
              }
            } else if (propertyDescriptor instanceof ICollectionPropertyDescriptor) {
              if (((ICollectionPropertyDescriptor<?>) propertyDescriptor).isComposition()) {
                for (Object composedElement : new ArrayList<>((Collection<?>) propertyValue)) {
                  if (composedElement instanceof IComponent) {
                    cleanRelationshipsOnDeletion((IComponent) composedElement, dryRun, clearedEntities, integrityViolations);
                  }
                }
              } else if (propertyDescriptor.isModifiable() && !((Collection<?>) propertyValue).isEmpty()) {
                if (((ICollectionPropertyDescriptor<?>) propertyDescriptor).getReverseRelationEnd() != null) {
                  IPropertyDescriptor reversePropertyDescriptor = ((ICollectionPropertyDescriptor<?>)
                      propertyDescriptor)
                      .getReverseRelationEnd();
                  for (Object collectionElement : new ArrayList<>((Collection<?>) propertyValue)) {
                    if (collectionElement instanceof IComponent && !clearedEntities.contains(collectionElement)) {
                      try {
                        if (reversePropertyDescriptor instanceof IReferencePropertyDescriptor) {
                          if (dryRun) {
                            // manually trigger reverse relations preprocessors.
                            reversePropertyDescriptor.preprocessSetter(collectionElement, null);
                          } else {
                            getAccessorFactory().createPropertyAccessor(reversePropertyDescriptor.getName(),
                                getComponentContract((IComponent) collectionElement)).setValue(collectionElement, null);
                          }
                        } else if (reversePropertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
                          if (dryRun) {
                            // manually trigger reverse relations preprocessors.
                            Collection<Object> reverseCollection = getAccessorFactory()
                                .createPropertyAccessor(reversePropertyDescriptor.getName(), getComponentContract(
                                    (IComponent) collectionElement)).getValue(collectionElement);
                            ((ICollectionPropertyDescriptor<?>) reversePropertyDescriptor).preprocessRemover(
                                collectionElement, reverseCollection, component);
                          } else {
                            getAccessorFactory().createCollectionPropertyAccessor(reversePropertyDescriptor.getName(),
                                getComponentContract((IComponent) collectionElement),
                                componentContract).removeFromValue(collectionElement, component);
                          }
                        }
                      } catch (RuntimeException ex) {
                        integrityViolations.put((IComponent) collectionElement, ex);
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    } finally {
      component.setPropertyProcessorsEnabled(true);
    }
  }

  /**
   * Unwrap ORM proxy if needed.
   *
   * @param componentOrProxy
   *     the component or proxy.
   * @return the proxy implementation if it's an ORM proxy.
   */
  protected Object unwrapProxy(Object componentOrProxy) {
    return componentOrProxy;
  }

  /**
   * Clones an uninitialized (proxied) property.
   *
   * @param <E>  the type parameter
   * @param owner      the property owner.
   * @param propertyValue      the propertyValue.
   * @return the property clone.
   */
  protected <E> E cloneUninitializedProperty(Object owner, E propertyValue) {
    return propertyValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void loggedIn(Subject subject) {
    getApplicationSession().setSubject(subject);

    String userPreferredLanguageCode = (String) getApplicationSession().getPrincipal().getCustomProperty(
        UserPrincipal.LANGUAGE_PROPERTY);
    if (userPreferredLanguageCode != null) {
      getApplicationSession().setLocale(LocaleUtils.toLocale(userPreferredLanguageCode));
    }
    if (getUserPreferencesStore() != null) {
      getUserPreferencesStore().setStorePath(getApplicationSession().getUsername());
    }
  }

  /**
   * Reads a user preference.
   *
   * @param key
   *     the key under which the preference as been stored.
   * @return the stored preference or null.
   */
  @Override
  public String getUserPreference(String key) {
    if (getUserPreferencesStore() != null) {
      return getUserPreferencesStore().getPreference(key);
    }
    return null;
  }

  /**
   * Stores a user preference.
   *
   * @param key
   *     the key under which the preference as to be stored.
   * @param value
   *     the value of the preference to be stored.
   */
  @Override
  public void putUserPreference(String key, String value) {
    if (getUserPreferencesStore() != null) {
      getUserPreferencesStore().putPreference(key, value);
    }
  }

  /**
   * Deletes a user preference.
   *
   * @param key
   *     the key under which the preference is stored.
   */
  @Override
  public void removeUserPreference(String key) {
    if (getUserPreferencesStore() != null) {
      getUserPreferencesStore().removePreference(key);
    }
  }

  /**
   * Gets the user preferences store.
   *
   * @return the user preferences store.
   */
  protected IPreferencesStore getUserPreferencesStore() {
    return userPreferencesStore;
  }

  /**
   * Sets the user preference store.
   *
   * @param userPreferencesStore
   *     the userPreferenceStore to set.
   */
  public void setUserPreferencesStore(IPreferencesStore userPreferencesStore) {
    this.userPreferencesStore = userPreferencesStore;
  }

  /**
   * Configures the translation provider used to compute internationalized
   * messages and labels.
   *
   * @param translationProvider
   *     the translationProvider to set.
   */
  public void setTranslationProvider(ITranslationProvider translationProvider) {
    this.translationProvider = translationProvider;
  }

  /**
   * Delegates to the translation provider.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public String getTranslation(String key, Locale locale) {
    if (customTranslationPlugin != null) {
      String translation = customTranslationPlugin.getTranslation(key, locale, getApplicationSession());
      if (translation != null) {
        return translation;
      }
    }
    return translationProvider.getTranslation(key, locale);
  }

  /**
   * Delegates to the translation provider.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public String getTranslation(String key, Object[] args, Locale locale) {
    if (customTranslationPlugin != null) {
      String translation = customTranslationPlugin.getTranslation(key, args, locale, getApplicationSession());
      if (translation != null) {
        return translation;
      }
    }
    return translationProvider.getTranslation(key, args, locale);
  }

  /**
   * Delegates to the translation provider.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public String getTranslation(String key, String defaultMessage, Locale locale) {
    if (customTranslationPlugin != null) {
      String translation = customTranslationPlugin.getTranslation(key, locale, getApplicationSession());
      if (translation != null) {
        return translation;
      }
    }
    return translationProvider.getTranslation(key, defaultMessage, locale);
  }

  /**
   * Delegates to the translation provider.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public String getTranslation(String key, Object[] args, String defaultMessage, Locale locale) {
    if (customTranslationPlugin != null) {
      String translation = customTranslationPlugin.getTranslation(key, args, locale, getApplicationSession());
      if (translation != null) {
        return translation;
      }
    }
    return translationProvider.getTranslation(key, args, defaultMessage, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAccessGranted(ISecurable securable) {
    if (SecurityHelper.isSubjectGranted(getApplicationSession().getSubject(), securable)) {
      if (customSecurityPlugin != null) {
        try {
          pushToSecurityContext(securable);
          Map<String, Object> securityContext = new HashMap<>();
          if (getApplicationSession() != null && getApplicationSession().getPrincipal() != null) {
            securityContext.put(SecurityContextConstants.USER_ROLES, SecurityHelper.getRoles(
                getApplicationSession().getSubject()));
            securityContext.put(SecurityContextConstants.USER_ID, getApplicationSession().getUsername());
            Map<String, Object> sessionProperties = getApplicationSession().getCustomValues();
            sessionProperties.putAll(getApplicationSession().getPrincipal().getCustomProperties());
            securityContext.put(SecurityContextConstants.SESSION_PROPERTIES, sessionProperties);
          }
          securityContext.putAll(getSecurityContext());
          return customSecurityPlugin.isAccessGranted(securable, securityContext);
        } finally {
          restoreLastSecurityContextSnapshot();
        }
      }
      return true;
    }
    return false;
  }

  /**
   * Configures a custom security plugin on the controller. The controller
   * itself is a security handler and is used as such across most of the
   * application layers. Before delegating to the custom security handler, the
   * controller will apply role-based security rules that cannot be disabled.
   *
   * @param customSecurityPlugin
   *     the customESecurityHandler to set.
   */
  public void setCustomSecurityPlugin(ISecurityPlugin customSecurityPlugin) {
    this.customSecurityPlugin = customSecurityPlugin;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, Object> getSecurityContext() {
    return securityContextBuilder.getSecurityContext();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ISecurityContextBuilder pushToSecurityContext(Object contextElement) {
    securityContextBuilder.pushToSecurityContext(contextElement);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ISecurityContextBuilder restoreLastSecurityContextSnapshot() {
    securityContextBuilder.restoreLastSecurityContextSnapshot();
    return this;
  }

  /**
   * Configures a custom translation plugin on the controller. The controller
   * itself is a translation provider and is used as such across most of the
   * application layers. The custom translation plugin is used to override the
   * default static, bundle-based, i18n scheme.
   *
   * @param customTranslationPlugin
   *     the customTranslationPlugin to set.
   */
  public void setCustomTranslationPlugin(ITranslationPlugin customTranslationPlugin) {
    this.customTranslationPlugin = customTranslationPlugin;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TimeZone getReferenceTimeZone() {
    if (referenceTimeZone != null) {
      return referenceTimeZone;
    }
    return TimeZone.getDefault();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TimeZone getClientTimeZone() {
    if (clientTimeZone != null) {
      return clientTimeZone;
    }
    return TimeZone.getDefault();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void cleanupRequestResources() {
    // Empty implementation
  }

  /**
   * Gets the throwExceptionOnBadUsage.
   *
   * @return the throwExceptionOnBadUsage.
   */
  public boolean isThrowExceptionOnBadUsage() {
    return throwExceptionOnBadUsage;
  }

  /**
   * Configures the backend controller to throw or not an exception whenever a
   * bad usage is detected like manually merging a dirty entity from an ongoing
   * UOW.
   *
   * @param throwExceptionOnBadUsage
   *     the throwExceptionOnBadUsage to set.
   */
  public void setThrowExceptionOnBadUsage(boolean throwExceptionOnBadUsage) {
    this.throwExceptionOnBadUsage = throwExceptionOnBadUsage;
  }

  /**
   * Performs necessary checks in order to ensure isolation on unit of work.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public Object sanitizeModifierParam(Object target, IPropertyDescriptor propertyDescriptor, Object param) {
    if (propertyDescriptor.isComputed()) {
      // do not perform any check regarding computed properties.
      return param;
    }
    if (param instanceof Collection<?>) {
      for (Object element : (Collection<?>) param) {
        // the return value is not leveraged.
        sanitizeModifierParam(target, propertyDescriptor, element);
      }
      return param;
    }
    Class<?> targetClass;
    IEntity targetEntity = null;
    IEntity sessionTargetEntity = null;
    IEntity paramEntity = null;
    IEntity sessionParamEntity = null;
    if (target instanceof IComponent) {
      targetEntity = refineEntity((IComponent) target);
      if (targetEntity != null) {
        sessionTargetEntity = getRegisteredEntity(getComponentContract(targetEntity), targetEntity.getId());
      }
    }
    if (param instanceof IComponent) {
      paramEntity = refineEntity((IComponent) param);
      if (paramEntity != null) {
        sessionParamEntity = getRegisteredEntity(getComponentContract(paramEntity), paramEntity.getId());
      }
    }

    if (target instanceof IComponent) {
      targetClass = getComponentContract(((IComponent) target));
    } else {
      targetClass = target.getClass();
    }

    if (isUnitOfWorkActive()) {
      if (targetEntity != null && objectEquals(targetEntity, sessionTargetEntity)) {
        // We are modifying on a session entity inside a unit of work. This is
        // not legal.
        LOG.error("*BAD UOW USAGE* You are modifying a session registered entity ({})[{}] inside an ongoing UOW.\n"
                + "You should only work on entities copies you obtain using the "
                + "backendController.cloneInUnitOfWork(...) method.\n" + "The property being modified is [{}].",
            targetEntity, getComponentContract(targetEntity).getSimpleName(), propertyDescriptor.getName());
        if (isThrowExceptionOnBadUsage()) {
          throw new BackendException(
              "An invalid modification on a session entity has been detected while having an active Unit of Work. "
                  + "Please check the logs.");
        }
      }
      if (paramEntity != null && objectEquals(paramEntity, sessionParamEntity)) {
        // We are linking an entity with a session entity inside a unit of work.
        // This is not legal.
        LOG.error(
            "*BAD UOW USAGE* You are linking an entity ({})[{}] with a session entity ({})[{}] inside an ongoing UOW.\n"
                + "You should only work on entities copies you obtain using the "
                + "backendController.cloneInUnitOfWork(...) method\n" + "The property being modified is [{}].", target,
            targetClass.getSimpleName(), paramEntity, getComponentContract(paramEntity).getSimpleName(),
            propertyDescriptor.getName());
        if (isThrowExceptionOnBadUsage()) {
          throw new BackendException(
              "An invalid usage of a session entity has been detected while having an active Unit of Work. "
                  + "Please check the logs.");
        }
      }
    } else {
      if (targetEntity != null && !objectEquals(targetEntity, sessionTargetEntity)) {
        // We are working on an entity that has not been registered in the
        // session. This is not legal.
        LOG.error(
            "*BAD SESSION USAGE* You are modifying an entity ({})[{}] that has not been previously merged in the "
                + "session.\n" + "You should 1st merge your entities in the session by using the "
                + "backendController.merge(...) method.\n" + "The property being modified is [{}].", targetEntity,
            getComponentContract(targetEntity).getSimpleName(), propertyDescriptor.getName());
        if (isThrowExceptionOnBadUsage()) {
          throw new BackendException(
              "An invalid modification of an entity that was not previously registered in the session has been "
                  + "detected. " + "Please check the logs.");
        }
      }
      if (paramEntity != null && !objectEquals(paramEntity, sessionParamEntity)) {
        // We are linking an entity with another one that has not been
        // registered in the session. This is not legal.
        LOG.error("*BAD SESSION USAGE* You are linking an entity ({})[{}] with another one ({})[{}] "
                + "that has not been previously merged in the session.\n"
                + "You should 1st merge your entities in the session by using the "
                + "backendController.merge(...) method.\n" + "The property being modified is [{}].", target,
            targetClass.getSimpleName(), paramEntity, getComponentContract(paramEntity).getSimpleName(),
            propertyDescriptor.getName());
        if (isThrowExceptionOnBadUsage()) {
          throw new BackendException(
              "An invalid usage of an entity that was not previously registered in the session has been detected. "
                  + "Please check the logs.");
        }
      }
    }
    return param;
  }

  private IEntity refineEntity(IComponent target) {
    return refineEntity(target, new IdentityHashMap<IComponent, Object>());
  }

  private IEntity refineEntity(IComponent target, IdentityHashMap<IComponent, Object> traversed) {
    if (traversed.containsKey(target)) {
      return null;
    }
    traversed.put(target, null);
    if (target instanceof IEntity) {
      return (IEntity) target;
    }
    if (target != null) {
      return refineEntity(target.getOwningComponent(), traversed);
    }
    return null;
  }

  /**
   * Checks object equality between 2 entities ignoring any implementation
   * details like proxy optimisation.
   *
   * @param e1
   *     the 1st entity.
   * @param e2
   *     the 2nd entity.
   * @return true if both entity are object equal.
   */
  protected boolean objectEquals(IEntity e1, IEntity e2) {
    return e1 == e2;
  }

  /**
   * Hook to allow subclasses to determine component contract without
   * initializing it.
   *
   * @param <E>
   *     the actual component type.
   * @param component
   *     the component to get the component contract for.
   * @return the component contract.
   */
  @SuppressWarnings("unchecked")
  protected <E extends IComponent> Class<? extends E> getComponentContract(E component) {
    return (Class<? extends E>) component.getComponentContract();
  }

  /**
   * Gets the slaveControllerFactory.
   *
   * @return the slaveControllerFactory.
   */
  protected IBackendControllerFactory getSlaveControllerFactory() {
    return slaveControllerFactory;
  }

  /**
   * Sets the slaveControllerFactory.
   *
   * @param slaveControllerFactory
   *     the slaveControllerFactory to set.
   */
  public void setSlaveControllerFactory(IBackendControllerFactory slaveControllerFactory) {
    this.slaveControllerFactory = slaveControllerFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<AsyncActionExecutor> getRunningExecutors() {
    if (controllerAsyncActionsThreadGroup != null) {
      int activeCount = controllerAsyncActionsThreadGroup.activeCount();
      AsyncActionExecutor[] activeExecutors = new AsyncActionExecutor[activeCount];
      controllerAsyncActionsThreadGroup.enumerate(activeExecutors);
      return new LinkedHashSet<>(Arrays.asList(activeExecutors));
    }
    return Collections.emptySet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<AsyncActionExecutor> getCompletedExecutors() {
    Set<AsyncActionExecutor> completedExecutors = new LinkedHashSet<>(asyncExecutors);
    completedExecutors.removeAll(getRunningExecutors());
    return completedExecutors;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void purgeCompletedExecutors() {
    Set<AsyncActionExecutor> oldValue = new LinkedHashSet<>(getCompletedExecutors());
    asyncExecutors.removeAll(getCompletedExecutors());
    firePropertyChange("completedExecutors", oldValue, getCompletedExecutors());
  }

  /**
   * Gets the asyncExecutorsMaxCount.
   *
   * @param context
   *     the action context.
   * @return the asyncExecutorsMaxCount.
   */
  @SuppressWarnings("UnusedParameters")
  protected int getAsyncExecutorsMaxCount(Map<String, Object> context) {
    return asyncExecutorsMaxCount;
  }

  /**
   * Configures the maximum count of concurrent asynchronous action executors.
   * It defaults to {@code 10}.
   *
   * @param asyncExecutorsMaxCount
   *     the asyncExecutorsMaxCount to set.
   */
  public void setAsyncExecutorsMaxCount(int asyncExecutorsMaxCount) {
    this.asyncExecutorsMaxCount = asyncExecutorsMaxCount;
  }

  /**
   * Creates an entity registry.
   *
   * @param name
   *     the entity registry name.
   * @return a new entity registry.
   */
  protected final IEntityRegistry createEntityRegistry(String name) {
    return createEntityRegistry(name, new HashMap<Class<? extends IEntity>, Map<Serializable, IEntity>>());
  }

  /**
   * Creates an entity registry.
   *
   * @param name      the entity registry name.
   * @param backingStore the backing store
   * @return a new entity registry.
   */
  protected IEntityRegistry createEntityRegistry(String name, Map<Class<? extends IEntity>,
      Map<Serializable, IEntity>> backingStore) {
    return new BasicEntityRegistry(name, backingStore);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void suspend() {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void resume() {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void flush() {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void beforeCommit(boolean readOnly) {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void beforeCompletion() {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void afterCommit() {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void afterCompletion(int status) {
    if (status == STATUS_COMMITTED) {
      commitUnitOfWork();
    } else {
      rollbackUnitOfWork();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDirtyTrackingEnabled() {
    // Since both session and UOW dirty tracker are synchronized, we can only query session one.
    return sessionUnitOfWork.isDirtyTrackingEnabled();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDirtyTrackingEnabled(boolean enabled) {
    // Both session AND UOW dirty tracker should be disabled at the same time. See bug #jspresso-ce-21.
    sessionUnitOfWork.setDirtyTrackingEnabled(enabled);
    if (isUnitOfWorkActive()) {
      unitOfWork.setDirtyTrackingEnabled(enabled);
    }
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public void addDirtInterceptor(PropertyChangeListener interceptor) {
    if (isUnitOfWorkActive()) {
      unitOfWork.addDirtInterceptor(interceptor);
    } else {
      sessionUnitOfWork.addDirtInterceptor(interceptor);
    }
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public void removeDirtInterceptor(PropertyChangeListener interceptor) {
    if (isUnitOfWorkActive()) {
      unitOfWork.removeDirtInterceptor(interceptor);
    } else {
      sessionUnitOfWork.removeDirtInterceptor(interceptor);
    }
  }

  /**
   * Suspend unit of work.
   */
  protected void suspendUnitOfWork() {
    unitOfWork.suspend();
  }

  /**
   * Resume unit of work.
   */
  protected void resumeUnitOfWork() {
    unitOfWork.resume();
  }

  /**
   * Delegates to the master controller if any.
   *
   * @param action
   *     the action
   * @param context
   *     the context
   */
  @Override
  public synchronized void executeLater(IAction action, Map<String, Object> context) {
    if (masterController != null) {
      masterController.executeLater(action, context);
    } else {
      super.executeLater(action, context);
    }
  }

  private List<IEntity> recordedMergedEntities;

  /**
   * Record uow merged entities for later reuse.
   */
  public void recordUowMergedEntities() {
    recordedMergedEntities = new ArrayList<>();
  }

  /**
   * Gets recorded uow merged entities.
   *
   * @return the recorded uow merged entities
   */
  public List<IEntity> getRecordedUowMergedEntitiesAndClear() {
    List<IEntity> copy = recordedMergedEntities;
    recordedMergedEntities = null;
    return copy;
  }

  /**
   * Sets async actions thread group.
   *
   * @param asyncActionsThreadGroup
   *     the async actions thread group
   */
  public void setAsyncActionsThreadGroup(ThreadGroup asyncActionsThreadGroup) {
    this.asyncActionsThreadGroup = asyncActionsThreadGroup;
  }
}
