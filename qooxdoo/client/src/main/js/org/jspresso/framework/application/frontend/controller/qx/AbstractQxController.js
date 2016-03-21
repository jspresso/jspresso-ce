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

/**
 * @asset(org/jspresso/framework/*.png)
 */
qx.Class.define("org.jspresso.framework.application.frontend.controller.qx.AbstractQxController", {
  extend: qx.core.Object,

  type: "abstract",

  implement: [org.jspresso.framework.util.remote.registry.IRemotePeerRegistry,
              org.jspresso.framework.action.IActionHandler,
              org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler],

  statics: {
    __JSPRESSO_VERSION: "${jspresso.version}",
    __HANDLE_COMMANDS_METHOD: "handleCommands",
    __START_METHOD: "start",
    __STOP_METHOD: "stop"
  },

  /**
   * @param remoteController {qx.io.remote.Rpc}
   * @param userLanguage {String}
   */
  construct: function (remoteController, userLanguage) {
    this.__remotePeerRegistry = new org.jspresso.framework.util.remote.registry.BasicRemotePeerRegistry();
    this.__viewFactory = this._createViewFactory();
    this._changeNotificationsEnabled = true;
    this.__remoteController = remoteController;
    this.__commandsQueue = [];
    this.__commandsBacklog = [];
    this.__userLanguage = userLanguage;
    this._dialogStack = [];
    this._dialogStack.push([null, null, null]);
    qx.locale.Manager.getInstance().setLocale(this.__userLanguage);
    this._initRemoteController();
  },

  properties: {
    name: {
      check: "String",
      event: "changeName"
    }
  },

  members: {
    /** @type {qx.io.remote.Rpc} */
    __remoteController: null,
    /** @type {org.jspresso.framework.view.qx.AbstractQxViewFactory} */
    __viewFactory: null,
    /** @type {org.jspresso.framework.util.remote.registry.IRemotePeerRegistry} */
    __remotePeerRegistry: null,
    /** @type {Boolean} */
    _changeNotificationsEnabled: null,
    /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteCommand[]} */
    __commandsQueue: null,
    /** @type {Boolean} */
    __roundTrip: false,
    /** @type {Array} */
    __commandsBacklog: null,
    /** @type {Object} */
    __postponedCommands: null,
    /** @type {Object} */
    __postponedChildrenNotificationBuffer: null,
    /** @type {Object} */
    __postponedSelectionCommands: null,
    /** @type {Object} */
    __postponedEditionCommands: null,
    /** @type {String} */
    __userLanguage: null,
    /** @type {Object} */
    __translations: null,
    /** @type {function} */
    __nextActionCallback: null,
    /** @type {Array} */
    _dialogStack: null,

    _getApplication: function () {
      return qx.core.Init.getApplication();
    },

    _createViewFactory: function () {
      throw new Error("_createViewFactory is abstract");
    },

    /**
     * @return {org.jspresso.framework.view.qx.AbstractQxViewFactory}
     */
    _getViewFactory: function () {
      return this.__viewFactory;
    },

    /**
     * @param remotePeer {org.jspresso.framework.util.remote.IRemotePeer}
     * @return {undefined}
     */
    register: function (remotePeer) {
      if (!remotePeer) {
        return;
      }
      if (!this.isRegistered(remotePeer.getGuid())) {
        this.__remotePeerRegistry.register(remotePeer);
        if (remotePeer instanceof org.jspresso.framework.state.remote.RemoteValueState) {
          this._bindRemoteValueState(/** @type {org.jspresso.framework.state.remote.RemoteValueState} */ remotePeer);
          if (remotePeer instanceof org.jspresso.framework.state.remote.RemoteCompositeValueState) {
            if ((/** @type {org.jspresso.framework.state.remote.RemoteValueState} */ remotePeer).getChildren()) {
              var children = (/** @type {org.jspresso.framework.state.remote.RemoteValueState} */ remotePeer).getChildren().toArray();
              for (var i = 0; i < children.length; i++) {
                this.register(children[i]);
              }
            }
          }
        } else if (remotePeer instanceof org.jspresso.framework.gui.remote.RComponent) {
          this.register((/** @type {org.jspresso.framework.gui.remote.RComponent} */ remotePeer).getState());
        }
        if (this.__postponedCommands) {
          if (this.__postponedCommands[remotePeer.getGuid()]) {
            this._handleCommands(this.__postponedCommands[remotePeer.getGuid()]);
            delete this.__postponedCommands[remotePeer.getGuid()];
          }
        }
      }
    },

    /**
     * @param busy {Boolean}
     */
    _showBusy: function (busy) {
      throw new Error("_showBusy is abstract");
    },

    /**
     * @return {undefined}
     */
    _dispatchCommands: function () {
      this._showBusy(true);
      if (!this.__roundTrip) {
        this.__roundTrip = true;
        this.__remoteController.callAsyncListeners(true,
            org.jspresso.framework.application.frontend.controller.qx.AbstractQxController.__HANDLE_COMMANDS_METHOD,
            org.jspresso.framework.util.object.ObjectUtil.untypeObjectGraph(new qx.data.Array(this.__commandsQueue)));
        this.__commandsQueue.length = 0;
      } else {
        for (var i = 0; i < this.__commandsQueue.length; i++) {
          this.__commandsBacklog[i] = this.__commandsQueue[i];
        }
      }
    },

    /**
     * @param remoteValueState {org.jspresso.framework.state.remote.RemoteValueState}
     * @return {undefined}
     */
    _bindRemoteValueState: function (remoteValueState) {
      var wasEnabled = this._changeNotificationsEnabled;
      try {
        this._changeNotificationsEnabled = false;
        remoteValueState.addListener("changeValue", this._valueUpdated, this);
        if (remoteValueState instanceof org.jspresso.framework.state.remote.RemoteCompositeValueState) {
          remoteValueState.addListener("changeSelectedIndices", this._selectedIndicesUpdated, this);
        }
      } catch (e) {
        throw e;
      } finally {
        this._changeNotificationsEnabled = wasEnabled;
      }
    },

    /**
     * @param event {qx.event.type.Data}
     * @return {undefined}
     */
    _selectedIndicesUpdated: function (event) {
      var remoteCompositeValueState = event.getTarget();
      if (this._changeNotificationsEnabled) {
        //this.debug(">>> Selected indices update <<< " + remoteCompositeValueState.getSelectedIndices() + " on " + remoteCompositeValueState.getValue());
        var command = new org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand();
        command.setTargetPeerGuid(remoteCompositeValueState.getGuid());
        command.setPermId(remoteCompositeValueState.getPermId());
        command.setSelectedIndices(remoteCompositeValueState.getSelectedIndices());
        command.setLeadingIndex(remoteCompositeValueState.getLeadingIndex());
        this.registerCommand(command);
      }
    },


    /**
     * @param event {qx.event.type.Data}
     * @return {undefined}
     */
    _valueUpdated: function (event) {
      var remoteValueState = event.getTarget();
      if (this._changeNotificationsEnabled) {
        //this.debug(">>> Value update <<< " + remoteValueState.getValue());
        var command = new org.jspresso.framework.application.frontend.command.remote.RemoteValueCommand();
        command.setTargetPeerGuid(remoteValueState.getGuid());
        command.setPermId(remoteValueState.getPermId());
        command.setValue(remoteValueState.getValue());
        this.registerCommand(command);
      }
    },

    /**
     *
     * @param action {org.jspresso.framework.gui.remote.RAction}
     * @param actionEvent {org.jspresso.framework.gui.remote.RActionEvent}
     * @param actionCallback {function}
     * @return {undefined}
     */
    execute: function (action, actionEvent, actionCallback) {
      actionEvent = (typeof actionEvent == 'undefined') ? null : actionEvent;
      actionCallback = (typeof actionCallback == 'undefined') ? null : actionCallback;
      if (action && action.getEnabled()) {
        //this.debug(">>> Execute <<< " + action.getName() + " param = " + param);
        var command = new org.jspresso.framework.application.frontend.command.remote.RemoteActionCommand();
        command.setTargetPeerGuid(action.getGuid());
        command.setPermId(action.getPermId());
        if (!actionEvent) {
          actionEvent = new org.jspresso.framework.gui.remote.RActionEvent();
        }
        command.setActionEvent(actionEvent);
        this._completeActionEvent(actionEvent);
        this.__nextActionCallback = actionCallback;
        this.registerCommand(command);
      }
    },

    refresh: function () {
      this.registerCommand(new org.jspresso.framework.application.frontend.command.remote.RemoteRefreshCommand());
    },

    /**
     * @param command {org.jspresso.framework.application.frontend.command.remote.RemoteCommand}
     * @return {undefined}
     */
    registerCommand: function (command) {
      if (this._changeNotificationsEnabled) {
        //this.debug("Command registered for next round trip : " + command);
        this.__commandsQueue.push(command);
        this._dispatchCommands();
        this.__commandsQueue.length = 0;
      }
    },

    /**
     * @param commands {org.jspresso.framework.application.frontend.command.remote.RemoteCommand[]}
     * @return {undefined}
     */
    _handleCommands: function (commands) {
      //this.debug("Received commands :");
      var wasEnabled = this._changeNotificationsEnabled;
      try {
        this._changeNotificationsEnabled = false;
        if (commands) {
          for (var i = 0; i < commands.length; i++) {
            //this.debug("  -> " + commands[i]);
            this._handleCommand(commands[i]);
          }
        }
      } catch (e) {
        throw e;
      } finally {
        this._changeNotificationsEnabled = wasEnabled;
      }
    },

    /**
     * Close top most dialog.
     */
    _closeDialog: function () {
      throw new Error("_closeDialog is abstract")
    },

    /**
     * @param remoteUpdateStatusCommand {org.jspresso.framework.application.frontend.command.remote.RemoteUpdateStatusCommand}
     * @return {undefined}
     */
    _updateStatusCommand: function (remoteUpdateStatusCommand) {
      throw new Error("_updateStatusCommand is abstract.");
    },

    /**
     * @param historyDisplayCommand {org.jspresso.framework.application.frontend.command.remote.RemoteHistoryDisplayCommand}
     * @return {undefined}
     */
    _handleHistoryDisplayCommand: function (historyDisplayCommand) {
      throw new Error("_handleHistoryDisplayCommand is abstract.")
    },

    /**
     * @param abstractDialogCommand {org.jspresso.framework.application.frontend.command.remote.RemoteAbstractDialogCommand}
     * @return {undefined}
     */
    _handleDialogCommand: function (abstractDialogCommand) {
      throw new Error("_handleDialogCommand is abstract.")
    },

    /**
     * @param localeCommand {org.jspresso.framework.application.frontend.command.remote.RemoteLocaleCommand}
     * @return {undefined}
     */
    _handleRemoteLocaleCommand: function (localeCommand) {
      qx.locale.Manager.getInstance().setLocale(localeCommand.getLanguage());
      this._getViewFactory().setDatePattern(localeCommand.getDatePattern());
      this._getViewFactory().setFirstDayOfWeek(localeCommand.getFirstDayOfWeek());
      this._getViewFactory().setDecimalSeparator(localeCommand.getDecimalSeparator());
      this._getViewFactory().setThousandsSeparator(localeCommand.getThousandsSeparator());
      this.__translations = localeCommand.getTranslations();
    },

    /**
     * @param cleanupCommand {org.jspresso.framework.application.frontend.command.remote.RemoteCleanupCommand}
     * @return {undefined}
     */
    _handleCleanupCommand: function (cleanupCommand) {
      var removedPeerGuids = cleanupCommand.getRemovedPeerGuids();
      for (var i = 0; i < removedPeerGuids.length; i++) {
        var removedPeer = this.getRegistered(removedPeerGuids[i]);
        if (removedPeer) {
          this.unregister(removedPeer);
        }
      }
    },

    /**
     * @param compositeValueState {org.jspresso.framework.state.remote.RemoteCompositeValueState}
     * @param childrenCommand {org.jspresso.framework.application.frontend.command.remote.RemoteChildrenCommand}
     * @return {undefined}
     */
    _handleChildrenCommand: function (compositeValueState, childrenCommand) {
      this.__postponedChildrenNotificationBuffer[compositeValueState.getGuid()] = null;

      /** @type {qx.data.Array} */
      var children = compositeValueState.getChildren();
      /** @type {Array} */
      var commandChildren = childrenCommand.getChildren().toArray();

      if (childrenCommand.getRemove()) {
        for (var i = 0; i < commandChildren.length; i++) {
          /** @type {org.jspresso.framework.state.remote.RemoteValueState} */
          var child = commandChildren[i];
          if (this.isRegistered(child.getGuid())) {
            child = /** @type {org.jspresso.framework.state.remote.RemoteValueState} */ this.getRegistered(
                child.getGuid());
            children.remove(child);
            child.setParent(null);
          }
        }
      } else {
        var oldLength = children.getLength();
        var newLength = commandChildren.length;

        var args = [];
        args.length = newLength;

        for (var i = 0; i < newLength; i++) {
          /** @type {org.jspresso.framework.state.remote.RemoteValueState} */
          var child = commandChildren[i];
          if (this.isRegistered(child.getGuid())) {
            child = /** @type {org.jspresso.framework.state.remote.RemoteValueState} */ this.getRegistered(
                child.getGuid());
          } else {
            this.register(child);
          }
          args[i] = child;
          child.setParent(compositeValueState);
        }
        if (oldLength > 0) {
          args.unshift(0, oldLength);
          qx.data.Array.prototype.splice.apply(children, args);
        } else {
          qx.data.Array.prototype.push.apply(children, args);
        }
      }
    },

    /**
     * @param valueState {org.jspresso.framework.state.remote.RemoteValueState}
     * @param valueCommand {org.jspresso.framework.application.frontend.command.remote.RemoteValueCommand}
     * @return {undefined}
     */
    _handleValueCommand: function (valueState, valueCommand) {
      valueState.setValue(valueCommand.getValue());
      if (valueState instanceof org.jspresso.framework.state.remote.RemoteFormattedValueState) {
        (/**@type {org.jspresso.framework.state.remote.RemoteFormattedValueState} */valueState).setValueAsObject(
            valueCommand.getValueAsObject());
      } else if (valueState instanceof org.jspresso.framework.state.remote.RemoteCompositeValueState) {
        (/**@type {org.jspresso.framework.state.remote.RemoteCompositeValueState} */valueState).setDescription(
            valueCommand.getDescription());
        (/**@type {org.jspresso.framework.state.remote.RemoteCompositeValueState} */valueState).setIconImageUrl(
            valueCommand.getIconImageUrl());
      }
    },

    /**
     * @param compositeValueState {org.jspresso.framework.state.remote.RemoteCompositeValueState}
     * @param selectionCommand {org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand}
     * @return {undefined}
     */
    _handleSelectionCommand: function (compositeValueState, selectionCommand) {
      if (this.__postponedChildrenNotificationBuffer.hasOwnProperty(compositeValueState.getGuid())) {
        this.__postponedSelectionCommands[compositeValueState.getGuid()] = selectionCommand;
      } else {
        compositeValueState.setLeadingIndex(selectionCommand.getLeadingIndex());
        compositeValueState.setSelectedIndices(selectionCommand.getSelectedIndices());
      }
    },

    /**
     * @param targetPeer {org.jspresso.framework.gui.remote.RComponent}
     * @param addCardCommand {org.jspresso.framework.application.frontend.command.remote.RemoteAddCardCommand}
     * @return {undefined}
     */
    _handleAddCardCommand: function (targetPeer, addCardCommand) {
      throw new Error("_handleAddCardCommand is abstract.");
    },

    /**
     * @param targetPeer {org.jspresso.framework.gui.remote.RComponent}
     * @param focusCommand {org.jspresso.framework.application.frontend.command.remote.RemoteFocusCommand}
     * @return {undefined}
     */
    _handleFocusCommand: function (targetPeer, focusCommand) {
      throw new Error("_handleFocusCommand is abstract.");
    },

    /**
     * @param targetPeer {org.jspresso.framework.gui.remote.RComponent}
     * @param editCommand {org.jspresso.framework.application.frontend.command.remote.RemoteEditCommand}
     * @return {undefined}
     */
    _handleEditCommand: function (targetPeer, editCommand) {
      throw new Error("_handleEditCommand is abstract.");
    },

    /**
     * @param remoteOpenUrlCommand {org.jspresso.framework.application.frontend.command.remote.RemoteOpenUrlCommand}
     * @return {undefined}
     */
    _handleOpenUrlCommand: function (remoteOpenUrlCommand) {
      window.open(remoteOpenUrlCommand.getUrlSpec(), remoteOpenUrlCommand.getTarget());
    },

    /**
     * @param command {org.jspresso.framework.application.frontend.command.remote.RemoteCommand}
     * @return {undefined}
     */
    _handleCommand: function (command) {
      var c;
      if (command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteMessageCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteMessageCommand} */
            command;
        this._handleMessageCommand(c);
      } else if (command
          instanceof org.jspresso.framework.application.frontend.command.remote.RemoteHistoryDisplayCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteHistoryDisplayCommand} */
            command;
        this._handleHistoryDisplayCommand(c);
      } else if (command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteRestartCommand) {
        this._restart();
      } else if (command
          instanceof org.jspresso.framework.application.frontend.command.remote.RemoteFileUploadCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteFileUploadCommand} */
            command;
        this._handleFileUpload(c);
      } else if (command
          instanceof org.jspresso.framework.application.frontend.command.remote.RemoteFileDownloadCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteFileDownloadCommand} */
            command;
        this._handleFileDownload(c);
      } else if (command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteLocaleCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteLocaleCommand} */
            command;
        this._handleRemoteLocaleCommand(c);
      } else if (command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteInitLoginCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteInitLoginCommand} */
            command;
        this._handleInitLoginCommmand(c);
      } else if (command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteCleanupCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteCleanupCommand} */
            command;
        this._handleCleanupCommand(c);
      } else if (command
          instanceof org.jspresso.framework.application.frontend.command.remote.RemoteAbstractDialogCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteAbstractDialogCommand} */
            command;
        this._handleDialogCommand(c);
      } else if (command
          instanceof org.jspresso.framework.application.frontend.command.remote.RemoteCloseDialogCommand) {
        this._closeDialog();
      } else if (command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteInitCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteInitCommand} */
            command;
        this._handleInitCommand(c);
      } else if (command
          instanceof org.jspresso.framework.application.frontend.command.remote.RemoteWorkspaceDisplayCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteWorkspaceDisplayCommand} */
            command;
        this._displayWorkspace(c.getWorkspaceName(), c.getWorkspaceView());
      } else if (command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteOpenUrlCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteOpenUrlCommand} */
            command;
        this._handleOpenUrlCommand(c);
      } else if (command
          instanceof org.jspresso.framework.application.frontend.command.remote.RemoteUpdateStatusCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteUpdateStatusCommand} */
            command;
        this._updateStatusCommand(c);
      } else if (command
          instanceof org.jspresso.framework.application.frontend.command.remote.RemoteApplicationDescriptionCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteApplicationDescriptionCommand} */
            command;
        this.setName(c.getApplicationName());
      } else if (command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteClipboardCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteClipboardCommand} */
            command;
        this._handleClipboardCommand(c);
      } else {
        var targetPeerGuid = command.getTargetPeerGuid();
        var targetPeer = this.getRegistered(targetPeerGuid);
        if (targetPeer == null) {
          if (!(command
              instanceof org.jspresso.framework.application.frontend.command.remote.RemoteEnablementCommand)) {
            if (!this.__postponedCommands[targetPeerGuid]) {
              this.__postponedCommands[targetPeerGuid] = [];
            }
            this.__postponedCommands[targetPeerGuid].push(command);
          }
          return;
        }
        if (command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteValueCommand) {
          c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteValueCommand} */
              command;
          this._handleValueCommand(targetPeer, c);
        } else if (command
            instanceof org.jspresso.framework.application.frontend.command.remote.RemoteReadabilityCommand) {
          c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteReadabilityCommand} */
              command;
          targetPeer.setReadable(c.getReadable());
        } else if (command
            instanceof org.jspresso.framework.application.frontend.command.remote.RemoteWritabilityCommand) {
          c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteWritabilityCommand} */
              command;
          targetPeer.setWritable(c.getWritable());
        } else if (command
            instanceof org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand) {
          c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand} */
              command;
          this._handleSelectionCommand(targetPeer, c);
        } else if (command
            instanceof org.jspresso.framework.application.frontend.command.remote.RemoteEnablementCommand) {
          c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteEnablementCommand} */
              command;
          targetPeer.setEnabled(c.getEnabled());
        } else if (command
            instanceof org.jspresso.framework.application.frontend.command.remote.RemoteChildrenCommand) {
          c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteChildrenCommand} */
              command;
          this._handleChildrenCommand(targetPeer, c);
        } else if (command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteAddCardCommand) {
          c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteAddCardCommand} */
              command;
          this._handleAddCardCommand(targetPeer, c);
        } else if (command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteFocusCommand) {
          c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteFocusCommand} */
              command;
          this._handleFocusCommand(targetPeer, c);
        } else if (command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteEditCommand) {
          c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteEditCommand} */
              command;
          this.__postponedEditionCommands[targetPeerGuid] = c;
        }
      }
    },

    /**
     *
     * @param uploadCommand {org.jspresso.framework.application.frontend.command.remote.RemoteFileUploadCommand}
     */
    _handleFileUpload: function (uploadCommand) {
      throw new Error("_handleFileUpload is abstract.")
    },

    /**
     * @param downloadCommand {org.jspresso.framework.application.frontend.command.remote.RemoteFileDownloadCommand}
     */
    _handleFileDownload: function (downloadCommand) {
      throw new Error("_handleFileDownload is abstract.")
    },

    /**
     * @param clipboardCommand {org.jspresso.framework.application.frontend.command.remote.RemoteClipboardCommand}
     */
    _handleClipboardCommand: function (clipboardCommand) {
      throw new Error("_handleClipboardCommand is abstract.")
    },

    /**
     *
     * @param workspaceName {String}
     * @param workspaceView {org.jspresso.framework.gui.remote.RComponent}
     * @return {undefined}
     */
    _displayWorkspace: function (workspaceName, workspaceView) {
      throw new Error("_displayWorkspace is abstract");
    },

    /**
     * @return {undefined}
     */
    _restart: function () {
      this.__remotePeerRegistry = new org.jspresso.framework.util.remote.registry.BasicRemotePeerRegistry();
      this._changeNotificationsEnabled = true;
      this.__commandsQueue = [];
      this.__commandsBacklog = [];
      this._dialogStack = [];
      this._dialogStack.push([null, null, null]);
      this.start();
    },


    /**
     * @return {undefined}
     */
    start: function () {
      this._showBusy(true);
      var startCommand = new org.jspresso.framework.application.frontend.command.remote.RemoteStartCommand();
      startCommand.setLanguage(this.__userLanguage);
      startCommand.setKeysToTranslate(this._getKeysToTranslate());
      startCommand.setTimezoneOffset(new Date().getTimezoneOffset() * (-60000));
      startCommand.setVersion(
          org.jspresso.framework.application.frontend.controller.qx.AbstractQxController.__JSPRESSO_VERSION);
      startCommand.setClientType(this._getClientType());
      this.__remoteController.callAsyncListeners(true,
          org.jspresso.framework.application.frontend.controller.qx.AbstractQxController.__START_METHOD,
          org.jspresso.framework.util.object.ObjectUtil.untypeObjectGraph(startCommand));
    },

    /**
     * @return {undefined}
     */
    stop: function () {
      this.__remoteController.callAsyncListeners(true,
          org.jspresso.framework.application.frontend.controller.qx.AbstractQxController.__STOP_METHOD);
    },

    /**
     * @return {Array}
     */
    _getKeysToTranslate: function () {
      return ["ok", "cancel", "yes", "no", "detail"];
    },

    translate: function (key) {
      if (this.__translations) {
        var tr = this.__translations[key];
        if (tr != null) {
          if (tr.indexOf("{resource:") >= 0) {
            tr = tr.replace(/\${resource:([^}]*)}/g, function (match, p1) {
              return qx.util.ResourceManager.getInstance().toUri(p1);
            });
          }
          return tr;
        }
      }
      return key;
    },

    /**
     * @param messageCommand {org.jspresso.framework.application.frontend.command.remote.RemoteMessageCommand}
     */
    _handleMessageCommand: function (messageCommand) {
      throw new Error("_handleMessageCommand is abstract")
    },

    /**
     * @param guid {String}
     * @return {org.jspresso.framework.util.remote.IRemotePeer}
     */
    getRegistered: function (guid) {
      return this.__remotePeerRegistry.getRegistered(guid);
    },

    /**
     * @param remotePeer {org.jspresso.framework.util.remote.IRemotePeer}
     * @return {undefined}
     */
    unregister: function (remotePeer) {
      this.__remotePeerRegistry.unregister(remotePeer);
      if (remotePeer instanceof qx.core.Object) {
        (/** @type {qx.core.Object} */ remotePeer).dispose();
      }
    },

    /**
     * @param guid {String}
     * @return {Boolean}
     */
    isRegistered: function (guid) {
      return this.__remotePeerRegistry.isRegistered(guid);
    },

    _handleError: function (message) {
      this.warn("Recieved error : " + message);
    },

    _initRemoteController: function () {
      /**
       * @param result {qx.event.type.Data}
       */
      var commandsHandler = function (result) {
        this.__postponedCommands = {};
        this.__postponedChildrenNotificationBuffer = {};
        this.__postponedSelectionCommands = {};
        this.__postponedEditionCommands = {};
        try {
          var data = result.getData();
          var typedResult = org.jspresso.framework.util.object.ObjectUtil.typeObjectGraph(data["result"]);
          if (typedResult) {
            this._handleCommands(typedResult.toArray());
          }
        } catch (e) {
          throw e;
        } finally {
          this._showBusy(false);
          this.__roundTrip = false;
          this._checkPostponedCommandsCompletion();
          this.__postponedCommands = null;
          this.__postponedChildrenNotificationBuffer = null;
          this.__postponedSelectionCommands = null;
          this.__postponedEditionCommands = null;
          if (this.__nextActionCallback != null) {
            try {
              this.__nextActionCallback();
            } catch (e) {
              //noinspection ThrowInsideFinallyBlockJS
              throw e;
            } finally {
              this.__nextActionCallback = null;
            }
          }
          if (this.__commandsBacklog.length > 0) {
            for (var i = 0; i < this.__commandsBacklog.length; i++) {
              this.__commandsQueue.push(this.__commandsBacklog[i]);
            }
            this.__commandsBacklog.length = 0;
            this._dispatchCommands();
          }
        }
      };

      /**
       * @param ex {qx.event.type.Data}
       */
      var errorHandler = function (ex) {
        this._showBusy(false);
        this.__roundTrip = false;
        this._handleError(ex.getData().toString());
        if (this.__commandsBacklog.length > 0) {
          for (var i = 0; i < this.__commandsBacklog.length; i++) {
            this.__commandsQueue.push(this.__commandsBacklog[i]);
          }
          this.__commandsBacklog.length = 0;
          this._dispatchCommands();
        }
      };

      this.__remoteController.addListener("completed", commandsHandler, this);
      this.__remoteController.addListener("failed", errorHandler, this);
    },

    /**
     * @return {undefined}
     */
    _checkPostponedCommandsCompletion: function () {
      for (var guid in this.__postponedCommands) {
        if (this.__postponedCommands.hasOwnProperty(guid)) {
          /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteCommand[]} */
          var commands = this.__postponedCommands[guid];
          for (var i = 0; i < commands.length; i++) {
            var command = commands[i];
            this._handleError("Target remote peer could not be retrieved :");
            this._handleError("  guid    = " + command.getTargetPeerGuid());
            this._handleError("  command = " + command);
            if (command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteValueCommand) {
              this._handleError("  value   = " + command.getValue());
            } else if (command
                instanceof org.jspresso.framework.application.frontend.command.remote.RemoteChildrenCommand) {
              /** @type {org.jspresso.framework.state.remote.RemoteValueState[]} */
              var children = command.getChildren().toArray();
              for (var j = 0; j < children.length; j++) {
                var childState = children[j];
                this._handleError("  child = " + childState);
                this._handleError("    guid  = " + childState.getGuid());
                this._handleError("    value = " + childState.getValue());
              }
            }
          }
        }
      }
      for (var guid in this.__postponedChildrenNotificationBuffer) {
        //noinspection JSUnfilteredForInLoop
        var peer = this.getRegistered(guid);
        if (peer instanceof org.jspresso.framework.state.remote.RemoteCompositeValueState) {
          peer.notifyChildrenChanged();
          if (this.__postponedSelectionCommands.hasOwnProperty(guid)) {
            var delayedSelectionCommand = this.__postponedSelectionCommands[guid];
            peer.setLeadingIndex(delayedSelectionCommand.getLeadingIndex());
            peer.setSelectedIndices(delayedSelectionCommand.getSelectedIndices());
          }
        }
      }
      for (var guid in this.__postponedEditionCommands) {
        var delayedEditionCommand = this.__postponedEditionCommands[guid];
        this._handleEditCommand(this.getRegistered(delayedEditionCommand.getTargetPeerGuid()));
      }
    },

    /**
     *
     * @param viewStateGuid {String}
     * @param viewStatePermId {String}
     */
    setCurrentViewStateGuid: function (viewStateGuid, viewStatePermId) {
      this._dialogStack[this._dialogStack.length - 1][1] = viewStateGuid;
      this._dialogStack[this._dialogStack.length - 1][2] = viewStatePermId;
    },

    /**
     * @param actionEvent {org.jspresso.framework.gui.remote.RActionEvent}
     * @return {undefined}
     */
    _completeActionEvent: function (actionEvent) {
      actionEvent.setViewStateGuid(this._dialogStack[this._dialogStack.length - 1][1]);
      actionEvent.setViewStatePermId(this._dialogStack[this._dialogStack.length - 1][2]);
    },

    /**
     * @param loginCommand {org.jspresso.framework.application.frontend.command.remote.RemoteInitLoginCommand}
     */
    _handleInitLoginCommmand: function (loginCommand) {
      var loginView = loginCommand.getLoginView();
      this._popupDialog(loginView.getLabel(), loginView.getToolTip(), loginView, loginView.getIcon(),
          this._getViewFactory().extractAllActions(loginCommand.getLoginActionLists()), false, null,
          loginCommand.getSecondaryLoginActionLists());
    },

    /**
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @return {qx.ui.core.Widget}
     */
    createComponent: function (remoteComponent) {
      return this._getViewFactory().createComponent(remoteComponent, true);
    },

    /**
     * @param initCommand {org.jspresso.framework.application.frontend.command.remote.RemoteInitCommand}
     * @return {undefined}
     */
    _handleInitCommand: function (initCommand) {
      this.setName(initCommand.getApplicationName());
      this._initApplicationFrame(initCommand);
    }
  }
});
