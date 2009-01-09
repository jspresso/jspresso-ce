package org.jspresso.framework.view.flex {
  import mx.formatters.NumberBase;
  import mx.formatters.NumberFormatter;
  

  public class NumberParser extends Parser {

    private var _parser:NumberBase;
    private var _precision:uint;

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