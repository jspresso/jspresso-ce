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

import mx.formatters.NumberBase;

public class NumberParser extends Parser {

  private var _parser:NumberBase;
  private var _precision:uint;

  public function NumberParser() {
    //default constructor.
  }

  override public function parse(value:String, existingValue:Object = null):Object {
    if (value == null || value.length == 0) {
      return null;
    }
    var prepared:String = value.replace(new RegExp("\\" + _parser.thousandsSeparatorTo, "g"),
                                        _parser.thousandsSeparatorFrom);
    prepared = prepared.replace(new RegExp("\\" + _parser.decimalSeparatorTo, "g"), _parser.decimalSeparatorFrom);
    var parsedNumber:Number = Number(_parser.parseNumberString(prepared));
    parsedNumber.toFixed(_precision);
    return parsedNumber;
  }

  public function set numberBase(value:NumberBase):void {
    _parser = value;
  }

  public function set precision(value:uint):void {
    _precision = value;
  }
}
}
