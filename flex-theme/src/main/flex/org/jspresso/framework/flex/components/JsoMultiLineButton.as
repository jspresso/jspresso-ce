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
package org.jspresso.framework.flex.components {

import flash.display.DisplayObject;
import flash.text.TextLineMetrics;

import mx.core.IFlexDisplayObject;
import mx.core.mx_internal;

import org.jspresso.framework.view.flex.EnhancedButton;

use namespace mx_internal;

public class JsoMultiLineButton extends EnhancedButton {
  public function JsoMultiLineButton() {
    super();
  }

  override protected function createChildren():void {
    if (!textField) {
      textField = new NoTruncationUITextField();
      textField.styleName = this;
      addChild(DisplayObject(textField));
    }

    super.createChildren();

    textField.multiline = true;
    textField.wordWrap = true;
    textField.width = width;
  }

  override protected function measure():void {
    if (!isNaN(explicitWidth)) {
      var tempIcon:IFlexDisplayObject = getCurrentIcon();
      var w:Number = explicitWidth;
			if (tempIcon) {
				w -= tempIcon.width + getStyle("horizontalGap") + getStyle("paddingLeft") + getStyle("paddingRight");
			}
      textField.width = w;
    }
    super.measure();

  }

  override public function measureText(s:String):TextLineMetrics {
    textField.text = s;
    var lineMetrics:TextLineMetrics = textField.getLineMetrics(0);
    lineMetrics.width = textField.textWidth + 4;
    lineMetrics.height = textField.textHeight + 4;
    return lineMetrics;
  }
}
}

import mx.core.UITextField;

class NoTruncationUITextField extends UITextField {
  public function NoTruncationUITextField() {
    super();
  }

  override public function truncateToFit(s:String = null):Boolean {
    return false;
  }
}
