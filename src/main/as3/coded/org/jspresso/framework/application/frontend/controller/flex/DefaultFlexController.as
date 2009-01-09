package org.jspresso.framework.application.frontend.controller.flex {
  import mx.binding.utils.BindingUtils;
  import mx.core.UIComponent;
  
  import org.jspresso.framework.action.IActionHandler;
  import org.jspresso.framework.gui.remote.RAction;
  import org.jspresso.framework.gui.remote.RComponent;
  import org.jspresso.framework.state.remote.RemoteCompositeValueState;
  import org.jspresso.framework.state.remote.RemoteValueState;
  import org.jspresso.framework.util.remote.IRemotePeer;
  import org.jspresso.framework.util.remote.registry.BasicRemotePeerRegistry;
  import org.jspresso.framework.util.remote.registry.IRemotePeerRegistry;
  import org.jspresso.framework.view.flex.DefaultFlexViewFactory;
  
  
  public class DefaultFlexController implements IRemotePeerRegistry, IActionHandler {
    
    private var _viewFactory:DefaultFlexViewFactory;
    private var _remotePeerRegistry:IRemotePeerRegistry;
    
    public function DefaultFlexController() {
      _remotePeerRegistry = new BasicRemotePeerRegistry();
      _viewFactory = new DefaultFlexViewFactory(this, this);
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
      var valueListener:Function = function(value:Object):void {
        valueUpdated(remoteValueState);
      }
      BindingUtils.bindSetter(valueListener, remoteValueState, "value");
    }
    
    public function valueUpdated(remoteValueState:RemoteValueState):void {
      trace(">>> Value update <<< " + remoteValueState.value + " on " + remoteValueState);
    }
    
    public function execute(action:RAction):void {
      trace(">>> Execute <<< " + action);
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
  }
}