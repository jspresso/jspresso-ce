/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.controller.wings;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
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

import org.springframework.dao.ConcurrencyFailureException;
import org.wings.SBorderLayout;
import org.wings.SComponent;
import org.wings.SDesktopPane;
import org.wings.SFrame;
import org.wings.SIcon;
import org.wings.SInternalFrame;
import org.wings.SMenu;
import org.wings.SMenuBar;
import org.wings.SMenuItem;
import org.wings.SOptionPane;
import org.wings.SSeparator;
import org.wings.event.SInternalFrameAdapter;
import org.wings.event.SInternalFrameEvent;

import com.d2s.framework.action.IAction;
import com.d2s.framework.application.backend.IBackendController;
import com.d2s.framework.application.frontend.controller.AbstractFrontendController;
import com.d2s.framework.application.model.Module;
import com.d2s.framework.application.view.descriptor.IModuleDescriptor;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.security.wings.DialogCallbackHandler;
import com.d2s.framework.util.exception.BusinessException;
import com.d2s.framework.util.html.HtmlHelper;
import com.d2s.framework.util.swing.SwingUtil;
import com.d2s.framework.view.IIconFactory;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.IViewFactory;
import com.d2s.framework.view.action.IDisplayableAction;

import foxtrot.Job;

/**
 * Default implementation of a wings frontend controller. This implementation is
 * usable "as-is".
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultWingsController extends
    AbstractFrontendController<SComponent, SIcon, Action> {

  private SFrame                      controllerFrame;
  private Map<String, SInternalFrame> moduleInternalFrames;

  /**
   * Creates the initial view from the root view descriptor, then a SFrame
   * containing this view and presents it to the user.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean start(IBackendController backendController, Locale locale) {
    if (super.start(backendController, locale)) {
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

  private void displayControllerFrame() {
    controllerFrame = createControllerFrame();
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

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean stop() {
    if (super.stop()) {
      controllerFrame.getSession().exit();
      return true;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void displayModule(String moduleId) {
    if (moduleInternalFrames == null) {
      moduleInternalFrames = new HashMap<String, SInternalFrame>();
    }
    SInternalFrame moduleInternalFrame = moduleInternalFrames.get(moduleId);
    if (moduleInternalFrame == null) {
      IModuleDescriptor moduleDescriptor = getModuleDescriptor(moduleId);
      IValueConnector moduleConnector = getBackendController()
          .getModuleConnector(moduleId);
      IView<SComponent> moduleView = createModuleView(moduleId,
          moduleDescriptor, (Module) moduleConnector.getConnectorValue());
      moduleInternalFrame = createJInternalFrame(moduleView);
      moduleInternalFrame.setIcon(getIconFactory().getIcon(
          moduleDescriptor.getIconImageURL(), IIconFactory.SMALL_ICON_SIZE));
      moduleInternalFrame
          .addInternalFrameListener(new ModuleInternalFrameListener(moduleId));
      moduleInternalFrames.put(moduleId, moduleInternalFrame);
      controllerFrame.getContentPane().add(moduleInternalFrame);
      getMvcBinder().bind(moduleView.getConnector(), moduleConnector);
    }
    moduleInternalFrame.setVisible(true);
    if (moduleInternalFrame.isIconified()) {
      moduleInternalFrame.setIconified(false);
    }
    moduleInternalFrame.setMaximized(true);
    setSelectedModuleId(moduleId);
    super.displayModule(moduleId);
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

  private final class ModuleInternalFrameListener extends SInternalFrameAdapter {

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
    public void internalFrameMaximized(@SuppressWarnings("unused")
    SInternalFrameEvent e) {
      setSelectedModuleId(moduleId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void internalFrameDeiconified(@SuppressWarnings("unused")
    SInternalFrameEvent e) {
      setSelectedModuleId(moduleId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void internalFrameOpened(@SuppressWarnings("unused")
    SInternalFrameEvent e) {
      setSelectedModuleId(moduleId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void internalFrameIconified(@SuppressWarnings("unused")
    SInternalFrameEvent e) {
      setSelectedModuleId(null);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setSelectedModuleId(String moduleId) {
    super.setSelectedModuleId(moduleId);
    for (Map.Entry<String, SInternalFrame> moduleEntry : moduleInternalFrames
        .entrySet()) {
      SInternalFrame moduleFrame = moduleEntry.getValue();
      if (moduleId != null && moduleId.equals(moduleEntry.getKey())) {
        if (moduleFrame.isIconified()) {
          moduleFrame.setIconified(false);
        }
      } else {
        moduleFrame.setIconified(true);
      }
    }
    updateFrameTitle();
  }

  private SFrame createControllerFrame() {
    SFrame frame = new SFrame();
    frame.setContentPane(new SDesktopPane());
    frame.getContentPane().setLayout(new SBorderLayout());
    frame.getContentPane().add(createApplicationMenuBar(), SBorderLayout.NORTH);
    return frame;
  }

  private SMenuBar createApplicationMenuBar() {
    SMenuBar applicationMenuBar = new SMenuBar();
    applicationMenuBar.add(createModulesMenu());
    List<SMenu> actionMenus = createActionMenus();
    if (actionMenus != null) {
      for (SMenu actionMenu : actionMenus) {
        applicationMenuBar.add(actionMenu);
      }
    }
    return applicationMenuBar;
  }

  private SMenu createModulesMenu() {
    SMenu modulesMenu = new SMenu(getTranslationProvider().getTranslation(
        "modules", getLocale()));
    // modulesMenu.setIcon(getIconFactory().getIcon(getModulesMenuIconImageUrl(),
    // IIconFactory.SMALL_ICON_SIZE));
    for (String moduleId : getModuleIds()) {
      IModuleDescriptor moduleDescriptor = getModuleDescriptor(moduleId);
      SMenuItem moduleMenuItem = new SMenuItem(new ModuleSelectionAction(
          moduleId, moduleDescriptor));
      modulesMenu.add(moduleMenuItem);
    }
    modulesMenu.add(new SSeparator());
    modulesMenu.add(new SMenuItem(new QuitAction()));
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
      putValue(Action.NAME, moduleDescriptor.getI18nName(
          getTranslationProvider(), getLocale()));
      putValue(Action.SHORT_DESCRIPTION, moduleDescriptor.getI18nDescription(
          getTranslationProvider(), getLocale())
          + IViewFactory.TOOLTIP_ELLIPSIS);
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
      try {
        getBackendController().checkModuleAccess(moduleId);
        displayModule(moduleId);
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

  private List<SMenu> createActionMenus() {
    Map<String, List<IDisplayableAction>> actions = getActions();
    List<SMenu> actionMenus = new ArrayList<SMenu>();
    if (actions != null) {
      for (Map.Entry<String, List<IDisplayableAction>> actionList : actions
          .entrySet()) {
        actionMenus.add(createActionMenu(actionList.getKey(), actionList
            .getValue()));
      }
    }
    return actionMenus;
  }

  private SMenu createActionMenu(String titleKey,
      List<IDisplayableAction> actionList) {
    SMenu menu = new SMenu(getTranslationProvider().getTranslation(titleKey,
        getLocale()));
    for (IDisplayableAction action : actionList) {
      menu.add(new SMenuItem(getViewFactory().getActionFactory().createAction(
          action, this, menu, null, null, getLocale())));
    }
    return menu;
  }

  /**
   * Creates a new SInternalFrame and populates it with a view.
   *
   * @param view
   *          the view to be set into the internal frame.
   * @return the constructed internal frame.
   */
  private SInternalFrame createJInternalFrame(IView<SComponent> view) {
    SInternalFrame internalFrame = new SInternalFrame();
    internalFrame.setTitle(view.getDescriptor().getI18nName(
        getTranslationProvider(), getLocale()));
    internalFrame.setClosable(false);
    internalFrame.setMaximizable(true);
    internalFrame.setIconifyable(true);
    internalFrame.getContentPane().add(view.getPeer(), BorderLayout.CENTER);
    return internalFrame;
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

  private boolean protectedExecuteBackend(IAction action,
      Map<String, Object> context) {
    return super.executeBackend(action, context);
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

  private boolean protectedExecuteFrontend(IAction action,
      Map<String, Object> context) {
    return super.executeFrontend(action, context);
  }

  /**
   * {@inheritDoc}
   */
  public void handleException(Throwable ex, @SuppressWarnings("unused")
  Map<String, Object> context) {
    SComponent sourceComponent = controllerFrame;
    if (ex instanceof SecurityException) {
      SOptionPane.showMessageDialog(sourceComponent, HtmlHelper.emphasis(ex
          .getMessage()), getTranslationProvider().getTranslation("error",
          getLocale()), SOptionPane.ERROR_MESSAGE);
    } else if (ex instanceof BusinessException) {
      SOptionPane.showMessageDialog(sourceComponent, HtmlHelper
          .emphasis(((BusinessException) ex).getI18nMessage(
              getTranslationProvider(), getLocale())), getTranslationProvider()
          .getTranslation("error", getLocale()), SOptionPane.ERROR_MESSAGE);
    } else if (ex instanceof ConcurrencyFailureException) {
      SOptionPane.showMessageDialog(sourceComponent, HtmlHelper
          .emphasis(getTranslationProvider().getTranslation(
              "concurrency.error.description", getLocale())),
          getTranslationProvider().getTranslation("error", getLocale()),
          SOptionPane.ERROR_MESSAGE);
    } else {
      ex.printStackTrace();
      SOptionPane.showMessageDialog(sourceComponent, HtmlHelper.emphasis(ex
          .getMessage()), getTranslationProvider().getTranslation("error",
          getLocale()), SOptionPane.ERROR_MESSAGE);
      // FIXME handle detailed error dialogs.
      // JErrorDialog dialog = JErrorDialog.createInstance(sourceComponent,
      // getTranslationProvider(), getLocale());
      // dialog.setMessageIcon(getIconFactory().getErrorIcon(
      // IIconFactory.MEDIUM_ICON_SIZE));
      // dialog.setTitle(getTranslationProvider().getTranslation("error",
      // getLocale()));
      // dialog.setMessage(HtmlHelper.emphasis(ex.getLocalizedMessage()));
      // dialog.setDetails(ex);
      // int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
      // dialog.setSize(8 * screenRes, 3 * screenRes);
      // dialog.pack();
      // dialog.setVisible(true);
    }
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
}
