/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.swing.std;

import java.io.IOException;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.ActionException;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.swing.AbstractSwingAction;
import com.d2s.framework.util.swing.BrowserControl;

/**
 * A simple action to display an static Url content.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
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
    String urlSpec = baseUrl
        + (String) context.get(ActionContextConstants.ACTION_PARAM);

    try {
      BrowserControl.displayURL(urlSpec);
    } catch (IOException ex) {
      throw new ActionException(ex);
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
