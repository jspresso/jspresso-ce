package org.jspresso.framework.view.flex {
  import mx.containers.GridItem;
  import mx.controls.TextInput;
  import mx.core.mx_internal;
  import mx.managers.IFocusManager;

  import flash.events.FocusEvent;

  use namespace mx_internal;

  public class EnhancedTextInput extends TextInput {
    
    private var _preventDefaultButton:Boolean = false;
         
    public function EnhancedTextInput() {
      super();
    }

    override protected function focusInHandler(event:FocusEvent):void {
      super.focusInHandler(event);
      if(preventDefaultButton) {
        var fm:IFocusManager = focusManager;
        if (fm) {
          fm.defaultButtonEnabled = false;
        }
      }
    }
    
    override protected function focusOutHandler(event:FocusEvent):void {
      super.focusOutHandler(event);
      var fm:IFocusManager = focusManager;
      if (fm) {
        fm.defaultButtonEnabled = true;
      }
    }

    public function get preventDefaultButton():Boolean
    {
      return _preventDefaultButton;
    }

    public function set preventDefaultButton(value:Boolean):void
    {
      _preventDefaultButton = value;
    }
    
    override public function set measuredWidth(value:Number):void {
      if(  measuredWidth == 0
        && value == DEFAULT_MEASURED_WIDTH
        && maxWidth > 0 && maxWidth < (DEFAULT_MAX_WIDTH/2)
        && parent is GridItem
        && (parent as GridItem).colSpan == 1) {
        super.measuredWidth = maxWidth;
      } else {
        super.measuredWidth = value;
      }
    }

  }
}