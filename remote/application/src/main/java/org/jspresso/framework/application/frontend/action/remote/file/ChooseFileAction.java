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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.application.frontend.action.remote.AbstractRemoteAction;
import org.jspresso.framework.application.frontend.file.IFileCallback;

/**
 * This is the abstract base class for actions dealing with client file system
 * operations. It holds several parametrizations that are common to all file
 * operations. Please, be aware that these FS actions heavily depend on the UI
 * channel, i.e. you have different implementation classes (but registered under
 * the same Spring name) for all supported UI technologies. For instance,
 * {@code SaveFileAction} will have as many implementations as the number
 * of supported UIs, each in a specific package :
 * <p>
 * {@code org.jspresso.framework.application.frontend.action.}<b>
 * {@code [ui]}</b>{@code .file.SaveFileAction}
 *
 * @author Vincent Vandenschrick
 */
public abstract class ChooseFileAction extends AbstractRemoteAction {

  /**
   * {@code FILE_CALLBACK} is "FILE_CALLBACK".
   */
  public static final String FILE_CALLBACK = "FILE_CALLBACK";

  private String                    defaultFileName;
  private FileCancelCallbackAction  fileCancelCallbackAction;
  private Map<String, List<String>> fileFilter;
  private IFileCallback             fileCallback;

  /**
   * Configures a default file name to be used whenever a file needs to be
   * chosen. Subclasses ma use their specific callback to override this name
   * with a more dynamically computed one.
   *
   * @param defaultFileName
   *          the defaultFileName to set.
   */
  public void setDefaultFileName(String defaultFileName) {
    this.defaultFileName = defaultFileName;
  }

  /**
   * Configures the file filters to be used whenever the UI technology supports
   * it in the file choosing dialog. Filter file types are a map of descriptions
   * keying file extension lists.
   * <p>
   * For instance, an entry in this map could be :
   * <ul>
   * <li>key : {@code &quot;images&quot;}</li>
   * <li>value :
   * {@code [&quot;.png&quot;,&quot;.jpg&quot;,&quot;.gif&quot;,&quot;.bmp&quot;]}
   * </li>
   * </ul>
   *
   * @param fileFilter
   *          the fileFilter to set.
   */
  public void setFileFilter(Map<String, List<String>> fileFilter) {
    this.fileFilter = fileFilter;
  }

  /**
   * Sets the file callback and performs necessary initializations.
   *
   * @param fileCallback
   *          the file callback.
   */
  protected void setFileCallback(IFileCallback fileCallback) {
    this.fileCallback = fileCallback;
    fileCancelCallbackAction = new FileCancelCallbackAction(fileCallback);
  }

  /**
   * Gets the defaultFileName.
   *
   * @return the defaultFileName.
   */
  protected String getDefaultFileName() {
    return defaultFileName;
  }

  /**
   * Gets the fileFilter.
   *
   * @param context
   *          the action context.
   * @return the fileFilter.
   */
  protected Map<String, List<String>> getFileFilter(Map<String, Object> context) {
    return fileFilter;
  }

  /**
   * Computes a file name to save the file. Defaults to the action default file
   * name parametrized in the action.
   *
   * @param context
   *          the action context.
   * @return the file name to save the file under.
   */
  protected String getFileName(Map<String, Object> context) {
    return getDefaultFileName();
  }

  /**
   * Gets the fileCancelCallbackAction.
   *
   * @param context the action context.
   * @return the fileCancelCallbackAction.
   */
  protected FileCancelCallbackAction getFileCancelCallbackAction(Map<String, Object> context) {
    IFileCallback callback = (IFileCallback) context.get(FILE_CALLBACK);
    if (callback != null) {
      return new FileCancelCallbackAction(callback);
    }
    return fileCancelCallbackAction;
  }

  /**
   * Translates the file filter for usage in remote commands.
   *
   * @param executionFileFilter
   *          the file filter to translate.
   * @param context
   *          the action context.
   * @return the translated file filter.
   */
  protected Map<String, String[]> translateFilter(
      Map<String, List<String>> executionFileFilter, Map<String, Object> context) {
    if (executionFileFilter == null) {
      return null;
    }
    Map<String, String[]> translatedFileFilter = new HashMap<>();
    for (Map.Entry<String, List<String>> filterEntry : executionFileFilter
        .entrySet()) {
      List<String> var = filterEntry.getValue();
      translatedFileFilter.put(
          getTranslationProvider(context).getTranslation(filterEntry.getKey(),
              getLocale(context)), var.toArray(new String[var.size()]));
    }
    return translatedFileFilter;
  }

  /**
   * Gets the fileCallback.
   *
   * @param context the action context.
   * @return the fileCallback.
   */
  protected IFileCallback getFileCallback(Map<String, Object> context) {
    IFileCallback callback = (IFileCallback) context.get(FILE_CALLBACK);
    if (callback == null) {
      callback = fileCallback;
    }
    return callback;
  }
}
