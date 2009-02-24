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
package org.jspresso.framework.application.frontend.controller.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.swing.Action;
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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.application.ControllerException;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.frontend.controller.AbstractFrontendController;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.gui.swing.components.JErrorDialog;
import org.jspresso.framework.security.swing.DialogCallbackHandler;
import org.jspresso.framework.util.exception.BusinessException;
import org.jspresso.framework.util.html.HtmlHelper;
import org.jspresso.framework.util.lang.ObjectUtils;
import org.jspresso.framework.util.swing.BrowserControl;
import org.jspresso.framework.util.swing.SwingUtil;
import org.jspresso.framework.util.swing.WaitCursorEventQueue;
import org.jspresso.framework.util.swing.WaitCursorTimer;
import org.jspresso.framework.view.IActionFactory;
import org.jspresso.framework.view.IIconFactory;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.ActionList;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.springframework.dao.ConcurrencyFailureException;

import foxtrot.Job;

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
public class DefaultSwingController extends
    AbstractFrontendController<JComponent, Icon, Action> {

  private JFrame                      controllerFrame;
  private WaitCursorTimer             waitTimer;

  private Map<String, JInternalFrame> workspaceInternalFrames;

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
    Component sourceComponent = controllerFrame;
    if (ex instanceof SecurityException) {
      JOptionPane.showMessageDialog(sourceComponent, HtmlHelper.emphasis(ex
          .getMessage()), getTranslationProvider().getTranslation("error",
          getLocale()), JOptionPane.ERROR_MESSAGE, getIconFactory()
          .getErrorIcon(IIconFactory.LARGE_ICON_SIZE));
    } else if (ex instanceof BusinessException) {
      JOptionPane.showMessageDialog(sourceComponent, HtmlHelper
          .emphasis(((BusinessException) ex).getI18nMessage(
              getTranslationProvider(), getLocale())), getTranslationProvider()
          .getTranslation("error", getLocale()), JOptionPane.ERROR_MESSAGE,
          getIconFactory().getErrorIcon(IIconFactory.LARGE_ICON_SIZE));
    } else if (ex instanceof ConcurrencyFailureException) {
      JOptionPane.showMessageDialog(sourceComponent, HtmlHelper
          .emphasis(getTranslationProvider().getTranslation(
              "concurrency.error.description", getLocale())),
          getTranslationProvider().getTranslation("error", getLocale()),
          JOptionPane.ERROR_MESSAGE, getIconFactory().getErrorIcon(
              IIconFactory.LARGE_ICON_SIZE));
    } else {
      ex.printStackTrace();
      JErrorDialog dialog = JErrorDialog.createInstance(sourceComponent,
          getTranslationProvider(), getLocale());
      dialog.setMessageIcon(getIconFactory().getErrorIcon(
          IIconFactory.MEDIUM_ICON_SIZE));
      dialog.setTitle(getTranslationProvider().getTranslation("error",
          getLocale()));
      dialog.setMessage(HtmlHelper.emphasis(ex.getLocalizedMessage()));
      dialog.setDetails(ex);
      int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
      dialog.setSize(8 * screenRes, 3 * screenRes);
      dialog.pack();
      SwingUtil.centerOnScreen(dialog);
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
  public boolean start(IBackendController backendController, Locale locale) {
    if (super.start(backendController, locale)) {
      Toolkit.getDefaultToolkit().getSystemEventQueue().push(
          new WaitCursorEventQueue(500));
      CallbackHandler callbackHandler = getLoginCallbackHandler();
      if (callbackHandler instanceof DialogCallbackHandler) {
        ((DialogCallbackHandler) callbackHandler)
            .setParentComponent(controllerFrame);
      }
      if (performLogin()) {
        try {
          SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
              displayControllerFrame();
              execute(getStartupAction(), getInitialActionContext());
            }
          });
          return true;
        } catch (InvocationTargetException ex) {
          ex.printStackTrace(System.err);
        } catch (InterruptedException ex) {
          ex.printStackTrace(System.err);
        }
        return false;
      }
      stop();
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

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayWorkspace(String workspaceName) {
    if (!ObjectUtils.equals(workspaceName, getSelectedWorkspaceName())) {
      super.displayWorkspace(workspaceName);
      if (workspaceInternalFrames == null) {
        workspaceInternalFrames = new HashMap<String, JInternalFrame>();
      }
      JInternalFrame workspaceInternalFrame = workspaceInternalFrames
          .get(workspaceName);
      if (workspaceInternalFrame == null) {
        IViewDescriptor workspaceViewDescriptor = getWorkspace(workspaceName)
            .getViewDescriptor();
        IValueConnector workspaceConnector = getBackendController()
            .getWorkspaceConnector(workspaceName);
        IView<JComponent> workspaceView = createWorkspaceView(workspaceName,
            workspaceViewDescriptor, (Workspace) workspaceConnector
                .getConnectorValue());
        workspaceInternalFrame = createJInternalFrame(workspaceView);
        workspaceInternalFrame
            .addInternalFrameListener(new WorkspaceInternalFrameListener(
                workspaceName));
        workspaceInternalFrames.put(workspaceName, workspaceInternalFrame);
        controllerFrame.getContentPane().add(workspaceInternalFrame);
        getMvcBinder().bind(workspaceView.getConnector(), workspaceConnector);
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
      updateFrameTitle();
    }
  }

  /**
   * This method has been overriden to take care of long-running operations not
   * to have the swing gui blocked. It uses the foxtrot library to achieve this.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected final boolean executeBackend(final IAction action,
      final Map<String, Object> context) {
    if (action.isLongOperation()) {
      Boolean success = (Boolean) SwingUtil.performLongOperation(new Job() {

        /**
         * Decorates the super implementation with the foxtrot job.
         * <p>
         * {@inheritDoc}
         */
        @Override
        public Object run() {
          return new Boolean(protectedExecuteBackend(action, context));
        }
      });
      return success.booleanValue();
    }
    return protectedExecuteBackend(action, context);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final boolean executeFrontend(final IAction action,
      final Map<String, Object> context) {
    return protectedExecuteFrontend(action, context);
  }

  private JMenu createMenu(ActionList actionList) {
    JMenu menu = new JMenu(actionList.getI18nName(getTranslationProvider(),
        getLocale()));
    if (actionList.getDescription() != null) {
      menu.setToolTipText(actionList.getI18nDescription(
          getTranslationProvider(), getLocale())
          + IActionFactory.TOOLTIP_ELLIPSIS);
    }
    menu.setIcon(getIconFactory().getIcon(actionList.getIconImageURL(),
        IIconFactory.SMALL_ICON_SIZE));
    for (JMenuItem menuItem : createMenuItems(menu, actionList)) {
      menu.add(menuItem);
    }
    return menu;
  }

  private List<JMenuItem> createMenuItems(JMenu menu, ActionList actionList) {
    List<JMenuItem> menuItems = new ArrayList<JMenuItem>();
    for (IDisplayableAction action : actionList.getActions()) {
      menuItems.add(createMenuItem(menu, action));
    }
    return menuItems;
  }

  private JMenuItem createMenuItem(JMenu menu, IDisplayableAction action) {
    return new JMenuItem(getViewFactory().getActionFactory().createAction(
        action, this, menu, null, null, getLocale()));
  }

  private List<JMenu> createActionMenus() {
    return createMenus(getActionMap(), false);
  }

  private JMenuBar createApplicationMenuBar() {
    JMenuBar applicationMenuBar = new JMenuBar();
    List<JMenu> workspaceMenus = createWorkspacesMenus();
    if (workspaceMenus != null) {
      for (JMenu workspaceMenu : workspaceMenus) {
        applicationMenuBar.add(workspaceMenu);
      }
    }
    List<JMenu> actionMenus = createActionMenus();
    if (actionMenus != null) {
      for (JMenu actionMenu : actionMenus) {
        applicationMenuBar.add(actionMenu);
      }
    }
    applicationMenuBar.add(Box.createHorizontalGlue());
    List<JMenu> helpActionMenus = createHelpActionMenus();
    if (helpActionMenus != null) {
      for (JMenu helpActionMenu : helpActionMenus) {
        applicationMenuBar.add(helpActionMenu);
      }
    }
    return applicationMenuBar;
  }

  private JFrame createControllerFrame() {
    JFrame frame = new JFrame();
    frame.setContentPane(new JDesktopPane());
    frame.setIconImage(((ImageIcon) getIconFactory().getIcon(getIconImageURL(),
        IIconFactory.SMALL_ICON_SIZE)).getImage());
    frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    frame.setJMenuBar(createApplicationMenuBar());
    frame.setGlassPane(createHermeticGlassPane());
    frame.addWindowListener(new WindowAdapter() {

      /**
       * {@inheritDoc}
       */
      @Override
      public void windowClosing(@SuppressWarnings("unused") WindowEvent e) {
        stop();
      }
    });
    return frame;
  }

  private List<JMenu> createHelpActionMenus() {
    return createMenus(getHelpActions(), true);
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
  private JInternalFrame createJInternalFrame(IView<JComponent> view) {
    JInternalFrame internalFrame = new JInternalFrame(view.getDescriptor()
        .getI18nName(getTranslationProvider(), getLocale()));
    internalFrame.setFrameIcon(getIconFactory().getIcon(
        view.getDescriptor().getIconImageURL(), IIconFactory.SMALL_ICON_SIZE));
    internalFrame.setResizable(true);
    internalFrame.setClosable(false);
    internalFrame.setMaximizable(true);
    internalFrame.setIconifiable(true);
    internalFrame.getContentPane().add(view.getPeer(), BorderLayout.CENTER);
    internalFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    internalFrame.setGlassPane(createHermeticGlassPane());
    return internalFrame;
  }

  @SuppressWarnings("null")
  private List<JMenu> createMenus(ActionMap actionMap, boolean useSeparator) {
    List<JMenu> menus = new ArrayList<JMenu>();
    if (actionMap != null) {
      JMenu menu = null;
      for (ActionList actionList : actionMap.getActionLists()) {
        if (!useSeparator || menus.isEmpty()) {
          menu = createMenu(actionList);
          menus.add(menu);
        } else {
          menu.addSeparator();
          for (JMenuItem menuItem : createMenuItems(menu, actionList)) {
            menu.add(menuItem);
          }
        }
      }
    }
    return menus;
  }

  private List<JMenu> createWorkspacesMenus() {
    return createMenus(createWorkspaceActionMap(), true);
  }

  private void displayControllerFrame() {
    waitTimer = new WaitCursorTimer(500);
    waitTimer.setDaemon(true);
    waitTimer.start();
    controllerFrame = createControllerFrame();
    controllerFrame.pack();
    int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
    controllerFrame.setSize(12 * screenRes, 8 * screenRes);
    controllerFrame.setSize(1100, 800);
    SwingUtil.centerOnScreen(controllerFrame);
    updateFrameTitle();
    controllerFrame.setVisible(true);
  }

  /**
   * Performs login using JAAS configuration.
   * 
   * @return true if login is successful.
   */
  private boolean performLogin() {
    if (getLoginContextName() != null) {
      int i;
      for (i = 0; i < MAX_LOGIN_RETRIES; i++) {
        try {
          LoginContext lc = null;
          try {
            lc = new LoginContext(getLoginContextName(),
                getLoginCallbackHandler());
          } catch (LoginException le) {
            System.err
                .println("Cannot create LoginContext. " + le.getMessage());
            return false;
          } catch (SecurityException se) {
            System.err
                .println("Cannot create LoginContext. " + se.getMessage());
            return false;
          }
          lc.login();
          loginSuccess(lc.getSubject());
          break;
        } catch (LoginException le) {
          System.err.println("Authentication failed:");
          System.err.println("  " + le.getMessage());
        }
      }
      if (i == 3) {
        return false;
      }
    } else {
      loginSuccess(getAnonymousSubject());
    }
    return true;
  }

  private boolean protectedExecuteBackend(IAction action,
      Map<String, Object> context) {
    return super.executeBackend(action, context);
  }

  private boolean protectedExecuteFrontend(IAction action,
      Map<String, Object> context) {
    return super.executeFrontend(action, context);
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

  /**
   * {@inheritDoc}
   */
  public void displayModalDialog(JComponent mainView, List<Action> actions,
      String title, JComponent sourceComponent) {
    final JDialog dialog;
    Window window = SwingUtil.getVisibleWindow(sourceComponent);
    if (window instanceof Dialog) {
      dialog = new JDialog((Dialog) window, title, true);
    } else {
      dialog = new JDialog((Frame) window, title, true);
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
    SwingUtil.centerInParent(dialog);
    dialog.setVisible(true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void disposeModalDialog(JComponent sourceWidget) {
    Window actionWindow = SwingUtil.getVisibleWindow(sourceWidget);
    if (actionWindow instanceof Dialog) {
      actionWindow.dispose();
    }
  }

  private final class WorkspaceInternalFrameListener extends
      InternalFrameAdapter {

    private String workspaceName;

    /**
     * Constructs a new <code>WorkspaceInternalFrameListener</code> instance.
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
    public void internalFrameActivated(
        @SuppressWarnings("unused") InternalFrameEvent e) {
      displayWorkspace(workspaceName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void internalFrameDeactivated(
        @SuppressWarnings("unused") InternalFrameEvent e) {
      // displayWorkspace(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void internalFrameDeiconified(
        @SuppressWarnings("unused") InternalFrameEvent e) {
      displayWorkspace(workspaceName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void internalFrameIconified(
        @SuppressWarnings("unused") InternalFrameEvent e) {
      // displayWorkspace(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void internalFrameOpened(
        @SuppressWarnings("unused") InternalFrameEvent e) {
      displayWorkspace(workspaceName);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayUrl(String urlSpec) {
    try {
      BrowserControl.displayURL(urlSpec);
    } catch (IOException ex) {
      throw new ActionException(ex);
    }
  }
}
