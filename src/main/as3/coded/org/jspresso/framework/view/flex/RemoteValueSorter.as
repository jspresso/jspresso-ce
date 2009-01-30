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
      var cell1:String= null;
      var cell2:String= null;
      if(obj1 != null) {
        cell1 = ((obj1 as RemoteCompositeValueState).children[sortColumnIndex +1] as RemoteValueState)
                            .value as String;
      }
      if(obj2 != null) {
        cell2 = ((obj2 as RemoteCompositeValueState).children[sortColumnIndex +1] as RemoteValueState)
                            .value as String;
      }
      return ObjectUtil.stringCompare(cell1, cell2, true);
    }
  }
}