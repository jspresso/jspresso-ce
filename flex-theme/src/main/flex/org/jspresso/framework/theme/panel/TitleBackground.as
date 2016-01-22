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
package org.jspresso.framework.theme.panel {

import flash.display.GradientType;
import flash.display.Graphics;
import flash.geom.Matrix;

import mx.skins.halo.TitleBackground;

public class TitleBackground extends mx.skins.halo.TitleBackground {
  public function TitleBackground() {
    super();
  }

  override protected function updateDisplayList(w:Number, h:Number):void {
    super.updateDisplayList(w, h);

    var backgroundColor:uint = getStyle("headerColor");
    var cornerRadius:Number = getStyle("cornerRadius");

    var g:Graphics = this.graphics;
    var m:Matrix = new Matrix();
    m.createGradientBox(w, 5, Math.PI / 2, 0, h - 5);

    g.clear();

    g.beginFill(backgroundColor, 1.0);
    g.drawRoundRectComplex(0, 0, w, h, cornerRadius, cornerRadius, 0, 0);

    g.beginGradientFill(GradientType.LINEAR, [0x000000, 0x000000], [0.0, 0.1], [0, 255], m);
    g.drawRect(0, h - 5, w, 5);

    g.endFill();
  }
}
}
