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
package org.jspresso.framework.application.backend.component;

import java.lang.reflect.InvocationHandler;

import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.basic.BasicProxyComponentFactory;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.security.UserPrincipal;

/**
 * Proxy component factory aware of a backend controller to deal with uniqueness
 * of component instances across the JVM.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ControllerAwareProxyComponentFactory extends
    BasicProxyComponentFactory {

  private IBackendController backendController;

  /**
   * Sets the backendController.
   * 
   * @param backendController
   *          the backendController to set.
   */
  public void setBackendController(IBackendController backendController) {
    this.backendController = backendController;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected InvocationHandler createComponentInvocationHandler(
      IComponentDescriptor<IComponent> componentDescriptor) {
    return new ControllerAwareComponentInvocationHandler(
        componentDescriptor, this, getComponentCollectionFactory(),
        getAccessorFactory(), getComponentExtensionFactory(), backendController);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected UserPrincipal getPrincipal() {
    return backendController.getApplicationSession().getPrincipal();
  }
}
