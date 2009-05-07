package org.jspresso.framework.view.flex {
  
  import mx.controls.dataGridClasses.DataGridItemRenderer;

  public class DgHeaderItemRenderer extends DataGridItemRenderer implements IColumnIndexProvider {
    
    private var _index:int;

    public function DgHeaderItemRenderer() {
      _index = -1;
    }
    
    public function set index(value:int):void {
      _index = value;
    }
    public function get index():int {
      return _index;
    }
  }
}