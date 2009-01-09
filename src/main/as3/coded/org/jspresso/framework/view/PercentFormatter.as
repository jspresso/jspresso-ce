package org.jspresso.framework.view {

  import mx.formatters.NumberFormatter;

  public class PercentFormatter extends NumberFormatter {
    
    public static const PERCENT_SUFFIX:String = " %";
    
    override public function format(value:Object):String {
      if(value == null) {
        return null;
      }
      if(value is Number) {
        return super.format((value as Number)*100) + PERCENT_SUFFIX;
      } else {
        return super.format(new Number(value)*100) + PERCENT_SUFFIX;
      }
    }
  }
}