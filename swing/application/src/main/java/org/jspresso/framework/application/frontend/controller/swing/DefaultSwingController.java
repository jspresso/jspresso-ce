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
package org.jspresso.framework.application.frontend.controller.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import chrriis.dj.nativeswing.swtimpl.components.FlashPluginOptions;
import chrriis.dj.nativeswing.swtimpl.components.JFlashPlayer;
import chrriis.dj.swingsuite.JComboButton;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.application.ControllerException;
import org.jspresso.framework.application.backend.BackendControllerHolder;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.frontend.controller.AbstractFrontendController;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.gui.swing.components.JErrorDialog;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.gui.EClientType;
import org.jspresso.framework.util.html.HtmlHelper;
import org.jspresso.framework.util.lang.ObjectUtils;
import org.jspresso.framework.util.preferences.IPreferencesStore;
import org.jspresso.framework.util.preferences.JavaPreferencesStore;
import org.jspresso.framework.util.security.LoginUtils;
import org.jspresso.framework.util.swing.BrowserControl;
import org.jspresso.framework.util.swing.SwingUtil;
import org.jspresso.framework.util.swing.WaitCursorEventQueue;
import org.jspresso.framework.util.swing.WaitCursorTimer;
import org.jspresso.framework.util.url.UrlHelper;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.ActionList;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.swing.BasicTransferable;

/**
 * This is is the default implementation of the <b>Swing</b> frontend
 * controller. It will implement a 2-tier architecture that is particularly
 * useful for the development/debugging phases. Workspaces are displayed using
 * an MDI UI using internal frames.
 *
 * @author Vincent Vandenschrick
 */
