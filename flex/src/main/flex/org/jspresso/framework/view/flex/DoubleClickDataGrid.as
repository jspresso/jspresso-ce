/**
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 */

package org.jspresso.framework.view.flex {
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