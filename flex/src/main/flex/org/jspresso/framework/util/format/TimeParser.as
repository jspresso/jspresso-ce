/**
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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

package org.jspresso.framework.util.format {
  import flexlib.scheduling.util.DateUtil;
  
  public class TimeParser extends Parser {

    public function TimeParser() {
      //default constructor.
    }

    override public function parse(value:String, existingValue:Object = null):Object	{
      if(value == null || value.length == 0) {
        return null;
      }
      var parsedDate:Date = DateUtils.parseTime(value);
      if(parsedDate == null) {
        return existingValue;
      } else if(existingValue != null) {
        var dt:Date = existingValue as Date;
        parsedDate.setFullYear(dt.fullYear, dt.month, dt.date);
      }
      return parsedDate;
    }
  }
}