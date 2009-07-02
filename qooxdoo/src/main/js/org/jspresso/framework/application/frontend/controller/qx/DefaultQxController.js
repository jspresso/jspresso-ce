/**
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
 */
qx.Class.define("org.jspresso.framework.application.frontend.controller.qx.DefaultQxController",
{
  extend : qx.core.Object,
  
  implement : [org.jspresso.framework.util.remote.registry.IRemotePeerRegistry,
               org.jspresso.framework.action.IActionHandler,
               org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler],
  
  statics :
  {
    __HANDLE_COMMANDS_METHOD : "handleCommandsQx",
    __START_METHOD : "startQx"
  },
  
  construct : function(application, remoteController, userLanguage) {
    this.__application = application;
    this.__application.getRoot().set({
      blockerColor: '#bfbfbf',
      blockerOpacity: 0.5
    });
    this.__remotePeerRegistry = new org.jspresso.framework.util.remote.registry.BasicRemotePeerRegistry();
    this.__viewFactory = new org.jspresso.framework.view.qx.DefaultQxViewFactory(this, this, this);
    this.__changeNotificationsEnabled = true;
    this.__remoteController = remoteController;
    this.__commandsQueue = new Array();
    this.__dialogStack = new Array();
    this.__dialogStack.push([null, null]);
    this.__workspaceWindows = new Object();
    this.__userLanguage = userLanguage;
    qx.locale.Manager.getInstance().setLocale(this.__userLanguage);
    this.__initRemoteController();
  },
  
  members :
  {
    /**@type qx.application.AbstractGui*/
    __application : null,
    /**@type qx.ui.embed.Iframe*/
    __dlFrame : null,
    /**@type qx.ui.window.Desktop*/
    __workspaceContainer : null,
    /**@type qx.io.remote.Rpc*/
    __remoteController : null,
    /**@type org.jspresso.framework.view.qx.DefaultQxViewFactory*/
    __viewFactory : null,
    /**@type org.jspresso.framework.util.remote.registry.IRemotePeerRegistry*/
    __remotePeerRegistry : null,
    /**@type Boolean*/
    __changeNotificationsEnabled : null,
    /**@type org.jspresso.framework.application.frontend.command.remote.RemoteCommand[]*/
    __commandsQueue : null,
    /**@type Object*/
    __workspaceWindows : null,
    /**@type Object*/
    __postponedCommands : null,
    /**@type Array*/
    __dialogStack : null,
    /**@type String*/
    __userLanguage : null,
    
    /**
     * @param {org.jspresso.framework.gui.remote.RComponent} remoteComponent
     * @return {qx.ui.core.Widget}
     */
    createComponent : function(remoteComponent) {
      return this.__viewFactory.createComponent(remoteComponent, true);
    },
    
    /**
     * @param {org.jspresso.framework.util.remote.IRemotePeer} remotePeer
     * @return void
     */
    register : function(remotePeer) {
      if(!this.isRegistered(remotePeer.getGuid())) {
        this.__remotePeerRegistry.register(remotePeer);
        if(remotePeer instanceof org.jspresso.framework.state.remote.RemoteValueState) {
          this.__bindRemoteValueState(remotePeer);
          if(remotePeer instanceof org.jspresso.framework.state.remote.RemoteCompositeValueState) {
            if(remotePeer.getChildren()) {
	            var children = remotePeer.getChildren().toArray();
	            for (var i = 0; i < children.length; i++) {
	              this.register(children[i]);
	            }
            }
          }
        }
        if(this.__postponedCommands) {
          if(this.__postponedCommands[remotePeer.getGuid()]) {
            this._handleCommands(this.__postponedCommands[remotePeer.getGuid()]);
            delete this.__postponedCommands[remotePeer.getGuid()];
          }
        }
      }
    },

    /**
     * @return void
     */
    _dispatchCommands : function () {
      this.__application.getRoot().setGlobalCursor("wait");
      this.__remoteController.callAsyncListeners(true,
                                                 org.jspresso.framework.application.frontend.controller.qx.DefaultQxController.__HANDLE_COMMANDS_METHOD,
                                                 org.jspresso.framework.util.object.ObjectUtil.untypeObjectGraph(new qx.data.Array(this.__commandsQueue)));
      this.__commandsQueue.length = 0;
    },
    
    /**
     * @param {org.jspresso.framework.state.remote.RemoteValueState} remoteValueState
     * @return void
     */
    __bindRemoteValueState : function(remoteValueState) {
      var wasEnabled = this.__changeNotificationsEnabled;
      try {
        this.__changeNotificationsEnabled = false;
        if(remoteValueState instanceof org.jspresso.framework.state.remote.RemoteCompositeValueState) {
          remoteValueState.addListener("changeSelectedIndices", this.__selectedIndicesUpdated, this);
        } else {
          remoteValueState.addListener("changeValue", this.__valueUpdated, this);
        }
      } finally {
        this.__changeNotificationsEnabled = wasEnabled;
      }
    },
    
    /**
     * @param {qx.event.type.Data} event
     * @return void
     */
    __selectedIndicesUpdated : function(event) {
      var remoteCompositeValueState = event.getTarget();
      if(this.__changeNotificationsEnabled) {
        this.debug(">>> Selected indices update <<< " + remoteCompositeValueState.getSelectedIndices() + " on " + remoteCompositeValueState.getValue());
        var command = new org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand();
        command.setTargetPeerGuid(remoteCompositeValueState.getGuid());
        command.setSelectedIndices(remoteCompositeValueState.getSelectedIndices());
        command.setLeadingIndex(remoteCompositeValueState.getLeadingIndex());
        this.registerCommand(command);
      }
    },
    
   
    /**
     * @param {qx.event.type.Data} event
     * @return void
     */
    __valueUpdated : function(event) {
      var remoteValueState = event.getTarget();
      if(this.__changeNotificationsEnabled) {
        this.debug(">>> Value update <<< " + remoteValueState.getValue());
        var command = new org.jspresso.framework.application.frontend.command.remote.RemoteValueCommand();
        command.setTargetPeerGuid(remoteValueState.getGuid());
        command.setValue(remoteValueState.getValue());
        this.registerCommand(command);
      }
    },

    /**
     * 
     * @param {org.jspresso.framework.gui.remote.RAction} action
     * @param {String} param
     * @return void
     */
    execute : function(action, param) {
      param = (typeof param == 'undefined') ? null : param;
      if(action) {
        this.debug(">>> Execute <<< " + action.getName() + " param = " + param);
        var command = new org.jspresso.framework.application.frontend.command.remote.RemoteActionCommand();
        command.setTargetPeerGuid(action.getGuid());
        command.setParameter(param);
        command.setViewStateGuid(this.__dialogStack[this.__dialogStack.length -1][1]);
        this.registerCommand(command);
      }
    },
    
    /**
     * @param {org.jspresso.framework.application.frontend.command.remote.RemoteCommand} command
     * @return void
     */
    registerCommand : function(command) {
      if(this.__changeNotificationsEnabled) {
        this.debug("Command registered for next round trip : " + command);
        this.__commandsQueue.push(command);
        this._dispatchCommands();
        this.__commandsQueue.length = 0;
      }
    },

    /**
     * @param {org.jspresso.framework.application.frontend.command.remote.RemoteCommand[]} commands
     * @return void
     */
    _handleCommands : function(commands) {
      this.debug("Recieved commands :");
      var wasEnabled = this.__changeNotificationsEnabled;
      try {
        this.__changeNotificationsEnabled = false;
        if (commands) {
          for(var i = 0; i < commands.length; i++) {
            this.debug("  -> " + commands[i]);
            this._handleCommand(commands[i]);
          }
        }
      } finally {
        this.__changeNotificationsEnabled = wasEnabled;
      }
    },

    /**
     * 
     * @param {org.jspresso.framework.application.frontend.command.remote.RemoteCommand} command
     * @return void
     */
    _handleCommand : function(command) {
      if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteMessageCommand) {
        this.__handleMessageCommand(command);
      } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteRestartCommand) {
        this.__restart();
      } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteFileUploadCommand) {
        this.__handleFileUpload(command);
      } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteFileDownloadCommand) {
        this.__handleFileDownload(command);
      } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteLocaleCommand) {
        qx.locale.Manager.getInstance().setLocale(command.getLanguage());
      } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteInitLoginCommand) {
        var loginButton = this.__viewFactory.createButton(command.getOkLabel(), null, command.getOkIcon());
        loginButton.addListener("execute", function(event) {
          this.__performLogin();
        }, this);
        var loginButtons = new Array();
        loginButtons.push(loginButton);
        this.__popupDialog(command.getTitle(), command.getMessage(), command.getLoginView(), loginButtons);
      } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteDialogCommand) {
        var dialogButtons = new Array();
        for(var i = 0; i < command.getActions().length; i++) {
          dialogButtons.push(this.__viewFactory.createAction(command.getActions()[i], true));
        }
        this.__popupDialog(command.getTitle(), null, command.getView(), dialogButtons, command.isUseCurrent());
      } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteCloseDialogCommand) {
        if(this.__dialogStack && this.__dialogStack.length > 1) {
          /**@type qx.ui.window.Window*/
          var topDialog = this.__dialogStack.pop()[0]; 
          topDialog.close();
          topDialog.destroy();
          this.__application.getRoot().remove(topDialog);
        }
      } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteInitCommand) {
        this.__initApplicationFrame(command.getWorkspaceActions(),
                             command.getActions(),
                             command.getHelpActions());
      } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteWorkspaceDisplayCommand) {
        this.__displayWorkspace(command.getWorkspaceName(),
                         command.getWorkspaceView());
      } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteOpenUrlCommand) {
        window.open(command.getUrlSpec(), "_blank"); 
      } else {
        var targetPeerGuid = command.getTargetPeerGuid();
        var targetPeer = this.getRegistered(targetPeerGuid);
        if(targetPeer == null) {
          if(!this.__postponedCommands[targetPeerGuid]) {
            this.__postponedCommands[targetPeerGuid] = new Array();
          } 
          this.__postponedCommands[targetPeerGuid].push(command);
          return;
        }
        if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteValueCommand) {
          targetPeer.setValue(command.getValue());
          if(targetPeer instanceof org.jspresso.framework.state.remote.RemoteCompositeValueState) {
           targetPeer.setDescription(command.getDescription());
           targetPeer.setIconImageUrl(command.getIconImageUrl());
          }
        } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteReadabilityCommand) {
          targetPeer.setReadable(command.isReadable());
        } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteWritabilityCommand) {
          targetPeer.setWritable(command.isWritable());
        } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand) {
          targetPeer.setLeadingIndex(command.getLeadingIndex());
          targetPeer.setSelectedIndices(command.getSelectedIndices());
        } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteEnablementCommand) {
          targetPeer.setEnabled(command.isEnabled());
        } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteChildrenCommand) {
          /**@type qx.data.Array */
          var children = targetPeer.getChildren();
          /**@type Array*/
          var commandChildren = command.getChildren().toArray();

          var oldLength = children.getLength();
          var newLength = commandChildren.length;
          
          children.removeAll();
          //fix for the event not being fired.
          if(commandChildren != null) {
            var buffer = new Array();
            for(var i = 0; i < commandChildren.length; i++) {
              /**@type org.jspresso.framework.state.remote.RemoteValueState */
              var child = commandChildren[i];
              if(this.isRegistered(child.getGuid())) {
                child = this.getRegistered(child.getGuid());
              } else {
                this.register(child);
              }
              buffer.push(child);
            }
            children.append(buffer);
          }
          //fix for event not being notified
          //TODO notify Qooxdoo 
          if(newLength > oldLength) {
            children.fireDataEvent("change", 
              {start: oldLength, end: newLength - 1, type: "add"}, null
            );
          } else if(newLength < oldLength) {
            children.fireDataEvent("change", 
              {start: newLength, end: oldLength - 1, type: "remove"}, null
            );
          }
        } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteAddCardCommand) {
          this.__viewFactory.addCard(targetPeer, command.getCard(), command.getCardName());
        }
      }
    },
    
    /**
     * 
     * @param {org.jspresso.framework.application.frontend.command.remote.RemoteFileUploadCommand} uploadCommand
     */
    __handleFileUpload : function(uploadCommand) {
      var uploadDialog = new qx.ui.window.Window("Upload file");
      uploadDialog.set({
        modal : true,
        showClose : false,
        showMaximize : false,
        showMinimize : false
      });
      uploadDialog.setLayout(new qx.ui.layout.VBox(10));
      //this.__viewFactory.setIcon(uploadDialog, messageCommand.getTitleIcon());
      this.__application.getRoot().add(uploadDialog);

      var uploadForm = new uploadwidget.UploadForm('uploadForm',uploadCommand.getFileUrl());
      uploadForm.set({
        decorator:"main",
        padding:8
      });
      uploadForm.setLayout(new qx.ui.layout.VBox(10));

      var uploadField = new uploadwidget.UploadField('uploadFile', 'Select File', 'icon/16/actions/document-save.png');
      uploadForm.add(uploadField);
      
      uploadDialog.add(uploadForm, {flex : 1});

      var buttonBox = new qx.ui.container.Composite();
      buttonBox.setLayout(new qx.ui.layout.HBox(10, "right"));
      uploadDialog.add(buttonBox);
      
      uploadForm.addListener("completed",function(e) {
        uploadField.setFieldValue('');
        var document = uploadForm.getIframeDocument();
        var resource = document.firstChild;
        var id = resource.getAttribute("id");
        if(id) {
          this.execute(uploadCommand.getSuccessCallbackAction(), id);
        }
        uploadDialog.close();
        uploadDialog.destroy();
      }, this);


      var okButton = this.__viewFactory.createOkButton(this.__application);
      okButton.addListener("execute", function(event) {
        uploadForm.send();
      }, this);
      buttonBox.add(okButton);

      var cancelButton = this.__viewFactory.createCancelButton(this.__application);
      cancelButton.addListener("execute", function(event) {
        uploadDialog.close();
        uploadDialog.destroy();
        this.execute(uploadCommand.getCancelCallbackAction());
      }, this);
      buttonBox.add(cancelButton);
      
      uploadDialog.open();
      uploadDialog.center();
    },

    /**
     * 
     * @param {org.jspresso.framework.application.frontend.command.remote.RemoteFileDownloadCommand} downloadCommand
     */
    __handleFileDownload : function(downloadCommand) {
      if(!this.__dlFrame) {
        this.__dlFrame = new qx.ui.embed.Iframe("");
        this.__dlFrame.set({
                width: 0,
                height: 0,
                decorator : null //new qx.ui.decoration.Background("transparent")
        });
        this.__application.getRoot().add(this.__dlFrame);
      }
      if (this.__dlFrame.getSource() === downloadCommand.getFileUrl()) {
        this.__dlFrame.resetSource();
      }
      this.__dlFrame.setSource(downloadCommand.getFileUrl());  
    },

    /**
     * 
     * @param {String} workspaceName
     * @param {org.jspresso.framework.gui.remote.RComponent} workspaceView
     * @return void
     */
    __displayWorkspace : function(workspaceName, workspaceView) {
      /**@type qx.ui.window.Window*/
      var workspaceWindow = this.__workspaceWindows[workspaceName];
      if(!workspaceWindow && workspaceView) {
        workspaceWindow = new qx.ui.window.Window();
	      workspaceWindow.set({
	        modal : false,
	        showClose : false,
	        showMaximize : true,
	        showMinimize : false
	      });
        workspaceWindow.setLayout(new qx.ui.layout.Grow());
        workspaceWindow.setCaption(workspaceView.getLabel());
        workspaceWindow.addListener("changeActive", this.__notifyWorkspaceSelection, this);
        this.__viewFactory.setIcon(workspaceWindow, workspaceView.getIcon());

        var workspace = this.__viewFactory.createComponent(workspaceView);
        workspaceWindow.add(workspace);
        
        this.__workspaceWindows[workspaceName] = workspaceWindow;
        this.__workspaceContainer.add(workspaceWindow);
        workspaceWindow.open();
        workspaceWindow.center();
      }
      this.__workspaceContainer.setActiveWindow(workspaceWindow);
      workspaceWindow.maximize();
    },
    
    __notifyWorkspaceSelection : function(e) {
      /**@type qx.ui.window.Window*/
      var workspaceWindow = e.getTarget();
      if(workspaceWindow.isActive()) {
	      var selectedWorkspaceName;
	      for(var workspaceName in this.__workspaceWindows) {
	        if(workspaceWindow === this.__workspaceWindows[workspaceName]) {
	          selectedWorkspaceName = workspaceName;
	        }
	      }
	      if(selectedWorkspaceName) {
	        var workspaceSelectAction = new org.jspresso.framework.application.frontend.command.remote.RemoteWorkspaceDisplayCommand();
	        workspaceSelectAction.setWorkspaceName(selectedWorkspaceName);
	        this.registerCommand(workspaceSelectAction);
	      }
      }
    },
    
    /**
     * 
     * @param {org.jspresso.framework.gui.remote.RAction[]} workspaceActions
     * @param {org.jspresso.framework.gui.remote.RAction[]} actions
     * @param {org.jspresso.framework.gui.remote.RAction[]} helpActions
     * @return void
     * 
     */
    __initApplicationFrame : function(workspaceActions,
                                    actions,
                                    helpActions) {
      this.__application.getRoot().removeAll();

      var applicationContainer = new qx.ui.container.Composite(new qx.ui.layout.VBox(10));
      
      var menuBar = this.__createApplicationMenuBar(workspaceActions, actions, helpActions);
      applicationContainer.add(menuBar);
      
      this.__workspaceContainer = new qx.ui.window.Desktop(new qx.ui.window.Manager());
      applicationContainer.add(this.__workspaceContainer, {flex:1});

      this.__application.getRoot().add(applicationContainer, {edge:0})
    },
    
    /**
     * 
     * @param {org.jspresso.framework.gui.remote.RActionList[]} workspaceActions
     * @param {org.jspresso.framework.gui.remote.RActionList[]} actions
     * @param {org.jspresso.framework.gui.remote.RActionList[]} helpActions
     * @return qx.ui.menubar.MenuBar
     * 
     */
    __createApplicationMenuBar : function(workspaceActions,
                                              actions,
                                              helpActions) {
      var menuBar = new qx.ui.menubar.MenuBar();
      
      this.__completeMenuBar(menuBar, workspaceActions, true);
      this.__completeMenuBar(menuBar, actions, false);
      menuBar.addSpacer();
      this.__completeMenuBar(menuBar, helpActions, true);

      return menuBar;                                            
    },
    
    /**
     * 
     * @param {qx.ui.menubar.MenuBar} menuBar
     * @param {org.jspresso.framework.gui.remote.RActionList[]} actionLists
     * @param {Boolean} useSeparator
     * @return {qx.ui.menu.Button}
     */
    __completeMenuBar : function(menuBar, actionLists, useSeparator) {
      if(actionLists) {
        for(var i = 0; i < actionLists.length; i++) {
          var actionList = actionLists[i];
          /**@type qx.ui.menubar.Button*/
          var menubarButton;
          if(!useSeparator || !menubarButton) {
            menubarButton = this.__viewFactory.createMenubarButton(actionList.getName(),
                                                                actionList.getDescription(),
                                                                actionList.getIcon());
            menuBar.add(menubarButton);
            menubarButton.setMenu(new qx.ui.menu.Menu());
          } else {
            menubarButton.getMenu().addSeparator();
          }
          var menu = menubarButton.getMenu();
          var menuItems = this.__createMenuItems(actionList.getActions());
          if(menuItems) {
            for(var j = 0; j < menuItems.length; j++) {
              menu.add(menuItems[j]);
            }
          }
        }
      }
      return menubarButton;
    },
    
    /**
     * 
     * @param {org.jspresso.framework.gui.remote.RAction[]} actions
     * @return {Array}
     */
     __createMenuItems : function(actions) {
      var menuItems = new Array();
      for(var i = 0; i < actions.length; i++) {
        var menuButton = this.__viewFactory.createMenuButton(actions[i].getName(),
                                                             actions[i].getDescription(),
                                                             actions[i].getIcon());
        var command = this.__viewFactory.createCommand(actions[i]);
        menuButton.setCommand(command);
        menuItems.push(menuButton);
      }
      return menuItems;
    },
    
    /**
     * @return void
     */
    __performLogin : function() {
      var loginCommand = new org.jspresso.framework.application.frontend.command.remote.RemoteLoginCommand();
      this.registerCommand(loginCommand);
    },

    /**
     * @return void
     */
    __restart : function() {
      this.__application.getRoot().removeAll();
      this.__dlFrame = null;
      this.__remotePeerRegistry = new org.jspresso.framework.util.remote.registry.BasicRemotePeerRegistry();
      this.__changeNotificationsEnabled = true;
      this.__commandsQueue = new Array();
      this.__workspaceContainer = null;
      this.__workspaceWindows = new Object();
      this.__dialogStack = new Array();
      this.__dialogStack.push([null, null]);
      this.start();
    },

    
    /**
     * @return void
     */
    start : function() {
      this.__application.getRoot().setGlobalCursor("wait");
      this.__remoteController.callAsyncListeners(true,
                                                 org.jspresso.framework.application.frontend.controller.qx.DefaultQxController.__START_METHOD,
                                                 this.__userLanguage);
    },

    /**
     * @param {org.jspresso.framework.application.frontend.command.remote.RemoteMessageCommand} messageCommand
     */
    __handleMessageCommand : function(messageCommand) {
      var messageDialog = new qx.ui.window.Window(messageCommand.getTitle());
      messageDialog.set({
        modal : true,
        showClose : false,
        showMaximize : false,
        showMinimize : false
      });
      messageDialog.setLayout(new qx.ui.layout.VBox(10));
      this.__viewFactory.setIcon(messageDialog, messageCommand.getTitleIcon());
      this.__application.getRoot().add(messageDialog);

      var message = new qx.ui.basic.Atom(messageCommand.getMessage());
      message.setRich(this.__viewFactory.isHtml(messageCommand.getMessage()));
      this.__viewFactory.setIcon(message, messageCommand.getMessageIcon());
      messageDialog.add(message);

      var buttonBox = new qx.ui.container.Composite();
      buttonBox.setLayout(new qx.ui.layout.HBox(10, "right"));
      messageDialog.add(buttonBox);

      if(messageCommand instanceof org.jspresso.framework.application.frontend.command.remote.RemoteYesNoCommand) {
        var yesButton = this.__viewFactory.createYesButton(this.__application);
        yesButton.addListener("execute", function(event) {
          messageDialog.close();
          messageDialog.destroy();
          this.execute(messageCommand.getYesAction());
        }, this);
        buttonBox.add(yesButton);

        var noButton = this.__viewFactory.createNoButton(this.__application);
        noButton.addListener("execute", function(event) {
          messageDialog.close();
          messageDialog.destroy();
          this.execute(messageCommand.getNoAction());
        }, this);
        buttonBox.add(noButton);

        if(messageCommand instanceof org.jspresso.framework.application.frontend.command.remote.RemoteYesNoCancelCommand) {
          var cancelButton = this.__viewFactory.createCancelButton(this.__application);
          cancelButton.addListener("execute", function(event) {
            messageDialog.close();
            messageDialog.destroy();
            this.execute(messageCommand.getCancelAction());
          }, this);
          buttonBox.add(cancelButton);
        }
      } else if (messageCommand instanceof org.jspresso.framework.application.frontend.command.remote.RemoteOkCancelCommand) {
        var okButton = this.__viewFactory.createOkButton(this.__application);
        okButton.addListener("execute", function(event) {
          messageDialog.close();
          messageDialog.destroy();
          this.execute(messageCommand.getOkAction());
        }, this);
        buttonBox.add(okButton);

        var cancelButton = this.__viewFactory.createCancelButton(this.__application);
        cancelButton.addListener("execute", function(event) {
          messageDialog.close();
          messageDialog.destroy();
          this.execute(messageCommand.getCancelAction());
        }, this);
        buttonBox.add(cancelButton);
      } else {
        var okButton = this.__viewFactory.createOkButton(this.__application);
        okButton.addListener("execute", function(event) {
          messageDialog.close();
          messageDialog.destroy();
          this.__application.getRoot().remove(messageDialog);
        }, this);
        buttonBox.add(okButton);
      }
      
      messageDialog.open();
      messageDialog.center();
    },
    
    /**
     * @param {String} guid
     * @return {org.jspresso.framework.util.remote.IRemotePeer}
     */
    getRegistered : function(guid) {
      return this.__remotePeerRegistry.getRegistered(guid);
    },

    /**
     * @param {String} guid
     * @return void
     */
    unregister : function(guid) {
      this.__remotePeerRegistry.unregister(guid);
    },

    /**
     * @param {String} guid
     * @return {Boolean}
     */
    isRegistered : function(guid) {
      return this.__remotePeerRegistry.isRegistered(guid);
    },
    
    /**
     * 
     * @param {String} viewStateGuid
     */
    setCurrentViewStateGuid : function(viewStateGuid) {
      this.__dialogStack[this.__dialogStack.length -1][1] = viewStateGuid;
    },

    _handleError : function(message) {
      this.warn("Recieved error : " + message);
    },

    __initRemoteController : function() {
      /**
       * @param {qx.event.type.Data} result
       */
      var commandsHandler = function(result) {
        this.__postponedCommands = new Object();
        try {
          var data = result.getData();
          this._handleCommands(org.jspresso.framework.util.object.ObjectUtil.typeObjectGraph(data["result"]).toArray());
        } finally {
          this.__application.getRoot().setGlobalCursor("default");
          this.__checkPostponedCommandsCompletion();
          this.__postponedCommands = null;
        }
      };
      
      /**
       * @param {qx.event.type.Data} ex
       */
      var errorHandler = function(ex) {
        this.__application.getRoot().setGlobalCursor("default");
        this._handleError(ex.getData().toString());
      };
      
      this.__remoteController.addListener("completed", commandsHandler, this);
      this.__remoteController.addListener("failed", errorHandler, this);
    },
    
    /**
     * @return void
     */
    __checkPostponedCommandsCompletion: function() {
      for(var guid in this.__postponedCommands) {
        /**@type org.jspresso.framework.application.frontend.command.remote.RemoteCommand[]*/
        var commands = this.__postponedCommands[guid];
        for(var i = 0; i < commands.length; i++) {
          var command = commands[i];
          this._handleError("Target remote peer could not be retrieved :");
          this._handleError("  guid    = " + command.getTargetPeerGuid());
          this._handleError("  command = " + command);
          if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteValueCommand) {
            this._handleError("  value   = " + command.getValue());
          } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteChildrenCommand) {
            /** @type org.jspresso.framework.state.remote.RemoteValueState[]*/
            var children = command.getChildren().toArray();
            for(var j = 0; j < children.length; j++) {
              var childState = children[j];
              this._handleError("  child = " + childState);
              this._handleError("    guid  = " + childState.getGuid());
              this._handleError("    value = " + childState.getValue());
            }
          }
        }
      }
    },

    
    /**
     * 
     * @param {String} title
     * @param {String} message
     * @param {org.jspresso.framework.gui.remote.RComponent} view
     * @param {qx.ui.form.Button[]} buttons
     * @param {Boolean} useCurrent
     * @return void
     */
    __popupDialog : function(title, message, view, buttons, useCurrent) {
      useCurrent = (typeof useCurrent == 'undefined') ? false : useCurrent;
      var dialogView = this.__viewFactory.createComponent(view);

      var buttonBox = new qx.ui.container.Composite();
      buttonBox.setLayout(new qx.ui.layout.HBox(10, "right"));
      
      var dialogBox = new qx.ui.container.Composite();
      dialogBox.setLayout(new qx.ui.layout.VBox(10, null, "separator-vertical"));
      
      if(message) {
        var messageLabel = new qx.ui.basic.Label(message);
        messageLabel.setRich(this.__viewFactory.isHtml(message));
        dialogBox.add(messageLabel);
      }
      dialogBox.add(dialogView, {flex:1});
      for(var i = 0; i < buttons.length; i++) {
        buttonBox.add(buttons[i]);
      }
      dialogBox.add(buttonBox);

      /**
       * @type qx.ui.window.Window
       */
      var dialog;
      if(useCurrent && this.__dialogStack && this.__dialogStack.length > 1) {
        dialog = this.__dialogStack[this.__dialogStack.length -1][0];
        dialog.removeAll();
      } else {
        var dialogParent;
				//        if(this.__dialogStack && this.__dialogStack.length > 1) {
				//          dialogParent = this.__dialogStack[__dialogStack.length -1];
				//        } else {
				//          dialogParent = this.__application.getRoot();
				//        }
        dialogParent = this.__application.getRoot();
        dialog = new qx.ui.window.Window();
        dialog.setLayout(new qx.ui.layout.Grow());
	      dialog.set({
	        modal : true,
	        showClose : false,
	        showMaximize : false,
	        showMinimize : false
	      });
        dialogParent.add(dialog);
        this.__dialogStack.push([dialog, null]);
      }
      dialog.setCaption(title);
      this.__viewFactory.setIcon(dialog, view.getIcon());
      if(buttons.length > 0) {
				dialog.addListener("keypress", function(e) {
					if(   e.getKeyIdentifier() == "Enter"
             && !qx.ui.core.FocusHandler.getInstance().isFocused(buttons[0])) {
						buttons[0].focus();
						buttons[0].execute(); // and call the default button's
					}
				});
      }

      dialog.add(dialogBox);
      dialog.open();
      dialog.center();
    }
  }
});
