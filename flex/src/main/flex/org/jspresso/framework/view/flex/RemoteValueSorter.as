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
    
    public function RemoteValueSorter() {
      //default constructor.
    }

    public function set sortColumnIndex(value:int):void {
        _sortColumnIndex = value;
    }
    public function get sortColumnIndex():int {
        return _sortColumnIndex;
    }
    
    public function compareStrings(obj1:Object, obj2:Object):int {
      var cell1:String;
      var cell2:String;
      if(obj1 != null) {
        cell1 = ((obj1 as RemoteCompositeValueState).children[sortColumnIndex] as RemoteValueState)
                            .value as String;
      }
      if(obj2 != null) {
        cell2 = ((obj2 as RemoteCompositeValueState).children[sortColumnIndex] as RemoteValueState)
                            .value as String;
      }
      return ObjectUtil.stringCompare(cell1, cell2, true);
    }

    public function compareBooleans(obj1:Object, obj2:Object):int {
      var cell1:Boolean;
      var cell2:Boolean;
      if(obj1 != null) {
        cell1 = ((obj1 as RemoteCompositeValueState).children[sortColumnIndex] as RemoteValueState)
                            .value as Boolean;
      }
      if(obj2 != null) {
        cell2 = ((obj2 as RemoteCompositeValueState).children[sortColumnIndex] as RemoteValueState)
                            .value as Boolean;
      }
      if(cell1 && !cell2) {
        return 1;
      } else if(cell2 && !cell1) {
        return -1;
      }
      return 0;
    }

    public function compareDates(obj1:Object, obj2:Object):int {
      var cell1:Date;
      var cell2:Date;
      if(obj1 != null) {
        cell1 = ((obj1 as RemoteCompositeValueState).children[sortColumnIndex] as RemoteValueState)
                            .value as Date;
      }
      if(obj2 != null) {
        cell2 = ((obj2 as RemoteCompositeValueState).children[sortColumnIndex] as RemoteValueState)
                            .value as Date;
      }
      return ObjectUtil.dateCompare(cell1, cell2);
    }

    public function compareNumbers(obj1:Object, obj2:Object):int {
      var cell1:Number;
      var cell2:Number;
      if(obj1 != null) {
        cell1 = ((obj1 as RemoteCompositeValueState).children[sortColumnIndex] as RemoteValueState)
                            .value as Number;
      }
      if(obj2 != null) {
        cell2 = ((obj2 as RemoteCompositeValueState).children[sortColumnIndex] as RemoteValueState)
                            .value as Number;
      }
      return ObjectUtil.numericCompare(cell1, cell2);
    }
  }
}