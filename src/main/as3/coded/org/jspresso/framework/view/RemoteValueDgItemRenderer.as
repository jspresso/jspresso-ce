package org.jspresso.framework.view {

  import mx.binding.utils.BindingUtils;
  import mx.binding.utils.ChangeWatcher;
  import mx.controls.listClasses.BaseListData;
  import mx.controls.listClasses.ListData;
  import mx.controls.listClasses.ListItemRenderer;
  
  import org.jspresso.framework.state.remote.RemoteCompositeValueState;
  import org.jspresso.framework.state.remote.RemoteValueState;

  public class RemoteValueDgItemRenderer extends ListItemRenderer {
    
    private var valueChangeListener:ChangeWatcher;
    private var _listData:BaseListData;
    
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

  	override public function get listData():BaseListData {
  	  return _listData;
  	}

  	override public function set data(value:Object):void	{
  	  updateLabel(value, _listData);
  	  super.listData.label = _listData.label;
  	  super.data = value;
  	}

  	private function updateLabel(rendererData:Object, rendererListData:BaseListData):void {
  	  if(rendererData && rendererListData) {
  	    var cellLabel:String;
  	    var cellValueState:RemoteValueState = ((rendererData as RemoteCompositeValueState).children[rendererListData.columnIndex +1] as RemoteValueState);
  	    if(valueChangeListener != null) {
  	      valueChangeListener.unwatch();
  	    }
  	    valueChangeListener = BindingUtils.bindSetter(refresh, cellValueState, "value");
  	    if(cellValueState.value != null) {
    	    cellLabel = cellValueState.value.toString();
    	  } else {
    	    cellLabel = null;
    	  }
    	  rendererListData.label = cellLabel;
  	  }
  	}
  	
  	private function refresh(cellLabel:Object):void {
      listData.label = cellLabel as String;
      validateProperties();
  	}
  }
}