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
package org.jspresso.framework.application.startup.development;

import java.util.List;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import org.jspresso.framework.application.backend.BackendControllerHolder;
import org.jspresso.framework.application.backend.persistence.mongo.MongoBackendController;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityFactory;

/**
 * A utility class used to persist some test data.
 *
 * @author Vincent Vandenschrick
 */
public abstract class AbstractMongoTestDataPersister {

  private final BeanFactory beanFactory;

  /**
   * Constructs a new {@code AbstractMongoTestDataPersister} instance.
   *
   * @param beanFactory
   *          the spring bean factory to use.
   */
  public AbstractMongoTestDataPersister(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  /**
   * Creates and persist the test data.
   */
  public final void persistTestData() {
    MongoBackendController hbc = (MongoBackendController) BackendControllerHolder
        .getThreadBackendController();
    boolean wasNull = false;
    if (hbc == null) {
      wasNull = true;
      hbc = (MongoBackendController) beanFactory
          .getBean("applicationBackController");
      BackendControllerHolder.setThreadBackendController(hbc);
    }
    try {
      createAndPersistTestData();
    } finally {
      hbc.cleanupRequestResources();
      if (wasNull) {
        BackendControllerHolder.setThreadBackendController(null);
      }
    }
  }

  /**
   * Creates and persist the test data. This method must be overridden in
   * subclasses.
   */
  protected abstract void createAndPersistTestData();

  /**
   * Creates a component instance.
   *
   * @param <T>
   *          the actual component type.
   * @param componentContract
   *          the component contract.
   * @return the created component.
   */
  protected <T extends IComponent> T createComponentInstance(
      Class<T> componentContract) {
    return getEntityFactory().createComponentInstance(componentContract);
  }

  /**
   * Creates an entity instance.
   *
   * @param <T>
   *          the actual entity type.
   * @param entityContract
   *          the entity contract.
   * @return the created entity.
   */
  protected <T extends IEntity> T createEntityInstance(Class<T> entityContract) {
    return getEntityFactory().createEntityInstance(entityContract);
  }

  /**
   * Persists or update an entity.
   *
   * @param entity
   *          the entity to persist or update.
   */
  protected void saveOrUpdate(final IEntity entity) {
    final MongoBackendController mongoBackendController = getBackendController();
    mongoBackendController.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        getMongoTemplate().save(mongoBackendController.cloneInUnitOfWork(entity));
      }
    });
  }

  /**
   * Query entities by criteria.
   *
   * @param query           the Mongo query.
   * @param entityType the entity type
   * @return the entity list.
   */
  protected List<?> findByQuery(Query query, Class<?> entityType) {
    return getMongoTemplate().find(query, entityType);
  }

  /**
   * Gets the backend controller.
   *
   * @return the backend controller.
   */
  protected MongoBackendController getBackendController() {
    return (MongoBackendController) BackendControllerHolder
        .getCurrentBackendController();
  }

  /**
   * Gets the entityFactory.
   *
   * @return the entityFactory.
   */
  protected IEntityFactory getEntityFactory() {
    return getBackendController().getEntityFactory();
  }

  /**
   * Gets the hibernateSession.
   *
   * @return the hibernateSession.
   */
  protected MongoTemplate getMongoTemplate() {
    return getBackendController().getMongoTemplate();
  }
}
