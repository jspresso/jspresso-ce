/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jspresso.framework.view.flex {

import mx.utils.ObjectUtil;

import org.jspresso.framework.state.remote.RemoteCompositeValueState;
import org.jspresso.framework.state.remote.RemoteFormattedValueState;
import org.jspresso.framework.state.remote.RemoteValueState;
import org.jspresso.framework.util.format.DateUtils;
import org.jspresso.framework.util.lang.DateDto;

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
    if (obj1 != null) {
      cell1 = ((obj1 as RemoteCompositeValueState).children[sortColumnIndex] as RemoteValueState).value as String;
    }
    if (obj2 != null) {
      cell2 = ((obj2 as RemoteCompositeValueState).children[sortColumnIndex] as RemoteValueState).value as String;
    }
    return ObjectUtil.stringCompare(cell1, cell2, true);
  }

  public function compareBooleans(obj1:Object, obj2:Object):int {
    var cell1:Boolean;
    var cell2:Boolean;
    if (obj1 != null) {
      cell1 = ((obj1 as RemoteCompositeValueState).children[sortColumnIndex] as RemoteValueState).value as Boolean;
    }
    if (obj2 != null) {
      cell2 = ((obj2 as RemoteCompositeValueState).children[sortColumnIndex] as RemoteValueState).value as Boolean;
    }
    if (cell1 && !cell2) {
      return 1;
    } else if (cell2 && !cell1) {
      return -1;
    }
    return 0;
  }

  public function compareDates(obj1:Object, obj2:Object):int {
    var cell1:Date;
    var cell2:Date;
    if (obj1 != null) {
      var cell1AsObject:Object = ((obj1 as RemoteCompositeValueState).children[sortColumnIndex]
          as RemoteValueState).value;
      if (cell1AsObject is DateDto) {
        cell1 = DateUtils.fromDateDto(cell1AsObject as DateDto);
      } else {
        cell1 = cell1AsObject as Date;
      }
    }
    if (obj2 != null) {
      var cell2AsObject:Object = ((obj2 as RemoteCompositeValueState).children[sortColumnIndex]
          as RemoteValueState).value;
      if (cell2AsObject is DateDto) {
        cell2 = DateUtils.fromDateDto(cell2AsObject as DateDto);
      } else {
        cell2 = cell2AsObject as Date;
      }
    }
    return ObjectUtil.dateCompare(cell1, cell2);
  }

  public function compareNumbers(obj1:Object, obj2:Object):int {
    var cell1:Number;
    var cell2:Number;
    if (obj1 != null) {
      cell1 = ((obj1 as RemoteCompositeValueState).children[sortColumnIndex] as RemoteValueState).value as Number;
    }
    if (obj2 != null) {
      cell2 = ((obj2 as RemoteCompositeValueState).children[sortColumnIndex] as RemoteValueState).value as Number;
    }
    return ObjectUtil.numericCompare(cell1, cell2);
  }

  public function compareFormatted(obj1:Object, obj2:Object):int {
    var cell1:Object;
    var cell2:Object;
    if (obj1 != null) {
      cell1 = ((obj1 as RemoteCompositeValueState).children[sortColumnIndex]
          as RemoteFormattedValueState).valueAsObject;
    }
    if (obj2 != null) {
      cell2 = ((obj2 as RemoteCompositeValueState).children[sortColumnIndex]
          as RemoteFormattedValueState).valueAsObject;
    }
    if (cell1 is Date || cell2 is Date) {
      return ObjectUtil.dateCompare(cell1 as Date, cell2 as Date);
    } else if (cell1 is Number || cell2 is Number) {
      return ObjectUtil.numericCompare(cell1 as Number, cell2 as Number);
    } else if (cell1 is Boolean || cell2 is Boolean) {
      if (cell1 && !cell2) {
        return 1;
      } else if (cell2 && !cell1) {
        return -1;
      }
      return 0;
    } else {
      return ObjectUtil.stringCompare(cell1 as String, cell2 as String, true);
    }
  }
}
}
