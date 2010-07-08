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
package org.jspresso.framework.application.backend.entity;

import java.lang.reflect.InvocationHandler;

import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.backend.component.ControllerAwareProxyComponentFactory;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityLifecycleHandler;
import org.jspresso.framework.model.entity.basic.BasicProxyEntityFactory;
import org.jspresso.framework.security.UserPrincipal;

/**
 * Proxy entity factory aware of a backend controller to deal with uniqueness of
 * entity instances across the JVM.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ControllerAwareProxyEntityFactory extends BasicProxyEntityFactory {

  private IBackendController backendController;

  /**
   * Takes care of registering the newly created bean in the backend controller.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public <T extends IEntity> T createEntityInstance(Class<T> entityContract) {
    T newEntity = super.createEntityInstance(entityContract);
    getBackendController().registerEntity(newEntity, true);
    return newEntity;
  }

  /**
   * Sets the backend controller.
   * 
   * @param backendController
   *          the backend controller to set.
   */
  public void setBackendController(IBackendController backendController) {
    this.backendController = backendController;
    if (getInlineComponentFactory() instanceof ControllerAwareProxyComponentFactory) {
      ((ControllerAwareProxyComponentFactory) getInlineComponentFactory())
          .setBackendController(backendController);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected InvocationHandler createEntityInvocationHandler(
      IComponentDescriptor<IEntity> entityDescriptor) {
    return new ControllerAwareEntityInvocationHandler(entityDescriptor,
        getInlineComponentFactory(), getEntityCollectionFactory(),
        getAccessorFactory(), getEntityExtensionFactory(),
        getBackendController());
  }

  /**
   * Gets the backendController.
   * 
   * @return the backendController.
   */
  protected IBackendController getBackendController() {
    return backendController;
  }

  /**
   * Returns the backend controller.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected IEntityLifecycleHandler getEntityLifecycleHandler() {
    return getBackendController();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected UserPrincipal getPrincipal() {
    return getBackendController().getApplicationSession().getPrincipal();
  }
}
