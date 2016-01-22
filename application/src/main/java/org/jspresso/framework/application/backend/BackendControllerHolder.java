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
package org.jspresso.framework.application.backend;

import javax.servlet.http.HttpSession;

import org.jspresso.framework.util.http.HttpRequestHolder;

/**
 * Holds the current thread backend controller.
 *
 * @author Vincent Vandenschrick
 */
public final class BackendControllerHolder {

  private static final boolean IS_WEB_CONTEXT;
  /**
   * {@code CURRENT_BACKEND_CONTROLLER_KEY}.
   */
  public static final  String                          CURRENT_BACKEND_CONTROLLER_KEY = "CURRENT_BACKEND_CONTROLLER";
  private static final ThreadLocal<IBackendController> THREADBOUND_BACKEND_CONTROLLER = new InheritableThreadLocal<>();
  private static final ThreadLocal<IBackendController> THREADLOCAL_BACKEND_CONTROLLER = new ThreadLocal<>();

  static {
    boolean wc = false;
    try {
      Class.forName("org.jspresso.framework.util.http.HttpRequestHolder");
      wc = true;
    } catch (Throwable ex) {
      // Not in web context
    }
    IS_WEB_CONTEXT = wc;
  }

  private BackendControllerHolder() {
    // Helper constructor.
  }

  /**
   * Sets the session-bound backend controller.
   *
   * @param controller
   *          the tread-bound backend controller.
   */
  public static void setSessionBackendController(IBackendController controller) {
    // First try to bind to the HttpSession
    if (IS_WEB_CONTEXT && HttpRequestHolder.isAvailable()) {
      HttpSession session = HttpRequestHolder.getServletRequest().getSession();
      if (session != null) {
        session.setAttribute(CURRENT_BACKEND_CONTROLLER_KEY, controller);
      }
    } else {
      setThreadBackendController(controller);
    }
  }

  /**
   * Sets the tread-bound backend controller.
   *
   * @param controller
   *          the tread-bound backend controller.
   */
  public static void setThreadBackendController(IBackendController controller) {
    THREADBOUND_BACKEND_CONTROLLER.set(controller);
    THREADLOCAL_BACKEND_CONTROLLER.set(controller);
  }

  /**
   * Gets the backend controller belonging to this thread without getting the
   * parent thread value.
   *
   * @return the backend controller belonging to this thread without getting the
   *         parent thread value.
   */
  public static IBackendController getThreadBackendController() {
    return THREADLOCAL_BACKEND_CONTROLLER.get();
  }

  /**
   * Gets the tread-bound backend controller.
   *
   * @return the tread-bound backend controller.
   */
  public static IBackendController getCurrentBackendController() {
    IBackendController controller;
    // First lookup into the current thread
    controller = THREADBOUND_BACKEND_CONTROLLER.get();
    // If none is set, then query the session.
    if (controller == null && IS_WEB_CONTEXT && HttpRequestHolder.isAvailable()) {
      HttpSession session = HttpRequestHolder.getServletRequest().getSession();
      if (session != null) {
        controller = (IBackendController) session
            .getAttribute(CURRENT_BACKEND_CONTROLLER_KEY);
      }
    }
    return controller;
  }
}
