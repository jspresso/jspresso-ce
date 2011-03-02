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
package org.jspresso.framework.application;

import java.util.Locale;

import javax.security.auth.Subject;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.util.i18n.ITranslationProvider;


/**
 * This interface is implemented by the controllers of the application.
 * Controllers implement the interface since their main role is to execute
 * application actions.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IController extends IActionHandler {

  /**
   * This method gets executed when a user succesfully logs in.
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
   */
  ITranslationProvider getTranslationProvider();

  /**
   * Stops the controller. This method performs any necessary cleanup.
   * 
   * @return true if the stop was successful.
   */
  boolean stop();
}
