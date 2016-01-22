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

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.remote.AbstractRemoteAction;
import org.jspresso.framework.application.frontend.file.IFileCallback;
import org.jspresso.framework.util.resources.server.ResourceManager;

/**
 * An (internal) action to trigger the file open cancel callback.
 *
 * @internal
 * @author Vincent Vandenschrick
 */
public class FileCancelCallbackAction extends AbstractRemoteAction {

  private final IFileCallback fileCallback;

  /**
   * Constructs a new {@code FileCancelCallbackAction} instance.
   *
   * @param fileCallback
   *          the file callback to cancel.
   */
  public FileCancelCallbackAction(IFileCallback fileCallback) {
    this.fileCallback = fileCallback;
  }

  /**
   * Triggers the file callback to cancel.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    String resourceId = getActionCommand(context);
    fileCallback.cancel(actionHandler, context);
    if (resourceId != null) {
      ResourceManager.getInstance().unregister(resourceId);
    }
    return super.execute(actionHandler, context);
  }
}
