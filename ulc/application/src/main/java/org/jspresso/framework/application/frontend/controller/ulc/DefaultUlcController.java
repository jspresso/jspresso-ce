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
package org.jspresso.framework.application.frontend.controller.ulc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.frontend.controller.AbstractFrontendController;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.gui.ulc.components.server.ULCErrorDialog;
import org.jspresso.framework.gui.ulc.components.server.ULCExtendedButton;
import org.jspresso.framework.gui.ulc.components.server.ULCExtendedInternalFrame;
import org.jspresso.framework.gui.ulc.components.server.event.ExtendedInternalFrameEvent;
import org.jspresso.framework.gui.ulc.components.server.event.IExtendedInternalFrameListener;
import org.jspresso.framework.security.ulc.DialogCallbackHandler;
import org.jspresso.framework.security.ulc.ICallbackHandlerListener;
import org.jspresso.framework.util.exception.BusinessException;
import org.jspresso.framework.util.html.HtmlHelper;
import org.jspresso.framework.util.lang.ObjectUtils;
import org.jspresso.framework.util.ulc.UlcUtil;
import org.jspresso.framework.view.IActionFactory;
import org.jspresso.framework.view.IIconFactory;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.ActionList;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.springframework.dao.ConcurrencyFailureException;

import com.ulcjava.base.application.ApplicationContext;
import com.ulcjava.base.application.ClientContext;
import com.ulcjava.base.application.IAction;
import com.ulcjava.base.application.ULCAlert;
import com.ulcjava.base.application.ULCBorderLayoutPane;
import com.ulcjava.base.application.ULCBoxLayoutPane;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.ULCDesktopPane;
import com.ulcjava.base.application.ULCDialog;
import com.ulcjava.base.application.ULCFiller;
import com.ulcjava.base.application.ULCFrame;
import com.ulcjava.base.application.ULCMenu;
import com.ulcjava.base.application.ULCMenuBar;
import com.ulcjava.base.application.ULCMenuItem;
import com.ulcjava.base.application.ULCPollingTimer;
import com.ulcjava.base.application.ULCWindow;
import com.ulcjava.base.application.border.ULCEmptyBorder;
import com.ulcjava.base.application.event.ActionEvent;
import com.ulcjava.base.application.event.WindowEvent;
import com.ulcjava.base.application.event.serializable.IActionListener;
import com.ulcjava.base.application.event.serializable.IWindowListener;
import com.ulcjava.base.application.util.Insets;
import com.ulcjava.base.application.util.ULCIcon;
import com.ulcjava.base.shared.IWindowConstants;

/**
 * Default implementation of a swing frontend controller. This implementation is
 * usable "as-is".
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
 */
