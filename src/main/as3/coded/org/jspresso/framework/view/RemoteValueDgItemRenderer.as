package org.jspresso.framework.view {

  import mx.controls.dataGridClasses.DataGridItemRenderer;
  import mx.controls.listClasses.BaseListData;
  
  import org.jspresso.framework.state.remote.RemoteCompositeValueState;
  import org.jspresso.framework.state.remote.RemoteValueState;

  public class RemoteValueDgItemRenderer extends DataGridItemRenderer {
    
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
    	  cellLabel = ((rendererData as RemoteCompositeValueState).children[rendererListData.columnIndex +1] as RemoteValueState).value as String;
    	  rendererListData.label = cellLabel;
  	  }
  	}
  }
}