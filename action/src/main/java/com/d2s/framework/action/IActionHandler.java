/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.action;

import java.util.Map;

/**
 * This interface establishes the general contract of an object able to axacute
 * actions (controllers for instance). No assumption is made on wether this
 * action is to be executed (a)synchroniously, transactionnally, ...
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IActionHandler {

  /**
   * Executes an action. Implementors should delegate the execution to the
   * action itself but are free to set the context of the execution (action
   * context, synchronous or not, transactionality, ...).
   * 
   * @param action
   *          the action to be executed.
   * @return the map containing the result of the action execution (return
   *         values, exceptions, messages, ...), generally the result map return
   *         by the action itself.
   * @see IAction#execute(IActionHandler)
   */
  Map<String, Object> execute(IAction action);

}
