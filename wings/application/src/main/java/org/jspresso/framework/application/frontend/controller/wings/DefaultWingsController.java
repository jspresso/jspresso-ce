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
package org.jspresso.framework.application.frontend.controller.wings;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.swing.Action;

import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.frontend.controller.AbstractFrontendController;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.gui.wings.components.SErrorDialog;
import org.jspresso.framework.util.exception.BusinessException;
import org.jspresso.framework.util.html.HtmlHelper;
import org.jspresso.framework.util.lang.ObjectUtils;
import org.jspresso.framework.util.wings.WingsUtil;
import org.jspresso.framework.view.IActionFactory;
import org.jspresso.framework.view.IIconFactory;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.ActionList;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.springframework.dao.ConcurrencyFailureException;
import org.wings.SBorderLayout;
import org.wings.SBoxLayout;
import org.wings.SButton;
import org.wings.SCardLayout;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SContainer;
import org.wings.SDialog;
import org.wings.SDimension;
import org.wings.SFrame;
import org.wings.SIcon;
import org.wings.SMenu;
import org.wings.SMenuBar;
import org.wings.SMenuItem;
import org.wings.SOptionPane;
import org.wings.SPanel;
import org.wings.SSpacer;
import org.wings.border.SEmptyBorder;
import org.wings.border.SLineBorder;
import org.wings.script.JavaScriptListener;
import org.wings.script.ScriptListener;
import org.wings.session.SessionManager;

