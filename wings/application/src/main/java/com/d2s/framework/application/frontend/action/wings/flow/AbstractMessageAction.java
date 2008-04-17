/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.wings.flow;

import java.util.Map;

import org.jspresso.framework.util.html.HtmlHelper;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.wings.AbstractWingsAction;

/**
 * Base class for all message wings actions. It just keeps a reference on the
 * message to be displayed.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractMessageAction extends AbstractWingsAction {

  /**
   * Calls the super-implementation to execute the next action.
   * 
   * @param actionHandler
   *            the action handler responsible for the action execution.
   * @param context
   *            the action context.
   */
  protected void executeNextAction(IActionHandler actionHandler,
      Map<String, Object> context) {
    super.execute(actionHandler, context);
  }

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
