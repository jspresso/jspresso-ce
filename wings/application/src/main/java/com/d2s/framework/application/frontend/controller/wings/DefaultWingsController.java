/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
import org.wings.SConstants;
import org.wings.SContainer;
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
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.gui.wings.components.SErrorDialog;
import com.d2s.framework.util.exception.BusinessException;
import com.d2s.framework.util.html.HtmlHelper;
import com.d2s.framework.util.wings.WingsUtil;
import com.d2s.framework.view.IActionFactory;
import com.d2s.framework.view.IIconFactory;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.IViewFactory;
import com.d2s.framework.view.action.ActionList;
import com.d2s.framework.view.action.ActionMap;
import com.d2s.framework.view.action.IDisplayableAction;
import com.d2s.framework.view.descriptor.IViewDescriptor;

/**
 * Default implementation of a wings frontend controller. This implementation is
 * usable "as-is".
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultWingsController extends
    AbstractFrontendController<SComponent, SIcon, Action> {

  private SPanel      cardPanel;

  private SFrame      controllerFrame;
  private Set<String> moduleViews;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean handleException(Throwable ex, Map<String, Object> context) {
    if (super.handleException(ex, context)) {
      return true;
    }
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
    return true;
  }

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
  protected CallbackHandler createLoginCallbackHandler() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void displayModule(String moduleName) {
    if (moduleViews == null) {
      moduleViews = new HashSet<String>();
    }
    if (!moduleViews.contains(moduleName)) {
      IViewDescriptor moduleViewDescriptor = getModule(moduleName)
          .getViewDescriptor();
      IValueConnector moduleConnector = getBackendController()
          .getModuleConnector(moduleName);
      IView<SComponent> moduleView = createModuleView(moduleName,
          moduleViewDescriptor, (Module) moduleConnector.getConnectorValue());
      SContainer moduleInternalFrame = createInternalFrame(moduleView);
      moduleViews.add(moduleName);
      cardPanel.add(moduleInternalFrame, moduleName);
      getMvcBinder().bind(moduleView.getConnector(), moduleConnector);
    }
    setSelectedModuleName(moduleName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setSelectedModuleName(String moduleName) {
    super.setSelectedModuleName(moduleName);
    ((SCardLayout) cardPanel.getLayout()).show(moduleName);
    updateFrameTitle();
  }

  private SMenu createActionMenu(ActionList actionList) {
    SMenu menu = new SMenu(actionList.getI18nName(getTranslationProvider(),
        getLocale()));
    if (actionList.getDescription() != null) {
      menu.setToolTipText(actionList.getI18nDescription(
          getTranslationProvider(), getLocale())
          + IActionFactory.TOOLTIP_ELLIPSIS);
    }
    menu.setIcon(getIconFactory().getIcon(actionList.getIconImageURL(),
        IIconFactory.SMALL_ICON_SIZE));
    for (IDisplayableAction action : actionList.getActions()) {
      menu.add(new SMenuItem(getViewFactory().getActionFactory().createAction(
          action, this, menu, null, null, getLocale())));
    }
    return menu;
  }

  private List<SMenu> createActionMenus() {
    return createMenus(getActions());
  }

  private List<SMenu> createHelpActionMenus() {
    return createMenus(getHelpActions());
  }

  private List<SMenu> createMenus(ActionMap actionMap) {
    List<SMenu> menus = new ArrayList<SMenu>();
    if (actionMap != null) {
      for (ActionList actionList : actionMap.getActionLists()) {
        SMenu menu = createActionMenu(actionList);
        menus.add(menu);
      }
    }
    return menus;
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
    List<SMenu> helpActionMenus = createHelpActionMenus();
    if (helpActionMenus != null) {
      for (SMenu helpActionMenu : helpActionMenus) {
        // helpActionMenu.setHorizontalAlignment(SConstants.RIGHT_ALIGN);
        applicationMenuBar.add(helpActionMenu);
      }
    }
    return applicationMenuBar;
  }

  private SFrame createControllerFrame() {
    SFrame frame = new SFrame();
    cardPanel = new SPanel(new SCardLayout());
    cardPanel.setPreferredSize(WingsUtil.FULLAREA);
    SPanel contentPane = new SPanel(new SBorderLayout());
    frame.setContentPane(contentPane);
    frame.getContentPane().add(createApplicationMenuBar(), SBorderLayout.NORTH);
    frame.getContentPane().add(cardPanel, SBorderLayout.CENTER);
    return frame;
  }

  /**
   * Creates a new SInternalFrame and populates it with a view.
   * 
   * @param view
   *            the view to be set into the internal frame.
   * @return the constructed internal frame.
   */
  private SContainer createInternalFrame(IView<SComponent> view) {
    SInternalFrame internalFrame = new SInternalFrame();
    internalFrame.setTitle(view.getDescriptor().getI18nName(
        getTranslationProvider(), getLocale()));
    internalFrame.setClosable(false);
    internalFrame.setMaximizable(false);
    internalFrame.setIconifyable(false);
    internalFrame.setClosable(false);
    internalFrame.getContentPane().setLayout(new SBorderLayout());
    internalFrame.setIcon(getIconFactory().getIcon(
        view.getDescriptor().getIconImageURL(), IIconFactory.SMALL_ICON_SIZE));
    SForm frameForm = new SForm();
    frameForm.add(view.getPeer());
    frameForm.setPreferredSize(WingsUtil.FULLAREA);
    internalFrame.getContentPane().add(frameForm, SBorderLayout.CENTER);
    internalFrame.setVerticalAlignment(SConstants.TOP_ALIGN);
    internalFrame.setHorizontalAlignment(SConstants.LEFT_ALIGN);
    return internalFrame;
  }

  private SMenu createModulesMenu() {
    SMenu modulesMenu = new SMenu(getTranslationProvider().getTranslation(
        "modules", getLocale()));
    modulesMenu.setIcon(getIconFactory().getIcon(getModulesMenuIconImageUrl(),
        IIconFactory.SMALL_ICON_SIZE));
    for (String moduleName : getModuleNames()) {
      IViewDescriptor moduleViewDescriptor = getModule(moduleName)
          .getViewDescriptor();
      SMenuItem moduleMenuItem = new SMenuItem(new ModuleSelectionAction(
          moduleName, moduleViewDescriptor));
      modulesMenu.add(moduleMenuItem);
    }
    SMenuItem separator = new SMenuItem("---------");
    separator.setBorder(new SLineBorder(1));
    modulesMenu.add(separator);

    modulesMenu.add(new SMenuItem(new QuitAction()));
    return modulesMenu;
  }

  private void displayControllerFrame() {
    controllerFrame = createControllerFrame();
    updateFrameTitle();
    controllerFrame.setVisible(true);
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
