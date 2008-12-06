package org.jspresso.framework.view {
  import mx.utils.ObjectUtil;
  
  import org.jspresso.framework.state.remote.RemoteCompositeValueState;
  import org.jspresso.framework.state.remote.RemoteValueState;
  
  public class RemoteValueSorter {
    
    private var _sortColumnIndex:int;
    
    public function set sortColumnIndex(value:int):void {
        _sortColumnIndex = value;
    }
    public function get sortColumnIndex():int {
        return _sortColumnIndex;
    }
    
    public function compareStrings(obj1:Object, obj2:Object):int {
      var cell1:String = ((obj1 as RemoteCompositeValueState).children[sortColumnIndex +1] as RemoteValueState)
                            .value as String;
      var cell2:String = ((obj2 as RemoteCompositeValueState).children[sortColumnIndex +1] as RemoteValueState)
                            .value as String;
      return ObjectUtil.stringCompare(cell1, cell2, true);
    }
  }
}