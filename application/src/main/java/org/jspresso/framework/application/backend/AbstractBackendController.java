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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.security.auth.Subject;

import org.apache.commons.collections.map.LRUMap;
import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.application.AbstractController;
import org.jspresso.framework.application.backend.action.Transactional;
import org.jspresso.framework.application.backend.entity.ControllerAwareProxyEntityFactory;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.application.backend.session.IEntityUnitOfWork;
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
import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.security.ISecurityContextBuilder;
import org.jspresso.framework.security.SecurityHelper;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.bean.BeanPropertyChangeRecorder;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.util.preferences.IPreferencesStore;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Base class for backend application controllers. Backend controllers are
 * responsible for :
 * <ul>
 * <li>keeping a reference to the application session</li>
 * <li>keeping a reference to the application workspaces and their state</li>
 * <li>keeping a reference to the application clipboard</li>
 * <li>keeping a reference to the entity registry that garantees the in-memory
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
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractBackendController extends AbstractController
    implements IBackendController {

  private IApplicationSession                              applicationSession;
  private IEntityCloneFactory                              carbonEntityCloneFactory;
  private IComponentCollectionFactory<IComponent>          collectionFactory;
  private BeanPropertyChangeRecorder                       dirtRecorder;

  private IEntityFactory                                   entityFactory;

  private IEntityRegistry                                  entityRegistry;
  private IModelConnectorFactory                           modelConnectorFactory;
  private TransactionTemplate                              transactionTemplate;
  private ComponentTransferStructure<? extends IComponent> transferStructure;
  private IEntityUnitOfWork                                unitOfWork;

  private Map<String, IValueConnector>                     workspaceConnectors;
  private LRUMap                                           moduleConnectors;

  private IPreferencesStore                                userPreferencesStore;
  private ITranslationProvider                             translationProvider;

  private ISecurityPlugin                                  customSecurityPlugin;
  private ISecurityContextBuilder                          securityContextBuilder;

  private ITranslationPlugin                               customTranslationPlugin;

  private TimeZone                                         clientTimeZone;

  /**
   * Constructs a new <code>AbstractBackendController</code> instance.
   */
  protected AbstractBackendController() {
    dirtRecorder = new BeanPropertyChangeRecorder();
    // moduleConnectors = new HashMap<Module, IValueConnector>();
    moduleConnectors = new LRUMap(20);
    securityContextBuilder = new SecurityContextBuilder();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void joinTransaction() {
    if (!unitOfWork.isActive()) {
      beginUnitOfWork();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void beginUnitOfWork() {
    if (unitOfWork.isActive()) {
      throw new BackendException(
          "Cannot begin a new unit of work. Another one is already active.");
    }
    unitOfWork.begin();
  }

  /**
   * Clears the pending operations.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void clearPendingOperations() {
    unitOfWork.clearPendingOperations();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <E extends IEntity> E cloneInUnitOfWork(E entity) {
    if (!unitOfWork.isActive()) {
      throw new BackendException(
          "Cannot use a unit of work that has not begun.");
    }
    return cloneInUnitOfWork(Collections.singletonList(entity)).get(0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <E extends IEntity> List<E> cloneInUnitOfWork(List<E> entities) {
    if (!unitOfWork.isActive()) {
      throw new BackendException(
          "Cannot use a unit of work that has not begun.");
    }
    List<E> uowEntities = new ArrayList<E>();
    // Map<Class<?>, Map<Serializable, IEntity>> alreadyCloned = new
    // HashMap<Class<?>, Map<Serializable, IEntity>>();
    Map<Class<?>, Map<Serializable, IEntity>> alreadyCloned = unitOfWork
        .getRegisteredEntities();
    for (E entity : entities) {
      uowEntities.add(cloneInUnitOfWork(entity, alreadyCloned));
    }
    return uowEntities;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void commitUnitOfWork() {
    if (!unitOfWork.isActive()) {
      throw new BackendException(
          "Cannot commit a unit of work that has not begun.");
    }
    try {
      Map<IEntity, IEntity> alreadyMerged = new HashMap<IEntity, IEntity>();
      if (unitOfWork.getUpdatedEntities() != null) {
        for (IEntity entityToMergeBack : unitOfWork.getUpdatedEntities()) {
          merge(entityToMergeBack, EMergeMode.MERGE_CLEAN_LAZY, alreadyMerged);
        }
      }
    } finally {
      unitOfWork.commit();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IValueConnector createModelConnector(String id,
      IModelDescriptor modelDescriptor) {
    return modelConnectorFactory
        .createModelConnector(id, modelDescriptor, this);
  }

  /**
   * Directly delegates execution to the action after having completed its
   * execution context with the controller's initial context.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(final IAction action, final Map<String, Object> context) {
    if (action == null) {
      return true;
    }
    if (!action.isBackend()) {
      throw new ActionException(
          "The backend controller is executing a frontend action. Please check the action chaining : "
              + action.toString());
    }
    // Should be handled before getting there.
    // checkAccess(action);
    Map<String, Object> actionContext = getInitialActionContext();
    if (context != null) {
      context.putAll(actionContext);
    }
    if (action.getClass().isAnnotationPresent(Transactional.class)) {
      Boolean ret = getTransactionTemplate().execute(
          new TransactionCallback<Boolean>() {

            @Override
            public Boolean doInTransaction(TransactionStatus status) {
              boolean executionStatus = action.execute(
                  AbstractBackendController.this, context);
              if (!executionStatus) {
                status.setRollbackOnly();
              }
              return new Boolean(executionStatus);
            }
          });
      return ret.booleanValue();
    }
    return action.execute(this, context);
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
    Map<String, Object> dirtyProperties;
    if (unitOfWork.isActive()) {
      dirtyProperties = unitOfWork.getDirtyProperties(entity);
    } else {
      dirtyProperties = dirtRecorder.getChangedProperties(entity);
    }
    if (dirtyProperties != null) {
      for (Iterator<Map.Entry<String, Object>> ite = dirtyProperties.entrySet()
          .iterator(); ite.hasNext();) {
        Map.Entry<String, Object> property = ite.next();
        Object propertyValue = property.getValue();
        Object currentProperty = entity.straightGetProperty(property.getKey());
        if ((currentProperty != null
            && !(currentProperty instanceof Collection) && currentProperty
              .equals(property.getValue()))
            || (currentProperty == null && propertyValue == null)) {
          // Unfortunately, we cannot ignore collections that have been
          // changed but reset to their original state. This prevents the entity
          // to be merged back into the session while the session state might be
          // wrong.
          clearPropertyDirtyState(currentProperty);
          ite.remove(); // actually removes the mapping from the map.
        }
      }
    }
    return dirtyProperties;
  }

  /**
   * Resets the property technical dirty state. Gives a chance to subclasses to
   * reset technical dirty state. Useful in Hibernate for resetting collection
   * dirty states when their state is identical to the original one after
   * several modifications.
   * 
   * @param property
   *          the property to reset the dirty state for.
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
   * <p>
   * {@inheritDoc}
   */
  @Override
  public Map<String, Object> getInitialActionContext() {
    Map<String, Object> initialActionContext = new HashMap<String, Object>();
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
  public IEntity getRegisteredEntity(Class<? extends IEntity> entityContract,
      Object entityId) {
    return entityRegistry.get(entityContract, entityId);
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
    Map<Module, IValueConnector> buff = new LinkedHashMap<Module, IValueConnector>();
    buff.putAll(moduleConnectors);
    moduleConnectors.clear();
    moduleConnectors.putAll(buff);
    IValueConnector moduleConnector = (IValueConnector) moduleConnectors
        .get(module);
    if (moduleConnector == null) {
      moduleConnector = createModelConnector(module.getName(),
          ModuleDescriptor.MODULE_DESCRIPTOR);
      moduleConnectors.put(module, moduleConnector);
    }
    moduleConnector.setConnectorValue(module);
    return moduleConnector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializePropertyIfNeeded(IComponent componentOrEntity,
      String propertyName) {
    Object propertyValue = componentOrEntity.straightGetProperty(propertyName);
    if (propertyValue instanceof Collection<?>) {
      for (Iterator<?> ite = ((Collection<?>) propertyValue).iterator(); ite
          .hasNext();) {
        Object collectionElement = ite.next();
        if (collectionElement instanceof IEntity) {
          if (isEntityRegisteredForDeletion((IEntity) collectionElement)) {
            ite.remove();
          }
        }
      }
    }
  }

  /**
   * Sets the model controller workspaces. These workspaces are not kept as-is.
   * Their connectors are.
   * 
   * @param workspaces
   *          A map containing the workspaces indexed by a well-known key used
   *          to bind them with their views.
   */
  @Override
  public void installWorkspaces(Map<String, Workspace> workspaces) {
    workspaceConnectors = new HashMap<String, IValueConnector>();
    for (Map.Entry<String, Workspace> workspaceEntry : workspaces.entrySet()) {
      String workspaceName = workspaceEntry.getKey();
      Workspace workspace = workspaceEntry.getValue();
      IModelDescriptor workspaceDescriptor;
      workspaceDescriptor = WorkspaceDescriptor.WORKSPACE_DESCRIPTOR;
      IValueConnector nextWorkspaceConnector = modelConnectorFactory
          .createModelConnector(workspaceName, workspaceDescriptor, this);
      nextWorkspaceConnector.setConnectorValue(workspace);
      workspaceConnectors.put(workspaceName, nextWorkspaceConnector);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAnyDirtyInDepth(Collection<?> elements) {
    Set<IEntity> alreadyTraversed = new HashSet<IEntity>();
    if (elements != null) {
      for (Object element : elements) {
        if (element instanceof IEntity) {
          if (isDirtyInDepth((IEntity) element, alreadyTraversed)) {
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
    return isAnyDirtyInDepth(Collections.singleton(entity));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEntityRegisteredForDeletion(IEntity entity) {
    return unitOfWork.isEntityRegisteredForDeletion(entity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEntityRegisteredForUpdate(IEntity entity) {
    return unitOfWork.isEntityRegisteredForUpdate(entity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInitialized(@SuppressWarnings("unused") Object objectOrProxy) {
    return true;
  }

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
    if (!unitOfWork.isActive()) {
      throw new BackendException("Cannot access unit of work.");
    }
    return unitOfWork.isUpdated(entity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <E extends IEntity> E merge(E entity, EMergeMode mergeMode) {
    return merge(entity, mergeMode, new HashMap<IEntity, IEntity>());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <E extends IEntity> List<E> merge(List<E> entities,
      EMergeMode mergeMode) {
    Map<IEntity, IEntity> alreadyMerged = new HashMap<IEntity, IEntity>();
    List<E> mergedList = new ArrayList<E>();
    for (E entity : entities) {
      mergedList.add(merge(entity, mergeMode, alreadyMerged));
    }
    return mergedList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void recordAsSynchronized(IEntity flushedEntity) {
    if (unitOfWork.isActive()) {
      boolean isDirty = isDirty(flushedEntity);
      unitOfWork.clearDirtyState(flushedEntity);
      if (isDirty) {
        unitOfWork.addUpdatedEntity(flushedEntity);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerEntity(IEntity entity, boolean isEntityTransient) {
    if (!unitOfWork.isActive()) {
      entityRegistry.register(entity);
      Map<String, Object> initialDirtyProperties = null;
      if (isEntityTransient) {
        initialDirtyProperties = new HashMap<String, Object>();
        for (Map.Entry<String, Object> property : entity
            .straightGetProperties().entrySet()) {
          String propertyName = property.getKey();
          Object propertyValue = property.getValue();
          if (propertyValue != null
              && !(propertyValue instanceof Collection<?> && ((Collection<?>) property
                  .getValue()).isEmpty())) {
            initialDirtyProperties.put(propertyName, null);
          }
        }
      }
      dirtRecorder.register(entity, initialDirtyProperties);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerForDeletion(IEntity entity) {
    unitOfWork.registerForDeletion(entity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerForUpdate(IEntity entity) {
    unitOfWork.registerForUpdate(entity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComponentTransferStructure<? extends IComponent> retrieveComponents() {
    return transferStructure;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void rollbackUnitOfWork() {
    if (!unitOfWork.isActive()) {
      throw new BackendException(
          "Cannot rollback a unit of work that has not begun.");
    }
    unitOfWork.rollback();
  }

  /**
   * Assigns the application session to this backend controller. This property
   * can only be set once and should only be used by the DI container. It will
   * rarely be changed from built-in defaults unless you need to specify a
   * custom implementation instance to be used.
   * 
   * @param applicationSession
   *          the applicationSession to set.
   */
  public void setApplicationSession(IApplicationSession applicationSession) {
    if (this.applicationSession != null) {
      throw new IllegalArgumentException(
          "application session can only be configured once.");
    }
    this.applicationSession = applicationSession;
  }

  /**
   * Configures the entity clone factory used to carbon-copy entities. An entity
   * carbon-copy is an technical copy of an entity, including id and version but
   * excluding relationhip properties. This mecanism is used by the controller
   * when duplicating entities into the UOW to allow for memory state aware
   * transactions. This property should only be used by the DI container. It
   * will rarely be changed from built-in defaults unless you need to specify a
   * custom implementation instance to be used.
   * 
   * @param carbonEntityCloneFactory
   *          the carbonEntityCloneFactory to set.
   */
  public void setCarbonEntityCloneFactory(
      IEntityCloneFactory carbonEntityCloneFactory) {
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
   *          the collectionFactory to set.
   */
  public void setCollectionFactory(
      IComponentCollectionFactory<IComponent> collectionFactory) {
    this.collectionFactory = collectionFactory;
  }

  /**
   * Configures the entity factory to use to create new entities. Backend
   * controllers only accept instances of
   * <code>ControllerAwareProxyEntityFactory</code> or a subclass. This is
   * because the backend controller must keep track of created entities.
   * Jspresso entity implementations also use the controller from which they
   * were created behind the scene.
   * 
   * @param entityFactory
   *          the entityFactory to set.
   */
  public void setEntityFactory(IEntityFactory entityFactory) {
    if (!(entityFactory instanceof ControllerAwareProxyEntityFactory)) {
      throw new IllegalArgumentException(
          "entityFactory must be a ControllerAwareProxyEntityFactory.");
    }
    this.entityFactory = entityFactory;
  }

  /**
   * Configures the entity registry to be used in this controller. The role of
   * the entity registry is to garantee that an entity will be represented by at
   * most 1 instance in the user application session. This property can only be
   * set once and should only be used by the DI container. It will rarely be
   * changed from built-in defaults unless you need to specify a custom
   * implementation instance to be used.
   * 
   * @param entityRegistry
   *          the entityRegistry to set.
   */
  public void setEntityRegistry(IEntityRegistry entityRegistry) {
    if (this.entityRegistry != null) {
      throw new IllegalArgumentException(
          "entityRegistry session can only be configured once.");
    }
    this.entityRegistry = entityRegistry;
  }

  /**
   * Configures the model connector factory to use to create new model
   * connectors. Connectors are adapters used by the binding layer to access
   * domain model values.
   * 
   * @param modelConnectorFactory
   *          the modelConnectorFactory to set.
   */
  public void setModelConnectorFactory(
      IModelConnectorFactory modelConnectorFactory) {
    this.modelConnectorFactory = modelConnectorFactory;
  }

  /**
   * Assigns the Spring transaction template to this backend controller. This
   * property can only be set once and should only be used by the DI container.
   * It will rarely be changed from built-in defaults unless you need to specify
   * a custom implementation instance to be used.
   * <p>
   * The configured instance is the one that will be returned by the
   * controller's <code>getTransactionTemplate()</code> method that should be
   * used by the service layer for transaction management.
   * 
   * @param transactionTemplate
   *          the transactionTemplate to set.
   */
  public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
    if (this.transactionTemplate != null) {
      throw new IllegalArgumentException(
          "Spring transaction template can only be configured once.");
    }
    this.transactionTemplate = transactionTemplate;
  }

  /**
   * Configures the &quot;Unit of Work&quot; implementation to be used by this
   * controller. The same UOW instance is reused (started, cleared) all along
   * the session. This property can only be set once and should only be used by
   * the DI container. It will rarely be changed from built-in defaults unless
   * you need to specify a custom implementation instance to be used.
   * 
   * @param unitOfWork
   *          the unitOfWork to set.
   */
  public void setUnitOfWork(IEntityUnitOfWork unitOfWork) {
    if (this.unitOfWork != null) {
      throw new IllegalArgumentException(
          "unitOfWork can only be configured once.");
    }
    this.unitOfWork = unitOfWork;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean start(Locale startingLocale, TimeZone theClientTimeZone) {
    applicationSession.setLocale(startingLocale);
    this.clientTimeZone = theClientTimeZone;
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean stop() {
    if (applicationSession != null) {
      applicationSession.clear();
    }
    if (dirtRecorder != null) {
      dirtRecorder.clear();
    }
    if (entityRegistry != null) {
      entityRegistry.clear();
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
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void storeComponents(
      ComponentTransferStructure<? extends IComponent> components) {
    this.transferStructure = components;
  }

  /**
   * Creates a transient collection instance, in respect to the type of
   * collection passed as parameter.
   * 
   * @param collection
   *          the collection to take the type from (List, Set, ...)
   * @return a transient collection instance with the same interface type as the
   *         parameter.
   */
  protected Collection<IComponent> createTransientEntityCollection(
      Collection<?> collection) {
    Collection<IComponent> uowEntityCollection = null;
    if (collection instanceof Set<?>) {
      uowEntityCollection = collectionFactory
          .createComponentCollection(Set.class);
    } else if (collection instanceof List<?>) {
      uowEntityCollection = collectionFactory
          .createComponentCollection(List.class);
    }
    return uowEntityCollection;
  }

  /**
   * Gets the entity dirt recorder. To be used by subclasses.
   * 
   * @return the entity dirt recorder.
   */
  protected BeanPropertyChangeRecorder getDirtRecorder() {
    return dirtRecorder;
  }

  /**
   * Gets the entities that are registered for deletion.
   * 
   * @return the entities that are registered for deletion.
   */
  protected Collection<IEntity> getEntitiesRegisteredForDeletion() {
    return unitOfWork.getEntitiesRegisteredForDeletion();
  }

  /**
   * Gets the entities that are registered for update.
   * 
   * @return the entities that are registered for update.
   */
  protected Collection<IEntity> getEntitiesRegisteredForUpdate() {
    return unitOfWork.getEntitiesRegisteredForUpdate();
  }

  /**
   * Gets wether the entity is dirty (has changes that need to be updated to the
   * persistent store).
   * 
   * @param entity
   *          the entity to test.
   * @return true if the entity is dirty.
   */
  @Override
  public boolean isDirty(IEntity entity) {
    if (entity == null) {
      return false;
    }
    Map<String, Object> entityDirtyProperties = getDirtyProperties(entity);
    IComponentDescriptor<?> entityDescriptor = getEntityFactory()
        .getComponentDescriptor(entity.getComponentContract());
    if (entityDirtyProperties != null) {
      entityDirtyProperties.remove(IEntity.VERSION);
      for (Map.Entry<String, Object> property : entityDirtyProperties
          .entrySet()) {
        String propertyName = property.getKey();
        IPropertyDescriptor propertyDescriptor = entityDescriptor
            .getPropertyDescriptor(propertyName);
        if (propertyDescriptor != null && !propertyDescriptor.isComputed()) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Gets wether the entity property is dirty (has changes that need to be
   * updated to the persistent store).
   * 
   * @param entity
   *          the entity to test.
   * @param propertyName
   *          the entity property to test.
   * @return true if the entity is dirty.
   */
  @Override
  public boolean isDirty(IEntity entity, String propertyName) {
    if (entity == null) {
      return false;
    }
    Map<String, Object> entityDirtyProperties = getDirtyProperties(entity);
    if (entityDirtyProperties != null
        && entityDirtyProperties.containsKey(propertyName)) {
      IPropertyDescriptor propertyDescriptor = getEntityFactory()
          .getComponentDescriptor(entity.getComponentContract())
          .getPropertyDescriptor(propertyName);
      return propertyDescriptor != null && !propertyDescriptor.isComputed();
    }
    return false;
  }

  /**
   * Gives a chance to the session to wrap a collection before making it part of
   * the unit of work.
   * 
   * @param owner
   *          the entity the collection belongs to.
   * @param transientCollection
   *          the transient collection to make part of the unit of work.
   * @param snapshotCollection
   *          the original collection state as reported by the dirt recorder.
   * @param role
   *          the name of the property represented by the collection in its
   *          owner.
   * @return the wrapped collection if any (it may be the collection itself as
   *         in this implementation).
   */
  protected Collection<IComponent> wrapDetachedCollection(IEntity owner,
      Collection<IComponent> transientCollection,
      Collection<IComponent> snapshotCollection, String role) {
    return transientCollection;
  }

  private void cleanDirtyProperties(IEntity entity) {
    dirtRecorder.resetChangedProperties(entity, null);
  }

  private IComponent cloneComponentInUnitOfWork(IComponent component,
      Map<Class<?>, Map<Serializable, IEntity>> alreadyCloned) {
    IComponent uowComponent = carbonEntityCloneFactory.cloneComponent(
        component, entityFactory);
    Map<String, Object> componentProperties = component.straightGetProperties();
    for (Map.Entry<String, Object> property : componentProperties.entrySet()) {
      String propertyName = property.getKey();
      Object propertyValue = property.getValue();
      if (propertyValue instanceof IEntity) {
        if (isInitialized(propertyValue)) {
          uowComponent.straightSetProperty(propertyName,
              cloneInUnitOfWork((IEntity) propertyValue, alreadyCloned));
        } else {
          uowComponent.straightSetProperty(propertyName,
              cloneUninitializedProperty(uowComponent, propertyValue));
        }
      } else if (propertyValue instanceof IComponent) {
        uowComponent.straightSetProperty(
            propertyName,
            cloneComponentInUnitOfWork((IComponent) propertyValue,
                alreadyCloned));
      }
    }
    return uowComponent;
  }

  @SuppressWarnings("unchecked")
  private <E extends IEntity> E cloneInUnitOfWork(E entity,
      Map<Class<?>, Map<Serializable, IEntity>> alreadyCloned) {
    Map<Serializable, IEntity> contractBuffer = alreadyCloned.get(entity
        .getComponentContract());
    IComponentDescriptor<?> entityDescriptor = getEntityFactory()
        .getComponentDescriptor(entity.getComponentContract());
    E uowEntity = null;
    if (contractBuffer == null) {
      contractBuffer = new HashMap<Serializable, IEntity>();
      alreadyCloned.put(entity.getComponentContract(), contractBuffer);
    } else {
      uowEntity = (E) contractBuffer.get(entity.getId());
      if (uowEntity != null) {
        return uowEntity;
      }
    }
    uowEntity = performUowEntityCloning(entity);
    Map<String, Object> dirtyProperties = dirtRecorder
        .getChangedProperties(entity);
    if (dirtyProperties == null) {
      dirtyProperties = new HashMap<String, Object>();
    }
    contractBuffer.put(entity.getId(), uowEntity);
    Map<String, Object> entityProperties = entity.straightGetProperties();
    for (Map.Entry<String, Object> property : entityProperties.entrySet()) {
      String propertyName = property.getKey();
      Object propertyValue = property.getValue();
      IPropertyDescriptor propertyDescriptor = entityDescriptor
          .getPropertyDescriptor(propertyName);
      if (propertyValue instanceof IEntity) {
        if (isInitialized(propertyValue)) {
          uowEntity.straightSetProperty(propertyName,
              cloneInUnitOfWork((IEntity) propertyValue, alreadyCloned));
        } else {
          uowEntity.straightSetProperty(propertyName,
              cloneUninitializedProperty(uowEntity, propertyValue));
        }
      } else if (propertyValue instanceof Collection<?>
          && propertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
        if (isInitialized(propertyValue)) {
          Collection<IComponent> uowCollection = createTransientEntityCollection((Collection<IComponent>) property
              .getValue());
          for (IComponent collectionElement : (Collection<IComponent>) property
              .getValue()) {
            if (collectionElement instanceof IEntity) {
              uowCollection.add(cloneInUnitOfWork((IEntity) collectionElement,
                  alreadyCloned));
            } else {
              uowCollection.add(cloneComponentInUnitOfWork(collectionElement,
                  alreadyCloned));
            }
          }
          if (!propertyDescriptor.isComputed()) {
            Collection<IComponent> snapshotCollection = (Collection<IComponent>) dirtyProperties
                .get(propertyName);
            if (snapshotCollection != null) {
              Collection<IComponent> clonedSnapshotCollection = createTransientEntityCollection(snapshotCollection);
              for (IComponent snapshotCollectionElement : snapshotCollection) {
                if (snapshotCollectionElement instanceof IEntity) {
                  clonedSnapshotCollection.add(cloneInUnitOfWork(
                      (IEntity) snapshotCollectionElement, alreadyCloned));
                } else {
                  clonedSnapshotCollection.add(cloneComponentInUnitOfWork(
                      snapshotCollectionElement, alreadyCloned));
                }
              }
              snapshotCollection = clonedSnapshotCollection;
            }
            uowCollection = wrapDetachedCollection(entity, uowCollection,
                snapshotCollection, propertyName);
          }
          uowEntity.straightSetProperty(propertyName, uowCollection);
        } else {
          uowEntity.straightSetProperty(propertyName,
              cloneUninitializedProperty(uowEntity, propertyValue));
        }
      } else if (propertyValue instanceof IEntity[]) {
        IEntity[] uowArray = new IEntity[((IEntity[]) propertyValue).length];
        for (int i = 0; i < uowArray.length; i++) {
          uowArray[i] = cloneInUnitOfWork(((IEntity[]) propertyValue)[i],
              alreadyCloned);
        }
        uowEntity.straightSetProperty(propertyName, uowArray);
      } else if (propertyValue instanceof IComponent[]) {
        IComponent[] uowArray = new IComponent[((IComponent[]) property
            .getValue()).length];
        for (int i = 0; i < uowArray.length; i++) {
          uowArray[i] = cloneComponentInUnitOfWork(
              ((IComponent[]) propertyValue)[i], alreadyCloned);
        }
        uowEntity.straightSetProperty(propertyName, uowArray);
      } else if (propertyValue instanceof IComponent) {
        uowEntity.straightSetProperty(
            propertyName,
            cloneComponentInUnitOfWork((IComponent) propertyValue,
                alreadyCloned));
      }
    }
    unitOfWork
        .register(uowEntity, new HashMap<String, Object>(dirtyProperties));
    if (uowEntity instanceof ILifecycleCapable) {
      ((ILifecycleCapable) uowEntity).onLoad();
      ((ILifecycleCapable) uowEntity).onClone(entity);
    }
    return uowEntity;
  }

  /**
   * Performs the actual entity cloning in unit of work. Gives a chance to
   * subclasses to override and take a better decision than just a deep carbon
   * copy.
   * 
   * @param <E>
   *          the actusal entity type.
   * @param entity
   *          the source entity.
   * @return the cloned entity.
   */
  protected <E extends IEntity> E performUowEntityCloning(E entity) {
    E uowEntity = carbonEntityCloneFactory.cloneEntity(entity, entityFactory);
    return uowEntity;
  }

  private boolean isDirtyInDepth(IEntity entity, Set<IEntity> alreadyTraversed) {
    alreadyTraversed.add(entity);
    if (isDirty(entity)) {
      return true;
    }
    Map<String, Object> entityProps = entity.straightGetProperties();
    for (Map.Entry<String, Object> property : entityProps.entrySet()) {
      Object propertyValue = property.getValue();
      if (propertyValue instanceof IEntity) {
        if (isInitialized(propertyValue)
            && !alreadyTraversed.contains(propertyValue)) {
          if (isDirtyInDepth((IEntity) propertyValue, alreadyTraversed)) {
            return true;
          }
        }
      } else if (propertyValue instanceof Collection<?>) {
        if (isInitialized(propertyValue)) {
          for (Object elt : ((Collection<?>) propertyValue)) {
            if (elt instanceof IEntity && !alreadyTraversed.contains(elt)) {
              if (isDirtyInDepth((IEntity) elt, alreadyTraversed)) {
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  }

  @SuppressWarnings("unchecked")
  private <E extends IEntity> E merge(E entity, final EMergeMode mergeMode,
      Map<IEntity, IEntity> alreadyMerged) {
    if (entity == null) {
      return null;
    }
    if (alreadyMerged.containsKey(entity)) {
      return (E) alreadyMerged.get(entity);
    }
    boolean dirtRecorderWasEnabled = dirtRecorder.isEnabled();
    try {
      if (mergeMode != EMergeMode.MERGE_EAGER) {
        dirtRecorder.setEnabled(false);
      }
      E registeredEntity = (E) getRegisteredEntity(
          entity.getComponentContract(), entity.getId());
      boolean newlyRegistered = false;
      if (registeredEntity == null) {
        registeredEntity = carbonEntityCloneFactory.cloneEntity(entity,
            entityFactory);
        entityRegistry.register(registeredEntity);
        dirtRecorder.register(registeredEntity, null);
        newlyRegistered = true;
      } else if (mergeMode == EMergeMode.MERGE_KEEP) {
        alreadyMerged.put(entity, registeredEntity);
        return registeredEntity;
      }
      alreadyMerged.put(entity, registeredEntity);
      if (newlyRegistered
          || (mergeMode != EMergeMode.MERGE_CLEAN_LAZY && mergeMode != EMergeMode.MERGE_LAZY)
          || registeredEntity.getVersion() == null
          || !registeredEntity.getVersion().equals(entity.getVersion())) {
        if (mergeMode == EMergeMode.MERGE_CLEAN_EAGER
            || mergeMode == EMergeMode.MERGE_CLEAN_LAZY
            || mergeMode == EMergeMode.MERGE_LAZY) {
          cleanDirtyProperties(registeredEntity);
        }
        IComponentDescriptor<?> entityDescriptor = getEntityFactory()
            .getComponentDescriptor(entity.getComponentContract());
        Map<String, Object> entityProperties = entity.straightGetProperties();
        Map<String, Object> registeredEntityProperties = registeredEntity
            .straightGetProperties();
        Map<String, Object> mergedProperties = new HashMap<String, Object>();
        for (Map.Entry<String, Object> property : entityProperties.entrySet()) {
          String propertyName = property.getKey();
          Object propertyValue = property.getValue();
          IPropertyDescriptor propertyDescriptor = entityDescriptor
              .getPropertyDescriptor(propertyName);
          if (propertyValue instanceof IEntity) {
            if (mergeMode != EMergeMode.MERGE_CLEAN_EAGER
                && mergeMode != EMergeMode.MERGE_EAGER
                && !isInitialized(propertyValue)) {
              if (registeredEntityProperties.get(propertyName) == null) {
                mergedProperties.put(propertyName, propertyValue);
              }
            } else {
              Object registeredProperty = registeredEntityProperties
                  .get(propertyName);
              if (mergeMode == EMergeMode.MERGE_EAGER
                  && isInitialized(propertyValue)) {
                initializePropertyIfNeeded(registeredEntity, propertyName);
              }
              if (isInitialized(registeredProperty)) {
                mergedProperties.put(propertyName,
                    merge((IEntity) propertyValue, mergeMode, alreadyMerged));
              }
            }
          } else if (propertyValue instanceof Collection
          // to support collections stored as java serializable blob.
              && propertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
            if (mergeMode != EMergeMode.MERGE_CLEAN_EAGER
                && mergeMode != EMergeMode.MERGE_EAGER
                && !isInitialized(propertyValue)) {
              if (registeredEntityProperties.get(propertyName) == null) {
                mergedProperties.put(propertyName, propertyValue);
              }
            } else {
              Collection<IComponent> registeredCollection = (Collection<IComponent>) registeredEntityProperties
                  .get(propertyName);
              if (mergeMode == EMergeMode.MERGE_EAGER
                  && isInitialized(propertyValue)) {
                initializePropertyIfNeeded(registeredEntity, propertyName);
              }
              if (isInitialized(registeredCollection)) {
                if (propertyValue instanceof Set) {
                  registeredCollection = collectionFactory
                      .createComponentCollection(Set.class);
                } else if (propertyValue instanceof List) {
                  registeredCollection = collectionFactory
                      .createComponentCollection(List.class);
                }
                initializePropertyIfNeeded(entity, propertyName);
                for (IComponent collectionElement : (Collection<IComponent>) property
                    .getValue()) {
                  if (collectionElement instanceof IEntity) {
                    registeredCollection.add(merge((IEntity) collectionElement,
                        mergeMode, alreadyMerged));
                  } else {
                    registeredCollection.add(mergeComponent(collectionElement,
                        null, mergeMode, alreadyMerged));
                  }
                }
                if (registeredEntity.isPersistent()) {
                  Collection<IComponent> snapshotCollection = null;
                  Map<String, Object> dirtyProperties = getDirtyProperties(registeredEntity);
                  if (dirtyProperties != null) {
                    snapshotCollection = (Collection<IComponent>) dirtyProperties
                        .get(propertyName);
                  }
                  mergedProperties.put(
                      propertyName,
                      wrapDetachedCollection(registeredEntity,
                          registeredCollection, snapshotCollection,
                          propertyName));
                } else {
                  mergedProperties.put(propertyName, registeredCollection);
                }
              }
            }
          } else if (propertyValue instanceof IComponent) {
            IComponent registeredComponent = (IComponent) registeredEntityProperties
                .get(propertyName);
            mergedProperties.put(
                propertyName,
                mergeComponent((IComponent) propertyValue, registeredComponent,
                    mergeMode, alreadyMerged));
          } else {
            mergedProperties.put(propertyName, propertyValue);
          }
        }
        registeredEntity.straightSetProperties(mergedProperties);
      } else if (mergeMode == EMergeMode.MERGE_CLEAN_LAZY) {
        // version has not evolved but we must still reset dirty properties in
        // case only versionControl false properties have changed.
        cleanDirtyProperties(registeredEntity);
      }
      if (registeredEntity instanceof ILifecycleCapable) {
        ((ILifecycleCapable) registeredEntity).onLoad();
        ((ILifecycleCapable) registeredEntity).onClone(entity);
      }
      return registeredEntity;
    } finally {
      dirtRecorder.setEnabled(dirtRecorderWasEnabled);
    }
  }

  private IComponent mergeComponent(IComponent componentToMerge,
      IComponent registeredComponent, EMergeMode mergeMode,
      Map<IEntity, IEntity> alreadyMerged) {
    IComponent varRegisteredComponent = registeredComponent;
    if (componentToMerge == null) {
      return null;
    }
    if (varRegisteredComponent == null) {
      varRegisteredComponent = carbonEntityCloneFactory.cloneComponent(
          componentToMerge, entityFactory);
    } else if (mergeMode == EMergeMode.MERGE_KEEP) {
      return varRegisteredComponent;
    }
    Map<String, Object> componentPropertiesToMerge = componentToMerge
        .straightGetProperties();
    Map<String, Object> registeredComponentProperties = varRegisteredComponent
        .straightGetProperties();
    Map<String, Object> mergedProperties = new HashMap<String, Object>();
    for (Map.Entry<String, Object> property : componentPropertiesToMerge
        .entrySet()) {
      String propertyName = property.getKey();
      Object propertyValue = property.getValue();
      if (propertyValue instanceof IEntity) {
        if (mergeMode != EMergeMode.MERGE_CLEAN_EAGER
            && mergeMode != EMergeMode.MERGE_EAGER
            && !isInitialized(propertyValue)) {
          if (registeredComponentProperties.get(propertyName) == null) {
            mergedProperties.put(propertyName, propertyValue);
          }
        } else {
          Object registeredProperty = registeredComponentProperties
              .get(propertyName);
          if (mergeMode == EMergeMode.MERGE_EAGER
              && isInitialized(propertyValue)) {
            initializePropertyIfNeeded(registeredComponent, propertyName);
          }
          if (isInitialized(registeredProperty)) {
            mergedProperties.put(propertyName,
                merge((IEntity) propertyValue, mergeMode, alreadyMerged));
          }
        }
      } else if (propertyValue instanceof IComponent) {
        IComponent registeredSubComponent = (IComponent) registeredComponentProperties
            .get(propertyName);
        mergedProperties.put(
            propertyName,
            mergeComponent((IComponent) propertyValue, registeredSubComponent,
                mergeMode, alreadyMerged));
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
   *          the deleted entity or component.
   * @param dryRun
   *          set to true to simulate before actually doing it.
   * @throws IllegalAccessException
   *           whenever this kind of exception occurs.
   * @throws InvocationTargetException
   *           whenever this kind of exception occurs.
   * @throws NoSuchMethodException
   *           whenever this kind of exception occurs.
   */
  @Override
  public void cleanRelationshipsOnDeletion(IComponent component, boolean dryRun)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    Set<IComponent> clearedEntities = new HashSet<IComponent>();
    Map<IComponent, RuntimeException> integrityViolations = new HashMap<IComponent, RuntimeException>();
    cleanRelationshipsOnDeletion(component, dryRun, clearedEntities,
        integrityViolations);
    // Throw exceptions for entities that have not been cleared during the
    // process.
    for (Map.Entry<IComponent, RuntimeException> integrityViolation : integrityViolations
        .entrySet()) {
      if (!(clearedEntities.contains(integrityViolation.getKey()))) {
        throw integrityViolation.getValue();
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void cleanRelationshipsOnDeletion(IComponent componentOrProxy,
      boolean dryRun, Set<IComponent> clearedEntities,
      Map<IComponent, RuntimeException> integrityViolations)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    if (componentOrProxy == null) {
      return;
    }
    IComponent component;
    component = (IComponent) unwrapProxy(componentOrProxy);
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
      IComponentDescriptor<?> componentDescriptor = getEntityFactory()
          .getComponentDescriptor(component.getComponentContract());
      for (Map.Entry<String, Object> property : component
          .straightGetProperties().entrySet()) {
        String propertyName = property.getKey();
        Object propertyValue = property.getValue();
        if (propertyValue != null) {
          IPropertyDescriptor propertyDescriptor = componentDescriptor
              .getPropertyDescriptor(propertyName);
          if (propertyDescriptor instanceof IRelationshipEndPropertyDescriptor) {
            // force initialization of relationship property.
            getAccessorFactory().createPropertyAccessor(propertyName,
                component.getComponentContract()).getValue(component);
            if (propertyDescriptor instanceof IReferencePropertyDescriptor
                && propertyValue instanceof IEntity) {
              if (((IRelationshipEndPropertyDescriptor) propertyDescriptor)
                  .isComposition()) {
                cleanRelationshipsOnDeletion((IEntity) propertyValue, dryRun,
                    clearedEntities, integrityViolations);
              } else {
                if (((IRelationshipEndPropertyDescriptor) propertyDescriptor)
                    .getReverseRelationEnd() != null) {
                  IPropertyDescriptor reversePropertyDescriptor = ((IReferencePropertyDescriptor<?>) propertyDescriptor)
                      .getReverseRelationEnd();
                  if (!clearedEntities.contains(propertyValue)) {
                    try {
                      if (reversePropertyDescriptor instanceof IReferencePropertyDescriptor) {
                        if (dryRun) {
                          // manually trigger reverse relations preprocessors.
                          reversePropertyDescriptor.preprocessSetter(
                              propertyValue, null);
                        } else {
                          getAccessorFactory().createPropertyAccessor(
                              reversePropertyDescriptor.getName(),
                              ((IComponent) propertyValue)
                                  .getComponentContract()).setValue(
                              propertyValue, null);
                          // Technically reset to original value to avoid
                          // Hibernate not-null checks
                          component.straightSetProperty(propertyName,
                              propertyValue);
                        }
                      } else if (reversePropertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
                        if (dryRun) {
                          // manually trigger reverse relations preprocessors.
                          Collection<?> reverseCollection = (Collection<?>) getAccessorFactory()
                              .createPropertyAccessor(
                                  reversePropertyDescriptor.getName(),
                                  ((IComponent) propertyValue)
                                      .getComponentContract()).getValue(
                                  propertyValue);
                          ((ICollectionPropertyDescriptor<?>) reversePropertyDescriptor)
                              .preprocessRemover(propertyValue,
                                  reverseCollection, component);
                        } else {
                          getAccessorFactory()
                              .createCollectionPropertyAccessor(
                                  reversePropertyDescriptor.getName(),
                                  ((IComponent) propertyValue)
                                      .getComponentContract(),
                                  component.getComponentContract())
                              .removeFromValue(propertyValue, component);
                          // but technically reset to original value to avoid
                          // Hibernate not-null checks
                          component.straightSetProperty(propertyName,
                              propertyValue);
                        }
                      }
                    } catch (RuntimeException ex) {
                      integrityViolations.put((IComponent) propertyValue, ex);
                    }
                  }
                }
              }
            } else if (propertyDescriptor instanceof ICollectionPropertyDescriptor) {
              if (((ICollectionPropertyDescriptor<?>) propertyDescriptor)
                  .isComposition()) {
                for (IComponent composedEntity : new ArrayList<IComponent>(
                    (Collection<IComponent>) propertyValue)) {
                  cleanRelationshipsOnDeletion(composedEntity, dryRun,
                      clearedEntities, integrityViolations);
                }
              } else if (propertyDescriptor.isModifiable()
                  && !((Collection<?>) propertyValue).isEmpty()) {
                if (((ICollectionPropertyDescriptor<?>) propertyDescriptor)
                    .getReverseRelationEnd() != null) {
                  IPropertyDescriptor reversePropertyDescriptor = ((ICollectionPropertyDescriptor<?>) propertyDescriptor)
                      .getReverseRelationEnd();
                  for (IComponent collectionElement : new ArrayList<IComponent>(
                      (Collection<IComponent>) property.getValue())) {
                    if (!clearedEntities.contains(collectionElement)) {
                      try {
                        if (reversePropertyDescriptor instanceof IReferencePropertyDescriptor) {
                          if (dryRun) {
                            // manually trigger reverse relations preprocessors.
                            reversePropertyDescriptor.preprocessSetter(
                                collectionElement, null);
                          } else {
                            getAccessorFactory().createPropertyAccessor(
                                reversePropertyDescriptor.getName(),
                                collectionElement.getComponentContract())
                                .setValue(collectionElement, null);
                          }
                        } else if (reversePropertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
                          if (dryRun) {
                            // manually trigger reverse relations preprocessors.
                            Collection<?> reverseCollection = (Collection<?>) getAccessorFactory()
                                .createPropertyAccessor(
                                    reversePropertyDescriptor.getName(),
                                    collectionElement.getComponentContract())
                                .getValue(collectionElement);
                            ((ICollectionPropertyDescriptor<?>) reversePropertyDescriptor)
                                .preprocessRemover(collectionElement,
                                    reverseCollection, component);
                          } else {
                            getAccessorFactory()
                                .createCollectionPropertyAccessor(
                                    reversePropertyDescriptor.getName(),
                                    collectionElement.getComponentContract(),
                                    component.getComponentContract())
                                .removeFromValue(collectionElement, component);
                          }
                        }
                      } catch (RuntimeException ex) {
                        integrityViolations.put(collectionElement, ex);
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
   *          the component or proxy.
   * @return the proxy implementation if it's an ORM proxy.
   */
  protected abstract Object unwrapProxy(Object componentOrProxy);

  /**
   * Clones an uninitialized (proxied) property.
   * 
   * @param owner
   *          the property owner.
   * @param propertyValue
   *          the propertyValue.
   * @return the property clone.
   */
  protected Object cloneUninitializedProperty(Object owner, Object propertyValue) {
    return propertyValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void loggedIn(Subject subject) {
    getApplicationSession().setSubject(subject);

    String userPreferredLanguageCode = (String) getApplicationSession()
        .getPrincipal().getCustomProperty(UserPrincipal.LANGUAGE_PROPERTY);
    if (userPreferredLanguageCode != null) {
      getApplicationSession().setLocale(new Locale(userPreferredLanguageCode));
    }
    if (getUserPreferencesStore() != null) {
      getUserPreferencesStore().setStorePath(new String[] {
        /* getName(), */getApplicationSession().getPrincipal().getName()
      });
    }

  }

  /**
   * Reads a user preference.
   * 
   * @param key
   *          the key under which the preference as been stored.
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
   *          the key under which the preference as to be stored.
   * @param value
   *          the value of the preference to be stored.
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
   *          the key under which the preference is stored.
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
   *          the userPreferenceStore to set.
   */
  public void setUserPreferencesStore(IPreferencesStore userPreferencesStore) {
    this.userPreferencesStore = userPreferencesStore;
  }

  /**
   * Configures the translation provider used to compute internationalized
   * messages and labels.
   * 
   * @param translationProvider
   *          the translationProvider to set.
   */
  public void setTranslationProvider(ITranslationProvider translationProvider) {
    this.translationProvider = translationProvider;
  }

  /**
   * Delegates to the translation provider.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public String getTranslation(String key, Locale locale) {
    if (customTranslationPlugin != null) {
      String translation = customTranslationPlugin.getTranslation(key, locale,
          getApplicationSession());
      if (translation != null) {
        return translation;
      }
    }
    return translationProvider.getTranslation(key, locale);
  }

  /**
   * Delegates to the translation provider.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public String getTranslation(String key, Object[] args, Locale locale) {
    if (customTranslationPlugin != null) {
      String translation = customTranslationPlugin.getTranslation(key, args,
          locale, getApplicationSession());
      if (translation != null) {
        return translation;
      }
    }
    return translationProvider.getTranslation(key, args, locale);
  }

  /**
   * Delegates to the translation provider.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public String getTranslation(String key, String defaultMessage, Locale locale) {
    if (customTranslationPlugin != null) {
      String translation = customTranslationPlugin.getTranslation(key, locale,
          getApplicationSession());
      if (translation != null) {
        return translation;
      }
    }
    return translationProvider.getTranslation(key, defaultMessage, locale);
  }

  /**
   * Delegates to the translation provider.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public String getTranslation(String key, Object[] args,
      String defaultMessage, Locale locale) {
    if (customTranslationPlugin != null) {
      String translation = customTranslationPlugin.getTranslation(key, args,
          locale, getApplicationSession());
      if (translation != null) {
        return translation;
      }
    }
    return translationProvider
        .getTranslation(key, args, defaultMessage, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAccessGranted(ISecurable securable) {
    if (SecurityHelper.isSubjectGranted(getApplicationSession().getSubject(),
        securable)) {
      if (customSecurityPlugin != null) {
        try {
          pushToSecurityContext(securable);
          Map<String, Object> securityContext = new HashMap<String, Object>();
          if (getApplicationSession() != null
              && getApplicationSession().getPrincipal() != null) {
            securityContext.put(SecurityContextConstants.USER_ROLES,
                SecurityHelper.getRoles(getApplicationSession().getSubject()));
            securityContext.put(SecurityContextConstants.USER_ID,
                getApplicationSession().getPrincipal().getName());
            Map<String, Object> sessionProperties = getApplicationSession()
                .getCustomValues();
            sessionProperties.putAll(getApplicationSession().getPrincipal()
                .getCustomProperties());
            securityContext.put(SecurityContextConstants.SESSION_PROPERTIES,
                sessionProperties);
          }
          securityContext.putAll(getSecurityContext());
          return customSecurityPlugin.isAccessGranted(securable,
              securityContext);
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
   *          the customESecurityHandler to set.
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
   *          the customTranslationPlugin to set.
   */
  public void setCustomTranslationPlugin(
      ITranslationPlugin customTranslationPlugin) {
    this.customTranslationPlugin = customTranslationPlugin;
  }

  /**
   * Gets the clientTimezone.
   * 
   * @return the clientTimezone.
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
}
