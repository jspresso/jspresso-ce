package org.jspresso.framework.view.flex {

import mx.controls.DateField;
import mx.core.ITextInput;
import mx.core.mx_internal;
import mx.events.FlexEvent;

use namespace mx_internal;

public class EnhancedDateField extends DateField {

  private var _enabled:Boolean;

  public function EnhancedDateField() {
    _enabled = true;
    addEventListener(FlexEvent.CREATION_COMPLETE, function (event:FlexEvent):void {
      enabled = _enabled;
    });
  }

  override public function set enabled(value:Boolean):void {
    _enabled = value;
    if (mx_internal::ComboDownArrowButton) {
      mx_internal::ComboDownArrowButton.enabled = value;
    }
    var ti:ITextInput = mx_internal::getTextInput();
    if (ti) {
      ti.enabled = true;
      ti.editable = value;
    }
  }

  private function syncTextInputBackground():void {
    var ti:ITextInput = mx_internal::getTextInput();
    if (ti) {
      if (enabled) {
        ti.setStyle("backgroundColor", getStyle("backgroundColor"));
      } else {
        ti.setStyle("backgroundColor", ti.getStyle("backgroundDisabledColor"));
      }
    }
  }

  override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
    syncTextInputBackground();
    super.updateDisplayList(unscaledWidth, unscaledHeight);
  }

}
}
