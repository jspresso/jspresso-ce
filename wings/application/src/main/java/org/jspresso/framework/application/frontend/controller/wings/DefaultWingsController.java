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

import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.frontend.controller.AbstractFrontendController;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.gui.wings.components.SErrorDialog;
import org.jspresso.framework.util.exception.BusinessException;
import org.jspresso.framework.util.html.HtmlHelper;
import org.jspresso.framework.view.IActionFactory;
import org.jspresso.framework.view.IIconFactory;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.IViewFactory;
import org.jspresso.framework.view.action.ActionList;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.springframework.dao.ConcurrencyFailureException;
import org.wings.SBorderLayout;
import org.wings.SCardLayout;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SDimension;
import org.wings.SFrame;
import org.wings.SIcon;
import org.wings.SMenu;
import org.wings.SMenuBar;
import org.wings.SMenuItem;
import org.wings.SOptionPane;
import org.wings.SPanel;
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

  private SPanel      cardPanel;

  private SFrame      controllerFrame;
  private String      frameHeight = "768px";

  private String      frameWidth  = "95%";
  private Set<String> workspaceViews;

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
   *            the frameHeight to set.
   */
  public void setFrameHeight(String frameHeight) {
    this.frameHeight = frameHeight;
  }

  /**
   * Sets the frameWidth.
   * 
   * @param frameWidth
   *            the frameWidth to set.
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
  protected void displayWorkspace(String workspaceName) {
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
    setSelectedWorkspaceName(workspaceName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setSelectedWorkspaceName(String workspaceName) {
    super.setSelectedWorkspaceName(workspaceName);
    ((SCardLayout) cardPanel.getLayout()).show(workspaceName);
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
    return createMenus(getActionMap());
  }

  private SMenuBar createApplicationMenuBar() {
    SMenuBar applicationMenuBar = new SMenuBar();
    applicationMenuBar.setBorder(new SLineBorder(Color.LIGHT_GRAY));
    applicationMenuBar.add(createWorkspacesMenu());
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
    frame
        .setPreferredSize(new SDimension(frameWidth, frameHeight/* WingsUtil.FULL_DIM_PERCENT */));
    cardPanel = new SPanel(new SCardLayout());
    cardPanel.setPreferredSize(SDimension.FULLAREA);
    frame.getContentPane().add(createApplicationMenuBar(), SBorderLayout.NORTH);
    frame.getContentPane().add(cardPanel, SBorderLayout.CENTER);
    frame.getContentPane().setPreferredSize(SDimension.FULLAREA);
    return frame;
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

  private SMenu createWorkspacesMenu() {
    SMenu workspacesMenu = new SMenu(getTranslationProvider().getTranslation(
        "workspaces", getLocale()));
    workspacesMenu.setIcon(getIconFactory().getIcon(
        getWorkspacesMenuIconImageUrl(), IIconFactory.SMALL_ICON_SIZE));
    for (String workspaceName : getWorkspaceNames()) {
      IViewDescriptor workspaceViewDescriptor = getWorkspace(workspaceName)
          .getViewDescriptor();
      SMenuItem workspaceMenuItem = new SMenuItem(new WorkspaceSelectionAction(
          workspaceName, workspaceViewDescriptor));
      workspacesMenu.add(workspaceMenuItem);
    }
    SMenuItem separator = new SMenuItem("---------");
    separator.setBorder(new SLineBorder(1));
    workspacesMenu.add(separator);

    workspacesMenu.add(new SMenuItem(new QuitAction()));
    return workspacesMenu;
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

  private final class QuitAction extends AbstractAction {

    private static final long serialVersionUID = -5797994634301619085L;

    /**
     * Constructs a new <code>QuitAction</code> instance.
     */
    public QuitAction() {
      putValue(Action.NAME, getTranslationProvider().getTranslation(
          "quit.name", getLocale()));
      putValue(Action.SHORT_DESCRIPTION, getTranslationProvider()
          .getTranslation("quit.description", getLocale()));
    }

    /**
     * Quits the application.
     * <p>
     * {@inheritDoc}
     */
    public void actionPerformed(@SuppressWarnings("unused")
    ActionEvent e) {
      stop();
    }
  }

  private final class WorkspaceSelectionAction extends AbstractAction {

    private static final long serialVersionUID = 3469745193806038352L;
    private String            workspaceName;

    /**
     * Constructs a new <code>WorkspaceSelectionAction</code> instance.
     * 
     * @param workspaceName
     * @param workspaceViewDescriptor
     */
    public WorkspaceSelectionAction(String workspaceName,
        IViewDescriptor workspaceViewDescriptor) {
      this.workspaceName = workspaceName;
      putValue(Action.NAME, workspaceViewDescriptor.getI18nName(
          getTranslationProvider(), getLocale()));
      putValue(Action.SHORT_DESCRIPTION, workspaceViewDescriptor
          .getI18nDescription(getTranslationProvider(), getLocale())
          + IViewFactory.TOOLTIP_ELLIPSIS);
      putValue(Action.SMALL_ICON, getIconFactory().getIcon(
          workspaceViewDescriptor.getIconImageURL(),
          IIconFactory.TINY_ICON_SIZE));
    }

    /**
     * displays the selected workspace.
     * <p>
     * {@inheritDoc}
     */
    public void actionPerformed(@SuppressWarnings("unused")
    ActionEvent e) {
      try {
        getBackendController().checkWorkspaceAccess(workspaceName);
        displayWorkspace(workspaceName);
      } catch (SecurityException ex) {
        handleException(ex, null);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayUrl(String urlSpec) {
    ScriptListener listener = new JavaScriptListener(null, null,
        "wingS.util.openLink('download','" + urlSpec + "',null);");
    SessionManager.getSession().getScriptManager()
        .addScriptListener(listener);
  }
}