/**
 * Default implementation of a wings frontend controller. This implementation is
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
public class DefaultWingsController extends
    AbstractFrontendController<SComponent, SIcon, Action> {

  private SPanel                  cardPanel;

  private SFrame                  controllerFrame;
  private String                  frameHeight      = "768px";

  private String                  frameWidth       = "95%";
  private Set<String>             workspaceViews;
  private static final SDimension DIALOG_DIMENSION = new SDimension("800px",
                                                       "600px");

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
   * Sets the frameHeight.
   * 
   * @param frameHeight
   *          the frameHeight to set.
   */
  public void setFrameHeight(String frameHeight) {
    this.frameHeight = frameHeight;
  }

  /**
   * Sets the frameWidth.
   * 
   * @param frameWidth
   *          the frameWidth to set.
   */
  public void setFrameWidth(String frameWidth) {
    this.frameWidth = frameWidth;
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
      execute(getStartupAction(), getInitialActionContext());
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
  public void displayWorkspace(String workspaceName) {
    if (!ObjectUtils.equals(workspaceName, getSelectedWorkspaceName())) {
      super.displayWorkspace(workspaceName);
      if (workspaceViews == null) {
        workspaceViews = new HashSet<String>();
      }
      if (!workspaceViews.contains(workspaceName)) {
        IViewDescriptor workspaceViewDescriptor = getWorkspace(workspaceName)
            .getViewDescriptor();
        IValueConnector workspaceConnector = getBackendController()
            .getWorkspaceConnector(workspaceName);
        IView<SComponent> workspaceView = createWorkspaceView(workspaceName,
            workspaceViewDescriptor, (Workspace) workspaceConnector
                .getConnectorValue());
        // getViewFactory().decorateWithTitle(moduleView, getLocale());
        workspaceViews.add(workspaceName);
        cardPanel.add(workspaceView.getPeer(), workspaceName);
        getMvcBinder().bind(workspaceView.getConnector(), workspaceConnector);
      }
      ((SCardLayout) cardPanel.getLayout()).show(workspaceName);
      updateFrameTitle();
    }
  }

  private SMenu createMenu(ActionList actionList) {
    SMenu menu = new SMenu(actionList.getI18nName(getTranslationProvider(),
        getLocale()));
    if (actionList.getDescription() != null) {
      menu.setToolTipText(actionList.getI18nDescription(
          getTranslationProvider(), getLocale())
          + IActionFactory.TOOLTIP_ELLIPSIS);
    }
    menu.setIcon(getIconFactory().getIcon(actionList.getIconImageURL(),
        IIconFactory.SMALL_ICON_SIZE));
    for (SMenuItem menuItem : createMenuItems(menu, actionList)) {
      menu.add(menuItem);
    }
    return menu;
  }

  private List<SMenuItem> createMenuItems(SMenu menu, ActionList actionList) {
    List<SMenuItem> menuItems = new ArrayList<SMenuItem>();
    for (IDisplayableAction action : actionList.getActions()) {
      menuItems.add(createMenuItem(menu, action));
    }
    return menuItems;
  }

  private SMenuItem createMenuItem(SMenu menu, IDisplayableAction action) {
    return new SMenuItem(getViewFactory().getActionFactory().createAction(
        action, this, menu, null, null, getLocale()));
  }

  private List<SMenu> createActionMenus() {
    return createMenus(getActionMap(), false);
  }

  private SMenuBar createApplicationMenuBar() {
    SMenuBar applicationMenuBar = new SMenuBar();
    applicationMenuBar.setBorder(new SLineBorder(Color.LIGHT_GRAY));
    List<SMenu> workspaceMenus = createWorkspacesMenus();
    if (workspaceMenus != null) {
      for (SMenu workspaceMenu : workspaceMenus) {
        applicationMenuBar.add(workspaceMenu);
      }
    }
    List<SMenu> actionMenus = createActionMenus();
    if (actionMenus != null) {
      for (SMenu actionMenu : actionMenus) {
        applicationMenuBar.add(actionMenu);
      }
    }
    List<SMenu> helpActionMenus = createHelpActionMenus();
    if (helpActionMenus != null) {
      for (SMenu helpActionMenu : helpActionMenus) {
        helpActionMenu.setHorizontalAlignment(SConstants.RIGHT_ALIGN);
        applicationMenuBar.add(helpActionMenu);
      }
    }
    return applicationMenuBar;
  }

  private SFrame createControllerFrame() {
    SFrame frame = new SFrame();
    frame.setPreferredSize(new SDimension(frameWidth, frameHeight/*
                                                                  * WingsUtil.FULL_DIM_PERCENT
                                                                  */));
    cardPanel = new SPanel(new SCardLayout());
    cardPanel.setPreferredSize(SDimension.FULLAREA);
    frame.getContentPane().add(createApplicationMenuBar(), SBorderLayout.NORTH);
    frame.getContentPane().add(cardPanel, SBorderLayout.CENTER);
    frame.getContentPane().setPreferredSize(SDimension.FULLAREA);
    return frame;
  }

  private List<SMenu> createHelpActionMenus() {
    return createMenus(getHelpActions(), true);
  }

  @SuppressWarnings("null")
  private List<SMenu> createMenus(ActionMap actionMap, boolean useSeparator) {
    List<SMenu> menus = new ArrayList<SMenu>();
    if (actionMap != null) {
      SMenu menu = null;
      for (ActionList actionList : actionMap.getActionLists()) {
        if (!useSeparator || menus.isEmpty()) {
          menu = createMenu(actionList);
          menus.add(menu);
        } else {
          menu.addSeparator();
          // SMenuItem separator = new SMenuItem("---------");
          // separator.setBorder(new SLineBorder(1));
          // menu.add(separator);

          for (SMenuItem menuItem : createMenuItems(menu, actionList)) {
            menu.add(menuItem);
          }
        }
      }
    }
    return menus;
  }

  private List<SMenu> createWorkspacesMenus() {
    return createMenus(createWorkspaceActionMap(), true);
  }

  private void displayControllerFrame() {
    controllerFrame = createControllerFrame();
    updateFrameTitle();
    controllerFrame.setVisible(true);
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
  @Override
  public void displayModalDialog(SComponent mainView, List<Action> actions,
      String title, SComponent sourceComponent, Map<String, Object> context) {
    super
        .displayModalDialog(mainView, actions, title, sourceComponent, context);
    final SDialog dialog;
    SFrame window = sourceComponent.getParentFrame();
    dialog = new SDialog(window, title, true);
    dialog.setDraggable(true);

    SPanel buttonBox = new SPanel(new SBoxLayout(dialog, SBoxLayout.X_AXIS));
    buttonBox.setBorder(new SEmptyBorder(new java.awt.Insets(5, 10, 5, 10)));

    SButton defaultButton = null;
    for (Action action : actions) {
      SButton actionButton = new SButton();
      actionButton.setAction(action);
      buttonBox.add(actionButton);
      buttonBox.add(new SSpacer(10, 10));
      if (defaultButton == null) {
        defaultButton = actionButton;
      }
    }
    SPanel actionPanel = new SPanel(new SBorderLayout());
    actionPanel.add(buttonBox, SBorderLayout.EAST);

    SPanel mainPanel = new SPanel(new SBorderLayout());
    mainPanel.add(mainView, SBorderLayout.CENTER);
    mainPanel.add(actionPanel, SBorderLayout.SOUTH);
    mainPanel.setPreferredSize(DIALOG_DIMENSION);
    dialog.add(mainPanel);
    if (defaultButton != null) {
      dialog.setDefaultButton(defaultButton);
    }
    dialog.setVisible(true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void disposeModalDialog(SComponent sourceWidget,
      Map<String, Object> context) {
    super.disposeModalDialog(sourceWidget, context);
    SContainer actionWindow = WingsUtil.getVisibleWindow(sourceWidget);
    if (actionWindow instanceof SDialog) {
      ((SDialog) actionWindow).dispose();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayUrl(String urlSpec) {
    ScriptListener listener = new JavaScriptListener(null, null,
        "wingS.util.openLink('download','" + urlSpec + "',null);");
    SessionManager.getSession().getScriptManager().addScriptListener(listener);
  }
}
