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

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import org.springframework.data.mongodb.core.MongoTemplate;

import org.jspresso.framework.model.component.ComponentException;
import org.jspresso.framework.model.entity.EntityHelper;
import org.jspresso.framework.model.entity.IEntity;

/**
 * A proxy for referenced Jspresso entities in order to support lazy loading.
 *
 * @author Vincent Vandenschrick
 */
public class JspressoMongoEntityProxyHandler implements InvocationHandler {

  private static final Object NULL_TARGET = new Object();

  private Serializable   id;
  private Class<IEntity> entityContract;
  private MongoTemplate  mongo;
  private Object         target;

  /**
   * Instantiates a new Jspresso mongo entity proxy handler.
   *
   * @param id
   *     the id
   * @param entityContract
   *     the entity contract
   * @param mongo
   *     the mongo
   */
  public JspressoMongoEntityProxyHandler(Serializable id, Class<IEntity> entityContract, MongoTemplate mongo) {
    this.id = id;
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
      case "hashCode":
        return computeHashCode();
      case "toString":
        return computeToString();
      case "equals":
        return computeEquals(proxy, args[0]);
      case "getComponentContract":
        return entityContract;
      case "isInitialized":
        return target != null;
      case "isNull":
        return target == NULL_TARGET;
      case "initialize":
        initializeIfNecessary();
        return null;
      default:
        return invokeTargetMethod(method, args);
    }
  }

  private Object computeToString() {
    checkNotNull();
    if (target != null) {
      return target.toString();
    }
    return "JspressoMongoProxy::" + entityContract.getName() + "[" + id + "]";
  }

  private boolean computeEquals(Object proxy, Object another) {
    if (proxy == another) {
      return true;
    }
    if (another instanceof IEntity) {
      return ((IEntity) another).getComponentContract().equals(entityContract) && ((IEntity) another).getId().equals(
          id);
    }
    return false;
  }

  private void checkNotNull() {
    if (target == NULL_TARGET) {
      throw new NullPointerException("Entity proxy points to a null entity.");
    }
  }

  private Object computeHashCode() {
    checkNotNull();
    return id.hashCode();
  }

  /**
   * Invoke method on .
   * <p/>
   * {@inheritDoc}
   */
  private Object invokeTargetMethod(Method method, Object... args) throws NoSuchMethodException {
    initializeIfNecessary();
    checkNotNull();
    try {
      return entityContract.getMethod(method.getName(), method.getParameterTypes()).invoke(target, args);
    } catch (IllegalArgumentException | IllegalAccessException e) {
      throw new ComponentException(method.toString() + " is not supported on the component " + entityContract);
    } catch (InvocationTargetException e) {
      if (e.getCause() instanceof RuntimeException) {
        throw (RuntimeException) e.getCause();
      }
      throw new ComponentException(e.getCause());
    }
  }

  private void initializeIfNecessary() {
    if (target == null) {
      target = findEntityFor(entityContract);
      if (target == null) {
        target = NULL_TARGET;
      }
    }
  }

  @SuppressWarnings("unchecked")
  private IEntity findEntityFor(Class<IEntity> rootEntityContract) {
    IEntity entity = mongo.findById(id, rootEntityContract);
    if (entity == null) {
      Collection<Class<IEntity>> entitySubContracts = EntityHelper.getEntitySubContracts(rootEntityContract);
      for (Class<IEntity> entitySubContract : entitySubContracts) {
        if (entity == null) {
          entity = findEntityFor(entitySubContract);
          if (entity != null) {
            entityContract = entitySubContract;
          }
        }
      }
    }
    return entity;
  }
}
