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
package org.jspresso.framework.application.frontend.action.ulc.file;

import java.io.InputStream;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.file.IFileOpenCallback;

import com.ulcjava.base.application.ClientContext;
import com.ulcjava.base.application.util.serializable.IFileLoadHandler;
import com.ulcjava.base.shared.FileChooserConfig;

/**
 * Initiates a file open action.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
public class OpenFileAction extends ChooseFileAction {

  private IFileOpenCallback fileOpenCallback;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(final IActionHandler actionHandler,
      final Map<String, Object> context) {
    ClientContext.loadFile(new IFileLoadHandler() {

      private static final long serialVersionUID = -1025629868916915262L;

      @SuppressWarnings("unused")
      public void onFailure(int reason, String description) {
        if (fileOpenCallback != null) {
          fileOpenCallback.cancel(actionHandler, context);
        }
      }

      public void onSuccess(InputStream in, String filePath) {
        if (fileOpenCallback != null) {
          createFileChooser(context).setCurrentDirectory(filePath);
          fileOpenCallback.fileChosen(in, actionHandler, context);
        }
      }
    }, createFileChooser(context));
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the fileOpenCallback.
   * 
   * @param fileOpenCallback
   *            the fileOpenCallback to set.
   */
  public void setFileOpenCallback(IFileOpenCallback fileOpenCallback) {
    this.fileOpenCallback = fileOpenCallback;
  }

  /**
   * Completes the file chooser configuration with behaviour specific to open
   * action.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected FileChooserConfig createFileChooser(Map<String, Object> context) {
    FileChooserConfig fileChooser = super.createFileChooser(context);
    return fileChooser;
  }
}
