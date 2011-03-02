/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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

import javax.security.auth.Subject;

import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.util.exception.IExceptionHandler;

/**
 * This interface establishes the general contract of an object able to axacute
 * actions (controllers for instance). No assumption is made on wether this
 * action is to be executed (a)synchroniously, transactionnally, ...
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
   *          the id of the secured access to check.
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
   *          the action to be executed.
   * @param context
   *          the action execution context.
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

  /**
   * Returns the JAAS subject attached to this action handler.
   * 
   * @return the JAAS subject attached to this action handler.
   */
  Subject getSubject();

  /**
   * Checks authorization for secured access.
   * 
   * @param securable
   *          the id of the secured access to check.
   * @return true if access is granted.
   */
  boolean isAccessGranted(ISecurable securable);

  /**
   * Reads a user preference.
   * 
   * @param key
   *          the key under which the preference as been stored.
   * @return the stored preference or null.
   */
  String getUserPreference(String key);

  /**
   * Stores a user preference.
   * 
   * @param key
   *          the key under which the preference as to be stored.
   * @param value
   *          the value of the preference to be stored.
   */
  void putUserPreference(String key, String value);

  /**
   * Deletes a user preference.
   * 
   * @param key
   *          the key under which the preference is stored.
   */
  void removeUserPreference(String key);
}
