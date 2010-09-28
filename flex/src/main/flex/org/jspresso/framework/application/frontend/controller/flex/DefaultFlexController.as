/**
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
  import flash.events.DataEvent;
  import flash.events.Event;
  import flash.events.MouseEvent;
  import flash.external.ExternalInterface;
  import flash.net.FileFilter;
  import flash.net.FileReference;
  import flash.net.URLRequest;
  import flash.net.navigateToURL;
  import flash.net.registerClassAlias;
  
  import flex.utils.ui.resize.ResizablePanel;
  
  import mx.binding.utils.BindingUtils;
  import mx.collections.ArrayCollection;
  import mx.collections.IList;
  import mx.containers.Accordion;
  import mx.containers.ApplicationControlBar;
  import mx.containers.Canvas;
  import mx.containers.HBox;
  import mx.containers.HDividedBox;
  import mx.containers.Panel;
  import mx.containers.TabNavigator;
  import mx.containers.VBox;
  import mx.containers.ViewStack;
  import mx.controls.Alert;
  import mx.controls.Button;
  import mx.controls.HRule;
  import mx.controls.Label;
  import mx.controls.Menu;
  import mx.controls.MenuBar;
  import mx.controls.PopUpButton;
  import mx.controls.SWFLoader;
  import mx.controls.Tree;
  import mx.core.Application;
  import mx.core.ClassFactory;
  import mx.core.Container;
  import mx.core.IFlexDisplayObject;
  import mx.core.ScrollPolicy;
  import mx.core.UIComponent;
  import mx.core.mx_internal;
  import mx.events.CloseEvent;
  import mx.events.FlexEvent;
  import mx.events.IndexChangedEvent;
  import mx.events.MenuEvent;
  import mx.managers.PopUpManager;
  import mx.resources.Locale;
  import mx.resources.ResourceManager;
  import mx.rpc.events.FaultEvent;
  import mx.rpc.events.ResultEvent;
  import mx.rpc.remoting.mxml.Operation;
  import mx.rpc.remoting.mxml.RemoteObject;
  
  import org.jspresso.framework.action.IActionHandler;
  import org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler;
  import org.jspresso.framework.application.frontend.command.remote.RemoteAbstractDialogCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteActionCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteAddCardCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteChildrenCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteCloseDialogCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteDialogCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteEnablementCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteFileDownloadCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteFileUploadCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteFlashDisplayCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteInitCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteInitLoginCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteLocaleCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteLoginCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteMessageCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteOkCancelCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteOpenUrlCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteReadabilityCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteRestartCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteValueCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteWorkspaceDisplayCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteWritabilityCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteYesNoCancelCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteYesNoCommand;
  import org.jspresso.framework.gui.remote.RAction;
  import org.jspresso.framework.gui.remote.RActionComponent;
  import org.jspresso.framework.gui.remote.RActionField;
  import org.jspresso.framework.gui.remote.RActionList;
  import org.jspresso.framework.gui.remote.RBorderContainer;
  import org.jspresso.framework.gui.remote.RCardContainer;
  import org.jspresso.framework.gui.remote.RCheckBox;
  import org.jspresso.framework.gui.remote.RColorField;
  import org.jspresso.framework.gui.remote.RComboBox;
  import org.jspresso.framework.gui.remote.RComponent;
  import org.jspresso.framework.gui.remote.RConstrainedGridContainer;
  import org.jspresso.framework.gui.remote.RContainer;
  import org.jspresso.framework.gui.remote.RDateField;
  import org.jspresso.framework.gui.remote.RDecimalField;
  import org.jspresso.framework.gui.remote.RDurationField;
  import org.jspresso.framework.gui.remote.REvenGridContainer;
  import org.jspresso.framework.gui.remote.RForm;
  import org.jspresso.framework.gui.remote.RIcon;
  import org.jspresso.framework.gui.remote.RImageComponent;
  import org.jspresso.framework.gui.remote.RIntegerField;
  import org.jspresso.framework.gui.remote.RLabel;
  import org.jspresso.framework.gui.remote.RLink;
  import org.jspresso.framework.gui.remote.RList;
  import org.jspresso.framework.gui.remote.RPasswordField;
  import org.jspresso.framework.gui.remote.RPercentField;
  import org.jspresso.framework.gui.remote.RSplitContainer;
  import org.jspresso.framework.gui.remote.RTabContainer;
  import org.jspresso.framework.gui.remote.RTable;
  import org.jspresso.framework.gui.remote.RTextArea;
  import org.jspresso.framework.gui.remote.RTextField;
  import org.jspresso.framework.gui.remote.RTimeField;
  import org.jspresso.framework.gui.remote.RTree;
  import org.jspresso.framework.state.remote.RemoteCompositeValueState;
  import org.jspresso.framework.state.remote.RemoteFormattedValueState;
  import org.jspresso.framework.state.remote.RemoteValueState;
  import org.jspresso.framework.util.array.ArrayUtil;
  import org.jspresso.framework.util.gui.CellConstraints;
  import org.jspresso.framework.util.gui.Dimension;
  import org.jspresso.framework.util.gui.Font;
  import org.jspresso.framework.util.html.HtmlUtil;
  import org.jspresso.framework.util.remote.IRemotePeer;
  import org.jspresso.framework.util.remote.RemotePeer;
  import org.jspresso.framework.util.remote.registry.BasicRemotePeerRegistry;
  import org.jspresso.framework.util.remote.registry.IRemotePeerRegistry;
  import org.jspresso.framework.view.flex.CollapsibleAccordion;
  import org.jspresso.framework.view.flex.DefaultFlexViewFactory;
  import org.jspresso.framework.view.flex.RIconMenuBarItem;
  import org.jspresso.framework.view.flex.RIconMenuItemRenderer;
  
  
  [ResourceBundle("Common_messages")]
  public class DefaultFlexController implements IRemotePeerRegistry, IActionHandler, IRemoteCommandHandler {
    
    private static const HANDLE_COMMANDS_METHOD:String = "handleCommands";
    private static const START_METHOD:String = "start";
    private var _remoteController:RemoteObject;
    private var _viewFactory:DefaultFlexViewFactory;
    private var _remotePeerRegistry:IRemotePeerRegistry;
    private var _changeNotificationsEnabled:Boolean;
    private var _commandsQueue:IList;
    private var _workspaceAccordion:CollapsibleAccordion;
    private var _workspaceViewStack:ViewStack;
    private var _unregistered:Object;
    private var _postponedCommands:Object;
    private var _dialogStack:Array;
    private var _userLanguage:String;
    private var _fileReference:FileReference;
    private var _initialLocaleChain:Array;
    private var _fakeDialog:Panel;
    
    public function DefaultFlexController(remoteController:RemoteObject, userLanguage:String) {
      _remotePeerRegistry = new BasicRemotePeerRegistry();
      _viewFactory = createViewFactory();
      _changeNotificationsEnabled = true;
      _remoteController = remoteController;
      _commandsQueue = new ArrayCollection(new Array());
      _dialogStack = new Array();
      _dialogStack.push([null, null, null]);
      _userLanguage = userLanguage;
      _initialLocaleChain = ResourceManager.getInstance().localeChain;
      _fakeDialog = new Panel();
      if (ExternalInterface.available) {
        ExternalInterface.addCallback("stop", stop);
      }
      registerRemoteClasses();
      initRemoteController();
    }
    
    protected function createViewFactory():DefaultFlexViewFactory {
      return new DefaultFlexViewFactory(this, this, this);
    }
    
    public function register(remotePeer:IRemotePeer):void {
      if(!remotePeer) {
        return;
      }
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
    
    protected function bindRemoteValueState(remoteValueState:RemoteValueState):void {
      var wasEnabled:Boolean = _changeNotificationsEnabled;
      try {
        _changeNotificationsEnabled = false;

        var valueListener:Function = function(value:Object):void {
          valueUpdated(remoteValueState);
        };
        BindingUtils.bindSetter(valueListener, remoteValueState, "value", true);

        if(remoteValueState is RemoteCompositeValueState) {
          var selectedIndicesListener:Function = function(selectedIndices:Array):void {
            selectedIndicesUpdated(remoteValueState as RemoteCompositeValueState);
          };
          BindingUtils.bindSetter(selectedIndicesListener, remoteValueState, "selectedIndices", true);
        }
      } finally {
        _changeNotificationsEnabled = wasEnabled;
      }
    }
    
    protected function valueUpdated(remoteValueState:RemoteValueState):void {
      if(_changeNotificationsEnabled) {
        //trace(">>> Value update <<< " + remoteValueState.value);
        var command:RemoteValueCommand = new RemoteValueCommand();
        command.targetPeerGuid = remoteValueState.guid;
        command.automationId = remoteValueState.automationId;
        command.value = remoteValueState.value;
        registerCommand(command);
      }
    }
    
    protected function selectedIndicesUpdated(remoteCompositeValueState:RemoteCompositeValueState):void {
      if(_changeNotificationsEnabled) {
        //trace(">>> Selected indices update <<< " + remoteCompositeValueState.selectedIndices + " on " + remoteCompositeValueState.value);
        var command:RemoteSelectionCommand = new RemoteSelectionCommand();
        command.targetPeerGuid = remoteCompositeValueState.guid;
        command.automationId = remoteCompositeValueState.automationId;
        command.selectedIndices = remoteCompositeValueState.selectedIndices;
        command.leadingIndex = remoteCompositeValueState.leadingIndex;
        registerCommand(command);
      }
    }

    public function execute(action:RAction, param:String=null):void {
      //trace(">>> Execute <<< " + action.name + " param = " + param);
      if(action && action.enabled) {
        var command:RemoteActionCommand = new RemoteActionCommand();
        command.targetPeerGuid = action.guid;
        command.automationId = action.automationId;
        command.parameter = param;
        command.viewStateGuid = (_dialogStack[_dialogStack.length -1] as Array)[1];
        command.viewStateAutomationId = (_dialogStack[_dialogStack.length -1] as Array)[2];
        registerCommand(command);
      }
    }
    
    public function registerCommand(command:RemoteCommand):void {
      if(_changeNotificationsEnabled) {
        //trace("Command registered for next round trip : " + command);
        _commandsQueue.addItem(command);
        dispatchCommands();
        _commandsQueue.removeAll();
      }
    }

    protected function handleCommands(commands:IList):void {
      //trace("Recieved commands :");
      var wasEnabled:Boolean = _changeNotificationsEnabled;
      try {
        _changeNotificationsEnabled = false;
        if (commands != null) {
          for(var i:int = 0; i < commands.length; i++) {
            //trace("  -> " + command);
            handleCommand(commands.getItemAt(i) as RemoteCommand);
          }
        }
      } finally {
        _changeNotificationsEnabled = wasEnabled;
      }
    }

    protected function handleCommand(command:RemoteCommand):void {
      if(command is RemoteMessageCommand) {
        handleMessageCommand(command as RemoteMessageCommand);
      } else if(command is RemoteRestartCommand) {
        restart();
      } else if(command is RemoteFileUploadCommand) {
        handleFileUpload(command as RemoteFileUploadCommand);
      } else if(command is RemoteFileDownloadCommand) {
        handleFileDownload(command as RemoteFileDownloadCommand);
      } else if(command is RemoteLocaleCommand) {
        var locale:Locale;
        for each (var availableLocaleString:String in ResourceManager.getInstance().getLocales()) {
          var availableLocale:Locale = new Locale(availableLocaleString);
          if(!locale && (command as RemoteLocaleCommand).language == availableLocale.language) {
            locale = availableLocale;
          }
        }
        if(locale) {
          ResourceManager.getInstance().localeChain = [locale.toString()].concat(_initialLocaleChain);
        }
      } else if(command is RemoteInitLoginCommand) {
        var initLoginCommand:RemoteInitLoginCommand = command as RemoteInitLoginCommand;
        var loginButton:Button = getViewFactory().createButton(initLoginCommand.okLabel, null, initLoginCommand.okIcon);
        loginButton.addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {
          performLogin();
        });
        var loginButtons:Array = new Array();
        loginButtons.push(loginButton);
        var loginView:UIComponent = getViewFactory().createComponent(initLoginCommand.loginView);
        popupDialog(initLoginCommand.title, initLoginCommand.message, loginView, initLoginCommand.loginView.icon, loginButtons);
      } else if(command is RemoteAbstractDialogCommand) {
        var dialogCommand:RemoteAbstractDialogCommand = command as RemoteAbstractDialogCommand;
        var dialogButtons:Array = new Array();
        for each(var action:RAction in dialogCommand.actions) {
          dialogButtons.push(getViewFactory().createAction(action));
        }
        var dialogView:UIComponent = null;
        var icon:RIcon = null;
        if(dialogCommand is RemoteDialogCommand) {
          dialogView = getViewFactory().createComponent((dialogCommand as RemoteDialogCommand).view);
          icon = (dialogCommand as RemoteDialogCommand).view.icon;
        } else if(dialogCommand is RemoteFlashDisplayCommand) {
          var url:String = new String((dialogCommand as RemoteFlashDisplayCommand).swfUrl);
          if((dialogCommand as RemoteFlashDisplayCommand).paramNames.length > 0) {
            url = url+"?&"
            for(var i:int = 0; i < (dialogCommand as RemoteFlashDisplayCommand).paramNames.length; i++) {
              if(i > 0) {
                url  = url+"&"
              }
              url = url+(dialogCommand as RemoteFlashDisplayCommand).paramNames[i]+"="+ 
                (dialogCommand as RemoteFlashDisplayCommand).paramValues[i];
            }
          }
          var swfLoader:SWFLoader = new SWFLoader();
          swfLoader.source = url;
          swfLoader.scaleContent = false;
          swfLoader.addEventListener(Event.ADDED_TO_STAGE, function(event:Event):void {
            event.currentTarget.stage.frameRate=96;
          });
          
          dialogView = swfLoader;
        }
        popupDialog(dialogCommand.title, null, dialogView, icon, dialogButtons, dialogCommand.useCurrent, dialogCommand.dimension);
      } else if(command is RemoteCloseDialogCommand) {
        if(_dialogStack && _dialogStack.length > 1) {
          PopUpManager.removePopUp((_dialogStack.pop() as Array)[0] as IFlexDisplayObject);
        }
      } else if(command is RemoteInitCommand) {
        var initCommand:RemoteInitCommand = command as RemoteInitCommand;
        initApplicationFrame(initCommand.workspaceNames,
                             initCommand.workspaceActions,
                             initCommand.exitAction,
                             initCommand.navigationActions,
                             initCommand.actions,
                             initCommand.secondaryActions,
                             initCommand.helpActions);
      } else if(command is RemoteWorkspaceDisplayCommand) {
        var workspaceDisplayCommand:RemoteWorkspaceDisplayCommand = command as RemoteWorkspaceDisplayCommand;
        displayWorkspace(workspaceDisplayCommand.workspaceName,
                         workspaceDisplayCommand.workspaceView);
      } else if(command is RemoteOpenUrlCommand) {
        var urlRequest:URLRequest = new URLRequest((command as RemoteOpenUrlCommand).urlSpec);
        navigateToURL(urlRequest, "_blank");
      } else {
        var targetPeer:IRemotePeer = getRegistered(command.targetPeerGuid);
        if(targetPeer == null) {
          if(_unregistered[command.targetPeerGuid] == null && !(command is RemoteEnablementCommand)) {
            if(!_postponedCommands[command.targetPeerGuid]) {
              _postponedCommands[command.targetPeerGuid] = new ArrayCollection(new Array());
            } 
            (_postponedCommands[command.targetPeerGuid] as IList).addItem(command);
          }
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
          if(targetPeer is RTabContainer) {
            (targetPeer as RTabContainer).selectedIndex =
              (command as RemoteSelectionCommand).leadingIndex;
          } else {
            (targetPeer as RemoteCompositeValueState).leadingIndex =
              (command as RemoteSelectionCommand).leadingIndex;
            (targetPeer as RemoteCompositeValueState).selectedIndices =
              (command as RemoteSelectionCommand).selectedIndices;
          }
        } else if(command is RemoteEnablementCommand) {
          (targetPeer as RAction).enabled =
            (command as RemoteEnablementCommand).enabled;
        } else if(command is RemoteChildrenCommand) {
          var children:IList = (targetPeer as RemoteCompositeValueState).children;
          //children.removeAll();
          var childIndex:int = 0;
          if((command as RemoteChildrenCommand).children != null) {
            for each(var child:RemoteValueState in (command as RemoteChildrenCommand).children) {
              if(isRegistered(child.guid)) {
                child = getRegistered(child.guid) as RemoteValueState;
              } else {
                register(child);
              }
              if(childIndex < children.length) {
                var existingChild:RemoteValueState = children.getItemAt(childIndex) as RemoteValueState;
                if(existingChild != child) {
                  unregister(existingChild);
                  children.setItemAt(child, childIndex);
                }
              } else {
                children.addItem(child);
              }
              childIndex++;
            }
          }
          while(childIndex < children.length) {
            var removedChild:RemoteValueState = children.removeItemAt(childIndex) as RemoteValueState;
            unregister(removedChild);
          }
        } else if(command is RemoteAddCardCommand) {
          getViewFactory().addCard(
            targetPeer as ViewStack,
            (command as RemoteAddCardCommand).card,
            (command as RemoteAddCardCommand).cardName);
        }
      }
    }
    
    protected function pushFakeDialog():void {
      var viewStateGuid:String = (_dialogStack[_dialogStack.length -1] as Array)[1];
      var viewStateAutomationId:String = (_dialogStack[_dialogStack.length -1] as Array)[2];
      _dialogStack.push([_fakeDialog,viewStateGuid,viewStateAutomationId]);
    }
    
    protected function popFakeDialog():void {
      if((_dialogStack[_dialogStack.length -1] as Array)[0] == _fakeDialog) {
        _dialogStack.pop();
      }
    }

    protected function handleFileUpload(uploadCommand:RemoteFileUploadCommand):void {
      _fileReference = new  FileReference();
      _fileReference.addEventListener(Event.SELECT, function(event:Event):void {
        var request:URLRequest = new URLRequest(uploadCommand.fileUrl);
        _fileReference.upload(request);
      });
      _fileReference.addEventListener(DataEvent.UPLOAD_COMPLETE_DATA, function(event:DataEvent):void {
        var xml:XML = new XML(event.data);
        var resourceId:String = xml.@id;
        execute(uploadCommand.successCallbackAction, resourceId);
        popFakeDialog();
      });
      _fileReference.addEventListener(Event.CANCEL, function(event:Event):void {
        execute(uploadCommand.cancelCallbackAction);
        popFakeDialog();
      });
      try {
        _fileReference.browse(createTypeFilters(uploadCommand.fileFilter));
      } catch(error:Error) {
        // we are certainly running on FP 10 or above. Need an extra
        // dialog to initiate the browsing...
        var alertCloseHandler:Function = function(event:CloseEvent):void {
          switch(event.detail) {
            case Alert.YES:
              _fileReference.browse(createTypeFilters(uploadCommand.fileFilter));
              break;
            default:
              popFakeDialog();
              break;
          }
        };
        pushFakeDialog();
        var alert:Alert = Alert.show(ResourceManager.getInstance().getString("Common_messages", "FS.browse.continue"),
               ResourceManager.getInstance().getString("Common_messages", "file.upload"),
               Alert.YES|Alert.NO,
               null,
               alertCloseHandler,
               null,
               Alert.NO);
        fixAlertSize(alert);
      }
    }
    
    protected function handleFileDownload(downloadCommand:RemoteFileDownloadCommand):void {
      _fileReference = new  FileReference();
      _fileReference.addEventListener(Event.CANCEL, function(event:Event):void {
        execute(downloadCommand.cancelCallbackAction, downloadCommand.resourceId);
        popFakeDialog();
      });
      try {
        _fileReference.download(new URLRequest(downloadCommand.fileUrl), downloadCommand.defaultFileName);
      } catch(error:Error) {
        // we are certainly running on FP 10 or above. Need an extra
        // dialog to initiate the browsing...
        var alertCloseHandler:Function = function(event:CloseEvent):void {
          switch(event.detail) {
            case Alert.YES:
              _fileReference.download(new URLRequest(downloadCommand.fileUrl), downloadCommand.defaultFileName);
              break;
            default:
              break;
          }
          popFakeDialog();
        };
        pushFakeDialog();
        var alert:Alert = Alert.show(ResourceManager.getInstance().getString("Common_messages", "FS.browse.continue"),
             ResourceManager.getInstance().getString("Common_messages", "file.download"),
             Alert.YES|Alert.NO,
             null,
             alertCloseHandler,
             null,
             Alert.NO);
        fixAlertSize(alert);
      }
    }
    
    protected function createTypeFilters(filter:Object):Array {
      var typeFilters:Array = null;
      if(filter) {
        typeFilters = new Array();
        for (var name:String in filter) {
          var extensions:Array = filter[name];
          if(extensions) {
            var extensionsString:String = new String();
            if(extensions) {
              for(var i:int = 0; i < extensions.length; i++) {
                extensionsString += ("*" + extensions[i] as String);
                if(i < extensions.length -1) {
                  extensionsString += ";";
                }
              }
            }
            typeFilters.push(new FileFilter(name + " (" + extensionsString + ")", extensionsString));
          }
        }
      }
      return typeFilters;
    }

    protected function handleMessageCommand(messageCommand:RemoteMessageCommand):void {
      var alert:Alert = createAlert(messageCommand);
  
      var alertForm:UIComponent =  alert.mx_internal::alertForm;
      if(messageCommand.messageIcon) {
        var messageIcon:Class = getViewFactory().getIconForComponent(alertForm, messageCommand.messageIcon);
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
        var titleIcon:Class = getViewFactory().getIconForComponent(alert, messageCommand.titleIcon);
        alert.titleIcon = titleIcon;
      }
      fixAlertSize(alert);
    }
    
    protected function fixAlertSize(alert:Alert):void {
      var h:Number = alert.getExplicitOrMeasuredHeight();
      if(alert.getStyle("paddingTop") > 0) {
        h += alert.getStyle("paddingTop");
      }
      if(alert.getStyle("paddingBottom") > 0) {
        h += alert.getStyle("paddingBottom");
      }
      alert.height = h;
    }
    
    protected function createAlert(messageCommand:RemoteMessageCommand):Alert {
      var alert:Alert;
      var alertCloseHandler:Function;
      var message:String = new String(messageCommand.message);
      var isHtml:Boolean = false;
      if(HtmlUtil.isHtml(message)) {
        isHtml = true;
       	// The HTML string must be passed to the show() method, so the width and height of
      	// the textField can be calculated correctly. All HTML tags will be removed and the
      	// <br> and <br/> tag will be replaced by /n (new line).
      	message = message.replace(/<br.*?>/g, "/n");
      	message = message.replace(/<.*?>/g, "");
      }
      if(messageCommand is RemoteOkCancelCommand) {
        alertCloseHandler = function(event:CloseEvent):void {
          switch(event.detail) {
            case Alert.OK:
              execute((messageCommand as RemoteOkCancelCommand).okAction);
              break;
            default:
              execute((messageCommand as RemoteOkCancelCommand).cancelAction);
          }
        };
        alert = Alert.show(message,
                   messageCommand.title,
                   Alert.OK|Alert.CANCEL,
                   null,
                   alertCloseHandler,
                   null,
                   Alert.CANCEL);
      } else if(messageCommand is RemoteYesNoCancelCommand) {
        alertCloseHandler = function(event:CloseEvent):void {
          switch(event.detail) {
            case Alert.YES:
              execute((messageCommand as RemoteYesNoCancelCommand).yesAction);
              break;
            case Alert.NO:
              execute((messageCommand as RemoteYesNoCancelCommand).noAction);
              break;
            default:
              execute((messageCommand as RemoteYesNoCancelCommand).cancelAction);
          }
        };
        alert = Alert.show(message,
                   messageCommand.title,
                   Alert.YES|Alert.NO|Alert.CANCEL,
                   null,
                   alertCloseHandler,
                   null,
                   Alert.CANCEL);
      } else if(messageCommand is RemoteYesNoCommand) {
        alertCloseHandler = function(event:CloseEvent):void {
          switch(event.detail) {
            case Alert.YES:
              execute((messageCommand as RemoteYesNoCommand).yesAction);
              break;
            default:
              execute((messageCommand as RemoteYesNoCommand).noAction);
          }
        };
        alert = Alert.show(message,
                   messageCommand.title,
                   Alert.YES|Alert.NO,
                   null,
                   alertCloseHandler,
                   null,
                   Alert.NO);
      } else {
        alert = Alert.show(message,
                   messageCommand.title,
                   Alert.OK,
                   null,
                   null,
                   null,
                   Alert.OK);
      }
      if(isHtml) {
        alert.mx_internal::alertForm.mx_internal::textField.htmlText = messageCommand.message;
      }
      return alert;
    }

    protected function restart():void {
      var applicationFrame:Application = Application.application as Application;
      applicationFrame.removeAllChildren();
      while(_dialogStack.length > 1) {
        PopUpManager.removePopUp((_dialogStack.pop() as Array)[0] as IFlexDisplayObject);
      }
      _remotePeerRegistry = new BasicRemotePeerRegistry();
      _changeNotificationsEnabled = true;
      _commandsQueue = new ArrayCollection(new Array());
      _dialogStack = new Array();
      _dialogStack.push([null, null, null]);
      start();
    }

    protected function stop():void {
      _remoteController.channelSet.disconnectAll();
    }

    protected function handleError(message:String):void {
      trace("Recieved error : " + message);
    }

    public function getRegistered(guid:String):IRemotePeer {
      return _remotePeerRegistry.getRegistered(guid);
    }

    public function unregister(remotePeer:IRemotePeer):void {
      if(_unregistered && remotePeer) {
        _unregistered[remotePeer.guid] = remotePeer;
      }
      _remotePeerRegistry.unregister(remotePeer);
      if(remotePeer is RemoteValueState) {
        //unbindRemoteValueState(remotePeer as RemoteValueState);
        if(remotePeer is RemoteCompositeValueState) {
          for each(var childState:RemoteValueState in (remotePeer as RemoteCompositeValueState).children) {
            unregister(childState);
          }
        }
      }
    }

    public function isRegistered(guid:String):Boolean {
      return _remotePeerRegistry.isRegistered(guid);
    }
    
    protected function dispatchCommands():void {
      var operation:Operation = _remoteController.operations[HANDLE_COMMANDS_METHOD] as Operation;
      operation.send(_commandsQueue);
      _commandsQueue.removeAll();
    }
    
    protected function initRemoteController():void {
      _remoteController.showBusyCursor = true;
      var commandsHandler:Function = function(resultEvent:ResultEvent):void {
        _postponedCommands = new Object();
        _unregistered = new Object();
        try {
          handleCommands(resultEvent.result as IList);
        } finally {
          checkPostponedCommandsCompletion();
          _postponedCommands = null;
          _unregistered = null;
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
    
    protected function checkPostponedCommandsCompletion():void {
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
    
    protected function createWorkspaceAccordion(workspaceNames:Array,
                                                workspaceActions:RActionList):CollapsibleAccordion {
      var wsAccordion:CollapsibleAccordion = new CollapsibleAccordion();
      wsAccordion.historyManagementEnabled = false;
      wsAccordion.percentHeight = 100.0;
      for(var i:int = 0; i < workspaceActions.actions.length; i++) {
        var workspaceAction:RAction = workspaceActions.actions[i];
        var cardCanvas:Canvas = createWorkspaceAccordionSection(workspaceAction.name, workspaceNames[i]);
        wsAccordion.addAcccordionSection(cardCanvas);
      }
      return wsAccordion;
    }
    
    protected function createWorkspaceAccordionSection(wsLabel:String, wsName:String):Canvas {
      var cardCanvas:Canvas = new Canvas();
      cardCanvas.percentWidth = 100.0;
      cardCanvas.percentHeight = 100.0;
      cardCanvas.horizontalScrollPolicy = ScrollPolicy.OFF;
      cardCanvas.verticalScrollPolicy = ScrollPolicy.OFF;
      cardCanvas.label = wsLabel;
      cardCanvas.name = wsName;
      return cardCanvas;
    }
    
    protected function createWorkspaceViewStack():ViewStack {
      var wsViewStack:ViewStack = new ViewStack();
      wsViewStack.percentWidth = 100.0;
      wsViewStack.percentHeight = 100.0;
      return wsViewStack;
    }
    
    
    protected function initApplicationFrame(workspaceNames:Array,
                                            workspaceActions:RActionList,
                                            exitAction:RAction,
                                            navigationActions:Array,
                                            actions:Array,
                                            secondaryActions:Array,
                                            helpActions:Array):void {
      _workspaceAccordion = createWorkspaceAccordion(workspaceNames, workspaceActions);
      _workspaceAccordion.addEventListener(FlexEvent.CREATION_COMPLETE, function(event:FlexEvent):void {
        for(var i:int = 0; i < workspaceActions.actions.length; i++) {
          var workspaceAction:RAction = workspaceActions.actions[i];
          var section:Canvas = _workspaceAccordion.content[i] as Canvas;
          section.icon = getViewFactory().getIconForComponent(_workspaceAccordion.getHeaderAt(i),
            workspaceAction.icon);
          var button:Button = _workspaceAccordion.getButtonAt(i);
          button.setStyle("icon", getViewFactory().getIconForComponent(button, workspaceAction.icon));
        }
      });
      var accordionHandler:Function = function(event:IndexChangedEvent):void {
        var workspaceIndex:int = event.newIndex;
        execute(workspaceActions.actions[workspaceIndex]);
      };
      _workspaceAccordion.addEventListener(IndexChangedEvent.CHANGE, accordionHandler);
      
      _workspaceViewStack = createWorkspaceViewStack();
      
      var appContent:UIComponent = assembleApplicationContent(_workspaceAccordion, _workspaceViewStack,
        exitAction,
        navigationActions,
        actions,
        secondaryActions,
        helpActions);

      var applicationFrame:Application = Application.application as Application;
      applicationFrame.addChild(appContent);
    }
    
    protected function assembleApplicationContent(navigationAccordion:CollapsibleAccordion,
                                                  mainViewStack:ViewStack,
                                                  exitAction:RAction,
                                                  navigationActions:Array,
                                                  actions:Array,
                                                  secondaryActions:Array,
                                                  helpActions:Array):UIComponent {

      var split:HBox = new HBox();
      split.addChild(navigationAccordion);
      split.addChild(mainViewStack);
      split.percentWidth = 100.0;
      split.percentHeight = 100.0;


      var applicationFrame:Application = Application.application as Application;
      var controlBar:ApplicationControlBar = applicationFrame.controlBar as ApplicationControlBar;
      if(controlBar) {
        controlBar.removeAllChildren();
      } else {
        controlBar = new ApplicationControlBar();
        controlBar.dock = true;
        applicationFrame.addChild(controlBar);
      }
      getViewFactory().installActionLists(controlBar, navigationActions);
      getViewFactory().addSeparator(controlBar);
      var i:int;
      if(actions != null) {
        for(i = 0; i < actions.length; i++) {
          controlBar.addChild(getViewFactory().createPopupButton(actions[i] as RActionList));
        }
      }
      getViewFactory().addSeparator(controlBar);
      var glue:HBox = new HBox();
      glue.percentWidth = 100.0;
      controlBar.addChild(glue);
      if(helpActions != null) {
        for(i = 0; i < helpActions.length; i++) {
          controlBar.addChild(getViewFactory().createPopupButton(helpActions[i] as RActionList));
        }
      }
      getViewFactory().addSeparator(controlBar);
      controlBar.addChild(getViewFactory().createAction(exitAction));

      if(secondaryActions && secondaryActions.length > 0) {
        var secondaryToolBar:UIComponent = getViewFactory().decorateWithSlideBar(getViewFactory().createToolBarFromActionLists(secondaryActions));
        var surroundingBox:VBox = new VBox();
        surroundingBox.percentWidth = 100.0;
        surroundingBox.percentHeight = 100.0;
        surroundingBox.addChild(split);
        surroundingBox.addChild(secondaryToolBar);
        return surroundingBox;
      } else {
        return split;
      }
    }

    protected function createApplicationMenuBar(actions:Array,
                                                helpActions:Array):MenuBar {
      var menuBarModel:Object = new Object();
      var menus:Array = new Array();
      menus = menus.concat(getViewFactory().createMenus(actions, false));
      menus = menus.concat(getViewFactory().createMenus(helpActions, true));
      menuBarModel["children"] = menus;
      
      var menuBar:MenuBar = new MenuBar();
      menuBar.setStyle("cornerRadius", 5);
      menuBar.percentWidth = 100.0;
      menuBar.showRoot = false;
      
      menuBar.menuBarItemRenderer = new ClassFactory(RIconMenuBarItem);

      menuBar.dataProvider = menus;
      
      for(var i:int = 0; i < menus.length; i++) {
        var menu:Menu = menuBar.getMenuAt(i); 
        menu.itemRenderer = new ClassFactory(RIconMenuItemRenderer);
      }
      
      var menuHandler:Function = function(event:MenuEvent):void  {
        if (event.item["data"] is RAction) {
          execute(event.item["data"] as RAction, null);
        }        
      };
      menuBar.addEventListener(MenuEvent.ITEM_CLICK, menuHandler);

      return menuBar;                                            
    }
    
    public function start():void {
      var operation:Operation = _remoteController.operations[START_METHOD] as Operation;
      operation.send(_userLanguage);
    }
    
    protected function displayWorkspace(workspaceName:String, workspaceView:RComponent):void {
      if(workspaceView) {
        var workspaceNavigator:RComponent = null;
        if(workspaceView is RSplitContainer) {
          workspaceNavigator = (workspaceView as RSplitContainer).leftTop;
          workspaceView = (workspaceView as RSplitContainer).rightBottom;
        }
        var cardCanvas:Canvas = new Canvas();
        cardCanvas.percentWidth = 100.0;
        cardCanvas.percentHeight = 100.0;
        cardCanvas.name = workspaceName;
        _workspaceViewStack.addChild(cardCanvas);

        var workspaceViewUI:UIComponent = getViewFactory().createComponent(workspaceView);
        workspaceViewUI.percentWidth = 100.0;
        workspaceViewUI.percentHeight = 100.0;
        cardCanvas.addChild(workspaceViewUI);
        
        if(workspaceNavigator) {
          var workspaceNavigatorUI:UIComponent = getViewFactory().createComponent(workspaceNavigator);
          workspaceNavigatorUI.percentWidth = 100.0;
          workspaceNavigatorUI.percentHeight = 100.0;
          if(workspaceNavigatorUI is Tree) {
            var tree:Tree = workspaceNavigatorUI as Tree;
            tree.showRoot = false;
            // workaround since top collection is not monitored
            // when showRoot=false
            var state:RemoteCompositeValueState = workspaceNavigator.state as RemoteCompositeValueState;
            BindingUtils.bindSetter(function(selectedIndices:Array):void {
              if(selectedIndices != null && selectedIndices.length > 0) {
                // work on items to translate indices independently of table sorting state.
                var selectedItems:Array = new Array(selectedIndices.length);
                for(var i:int = 0; i < selectedIndices.length; i++) {
                  selectedItems[i] = state.children.getItemAt(selectedIndices[i]);
                }
                if(!ArrayUtil.areUnorderedArraysEqual(tree.selectedItems, selectedItems)) {
                  tree.selectedItems = selectedItems;
                }
              } else {
                tree.selectedItems = [];
              }
            }, state, "selectedIndices", true);
          }
          (_workspaceAccordion.getAcccordionSectionByName(workspaceName) as Container).addChild(workspaceNavigatorUI);
        }
      }
      _workspaceViewStack.selectedChild = _workspaceViewStack.getChildByName(workspaceName) as Container;
      _workspaceAccordion.selectedChild = _workspaceAccordion.getAcccordionSectionByName(workspaceName) as Container;
    }
    
    protected function performLogin():void {
      var loginCommand:RemoteLoginCommand = new RemoteLoginCommand();
      registerCommand(loginCommand);
    }
    
    protected function popupDialog(title:String, message:String, dialogView:UIComponent, icon:RIcon, buttons:Array, useCurrent:Boolean=false, dimension:Dimension=null):void {
      if(dimension != null) {
        dialogView.width = dimension.width;
        dialogView.height = dimension.height;
      } else {
        dialogView.percentWidth = 100.0;
        dialogView.percentHeight = 100.0;
      }
      dialogView.maxWidth = 90.0 * (Application.application as DisplayObject).width / 100.0;
      dialogView.maxHeight = 90.0 * (Application.application as DisplayObject).height / 100.0;
      var buttonBox:HBox = new HBox();
      buttonBox.setStyle("horizontalAlign","right");
      buttonBox.setStyle("paddingLeft", 5);
      buttonBox.setStyle("paddingRight", 5);
      buttonBox.setStyle("paddingTop", 0);
      buttonBox.setStyle("paddingBottom", 5);
      buttonBox.percentWidth = 100.0;
      
      var dialogBox:VBox = new VBox();
      if(message) {
        var messageLabel:Label = new Label();
        messageLabel.setStyle("paddingLeft", 10);
        messageLabel.setStyle("paddingRight", 10);
        messageLabel.setStyle("paddingTop", 10);
        messageLabel.setStyle("paddingBottom", 0);
        messageLabel.percentWidth = 100.0;
        messageLabel.text = message;
        dialogBox.addChild(messageLabel);
      }
      dialogBox.addChild(dialogView);
      var separator:HRule = new HRule();
      separator.percentWidth = 100.0;
      dialogBox.addChild(separator);
      for each(var button:Button in buttons) {
        if(!dialogBox.defaultButton) {
          dialogBox.defaultButton = button;
        }
        buttonBox.addChild(button);
      }
      dialogBox.addChild(buttonBox);

      var dialog:ResizablePanel;
      var newDialog:Boolean = true;
      if(useCurrent && _dialogStack && _dialogStack.length > 1) {
        dialog = (_dialogStack[_dialogStack.length -1] as Array)[0] as ResizablePanel;
        dialog.removeAllChildren();
        newDialog = false;
      } else {
        var dialogParent:DisplayObject;
        if(_dialogStack && _dialogStack.length > 1) {
          dialogParent = _dialogStack[_dialogStack.length -1][0];
        } else {
          dialogParent = Application.application as DisplayObject;
        }
        dialog = PopUpManager.createPopUp(dialogParent,ResizablePanel,true) as ResizablePanel;
        dialog.resizable = true;
        dialog.setStyle("borderAlpha", 1);
        dialog.setStyle("borderThicknessLeft", 5);
        dialog.setStyle("borderThicknessRight", 5);
        dialog.setStyle("borderThicknessBottom", 5);
        dialog.horizontalScrollPolicy = ScrollPolicy.OFF;
        dialog.verticalScrollPolicy = ScrollPolicy.OFF;
        _dialogStack.push([dialog, null]);
      }
      dialog.title = title;
      if(icon) {
        dialog.titleIcon = getViewFactory().getIconForComponent(dialog, icon);
      }
      dialogBox.percentWidth = 100.0;
      dialogBox.percentHeight = 100.0;
      var applicationFrame:Application = Application.application as Application;
      dialogBox.maxHeight = applicationFrame.height * 95 / 100;
      dialogBox.maxWidth = applicationFrame.width * 95 / 100;
      dialog.addChild(dialogBox);
      dialogView.addEventListener(FlexEvent.CREATION_COMPLETE, function(evt:FlexEvent):void {
        dialog.minWidth = dialogBox.minWidth * 1.2;
        dialog.minHeight = dialogBox.minHeight * 1.2;
        });
      if(newDialog) {
        PopUpManager.centerPopUp(dialog);
      }
    }
    
    public function setCurrentViewStateGuid(component:UIComponent, viewStateGuid:String, viewStateAutomationId:String):void {
      if(_dialogStack.length > 1) {
        // at least a dialog is open
        for(var i:int = _dialogStack.length -1; i > 0 ; i--) {
          // Find the owning dialog that may not be the topmost one.
          var dialog:Array = _dialogStack[_dialogStack.length -i] as Array; 
          if((dialog[0] as Panel).contains(component)) {
            dialog[1] = viewStateGuid;
            dialog[2] = viewStateAutomationId;
            return;
          }
        }
      }
      (_dialogStack[0] as Array)[1] = viewStateGuid;
      (_dialogStack[0] as Array)[2] = viewStateAutomationId;
    }
    
    protected function getViewFactory():DefaultFlexViewFactory {
      return _viewFactory;
    }
    
    protected function registerRemoteClasses():void {
      registerClassAlias("org.jspresso.framework.application.frontend.command.remote.RemoteCommand",RemoteCommand);
      registerClassAlias("org.jspresso.framework.application.frontend.command.remote.RemoteReadabilityCommand",RemoteReadabilityCommand);
      registerClassAlias("org.jspresso.framework.application.frontend.command.remote.RemoteWritabilityCommand",RemoteWritabilityCommand);
      registerClassAlias("org.jspresso.framework.application.frontend.command.remote.RemoteActionCommand",RemoteActionCommand);
      registerClassAlias("org.jspresso.framework.application.frontend.command.remote.RemoteChildrenCommand",RemoteChildrenCommand);
      registerClassAlias("org.jspresso.framework.application.frontend.command.remote.RemoteEnablementCommand",RemoteEnablementCommand);
      registerClassAlias("org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand",RemoteSelectionCommand);
      registerClassAlias("org.jspresso.framework.application.frontend.command.remote.RemoteValueCommand",RemoteValueCommand);
      registerClassAlias("org.jspresso.framework.application.frontend.command.remote.RemoteAddCardCommand",RemoteAddCardCommand);
  
      registerClassAlias("org.jspresso.framework.util.gui.CellConstraints",CellConstraints);
      registerClassAlias("org.jspresso.framework.util.gui.Dimension",Dimension);
      registerClassAlias("org.jspresso.framework.util.gui.Font", Font);

      registerClassAlias("org.jspresso.framework.state.remote.RemoteCompositeValueState",RemoteCompositeValueState);
      registerClassAlias("org.jspresso.framework.state.remote.RemoteFormattedValueState", RemoteFormattedValueState);
      registerClassAlias("org.jspresso.framework.state.remote.RemoteValueState", RemoteValueState);
  
      registerClassAlias("org.jspresso.framework.util.remote.IRemotePeer",IRemotePeer);
      registerClassAlias("org.jspresso.framework.util.remote.RemotePeer", RemotePeer);
  
      registerClassAlias("org.jspresso.framework.gui.remote.RAction", RAction);
      registerClassAlias("org.jspresso.framework.gui.remote.RActionComponent", RActionComponent);
      registerClassAlias("org.jspresso.framework.gui.remote.RActionField", RActionField);
      registerClassAlias("org.jspresso.framework.gui.remote.RBorderContainer", RBorderContainer);
      registerClassAlias("org.jspresso.framework.gui.remote.RCardContainer", RCardContainer);
      registerClassAlias("org.jspresso.framework.gui.remote.RCheckBox", RCheckBox);
      registerClassAlias("org.jspresso.framework.gui.remote.RColorField", RColorField);
      registerClassAlias("org.jspresso.framework.gui.remote.RComboBox", RComboBox);
      registerClassAlias("org.jspresso.framework.gui.remote.RComponent", RComponent);
      registerClassAlias("org.jspresso.framework.gui.remote.RContainer", RContainer);
      registerClassAlias("org.jspresso.framework.gui.remote.RDateField", RDateField);
      registerClassAlias("org.jspresso.framework.gui.remote.RDecimalField", RDecimalField);
      registerClassAlias("org.jspresso.framework.gui.remote.RDurationField", RDurationField);
      registerClassAlias("org.jspresso.framework.gui.remote.RForm", RForm);
      registerClassAlias("org.jspresso.framework.gui.remote.REvenGridContainer", REvenGridContainer);
      registerClassAlias("org.jspresso.framework.gui.remote.RConstrainedGridContainer", RConstrainedGridContainer);
      registerClassAlias("org.jspresso.framework.gui.remote.RIcon", RIcon);
      registerClassAlias("org.jspresso.framework.gui.remote.RImageComponent", RImageComponent);
      registerClassAlias("org.jspresso.framework.gui.remote.RIntegerField", RIntegerField);
      registerClassAlias("org.jspresso.framework.gui.remote.RLabel", RLabel);
      registerClassAlias("org.jspresso.framework.gui.remote.RLink", RLink);
      registerClassAlias("org.jspresso.framework.gui.remote.RList", RList);
      registerClassAlias("org.jspresso.framework.gui.remote.RPasswordField", RPasswordField);
      registerClassAlias("org.jspresso.framework.gui.remote.RPercentField", RPercentField);
      registerClassAlias("org.jspresso.framework.gui.remote.RSplitContainer", RSplitContainer);
      registerClassAlias("org.jspresso.framework.gui.remote.RTabContainer", RTabContainer);
      registerClassAlias("org.jspresso.framework.gui.remote.RTable", RTable);
      registerClassAlias("org.jspresso.framework.gui.remote.RTextArea", RTextArea);
      registerClassAlias("org.jspresso.framework.gui.remote.RTextField", RTextField);
      registerClassAlias("org.jspresso.framework.gui.remote.RTimeField", RTimeField);
      registerClassAlias("org.jspresso.framework.gui.remote.RTree", RTree);
    }
  }
}