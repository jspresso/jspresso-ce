package org.jspresso.framework.view
{
  import mx.binding.utils.BindingUtils;
  import mx.binding.utils.ChangeWatcher;
  
  import org.jspresso.framework.gui.remote.RComponent;
  import org.jspresso.framework.state.remote.RemoteCompositeValueState;
  import org.jspresso.framework.state.remote.RemoteValueState;

  public class UIComponentDgItemRenderer extends RemoteValueDgItemEditor {
    
    private var _viewFactory:DefaultFlexViewFactory;
    private var _remoteComponent:RComponent;

    private var valueChangeListener:ChangeWatcher;
    
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
    
    override public function set data(value:Object):void {
      super.data = value;
      var cellValueState:RemoteValueState;
      if(index != -1 && value is RemoteCompositeValueState) {
        cellValueState = (value as RemoteCompositeValueState).children[index] as RemoteValueState;
      } else if(value is RemoteValueState) {
        cellValueState = value as RemoteValueState;
      }
	    if(valueChangeListener != null) {
	      valueChangeListener.unwatch();
	    }
	    valueChangeListener = BindingUtils.bindSetter(refresh, cellValueState, "value");
    }
    
  	protected function refresh(value:Object):void {
      state.value = value;
  	}
  }
}