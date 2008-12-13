package org.jspresso.framework.view {
  import flash.events.MouseEvent;
  import mx.controls.DataGrid;
  import mx.controls.dataGridClasses.DataGridColumn;
  import mx.controls.listClasses.IDropInListItemRenderer;
  import mx.controls.listClasses.IListItemRenderer;
  import mx.core.EventPriority;
  import mx.events.DataGridEvent;
  
  
  /** 
   *  DataGrid that only allows editing if you double click
   */
  public class DoubleClickDataGrid extends DataGrid {
  
  	public function DoubleClickDataGrid()	{
  		super();
  		doubleClickEnabled = true;
  	}
  
    override protected function mouseDoubleClickHandler(event:MouseEvent):void {
      super.mouseDoubleClickHandler(event);

      // simulate a click (just calling the mouseUpHandler wont work)
      super.mouseDownHandler(event);
      super.mouseUpHandler(event);
    }
    
    
    override protected function mouseUpHandler(event:MouseEvent):void {
      // prevent edits on normal mouse-up
      var saved:Boolean = editable;
      editable = false;
      super.mouseUpHandler(event);
      editable = saved;
    }
    
//  	override protected function mouseDoubleClickHandler(event:MouseEvent):void {
//      var dataGridEvent:DataGridEvent;
//      var r:IListItemRenderer;
//      var dgColumn:DataGridColumn;
//
//      r = mouseEventToItemRenderer(event);
//          
//  		if (r && r != itemEditorInstance) {
//  			var dilr:IDropInListItemRenderer = IDropInListItemRenderer(r);
//  			if (columns[dilr.listData.columnIndex].editable) {
//  				dgColumn = columns[dilr.listData.columnIndex];
//                  dataGridEvent = new DataGridEvent(DataGridEvent.ITEM_EDIT_BEGINNING, false, true);
//                  // ITEM_EDIT events are cancelable
//                  dataGridEvent.columnIndex = dilr.listData.columnIndex;
//                  dataGridEvent.dataField = dgColumn.dataField;
//                  dataGridEvent.rowIndex = dilr.listData.rowIndex + verticalScrollPosition;
//                  dataGridEvent.itemRenderer = r;
//                  dispatchEvent(dataGridEvent);
//        }
//      }
//  		super.mouseDoubleClickHandler(event);
//  	}
//  
//  	override protected function mouseUpHandler(event:MouseEvent):void	{
//      var r:IListItemRenderer;
//  		var dgColumn:DataGridColumn;
//  
//      r = mouseEventToItemRenderer(event);
//      
//  		if (r) {
//  			var dilr:IDropInListItemRenderer = IDropInListItemRenderer(r);
//  			if (columns[dilr.listData.columnIndex].editable) {
//  				dgColumn = columns[dilr.listData.columnIndex];
//  				dgColumn.editable = false;
//  			}
//  		}
//  		super.mouseUpHandler(event);
//  		if (dgColumn) {
//  		  dgColumn.editable = true;
//  		}
//  	}
  }
}