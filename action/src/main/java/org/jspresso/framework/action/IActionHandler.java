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
import java.util.TimeZone;

import javax.security.auth.Subject;

import org.jspresso.framework.security.ISecurityHandler;
import org.jspresso.framework.util.exception.IExceptionHandler;
import org.jspresso.framework.util.gui.EClientType;
import org.jspresso.framework.util.i18n.ITranslationProvider;

/**
 * This interface establishes the general contract of an object able to execute
 * actions (controllers for instance). No assumption is made on whether this
 * action is to be executed (a)synchronously, transactionally, ...
 *
 * @author Vincent Vandenschrick
 */
public interface IActionHandler extends IExceptionHandler, ISecurityHandler,
    ITranslationProvider {

  /**
   * Executes an action. Implementers should delegate the execution to the
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
   * Registers an action for later execution. It's up to the implementing class to decide when the queued actions
   * will be executed.
   *
   * @param action the action
   * @param context the context
   */
  void executeLater(IAction action, Map<String, Object> context);

  /**
   * Returns the JAAS subject attached to this action handler.
   *
   * @return the JAAS subject attached to this action handler.
   */
  Subject getSubject();

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

  /**
   * Gets the client timeZone.
   *
   * @return the client timeZone.
   */
  TimeZone getClientTimeZone();

  /**
   * Gets the reference timeZone.
   *
   * @return the reference timeZone.
   */
  TimeZone getReferenceTimeZone();

  /**
   * Gets the session client type.
   *
   * @return the client type
   */
  EClientType getClientType();
}
