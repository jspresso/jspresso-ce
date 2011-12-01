package org.jspresso.framework.view.flex {
  import mx.controls.Label;
  import mx.core.UIComponent;
  
  import org.jspresso.framework.gui.remote.RComponent;
  
  

  public class DgHeaderItemRenderer extends Label implements IColumnIndexProvider {
    
    private var _index:int;
    private var _viewFactory:DefaultFlexViewFactory;
    private var _rTemplate:RComponent;

    public function DgHeaderItemRenderer() {
      _index = -1;
    }
    
    public function set index(value:int):void {
      _index = value;
    }
    public function get index():int {
      return _index;
    }
    
    public function set viewFactory(value:DefaultFlexViewFactory):void {
      _viewFactory = value;
      if(_rTemplate != null) {
        _viewFactory.applyComponentStyle(this, _rTemplate);
      }
    }
    public function set rTemplate(value:RComponent):void {
      _rTemplate = value;
      if(_viewFactory != null) {
        _viewFactory.applyComponentStyle(this, _rTemplate);
      }
    }
  }
}