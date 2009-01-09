package org.jspresso.framework.view.flex {
  import mx.utils.StringUtil;
  

  public class PercentParser extends NumberParser {

    override public function parse(value:String):Object	{
      if(value == null || value.length == 0) {
        return null;
      }
      var parsedNumber:Number = super.parse(trimSuffix(value)) as Number;
      return parsedNumber / 100;
    }
    
    private function trimSuffix(value:String):String {
      var pattern:RegExp = /( |%)/g;
      return value.replace(pattern,""); 
    }
  }
}