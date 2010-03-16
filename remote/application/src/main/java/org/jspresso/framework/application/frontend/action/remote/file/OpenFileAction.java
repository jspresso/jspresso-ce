/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
import org.jspresso.framework.application.frontend.command.remote.RemoteFileUploadCommand;
import org.jspresso.framework.application.frontend.file.IFileOpenCallback;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.util.resources.server.ResourceProviderServlet;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * Initiates a file open action.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class OpenFileAction extends ChooseFileAction {

  private IFileOpenCallback      fileOpenCallback;
  private FileOpenCallbackAction fileOpenCallbackAction;

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    RemoteFileUploadCommand fileUploadCommand = new RemoteFileUploadCommand();
    fileUploadCommand.setFileFilter(translateFilter(getFileFilter(context),
        context));
    RAction successCallbackAction = getActionFactory(context).createAction(
        createSuccessCallbackAction(), actionHandler,
        (IView<RComponent>) getView(context), getLocale(context));
    fileUploadCommand.setSuccessCallbackAction(successCallbackAction);
    RAction cancelCallbackAction = getActionFactory(context).createAction(
        createCancelCallbackAction(fileOpenCallback), actionHandler,
        (IView<RComponent>) getView(context), getLocale(context));
    fileUploadCommand.setCancelCallbackAction(cancelCallbackAction);
    fileUploadCommand.setFileUrl(ResourceProviderServlet.computeUploadUrl());
    registerCommand(fileUploadCommand, context);
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the fileOpenCallback.
   * 
   * @param fileOpenCallback
   *          the fileOpenCallback to set.
   */
  public void setFileOpenCallback(IFileOpenCallback fileOpenCallback) {
    this.fileOpenCallback = fileOpenCallback;
  }

  private IDisplayableAction createSuccessCallbackAction() {
    if (fileOpenCallbackAction == null) {
      fileOpenCallbackAction = new FileOpenCallbackAction(fileOpenCallback);
    }
    return fileOpenCallbackAction;
  }
}
