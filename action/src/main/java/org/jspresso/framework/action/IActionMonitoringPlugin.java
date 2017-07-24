/*
 * Copyright (c) 2005-2017 Vincent Vandenschrick. All rights reserved.
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

/**
 * This is the general contract of an action monitoring plugin. This type of plugin can be used to monitor application
 * usage, actions performance, and so on. It should be installed on the application frontend controller and in that
 * case, will be shared with its peer backend controller, or on the backend controller only in order to monitor batches,
 * messages, webservices, ...
 *
 * @author Vincent Vandenschrick
 */
public interface IActionMonitoringPlugin {

  /**
   * Called when an action starts.
   *
   * @param action
   *     the action
   * @param context
   *     the context
   */
  void actionStart(IAction action,  Map<String, Object> context);

  /**
   * Called when an action ends.
   *
   * @param action
   *     the action
   * @param context
   *     the context
   */
  void actionEnd(IAction action, Map<String, Object> context);

  /**
   * Starts the plugin.
   *
   * @param context
   *     the context
   */
  void start(Map<String, Object> context);
}
