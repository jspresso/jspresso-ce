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

import flash.events.FocusEvent;

import mx.containers.GridItem;
import mx.controls.TextInput;
import mx.core.mx_internal;
import mx.managers.IFocusManager;

use namespace mx_internal;

public class EnhancedTextInput extends TextInput {

  private var _preventDefaultButton:Boolean = false;

  public function EnhancedTextInput() {
    super();
  }

  override protected function focusInHandler(event:FocusEvent):void {
    super.focusInHandler(event);
    if (preventDefaultButton) {
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

  public function get preventDefaultButton():Boolean {
    return _preventDefaultButton;
  }

  public function set preventDefaultButton(value:Boolean):void {
    _preventDefaultButton = value;
  }

  override public function set measuredWidth(value:Number):void {
    if (measuredWidth == 0 && value == DEFAULT_MEASURED_WIDTH && maxWidth > 0 && maxWidth < (DEFAULT_MAX_WIDTH / 2)
        && parent is GridItem && (parent as GridItem).colSpan == 1) {
      super.measuredWidth = maxWidth;
    } else {
      super.measuredWidth = value;
    }
  }

  override protected function commitProperties():void {
    super.commitProperties();
    // prevents newlines to be stripped since they
    // can be transformed to something else on server-side.
    textField.multiline = true;
  }
}
}
