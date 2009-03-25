/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.frontend;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.application.IController;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.binding.IMvcBinder;
import org.jspresso.framework.util.descriptor.IIconDescriptor;
import org.jspresso.framework.view.IViewFactory;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.action.IActionable;

/**
 * General contract of frontend (view) application controllers.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public interface IFrontendController<E, F, G> extends IController,
    IIconDescriptor, IActionable {

  /**
   * Retrieves a map of help action lists to be presented on this view.
   * 
   * @return the map of action lists handled by this view.
   */
  ActionMap getHelpActions();

  /**
   * Gets the mvc binder used by this controller.
   * 
   * @return the mvc binder used by this controller.
   */
  IMvcBinder getMvcBinder();

  /**
   * Gets the action which is executed when the controller is started.
   * 
   * @return the action which is executed when the controller is started.
   */
  IAction getStartupAction();

  /**
   * Gets the view factory used by this controller.
   * 
   * @return the view factory used by this controller.
   */
  IViewFactory<E, F, G> getViewFactory();

  /**
   * Starts the controller. This method performs any necessary initializations
   * (such as binding to the backend controller) and shows the initial view to
   * the user. The initial view is generally built from the root view
   * descriptor.
   * 
   * @param backendController
   *          the backend controller to bind to.
   * @param clientLocale
   *          the locale this controller should use to initiate the login
   *          session while not knowing yet the user locale.
   * @return true if the controller succesfully started.
   */
  boolean start(IBackendController backendController, Locale clientLocale);

  /**
   * Displays a workspace.
   * 
   * @param workspaceName
   *          the workspace identifier.
   */
  void displayWorkspace(String workspaceName);

  /**
   * Displays a modal dialog.
   * 
   * @param mainView
   *          the view to install in the modal dialog.
   * @param actions
   *          the actions available in the dialog.
   * @param title
   *          the dialog title.
   * @param sourceComponent
   *          the source component.
   * @param context
   *          the context to store on the context stack.
   * @param reuseCurrent
   *          set to true to reuse an existing modal dialog.
   */
  void displayModalDialog(E mainView, List<G> actions, String title,
      E sourceComponent, Map<String, Object> context, boolean reuseCurrent);

  /**
   * Disposes a modal dialog.
   * 
   * @param sourceWidget
   *          the source widget.
   * @param context
   *          the context to complement with the head of the context stack.
   */
  void disposeModalDialog(E sourceWidget, Map<String, Object> context);

  /**
   * Displays the given URL in a new braowser window (or tab).
   * 
   * @param urlSpec
   *          the url to display.
   */
  void displayUrl(String urlSpec);
}
