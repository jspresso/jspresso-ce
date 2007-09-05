/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.wings.std;

import java.util.Map;

import org.wings.SAnchor;
import org.wings.SLabel;
import org.wings.SOptionPane;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.wings.AbstractWingsAction;

/**
 * A simple action to display an static Url content.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
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
    String urlSpec = baseUrl
        + (String) context.get(ActionContextConstants.ACTION_PARAM);
    SAnchor downloadLink = new SAnchor(urlSpec, "downloadWindow");
    downloadLink.add(new SLabel(getTranslationProvider(context).getTranslation(
        "click.me", getLocale(context))));
    SOptionPane.showMessageDialog(getSourceComponent(context), downloadLink);
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
