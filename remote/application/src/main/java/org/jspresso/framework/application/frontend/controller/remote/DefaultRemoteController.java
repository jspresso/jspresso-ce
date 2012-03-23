/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.frontend.controller.remote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.frontend.command.remote.CommandException;
import org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler;
import org.jspresso.framework.application.frontend.command.remote.RemoteActionCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteCleanupCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteClipboardCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteCloseDialogCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteDialogCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteFlashDisplayCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteInitCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteInitLoginCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteLocaleCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteLoginCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteMessageCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteOkCancelCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteOpenUrlCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteRestartCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteSortCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteStartCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteTableChangedCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteUpdateStatusCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteValueCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteWorkspaceDisplayCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteYesNoCancelCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteYesNoCommand;
import org.jspresso.framework.application.frontend.controller.AbstractFrontendController;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IConfigurableConnectorFactory;
import org.jspresso.framework.binding.IFormattedValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.remote.RemoteConnectorFactory;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RActionEvent;
import org.jspresso.framework.gui.remote.RActionList;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.RIcon;
import org.jspresso.framework.gui.remote.RSplitContainer;
import org.jspresso.framework.gui.remote.RTabContainer;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.state.remote.IRemoteStateOwner;
import org.jspresso.framework.util.collection.ESort;
import org.jspresso.framework.util.event.ISelectable;
import org.jspresso.framework.util.exception.BusinessException;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.http.CookiePreferencesStore;
import org.jspresso.framework.util.http.HttpRequestHolder;
import org.jspresso.framework.util.http.RequestParamsHttpFilter;
import org.jspresso.framework.util.lang.ObjectUtils;
import org.jspresso.framework.util.preferences.IPreferencesStore;
import org.jspresso.framework.util.remote.IRemotePeer;
import org.jspresso.framework.util.remote.registry.IRemotePeerRegistry;
import org.jspresso.framework.util.remote.registry.IRemotePeerRegistryListener;
import org.jspresso.framework.util.security.LoginUtils;
import org.jspresso.framework.util.uid.IGUIDGenerator;
import org.jspresso.framework.view.IActionFactory;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.IViewFactory;
import org.jspresso.framework.view.action.ActionList;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.EOrientation;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.remote.DefaultRemoteViewFactory;
import org.jspresso.framework.view.remote.RemoteActionFactory;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * This is is the default implementation of a &quot;remotable&quot; frontend
 * controller. It will implement a 3-tier architecture. The remote controller
 * lives on server-side and communicates with generic UI engines that are
 * deployed on client side. As of now, the remote frontend controller is used by
 * the <b>Flex</b> and <b>Qooxdoo</b> frontends. Communication happens through
 * the use of generic UI commands that are produced/consumed on both sides of
 * the network.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultRemoteController extends
    AbstractFrontendController<RComponent, RIcon, RAction> implements
    IRemoteCommandHandler, IRemotePeerRegistry, IRemotePeerRegistryListener {

  // private int commandLowPriorityOffset;
  private List<RemoteCommand>    commandQueue;
  private List<String>           removedPeersGuids;
  private boolean                commandRegistrationEnabled;
  private IGUIDGenerator<String> guidGenerator;
  private IRemotePeerRegistry    remotePeerRegistry;
  private Set<String>            workspaceViews;

  // Keep a hard reference on the login view, so that it is not garbage
  // collected.
  private IView<RComponent>      loginView;
  private String[]               clientKeysToTranslate;

  /**
   * Constructs a new <code>DefaultRemoteController</code> instance.
   */
  public DefaultRemoteController() {
    // commandLowPriorityOffset = 0;
    commandRegistrationEnabled = false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clear() {
    if (remotePeerRegistry != null) {
      remotePeerRegistry.clear();
    }
    if (removedPeersGuids != null) {
      removedPeersGuids.clear();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayFlashObject(String swfUrl,
      Map<String, String> flashContext, List<RAction> actions, String title,
      @SuppressWarnings("unused")
      RComponent sourceComponent, Map<String, Object> context,
      Dimension dimension, boolean reuseCurrent) {
    super.displayModalDialog(context, reuseCurrent);
    RemoteFlashDisplayCommand flashCommand = new RemoteFlashDisplayCommand();
    flashCommand.setSwfUrl(swfUrl);
    flashCommand.setTitle(title);
    flashCommand.setActions(actions.toArray(new RAction[0]));
    flashCommand.setUseCurrent(reuseCurrent);
    List<String> paramNames = new ArrayList<String>();
    List<String> paramValues = new ArrayList<String>();
    for (Map.Entry<String, String> flashVar : flashContext.entrySet()) {
      paramNames.add(flashVar.getKey());
      paramValues.add(flashVar.getValue());
    }
    flashCommand.setParamNames(paramNames.toArray(new String[0]));
    flashCommand.setParamValues(paramValues.toArray(new String[0]));
    flashCommand.setDimension(dimension);
    registerCommand(flashCommand);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayModalDialog(RComponent mainView, List<RAction> actions,
      String title, @SuppressWarnings("unused")
      RComponent sourceComponent, Map<String, Object> context,
      Dimension dimension, boolean reuseCurrent) {
    super.displayModalDialog(context, reuseCurrent);
    RemoteDialogCommand dialogCommand = new RemoteDialogCommand();
    dialogCommand.setTitle(title);
    dialogCommand.setView(mainView);
    dialogCommand.setActions(actions.toArray(new RAction[0]));
    dialogCommand.setUseCurrent(reuseCurrent);
    dialogCommand.setDimension(dimension);
    registerCommand(dialogCommand);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayUrl(String urlSpec) {
    RemoteOpenUrlCommand openUrlCommand = new RemoteOpenUrlCommand();
    openUrlCommand.setUrlSpec(urlSpec);
    registerCommand(openUrlCommand);
  }

  /**
   * Sends a remote workspace display command.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void displayWorkspace(String workspaceName,
      boolean bypassModuleBoundaryActions) {
    displayWorkspace(workspaceName, bypassModuleBoundaryActions, true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void disposeModalDialog(RComponent sourceWidget,
      Map<String, Object> context) {
    super.disposeModalDialog(sourceWidget, context);
    registerCommand(new RemoteCloseDialogCommand());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IRemotePeer getRegistered(String guid) {
    return remotePeerRegistry.getRegistered(guid);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IRemotePeer getRegisteredForPermId(String permId) {
    return remotePeerRegistry.getRegisteredForPermId(permId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized List<RemoteCommand> handleCommands(
      List<RemoteCommand> commands) {
    try {
      commandRegistrationEnabled = true;
      commandQueue = new ArrayList<RemoteCommand>();
      // commandLowPriorityOffset = 0;
      if (commands != null) {
        for (RemoteCommand command : commands) {
          handleCommand(command);
        }
      }
      if (removedPeersGuids != null && removedPeersGuids.size() > 0) {
        RemoteCleanupCommand cleanupCommand = new RemoteCleanupCommand();
        cleanupCommand.setRemovedPeerGuids(removedPeersGuids
            .toArray(new String[removedPeersGuids.size()]));
        registerCommand(cleanupCommand);
        removedPeersGuids.clear();
      }
    } finally {
      commandRegistrationEnabled = false;
      IBackendController bc = getBackendController();
      if (bc != null) {
        bc.cleanupRequestResources();
      }
    }
    return commandQueue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean handleException(Throwable ex, Map<String, Object> context) {
    if (super.handleException(ex, context)) {
      return true;
    }
    RemoteMessageCommand messageCommand = createErrorMessageCommand();
    if (ex instanceof SecurityException) {
      messageCommand.setMessage(ex.getMessage());
    } else if (ex instanceof BusinessException) {
      messageCommand.setMessage(((BusinessException) ex).getI18nMessage(this,
          getLocale()));
    } else if (ex instanceof DataIntegrityViolationException) {
      messageCommand
          .setMessage(this
              .getTranslation(
                  refineIntegrityViolationTranslationKey((DataIntegrityViolationException) ex),
                  getLocale()));
    } else if (ex instanceof ConcurrencyFailureException) {
      messageCommand.setMessage(getTranslation("concurrency.error.description",
          getLocale()));
    } else {
      ex.printStackTrace();
      messageCommand.setMessage(ex.getLocalizedMessage());
    }
    registerCommand(messageCommand);
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRegistered(String guid) {
    return remotePeerRegistry.isRegistered(guid);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void popupInfo(@SuppressWarnings("unused")
  RComponent sourceComponent, String title, String iconImageUrl, String message) {
    RemoteMessageCommand messageCommand = new RemoteMessageCommand();
    messageCommand.setTitle(title);
    messageCommand.setMessage(message);
    messageCommand.setTitleIcon(getIconFactory().getInfoIcon(
        getIconFactory().getTinyIconSize()));
    if (iconImageUrl != null) {
      messageCommand.setMessageIcon(getIconFactory().getIcon(iconImageUrl,
          getIconFactory().getLargeIconSize()));
    } else {
      messageCommand.setMessageIcon(getIconFactory().getInfoIcon(
          getIconFactory().getLargeIconSize()));
    }
    registerCommand(messageCommand);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void popupOkCancel(@SuppressWarnings("unused")
  RComponent sourceComponent, String title, String iconImageUrl,
      String message, IAction okAction, IAction cancelAction,
      Map<String, Object> context) {
    RemoteOkCancelCommand messageCommand = new RemoteOkCancelCommand();
    messageCommand.setTitle(title);
    messageCommand.setMessage(message);

    messageCommand.setTitleIcon(getIconFactory().getWarningIcon(
        getIconFactory().getTinyIconSize()));
    if (iconImageUrl != null) {
      messageCommand.setMessageIcon(getIconFactory().getIcon(iconImageUrl,
          getIconFactory().getLargeIconSize()));
    } else {
      messageCommand.setMessageIcon(getIconFactory().getWarningIcon(
          getIconFactory().getLargeIconSize()));
    }
    if (okAction != null) {
      messageCommand.setOkAction(createRAction(okAction, context));
    }
    if (cancelAction != null) {
      messageCommand.setCancelAction(createRAction(cancelAction, context));
    }
    registerCommand(messageCommand);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void popupYesNo(@SuppressWarnings("unused")
  RComponent sourceComponent, String title, String iconImageUrl,
      String message, IAction yesAction, IAction noAction,
      Map<String, Object> context) {
    RemoteYesNoCommand messageCommand = new RemoteYesNoCommand();
    messageCommand.setTitle(title);
    messageCommand.setMessage(message);

    messageCommand.setTitleIcon(getIconFactory().getQuestionIcon(
        getIconFactory().getTinyIconSize()));
    if (iconImageUrl != null) {
      messageCommand.setMessageIcon(getIconFactory().getIcon(iconImageUrl,
          getIconFactory().getLargeIconSize()));
    } else {
      messageCommand.setMessageIcon(getIconFactory().getQuestionIcon(
          getIconFactory().getLargeIconSize()));
    }
    if (yesAction != null) {
      messageCommand.setYesAction(createRAction(yesAction, context));
    }
    if (noAction != null) {
      messageCommand.setNoAction(createRAction(noAction, context));
    }
    registerCommand(messageCommand);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void popupYesNoCancel(@SuppressWarnings("unused")
  RComponent sourceComponent, String title, String iconImageUrl,
      String message, IAction yesAction, IAction noAction,
      IAction cancelAction, Map<String, Object> context) {
    RemoteYesNoCancelCommand messageCommand = new RemoteYesNoCancelCommand();
    messageCommand.setTitle(title);
    messageCommand.setMessage(message);

    messageCommand.setTitleIcon(getIconFactory().getQuestionIcon(
        getIconFactory().getTinyIconSize()));
    if (iconImageUrl != null) {
      messageCommand.setMessageIcon(getIconFactory().getIcon(iconImageUrl,
          getIconFactory().getLargeIconSize()));
    } else {
      messageCommand.setMessageIcon(getIconFactory().getQuestionIcon(
          getIconFactory().getLargeIconSize()));
    }
    if (yesAction != null) {
      messageCommand.setYesAction(createRAction(yesAction, context));
    }
    if (noAction != null) {
      messageCommand.setNoAction(createRAction(noAction, context));
    }
    if (cancelAction != null) {
      messageCommand.setCancelAction(createRAction(cancelAction, context));
    }
    registerCommand(messageCommand);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void register(IRemotePeer remotePeer) {
    remotePeerRegistry.register(remotePeer);
    if (remotePeer instanceof ICompositeValueConnector) {
      for (String childKey : ((ICompositeValueConnector) remotePeer)
          .getChildConnectorKeys()) {
        IValueConnector childConnector = ((ICompositeValueConnector) remotePeer)
            .getChildConnector(childKey);
        register((IRemotePeer) childConnector);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addRemotePeerRegistryListener(IRemotePeerRegistryListener listener) {
    remotePeerRegistry.addRemotePeerRegistryListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeRemotePeerRegistryListener(
      IRemotePeerRegistryListener listener) {
    remotePeerRegistry.removeRemotePeerRegistryListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String registerPermId(String automationsSeed, String guid) {
    return remotePeerRegistry.registerPermId(automationsSeed, guid);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerCommand(RemoteCommand command) {
    if (commandRegistrationEnabled) {
      // if (command instanceof RemoteChildrenCommand) {
      // // The remote children commands, that may create and register
      // // remote server peers on client side must be handled first.
      // commandQueue.add(commandLowPriorityOffset, command);
      // commandLowPriorityOffset++;
      // } else {
      commandQueue.add(command);
      // }
    }
  }

  /**
   * Sets the guidGenerator.
   * 
   * @param guidGenerator
   *          the guidGenerator to set.
   * @internal
   */
  public void setGuidGenerator(IGUIDGenerator<String> guidGenerator) {
    this.guidGenerator = guidGenerator;
  }

  /**
   * Sets the remotePeerRegistry.
   * 
   * @param remotePeerRegistry
   *          the remotePeerRegistry to set.
   * @internal
   */
  public void setRemotePeerRegistry(IRemotePeerRegistry remotePeerRegistry) {
    if (this.remotePeerRegistry != null) {
      this.remotePeerRegistry.removeRemotePeerRegistryListener(this);
    }
    this.remotePeerRegistry = remotePeerRegistry;
    if (this.remotePeerRegistry != null) {
      this.remotePeerRegistry.addRemotePeerRegistryListener(this);
      this.removedPeersGuids = new ArrayList<String>();
    }
  }

  /**
   * Updates the view factory with the remote peer registry.
   * <p>
   * {@inheritDoc}
   * 
   * @internal
   */
  @Override
  public void setViewFactory(
      IViewFactory<RComponent, RIcon, RAction> viewFactory) {
    if (viewFactory instanceof DefaultRemoteViewFactory) {
      ((DefaultRemoteViewFactory) viewFactory).setRemoteCommandHandler(this);
      ((DefaultRemoteViewFactory) viewFactory).setRemotePeerRegistry(this);
    }
    IActionFactory<RAction, RComponent> actionFactory = viewFactory
        .getActionFactory();
    if (actionFactory instanceof RemoteActionFactory) {
      ((RemoteActionFactory) actionFactory).setRemoteCommandHandler(this);
      ((RemoteActionFactory) actionFactory).setRemotePeerRegistry(this);
    }
    IConfigurableConnectorFactory connectorFactory = viewFactory
        .getConnectorFactory();
    if (connectorFactory instanceof RemoteConnectorFactory) {
      ((RemoteConnectorFactory) connectorFactory).setRemoteCommandHandler(this);
      ((RemoteConnectorFactory) connectorFactory).setRemotePeerRegistry(this);
    }
    super.setViewFactory(viewFactory);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean stop() {
    clear();
    clearRequestParams();
    if (workspaceViews != null) {
      workspaceViews.clear();
    }
    registerCommand(new RemoteRestartCommand());
    return super.stop();
  }

  private void clearRequestParams() {
    HttpSession session = HttpRequestHolder.getServletRequest().getSession();
    session.removeAttribute(RequestParamsHttpFilter.REQUEST_PARAMS_KEY);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unregister(String guid) {
    IRemotePeer remotePeer = getRegistered(guid);
    remotePeerRegistry.unregister(guid);
    if (remotePeer instanceof ICompositeValueConnector) {
      for (String childKey : ((ICompositeValueConnector) remotePeer)
          .getChildConnectorKeys()) {
        unregister(((IRemotePeer) ((ICompositeValueConnector) remotePeer)
            .getChildConnector(childKey)).getGuid());
      }
    }
  }

  /**
   * Creates the init commands to be sent to the remote peer.
   * 
   * @return the init commands to be sent to the remote peer.
   */
  protected List<RemoteCommand> createInitCommands() {
    List<RemoteCommand> initCommands = new ArrayList<RemoteCommand>();
    initCommands.add(createLocaleCommand());
    initCommands.add(createInitCommand());
    return initCommands;
  }

  /**
   * Create the application frame init command.
   * 
   * @return the application frame init command.
   */
  protected RemoteInitCommand createInitCommand() {
    RemoteInitCommand initCommand = new RemoteInitCommand();
    initCommand.setWorkspaceNames(getWorkspaceNames().toArray(new String[0]));
    initCommand
        .setWorkspaceActions(createRActionList(createWorkspaceActionList()));
    initCommand.setActions(createRActionLists(getActionMap()));
    initCommand
        .setSecondaryActions(createRActionLists(getSecondaryActionMap()));
    initCommand.setHelpActions(createRActionLists(getHelpActions()));
    initCommand
        .setNavigationActions(createRActionLists(getNavigationActions()));
    initCommand.setExitAction(getViewFactory().getActionFactory().createAction(
        getExitAction(), this, null, getLocale()));
    int w = 0;
    if (getFrameWidth() != null) {
      w = getFrameWidth().intValue();
    }
    int h = 0;
    if (getFrameHeight() != null) {
      h = getFrameHeight().intValue();
    }
    initCommand.setSize(new Dimension(w, h));
    return initCommand;
  }

  /**
   * Creates the locale command for translations and client locale handling.
   * 
   * @return the locale command for translations and client locale handling.
   */
  protected RemoteLocaleCommand createLocaleCommand() {
    Map<String, String> translations = new HashMap<String, String>();
    if (clientKeysToTranslate != null) {
      for (String key : clientKeysToTranslate) {
        translations.put(key, getTranslation(key, getLocale()));
      }
    }
    RemoteLocaleCommand localeCommand = new RemoteLocaleCommand();
    localeCommand.setLanguage(getLocale().getLanguage());
    localeCommand.setDatePattern(getDatePattern(getLocale()));
    localeCommand.setTranslations(translations);
    return localeCommand;
  }

  /**
   * Sets the workspace as selected and optionaly notifies the remote peer.
   * 
   * @param workspaceName
   *          the selected workspace name.
   * @param bypassModuleBoundaryActions
   *          should we bypass module onEnter/Exit actions ?
   * @param notifyRemote
   *          if true, a remote notification will be sent to the remote peer.
   */
  protected void displayWorkspace(String workspaceName,
      boolean bypassModuleBoundaryActions, boolean notifyRemote) {
    if (!ObjectUtils.equals(workspaceName, getSelectedWorkspaceName())) {
      super.displayWorkspace(workspaceName, bypassModuleBoundaryActions);
      if (workspaceViews == null) {
        workspaceViews = new HashSet<String>();
      }
      RSplitContainer workspaceView = null;
      if (!workspaceViews.contains(workspaceName)) {
        workspaceView = new RSplitContainer(workspaceName + "_split");
        workspaceView.setOrientation(EOrientation.HORIZONTAL.toString());
        IViewDescriptor workspaceNavigatorViewDescriptor = getWorkspace(
            workspaceName).getViewDescriptor();
        IValueConnector workspaceConnector = getBackendController()
            .getWorkspaceConnector(workspaceName);
        IView<RComponent> workspaceNavigator = createWorkspaceNavigator(
            workspaceName, workspaceNavigatorViewDescriptor);
        IView<RComponent> moduleAreaView = createModuleAreaView(workspaceName);
        workspaceView.setLeftTop(workspaceNavigator.getPeer());
        workspaceView.setRightBottom(moduleAreaView.getPeer());
        workspaceViews.add(workspaceName);
        getMvcBinder().bind(workspaceNavigator.getConnector(),
            workspaceConnector);
      }
      if (notifyRemote) {
        RemoteWorkspaceDisplayCommand workspaceDisplayCommand = new RemoteWorkspaceDisplayCommand();
        if (workspaceView != null) {
          workspaceDisplayCommand.setWorkspaceView(workspaceView);
        }
        workspaceDisplayCommand.setWorkspaceName(workspaceName);
        registerCommand(workspaceDisplayCommand);
      }
    }
  }

  /**
   * Handles a single command.
   * 
   * @param command
   *          the command to handle.
   */
  protected void handleCommand(RemoteCommand command) {
    if (command instanceof RemoteStartCommand) {
      String[] keysToTranslate = ((RemoteStartCommand) command)
          .getKeysToTranslate();
      if (keysToTranslate != null) {
        clientKeysToTranslate = keysToTranslate;
      }
      if (isLoginInteractive()) {
        registerCommand(createLocaleCommand());
        RemoteInitLoginCommand initLoginCommand = new RemoteInitLoginCommand();
        loginView = createLoginView();
        initLoginCommand.setLoginView(loginView.getPeer());
        initLoginCommand.setTitle(getLoginViewDescriptor().getI18nName(this,
            getLocale()));
        initLoginCommand.setMessage(getTranslation(LoginUtils.CRED_MESSAGE,
            getLocale()));
        initLoginCommand.setOkLabel(getTranslation("ok", getLocale()));
        initLoginCommand.setOkIcon(getIconFactory().getOkYesIcon(
            getIconFactory().getSmallIconSize()));
        registerCommand(initLoginCommand);
      } else {
        handleCommand(new RemoteLoginCommand());
      }
    } else if (command instanceof RemoteLoginCommand) {
      if (performLogin()) {
        if (isLoginInteractive()) {
          registerCommand(new RemoteCloseDialogCommand());
        }
        List<RemoteCommand> initCommands = createInitCommands();
        for (RemoteCommand initCommand : initCommands) {
          registerCommand(initCommand);
        }
        if (getWorkspaceNames() != null && getWorkspaceNames().size() > 0) {
          displayWorkspace(getWorkspaceNames().get(0));
        }
        execute(getStartupAction(), getStartupActionContext());
      } else {
        loginFailed();
      }
    } else if (command instanceof RemoteWorkspaceDisplayCommand) {
      displayWorkspace(
          ((RemoteWorkspaceDisplayCommand) command).getWorkspaceName(), false);
    } else if (command instanceof RemoteTableChangedCommand) {
      Object[][] columnPrefs = new Object[((RemoteTableChangedCommand) command)
          .getColumnIds().length][2];
      for (int i = 0; i < ((RemoteTableChangedCommand) command).getColumnIds().length; i++) {
        columnPrefs[i] = new Object[] {
            ((RemoteTableChangedCommand) command).getColumnIds()[i],
            ((RemoteTableChangedCommand) command).getColumnWidths()[i]};
      }
      getViewFactory()
          .storeTablePreferences(
              ((RemoteTableChangedCommand) command).getTableId(), columnPrefs,
              this);
    } else {
      IRemotePeer targetPeer = null;
      if (command.getPermId() != null) {
        targetPeer = getRegisteredForPermId(command.getPermId());
      }
      if (targetPeer == null) {
        targetPeer = getRegistered(command.getTargetPeerGuid());
      }
      if (targetPeer == null) {
        throw new CommandException("Target remote peer could not be retrieved");
      }
      if (command instanceof RemoteValueCommand) {
        if (targetPeer instanceof IFormattedValueConnector) {
          ((IFormattedValueConnector) targetPeer)
              .setConnectorValueAsString((String) ((RemoteValueCommand) command)
                  .getValue());
        } else if (targetPeer instanceof IRemoteStateOwner) {
          ((IRemoteStateOwner) targetPeer)
              .setValueFromState(((RemoteValueCommand) command).getValue());
        } else if (targetPeer instanceof IValueConnector) {
          ((IValueConnector) targetPeer)
              .setConnectorValue(((RemoteValueCommand) command).getValue());
        } else {
          throw new CommandException("Target peer type cannot be handled : "
              + targetPeer.getClass().getName());
        }

        // The following code has been handled at a lower level,
        // see AbstractCompositeValueConnector.propagateRollback
        // if (targetPeer instanceof IRemoteStateOwner) {
        // ((IRemoteStateOwner) targetPeer).synchRemoteState();
        // RemoteValueState state = ((IRemoteStateOwner) targetPeer).getState();
        // if (!ObjectUtils.equals(((RemoteValueCommand) command).getValue(),
        // state.getValue())) {
        //
        // RemoteValueCommand reverseCommand = new RemoteValueCommand();
        // reverseCommand.setTargetPeerGuid(state.getGuid());
        // reverseCommand.setValue(state.getValue());
        // registerCommand(reverseCommand);
        // }
        // }
      } else if (command instanceof RemoteSelectionCommand) {
        if (targetPeer instanceof RTabContainer) {
          ((RTabContainer) targetPeer)
              .setSelectedIndex(((RemoteSelectionCommand) command)
                  .getLeadingIndex());
        } else {
          ISelectable selectable = null;
          if (targetPeer instanceof ICollectionConnectorProvider) {
            selectable = ((ICollectionConnectorProvider) targetPeer)
                .getCollectionConnector();
          } else if (targetPeer instanceof ISelectable) {
            selectable = (ISelectable) targetPeer;
          }
          if (selectable != null) {
            selectable.setSelectedIndices(
                ((RemoteSelectionCommand) command).getSelectedIndices(),
                ((RemoteSelectionCommand) command).getLeadingIndex());
          }
        }
      } else if (command instanceof RemoteActionCommand) {
        RAction action = (RAction) targetPeer;
        RActionEvent actionEvent = ((RemoteActionCommand) command)
            .getActionEvent();
        String viewStatePermId = actionEvent.getViewStatePermId();
        if (viewStatePermId != null) {
          IRemotePeer viewPeer = getRegisteredForPermId(viewStatePermId);
          if (viewPeer != null) {
            actionEvent.setViewStateGuid(viewPeer.getGuid());
          }
        }
        action.actionPerformed(actionEvent, null);
      } else if (command instanceof RemoteSortCommand) {
        RAction sortAction = (RAction) targetPeer;
        Map<String, String> orderingProperties = ((RemoteSortCommand) command)
            .getOrderingProperties();
        Map<String, ESort> typedOrderingProperties = new LinkedHashMap<String, ESort>();
        if (orderingProperties != null) {
          for (Map.Entry<String, String> orderingProperty : orderingProperties
              .entrySet()) {
            typedOrderingProperties.put(orderingProperty.getKey(),
                ESort.valueOf(orderingProperty.getValue()));
          }
        }
        Map<String, Object> context = new HashMap<String, Object>();
        context.put(IQueryComponent.ORDERING_PROPERTIES,
            typedOrderingProperties);
        String viewStateGuid = null;
        String viewStatePermId = ((RemoteSortCommand) command)
            .getViewStatePermId();
        if (viewStatePermId != null) {
          IRemotePeer viewPeer = getRegisteredForPermId(viewStatePermId);
          if (viewPeer != null) {
            viewStateGuid = viewPeer.getGuid();
          }
        }
        if (viewStateGuid == null) {
          viewStateGuid = ((RemoteSortCommand) command).getViewStateGuid();
        }
        RActionEvent event = new RActionEvent();
        event.setViewStateGuid(viewStateGuid);
        sortAction.actionPerformed(event, context);
      } else {
        throw new CommandException("Unsupported command type : "
            + command.getClass().getSimpleName());
      }
    }
  }

  /**
   * Callback after a failed login.
   */
  protected void loginFailed() {
    RemoteMessageCommand errorMessageCommand = createErrorMessageCommand();
    errorMessageCommand.setMessage(getTranslation(LoginUtils.LOGIN_FAILED,
        getLocale()));
    registerCommand(errorMessageCommand);
  }

  private RemoteMessageCommand createErrorMessageCommand() {
    RemoteMessageCommand messageCommand = new RemoteMessageCommand();
    messageCommand.setTitle(getTranslation("error", getLocale()));
    messageCommand.setTitleIcon(getIconFactory().getErrorIcon(
        getIconFactory().getTinyIconSize()));
    messageCommand.setMessageIcon(getIconFactory().getErrorIcon(
        getIconFactory().getLargeIconSize()));
    return messageCommand;
  }

  @SuppressWarnings("unchecked")
  private RAction createRAction(IAction action, Map<String, Object> context) {
    return getViewFactory().getActionFactory().createAction(
        wrapAction(action, context), this,
        (IView<RComponent>) context.get(ActionContextConstants.VIEW),
        getLocale());
  }

  private RActionList createRActionList(ActionList actionList) {
    RActionList rActionList = new RActionList(guidGenerator.generateGUID());
    rActionList.setName(actionList.getI18nName(this, getLocale()));
    rActionList
        .setDescription(actionList.getI18nDescription(this, getLocale()));
    rActionList.setIcon(getIconFactory().getIcon(actionList.getIconImageURL(),
        getIconFactory().getTinyIconSize()));

    List<RAction> actions = new ArrayList<RAction>();
    for (IDisplayableAction action : actionList.getActions()) {
      if (isAccessGranted(action)) {
        try {
          pushToSecurityContext(action);
          actions.add(getViewFactory().getActionFactory().createAction(action,
              this, null, getLocale()));
        } finally {
          restoreLastSecurityContextSnapshot();
        }
      }
    }
    rActionList.setActions(actions.toArray(new RAction[0]));
    rActionList.setCollapsable(actionList.isCollapsable());
    return rActionList;
  }

  private RActionList[] createRActionLists(ActionMap actionMap) {
    List<RActionList> actionLists = new ArrayList<RActionList>();
    if (actionMap != null) {
      if (isAccessGranted(actionMap)) {
        try {
          pushToSecurityContext(actionMap);
          for (ActionList actionList : actionMap.getActionLists(this)) {
            if (isAccessGranted(actionList)) {
              try {
                pushToSecurityContext(actionList);
                actionLists.add(createRActionList(actionList));
              } finally {
                restoreLastSecurityContextSnapshot();
              }
            }
          }
        } finally {
          restoreLastSecurityContextSnapshot();
        }
      }
    }
    return actionLists.toArray(new RActionList[0]);
  }

  private IDisplayableAction wrapAction(IAction action,
      final Map<String, Object> initialContext) {
    FrontendAction<RComponent, RIcon, RAction> wrapper;
    if (action instanceof IDisplayableAction) {
      wrapper = new FrontendAction<RComponent, RIcon, RAction>() {

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean execute(IActionHandler actionHandler,
            Map<String, Object> context) {
          // To keep original context
          context.putAll(initialContext);
          return super.execute(actionHandler, context);
        }
      };
      wrapper.setNextAction(action);
    } else {
      wrapper = new FrontendAction<RComponent, RIcon, RAction>() {

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean execute(IActionHandler actionHandler,
            Map<String, Object> context) {
          // To keep original context
          context.putAll(initialContext);
          return super.execute(actionHandler, context);
        }
      };
      wrapper.setWrappedAction(action);
    }
    return wrapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void remotePeerAdded(@SuppressWarnings("unused")
  IRemotePeer peer) {
    // No-op
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void remotePeerRemoved(String peerGuid) {
    removedPeersGuids.add(peerGuid);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setStatusInfo(String statusInfo) {
    RemoteUpdateStatusCommand updateStatusCommand = new RemoteUpdateStatusCommand();
    updateStatusCommand.setStatus(statusInfo);
    registerCommand(updateStatusCommand);
  }

  /**
   * Returns a preference store based on Java preferences API.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected IPreferencesStore createClientPreferencesStore() {
    return new CookiePreferencesStore();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setClipboardContent(String plainContent, String htmlContent) {
    RemoteClipboardCommand clipboardCommand = new RemoteClipboardCommand();
    clipboardCommand.setPlainContent(plainContent);
    clipboardCommand.setHtmlContent(htmlContent);
    registerCommand(clipboardCommand);
  }

  /**
   * Complements with request parameters.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Map<String, Object> getLoginActionContext() {
    Map<String, Object> loginActionContext = super.getLoginActionContext();
    completeActionContextWithRequestParameters(loginActionContext);
    return loginActionContext;
  }

  /**
   * Complements with request parameters.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Map<String, Object> getStartupActionContext() {
    Map<String, Object> startupActionContext = super.getStartupActionContext();
    completeActionContextWithRequestParameters(startupActionContext);
    return startupActionContext;
  }

  private void completeActionContextWithRequestParameters(
      Map<String, Object> actionContext) {
    HttpSession session = HttpRequestHolder.getServletRequest().getSession();
    @SuppressWarnings("unchecked")
    Map<String, Object> requestParams = (Map<String, Object>) session
        .getAttribute(RequestParamsHttpFilter.REQUEST_PARAMS_KEY);
    if (requestParams != null) {
      actionContext.putAll(requestParams);
    }
  }
}
