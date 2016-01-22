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
package org.jspresso.framework.theme {

import flash.display.GradientType;
import flash.display.Graphics;
import flash.geom.Matrix;

import mx.controls.TextArea;
import mx.skins.ProgrammaticSkin;

public class TextAreaSkin extends ProgrammaticSkin {
  private static const GRADIENT_HEIGHT:uint = 10;

  public function TextAreaSkin() {
    super();
  }

  override protected function updateDisplayList(w:Number, h:Number):void {
    super.updateDisplayList(w, h);

    var borderColor:Number = this.getStyle("borderColor");
    if (!borderColor) {
      borderColor = 0x888888;
    }

    var g:Graphics = this.graphics;

    var m:Matrix = new Matrix();
    m.createGradientBox(w, GRADIENT_HEIGHT, Math.PI / 2);

    g.clear();

    var borderAlpha:Number = (this.parent && TextArea(this.parent).selectable) ? 1.0 : 0.0;

    // stroke
    g.beginFill(borderColor, borderAlpha);
    g.drawRect(0, 0, w, h);
    // fill
    var backgroundColor:Number = this.getStyle("backgroundColor");
    if (!backgroundColor) {
      backgroundColor = 0xFEFEFE;
    }
    g.beginGradientFill(GradientType.LINEAR, [backgroundColor - 0x151515, backgroundColor], [1.0, 1.0], [0, 255], m);
    g.drawRect(1, 1, w - 2, GRADIENT_HEIGHT);

    g.beginFill(backgroundColor, 1.0);
    g.drawRect(1, 1 + GRADIENT_HEIGHT, w - 2, h - GRADIENT_HEIGHT - 2);

    g.endFill();
  }
}
}
