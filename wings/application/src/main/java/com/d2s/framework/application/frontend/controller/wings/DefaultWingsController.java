/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.controller.wings;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.swing.AbstractAction;
import javax.swing.Action;

import org.springframework.dao.ConcurrencyFailureException;
import org.wings.SBorderLayout;
import org.wings.SCardLayout;
import org.wings.SComponent;
import org.wings.SDimension;
import org.wings.SForm;
import org.wings.SFrame;
import org.wings.SIcon;
import org.wings.SInternalFrame;
import org.wings.SMenu;
import org.wings.SMenuBar;
import org.wings.SMenuItem;
import org.wings.SOptionPane;
import org.wings.SPanel;
import org.wings.border.SLineBorder;
import org.wings.session.SessionManager;

import com.d2s.framework.application.backend.IBackendController;
import com.d2s.framework.application.frontend.controller.AbstractFrontendController;
import com.d2s.framework.application.model.Module;
import com.d2s.framework.application.view.descriptor.IModuleDescriptor;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.gui.wings.components.SErrorDialog;
import com.d2s.framework.util.exception.BusinessException;
import com.d2s.framework.util.html.HtmlHelper;
import com.d2s.framework.view.IIconFactory;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.IViewFactory;
import com.d2s.framework.view.action.IDisplayableAction;

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

  private SFrame      controllerFrame;

  private SPanel      cardPanel;
  private Set<String> moduleViews;

  /**
   * Creates the initial view from the root view descriptor, then a SFrame
   * containing this view and presents it to the user.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean start(IBackendController backendController, Locale locale) {
    if (super.start(backendController, locale)) {
      loginSuccess((Subject) SessionManager.getSession().getServletRequest()
          .getSession().getAttribute("SUBJECT"));
      displayControllerFrame();
      return true;
    }
    return false;
  }

  private void displayControllerFrame() {
    controllerFrame = createControllerFrame();
    updateFrameTitle();
    // Ajax calendar bug fix
    controllerFrame.setUpdateEnabled(false);
    controllerFrame.setVisible(true);
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
    if (moduleViews == null) {
      moduleViews = new HashSet<String>();
    }
    if (!moduleViews.contains(moduleId)) {
      IModuleDescriptor moduleDescriptor = getModuleDescriptor(moduleId);
      IValueConnector moduleConnector = getBackendController()
          .getModuleConnector(moduleId);
      IView<SComponent> moduleView = createModuleView(moduleId,
          moduleDescriptor, (Module) moduleConnector.getConnectorValue());
      SInternalFrame moduleInternalFrame = createInternalFrame(moduleView);
      moduleInternalFrame.setIcon(getIconFactory().getIcon(
          moduleDescriptor.getIconImageURL(), IIconFactory.SMALL_ICON_SIZE));
      moduleViews.add(moduleId);
      cardPanel.add(moduleInternalFrame, moduleId);
      getMvcBinder().bind(moduleView.getConnector(), moduleConnector);
      if (!controllerFrame.isUpdateEnabled()) {
        controllerFrame.setUpdateEnabled(true);
      }
    }
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

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setSelectedModuleId(String moduleId) {
    super.setSelectedModuleId(moduleId);
    ((SCardLayout) cardPanel.getLayout()).show(moduleId);
    updateFrameTitle();
  }

  private SFrame createControllerFrame() {
    SFrame frame = new SFrame();
    cardPanel = new SPanel(new SCardLayout());
    cardPanel.setPreferredSize(SDimension.FULLAREA);
    SPanel contentPane = new SPanel(new SBorderLayout());
    frame.setContentPane(contentPane);
    frame.getContentPane().add(createApplicationMenuBar(), SBorderLayout.NORTH);
    frame.getContentPane().add(cardPanel, SBorderLayout.CENTER);
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
    for (String moduleId : getModuleIds()) {
      IModuleDescriptor moduleDescriptor = getModuleDescriptor(moduleId);
      SMenuItem moduleMenuItem = new SMenuItem(new ModuleSelectionAction(
          moduleId, moduleDescriptor));
      modulesMenu.add(moduleMenuItem);
    }
    SMenuItem separator = new SMenuItem("---------");
    separator.setBorder(new SLineBorder(1));
    modulesMenu.add(separator);

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
  private SInternalFrame createInternalFrame(IView<SComponent> view) {
    SInternalFrame internalFrame = new SInternalFrame();
    internalFrame.setTitle(view.getDescriptor().getI18nName(
        getTranslationProvider(), getLocale()));
    internalFrame.setClosable(false);
    internalFrame.setMaximizable(false);
    internalFrame.setIconifyable(false);
    internalFrame.setClosable(false);
    internalFrame.getContentPane().setLayout(new SBorderLayout());
    SForm frameForm = new SForm();
    frameForm.add(view.getPeer());
    internalFrame.getContentPane().add(frameForm, SBorderLayout.CENTER);
    return internalFrame;
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
      SOptionPane.showMessageDialog(sourceComponent, String.valueOf(ex
          .getMessage()), getTranslationProvider().getTranslation("error",
          getLocale()), SOptionPane.ERROR_MESSAGE);
      SErrorDialog dialog = SErrorDialog.createInstance(sourceComponent,
          getTranslationProvider(), getLocale());
      dialog.setMessageIcon(getIconFactory().getErrorIcon(
          IIconFactory.MEDIUM_ICON_SIZE));
      dialog.setTitle(getTranslationProvider().getTranslation("error",
          getLocale()));
      dialog.setMessage(HtmlHelper.emphasis(ex.getLocalizedMessage()));
      dialog.setDetails(ex);
      dialog.setVisible(true);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected CallbackHandler createLoginCallbackHandler() {
    return null;
  }
}
