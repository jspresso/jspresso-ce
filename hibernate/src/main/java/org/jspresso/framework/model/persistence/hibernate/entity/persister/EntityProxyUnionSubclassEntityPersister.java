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
package org.jspresso.framework.model.persistence.hibernate.entity.persister;

import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.metamodel.binding.EntityBinding;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.persister.entity.UnionSubclassEntityPersister;
import org.jspresso.framework.model.entity.IEntity;

/**
 * Subclassed the hibernate default persister for union subclasses to workaround
 * a problem with determination of the subclass entity name when using dynamic
 * proxies.
 *
 * @author Vincent Vandenschrick
 */
public class EntityProxyUnionSubclassEntityPersister extends
    UnionSubclassEntityPersister {

  /**
   * Constructs a new {@code EntityProxyUnionSubclassEntityPersister}
   * instance.
   *
   * @param entityBinding
   *          the entity binding.
   * @param cacheAccessStrategy
   *          the cache access strategy.
   * @param naturalIdRegionAccessStrategy
   *          the natural ID cache access strategy.
   * @param factory
   *          the session.
   * @param mapping
   *          the mapping.
   */
  public EntityProxyUnionSubclassEntityPersister(EntityBinding entityBinding,
      EntityRegionAccessStrategy cacheAccessStrategy,
      NaturalIdRegionAccessStrategy naturalIdRegionAccessStrategy,
      SessionFactoryImplementor factory, Mapping mapping) {
    super(entityBinding, cacheAccessStrategy, naturalIdRegionAccessStrategy,
        factory, mapping);
  }

  /**
   * Constructs a new {@code EntityProxyUnionSubclassEntityPersister}
   * instance.
   *
   * @param persistentClass
   *          the persistent class.
   * @param cacheAccessStrategy
   *          the cache access strategy.
   * @param naturalIdRegionAccessStrategy
   *          the natural ID cache access strategy.
   * @param factory
   *          the session.
   * @param mapping
   *          the mapping.
   */
  public EntityProxyUnionSubclassEntityPersister(
      PersistentClass persistentClass,
      EntityRegionAccessStrategy cacheAccessStrategy,
      NaturalIdRegionAccessStrategy naturalIdRegionAccessStrategy,
      SessionFactoryImplementor factory, Mapping mapping) {
    super(persistentClass, cacheAccessStrategy, naturalIdRegionAccessStrategy,
        factory, mapping);
  }

  /**
   * Takes a chance to determine the entity persister from the proxy handler
   * contract.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public EntityPersister getSubclassEntityPersister(Object instance,
      SessionFactoryImplementor factory) {
    if (instance instanceof IEntity) {
      return factory.getEntityPersister(((IEntity) instance)
          .getComponentContract().getName());
    }
    return super.getSubclassEntityPersister(instance, factory);
  }
}
