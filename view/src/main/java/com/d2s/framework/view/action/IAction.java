/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.action;

import java.util.Map;

/**
 * This interface establishes the contract of any action in the application.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IAction {

  /**
   * Executes the action. During execution, the action should access its
   * execution context through the <code>getContext()</code> method of the
   * <code>IContextAware</code> interface.
   * 
   * @param actionHandler
   *          the action handler this action has been told to execute by. It may
   *          be used to post another actio execution upon completion of this
   *          one.
   * @param context
   *          the execution context. The action should update it depending on
   *          its result.
   */
  void execute(IActionHandler actionHandler, Map<String, Object> context);

  /**
   * Tells the framework wether this action executes on the application model or
   * if it is a pure frontend action. this is aimed at distributing the action
   * execution correctly to the different controllers of the application.
   * 
   * @return <code>true</code> if the action needs the application model
   *         (domain model objects).
   */
  boolean isBackend();

  /**
   * Wether the action take a long time.
   * 
   * @return true if the action may take a long time.
   */
  boolean isLongOperation();
}
