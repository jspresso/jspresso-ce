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
