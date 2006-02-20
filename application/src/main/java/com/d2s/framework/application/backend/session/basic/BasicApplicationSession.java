/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.session.basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.collection.PersistentCollection;
import org.hibernate.collection.PersistentSet;

import com.d2s.framework.application.backend.session.ApplicationSessionException;
import com.d2s.framework.application.backend.session.IApplicationSession;
import com.d2s.framework.application.backend.session.IEntityUnitOfWork;
import com.d2s.framework.application.backend.session.MergeMode;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IEntityCollectionFactory;
import com.d2s.framework.model.entity.IEntityRegistry;
import com.d2s.framework.util.bean.BeanPropertyChangeRecorder;

/**
 * Basic implementation of an application session.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicApplicationSession implements IApplicationSession {

  private IEntityRegistry            entityRegistry;
  private BeanPropertyChangeRecorder dirtRecorder;
  private IEntityUnitOfWork          unitOfWork;
  private IEntityCollectionFactory   collectionFactory;
  private Set<IEntity>               entitiesToMergeBack;

  /**
   * Constructs a new <code>BasicApplicationSession</code> instance.
   */
  public BasicApplicationSession() {
    dirtRecorder = new BeanPropertyChangeRecorder();
  }

  /**
   * {@inheritDoc}
   */
  public void registerEntity(IEntity entity) {
    if (!unitOfWork.isActive()) {
      entityRegistry.register(entity);
      Map<String, Object> initialDirtyProperties = new HashMap<String, Object>();
      if (!entity.isPersistent()) {
        for (Map.Entry<String, Object> property : entity
            .straightGetProperties().entrySet()) {
          if (property.getValue() != null
              && !(property.getValue() instanceof Collection && ((Collection) property
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
  public IEntity merge(IEntity entity, MergeMode mergeMode) {
    return merge(entity, mergeMode, new HashMap<IEntity, IEntity>());
  }

  @SuppressWarnings("unchecked")
  private IEntity merge(IEntity entity, MergeMode mergeMode,
      Map<IEntity, IEntity> alreadyMerged) {
    if (alreadyMerged.containsKey(entity)) {
      return alreadyMerged.get(entity);
    }
    boolean dirtRecorderWasEnabled = dirtRecorder.isEnabled();
    try {
      dirtRecorder.setEnabled(false);
      IEntity registeredEntity = getRegisteredEntity(entity.getContract()
          .getName(), entity.getId());
      if (registeredEntity == null) {
        registeredEntity = entity.clone(true);
        entityRegistry.register(registeredEntity);
        dirtRecorder.register(registeredEntity, null);
      }
      alreadyMerged.put(entity, registeredEntity);
      if (mergeMode == MergeMode.MERGE_KEEP) {
        return registeredEntity;
      }
      Map sessionDirtyProperties = dirtRecorder
          .getChangedProperties(registeredEntity);
      boolean dirtyInSession = (sessionDirtyProperties != null && (!sessionDirtyProperties
          .isEmpty()));
      if (mergeMode == MergeMode.MERGE_CLEAN_EAGER
          || mergeMode == MergeMode.MERGE_CLEAN_INITIALIZED
          || (dirtyInSession || (!registeredEntity.getVersion().equals(
              entity.getVersion())))) {
        cleanDirtyProperties(registeredEntity);
        Map<String, Object> entityProperties = entity.straightGetProperties();
        Map<String, Object> registeredEntityProperties = registeredEntity
            .straightGetProperties();
        Map<String, Object> mergedProperties = new HashMap<String, Object>();
        for (Map.Entry<String, Object> property : entityProperties.entrySet()) {
          if (property.getValue() instanceof IEntity) {
            if (mergeMode == MergeMode.MERGE_CLEAN_INITIALIZED
                && !isInitialized((IEntity) property.getValue())) {
              if (registeredEntityProperties.get(property.getKey()) == null) {
                mergedProperties.put(property.getKey(), property.getValue());
              }
            } else {
              mergedProperties.put(property.getKey(), merge((IEntity) property
                  .getValue(), mergeMode, alreadyMerged));
            }
          } else if (property.getValue() instanceof Collection) {
            if (mergeMode == MergeMode.MERGE_CLEAN_INITIALIZED
                && !isInitialized((Collection) property.getValue())) {
              if (registeredEntityProperties.get(property.getKey()) == null) {
                mergedProperties.put(property.getKey(), property.getValue());
              }
            } else {
              Collection<IEntity> registeredCollection = (Collection<IEntity>) registeredEntityProperties
                  .get(property.getKey());
              if (registeredCollection == null
                  || (registeredCollection instanceof PersistentCollection)) {
                if (property.getValue() instanceof Set) {
                  registeredCollection = collectionFactory
                      .createEntityCollection(Set.class);
                } else if (property.getValue() instanceof List) {
                  registeredCollection = collectionFactory
                      .createEntityCollection(List.class);
                }
              }
              registeredCollection.clear();
              for (IEntity entityCollectionElement : (Collection<IEntity>) property
                  .getValue()) {
                registeredCollection.add(merge(entityCollectionElement,
                    mergeMode, alreadyMerged));
              }
              mergedProperties.put(property.getKey(), registeredCollection);
            }
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

  protected boolean isInitialized(IEntity entity) {
    return true;
  }

  protected boolean isInitialized(Collection collection) {
    return true;
  }

  /**
   * Gets a previously registered entity in this application session.
   * 
   * @param entityContractName
   *          the entity contract name.
   * @param entityId
   *          the identifier of the looked-up entity.
   * @return the registered entity or null.
   */
  private IEntity getRegisteredEntity(String entityContractName, Object entityId) {
    return entityRegistry.get(entityContractName, entityId);
  }

  /**
   * Sets the entityRegistry.
   * 
   * @param entityRegistry
   *          the entityRegistry to set.
   */
  public void setEntityRegistry(IEntityRegistry entityRegistry) {
    this.entityRegistry = entityRegistry;
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

  private void cleanDirtyProperties(IEntity entity) {
    dirtRecorder.resetChangedProperties(entity, null);
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
   * {@inheritDoc}
   */
  public void commitUnitOfWork() {
    if (!unitOfWork.isActive()) {
      throw new ApplicationSessionException(
          "Cannot commit a unit of work that has not begun.");
    }
    Map<IEntity, IEntity> alreadyMerged = new HashMap<IEntity, IEntity>();
    for (IEntity entityToMergeBack : entitiesToMergeBack) {
      merge(entityToMergeBack, MergeMode.MERGE_CLEAN_LAZY, alreadyMerged);
    }
    unitOfWork.commit();
    entitiesToMergeBack = null;
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
   * {@inheritDoc}
   */
  public void recordAsSynchronized(IEntity flushedEntity) {
    if (unitOfWork.isActive()) {
      entitiesToMergeBack.add(flushedEntity);
    }
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
  public List<IEntity> cloneInUnitOfWork(List<IEntity> entities) {
    List<IEntity> uowEntities = new ArrayList<IEntity>();
    Map<IEntity, IEntity> alreadyMerged = new HashMap<IEntity, IEntity>();
    for (IEntity entity : entities) {
      uowEntities.add(cloneInUnitOfWork(entity, alreadyMerged));
    }
    return uowEntities;
  }

  @SuppressWarnings("unchecked")
  private IEntity cloneInUnitOfWork(IEntity entity,
      Map<IEntity, IEntity> alreadyMerged) {
    if (alreadyMerged.containsKey(entity)) {
      return alreadyMerged.get(entity);
    }
    Map<String, Object> dirtyProperties = dirtRecorder
        .getChangedProperties(entity);
    if (dirtyProperties == null) {
      dirtyProperties = new HashMap<String, Object>();
    }
    IEntity uowEntity = entity.clone(true);
    alreadyMerged.put(entity, uowEntity);
    Map<String, Object> entityProperties = entity.straightGetProperties();
    for (Map.Entry<String, Object> property : entityProperties.entrySet()) {
      if (property.getValue() instanceof IEntity) {
        uowEntity.straightSetProperty(property.getKey(), cloneInUnitOfWork(
            (IEntity) property.getValue(), alreadyMerged));
      } else if (property.getValue() instanceof Collection) {
        Collection<IEntity> uowEntityCollection = createUnitOfWorkEntityCollection((Collection) property
            .getValue());
        for (IEntity entityCollectionElement : (Collection<IEntity>) property
            .getValue()) {
          uowEntityCollection.add(cloneInUnitOfWork(entityCollectionElement,
              alreadyMerged));
        }
        uowEntityCollection = wrapUnitOfWorkEntityCollection(entity,
            uowEntityCollection, (Collection) dirtyProperties.get(property
                .getKey()), property.getKey());
        uowEntity.straightSetProperty(property.getKey(), uowEntityCollection);
      }
    }
    unitOfWork.register(uowEntity, new HashMap<String, Object>(dirtRecorder
        .getChangedProperties(entity)));
    return uowEntity;
  }

  private Collection<IEntity> createUnitOfWorkEntityCollection(
      Collection collection) {
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
   * Gives a chance to the session to wrap a collection before making it part of
   * the unit of work.
   * 
   * @param entity
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
  @SuppressWarnings("unused")
  protected Collection<IEntity> wrapUnitOfWorkEntityCollection(IEntity entity,
      Collection<IEntity> transientCollection,
      Collection<IEntity> snapshotCollection, String role) {
    return transientCollection;
  }

  /**
   * Sets the unitOfWork.
   * 
   * @param unitOfWork
   *          the unitOfWork to set.
   */
  public void setUnitOfWork(IEntityUnitOfWork unitOfWork) {
    this.unitOfWork = unitOfWork;
  }

  /**
   * Sets the collectionFactory.
   * 
   * @param collectionFactory
   *          the collectionFactory to set.
   */
  public void setCollectionFactory(IEntityCollectionFactory collectionFactory) {
    this.collectionFactory = collectionFactory;
  }

  /**
   * Gets the entitiesToMergeBack.
   * 
   * @return the entitiesToMergeBack.
   */
  protected Set<IEntity> getEntitiesToMergeBack() {
    return entitiesToMergeBack;
  }

}
