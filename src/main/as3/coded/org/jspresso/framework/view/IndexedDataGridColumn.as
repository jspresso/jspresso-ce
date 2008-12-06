package org.jspresso.framework.view
{
  import mx.controls.dataGridClasses.DataGridColumn;

  public class IndexedDataGridColumn extends DataGridColumn {
    
    private var _modelIndex:int;
    
    public function IndexedDataGridColumn(columnName:String=null) {
      super(columnName);
    }
    
    public function set modelIndex(value:int):void {
        _modelIndex = value;
    }

    public function get modelIndex():int {
        return _modelIndex;
    }
  }
}