public class DefaultUlcController extends
    AbstractFrontendController<ULCComponent, ULCIcon, IAction> implements
    ICallbackHandlerListener {

  private ULCFrame                              controllerFrame;
  private Callback[]                            loginCallbacks;
  private boolean                               loginComplete;
  private int                                   loginRetries;
  private boolean                               loginSuccessful;
  private ULCPollingTimer                       loginTimer;
  private Map<String, ULCExtendedInternalFrame> workspaceInternalFrames;

  /**
   * {@inheritDoc}
   */
  public void callbackHandlingComplete() {
    notifyWaiters();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayModalDialog(ULCComponent mainView, List<IAction> actions,
      String title, ULCComponent sourceComponent, Map<String, Object> context,
      boolean reuseCurrent) {
    super.displayModalDialog(mainView, actions, title, sourceComponent,
        context, reuseCurrent);
    final ULCDialog dialog;
    ULCWindow window = UlcUtil.getVisibleWindow(sourceComponent);
    if (reuseCurrent && window instanceof ULCDialog) {
        dialog = (ULCDialog) window;
        dialog.getContentPane().removeAll();
    } else {
      dialog = new ULCDialog(window, title, true);
    }

    ULCBoxLayoutPane buttonBox = new ULCBoxLayoutPane(
        ULCBoxLayoutPane.LINE_AXIS);
    buttonBox.setBorder(new ULCEmptyBorder(new Insets(5, 10, 5, 10)));

    ULCExtendedButton defaultButton = null;
    for (IAction action : actions) {
      ULCExtendedButton actionButton = new ULCExtendedButton();
      actionButton.setAction(action);
      buttonBox.add(actionButton);
      buttonBox.add(ULCFiller.createHorizontalStrut(10));
      if (defaultButton == null) {
        defaultButton = actionButton;
      }
    }
    ULCBorderLayoutPane actionPanel = new ULCBorderLayoutPane();
    actionPanel.add(buttonBox, ULCBorderLayoutPane.EAST);

    ULCBorderLayoutPane mainPanel = new ULCBorderLayoutPane();
    mainPanel.add(mainView, ULCBorderLayoutPane.CENTER);
    mainPanel.add(actionPanel, ULCBorderLayoutPane.SOUTH);
    dialog.getContentPane().add(mainPanel);
    dialog.setDefaultCloseOperation(IWindowConstants.DO_NOTHING_ON_CLOSE);
    if (defaultButton != null) {
      dialog.getRootPane().setDefaultButton(defaultButton);
    }
    dialog.pack();
    dialog.setVisible(true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayUrl(String urlSpec) {
    ClientContext.showDocument(urlSpec);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayWorkspace(String workspaceName) {
    if (!ObjectUtils.equals(workspaceName, getSelectedWorkspaceName())) {
      super.displayWorkspace(workspaceName);
      if (workspaceInternalFrames == null) {
        workspaceInternalFrames = new HashMap<String, ULCExtendedInternalFrame>();
      }
      ULCExtendedInternalFrame workspaceInternalFrame = workspaceInternalFrames
          .get(workspaceName);
      if (workspaceInternalFrame == null) {
        IViewDescriptor workspaceViewDescriptor = getWorkspace(workspaceName)
            .getViewDescriptor();
        IValueConnector workspaceConnector = getBackendController()
            .getWorkspaceConnector(workspaceName);
        IView<ULCComponent> workspaceView = createWorkspaceView(workspaceName,
            workspaceViewDescriptor, (Workspace) workspaceConnector
                .getConnectorValue());
        workspaceInternalFrame = createULCExtendedInternalFrame(workspaceView);
        workspaceInternalFrame
            .addExtendedInternalFrameListener(new WorkspaceInternalFrameListener(
                workspaceName));
        workspaceInternalFrames.put(workspaceName, workspaceInternalFrame);
        controllerFrame.getContentPane().add(workspaceInternalFrame);
        getMvcBinder().bind(workspaceView.getConnector(), workspaceConnector);
        workspaceInternalFrame.pack();
        workspaceInternalFrame.setSize(controllerFrame.getWidth() - 50,
            controllerFrame.getHeight() - 50);
        workspaceInternalFrame.setMaximum(true);
      }
      workspaceInternalFrame.setVisible(true);
      if (workspaceInternalFrame.isIcon()) {
        workspaceInternalFrame.setIcon(false);
      }
      workspaceInternalFrame.moveToFront();
      updateFrameTitle();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void disposeModalDialog(ULCComponent sourceWidget,
      Map<String, Object> context) {
    super.disposeModalDialog(sourceWidget, context);
    ULCWindow actionWindow = UlcUtil.getVisibleWindow(sourceWidget);
    if (actionWindow instanceof ULCDialog) {
      actionWindow.setVisible(false);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean handleException(Throwable ex, Map<String, Object> context) {
    if (super.handleException(ex, context)) {
      return true;
    }
    ULCComponent sourceComponent = controllerFrame;
    if (ex instanceof SecurityException) {
      ULCAlert alert = new ULCAlert(UlcUtil.getVisibleWindow(sourceComponent),
          getTranslationProvider().getTranslation("error", getLocale()),
          HtmlHelper.emphasis(ex.getMessage()), getTranslationProvider()
              .getTranslation("ok", getLocale()), null, null, getIconFactory()
              .getErrorIcon(IIconFactory.LARGE_ICON_SIZE));
      alert.show();
    } else if (ex instanceof BusinessException) {
      ULCAlert alert = new ULCAlert(UlcUtil.getVisibleWindow(sourceComponent),
          getTranslationProvider().getTranslation("error", getLocale()),
          HtmlHelper.emphasis(((BusinessException) ex).getI18nMessage(
              getTranslationProvider(), getLocale())), getTranslationProvider()
              .getTranslation("ok", getLocale()), null, null, getIconFactory()
              .getErrorIcon(IIconFactory.LARGE_ICON_SIZE));
      alert.show();
    } else if (ex instanceof ConcurrencyFailureException) {
      ULCAlert alert = new ULCAlert(UlcUtil.getVisibleWindow(sourceComponent),
          getTranslationProvider().getTranslation("error", getLocale()),
          HtmlHelper.emphasis(getTranslationProvider().getTranslation(
              "concurrency.error.description", getLocale())),
          getTranslationProvider().getTranslation("ok", getLocale()), null,
          null, getIconFactory().getErrorIcon(IIconFactory.LARGE_ICON_SIZE));
      alert.show();
    } else {
      ex.printStackTrace();
      ULCErrorDialog dialog = ULCErrorDialog.createInstance(sourceComponent,
          getTranslationProvider(), getLocale());
      dialog.setMessageIcon(getIconFactory().getErrorIcon(
          IIconFactory.MEDIUM_ICON_SIZE));
      dialog.setTitle(getTranslationProvider().getTranslation("error",
          getLocale()));
      dialog.setMessage(HtmlHelper.emphasis(ex.getLocalizedMessage()));
      dialog.setDetails(ex);
      int screenRes = ClientContext.getScreenResolution();
      dialog.setSize(8 * screenRes, 3 * screenRes);
      dialog.pack();
      UlcUtil.centerOnScreen(dialog);
      dialog.setVisible(true);
    }
    return true;
  }

  /**
   * Creates the initial view from the root view descriptor, then a JFrame
   * containing this view and presents it to the user.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean start(IBackendController backendController, Locale clientLocale) {
    if (super.start(backendController, clientLocale)) {
      loginRetries = 0;
      loginSuccessful = false;
      loginComplete = false;
      CallbackHandler callbackHandler = getLoginCallbackHandler();
      if (callbackHandler instanceof DialogCallbackHandler) {
        ((DialogCallbackHandler) callbackHandler)
            .setParentComponent(controllerFrame);
        ((DialogCallbackHandler) callbackHandler)
            .setCallbackHandlerListener(this);
      }
      performLogin();
      return true;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean stop() {
    if (controllerFrame != null) {
      controllerFrame.setVisible(false);
    }
    ApplicationContext.terminate();
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected CallbackHandler createLoginCallbackHandler() {
    DialogCallbackHandler callbackHandler = new DialogCallbackHandler();
    callbackHandler.setLocale(getLocale());
    callbackHandler.setTranslationProvider(getTranslationProvider());
    callbackHandler.setIconFactory(getIconFactory());
    return callbackHandler;
  }

  private List<ULCMenu> createActionMenus(ULCComponent sourceComponent) {
    return createMenus(sourceComponent, getActionMap(), false);
  }

  private ULCMenuBar createApplicationMenuBar(ULCComponent sourceComponent) {
    ULCMenuBar applicationMenuBar = new ULCMenuBar();
    List<ULCMenu> workspaceMenus = createWorkspacesMenus(sourceComponent);
    if (workspaceMenus != null) {
      for (ULCMenu workspaceMenu : workspaceMenus) {
        applicationMenuBar.add(workspaceMenu);
      }
    }
    List<ULCMenu> actionMenus = createActionMenus(sourceComponent);
    if (actionMenus != null) {
      for (ULCMenu actionMenu : actionMenus) {
        applicationMenuBar.add(actionMenu);
      }
    }
    applicationMenuBar.add(ULCFiller.createHorizontalGlue());
    List<ULCMenu> helpActionMenus = createHelpActionMenus(sourceComponent);
    if (helpActionMenus != null) {
      for (ULCMenu helpActionMenu : helpActionMenus) {
        applicationMenuBar.add(helpActionMenu);
      }
    }
    return applicationMenuBar;
  }

  private ULCFrame createControllerFrame() {
    ULCFrame frame = new ULCFrame();
    frame.setContentPane(new ULCDesktopPane());
    frame.setIconImage(getIconFactory().getIcon(getIconImageURL(),
        IIconFactory.SMALL_ICON_SIZE));
    frame.setDefaultCloseOperation(IWindowConstants.DO_NOTHING_ON_CLOSE);
    frame.setMenuBar(createApplicationMenuBar(frame));
    frame.addWindowListener(new IWindowListener() {

      private static final long serialVersionUID = -7845554617417316256L;

      public void windowClosing(@SuppressWarnings("unused") WindowEvent event) {
        stop();
      }
    });
    return frame;
  }

  private List<ULCMenu> createHelpActionMenus(ULCComponent sourceComponent) {
    return createMenus(sourceComponent, getHelpActions(), true);
  }

  private ULCMenu createMenu(ActionList actionList, ULCComponent sourceComponent) {
    ULCMenu menu = new ULCMenu(actionList.getI18nName(getTranslationProvider(),
        getLocale()));
    if (actionList.getDescription() != null) {
      menu.setToolTipText(actionList.getI18nDescription(
          getTranslationProvider(), getLocale())
          + IActionFactory.TOOLTIP_ELLIPSIS);
    }
    menu.setIcon(getIconFactory().getIcon(actionList.getIconImageURL(),
        IIconFactory.SMALL_ICON_SIZE));
    for (ULCMenuItem menuItem : createMenuItems(sourceComponent, actionList)) {
      menu.add(menuItem);
    }
    return menu;
  }

  private ULCMenuItem createMenuItem(ULCComponent sourceComponent,
      IDisplayableAction action) {
    return new ULCMenuItem(getViewFactory().getActionFactory().createAction(
        action, this, sourceComponent, null, null, getLocale()));
  }

  private List<ULCMenuItem> createMenuItems(ULCComponent sourceComponent,
      ActionList actionList) {
    List<ULCMenuItem> menuItems = new ArrayList<ULCMenuItem>();
    for (IDisplayableAction action : actionList.getActions()) {
      menuItems.add(createMenuItem(sourceComponent, action));
    }
    return menuItems;
  }

  @SuppressWarnings("null")
  private List<ULCMenu> createMenus(ULCComponent sourceComponent,
      ActionMap actionMap, boolean useSeparator) {
    List<ULCMenu> menus = new ArrayList<ULCMenu>();
    if (actionMap != null) {
      ULCMenu menu = null;
      for (ActionList actionList : actionMap.getActionLists()) {
        if (!useSeparator || menus.isEmpty()) {
          menu = createMenu(actionList, sourceComponent);
          menus.add(menu);
        } else {
          menu.addSeparator();
          for (ULCMenuItem menuItem : createMenuItems(sourceComponent,
              actionList)) {
            menu.add(menuItem);
          }
        }
      }
    }
    return menus;
  }

  /**
   * Creates a new ULCExtendedInternalFrame and populates it with a view.
   * 
   * @param view
   *          the view to be set into the internal frame.
   * @return the constructed internal frame.
   */
  private ULCExtendedInternalFrame createULCExtendedInternalFrame(
      IView<ULCComponent> view) {
    ULCExtendedInternalFrame internalFrame = new ULCExtendedInternalFrame(view
        .getDescriptor().getI18nName(getTranslationProvider(), getLocale()));
    internalFrame.setFrameIcon(getIconFactory().getIcon(
        view.getDescriptor().getIconImageURL(), IIconFactory.SMALL_ICON_SIZE));
    internalFrame.setResizable(true);
    internalFrame.setClosable(false);
    internalFrame.setMaximizable(true);
    internalFrame.setIconifiable(true);
    internalFrame.getContentPane().add(view.getPeer());
    internalFrame.setDefaultCloseOperation(IWindowConstants.HIDE_ON_CLOSE);
    return internalFrame;
  }

  private List<ULCMenu> createWorkspacesMenus(ULCComponent sourceComponent) {
    return createMenus(sourceComponent, createWorkspaceActionMap(), true);
  }

  private void displayControllerFrame() {
    controllerFrame = createControllerFrame();
    controllerFrame.pack();
    int screenRes = ClientContext.getScreenResolution();
    controllerFrame.setSize(12 * screenRes, 8 * screenRes);
    UlcUtil.centerOnScreen(controllerFrame);
    updateFrameTitle();
    controllerFrame.setVisible(true);
  }

  private synchronized void notifyWaiters() {
    notifyAll();
  }

  private void performLogin() {
    if (getLoginContextName() != null) {
      new LoginThread().start();
      loginTimer = new ULCPollingTimer(2000, new IActionListener() {

        private static final long serialVersionUID = 5630061795918376362L;

        public void actionPerformed(
            @SuppressWarnings("unused") ActionEvent event) {
          if (loginCallbacks != null) {
            Callback[] loginCallbacksCopy = loginCallbacks;
            loginCallbacks = null;
            try {
              getLoginCallbackHandler().handle(loginCallbacksCopy);
            } catch (IOException ex) {
              // NO-OP
            } catch (UnsupportedCallbackException ex) {
              // NO-OP
            }
          }
          if (loginComplete) {
            loginTimer.stop();
            loginTimer = null;
            ClientContext.sendMessage("appStarted");
            if (loginSuccessful) {
              displayControllerFrame();
              execute(getStartupAction(), getInitialActionContext());
            } else {
              stop();
            }
          }
        }
      });
      loginTimer.setInitialDelay(100);
      loginTimer.start();
    } else {
      loginSuccess(getAnonymousSubject());
      ClientContext.sendMessage("appStarted");
      displayControllerFrame();
      execute(getStartupAction(), getInitialActionContext());
    }
  }

  private void updateFrameTitle() {
    String workspaceName = getSelectedWorkspaceName();
    if (workspaceName != null) {
      controllerFrame.setTitle(getWorkspace(getSelectedWorkspaceName())
          .getViewDescriptor().getI18nDescription(getTranslationProvider(),
              getLocale())
          + " - " + getI18nName(getTranslationProvider(), getLocale()));
    } else {
      controllerFrame.setTitle(getI18nName(getTranslationProvider(),
          getLocale()));
    }
  }

  private synchronized void waitForNotification() {
    try {
      wait();
    } catch (InterruptedException ex) {
      // NO-OP.
    }
  }

  private class LoginThread extends Thread {

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
      if (getLoginContextName() != null) {
        while (!loginSuccessful && loginRetries < MAX_LOGIN_RETRIES) {
          LoginContext lc = null;
          try {
            lc = new LoginContext(getLoginContextName(),
                new ThreadBlockingCallbackHandler());
          } catch (LoginException le) {
            System.err
                .println("Cannot create LoginContext. " + le.getMessage());
          } catch (SecurityException se) {
            System.err
                .println("Cannot create LoginContext. " + se.getMessage());
          }
          if (lc != null) {
            try {
              lc.login();
              loginSuccess(lc.getSubject());
              loginSuccessful = true;
            } catch (LoginException le) {
              loginRetries++;
              System.err.println("Authentication failed:");
              System.err.println("  " + le.getMessage());
            }
          }
        }
      } else {
        loginSuccess(getAnonymousSubject());
      }
      loginComplete = true;
    }
  }

  private class ThreadBlockingCallbackHandler implements CallbackHandler {

    /**
     * {@inheritDoc}
     */
    public void handle(Callback[] callbacks) {
      loginCallbacks = callbacks;
      waitForNotification();
    }
  }

  private final class WorkspaceInternalFrameListener implements
      IExtendedInternalFrameListener {

    private String workspaceName;

    /**
     * Constructs a new <code>WorkspaceInternalFrameListener</code> instance.
     * 
     * @param workspaceName
     *          the root workspace identifier this listener is attached to.
     */
    public WorkspaceInternalFrameListener(String workspaceName) {
      this.workspaceName = workspaceName;
    }

    /**
     * {@inheritDoc}
     */
    public void internalFrameActivated(
        @SuppressWarnings("unused") ExtendedInternalFrameEvent e) {
      displayWorkspace(workspaceName);
    }

    /**
     * {@inheritDoc}
     */
    public void internalFrameDeactivated(
        @SuppressWarnings("unused") ExtendedInternalFrameEvent e) {
      // displayWorkspace(null);
    }

    /**
     * {@inheritDoc}
     */
    public void internalFrameDeiconified(
        @SuppressWarnings("unused") ExtendedInternalFrameEvent event) {
      displayWorkspace(workspaceName);
    }

    /**
     * {@inheritDoc}
     */
    public void internalFrameIconified(
        @SuppressWarnings("unused") ExtendedInternalFrameEvent event) {
      // displayWorkspace(null);
    }

    /**
     * {@inheritDoc}
     */
    public void internalFrameOpened(
        @SuppressWarnings("unused") ExtendedInternalFrameEvent event) {
      displayWorkspace(workspaceName);
    }

  }
}
