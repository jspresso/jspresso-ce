/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action;

import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;


/**
 * An simple frontend action which holds a reference on a chained next action.
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
public abstract class AbstractChainedAction<E, F, G> extends
    AbstractFrontendAction<E, F, G> {

  private IAction nextAction;

  /**
   * Executes the next action.
   * <p>
   * {@inheritDoc}
   */
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    if (getNextAction(context) != null) {
      return actionHandler.execute(getNextAction(context), context);
    }
    return true;
  }

  /**
   * Gets the next action reference. If the next action has been configured
   * strongly through the setter method, it is directly returned. If not, it is
   * looked up into the action context.
   * 
   * @param context
   *            the action context.
   * @return the next action to execute.
   * @see #setNextAction(IAction)
   */
  public IAction getNextAction(Map<String, Object> context) {
    if (nextAction != null) {
      return nextAction;
    }
    return (IAction) context.get(ActionContextConstants.NEXT_ACTION);
  }

  /**
   * Sets the nextAction.
   * 
   * @param nextAction
   *            the next action to execute.
   */
  public void setNextAction(IAction nextAction) {
    this.nextAction = nextAction;
  }

}
