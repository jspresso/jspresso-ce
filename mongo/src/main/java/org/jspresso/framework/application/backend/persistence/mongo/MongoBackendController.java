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
package org.jspresso.framework.application.backend.persistence.mongo;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import org.jspresso.framework.application.backend.AbstractBackendController;
import org.jspresso.framework.application.backend.BackendException;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IRelationshipEndPropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.persistence.mongo.JspressoMongoEntityProxy;
import org.jspresso.framework.model.persistence.mongo.JspressoMongoProxy;

/**
 * This is the default Jspresso implementation of MongoDB-based backend
 * controller.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision : 10441 $
 */
public class MongoBackendController extends AbstractBackendController {

  private MongoTemplate mongoTemplate;
  private static final Logger LOG = LoggerFactory.getLogger(MongoBackendController.class);
  private Set<IEntity> flushedEntities;

  /**
   * Flushes all changes to Mongo which is not transactional anyway...
   *
   * @param readOnly
   *     the read only
   */
  @Override
  public void beforeCommit(boolean readOnly) {
    // flushed entities must be recorded, because MongoDB is not transactional.
    if (flushedEntities == null) {
      flushedEntities = new HashSet<>();
    }
    for (Map<Serializable, IEntity> uowEntities : getUnitOfWorkEntities().values()) {
      for (IEntity uowEntity : uowEntities.values()) {
        if (isEntityRegisteredForDeletion(uowEntity)) {
          if (readOnly) {
            throw new BackendException("The transaction is read-only but would lead to a flush in the database.");
          }
          getMongoTemplate().remove(uowEntity);
          flushedEntities.add(uowEntity);
        } else if (isDirtyInDepth(uowEntity)) {
          if (readOnly) {
            throw new BackendException("The transaction is read-only but would lead to a flush in the database.");
          }
          getMongoTemplate().save(uowEntity);
          flushedEntities.add(uowEntity);
        }
      }
    }
    super.beforeCommit(readOnly);
  }

  /**
   * Cleans flushed entities.
   *
   * @param status
   *     the status
   */
  @Override
  public void afterCompletion(int status) {
    if (status == STATUS_ROLLED_BACK) {
      // In case of rollback, we must still merge back flushed entities, since MongoDB is not transactional.
      if (flushedEntities != null) {
        for (IEntity updatedEntity : flushedEntities) {
          recordAsSynchronized(updatedEntity);
        }
        mergeBackFlushedEntities(flushedEntities);
      }
    }
    flushedEntities = null;
    super.afterCompletion(status);
  }

  /**
   * Initialize property if needed.
   *
   * @param componentOrEntity
   *     the component or entity
   * @param propertyName
   *     the property name
   */
  @Override
  public void initializePropertyIfNeeded(IComponent componentOrEntity, String propertyName) {
    Object propertyValue = componentOrEntity.straightGetProperty(propertyName);
    if (!isInitialized(propertyValue)) {
      ((JspressoMongoProxy) propertyValue).initialize();
      if (propertyValue instanceof JspressoMongoEntityProxy) {
        if (((JspressoMongoEntityProxy) propertyValue).isNull()) {
          componentOrEntity.straightSetProperty(propertyName, null);
        }
      } else if (propertyValue instanceof Collection<?>) {
        for (Iterator<?> ite = ((Collection<?>) propertyValue).iterator(); ite.hasNext(); ) {
          Object collectionElement = ite.next();
          if (collectionElement instanceof IEntity) {
            if (isEntityRegisteredForDeletion((IEntity) collectionElement)) {
              ite.remove();
            }
          }
        }
      }
    }
  }

  /**
   * Is initialized.
   *
   * @param objectOrProxy
   *     the object or proxy
   * @return the boolean
   */
  @Override
  public boolean isInitialized(Object objectOrProxy) {
    return !(objectOrProxy instanceof JspressoMongoProxy) || ((JspressoMongoProxy) objectOrProxy).isInitialized();
  }

