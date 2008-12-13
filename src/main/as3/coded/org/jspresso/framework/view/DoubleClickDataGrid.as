package org.jspresso.framework.view {
  import flash.events.MouseEvent;
  
  import mx.controls.DataGrid;
  import mx.controls.listClasses.IDropInListItemRenderer;
  import mx.controls.listClasses.IListItemRenderer;
  import mx.events.DataGridEvent;
  import mx.events.ListEvent;
  
  
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
  		addEventListener(ListEvent.ITEM_DOUBLE_CLICK, itemDoubleClicked);
  		addEventListener(ListEvent.ITEM_CLICK, itemClicked);
  		addEventListener(DataGridEvent.ITEM_EDIT_BEGINNING, itemEditBeginning);
  	}
  
    private function itemDoubleClicked(event:ListEvent):void {
      preventEditing = false;
    }
  
  	private function itemClicked(event:ListEvent):void	{
			if(   event.rowIndex != lastClickedRow
			   || event.columnIndex != lastClickedColumn) {
			  preventEditing = true; 
			} else {
			  preventEditing = false;
			}
			lastClickedRow = event.rowIndex;
			lastClickedColumn = event.columnIndex;
  	}

  	public function itemEditBeginning(event:DataGridEvent):void {
  	  if(preventEditing) {
  	    event.preventDefault();
  	  }
  	}
  }
}