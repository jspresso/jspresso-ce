/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.controller.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
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
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.springframework.dao.ConcurrencyFailureException;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IAction;
import com.d2s.framework.application.ControllerException;
import com.d2s.framework.application.backend.IBackendController;
import com.d2s.framework.application.frontend.controller.AbstractFrontendController;
import com.d2s.framework.application.model.Module;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.gui.swing.components.JErrorDialog;
import com.d2s.framework.security.swing.DialogCallbackHandler;
import com.d2s.framework.util.exception.BusinessException;
import com.d2s.framework.util.html.HtmlHelper;
import com.d2s.framework.util.swing.SwingUtil;
import com.d2s.framework.util.swing.WaitCursorEventQueue;
import com.d2s.framework.util.swing.WaitCursorTimer;
import com.d2s.framework.view.IIconFactory;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.IViewFactory;
import com.d2s.framework.view.action.ActionMap;
import com.d2s.framework.view.action.IDisplayableAction;
import com.d2s.framework.view.descriptor.IViewDescriptor;

import foxtrot.Job;

/**
 * Default implementation of a swing frontend controller. This implementation is
 * usable "as-is".
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultSwingController extends
    AbstractFrontendController<JComponent, Icon, Action> {

  private JFrame                      controllerFrame;
  private Map<String, JInternalFrame> moduleInternalFrames;

  private WaitCursorTimer             waitTimer;

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
  public void handleException(Throwable ex, @SuppressWarnings("unused")
  Map<String, Object> context) {
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
        displayControllerFrame();
        execute(getStartupAction(), getInitialActionContext());
        return true;
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
  protected void displayModule(String moduleName) {
    if (moduleInternalFrames == null) {
      moduleInternalFrames = new HashMap<String, JInternalFrame>();
    }
    JInternalFrame moduleInternalFrame = moduleInternalFrames.get(moduleName);
    if (moduleInternalFrame == null) {
      IViewDescriptor moduleViewDescriptor = getModule(moduleName)
          .getViewDescriptor();
      IValueConnector moduleConnector = getBackendController()
          .getModuleConnector(moduleName);
      IView<JComponent> moduleView = createModuleView(moduleName,
          moduleViewDescriptor, (Module) moduleConnector.getConnectorValue());
      moduleInternalFrame = createJInternalFrame(moduleView);
      moduleInternalFrame
          .setFrameIcon(getIconFactory().getIcon(
              moduleViewDescriptor.getIconImageURL(),
              IIconFactory.SMALL_ICON_SIZE));
      moduleInternalFrame
          .addInternalFrameListener(new ModuleInternalFrameListener(moduleName));
      moduleInternalFrames.put(moduleName, moduleInternalFrame);
      controllerFrame.getContentPane().add(moduleInternalFrame);
      getMvcBinder().bind(moduleView.getConnector(), moduleConnector);
      moduleInternalFrame.pack();
      moduleInternalFrame.setSize(controllerFrame.getSize());
    }
    moduleInternalFrame.setVisible(true);
    if (moduleInternalFrame.isIcon()) {
      try {
        moduleInternalFrame.setIcon(false);
      } catch (PropertyVetoException ex) {
        throw new ControllerException(ex);
      }
    }
    try {
      moduleInternalFrame.setMaximum(true);
    } catch (PropertyVetoException ex) {
      throw new ControllerException(ex);
    }
    setSelectedModuleName(moduleName);
    moduleInternalFrame.toFront();
    super.displayModule(moduleName);
  }

  /**
   * This method has been overriden to take care of long-running operations not
   * to have the swing gui blocked. It uses the foxtrot library to achieve this.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
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
  @SuppressWarnings("unchecked")
  @Override
  protected final boolean executeFrontend(final IAction action,
      final Map<String, Object> context) {
    return protectedExecuteFrontend(action, context);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setSelectedModuleName(String moduleName) {
    super.setSelectedModuleName(moduleName);
    updateFrameTitle();
  }

  private JMenu createActionMenu(String titleKey,
      List<IDisplayableAction> actionList) {
    JMenu menu = new JMenu(getTranslationProvider().getTranslation(titleKey,
        getLocale()));
    for (IDisplayableAction action : actionList) {
      menu.add(new JMenuItem(getViewFactory().getActionFactory().createAction(
          action, this, menu, null, null, getLocale())));
    }
    return menu;
  }

  private List<JMenu> createActionMenus() {
    return createMenus(getActions());
  }

  private List<JMenu> createHelpActionMenus() {
    return createMenus(getHelpActions());
  }

  private List<JMenu> createMenus(ActionMap actionMap) {
    List<JMenu> menus = new ArrayList<JMenu>();
    if (actionMap != null) {
      for (Map.Entry<String, List<IDisplayableAction>> actionList : actionMap
          .getActionMap().entrySet()) {
        JMenu menu = createActionMenu(actionList.getKey(), actionList.getValue());
        menu.setIcon(getIconFactory().getIcon(
            actionMap.getIconImageURL(actionList.getKey()),
            IIconFactory.SMALL_ICON_SIZE));
        menus.add(menu);
      }
    }
    return menus;
  }

  private JMenuBar createApplicationMenuBar() {
    JMenuBar applicationMenuBar = new JMenuBar();
    applicationMenuBar.add(createModulesMenu());
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
      public void windowClosing(@SuppressWarnings("unused")
      WindowEvent e) {
        stop();
      }
    });
    return frame;
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
   *            the view to be set into the internal frame.
   * @return the constructed internal frame.
   */
  private JInternalFrame createJInternalFrame(IView<JComponent> view) {
    JInternalFrame internalFrame = new JInternalFrame(view.getDescriptor()
        .getI18nName(getTranslationProvider(), getLocale()));
    internalFrame.setResizable(true);
    internalFrame.setClosable(false);
    internalFrame.setMaximizable(true);
    internalFrame.setIconifiable(true);
    internalFrame.getContentPane().add(view.getPeer(), BorderLayout.CENTER);
    internalFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    internalFrame.setGlassPane(createHermeticGlassPane());
    return internalFrame;
  }

  private JMenu createModulesMenu() {
    JMenu modulesMenu = new JMenu(getTranslationProvider().getTranslation(
        "modules", getLocale()));
    modulesMenu.setIcon(getIconFactory().getIcon(getModulesMenuIconImageUrl(),
        IIconFactory.SMALL_ICON_SIZE));
    for (String moduleName : getModuleNames()) {
      IViewDescriptor moduleViewDescriptor = getModule(moduleName)
          .getViewDescriptor();
      JMenuItem moduleMenuItem = new JMenuItem(new ModuleSelectionAction(
          moduleName, moduleViewDescriptor));
      modulesMenu.add(moduleMenuItem);
    }
    modulesMenu.addSeparator();
    modulesMenu.add(new JMenuItem(new QuitAction()));
    return modulesMenu;
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
    int i;
    for (i = 0; i < MAX_LOGIN_RETRIES; i++) {
      try {
        LoginContext lc = null;
        try {
          lc = new LoginContext(getLoginContextName(),
              getLoginCallbackHandler());
        } catch (LoginException le) {
          System.err.println("Cannot create LoginContext. " + le.getMessage());
          return false;
        } catch (SecurityException se) {
          System.err.println("Cannot create LoginContext. " + se.getMessage());
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
    String moduleName = getSelectedModuleName();
    if (moduleName != null) {
      controllerFrame.setTitle(getModule(getSelectedModuleName())
          .getViewDescriptor().getI18nDescription(getTranslationProvider(),
              getLocale())
          + " - " + getI18nName(getTranslationProvider(), getLocale()));
    } else {
      controllerFrame.setTitle(getI18nName(getTranslationProvider(),
          getLocale()));
    }
  }

  private final class ModuleInternalFrameListener extends InternalFrameAdapter {

    private String moduleName;

    /**
     * Constructs a new <code>ModuleInternalFrameListener</code> instance.
     * 
     * @param moduleName
     *            the root module identifier this listener is attached to.
     */
    public ModuleInternalFrameListener(String moduleName) {
      this.moduleName = moduleName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void internalFrameActivated(@SuppressWarnings("unused")
    InternalFrameEvent e) {
      setSelectedModuleName(moduleName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void internalFrameDeactivated(@SuppressWarnings("unused")
    InternalFrameEvent e) {
      setSelectedModuleName(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void internalFrameDeiconified(@SuppressWarnings("unused")
    InternalFrameEvent e) {
      setSelectedModuleName(moduleName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void internalFrameIconified(@SuppressWarnings("unused")
    InternalFrameEvent e) {
      setSelectedModuleName(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void internalFrameOpened(@SuppressWarnings("unused")
    InternalFrameEvent e) {
      setSelectedModuleName(moduleName);
    }

  }

  private final class ModuleSelectionAction extends AbstractAction {

    private static final long serialVersionUID = 3469745193806038352L;
    private String            moduleName;

    /**
     * Constructs a new <code>ModuleSelectionAction</code> instance.
     * 
     * @param moduleName
     * @param moduleViewDescriptor
     */
    public ModuleSelectionAction(String moduleName,
        IViewDescriptor moduleViewDescriptor) {
      this.moduleName = moduleName;
      putValue(Action.NAME, moduleViewDescriptor.getI18nName(
          getTranslationProvider(), getLocale()));
      putValue(Action.SHORT_DESCRIPTION, moduleViewDescriptor
          .getI18nDescription(getTranslationProvider(), getLocale())
          + IViewFactory.TOOLTIP_ELLIPSIS);
      putValue(Action.SMALL_ICON, getIconFactory().getIcon(
          moduleViewDescriptor.getIconImageURL(), IIconFactory.TINY_ICON_SIZE));
    }

    /**
     * displays the selected module.
     * <p>
     * {@inheritDoc}
     */
    public void actionPerformed(@SuppressWarnings("unused")
    ActionEvent e) {
      try {
        getBackendController().checkModuleAccess(moduleName);
        displayModule(moduleName);
      } catch (SecurityException ex) {
        handleException(ex, null);
      }
    }
  }

  private final class QuitAction extends AbstractAction {

    private static final long serialVersionUID = -5797994634301619085L;

    /**
     * Constructs a new <code>ModuleSelectionAction</code> instance.
     */
    public QuitAction() {
      putValue(Action.NAME, getTranslationProvider().getTranslation(
          "quit.name", getLocale()));
      putValue(Action.SHORT_DESCRIPTION, getTranslationProvider()
          .getTranslation("quit.description", getLocale()));
    }

    /**
     * displays the selected module.
     * <p>
     * {@inheritDoc}
     */
    public void actionPerformed(@SuppressWarnings("unused")
    ActionEvent e) {
      stop();
    }
  }
}
