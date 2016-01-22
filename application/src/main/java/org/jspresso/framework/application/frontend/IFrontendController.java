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
package org.jspresso.framework.application.frontend;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.application.IController;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.binding.IMvcBinder;
import org.jspresso.framework.util.descriptor.IIconDescriptor;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.IViewFactory;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.action.IActionable;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * General contract of frontend (view) application controllers.
 *
 * @param <E>
 *     the actual gui component type used.
 * @param <F>
 *     the actual icon type used.
 * @param <G>
 *     the actual action type used.
 * @author Vincent Vandenschrick
 */
public interface IFrontendController<E, F, G> extends IController, IIconDescriptor, IActionable {


  /**
   * Displays a flash object on the client in a modal dialog.
   *
   * @param swfUrl
   *     the URL of the swf to load.
   * @param flashContext
   *     the flash context from which the flashVars is computed.
   * @param actions
   *     the actions available in the dialog.
   * @param title
   *     the dialog title.
   * @param sourceComponent
   *     the source component.
   * @param context
   *     the context to store on the context stack.
   * @param dimension
   *     the dimension to set the dialog to. If null, the dialog will be sized to the preferred
   *     size of the contained view.
   * @param reuseCurrent
   *     set to true to reuse an existing modal dialog.
   */
  void displayFlashObject(String swfUrl, Map<String, String> flashContext, List<G> actions, String title,
                          E sourceComponent, Map<String, Object> context, Dimension dimension, boolean reuseCurrent);

  /**
   * Displays a modal dialog.
   *
   * @param mainView
   *     the view to install in the modal dialog.
   * @param actions
   *     the actions available in the dialog.
   * @param title
   *     the dialog title.
   * @param sourceComponent
   *     the source component.
   * @param context
   *     the context to store on the context stack.
   * @param dimension
   *     the dimension to set the dialog to. If null, the dialog will be sized to the preferred
   *     size of the contained view.
   * @param reuseCurrent
   *     set to true to reuse an existing modal dialog.
   */
  void displayModalDialog(E mainView, List<G> actions, String title, E sourceComponent, Map<String, Object> context,
                          Dimension dimension, boolean reuseCurrent);

  /**
   * Displays a modal dialog.
   *
   * @param mainView
   *     the view to install in the modal dialog.
   * @param actions
   *     the actions available in the dialog.
   * @param title
   *     the dialog title.
   * @param sourceComponent
   *     the source component.
   * @param context
   *     the context to store on the context stack.
   * @param dimension
   *     the dimension to set the dialog to. If null, the dialog will be sized to the preferred
   *     size of the contained view.
   * @param reuseCurrent
   *     set to true to reuse an existing modal dialog.
   * @param modal
   *     whether the dialog is modal.
   */
  void displayDialog(E mainView, List<G> actions, String title, E sourceComponent, Map<String, Object> context,
                     Dimension dimension, boolean reuseCurrent, boolean modal);

  /**
   * Sets the selected module in the current workspace.
   *
   * @param module
   *     the module to display to the user.
   */
  void displayModule(Module module);

  /**
   * Sets the selected module in the given workspace.
   *
   * @param workspaceName
   *     the workspace name for which to display the module.
   * @param module
   *     the module to display to the user.
   */
  void displayModule(String workspaceName, Module module);

  /**
   * Navigates forward in the pinned modules.
   */
  void displayNextPinnedModule();

  /**
   * Navigates backward in the pinned modules.
   */
  void displayPreviousPinnedModule();

  /**
   * Displays the given URL in a new browser window (or tab).
   *
   * @param urlSpec
   *     the url to display.
   */
  void displayUrl(String urlSpec);

  /**
   * Displays the given URL in a new browser window (or tab).
   *
   * @param urlSpec
   *     the url to display.
   * @param target
   *     the target window.
   */
  void displayUrl(String urlSpec, String target);

