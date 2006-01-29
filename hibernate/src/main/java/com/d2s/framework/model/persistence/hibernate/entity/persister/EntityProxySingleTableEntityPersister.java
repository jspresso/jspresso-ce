/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.persistence.hibernate.entity.persister;

import java.io.Serializable;

import org.hibernate.EntityMode;
import org.hibernate.cache.CacheConcurrencyStrategy;
import org.hibernate.engine.Mapping;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.persister.entity.SingleTableEntityPersister;

import com.d2s.framework.model.entity.EntityException;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IEntityFactory;

/**
 * Subclassed the hibernate default persister for subclasses to workaround a
 * problem with determination of the subclass entity name when using dynamic
 * proxies.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EntityProxySingleTableEntityPersister extends
    SingleTableEntityPersister {

  private static IEntityFactory entityFactory;

  /**
   * Constructs a new <code>EntityProxyJoinedSubclassEntityPersister</code>
   * instance.
   * 
   * @param persistentClass
   *          the persistentClass.
   * @param cache
   *          the cache.
   * @param factory
   *          the factory.
   * @param mapping
   *          the mapping.
   */
  public EntityProxySingleTableEntityPersister(PersistentClass persistentClass,
      CacheConcurrencyStrategy cache, SessionFactoryImplementor factory,
      Mapping mapping) {
    super(persistentClass, cache, factory, mapping);
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
      return factory.getEntityPersister(((IEntity) instance).getContract()
          .getName());
    }
    return super.getSubclassEntityPersister(instance, factory, entityMode);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object instantiate(Serializable id, @SuppressWarnings("unused")
  EntityMode entityMode) {
    try {
      return entityFactory.createEntityInstance(Class
          .forName(getEntityMetamodel().getName()), id);
    } catch (ClassNotFoundException ex) {
      throw new EntityException(ex);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityMode guessEntityMode(@SuppressWarnings("unused")
  Object object) {
    return EntityMode.POJO;
  }

  /**
   * Sets the entityFactory.
   * 
   * @param factory
   *          the entityFactory to set.
   */
  public static void setEntityFactory(IEntityFactory factory) {
    entityFactory = factory;
  }
}
