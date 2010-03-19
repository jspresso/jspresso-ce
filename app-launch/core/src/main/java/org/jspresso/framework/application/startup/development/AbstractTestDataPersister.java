/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.model.persistence.hibernate.EntityProxyInterceptor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;


/**
 * A utility class used to persist some test data.
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
   * Creates and persist the test data.
   */
  public abstract void persistTestData();

  /**
   * Creates a component instance.
   * 
   * @param <T>
   *            the actual component type.
   * @param componentContract
   *            the component contract.
   * @return the created component.
   */
  protected <T extends IComponent> T createComponentInstance(Class<T> componentContract) {
    return entityFactory.createComponentInstance(componentContract);
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
}
