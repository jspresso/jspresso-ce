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
package org.jspresso.framework.application.frontend.action.wings.file;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.file.IFileSaveCallback;
import org.wings.externalizer.AbstractExternalizeManager;
import org.wings.io.Device;
import org.wings.io.DeviceOutputStream;
import org.wings.resource.DynamicResource;
import org.wings.script.JavaScriptListener;
import org.wings.script.ScriptListener;
import org.wings.session.SessionManager;

/**
 * Initiates a file save action.
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
  public boolean execute(final IActionHandler actionHandler,
      final Map<String, Object> context) {
    if (fileSaveCallback != null) {
      DynamicResource resource = new DynamicResource(
          getSourceComponent(context).getParentFrame()) {

        private static final long serialVersionUID = 2216910348294774650L;

        public void write(Device device) {
          DeviceOutputStream out = new DeviceOutputStream(device);
          try {
            fileSaveCallback.fileChosen(getName(), out, actionHandler, context);
            out.flush();
          } catch (IOException ex) {
            throw new ActionException(ex);
          } finally {
            try {
              out.close();
              device.close();
            } catch (IOException ex) {
              // NO-OP.
            }
          }
        }
      };
      Map<String, String> headers = new HashMap<String, String>();
      headers.put("Content-Disposition", "attachment; filename="
          + getFileName(context));
      String url = SessionManager
          .getSession()
          .getExternalizeManager()
          .externalize(resource, headers.entrySet(),
              AbstractExternalizeManager.REQUEST);
      ScriptListener listener = new JavaScriptListener(null, null,
          "location.href='" + url.toString() + "'");
      SessionManager.getSession().getScriptManager()
          .addScriptListener(listener);
    }
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
}
