/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.wings.std;

import java.util.Map;

import org.wings.script.JavaScriptListener;
import org.wings.script.ScriptListener;
import org.wings.session.SessionManager;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.wings.AbstractWingsAction;

/**
 * A simple action to display an static Url content.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DisplayUrlAction extends AbstractWingsAction {

  private String baseUrl;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    StringBuffer urlSpec = new StringBuffer();
    if (baseUrl != null) {
      urlSpec.append(baseUrl);
    }
    urlSpec.append((String) context.get(ActionContextConstants.ACTION_PARAM));

    if (urlSpec.length() > 0) {
      ScriptListener listener = new JavaScriptListener(null, null,
          "location.href='" + urlSpec.toString() + "'; location.target = 'download'");
      SessionManager.getSession().getScriptManager()
          .addScriptListener(listener);
    }
    return true;
  }

  /**
   * Sets the baseUrl.
   * 
   * @param baseUrl
   *            the baseUrl to set.
   */
  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }
}
