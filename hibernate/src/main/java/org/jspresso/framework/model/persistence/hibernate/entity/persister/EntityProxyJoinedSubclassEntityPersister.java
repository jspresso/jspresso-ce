/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.model.persistence.hibernate.entity.persister;

import org.hibernate.EntityMode;
import org.hibernate.cache.access.EntityRegionAccessStrategy;
import org.hibernate.engine.Mapping;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.persister.entity.JoinedSubclassEntityPersister;
import org.jspresso.framework.model.entity.IEntity;

/**
 * Subclassed the hibernate default persister for joined subclasses to
 * workaround a problem with determination of the subclass entity name when
 * using dynamic proxies.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EntityProxyJoinedSubclassEntityPersister extends
    JoinedSubclassEntityPersister {

  /**
   * Constructs a new <code>EntityProxyJoinedSubclassEntityPersister</code>
   * instance.
   * 
   * @param persistentClass
   *          the persistent class.
   * @param cacheAccessStrategy
   *          the cache access strategy.
   * @param factory
   *          the session.
   * @param mapping
   *          the mapping.
   */
  public EntityProxyJoinedSubclassEntityPersister(
      PersistentClass persistentClass,
      EntityRegionAccessStrategy cacheAccessStrategy,
      SessionFactoryImplementor factory, Mapping mapping) {
    super(persistentClass, cacheAccessStrategy, factory, mapping);
  }

  /**
   * Takes a chance to determine the entity persister from the proxy handler
   * contract.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public EntityPersister getSubclassEntityPersister(Object instance,
      SessionFactoryImplementor factory, EntityMode entityMode) {
    if (instance instanceof IEntity) {
      return factory.getEntityPersister(((IEntity) instance)
          .getComponentContract().getName());
    }
    return super.getSubclassEntityPersister(instance, factory, entityMode);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityMode guessEntityMode(Object object) {
    return EntityMode.POJO;
  }
}
