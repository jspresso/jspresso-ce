/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.ulc.std;

import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;


/**
 * A simple action to display an static Url content.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DisplayStaticUrlAction extends DisplayUrlAction {

  private String urlKey;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    context.put(ActionContextConstants.ACTION_PARAM, getTranslationProvider(
        context).getTranslation(urlKey, getLocale(context)));
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the urlKey.
   * 
   * @param urlKey
   *            the urlKey to set.
   */
  public void setUrlKey(String urlKey) {
    this.urlKey = urlKey;
  }
}
