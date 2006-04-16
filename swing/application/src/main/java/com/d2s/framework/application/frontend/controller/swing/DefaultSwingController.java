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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.swing.AbstractAction;
import javax.swing.Action;
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
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.d2s.framework.application.ControllerException;
import com.d2s.framework.application.backend.IBackendController;
import com.d2s.framework.application.frontend.controller.AbstractFrontendController;
import com.d2s.framework.application.view.descriptor.IModuleDescriptor;
import com.d2s.framework.security.swing.DialogCallbackHandler;
import com.d2s.framework.util.swing.SwingUtil;
import com.d2s.framework.util.swing.WaitCursorTimer;
import com.d2s.framework.view.IIconFactory;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.action.ActionContextConstants;
import com.d2s.framework.view.action.IAction;

import foxtrot.Job;

/**
 * Default implementation of a swing frontend controller. This implementation is
 * usable "as-is".
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultSwingController extends
    AbstractFrontendController<JComponent> {

  private JFrame                      controllerFrame;
  private Map<String, JInternalFrame> moduleInternalFrames;

  private WaitCursorTimer             waitTimer;

  /**
   * Creates the initial view from the root view descriptor, then a JFrame
   * containing this view and presents it to the user.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean start(IBackendController backendController, Locale locale) {
    if (super.start(backendController, locale)) {
      // Toolkit.getDefaultToolkit().getSystemEventQueue().push(new
      // WaitCursorEventQueue(500));
      waitTimer = new WaitCursorTimer(500);
      waitTimer.setDaemon(true);
      waitTimer.start();
      controllerFrame = createControllerFrame();
      controllerFrame.pack();
      int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
      controllerFrame.setSize(12 * screenRes, 8 * screenRes);
      controllerFrame.setSize(1100, 800);
      SwingUtil.centerOnScreen(controllerFrame);
      controllerFrame.setVisible(true);
      CallbackHandler callbackHandler = getLoginCallbackHandler();
      if (callbackHandler instanceof DialogCallbackHandler) {
        ((DialogCallbackHandler) callbackHandler)
            .setParentComponent(controllerFrame);
      }
      if (performLogin()) {
        return true;
      }
      stop();
    }
    return false;
  }

  /**
   * Performs login using JAAS configuration.
   * 
   * @return true if login is successful.
   */
  private boolean performLogin() {
    LoginContext lc = null;
    try {
      lc = new LoginContext(getLoginContextName(), getLoginCallbackHandler());
    } catch (LoginException le) {
      System.err.println("Cannot create LoginContext. " + le.getMessage());
      return false;
    } catch (SecurityException se) {
      System.err.println("Cannot create LoginContext. " + se.getMessage());
      return false;
    }
    int i;
    for (i = 0; i < MAX_LOGIN_RETRIES; i++) {
      try {
        lc.login();
        getBackendController().getApplicationSession()
            .setOwner(lc.getSubject());
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

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean stop() {
    controllerFrame.dispose();
    return true;
  }

  private void displayModule(String moduleId) {
    if (moduleInternalFrames == null) {
      moduleInternalFrames = new HashMap<String, JInternalFrame>();
    }
    JInternalFrame moduleInternalFrame = moduleInternalFrames.get(moduleId);
    if (moduleInternalFrame == null) {
      IModuleDescriptor moduleDescriptor = getModuleDescriptor(moduleId);
      IView<JComponent> moduleView = createModuleView(moduleId,
          moduleDescriptor);
      moduleInternalFrame = createJInternalFrame(moduleView);
      moduleInternalFrame.setFrameIcon(getIconFactory().getIcon(
          moduleDescriptor.getIconImageURL(), IIconFactory.SMALL_ICON_SIZE));
      moduleInternalFrame
          .addInternalFrameListener(new ModuleInternalFrameListener(moduleId));
      moduleInternalFrames.put(moduleId, moduleInternalFrame);
      controllerFrame.getContentPane().add(moduleInternalFrame);
      getMvcBinder().bind(moduleView.getConnector(),
          getBackendController().getModuleConnector(moduleId));
      moduleInternalFrame.pack();
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
    moduleInternalFrame.toFront();
  }

  private final class ModuleInternalFrameListener extends InternalFrameAdapter {

    private String moduleId;

    /**
     * Constructs a new <code>ModuleInternalFrameListener</code> instance.
     * 
     * @param moduleId
     *          the root module identifier this listener is attached to.
     */
    public ModuleInternalFrameListener(String moduleId) {
      this.moduleId = moduleId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void internalFrameActivated(@SuppressWarnings("unused")
    InternalFrameEvent e) {
      setSelectedModuleId(moduleId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void internalFrameDeiconified(@SuppressWarnings("unused")
    InternalFrameEvent e) {
      setSelectedModuleId(moduleId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void internalFrameOpened(@SuppressWarnings("unused")
    InternalFrameEvent e) {
      setSelectedModuleId(moduleId);
    }
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected IIconFactory<Icon> getIconFactory() {
    return (IIconFactory<Icon>) super.getIconFactory();
  }

  private JFrame createControllerFrame() {
    JFrame frame = new JFrame(getLabelTranslator().getTranslation(getName(),
        getLocale()));
    frame.setContentPane(new JDesktopPane());
    frame.setIconImage(((ImageIcon) getIconFactory().getIcon(getIconImageURL(),
        IIconFactory.SMALL_ICON_SIZE)).getImage());
    frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    frame.setJMenuBar(getApplicationMenuBar());
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

  private JMenuBar getApplicationMenuBar() {
    JMenuBar applicationMenuBar = new JMenuBar();
    applicationMenuBar.add(getModulesMenu());
    return applicationMenuBar;
  }

  private JMenu getModulesMenu() {
    JMenu modulesMenu = new JMenu(getLabelTranslator().getTranslation(
        "Modules", getLocale()));
    modulesMenu.setIcon(getIconFactory().getIcon(getModulesMenuIconImageUrl(),
        IIconFactory.SMALL_ICON_SIZE));
    for (String moduleId : getModuleIds()) {
      IModuleDescriptor moduleDescriptor = getModuleDescriptor(moduleId);
      JMenuItem moduleMenuItem = new JMenuItem(new ModuleSelectionAction(
          moduleId, moduleDescriptor));
      modulesMenu.add(moduleMenuItem);
    }
    modulesMenu.addSeparator();
    modulesMenu.add(new JMenuItem(new QuitAction()));
    return modulesMenu;
  }

  private final class ModuleSelectionAction extends AbstractAction {

    private static final long serialVersionUID = 3469745193806038352L;
    private String            moduleId;

    /**
     * Constructs a new <code>ModuleSelectionAction</code> instance.
     * 
     * @param moduleId
     * @param moduleDescriptor
     */
    public ModuleSelectionAction(String moduleId,
        IModuleDescriptor moduleDescriptor) {
      this.moduleId = moduleId;
      putValue(Action.NAME, getLabelTranslator().getTranslation(
          moduleDescriptor.getName(), getLocale()));
      putValue(Action.SHORT_DESCRIPTION, getDescriptionTranslator()
          .getTranslation(moduleDescriptor.getDescription(), getLocale()));
      putValue(Action.SMALL_ICON, getIconFactory().getIcon(
          moduleDescriptor.getIconImageURL(), IIconFactory.TINY_ICON_SIZE));
    }

    /**
     * displays the selected module.
     * <p>
     * {@inheritDoc}
     */
    public void actionPerformed(@SuppressWarnings("unused")
    ActionEvent e) {
      displayModule(moduleId);
    }
  }

  private final class QuitAction extends AbstractAction {

    private static final long serialVersionUID = -5797994634301619085L;

    /**
     * Constructs a new <code>ModuleSelectionAction</code> instance.
     */
    public QuitAction() {
      putValue(Action.NAME, getLabelTranslator().getTranslation("QUIT",
          getLocale()));
      putValue(Action.SHORT_DESCRIPTION, getDescriptionTranslator()
          .getTranslation("QUIT", getLocale()));
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

  /**
   * Creates a new JInternalFrame and populates it with a view.
   * 
   * @param view
   *          the view to be set into the internal frame.
   * @return the constructed internal frame.
   */
  private JInternalFrame createJInternalFrame(IView<JComponent> view) {
    JInternalFrame internalFrame = new JInternalFrame(getLabelTranslator()
        .getTranslation(view.getDescriptor().getName(), getLocale()), true,
        false, true, true);
    internalFrame.getContentPane().add(view.getPeer(), BorderLayout.CENTER);
    internalFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    internalFrame.setGlassPane(createHermeticGlassPane());
    return internalFrame;
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
   * This method has been overriden to take care of long-running operations not
   * to have the swing gui blocked. It uses the foxtrot library to achieve this.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected final void executeBackend(final IAction action,
      final Map<String, Object> context) {
    if (action.isLongOperation()) {
      SwingUtil.performLongOperation(new Job() {

        /**
         * Decorates the super implementation with the foxtrot job.
         * <p>
         * {@inheritDoc}
         */
        @Override
        public Object run() {
          protectedExecuteBackend(action, context);
          return null;
        }
      });
    } else {
      protectedExecuteBackend(action, context);
    }
  }

  private void protectedExecuteBackend(IAction action,
      Map<String, Object> context) {
    super.executeBackend(action, context);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected final void executeFrontend(final IAction action,
      final Map<String, Object> context) {
    // return (Map<String, Object>) SwingUtil.performLongOperation(new Job() {
    //
    // /**
    // * Decorates the super implementation with the foxtrot job.
    // * <p>
    // * {@inheritDoc}
    // */
    // @Override
    // public Object run() {
    // return protectedExecuteFrontend(action);
    // }
    // });
    protectedExecuteFrontend(action, context);
  }

  private void protectedExecuteFrontend(IAction action,
      Map<String, Object> context) {
    super.executeFrontend(action, context);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute(IAction action, Map<String, Object> context) {
    if (action == null) {
      return;
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
      super.execute(action, context);
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
}
