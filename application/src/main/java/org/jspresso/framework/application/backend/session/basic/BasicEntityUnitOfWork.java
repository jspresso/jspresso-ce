/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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
import org.jspresso.framework.model.component.IComponent;
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
  @Override
  public void addUpdatedEntity(IEntity entity) {
    if (updatedEntities == null) {
      updatedEntities = new LinkedHashSet<IEntity>();
    }
    updatedEntities.add(entity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void begin() {
    dirtRecorder = new BeanPropertyChangeRecorder();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clearDirtyState(IEntity flushedEntity) {
    dirtRecorder.resetChangedProperties(flushedEntity, null);
  }

  /**
   * Clears the pending operations.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void clearPendingOperations() {
    entitiesRegisteredForUpdate = null;
    entitiesRegisteredForDeletion = null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void commit() {
    cleanup();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, Object> getDirtyProperties(IEntity entity) {
    return dirtRecorder.getChangedProperties(entity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<IEntity> getEntitiesRegisteredForDeletion() {
    return entitiesRegisteredForDeletion;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<IEntity> getEntitiesRegisteredForUpdate() {
    return entitiesRegisteredForUpdate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<Class<? extends IEntity>, Map<Serializable, IEntity>> getRegisteredEntities() {
    Map<Class<? extends IEntity>, Map<Serializable, IEntity>> registeredEntities =
        new HashMap<Class<? extends IEntity>, Map<Serializable, IEntity>>();
    for (IPropertyChangeCapable entity : dirtRecorder.getRegistered()) {
      Class<? extends IEntity> entityContract = ((IEntity) entity)
          .getComponentContract();
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
  @Override
  public Set<IEntity> getUpdatedEntities() {
    return updatedEntities;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isActive() {
    return dirtRecorder != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEntityRegisteredForDeletion(IEntity entity) {
    return entitiesRegisteredForDeletion != null
        && entitiesRegisteredForDeletion.contains(entity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEntityRegisteredForUpdate(IEntity entity) {
    return entitiesRegisteredForUpdate != null
        && entitiesRegisteredForUpdate.contains(entity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isUpdated(IEntity entity) {
    return updatedEntities != null && updatedEntities.contains(entity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void register(IEntity bean,
      Map<String, Object> initialChangedProperties) {
    dirtRecorder.register(bean, initialChangedProperties);
  }

  /**
   * {@inheritDoc}
   */
  @Override
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
  @Override
  public void registerForUpdate(IEntity entity) {
    if (entitiesRegisteredForUpdate == null) {
      entitiesRegisteredForUpdate = new ArrayList<IEntity>();
    }
    entitiesRegisteredForUpdate.add(entity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void rollback() {
    cleanup();
  }

  private void cleanup() {
    dirtRecorder = null;
    updatedEntities = null;
  }

  /**
   * UnsupportedOperationException.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void cleanRelationshipsOnDeletion(IComponent component, boolean dryRun) {
    throw new UnsupportedOperationException(
        "entity unit of work does not support cleanRelationshipsOnDeletion.");
  }

  /**
   * UnsupportedOperationException.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void reload(IEntity entity) {
    throw new UnsupportedOperationException(
        "entity unit of work does not support reload of an entity.");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clear() {
    clearPendingOperations();
    cleanup();
  }
}
