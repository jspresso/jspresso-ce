package org.jspresso.framework.view.flex
{
  import mx.controls.DateField;
  import mx.controls.TextInput;
  import mx.core.IFlexDisplayObject;
  import mx.core.mx_internal;
  import mx.events.FlexEvent;

  use namespace mx_internal;

  public class EnhancedDateField extends DateField {
    
    private var _enabled:Boolean;
    
    public function EnhancedDateField() {
      _enabled = true;
      addEventListener(FlexEvent.CREATION_COMPLETE, function(event:FlexEvent):void {
        enabled = _enabled;
      });
    }
    
    override public function set enabled(value:Boolean):void {
      _enabled = value;
      if(mx_internal::ComboDownArrowButton) {
        mx_internal::ComboDownArrowButton.enabled = value;
      }
      var ti:TextInput = mx_internal::getTextInput();
      if(ti) {
        ti.enabled = true;
        if(value) {
          ti.setStyle("backgroundColor", null);
        } else {
          ti.setStyle("backgroundColor", ti.getStyle("backgroundDisabledColor"));
        }
        ti.editable = value;
      }
    }
  }
}