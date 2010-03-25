/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.binding.IMvcBinder;
import org.jspresso.framework.util.descriptor.IIconDescriptor;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.view.IViewFactory;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.action.IActionable;

/**
 * General contract of frontend (view) application controllers.
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
   * Gets the peer model controller.
   * 
   * @return the backend controller this frontend controller is attached to.
   */
  IBackendController getBackendController();

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
   * @param dimension
   *          the dimension to set the dialog to. If null, the dialog will be
   *          sized to the preferred size of the contained view.
   * @param reuseCurrent
   *          set to true to reuse an existing modal dialog.
   */
  void displayModalDialog(E mainView, List<G> actions, String title,
      E sourceComponent, Map<String, Object> context, Dimension dimension,
      boolean reuseCurrent);

  /**
   * Displays the given URL in a new browser window (or tab).
   * 
   * @param urlSpec
   *          the url to display.
   */
  void displayUrl(String urlSpec);

  /**
   * Displays a workspace.
   * 
   * @param workspaceName
   *          the workspace identifier.
   */
  void displayWorkspace(String workspaceName);

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
   * Displays a flash object on the client in a modal dialog.
   * 
   * @param swfUrl
   *          the URL of the swf to load.
   * @param flashContext
   *          the flash context from which the flashVars is computed.
   * @param actions
   *          the actions available in the dialog.
   * @param title
   *          the dialog title.
   * @param sourceComponent
   *          the source component.
   * @param context
   *          the context to store on the context stack.
   * @param dimension
   *          the dimension to set the dialog to. If null, the dialog will be
   *          sized to the preferred size of the contained view.
   * @param reuseCurrent
   *          set to true to reuse an existing modal dialog.
   */
  void displayFlashObject(String swfUrl, Map<String, String> flashContext,
      List<G> actions, String title, E sourceComponent,
      Map<String, Object> context, Dimension dimension, boolean reuseCurrent);

  /**
   * Sets the selected module in the current workspace.
   * 
   * @param module
   *          the module to display to the user.
   */
  void displayModule(Module module);

  /**
   * Sets the selected module in the given workspace.
   * 
   * @param workspaceName
   *          the workspace name for which to display the module.
   * @param module
   *          the module to display to the user.
   */
  void displayModule(String workspaceName, Module module);

  /**
   * Pins a module in the history navigation thus allowing the user to navigate
   * back.
   * 
   * @param module
   *          the module to pin.
   */
  void pinModule(Module module);

  /**
   * Navigates forward in the pinned modules.
   */
  void displayNextPinnedModule();

  /**
   * Navigates backward in the pinned modules.
   */
  void displayPreviousPinnedModule();

  /**
   * Returns the list of workspace names. This list defines the set of
   * workspaces the user has access to.
   * 
   * @return the list of workspace names.
   */
  List<String> getWorkspaceNames();

  /**
   * Given a workspace name, this method returns the associated workspace.
   * 
   * @param workspaceName
   *          the name of the workspace.
   * @return the selected workspace.
   */
  Workspace getWorkspace(String workspaceName);

  /**
   * Gets the selectedWorkspaceName.
   * 
   * @return the selectedWorkspaceName.
   */
  String getSelectedWorkspaceName();

  /**
   * Pops up an information message.
   * 
   * @param sourceComponent
   *          the source component to base the dialog on.
   * @param title
   *          the dialog title.
   * @param iconImageUrl
   *          the icon to use.
   * @param message
   *          the message to display.
   */
  void popupInfo(E sourceComponent, String title, String iconImageUrl,
      String message);

  /**
   * Pops up an OK Cancel message.
   * 
   * @param sourceComponent
   *          the source component to base the dialog on.
   * @param title
   *          the dialog title.
   * @param iconImageUrl
   *          the icon to use.
   * @param message
   *          the message to display.
   * @param okAction
   *          the action to execute on ok.
   * @param cancelAction
   *          the action to execute on cancel.
   * @param context
   *          the context to execute the selected action.
   */
  void popupOkCancel(E sourceComponent, String title, String iconImageUrl,
      String message, IAction okAction, IAction cancelAction,
      Map<String, Object> context);

  /**
   * Pops up an Yes No message.
   * 
   * @param sourceComponent
   *          the source component to base the dialog on.
   * @param title
   *          the dialog title.
   * @param iconImageUrl
   *          the icon to use.
   * @param message
   *          the message to display.
   * @param yesAction
   *          the action to execute on yes.
   * @param noAction
   *          the action to execute on no.
   * @param context
   *          the context to execute the selected action.
   */
  void popupYesNo(E sourceComponent, String title, String iconImageUrl,
      String message, IAction yesAction, IAction noAction,
      Map<String, Object> context);

  /**
   * Pops up an Yes No Cancel message.
   * 
   * @param sourceComponent
   *          the source component to base the dialog on.
   * @param title
   *          the dialog title.
   * @param iconImageUrl
   *          the icon to use.
   * @param message
   *          the message to display.
   * @param yesAction
   *          the action to execute on yes.
   * @param noAction
   *          the action to execute on no.
   * @param cancelAction
   *          the action to execute on cancel.
   * @param context
   *          the context to execute the selected action.
   */
  void popupYesNoCancel(E sourceComponent, String title, String iconImageUrl,
      String message, IAction yesAction, IAction noAction,
      IAction cancelAction, Map<String, Object> context);
}
