/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action;

import java.util.Map;

import com.d2s.framework.view.action.IAction;
import com.d2s.framework.view.action.IActionHandler;

/**
 * A simple frontend action to wrap a backend action.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ActionWrapper extends AbstractChainedAction {

  private IAction wrappedAction;

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute(IActionHandler actionHandler, Map<String, Object> context) {
    actionHandler.execute(wrappedAction, context);
    if (getNextAction() != null) {
      super.execute(actionHandler, context);
    }
  }

  /**
   * Gets the wrappedAction.
   * 
   * @return the wrappedAction.
   */
  protected IAction getWrappedAction() {
    return wrappedAction;
  }

  /**
   * Sets the wrappedAction.
   * 
   * @param wrappedAction
   *          the wrappedAction to set.
   */
  public void setWrappedAction(IAction wrappedAction) {
    this.wrappedAction = wrappedAction;
  }

}
