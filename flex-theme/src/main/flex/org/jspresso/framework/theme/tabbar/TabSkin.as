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
package org.jspresso.framework.theme.tabbar {

import flash.display.GradientType;
import flash.display.Graphics;
import flash.geom.Matrix;

import mx.skins.halo.TabSkin;

public class TabSkin extends mx.skins.halo.TabSkin {
  public function TabSkin() {
    super();
  }

  override protected function updateDisplayList(w:Number, h:Number):void {
    super.updateDisplayList(w, h);

    var borderColor:Number = this.getStyle("borderColor");
    if (!borderColor) {
      borderColor = 0x888888;
    }

    var cornerRadius:Number = this.getStyle("cornerRadius");
    if (!cornerRadius) {
      cornerRadius = 0;
    }

    var innerRadius:Number = Math.max(0, cornerRadius - 1);

    var g:Graphics = this.graphics;

    var m:Matrix = new Matrix();
    m.createGradientBox(w - 2, h - 5, Math.PI / 2, 0, 5);

    g.clear();

    switch (name) {
      case "upSkin":
      {
        g.beginFill(borderColor, 1.0);
        g.drawRoundRectComplex(0, 4, w, h - 4, cornerRadius, cornerRadius, 0, 0);
        g.beginGradientFill(GradientType.LINEAR, [0xFEFEFE, 0xDBDBDB], [1.0, 1.0], [0, 255], m);
        g.drawRoundRectComplex(1, 5, w - 2, h - 5, innerRadius, innerRadius, 0, 0);
        g.endFill();
        break;
      }

      case "downSkin":
      case "overSkin":
      {
        g.beginFill(borderColor, 1.0);
        g.drawRoundRectComplex(0, 4, w, h - 4, cornerRadius, cornerRadius, 0, 0);
        g.beginGradientFill(GradientType.LINEAR, [0xF0F0F0, 0xE9E9E9], [1.0, 1.0], [0, 255], m);
        g.drawRoundRectComplex(1, 5, w - 2, h - 5, innerRadius, innerRadius, 0, 0);
        g.endFill();
        break;
      }

      case "selectedUpSkin":
      case "selectedDownSkin":
      case "selectedOverSkin":
      case "selectedDisabledSkin":
      {
        g.beginFill(0x545454, 1.0);
        g.drawRoundRectComplex(0, 0, w, h, cornerRadius, cornerRadius, 0, 0);
        g.endFill();
        break;
      }
    }

    m.createGradientBox(w, 5, Math.PI / 2, 0, h - 5);

    g.beginGradientFill(GradientType.LINEAR, [0x000000, 0x000000], [0.0, 0.1], [0, 255], m);
    g.drawRect(0, h - 5, w, 5);

    g.endFill();
  }
}
}
