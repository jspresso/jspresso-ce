/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.ulc.flow;

import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.application.frontend.action.ulc.AbstractUlcAction;
import org.jspresso.framework.util.html.HtmlHelper;


/**
 * Base class for all message ULC actions. It just keeps a reference on the
 * message to be displayed.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractMessageAction extends AbstractUlcAction {

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
