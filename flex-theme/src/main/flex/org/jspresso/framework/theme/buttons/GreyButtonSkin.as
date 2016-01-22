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
package org.jspresso.framework.theme.buttons {

import flash.display.GradientType;
import flash.display.Graphics;
import flash.filters.BitmapFilterQuality;
import flash.filters.GlowFilter;
import flash.geom.Matrix;

import mx.skins.halo.ButtonSkin;

public class GreyButtonSkin extends ButtonSkin {
  public function GreyButtonSkin() {
    super();
  }

  override protected function updateDisplayList(w:Number, h:Number):void {
    super.updateDisplayList(w, h);

    var cornerRadius:Number = this.getStyle("cornerRadius");
    if (!cornerRadius) {
      cornerRadius = 0;
    }

    var innerRadius:Number = Math.abs(cornerRadius - 1);

    var g:Graphics = this.graphics;

    var m:Matrix = new Matrix();
    m.createGradientBox(w, h, Math.PI / 2);

    g.clear();

    if (name == "downSkin") {
      // stroke
      g.beginFill(0xBDBDBD, 1.0);
      g.drawRoundRect(0, 0, w, h, cornerRadius, cornerRadius);
      // fill
      g.beginFill(0xEDEDED, 1.0);
      g.drawRoundRect(1, 1, w - 2, h - 2, innerRadius, innerRadius);
      filters = [new GlowFilter(0x000000, 0.2, 5, 5, 1, BitmapFilterQuality.HIGH, true)];
    } else {
      g.beginGradientFill(GradientType.LINEAR, [0xFFFFFF, 0xEFEFEF], [1.0, 1.0], [0, 255], m);
      g.drawRoundRect(0, 0, w, h, cornerRadius, cornerRadius);

      filters = [];
    }

    g.endFill();
  }
}
}
