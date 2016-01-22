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
package org.jspresso.framework.model.persistence.hibernate;

import java.io.Serializable;
import java.util.Collection;

import org.hibernate.EmptyInterceptor;
import org.hibernate.EntityMode;
import org.hibernate.Hibernate;
import org.hibernate.type.Type;

import org.jspresso.framework.model.component.ILifecycleCapable;
import org.jspresso.framework.model.entity.EntityException;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.model.entity.IEntityLifecycleHandler;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.framework.util.bean.PropertyHelper;

/**
 * This hibernate interceptor enables hibernate to handle entities which are
 * implemented by proxies. Its main goal is to provide hibernate with new
 * instances of entities based on their core interface contract.
 *
 * @author Vincent Vandenschrick
 */
public class EntityProxyInterceptor extends EmptyInterceptor {

  private static final long serialVersionUID = -7357726538191018694L;

  private IEntityFactory entityFactory;

  /**
   * Returns the fully qualified name of the entity passed as parameter.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public String getEntityName(Object object) {
    if (object instanceof IEntity) {
      return ((IEntity) object).getComponentContract().getName();
    }
    return null;
  }

  /**
   * Instantiates a new entity (proxy) based on the entityDescriptor returned
   * from the bean factory. The entityName which is the fully qualified name of
   * the entity interface contract is used as the lookup key in the bean
   * factory.
   * <p/>
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public Object instantiate(String entityName, EntityMode entityMode, Serializable id) {
    try {
      return entityFactory.createEntityInstance((Class<? extends IEntity>) Class.forName(entityName), id);
    } catch (ClassNotFoundException ex) {
      throw new EntityException(ex);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
    if (entity instanceof ILifecycleCapable) {
      ((ILifecycleCapable) entity).onDelete(getEntityFactory(), getPrincipal(), getEntityLifecycleHandler());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
    boolean stateUpdated = false;
    if (entity instanceof IEntity && entity instanceof ILifecycleCapable) {
      if (((ILifecycleCapable) entity).onPersist(getEntityFactory(), getPrincipal(), getEntityLifecycleHandler())) {
        extractState((IEntity) entity, propertyNames, state);
        stateUpdated = true;
      }
    }
    return stateUpdated;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Boolean isTransient(Object entity) {
    if (!Hibernate.isInitialized(entity)) {
      return Boolean.FALSE;
    }
    return super.isTransient(entity);
  }

  /**
   * Sets the entityFactory.
   *
   * @param entityFactory
   *     the entityFactory to set.
   */
  public void setEntityFactory(IEntityFactory entityFactory) {
    this.entityFactory = entityFactory;
  }

  /**
   * Gets the entityFactory.
   *
   * @return the entityFactory.
   */
  protected IEntityFactory getEntityFactory() {
    return entityFactory;
  }

  /**
   * Gets the lifecycle handler.
   *
   * @return the entity lifecycle handler.
   */
  protected IEntityLifecycleHandler getEntityLifecycleHandler() {
    return null;
  }

  /**
   * Gets the principal owning the session.
   *
   * @return the principal owning the session.
   */
  protected UserPrincipal getPrincipal() {
    return null;
  }

  /**
   * Hibernate uses "extra" properties to cope with relationships e.g.
   * _[collectionName]BackRef or _[collectionName]IndexBackRef Those properties
   * are not known by the entity and thus cannot be extracted.
   *
   * @param propertyName
   *     the property name to test.
   * @return true if this property is an Hibernate internal managed one.
   */
  protected boolean isHibernateInternal(String propertyName) {
    return propertyName.startsWith("_");
  }

  private void extractState(IEntity entity, String[] propertyNames, Object... state) {
    for (int i = 0; i < propertyNames.length; i++) {
      String propertyName = propertyNames[i];
      if (!isHibernateInternal(propertyName)) {
        Object property = entity.straightGetProperty(PropertyHelper.fromJavaBeanPropertyName(propertyName));
        if (!(property instanceof Collection<?>)) {
          state[i] = property;
        }
      }
    }
  }
}
