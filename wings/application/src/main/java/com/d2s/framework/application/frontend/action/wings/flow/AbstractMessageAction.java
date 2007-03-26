/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.wings.flow;

import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.wings.AbstractWingsAction;
import com.d2s.framework.util.html.HtmlHelper;

/**
 * Base class for all message wings actions. It just keeps a reference on the
 * message to be displayed.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractMessageAction extends AbstractWingsAction {

  /**
   * Gets the message.
   * 
   * @param context
   *          the actionContext.
   * @return the message.
   */
  protected String getMessage(Map<String, Object> context) {
    return HtmlHelper.emphasis((String) context
        .get(ActionContextConstants.ACTION_PARAM));
  }

  /**
   * Calls the super-implementation to execute the next action.
   * 
   * @param actionHandler
   *          the action handler responsible for the action execution.
   * @param context
   *          the action context.
   */
  protected void executeNextAction(IActionHandler actionHandler,
      Map<String, Object> context) {
    super.execute(actionHandler, context);
  }
}
