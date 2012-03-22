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
package org.jspresso.framework.model.entity.basic;

import java.io.Serializable;
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
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicEntityRegistry implements IEntityRegistry {

  private Map<Class<? extends IEntity>, Map<Serializable, IEntity>> backingStore;

  /**
   * Constructs a new <code>BasicEntityRegistry</code> instance.
   */
  public BasicEntityRegistry() {
    backingStore = new HashMap<Class<? extends IEntity>, Map<Serializable, IEntity>>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IEntity get(Class<? extends IEntity> entityContract, Serializable id) {
    IEntity registeredEntity = null;
    Map<Serializable, IEntity> contractStore = backingStore.get(entityContract);
    if (contractStore != null) {
      registeredEntity = contractStore.get(id);
      if (registeredEntity == null) {
        contractStore.remove(id);
      }
    }
    if (registeredEntity == null) {
      // we may try subclasses
      for (Map.Entry<Class<? extends IEntity>, Map<Serializable, IEntity>> subclassContractStore : backingStore
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
  @Override
  @SuppressWarnings("unchecked")
  public void register(IEntity entity) {
    IEntity existingRegisteredEntity = get(entity.getComponentContract(),
        entity.getId());
    if (existingRegisteredEntity != null) {
      if (entity != existingRegisteredEntity) {
        throw new EntityRegistryException(
            "This entity was previously registered with a different instance"
                + entity);
      }
      // do nothing since the entity is already registered.
    } else {
      Map<Serializable, IEntity> contractStore = backingStore.get(entity
          .getComponentContract());
      if (contractStore == null) {
        contractStore = new ReferenceMap(AbstractReferenceMap.HARD,
            AbstractReferenceMap.WEAK, true);
        backingStore.put(entity.getComponentContract(), contractStore);
      }
      contractStore.put(entity.getId(), entity);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clear() {
    if (backingStore != null) {
      backingStore.clear();
    }
  }
}
