package org.jspresso.framework.util.remote.registry {
  import org.jspresso.framework.util.remote.IRemotePeer;
  
  
  public class BasicRemotePeerRegistry implements IRemotePeerRegistry {
    
    private var _backingStore:Object;
    
    public function BasicRemotePeerRegistry() {
      _backingStore = new Object();
    }

    public function register(remotePeer:IRemotePeer):void {
      _backingStore[remotePeer.guid] = remotePeer;
    }

    public function getRegistered(guid:String):IRemotePeer {
      return _backingStore[guid];
    }

    public function unregister(guid:String):void {
      delete _backingStore[guid];
    }

    public function isRegistered(guid:String):Boolean {
      return _backingStore.hasOwnProperty(guid);
    }
  }
}