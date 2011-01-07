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
  import mx.formatters.NumberBase;
  import mx.formatters.NumberFormatter;
  

  public class NumberParser extends Parser {

    private var _parser:NumberBase;
    private var _precision:uint;

    public function NumberParser() {
      //default constructor.
    }

    override public function parse(value:String):Object	{
      if(value == null || value.length == 0) {
        return null;
      }
	    var parsedNumber:Number = new Number(_parser.parseNumberString(value));
	    parsedNumber.toFixed(_precision);
	    return parsedNumber;
    }
    
  	public function set numberBase(value:NumberBase):void	{
  	  _parser = value;
  	}

  	public function set precision(value:uint):void	{
  	  _precision = value;
  	}
  }
}