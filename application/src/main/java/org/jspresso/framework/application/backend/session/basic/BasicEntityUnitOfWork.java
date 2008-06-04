/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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

import java.util.Map;

import org.jspresso.framework.application.backend.session.IEntityUnitOfWork;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.util.bean.BeanPropertyChangeRecorder;


/**
 * An implementation helps an application session in managing unit of works. It
 * uses the hibernate session behind the scene to keep track of entities,
 * ensuring uniqueness in the UOW scope as well as state propagation in case of
 * a successful commit.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
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
