/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.backend.session.basic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;

import org.jspresso.framework.application.backend.session.ApplicationSessionException;
import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.application.backend.session.IEntityUnitOfWork;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IComponentCollectionFactory;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityCloneFactory;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.model.entity.IEntityRegistry;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.bean.BeanComparator;
import org.jspresso.framework.util.bean.BeanPropertyChangeRecorder;


/**
 * Basic implementation of an application session.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicApplicationSession implements IApplicationSession {

  private IAccessorFactory                     accessorFactory;
  private IEntityCloneFactory                  carbonEntityCloneFactory;
  private IComponentCollectionFactory<IEntity> collectionFactory;
  private BeanPropertyChangeRecorder           dirtRecorder;
  private Set<IEntity>                         entitiesRegisteredForDeletion;
  private List<IEntity>                        entitiesRegisteredForUpdate;
  private Set<IEntity>                         entitiesToMergeBack;
  private IEntityFactory                       entityFactory;
  private IEntityRegistry                      entityRegistry;
  private Locale                               locale;
  private Subject                              subject;
  private IEntityUnitOfWork                    unitOfWork;

  /**
   * Constructs a new <code>BasicApplicationSession</code> instance.
   */
  public BasicApplicationSession() {
    dirtRecorder = new BeanPropertyChangeRecorder();
  }

  /**
   * {@inheritDoc}
   */
  public void beginUnitOfWork() {
    if (unitOfWork.isActive()) {
      throw new ApplicationSessionException(
          "Cannot begin a new unit of work. Another one is already active.");
    }
    unitOfWork.begin();
    entitiesToMergeBack = new LinkedHashSet<IEntity>();
  }

  /**
   * Clears the pending operations.
   */
  public void clearPendingOperations() {
    entitiesRegisteredForUpdate = null;
    entitiesRegisteredForDeletion = null;
  }

  /**
   * {@inheritDoc}
   */
  public IEntity cloneInUnitOfWork(IEntity entity) {
    return cloneInUnitOfWork(Collections.singletonList(entity)).get(0);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isUpdatedInUnitOfWork(IEntity entity) {
    return entitiesToMergeBack != null && entitiesToMergeBack.contains(entity);
  }

  /**
   * {@inheritDoc}
   */
  public List<IEntity> cloneInUnitOfWork(List<IEntity> entities) {
    List<IEntity> uowEntities = new ArrayList<IEntity>();
    Map<Class<?>, Map<Serializable, IEntity>> alreadyCloned = new HashMap<Class<?>, Map<Serializable, IEntity>>();
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
      throw new ApplicationSessionException(
          "Cannot commit a unit of work that has not begun.");
    }
    try {
      Map<IEntity, IEntity> alreadyMerged = new HashMap<IEntity, IEntity>();
      for (IEntity entityToMergeBack : entitiesToMergeBack) {
        merge(entityToMergeBack, EMergeMode.MERGE_CLEAN_LAZY, alreadyMerged);
      }
    } finally {
      unitOfWork.commit();
      entitiesToMergeBack = null;
      clearPendingOperations();
    }
  }

  /**
   * {@inheritDoc}
   */
  public void deleteEntity(IEntity entity) {
    if (entity.isPersistent()) {
      if (entitiesRegisteredForDeletion == null) {
        entitiesRegisteredForDeletion = new LinkedHashSet<IEntity>();
      }
      entitiesRegisteredForDeletion.add(entity);
    }
  }

  /**
   * {@inheritDoc}
   */
  public Map<String, Object> getDirtyProperties(IEntity entity) {
    if (unitOfWork.isActive()) {
      return unitOfWork.getDirtyProperties(entity);
    }
    return dirtRecorder.getChangedProperties(entity);
  }

  /**
   * Gets the locale.
   * 
   * @return the locale.
   */
  public Locale getLocale() {
    return locale;
  }

  /**
   * {@inheritDoc}
   */
  public UserPrincipal getPrincipal() {
    if (subject != null && !subject.getPrincipals().isEmpty()) {
      return (UserPrincipal) subject.getPrincipals().iterator().next();
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public IEntity getRegisteredEntity(Class<? extends IEntity> entityContract,
      Object entityId) {
    return entityRegistry.get(entityContract, entityId);
  }

  /**
   * Gets the owner.
   * 
   * @return the owner.
   */
  public Subject getSubject() {
    return subject;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public void initializePropertyIfNeeded(IComponent componentOrEntity,
      IPropertyDescriptor propertyDescriptor) {
    if (propertyDescriptor instanceof ICollectionPropertyDescriptor) {
      String propertyName = propertyDescriptor.getName();
      Object propertyValue = componentOrEntity
          .straightGetProperty(propertyName);
      sortCollectionProperty(componentOrEntity, propertyName);
      for (Iterator<IEntity> ite = ((Collection<IEntity>) propertyValue)
          .iterator(); ite.hasNext();) {
        IEntity collectionElement = ite.next();
        if (isEntityRegisteredForDeletion(collectionElement)) {
          ite.remove();
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public boolean isDirty(IEntity entity) {
    if (unitOfWork.isActive()) {
      return unitOfWork.isDirty(entity);
    }
    Map<String, Object> entityDirtyProperties = getDirtyProperties(entity);
    return (entityDirtyProperties != null && entityDirtyProperties.size() > 0);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isDirty(IEntity entity, String propertyName) {
    if (unitOfWork.isActive()) {
      return unitOfWork.isDirty(entity, propertyName);
    }
    Map<String, Object> entityDirtyProperties = getDirtyProperties(entity);
    return (entityDirtyProperties != null && entityDirtyProperties
        .containsKey(propertyName));
  }

  /**
   * {@inheritDoc}
   */
  public boolean isEntityRegisteredForDeletion(IEntity entity) {
    return entitiesRegisteredForDeletion != null
        && entitiesRegisteredForDeletion.contains(entity);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isEntityRegisteredForUpdate(IEntity entity) {
    return entitiesRegisteredForUpdate != null
        && entitiesRegisteredForUpdate.contains(entity);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isInitialized(@SuppressWarnings("unused")
  Object objectOrProxy) {
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
  public void performPendingOperations() {
    clearPendingOperations();
  }

  /**
   * {@inheritDoc}
   */
  public void recordAsSynchronized(IEntity flushedEntity) {
    if (unitOfWork.isActive()) {
      unitOfWork.clearDirtyState(flushedEntity);
      entitiesToMergeBack.add(flushedEntity);
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
              && !(property.getValue() instanceof Collection && ((Collection<?>) property
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
  public void rollbackUnitOfWork() {
    if (!unitOfWork.isActive()) {
      throw new ApplicationSessionException(
          "Cannot rollback a unit of work that has not begun.");
    }
    unitOfWork.rollback();
    entitiesToMergeBack = null;
  }

  /**
   * Sets the accessorFactory.
   * 
   * @param accessorFactory
   *            the accessorFactory to set.
   */
  public void setAccessorFactory(IAccessorFactory accessorFactory) {
    this.accessorFactory = accessorFactory;
  }

  /**
   * Sets the carbonEntityCloneFactory.
   * 
   * @param carbonEntityCloneFactory
   *            the carbonEntityCloneFactory to set.
   */
  public void setCarbonEntityCloneFactory(
      IEntityCloneFactory carbonEntityCloneFactory) {
    this.carbonEntityCloneFactory = carbonEntityCloneFactory;
  }

  /**
   * Sets the collectionFactory.
   * 
   * @param collectionFactory
   *            the collectionFactory to set.
   */
  public void setCollectionFactory(
      IComponentCollectionFactory<IEntity> collectionFactory) {
    this.collectionFactory = collectionFactory;
  }

  /**
   * Sets the entityFactory.
   * 
   * @param entityFactory
   *            the entityFactory to set.
   */
  public void setEntityFactory(IEntityFactory entityFactory) {
    this.entityFactory = entityFactory;
  }

  /**
   * Sets the entityRegistry.
   * 
   * @param entityRegistry
   *            the entityRegistry to set.
   */
  public void setEntityRegistry(IEntityRegistry entityRegistry) {
    this.entityRegistry = entityRegistry;
  }

  /**
   * Sets the locale.
   * 
   * @param locale
   *            the locale to set.
   */
  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  /**
   * Sets the owner.
   * 
   * @param subject
   *            the owner to set.
   */
  public void setSubject(Subject subject) {
    this.subject = subject;
  }

  /**
   * Sets the unitOfWork.
   * 
   * @param unitOfWork
   *            the unitOfWork to set.
   */
  public void setUnitOfWork(IEntityUnitOfWork unitOfWork) {
    this.unitOfWork = unitOfWork;
  }

  /**
   * {@inheritDoc}
   */
  public void updateEntity(IEntity entity) {
    if (entity.isPersistent()) {
      if (entitiesRegisteredForUpdate == null) {
        entitiesRegisteredForUpdate = new ArrayList<IEntity>();
      }
      entitiesRegisteredForUpdate.add(entity);
    }
  }

  /**
   * Creates a transient collection instance, in respect to the type of
   * collection passed as parameter.
   * 
   * @param collection
   *            the collection to take the type from (List, Set, ...)
   * @return a transient collection instance with the same interface type as the
   *         parameter.
   */
  protected Collection<IEntity> createTransientEntityCollection(
      Collection<?> collection) {
    Collection<IEntity> uowEntityCollection = null;
    if (collection instanceof Set) {
      uowEntityCollection = collectionFactory.createEntityCollection(Set.class);
    } else if (collection instanceof List) {
      uowEntityCollection = collectionFactory
          .createEntityCollection(List.class);
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
   * Gets the entitiesRegisteredForDeletion.
   * 
   * @return the entitiesRegisteredForDeletion.
   */
  protected Set<IEntity> getEntitiesRegisteredForDeletion() {
    return entitiesRegisteredForDeletion;
  }

  /**
   * Gets the entitiesRegisteredForUpdate.
   * 
   * @return the entitiesRegisteredForUpdate.
   */
  protected List<IEntity> getEntitiesRegisteredForUpdate() {
    return entitiesRegisteredForUpdate;
  }

  /**
   * Gets the entitiesToMergeBack.
   * 
   * @return the entitiesToMergeBack.
   */
  protected Set<IEntity> getEntitiesToMergeBack() {
    return entitiesToMergeBack;
  }

  /**
   * Sorts an entity collection property.
   * 
   * @param component
   *            the component to sort the collection property of.
   * @param propertyName
   *            the name of the collection property to sort.
   */
  @SuppressWarnings("unchecked")
  protected void sortCollectionProperty(IComponent component,
      String propertyName) {
    Collection<Object> propertyValue = (Collection<Object>) component
        .straightGetProperty(propertyName);
    ICollectionPropertyDescriptor propertyDescriptor = (ICollectionPropertyDescriptor) entityFactory
        .getComponentDescriptor(component.getContract()).getPropertyDescriptor(
            propertyName);
    if (propertyValue != null
        && !propertyValue.isEmpty()
        && !List.class.isAssignableFrom(propertyDescriptor
            .getCollectionDescriptor().getCollectionInterface())) {
      List<String> orderingProperties = propertyDescriptor
          .getOrderingProperties();
      if (orderingProperties != null && !orderingProperties.isEmpty()) {
        BeanComparator comparator = new BeanComparator();
        List<IAccessor> orderingAccessors = new ArrayList<IAccessor>();
        Class collectionElementContract = propertyDescriptor
            .getCollectionDescriptor().getElementDescriptor()
            .getComponentContract();
        for (String orderingProperty : orderingProperties) {
          orderingAccessors.add(accessorFactory.createPropertyAccessor(
              orderingProperty, collectionElementContract));
        }
        comparator.setOrderingAccessors(orderingAccessors);
        List<Object> collectionCopy = new ArrayList<Object>(propertyValue);
        Collections.sort(collectionCopy, comparator);
        Collection<Object> collectionProperty = propertyValue;
        collectionProperty.clear();
        collectionProperty.addAll(collectionCopy);
      }
    }
  }

  /**
   * Gives a chance to the session to wrap a collection before making it part of
   * the unit of work.
   * 
   * @param entity
   *            the entity the collection belongs to.
   * @param transientCollection
   *            the transient collection to make part of the unit of work.
   * @param snapshotCollection
   *            the original collection state as reported by the dirt recorder.
   * @param role
   *            the name of the property represented by the collection in its
   *            owner.
   * @return the wrapped collection if any (it may be the collection itself as
   *         in this implementation).
   */
  protected Collection<IEntity> wrapDetachedEntityCollection(IEntity entity,
      Collection<IEntity> transientCollection,
      Collection<IEntity> snapshotCollection, String role) {
    return transientCollection;
  }

  private void cleanDirtyProperties(IEntity entity) {
    dirtRecorder.resetChangedProperties(entity, null);
  }

  @SuppressWarnings("unchecked")
  private IEntity cloneInUnitOfWork(IEntity entity,
      Map<Class<?>, Map<Serializable, IEntity>> alreadyCloned) {
    Map<Serializable, IEntity> contractBuffer = alreadyCloned.get(entity
        .getContract());
    IEntity uowEntity = null;
    if (contractBuffer == null) {
      contractBuffer = new HashMap<Serializable, IEntity>();
      alreadyCloned.put(entity.getContract(), contractBuffer);
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
      if (property.getValue() instanceof IEntity) {
        if (isInitialized(property.getValue())) {
          uowEntity.straightSetProperty(property.getKey(), cloneInUnitOfWork(
              (IEntity) property.getValue(), alreadyCloned));
        } else {
          uowEntity.straightSetProperty(property.getKey(), property.getValue());
        }
      } else if (property.getValue() instanceof Collection) {
        if (isInitialized(property.getValue())) {
          Collection<IEntity> uowEntityCollection = createTransientEntityCollection((Collection) property
              .getValue());
          for (IEntity entityCollectionElement : (Collection<IEntity>) property
              .getValue()) {
            uowEntityCollection.add(cloneInUnitOfWork(entityCollectionElement,
                alreadyCloned));
          }
          Collection snapshotCollection = (Collection) dirtyProperties
              .get(property.getKey());
          if (snapshotCollection != null) {
            Collection clonedSnapshotCollection = createTransientEntityCollection(snapshotCollection);
            for (Object snapshotCollectionElement : snapshotCollection) {
              clonedSnapshotCollection.add(cloneInUnitOfWork(
                  (IEntity) snapshotCollectionElement, alreadyCloned));
            }
            snapshotCollection = clonedSnapshotCollection;
          }
          uowEntityCollection = wrapDetachedEntityCollection(entity,
              uowEntityCollection, snapshotCollection, property.getKey());
          uowEntity.straightSetProperty(property.getKey(), uowEntityCollection);
        } else {
          uowEntity.straightSetProperty(property.getKey(), property.getValue());
        }
      } else if (property.getValue() instanceof IComponent) {
        uowEntity.straightSetProperty(property.getKey(),
            cloneComponentInUnitOfWork((IComponent) property.getValue(),
                alreadyCloned));
      }
    }
    unitOfWork
        .register(uowEntity, new HashMap<String, Object>(dirtyProperties));
    return uowEntity;
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
          uowComponent.straightSetProperty(property.getKey(), property
              .getValue());
        }
      } else if (property.getValue() instanceof IComponent) {
        uowComponent.straightSetProperty(property.getKey(),
            cloneComponentInUnitOfWork((IComponent) property.getValue(),
                alreadyCloned));
      }
    }
    return uowComponent;
  }

  @SuppressWarnings("unchecked")
  private IEntity merge(IEntity entity, EMergeMode mergeMode,
      Map<IEntity, IEntity> alreadyMerged) {
    if (entity == null) {
      return null;
    }
    if (alreadyMerged.containsKey(entity)) {
      return alreadyMerged.get(entity);
    }
    boolean dirtRecorderWasEnabled = dirtRecorder.isEnabled();
    try {
      dirtRecorder.setEnabled(false);
      IEntity registeredEntity = getRegisteredEntity(entity.getContract(),
          entity.getId());
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
      Map sessionDirtyProperties = dirtRecorder
          .getChangedProperties(registeredEntity);
      boolean dirtyInSession = (sessionDirtyProperties != null && (!sessionDirtyProperties
          .isEmpty()));
      if (mergeMode != EMergeMode.MERGE_CLEAN_LAZY
          || (dirtyInSession || (!registeredEntity.getVersion().equals(
              entity.getVersion()))) || newlyRegistered) {
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
                && !isInitialized(property.getValue())) {
              if (registeredEntityProperties.get(property.getKey()) == null) {
                mergedProperties.put(property.getKey(), property.getValue());
              }
            } else {
              Object registeredProperty = registeredEntityProperties
                  .get(property.getKey());
              if (isInitialized(registeredProperty)) {
                mergedProperties.put(property.getKey(), merge(
                    (IEntity) property.getValue(), mergeMode, alreadyMerged));
              }
            }
          } else if (property.getValue() instanceof Collection) {
            if (mergeMode != EMergeMode.MERGE_CLEAN_EAGER
                && !isInitialized(property.getValue())) {
              if (registeredEntityProperties.get(property.getKey()) == null) {
                mergedProperties.put(property.getKey(), property.getValue());
              }
            } else {
              Collection<IEntity> registeredCollection = (Collection<IEntity>) registeredEntityProperties
                  .get(property.getKey());
              if (isInitialized(registeredCollection)) {
                if (property.getValue() instanceof Set) {
                  registeredCollection = collectionFactory
                      .createEntityCollection(Set.class);
                } else if (property.getValue() instanceof List) {
                  registeredCollection = collectionFactory
                      .createEntityCollection(List.class);
                }
                for (IEntity entityCollectionElement : (Collection<IEntity>) property
                    .getValue()) {
                  registeredCollection.add(merge(entityCollectionElement,
                      mergeMode, alreadyMerged));
                }
                if (registeredEntity.isPersistent()) {
                  Collection<IEntity> snapshotCollection = null;
                  Map<String, Object> dirtyProperties = getDirtyProperties(registeredEntity);
                  if (dirtyProperties != null) {
                    snapshotCollection = (Collection<IEntity>) dirtyProperties
                        .get(property.getKey());
                  }
                  mergedProperties.put(property.getKey(),
                      wrapDetachedEntityCollection(registeredEntity,
                          registeredCollection, snapshotCollection, property
                              .getKey()));
                } else {
                  mergedProperties.put(property.getKey(), registeredCollection);
                }
              }
            }
          } else if (property.getValue() instanceof IComponent) {
            IComponent registeredComponent = (IComponent) registeredEntityProperties
                .get(property.getKey());
            mergedProperties.put(property.getKey(), mergeComponent(
                (IComponent) property.getValue(), registeredComponent,
                mergeMode, alreadyMerged));
          } else {
            mergedProperties.put(property.getKey(), property.getValue());
          }
        }
        registeredEntity.straightSetProperties(mergedProperties);
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
    if (mergeMode != EMergeMode.MERGE_CLEAN_LAZY) {
      Map<String, Object> componentPropertiesToMerge = componentToMerge
          .straightGetProperties();
      Map<String, Object> registeredComponentProperties = varRegisteredComponent
          .straightGetProperties();
      Map<String, Object> mergedProperties = new HashMap<String, Object>();
      for (Map.Entry<String, Object> property : componentPropertiesToMerge
          .entrySet()) {
        if (property.getValue() instanceof IEntity) {
          if (mergeMode != EMergeMode.MERGE_CLEAN_EAGER
              && !isInitialized(property.getValue())) {
            if (registeredComponentProperties.get(property.getKey()) == null) {
              mergedProperties.put(property.getKey(), property.getValue());
            }
          } else {
            Object registeredProperty = registeredComponentProperties
                .get(property.getKey());
            if (isInitialized(registeredProperty)) {
              mergedProperties.put(property.getKey(), merge((IEntity) property
                  .getValue(), mergeMode, alreadyMerged));
            }
          }
        } else if (property.getValue() instanceof IComponent) {
          IComponent registeredSubComponent = (IComponent) registeredComponentProperties
              .get(property.getKey());
          mergedProperties.put(property.getKey(), mergeComponent(
              (IComponent) property.getValue(), registeredSubComponent,
              mergeMode, alreadyMerged));
        } else {
          mergedProperties.put(property.getKey(), property.getValue());
        }
      }
      varRegisteredComponent.straightSetProperties(mergedProperties);
    }
    return varRegisteredComponent;
  }
}
