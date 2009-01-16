package org.jspresso.framework.application.frontend.controller.flex {
  import mx.binding.utils.BindingUtils;
  import mx.collections.ListCollectionView;
  import mx.core.UIComponent;
  import mx.rpc.events.FaultEvent;
  import mx.rpc.events.ResultEvent;
  import mx.rpc.remoting.mxml.Operation;
  import mx.rpc.remoting.mxml.RemoteObject;
  
  import org.jspresso.framework.action.IActionHandler;
  import org.jspresso.framework.application.frontend.command.remote.RemoteCommand;
  import org.jspresso.framework.gui.remote.RAction;
  import org.jspresso.framework.gui.remote.RComponent;
  import org.jspresso.framework.state.remote.RemoteCompositeValueState;
  import org.jspresso.framework.state.remote.RemoteValueState;
  import org.jspresso.framework.util.remote.IRemotePeer;
  import org.jspresso.framework.util.remote.registry.BasicRemotePeerRegistry;
  import org.jspresso.framework.util.remote.registry.IRemotePeerRegistry;
  import org.jspresso.framework.view.flex.DefaultFlexViewFactory;
  
  
  public class DefaultFlexController implements IRemotePeerRegistry, IActionHandler {
    
    private var _remoteController:RemoteObject;
    private var _viewFactory:DefaultFlexViewFactory;
    private var _remotePeerRegistry:IRemotePeerRegistry;
    private var _changeNotificationsEnabled:Boolean;
    
    public function DefaultFlexController(remoteController:RemoteObject) {
      _remotePeerRegistry = new BasicRemotePeerRegistry();
      _viewFactory = new DefaultFlexViewFactory(this, this);
      _changeNotificationsEnabled = true;
      _remoteController = remoteController;
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

          var leadingIndexListener:Function = function(leadingIndex:int):void {
            leadingIndexUpdated(remoteValueState as RemoteCompositeValueState);
          }
          BindingUtils.bindSetter(leadingIndexListener, remoteValueState, "leadingIndex", true);
        }

      } finally {
        _changeNotificationsEnabled = wasEnabled;
      }
    }
    
    public function valueUpdated(remoteValueState:RemoteValueState):void {
      if(_changeNotificationsEnabled) {
        trace(">>> Value update <<< " + remoteValueState.value);
      }
    }
    
    public function selectedIndicesUpdated(remoteCompositeValueState:RemoteCompositeValueState):void {
      if(_changeNotificationsEnabled) {
        trace(">>> Selected indices update <<< " + remoteCompositeValueState.selectedIndices + " on " + remoteCompositeValueState.value);
      }
    }

    public function leadingIndexUpdated(remoteCompositeValueState:RemoteCompositeValueState):void {
      if(_changeNotificationsEnabled) {
        trace(">>> Leading index update <<< " + remoteCompositeValueState.leadingIndex + " on " + remoteCompositeValueState.value);
      }
    }

    public function execute(action:RAction, param:String=null):void {
      trace(">>> Execute <<< " + action.name + " param = " + param);
    }
    
    protected function registerCommand(command:RemoteCommand):void {
      trace("Command registered for next round trip : " + command);
    }

    protected function handleCommands(commands:ListCollectionView):void {
      trace("Recieved commands :");
      if (commands != null) {
        for each(var command:RemoteCommand in commands) {
          trace("  -> " + command);
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
    
    private function initRemoteController():void {
      var operation:Operation;
      
      operation = new Operation(_remoteController, "handleCommands");
      operation.addEventListener(ResultEvent.RESULT, function(resultEvent:ResultEvent):void {
        handleCommands(resultEvent.result as ListCollectionView);
      });
      operation.addEventListener(FaultEvent.FAULT, function(faultEvent:FaultEvent):void {
        handleError(faultEvent.fault.message);
      });
      _remoteController.operations[operation.name] = operation;
    }
  }
}