  /**
   * Finds an entity by ID.
   *
   * @param <T>
   *     the entity type to return
   * @param id
   *     the entity ID.
   * @param mergeMode
   *     the merge mode to use when merging back retrieved entities or null
   *     if no merge is requested.
   * @param clazz
   *     the type of the entity.
   * @return the found entity
   */
  @SuppressWarnings("unchecked")
  public <T extends IEntity> T findById(final Serializable id, final EMergeMode mergeMode,
                                        final Class<? extends T> clazz) {
    T res;
    if (isUnitOfWorkActive()) {
      // merge mode must be ignored if a transaction is pre-existing.
      res = cloneInUnitOfWork(getMongoTemplate().findById(id, clazz));
    } else {
      // merge mode is used for merge to occur inside the transaction.
      res = getTransactionTemplate().execute(new TransactionCallback<T>() {

        @SuppressWarnings("unchecked")
        @Override
        public T doInTransaction(TransactionStatus status) {
          return merge(getMongoTemplate().findById(id, clazz), mergeMode);
        }
      });
    }
    return res;
  }

  /**
   * Search Mongo using query. The result is then merged into session unless the method is called into a
   * pre-existing transaction, in which case, the merge mode is ignored and the merge is not performed.
   *
   * @param <T>
   *     the entity type to return
   * @param query
   *     the Mongo query.
   * @param mergeMode
   *     the merge mode to use when merging back retrieved entities or null
   *     if no merge is requested.
   * @param clazz
   *     the type of the entity.
   * @return the first found entity or null
   */
  public <T extends IEntity> T findFirstByQuery(Query query, EMergeMode mergeMode, Class<? extends T> clazz) {
    List<T> ret = findByQuery(query, 0, 1, mergeMode, clazz);
    if (ret != null && !ret.isEmpty()) {
      return ret.get(0);
    }
    return null;
  }

  /**
   * Search Mongo using query. The result is then merged into session unless the method is called into a
   * pre-existing transaction, in which case, the merge mode is ignored and the merge is not performed.
   *
   * @param <T>
   *     the entity type to return
   * @param query
   *     the Mongo query.
   * @param mergeMode
   *     the merge mode to use when merging back retrieved entities or null
   *     if no merge is requested.
   * @param clazz
   *     the type of the entity.
   * @return the first found entity or null
   */
  public <T extends IEntity> List<T> findByQuery(final Query query, EMergeMode mergeMode, Class<? extends T> clazz) {
    return findByQuery(query, -1, -1, mergeMode, clazz);
  }

  /**
   * Search Mongo using query. The result is then merged into session unless the method is called into a
   * pre-existing transaction, in which case, the merge mode is ignored and the merge is not performed.
   *
   * @param <T>
   *     the entity type to return
   * @param query
   *     the Mongo query.
   * @param firstResult
   *     the first result rank to retrieve.
   * @param maxResults
   *     the max number of results to retrieve.
   * @param mergeMode
   *     the merge mode to use when merging back retrieved entities or null
   *     if no merge is requested.
   * @param clazz
   *     the type of the entity.
   * @return the first found entity or null
   */
  @SuppressWarnings("UnusedParameters")
  public <T extends IEntity> List<T> findByQuery(final Query query, int firstResult, int maxResults,
                                                 EMergeMode mergeMode, Class<? extends T> clazz) {
    List<T> res;
    if (isUnitOfWorkActive()) {
      // merge mode must be ignored if a transaction is pre-existing, so force
      // to null.

      // This is useless to clone in UOW now that UOW registration is done
      // in onLoad interceptor
      // res = (List<T>) cloneInUnitOfWork(find(query, firstResult,
      // maxResults, null), clazz);

      res = find(query, firstResult, maxResults, null, clazz);
    } else {
      // merge mode is passed for merge to occur inside the transaction.
      res = find(query, firstResult, maxResults, mergeMode, clazz);
    }
    return res;
  }

