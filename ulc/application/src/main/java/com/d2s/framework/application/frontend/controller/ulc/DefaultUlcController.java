/*
 * Copyright (c) 2005 Design2see. All rights reserved.
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
import com.d2s.framework.application.view.descriptor.IModuleDescriptor;
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
import com.d2s.framework.view.action.IDisplayableAction;
import com.ulcjava.base.application.AbstractAction;
import com.ulcjava.base.application.ApplicationContext;
import com.ulcjava.base.application.ClientContext;
import com.ulcjava.base.application.IAction;
import com.ulcjava.base.application.ULCAlert;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.ULCDesktopPane;
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
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultUlcController extends
    AbstractFrontendController<ULCComponent, ULCIcon, IAction> implements
    ICallbackHandlerListener {

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
      loginComplete = true;
    }
  }
  private final class ModuleInternalFrameListener implements
      IExtendedInternalFrameListener {

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
    public void internalFrameActivated(@SuppressWarnings("unused")
    ExtendedInternalFrameEvent e) {
      setSelectedModuleId(moduleId);
    }

    /**
     * {@inheritDoc}
     */
    public void internalFrameDeactivated(@SuppressWarnings("unused")
    ExtendedInternalFrameEvent e) {
      setSelectedModuleId(null);
    }

    /**
     * {@inheritDoc}
     */
    public void internalFrameDeiconified(@SuppressWarnings("unused")
    ExtendedInternalFrameEvent event) {
      setSelectedModuleId(moduleId);
    }

    /**
     * {@inheritDoc}
     */
    public void internalFrameIconified(@SuppressWarnings("unused")
    ExtendedInternalFrameEvent event) {
      setSelectedModuleId(null);
    }

    /**
     * {@inheritDoc}
     */
    public void internalFrameOpened(@SuppressWarnings("unused")
    ExtendedInternalFrameEvent event) {
      setSelectedModuleId(moduleId);
    }

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
      putValue(com.ulcjava.base.application.IAction.NAME, moduleDescriptor
          .getI18nName(getTranslationProvider(), getLocale()));
      putValue(com.ulcjava.base.application.IAction.SHORT_DESCRIPTION,
          moduleDescriptor.getI18nDescription(getTranslationProvider(),
              getLocale())
              + IViewFactory.TOOLTIP_ELLIPSIS);
      putValue(com.ulcjava.base.application.IAction.SMALL_ICON,
          getIconFactory().getIcon(moduleDescriptor.getIconImageURL(),
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
        getBackendController().checkModuleAccess(moduleId);
        displayModule(moduleId);
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
    Map<String, List<IDisplayableAction>> actions = getActions();
    List<ULCMenu> actionMenus = new ArrayList<ULCMenu>();
    if (actions != null) {
      for (Map.Entry<String, List<IDisplayableAction>> actionList : actions
          .entrySet()) {
        actionMenus.add(createActionMenu(actionList.getKey(), actionList
            .getValue(), sourceComponent));
      }
    }
    return actionMenus;
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

  private ULCMenu createModulesMenu() {
    ULCMenu modulesMenu = new ULCMenu(getTranslationProvider().getTranslation(
        "modules", getLocale()));
    // modulesMenu.setIcon(getIconFactory().getIcon(getModulesMenuIconImageUrl(),
    // IIconFactory.SMALL_ICON_SIZE));
    for (String moduleId : getModuleIds()) {
      IModuleDescriptor moduleDescriptor = getModuleDescriptor(moduleId);
      ULCMenuItem moduleMenuItem = new ULCMenuItem(new ModuleSelectionAction(
          moduleId, moduleDescriptor));
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
   *          the view to be set into the internal frame.
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

  /**
   * {@inheritDoc}
   */
  @Override
  protected void displayModule(String moduleId) {
    if (moduleInternalFrames == null) {
      moduleInternalFrames = new HashMap<String, ULCExtendedInternalFrame>();
    }
    ULCExtendedInternalFrame moduleInternalFrame = moduleInternalFrames
        .get(moduleId);
    if (moduleInternalFrame == null) {
      IModuleDescriptor moduleDescriptor = getModuleDescriptor(moduleId);
      IValueConnector moduleConnector = getBackendController()
          .getModuleConnector(moduleId);
      IView<ULCComponent> moduleView = createModuleView(moduleId,
          moduleDescriptor, (Module) moduleConnector.getConnectorValue());
      moduleInternalFrame = createULCExtendedInternalFrame(moduleView);
      moduleInternalFrame.setFrameIcon(getIconFactory().getIcon(
          moduleDescriptor.getIconImageURL(), IIconFactory.SMALL_ICON_SIZE));
      moduleInternalFrame
          .addExtendedInternalFrameListener(new ModuleInternalFrameListener(
              moduleId));
      moduleInternalFrames.put(moduleId, moduleInternalFrame);
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
    setSelectedModuleId(moduleId);
    moduleInternalFrame.moveToFront();
    super.displayModule(moduleId);
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

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setSelectedModuleId(String moduleId) {
    super.setSelectedModuleId(moduleId);
    updateFrameTitle();
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

  private void updateFrameTitle() {
    String moduleId = getSelectedModuleId();
    if (moduleId != null) {
      controllerFrame.setTitle(getModuleDescriptor(getSelectedModuleId())
          .getI18nDescription(getTranslationProvider(), getLocale())
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
}
