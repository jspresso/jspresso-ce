/*
 * Copyright (c) 2005-2014 Vincent Vandenschrick. All rights reserved.
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

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.util.exception.NestedRuntimeException;

/**
 * Collection proxy invocation handler.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision$
 */
public abstract class JspressoMongoEntityCollectionInvocationHandler implements InvocationHandler {

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
  public JspressoMongoEntityCollectionInvocationHandler(Collection<Serializable> ids,
                                                        Class<IEntity> entityContract, MongoTemplate mongo) {
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
      case "isInitialized":
        return target != null;
      case "initialize":
        initializeIfNecessary();
        return null;
      default:
        return invokeTargetMethod(method, args);
    }
  }

  /**
   * Invoke method on .
   * <p/>
   * {@inheritDoc}
   */
  private Object invokeTargetMethod(Method method, Object... args) throws NoSuchMethodException {
    initializeIfNecessary();
    try {
      return Set.class.getMethod(method.getName(), method.getParameterTypes()).invoke(target, args);
    } catch (IllegalArgumentException | IllegalAccessException e) {
      throw new NestedRuntimeException(method.toString() + " is not supported on the Set interface");
    } catch (InvocationTargetException e) {
      if (e.getCause() instanceof RuntimeException) {
        throw (RuntimeException) e.getCause();
      }
      throw new NestedRuntimeException(e.getCause());
    }
  }

  private void initializeIfNecessary() {
    if (target == null) {
      if (ids == null || ids.isEmpty()) {
        target = createTargetCollection(Collections.<IEntity>emptySet());
      } else {
        target = createTargetCollection(mongo.find(new Query(Criteria.where("_id").in(ids)), entityContract));
      }
    }
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