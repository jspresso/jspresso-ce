package org.jspresso.framework.view
{
  import org.jspresso.framework.gui.remote.RComponent;
  import org.jspresso.framework.state.remote.RemoteValueState;

  public class UIComponentDgItemRenderer extends RemoteValueDgItemEditor {
    
    private var _viewFactory:DefaultFlexViewFactory;
    private var _remoteComponent:RComponent;
    
    public function set viewFactory(value:DefaultFlexViewFactory):void {
      _viewFactory = value;
      updateComponents();
    }
    public function get viewFactory():DefaultFlexViewFactory {
      return _viewFactory;
    }
    
    public function set remoteComponent(value:RComponent):void {
      _remoteComponent = value;
      updateComponents();
    }
    public function get remoteComponent():RComponent {
      return _remoteComponent;
    }
    
    private function updateComponents():void {
      if(viewFactory != null && remoteComponent != null) {
        if(state == null || editor == null) {
          state = new RemoteValueState();
          remoteComponent.state = state;
          editor = viewFactory.createComponent(remoteComponent);
        }
      }
    }
  }
}