  private <T extends IEntity> List<T> find(final Query query, final int firstResult, final int maxResults,
                                           final EMergeMode mergeMode, final Class<? extends T> clazz) {
    return getTransactionTemplate().execute(new TransactionCallback<List<T>>() {

      @SuppressWarnings("unchecked")
      @Override
      public List<T> doInTransaction(TransactionStatus status) {
        if (firstResult >= 0) {
          query.skip(firstResult);
        }
        if (maxResults > 0) {
          query.limit(maxResults);
        }
        List<? extends T> entities = getMongoTemplate().find(query, clazz);
        if (mergeMode != null) {
          entities = merge(entities, mergeMode);
        }
        return (List<T>) entities;
      }
    });
  }

  /**
   * Reloads an entity in Mongo.
   *
   * @param entity
   *     the entity to reload.
   */
  @Override
  public void reload(final IEntity entity) {
    if (entity == null) {
      throw new IllegalArgumentException("Passed entity cannot be null");
    }
    // builds a collection of entities to reload.
    Set<IEntity> dirtyReachableEntities = buildReachableDirtyEntitySet(entity);

    if (entity.isPersistent()) {
      getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {

        @Override
        protected void doInTransactionWithoutResult(TransactionStatus status) {
          merge(getMongoTemplate().findById(entity.getId(), getComponentContract(entity)),
              EMergeMode.MERGE_CLEAN_EAGER);
        }
      });
    }

    // traverse the reachable dirty entities to explicitly reload the
    // ones that were not reloaded by the previous pass.
    for (IEntity reachableEntity : dirtyReachableEntities) {
      if (reachableEntity.isPersistent() && isDirty(reachableEntity)) {
        reload(reachableEntity);
      }
    }
  }

  private Set<IEntity> buildReachableDirtyEntitySet(IEntity entity) {
    Set<IEntity> reachableDirtyEntities = new HashSet<>();
    completeReachableDirtyEntities(entity, reachableDirtyEntities, new HashSet<IEntity>());
    return reachableDirtyEntities;
  }

  private void completeReachableDirtyEntities(IEntity entity, Set<IEntity> reachableDirtyEntities,
                                              Set<IEntity> alreadyTraversed) {
    if (alreadyTraversed.contains(entity)) {
      return;
    }
    alreadyTraversed.add(entity);
    if (isDirty(entity)) {
      reachableDirtyEntities.add(entity);
    }
    Map<String, Object> entityProps = entity.straightGetProperties();
    IComponentDescriptor<?> entityDescriptor = getEntityFactory().getComponentDescriptor(getComponentContract(entity));
    for (Map.Entry<String, Object> property : entityProps.entrySet()) {
      Object propertyValue = property.getValue();
      if (propertyValue instanceof IEntity) {
        IPropertyDescriptor propertyDescriptor = entityDescriptor.getPropertyDescriptor(property.getKey());
        if (isInitialized(propertyValue) && propertyDescriptor instanceof IRelationshipEndPropertyDescriptor
            // It's not a master data relationship.
            && ((IRelationshipEndPropertyDescriptor) propertyDescriptor).getReverseRelationEnd() != null) {
          completeReachableDirtyEntities((IEntity) propertyValue, reachableDirtyEntities, alreadyTraversed);
        }
      } else if (propertyValue instanceof Collection<?>) {
        if (isInitialized(propertyValue)) {
          for (Object elt : ((Collection<?>) propertyValue)) {
            if (elt instanceof IEntity) {
              completeReachableDirtyEntities((IEntity) elt, reachableDirtyEntities, alreadyTraversed);
            }
          }
        }
      }
    }
  }

  /**
   * Gets mongo template.
   *
   * @return the mongo template
   */
  public MongoTemplate getMongoTemplate() {
    return mongoTemplate;
  }

  /**
   * Sets mongo template.
   *
   * @param mongoTemplate
   *     the mongo template
   */
  public void setMongoTemplate(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }
}
