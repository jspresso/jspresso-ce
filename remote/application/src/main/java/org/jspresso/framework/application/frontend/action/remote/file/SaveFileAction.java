/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.command.remote.RemoteFileDownloadCommand;
import org.jspresso.framework.application.frontend.file.IFileSaveCallback;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.util.resources.AbstractActiveResource;
import org.jspresso.framework.util.resources.server.ResourceManager;
import org.jspresso.framework.util.resources.server.ResourceProviderServlet;
import org.jspresso.framework.view.IView;

/**
 * This action lets the user browse his local file system and choose a file to
 * write some content to. What is done with the file content is determined by
 * the configured <code>fileSaveCallback</code> instance.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SaveFileAction extends ChooseFileAction {

  private String            contentType;
  private IFileSaveCallback fileSaveCallback;

  /**
   * Constructs a new <code>SaveFileAction</code> instance.
   */
  public SaveFileAction() {
    this.contentType = "application/octet-stream";
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    RemoteFileDownloadCommand fileDownloadCommand = new RemoteFileDownloadCommand();
    fileDownloadCommand.setFileFilter(translateFilter(getFileFilter(context),
        context));
    String fileName = getFileName(context);
    fileDownloadCommand.setDefaultFileName(fileName);
    String resourceId = ResourceManager.getInstance().register(
        new ResourceAdapter(fileName, getContentType(), fileSaveCallback,
            actionHandler, context));
    fileDownloadCommand.setResourceId(resourceId);
    fileDownloadCommand.setFileUrl(ResourceProviderServlet
        .computeDownloadUrl(resourceId));
    RAction cancelCallbackAction = getActionFactory(context).createAction(
        createCancelCallbackAction(fileSaveCallback), actionHandler,
        (IView<RComponent>) getView(context), getLocale(context));
    fileDownloadCommand.setCancelCallbackAction(cancelCallbackAction);
    registerCommand(fileDownloadCommand, context);
    return super.execute(actionHandler, context);
  }

  /**
   * Gets the contentType.
   * 
   * @return the contentType.
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * Configures the conten type to be used whenever the UI technology used
   * requires a download. The content type defaults to
   * <code>&quot;application/octet-stream&quot;</code>.
   * 
   * @param contentType
   *          the contentType to set.
   */
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  /**
   * Configures the file save callback instance that will be used to deal with
   * the file dialog events. Three methods must be implemented :
   * <ul>
   * <li>
   * <code>fileChosen(OutputStream, IActionHandler, Map&lt;String, Object&gt;)</code>
   * that is called whenever a file has been chosen. The output stream that is
   * passed as parameter allows for writing to the chosen file. The developer
   * doesn't have to cope with flushing nor closing the stream.</li>
   * <li><code>getFileName(Map&lt;String, Object&gt;)</code> that is called to
   * give a chance top the callback to compute a file name dynamically depending
   * on the action context. Whenever the callback returns a <code>null</code> or
   * empty file name, the default file name parameterized in the application is
   * used.</li>
   * <li><code>cancel(IActionHandler, Map&lt;String, Object&gt;)</code> that is
   * called whenever the file selection is cancelled. It is perfectly legal not
   * to do anything.</li>
   * </ul>
   * 
   * @param fileSaveCallback
   *          the fileSaveCallback to set.
   */
  public void setFileSaveCallback(IFileSaveCallback fileSaveCallback) {
    this.fileSaveCallback = fileSaveCallback;
  }

  /**
   * Computes a file name to save the file. Queries the file save callback for a
   * file name and defaults to the action default one if none is returned.
   * 
   * @param context
   *          the action context.
   * @return the file name to save the file under.
   */
  @Override
  protected String getFileName(Map<String, Object> context) {
    if (fileSaveCallback != null) {
      String fileName = fileSaveCallback.getFileName(context);
      if (fileName != null && fileName.length() > 0) {
        return fileName;
      }
    }
    return super.getFileName(context);
  }

  private static class ResourceAdapter extends AbstractActiveResource {

    private IActionHandler      actionHandler;
    private Map<String, Object> context;
    private String              name;
    private IFileSaveCallback   source;

    public ResourceAdapter(String name, String contentType,
        IFileSaveCallback source, IActionHandler actionHandler,
        Map<String, Object> context) {
      super(contentType);
      this.name = name;
      this.source = source;
      this.actionHandler = actionHandler;
      this.context = new HashMap<String, Object>();
      if (context != null) {
        this.context = context;
      }
    }

    /**
     * Gets the name.
     * 
     * @return the name.
     */
    public String getName() {
      return name;
    }

    /**
     * {@inheritDoc}
     */
    public long getSize() {
      return -1; // unknown.
    }

    /**
     * {@inheritDoc}
     */
    public void writeToContent(OutputStream out) throws IOException {
      source.fileChosen(getName(), out, actionHandler, context);
    }
  }
}
