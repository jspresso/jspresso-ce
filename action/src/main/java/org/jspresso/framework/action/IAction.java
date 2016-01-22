/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.action;

import java.util.Map;

import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.util.automation.IPermIdSource;
import org.jspresso.framework.util.lang.ICloneable;

/**
 * This interface establishes the contract of any action in the application.
 *
 * @author Vincent Vandenschrick
 */
public interface IAction extends ISecurable, IPermIdSource, ICloneable {

  /**
   * {@code STATIC_CONTEXT_KEY} is "STATIC_CONTEXT_KEY".
   */
  String STATIC_CONTEXT_KEY = "STATIC_CONTEXT_KEY";

  /**
   * Executes the action. During execution, the action should access its
   * execution context through the {@code getContext()} method of the
   * {@code IContextAware} interface.
   *
   * @param actionHandler
   *          the action handler this action has been told to execute by. It may
   *          be used to post another action execution upon completion of this
   *          one.
   * @param context
   *          the execution context. The action should update it depending on
   *          its result.
   * @return true whenever this action completes normally.
   */
  boolean execute(IActionHandler actionHandler, Map<String, Object> context);

  /**
   * Tells the framework whether this action executes on the application backend
   * or if it is a pure frontend action. this is aimed at distributing the
   * action execution correctly to the different controllers of the application.
   *
   * @return {@code true} if the action needs the application model (domain
   *         model objects).
   */
  boolean isBackend();
}
