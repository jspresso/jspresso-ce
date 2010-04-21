/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.frontend.controller.ulc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.frontend.controller.AbstractFrontendController;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.gui.ulc.components.server.ULCErrorDialog;
import org.jspresso.framework.gui.ulc.components.server.ULCExtendedButton;
import org.jspresso.framework.gui.ulc.components.server.ULCExtendedInternalFrame;
import org.jspresso.framework.gui.ulc.components.server.event.ExtendedInternalFrameEvent;
import org.jspresso.framework.gui.ulc.components.server.event.IExtendedInternalFrameListener;
import org.jspresso.framework.util.exception.BusinessException;
import org.jspresso.framework.util.exception.NestedRuntimeException;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.html.HtmlHelper;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.util.lang.ObjectUtils;
import org.jspresso.framework.util.resources.IResource;
import org.jspresso.framework.util.resources.MemoryResource;
import org.jspresso.framework.util.resources.server.ResourceManager;
import org.jspresso.framework.util.security.LoginUtils;
import org.jspresso.framework.util.ulc.UlcUtil;
import org.jspresso.framework.util.ulc.resource.DocumentHelper;
import org.jspresso.framework.view.IActionFactory;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.ActionList;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataIntegrityViolationException;

import com.ulcjava.base.application.ApplicationContext;
import com.ulcjava.base.application.ClientContext;
import com.ulcjava.base.application.IAction;
import com.ulcjava.base.application.ULCAlert;
import com.ulcjava.base.application.ULCBorderLayoutPane;
import com.ulcjava.base.application.ULCBoxLayoutPane;
import com.ulcjava.base.application.ULCButton;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.ULCDesktopPane;
import com.ulcjava.base.application.ULCDialog;
import com.ulcjava.base.application.ULCFiller;
import com.ulcjava.base.application.ULCFrame;
import com.ulcjava.base.application.ULCLabel;
import com.ulcjava.base.application.ULCMenu;
import com.ulcjava.base.application.ULCMenuBar;
import com.ulcjava.base.application.ULCMenuItem;
import com.ulcjava.base.application.ULCSplitPane;
import com.ulcjava.base.application.ULCWindow;
import com.ulcjava.base.application.border.ULCEmptyBorder;
import com.ulcjava.base.application.event.ActionEvent;
import com.ulcjava.base.application.event.WindowEvent;
import com.ulcjava.base.application.event.serializable.IActionListener;
import com.ulcjava.base.application.event.serializable.IWindowListener;
import com.ulcjava.base.application.util.Insets;
import com.ulcjava.base.application.util.ULCIcon;
import com.ulcjava.base.shared.IWindowConstants;

