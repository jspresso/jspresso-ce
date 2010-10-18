/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.application.AbstractController;
import org.jspresso.framework.application.backend.action.Transactional;
import org.jspresso.framework.application.backend.entity.ControllerAwareProxyEntityFactory;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.application.backend.session.IEntityUnitOfWork;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.application.model.descriptor.WorkspaceDescriptor;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.model.IModelConnectorFactory;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IComponentCollectionFactory;
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
import org.jspresso.framework.security.SecurityHelper;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.bean.BeanPropertyChangeRecorder;
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

  /**
   * Constructs a new <code>AbstractBackendController</code> instance.
   */
  protected AbstractBackendController() {
    dirtRecorder = new BeanPropertyChangeRecorder();
  }

  /**
   * {@inheritDoc}
   */
  public void joinTransaction() {
    if (!unitOfWork.isActive()) {
      beginUnitOfWork();
    }
  }

  /**
   * {@inheritDoc}
   */
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
  public void clearPendingOperations() {
    unitOfWork.clearPendingOperations();
  }

  /**
   * {@inheritDoc}
   */
  public IEntity cloneInUnitOfWork(IEntity entity) {
    if (!unitOfWork.isActive()) {
      throw new BackendException(
          "Cannot use a unit of work that has not begun.");
    }
    return cloneInUnitOfWork(Collections.singletonList(entity)).get(0);
  }

  /**
   * {@inheritDoc}
   */
  public List<IEntity> cloneInUnitOfWork(List<IEntity> entities) {
    if (!unitOfWork.isActive()) {
      throw new BackendException(
          "Cannot use a unit of work that has not begun.");
    }
    List<IEntity> uowEntities = new ArrayList<IEntity>();
    // Map<Class<?>, Map<Serializable, IEntity>> alreadyCloned = new
    // HashMap<Class<?>, Map<Serializable, IEntity>>();
    Map<Class<?>, Map<Serializable, IEntity>> alreadyCloned = unitOfWork
        .getRegisteredEntities();
    for (IEntity entity : entities) {
      uowEntities.add(cloneInUnitOfWork(entity, alreadyCloned));
    }
    return uowEntities;
  }

  /**
   * {@inheritDoc}
   */
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
  public IValueConnector createModelConnector(String id,
      IModelDescriptor modelDescriptor) {
    return modelConnectorFactory.createModelConnector(id, modelDescriptor,
        getApplicationSession().getSubject());
  }

  /**
   * Directly delegates execution to the action after having completed its
   * execution context with the controller's initial context.
   * <p>
   * {@inheritDoc}
   */
  public boolean execute(final IAction action, final Map<String, Object> context) {
    if (action == null) {
      return true;
    }
    if (!action.isBackend()) {
      throw new ActionException(
          "The backend controller is executing a frontend action. Please check the action chaining : "
              + action.toString());
    }
    SecurityHelper.checkAccess(getApplicationSession().getSubject(), action,
        getTranslationProvider(), getLocale());
    Map<String, Object> actionContext = getInitialActionContext();
    if (context != null) {
      context.putAll(actionContext);
    }
    if (action.getClass().isAnnotationPresent(Transactional.class)) {
      Boolean ret = (Boolean) getTransactionTemplate().execute(
          new TransactionCallback() {

            public Object doInTransaction(TransactionStatus status) {
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
  public IAccessorFactory getAccessorFactory() {
    return modelConnectorFactory.getAccessorFactory();
  }

  /**
   * {@inheritDoc}
   */
  public IApplicationSession getApplicationSession() {
    return applicationSession;
  }

  /**
   * {@inheritDoc}
   */
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
        Map.Entry<String, Object> dirtyProperty = ite.next();
        Object currentProperty = entity.straightGetProperty(dirtyProperty
            .getKey());
        if ((currentProperty != null && currentProperty.equals(dirtyProperty
            .getValue()))
            || (currentProperty == null && dirtyProperty.getValue() == null)) {
          ite.remove(); // actually removes the mapping from the map.
        }
      }
    }
    return dirtyProperties;
  }

  /**
   * {@inheritDoc}
   */
  public IEntityFactory getEntityFactory() {
    return entityFactory;
  }

  /**
   * Contains the current application session.
   * <p>
   * {@inheritDoc}
   */
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
  public Locale getLocale() {
    return applicationSession.getLocale();
  }

  /**
   * {@inheritDoc}
   */
  public IEntity getRegisteredEntity(Class<? extends IEntity> entityContract,
      Object entityId) {
    return entityRegistry.get(entityContract, entityId);
  }

  /**
   * Gets the transactionTemplate.
   * 
   * @return the transactionTemplate.
   */
  public TransactionTemplate getTransactionTemplate() {
    return transactionTemplate;
  }

  /**
   * {@inheritDoc}
   */
  public IValueConnector getWorkspaceConnector(String workspaceName) {
    return workspaceConnectors.get(workspaceName);
  }

  /**
   * {@inheritDoc}
   */
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
  public void installWorkspaces(Map<String, Workspace> workspaces) {
    workspaceConnectors = new HashMap<String, IValueConnector>();
    for (Map.Entry<String, Workspace> workspaceEntry : workspaces.entrySet()) {
      String workspaceName = workspaceEntry.getKey();
      Workspace workspace = workspaceEntry.getValue();
      if (isAccessGranted(workspace)) {
        IModelDescriptor workspaceDescriptor;
        workspaceDescriptor = WorkspaceDescriptor.WORKSPACE_DESCRIPTOR;
        IValueConnector nextWorkspaceConnector = modelConnectorFactory
            .createModelConnector(workspaceName, workspaceDescriptor,
                getApplicationSession().getSubject());
        nextWorkspaceConnector.setConnectorValue(workspace);
        workspaceConnectors.put(workspaceName, nextWorkspaceConnector);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
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
  public boolean isDirtyInDepth(IEntity entity) {
    return isAnyDirtyInDepth(Collections.singleton(entity));
  }

  /**
   * {@inheritDoc}
   */
  public boolean isEntityRegisteredForDeletion(IEntity entity) {
    return unitOfWork.isEntityRegisteredForDeletion(entity);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isEntityRegisteredForUpdate(IEntity entity) {
    return unitOfWork.isEntityRegisteredForUpdate(entity);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isInitialized(@SuppressWarnings("unused") Object objectOrProxy) {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isUnitOfWorkActive() {
    return unitOfWork.isActive();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isUpdatedInUnitOfWork(IEntity entity) {
    if (!unitOfWork.isActive()) {
      throw new BackendException("Cannot access unit of work.");
    }
    return unitOfWork.isUpdated(entity);
  }

  /**
   * {@inheritDoc}
   */
  public IEntity merge(IEntity entity, EMergeMode mergeMode) {
    return merge(entity, mergeMode, new HashMap<IEntity, IEntity>());
  }

  /**
   * {@inheritDoc}
   */
  public List<IEntity> merge(List<IEntity> entities, EMergeMode mergeMode) {
    Map<IEntity, IEntity> alreadyMerged = new HashMap<IEntity, IEntity>();
    List<IEntity> mergedList = new ArrayList<IEntity>();
    for (IEntity entity : entities) {
      mergedList.add(merge(entity, mergeMode, alreadyMerged));
    }
    return mergedList;
  }

  /**
   * {@inheritDoc}
   */
  public void recordAsSynchronized(IEntity flushedEntity) {
    if (unitOfWork.isActive()) {
      unitOfWork.clearDirtyState(flushedEntity);
      unitOfWork.addUpdatedEntity(flushedEntity);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void registerEntity(IEntity entity, boolean isEntityTransient) {
    if (!unitOfWork.isActive()) {
      entityRegistry.register(entity);
      Map<String, Object> initialDirtyProperties = null;
      if (isEntityTransient) {
        initialDirtyProperties = new HashMap<String, Object>();
        for (Map.Entry<String, Object> property : entity
            .straightGetProperties().entrySet()) {
          if (property.getValue() != null
              && !(property.getValue() instanceof Collection<?> && ((Collection<?>) property
                  .getValue()).isEmpty())) {
            initialDirtyProperties.put(property.getKey(), null);
          }
        }
      }
      dirtRecorder.register(entity, initialDirtyProperties);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void registerForDeletion(IEntity entity) {
    unitOfWork.registerForDeletion(entity);
  }

  /**
   * {@inheritDoc}
   */
  public void registerForUpdate(IEntity entity) {
    unitOfWork.registerForUpdate(entity);
  }

  /**
   * {@inheritDoc}
   */
  public ComponentTransferStructure<? extends IComponent> retrieveComponents() {
    return transferStructure;
  }

  /**
   * {@inheritDoc}
   */
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
    ((ControllerAwareProxyEntityFactory) getEntityFactory())
        .setBackendController(this);
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
    this.transactionTemplate = new ControllerAwareTransactionTemplate(
        transactionTemplate, this);
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
  public boolean start(Locale startingLocale) {
    applicationSession.setLocale(startingLocale);
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public boolean stop() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
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
  public boolean isDirty(IEntity entity) {
    if (entity == null) {
      return false;
    }
    Map<String, Object> entityDirtyProperties = getDirtyProperties(entity);
    IComponentDescriptor<?> entityDescriptor = getEntityFactory()
        .getComponentDescriptor(entity.getComponentContract());
    if (entityDirtyProperties != null) {
      entityDirtyProperties.remove(IEntity.VERSION);
      for (Map.Entry<String, Object> dirtyProperty : entityDirtyProperties
          .entrySet()) {
        if (!entityDescriptor.getPropertyDescriptor(dirtyProperty.getKey())
            .isComputed()) {
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
  public boolean isDirty(IEntity entity, String propertyName) {
    if (entity == null) {
      return false;
    }
    Map<String, Object> entityDirtyProperties = getDirtyProperties(entity);
    if (entityDirtyProperties != null
        && entityDirtyProperties.containsKey(propertyName)) {
      return !getEntityFactory()
          .getComponentDescriptor(entity.getComponentContract())
          .getPropertyDescriptor(propertyName).isComputed();
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
      if (property.getValue() instanceof IEntity) {
        if (isInitialized(property.getValue())) {
          uowComponent.straightSetProperty(property.getKey(),
              cloneInUnitOfWork((IEntity) property.getValue(), alreadyCloned));
        } else {
          uowComponent.straightSetProperty(property.getKey(),
              property.getValue());
        }
      } else if (property.getValue() instanceof IComponent) {
        uowComponent.straightSetProperty(
            property.getKey(),
            cloneComponentInUnitOfWork((IComponent) property.getValue(),
                alreadyCloned));
      }
    }
    return uowComponent;
  }

  @SuppressWarnings("unchecked")
  private IEntity cloneInUnitOfWork(IEntity entity,
      Map<Class<?>, Map<Serializable, IEntity>> alreadyCloned) {
    Map<Serializable, IEntity> contractBuffer = alreadyCloned.get(entity
        .getComponentContract());
    IComponentDescriptor<?> entityDescriptor = getEntityFactory()
        .getComponentDescriptor(entity.getComponentContract());
    IEntity uowEntity = null;
    if (contractBuffer == null) {
      contractBuffer = new HashMap<Serializable, IEntity>();
      alreadyCloned.put(entity.getComponentContract(), contractBuffer);
    } else {
      uowEntity = contractBuffer.get(entity.getId());
      if (uowEntity != null) {
        return uowEntity;
      }
    }
    uowEntity = carbonEntityCloneFactory.cloneEntity(entity, entityFactory);
    Map<String, Object> dirtyProperties = dirtRecorder
        .getChangedProperties(entity);
    if (dirtyProperties == null) {
      dirtyProperties = new HashMap<String, Object>();
    }
    contractBuffer.put(entity.getId(), uowEntity);
    Map<String, Object> entityProperties = entity.straightGetProperties();
    for (Map.Entry<String, Object> property : entityProperties.entrySet()) {
      IPropertyDescriptor propertyDescriptor = entityDescriptor
          .getPropertyDescriptor(property.getKey());
      if (property.getValue() instanceof IEntity) {
        if (isInitialized(property.getValue())) {
          uowEntity.straightSetProperty(property.getKey(),
              cloneInUnitOfWork((IEntity) property.getValue(), alreadyCloned));
        } else {
          uowEntity.straightSetProperty(property.getKey(), property.getValue());
        }
      } else if (property.getValue() instanceof Collection<?>) {
        if (isInitialized(property.getValue())) {
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
          if (propertyDescriptor != null && !propertyDescriptor.isComputed()) {
            Collection<IComponent> snapshotCollection = (Collection<IComponent>) dirtyProperties
                .get(property.getKey());
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
                snapshotCollection, property.getKey());
          }
          uowEntity.straightSetProperty(property.getKey(), uowCollection);
        } else {
          uowEntity.straightSetProperty(property.getKey(), property.getValue());
        }
      } else if (property.getValue() instanceof IEntity[]) {
        IEntity[] uowArray = new IEntity[((IEntity[]) property.getValue()).length];
        for (int i = 0; i < uowArray.length; i++) {
          uowArray[i] = cloneInUnitOfWork(((IEntity[]) property.getValue())[i],
              alreadyCloned);
        }
        uowEntity.straightSetProperty(property.getKey(), uowArray);
      } else if (property.getValue() instanceof IComponent[]) {
        IComponent[] uowArray = new IComponent[((IComponent[]) property
            .getValue()).length];
        for (int i = 0; i < uowArray.length; i++) {
          uowArray[i] = cloneComponentInUnitOfWork(
              ((IComponent[]) property.getValue())[i], alreadyCloned);
        }
        uowEntity.straightSetProperty(property.getKey(), uowArray);
      } else if (property.getValue() instanceof IComponent) {
        uowEntity.straightSetProperty(
            property.getKey(),
            cloneComponentInUnitOfWork((IComponent) property.getValue(),
                alreadyCloned));
      }
    }
    unitOfWork
        .register(uowEntity, new HashMap<String, Object>(dirtyProperties));
    uowEntity.onLoad();
    return uowEntity;
  }

  private boolean isDirtyInDepth(IEntity entity, Set<IEntity> alreadyTraversed) {
    alreadyTraversed.add(entity);
    if (isDirty(entity)) {
      return true;
    }
    Map<String, Object> entityProps = entity.straightGetProperties();
    for (Map.Entry<String, Object> property : entityProps.entrySet()) {
      if (property.getValue() instanceof IEntity) {
        if (isInitialized(property.getValue())
            && !alreadyTraversed.contains(property.getValue())) {
          if (isDirtyInDepth((IEntity) property.getValue(), alreadyTraversed)) {
            return true;
          }
        }
      } else if (property.getValue() instanceof Collection<?>) {
        if (isInitialized(property.getValue())) {
          for (Object elt : ((Collection<?>) property.getValue())) {
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
  private IEntity merge(IEntity entity, final EMergeMode mergeMode,
      Map<IEntity, IEntity> alreadyMerged) {
    if (entity == null) {
      return null;
    }
    if (alreadyMerged.containsKey(entity)) {
      return alreadyMerged.get(entity);
    }
    boolean dirtRecorderWasEnabled = dirtRecorder.isEnabled();
    try {
      if (mergeMode != EMergeMode.MERGE_EAGER) {
        dirtRecorder.setEnabled(false);
      }
      IEntity registeredEntity = getRegisteredEntity(
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
      if (newlyRegistered || mergeMode != EMergeMode.MERGE_CLEAN_LAZY
          || registeredEntity.getVersion() == null
          || !registeredEntity.getVersion().equals(entity.getVersion())) {
        if (mergeMode == EMergeMode.MERGE_CLEAN_EAGER
            || mergeMode == EMergeMode.MERGE_CLEAN_LAZY) {
          cleanDirtyProperties(registeredEntity);
        }
        Map<String, Object> entityProperties = entity.straightGetProperties();
        Map<String, Object> registeredEntityProperties = registeredEntity
            .straightGetProperties();
        Map<String, Object> mergedProperties = new HashMap<String, Object>();
        for (Map.Entry<String, Object> property : entityProperties.entrySet()) {
          if (property.getValue() instanceof IEntity) {
            if (mergeMode != EMergeMode.MERGE_CLEAN_EAGER
                && mergeMode != EMergeMode.MERGE_EAGER
                && !isInitialized(property.getValue())) {
              if (registeredEntityProperties.get(property.getKey()) == null) {
                mergedProperties.put(property.getKey(), property.getValue());
              }
            } else {
              Object registeredProperty = registeredEntityProperties
                  .get(property.getKey());
              if (mergeMode == EMergeMode.MERGE_EAGER
                  && isInitialized(property.getValue())) {
                initializePropertyIfNeeded(registeredEntity, property.getKey());
              }
              if (isInitialized(registeredProperty)) {
                mergedProperties.put(
                    property.getKey(),
                    merge((IEntity) property.getValue(), mergeMode,
                        alreadyMerged));
              }
            }
          } else if (property.getValue() instanceof Collection) {
            if (mergeMode != EMergeMode.MERGE_CLEAN_EAGER
                && mergeMode != EMergeMode.MERGE_EAGER
                && !isInitialized(property.getValue())) {
              if (registeredEntityProperties.get(property.getKey()) == null) {
                mergedProperties.put(property.getKey(), property.getValue());
              }
            } else {
              Collection<IComponent> registeredCollection = (Collection<IComponent>) registeredEntityProperties
                  .get(property.getKey());
              if (mergeMode == EMergeMode.MERGE_EAGER
                  && isInitialized(property.getValue())) {
                initializePropertyIfNeeded(registeredEntity, property.getKey());
              }
              if (isInitialized(registeredCollection)) {
                if (property.getValue() instanceof Set) {
                  registeredCollection = collectionFactory
                      .createComponentCollection(Set.class);
                } else if (property.getValue() instanceof List) {
                  registeredCollection = collectionFactory
                      .createComponentCollection(List.class);
                }
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
                        .get(property.getKey());
                  }
                  mergedProperties.put(
                      property.getKey(),
                      wrapDetachedCollection(registeredEntity,
                          registeredCollection, snapshotCollection,
                          property.getKey()));
                } else {
                  mergedProperties.put(property.getKey(), registeredCollection);
                }
              }
            }
          } else if (property.getValue() instanceof IComponent) {
            IComponent registeredComponent = (IComponent) registeredEntityProperties
                .get(property.getKey());
            mergedProperties.put(
                property.getKey(),
                mergeComponent((IComponent) property.getValue(),
                    registeredComponent, mergeMode, alreadyMerged));
          } else {
            mergedProperties.put(property.getKey(), property.getValue());
          }
        }
        registeredEntity.straightSetProperties(mergedProperties);
      } else if (mergeMode == EMergeMode.MERGE_CLEAN_LAZY) {
        // version has not evolved but we must still reset dirty properties in
        // case only versionControl false properties have changed.
        cleanDirtyProperties(registeredEntity);
      }
      registeredEntity.onLoad();
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
      if (property.getValue() instanceof IEntity) {
        if (mergeMode != EMergeMode.MERGE_CLEAN_EAGER
            && mergeMode != EMergeMode.MERGE_EAGER
            && !isInitialized(property.getValue())) {
          if (registeredComponentProperties.get(property.getKey()) == null) {
            mergedProperties.put(property.getKey(), property.getValue());
          }
        } else {
          Object registeredProperty = registeredComponentProperties
              .get(property.getKey());
          if (mergeMode == EMergeMode.MERGE_EAGER
              && isInitialized(property.getValue())) {
            initializePropertyIfNeeded(registeredComponent, property.getKey());
          }
          if (isInitialized(registeredProperty)) {
            mergedProperties.put(property.getKey(),
                merge((IEntity) property.getValue(), mergeMode, alreadyMerged));
          }
        }
      } else if (property.getValue() instanceof IComponent) {
        IComponent registeredSubComponent = (IComponent) registeredComponentProperties
            .get(property.getKey());
        mergedProperties.put(
            property.getKey(),
            mergeComponent((IComponent) property.getValue(),
                registeredSubComponent, mergeMode, alreadyMerged));
      } else {
        mergedProperties.put(property.getKey(), property.getValue());
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
  public void cleanRelationshipsOnDeletion(IComponent component, boolean dryRun)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    cleanRelationshipsOnDeletion(component, dryRun, new HashSet<IComponent>());
  }

  @SuppressWarnings("unchecked")
  private void cleanRelationshipsOnDeletion(IComponent componentOrProxy,
      boolean dryRun, Set<IComponent> clearedEntities)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    IComponent component;
    component = unwrapProxy(componentOrProxy);
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
        if (property.getValue() != null) {
          IPropertyDescriptor propertyDescriptor = componentDescriptor
              .getPropertyDescriptor(property.getKey());
          if (propertyDescriptor instanceof IRelationshipEndPropertyDescriptor) {
            // force initialization of relationship property.
            getAccessorFactory().createPropertyAccessor(property.getKey(),
                component.getComponentContract()).getValue(component);
            if (propertyDescriptor instanceof IReferencePropertyDescriptor
                && property.getValue() instanceof IEntity) {
              if (((IRelationshipEndPropertyDescriptor) propertyDescriptor)
                  .isComposition()) {
                cleanRelationshipsOnDeletion((IEntity) property.getValue(),
                    dryRun, clearedEntities);
              } else {
                if (dryRun) {
                  // manually trigger reverse relations preprocessors.
                  if (((IRelationshipEndPropertyDescriptor) propertyDescriptor)
                      .getReverseRelationEnd() != null
                      && !((IRelationshipEndPropertyDescriptor) propertyDescriptor)
                          .isComposition()) {
                    IPropertyDescriptor reversePropertyDescriptor = ((IReferencePropertyDescriptor<?>) propertyDescriptor)
                        .getReverseRelationEnd();
                    if (reversePropertyDescriptor instanceof IReferencePropertyDescriptor) {
                      reversePropertyDescriptor.preprocessSetter(
                          property.getValue(), null);
                    } else if (reversePropertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
                      Collection<?> reverseCollection = (Collection<?>) getAccessorFactory()
                          .createPropertyAccessor(
                              reversePropertyDescriptor.getName(),
                              ((IComponent) property.getValue())
                                  .getComponentContract()).getValue(
                              property.getValue());
                      ((ICollectionPropertyDescriptor<?>) reversePropertyDescriptor)
                          .preprocessRemover(property.getValue(),
                              reverseCollection, component);
                    }
                  }
                } else {
                  // test to see if we already traversed the reverse
                  // relationship that is a composition.
                  if (((IRelationshipEndPropertyDescriptor) propertyDescriptor)
                      .getReverseRelationEnd() == null
                      || !(((IRelationshipEndPropertyDescriptor) propertyDescriptor)
                          .getReverseRelationEnd().isComposition() && clearedEntities
                          .contains(property.getValue()))) {
                    // set to null to clean reverse relation ends
                    getAccessorFactory().createPropertyAccessor(
                        property.getKey(), component.getComponentContract())
                        .setValue(component, null);
                    // but technically reset to original value to avoid
                    // Hibernate
                    // not-null checks
                    component.straightSetProperty(property.getKey(),
                        property.getValue());
                  }
                }
              }
            } else if (propertyDescriptor instanceof ICollectionPropertyDescriptor) {
              if (((ICollectionPropertyDescriptor<?>) propertyDescriptor)
                  .isComposition()) {
                for (IComponent composedEntity : new ArrayList<IComponent>(
                    (Collection<IComponent>) property.getValue())) {
                  cleanRelationshipsOnDeletion(composedEntity, dryRun,
                      clearedEntities);
                }
              } else if (propertyDescriptor.isModifiable()
                  && !((Collection<?>) property.getValue()).isEmpty()) {
                if (dryRun) {
                  // manually trigger reverse relations preprocessors.
                  if (((ICollectionPropertyDescriptor<?>) propertyDescriptor)
                      .getReverseRelationEnd() != null) {
                    IPropertyDescriptor reversePropertyDescriptor = ((ICollectionPropertyDescriptor<?>) propertyDescriptor)
                        .getReverseRelationEnd();
                    if (reversePropertyDescriptor instanceof IReferencePropertyDescriptor) {
                      reversePropertyDescriptor.preprocessSetter(
                          property.getValue(), null);
                    } else if (reversePropertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
                      for (Object collectionElement : (Collection<?>) property
                          .getValue()) {
                        Collection<?> reverseCollection = (Collection<?>) getAccessorFactory()
                            .createPropertyAccessor(
                                reversePropertyDescriptor.getName(),
                                ((IComponent) collectionElement)
                                    .getComponentContract()).getValue(
                                collectionElement);
                        ((ICollectionPropertyDescriptor<?>) reversePropertyDescriptor)
                            .preprocessRemover(collectionElement,
                                reverseCollection, component);
                      }
                    }
                  }
                } else {
                  getAccessorFactory().createPropertyAccessor(
                      property.getKey(), component.getComponentContract())
                      .setValue(component, null);
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
  protected abstract IComponent unwrapProxy(IComponent componentOrProxy);

}
