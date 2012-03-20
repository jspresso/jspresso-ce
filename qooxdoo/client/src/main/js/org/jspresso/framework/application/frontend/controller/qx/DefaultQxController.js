/**
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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
    __HANDLE_COMMANDS_METHOD : "handleCommands",
    __START_METHOD : "start",
    __STOP_METHOD : "stop"
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
    this.__commandsBacklog = new Array();
    this.__dialogStack = new Array();
    this.__dialogStack.push([null, null, null]);
    this.__userLanguage = userLanguage;
    qx.locale.Manager.getInstance().setLocale(this.__userLanguage);
    this._initRemoteController();
  },
  
  members :
  {
    /**@type qx.application.AbstractGui*/
    __application : null,
    /**@type qx.ui.embed.Iframe*/
    __dlFrame : null,
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
    /**@type Boolean*/
    __roundTrip : false,
    /**@type Array*/
    __commandsBacklog : null,
    /**@type qx.ui.form.RadioGroup*/
    __workspaceAccordionGroup : null,
    /**@type qx.ui.basic.Label*/
    __statusBar : null,
    /**@type qx.ui.container.Stack*/
    __workspaceStack : null,
    /**@type Object*/
    __postponedCommands : null,
    /**@type Object*/
    __postponedNotificationBuffer : null,
    /**@type Array*/
    __dialogStack : null,
    /**@type String*/
    __userLanguage : null,
    /**@type Object*/
    __translations : null,
    
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
    	if(!remotePeer) {
    		return;
    	}
      if(!this.isRegistered(remotePeer.getGuid())) {
        this.__remotePeerRegistry.register(remotePeer);
        if(remotePeer instanceof org.jspresso.framework.state.remote.RemoteValueState) {
          this._bindRemoteValueState(remotePeer);
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
      if(!this.__roundTrip) {
        this.__roundTrip = true;
        this.__remoteController.callAsyncListeners(true,
                                                   org.jspresso.framework.application.frontend.controller.qx.DefaultQxController.__HANDLE_COMMANDS_METHOD,
                                                   org.jspresso.framework.util.object.ObjectUtil.untypeObjectGraph(new qx.data.Array(this.__commandsQueue)));
        this.__commandsQueue.length = 0;
      } else {
        for(var i = 0; i < this.__commandsQueue.length; i++) {
          this.__commandsBacklog[i] = this.__commandsQueue[i];
        }
      }
    },
    
    /**
     * @param {org.jspresso.framework.state.remote.RemoteValueState} remoteValueState
     * @return void
     */
    _bindRemoteValueState : function(remoteValueState) {
      var wasEnabled = this.__changeNotificationsEnabled;
      try {
        this.__changeNotificationsEnabled = false;
        remoteValueState.addListener("changeValue", this._valueUpdated, this);
        if(remoteValueState instanceof org.jspresso.framework.state.remote.RemoteCompositeValueState) {
          remoteValueState.addListener("changeSelectedIndices", this._selectedIndicesUpdated, this);
        }
//      } catch(err) {
//        this.error(err);
      } finally {
        this.__changeNotificationsEnabled = wasEnabled;
      }
    },
    
    /**
     * @param {qx.event.type.Data} event
     * @return void
     */
    _selectedIndicesUpdated : function(event) {
      var remoteCompositeValueState = event.getTarget();
      if(this.__changeNotificationsEnabled) {
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
     * @param {qx.event.type.Data} event
     * @return void
     */
    _valueUpdated : function(event) {
      var remoteValueState = event.getTarget();
      if(this.__changeNotificationsEnabled) {
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
     * @param {org.jspresso.framework.gui.remote.RAction} action
     * @param {String} param
     * @return void
     */
    execute : function(action, actionEvent) {
      actionEvent = (typeof actionEvent == 'undefined') ? null : actionEvent;
      if(action && action.isEnabled()) {
        //this.debug(">>> Execute <<< " + action.getName() + " param = " + param);
        var command = new org.jspresso.framework.application.frontend.command.remote.RemoteActionCommand();
        command.setTargetPeerGuid(action.getGuid());
        command.setPermId(action.getPermId());
        if(!actionEvent) {
          actionEvent = new org.jspresso.framework.gui.remote.RActionEvent();
        }
        command.setActionEvent(actionEvent);
        actionEvent.setViewStateGuid(this.__dialogStack[this.__dialogStack.length -1][1]);
        actionEvent.setViewStatePermId(this.__dialogStack[this.__dialogStack.length -1][2]);
        this.registerCommand(command);
      }
    },
    
    /**
     * @param {org.jspresso.framework.application.frontend.command.remote.RemoteCommand} command
     * @return void
     */
    registerCommand : function(command) {
      if(this.__changeNotificationsEnabled) {
        //this.debug("Command registered for next round trip : " + command);
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
      //this.debug("Recieved commands :");
      var wasEnabled = this.__changeNotificationsEnabled;
      try {
        this.__changeNotificationsEnabled = false;
        if (commands) {
          for(var i = 0; i < commands.length; i++) {
            //this.debug("  -> " + commands[i]);
            this._handleCommand(commands[i]);
          }
        }
//      } catch(err) {
//        this.error(err);
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
        this._handleMessageCommand(command);
      } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteRestartCommand) {
        this._restart();
      } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteFileUploadCommand) {
        this._handleFileUpload(command);
      } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteFileDownloadCommand) {
        this._handleFileDownload(command);
      } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteLocaleCommand) {
        qx.locale.Manager.getInstance().setLocale(command.getLanguage());
        this.__viewFactory.setDatePattern(command.getDatePattern());
        this.__translations = command.getTranslations();
      } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteInitLoginCommand) {
        var loginButton = this.__viewFactory.createButton(command.getOkLabel(), null, command.getOkIcon());
        loginButton.addListener("execute", function(event) {
          this._performLogin();
        }, this);
        var loginButtons = new Array();
        loginButtons.push(loginButton);
        var dialogView = this.createComponent(command.getLoginView());
        this._popupDialog(command.getTitle(), command.getMessage(), dialogView, command.getLoginView().getIcon(), loginButtons);
      } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteCleanupCommand) {
        var removedPeerGuids = command.getRemovedPeerGuids();
        for(var i = 0; i < removedPeerGuids.length; i++) {
          var removedPeer = this.getRegistered(removedPeerGuids[i]);
          if(removedPeer) {
            this.unregister(removedPeer);
          }
        }
      } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteAbstractDialogCommand) {
        var dialogButtons = new Array();
        for(var i = 0; i < command.getActions().length; i++) {
          dialogButtons.push(this.__viewFactory.createAction(command.getActions()[i]));
        }
        var dialogView;
        var icon;
        if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteDialogCommand) {
          dialogView = this.createComponent(command.getView());
          icon = command.getView().getIcon();
        } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteFlashDisplayCommand) {
          dialogView = new qx.ui.embed.Flash(command.getSwfUrl());
          var flashVars = new Object();
          for(var i = 0; i < command.getParamNames().length; i++) {
            flashVars[command.getParamNames()[i]] = command.getParamValues()[i];
          }
          dialogView.setVariables(flashVars);
        }
        this._popupDialog(command.getTitle(), null, dialogView, icon, dialogButtons, command.isUseCurrent(), command.getDimension());
      } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteCloseDialogCommand) {
        if(this.__dialogStack && this.__dialogStack.length > 1) {
          /**@type qx.ui.window.Window*/
          var topDialog = this.__dialogStack.pop()[0]; 
          this.__application.getRoot().remove(topDialog);
          topDialog.close();
          topDialog.destroy();
        }
      } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteInitCommand) {
        this._initApplicationFrame(command.getWorkspaceNames(),
                             command.getWorkspaceActions(),
                             command.getExitAction(),
                             command.getNavigationActions(),
                             command.getActions(),
                             command.getSecondaryActions(),
                             command.getHelpActions(),
                             command.getSize());
      } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteWorkspaceDisplayCommand) {
        this._displayWorkspace(command.getWorkspaceName(),
                         command.getWorkspaceView());
      } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteOpenUrlCommand) {
        window.open(command.getUrlSpec(), "_blank"); 
      } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteUpdateStatusCommand) {
        var status  = command.getStatus();
        if(status != null && status.length > 0) {
          this.__statusBar.setValue(status);
          this.__statusBar.setVisibility("visible");
        } else {
          this.__statusBar.setVisibility("excluded");
        }
      } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteClipboardCommand) {
        this._handleClipboardCommand(command);
      } else {
        var targetPeerGuid = command.getTargetPeerGuid();
        var targetPeer = this.getRegistered(targetPeerGuid);
        if(targetPeer == null) {
          if(!(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteEnablementCommand)) {
            if(!this.__postponedCommands[targetPeerGuid]) {
              this.__postponedCommands[targetPeerGuid] = new Array();
            } 
            this.__postponedCommands[targetPeerGuid].push(command);
          }
          return;
        }
        if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteValueCommand) {
          targetPeer.setValue(command.getValue());
          if(targetPeer instanceof org.jspresso.framework.state.remote.RemoteFormattedValueState) {
           targetPeer.setValueAsObject(command.getValueAsObject());
          } else if(targetPeer instanceof org.jspresso.framework.state.remote.RemoteCompositeValueState) {
           targetPeer.setDescription(command.getDescription());
           targetPeer.setIconImageUrl(command.getIconImageUrl());
          }
        } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteReadabilityCommand) {
          targetPeer.setReadable(command.isReadable());
        } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteWritabilityCommand) {
          targetPeer.setWritable(command.isWritable());
        } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand) {
          if(targetPeer instanceof org.jspresso.framework.gui.remote.RTabContainer) {
            targetPeer.setSelectedIndex(command.getLeadingIndex());
          } else {
	          targetPeer.setLeadingIndex(command.getLeadingIndex());
	          targetPeer.setSelectedIndices(command.getSelectedIndices());
          }
        } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteEnablementCommand) {
          targetPeer.setEnabled(command.isEnabled());
        } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteChildrenCommand) {
          this.__postponedNotificationBuffer[targetPeer.getGuid()] = null;

          /**@type qx.data.Array */
          var children = targetPeer.getChildren();
          /**@type Array*/
          var childrenContent = children.toArray();
          /**@type Array*/
          var commandChildren = command.getChildren().toArray();

          var oldLength = childrenContent.length;
          var newLength = commandChildren.length;
          
          for(var i = 0; i < commandChildren.length; i++) {
            /**@type org.jspresso.framework.state.remote.RemoteValueState */
            var child = commandChildren[i];
            if(this.isRegistered(child.getGuid())) {
              child = this.getRegistered(child.getGuid());
            } else {
              this.register(child);
            }
            var existingChild = null;
            if(i < childrenContent.length) {
              existingChild = childrenContent[i];
            }
            if(existingChild != child) {
              childrenContent[i] = child;
            }
          }
          childrenContent.length = newLength;
          children.length = newLength;
          children.fireDataEvent("changeLength", oldLength, newLength);
          children.fireDataEvent("change",
            {
              start: 0,
              end: newLength,
              type: "add",
              items: childrenContent
            }, null
          );
        } else if(command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteAddCardCommand) {
          this.__viewFactory.addCard(targetPeer, command.getCard(), command.getCardName());
        }
      }
    },
    
    /**
     * 
     * @param {org.jspresso.framework.application.frontend.command.remote.RemoteFileUploadCommand} uploadCommand
     */
    _handleFileUpload : function(uploadCommand) {
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
        uploadField.setFileName('');
        var document = uploadForm.getIframeDocument();
        var resource = document.firstChild;
        var id = resource.getAttribute("id");
        if(id) {
          var actionEvent = new org.jspresso.framework.gui.remote.RActionEvent();
          actionEvent.setActionCommand(id);
          this.execute(uploadCommand.getSuccessCallbackAction(), actionEvent);
        }
        uploadDialog.close();
        uploadDialog.destroy();
      }, this);


      var okButton = this.__viewFactory.createOkButton();
      okButton.addListener("execute", function(event) {
        uploadForm.send();
      }, this);
      buttonBox.add(okButton);

      var cancelButton = this.__viewFactory.createCancelButton();
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
    _handleFileDownload : function(downloadCommand) {
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
     * @param {org.jspresso.framework.application.frontend.command.remote.RemoteClipboardCommand} clipboardCommand
     */
    _handleClipboardCommand : function(clipboardCommand) {
      var dataTransfers = new Array();
      if(clipboardCommand.getPlainContent()) {
        dataTransfers.push("text/unicode");
        dataTransfers.push(clipboardCommand.getPlainContent());
      }
      if(clipboardCommand.getHtmlContent()) {
        dataTransfers.push("text/html");
        dataTransfers.push(clipboardCommand.getHtmlContent());
      }
      org.jspresso.framework.util.browser.ClipboardHelper.copyToSystemClipboard(dataTransfers);
    },

    /**
     * 
     * @param {String} workspaceName
     * @param {org.jspresso.framework.gui.remote.RComponent} workspaceView
     * @return void
     */
    _displayWorkspace : function(workspaceName, workspaceView) {
      if(workspaceView) {
        var workspaceNavigator = null;
        if(workspaceView instanceof org.jspresso.framework.gui.remote.RSplitContainer) {
          workspaceNavigator = workspaceView.getLeftTop();
          workspaceView = workspaceView.getRightBottom();
        }
        var workspaceViewUI = this.createComponent(workspaceView);
        workspaceViewUI.setUserData("workspaceName", workspaceName);
        this.__workspaceStack.add(workspaceViewUI);
        if(workspaceNavigator) {
          var workspaceNavigatorUI = this.createComponent(workspaceNavigator);
          if(workspaceNavigatorUI instanceof qx.ui.tree.Tree) {
            workspaceNavigatorUI.setHideRoot(true);
          }
          var existingChildren = this.__workspaceAccordionGroup.getChildren();
          var existingChild;
          for(var i = 0; i < existingChildren.length; i++) {
            var child = existingChildren[i];
            if(child.getUserData("workspaceName") == workspaceName) {
              existingChild = child;
            }
          }
          if(existingChild) {
            existingChild.add(workspaceNavigatorUI);
          }
        }
      }
      var children = this.__workspaceStack.getChildren();
      var selectedChild;
      for(var i = 0; i < children.length; i++) {
        var child = children[i];
        if(child.getUserData("workspaceName") == workspaceName) {
          selectedChild = child;
        }
      }
      if(selectedChild) {
        this.__workspaceStack.setSelection([selectedChild]);
      }

      children = this.__workspaceAccordionGroup.getChildren();
      selectedChild = null;
      for(var i = 0; i < children.length; i++) {
        var child = children[i];
        if(child.getUserData("workspaceName") == workspaceName) {
          selectedChild = child;
        }
      }
      if(selectedChild) {
        this.__workspaceAccordionGroup.setSelection([selectedChild]);
      }
    },
    
    /**
     * 
     * @param {org.jspresso.framework.gui.remote.RActionList[]} workspaceActions
     * @param {org.jspresso.framework.gui.remote.RAction[]} actions
     * @param {org.jspresso.framework.gui.remote.RAction[]} helpActions
     * @param {org.jspresso.framework.util.Dimension} size
     * @return void
     * 
     */
    _initApplicationFrame : function(workspaceNames,
                                      workspaceActions,
                                      exitAction,
                                      navigationActions,
                                      actions,
                                      secondaryActions,
                                      helpActions,
                                      size) {
      //this.__application.getRoot().removeAll();

      var applicationFrame = new qx.ui.container.Composite(new qx.ui.layout.VBox());
      
      this.__statusBar = new qx.ui.basic.Label();
      this.__statusBar.setVisibility("excluded");
      this._decorateApplicationFrame(applicationFrame, exitAction, navigationActions, actions, helpActions);
      
      var workspaceAccordion = new qx.ui.container.Composite(new qx.ui.layout.VBox(5));
      this.__workspaceAccordionGroup = new qx.ui.form.RadioGroup();
      this.__workspaceAccordionGroup.setAllowEmptySelection(false);
      for(var i = 0; i < workspaceActions.getActions().length; i++) {
        var workspacePanel = new collapsablepanel.Panel(workspaceActions.getActions()[i].getName());
        if(i == 0) {
          workspacePanel.setValue(true);
        } else {
          workspacePanel.setValue(false);
        }
        workspacePanel.setUserData("workspaceName", workspaceNames[i]);
        workspacePanel.setGroup(this.__workspaceAccordionGroup);
        workspacePanel.setUserData("rAction", workspaceActions.getActions()[i]);
        workspacePanel.addListener("changeValue", function(event) {
          this.execute(event.getTarget().getUserData("rAction"));
        }, this);
        this.__viewFactory.setIcon(workspacePanel.getChildControl("bar"),
                             workspaceActions.getActions()[i].getIcon());
        workspaceAccordion.add(workspacePanel, {flex:1});
      }

      this.__workspaceStack = new qx.ui.container.Stack();
      
      var splitContainer = new qx.ui.splitpane.Pane("horizontal");
      splitContainer.add(workspaceAccordion, 0.15);
      splitContainer.add(this.__workspaceStack, 0.85);
      
      applicationFrame.add(splitContainer, {flex:1});
      if(secondaryActions && secondaryActions.length > 0) {
        var secondaryToolBar = new qx.ui.container.SlideBar();
        secondaryToolBar.add(this.__viewFactory.createToolBarFromActionLists(secondaryActions));
        applicationFrame.add(secondaryToolBar);
      }
      if(size) {
        if(size.getWidth() > 0) {
          applicationFrame.setMinWidth(size.getWidth());
        }
        if(size.getHeight() > 0) {
          applicationFrame.setMinHeight(size.getHeight());
        }
      }
      var scrollContainer = new qx.ui.container.Scroll();
      scrollContainer.add(applicationFrame);

      this.__application.getRoot().add(scrollContainer, {edge:0})
    },
    
    /**
     * 
     * @param {qx.ui.container.Composite} applicationFrame
     * @param {org.jspresso.framework.gui.remote.RAction} exitAction
     * @param {org.jspresso.framework.gui.remote.RActionList[]} navigationActions
     * @param {org.jspresso.framework.gui.remote.RActionList[]} actions
     * @param {org.jspresso.framework.gui.remote.RActionList[]} helpActions
     * @return void
     * 
     */
    _decorateApplicationFrame : function(applicationFrame, exitAction, navigationActions, actions, helpActions) {
      //var menuBar = this._createApplicationMenuBar(workspaceActions, actions, helpActions);
      //applicationFrame.add(menuBar);
      var toolBar = new qx.ui.toolbar.ToolBar();
      this.__viewFactory.installActionLists(toolBar, navigationActions);
      if(actions) {
        for(var i = 0; i < actions.length; i++) {
          var splitButton = this.__viewFactory.createSplitButton(actions[i])
          if(splitButton) {
            toolBar.add(splitButton);
          }
        }
      }
      //this.__viewFactory.installActionLists(toolBar, actions);
      toolBar.addSpacer();
      toolBar.add(this._getStatusBar());
      //toolBar.addSpacer();
      if(helpActions) {
        for(var i = 0; i < helpActions.length; i++) {
          var splitButton = this.__viewFactory.createSplitButton(helpActions[i])
          if(splitButton) {
            toolBar.add(splitButton);
          }
        }
      }
      //this.__viewFactory.installActionLists(toolBar, helpActions);
      toolBar.add(this.__viewFactory.createAction(exitAction));
      applicationFrame.add(toolBar);
    },

    /**
     * 
     * @return qx.ui.basic.Label
     * 
     */
    _getStatusBar : function() {
      return this.__statusBar;
    },
    
    /**
     * 
     * @param {org.jspresso.framework.gui.remote.RActionList[]} workspaceActions
     * @param {org.jspresso.framework.gui.remote.RActionList[]} actions
     * @param {org.jspresso.framework.gui.remote.RActionList[]} helpActions
     * @return qx.ui.menubar.MenuBar
     * 
     */
    _createApplicationMenuBar : function(actions,
                                         helpActions) {
      var menuBar = new qx.ui.menubar.MenuBar();
      this._completeMenuBar(menuBar, actions, false);
      menuBar.addSpacer();
      this._completeMenuBar(menuBar, helpActions, true);
      return menuBar;                                            
    },
    
    /**
     * 
     * @param {qx.ui.menubar.MenuBar} menuBar
     * @param {org.jspresso.framework.gui.remote.RActionList[]} actionLists
     * @param {Boolean} useSeparator
     * @return {qx.ui.menu.Button}
     */
    _completeMenuBar : function(menuBar, actionLists, useSeparator) {
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
          var menuItems = this.__viewFactory.createMenuItems(actionList.getActions());
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
     * @return void
     */
    _performLogin : function() {
      var loginCommand = new org.jspresso.framework.application.frontend.command.remote.RemoteLoginCommand();
      this.registerCommand(loginCommand);
    },

    /**
     * @return void
     */
    _restart : function() {
      this.__application.getRoot().removeAll();
      this.__dlFrame = null;
      this.__remotePeerRegistry = new org.jspresso.framework.util.remote.registry.BasicRemotePeerRegistry();
      this.__changeNotificationsEnabled = true;
      this.__commandsQueue = new Array();
      this.__commandsBacklog = new Array();
      this.__dialogStack = new Array();
      this.__dialogStack.push([null, null, null]);
      this.start();
    },

    
    /**
     * @return void
     */
    start : function() {
      this.__application.getRoot().setGlobalCursor("wait");
      this.__remoteController.callAsyncListeners(true,
                                                 org.jspresso.framework.application.frontend.controller.qx.DefaultQxController.__START_METHOD,
                                                 this.__userLanguage, this._getKeysToTranslate(),
                                                 new Date().getTimezoneOffset() * (-60000));
    },
    
    /**
     * @return void
     */
    stop : function() {
      this.__remoteController.callAsyncListeners(true,
                                                 org.jspresso.framework.application.frontend.controller.qx.DefaultQxController.__STOP_METHOD);
    },

    /**
     * @return Array
     */
    _getKeysToTranslate:function() {
      return [
	      "change_font_family",
				"change_font_size",
				"format_bold",
				"format_italic",
				"format_underline",
				"format_strikethrough",
				"remove_format",
				"align_left",
				"align_center",
				"align_right",
				"align_justify",
				"indent_more",
				"indent_less",
				"insert_ordered_list",
				"insert_unordered_list",
				"undo",  
				"redo",
				"ok",  
				"cancel",  
				"yes",
				"no"
			]
    },

    translate:function(key) {
      var tr = this.__translations[key];
      if(tr != null) {
        return tr;
      }
      return key;
    },
    
    /**
     * @param {org.jspresso.framework.application.frontend.command.remote.RemoteMessageCommand} messageCommand
     */
    _handleMessageCommand : function(messageCommand) {
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
      message.setRich(org.jspresso.framework.util.html.HtmlUtil.isHtml(messageCommand.getMessage()));
      this.__viewFactory.setIcon(message, messageCommand.getMessageIcon());
      messageDialog.add(message);

      var buttonBox = new qx.ui.container.Composite();
      buttonBox.setLayout(new qx.ui.layout.HBox(10, "right"));
      messageDialog.add(buttonBox);

      if(messageCommand instanceof org.jspresso.framework.application.frontend.command.remote.RemoteYesNoCommand) {
        var yesButton = this.__viewFactory.createYesButton();
        yesButton.addListener("execute", function(event) {
          messageDialog.close();
          messageDialog.destroy();
          this.execute(messageCommand.getYesAction());
        }, this);
        buttonBox.add(yesButton);

        var noButton = this.__viewFactory.createNoButton();
        noButton.addListener("execute", function(event) {
          messageDialog.close();
          messageDialog.destroy();
          this.execute(messageCommand.getNoAction());
        }, this);
        buttonBox.add(noButton);

        if(messageCommand instanceof org.jspresso.framework.application.frontend.command.remote.RemoteYesNoCancelCommand) {
          var cancelButton = this.__viewFactory.createCancelButton();
          cancelButton.addListener("execute", function(event) {
            messageDialog.close();
            messageDialog.destroy();
            this.execute(messageCommand.getCancelAction());
          }, this);
          buttonBox.add(cancelButton);
        }
      } else if (messageCommand instanceof org.jspresso.framework.application.frontend.command.remote.RemoteOkCancelCommand) {
        var okButton = this.__viewFactory.createOkButton();
        okButton.addListener("execute", function(event) {
          messageDialog.close();
          messageDialog.destroy();
          this.execute(messageCommand.getOkAction());
        }, this);
        buttonBox.add(okButton);

        var cancelButton = this.__viewFactory.createCancelButton();
        cancelButton.addListener("execute", function(event) {
          messageDialog.close();
          messageDialog.destroy();
          this.execute(messageCommand.getCancelAction());
        }, this);
        buttonBox.add(cancelButton);
      } else {
        var okButton = this.__viewFactory.createOkButton();
        okButton.addListener("execute", function(event) {
          this.__application.getRoot().remove(messageDialog);
          messageDialog.close();
          messageDialog.destroy();
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
     * @param {org.jspresso.framework.util.remote.IRemotePeer} remotePeer
     * @return void
     */
    unregister : function(remotePeer) {
      this.__remotePeerRegistry.unregister(remotePeer);
      if(remotePeer instanceof qx.core.Object) {
      	remotePeer.dispose();
      }
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
     * @param {String} viewStatePermId
     */
    setCurrentViewStateGuid : function(viewStateGuid, viewStatePermId) {
      this.__dialogStack[this.__dialogStack.length -1][1] = viewStateGuid;
      this.__dialogStack[this.__dialogStack.length -1][2] = viewStatePermId;
    },

    _handleError : function(message) {
      this.warn("Recieved error : " + message);
    },

    _initRemoteController : function() {
      /**
       * @param {qx.event.type.Data} result
       */
      var commandsHandler = function(result) {
        this.__postponedCommands = new Object();
        this.__postponedNotificationBuffer = new Object();
        try {
          var data = result.getData();
          this._handleCommands(org.jspresso.framework.util.object.ObjectUtil.typeObjectGraph(data["result"]).toArray());
//        } catch(err) {
//          this.error(err);
        } finally {
          this.__application.getRoot().setGlobalCursor("default");
          this.__roundTrip = false;
          this._checkPostponedCommandsCompletion();
          this.__postponedCommands = null;
          this.__postponedNotificationBuffer = null;
          if(this.__commandsBacklog.length > 0) {
            for(var i = 0; i < this.__commandsBacklog.length; i++) {
              this.__commandsQueue.push(this.__commandsBacklog[i]);
            }
            this.__commandsBacklog.length = 0;
            this._dispatchCommands();
          }
        }
      };
      
      /**
       * @param {qx.event.type.Data} ex
       */
      var errorHandler = function(ex) {
        this.__application.getRoot().setGlobalCursor("default");
        this.__roundTrip = false;
        this._handleError(ex.getData().toString());
        if(this.__commandsBacklog.length > 0) {
          for(var i = 0; i < this.__commandsBacklog.length; i++) {
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
     * @return void
     */
    _checkPostponedCommandsCompletion: function() {
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
      for(var guid in this.__postponedNotificationBuffer) {
        var peer = this.getRegistered(guid);
        if(peer instanceof org.jspresso.framework.state.remote.RemoteCompositeValueState) {
          peer.notifyChildrenChanged();
        }
      }
    },

    
    /**
     * 
     * @param {String} title
     * @param {String} message
     * @param {qx.ui.core.Widget} dialogView
     * @param {org.jspresso.framework.gui.remote.RIcon} icon
     * @param {qx.ui.form.Button[]} buttons
     * @param {Boolean} useCurrent
     * @param {org.jspresso.framework.util.gui.Dimension} Dimension
     * @return void
     */
    _popupDialog : function(title, message, dialogView, icon, buttons, useCurrent, dimension) {
      useCurrent = (typeof useCurrent == 'undefined') ? false : useCurrent;

      var buttonBox = new qx.ui.container.Composite();
      buttonBox.setLayout(new qx.ui.layout.HBox(10, "right"));
      
      var dialogBox = new qx.ui.container.Composite();
      dialogBox.setLayout(new qx.ui.layout.VBox(10, null, "separator-vertical"));
      
      if(message) {
        var messageLabel = new qx.ui.basic.Label(message);
        messageLabel.setRich(org.jspresso.framework.util.html.HtmlUtil.isHtml(message));
        dialogBox.add(messageLabel);
      }
      if(dimension != null) {
        dialogView.setWidth(dimension.getWidth());
        dialogView.setHeight(dimension.getHeight());
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
      var newDialog = true;
      if(useCurrent && this.__dialogStack && this.__dialogStack.length > 1) {
        dialog = this.__dialogStack[this.__dialogStack.length -1][0];
        dialog.removeAll();
        newDialog = false;
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
        this.__dialogStack.push([dialog, null, null]);
      }
      dialog.setCaption(title);
      this.__viewFactory.setIcon(dialog, icon);
      if(buttons.length > 0) {
        dialog.addListener("keypress", function(e) {
          if(   e.getKeyIdentifier() == "Enter"
             && ! qx.ui.core.FocusHandler.getInstance().isFocused(buttons[0])
             && !(qx.ui.core.FocusHandler.getInstance().getFocusedWidget() instanceof qx.ui.form.AbstractField)) {
            buttons[0].focus();
            buttons[0].execute(); // and call the default button's
          }
        });
      }

      if(this.__workspaceStack && this.__workspaceStack.getBounds()) {
        dialog.setMaxWidth(Math.floor(this.__workspaceStack.getBounds().width * 90 / 100));
        dialog.setMaxHeight(Math.floor(this.__workspaceStack.getBounds().height * 90 / 100));
      }
      dialog.add(dialogBox);
      dialog.open();
      if(newDialog) {
        dialog.center();
      }
      var focusableChild = this.__findFirstFocusableComponent(dialogBox);
      if(focusableChild) {
        focusableChild.focus();
      }
    },

    /**
     * @param {qx.ui.core.Widget} root
     * @return {qx.ui.core.Widget}
     */
    __findFirstFocusableComponent : function(root) {
      if(  root instanceof qx.ui.form.TextField
        || root instanceof qx.ui.form.CheckBox
        || root instanceof qx.ui.form.SelectBox
        || root instanceof qx.ui.form.TextArea
        || root instanceof qx.ui.form.DateField) {
        if(root.isEnabled()) {
          return root;
        }
      }
      if(   root instanceof qx.ui.container.Composite
         || root instanceof qx.ui.splitpane.Pane
         || root instanceof qx.ui.tabview.TabView) {
        for(var i = 0; i < root.getChildren().length; i++) {
          var child = root.getChildren()[i];
          if(child instanceof qx.ui.core.Widget) {
            var focusableChild = this.__findFirstFocusableComponent(child);
            if(focusableChild != null) {
              return focusableChild;
            }
          }
        }
      }
      return null;
    }
  }
});
