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
package org.jspresso.framework.application.frontend.action.remote.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.command.remote.RemoteFileDownloadCommand;
import org.jspresso.framework.application.frontend.file.IFileSaveCallback;
import org.jspresso.framework.util.resources.AbstractResource;
import org.jspresso.framework.util.resources.server.ResourceManager;
import org.jspresso.framework.util.resources.server.ResourceProviderServlet;

/**
 * Initiates a file save action.
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
public class SaveFileAction extends ChooseFileAction {

  private IFileSaveCallback fileSaveCallback;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    RemoteFileDownloadCommand fileDownloadCommand = new RemoteFileDownloadCommand();
    fileDownloadCommand.setFileFilter(translateFilter(getFileFilter(context),
        context));
    fileDownloadCommand.setDefaultFileName(getDefaultFileName());
    String resourceId = ResourceManager.getInstance().register(
        new ResourceAdapter(fileSaveCallback, context));
    fileDownloadCommand.setResourceId(resourceId);
    fileDownloadCommand.setDownloadUrl(ResourceProviderServlet.computeDownloadUrl(resourceId));
    registerCommand(fileDownloadCommand, context);
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the fileSaveCallback.
   * 
   * @param fileSaveCallback
   *          the fileSaveCallback to set.
   */
  public void setFileSaveCallback(IFileSaveCallback fileSaveCallback) {
    this.fileSaveCallback = fileSaveCallback;
  }

  private static class ResourceAdapter extends AbstractResource {

    private byte[] content;

    public ResourceAdapter(IFileSaveCallback source, Map<String, Object> context) {
      super("application/octet-stream");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      source.fileChosen(baos, context);
      content = baos.toByteArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getContent() {
      return new ByteArrayInputStream(content);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getSize() {
      return content.length;
    }
  }
}
