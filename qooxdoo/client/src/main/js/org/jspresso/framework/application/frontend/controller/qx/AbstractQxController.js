/**
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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
 */
qx.Class.define("org.jspresso.framework.application.frontend.controller.qx.AbstractQxController", {
  extend: qx.core.Object,

  type : "abstract",

  implement: [org.jspresso.framework.util.remote.registry.IRemotePeerRegistry,
              org.jspresso.framework.action.IActionHandler,
              org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler],

  statics: {
    __JSPRESSO_VERSION: "${jspresso.version}",
    __HANDLE_COMMANDS_METHOD: "handleCommands",
    __START_METHOD: "start",
    __STOP_METHOD: "stop"
  },

  construct: function (application, remoteController, userLanguage) {
    this._application = application;
    this.__remotePeerRegistry = new org.jspresso.framework.util.remote.registry.BasicRemotePeerRegistry();
    this._viewFactory = this._createViewFactory();
    this.__changeNotificationsEnabled = true;
    this.__remoteController = remoteController;
    this.__commandsQueue = [];
    this.__commandsBacklog = [];
    this._dialogStack = [];
    this._dialogStack.push([null, null, null]);
    this.__userLanguage = userLanguage;
    qx.locale.Manager.getInstance().setLocale(this.__userLanguage);
    this._initRemoteController();
  },

  members: {
    /** @type {qx.application.Standalone | qx.application.Mobile} */
    _application: null,
    /** @type {qx.ui.embed.Iframe} */
    __dlFrame: null,
    /** @type {qx.io.remote.Rpc} */
    __remoteController: null,
    /** @type {org.jspresso.framework.view.qx.AbstractQxViewFactory} */
    _viewFactory: null,
    /** @type {org.jspresso.framework.util.remote.registry.IRemotePeerRegistry} */
    __remotePeerRegistry: null,
    /** @type {Boolean} */
    __changeNotificationsEnabled: null,
    /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteCommand[]} */
    __commandsQueue: null,
    /** @type {Boolean} */
    __roundTrip: false,
    /** @type {Array} */
    __commandsBacklog: null,
    /** @type {qx.ui.basic.Label} */
    __statusBar: null,
    /** @type {Object} */
    __postponedCommands: null,
    /** @type {Object} */
    __postponedNotificationBuffer: null,
    /** @type {Array} */
    _dialogStack: null,
    /** @type {String} */
    __userLanguage: null,
    /** @type {Object} */
    __translations: null,

    __nextActionCallback: null,
    __lastReceivedSnapshotId: null,

    _createViewFactory: function () {
      throw new Error("_createViewFactory is abstract");
    },

    /**
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @return {qx.ui.core.Widget}
     */
    createComponent: function (remoteComponent) {
      return this._viewFactory.createComponent(remoteComponent, true);
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
    _showBusy: function(busy) {
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
            org.jspresso.framework.util.object.ObjectUtil.
                untypeObjectGraph(new qx.data.Array(this.__commandsQueue)));
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
      var wasEnabled = this.__changeNotificationsEnabled;
      try {
        this.__changeNotificationsEnabled = false;
        remoteValueState.addListener("changeValue", this._valueUpdated, this);
        if (remoteValueState instanceof org.jspresso.framework.state.remote.RemoteCompositeValueState) {
          remoteValueState.addListener("changeSelectedIndices", this._selectedIndicesUpdated, this);
        }
      } catch (e) {
        throw e;
      } finally {
        this.__changeNotificationsEnabled = wasEnabled;
      }
    },

    /**
     * @param event {qx.event.type.Data}
     * @return {undefined}
     */
    _selectedIndicesUpdated: function (event) {
      var remoteCompositeValueState = event.getTarget();
      if (this.__changeNotificationsEnabled) {
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
      if (this.__changeNotificationsEnabled) {
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
        actionEvent.setViewStateGuid(this._dialogStack[this._dialogStack.length - 1][1]);
        actionEvent.setViewStatePermId(this._dialogStack[this._dialogStack.length - 1][2]);
        this.__nextActionCallback = actionCallback;
        this.registerCommand(command);
      }
    },

    /**
     * @param command {org.jspresso.framework.application.frontend.command.remote.RemoteCommand}
     * @return {undefined}
     */
    registerCommand: function (command) {
      if (this.__changeNotificationsEnabled) {
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
      var wasEnabled = this.__changeNotificationsEnabled;
      try {
        this.__changeNotificationsEnabled = false;
        if (commands) {
          for (var i = 0; i < commands.length; i++) {
            //this.debug("  -> " + commands[i]);
            this._handleCommand(commands[i]);
          }
        }
      } catch (e) {
        throw e;
      } finally {
        this.__changeNotificationsEnabled = wasEnabled;
      }
    },

    /**
     * @param loginCommand {org.jspresso.framework.application.frontend.command.remote.RemoteInitLoginCommand}
     */
    _handleInitLoginCommmand: function (loginCommand) {
      var loginButton = this._viewFactory.createButton(loginCommand.getOkLabel(), null, loginCommand.getOkIcon());
      this._viewFactory.addButtonListener(loginButton, function (event) {
        this._performLogin();
      }, this);
      var loginButtons = [];
      loginButtons.push(loginButton);
      var dialogView = this.createComponent(loginCommand.getLoginView());
      this._popupDialog(loginCommand.getTitle(), loginCommand.getMessage(), dialogView, loginCommand.getLoginView().getIcon(), loginButtons);
    },

    /**
     * Close top most dialog.
     */
    _closeDialog: function () {
      throw new Error("_closeDialog is abstract")
    },

    /**
     *
     * @param command {org.jspresso.framework.application.frontend.command.remote.RemoteCommand}
     * @return {undefined}
     */
    _handleCommand: function (command) {
      var c;
      if (command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteMessageCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteMessageCommand } */
            command;
        this._handleMessageCommand(c);
      } else if (command
          instanceof org.jspresso.framework.application.frontend.command.remote.RemoteHistoryDisplayCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteHistoryDisplayCommand } */
            command;
        if (c.getSnapshotId()) {
          this.__lastReceivedSnapshotId = c.getSnapshotId();
          qx.bom.History.getInstance().addToHistory("snapshotId=" + c.getSnapshotId(), c.getName());
        } else if (c.getName()) {
          qx.bom.History.getInstance().setTitle(c.getName());
        }
      } else if (command
          instanceof org.jspresso.framework.application.frontend.command.remote.RemoteRestartCommand) {
        this._restart();
      } else if (command
          instanceof org.jspresso.framework.application.frontend.command.remote.RemoteFileUploadCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteFileUploadCommand } */
            command;
        this._handleFileUpload(c);
      } else if (command
          instanceof org.jspresso.framework.application.frontend.command.remote.RemoteFileDownloadCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteFileDownloadCommand } */
            command;
        this._handleFileDownload(c);
      } else if (command
          instanceof org.jspresso.framework.application.frontend.command.remote.RemoteLocaleCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteLocaleCommand } */
            command;
        qx.locale.Manager.getInstance().setLocale(c.getLanguage());
        this._viewFactory.setDatePattern(c.getDatePattern());
        this.__translations = c.getTranslations();
      } else if (command
          instanceof org.jspresso.framework.application.frontend.command.remote.RemoteInitLoginCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteInitLoginCommand } */
            command;
        this._handleInitLoginCommmand(c);
      } else if (command
          instanceof org.jspresso.framework.application.frontend.command.remote.RemoteCleanupCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteCleanupCommand } */
            command;
        var removedPeerGuids = c.getRemovedPeerGuids();
        for (var i = 0; i < removedPeerGuids.length; i++) {
          var removedPeer = this.getRegistered(removedPeerGuids[i]);
          if (removedPeer) {
            this.unregister(removedPeer);
          }
        }
      } else if (command
          instanceof org.jspresso.framework.application.frontend.command.remote.RemoteAbstractDialogCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteAbstractDialogCommand } */
            command;
        var dialogButtons = [];
        for (var i = 0; i < c.getActions().length; i++) {
          dialogButtons.push(this._viewFactory.createAction(c.getActions()[i]));
        }
        var dialogView;
        var icon;
        if (command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteDialogCommand) {
          c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteDialogCommand } */
              command;
          dialogView = this.createComponent(c.getView());
          icon = c.getView().getIcon();
        } else if (command
            instanceof org.jspresso.framework.application.frontend.command.remote.RemoteFlashDisplayCommand) {
          c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteFlashDisplayCommand } */
              command;
          dialogView = new qx.ui.embed.Flash(c.getSwfUrl());
          var flashVars = {};
          for (var i = 0; i < c.getParamNames().length; i++) {
            flashVars[c.getParamNames()[i]] = c.getParamValues()[i];
          }
          dialogView.setVariables(flashVars);
        }
        this._popupDialog(c.getTitle(), null, dialogView, icon, dialogButtons, c.getUseCurrent(), c.getDimension());
      } else if (command
          instanceof org.jspresso.framework.application.frontend.command.remote.RemoteCloseDialogCommand) {
        this._closeDialog();
      } else if (command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteInitCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteInitCommand } */
            command;
        this.linkBowserHistory();
        this._initApplicationFrame(c.getWorkspaceNames(), c.getWorkspaceActions(), c.getExitAction(),
            c.getNavigationActions(), c.getActions(), c.getSecondaryActions(), c.getHelpActions(), c.getSize());
      } else if (command
          instanceof org.jspresso.framework.application.frontend.command.remote.RemoteWorkspaceDisplayCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteWorkspaceDisplayCommand } */
            command;
        this._displayWorkspace(c.getWorkspaceName(), c.getWorkspaceView());
      } else if (command
          instanceof org.jspresso.framework.application.frontend.command.remote.RemoteOpenUrlCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteOpenUrlCommand } */
            command;
        window.open(c.getUrlSpec(), c.getTarget());
      } else if (command
          instanceof org.jspresso.framework.application.frontend.command.remote.RemoteUpdateStatusCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteUpdateStatusCommand } */
            command;
        var status = c.getStatus();
        if (status != null && status.length > 0) {
          this.__statusBar.setValue(status);
          this.__statusBar.setVisibility("visible");
        } else {
          this.__statusBar.setVisibility("excluded");
        }
      } else if (command
          instanceof org.jspresso.framework.application.frontend.command.remote.RemoteClipboardCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteClipboardCommand } */
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
          c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteValueCommand } */
              command;
          targetPeer.setValue(c.getValue());
          if (targetPeer instanceof org.jspresso.framework.state.remote.RemoteFormattedValueState) {
            targetPeer.setValueAsObject(c.getValueAsObject());
          } else if (targetPeer instanceof org.jspresso.framework.state.remote.RemoteCompositeValueState) {
            targetPeer.setDescription(c.getDescription());
            targetPeer.setIconImageUrl(c.getIconImageUrl());
          }
        } else if (command
            instanceof org.jspresso.framework.application.frontend.command.remote.RemoteReadabilityCommand) {
          c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteReadabilityCommand } */
              command;
          targetPeer.setReadable(c.getReadable());
        } else if (command
            instanceof org.jspresso.framework.application.frontend.command.remote.RemoteWritabilityCommand) {
          c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteWritabilityCommand } */
              command;
          targetPeer.setWritable(c.getWritable());
        } else if (command
            instanceof org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand) {
          c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand } */
              command;
          if (targetPeer instanceof org.jspresso.framework.gui.remote.RTabContainer) {
            targetPeer.setSelectedIndex(c.getLeadingIndex());
          } else {
            targetPeer.setLeadingIndex(c.getLeadingIndex());
            targetPeer.setSelectedIndices(c.getSelectedIndices());
          }
        } else if (command
            instanceof org.jspresso.framework.application.frontend.command.remote.RemoteEnablementCommand) {
          c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteEnablementCommand } */
              command;
          targetPeer.setEnabled(c.getEnabled());
        } else if (command
            instanceof org.jspresso.framework.application.frontend.command.remote.RemoteChildrenCommand) {
          c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteChildrenCommand } */
              command;
          this.__postponedNotificationBuffer[targetPeer.getGuid()] = null;

          /** @type {qx.data.Array } */
          var children = targetPeer.getChildren();
          /** @type {Array} */
          var childrenContent = children.toArray();
          /** @type {Array} */
          var commandChildren = c.getChildren().toArray();

          if (c.getRemove()) {
            for (var i = 0; i < commandChildren.length; i++) {
              /** @type {org.jspresso.framework.state.remote.RemoteValueState } */
              var child = commandChildren[i];
              if (this.isRegistered(child.getGuid())) {
                child = /** @type {org.jspresso.framework.state.remote.RemoteValueState } */ this.getRegistered(child.getGuid());
                children.remove(child);
              }
            }
          } else {
            var oldLength = childrenContent.length;
            var newLength = commandChildren.length;

            for (var i = 0; i < commandChildren.length; i++) {
              /** @type {org.jspresso.framework.state.remote.RemoteValueState } */
              var child = commandChildren[i];
              if (this.isRegistered(child.getGuid())) {
                child = /** @type {org.jspresso.framework.state.remote.RemoteValueState } */ this.getRegistered(child.getGuid());
              } else {
                this.register(child);
              }
              var existingChild = null;
              if (i < childrenContent.length) {
                existingChild = childrenContent[i];
              }
              if (existingChild != child) {
                childrenContent[i] = child;
              }
            }
            childrenContent.length = newLength;
            children.length = newLength;
            children.fireDataEvent("changeLength", oldLength, newLength);
            children.fireDataEvent("change", {
              start: 0,
              end: newLength,
              type: "add",
              items: childrenContent
            }, null);
          }
        } else if (command
            instanceof org.jspresso.framework.application.frontend.command.remote.RemoteAddCardCommand) {
          c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteAddCardCommand } */
              command;
          this._viewFactory.addCard(targetPeer.retrievePeer(), c.getCard(), c.getCardName());
        } else if (command
            instanceof org.jspresso.framework.application.frontend.command.remote.RemoteFocusCommand) {
          this._viewFactory.focus(targetPeer.retrievePeer());
        } else if (command
            instanceof org.jspresso.framework.application.frontend.command.remote.RemoteEditCommand) {
          this._viewFactory.edit(targetPeer.retrievePeer());
        }
      }
    },

    /**
     *
     * @param uploadCommand {org.jspresso.framework.application.frontend.command.remote.RemoteFileUploadCommand}
     */
    _handleFileUpload: function (uploadCommand) {
      var uploadDialog = new qx.ui.window.Window("Upload file");
      uploadDialog.set({
        modal: true,
        showClose: false,
        showMaximize: false,
        showMinimize: false
      });
      uploadDialog.setLayout(new qx.ui.layout.VBox(10));
      //this._viewFactory.setIcon(uploadDialog, messageCommand.getTitleIcon());
      this._application.getRoot().add(uploadDialog);

      var uploadForm = new uploadwidget.UploadForm('uploadForm', uploadCommand.getFileUrl());
      uploadForm.set({
        decorator: "main",
        padding: 8
      });
      uploadForm.setLayout(new qx.ui.layout.VBox(10));

      var uploadField = new uploadwidget.UploadField('uploadFile', 'Select File', 'icon/16/actions/document-save.png');
      uploadForm.add(uploadField);

      uploadDialog.add(uploadForm, {flex: 1});

      var buttonBox = new qx.ui.container.Composite();
      buttonBox.setLayout(new qx.ui.layout.HBox(10, "right"));
      uploadDialog.add(buttonBox);

      uploadForm.addListener("completed", function (e) {
        uploadField.setFileName('');
        var document = uploadForm.getIframeDocument();
        var resource = document.firstChild;
        var id = resource.getAttribute("id");
        if (id) {
          var actionEvent = new org.jspresso.framework.gui.remote.RActionEvent();
          actionEvent.setActionCommand(id);
          this.execute(uploadCommand.getSuccessCallbackAction(), actionEvent);
        }
        uploadDialog.close();
        uploadDialog.destroy();
      }, this);


      var okButton = this._viewFactory.createOkButton();
      this._viewFactory.addButtonListener(okButton, function (event) {
        uploadForm.send();
      }, this);
      buttonBox.add(okButton);

      var cancelButton = this._viewFactory.createCancelButton();
      this._viewFactory.addButtonListener(cancelButton, function (event) {
        uploadDialog.close();
        uploadDialog.destroy();
        this.execute(uploadCommand.getCancelCallbackAction());
      }, this);
      buttonBox.add(cancelButton);

      uploadDialog.open();
      uploadDialog.center();
    },

    linkBowserHistory: function () {
      /**
       * @type {qx.bom.History}
       */
      var browserManager = qx.bom.History.getInstance();
      browserManager.addListener("request", function (e) {
        var state = e.getData();
        var vars = state.split('&');
        var decodedFragment = {};
        for (var i = 0; i < vars.length; i++) {
          var tmp = vars[i].split('=');
          decodedFragment[tmp[0]] = tmp[1];
        }
        if (decodedFragment.snapshotId && decodedFragment.snapshotId != this.__lastReceivedSnapshotId) {
          var command = new org.jspresso.framework.application.frontend.command.remote.RemoteHistoryDisplayCommand();
          command.setSnapshotId(decodedFragment.snapshotId);
          this.registerCommand(command);
        }
      }, this);
    },

    /**
     *
     * @param downloadCommand {org.jspresso.framework.application.frontend.command.remote.RemoteFileDownloadCommand}
     */
    _handleFileDownload: function (downloadCommand) {
      if (!this.__dlFrame) {
        this.__dlFrame = new qx.ui.embed.Iframe("");
        this.__dlFrame.set({
          width: 0,
          height: 0,
          decorator: null //new qx.ui.decoration.Background("transparent")
        });
        this._application.getRoot().add(this.__dlFrame);
      }
      if (this.__dlFrame.getSource() === downloadCommand.getFileUrl()) {
        this.__dlFrame.resetSource();
      }
      this.__dlFrame.setSource(downloadCommand.getFileUrl());
    },

    /**
     *
     * @param clipboardCommand {org.jspresso.framework.application.frontend.command.remote.RemoteClipboardCommand}
     */
    _handleClipboardCommand: function (clipboardCommand) {
      var dataTransfers = [];
      if (clipboardCommand.getPlainContent()) {
        dataTransfers.push("text/unicode");
        dataTransfers.push(clipboardCommand.getPlainContent());
      }
      if (clipboardCommand.getHtmlContent()) {
        dataTransfers.push("text/html");
        dataTransfers.push(clipboardCommand.getHtmlContent());
      }
      org.jspresso.framework.util.browser.ClipboardHelper.copyToSystemClipboard(dataTransfers);
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
     * @param workspaceNames {String[]}
     * @param workspaceActions {org.jspresso.framework.gui.remote.RActionList}
     * @param exitAction {org.jspresso.framework.gui.remote.RAction}
     * @param navigationActions {org.jspresso.framework.gui.remote.RActionList[]}
     * @param actions {org.jspresso.framework.gui.remote.RActionList[]}
     * @param secondaryActions {org.jspresso.framework.gui.remote.RActionList[]}
     * @param helpActions {org.jspresso.framework.gui.remote.RActionList[]}
     * @param size {org.jspresso.framework.util.gui.Dimension}
     * @return {undefined}
     *
     */
    _initApplicationFrame: function (workspaceNames, workspaceActions, exitAction, navigationActions, actions,
                                     secondaryActions, helpActions, size) {
      throw new Error("_initApplicationFrame is abstract");
    },

    /**
     *
     * @return {qx.ui.basic.Label}
     *
     */
    _getStatusBar: function () {
      return this.__statusBar;
    },

    /**
     * @param actions {org.jspresso.framework.gui.remote.RActionList[]}
     * @param helpActions {org.jspresso.framework.gui.remote.RActionList[]}
     * @return {qx.ui.menubar.MenuBar}
     *
     */
    _createApplicationMenuBar: function (actions, helpActions) {
      var menuBar = new qx.ui.menubar.MenuBar();
      this._completeMenuBar(menuBar, actions, false);
      menuBar.addSpacer();
      this._completeMenuBar(menuBar, helpActions, true);
      return menuBar;
    },

    /**
     *
     * @param menuBar {qx.ui.menubar.MenuBar}
     * @param actionLists {org.jspresso.framework.gui.remote.RActionList[]}
     * @param useSeparator {Boolean}
     * @return {undefined}
     */
    _completeMenuBar: function (menuBar, actionLists, useSeparator) {
      if (actionLists) {
        /** @type {qx.ui.menubar.Button } */
        var menubarButton = null;
        for (var i = 0; i < actionLists.length; i++) {
          var actionList = actionLists[i];
          if (!useSeparator || !menubarButton) {
            menubarButton = this._viewFactory.createMenubarButton(actionList.getName(), actionList.getDescription(),
                actionList.getIcon());
            menuBar.add(menubarButton);
            menubarButton.setMenu(new qx.ui.menu.Menu());
          } else {
            menubarButton.getMenu().addSeparator();
          }
          var menu = menubarButton.getMenu();
          var menuItems = this._viewFactory.createMenuItems(actionList.getActions());
          if (menuItems) {
            for (var j = 0; j < menuItems.length; j++) {
              menu.add(menuItems[j]);
            }
          }
        }
      }
    },

    /**
     * @return {undefined}
     */
    _performLogin: function () {
      var loginCommand = new org.jspresso.framework.application.frontend.command.remote.RemoteLoginCommand();
      this.registerCommand(loginCommand);
    },

    /**
     * @return {undefined}
     */
    _restart: function () {
      this._application.getRoot().removeAll();
      this.__dlFrame = null;
      this.__remotePeerRegistry = new org.jspresso.framework.util.remote.registry.BasicRemotePeerRegistry();
      this.__changeNotificationsEnabled = true;
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
      startCommand.setVersion(org.jspresso.framework.application.frontend.controller.qx.AbstractQxController.__JSPRESSO_VERSION);
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
      return [
        "change_font_family", "change_font_size", "format_bold", "format_italic", "format_underline",
        "format_strikethrough", "remove_format", "align_left", "align_center", "align_right", "align_justify",
        "indent_more", "indent_less", "insert_ordered_list", "insert_unordered_list", "undo", "redo", "ok", "cancel",
        "yes", "no"
      ]
    },

    translate:function(key) {
      if(this.__translations) {
        var tr = this.__translations[key];
        if(tr != null) {
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
        (/** @type {qx.core.Object } */ remotePeer).dispose();
      }
    },

    /**
     * @param guid {String}
     * @return {Boolean}
     */
    isRegistered: function (guid) {
      return this.__remotePeerRegistry.isRegistered(guid);
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

    _handleError: function (message) {
      this.warn("Recieved error : " + message);
    },

    _initRemoteController: function () {
      /**
       * @param result {qx.event.type.Data}
       */
      var commandsHandler = function (result) {
        this.__postponedCommands = {};
        this.__postponedNotificationBuffer = {};
        try {
          var data = result.getData();
          this._handleCommands(org.jspresso.framework.util.object.ObjectUtil.typeObjectGraph(data["result"]).toArray());
        } catch (e) {
          throw e;
        } finally {
          this._showBusy(false);
          this.__roundTrip = false;
          this._checkPostponedCommandsCompletion();
          this.__postponedCommands = null;
          this.__postponedNotificationBuffer = null;
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
      for (var guid in this.__postponedNotificationBuffer) {
        //noinspection JSUnfilteredForInLoop
        var peer = this.getRegistered(guid);
        if (peer instanceof org.jspresso.framework.state.remote.RemoteCompositeValueState) {
          peer.notifyChildrenChanged();
        }
      }
    },


    /**
     *
     * @param title {String}
     * @param message {String}
     * @param dialogView {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param icon {org.jspresso.framework.gui.remote.RIcon}
     * @param buttons {qx.ui.form.Button[]|qx.ui.mobile.form.Button}
     * @param useCurrent {Boolean}
     * @param dimension {org.jspresso.framework.util.gui.Dimension}
     * @return {undefined}
     */
    _popupDialog: function (title, message, dialogView, icon, buttons, useCurrent, dimension) {
      throw new Error("_popupDialog is abstract");
    }
  }
});
