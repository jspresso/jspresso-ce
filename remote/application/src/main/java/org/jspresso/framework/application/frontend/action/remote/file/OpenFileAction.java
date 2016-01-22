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
import org.jspresso.framework.application.frontend.command.remote.RemoteFileUploadCommand;
import org.jspresso.framework.application.frontend.file.IFileOpenCallback;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.util.resources.server.ResourceProviderServlet;
import org.jspresso.framework.view.IView;

/**
 * This action lets the user browse his local file system and choose a file to
 * read some content from. What is done with the file content is determined by
 * the configured {@code fileOpenCallback} instance.
 *
 * @author Vincent Vandenschrick
 */
public class OpenFileAction extends ChooseFileAction {

  private FileOpenCallbackAction fileOpenCallbackAction;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    RemoteFileUploadCommand fileUploadCommand = new RemoteFileUploadCommand();
    fileUploadCommand.setFileFilter(translateFilter(getFileFilter(context),
        context));
    IView<RComponent> view = getView(context);
    RAction successCallbackAction = getActionFactory(context).createAction(
        getFileOpenCallbackAction(context), actionHandler, view, getLocale(context));
    fileUploadCommand.setSuccessCallbackAction(successCallbackAction);
    RAction cancelCallbackAction = getActionFactory(context).createAction(
        getFileCancelCallbackAction(context), actionHandler, view, getLocale(context));
    fileUploadCommand.setCancelCallbackAction(cancelCallbackAction);
    fileUploadCommand.setFileUrl(ResourceProviderServlet.computeUploadUrl());
    registerCommand(fileUploadCommand, context);
    return super.execute(actionHandler, context);
  }

  /**
   * Configures the file open callback instance that will be used to deal with
   * the file dialog events. Two methods must be implemented :
   * <ul>
   * <li>
   * {@code fileChosen(InputStream, IActionHandler, Map[String, Object])}
   * that is called whenever a file has been chosen. The input stream that is
   * passed as parameter allows for reading from the chosen file. The developer
   * doesn't have to cope with closing the stream.</li>
   * <li>{@code cancel(IActionHandler, Map[String, Object])} that is
   * called whenever the file selection is cancelled. It is perfectly legal not
   * to do anything.</li>
   * </ul>
   *
   * @param fileOpenCallback
   *          the fileOpenCallback to set.
   */
  public void setFileOpenCallback(IFileOpenCallback fileOpenCallback) {
    super.setFileCallback(fileOpenCallback);
    fileOpenCallbackAction = new FileOpenCallbackAction(fileOpenCallback);
  }

  /**
   * Gets the file save callback.
   *
   * @param context the action context.
   * @return the file save callback.
   */
  protected IFileOpenCallback getFileOpenCallback(Map<String, Object> context) {
    return (IFileOpenCallback) super.getFileCallback(context);
  }

  /**
   * Gets the fileCancelCallbackAction.
   *
   * @param context the action context.
   * @return the fileCancelCallbackAction.
   */
  protected FileOpenCallbackAction getFileOpenCallbackAction(Map<String, Object> context) {
    IFileOpenCallback callback = (IFileOpenCallback) context.get(FILE_CALLBACK);
    if (callback != null) {
      return new FileOpenCallbackAction(callback);
    }
    return fileOpenCallbackAction;
  }
}
