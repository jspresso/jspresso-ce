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
package org.jspresso.framework.application.frontend.action.ulc.file;

import java.io.OutputStream;
import java.util.Map;

import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.file.IFileSaveCallback;

import com.ulcjava.base.application.ClientContext;
import com.ulcjava.base.application.util.serializable.IFileStoreHandler;
import com.ulcjava.base.shared.FileChooserConfig;

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
      final Map<String, Object> context) {
    try {
      ClientContext.storeFile(new IFileStoreHandler() {

        private static final long serialVersionUID = -1025629868916915262L;

        @SuppressWarnings("unused")
        public void onFailure(int reason, String description) {
          if (fileSaveCallback != null) {
            fileSaveCallback.cancel(context);
          }
        }

        public void onSuccess(String filePath) {
          createFileChooser(context).setCurrentDirectory(filePath);
          if (fileSaveCallback != null) {
            fileSaveCallback.fileWritten(filePath, context);
          }
        }

        public void prepareFile(OutputStream out) {
          if (fileSaveCallback != null) {
            fileSaveCallback.fileChosen(out, context);
          }
        }
      }, createFileChooser(context));
    } catch (Exception ex) {
      throw new ActionException(ex);
    }
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the fileSaveCallback.
   * 
   * @param fileSaveCallback
   *            the fileSaveCallback to set.
   */
  public void setFileSaveCallback(IFileSaveCallback fileSaveCallback) {
    this.fileSaveCallback = fileSaveCallback;
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
