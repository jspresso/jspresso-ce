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
package org.jspresso.framework.model.persistence.mongo;

import static org.springframework.data.mongodb.core.query.Criteria.*;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import org.jspresso.framework.model.entity.EntityHelper;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.util.exception.NestedRuntimeException;

/**
 * Collection proxy invocation handler.
 *
 * @author Vincent Vandenschrick
 */
public abstract class JspressoMongoEntityCollectionHandler implements InvocationHandler {

  private Collection<Serializable> ids;
  private Class<IEntity>           entityContract;
  private MongoTemplate            mongo;
  private Collection<IEntity>      target;


  /**
   * Instantiates a new Jspresso mongo entity list invocation handler.
   *
   * @param ids
   *     the ids
   * @param entityContract
   *     the entity contract
   * @param mongo
   *     the mongo
   */
  public JspressoMongoEntityCollectionHandler(Collection<Serializable> ids, Class<IEntity> entityContract,
                                              MongoTemplate mongo) {
    this.ids = ids;
    this.entityContract = entityContract;
    this.mongo = mongo;
  }

  /**
   * Invoke object.
   *
   * @param proxy
   *     the proxy
   * @param method
   *     the method
   * @param args
   *     the args
   * @return the object
   *
   * @throws Throwable
   *     the throwable
   */
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    String methodName = method.getName()/* .intern() */;
    switch (methodName) {
      case "size":
        return target != null ? target.size() : ids.size();
      case "toString":
        return computeToString();
      case "isInitialized":
        return target != null;
      case "initialize":
        initializeIfNecessary();
        return null;
      default:
        return invokeTargetMethod(method, args);
    }
  }

  private Object computeToString() {
    if (target != null) {
      return target.toString();
    }
    StringBuilder toString = new StringBuilder("JspressoMongoCollectionProxy::").append(entityContract.getName())
                                                                                .append("[");
    for (Serializable id : ids) {
      toString.append(id).append(", ");
    }
    return toString.append("]").toString();
  }

  /**
   * Invoke method on .
   * <p/>
   * {@inheritDoc}
   */
  private Object invokeTargetMethod(Method method, Object... args) throws NoSuchMethodException {
    initializeIfNecessary();
    try {
      return getCollectionInterface().getMethod(method.getName(), method.getParameterTypes()).invoke(target, args);
    } catch (IllegalArgumentException | IllegalAccessException e) {
      throw new NestedRuntimeException(method.toString() + " is not supported on the Set interface");
    } catch (InvocationTargetException e) {
      if (e.getCause() instanceof RuntimeException) {
        throw (RuntimeException) e.getCause();
      }
      throw new NestedRuntimeException(e.getCause());
    }
  }

  /**
   * Gets collection interface.
   *
   * @return the collection interface
   */
  protected abstract Class<? extends Collection> getCollectionInterface();

  private void initializeIfNecessary() {
    if (target == null) {
      if (ids == null || ids.isEmpty()) {
        target = createTargetCollection(Collections.<IEntity>emptySet());
      } else {
        Collection<IEntity> foundEntities = findEntitiesFor(entityContract);
        Map<Serializable, IEntity> foundEntitiesById = new HashMap<>();
        for (IEntity foundEntity : foundEntities) {
          foundEntitiesById.put(foundEntity.getId(), foundEntity);
        }
        List<IEntity> sourceCollection = new ArrayList<>(ids.size());
        for (Serializable id : ids) {
          sourceCollection.add(foundEntitiesById.get(id));
        }
        target = createTargetCollection(sourceCollection);
      }
    }
  }

  private Collection<IEntity> findEntitiesFor(Class<IEntity> rootEntityContract) {
    List<IEntity> entities = mongo.find(new Query().addCriteria(where("_id").in(ids)), rootEntityContract);
    if (entities.size() != ids.size()) {
      Collection<Class<IEntity>> entitySubContracts = EntityHelper.getEntitySubContracts(rootEntityContract);
      for (Class<IEntity> entitySubContract : entitySubContracts) {
        if (entities.size() != ids.size()) {
          entities.addAll(findEntitiesFor(entitySubContract));
        }
      }
    }
    return entities;
  }

  /**
   * Create target collection.
   *
   * @param sourceCollection
   *     the source collection
   * @return the collection
   */
  protected abstract Collection<IEntity> createTargetCollection(Collection<IEntity> sourceCollection);
}
