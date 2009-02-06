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

package org.jspresso.framework.application.frontend.controller.flex {
  import flash.display.DisplayObject;
  import flash.events.MouseEvent;
  
  import mx.binding.utils.BindingUtils;
  import mx.collections.ArrayCollection;
  import mx.collections.IList;
  import mx.containers.ApplicationControlBar;
  import mx.containers.Canvas;
  import mx.containers.HBox;
  import mx.containers.Panel;
  import mx.containers.VBox;
  import mx.containers.ViewStack;
  import mx.controls.Alert;
  import mx.controls.Button;
  import mx.controls.HRule;
  import mx.controls.Label;
  import mx.controls.MenuBar;
  import mx.core.Application;
  import mx.core.ClassFactory;
  import mx.core.Container;
  import mx.core.IFlexDisplayObject;
  import mx.core.UIComponent;
  import mx.events.MenuEvent;
  import mx.managers.PopUpManager;
  import mx.rpc.events.FaultEvent;
  import mx.rpc.events.ResultEvent;
  import mx.rpc.remoting.mxml.Operation;
  import mx.rpc.remoting.mxml.RemoteObject;
  
  import org.jspresso.framework.action.IActionHandler;
  import org.jspresso.framework.application.frontend.command.remote.RemoteActionCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteChildrenCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteCloseDialogCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteDialogCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteEnablementCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteInitCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteInitLoginCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteLoginCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteMessageCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteReadabilityCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteValueCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteWorkspaceDisplayCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteWritabilityCommand;
  import org.jspresso.framework.gui.remote.RAction;
  import org.jspresso.framework.gui.remote.RActionList;
  import org.jspresso.framework.gui.remote.RComponent;
  import org.jspresso.framework.state.remote.RemoteCompositeValueState;
  import org.jspresso.framework.state.remote.RemoteValueState;
  import org.jspresso.framework.util.remote.IRemotePeer;
  import org.jspresso.framework.util.remote.registry.BasicRemotePeerRegistry;
  import org.jspresso.framework.util.remote.registry.IRemotePeerRegistry;
  import org.jspresso.framework.view.flex.DefaultFlexViewFactory;
  import org.jspresso.framework.view.flex.RIconMenuBarItem;
  import org.jspresso.framework.view.flex.RIconMenuItemRenderer;
  
  
  public class DefaultFlexController implements IRemotePeerRegistry, IActionHandler {
    
    private static const HANDLE_COMMANDS_METHOD:String = "handleCommands";
    private static const START_METHOD:String = "start";
    private var _remoteController:RemoteObject;
    private var _viewFactory:DefaultFlexViewFactory;
    private var _remotePeerRegistry:IRemotePeerRegistry;
    private var _changeNotificationsEnabled:Boolean;
    private var _commandsQueue:IList;
    private var _commandRegistrationEnabled:Boolean;
    private var _workspaceViewStack:ViewStack;
    private var _postponedCommands:Object;
    private var _dialogStack:Array;
    
    public function DefaultFlexController(remoteController:RemoteObject) {
      _remotePeerRegistry = new BasicRemotePeerRegistry();
      _viewFactory = new DefaultFlexViewFactory(this, this);
      _changeNotificationsEnabled = true;
      _remoteController = remoteController;
      _commandsQueue = new ArrayCollection(new Array());
      _commandRegistrationEnabled = true;
      _dialogStack = new Array();
      initRemoteController();
    }
    
    public function createComponent(remoteComponent:RComponent):UIComponent {
      return _viewFactory.createComponent(remoteComponent);
    }
    
    public function register(remotePeer:IRemotePeer):void {
      if(!isRegistered(remotePeer.guid)) {
        _remotePeerRegistry.register(remotePeer);
        if(remotePeer is RemoteValueState) {
          bindRemoteValueState(remotePeer as RemoteValueState);
          if(remotePeer is RemoteCompositeValueState) {
            for each(var childState:RemoteValueState in (remotePeer as RemoteCompositeValueState).children) {
              register(childState);
            }
          }
        }
        if(_postponedCommands) {
          if(_postponedCommands[remotePeer.guid]) {
            handleCommands(_postponedCommands[remotePeer.guid]);
            delete _postponedCommands[remotePeer.guid];
          }
        }
      }
    }
    
    private function bindRemoteValueState(remoteValueState:RemoteValueState):void {
      var wasEnabled:Boolean = _changeNotificationsEnabled;
      try {
        _changeNotificationsEnabled = false;

        var valueListener:Function = function(value:Object):void {
          valueUpdated(remoteValueState);
        }
        BindingUtils.bindSetter(valueListener, remoteValueState, "value", true);

        if(remoteValueState is RemoteCompositeValueState) {
          var selectedIndicesListener:Function = function(selectedIndices:Array):void {
            selectedIndicesUpdated(remoteValueState as RemoteCompositeValueState);
          }
          BindingUtils.bindSetter(selectedIndicesListener, remoteValueState, "selectedIndices", true);
        }

      } finally {
        _changeNotificationsEnabled = wasEnabled;
      }
    }
    
    public function valueUpdated(remoteValueState:RemoteValueState):void {
      if(_changeNotificationsEnabled) {
        //trace(">>> Value update <<< " + remoteValueState.value);
        var command:RemoteValueCommand = new RemoteValueCommand();
        command.targetPeerGuid = remoteValueState.guid;
        command.value = remoteValueState.value;
        registerCommand(command);
      }
    }
    
    public function selectedIndicesUpdated(remoteCompositeValueState:RemoteCompositeValueState):void {
      if(_changeNotificationsEnabled) {
        //trace(">>> Selected indices update <<< " + remoteCompositeValueState.selectedIndices + " on " + remoteCompositeValueState.value);
        var command:RemoteSelectionCommand = new RemoteSelectionCommand();
        command.targetPeerGuid = remoteCompositeValueState.guid;
        command.selectedIndices = remoteCompositeValueState.selectedIndices;
        command.leadingIndex = remoteCompositeValueState.leadingIndex;
        registerCommand(command);
      }
    }

    public function leadingIndexUpdated(remoteCompositeValueState:RemoteCompositeValueState):void {
      if(_changeNotificationsEnabled) {
        //trace(">>> Leading index update <<< " + remoteCompositeValueState.leadingIndex + " on " + remoteCompositeValueState.value);
        var command:RemoteSelectionCommand = new RemoteSelectionCommand();
        command.targetPeerGuid = remoteCompositeValueState.guid;
        command.selectedIndices = remoteCompositeValueState.selectedIndices;
        command.leadingIndex = remoteCompositeValueState.leadingIndex;
        registerCommand(command);
      }
    }

    public function execute(action:RAction, param:String=null):void {
      //trace(">>> Execute <<< " + action.name + " param = " + param);
      var command:RemoteActionCommand = new RemoteActionCommand();
      command.targetPeerGuid = action.guid;
      command.parameter = param;
      registerCommand(command);
    }
    
    protected function registerCommand(command:RemoteCommand):void {
      if(_commandRegistrationEnabled) {
        //trace("Command registered for next round trip : " + command);
        _commandsQueue.addItem(command);
        dispatchCommands();
        _commandsQueue.removeAll();
      }
    }

    protected function handleCommands(commands:IList):void {
      //trace("Recieved commands :");
      try {
        _commandRegistrationEnabled = false;
        if (commands != null) {
          for each(var command:RemoteCommand in commands) {
            //trace("  -> " + command);
            handleCommand(command);
          }
        }
      } finally {
        _commandRegistrationEnabled = true;
      }
    }

    protected function handleCommand(command:RemoteCommand):void {
      if(command is RemoteMessageCommand) {
        var messageCommand:RemoteMessageCommand = command as RemoteMessageCommand;
        var alert:Alert = Alert.show(messageCommand.message,
                   messageCommand.title,
                   Alert.OK,
                   null,
                   null,
                   null,
                   Alert.OK);

        if(messageCommand.messageIcon) {
          var alertForm:UIComponent =  alert.getChildAt(0) as UIComponent;
          var messageIcon:Class = _viewFactory.getIconForComponent(alertForm, messageCommand.messageIcon);
          alert.iconClass = messageIcon;
          alert.removeChild(alertForm);
          for(var childIndex:int = alertForm.numChildren - 1; childIndex>=0; childIndex--) {
            var childComp:DisplayObject = alertForm.getChildAt(childIndex);
            if(childComp is Button) {
              alertForm.removeChildAt(childIndex);
            }
          }
          alert.addChild(alertForm);
        }

        if(messageCommand.titleIcon) {
          var titleIcon:Class = _viewFactory.getIconForComponent(alert, messageCommand.titleIcon);
          alert.titleIcon = titleIcon;
        }
      } else if(command is RemoteInitLoginCommand) {
        var initLoginCommand:RemoteInitLoginCommand = command as RemoteInitLoginCommand;
        var loginButton:Button = _viewFactory.createButton(initLoginCommand.okLabel, null, initLoginCommand.okIcon);
        loginButton.addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {
          performLogin();
        });
        var loginButtons:Array = new Array();
        loginButtons.push(loginButton);
        popupDialog(initLoginCommand.title, initLoginCommand.message, initLoginCommand.loginView, loginButtons);
      } else if(command is RemoteDialogCommand) {
        var dialogCommand:RemoteDialogCommand = command as RemoteDialogCommand;
        var dialogButtons:Array = new Array();
        for each(var action:RAction in dialogCommand.actions) {
          dialogButtons.push(_viewFactory.createAction(action, true));
        }
        popupDialog(dialogCommand.title, null, dialogCommand.view, dialogCommand.actions);
      } else if(command is RemoteCloseDialogCommand) {
        if(_dialogStack && _dialogStack.length > 0) {
          PopUpManager.removePopUp(_dialogStack.pop() as IFlexDisplayObject);
        }
      } else if(command is RemoteInitCommand) {
        var initCommand:RemoteInitCommand = command as RemoteInitCommand;
        initApplicationFrame(initCommand.workspaceActions,
                             initCommand.actions,
                             initCommand.helpActions);
      } else if(command is RemoteWorkspaceDisplayCommand) {
        var workspaceDisplayCommand:RemoteWorkspaceDisplayCommand = command as RemoteWorkspaceDisplayCommand;
        displayWorkspace(workspaceDisplayCommand.workspaceName,
                         workspaceDisplayCommand.workspaceView);
      } else {
        var targetPeer:IRemotePeer = getRegistered(command.targetPeerGuid);
        if(targetPeer == null) {
          if(!_postponedCommands[command.targetPeerGuid]) {
            _postponedCommands[command.targetPeerGuid] = new ArrayCollection(new Array());
          } 
          (_postponedCommands[command.targetPeerGuid] as IList).addItem(command);
          return;
        }
        if(command is RemoteValueCommand) {
          (targetPeer as RemoteValueState).value =
            (command as RemoteValueCommand).value;
          if(targetPeer is RemoteCompositeValueState) {
           (targetPeer as RemoteCompositeValueState).description =
             (command as RemoteValueCommand).description;
           (targetPeer as RemoteCompositeValueState).iconImageUrl =
             (command as RemoteValueCommand).iconImageUrl;
          }
        } else if(command is RemoteReadabilityCommand) {
          (targetPeer as RemoteValueState).readable =
            (command as RemoteReadabilityCommand).readable;
        } else if(command is RemoteWritabilityCommand) {
          (targetPeer as RemoteValueState).writable =
            (command as RemoteWritabilityCommand).writable;
        } else if(command is RemoteSelectionCommand) {
          (targetPeer as RemoteCompositeValueState).leadingIndex =
            (command as RemoteSelectionCommand).leadingIndex;
          (targetPeer as RemoteCompositeValueState).selectedIndices =
            (command as RemoteSelectionCommand).selectedIndices;
        } else if(command is RemoteEnablementCommand) {
          (targetPeer as RAction).enabled =
            (command as RemoteEnablementCommand).enabled;
        } else if(command is RemoteChildrenCommand) {
          var children:IList = (targetPeer as RemoteCompositeValueState).children; 
          children.removeAll();
          if((command as RemoteChildrenCommand).children != null) {
            for each(var child:RemoteValueState in (command as RemoteChildrenCommand).children) {
              if(isRegistered(child.guid)) {
                child = getRegistered(child.guid) as RemoteValueState;
              } else {
                register(child);
              }
              children.addItem(child);
            }
          }
        }
      }
    }

    protected function handleError(message:String):void {
      trace("Recieved error : " + message);
    }

    public function getRegistered(guid:String):IRemotePeer {
      return _remotePeerRegistry.getRegistered(guid);
    }

    public function unregister(guid:String):void {
      _remotePeerRegistry.unregister(guid);
    }

    public function isRegistered(guid:String):Boolean {
      return _remotePeerRegistry.isRegistered(guid);
    }
    
    protected function dispatchCommands():void {
      var operation:Operation = _remoteController.operations[HANDLE_COMMANDS_METHOD] as Operation;
      operation.send(_commandsQueue);
      _commandsQueue.removeAll();
    }
    
    private function initRemoteController():void {
      _remoteController.showBusyCursor = true;
      var commandsHandler:Function = function(resultEvent:ResultEvent):void {
        _postponedCommands = new Object();
        try {
          handleCommands(resultEvent.result as IList);
        } finally {
          checkPostponedCommandsCompletion();
          _postponedCommands = null;
        }
      };
      var errorHandler:Function = function(faultEvent:FaultEvent):void {
        handleError(faultEvent.fault.message);
      };
      var operation:Operation;
      operation = _remoteController.operations[HANDLE_COMMANDS_METHOD];
      operation.addEventListener(ResultEvent.RESULT, commandsHandler);
      operation.addEventListener(FaultEvent.FAULT, errorHandler);
      operation = _remoteController.operations[START_METHOD];
      operation.addEventListener(ResultEvent.RESULT, commandsHandler);
      operation.addEventListener(FaultEvent.FAULT, errorHandler);
    }
    
    private function checkPostponedCommandsCompletion():void {
      for(var guid:String in _postponedCommands) {
        var commands:IList = _postponedCommands[guid] as IList;
        for each(var command:RemoteCommand in commands) {
          handleError("Target remote peer could not be retrieved :");
          handleError("  guid    = " + command.targetPeerGuid);
          handleError("  command = " + command);
          if(command is RemoteValueCommand) {
            handleError("  value   = " + (command as RemoteValueCommand).value);
          } else if(command is RemoteChildrenCommand) {
            for each (var childState:RemoteValueState in (command as RemoteChildrenCommand).children) {
              handleError("  child = " + childState);
              handleError("    guid  = " + childState.guid);
              handleError("    value = " + childState.value);
            }
          }
        }
      }
    }
    
    private function initApplicationFrame(workspaceActions:Array,
                                          actions:Array,
                                          helpActions:Array):void {
      var controlBar:ApplicationControlBar = new ApplicationControlBar();
      controlBar.dock = true;
      var applicationFrame:Application = Application.application as Application; 
      applicationFrame.addChild(controlBar);
      controlBar.addChild(createApplicationMenuBar(workspaceActions, actions, helpActions));
      _workspaceViewStack = new ViewStack();
      _workspaceViewStack.resizeToContent = true;
      _workspaceViewStack.percentWidth = 100.0;
      _workspaceViewStack.percentHeight = 100.0;
      applicationFrame.addChild(_workspaceViewStack);
    }
    
    private function createApplicationMenuBar(workspaceActions:Array,
                                              actions:Array,
                                              helpActions:Array):MenuBar {
      var menuBarModel:Object = new Object();
      var menus:Array = new Array();
      menus = menus.concat(createMenus(workspaceActions, true));
      menus = menus.concat(createMenus(actions, false));
      menus = menus.concat(createMenus(helpActions, true));
      menuBarModel["children"] = menus;
      
      var menuBar:MenuBar = new MenuBar();
      menuBar.percentWidth = 100.0;
      menuBar.showRoot = false;
      
      menuBar.menuBarItemRenderer = new ClassFactory(RIconMenuBarItem);

      menuBar.dataProvider = menus;
      
      for(var i:int = 0; i < menus.length; i++) {
        menuBar.getMenuAt(i).itemRenderer = new ClassFactory(RIconMenuItemRenderer);
      }
      
      var menuHandler:Function = function(event:MenuEvent):void  {
        if (event.item["data"] is RAction) {
          execute(event.item["data"] as RAction, null);
        }        
      }
      menuBar.addEventListener(MenuEvent.ITEM_CLICK, menuHandler);

      return menuBar;                                            
    }
    
    private function createMenus(actionLists:Array, useSeparator:Boolean):Array {
      var menus:Array = new Array();
      if(actionLists != null) {
        var menu:Object;
        for each (var actionList:RActionList in actionLists) {
          if (!useSeparator || menus.length == 0) {
            menu = createMenu(actionList);
            menus.push(menu);
          } else {
            var separator:Object = new Object();
            separator["type"] = "separator";
            for each (var menuItem:Object in createMenuItems(actionList)) {
              menu["children"].push(menuItem);
            }
          }
        }
      }
      return menus;
    }
    
    private function createMenu(actionList:RActionList):Object {
      var menu:Object = new Object();
      menu["label"] = actionList.name;
      menu["description"] = actionList.description;
      menu["data"] = actionList;
      if(actionList.icon) {
        menu["icon"] = _viewFactory.iconTemplate;
        menu["rIcon"] = actionList.icon;
      }
      
      var menuItems:Array = new Array();
      for each (var menuItem:Object in createMenuItems(actionList)) {
        menuItems.push(menuItem);
      }
      menu["children"] = menuItems;
      return menu;
    }
  
    private function createMenuItems(actionList:RActionList):Array {
      var menuItems:Array = new Array();
      for each(var action:RAction in actionList.actions) {
        menuItems.push(createMenuItem(action));
      }
      return menuItems;
    }
  
    private function createMenuItem(action:RAction):Object {
      var menuItem:Object = new Object();
      menuItem["label"] = action.name;
      menuItem["description"] = action.description;
      menuItem["data"] = action;
      if(action.icon) {
        menuItem["icon"] = _viewFactory.iconTemplate;
        menuItem["rIcon"] = action.icon;
      }
      return menuItem;
    }

    public function start(language:String):void {
      var operation:Operation = _remoteController.operations[START_METHOD] as Operation;
      operation.send(language);
    }
    
    private function displayWorkspace(workspaceName:String, workspaceView:RComponent):void {
      if(workspaceView) {
        var cardCanvas:Canvas = new Canvas();
        cardCanvas.percentWidth = 100.0;
        cardCanvas.percentHeight = 100.0;
        cardCanvas.name = workspaceName;
        _workspaceViewStack.addChild(cardCanvas);

        var workspace:UIComponent = _viewFactory.createComponent(workspaceView);
        workspace.percentWidth = 100.0;
        workspace.percentHeight = 100.0;
        cardCanvas.addChild(workspace) ;
      }
      _workspaceViewStack.selectedChild = _workspaceViewStack.getChildByName(workspaceName) as Container;
    }
    
    private function performLogin():void {
      var loginCommand:RemoteLoginCommand = new RemoteLoginCommand();
      registerCommand(loginCommand);
    }
    
    private function popupDialog(title:String, message:String, view:RComponent, buttons:Array):void {
      var dialogView:UIComponent = _viewFactory.createComponent(view);
      dialogView.percentWidth = 100.0;
      dialogView.percentHeight = 100.0;
      var buttonBox:HBox = new HBox();
      buttonBox.setStyle("horizontalAlign","right");
      buttonBox.percentWidth = 100.0;
      
      var dialogParent:DisplayObject;
      if(_dialogStack && _dialogStack.length > 0) {
        dialogParent = _dialogStack[_dialogStack.length -1];
      } else {
        dialogParent = Application.application as DisplayObject;
      }
      var dialogBox:VBox = new VBox();
      var separator:HRule;
      if(message) {
        var messageLabel:Label = new Label();
        messageLabel.percentWidth = 100.0;
        messageLabel.text = message;
        dialogBox.addChild(messageLabel);
        separator = new HRule();
        separator.percentWidth = 100.0;
        dialogBox.addChild(separator);
      }
      dialogBox.addChild(dialogView);
      separator = new HRule();
      separator.percentWidth = 100.0;
      dialogBox.addChild(separator);
      for each(var button:Button in buttons) {
        buttonBox.addChild(button);
      }
      dialogBox.addChild(buttonBox);

      var dialog:Panel = PopUpManager.createPopUp(dialogParent,Panel,true) as Panel;
      dialog.title = title;
      dialog.addChild(dialogBox);
      PopUpManager.centerPopUp(dialog);
      _dialogStack.push(dialog);
    }
  }
}