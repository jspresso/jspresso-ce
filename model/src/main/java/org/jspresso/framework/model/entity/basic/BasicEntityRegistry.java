/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.entity.basic;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.AbstractReferenceMap;
import org.apache.commons.collections.map.ReferenceMap;
import org.jspresso.framework.model.entity.EntityRegistryException;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityRegistry;


/**
 * Basic implementation of an entity registry backed by an HashMap of weak
 * reference values.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicEntityRegistry implements IEntityRegistry {

  private Map<Class<? extends IEntity>, Map<Object, IEntity>> backingStore;

  /**
   * Constructs a new <code>BasicEntityRegistry</code> instance.
   */
  public BasicEntityRegistry() {
    backingStore = new HashMap<Class<? extends IEntity>, Map<Object, IEntity>>();
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public IEntity get(Class entityContract, Object id) {
    IEntity registeredEntity = null;
    Map<Object, IEntity> contractStore = backingStore.get(entityContract);
    if (contractStore != null) {
      registeredEntity = contractStore.get(id);
      if (registeredEntity == null) {
        contractStore.remove(id);
      }
    }
    if (registeredEntity == null) {
      // we may try subclasses
      for (Map.Entry<Class<? extends IEntity>, Map<Object, IEntity>> subclassContractStore : backingStore
          .entrySet()) {
        if (entityContract.isAssignableFrom(subclassContractStore.getKey())) {
          contractStore = subclassContractStore.getValue();
          if (contractStore != null) {
            registeredEntity = contractStore.get(id);
            if (registeredEntity == null) {
              contractStore.remove(id);
            } else {
              break;
            }
          }
        }
      }
    }
    return registeredEntity;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public void register(IEntity entity) {
    IEntity existingRegisteredEntity = get(entity.getContract(), entity.getId());
    if (existingRegisteredEntity != null) {
      if (entity != existingRegisteredEntity) {
        throw new EntityRegistryException(
            "This entity was previously registered with a different instance"
                + entity);
      }
      // do nothing since the entity is already registered.
    } else {
      Map<Object, IEntity> contractStore = backingStore.get(entity
          .getContract());
      if (contractStore == null) {
        contractStore = new ReferenceMap(AbstractReferenceMap.HARD,
            AbstractReferenceMap.WEAK, true);
        backingStore.put(entity.getContract(), contractStore);
      }
      contractStore.put(entity.getId(), entity);
    }
  }
}
