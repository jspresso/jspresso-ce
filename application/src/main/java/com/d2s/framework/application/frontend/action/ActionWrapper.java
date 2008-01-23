/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.frontend.action;

import java.util.Map;

import com.d2s.framework.action.IAction;
import com.d2s.framework.action.IActionHandler;

/**
 * A simple frontend action to wrap a backend action.
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
public class ActionWrapper<E, F, G> extends AbstractChainedAction<E, F, G> {

  private IAction wrappedAction;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    if (actionHandler.execute(wrappedAction, context)) {
      if (getNextAction(context) != null) {
        return super.execute(actionHandler, context);
      }
      return true;
    }
    return false;
  }

  /**
   * Sets the wrappedAction.
   * 
   * @param wrappedAction
   *            the wrappedAction to set.
   */
  public void setWrappedAction(IAction wrappedAction) {
    this.wrappedAction = wrappedAction;
  }

  /**
   * Gets the wrappedAction.
   * 
   * @return the wrappedAction.
   */
  protected IAction getWrappedAction() {
    return wrappedAction;
  }

}
