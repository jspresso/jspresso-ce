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
package org.jspresso.framework.application.backend.session.basic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jspresso.framework.application.backend.session.IEntityUnitOfWork;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.util.bean.BeanPropertyChangeRecorder;
import org.jspresso.framework.util.bean.IPropertyChangeCapable;

/**
 * An implementation helps an application session in managing unit of works. It
 * uses the hibernate session behind the scene to keep track of entities,
 * ensuring uniqueness in the UOW scope as well as state propagation in case of
 * a successful commit.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicEntityUnitOfWork implements IEntityUnitOfWork {

  private BeanPropertyChangeRecorder dirtRecorder;

  private Set<IEntity>               entitiesRegisteredForDeletion;
  private List<IEntity>              entitiesRegisteredForUpdate;
  private Set<IEntity>               updatedEntities;

  /**
   * {@inheritDoc}
   */
  public void addUpdatedEntity(IEntity entity) {
    if (updatedEntities == null) {
      updatedEntities = new LinkedHashSet<IEntity>();
    }
    updatedEntities.add(entity);
  }

  /**
   * {@inheritDoc}
   */
  public void begin() {
    dirtRecorder = new BeanPropertyChangeRecorder();
  }

  /**
   * {@inheritDoc}
   */
  public void clearDirtyState(IEntity flushedEntity) {
    dirtRecorder.resetChangedProperties(flushedEntity, null);
  }

  /**
   * Clears the pending operations.
   * <p>
   * {@inheritDoc}
   */
  public void clearPendingOperations() {
    entitiesRegisteredForUpdate = null;
    entitiesRegisteredForDeletion = null;
  }

  /**
   * {@inheritDoc}
   */
  public void commit() {
    // We must get rid of the pending operations only in the case of a
    // successful commit.
    clearPendingOperations();
    cleanup();
  }

  /**
   * {@inheritDoc}
   */
  public Map<String, Object> getDirtyProperties(IEntity entity) {
    return dirtRecorder.getChangedProperties(entity);
  }

  /**
   * {@inheritDoc}
   */
  public Set<IEntity> getEntitiesRegisteredForDeletion() {
    return entitiesRegisteredForDeletion;
  }

  /**
   * {@inheritDoc}
   */
  public List<IEntity> getEntitiesRegisteredForUpdate() {
    return entitiesRegisteredForUpdate;
  }

  /**
   * {@inheritDoc}
   */
  public Map<Class<?>, Map<Serializable, IEntity>> getRegisteredEntities() {
    Map<Class<?>, Map<Serializable, IEntity>> registeredEntities = new HashMap<Class<?>, Map<Serializable, IEntity>>();
    for (IPropertyChangeCapable entity : dirtRecorder.getRegistered()) {
      Class<?> entityContract = ((IEntity) entity).getComponentContract();
      Map<Serializable, IEntity> contractBuffer = registeredEntities
          .get(entityContract);
      if (contractBuffer == null) {
        contractBuffer = new HashMap<Serializable, IEntity>();
        registeredEntities.put(entityContract, contractBuffer);
      }
      contractBuffer.put(((IEntity) entity).getId(), (IEntity) entity);
    }
    return registeredEntities;
  }

  /**
   * {@inheritDoc}
   */
  public Set<IEntity> getUpdatedEntities() {
    return updatedEntities;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isActive() {
    return dirtRecorder != null;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isEntityRegisteredForDeletion(IEntity entity) {
    return entitiesRegisteredForDeletion != null
        && entitiesRegisteredForDeletion.contains(entity);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isEntityRegisteredForUpdate(IEntity entity) {
    return entitiesRegisteredForUpdate != null
        && entitiesRegisteredForUpdate.contains(entity);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isUpdated(IEntity entity) {
    return updatedEntities != null && updatedEntities.contains(entity);
  }

  /**
   * {@inheritDoc}
   */
  public void register(IEntity bean,
      Map<String, Object> initialChangedProperties) {
    dirtRecorder.register(bean, initialChangedProperties);
  }

  /**
   * {@inheritDoc}
   */
  public void registerForDeletion(IEntity entity) {
    if (entity.isPersistent()) {
      if (entitiesRegisteredForDeletion == null) {
        entitiesRegisteredForDeletion = new LinkedHashSet<IEntity>();
      }
      entitiesRegisteredForDeletion.add(entity);
    }
    if (entitiesRegisteredForUpdate != null) {
      while (entitiesRegisteredForUpdate.remove(entity)) {
        // NO-OP.
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public void registerForUpdate(IEntity entity) {
    if (entitiesRegisteredForUpdate == null) {
      entitiesRegisteredForUpdate = new ArrayList<IEntity>();
    }
    entitiesRegisteredForUpdate.add(entity);
  }

  /**
   * {@inheritDoc}
   */
  public void rollback() {
    cleanup();
  }

  private void cleanup() {
    dirtRecorder = null;
    updatedEntities = null;
  }
}
