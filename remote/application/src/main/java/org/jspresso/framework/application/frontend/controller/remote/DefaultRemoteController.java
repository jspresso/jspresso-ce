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
package org.jspresso.framework.application.frontend.controller.remote;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jspresso.framework.application.frontend.command.remote.CommandException;
import org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler;
import org.jspresso.framework.application.frontend.command.remote.RemoteActionCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteChildrenCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteCloseDialogCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteDialogCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteInitCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteInitLoginCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteLocaleCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteLoginCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteMessageCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteOpenUrlCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteRestartCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteStartCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteValueCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteWorkspaceDisplayCommand;
import org.jspresso.framework.application.frontend.controller.AbstractFrontendController;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IConfigurableConnectorFactory;
import org.jspresso.framework.binding.IFormattedValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.remote.RemoteConnectorFactory;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RActionList;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.RIcon;
import org.jspresso.framework.util.event.ISelectable;
import org.jspresso.framework.util.exception.BusinessException;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.lang.ObjectUtils;
import org.jspresso.framework.util.remote.IRemotePeer;
import org.jspresso.framework.util.remote.registry.IRemotePeerRegistry;
import org.jspresso.framework.util.security.LoginUtils;
import org.jspresso.framework.util.uid.IGUIDGenerator;
import org.jspresso.framework.view.IActionFactory;
import org.jspresso.framework.view.IIconFactory;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.IViewFactory;
import org.jspresso.framework.view.action.ActionList;
import org.jspresso.framework.view.action.ActionMap;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.remote.DefaultRemoteViewFactory;
import org.jspresso.framework.view.remote.RemoteActionFactory;
import org.springframework.dao.ConcurrencyFailureException;

