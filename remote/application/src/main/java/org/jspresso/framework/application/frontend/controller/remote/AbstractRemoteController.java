/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.frontend.command.remote.CommandException;
import org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler;
import org.jspresso.framework.application.frontend.command.remote.RemoteActionCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteAddCardCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteApplicationDescriptionCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteChildrenCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteCleanupCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteCloseDialogCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteDialogCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteErrorMessageCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteHistoryDisplayCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteInitCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteInitLoginCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteLocaleCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteMessageCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteOkCancelCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteOpenUrlCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteRefreshCommand;
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
import org.jspresso.framework.application.frontend.controller.ModuleHistoryEntry;
import org.jspresso.framework.binding.ConnectorInputException;
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
import org.jspresso.framework.gui.remote.RTabContainer;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.state.remote.IRemoteStateOwner;
import org.jspresso.framework.state.remote.RemoteValueState;
import org.jspresso.framework.util.collection.ESort;
import org.jspresso.framework.util.event.ISelectable;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.gui.EClientType;
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
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.remote.AbstractRemoteViewFactory;
import org.jspresso.framework.view.remote.RemoteActionFactory;

/**
 * This is is the base implementation of all &quot;remotable&quot; frontend
 * controller.
 *
 * @author Vincent Vandenschrick
 */
