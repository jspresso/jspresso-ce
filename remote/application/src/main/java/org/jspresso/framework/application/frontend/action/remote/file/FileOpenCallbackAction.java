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
package org.jspresso.framework.application.frontend.action.remote.file;

import java.io.IOException;
import java.util.Map;

import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.remote.AbstractRemoteAction;
import org.jspresso.framework.application.frontend.file.IFileOpenCallback;
import org.jspresso.framework.util.resources.IResource;
import org.jspresso.framework.util.resources.server.ResourceManager;

/**
 * An (internal) action to trigger the file open callback.
 *
 * @internal
 * @author Vincent Vandenschrick
 */
public class FileOpenCallbackAction extends AbstractRemoteAction {

  private final IFileOpenCallback fileOpenCallback;

  /**
   * Constructs a new {@code FileOpenCallbackAction} instance.
   *
   * @param fileOpenCallback
   *          the file open callback to trigger.
   */
  public FileOpenCallbackAction(IFileOpenCallback fileOpenCallback) {
    this.fileOpenCallback = fileOpenCallback;
  }

  /**
   * Triggers the file open callback.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    String resourceId = getActionCommand(context);
    IResource uploadedResource = (IResource) ResourceManager.getInstance()
        .getRegistered(resourceId);
    if (uploadedResource != null) {
      try {
        fileOpenCallback.fileChosen(uploadedResource.getName(),
            uploadedResource.getContent(), actionHandler, context);
      } catch (IOException ex) {
        throw new ActionException(ex);
      }
    } else {
      fileOpenCallback.cancel(actionHandler, context);
    }
    return super.execute(actionHandler, context);
  }
}