/**
 * Default implementation of a remote frontend controller. This implementation
 * is usable "as-is".
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
public class DefaultRemoteController extends
    AbstractFrontendController<RComponent, RIcon, RAction> implements
    IRemoteCommandHandler, IRemotePeerRegistry {

  private int                 commandLowPriorityOffset;
  private List<RemoteCommand> commandQueue;
  private boolean             commandRegistrationEnabled;
  private IGUIDGenerator      guidGenerator;
  private IRemotePeerRegistry remotePeerRegistry;
  private Set<String>         workspaceViews;

  /**
   * Constructs a new <code>DefaultRemoteController</code> instance.
   */
  public DefaultRemoteController() {
    commandLowPriorityOffset = 0;
    commandRegistrationEnabled = false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clear() {
    remotePeerRegistry.clear();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void displayModalDialog(RComponent mainView, List<RAction> actions,
      String title, RComponent sourceComponent, Map<String, Object> context,
      Dimension dimension, boolean reuseCurrent) {
    super.displayModalDialog(mainView, actions, title, sourceComponent,
        context, dimension, reuseCurrent);
    RemoteDialogCommand dialogCommand = new RemoteDialogCommand();
    dialogCommand.setTitle(title);
    dialogCommand.setView(mainView);
    dialogCommand.setActions(actions.toArray(new RAction[0]));
    dialogCommand.setUseCurrent(reuseCurrent);
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
  public void displayWorkspace(String workspaceName) {
    RemoteWorkspaceDisplayCommand workspaceDisplayCommand = new RemoteWorkspaceDisplayCommand();
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
        IView<RComponent> workspaceView = createWorkspaceView(workspaceName,
            workspaceViewDescriptor, (Workspace) workspaceConnector
                .getConnectorValue());
        workspaceViews.add(workspaceName);
        workspaceDisplayCommand.setWorkspaceView(workspaceView.getPeer());
        getMvcBinder().bind(workspaceView.getConnector(), workspaceConnector);
      }
      workspaceDisplayCommand.setWorkspaceName(workspaceName);
      registerCommand(workspaceDisplayCommand);
    }
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
  public IRemotePeer getRegistered(String guid) {
    return remotePeerRegistry.getRegistered(guid);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<RemoteCommand> handleCommands(List<RemoteCommand> commands) {
    try {
      commandRegistrationEnabled = true;
      commandQueue = new ArrayList<RemoteCommand>();
      commandLowPriorityOffset = 0;
      if (commands != null) {
        for (RemoteCommand command : commands) {
          handleCommand(command);
        }
      }
    } catch (Throwable ex) {
      ex.printStackTrace();
    } finally {
      commandRegistrationEnabled = false;
    }
    return commandQueue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean handleException(Throwable ex, Map<String, Object> context) {
    ex.printStackTrace();
    if (super.handleException(ex, context)) {
      return true;
    }
    RemoteMessageCommand messageCommand = createErrorMessageCommand();
    if (ex instanceof SecurityException) {
      messageCommand.setMessage(ex.getMessage());
    } else if (ex instanceof BusinessException) {
      messageCommand.setMessage(((BusinessException) ex).getI18nMessage(
          getTranslationProvider(), getLocale()));
    } else if (ex instanceof ConcurrencyFailureException) {
      messageCommand.setMessage(getTranslationProvider().getTranslation(
          "concurrency.error.description", getLocale()));
    } else {
      messageCommand.setMessage(ex.getLocalizedMessage());
    }
    registerCommand(messageCommand);
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isRegistered(String guid) {
    return remotePeerRegistry.isRegistered(guid);
  }

  /**
   * {@inheritDoc}
   */
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
  public void registerCommand(RemoteCommand command) {
    if (commandRegistrationEnabled) {
      if (command instanceof RemoteChildrenCommand) {
        // The remote children commands, that may create and register
        // remote server peers on client side must be handled first.
        commandQueue.add(commandLowPriorityOffset, command);
        commandLowPriorityOffset++;
      } else {
        commandQueue.add(command);
      }
    }
  }

  /**
   * Sets the guidGenerator.
   * 
   * @param guidGenerator
   *          the guidGenerator to set.
   */
  public void setGuidGenerator(IGUIDGenerator guidGenerator) {
    this.guidGenerator = guidGenerator;
  }

  /**
   * Sets the remotePeerRegistry.
   * 
   * @param remotePeerRegistry
   *          the remotePeerRegistry to set.
   */
  public void setRemotePeerRegistry(IRemotePeerRegistry remotePeerRegistry) {
    this.remotePeerRegistry = remotePeerRegistry;
  }

  /**
   * Updates the view factory with the remote peer registry.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void setViewFactory(
      IViewFactory<RComponent, RIcon, RAction> viewFactory) {
    if (viewFactory instanceof DefaultRemoteViewFactory) {
      ((DefaultRemoteViewFactory) viewFactory).setRemoteCommandHandler(this);
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
    if (remotePeerRegistry != null) {
      remotePeerRegistry.clear();
    }
    if (workspaceViews != null) {
      workspaceViews.clear();
    }
    registerCommand(new RemoteRestartCommand());
    return super.stop();
  }

  /**
   * {@inheritDoc}
   */
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
    RemoteLocaleCommand localeCommand = new RemoteLocaleCommand();
    localeCommand.setLanguage(getLocale().getLanguage());
    initCommands.add(localeCommand);
    RemoteInitCommand initCommand = new RemoteInitCommand();
    initCommand
        .setWorkspaceActions(createRActionLists(createWorkspaceActionMap()));
    initCommand.setActions(createRActionLists(getActionMap()));
    initCommand.setHelpActions(createRActionLists(getHelpActions()));
    initCommands.add(initCommand);
    return initCommands;
  }

  /**
   * Handles a single command.
   * 
   * @param command
   *          the command to handle.
   */
  protected void handleCommand(RemoteCommand command) {
    if (command instanceof RemoteStartCommand) {
      if (getLoginContextName() != null) {
        RemoteInitLoginCommand initLoginCommand = new RemoteInitLoginCommand();
        IView<RComponent> loginView = createLoginView();
        initLoginCommand.setLoginView(loginView.getPeer());
        initLoginCommand.setTitle(getLoginViewDescriptor().getI18nName(
            getTranslationProvider(), getLocale()));
        initLoginCommand.setMessage(getTranslationProvider().getTranslation(
            LoginUtils.CRED_MESSAGE, getLocale()));
        initLoginCommand.setOkLabel(getTranslationProvider().getTranslation(
            "ok", getLocale()));
        initLoginCommand.setOkIcon(getIconFactory().getOkYesIcon(
            IIconFactory.SMALL_ICON_SIZE));
        registerCommand(initLoginCommand);
      } else {
        performLogin();
        execute(getStartupAction(), getInitialActionContext());
        List<RemoteCommand> initCommands = createInitCommands();
        for (RemoteCommand initCommand : initCommands) {
          registerCommand(initCommand);
        }
      }
    } else if (command instanceof RemoteLoginCommand) {
      if (performLogin()) {
        execute(getStartupAction(), getInitialActionContext());
        registerCommand(new RemoteCloseDialogCommand());
        List<RemoteCommand> initCommands = createInitCommands();
        for (RemoteCommand initCommand : initCommands) {
          registerCommand(initCommand);
        }
      } else {
        RemoteMessageCommand errorMessageCommand = createErrorMessageCommand();
        errorMessageCommand.setMessage(getTranslationProvider().getTranslation(
            LoginUtils.LOGIN_FAILED, getLocale()));
        registerCommand(errorMessageCommand);
      }
    } else {
      IRemotePeer targetPeer = getRegistered(command.getTargetPeerGuid());
      if (targetPeer == null) {
        throw new CommandException("Target remote peer could not be retrieved");
      }
      if (command instanceof RemoteValueCommand) {
        if (targetPeer instanceof IFormattedValueConnector) {
          ((IFormattedValueConnector) targetPeer)
              .setConnectorValueAsString((String) ((RemoteValueCommand) command)
                  .getValue());
        } else {
          ((IValueConnector) targetPeer)
              .setConnectorValue(((RemoteValueCommand) command).getValue());
        }
      } else if (command instanceof RemoteSelectionCommand) {
        ISelectable selectable;
        if (targetPeer instanceof ICollectionConnectorProvider) {
          selectable = ((ICollectionConnectorProvider) targetPeer)
              .getCollectionConnector();
        } else {
          selectable = (ISelectable) targetPeer;
        }
        selectable.setSelectedIndices(((RemoteSelectionCommand) command)
            .getSelectedIndices(), ((RemoteSelectionCommand) command)
            .getLeadingIndex());
      } else if (command instanceof RemoteActionCommand) {
        RAction action = (RAction) targetPeer;
        action.actionPerformed(((RemoteActionCommand) command).getParameter(),
            ((RemoteActionCommand) command).getViewStateGuid());
      } else {
        throw new CommandException("Unsupported command type : "
            + command.getClass().getSimpleName());
      }
    }
  }

  private RemoteMessageCommand createErrorMessageCommand() {
    RemoteMessageCommand messageCommand = new RemoteMessageCommand();
    messageCommand.setTitle(getTranslationProvider().getTranslation("error",
        getLocale()));
    messageCommand.setTitleIcon(getIconFactory().getErrorIcon(
        IIconFactory.TINY_ICON_SIZE));
    messageCommand.setMessageIcon(getIconFactory().getErrorIcon(
        IIconFactory.LARGE_ICON_SIZE));
    return messageCommand;
  }

  private RActionList createRActionList(ActionList actionList) {
    RActionList rActionList = new RActionList(guidGenerator.generateGUID());
    rActionList.setName(actionList.getI18nName(getTranslationProvider(),
        getLocale()));
    rActionList.setDescription(actionList.getI18nDescription(
        getTranslationProvider(), getLocale()));
    rActionList.setIcon(getIconFactory().getIcon(actionList.getIconImageURL(),
        IIconFactory.TINY_ICON_SIZE));

    List<RAction> actions = new ArrayList<RAction>();
    for (IDisplayableAction action : actionList.getActions()) {
      actions.add(getViewFactory().getActionFactory().createAction(action,
          this, null, null, null, getLocale()));
    }
    rActionList.setActions(actions.toArray(new RAction[0]));
    return rActionList;
  }

  private RActionList[] createRActionLists(ActionMap actionMap) {
    List<RActionList> actionLists = new ArrayList<RActionList>();
    if (actionMap != null) {
      for (ActionList actionList : actionMap.getActionLists()) {
        actionLists.add(createRActionList(actionList));
      }
    }
    return actionLists.toArray(new RActionList[0]);
  }

}
