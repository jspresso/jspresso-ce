package org.jspresso.framework.view {

  import mx.binding.utils.BindingUtils;
  import mx.binding.utils.ChangeWatcher;
  import mx.controls.dataGridClasses.DataGridItemRenderer;
  import mx.controls.listClasses.BaseListData;
  
  import org.jspresso.framework.state.remote.RemoteCompositeValueState;
  import org.jspresso.framework.state.remote.RemoteValueState;

  public class RemoteValueDgItemRenderer extends DataGridItemRenderer {
    
    private var valueChangeListener:ChangeWatcher;
    
  	override public function set listData(value:BaseListData):void {
   	  updateLabel(data, value);
  	  super.listData = value;
  	}

  	override public function set data(value:Object):void	{
  	  updateLabel(value, listData);
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
    	  cellLabel = cellValueState.value as String;
    	  rendererListData.label = cellLabel;
  	  }
  	}
  	
  	private function refresh(cellLabel:Object):void {
      listData.label = cellLabel as String;
      validateProperties();
  	}
  }
}