  /**
   * Displays a workspace.
   *
   * @param workspaceName
   *     the workspace identifier.
   */
  void displayWorkspace(String workspaceName);

  /**
   * Disposes a modal dialog.
   *
   * @param sourceWidget
   *     the source widget.
   * @param context
   *     the context to complement with the head of the context stack.
   * @return true if the modal dialog could actually be disposed.
   */
  boolean disposeModalDialog(E sourceWidget, Map<String, Object> context);

  /**
   * Gets the peer model controller.
   *
   * @return the backend controller this frontend controller is attached to.
   */
  IBackendController getBackendController();

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
   * Gets the selectedWorkspaceName.
   *
   * @return the selectedWorkspaceName.
   */
  String getSelectedWorkspaceName();

  /**
   * Gets the selected workspace.
   *
   * @return the selected workspace.
   */
  Workspace getSelectedWorkspace();

  /**
   * Gets the selected module.
   *
   * @return the selected module.
   */
  Module getSelectedModule();

  /**
   * Gets the action that is executed just after the login process has ended but
   * before the UI is actually constructed.
   *
   * @return the action which is executed when the controller is started.
   */
  IAction getLoginAction();

  /**
   * Gets the action that is executed when the controller is started, after the
   * UI initialization is finished based on the logged-in user.
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
   * Given a workspace name, this method returns the associated workspace. If
   * the access is not granted, then null is returned.
   *
   * @param workspaceName
   *     the name of the workspace.
   * @return the selected workspace.
   */
  Workspace getWorkspace(String workspaceName);

  /**
   * Given a workspace name, this method returns the associated workspace. If
   * the access is not granted, then null is returned.
   *
   * @param workspaceName
   *     the name of the workspace.
   * @param bypassSecurity
   *     bypasses security restrictions imposed to the user.
   * @return the selected workspace.
   */
  Workspace getWorkspace(String workspaceName, boolean bypassSecurity);

  /**
   * Returns the list of workspace names. This list defines the set of
   * workspaces the user has access to.
   *
   * @return the list of workspace names.
   */
  List<String> getWorkspaceNames();

  /**
   * Returns the list of workspace names. This list defines the set of
   * workspaces the user has access to.
   *
   * @param bypassSecurity
   *     bypasses security restrictions imposed to the user.
   * @return the list of workspace names.
   */
  List<String> getWorkspaceNames(boolean bypassSecurity);

  /**
   * Pins a module in the history navigation thus allowing the user to navigate
   * back.
   *
   * @param module
   *     the module to pin.
   */
  void pinModule(Module module);

  /**
   * Pops up an information message.
   *
   * @param sourceComponent
   *     the source component to base the dialog on.
   * @param title
   *     the dialog title.
   * @param iconImageUrl
   *     the icon to use.
   * @param message
   *     the message to display.
   */
  void popupInfo(E sourceComponent, String title, String iconImageUrl, String message);

  /**
   * Pops up an OK Cancel message.
   *
   * @param sourceComponent
   *     the source component to base the dialog on.
   * @param title
   *     the dialog title.
   * @param iconImageUrl
   *     the icon to use.
   * @param message
   *     the message to display.
   * @param okAction
   *     the action to execute on ok.
   * @param cancelAction
   *     the action to execute on cancel.
   * @param context
   *     the context to execute the selected action.
   */
  void popupOkCancel(E sourceComponent, String title, String iconImageUrl, String message, IAction okAction,
                     IAction cancelAction, Map<String, Object> context);

  /**
   * Pops up an Yes No message.
   *
   * @param sourceComponent
   *     the source component to base the dialog on.
   * @param title
   *     the dialog title.
   * @param iconImageUrl
   *     the icon to use.
   * @param message
   *     the message to display.
   * @param yesAction
   *     the action to execute on yes.
   * @param noAction
   *     the action to execute on no.
   * @param context
   *     the context to execute the selected action.
   */
  void popupYesNo(E sourceComponent, String title, String iconImageUrl, String message, IAction yesAction,
                  IAction noAction, Map<String, Object> context);

