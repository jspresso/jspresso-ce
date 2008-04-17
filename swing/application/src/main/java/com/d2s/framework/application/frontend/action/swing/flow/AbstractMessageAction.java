/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.swing.flow;

import java.util.Map;

import org.jspresso.framework.util.html.HtmlHelper;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.application.frontend.action.swing.AbstractSwingAction;

/**
 * Base class for all message swing actions. It just keeps a reference on the
 * message to be displayed.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractMessageAction extends AbstractSwingAction {

  /**
   * Gets the message.
   * 
   * @param context
   *            the actionContext.
   * @return the message.
   */
  protected String getMessage(Map<String, Object> context) {
    return HtmlHelper.emphasis((String) context
        .get(ActionContextConstants.ACTION_PARAM));
  }
}
