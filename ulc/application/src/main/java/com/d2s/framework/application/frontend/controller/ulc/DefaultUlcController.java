/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.frontend.controller.ulc;

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

import org.springframework.dao.ConcurrencyFailureException;

import com.d2s.framework.application.backend.IBackendController;
import com.d2s.framework.application.frontend.controller.AbstractFrontendController;
import com.d2s.framework.application.model.Module;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.gui.ulc.components.server.ULCErrorDialog;
import com.d2s.framework.gui.ulc.components.server.ULCExtendedInternalFrame;
import com.d2s.framework.gui.ulc.components.server.event.ExtendedInternalFrameEvent;
import com.d2s.framework.gui.ulc.components.server.event.IExtendedInternalFrameListener;
import com.d2s.framework.security.ulc.DialogCallbackHandler;
import com.d2s.framework.security.ulc.ICallbackHandlerListener;
import com.d2s.framework.util.exception.BusinessException;
import com.d2s.framework.util.html.HtmlHelper;
import com.d2s.framework.util.ulc.UlcUtil;
import com.d2s.framework.view.IIconFactory;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.IViewFactory;
import com.d2s.framework.view.action.ActionMap;
import com.d2s.framework.view.action.IDisplayableAction;
import com.d2s.framework.view.descriptor.IViewDescriptor;
import com.ulcjava.base.application.AbstractAction;
import com.ulcjava.base.application.ApplicationContext;
import com.ulcjava.base.application.ClientContext;
import com.ulcjava.base.application.IAction;
import com.ulcjava.base.application.ULCAlert;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.ULCDesktopPane;
import com.ulcjava.base.application.ULCFiller;
import com.ulcjava.base.application.ULCFrame;
import com.ulcjava.base.application.ULCMenu;
import com.ulcjava.base.application.ULCMenuBar;
import com.ulcjava.base.application.ULCMenuItem;
import com.ulcjava.base.application.ULCPollingTimer;
import com.ulcjava.base.application.event.ActionEvent;
import com.ulcjava.base.application.event.WindowEvent;
import com.ulcjava.base.application.event.serializable.IActionListener;
import com.ulcjava.base.application.event.serializable.IWindowListener;
import com.ulcjava.base.application.util.ULCIcon;
import com.ulcjava.base.shared.IWindowConstants;

