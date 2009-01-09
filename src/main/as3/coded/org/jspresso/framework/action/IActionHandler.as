package org.jspresso.framework.action {
  import org.jspresso.framework.gui.remote.RAction;
  
  
  public interface IActionHandler {
    
    function execute(action:RAction, param:String=null):void;
    
  }
}