  /**
   * Pops up an Yes No Cancel message.
   *
   * @param sourceComponent
   *     the source component to base the dialog on.
   * @param title
   *     the dialog title.
   * @param iconImageUrl
   *     the icon to use.
   * @param message
   *     the message to display.
   * @param yesAction
   *     the action to execute on yes.
   * @param noAction
   *     the action to execute on no.
   * @param cancelAction
   *     the action to execute on cancel.
   * @param context
   *     the context to execute the selected action.
   */
  void popupYesNoCancel(E sourceComponent, String title, String iconImageUrl, String message, IAction yesAction,
                        IAction noAction, IAction cancelAction, Map<String, Object> context);

  /**
   * Starts the controller. This method performs any necessary initializations
   * (such as binding to the backend controller) and shows the initial view to
   * the user. The initial view is generally built from the root view
   * descriptor.
   *
   * @param backendController
   *     the backend controller to bind to.
   * @param clientLocale
   *     the locale this controller should use to initiate the login session while not
   *     knowing yet the user locale.
   * @param clientTimeZone
   *     the timeZone the client runs in.
   * @return true if the controller successfully started.
   */
  boolean start(IBackendController backendController, Locale clientLocale, TimeZone clientTimeZone);

  /**
   * Gets whether this controller has been started and not stopped.
   *
   * @return whether this controller has been started and not stopped.
   */
  boolean isStarted();

  /**
   * Gets the application navigation action map.
   *
   * @return the application navigation action map or null if none.
   */
  ActionMap getNavigationActions();

  /**
   * Gets the action used to exit the application.
   *
   * @return the action used to exit the application.
   */
  IDisplayableAction getExitAction();

  /**
   * Updates the status information that is displayed in the main application
   * frame.
   *
   * @param statusInfo
   *     the status info that is displayed in the application frame.
   */
  void setStatusInfo(String statusInfo);

  /**
   * Retrieves the currently displayed module view.
   *
   * @return the currently displayed module view or null.
   */
  IView<E> getCurrentModuleView();

  /**
   * Remember login.
   *
   * @param username
   *     the username
   * @param password
   *     the password
   */
  void rememberLogin(String username, String password);

  /**
   * Gets remembered login.
   *
   * @return the remembered login
   */
  String getRememberedLogin();

  /**
   * Reads a client preference.
   *
   * @param key
   *     the key under which the preference as been stored.
   * @return the stored preference or null.
   */
  String getClientPreference(String key);

  /**
   * Stores a client preference.
   *
   * @param key
   *     the key under which the preference as to be stored.
   * @param value
   *     the value of the preference to be stored.
   */
  void putClientPreference(String key, String value);

  /**
   * Deletes a client preference.
   *
   * @param key
   *     the key under which the preference is stored.
   */
  void removeClientPreference(String key);

  /**
   * Assigns system clipboard content.
   *
   * @param plainContent
   *     the plain text content to assign to system clipboard.
   * @param htmlContent
   *     the html text content to assign to system clipboard.
   */
  void setClipboardContent(String plainContent, String htmlContent);

  /**
   * Request focus for a component.
   *
   * @param component
   *     the component to focus.
   */
  void focus(E component);

  /**
   * Request editing for a component.
   *
   * @param component
   *     the component to turn to editing mode.
   */
  void edit(E component);

  /**
   * Traces unexpected exceptions properly.
   *
   * @param ex
   *     the exception to trace.
   */
  void traceUnexpectedException(Throwable ex);

  /**
   * Triggers login to the application.
   */
  void login();

  /**
   * Triggers login to the application anonymously.
   */
  void loginAnonymously();

  /**
   * Sets name.
   *
   * @param name
   *     the name
   */
  void setName(String name);
}
