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

package org.jspresso.framework.util.format {

import org.jspresso.framework.util.lang.DateDto;

public class TimeParser extends Parser {

  private var _parseDateDto:Boolean = true;

  public function TimeParser() {
    parseDateDto = true;
  }

  override public function parse(value:String, existingValue:Object = null):Object {
    if (value == null || value.length == 0) {
      return null;
    }
    var parsedDate:Date = DateUtils.parseTime(value);
    if (parsedDate == null) {
      return existingValue;
    } else if (existingValue != null) {
      if (existingValue is Date) {
        var existingDate:Date = existingValue as Date;
        parsedDate.setFullYear(existingDate.fullYear, existingDate.month, existingDate.date);
      } else if (existingValue is DateDto) {
        var existingDateDto:DateDto = existingValue as DateDto;
        parsedDate.setFullYear(existingDateDto.year, existingDateDto.month, existingDateDto.date);
      }
    }
    if (parseDateDto) {
      return DateUtils.fromDate(parsedDate);
    } else {
      return parsedDate;
    }
  }

  public function get parseDateDto():Boolean {
    return _parseDateDto;
  }

  public function set parseDateDto(value:Boolean):void {
    _parseDateDto = value;
  }

}
}
