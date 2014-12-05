/*
 * Copyright (c) 2005-2014 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.backend.action.persistence.mongo;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.springframework.data.mongodb.core.MongoTemplate;

import org.jspresso.framework.application.backend.action.BackendAction;
import org.jspresso.framework.application.backend.persistence.mongo.MongoBackendController;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.entity.IEntity;

/**
 * This the root abstract class of all mongo related persistence actions. It
 * refines the return values of some protected methods (like the controller that
 * is refined to {@code MongoBackendController}) and adds some new ones
 * (like the access to the controller's configured &quot;<i>Spring Mongo
 * template</i>&quot;. It provides also protected utility methods for various
 * standard persistence operations on Jspresso managed entities.
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractMongoAction extends BackendAction {

  /**
   * Performs necessary cleanings when an entity or component is deleted.
   * 
   * @param component
   *          the deleted entity or component.
   * @param context
   *          The action context.
   * @param dryRun
   *          set to true to simulate before actually doing it.
   * @throws IllegalAccessException
   *           whenever this kind of exception occurs.
   * @throws java.lang.reflect.InvocationTargetException
   *           whenever this kind of exception occurs.
   * @throws NoSuchMethodException
   *           whenever this kind of exception occurs.
   */
  protected void cleanRelationshipsOnDeletion(IComponent component,
      Map<String, Object> context, boolean dryRun)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    getController(context).cleanRelationshipsOnDeletion(component, dryRun);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected MongoBackendController getController(Map<String, Object> context) {
    return (MongoBackendController) super.getController(context);
  }

  /**
   * Gets the mongoTemplate.
   *
   * @param context
   *          the action context.
   * @return the mongoTemplate.
   */
  protected MongoTemplate getMongoTemplate(Map<String, Object> context) {
    return getController(context).getMongoTemplate();
  }

  /**
   * Reloads an entity in mongo.
   * 
   * @param entity
   *          the entity to reload.
   * @param context
   *          the action context.
   */
  protected void reloadEntity(IEntity entity, Map<String, Object> context) {
    getController(context).reload(entity);
  }

  /**
   * Refines the controller with an Mongo backend controller.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected MongoBackendController getBackendController(
      Map<String, Object> context) {
    return (MongoBackendController) super.getBackendController(context);
  }
}
