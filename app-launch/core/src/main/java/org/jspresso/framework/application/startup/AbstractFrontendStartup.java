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
package org.jspresso.framework.application.startup;

import org.jspresso.framework.application.backend.BackendControllerHolder;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.frontend.IFrontendController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class for application startup including all layers.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public abstract class AbstractFrontendStartup<E, F, G> extends AbstractStartup {

  private IFrontendController<E, F, G> frontendController;

  private static final Logger          LOG = LoggerFactory
                                               .getLogger(AbstractFrontendStartup.class);

  /**
   * Both front and back controllers are retrieved from the spring context,
   * associated and started.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public void start() {
    // start on brand new instances.
    stop();
    IBackendController backendController;
    try {
      backendController = (IBackendController) getApplicationContext().getBean(
          "applicationBackController");
    } catch (RuntimeException ex) {
      LOG.error("applicationBackController could not be instantiated.", ex);
      throw ex;
    }
    try {
      frontendController = (IFrontendController<E, F, G>) getApplicationContext()
          .getBean("applicationFrontController");
    } catch (RuntimeException ex) {
      LOG.error("applicationFrontController could not be instantiated.", ex);
      throw ex;
    }
    frontendController.start(backendController, getStartupLocale(),
        getClientTimeZone());
    BackendControllerHolder.setSessionBackendController(backendController);
  }

  /**
   * Gets the application frontend controller.
   *
   * @return the application frontend controller.
   */
  protected IFrontendController<E, F, G> getFrontendController() {
    return frontendController;
  }

  /**
   * Programmatically stops the application and performs all necessary cleanups.
   */
  public void stop() {
    // Breaks SSO. Useless to perform before garbage collecting.
    // if (frontendController != null) {
    // frontendController.stop();
    // }
    frontendController = null;
    BackendControllerHolder.setSessionBackendController(null);
  }
}
