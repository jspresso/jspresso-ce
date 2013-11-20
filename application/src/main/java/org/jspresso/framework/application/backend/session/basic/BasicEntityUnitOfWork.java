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
import org.jspresso.framework.model.entity.IEntityRegistry;
import org.jspresso.framework.model.entity.basic.BasicEntityRegistry;
import org.jspresso.framework.util.bean.BeanPropertyChangeRecorder;
import org.jspresso.framework.util.bean.IPropertyChangeCapable;

/**
 * An implementation helps an application session in managing unit of works. It
 * uses the hibernate session behind the scene to keep track of entities,
 * ensuring uniqueness in the UOW scope as well as state propagation in case of
 * a successful commit.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision$
 */
public class BasicEntityUnitOfWork implements IEntityUnitOfWork {

  private final IEntityRegistry            entityRegistry;
  private       BeanPropertyChangeRecorder dirtRecorder;
  private       Set<IEntity>               entitiesRegisteredForDeletion;
  private       List<IEntity>              entitiesRegisteredForUpdate;
  private       Set<IEntity>               updatedEntities;
  private       BasicEntityUnitOfWork      parentUnitOfWork;
  private       BasicEntityUnitOfWork      nestedUnitOfWork;
  private       boolean                    suspended;

  /**
   * Constructs a new <code>BasicEntityUnitOfWork</code> instance.
   */
  public BasicEntityUnitOfWork() {
    entityRegistry = new BasicEntityRegistry("uowEntityRegistry");
    suspended = false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addUpdatedEntity(IEntity entity) {
    if (nestedUnitOfWork != null) {
      nestedUnitOfWork.addUpdatedEntity(entity);
    } else {
      if (updatedEntities == null) {
        updatedEntities = new LinkedHashSet<IEntity>();
      }
      updatedEntities.add(entity);
    }
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
  public void beginNested() {
    if (nestedUnitOfWork != null) {
      nestedUnitOfWork.beginNested();
    } else {
      nestedUnitOfWork = new BasicEntityUnitOfWork();
      nestedUnitOfWork.parentUnitOfWork = this;
      nestedUnitOfWork.begin();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasNested() {
    return nestedUnitOfWork != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clearDirtyState(IEntity flushedEntity) {
    if (nestedUnitOfWork != null) {
      nestedUnitOfWork.clearDirtyState(flushedEntity);
    } else {
      dirtRecorder.resetChangedProperties(flushedEntity, null);
    }
  }

  /**
   * Clears the pending operations.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public void clearPendingOperations() {
    if (nestedUnitOfWork != null) {
      nestedUnitOfWork.clearPendingOperations();
    } else {
      entitiesRegisteredForUpdate = null;
      entitiesRegisteredForDeletion = null;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void commit() {
    if (nestedUnitOfWork != null) {
      nestedUnitOfWork.commit();
    } else {
      cleanup();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, Object> getDirtyProperties(IEntity entity) {
    if (nestedUnitOfWork != null) {
      return nestedUnitOfWork.getDirtyProperties(entity);
    }
    return dirtRecorder.getChangedProperties(entity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<IEntity> getEntitiesRegisteredForDeletion() {
    if (nestedUnitOfWork != null) {
      return nestedUnitOfWork.getEntitiesRegisteredForDeletion();
    }
    return entitiesRegisteredForDeletion;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<IEntity> getEntitiesRegisteredForUpdate() {
    if (nestedUnitOfWork != null) {
      return nestedUnitOfWork.getEntitiesRegisteredForUpdate();
    }
    return entitiesRegisteredForUpdate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<Class<? extends IEntity>, Map<Serializable, IEntity>> getRegisteredEntities() {
    if (nestedUnitOfWork != null) {
      return nestedUnitOfWork.getRegisteredEntities();
    }
    Map<Class<? extends IEntity>, Map<Serializable, IEntity>> registeredEntities = new HashMap<Class<? extends IEntity>, Map<Serializable, IEntity>>();
    for (IPropertyChangeCapable entity : dirtRecorder.getRegistered()) {
      Class<? extends IEntity> entityContract = ((IEntity) entity).getComponentContract();
      Map<Serializable, IEntity> contractBuffer = registeredEntities.get(entityContract);
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
    if (nestedUnitOfWork != null) {
      return nestedUnitOfWork.getUpdatedEntities();
    }
    return updatedEntities;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isActive() {
    if (nestedUnitOfWork != null && nestedUnitOfWork.isActive()) {
      return true;
    }
    return suspended == false && dirtRecorder != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEntityRegisteredForDeletion(IEntity entity) {
    if (nestedUnitOfWork != null) {
      return nestedUnitOfWork.isEntityRegisteredForDeletion(entity);
    }
    return entitiesRegisteredForDeletion != null && entitiesRegisteredForDeletion.contains(entity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEntityRegisteredForUpdate(IEntity entity) {
    if (nestedUnitOfWork != null) {
      return nestedUnitOfWork.isEntityRegisteredForUpdate(entity);
    }
    return entitiesRegisteredForUpdate != null && entitiesRegisteredForUpdate.contains(entity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isUpdated(IEntity entity) {
    if (nestedUnitOfWork != null) {
      return nestedUnitOfWork.isUpdated(entity);
    }
    return updatedEntities != null && updatedEntities.contains(entity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void register(IEntity entity, Map<String, Object> initialChangedProperties) {
    if (nestedUnitOfWork != null) {
      nestedUnitOfWork.register(entity, initialChangedProperties);
    } else {
      dirtRecorder.register(entity, initialChangedProperties);
      entityRegistry.register(entity.getComponentContract(), entity.getId(), entity);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerForDeletion(IEntity entity) {
    if (nestedUnitOfWork != null) {
      nestedUnitOfWork.registerForDeletion(entity);
    } else {
      if (entity == null) {
        throw new IllegalArgumentException("Passed entity cannot be null");
      }
      if (entity.isPersistent()) {
        if (entitiesRegisteredForDeletion == null) {
          entitiesRegisteredForDeletion = new LinkedHashSet<IEntity>();
        }
        entitiesRegisteredForDeletion.add(entity);
      }
      if (entitiesRegisteredForUpdate != null) {
        //noinspection StatementWithEmptyBody
        while (entitiesRegisteredForUpdate.remove(entity)) {
          // NO-OP.
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerForUpdate(IEntity entity) {
    if (nestedUnitOfWork != null) {
      nestedUnitOfWork.registerForUpdate(entity);
    } else {
      if (entity == null) {
        throw new IllegalArgumentException("Passed entity cannot be null");
      }
      if (entitiesRegisteredForUpdate == null) {
        entitiesRegisteredForUpdate = new ArrayList<IEntity>();
      }
      entitiesRegisteredForUpdate.add(entity);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void rollback() {
    if (nestedUnitOfWork != null) {
      nestedUnitOfWork.rollback();
    } else {
      cleanup();
    }
  }

  private void cleanup() {
    if (parentUnitOfWork != null) {
      parentUnitOfWork.nestedUnitOfWork = null;
    }
    dirtRecorder = null;
    updatedEntities = null;
    entityRegistry.clear();
  }

  /**
   * UnsupportedOperationException.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public void cleanRelationshipsOnDeletion(IComponent component, boolean dryRun) {
    throw new UnsupportedOperationException("entity unit of work does not support cleanRelationshipsOnDeletion.");
  }

  /**
   * UnsupportedOperationException.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public void reload(IEntity entity) {
    throw new UnsupportedOperationException("entity unit of work does not support reload of an entity.");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clear() {
    if (nestedUnitOfWork != null) {
      nestedUnitOfWork.clear();
    } else {
      clearPendingOperations();
      cleanup();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDirtyTrackingEnabled() {
    if (nestedUnitOfWork != null) {
      return nestedUnitOfWork.isDirtyTrackingEnabled();
    }
    return dirtRecorder.isEnabled();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDirtyTrackingEnabled(boolean enabled) {
    if (nestedUnitOfWork != null) {
      nestedUnitOfWork.setDirtyTrackingEnabled(enabled);
    }
    dirtRecorder.setEnabled(enabled);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IEntity getRegisteredEntity(Class<? extends IEntity> entityContract, Serializable entityId) {
    if (nestedUnitOfWork != null) {
      return nestedUnitOfWork.getRegisteredEntity(entityContract, entityId);
    }
    return entityRegistry.get(entityContract, entityId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void suspend() {
    if (nestedUnitOfWork != null) {
      nestedUnitOfWork.suspend();
    } else {
      suspended = true;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void resume() {
    if (nestedUnitOfWork != null) {
      nestedUnitOfWork.resume();
    } else {
      suspended = false;
    }
  }
}
