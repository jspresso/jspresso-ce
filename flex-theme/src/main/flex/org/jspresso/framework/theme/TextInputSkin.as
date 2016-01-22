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

import mx.skins.ProgrammaticSkin;

public class TextInputSkin extends ProgrammaticSkin {
  public function TextInputSkin() {
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
    m.createGradientBox(w, h - 2, Math.PI / 2);

    g.clear();

    // stroke
    g.beginFill(borderColor, 1.0);
    g.drawRect(0, 0, w, h);

    var backgroundColor:Number = this.getStyle("backgroundColor");
    if (!backgroundColor) {
      backgroundColor = 0xFEFEFE;
    }
    // fill
    g.beginGradientFill(GradientType.LINEAR, [backgroundColor - 0x151515, backgroundColor], [1.0, 1.0], [0, 255], m);
    g.drawRect(1, 1, w - 2, h - 2);

    g.endFill();
  }
}
}
