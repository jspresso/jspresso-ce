package org.jspresso.framework.view.flex
{
  import flash.events.FocusEvent;
  
  import mx.containers.Canvas;
  import mx.controls.CheckBox;
  import mx.core.ScrollPolicy;
  import mx.core.UIComponent;
  
  import org.jspresso.framework.state.remote.RemoteCompositeValueState;
  import org.jspresso.framework.state.remote.RemoteValueState;
  
  public class RemoteValueDgItemEditor extends Canvas {
    
    private var _editor:UIComponent;
    private var _state:RemoteValueState;
    private var _index:int;

    public function RemoteValueDgItemEditor() {
      _index = -1;
      horizontalScrollPolicy = ScrollPolicy.OFF;
      verticalScrollPolicy = ScrollPolicy.OFF;
    }
    
    public function set editor(value:UIComponent):void {
      _editor = value;
      setupChildren();
    }
    public function get editor():UIComponent {
      return _editor;
    }

    public function set state(value:RemoteValueState):void {
      _state = value;
    }
    public function get state():RemoteValueState {
      return _state;
    }

    public function set index(value:int):void {
      _index = value;
    }
    public function get index():int {
      return _index;
    }

    private function setupChildren():void {
      removeAllChildren();
      if(_editor is CheckBox) {
        _editor.setStyle("horizontalCenter", 0);
        _editor.setStyle("verticalCenter", 0);
      } else {
        _editor.percentWidth = 100.0;
        _editor.percentHeight = 100.0;
      }
      addChild(_editor);
    }

    override public function set data(value:Object):void {
      super.data = value;
      if(index != -1 && value is RemoteCompositeValueState) {
        _state.value = ((value as RemoteCompositeValueState).children[index] as RemoteValueState).value;
      } else if(value is RemoteValueState) {
        _state.value = (value as RemoteValueState).value;
      }
    }
    
    override public function setFocus():void {
      _editor.setFocus();
    }
    
    override public function addEventListener(type:String, listener:Function, useCapture:Boolean=false, priority:int=0, useWeakReference:Boolean=false):void {
      // prevent bogus DataGrid to install its bogus focus out listener...
      if(type != FocusEvent.FOCUS_OUT) {
        super.addEventListener(type, listener, useCapture, priority, useWeakReference);
      }
    }
  }
}