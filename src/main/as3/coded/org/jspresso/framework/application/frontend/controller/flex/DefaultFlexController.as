package org.jspresso.framework.application.frontend.controller.flex {
  import mx.binding.utils.BindingUtils;
  import mx.collections.ArrayCollection;
  import mx.collections.IList;
  import mx.collections.ListCollectionView;
  import mx.core.UIComponent;
  import mx.rpc.events.FaultEvent;
  import mx.rpc.events.ResultEvent;
  import mx.rpc.remoting.mxml.Operation;
  import mx.rpc.remoting.mxml.RemoteObject;
  
  import org.jspresso.framework.action.IActionHandler;
  import org.jspresso.framework.application.frontend.command.remote.RemoteAccessibilityCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteActionCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteChildrenCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteEnablementCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteValueCommand;
  import org.jspresso.framework.gui.remote.RAction;
  import org.jspresso.framework.gui.remote.RComponent;
  import org.jspresso.framework.state.remote.RemoteCompositeValueState;
  import org.jspresso.framework.state.remote.RemoteValueState;
  import org.jspresso.framework.util.remote.IRemotePeer;
  import org.jspresso.framework.util.remote.registry.BasicRemotePeerRegistry;
  import org.jspresso.framework.util.remote.registry.IRemotePeerRegistry;
  import org.jspresso.framework.view.flex.DefaultFlexViewFactory;
  
  
  public class DefaultFlexController implements IRemotePeerRegistry, IActionHandler {
    
    private static const HANDLE_COMMANDS_METHOD:String = "handleCommands";
    private var _remoteController:RemoteObject;
    private var _viewFactory:DefaultFlexViewFactory;
    private var _remotePeerRegistry:IRemotePeerRegistry;
    private var _changeNotificationsEnabled:Boolean;
    private var _commandsQueue:IList;
    private var _commandRegistrationEnabled;
    
    public function DefaultFlexController(remoteController:RemoteObject) {
      _remotePeerRegistry = new BasicRemotePeerRegistry();
      _viewFactory = new DefaultFlexViewFactory(this, this);
      _changeNotificationsEnabled = true;
      _remoteController = remoteController;
      _commandsQueue = new ArrayCollection(new Array());
      _commandRegistrationEnabled = true;
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

// Forget leading index update since it will be propagated automatically.
//          var leadingIndexListener:Function = function(leadingIndex:int):void {
//            leadingIndexUpdated(remoteValueState as RemoteCompositeValueState);
//          }
//          BindingUtils.bindSetter(leadingIndexListener, remoteValueState, "leadingIndex", true);

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

    protected function handleCommands(commands:ListCollectionView):void {
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
      var targetPeer:IRemotePeer = getRegistered(command.targetPeerGuid);
      if(targetPeer == null) {
        handleError("Target remote peer could not be retrieved");
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
      } else if(command is RemoteAccessibilityCommand) {
        (targetPeer as RemoteValueState).readable =
          (command as RemoteAccessibilityCommand).readable;
        (targetPeer as RemoteValueState).writable =
          (command as RemoteAccessibilityCommand).writable;
      } else if(command is RemoteSelectionCommand) {
        (targetPeer as RemoteCompositeValueState).selectedIndices =
          (command as RemoteSelectionCommand).selectedIndices;
        (targetPeer as RemoteCompositeValueState).leadingIndex =
          (command as RemoteSelectionCommand).leadingIndex;
      } else if(command is RemoteEnablementCommand) {
        (targetPeer as RAction).enabled =
          (command as RemoteEnablementCommand).enabled;
      } else if(command is RemoteChildrenCommand) {
        var children:ListCollectionView = (targetPeer as RemoteCompositeValueState).children; 
        //children.disableAutoUpdate();
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
        children.enableAutoUpdate();
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
      
      var operation:Operation;
      operation = _remoteController.operations[HANDLE_COMMANDS_METHOD];
      operation.addEventListener(ResultEvent.RESULT, function(resultEvent:ResultEvent):void {
        handleCommands(resultEvent.result as ListCollectionView);
      });
      operation.addEventListener(FaultEvent.FAULT, function(faultEvent:FaultEvent):void {
        handleError(faultEvent.fault.message);
      });
    }
  }
}