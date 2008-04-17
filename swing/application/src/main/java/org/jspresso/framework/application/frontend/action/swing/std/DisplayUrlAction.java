/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.swing.std;

import java.io.IOException;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.swing.AbstractSwingAction;
import org.jspresso.framework.util.swing.BrowserControl;


/**
 * A simple action to display an static Url content.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DisplayUrlAction extends AbstractSwingAction {

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
      try {
        BrowserControl.displayURL(urlSpec.toString());
      } catch (IOException ex) {
        throw new ActionException(ex);
      }
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
