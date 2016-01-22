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

import mx.skins.Border;

public class CentralViewStackBorderSkin extends Border {
  public function CentralViewStackBorderSkin() {
    super();
  }

  override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
    super.updateDisplayList(unscaledWidth, unscaledHeight);

    var backgroundGradientColors:Array = getStyle("backgroundGradientColors");

    var g:Graphics = this.graphics;
    var m:Matrix = new Matrix();
    m.createGradientBox(unscaledWidth, unscaledHeight, Math.PI / 2);

    g.clear();
    g.beginGradientFill(GradientType.LINEAR, backgroundGradientColors, [1.0, 1.0], [0, 255], m);
    g.drawRect(0, 0, unscaledWidth, unscaledHeight);
    g.endFill();
  }
}
}
