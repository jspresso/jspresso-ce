/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.startup.development;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IEntityFactory;
import com.d2s.framework.model.persistence.hibernate.EntityProxyInterceptor;

/**
 * A utility class used to persist some test data.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractTestDataPersister {

  private IEntityFactory    entityFactory;
  private HibernateTemplate hibernateTemplate;

  /**
   * Constructs a new <code>AbstractTestDataPersister</code> instance.
   * 
   * @param beanFactory
   *            the spring bean factory to use.
   */
  public AbstractTestDataPersister(BeanFactory beanFactory) {
    // use basicEntityFactory instead of entityFactory so that the created
    // beans do not get registered in the session.
    entityFactory = (IEntityFactory) beanFactory.getBean("basicEntityFactory");

    hibernateTemplate = (HibernateTemplate) beanFactory
        .getBean("hibernateTemplate");
    EntityProxyInterceptor entityInterceptor = new EntityProxyInterceptor();
    entityInterceptor.setEntityFactory(entityFactory);
    hibernateTemplate.setEntityInterceptor(entityInterceptor);
  }

  /**
   * Creates an entity instance.
   * 
   * @param <T>
   *            the actual entity type.
   * @param entityContract
   *            the entity contract.
   * @return the created entity.
   */
  protected <T extends IEntity> T createEntityInstance(Class<T> entityContract) {
    return entityFactory.createEntityInstance(entityContract);
  }

  /**
   * Persists or update an entity.
   * 
   * @param entity
   *            the entity to persist or update.
   */
  protected void saveOrUpdate(IEntity entity) {
    hibernateTemplate.saveOrUpdate(entity);
  }

  /**
   * Creates and persist the test data.
   */
  public abstract void persistTestData();
}