public class DefaultSwingController extends
    AbstractFrontendController<JComponent, Icon, Action> {

  private JFrame          controllerFrame;
  private JDesktopPane    desktopPane;
  private JLabel          statusBar;
  private WaitCursorTimer waitTimer;

  private Map<String, JInternalFrame> workspaceInternalFrames;
  private JDialog                     loginDialog;

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayFlashObject(String swfUrl, Map<String, String> flashContext, List<Action> actions, String title,
                                 JComponent sourceComponent, Map<String, Object> context, Dimension dimension,
                                 boolean reuseCurrent) {

    JFlashPlayer flashPlayer = new JFlashPlayer();
    FlashPluginOptions options = new FlashPluginOptions();
    options.setVariables(flashContext);
    flashPlayer.load(getClass(), UrlHelper.getResourcePathOrUrl(swfUrl, true), options);

    displayDialog(flashPlayer, actions, title, sourceComponent, context, dimension, reuseCurrent, false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayDialog(final JComponent mainView, final List<Action> actions, final String title,
                            final JComponent sourceComponent, final Map<String, Object> context,
                            final Dimension dimension, final boolean reuseCurrent, final boolean modal) {
    displayModalDialog(mainView, context, reuseCurrent);
    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        JDialog dialog;
        Window window;
        if (sourceComponent != null) {
          window = SwingUtil.getVisibleWindow(sourceComponent);
        } else {
          window = controllerFrame;
        }
        boolean newDialog = true;
        if (window instanceof JDialog) {
          if (reuseCurrent) {
            dialog = (JDialog) window;
            dialog.getContentPane().removeAll();
            newDialog = false;
          } else {
            dialog = new JDialog((JDialog) window, title, modal);
          }
        } else {
          dialog = new JDialog((Frame) window, title, modal);
        }

        Box buttonBox = new Box(BoxLayout.LINE_AXIS);
        buttonBox.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));

        JButton defaultButton = null;
        for (Action action : actions) {
          JButton actionButton = new JButton();
          SwingUtil.configureButton(actionButton);
          actionButton.setAction(action);
          buttonBox.add(actionButton);
          buttonBox.add(Box.createHorizontalStrut(10));
          if (defaultButton == null) {
            defaultButton = actionButton;
          }
        }
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BorderLayout());
        actionPanel.add(buttonBox, BorderLayout.EAST);

        if (dimension != null) {
          mainView.setPreferredSize(new java.awt.Dimension(dimension.getWidth(), dimension.getHeight()));
        }
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(mainView, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);
        dialog.getContentPane().add(mainPanel);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        if (defaultButton != null) {
          dialog.getRootPane().setDefaultButton(defaultButton);
        }
        dialog.pack();
        if (newDialog) {
          SwingUtil.centerInParent(dialog);
        }
        dialog.setVisible(true);
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayUrl(String urlSpec, String target) {
    try {
      BrowserControl.displayURL(urlSpec);
    } catch (IOException ex) {
      throw new ActionException(ex);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void displayWorkspace(String workspaceName, boolean bypassModuleBoundaryActions) {
    if (!ObjectUtils.equals(workspaceName, getSelectedWorkspaceName())) {
      super.displayWorkspace(workspaceName, bypassModuleBoundaryActions);
      if (workspaceName != null) {
        if (workspaceInternalFrames == null) {
          workspaceInternalFrames = new HashMap<>();
        }
        JInternalFrame workspaceInternalFrame = workspaceInternalFrames.get(workspaceName);
        if (workspaceInternalFrame == null) {
          IViewDescriptor workspaceNavigatorViewDescriptor = getWorkspace(workspaceName).getViewDescriptor();
          IValueConnector workspaceConnector = getBackendController().getWorkspaceConnector(workspaceName);
          IView<JComponent> workspaceNavigator = createWorkspaceNavigator(workspaceName,
              workspaceNavigatorViewDescriptor);
          IView<JComponent> moduleAreaView = createModuleAreaView(workspaceName);
          JSplitPane workspaceView = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
          workspaceView.setOneTouchExpandable(true);
          workspaceView.add(workspaceNavigator.getPeer());
          workspaceView.add(moduleAreaView.getPeer());
          workspaceInternalFrame = createJInternalFrame(
              workspaceView,
              workspaceNavigatorViewDescriptor.getI18nName(this, getLocale()),
              getIconFactory().getIcon(
                  workspaceNavigatorViewDescriptor.getIcon(),
                  getIconFactory().getSmallIconSize()));
          workspaceInternalFrame
              .addInternalFrameListener(new WorkspaceInternalFrameListener(
                  workspaceName));
          workspaceInternalFrames.put(workspaceName, workspaceInternalFrame);
          desktopPane.add(workspaceInternalFrame);
          getMvcBinder().bind(workspaceNavigator.getConnector(),
              workspaceConnector);
          workspaceInternalFrame.pack();
          workspaceInternalFrame.setSize(controllerFrame.getWidth() - 50,
              controllerFrame.getHeight() - 50);
          try {
            workspaceInternalFrame.setMaximum(true);
          } catch (PropertyVetoException ex) {
            throw new ControllerException(ex);
          }
        }
        workspaceInternalFrame.setVisible(true);
        if (workspaceInternalFrame.isIcon()) {
          try {
            workspaceInternalFrame.setIcon(false);
          } catch (PropertyVetoException ex) {
            throw new ControllerException(ex);
          }
        }
        workspaceInternalFrame.toFront();
      }
    }
    updateFrameTitle();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean disposeModalDialog(JComponent sourceWidget,
      Map<String, Object> context) {
    if (super.disposeModalDialog(sourceWidget, context)) {
      Window actionWindow = SwingUtil.getVisibleWindow(sourceWidget);
      if (actionWindow instanceof JDialog) {
        actionWindow.dispose();
      }
      transferFocus(context);
      return true;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IAction action, Map<String, Object> context) {
    if (action == null) {
      return true;
    }
    JComponent sourceComponent = (JComponent) context
        .get(ActionContextConstants.SOURCE_COMPONENT);
    Component windowOrInternalFrame = null;
    if (sourceComponent != null) {
      windowOrInternalFrame = SwingUtil
          .getWindowOrInternalFrame(sourceComponent);
    }
    if (windowOrInternalFrame instanceof JFrame) {
      ((JFrame) windowOrInternalFrame).getGlassPane().setVisible(true);
    } else if (windowOrInternalFrame instanceof JInternalFrame) {
      ((JInternalFrame) windowOrInternalFrame).getGlassPane().setVisible(true);
    } else if (windowOrInternalFrame instanceof JDialog) {
      ((JDialog) windowOrInternalFrame).getGlassPane().setVisible(true);
    }
    waitTimer.startTimer(sourceComponent);
    try {
      return super.execute(action, context);
    } finally {
      if (windowOrInternalFrame instanceof JFrame) {
        ((JFrame) windowOrInternalFrame).getGlassPane().setVisible(false);
      } else if (windowOrInternalFrame instanceof JInternalFrame) {
        ((JInternalFrame) windowOrInternalFrame).getGlassPane().setVisible(
            false);
      } else if (windowOrInternalFrame instanceof JDialog) {
        ((JDialog) windowOrInternalFrame).getGlassPane().setVisible(false);
      }
      waitTimer.stopTimer();
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
    String userFriendlyExceptionMessage = computeUserFriendlyExceptionMessage(ex);
    Component sourceComponent = controllerFrame;
    if (userFriendlyExceptionMessage != null) {
      JOptionPane.showMessageDialog(sourceComponent, HtmlHelper
          .toHtml(HtmlHelper.emphasis(HtmlHelper
              .escapeForHTML(userFriendlyExceptionMessage))),
          getTranslation("error", getLocale()), JOptionPane.ERROR_MESSAGE,
          getIconFactory().getErrorIcon(getIconFactory().getLargeIconSize()));
    } else {
      traceUnexpectedException(ex);
      JErrorDialog dialog = JErrorDialog.createInstance(sourceComponent, this,
          getLocale());
      dialog.setMessageIcon(getIconFactory().getErrorIcon(
          getIconFactory().getMediumIconSize()));
      dialog.setTitle(getTranslation("error", getLocale()));
      dialog.setMessage(HtmlHelper.toHtml(HtmlHelper.emphasis(HtmlHelper
          .escapeForHTML(ex.getLocalizedMessage()))));
      dialog.setDetails(ex);
      int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
      dialog.pack();
      dialog.setSize(8 * screenRes, 3 * screenRes);
      SwingUtil.centerOnScreen(dialog);
      dialog.setVisible(true);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void popupInfo(final JComponent sourceComponent, final String title,
      final String iconImageUrl, final String message) {
    // To have the same threading model than the other UI channels
    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        JOptionPane.showMessageDialog(
            SwingUtil.getWindowOrInternalFrame(sourceComponent),
            message,
            title,
            JOptionPane.INFORMATION_MESSAGE,
            getIconFactory().getIcon(iconImageUrl,
                getIconFactory().getLargeIconSize()));
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void popupOkCancel(final JComponent sourceComponent,
      final String title, final String iconImageUrl, final String message,
      final IAction okAction, final IAction cancelAction,
      final Map<String, Object> context) {
    // To have the same threading model than the other UI channels
    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        int selectedOption = JOptionPane.showConfirmDialog(
            SwingUtil.getWindowOrInternalFrame(sourceComponent),
            message,
            title,
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.WARNING_MESSAGE,
            getIconFactory().getIcon(iconImageUrl,
                getIconFactory().getLargeIconSize()));
        IAction nextAction;
        if (selectedOption == JOptionPane.OK_OPTION) {
          nextAction = okAction;
        } else {
          nextAction = cancelAction;
        }
        if (nextAction != null) {
          execute(nextAction, context);
        }
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void popupYesNo(final JComponent sourceComponent, final String title,
      final String iconImageUrl, final String message, final IAction yesAction,
      final IAction noAction, final Map<String, Object> context) {
    // To have the same threading model than the other UI channels
    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        int selectedOption = JOptionPane.showConfirmDialog(
            SwingUtil.getWindowOrInternalFrame(sourceComponent),
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            getIconFactory().getIcon(iconImageUrl,
                getIconFactory().getLargeIconSize()));
        IAction nextAction;
        if (selectedOption == JOptionPane.YES_OPTION) {
          nextAction = yesAction;
        } else {
          nextAction = noAction;
        }
        if (nextAction != null) {
          execute(nextAction, context);
        }
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void popupYesNoCancel(final JComponent sourceComponent,
      final String title, final String iconImageUrl, final String message,
      final IAction yesAction, final IAction noAction,
      final IAction cancelAction, final Map<String, Object> context) {
    // To have the same threading model than the other UI channels
    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        int selectedOption = JOptionPane.showConfirmDialog(
            SwingUtil.getWindowOrInternalFrame(sourceComponent),
            message,
            title,
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            getIconFactory().getIcon(iconImageUrl,
                getIconFactory().getLargeIconSize()));
        IAction nextAction;
        if (selectedOption == JOptionPane.YES_OPTION) {
          nextAction = yesAction;
        } else if (selectedOption == JOptionPane.NO_OPTION) {
          nextAction = noAction;
        } else {
          nextAction = cancelAction;
        }
        if (nextAction != null) {
          execute(nextAction, context);
        }
      }
    });
  }

  /**
   * Creates the initial view from the root view descriptor, then a SFrame
   * containing this view and presents it to the user.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean start(final IBackendController backendController,
      Locale clientLocale, TimeZone clientTimeZone) {
    if (super.start(backendController, clientLocale, clientTimeZone)) {
      waitTimer = new WaitCursorTimer(500);
      waitTimer.setDaemon(true);
      waitTimer.start();
      Toolkit.getDefaultToolkit().getSystemEventQueue()
          .push(new WaitCursorEventQueue(500));
      Toolkit.getDefaultToolkit().setDynamicLayout(true);
      getApplicationSession().setClientType(EClientType.DESKTOP_SWING);
      SwingUtilities.invokeLater(new Runnable() {

        @Override
        public void run() {
          // To register the backend controller in the event dispatch thread
          BackendControllerHolder.setSessionBackendController(backendController);
          initLoginProcess();
        }
      });
      return true;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean stop() {
    if (super.stop()) {
      if (controllerFrame != null) {
        controllerFrame.dispose();
      }
      System.exit(0);
      return true;
    }
    return false;
  }

  private JToolBar createApplicationToolBar() {
    JToolBar applicationToolBar = new JToolBar();
    applicationToolBar.setRollover(true);
    applicationToolBar.setFloatable(false);

    if (getWorkspaceNames() != null && !getWorkspaceNames().isEmpty()) {
      applicationToolBar.add(createComboButton(createWorkspaceActionList()));
    }
    applicationToolBar.addSeparator();
    if (getNavigationActions() != null
        && isAccessGranted(getNavigationActions())) {
      try {
        pushToSecurityContext(getNavigationActions());
        for (ActionList actionList : getNavigationActions()
            .getActionLists(this)) {
          completeApplicationToolBar(applicationToolBar, actionList);
        }
      } finally {
        restoreLastSecurityContextSnapshot();
      }
    }
    if (getActionMap() != null && isAccessGranted(getActionMap())) {
      try {
        pushToSecurityContext(getActionMap());
        for (ActionList actionList : getActionMap().getActionLists(this)) {
          completeApplicationToolBar(applicationToolBar, actionList);
        }
      } finally {
        restoreLastSecurityContextSnapshot();
      }
    }
    applicationToolBar.add(Box.createHorizontalGlue());
    if (getHelpActions() != null && isAccessGranted(getHelpActions())) {
      try {
        pushToSecurityContext(getHelpActions());
        for (ActionList actionList : getHelpActions().getActionLists(this)) {
          completeApplicationToolBar(applicationToolBar, actionList);
        }
      } finally {
        restoreLastSecurityContextSnapshot();
      }
    }
    JButton exitButton = new JButton();
    exitButton.setAction(getViewFactory().getActionFactory().createAction(
        getExitAction(), this, null, getLocale()));
    applicationToolBar.add(exitButton);
    return applicationToolBar;
  }

  private JToolBar createSecondaryApplicationToolBar() {
    JToolBar applicationToolBar = new JToolBar();
    applicationToolBar.setRollover(true);
    applicationToolBar.setFloatable(false);

    if (getSecondaryActionMap() != null) {
      for (ActionList actionList : getSecondaryActionMap().getActionLists(this)) {
        completeApplicationToolBar(applicationToolBar, actionList);
      }
    }
    return applicationToolBar;
  }

  private void completeApplicationToolBar(JToolBar applicationToolBar,
      ActionList actionList) {
    if (isAccessGranted(actionList)) {
      try {
        pushToSecurityContext(actionList);
        if (actionList.isCollapsable()) {
          applicationToolBar.add(createComboButton(actionList));
        } else {
          for (IDisplayableAction da : actionList.getActions()) {
            if (isAccessGranted(da)) {
              JButton b = new JButton();
              b.setAction(getViewFactory().getActionFactory().createAction(da,
                  this, null, getLocale()));
              applicationToolBar.add(b);
            }
          }
        }
        applicationToolBar.addSeparator();
      } finally {
        restoreLastSecurityContextSnapshot();
      }
    }
  }

  private void createControllerFrame() {
    controllerFrame = new JFrame();
    desktopPane = new JDesktopPane();
    controllerFrame.getContentPane().add(desktopPane, BorderLayout.CENTER);

    statusBar = new JLabel();
    statusBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    statusBar.setVisible(false);
    controllerFrame.getContentPane().add(statusBar, BorderLayout.SOUTH);

    controllerFrame
        .setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    controllerFrame.setGlassPane(createHermeticGlassPane());
    controllerFrame.addWindowListener(new WindowAdapter() {

      /**
       * {@inheritDoc}
       */
      @Override
      public void windowClosing(WindowEvent e) {
        execute(getExitAction(), new HashMap<String, Object>());
      }
    });
    controllerFrame.pack();
    int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
    int w = 12 * screenRes;
    int h = 8 * screenRes;
    if (getFrameWidth() != null) {
      w = getFrameWidth();
    }
    if (getFrameHeight() != null) {
      h = getFrameHeight();
    }
    controllerFrame.setSize(w, h);
    // controllerFrame.setSize(1100, 800);
    ImageIcon frameIcon = ((ImageIcon) getIconFactory().getIcon(getIcon(),
        getIconFactory().getSmallIconSize()));
    if (frameIcon != null) {
      controllerFrame.setIconImage(frameIcon.getImage());
    }
    SwingUtil.centerOnScreen(controllerFrame);
    updateFrameTitle();
    controllerFrame.setVisible(true);
  }

  private JComponent createHermeticGlassPane() {
    JPanel glassPane = new JPanel();
    glassPane.setOpaque(false);
    glassPane.addMouseListener(new MouseAdapter() {
      // No-op
    });
    glassPane.addKeyListener(new KeyAdapter() {
      // No-op
    });
    return glassPane;
  }

  /**
   * Creates a new JInternalFrame and populates it with a view.
   *
   * @param view
   *          the view to be set into the internal frame.
   * @return the constructed internal frame.
   */
  private JInternalFrame createJInternalFrame(JComponent view, String title,
      Icon frameIcon) {
    JInternalFrame internalFrame = new JInternalFrame(title);
    internalFrame.setFrameIcon(frameIcon);
    internalFrame.setResizable(true);
    internalFrame.setClosable(true);
    internalFrame.setMaximizable(true);
    internalFrame.setIconifiable(true);
    internalFrame.getContentPane().add(view, BorderLayout.CENTER);
    internalFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    internalFrame.setGlassPane(createHermeticGlassPane());
    return internalFrame;
  }

  private JMenuItem createMenuItem(IDisplayableAction action) {
    return new JMenuItem(getViewFactory().getActionFactory().createAction(
        action, this, null, getLocale()));
  }

  private JButton createComboButton(ActionList actionList) {
    JButton button;
    List<IDisplayableAction> actions = new ArrayList<>();
    for (IDisplayableAction action : actionList.getActions()) {
      if (isAccessGranted(action)) {
        try {
          pushToSecurityContext(action);
          actions.add(action);
        } finally {
          restoreLastSecurityContextSnapshot();
        }
      }
    }

    if (actions.isEmpty()) {
      return null;
    }
    if (actions.size() > 1) {
      button = new JComboButton(true);
    } else {
      button = new JButton();
    }
    Action action = getViewFactory().getActionFactory().createAction(
        actionList.getActions().get(0), this, null, getLocale());
    button.setAction(action);
    if (actions.size() > 1) {
      JPopupMenu popupMenu = new JPopupMenu();
      for (IDisplayableAction menuAction : actions) {
        popupMenu.add(createMenuItem(menuAction));
      }
      ((JComboButton) button).setArrowPopupMenu(popupMenu);
    }
    return button;
  }

  private void initLoginProcess() {
    createControllerFrame();
    if (isLoginInteractive()) {
      IView<JComponent> loginView = createLoginView();

      // Login dialog
      loginDialog = new JDialog(controllerFrame, getLoginViewDescriptor().getI18nName(this, getLocale()), true);
      loginDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

      JPanel buttonBox = new JPanel();
      buttonBox.setLayout(new BoxLayout(buttonBox, BoxLayout.X_AXIS));
      buttonBox.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));

      int i = 0;
      for (ActionList actionList : getLoginViewDescriptor().getActionMap().getActionLists(this)) {
        for (IDisplayableAction action : actionList.getActions()) {
          JButton button = new JButton(getViewFactory().getActionFactory().createAction(action,
              getViewFactory().getIconFactory().getSmallIconSize(), this, loginView, getLocale()));
          buttonBox.add(button);
          if (i == 0) {
            loginDialog.getRootPane().setDefaultButton(button);
          }
          i++;
        }
      }

      JButton exitButton = new JButton();
      exitButton.setAction(getViewFactory().getActionFactory().createAction(getExitAction(), this, null, getLocale()));
      exitButton.setIcon(getIconFactory().getCancelIcon(getIconFactory().getSmallIconSize()));
      buttonBox.add(exitButton);

      JPanel actionPanel = new JPanel(new BorderLayout());
      actionPanel.add(buttonBox, BorderLayout.EAST);

      JPanel mainPanel = new JPanel(new BorderLayout());
      mainPanel.add(new JLabel(getLoginViewDescriptor().getI18nDescription(this, getLocale())),
          BorderLayout.NORTH);
      mainPanel.add(loginView.getPeer(), BorderLayout.CENTER);
      mainPanel.add(actionPanel, BorderLayout.SOUTH);
      loginDialog.add(mainPanel);

      loginDialog.pack();
      SwingUtil.centerInParent(loginDialog);
      loginDialog.setVisible(true);
    } else {
      performLogin();
      updateControllerFrame();
      execute(getStartupAction(), getInitialActionContext());
    }
  }

  /**
   * Login to the application.
   */
  @Override
  public void login() {
    if (performLogin()) {
      loginDialog.dispose();
      updateControllerFrame();
      execute(getStartupAction(), getStartupActionContext());
    } else {
      loginFailed(loginDialog);
    }
  }

  /**
   * Callback after a failed login.
   *
   * @param dialog
   *          the login dialog if any.
   */
  protected void loginFailed(JDialog dialog) {
    if (dialog != null) {
      JOptionPane.showMessageDialog(dialog,
          getTranslation(LoginUtils.LOGIN_FAILED, getLocale()),
          getTranslation("error", getLocale()), JOptionPane.ERROR_MESSAGE);
    }
  }

  private void updateControllerFrame() {
    // controllerFrame.setJMenuBar(createApplicationMenuBar());
    controllerFrame.getContentPane().add(createApplicationToolBar(),
        BorderLayout.NORTH);
    if (getSecondaryActionMap() != null
        && isAccessGranted(getSecondaryActionMap())) {
      try {
        pushToSecurityContext(getSecondaryActionMap());
        controllerFrame.getContentPane().add(
            createSecondaryApplicationToolBar(), BorderLayout.SOUTH);
      } finally {
        restoreLastSecurityContextSnapshot();
      }
    }
    controllerFrame.invalidate();
    controllerFrame.validate();
    updateFrameTitle();
  }

  private void updateFrameTitle() {
    String workspaceName = getSelectedWorkspaceName();
    if (workspaceName != null) {
      controllerFrame.setTitle(getSelectedWorkspace()
          .getViewDescriptor().getI18nDescription(this, getLocale())
          + " - "
          + getI18nName(this, getLocale()));
    } else {
      controllerFrame.setTitle(getI18nName(this, getLocale()));
    }
  }

  private final class WorkspaceInternalFrameListener extends
      InternalFrameAdapter {

    private final String workspaceName;

    /**
     * Constructs a new {@code WorkspaceInternalFrameListener} instance.
     *
     * @param workspaceName
     *          the workspace identifier this listener is attached to.
     */
    public WorkspaceInternalFrameListener(String workspaceName) {
      this.workspaceName = workspaceName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void internalFrameActivated(InternalFrameEvent e) {
      displayWorkspace(workspaceName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
      displayWorkspace(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
      displayWorkspace(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {
      // displayWorkspace(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {
      displayWorkspace(workspaceName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void internalFrameIconified(InternalFrameEvent e) {
      // displayWorkspace(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void internalFrameOpened(InternalFrameEvent e) {
      displayWorkspace(workspaceName);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setStatusInfo(String statusInfo) {
    if (statusInfo != null && statusInfo.length() > 0) {
      statusBar.setText(statusInfo);
      statusBar.setVisible(true);
    } else {
      statusBar.setVisible(false);
    }
  }

  /**
   * Returns a preference store based pon Java preferences API.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected IPreferencesStore createClientPreferencesStore() {
    return new JavaPreferencesStore();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setClipboardContent(String plainContent, String htmlContent) {
    Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
    BasicTransferable dataTransferObject = new BasicTransferable(plainContent,
        htmlContent);
    cb.setContents(dataTransferObject, dataTransferObject);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setName(String name) {
    super.setName(name);
    if (isStarted()) {
      updateFrameTitle();
    }
  }
}
