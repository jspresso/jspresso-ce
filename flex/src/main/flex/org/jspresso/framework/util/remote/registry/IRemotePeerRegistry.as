package org.jspresso.framework.util.remote.registry {
  import org.jspresso.framework.util.remote.IRemotePeer;
  
  
  public interface IRemotePeerRegistry {

    function register(remotePeer:IRemotePeer):void;

    function getRegistered(guid:String):IRemotePeer;

    function unregister(guid:String):void;
    
    function isRegistered(guid:String):Boolean;
  }
}