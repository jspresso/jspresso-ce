/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action;

import java.util.Map;

import com.d2s.framework.view.action.IAction;
import com.d2s.framework.view.action.IActionHandler;

/**
 * An simple frontend swing action which holds a reference on a chained next
 * action.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 */
public abstract class AbstractChainedAction<E, F> extends
    AbstractFrontendAction<E, F> {

  private IAction nextAction;

  /**
   * Gets the next action reference.
   * 
   * @return the next action to execute.
   * @see #setNextAction(IAction)
   */
  public IAction getNextAction() {
    return nextAction;
  }

  /**
   * Sets the nextAction.
   * 
   * @param nextAction
   *          the next action to execute.
   */
  public void setNextAction(IAction nextAction) {
    this.nextAction = nextAction;
  }

  /**
   * Executes the next action.
   * <p>
   * {@inheritDoc}
   */
  public void execute(IActionHandler actionHandler, Map<String, Object> context) {
    if (nextAction != null) {
      actionHandler.execute(nextAction, context);
    }
  }

}
