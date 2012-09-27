/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.backend.resources.server;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.jspresso.framework.application.backend.BackendControllerHolder;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.util.resources.IActiveResource;
import org.jspresso.framework.util.resources.server.ResourceProviderServlet;

/**
 * A resource provider servlet subclass that cleans up backend controller after
 * an active resource download.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ControllerAwareResourceProviderServlet extends
    ResourceProviderServlet {

  private static final long serialVersionUID = 2970338035826498738L;

  /**
   * {@inheritDoc}
   */
  @Override
  protected void writeActiveResource(IActiveResource resource,
      HttpServletResponse response) throws IOException {
    try {
      super.writeActiveResource(resource, response);
    } finally {
      IBackendController backendController = BackendControllerHolder
          .getCurrentBackendController();
      if (backendController != null) {
        backendController.cleanupRequestResources();
      }
    }
  }
}
