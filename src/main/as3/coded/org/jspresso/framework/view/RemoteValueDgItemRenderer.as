package org.jspresso.framework.view {

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
  	  super.listData.label = listData.label;
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
  	    valueChangeListener = BindingUtils.bindSetter(refresh, cellValueState, "value");
    	  rendererListData.label = computeLabel(cellValueState);
  	  }
  	}
  	
  	protected function computeLabel(cellValueState:RemoteValueState):String {
 	    var cellLabel:String;
	    if(cellValueState.value != null) {
	      if(_formatter != null) {
	        cellLabel = _formatter.format(cellValueState.value);
	      } else {
  	      cellLabel = cellValueState.value.toString();
  	    }
  	  } else {
  	    cellLabel = null;
  	  }
      return cellLabel
  	}
  	
  	protected function refresh(cellLabel:Object):void {
      listData.label = cellLabel as String;
      validateProperties();
  	}
  }
}