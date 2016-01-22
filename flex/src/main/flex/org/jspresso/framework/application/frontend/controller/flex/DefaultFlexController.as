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

package org.jspresso.framework.application.frontend.controller.flex {

import flash.desktop.Clipboard;
import flash.desktop.ClipboardFormats;
import flash.display.DisplayObject;
import flash.display.Sprite;
import flash.events.DataEvent;
import flash.events.Event;
import flash.events.IOErrorEvent;
import flash.events.MouseEvent;
import flash.external.ExternalInterface;
import flash.net.FileFilter;
import flash.net.FileReference;
import flash.net.URLRequest;
import flash.net.navigateToURL;
import flash.utils.getTimer;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.collections.IList;
import mx.collections.ListCollectionView;
import mx.containers.ApplicationControlBar;
import mx.containers.Canvas;
import mx.containers.HBox;
import mx.containers.HDividedBox;
import mx.containers.Panel;
import mx.containers.TabNavigator;
import mx.containers.VBox;
import mx.containers.ViewStack;
import mx.containers.dividedBoxClasses.BoxDivider;
import mx.controls.Alert;
import mx.controls.Button;
import mx.controls.HRule;
import mx.controls.Label;
import mx.controls.Menu;
import mx.controls.MenuBar;
import mx.controls.SWFLoader;
import mx.controls.Text;
import mx.controls.Tree;
import mx.controls.alertClasses.AlertForm;
import mx.core.Application;
import mx.core.ClassFactory;
import mx.core.Container;
import mx.core.FlexGlobals;
import mx.core.IFlexDisplayObject;
import mx.core.ScrollPolicy;
import mx.core.UIComponent;
import mx.core.mx_internal;
import mx.events.BrowserChangeEvent;
import mx.events.CloseEvent;
import mx.events.FlexEvent;
import mx.events.IndexChangedEvent;
import mx.events.MenuEvent;
import mx.managers.BrowserManager;
import mx.managers.IBrowserManager;
import mx.managers.PopUpManager;
import mx.resources.Locale;
import mx.resources.ResourceManager;
import mx.rpc.AbstractOperation;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.mxml.RemoteObject;
import mx.utils.URLUtil;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler;
import org.jspresso.framework.application.frontend.command.remote.RemoteAbstractDialogCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteActionCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteAddCardCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteApplicationDescriptionCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteChildrenCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteCleanupCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteClipboardCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteCloseDialogCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteDialogCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteEditCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteEnablementCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteErrorMessageCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteFileDownloadCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteFileUploadCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteFlashDisplayCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteFocusCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteHistoryDisplayCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteInitCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteInitLoginCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteLocaleCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteMessageCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteOkCancelCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteOpenUrlCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteReadabilityCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteRefreshCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteRestartCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteStartCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteUpdateStatusCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteValueCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteWorkspaceDisplayCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteWritabilityCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteYesNoCancelCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteYesNoCommand;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RActionEvent;
import org.jspresso.framework.gui.remote.RActionList;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.RIcon;
import org.jspresso.framework.gui.remote.RSplitContainer;
import org.jspresso.framework.gui.remote.RTabContainer;
import org.jspresso.framework.state.remote.RemoteCompositeValueState;
import org.jspresso.framework.state.remote.RemoteFormattedValueState;
import org.jspresso.framework.state.remote.RemoteValueState;
import org.jspresso.framework.util.array.ArrayUtil;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.html.HtmlUtil;
import org.jspresso.framework.util.remote.IRemotePeer;
import org.jspresso.framework.util.remote.registry.BasicRemotePeerRegistry;
import org.jspresso.framework.util.remote.registry.IRemotePeerRegistry;
import org.jspresso.framework.view.flex.CollapsibleAccordion;
import org.jspresso.framework.view.flex.DefaultFlexViewFactory;
import org.jspresso.framework.view.flex.EnhancedButton;
import org.jspresso.framework.view.flex.EnhancedTabNavigator;
import org.jspresso.framework.view.flex.RIconMenuBarItem;
import org.jspresso.framework.view.flex.RIconMenuItemRenderer;

public class DefaultFlexController implements IRemotePeerRegistry, IActionHandler, IRemoteCommandHandler {
  private static const JSPRESSO_VERSION:String = VERSIONS::jspresso_version;
  private static const HANDLE_COMMANDS_METHOD:String = "handleCommands";
  private static const START_METHOD:String = "start";
  private static const STOP_METHOD:String = "stop";
  private var _remoteController:RemoteObject;
  private var _viewFactory:DefaultFlexViewFactory;
  private var _remotePeerRegistry:IRemotePeerRegistry;
  private var _lastFiredActions:Object;
  private var _changeNotificationsEnabled:Boolean;
  private var _commandsQueue:IList;
  private var _workspaceAccordion:CollapsibleAccordion;
  private var _workspaceViewStack:ViewStack;
  private var _statusBar:Label;
  private var _postponedCommands:Object;
  private var _dialogStack:Array;
  private var _userLanguage:String;
  private var _fileReference:FileReference;
  private var _initialLocaleChain:Array;
  private var _fakeDialog:Panel;
  private var _translations:Object;
  private var _nextActionCallback:Function;
  private var _postponedChildrenNotificationBuffer:Array;
  private var _postponedSelectionCommands:Object;
  private var _postponedEditionCommands:Array;
  private var _userGeoLocation:Object;
  private var _applicationName:String;


  public function DefaultFlexController(remoteController:RemoteObject, userLanguage:String) {
    _remotePeerRegistry = new BasicRemotePeerRegistry();
    _lastFiredActions = {};
    _viewFactory = createViewFactory();
    _changeNotificationsEnabled = true;
    _remoteController = remoteController;
    _commandsQueue = new ArrayCollection([]);
    _dialogStack = [];
    _dialogStack.push([null, null, null]);
    _userLanguage = userLanguage;
    _initialLocaleChain = ResourceManager.getInstance().localeChain;
    _fakeDialog = getViewFactory().createPanelComponent();
    initRemoteController();
    registerRemoteClasses();
    initGeoLocation();
  }

  protected function createViewFactory():DefaultFlexViewFactory {
    return new DefaultFlexViewFactory(this, this, this);
  }

  public function register(remotePeer:IRemotePeer):void {
    if (!remotePeer) {
      return;
    }
    if (!isRegistered(remotePeer.guid)) {
      _remotePeerRegistry.register(remotePeer);
      if (remotePeer is RemoteValueState) {
        bindRemoteValueState(remotePeer as RemoteValueState);
        if (remotePeer is RemoteCompositeValueState) {
          for each(var childState:RemoteValueState in (remotePeer as RemoteCompositeValueState).children) {
            register(childState);
          }
        }
      } else if (remotePeer is RComponent) {
        register((remotePeer as RComponent).state);
      }
      if (_postponedCommands) {
        if (_postponedCommands[remotePeer.guid]) {
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

      var valueListener:Function = function (value:Object):void {
        valueUpdated(remoteValueState);
      };
      BindingUtils.bindSetter(valueListener, remoteValueState, "value", true);

      if (remoteValueState is RemoteCompositeValueState) {
        var selectedIndicesListener:Function = function (selectedIndices:Array):void {
          selectedIndicesUpdated(remoteValueState as RemoteCompositeValueState);
        };
        BindingUtils.bindSetter(selectedIndicesListener, remoteValueState, "selectedIndices", true);
      }
    } finally {
      _changeNotificationsEnabled = wasEnabled;
    }
  }

  protected function valueUpdated(remoteValueState:RemoteValueState):void {
    if (_changeNotificationsEnabled) {
      //trace(">>> Value update <<< " + remoteValueState.value);
      var command:RemoteValueCommand = new RemoteValueCommand();
      command.targetPeerGuid = remoteValueState.guid;
      command.permId = remoteValueState.permId;
      command.value = remoteValueState.value;
      registerCommand(command);
    }
  }

  protected function selectedIndicesUpdated(remoteCompositeValueState:RemoteCompositeValueState):void {
    if (_changeNotificationsEnabled) {
      //trace(">>> Selected indices update <<< " + remoteCompositeValueState.selectedIndices + " on " + remoteCompositeValueState.value);
      var command:RemoteSelectionCommand = new RemoteSelectionCommand();
      command.targetPeerGuid = remoteCompositeValueState.guid;
      command.permId = remoteCompositeValueState.permId;
      command.selectedIndices = remoteCompositeValueState.selectedIndices;
      command.leadingIndex = remoteCompositeValueState.leadingIndex;
      registerCommand(command);
    }
  }

  public function execute(action:RAction, actionEvent:RActionEvent = null, actionCallback:Function = null,
                          disableUI:Boolean = true):void {
    //trace(">>> Execute <<< " + action.name + " param = " + param);
    if (action && action.enabled) {
      var ts:Number = getTimer();
      var lastTs:Number = _lastFiredActions[action.guid];
      if (isNaN(lastTs) || (ts - lastTs) > 500) {
        _lastFiredActions[action.guid] = ts;
        var command:RemoteActionCommand = new RemoteActionCommand();
        command.targetPeerGuid = action.guid;
        command.permId = action.permId;
        if (actionEvent == null) {
          actionEvent = new RActionEvent();
        }
        command.actionEvent = actionEvent;
        actionEvent.viewStateGuid = (_dialogStack[_dialogStack.length - 1] as Array)[1];
        actionEvent.viewStatePermId = (_dialogStack[_dialogStack.length - 1] as Array)[2];
        _nextActionCallback = actionCallback;
        registerCommand(command);
        blockUI(!disableUI);
      }
    }
  }

  public function refresh():void {
    registerCommand(new RemoteRefreshCommand());
  }

  public function registerCommand(command:RemoteCommand):void {
    if (_changeNotificationsEnabled) {
      //trace("Command registered for next round trip : " + command);
      _commandsQueue.addItem(command);
      dispatchCommands();
      _commandsQueue.removeAll();
    }
  }

  protected function handleCommands(commands:IList):void {
    //trace("Received commands :");
    var wasEnabled:Boolean = _changeNotificationsEnabled;
    try {
      _changeNotificationsEnabled = false;
      if (commands != null) {
        for (var i:int = 0; i < commands.length; i++) {
          //trace("  -> " + command);
          handleCommand(commands.getItemAt(i) as RemoteCommand);
        }
      }
    } finally {
      _changeNotificationsEnabled = wasEnabled;
    }
  }

  protected function handleCommand(command:RemoteCommand):void {
    if (command is RemoteMessageCommand) {
      handleMessageCommand(command as RemoteMessageCommand);
    } else if (command is RemoteHistoryDisplayCommand) {
      if ((command as RemoteHistoryDisplayCommand).name) {
        BrowserManager.getInstance().setTitle((command as RemoteHistoryDisplayCommand).name);
      } else {
        BrowserManager.getInstance().setTitle("");
      }
      if ((command as RemoteHistoryDisplayCommand).snapshotId) {
        BrowserManager.getInstance().setFragment("snapshotId=" + (command as RemoteHistoryDisplayCommand).snapshotId);
      }
    } else if (command is RemoteRestartCommand) {
      restart();
    } else if (command is RemoteFileUploadCommand) {
      handleFileUpload(command as RemoteFileUploadCommand);
    } else if (command is RemoteFileDownloadCommand) {
      handleFileDownload(command as RemoteFileDownloadCommand);
    } else if (command is RemoteLocaleCommand) {
      var locale:Locale;
      for each (var availableLocaleString:String in ResourceManager.getInstance().getLocales()) {
        var availableLocale:Locale = new Locale(availableLocaleString);
        if (!locale && (command as RemoteLocaleCommand).language == availableLocale.language) {
          locale = availableLocale;
        }
      }
      if (locale) {
        ResourceManager.getInstance().localeChain = [locale.toString()].concat(_initialLocaleChain);
      }
      getViewFactory().datePattern = (command as RemoteLocaleCommand).datePattern.toUpperCase();
      getViewFactory().firstDayOfWeek = (command as RemoteLocaleCommand).firstDayOfWeek;
      getViewFactory().decimalSeparator = (command as RemoteLocaleCommand).decimalSeparator;
      getViewFactory().thousandsSeparator = (command as RemoteLocaleCommand).thousandsSeparator;
      _translations = (command as RemoteLocaleCommand).translations;
    } else if (command is RemoteInitLoginCommand) {
      var initLoginCommand:RemoteInitLoginCommand = command as RemoteInitLoginCommand;
      var rLoginView:RComponent = initLoginCommand.loginView;
      var loginView:UIComponent = getViewFactory().createComponent(rLoginView);
      popupDialog(rLoginView.label, rLoginView.toolTip, loginView, rLoginView.icon,
                  extractAllActions(initLoginCommand.loginActionLists), false, null, initLoginCommand.secondaryLoginActionLists);
    } else if (command is RemoteCleanupCommand) {
      var removedPeerGuids:Array = (command as RemoteCleanupCommand).removedPeerGuids;
      for (var rpeerIndex:int = 0; rpeerIndex < removedPeerGuids.length; rpeerIndex++) {
        var removedPeer:IRemotePeer = getRegistered(removedPeerGuids[rpeerIndex]);
        if (removedPeer) {
          unregister(removedPeer);
        }
      }
    } else if (command is RemoteApplicationDescriptionCommand) {
      name = (command as RemoteApplicationDescriptionCommand).applicationName;
    } else if (command is RemoteAbstractDialogCommand) {
      var dialogCommand:RemoteAbstractDialogCommand = command as RemoteAbstractDialogCommand;
      var dialogButtons:Array = [];
      for each(var action:RAction in dialogCommand.actions) {
        if (action != null) {
          dialogButtons.push(getViewFactory().createDialogAction(action));
        }
      }
      var dialogView:UIComponent = null;
      var icon:RIcon = null;
      if (dialogCommand is RemoteDialogCommand) {
        dialogView = getViewFactory().createComponent((dialogCommand as RemoteDialogCommand).view);
        icon = (dialogCommand as RemoteDialogCommand).view.icon;
      } else if (dialogCommand is RemoteFlashDisplayCommand) {
        var url:String = (dialogCommand as RemoteFlashDisplayCommand).swfUrl;
        if ((dialogCommand as RemoteFlashDisplayCommand).paramNames.length > 0) {
          url = url + "?&";
          for (var i:int = 0; i < (dialogCommand as RemoteFlashDisplayCommand).paramNames.length; i++) {
            if (i > 0) {
              url = url + "&";
            }
            url = url + (dialogCommand as RemoteFlashDisplayCommand).paramNames[i] + "=" + (dialogCommand
                as RemoteFlashDisplayCommand).paramValues[i];
          }
        }
        var swfLoader:SWFLoader = new SWFLoader();
        swfLoader.source = url;
        swfLoader.scaleContent = false;
        swfLoader.addEventListener(Event.ADDED_TO_STAGE, function (event:Event):void {
          (event.currentTarget as Application).stage.frameRate = 96;
        });

        dialogView = swfLoader;
      }
      popupDialog(dialogCommand.title, null, dialogView, icon, dialogButtons, dialogCommand.useCurrent,
                  dialogCommand.dimension);
    } else if (command is RemoteCloseDialogCommand) {
      closeDialog();
    } else if (command is RemoteInitCommand) {
      var initCommand:RemoteInitCommand = command as RemoteInitCommand;
      this.name = initCommand.applicationName;
      linkBrowserHistory();
      initApplicationFrame(initCommand);
    } else if (command is RemoteWorkspaceDisplayCommand) {
      var workspaceDisplayCommand:RemoteWorkspaceDisplayCommand = command as RemoteWorkspaceDisplayCommand;
      displayWorkspace(workspaceDisplayCommand.workspaceName, workspaceDisplayCommand.workspaceView);
    } else if (command is RemoteOpenUrlCommand) {
      var urlRequest:URLRequest = new URLRequest((command as RemoteOpenUrlCommand).urlSpec);
      navigateToURL(urlRequest, (command as RemoteOpenUrlCommand).target);
    } else if (command is RemoteUpdateStatusCommand) {
      var status:String = (command as RemoteUpdateStatusCommand).status;
      if (status != null && status.length > 0) {
        if (HtmlUtil.isHtml(status)) {
          _statusBar.text = null;
          _statusBar.htmlText = HtmlUtil.sanitizeHtml(status);
        } else {
          _statusBar.htmlText = null;
          _statusBar.text = status;
        }
        _statusBar.visible = true;
      } else {
        _statusBar.visible = false;
      }
    } else if (command is RemoteClipboardCommand) {
      handleClipboardCommand(command as RemoteClipboardCommand);
    } else {
      var targetPeer:IRemotePeer = getRegistered(command.targetPeerGuid);
      if (targetPeer == null) {
        if (!_postponedCommands[command.targetPeerGuid]) {
          _postponedCommands[command.targetPeerGuid] = new ArrayCollection([]);
        }
        (_postponedCommands[command.targetPeerGuid] as IList).addItem(command);
        return;
      }
      if (command is RemoteValueCommand) {
        (targetPeer as RemoteValueState).value = (command as RemoteValueCommand).value;
        if (targetPeer is RemoteFormattedValueState) {
          (targetPeer as RemoteFormattedValueState).valueAsObject = (command as RemoteValueCommand).valueAsObject;
        } else if (targetPeer is RemoteCompositeValueState) {
          (targetPeer as RemoteCompositeValueState).description = (command as RemoteValueCommand).description;
          (targetPeer as RemoteCompositeValueState).iconImageUrl = (command as RemoteValueCommand).iconImageUrl;
        }
      } else if (command is RemoteReadabilityCommand) {
        (targetPeer as RemoteValueState).readable = (command as RemoteReadabilityCommand).readable;
      } else if (command is RemoteWritabilityCommand) {
        (targetPeer as RemoteValueState).writable = (command as RemoteWritabilityCommand).writable;
      } else if (command is RemoteSelectionCommand) {
        if (targetPeer is RTabContainer) {
          (targetPeer as RTabContainer).selectedIndex = (command as RemoteSelectionCommand).leadingIndex;
        } else {
          if (_postponedChildrenNotificationBuffer.indexOf(targetPeer.guid) >= 0) {
            _postponedSelectionCommands[targetPeer.guid] = command;
          } else {
            (targetPeer as RemoteCompositeValueState).leadingIndex = (command as RemoteSelectionCommand).leadingIndex;
            (targetPeer as RemoteCompositeValueState).selectedIndices = (command
                as RemoteSelectionCommand).selectedIndices;
          }
        }
      } else if (command is RemoteEnablementCommand) {
        (targetPeer as RAction).enabled = (command as RemoteEnablementCommand).enabled;
      } else if (command is RemoteChildrenCommand) {
        var children:ListCollectionView = (targetPeer as RemoteCompositeValueState).children;
        _postponedChildrenNotificationBuffer.push(targetPeer.guid);
        if ((command as RemoteChildrenCommand).remove) {
          for each(var removedChild:RemoteValueState in (command as RemoteChildrenCommand).children) {
            if (isRegistered(removedChild.guid)) {
              removedChild = getRegistered(removedChild.guid) as RemoteValueState;
              var removedIndex:int = children.getItemIndex(removedChild);
              if (removedIndex >= 0) {
                children.removeItemAt(removedIndex);
              }
            }
          }
        } else {
          if ((command as RemoteChildrenCommand).children != null) {
            var newChildren:ArrayCollection = new ArrayCollection();
            for each(var child:RemoteValueState in (command as RemoteChildrenCommand).children) {
              if (isRegistered(child.guid)) {
                child = getRegistered(child.guid) as RemoteValueState;
              } else {
                register(child);
              }
              newChildren.addItem(child);
            }
            for (var toRemove:int = children.length - 1; toRemove >= 0; toRemove--) {
              if (newChildren.getItemIndex(children.getItemAt(toRemove)) < 0) {
                children.removeItemAt(toRemove);
              }
            }
            var index:int = 0;
            for each(var newChild:RemoteValueState in newChildren) {
              var existingIndex:int = children.getItemIndex(newChild);
              if (existingIndex != index) {
                if (existingIndex >= 0) {
                  children.removeItemAt(existingIndex);
                }
                children.addItemAt(newChild, index);
              }
              index++;
            }
          } else {
            children.removeAll();
          }
        }
      } else {
        var targetComponent:RComponent = targetPeer as RComponent;
        if (command is RemoteAddCardCommand) {
          getViewFactory().addCard(targetComponent.retrievePeer() as ViewStack, (command as RemoteAddCardCommand).card,
                                   (command as RemoteAddCardCommand).cardName);
        } else if (command is RemoteFocusCommand) {
          getViewFactory().focus(targetComponent.retrievePeer());
        } else if (command is RemoteEditCommand) {
          _postponedEditionCommands.push(command);
        }
      }
    }
  }

  private function closeDialog():void {
    if (_dialogStack && _dialogStack.length > 1) {
      PopUpManager.removePopUp((_dialogStack.pop() as Array)[0] as UIComponent);
    }
  }

  private function linkBrowserHistory():void {
    var browserManager:IBrowserManager = BrowserManager.getInstance();
    browserManager.init();
    browserManager.addEventListener(BrowserChangeEvent.BROWSER_URL_CHANGE, function (event:BrowserChangeEvent):void {
      var oldSnapshotId:String = URLUtil.stringToObject(event.lastURL.substr(event.lastURL.lastIndexOf("#") + 1))["snapshotId"];
      var newSnapshotId:String = URLUtil.stringToObject(event.url.substr(event.url.lastIndexOf("#") + 1))["snapshotId"];
      if (oldSnapshotId && newSnapshotId && oldSnapshotId != newSnapshotId) {
        var command:RemoteHistoryDisplayCommand = new RemoteHistoryDisplayCommand();
        command.snapshotId = newSnapshotId;
        registerCommand(command);
      }
    });
  }

  protected function pushFakeDialog():void {
    var viewStateGuid:String = (_dialogStack[_dialogStack.length - 1] as Array)[1];
    var viewStatePermId:String = (_dialogStack[_dialogStack.length - 1] as Array)[2];
    _dialogStack.push([_fakeDialog, viewStateGuid, viewStatePermId]);
  }

  protected function popFakeDialog():void {
    if ((_dialogStack[_dialogStack.length - 1] as Array)[0] == _fakeDialog) {
      _dialogStack.pop();
    }
  }

  protected function handleFileUpload(uploadCommand:RemoteFileUploadCommand):void {
    _fileReference = new FileReference();
    _fileReference.addEventListener(Event.SELECT, function (event:Event):void {
      blockUI(false);
      var request:URLRequest = new URLRequest(uploadCommand.fileUrl);
      _fileReference.upload(request);
    });
    _fileReference.addEventListener(DataEvent.UPLOAD_COMPLETE_DATA, function (event:DataEvent):void {
      blockUI(true);
      var xml:XML = new XML(event.data);
      var resourceId:String = xml.@id;
      var actionEvent:RActionEvent = new RActionEvent();
      actionEvent.actionCommand = resourceId;
      execute(uploadCommand.successCallbackAction, actionEvent);
      popFakeDialog();
    });
    _fileReference.addEventListener(Event.CANCEL, function (event:Event):void {
      blockUI(true);
      execute(uploadCommand.cancelCallbackAction);
      popFakeDialog();
    });
    _fileReference.addEventListener(IOErrorEvent.IO_ERROR, function (event:IOErrorEvent):void {
      blockUI(true);
      popupError(event.text);
      popFakeDialog();
    });
    try {
      _fileReference.browse(createTypeFilters(uploadCommand.fileFilter));
    } catch (error:Error) {
      // we are certainly running on FP 10 or above. Need an extra
      // dialog to initiate the browsing...
      var alertCloseHandler:Function = function (event:CloseEvent):void {
        switch (event.detail) {
          case Alert.YES:
            _fileReference.browse(createTypeFilters(uploadCommand.fileFilter));
            break;
          default:
            popFakeDialog();
            break;
        }
      };
      pushFakeDialog();
      var alert:Alert = Alert.show(translate("FS.browse.continue"), translate("file.upload"), Alert.YES | Alert.NO,
                                   null, alertCloseHandler, null, Alert.NO);
      fixAlertSize(alert);
    }
  }

  protected function handleFileDownload(downloadCommand:RemoteFileDownloadCommand):void {
    _fileReference = new FileReference();
    _fileReference.addEventListener(Event.CANCEL, function (event:Event):void {
      var actionEvent:RActionEvent = new RActionEvent();
      actionEvent.actionCommand = downloadCommand.resourceId;
      execute(downloadCommand.cancelCallbackAction, actionEvent);
      popFakeDialog();
    });
    _fileReference.addEventListener(IOErrorEvent.IO_ERROR, function (event:IOErrorEvent):void {
      popupError(event.text);
      popFakeDialog();
    });

    try {
      _fileReference.download(new URLRequest(downloadCommand.fileUrl), downloadCommand.defaultFileName);
    } catch (error:Error) {
      // we are certainly running on FP 10 or above. Need an extra
      // dialog to initiate the browsing...
      var alertCloseHandler:Function = function (event:CloseEvent):void {
        switch (event.detail) {
          case Alert.YES:
            _fileReference.download(new URLRequest(downloadCommand.fileUrl), downloadCommand.defaultFileName);
            break;
          default:
            break;
        }
        popFakeDialog();
      };
      pushFakeDialog();
      var alert:Alert = Alert.show(translate("FS.browse.continue"), translate("file.download"), Alert.YES | Alert.NO,
                                   null, alertCloseHandler, null, Alert.NO);
      fixAlertSize(alert);
    }
  }

  protected function handleClipboardCommand(clipboardCommand:RemoteClipboardCommand):void {
    try {
      Clipboard.generalClipboard.clear();
      if (clipboardCommand.plainContent) {
        Clipboard.generalClipboard.setData(ClipboardFormats.TEXT_FORMAT, clipboardCommand.plainContent);
      }
      if (clipboardCommand.htmlContent) {
        Clipboard.generalClipboard.setData(ClipboardFormats.HTML_FORMAT, clipboardCommand.htmlContent);
      }
    } catch (error:Error) {
      // we are certainly running on FP 10 or above. Need an extra
      // dialog to initiate the browsing...
      var alertCloseHandler:Function = function (event:CloseEvent):void {
        switch (event.detail) {
          case Alert.YES:
            try {
              Clipboard.generalClipboard.clear();
              if (clipboardCommand.plainContent) {
                Clipboard.generalClipboard.setData(ClipboardFormats.TEXT_FORMAT, clipboardCommand.plainContent);
              }
              if (clipboardCommand.htmlContent) {
                Clipboard.generalClipboard.setData(ClipboardFormats.HTML_FORMAT, clipboardCommand.htmlContent);
              }
            } catch (ignored:Error) {
              // Not much we can do about it...
            }
            break;
          default:
            break;
        }
        popFakeDialog();
      };
      pushFakeDialog();
      var alert:Alert = Alert.show(translate("system.clipboard.continue"), translate("content.copy"),
                                   Alert.YES | Alert.NO, null, alertCloseHandler, null, Alert.NO);
      fixAlertSize(alert);
    }
  }

  protected function createTypeFilters(filter:Object):Array {
    var typeFilters:Array = null;
    if (filter) {
      typeFilters = [];
      for (var name:String in filter) {
        //noinspection JSUnfilteredForInLoop
        var extensions:Array = filter[name];
        if (extensions) {
          var extensionsString:String = "";
          if (extensions) {
            for (var i:int = 0; i < extensions.length; i++) {
              extensionsString += ("*" + extensions[i] as String);
              if (i < extensions.length - 1) {
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
    if(messageCommand is RemoteErrorMessageCommand) {
      handleErrorMessageCommand(messageCommand as RemoteErrorMessageCommand);
    } else {
      var alert:Alert = createAlert(messageCommand);
      var alertForm:AlertForm = alert.mx_internal::alertForm;
      if (messageCommand.messageIcon) {
        alert.iconClass = getViewFactory().getIconForComponent(alertForm, messageCommand.messageIcon);
        alert.removeChild(alertForm);
        for (var childIndex:int = alertForm.numChildren - 1; childIndex >= 0; childIndex--) {
          var childComp:DisplayObject = alertForm.getChildAt(childIndex);
          if (childComp is Button) {
            alertForm.removeChildAt(childIndex);
          }
        }
        alertForm.mx_internal::buttons = [];
        //Force re-initialization of alert form.
        alertForm.initialized = false;
        alert.addChild(alertForm);
      }

      if (messageCommand.titleIcon) {
        alert.titleIcon = getViewFactory().getIconForComponent(alert, messageCommand.titleIcon);
      }
      fixAlertSize(alert);
      PopUpManager.centerPopUp(alert as IFlexDisplayObject);
    }
  }

  protected function handleErrorMessageCommand(remoteErrorMessageCommand:RemoteErrorMessageCommand):void {
    var tabContainer:TabNavigator = new EnhancedTabNavigator();
    tabContainer.historyManagementEnabled = false;
    tabContainer.resizeToContent = true;

    tabContainer.addChild(createErrorTab(translate("error"), remoteErrorMessageCommand.message));
    tabContainer.addChild(createErrorTab(translate("detail"), remoteErrorMessageCommand.detailMessage));

    var closeButton:Button = getViewFactory().createDialogButton("ok", null, remoteErrorMessageCommand.titleIcon);
    closeButton.addEventListener(MouseEvent.CLICK, function(evt:MouseEvent):void {
      closeDialog();
    });
    popupDialog(remoteErrorMessageCommand.title, translate("error.unexpected"), tabContainer, remoteErrorMessageCommand.titleIcon, [closeButton]);
  }

  private function createErrorTab(tabLabel:String, message:String):UIComponent {
    var errorTabCanvas:Canvas = new Canvas();
    errorTabCanvas.percentWidth = 100.0;
    errorTabCanvas.percentHeight = 100.0;
    errorTabCanvas.label = tabLabel;
    errorTabCanvas.horizontalScrollPolicy = ScrollPolicy.AUTO;
    errorTabCanvas.verticalScrollPolicy = ScrollPolicy.AUTO;
    var errorText:Text = new Text();
    if (HtmlUtil.isHtml(message)) {
      errorText.htmlText = message;
    } else {
      errorText.text = message;
    }
    errorTabCanvas.addChild(errorText);
    return errorTabCanvas;
  }

  protected function fixAlertSize(alert:Alert):void {
    var h:Number = alert.getExplicitOrMeasuredHeight();
    if (alert.getStyle("paddingTop") > 0) {
      h += alert.getStyle("paddingTop");
    }
    if (alert.getStyle("paddingBottom") > 0) {
      h += (alert.getStyle("paddingBottom")) * 2;
    }
    alert.height = h + 25;
  }

  protected function createAlert(messageCommand:RemoteMessageCommand):Alert {
    var alert:Alert;
    var alertCloseHandler:Function;
    var message:String = messageCommand.message;
    var isHtml:Boolean = false;
    if (HtmlUtil.isHtml(message)) {
      isHtml = true;
      // The HTML string must be passed to the show() method, so the width and height of
      // the textField can be calculated correctly. All HTML tags will be removed and the
      // <br> and <br/> tag will be replaced by /n (new line).
      message = message.replace(/<br.*?>/g, "/n");
      message = message.replace(/<.*?>/g, "");
    }
    var alertParent:Sprite;
    //fails to center in parent if the second dialog is bigger than the first one.
    //        if(_dialogStack && _dialogStack.length > 1) {
    //          dialogParent = _dialogStack[_dialogStack.length -1][0];
    //        } else {
    alertParent = getTopLevelApplication();
    //        }
    if (messageCommand is RemoteOkCancelCommand) {
      alertCloseHandler = function (event:CloseEvent):void {
        switch (event.detail) {
          case Alert.OK:
            execute((messageCommand as RemoteOkCancelCommand).okAction);
            break;
          default:
            execute((messageCommand as RemoteOkCancelCommand).cancelAction);
        }
      };
      alert = Alert.show(message, messageCommand.title, Alert.OK | Alert.CANCEL, alertParent, alertCloseHandler, null,
                         Alert.CANCEL);
    } else if (messageCommand is RemoteYesNoCancelCommand) {
      alertCloseHandler = function (event:CloseEvent):void {
        switch (event.detail) {
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
      alert = Alert.show(message, messageCommand.title, Alert.YES | Alert.NO | Alert.CANCEL, alertParent,
                         alertCloseHandler, null, Alert.CANCEL);
    } else if (messageCommand is RemoteYesNoCommand) {
      alertCloseHandler = function (event:CloseEvent):void {
        switch (event.detail) {
          case Alert.YES:
            execute((messageCommand as RemoteYesNoCommand).yesAction);
            break;
          default:
            execute((messageCommand as RemoteYesNoCommand).noAction);
        }
      };
      alert = Alert.show(message, messageCommand.title, Alert.YES | Alert.NO, alertParent, alertCloseHandler, null,
                         Alert.NO);
    } else {
      alert = Alert.show(message, messageCommand.title, Alert.OK, alertParent, null, null, Alert.OK);
    }
    if (isHtml) {
      alert.mx_internal::alertForm.mx_internal::textField.htmlText = HtmlUtil.sanitizeHtml(messageCommand.message);
    }
    return alert;
  }

  protected function restart():void {
    var applicationFrame:Application = getTopLevelApplication();
    applicationFrame.removeAllChildren();
    while (_dialogStack.length > 1) {
      PopUpManager.removePopUp((_dialogStack.pop() as Array)[0] as UIComponent);
    }
    _remotePeerRegistry = new BasicRemotePeerRegistry();
    _lastFiredActions = {};
    _changeNotificationsEnabled = true;
    _commandsQueue = new ArrayCollection([]);
    _dialogStack = [];
    _dialogStack.push([null, null, null]);
    _viewFactory.reset();
    start();
  }

  protected function stop():void {
    blockUI(false);
    var operation:AbstractOperation = _remoteController.getOperation(STOP_METHOD);
    operation.send();
    // breaks SSO
    //_remoteController.channelSet.disconnectAll();
  }

  protected function popupError(message:String, messageHeader:String = "An unexpected error occured",
                                messageHeaderCode:String = "error.unexpected", title:String = "Error",
                                titleCode:String = "error"):void {
    var popupTitle:String;
    var popupMessageHeader:String;
    var popupMessage:String = message;
    if (_translations != null) {
      if (titleCode) {
        popupTitle = translate(titleCode);
      } else {
        popupTitle = title;
      }
      if (messageHeaderCode) {
        popupMessageHeader = translate(messageHeaderCode);
      } else {
        popupMessageHeader = messageHeader;
      }
    } else {
      popupTitle = title;
      popupMessageHeader = messageHeader;
    }
    if (popupMessage && popupMessage.length > 1200) {
      var buff:String = popupMessage.substr(0, 600);
      buff += "\n...\n...\n...\n";
      buff += popupMessage.substr(popupMessage.length - 600, popupMessage.length);
      popupMessage = buff;
    }
    var alert:Alert = Alert.show(popupMessageHeader + "\n\nDetails :\n" + popupMessage, popupTitle, Alert.OK, null,
                                 null, null, Alert.OK);
    fixAlertSize(alert);
  }

  protected function traceError(errorMessage:String):void {
    trace("Error : " + errorMessage);
  }

  public function getRegistered(guid:String):IRemotePeer {
    return _remotePeerRegistry.getRegistered(guid);
  }

  public function unregister(remotePeer:IRemotePeer):void {
    _remotePeerRegistry.unregister(remotePeer);
  }

  public function isRegistered(guid:String):Boolean {
    return _remotePeerRegistry.isRegistered(guid);
  }

  protected function dispatchCommands():void {
    var operation:AbstractOperation = _remoteController.getOperation(HANDLE_COMMANDS_METHOD);
    operation.send(_commandsQueue);
    _commandsQueue.removeAll();
  }

  protected function initRemoteController():void {
    _remoteController.showBusyCursor = true;
    var commandsHandler:Function = function (resultEvent:ResultEvent):void {
      blockUI(true);
      _postponedCommands = {};
      _postponedChildrenNotificationBuffer = [];
      _postponedSelectionCommands = {};
      _postponedEditionCommands = [];
      try {
        handleCommands(resultEvent.result as IList);
      } finally {
        checkPostponedCommandsCompletion();
        _postponedCommands = null;
        _postponedChildrenNotificationBuffer = null;
        _postponedSelectionCommands = null;
        _postponedEditionCommands = null;
        if (_nextActionCallback) {
          try {
            _nextActionCallback();
          } finally {
            _nextActionCallback = null;
          }
        }
      }
    };
    var errorHandler:Function = function (faultEvent:FaultEvent):void {
      blockUI(true);
      popupError(faultEvent.fault.message, "A network error has occurred while communicating with the server",
                 "server.comm.failure");
    };
    var operation:AbstractOperation;
    operation = _remoteController.getOperation(HANDLE_COMMANDS_METHOD);
    operation.addEventListener(ResultEvent.RESULT, commandsHandler);
    operation.addEventListener(FaultEvent.FAULT, errorHandler);
    operation = _remoteController.getOperation(START_METHOD);
    operation.addEventListener(ResultEvent.RESULT, commandsHandler);
    operation.addEventListener(FaultEvent.FAULT, errorHandler);
  }

  protected function checkPostponedCommandsCompletion():void {
    for (var guid:String in _postponedCommands) {
      //noinspection JSUnfilteredForInLoop
      var commands:IList = _postponedCommands[guid] as IList;
      for each(var command:RemoteCommand in commands) {
        traceError("Target remote peer could not be retrieved :");
        traceError("  guid    = " + command.targetPeerGuid);
        traceError("  command = " + command);
        if (command is RemoteValueCommand) {
          traceError("  value   = " + (command as RemoteValueCommand).value);
        } else if (command is RemoteChildrenCommand) {
          for each (var childState:RemoteValueState in (command as RemoteChildrenCommand).children) {
            traceError("  child = " + childState);
            traceError("    guid  = " + childState.guid);
            traceError("    value = " + childState.value);
          }
        }
      }
    }
    var i:int;
    for (i = 0; i < _postponedChildrenNotificationBuffer.length; i++) {
      var peer:IRemotePeer = getRegistered(_postponedChildrenNotificationBuffer[i]);
      if (peer is RemoteCompositeValueState) {
        (peer as RemoteCompositeValueState).notifyChildrenChanged();
        if (_postponedSelectionCommands.hasOwnProperty(peer.guid)) {
          var delayedSelectionCommand:RemoteSelectionCommand = _postponedSelectionCommands[peer.guid]
              as RemoteSelectionCommand;
          if (ArrayUtil.areUnorderedArraysEqual(delayedSelectionCommand.selectedIndices,
                                                (peer as RemoteCompositeValueState).selectedIndices)) {
            (peer as RemoteCompositeValueState).notifySelectionChanged();
          } else {
            (peer as RemoteCompositeValueState).leadingIndex = delayedSelectionCommand.leadingIndex;
            (peer as RemoteCompositeValueState).selectedIndices = delayedSelectionCommand.selectedIndices;
          }
        }
      }
    }
    for (i = 0; i < _postponedEditionCommands.length; i++) {
      var delayedEditionCommand:RemoteEditCommand = _postponedEditionCommands[i] as RemoteEditCommand;
      getViewFactory().edit((getRegistered(delayedEditionCommand.targetPeerGuid) as RComponent).retrievePeer());
    }
  }

  protected function createWorkspaceAccordion(workspaceNames:Array, workspaceActions:RActionList):CollapsibleAccordion {
    var wsAccordion:CollapsibleAccordion = new CollapsibleAccordion();
    wsAccordion.historyManagementEnabled = false;
    wsAccordion.percentHeight = 100.0;
    for (var i:int = 0; i < workspaceActions.actions.length; i++) {
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


  protected function initApplicationFrame(initCommand:RemoteInitCommand):void {

    var workspaceNames:Array = initCommand.workspaceNames;
    var workspaceActions:RActionList = initCommand.workspaceActions;
    var exitAction:RAction = initCommand.exitAction;
    var navigationActions:Array = initCommand.navigationActions;
    var actions:Array = initCommand.actions;
    var secondaryActions:Array = initCommand.secondaryActions;
    var helpActions:Array = initCommand.helpActions;
    var size:Dimension = initCommand.size;


    _workspaceAccordion = createWorkspaceAccordion(workspaceNames, workspaceActions);
    _workspaceAccordion.addEventListener(FlexEvent.CREATION_COMPLETE, function (event:FlexEvent):void {
      for (var i:int = 0; i < workspaceActions.actions.length; i++) {
        var workspaceAction:RAction = workspaceActions.actions[i];
        var section:Canvas = _workspaceAccordion.content[i] as Canvas;
        section.icon = getViewFactory().getIconForComponent(_workspaceAccordion.getHeaderAt(i), workspaceAction.icon);
        var button:Button = _workspaceAccordion.getButtonAt(i);
        button.setStyle("icon", getViewFactory().getIconForComponent(button, workspaceAction.icon));
      }
    });
    var accordionHandler:Function = function (event:IndexChangedEvent):void {
      var workspaceIndex:int = event.newIndex;
      execute(workspaceActions.actions[workspaceIndex]);
    };
    _workspaceAccordion.addEventListener(IndexChangedEvent.CHANGE, accordionHandler);

    _workspaceViewStack = createWorkspaceViewStack();

    _statusBar = new Label();
    _statusBar.visible = false;

    var appContent:UIComponent = assembleApplicationContent(_workspaceAccordion, _workspaceViewStack, exitAction,
                                                            navigationActions, actions, secondaryActions, helpActions);

    var applicationFrame:Application = getTopLevelApplication();
    if (size) {
      if (size.width > 0) {
        appContent.minWidth = size.width;
      }
      if (size.height > 0) {
        appContent.minHeight = size.height;
      }
    }
    applicationFrame.horizontalScrollPolicy = ScrollPolicy.AUTO;
    applicationFrame.verticalScrollPolicy = ScrollPolicy.AUTO;
    applicationFrame.addChild(appContent);
  }

  protected function getStatusBar():Label {
    return _statusBar;
  }

  protected function assembleApplicationContent(navigationAccordion:CollapsibleAccordion, mainViewStack:ViewStack,
                                                exitAction:RAction, navigationActions:Array, actions:Array,
                                                secondaryActions:Array, helpActions:Array):UIComponent {

    var applicationFrame:Application = getTopLevelApplication();
    var split:UIComponent = assembleSplittedSection(navigationAccordion, mainViewStack);
    assembleApplicationControlBar(exitAction, navigationActions, actions, helpActions);
    if (secondaryActions && secondaryActions.length > 0) {
      var secondaryToolBar:UIComponent = getViewFactory().decorateWithSlideBar(getViewFactory().createToolBarFromActionLists(secondaryActions,
                                                                                                                             applicationFrame));
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

  protected function assembleSplittedSection(navigationAccordion:CollapsibleAccordion,
                                             mainViewStack:ViewStack):UIComponent {
    var split:HDividedBox = new HDividedBox();
    split.liveDragging = true;
    split.resizeToContent = true;
    navigationAccordion.horizontalScrollPolicy = ScrollPolicy.OFF;
    navigationAccordion.verticalScrollPolicy = ScrollPolicy.OFF;
    var adjustDividerLocation:Function = function (event:Event):void {
      var divider:BoxDivider = split.getDividerAt(0);
      var dividerX:int = divider.x;
      var accordionWidth:int = navigationAccordion.measuredWidth;
      accordionWidth += 2;
      if (dividerX != accordionWidth) {
        split.moveDivider(0, accordionWidth - dividerX);
      }
    };
    var adjustMinDividerLocation:Function = function (event:Event):void {
      split.callLater(function ():void {
        split.moveDivider(0, 1);
        split.moveDivider(0, -1);
      });
    };
    navigationAccordion.addEventListener("closeDrawerComplete", adjustMinDividerLocation);
    navigationAccordion.addEventListener("openDrawerComplete", adjustDividerLocation);
    split.addChild(navigationAccordion);
    split.addChild(mainViewStack);
    split.percentWidth = 100.0;
    split.percentHeight = 100.0;
    return split;
  }

  protected function assembleApplicationControlBar(exitAction:RAction, navigationActions:Array, actions:Array,
                                                   helpActions:Array):ApplicationControlBar {
    var applicationFrame:Application = getTopLevelApplication();
    var controlBar:ApplicationControlBar = applicationFrame.controlBar as ApplicationControlBar;
    if (controlBar) {
      controlBar.removeAllChildren();
    } else {
      controlBar = new ApplicationControlBar();
      controlBar.dock = true;
      applicationFrame.addChild(controlBar);
    }
    getViewFactory().installActionLists(controlBar, navigationActions, applicationFrame);
    getViewFactory().addSeparator(controlBar);
    var i:int;
    var popupButton:Button;
    if (actions != null) {
      for (i = 0; i < actions.length; i++) {
        popupButton = getViewFactory().createPopupButton(actions[i] as RActionList, applicationFrame, true);
        if (popupButton != null) {
          controlBar.addChild(popupButton);
        }
      }
    }
    getViewFactory().addSeparator(controlBar);
    var glue:HBox = new HBox();
    glue.percentWidth = 100.0;
    controlBar.addChild(glue);
    var sb:Label = getStatusBar();
    controlBar.addChild(sb);
    if (helpActions != null) {
      for (i = 0; i < helpActions.length; i++) {
        popupButton = getViewFactory().createPopupButton(helpActions[i] as RActionList, applicationFrame, true);
        if (popupButton != null) {
          controlBar.addChild(popupButton);
        }
      }
    }
    getViewFactory().addSeparator(controlBar);
    controlBar.addChild(getViewFactory().createAction(exitAction, applicationFrame));
    return controlBar;
  }

  protected function createApplicationMenuBar(actions:Array, helpActions:Array):MenuBar {
    var applicationFrame:Application = getTopLevelApplication();
    var menuBarModel:Object = {};
    var menus:Array = [];
    menus = menus.concat(getViewFactory().createMenus(actions, false, applicationFrame));
    menus = menus.concat(getViewFactory().createMenus(helpActions, true, applicationFrame));
    menuBarModel["children"] = menus;

    var menuBar:MenuBar = new MenuBar();
    menuBar.setStyle("cornerRadius", 5);
    menuBar.percentWidth = 100.0;
    menuBar.showRoot = false;

    menuBar.menuBarItemRenderer = new ClassFactory(RIconMenuBarItem);

    menuBar.dataProvider = menus;

    for (var i:int = 0; i < menus.length; i++) {
      var menu:Menu = menuBar.getMenuAt(i);
      menu.itemRenderer = new ClassFactory(RIconMenuItemRenderer);
    }

    var menuHandler:Function = function (event:MenuEvent):void {
      if (event.item["data"] is RAction) {
        execute(event.item["data"] as RAction, null);
      }
    };
    menuBar.addEventListener(MenuEvent.ITEM_CLICK, menuHandler);

    return menuBar;
  }

  public function start():void {
    blockUI(false);
    var operation:AbstractOperation = _remoteController.getOperation(START_METHOD);
    if (JSPRESSO_VERSION.substr(0, 3) > "3.5" || JSPRESSO_VERSION.substr(0, 6) > "3.5.13") {
      var startCommand:RemoteStartCommand = new RemoteStartCommand();
      startCommand.language = _userLanguage;
      startCommand.keysToTranslate = getKeysToTranslate();
      startCommand.timezoneOffset = new Date().timezoneOffset * (-60000);
      startCommand.version = JSPRESSO_VERSION;
      startCommand.clientType = "DESKTOP_FLEX";
      operation.send(startCommand);
    } else {
      operation.send(_userLanguage, getKeysToTranslate(), new Date().timezoneOffset * (-60000));
    }
  }

  private var _stillDisable:Boolean = false;

  private function setUIEnabled(value:Boolean):void {
    // Protect from lightning actions that would falsely disable the UI.
    // Fixes bug #718
    if (!value && !_stillDisable) {
      return;
    }
    if (_dialogStack && _dialogStack.length > 1) {
      ((_dialogStack[_dialogStack.length - 1] as Array)[0] as UIComponent).enabled = value;
    }
    var application:Application = getTopLevelApplication();
    application.enabled = value;
    if (application.controlBar) {
      application.controlBar.enabled = true;
    }
  }

  protected function blockUI(value:Boolean):void {
    _stillDisable = !value;
    if (value) {
      setUIEnabled(true);
    } else {
      // Delays the UI to the next repaint so that if an action needs to perform immediately
      // after, it can. see Bug #674
      getTopLevelApplication().callLater(setUIEnabled, [value]);
    }
  }

  protected function getKeysToTranslate():Array {
    return ["FS.browse.continue", "file.upload", "file.download", "system.clipboard.continue", "content.copy", "error",
            "error.unexpected", "server.comm.failure", "ok", "cancel", "yes", "no", "detail"];
  }

  public function translate(key:String):String {
    if (_translations != null) {
      var tr:String = _translations[key] as String;
      if (tr != null) {
        return tr;
      }
    }
    return key;
  }

  protected function displayWorkspace(workspaceName:String, workspaceView:RComponent):void {
    if (workspaceView) {
      var workspaceNavigator:RComponent = null;
      if (workspaceView is RSplitContainer) {
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

      if (workspaceNavigator) {
        var workspaceNavigatorUI:UIComponent = getViewFactory().createComponent(workspaceNavigator);
        workspaceNavigatorUI.percentWidth = 100.0;
        workspaceNavigatorUI.percentHeight = 100.0;
        if (workspaceNavigatorUI is Tree) {
          var tree:Tree = workspaceNavigatorUI as Tree;
          tree.showRoot = false;
          // workaround since top collection is not monitored
          // when showRoot=false
          var state:RemoteCompositeValueState = workspaceNavigator.state as RemoteCompositeValueState;
          BindingUtils.bindSetter(function (selectedIndices:Array):void {
            if (selectedIndices != null && selectedIndices.length > 0) {
              // work on items to translate indices independently of table sorting state.
              var selectedItems:Array = new Array(selectedIndices.length);
              for (var i:int = 0; i < selectedIndices.length; i++) {
                selectedItems[i] = state.children.getItemAt(selectedIndices[i]);
              }
              if (!ArrayUtil.areUnorderedArraysEqual(tree.selectedItems, selectedItems)) {
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

  protected function popupDialog(title:String, message:String, dialogView:UIComponent, icon:RIcon, actions:Array,
                                 useCurrent:Boolean = false, dimension:Dimension = null,
                                 secondaryActionLists:Array = null):void {
    dialogView.percentWidth = 100.0;
    dialogView.percentHeight = 100.0;
    var buttonBox:HBox = new HBox();
    buttonBox.setStyle("horizontalAlign", "right");
    buttonBox.setStyle("paddingLeft", 5);
    buttonBox.setStyle("paddingRight", 5);
    buttonBox.setStyle("paddingTop", 0);
    buttonBox.setStyle("paddingBottom", 5);
    buttonBox.percentWidth = 100.0;

    var dialogBox:VBox = new VBox();
    if (message) {
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
    var allActions:Array = actions.concat(extractAllActions(secondaryActionLists));
    for each(var buttonOrAction:Object in allActions) {
      var button:Button;
      if (buttonOrAction is RAction) {
        button = getViewFactory().createDialogAction(buttonOrAction as RAction);
      } else {
        button = buttonOrAction as Button;
      }
      if (!dialogBox.defaultButton) {
        dialogBox.defaultButton = button;
      }
      buttonBox.addChild(button);
    }
    dialogBox.addChild(buttonBox);

    var dialog:Panel;
    if (useCurrent && _dialogStack && _dialogStack.length > 1) {
      dialog = (_dialogStack[_dialogStack.length - 1] as Array)[0] as Panel;
      dialog.removeAllChildren();
    } else {
      var dialogParent:DisplayObject;
      //fails to center in parent if the second dialog is bigger than the first one.
      //        if(_dialogStack && _dialogStack.length > 1) {
      //          dialogParent = _dialogStack[_dialogStack.length -1][0];
      //        } else {
      dialogParent = getTopLevelApplication();
      //        }
      dialog = getViewFactory().createResizableDialog(dialogParent);
      dialog.setStyle("borderAlpha", 1);
      dialog.setStyle("borderThicknessLeft", 5);
      dialog.setStyle("borderThicknessRight", 5);
      dialog.setStyle("borderThicknessBottom", 5);
      dialog.horizontalScrollPolicy = ScrollPolicy.OFF;
      dialog.verticalScrollPolicy = ScrollPolicy.OFF;
      _dialogStack.push([dialog, null]);
    }
    dialog.title = title;
    if (icon) {
      dialog.titleIcon = getViewFactory().getIconForComponent(dialog, icon);
    }
    dialogBox.percentWidth = 100.0;
    dialogBox.percentHeight = 100.0;
    dialog.addChild(dialogBox);
    dialogBox.addEventListener(FlexEvent.CREATION_COMPLETE, function (evt:FlexEvent):void {
      if (dimension) {
        if (dimension.width > 0) {
          dialog.width = dimension.width;
        }
        if (dimension.height > 0) {
          dialog.height = dimension.height;
        }
      } else {
        var applicationFrame:Application = getTopLevelApplication();
        dialog.width = Math.min(Math.max(dialog.width, dialogView.getExplicitOrMeasuredWidth() + 15),
                                applicationFrame.width * 95 / 100);
        dialog.height = Math.min(Math.max(dialog.height, dialogView.getExplicitOrMeasuredHeight()
            + buttonBox.getExplicitOrMeasuredHeight() + 80), applicationFrame.height * 95 / 100);
        dialogView.width = NaN;
        dialogView.percentWidth = 100.0;
        dialogView.height = NaN;
        dialogView.percentHeight = 100.0;
      }
    });
    var focusInit:Function = function ():void {
      getViewFactory().focus(dialogView);
    };
    dialog.addEventListener(FlexEvent.CREATION_COMPLETE, function (evt:FlexEvent):void {
      dialog.callLater(focusInit);
    });
    PopUpManager.centerPopUp(dialog);
  }

  private function extractAllActions(secondaryActionLists:Array):Array {
    var allActions:Array = [];
    if (secondaryActionLists) {
      if (secondaryActionLists != null) {
        for (var i:int = 0; i < secondaryActionLists.length; i++) {
          var actionList:RActionList = secondaryActionLists[i] as RActionList;
          if (actionList.actions != null) {
            for (var j:int = 0; j < actionList.actions.length; j++) {
              allActions.push(actionList.actions[j]);
            }
          }
        }
      }
    }
    return allActions;
  }

  public function setCurrentViewStateGuid(component:UIComponent, viewStateGuid:String, viewStatePermId:String):void {
    if (_dialogStack.length > 1) {
      // at least a dialog is open
      for (var i:int = _dialogStack.length - 1; i > 0; i--) {
        // Find the owning dialog that may not be the topmost one.
        var dialog:Array = _dialogStack[_dialogStack.length - i] as Array;
        if ((dialog[0] as Panel).contains(component)) {
          dialog[1] = viewStateGuid;
          dialog[2] = viewStatePermId;
          return;
        }
      }
    }
    (_dialogStack[0] as Array)[1] = viewStateGuid;
    (_dialogStack[0] as Array)[2] = viewStatePermId;
  }

  protected function getViewFactory():DefaultFlexViewFactory {
    return _viewFactory;
  }

  protected function getTopLevelApplication():Application {
    return FlexGlobals.topLevelApplication as Application;
  }

  public function initGeoLocation():void {
    if (ExternalInterface.available) {
      //Security.allowDomain("*");
      var appId:String = getTopLevelApplication().id;

      const CHECK_GEOLOCATION:String = "document.insertScript = function() { checkGeoLocation = function() {"
          + "return (navigator && navigator.geolocation); } }";
      const GET_GEOLOCATION:String = "document.insertScript = function() { getGeoLocation = function() {"
          + "var app = document.getElementById('" + appId + "');"
          + "navigator.geolocation.getCurrentPosition(" +
          "function(position) {app.geoLocationSuccessHandler(position);}," +
          "function(error) {app.geoLocationErrorHandler(error);}" +
          "); } }";

      ExternalInterface.call(CHECK_GEOLOCATION);
      var geoEnabled:Boolean = ExternalInterface.call("checkGeoLocation");
      if (geoEnabled == true) {
        ExternalInterface.addCallback("geoLocationSuccessHandler", geoLocationSuccessHandler);
        ExternalInterface.addCallback("geoLocationErrorHandler", geoLocationErrorHandler);
        ExternalInterface.call(GET_GEOLOCATION);
      }
    }
  }

  protected function registerRemoteClasses():void {
    // Override to register custom DTOs
  }


  [Bindable]
  public function get userGeoLocation():Object {
    return _userGeoLocation;
  }

  public function set userGeoLocation(value:Object):void {
    _userGeoLocation = value;
  }

  public function geoLocationSuccessHandler(position:Object):void {
    userGeoLocation = position;
  }

  public function geoLocationErrorHandler(error:Object):void {
    //_geoLocation = null;
  }

  public function queryUserGeoLocation():void {
    ExternalInterface.call("getGeoLocation");
  }

  public function set name(name:String):void {
    this._applicationName = name;
  }

  [Bindable]
  public function get name():String {
    return _applicationName;
  }
}
}
