/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.backend.action.persistence.hibernate;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.hibernate.Session;
import org.jspresso.framework.application.backend.action.BackendAction;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.entity.IEntity;

/**
 * This the root abstract class of all hibernate related persistence actions. It
 * refines the return values of some protected methods (like the controller that
 * is refined to {@code HibernateBackendController}) and adds some new ones
 * (like the access to the controller's configured &quot;<i>Spring Hibernate
 * template</i>&quot;. It provides also protected utility methods for various
 * standard persistence operations on Jspresso managed entities.
 *
 * @author Vincent Vandenschrick
 *
 * @deprecated directly use BackendAction and perform appropriate casts.
 */
@Deprecated
public abstract class AbstractHibernateAction extends BackendAction {

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
   * @throws InvocationTargetException
   *           whenever this kind of exception occurs.
   * @throws NoSuchMethodException
   *           whenever this kind of exception occurs.
   */
  @Override
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
  protected HibernateBackendController getController(Map<String, Object> context) {
    return (HibernateBackendController) super.getController(context);
  }

  /**
   * Gets the hibernateTemplate.
   *
   * @param context
   *          the action context.
   * @return the hibernateTemplate.
   */
  protected Session getHibernateSession(Map<String, Object> context) {
    return getController(context).getHibernateSession();
  }

  /**
   * Reloads an entity in hibernate.
   *
   * @param entity
   *          the entity to reload.
   * @param context
   *          the action context.
   */
  @Override
  protected void reloadEntity(IEntity entity, Map<String, Object> context) {
    getController(context).reload(entity);
  }

  /**
   * Refines the controller with an Hibernate backend controller.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected HibernateBackendController getBackendController(
      Map<String, Object> context) {
    return (HibernateBackendController) super.getBackendController(context);
  }
}
