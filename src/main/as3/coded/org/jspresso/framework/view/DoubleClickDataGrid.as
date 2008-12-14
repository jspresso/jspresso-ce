package org.jspresso.framework.view {
  import flash.events.MouseEvent;
  
  import mx.controls.DataGrid;
  import mx.controls.listClasses.IDropInListItemRenderer;
  import mx.controls.listClasses.IListItemRenderer;
  import mx.events.DataGridEvent;
  
  
  /** 
   *  DataGrid that only allows editing if you double click
   */
  public class DoubleClickDataGrid extends DataGrid {
    
    private var preventEditing:Boolean;
    
    private var lastClickedRow:int;
    private var lastClickedColumn:int;
  
  	public function DoubleClickDataGrid()	{
  		super();
  		doubleClickEnabled = true;
  		preventEditing = false;
  		lastClickedRow = -1;
  		lastClickedColumn = -1;
  		addEventListener(DataGridEvent.ITEM_EDIT_BEGINNING, itemEditBeginning);
  	}
  
    override protected function mouseDoubleClickHandler(event:MouseEvent):void {
      preventEditing = false;
      super.mouseDoubleClickHandler(event);
    }
  
  	override protected function mouseUpHandler(event:MouseEvent):void	{
      var r:IListItemRenderer;
      
      r = mouseEventToItemRenderer(event);
      
  		if (r && r is IDropInListItemRenderer) {
  			var dilr:IDropInListItemRenderer = r as IDropInListItemRenderer;
  			if(   dilr.listData.rowIndex != lastClickedRow
  			   || dilr.listData.columnIndex != lastClickedColumn) {
  			  preventEditing = true; 
  			} else {
  			  preventEditing = false;
  			}
  			lastClickedRow = dilr.listData.rowIndex;
  			lastClickedColumn = dilr.listData.columnIndex;
  		} else {
  		  preventEditing = false;
  		}
  		super.mouseUpHandler(event);
  	}
  	
  	public function itemEditBeginning(event:DataGridEvent):void {
  	  if(preventEditing) {
  	    event.preventDefault();
  	  }
  	}
  }
}