/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.action;

import java.util.Map;

import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.util.exception.IExceptionHandler;


/**
 * This interface establishes the general contract of an object able to axacute
 * actions (controllers for instance). No assumption is made on wether this
 * action is to be executed (a)synchroniously, transactionnally, ...
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IActionHandler extends IExceptionHandler {

  /**
   * Checks authorization for secured access. It shoud throw a SecurityException
   * whenever access should not be granted.
   * 
   * @param securable
   *            the id of the secured access to check.
   */
  void checkAccess(ISecurable securable);

  /**
   * Creates an empty action context.
   * 
   * @return an empty action context.
   */
  Map<String, Object> createEmptyContext();

  /**
   * Executes an action. Implementors should delegate the execution to the
   * action itself but are free to set the context of the execution (action
   * context, synchronous or not, transactionality, ...).
   * 
   * @param action
   *            the action to be executed.
   * @param context
   *            the action execution context.
   * @return true whenever this action completes normally.
   * @see IAction#execute(IActionHandler, Map)
   */
  boolean execute(IAction action, Map<String, Object> context);

  /**
   * Retrieves the initial action context from the controller. This context is
   * passed to the action chain and contains application-wide context key-value
   * pairs.
   * 
   * @return the map representing the initial context provided by this
   *         controller.
   */
  Map<String, Object> getInitialActionContext();
}