/**
 * This is is the default implementation of the <b>ULC</b> frontend controller.
 * It will implement a 3-tier architecture through the ULC internal
 * architecture. Workspaces are displayed using an MDI UI using internal frames.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultUlcController extends
    AbstractFrontendController<ULCComponent, ULCIcon, IAction> {

  private static final String                   CANCEL_OPTION = "cancel";

  private static final String                   NO_OPTION     = "no";

  private static final String                   OK_OPTION     = "ok";

  private static final String                   YES_OPTION    = "yes";

  private ULCFrame                              controllerFrame;

  private Map<String, ULCExtendedInternalFrame> workspaceInternalFrames;

  /**
   * {@inheritDoc}
   */
  public void displayFlashObject(String swfUrl,
      Map<String, String> flashContext,
      @SuppressWarnings("unused") List<IAction> actions,
      @SuppressWarnings("unused") String title,
      @SuppressWarnings("unused") ULCComponent sourceComponent,
      @SuppressWarnings("unused") Map<String, Object> context,
      Dimension dimension, @SuppressWarnings("unused") boolean reuseCurrent) {
    StringBuffer flashVars = new StringBuffer();
    for (Map.Entry<String, String> flashVar : flashContext.entrySet()) {
      flashVars.append("&").append(flashVar.getKey()).append("=").append(
          flashVar.getValue());
    }
    int width = 600;
    int height = 600;
    if (dimension != null) {
      width = dimension.getWidth();
      height = dimension.getHeight();
    }
    final String htmlText = "<html>"
        + "<body bgcolor=\"#ffffff\">"
        + "<OBJECT classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" "
        + "codebase=http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0\" width=\""
        + width
        + "\" height=\""
        + height
        + "\" id=\"Column3D\" >"
        + "<param name=\"movie\" value=\""
        + swfUrl
        + "\" />"
        + "<param name=\"FlashVars\" value=\""
        + flashVars.toString()
        + "\">"
        + "<embed src=\""
        + swfUrl
        + "\" flashVars=\""
        + flashVars.toString()
        + "\" quality=\"high\" width=\""
        + width
        + "\" height=\""
        + height
        + "\" name=\"Column3D\" type=\"application/x-shockwave-flash\" "
        + "pluginspage=\"http://www.macromedia.com/go/getflashplayer\" />"
        + "</object>" + "</body>" + "</html>";
    IResource resource = new MemoryResource(null, "text/html", htmlText
        .getBytes());
    String resourceId = ResourceManager.getInstance().register(resource);
    try {
      DocumentHelper.showDocument(resourceId);
    } catch (IOException ex) {
      throw new NestedRuntimeException(ex);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void displayModalDialog(ULCComponent mainView, List<IAction> actions,
      String title, ULCComponent sourceComponent, Map<String, Object> context,
      Dimension dimension, boolean reuseCurrent) {
    super.displayModalDialog(context, reuseCurrent);
    final ULCDialog dialog;
    ULCWindow window;
    if (sourceComponent != null) {
      window = UlcUtil.getVisibleWindow(sourceComponent);
    } else {
      window = controllerFrame;
    }
    if (reuseCurrent && window instanceof ULCDialog) {
      dialog = (ULCDialog) window;
      dialog.getContentPane().removeAll();
    } else {
      dialog = new ULCDialog(window, title, true);
    }

    ULCBoxLayoutPane buttonBox = new ULCBoxLayoutPane(
        ULCBoxLayoutPane.LINE_AXIS);
    buttonBox.setBorder(new ULCEmptyBorder(new Insets(5, 10, 5, 10)));

    ULCExtendedButton defaultButton = null;
    for (IAction action : actions) {
      ULCExtendedButton actionButton = new ULCExtendedButton();
      actionButton.setAction(action);
      buttonBox.add(actionButton);
      buttonBox.add(ULCFiller.createHorizontalStrut(10));
      if (defaultButton == null) {
        defaultButton = actionButton;
      }
    }
    ULCBorderLayoutPane actionPanel = new ULCBorderLayoutPane();
    actionPanel.add(buttonBox, ULCBorderLayoutPane.EAST);

    ULCBorderLayoutPane mainPanel = new ULCBorderLayoutPane();
    mainPanel.add(mainView, ULCBorderLayoutPane.CENTER);
    mainPanel.add(actionPanel, ULCBorderLayoutPane.SOUTH);
    dialog.getContentPane().add(mainPanel);
    dialog.setDefaultCloseOperation(IWindowConstants.DO_NOTHING_ON_CLOSE);
    if (defaultButton != null) {
      dialog.getRootPane().setDefaultButton(defaultButton);
    }
    dialog.pack();
    if (dimension != null) {
      dialog.setSize(dimension.getWidth(), dimension.getHeight());
    }
    UlcUtil.centerInParent(dialog);
    dialog.setVisible(true);
  }

  /**
   * {@inheritDoc}
   */
  public void displayUrl(String urlSpec) {
    ClientContext.showDocument(urlSpec);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayWorkspace(String workspaceName) {
    if (!ObjectUtils.equals(workspaceName, getSelectedWorkspaceName())) {
      super.displayWorkspace(workspaceName);
      if (workspaceName != null) {
        if (workspaceInternalFrames == null) {
          workspaceInternalFrames = new HashMap<String, ULCExtendedInternalFrame>();
        }
        ULCExtendedInternalFrame workspaceInternalFrame = workspaceInternalFrames
            .get(workspaceName);
        if (workspaceInternalFrame == null) {
          IViewDescriptor workspaceNavigatorViewDescriptor = getWorkspace(
              workspaceName).getViewDescriptor();
          IValueConnector workspaceConnector = getBackendController()
              .getWorkspaceConnector(workspaceName);
          IView<ULCComponent> workspaceNavigator = createWorkspaceNavigator(
              workspaceName, workspaceNavigatorViewDescriptor);
          IView<ULCComponent> moduleAreaView = createModuleAreaView(workspaceName);
          ULCSplitPane workspaceView = new ULCSplitPane(
              ULCSplitPane.HORIZONTAL_SPLIT);
          workspaceView.add(workspaceNavigator.getPeer());
          workspaceView.add(moduleAreaView.getPeer());
          workspaceInternalFrame = createULCExtendedInternalFrame(
              workspaceView, workspaceNavigatorViewDescriptor.getI18nName(
                  getTranslationProvider(), getLocale()), getIconFactory()
                  .getIcon(workspaceNavigatorViewDescriptor.getIconImageURL(),
                      getIconFactory().getSmallIconSize()));
          workspaceInternalFrame
              .addExtendedInternalFrameListener(new WorkspaceInternalFrameListener(
                  workspaceName));
          workspaceInternalFrames.put(workspaceName, workspaceInternalFrame);
          controllerFrame.getContentPane().add(workspaceInternalFrame);
          getMvcBinder().bind(workspaceNavigator.getConnector(),
              workspaceConnector);
          workspaceInternalFrame.pack();
          workspaceInternalFrame.setSize(controllerFrame.getWidth() - 50,
              controllerFrame.getHeight() - 50);
          workspaceInternalFrame.setMaximum(true);
        }
        workspaceInternalFrame.setVisible(true);
        if (workspaceInternalFrame.isIcon()) {
          workspaceInternalFrame.setIcon(false);
        }
        workspaceInternalFrame.moveToFront();
      }
    }
    updateFrameTitle();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void disposeModalDialog(ULCComponent sourceWidget,
      Map<String, Object> context) {
    super.disposeModalDialog(sourceWidget, context);
    ULCWindow actionWindow = UlcUtil.getVisibleWindow(sourceWidget);
    if (actionWindow instanceof ULCDialog) {
      actionWindow.setVisible(false);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean handleException(Throwable ex, Map<String, Object> context) {
    if (super.handleException(ex, context)) {
      return true;
    }
    ULCComponent sourceComponent = controllerFrame;
    if (ex instanceof SecurityException) {
      ULCAlert alert = new ULCAlert(UlcUtil.getVisibleWindow(sourceComponent),
          getTranslationProvider().getTranslation("error", getLocale()),
          HtmlHelper.toHtml(HtmlHelper.emphasis(HtmlHelper.escapeForHTML(ex
              .getMessage()))), getTranslationProvider().getTranslation("ok",
              getLocale()), null, null, getIconFactory().getErrorIcon(
              getIconFactory().getLargeIconSize()));
      alert.show();
    } else if (ex instanceof BusinessException) {
      ULCAlert alert = new ULCAlert(UlcUtil.getVisibleWindow(sourceComponent),
          getTranslationProvider().getTranslation("error", getLocale()),
          HtmlHelper.toHtml(HtmlHelper.emphasis(HtmlHelper
              .escapeForHTML(((BusinessException) ex).getI18nMessage(
                  getTranslationProvider(), getLocale())))),
          getTranslationProvider().getTranslation("ok", getLocale()), null,
          null, getIconFactory().getErrorIcon(
              getIconFactory().getLargeIconSize()));
      alert.show();
    } else if (ex instanceof DataIntegrityViolationException) {
      ULCAlert alert = new ULCAlert(
          UlcUtil.getVisibleWindow(sourceComponent),
          getTranslationProvider().getTranslation("error", getLocale()),
          HtmlHelper
              .toHtml(HtmlHelper
                  .emphasis(HtmlHelper
                      .escapeForHTML(getTranslationProvider()
                          .getTranslation(
                              refineIntegrityViolationTranslationKey((DataIntegrityViolationException) ex),
                              getLocale())))), getTranslationProvider()
              .getTranslation("ok", getLocale()), null, null, getIconFactory()
              .getErrorIcon(getIconFactory().getLargeIconSize()));
      alert.show();
    } else if (ex instanceof ConcurrencyFailureException) {
      ULCAlert alert = new ULCAlert(UlcUtil.getVisibleWindow(sourceComponent),
          getTranslationProvider().getTranslation("error", getLocale()),
          HtmlHelper.toHtml(HtmlHelper.emphasis(HtmlHelper
              .escapeForHTML(getTranslationProvider().getTranslation(
                  "concurrency.error.description", getLocale())))),
          getTranslationProvider().getTranslation("ok", getLocale()), null,
          null, getIconFactory().getErrorIcon(
              getIconFactory().getLargeIconSize()));
      alert.show();
    } else {
      ex.printStackTrace();
      ULCErrorDialog dialog = ULCErrorDialog.createInstance(sourceComponent,
          getTranslationProvider(), getLocale());
      dialog.setMessageIcon(getIconFactory().getErrorIcon(
          getIconFactory().getMediumIconSize()));
      dialog.setTitle(getTranslationProvider().getTranslation("error",
          getLocale()));
      dialog.setMessage(HtmlHelper.toHtml(HtmlHelper.emphasis(HtmlHelper
          .escapeForHTML(ex.getLocalizedMessage()))));
      dialog.setDetails(ex);
      int screenRes = ClientContext.getScreenResolution();
      dialog.setSize(8 * screenRes, 3 * screenRes);
      dialog.pack();
      UlcUtil.centerOnScreen(dialog);
      dialog.setVisible(true);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public void popupInfo(ULCComponent sourceComponent, String title,
      String iconImageUrl, String message) {
    displayPopup(sourceComponent, title, iconImageUrl, message, null,
        OK_OPTION, null, null, null, null, null);
  }

  /**
   * {@inheritDoc}
   */
  public void popupOkCancel(ULCComponent sourceComponent, String title,
      String iconImageUrl, String message,
      org.jspresso.framework.action.IAction okAction,
      org.jspresso.framework.action.IAction cancelAction,
      Map<String, Object> context) {
    displayPopup(sourceComponent, title, iconImageUrl, message, context,
        OK_OPTION, CANCEL_OPTION, null, okAction, cancelAction, null);
  }

  /**
   * {@inheritDoc}
   */
  public void popupYesNo(ULCComponent sourceComponent, String title,
      String iconImageUrl, String message,
      org.jspresso.framework.action.IAction yesAction,
      org.jspresso.framework.action.IAction noAction,
      Map<String, Object> context) {
    displayPopup(sourceComponent, title, iconImageUrl, message, context,
        YES_OPTION, NO_OPTION, null, yesAction, noAction, null);
  }

  /**
   * {@inheritDoc}
   */
  public void popupYesNoCancel(ULCComponent sourceComponent, String title,
      String iconImageUrl, String message,
      org.jspresso.framework.action.IAction yesAction,
      org.jspresso.framework.action.IAction noAction,
      org.jspresso.framework.action.IAction cancelAction,
      Map<String, Object> context) {
    displayPopup(sourceComponent, title, iconImageUrl, message, context,
        YES_OPTION, NO_OPTION, CANCEL_OPTION, yesAction, noAction, cancelAction);
  }

  /**
   * Creates the initial view from the root view descriptor, then a SFrame
   * containing this view and presents it to the user.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean start(IBackendController backendController, Locale clientLocale) {
    if (super.start(backendController, clientLocale)) {
      initLoginProcess();
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

  private List<ULCMenu> createActionMenus() {
    return createMenus(getActionMap(), false);
  }

  private ULCMenuBar createApplicationMenuBar() {
    ULCMenuBar applicationMenuBar = new ULCMenuBar();
    List<ULCMenu> workspaceMenus = createWorkspacesMenus();
    if (workspaceMenus != null) {
      for (ULCMenu workspaceMenu : workspaceMenus) {
        applicationMenuBar.add(workspaceMenu);
      }
    }
    List<ULCMenu> actionMenus = createActionMenus();
    if (actionMenus != null) {
      for (ULCMenu actionMenu : actionMenus) {
        applicationMenuBar.add(actionMenu);
      }
    }
    applicationMenuBar.add(ULCFiller.createHorizontalGlue());
    List<ULCMenu> helpActionMenus = createHelpActionMenus();
    if (helpActionMenus != null) {
      for (ULCMenu helpActionMenu : helpActionMenus) {
        applicationMenuBar.add(helpActionMenu);
      }
    }
    return applicationMenuBar;
  }

  private void createControllerFrame() {
    controllerFrame = new ULCFrame();
    controllerFrame.setContentPane(new ULCDesktopPane());
    controllerFrame
        .setDefaultCloseOperation(IWindowConstants.DO_NOTHING_ON_CLOSE);
    controllerFrame.addWindowListener(new IWindowListener() {

      private static final long serialVersionUID = -7845554617417316256L;

      public void windowClosing(@SuppressWarnings("unused") WindowEvent event) {
        stop();
      }
    });
    controllerFrame.pack();
    int screenRes = ClientContext.getScreenResolution();
    int w = 12 * screenRes;
    int h = 8 * screenRes;
    if (getFrameWidth() != null) {
      w = getFrameWidth().intValue();
    }
    if (getFrameHeight() != null) {
      h = getFrameHeight().intValue();
    }
    controllerFrame.setSize(w, h);
    controllerFrame.setIconImage(getIconFactory().getIcon(getIconImageURL(),
        getIconFactory().getSmallIconSize()));
    UlcUtil.centerOnScreen(controllerFrame);
    updateFrameTitle();
    controllerFrame.setVisible(true);
  }

  private List<ULCMenu> createHelpActionMenus() {
    return createMenus(getHelpActions(), true);
  }

  private ULCMenu createMenu(ActionList actionList) {
    ULCMenu menu = new ULCMenu(actionList.getI18nName(getTranslationProvider(),
        getLocale()));
    if (actionList.getDescription() != null) {
      menu.setToolTipText(actionList.getI18nDescription(
          getTranslationProvider(), getLocale())
          + IActionFactory.TOOLTIP_ELLIPSIS);
    }
    menu.setIcon(getIconFactory().getIcon(actionList.getIconImageURL(),
        getIconFactory().getSmallIconSize()));
    for (ULCMenuItem menuItem : createMenuItems(actionList)) {
      menu.add(menuItem);
    }
    return menu;
  }

  private ULCMenuItem createMenuItem(IDisplayableAction action) {
    return new ULCMenuItem(getViewFactory().getActionFactory().createAction(
        action, this, null, getLocale()));
  }

  private List<ULCMenuItem> createMenuItems(ActionList actionList) {
    List<ULCMenuItem> menuItems = new ArrayList<ULCMenuItem>();
    for (IDisplayableAction action : actionList.getActions()) {
      if (isAccessGranted(action)) {
        menuItems.add(createMenuItem(action));
      }
    }
    return menuItems;
  }

  @SuppressWarnings("null")
  private List<ULCMenu> createMenus(ActionMap actionMap, boolean useSeparator) {
    List<ULCMenu> menus = new ArrayList<ULCMenu>();
    if (actionMap != null) {
      ULCMenu menu = null;
      for (ActionList actionList : actionMap.getActionLists()) {
        if (!useSeparator || menus.isEmpty()) {
          menu = createMenu(actionList);
          menus.add(menu);
        } else {
          menu.addSeparator();
          for (ULCMenuItem menuItem : createMenuItems(actionList)) {
            menu.add(menuItem);
          }
        }
      }
    }
    return menus;
  }

  /**
   * Creates a new ULCExtendedInternalFrame and populates it with a view.
   * 
   * @param view
   *          the view to be set into the internal frame.
   * @return the constructed internal frame.
   */
  private ULCExtendedInternalFrame createULCExtendedInternalFrame(
      ULCComponent view, String title, ULCIcon frameIcon) {
    ULCExtendedInternalFrame internalFrame = new ULCExtendedInternalFrame(title);
    internalFrame.setFrameIcon(frameIcon);
    internalFrame.setResizable(true);
    internalFrame.setClosable(true);
    internalFrame.setMaximizable(true);
    internalFrame.setIconifiable(true);
    internalFrame.getContentPane().add(view);
    internalFrame.setDefaultCloseOperation(IWindowConstants.HIDE_ON_CLOSE);
    return internalFrame;
  }

  private List<ULCMenu> createWorkspacesMenus() {
    return createMenus(createWorkspaceActionMap(), true);
  }

  private void displayPopup(ULCComponent sourceComponent, String title,
      String iconImageUrl, String message, final Map<String, Object> context,
      String firstOption, String secondOption, String thirdOption,
      final org.jspresso.framework.action.IAction firstAction,
      final org.jspresso.framework.action.IAction secondAction,
      final org.jspresso.framework.action.IAction thirdAction) {
    ITranslationProvider translationProvider = getTranslationProvider();
    Locale locale = getLocale();
    final Map<String, org.jspresso.framework.action.IAction> optionReverseDictionary;
    optionReverseDictionary = new HashMap<String, org.jspresso.framework.action.IAction>();
    String translatedFirstOption = null;
    if (firstOption != null) {
      translatedFirstOption = translationProvider.getTranslation(firstOption,
          locale);
      optionReverseDictionary.put(translatedFirstOption, firstAction);
    }
    String translatedSecondOption = null;
    if (secondOption != null) {
      translatedSecondOption = translationProvider.getTranslation(secondOption,
          locale);
      optionReverseDictionary.put(translatedSecondOption, secondAction);
    }
    String translatedThirdOption = null;
    if (thirdOption != null) {
      translatedThirdOption = translationProvider.getTranslation(thirdOption,
          locale);
      optionReverseDictionary.put(translatedThirdOption, thirdAction);
    }
    final ULCAlert alert = new ULCAlert(UlcUtil
        .getVisibleWindow(sourceComponent), title, message,
        translatedFirstOption, translatedSecondOption, translatedThirdOption,
        getIconFactory().getIcon(iconImageUrl,
            getIconFactory().getLargeIconSize()));
    alert.addWindowListener(new IWindowListener() {

      private static final long serialVersionUID = -6049928144066455758L;

      public void windowClosing(@SuppressWarnings("unused") WindowEvent event) {
        org.jspresso.framework.action.IAction nextAction = optionReverseDictionary
            .get(alert.getValue());
        if (nextAction != null) {
          execute(nextAction, context);
        }
      }
    });
    alert.show();
  }

  private void initLoginProcess() {
    createControllerFrame();
    if (getLoginContextName() == null) {
      performLogin();
      updateControllerFrame();
      execute(getStartupAction(), getInitialActionContext());
      return;
    }

    IView<ULCComponent> loginView = createLoginView();

    // Login dialog
    final ULCDialog dialog = new ULCDialog(controllerFrame,
        getLoginViewDescriptor().getI18nName(getTranslationProvider(),
            getLocale()), true);
    dialog.setDefaultCloseOperation(IWindowConstants.DO_NOTHING_ON_CLOSE);

    ULCBoxLayoutPane buttonBox = new ULCBoxLayoutPane(
        ULCBoxLayoutPane.LINE_AXIS);
    buttonBox.setBorder(new ULCEmptyBorder(new Insets(5, 10, 5, 10)));

    ULCButton loginButton = new ULCButton(getTranslationProvider()
        .getTranslation("ok", getLocale()));
    loginButton.setIcon(getIconFactory().getOkYesIcon(
        getIconFactory().getSmallIconSize()));
    loginButton.addActionListener(new IActionListener() {

      private static final long serialVersionUID = 2403541025764054935L;

      public void actionPerformed(@SuppressWarnings("unused") ActionEvent e) {
        if (performLogin()) {
          dialog.setVisible(false);
          updateControllerFrame();
          execute(getStartupAction(), getInitialActionContext());
        } else {
          ULCAlert alert = new ULCAlert(dialog, getTranslationProvider()
              .getTranslation("error", getLocale()), getTranslationProvider()
              .getTranslation(LoginUtils.LOGIN_FAILED, getLocale()),
              getTranslationProvider().getTranslation("ok", getLocale()), null,
              null, getIconFactory().getErrorIcon(
                  getIconFactory().getLargeIconSize()));
          alert.show();
        }
      }
    });
    buttonBox.add(loginButton);
    dialog.getRootPane().setDefaultButton(loginButton);

    ULCBorderLayoutPane actionPanel = new ULCBorderLayoutPane();
    actionPanel.add(buttonBox, ULCBorderLayoutPane.EAST);

    ULCBorderLayoutPane mainPanel = new ULCBorderLayoutPane();
    mainPanel.add(new ULCLabel(getTranslationProvider().getTranslation(
        LoginUtils.CRED_MESSAGE, getLocale())), ULCBorderLayoutPane.NORTH);
    mainPanel.add(loginView.getPeer(), ULCBorderLayoutPane.CENTER);
    mainPanel.add(actionPanel, ULCBorderLayoutPane.SOUTH);
    dialog.add(mainPanel);

    int screenRes = ClientContext.getScreenResolution();
    dialog.setSize(new com.ulcjava.base.application.util.Dimension(
        3 * screenRes, screenRes * 3 / 2));
    dialog.pack();
    UlcUtil.centerInParent(dialog);
    dialog.setVisible(true);
  }

  private void updateControllerFrame() {
    controllerFrame.setMenuBar(createApplicationMenuBar());
    updateFrameTitle();
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

  private final class WorkspaceInternalFrameListener implements
      IExtendedInternalFrameListener {

    private String workspaceName;

    /**
     * Constructs a new <code>WorkspaceInternalFrameListener</code> instance.
     * 
     * @param workspaceName
     *          the root workspace identifier this listener is attached to.
     */
    public WorkspaceInternalFrameListener(String workspaceName) {
      this.workspaceName = workspaceName;
    }

    /**
     * {@inheritDoc}
     */
    public void internalFrameActivated(
        @SuppressWarnings("unused") ExtendedInternalFrameEvent e) {
      displayWorkspace(workspaceName);
    }

    /**
     * {@inheritDoc}
     */
    public void internalFrameClosed(
        @SuppressWarnings("unused") ExtendedInternalFrameEvent event) {
      displayWorkspace(null);
    }

    /**
     * {@inheritDoc}
     */
    public void internalFrameClosing(
        @SuppressWarnings("unused") ExtendedInternalFrameEvent event) {
      displayWorkspace(null);
    }

    /**
     * {@inheritDoc}
     */
    public void internalFrameDeactivated(
        @SuppressWarnings("unused") ExtendedInternalFrameEvent e) {
      // displayWorkspace(null);
    }

    /**
     * {@inheritDoc}
     */
    public void internalFrameDeiconified(
        @SuppressWarnings("unused") ExtendedInternalFrameEvent event) {
      displayWorkspace(workspaceName);
    }

    /**
     * {@inheritDoc}
     */
    public void internalFrameIconified(
        @SuppressWarnings("unused") ExtendedInternalFrameEvent event) {
      // displayWorkspace(null);
    }

    /**
     * {@inheritDoc}
     */
    public void internalFrameOpened(
        @SuppressWarnings("unused") ExtendedInternalFrameEvent event) {
      displayWorkspace(workspaceName);
    }
  }
}
