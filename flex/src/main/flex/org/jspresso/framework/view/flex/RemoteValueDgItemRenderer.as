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

  import mx.binding.utils.BindingUtils;
  import mx.binding.utils.ChangeWatcher;
  import mx.controls.DataGrid;
  import mx.controls.listClasses.BaseListData;
  import mx.controls.listClasses.ListData;
  import mx.controls.listClasses.ListItemRenderer;
  import mx.formatters.Formatter;
  
  import org.jspresso.framework.state.remote.RemoteCompositeValueState;
  import org.jspresso.framework.state.remote.RemoteValueState;

  public class RemoteValueDgItemRenderer extends ListItemRenderer {
    
    private var valueChangeListener:ChangeWatcher;
    private var _listData:BaseListData;
    private var _formatter:Formatter;
    
  	override public function set listData(value:BaseListData):void {
   	  updateLabel(data, value);
   	  if(value) {
     	  if(value is ListData) {
     	    super.listData = value;
     	  } else {
    	    super.listData = new ListData(value.label,
    	     null,
           null,
           value.uid,
           value.owner,
           value.rowIndex,
           value.columnIndex);
    	  }
   	  } else {
   	    super.listData = value;
  	  }
  	  _listData = value;
  	}
  	
  	protected function set listDataIcon(value:Class):void {
  	  if(super.listData != null) {
  	    (super.listData as ListData).icon = value;
  	  }
  	}

  	override public function get listData():BaseListData {
  	  return _listData;
  	}

  	public function set formatter(value:Formatter):void {
  	  _formatter = value;
  	}

  	override public function set data(value:Object):void	{
  	  updateLabel(value, listData);
  	  if(listData && super.listData) {
  	    super.listData.label = listData.label;
  	  }
  	  super.data = value;
  	}

  	protected function updateLabel(rendererData:Object, rendererListData:BaseListData):void {
  	  if(rendererData && rendererListData) {
  	    var cellValueState:RemoteValueState;
  	    if(rendererListData.owner is DataGrid) {
  	      cellValueState = ((rendererData as RemoteCompositeValueState).children[rendererListData.columnIndex +1] as RemoteValueState); 
  	    } else {
  	      cellValueState = rendererData as RemoteValueState;
  	    }
  	    if(valueChangeListener != null) {
  	      valueChangeListener.unwatch();
  	    }
  	    valueChangeListener = BindingUtils.bindSetter(refresh, cellValueState, "value", true);
    	  rendererListData.label = computeLabel(cellValueState.value);
  	  }
  	}
  	
  	protected function computeLabel(cellValue:Object):String {
 	    var cellLabel:String;
	    if(cellValue != null) {
	      if(_formatter != null) {
	        cellLabel = _formatter.format(cellValue);
	      } else {
  	      cellLabel = cellValue.toString();
  	    }
  	  } else {
  	    cellLabel = null;
  	  }
      return cellLabel
  	}
  	
  	protected function refresh(cellValue:Object):void {
      listData.label = computeLabel(cellValue);
  	  if(listData && super.listData) {
  	    super.listData.label = listData.label;
  	  }
      invalidateProperties();
  	}
  }
}