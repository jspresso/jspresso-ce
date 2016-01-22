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
import java.lang.reflect.Proxy;

import com.mongodb.DBObject;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.event.AbstractDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterLoadEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
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
   * @param event
   *     the event
   */
  @Override
  public void onBeforeConvert(BeforeConvertEvent<IEntity> event) {
    IEntity entity = event.getSource();
    super.onBeforeConvert(event);
    // Version has already been changed.
    if (entity.isPersistent() && entity.getVersion() > 0) {
      ((ILifecycleCapable) entity).onUpdate(getEntityFactory(), getPrincipal(), getEntityLifecycleHandler());
    } else {
      ((ILifecycleCapable) entity).onPersist(getEntityFactory(), getPrincipal(), getEntityLifecycleHandler());
    }
  }

  /**
   * On after save.
   *
   * @param event
   *     the event
   */
  @Override
  public void onAfterSave(AfterSaveEvent<IEntity> event) {
    super.onAfterSave(event);
    IEntity entity = event.getSource();
    getBackendController().recordAsSynchronized(entity);
  }

  /**
   * On after convert.
   *
   * @param event
   *     the event
   */
  @Override
  public void onAfterConvert(AfterConvertEvent<IEntity> event) {
    super.onAfterConvert(event);
    IEntity entity = event.getSource();
    getBackendController().registerEntity(entity);
    ((ILifecycleCapable) entity).onLoad();
  }

  /**
   * On after delete.
   *
   * @param event
   *     the event
   */
  @Override
  public void onAfterDelete(AfterDeleteEvent<IEntity> event) {
    super.onAfterDelete(event);
    DBObject dbo = event.getSource();
    Class<?> entityType = event.getType();
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
}
