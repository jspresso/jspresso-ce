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
package org.jspresso.framework.application;

import java.util.Locale;
import java.util.Map;

import javax.security.auth.Subject;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.util.bean.IPropertyChangeCapable;
import org.jspresso.framework.util.gui.EClientType;
import org.jspresso.framework.util.i18n.ITranslationProvider;

/**
 * This interface is implemented by the controllers of the application.
 * Controllers implement the interface since their main role is to execute
 * application actions.
 *
 * @author Vincent Vandenschrick
 */
public interface IController extends IActionHandler, IPropertyChangeCapable {

  /**
   * {@code SELECTED_MODULE} is "selectedModule".
   */
  String SELECTED_MODULE = "selectedModule";

  /**
   * {@code SELECTED_WORKSPACE} is "selectedWorkspace".
   */
  String SELECTED_WORKSPACE = "selectedWorkspace";

  /**
   * This method gets executed when a user successfully logs in.
   *
   * @param subject
   *          the authenticated user subject.
   */
  void loggedIn(Subject subject);

  /**
   * Gets the applicationSession for this backend controller.
   *
   * @return the current controller application session.
   */
  IApplicationSession getApplicationSession();

  /**
   * Gets the current controller locale.
   *
   * @return the current controller locale.
   */
  Locale getLocale();

  /**
   * Gets the translation provider used by this controller.
   *
   * @return the translation provider used by this controller.
   * @deprecated the controller is now a translation provider by itself.
   */
  @Deprecated
  ITranslationProvider getTranslationProvider();

  /**
   * Stops the controller. This method performs any necessary cleanup.
   *
   * @return true if the stop was successful.
   */
  boolean stop();

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
