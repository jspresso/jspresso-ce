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
import flash.display.GradientType;
import flash.display.Graphics;
import flash.display.Shape;
import flash.geom.Matrix;

import flex.utils.ui.resize.ResizablePanel;

import mx.core.mx_internal;

public class JsoResizablePanel extends ResizablePanel {
  private var _backgroundWidth:Number = 0;
  private var _textWidth:Number;
  private var _backgroundShape:Shape;

  public function JsoResizablePanel() {
    super();
  }

  override protected function createChildren():void {
    super.createChildren();

    if (!_backgroundShape) {
      _backgroundShape = new Shape();
      rawChildren.addChildAt(_backgroundShape, 0);
    }
  }

  override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
    super.updateDisplayList(unscaledWidth, unscaledHeight);

		if (_textWidth == Math.ceil(this.titleTextField.textWidth)) {
			return;
		}

    var backgroundColor:uint = getStyle("headerColor");
    var cornerRadius:Number = getStyle("cornerRadius");
    var headerHeight:Number = getStyle("headerHeight");

    _textWidth = this.titleTextField.textWidth;
    _backgroundWidth = _textWidth + 20;

    if (titleIcon) {
      (this.mx_internal::titleIconObject as DisplayObject).x += 10;
      _backgroundWidth += (this.mx_internal::titleIconObject as DisplayObject).width + 10;
    }

    var g:Graphics = _backgroundShape.graphics;
    var m:Matrix = new Matrix();
    m.createGradientBox(unscaledWidth, 5, Math.PI / 2, 0, headerHeight - 5);

    g.clear();

    g.beginFill(backgroundColor, 1.0);
    g.drawRoundRectComplex(10, 0, _backgroundWidth, headerHeight, cornerRadius, cornerRadius, 0, 0);

    g.beginGradientFill(GradientType.LINEAR, [0x000000, 0x000000], [0.0, 0.1], [0, 255], m);
    g.drawRect(0, headerHeight - 5, unscaledWidth, 5);

    g.endFill();

    titleTextField.x += 10;
    titleTextField.y += 1;
  }
}
}
