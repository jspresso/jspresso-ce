/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.controller.ulc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import com.d2s.framework.application.backend.IBackendController;
import com.d2s.framework.application.frontend.controller.AbstractFrontendController;
import com.d2s.framework.security.ulc.DialogCallbackHandler;
import com.d2s.framework.security.ulc.ILoginListener;
import com.d2s.framework.util.ulc.UlcUtil;
import com.d2s.framework.view.IIconFactory;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.descriptor.module.IModuleDescriptor;
import com.ulcjava.base.application.AbstractAction;
import com.ulcjava.base.application.ApplicationContext;
import com.ulcjava.base.application.ClientContext;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.ULCDesktopPane;
import com.ulcjava.base.application.ULCFrame;
import com.ulcjava.base.application.ULCInternalFrame;
import com.ulcjava.base.application.ULCMenu;
import com.ulcjava.base.application.ULCMenuBar;
import com.ulcjava.base.application.ULCMenuItem;
import com.ulcjava.base.application.event.ActionEvent;
import com.ulcjava.base.application.event.WindowEvent;
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
    AbstractFrontendController<ULCComponent> implements ILoginListener {

  private ULCFrame                      controllerFrame;
  private Map<String, ULCInternalFrame> moduleInternalFrames;
  private NameCallback                  nameLoginCallback;
  private PasswordCallback              passwordLoginCallback;
  private int                           loginRetries;

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
      controllerFrame = createControllerFrame();
      controllerFrame.pack();
      int screenRes = ClientContext.getScreenResolution();
      controllerFrame.setSize(12 * screenRes, 8 * screenRes);
      UlcUtil.centerOnScreen(controllerFrame);
      controllerFrame.setVisible(true);
      CallbackHandler callbackHandler = getLoginCallbackHandler();
      if (callbackHandler instanceof DialogCallbackHandler) {
        ((DialogCallbackHandler) callbackHandler)
            .setParentComponent(controllerFrame);
        ((DialogCallbackHandler) callbackHandler).setLoginListener(this);
      }
      initiateLogin();
      return true;
    }
    return false;
  }

  private void initiateLogin() {
    Callback[] callbacks = new Callback[3];
    nameLoginCallback = new NameCallback("User");
    passwordLoginCallback = new PasswordCallback("Password", false);
    callbacks[0] = nameLoginCallback;
    callbacks[1] = passwordLoginCallback;
    callbacks[2] = new TextOutputCallback(TextOutputCallback.INFORMATION,
        "Enter login information :");
    try {
      getLoginCallbackHandler().handle(callbacks);
    } catch (IOException ex) {
      // NO-OP
    } catch (UnsupportedCallbackException ex) {
      // NO-OP
    }
  }

  /**
   * {@inheritDoc}
   */
  public void loginComplete() {
    LoginContext lc = null;
    try {
      lc = new LoginContext(getLoginContextName(),
          new AutomaticCallbackHandler());
    } catch (LoginException le) {
      System.err.println("Cannot create LoginContext. " + le.getMessage());
    } catch (SecurityException se) {
      System.err.println("Cannot create LoginContext. " + se.getMessage());
    }
    try {
      // attempt authentication
      lc.login();
      // if we return with no exception,
      // authentication succeeded
      getBackendController().getApplicationSession().setOwner(lc.getSubject());
      return;
    } catch (LoginException le) {
      loginRetries++;
      System.err.println("Authentication failed:");
      System.err.println("  " + le.getMessage());
    }
    if (loginRetries < 3) {
      initiateLogin();
    } else {
      stop();
    }
  }

  private class AutomaticCallbackHandler implements CallbackHandler {

    /**
     * {@inheritDoc}
     */
    public void handle(Callback[] callbacks) {
      for (Callback callback : callbacks) {
        if (callback instanceof NameCallback) {
          ((NameCallback) callback).setName(nameLoginCallback.getName());
        } else if (callback instanceof PasswordCallback) {
          ((PasswordCallback) callback).setPassword(passwordLoginCallback
              .getPassword());
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean stop() {
    controllerFrame.setVisible(false);
    ApplicationContext.terminate();
    return true;
  }

  private void displayModule(String moduleId) {
    if (moduleInternalFrames == null) {
      moduleInternalFrames = new HashMap<String, ULCInternalFrame>();
    }
    ULCInternalFrame moduleInternalFrame = moduleInternalFrames.get(moduleId);
    if (moduleInternalFrame == null) {
      IModuleDescriptor moduleDescriptor = getModuleDescriptor(moduleId);
      IView<ULCComponent> moduleView = createModuleView(moduleId,
          moduleDescriptor);
      moduleInternalFrame = createULCInternalFrame(moduleView);
      moduleInternalFrame.setFrameIcon(getIconFactory().getIcon(
          moduleDescriptor.getIconImageURL(), IIconFactory.SMALL_ICON_SIZE));
      moduleInternalFrames.put(moduleId, moduleInternalFrame);
      controllerFrame.getContentPane().add(moduleInternalFrame);
      getMvcBinder().bind(moduleView.getConnector(),
          getBackendController().getModuleConnector(moduleId));
      moduleInternalFrame.pack();
    }
    moduleInternalFrame.setVisible(true);
    if (moduleInternalFrame.isIcon()) {
      moduleInternalFrame.setIcon(false);
    }
    moduleInternalFrame.setMaximum(true);
    moduleInternalFrame.moveToFront();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getSelectedModuleId() {
    for (Map.Entry<String, ULCInternalFrame> moduleIdAndFrame : moduleInternalFrames
        .entrySet()) {
      if (moduleIdAndFrame.getValue() != null
          && moduleIdAndFrame.getValue().isSelected()) {
        return moduleIdAndFrame.getKey();
      }
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setSelectedModuleId(@SuppressWarnings("unused")
  String selectedModuleId) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected IIconFactory<ULCIcon> getIconFactory() {
    return (IIconFactory<ULCIcon>) super.getIconFactory();
  }

  private ULCFrame createControllerFrame() {
    ULCFrame frame = new ULCFrame(getLabelTranslator().getTranslation(
        getName(), getLocale()));
    frame.setContentPane(new ULCDesktopPane());
    frame.setIconImage(getIconFactory().getIcon(getIconImageURL(),
        IIconFactory.SMALL_ICON_SIZE));
    frame.setDefaultCloseOperation(IWindowConstants.DO_NOTHING_ON_CLOSE);
    frame.setMenuBar(getApplicationMenuBar());
    frame.addWindowListener(new IWindowListener() {

      private static final long serialVersionUID = -7845554617417316256L;

      public void windowClosing(@SuppressWarnings("unused")
      WindowEvent event) {
        stop();
      }
    });
    return frame;
  }

  private ULCMenuBar getApplicationMenuBar() {
    ULCMenuBar applicationMenuBar = new ULCMenuBar();
    applicationMenuBar.add(getModulesMenu());
    return applicationMenuBar;
  }

  private ULCMenu getModulesMenu() {
    ULCMenu modulesMenu = new ULCMenu(getLabelTranslator().getTranslation(
        "Modules", getLocale()));
    modulesMenu.setIcon(getIconFactory().getIcon(getModulesMenuIconImageUrl(),
        IIconFactory.SMALL_ICON_SIZE));
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
      putValue(com.ulcjava.base.application.IAction.NAME, getLabelTranslator()
          .getTranslation(moduleDescriptor.getName(), getLocale()));
      putValue(com.ulcjava.base.application.IAction.SHORT_DESCRIPTION,
          getDescriptionTranslator().getTranslation(
              moduleDescriptor.getDescription(), getLocale()));
      putValue(com.ulcjava.base.application.IAction.SMALL_ICON,
          getIconFactory().getIcon(moduleDescriptor.getIconImageURL(),
              IIconFactory.TINY_ICON_SIZE));
    }

    /**
     * displays the selected module.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(@SuppressWarnings("unused")
    ActionEvent e) {
      displayModule(moduleId);
    }
  }

  private final class QuitAction extends AbstractAction {

    private static final long serialVersionUID = -1476651758085260422L;

    /**
     * Constructs a new <code>ModuleSelectionAction</code> instance.
     */
    public QuitAction() {
      putValue(com.ulcjava.base.application.IAction.NAME, getLabelTranslator()
          .getTranslation("QUIT", getLocale()));
      putValue(com.ulcjava.base.application.IAction.SHORT_DESCRIPTION,
          getDescriptionTranslator().getTranslation("QUIT", getLocale()));
    }

    /**
     * displays the selected module.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(@SuppressWarnings("unused")
    ActionEvent e) {
      stop();
    }
  }

  /**
   * Creates a new ULCInternalFrame and populates it with a view.
   * 
   * @param view
   *          the view to be set into the internal frame.
   * @return the constructed internal frame.
   */
  private ULCInternalFrame createULCInternalFrame(IView<ULCComponent> view) {
    ULCInternalFrame internalFrame = new ULCInternalFrame(getLabelTranslator()
        .getTranslation(view.getDescriptor().getName(), getLocale()), true,
        false, true, true);
    internalFrame.getContentPane().add(view.getPeer());
    internalFrame.setDefaultCloseOperation(IWindowConstants.HIDE_ON_CLOSE);
    return internalFrame;
  }
}
