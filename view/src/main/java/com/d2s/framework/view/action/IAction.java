/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.action;

import java.util.Collection;

import com.d2s.framework.util.context.IContextAware;

/**
 * This interface establishes the contract of any action in the application.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IAction extends IContextAware {

  /**
   * Executes the action. During execution, the action should access its
   * execution context through the <code>getContext()</code> method of the
   * <code>IContextAware</code> interface.
   * 
   * @param actionHandler
   *          the action handler this action has been told to execute by. It may
   *          be used to post another actio execution upon completion of this
   *          one.
   */
  void execute(IActionHandler actionHandler);

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
   * Gets the inputContextKeys. These keys index the context values used by the
   * action to execute. The values are accessed using
   * <code>getContext().get(key)</code>.
   * 
   * @return the inputContextKeys.
   */
  Collection<String> getInputContextKeys();

  /**
   * Wether the action take a long time.
   * 
   * @return true if the action may take a long time.
   */
  boolean isLongOperation();
}