/**
 * Default implementation of a swing frontend controller. This implementation is
 * usable "as-is".
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
  private Map<String, ULCExtendedInternalFrame> moduleInternalFrames;

  /**
   * {@inheritDoc}
   */
  public void callbackHandlingComplete() {
    notifyWaiters();
  }

  /**
   * {@inheritDoc}
   */
  public void handleException(Throwable ex, @SuppressWarnings("unused")
  Map<String, Object> context) {
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

  /**
   * {@inheritDoc}
   */
  @Override
  protected void displayModule(String moduleName) {
    if (moduleInternalFrames == null) {
      moduleInternalFrames = new HashMap<String, ULCExtendedInternalFrame>();
    }
    ULCExtendedInternalFrame moduleInternalFrame = moduleInternalFrames
        .get(moduleName);
    if (moduleInternalFrame == null) {
      IViewDescriptor moduleViewDescriptor = getModule(moduleName)
          .getViewDescriptor();
      IValueConnector moduleConnector = getBackendController()
          .getModuleConnector(moduleName);
      IView<ULCComponent> moduleView = createModuleView(moduleName,
          moduleViewDescriptor, (Module) moduleConnector.getConnectorValue());
      moduleInternalFrame = createULCExtendedInternalFrame(moduleView);
      moduleInternalFrame
          .setFrameIcon(getIconFactory().getIcon(
              moduleViewDescriptor.getIconImageURL(),
              IIconFactory.SMALL_ICON_SIZE));
      moduleInternalFrame
          .addExtendedInternalFrameListener(new ModuleInternalFrameListener(
              moduleName));
      moduleInternalFrames.put(moduleName, moduleInternalFrame);
      controllerFrame.getContentPane().add(moduleInternalFrame);
      getMvcBinder().bind(moduleView.getConnector(), moduleConnector);
      moduleInternalFrame.pack();
      moduleInternalFrame.setSize(controllerFrame.getSize());
    }
    moduleInternalFrame.setVisible(true);
    if (moduleInternalFrame.isIcon()) {
      moduleInternalFrame.setIcon(false);
    }
    moduleInternalFrame.setMaximum(true);
    setSelectedModuleName(moduleName);
    moduleInternalFrame.moveToFront();
    super.displayModule(moduleName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setSelectedModuleName(String moduleName) {
    super.setSelectedModuleName(moduleName);
    updateFrameTitle();
  }

  private ULCMenu createActionMenu(String titleKey,
      List<IDisplayableAction> actionList, ULCComponent sourceComponent) {
    ULCMenu menu = new ULCMenu(getTranslationProvider().getTranslation(
        titleKey, getLocale()));
    for (IDisplayableAction action : actionList) {
      menu
          .add(new ULCMenuItem(getViewFactory().getActionFactory()
              .createAction(action, this, sourceComponent, null, null,
                  getLocale())));
    }
    return menu;
  }

  private List<ULCMenu> createActionMenus(ULCComponent sourceComponent) {
    return createMenus(sourceComponent, getActions());
  }

  private List<ULCMenu> createHelpActionMenus(ULCComponent sourceComponent) {
    return createMenus(sourceComponent, getHelpActions());
  }

  private List<ULCMenu> createMenus(ULCComponent sourceComponent,
      ActionMap actionMap) {
    List<ULCMenu> menus = new ArrayList<ULCMenu>();
    if (actionMap != null) {
      for (Map.Entry<String, List<IDisplayableAction>> actionList : actionMap
          .getActionMap().entrySet()) {
        ULCMenu menu = createActionMenu(actionList.getKey(), actionList
            .getValue(), sourceComponent);
        menu.setIcon(getIconFactory().getIcon(
              actionMap.getIconImageURL(actionList.getKey()),
              IIconFactory.SMALL_ICON_SIZE));
        menus.add(menu);
      }
    }
    return menus;
  }

  private ULCMenuBar createApplicationMenuBar(ULCComponent sourceComponent) {
    ULCMenuBar applicationMenuBar = new ULCMenuBar();
    applicationMenuBar.add(createModulesMenu());
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

      public void windowClosing(@SuppressWarnings("unused")
      WindowEvent event) {
        stop();
      }
    });
    return frame;
  }

  private ULCMenu createModulesMenu() {
    ULCMenu modulesMenu = new ULCMenu(getTranslationProvider().getTranslation(
        "modules", getLocale()));
    modulesMenu.setIcon(getIconFactory().getIcon(getModulesMenuIconImageUrl(),
        IIconFactory.SMALL_ICON_SIZE));
    for (String moduleName : getModuleNames()) {
      IViewDescriptor moduleViewDescriptor = getModule(moduleName)
          .getViewDescriptor();
      ULCMenuItem moduleMenuItem = new ULCMenuItem(new ModuleSelectionAction(
          moduleName, moduleViewDescriptor));
      modulesMenu.add(moduleMenuItem);
    }
    modulesMenu.addSeparator();
    modulesMenu.add(new ULCMenuItem(new QuitAction()));
    return modulesMenu;
  }

  /**
   * Creates a new ULCExtendedInternalFrame and populates it with a view.
   * 
   * @param view
   *            the view to be set into the internal frame.
   * @return the constructed internal frame.
   */
  private ULCExtendedInternalFrame createULCExtendedInternalFrame(
      IView<ULCComponent> view) {
    ULCExtendedInternalFrame internalFrame = new ULCExtendedInternalFrame(view
        .getDescriptor().getI18nName(getTranslationProvider(), getLocale()));
    internalFrame.setResizable(true);
    internalFrame.setClosable(false);
    internalFrame.setMaximizable(true);
    internalFrame.setIconifiable(true);
    internalFrame.getContentPane().add(view.getPeer());
    internalFrame.setDefaultCloseOperation(IWindowConstants.HIDE_ON_CLOSE);
    return internalFrame;
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
    new LoginThread().start();
    loginTimer = new ULCPollingTimer(2000, new IActionListener() {

      private static final long serialVersionUID = 5630061795918376362L;

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent event) {
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
      while (!loginSuccessful && loginRetries < MAX_LOGIN_RETRIES) {
        LoginContext lc = null;
        try {
          lc = new LoginContext(getLoginContextName(),
              new ThreadBlockingCallbackHandler());
        } catch (LoginException le) {
          System.err.println("Cannot create LoginContext. " + le.getMessage());
        } catch (SecurityException se) {
          System.err.println("Cannot create LoginContext. " + se.getMessage());
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
      loginComplete = true;
    }
  }

  private final class ModuleInternalFrameListener implements
      IExtendedInternalFrameListener {

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
    public void internalFrameActivated(@SuppressWarnings("unused")
    ExtendedInternalFrameEvent e) {
      setSelectedModuleName(moduleName);
    }

    /**
     * {@inheritDoc}
     */
    public void internalFrameDeactivated(@SuppressWarnings("unused")
    ExtendedInternalFrameEvent e) {
      setSelectedModuleName(null);
    }

    /**
     * {@inheritDoc}
     */
    public void internalFrameDeiconified(@SuppressWarnings("unused")
    ExtendedInternalFrameEvent event) {
      setSelectedModuleName(moduleName);
    }

    /**
     * {@inheritDoc}
     */
    public void internalFrameIconified(@SuppressWarnings("unused")
    ExtendedInternalFrameEvent event) {
      setSelectedModuleName(null);
    }

    /**
     * {@inheritDoc}
     */
    public void internalFrameOpened(@SuppressWarnings("unused")
    ExtendedInternalFrameEvent event) {
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
      putValue(com.ulcjava.base.application.IAction.NAME, moduleViewDescriptor
          .getI18nName(getTranslationProvider(), getLocale()));
      putValue(com.ulcjava.base.application.IAction.SHORT_DESCRIPTION,
          moduleViewDescriptor.getI18nDescription(getTranslationProvider(),
              getLocale())
              + IViewFactory.TOOLTIP_ELLIPSIS);
      putValue(com.ulcjava.base.application.IAction.SMALL_ICON,
          getIconFactory().getIcon(moduleViewDescriptor.getIconImageURL(),
              IIconFactory.TINY_ICON_SIZE));
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

    private static final long serialVersionUID = -1476651758085260422L;

    /**
     * Constructs a new <code>ModuleSelectionAction</code> instance.
     */
    public QuitAction() {
      putValue(com.ulcjava.base.application.IAction.NAME,
          getTranslationProvider().getTranslation("quit.name", getLocale()));
      putValue(com.ulcjava.base.application.IAction.SHORT_DESCRIPTION,
          getTranslationProvider().getTranslation("quit.description",
              getLocale()));
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

  private class ThreadBlockingCallbackHandler implements CallbackHandler {

    /**
     * {@inheritDoc}
     */
    public void handle(Callback[] callbacks) {
      loginCallbacks = callbacks;
      waitForNotification();
    }
  }
}
