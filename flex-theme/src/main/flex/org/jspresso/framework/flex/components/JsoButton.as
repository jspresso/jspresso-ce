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

import mx.core.IFlexDisplayObject;
import mx.core.mx_internal;

import org.jspresso.framework.theme.utils.ThemeFilterUtils;

import org.jspresso.framework.view.flex.EnhancedButton;

use namespace mx_internal;

[Style(name="grayFactor", type="Number", inherit="no")]

public class JsoButton extends EnhancedButton {

  public function JsoButton() {
    super();
    this.buttonMode = true;
  }

  override public function set enabled(value:Boolean):void {
    super.enabled = value;
    this.buttonMode = value;
  }

  override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
    super.updateDisplayList(unscaledWidth, unscaledHeight);
    textField.y += 1;
  }

  override mx_internal function viewIconForPhase(tempIconName:String):IFlexDisplayObject {
    var icon:IFlexDisplayObject = super.viewIconForPhase(tempIconName);
    var grayFactor:Number = getStyle("grayFactor");
    if (icon && grayFactor) {
      if (tempIconName == overIconName) {
        if (grayFactor > 0) {
          ThemeFilterUtils.ungray(icon);
        } else {
          ThemeFilterUtils.gray(icon, grayFactor);
        }
      } else {
        if (grayFactor > 0) {
          ThemeFilterUtils.gray(icon, grayFactor);
        } else {
          ThemeFilterUtils.ungray(icon);
        }
      }
    }
    return icon;
  }
}
}
