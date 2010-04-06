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
package org.jspresso.framework.application.startup;

import org.jspresso.framework.application.backend.IBackendController;

/**
 * Abstract class for application startup including only the backend layer. This
 * serves for batch processes for instance.
 * 
 * @version $LastChangedRevision: 2097 $
 * @author Vincent Vandenschrick
 */
public abstract class AbstractBackendStartup extends AbstractStartup {

  private IBackendController backendController;

  /**
   * Back controller is retrieved from the spring context and started.
   */
  protected void startController() {
    // start on brand new instances.
    backendController = null;
    getBackendController().start(getStartupLocale());
  }

  /**
   * Gets the application backend controller.
   * 
   * @return the application backend controller.
   */
  protected IBackendController getBackendController() {
    try {
      if (backendController == null) {
        backendController = (IBackendController) getApplicationContext()
            .getBean("applicationBackController");
      }
      return backendController;
    } catch (RuntimeException ex) {
      getLogger().error("applicationBackController could not be instanciated.",
          ex);
      throw ex;
    }
  }
}
