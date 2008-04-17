/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.std;

import java.util.Map;

import org.jspresso.framework.application.frontend.action.AbstractChainedAction;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IAction;
import com.d2s.framework.action.IActionHandler;

/**
 * Executes the action set in the context using the key ACTION_PARAM.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            the actual gui component type used.
 * @param <F>
 *            the actual icon type used.
 * @param <G>
 *            the actual action type used.
 */
public class ExecuteActionAction<E, F, G> extends
    AbstractChainedAction<E, F, G> {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    IAction action = (IAction) context.get(ActionContextConstants.ACTION_PARAM);
    return actionHandler.execute(action, context)
        && super.execute(actionHandler, context);
  }

}
