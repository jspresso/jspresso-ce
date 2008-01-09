/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
      urlSpec.append(urlSpec);
    }
    urlSpec.append((String) context.get(ActionContextConstants.ACTION_PARAM));

    if (urlSpec.length() > 0) {
      SAnchor downloadLink = new SAnchor(urlSpec.toString(), "downloadWindow");
      downloadLink.add(new SLabel(getTranslationProvider(context)
          .getTranslation("click.me", getLocale(context))));
      SOptionPane.showMessageDialog(getSourceComponent(context), downloadLink);
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
