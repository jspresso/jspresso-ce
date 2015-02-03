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
package org.jspresso.framework.application.backend.persistence.mongo;

import java.io.Serializable;
import java.lang.reflect.Proxy;

import com.mongodb.DBObject;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.event.AbstractDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.MongoMappingEvent;

import org.jspresso.framework.application.backend.BackendControllerHolder;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.model.component.ILifecycleCapable;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.model.entity.IEntityLifecycleHandler;
import org.jspresso.framework.security.UserPrincipal;

/**
 * Entity Mongo lifecycle listener.
 *
 * @author Vincent Vandenschrick
 */
public class JspressoMongoEventListener extends AbstractMongoEventListener<IEntity> {

  private static final Logger LOG = LoggerFactory.getLogger(JspressoMongoEventListener.class);

  private IEntityFactory entityFactory;

  /**
   * On before convert.
   *
   * @param entity
   *     the entity
   */
  @Override
  public void onBeforeConvert(IEntity entity) {
    super.onBeforeConvert(entity);
    // Version has already been changed.
    if (entity.isPersistent() && entity.getVersion() > 0) {
      ((ILifecycleCapable) entity).onUpdate(getEntityFactory(), getPrincipal(), getEntityLifecycleHandler());
    } else {
      ((ILifecycleCapable) entity).onPersist(getEntityFactory(), getPrincipal(), getEntityLifecycleHandler());
    }
  }

  /**
   * On before save.
   *
   * @param entity
   *     the entity
   * @param dbo
   *     the dbo
   */
  @Override
  public void onBeforeSave(IEntity entity, DBObject dbo) {
    super.onBeforeSave(entity, dbo);
  }

  /**
   * On after save.
   *
   * @param entity
   *     the entity
   * @param dbo
   *     the dbo
   */
  @Override
  public void onAfterSave(IEntity entity, DBObject dbo) {
    super.onAfterSave(entity, dbo);
    getBackendController().recordAsSynchronized(entity);
  }

  /**
   * On after load.
   *
   * @param dbo
   *     the dbo
   */
  @Override
  public void onAfterLoad(DBObject dbo) {
    super.onAfterLoad(dbo);
  }

  /**
   * On after convert.
   *
   * @param dbo
   *     the dbo
   * @param entity
   *     the entity
   */
  @Override
  public void onAfterConvert(DBObject dbo, IEntity entity) {
    super.onAfterConvert(dbo, entity);
    getBackendController().registerEntity(entity);
    ((ILifecycleCapable) entity).onLoad();
  }

  /**
   * On before delete.
   *
   * @param entityType
   *     the entity type
   * @param dbo
   *     the dbo
   */
  public void onBeforeDelete(@SuppressWarnings("unused") Class<?> entityType, DBObject dbo) {
    super.onBeforeDelete(dbo);
  }

  /**
   * On after delete.
   *
   * @param entityType
   *     the entity type
   * @param dbo
   *     the dbo
   */
  public void onAfterDelete(Class<?> entityType, DBObject dbo) {
    super.onAfterDelete(dbo);
    Class<?> entityContract = entityType;
    if (Proxy.isProxyClass(entityType)) {
      for (Class<?> superInterface : entityType.getInterfaces()) {
        if (IEntity.class.isAssignableFrom(superInterface)) {
          entityContract = superInterface;
        }
      }
    }
    IEntity entity = getEntity(entityContract.getName(), (Serializable) dbo.get("_id"));
    if (entity != null) {
      ((ILifecycleCapable) entity).onDelete(getEntityFactory(), getPrincipal(), getEntityLifecycleHandler());
    }
  }

  /**
   * Gets entity.
   *
   * @param entityName
   *     the entity name
   * @param id
   *     the id
   * @return the entity
   */
  @SuppressWarnings("unchecked")
  protected IEntity getEntity(String entityName, Serializable id) {
    IEntity registeredEntity = null;
    try {
      if (getBackendController().isUnitOfWorkActive()) {
        registeredEntity = getBackendController().getUnitOfWorkEntity((Class<? extends IEntity>) Class.forName(
            entityName), id);
      } else {
        registeredEntity = getBackendController().getRegisteredEntity((Class<? extends IEntity>) Class.forName(
            entityName), id);
        if (registeredEntity instanceof HibernateProxy) {
          HibernateProxy proxy = (HibernateProxy) registeredEntity;
          LazyInitializer li = proxy.getHibernateLazyInitializer();
          registeredEntity = (IEntity) li.getImplementation();
        }
      }
    } catch (ClassNotFoundException ex) {
      LOG.error("Class for entity {} was not found", entityName, ex);
    }
    return registeredEntity;
  }

  /**
   * Gets the getBackendController().
   *
   * @return the backendController.
   */
  protected IBackendController getBackendController() {
    return BackendControllerHolder.getCurrentBackendController();
  }

  /**
   * {@inheritDoc}
   *
   * @return the entity lifecycle handler
   */
  protected IEntityLifecycleHandler getEntityLifecycleHandler() {
    return getBackendController();
  }

  /**
   * Gets the principal of the application session.
   * <p/>
   * {@inheritDoc}
   *
   * @return the principal
   */
  protected UserPrincipal getPrincipal() {
    return getBackendController().getApplicationSession().getPrincipal();
  }

  /**
   * Gets entity factory.
   *
   * @return the entity factory
   */
  protected IEntityFactory getEntityFactory() {
    return entityFactory;
  }

  /**
   * Sets entity factory.
   *
   * @param entityFactory
   *     the entity factory
   */
  public void setEntityFactory(IEntityFactory entityFactory) {
    this.entityFactory = entityFactory;
  }

  /**
   * On application event.
   *
   * @param event
   *     the event
   */
  @Override
  public void onApplicationEvent(MongoMappingEvent<?> event) {
    if (event instanceof AbstractDeleteEvent) {
      Class<?> eventDomainType = ((AbstractDeleteEvent) event).getType();
      if (eventDomainType != null && IEntity.class.isAssignableFrom(eventDomainType)) {
        if (event instanceof BeforeDeleteEvent) {
          onBeforeDelete(eventDomainType, event.getDBObject());
        }
        if (event instanceof AfterDeleteEvent) {
          onAfterDelete(eventDomainType, event.getDBObject());
        }
      }
    } else {
      super.onApplicationEvent(event);
    }
  }
}
