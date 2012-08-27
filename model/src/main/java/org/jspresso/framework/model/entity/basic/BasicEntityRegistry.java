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

  private String                                                    name;
  private Map<Class<? extends IEntity>, Map<Serializable, IEntity>> backingStore;

  /**
   * Constructs a new <code>BasicEntityRegistry</code> instance.
   * 
   * @param name
   *          the name of the registry;
   */
  public BasicEntityRegistry(String name) {
    this.name = name;
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
      // we may try subclasses / superclasses
      for (Map.Entry<Class<? extends IEntity>, Map<Serializable, IEntity>> suberclassContractStore : backingStore
          .entrySet()) {
        Class<? extends IEntity> suberClass = suberclassContractStore.getKey();
        if (suberClass != entityContract
            && (entityContract.isAssignableFrom(suberClass) || suberClass
                .isAssignableFrom(entityContract))) {
          contractStore = suberclassContractStore.getValue();
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
  public void register(Class<? extends IEntity> entityContract,
      Serializable id, IEntity entity) {
    IEntity existingRegisteredEntity = get(entityContract, id);
    if (existingRegisteredEntity != null) {
      if (!checkUnicity(entity, existingRegisteredEntity)) {
        throw new EntityRegistryException(
            "This entity was previously registered with a different instance : "
                + entity);
      }
      // do nothing since the entity is already registered.
    } else {
      Map<Serializable, IEntity> contractStore = backingStore
          .get(entityContract);
      if (contractStore == null) {
        contractStore = new ReferenceMap(AbstractReferenceMap.HARD,
            AbstractReferenceMap.WEAK, true);
        backingStore.put(entityContract, contractStore);
      }
      contractStore.put(id, entity);
    }
  }

  /**
   * Checks that the entity being registered is the &quot;same&quot; that the
   * already registered entity.
   * 
   * @param entity
   *          the entity to test.
   * @param existingRegisteredEntity
   *          the entity with same contract and id being already rehgistered.
   * @return true if both entity are &quot;same&quot;. Default implementation is
   *         based on object equality.
   */
  protected boolean checkUnicity(IEntity entity,
      IEntity existingRegisteredEntity) {
    return entity == existingRegisteredEntity;
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

  /**
   * Gets the name.
   * 
   * @return the name.
   */
  public String getName() {
    return name;
  }
}
