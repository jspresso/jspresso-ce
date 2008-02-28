/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.wings.file;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.wings.externalizer.AbstractExternalizeManager;
import org.wings.io.Device;
import org.wings.io.DeviceOutputStream;
import org.wings.resource.DynamicResource;
import org.wings.script.JavaScriptListener;
import org.wings.script.ScriptListener;
import org.wings.session.SessionManager;

import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.file.IFileSaveCallback;

/**
 * Initiates a file save action.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
    if (fileSaveCallback != null) {
      DynamicResource resource = new DynamicResource(
          getSourceComponent(context).getParentFrame()) {

        private static final long serialVersionUID = 2216910348294774650L;

        public void write(Device device) throws IOException {
          DeviceOutputStream out = new DeviceOutputStream(device);
          fileSaveCallback.fileChosen(out, context);
          out.flush();
          device.close();
        }
      };
      Map<String, String> headers = new HashMap<String, String>();
      headers.put("Content-Disposition", "attachment; filename="
          + getDefaultFileName());
      String url = SessionManager.getSession().getExternalizeManager()
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
   *            the fileSaveCallback to set.
   */
  public void setFileSaveCallback(IFileSaveCallback fileSaveCallback) {
    this.fileSaveCallback = fileSaveCallback;
  }
}
