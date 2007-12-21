/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.backend.session.basic;

import java.util.Map;

import com.d2s.framework.application.backend.session.IEntityUnitOfWork;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.util.bean.BeanPropertyChangeRecorder;

/**
 * An implementation helps an application session in managing unit of works. It
 * uses the hibernate session behind the scene to keep track of entities,
 * ensuring uniqueness in the UOW scope as well as state propagation in case of
 * a successful commit.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicEntityUnitOfWork implements IEntityUnitOfWork {

  private BeanPropertyChangeRecorder dirtRecorder;

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
   * {@inheritDoc}
   */
  public void commit() {
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
  public boolean isActive() {
    return dirtRecorder != null;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isDirty(IEntity entity) {
    Map<String, Object> entityDirtyProperties = getDirtyProperties(entity);
    return (entityDirtyProperties != null && entityDirtyProperties.size() > 0);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isDirty(IEntity entity, String propertyName) {
    Map<String, Object> entityDirtyProperties = getDirtyProperties(entity);
    return (entityDirtyProperties != null && entityDirtyProperties
        .containsKey(propertyName));
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
  public void rollback() {
    cleanup();
  }

  private void cleanup() {
    dirtRecorder = null;
  }
}