public abstract class AbstractRemoteController extends AbstractFrontendController<RComponent, RIcon, RAction>
    implements IRemoteCommandHandler, IRemotePeerRegistry, IRemotePeerRegistryListener {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractRemoteController.class);

  private List<RemoteCommand>                                       commandQueue;
  private Map<Class<? extends RemoteCommand>, Map<String, Integer>> commandIndices;

  private IGUIDGenerator<String> guidGenerator;
  private int                    commandLowPriorityOffset;
  private List<String>           removedPeersGuids;
  private IRemotePeerRegistry    remotePeerRegistry;
  // Keep a hard reference on the login view, so that it is not garbage collected.
  @SuppressWarnings("FieldCanBeLocal")
  private IView<RComponent>      loginView;
  private String[]               clientKeysToTranslate;
  private Set<String>            workspaceViews;

  /**
   * Instantiates a new Abstract remote controller.
   */
  public AbstractRemoteController() {
    resetCommandQueue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String registerPermId(String automationsSeed, String guid) {
    return remotePeerRegistry.registerPermId(automationsSeed, guid);
  }

  /**
   * Sets the remotePeerRegistry.
   *
   * @param remotePeerRegistry
   *     the remotePeerRegistry to set.
   * @internal
   */
  public void setRemotePeerRegistry(IRemotePeerRegistry remotePeerRegistry) {
    if (this.remotePeerRegistry != null) {
      this.remotePeerRegistry.removeRemotePeerRegistryListener(this);
    }
    this.remotePeerRegistry = remotePeerRegistry;
    if (this.remotePeerRegistry != null) {
      this.remotePeerRegistry.addRemotePeerRegistryListener(this);
      this.removedPeersGuids = new ArrayList<>();
    }
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
  public synchronized List<RemoteCommand> handleCommands(List<RemoteCommand> commands) {
    try {
      if (commands != null) {
        for (RemoteCommand command : commands) {
          handleCommand(command);
        }
      }
      if (removedPeersGuids != null && removedPeersGuids.size() > 0) {
        RemoteCleanupCommand cleanupCommand = new RemoteCleanupCommand();
        cleanupCommand.setRemovedPeerGuids(removedPeersGuids.toArray(new String[removedPeersGuids.size()]));
        registerCommand(cleanupCommand);
        removedPeersGuids.clear();
      }
    } catch (Exception ex) {
      handleException(ex, null);
    } finally {
      IBackendController bc = getBackendController();
      if (bc != null) {
        bc.cleanupRequestResources();
      }
    }
    return resetCommandQueue();
  }

  private List<RemoteCommand> resetCommandQueue() {
    List<RemoteCommand> copy = commandQueue;
    commandQueue = new ArrayList<>();
    commandIndices = new HashMap<>();
    commandLowPriorityOffset = 0;
    return copy;
  }

  private void clearRequestParams() {
    if (HttpRequestHolder.isAvailable()) {
      HttpSession session = HttpRequestHolder.getServletRequest().getSession();
      session.removeAttribute(RequestParamsHttpFilter.REQUEST_PARAMS_KEY);
    }
  }

  /**
   * Creates the init commands to be sent to the remote peer.
   *
   * @return the init commands to be sent to the remote peer.
   */
  protected List<RemoteCommand> createInitCommands() {
    List<RemoteCommand> initCommands = new ArrayList<>();
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
    List<String> workspaceNames = getWorkspaceNames();
    initCommand.setWorkspaceNames(workspaceNames.toArray(new String[workspaceNames.size()]));
    String[] workspaceDescriptions = new String[workspaceNames.size()];
    for (int i = 0; i < workspaceDescriptions.length; i++) {
      workspaceDescriptions[i] = getWorkspace(workspaceNames.get(i)).getI18nHeaderDescription();
    }
    initCommand.setWorkspaceDescriptions(workspaceDescriptions);
    initCommand.setWorkspaceActions(createRActionList(createWorkspaceActionList(), null));
    if (getActionMap() != null && isAccessGranted(getActionMap())) {
      try {
        pushToSecurityContext(getActionMap());
        initCommand.setActions(createRActionLists(getActionMap(), null));
      } finally {
        restoreLastSecurityContextSnapshot();
      }
    }
    initCommand.setSecondaryActions(createRActionLists(getSecondaryActionMap(), null));
    initCommand.setHelpActions(createRActionLists(getHelpActions(), null));
    initCommand.setNavigationActions(createRActionLists(getNavigationActions(), null));
    initCommand.setExitAction(
        getViewFactory().getActionFactory().createAction(getExitAction(), this, null, getLocale()));
    int w = 0;
    if (getFrameWidth() != null) {
      w = getFrameWidth();
    }
    int h = 0;
    if (getFrameHeight() != null) {
      h = getFrameHeight();
    }
    initCommand.setSize(new Dimension(w, h));
    initCommand.setApplicationName(getI18nName(this, getLocale()));
    initCommand.setApplicationDescription(getI18nDescription(this, getLocale()));
    return initCommand;
  }

  /**
   * Creates the locale command for translations and client locale handling.
   *
   * @return the locale command for translations and client locale handling.
   */
  protected RemoteLocaleCommand createLocaleCommand() {
    Map<String, String> translations = new HashMap<>();
    if (clientKeysToTranslate != null) {
      for (String key : clientKeysToTranslate) {
        translations.put(key, getTranslation(key, getLocale()));
      }
    }
    RemoteLocaleCommand localeCommand = new RemoteLocaleCommand();
    localeCommand.setLanguage(getLocale().getLanguage());
    localeCommand.setDatePattern(getDatePattern(getLocale()));
    localeCommand.setFirstDayOfWeek(getFirstDayOfWeek(getLocale()));
    localeCommand.setDecimalSeparator(getDecimalSeparator(getLocale()));
    localeCommand.setThousandsSeparator(getThousandsSeparator(getLocale()));
    localeCommand.setTranslations(translations);
    return localeCommand;
  }

  /**
   * Handles a single command.
   *
   * @param command
   *     the command to handle.
   */
  protected void handleCommand(RemoteCommand command) {
    if (command instanceof RemoteStartCommand) {
      String[] keysToTranslate = ((RemoteStartCommand) command).getKeysToTranslate();
      if (keysToTranslate != null) {
        clientKeysToTranslate = keysToTranslate;
      }
      if (((RemoteStartCommand) command).getClientType() != null) {
        getApplicationSession().setClientType(EClientType.valueOf(((RemoteStartCommand) command).getClientType()));
      }
      if (isLoginInteractive()) {
        registerCommand(createLocaleCommand());
        RemoteInitLoginCommand initLoginCommand = new RemoteInitLoginCommand();
        loginView = createLoginView();
        initLoginCommand.setLoginView(loginView.getPeer());
        IViewDescriptor loginViewDescriptor = getLoginViewDescriptor();
        initLoginCommand.setLoginActionLists(createRActionLists(loginViewDescriptor.getActionMap(), loginView));
        initLoginCommand.setSecondaryLoginActionLists(
            createRActionLists(loginViewDescriptor.getSecondaryActionMap(), loginView));
        registerCommand(initLoginCommand);
      } else {
        login();
      }
    } else if (command instanceof RemoteWorkspaceDisplayCommand) {
      displayWorkspace(((RemoteWorkspaceDisplayCommand) command).getWorkspaceName(), false);
    } else if (command instanceof RemoteRefreshCommand) {
      // do nothing. Previously buffered commands will simply be sent to the client.
    } else if (command instanceof RemoteHistoryDisplayCommand) {
      displayPinnedModule(((RemoteHistoryDisplayCommand) command).getSnapshotId());
      /*
      ModuleHistoryEntry historyEntry = displayPinnedModule(((RemoteHistoryDisplayCommand) command).getSnapshotId());
      if (historyEntry != null) {
        // Update the name on client side.
        RemoteHistoryDisplayCommand reply = new RemoteHistoryDisplayCommand();
        reply.setName(historyEntry.getName());
        registerCommand(reply);
      }
      */
    } else if (command instanceof RemoteTableChangedCommand) {
      Object[][] columnPrefs = new Object[((RemoteTableChangedCommand) command).getColumnIds().length][2];
      for (int i = 0; i < ((RemoteTableChangedCommand) command).getColumnIds().length; i++) {
        columnPrefs[i] = new Object[]{((RemoteTableChangedCommand) command).getColumnIds()[i],
                                      ((RemoteTableChangedCommand) command).getColumnWidths()[i]};
      }
      getViewFactory().storeTablePreferences(((RemoteTableChangedCommand) command).getTableId(), columnPrefs, this);
    } else {
      IRemotePeer targetPeer = null;
      if (command.getPermId() != null) {
        targetPeer = getRegisteredForPermId(command.getPermId());
      }
      if (targetPeer == null) {
        targetPeer = getRegistered(command.getTargetPeerGuid());
      }
      if (targetPeer == null) {
        LOG.warn("No target peer registered for GUID {} in session {}", command.getTargetPeerGuid(),
            getApplicationSession().getId());
        throw new CommandException(getTranslation("session.unsynced", getApplicationSession().getLocale()));
      }
      if (command instanceof RemoteValueCommand) {
        try {
          if (targetPeer instanceof IFormattedValueConnector) {
            ((IFormattedValueConnector) targetPeer).setFormattedValue(((RemoteValueCommand) command).getValue());
          } else if (targetPeer instanceof IRemoteStateOwner) {
            IRemoteStateOwner remoteStateOwner = (IRemoteStateOwner) targetPeer;
            RemoteValueCommand valueCommand = (RemoteValueCommand) command;
            remoteStateOwner.setValueFromState(valueCommand.getValue());
            if (!ObjectUtils.equals(remoteStateOwner.getState().getValue(), valueCommand.getValue())) {
              // There are rare cases (e.g. due to interceptSetter that resets the command value to the connector
              // actual state), when the connector and the state are not synced.
              valueCommand.setValue(remoteStateOwner.getState().getValue());
              registerCommand(valueCommand);
            }
          } else if (targetPeer instanceof IValueConnector) {
            IValueConnector connector = (IValueConnector) targetPeer;
            RemoteValueCommand valueCommand = (RemoteValueCommand) command;
            connector.setConnectorValue(valueCommand.getValue());
            if (!ObjectUtils.equals(connector.getConnectorValue(), valueCommand.getValue())) {
              // There are rare cases (e.g. due to interceptSetter that resets the command value to the connector
              // actual state), when the connector and the state are not synced.
              valueCommand.setValue(connector.getConnectorValue());
              registerCommand(valueCommand);
            }
          } else {
            throw new CommandException("Target peer type cannot be handled : " + targetPeer.getClass().getName());
          }

          // The following code has been handled at a lower level,
          // see AbstractCompositeValueConnector.propagateRollback
          // if (targetPeer instanceof IRemoteStateOwner) {
          // ((IRemoteStateOwner) targetPeer).synchRemoteState();
          // RemoteValueState state = ((IRemoteStateOwner)
          // targetPeer).getState();
          // if (!ObjectUtils.equals(((RemoteValueCommand) command).getValue(),
          // state.getValue())) {
          //
          // RemoteValueCommand reverseCommand = new RemoteValueCommand();
          // reverseCommand.setTargetPeerGuid(state.getGuid());
          // reverseCommand.setValue(state.getValue());
          // registerCommand(reverseCommand);
          // }
          // }
        } catch (ConnectorInputException ex) {
          if (targetPeer instanceof IRemoteStateOwner) {
            ((IRemoteStateOwner) targetPeer).synchRemoteState();
            RemoteValueState state = ((IRemoteStateOwner) targetPeer).getState();
            if (!ObjectUtils.equals(((RemoteValueCommand) command).getValue(), state.getValue())) {

              RemoteValueCommand rollbackCommand = new RemoteValueCommand();
              rollbackCommand.setTargetPeerGuid(state.getGuid());
              rollbackCommand.setValue(state.getValue());
              registerCommand(rollbackCommand);
            }
          }
        }
      } else if (command instanceof RemoteSelectionCommand) {
        if (targetPeer instanceof RTabContainer) {
          ((RTabContainer) targetPeer).setSelectedIndex(((RemoteSelectionCommand) command).getLeadingIndex());
        } else {
          ISelectable selectable = null;
          if (targetPeer instanceof ICollectionConnectorProvider) {
            selectable = ((ICollectionConnectorProvider) targetPeer).getCollectionConnector();
          } else if (targetPeer instanceof ISelectable) {
            selectable = (ISelectable) targetPeer;
          }
          if (selectable != null) {
            selectable.setSelectedIndices(((RemoteSelectionCommand) command).getSelectedIndices(),
                ((RemoteSelectionCommand) command).getLeadingIndex());
          }
        }
      } else if (command instanceof RemoteActionCommand) {
        RAction action = (RAction) targetPeer;
        // action state might have been changed by previous command. We must re-check it before executing since the
        // client is not aware of it yet.
        // See bug #52
        if (action.isEnabled()) {
          RActionEvent actionEvent = ((RemoteActionCommand) command).getActionEvent();
          String viewStatePermId = actionEvent.getViewStatePermId();
          if (viewStatePermId != null) {
            IRemotePeer viewPeer = getRegisteredForPermId(viewStatePermId);
            if (viewPeer != null) {
              actionEvent.setViewStateGuid(viewPeer.getGuid());
            }
          }
          action.actionPerformed(actionEvent, null);
        }
      } else if (command instanceof RemoteSortCommand) {
        RAction sortAction = (RAction) targetPeer;
        Map<String, String> orderingProperties = ((RemoteSortCommand) command).getOrderingProperties();
        Map<String, ESort> typedOrderingProperties = new LinkedHashMap<>();
        if (orderingProperties != null) {
          for (Map.Entry<String, String> orderingProperty : orderingProperties.entrySet()) {
            typedOrderingProperties.put(orderingProperty.getKey(), ESort.valueOf(orderingProperty.getValue()));
          }
        }
        Map<String, Object> context = new HashMap<>();
        context.put(IQueryComponent.ORDERING_PROPERTIES, typedOrderingProperties);
        String viewStateGuid = null;
        String viewStatePermId = ((RemoteSortCommand) command).getViewStatePermId();
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
        throw new CommandException("Unsupported command type : " + command.getClass().getSimpleName());
      }
    }
  }

  /**
   * Logs into the application.
   */
  @Override
  public void login() {
    if (performLogin()) {
      if (isLoginInteractive()) {
        registerCommand(new RemoteCloseDialogCommand());
      }
      List<RemoteCommand> initCommands = createInitCommands();
      for (RemoteCommand initCommand : initCommands) {
        registerCommand(initCommand);
      }
      userLoggedIn();
      clearRequestParams();
    } else {
      loginFailed();
    }
  }

  /**
   * User logged in.
   */
  protected void userLoggedIn() {
    execute(getStartupAction(), getStartupActionContext());
  }

  /**
   * Callback after a failed login.
   */
  protected void loginFailed() {
    RemoteMessageCommand errorMessageCommand = createErrorMessageCommand();
    errorMessageCommand.setMessage(getTranslation(LoginUtils.LOGIN_FAILED, getLocale()));
    registerCommand(errorMessageCommand);
  }

  /**
   * Create error message command.
   *
   * @return the remote message command
   */
  protected RemoteMessageCommand createErrorMessageCommand() {
    RemoteMessageCommand errorMessageCommand = new RemoteMessageCommand();
    errorMessageCommand.setTitle(getTranslation("error", getLocale()));
    errorMessageCommand.setTitleIcon(getIconFactory().getErrorIcon(getIconFactory().getTinyIconSize()));
    errorMessageCommand.setMessageIcon(getIconFactory().getErrorIcon(getIconFactory().getLargeIconSize()));
    return errorMessageCommand;
  }

  /**
   * Creates an empty error message command.
   *
   * @return a empty error message command.
   */
  protected RemoteErrorMessageCommand createDetailedErrorMessageCommand() {
    RemoteErrorMessageCommand messageCommand = new RemoteErrorMessageCommand();
    messageCommand.setTitle(getTranslation("error", getLocale()));
    messageCommand.setTitleIcon(getIconFactory().getErrorIcon(getIconFactory().getTinyIconSize()));
    messageCommand.setMessageIcon(getIconFactory().getErrorIcon(getIconFactory().getLargeIconSize()));
    return messageCommand;
  }

  private RActionList createRActionList(ActionList actionList, IView<RComponent> view) {
    if (isAccessGranted(actionList)) {
      try {
        pushToSecurityContext(actionList);
        RActionList rActionList = new RActionList(getGuidGenerator().generateGUID());
        rActionList.setName(actionList.getI18nName(this, getLocale()));
        rActionList.setDescription(actionList.getI18nDescription(this, getLocale()));
        rActionList.setIcon(getIconFactory().getIcon(actionList.getIcon(), getIconFactory().getTinyIconSize()));

        List<RAction> actions = new ArrayList<>();
        for (IDisplayableAction action : actionList.getActions()) {
          if (isAccessGranted(action)) {
            try {
              pushToSecurityContext(action);
              actions.add(getViewFactory().getActionFactory().createAction(action, this, view, getLocale()));
            } finally {
              restoreLastSecurityContextSnapshot();
            }
          }
        }
        rActionList.setActions(actions.toArray(new RAction[actions.size()]));
        rActionList.setCollapsable(actionList.isCollapsable());
        return rActionList;
      } finally {
        restoreLastSecurityContextSnapshot();
      }
    }
    return null;
  }

  private RActionList[] createRActionLists(ActionMap actionMap, IView<RComponent> view) {
    List<RActionList> actionLists = new ArrayList<>();
    if (actionMap != null) {
      if (isAccessGranted(actionMap)) {
        try {
          pushToSecurityContext(actionMap);
          for (ActionList actionList : actionMap.getActionLists(this)) {
            if (isAccessGranted(actionList)) {
              try {
                pushToSecurityContext(actionList);
                RActionList rActionList = createRActionList(actionList, view);
                if (rActionList != null) {
                  actionLists.add(rActionList);
                }
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
    return actionLists.toArray(new RActionList[actionLists.size()]);
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
  public void register(IRemotePeer remotePeer) {
    remotePeerRegistry.register(remotePeer);
    if (remotePeer instanceof ICompositeValueConnector) {
      for (String childKey : ((ICompositeValueConnector) remotePeer).getChildConnectorKeys()) {
        IValueConnector childConnector = ((ICompositeValueConnector) remotePeer).getChildConnector(childKey);
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
  public void removeRemotePeerRegistryListener(IRemotePeerRegistryListener listener) {
    remotePeerRegistry.removeRemotePeerRegistryListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerCommand(RemoteCommand command) {
    if (command instanceof RemoteChildrenCommand && ((RemoteChildrenCommand) command).isRemove()) {
      commandQueue.add(commandLowPriorityOffset, command);
      commandLowPriorityOffset++;
    } else {
      if (isIdempotent(command)) {
        Class<? extends RemoteCommand> commandClass = command.getClass();
        Map<String, Integer> guidToIndex = commandIndices.get(commandClass);
        if (guidToIndex == null) {
          guidToIndex = new HashMap<>();
          commandIndices.put(commandClass, guidToIndex);
        }
        String guid = command.getTargetPeerGuid();
        Integer oldIndex = guidToIndex.get(guid);
        if (oldIndex != null) {
          RemoteCommand oldCommand = commandQueue.set(oldIndex + commandLowPriorityOffset, command);
          assert ObjectUtils.equals(oldCommand.getClass(), command.getClass()) : "Different command types";
          assert ObjectUtils.equals(oldCommand.getTargetPeerGuid(), command.getTargetPeerGuid()) :
              "Different command targets";
        } else {
          guidToIndex.put(guid, commandQueue.size() - commandLowPriorityOffset);
          commandQueue.add(command);
        }
      } else {
        commandQueue.add(command);
      }
    }
  }

  /**
   * Is idempotent boolean.
   *
   * @param command
   *     the command
   * @return the boolean
   */
  public boolean isIdempotent(RemoteCommand command) {
    return getRegistered(command.getTargetPeerGuid()) != null;
  }

  /**
   * Sets the guidGenerator.
   *
   * @param guidGenerator
   *     the guidGenerator to set.
   * @internal
   */
  public void setGuidGenerator(IGUIDGenerator<String> guidGenerator) {
    this.guidGenerator = guidGenerator;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unregister(String guid) {
    IRemotePeer remotePeer = getRegistered(guid);
    remotePeerRegistry.unregister(guid);
    if (remotePeer instanceof ICompositeValueConnector) {
      for (String childKey : ((ICompositeValueConnector) remotePeer).getChildConnectorKeys()) {
        unregister(((IRemotePeer) ((ICompositeValueConnector) remotePeer).getChildConnector(childKey)).getGuid());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void remotePeerAdded(IRemotePeer peer) {
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
  public boolean handleException(Throwable ex, Map<String, Object> context) {
    if (super.handleException(ex, context)) {
      return true;
    }
    RemoteMessageCommand errorMessageCommand;
    String userFriendlyExceptionMessage = computeUserFriendlyExceptionMessage(ex);
    if (userFriendlyExceptionMessage != null) {
      errorMessageCommand = createErrorMessageCommand();
      errorMessageCommand.setMessage(userFriendlyExceptionMessage);
    } else {
      errorMessageCommand = createDetailedErrorMessageCommand();
      traceUnexpectedException(ex);
      errorMessageCommand.setMessage(ex.getLocalizedMessage());
      StringWriter stringWriter = new StringWriter();
      ex.printStackTrace(new PrintWriter(stringWriter));
      ((RemoteErrorMessageCommand) errorMessageCommand).setDetailMessage(stringWriter.toString());
    }
    registerCommand(errorMessageCommand);
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void popupInfo(RComponent sourceComponent, String title, String iconImageUrl, String message) {
    RemoteMessageCommand messageCommand = new RemoteMessageCommand();
    messageCommand.setTitle(title);
    messageCommand.setMessage(message);
    messageCommand.setTitleIcon(getIconFactory().getInfoIcon(getIconFactory().getTinyIconSize()));
    if (iconImageUrl != null) {
      messageCommand.setMessageIcon(getIconFactory().getIcon(iconImageUrl, getIconFactory().getLargeIconSize()));
    } else {
      messageCommand.setMessageIcon(getIconFactory().getInfoIcon(getIconFactory().getLargeIconSize()));
    }
    registerCommand(messageCommand);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void popupOkCancel(RComponent sourceComponent, String title, String iconImageUrl, String message,
                            IAction okAction, IAction cancelAction, Map<String, Object> context) {
    RemoteOkCancelCommand messageCommand = new RemoteOkCancelCommand();
    messageCommand.setTitle(title);
    messageCommand.setMessage(message);

    messageCommand.setTitleIcon(getIconFactory().getWarningIcon(getIconFactory().getTinyIconSize()));
    if (iconImageUrl != null) {
      messageCommand.setMessageIcon(getIconFactory().getIcon(iconImageUrl, getIconFactory().getLargeIconSize()));
    } else {
      messageCommand.setMessageIcon(getIconFactory().getWarningIcon(getIconFactory().getLargeIconSize()));
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
  public void popupYesNo(RComponent sourceComponent, String title, String iconImageUrl, String message,
                         IAction yesAction, IAction noAction, Map<String, Object> context) {
    RemoteYesNoCommand messageCommand = new RemoteYesNoCommand();
    messageCommand.setTitle(title);
    messageCommand.setMessage(message);

    messageCommand.setTitleIcon(getIconFactory().getQuestionIcon(getIconFactory().getTinyIconSize()));
    if (iconImageUrl != null) {
      messageCommand.setMessageIcon(getIconFactory().getIcon(iconImageUrl, getIconFactory().getLargeIconSize()));
    } else {
      messageCommand.setMessageIcon(getIconFactory().getQuestionIcon(getIconFactory().getLargeIconSize()));
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
  public void popupYesNoCancel(RComponent sourceComponent, String title, String iconImageUrl, String message,
                               IAction yesAction, IAction noAction, IAction cancelAction, Map<String, Object> context) {
    RemoteYesNoCancelCommand messageCommand = new RemoteYesNoCancelCommand();
    messageCommand.setTitle(title);
    messageCommand.setMessage(message);

    messageCommand.setTitleIcon(getIconFactory().getQuestionIcon(getIconFactory().getTinyIconSize()));
    if (iconImageUrl != null) {
      messageCommand.setMessageIcon(getIconFactory().getIcon(iconImageUrl, getIconFactory().getLargeIconSize()));
    } else {
      messageCommand.setMessageIcon(getIconFactory().getQuestionIcon(getIconFactory().getLargeIconSize()));
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
   * Updates the view factory with the remote peer registry.
   * <p/>
   * {@inheritDoc}
   *
   * @internal
   */
  @Override
  public void setViewFactory(IViewFactory<RComponent, RIcon, RAction> viewFactory) {
    if (viewFactory instanceof AbstractRemoteViewFactory) {
      ((AbstractRemoteViewFactory) viewFactory).setRemoteCommandHandler(this);
      ((AbstractRemoteViewFactory) viewFactory).setRemotePeerRegistry(this);
    }
    IActionFactory<RAction, RComponent> actionFactory = viewFactory.getActionFactory();
    if (actionFactory instanceof RemoteActionFactory) {
      ((RemoteActionFactory) actionFactory).setRemoteCommandHandler(this);
      ((RemoteActionFactory) actionFactory).setRemotePeerRegistry(this);
    }
    IConfigurableConnectorFactory connectorFactory = viewFactory.getConnectorFactory();
    if (connectorFactory instanceof RemoteConnectorFactory) {
      ((RemoteConnectorFactory) connectorFactory).setRemoteCommandHandler(this);
      ((RemoteConnectorFactory) connectorFactory).setRemotePeerRegistry(this);
    }
    super.setViewFactory(viewFactory);
  }

  @SuppressWarnings("unchecked")
  private RAction createRAction(IAction action, Map<String, Object> context) {
    return getViewFactory().getActionFactory().createAction(wrapAction(action, context), this,
        (IView<RComponent>) context.get(ActionContextConstants.VIEW), getLocale());
  }

  private IDisplayableAction wrapAction(IAction action, final Map<String, Object> initialContext) {
    FrontendAction<RComponent, RIcon, RAction> wrapper;
    if (action instanceof IDisplayableAction) {
      wrapper = new FrontendAction<RComponent, RIcon, RAction>() {

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
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
        public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
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
  public void displayUrl(String urlSpec, String target) {
    RemoteOpenUrlCommand openUrlCommand = new RemoteOpenUrlCommand();
    openUrlCommand.setUrlSpec(urlSpec);
    openUrlCommand.setTarget(target);
    registerCommand(openUrlCommand);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayDialog(RComponent mainView, List<RAction> actions, String title, RComponent sourceComponent,
                            Map<String, Object> context, Dimension dimension, boolean reuseCurrent, boolean modal) {
    displayModalDialog(mainView, context, reuseCurrent);
    RemoteDialogCommand dialogCommand = new RemoteDialogCommand();
    dialogCommand.setTitle(title);
    dialogCommand.setView(mainView);
    dialogCommand.setActions(actions.toArray(new RAction[actions.size()]));
    dialogCommand.setUseCurrent(reuseCurrent);
    dialogCommand.setDimension(dimension);
    registerCommand(dialogCommand);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean disposeModalDialog(RComponent sourceWidget, Map<String, Object> context) {
    if (super.disposeModalDialog(sourceWidget, context)) {
      registerCommand(new RemoteCloseDialogCommand());
      transferFocus(context);
      return true;
    }
    return false;
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

  /**
   * Returns a preference store based on Java preferences API.
   * <p/>
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
  public void setStatusInfo(String statusInfo) {
    RemoteUpdateStatusCommand updateStatusCommand = new RemoteUpdateStatusCommand();
    updateStatusCommand.setStatus(statusInfo);
    registerCommand(updateStatusCommand);
  }

  /**
   * Complements with request parameters.
   * <p/>
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
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected Map<String, Object> getStartupActionContext() {
    Map<String, Object> startupActionContext = super.getStartupActionContext();
    completeActionContextWithRequestParameters(startupActionContext);
    return startupActionContext;
  }

  @SuppressWarnings("unchecked")
  private void completeActionContextWithRequestParameters(Map<String, Object> actionContext) {
    if (HttpRequestHolder.isAvailable()) {
      HttpSession session = HttpRequestHolder.getServletRequest().getSession();
      Map<String, Object> requestParams = (Map<String, Object>) session.getAttribute(
          RequestParamsHttpFilter.REQUEST_PARAMS_KEY);
      if (requestParams != null) {
        actionContext.putAll(requestParams);
      }
    }
  }

  /**
   * Notifies the client.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected void pinnedModuleDisplayed(ModuleHistoryEntry historyEntry, boolean addToHistory) {
    super.pinnedModuleDisplayed(historyEntry, addToHistory);
    RemoteHistoryDisplayCommand historyCommand = new RemoteHistoryDisplayCommand();
    if (addToHistory) {
      historyCommand.setSnapshotId(historyEntry.getId());
    }
    historyCommand.setName(historyEntry.getName());
    registerCommand(historyCommand);
  }

  /**
   * Sends a remote workspace display command.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  protected void displayWorkspace(String workspaceName, boolean bypassModuleBoundaryActions) {
    displayWorkspace(workspaceName, bypassModuleBoundaryActions, true);
  }

  /**
   * Sets the workspace as selected and optionally notifies the remote peer.
   *
   * @param workspaceName
   *     the selected workspace name.
   * @param bypassModuleBoundaryActions
   *     should we bypass module onEnter/Exit actions ?
   * @param notifyRemote
   *     if true, a remote notification will be sent to the remote peer.
   */
  protected void displayWorkspace(String workspaceName, boolean bypassModuleBoundaryActions, boolean notifyRemote) {
    if (!ObjectUtils.equals(workspaceName, getSelectedWorkspaceName())) {
      super.displayWorkspace(workspaceName, bypassModuleBoundaryActions);
      if (workspaceViews == null) {
        workspaceViews = new HashSet<>();
      }
      RComponent workspaceView = null;
      if (!workspaceViews.contains(workspaceName)) {
        workspaceViews.add(workspaceName);
        workspaceView = createWorkspaceView(workspaceName);
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
   * Create workspace view.
   *
   * @param workspaceName
   *     the workspace name
   * @return the r component
   */
  protected abstract RComponent createWorkspaceView(String workspaceName);

  /**
   * Gets guid generator.
   *
   * @return the guid generator
   */
  protected IGUIDGenerator<String> getGuidGenerator() {
    return guidGenerator;
  }

  @Override
  public void setName(String name) {
    super.setName(name);
    if (isStarted()) {
      RemoteApplicationDescriptionCommand radCommand = new RemoteApplicationDescriptionCommand();
      radCommand.setApplicationName(getI18nName(this, getLocale()));
      registerCommand(radCommand);
    }
  }
}
