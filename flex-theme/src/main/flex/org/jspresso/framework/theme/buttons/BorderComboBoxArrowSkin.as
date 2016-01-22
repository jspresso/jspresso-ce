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

import flash.display.DisplayObject;
import flash.display.GradientType;
import flash.display.Graphics;
import flash.filters.BitmapFilterQuality;
import flash.filters.GlowFilter;
import flash.geom.Matrix;

import mx.controls.Button;
import mx.controls.ComboBox;
import mx.controls.Image;
import mx.skins.halo.ComboBoxArrowSkin;

import org.jspresso.framework.theme.utils.ThemeFilterUtils;

public class BorderComboBoxArrowSkin extends ComboBoxArrowSkin {
  private static const ARROW_WIDTH:uint = 6;
  private static const ARROW_HEIGHT:uint = 4;

  public function BorderComboBoxArrowSkin() {
    super();
  }

  override protected function updateDisplayList(w:Number, h:Number):void {
    super.updateDisplayList(w, h);

    var cornerRadius:Number = this.getStyle("cornerRadius");
    if (!cornerRadius) {
      cornerRadius = 0;
    }

    var innerRadius:Number = Math.abs(cornerRadius - 1);

    var icon:DisplayObject = getIconComponent();
    var hole:Object = null;
    if (icon) {
      hole = {x: icon.x, y: icon.y, w: icon.width, h: icon.height, r: 1};
    }

    var g:Graphics = this.graphics;

    var m:Matrix = new Matrix();
    m.createGradientBox(w, h, Math.PI / 2);

    g.clear();

    if (name == "downSkin") {
      // stroke
      drawRoundRect(0, 0, w, h, cornerRadius, 0xBDBDBD, 1.0, null, GradientType.LINEAR, null, hole);

      // fill
      drawRoundRect(1, 1, w - 2, h - 2, innerRadius, 0xEDEDED, 1.0, null, GradientType.LINEAR, null, hole);

      filters = [new GlowFilter(0x000000, 0.2, 5, 5, 1, BitmapFilterQuality.HIGH, true)];
    } else {
      // stroke
      drawRoundRect(0, 0, w, h, cornerRadius, 0xBDBDBD, 1.0, null, GradientType.LINEAR, null, hole);

      // fill
      drawRoundRect(1, 1, w - 2, h - 2, cornerRadius, [0xFFFFFF, 0xF0F0F0], 1.0, m, GradientType.LINEAR, [0, 255],
                    hole);

      filters = [ThemeFilterUtils.buttonShadow()];
    }

    var startX:Number = w - 15;
    var startY:Number = (h - ARROW_HEIGHT) * 0.5;
    // arrow
    g.beginFill(getStyle("color"), 1.0);
    g.moveTo(startX, startY);
    g.lineTo(startX + ARROW_WIDTH, startY);
    g.lineTo(startX + (ARROW_WIDTH * 0.5), startY + ARROW_HEIGHT);
    g.lineTo(startX, startY);

    g.endFill();
  }

  private function getIconComponent():DisplayObject {
    if (parent is Button) {
      var grandParent:DisplayObject = parent.parent;
      if (grandParent is ComboBox) {
        var icon:DisplayObject = (grandParent as ComboBox).getChildAt(0);

        if (icon is Image) {
          return icon;
        }
      }
    }
    return null;
  }
}
}
