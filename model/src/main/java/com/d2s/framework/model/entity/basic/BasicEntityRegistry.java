/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity.basic;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.AbstractReferenceMap;
import org.apache.commons.collections.map.ReferenceMap;

import com.d2s.framework.model.entity.EntityRegistryException;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IEntityRegistry;

/**
 * Basic implementation of an entity registry backed by an HashMap of weak
 * reference values.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicEntityRegistry implements IEntityRegistry {

  private Map<String, Map<Object, IEntity>> backingStore;

  /**
   * Constructs a new <code>BasicEntityRegistry</code> instance.
   */
  public BasicEntityRegistry() {
    backingStore = new HashMap<String, Map<Object, IEntity>>();
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public void register(IEntity entity) {
    IEntity existingRegisteredEntity = get(entity.getContract().getName(),
        entity.getId());
    if (existingRegisteredEntity != null) {
      if (entity != existingRegisteredEntity) {
        throw new EntityRegistryException(
            "This entity was previously registered with a different instance"
                + entity);
      }
      // do nothing since the entity is already registered.
    } else {
      Map<Object, IEntity> contractStore = backingStore.get(entity
          .getContract().getName());
      if (contractStore == null) {
        contractStore = new ReferenceMap(AbstractReferenceMap.HARD,
            AbstractReferenceMap.WEAK, true);
        backingStore.put(entity.getContract().getName(), contractStore);
      }
      contractStore.put(entity.getId(), entity);
    }
  }

  /**
   * {@inheritDoc}
   */
  public IEntity get(String entityContractName, Object id) {
    Map<Object, IEntity> contractStore = backingStore.get(entityContractName);
    if (contractStore != null) {
      IEntity registeredEntity = contractStore.get(id);
      if (registeredEntity == null) {
        contractStore.remove(id);
      }
      return registeredEntity;
    }
    return null;
  }
}
