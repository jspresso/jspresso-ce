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
package org.jspresso.framework.application.backend.resources.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.AbstractBackendController;
import org.jspresso.framework.application.backend.BackendControllerHolder;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.backend.action.Asynchronous;
import org.jspresso.framework.application.backend.action.BackendAction;
import org.jspresso.framework.application.backend.async.AsyncActionExecutor;
import org.jspresso.framework.util.exception.NestedRuntimeException;
import org.jspresso.framework.util.resources.IActiveResource;
import org.jspresso.framework.util.resources.server.ResourceProviderServlet;

/**
 * A resource provider servlet subclass that cleans up backend controller after
 * an active resource download.
 *
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
                                     OutputStream outputStream) throws IOException {
    IBackendController backendController = BackendControllerHolder
        .getCurrentBackendController();
    AbstractBackendController slaveBackendController = (AbstractBackendController) backendController
        .createBackendController();
    AsyncActionExecutor executor = new AsyncActionExecutor(new AsyncExportAction(resource, outputStream),
        new HashMap<String, Object>(), null, slaveBackendController);
    // execute synchronously in the thread
    try {
      executor.run();
    } catch (RuntimeException ex) {
      if (ex.getCause() instanceof IOException) {
        throw (IOException) ex.getCause();
      } else {
        throw ex;
      }
    }
    // Do not cleanup the session backend controller since it can be used by a GUI thread. See bug #75.
    // backendController.cleanupRequestResources();
  }

  /**
   * Do write active resource.
   *
   * @param resource the resource
   * @param outputStream the output stream
   * @throws IOException the IO exception
   */
  protected void doWriteActiveResource(IActiveResource resource, OutputStream outputStream) throws IOException {
    super.writeActiveResource(resource, outputStream);
  }

  @Asynchronous
  private class AsyncExportAction extends BackendAction {

    private IActiveResource resource;
    private OutputStream outputStream;

    public AsyncExportAction(IActiveResource resource, OutputStream outputStream) {
      this.resource = resource;
      this.outputStream = outputStream;
    }

    @Override
    public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
      try {
        doWriteActiveResource(resource, outputStream);
      } catch (IOException ioe) {
        throw new NestedRuntimeException(ioe);
      }
      return true;
    }
  }
}
