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
import flash.filters.BitmapFilterQuality;
import flash.filters.GlowFilter;
import flash.geom.Matrix;

import mx.skins.halo.ColorPickerSkin;

import org.jspresso.framework.theme.utils.ThemeFilterUtils;

public class ColorPickerSkin extends mx.skins.halo.ColorPickerSkin {
  private static const ARROW_HEIGHT:uint = 4;
  private static const ARROW_WIDTH:uint = 6;

  private static const SWATCH_SIZE:uint = 14;

  public function ColorPickerSkin() {
    super();
  }

  override protected function updateDisplayList(w:Number, h:Number):void {
    var g:Graphics = this.graphics;
    var m:Matrix = new Matrix();
    m.createGradientBox(w, h, Math.PI / 2);

    g.clear();

    var pos:Number = (h - SWATCH_SIZE) * 0.5;

    // hit Zone
    g.beginFill(0xCCFF33, 0.0);
    g.drawRect(0, 0, w, h);
    g.endFill();

    switch (name) {
      case "overSkin":
      {
        // stroke
        g.beginFill(0xBDBDBD, 1.0);
        g.drawRect(0, 0, w, h);
        g.drawRect(pos, pos, SWATCH_SIZE, SWATCH_SIZE);
        // fill
        g.beginGradientFill(GradientType.LINEAR, [0xFFFFFF, 0xF0F0F0], [1.0, 1.0], [0, 255], m);
        g.drawRect(1, 1, w - 2, h - 2);
        g.drawRect(pos, pos, SWATCH_SIZE, SWATCH_SIZE);

        filters = [ThemeFilterUtils.buttonShadow()];
        break;
      }
      case "downSkin":
      {
        // stroke
        g.beginFill(0xBDBDBD, 1.0);
        g.drawRect(0, 0, w, h);
        g.drawRect(pos, pos, SWATCH_SIZE, SWATCH_SIZE);
        // fill
        g.beginFill(0xEDEDED, 1.0);
        g.drawRect(1, 1, w - 2, h - 2);
        g.drawRect(pos, pos, SWATCH_SIZE, SWATCH_SIZE);
        filters = [new GlowFilter(0x000000, 0.2, 5, 5, 1, BitmapFilterQuality.HIGH, true)];
        break;
      }
      case "upSkin":
      default:
      {
        g.beginFill(0xFFFFFF, 1.0);
        g.drawRect(0, 0, w, h);
        g.drawRect(pos, pos, SWATCH_SIZE, SWATCH_SIZE);
        break;
      }
    }

    // swatch's stroke
    g.beginFill(0x919191, 1.0);
    g.drawRect(pos - 1, pos - 1, SWATCH_SIZE + 2, 1); // --- top
    g.drawRect(pos - 1, pos + SWATCH_SIZE, SWATCH_SIZE + 2, 1); // --- bottom
    g.drawRect(pos - 1, pos, 1, SWATCH_SIZE); // --- left
    g.drawRect(pos + SWATCH_SIZE, pos, 1, SWATCH_SIZE); // --- right

    // arrows
    g.beginFill(getStyle("color"), 1.0);
    var startX:Number = w - 12;
    var startY:Number = (h * 0.5) + 1;
    // bottom
    g.moveTo(startX, startY);
    g.lineTo(startX + ARROW_WIDTH, startY);
    g.lineTo(startX + (ARROW_WIDTH * 0.5), startY + ARROW_HEIGHT);
    g.lineTo(startX, startY);
    // top
    startY = (h * 0.5) - 1;
    g.moveTo(startX, startY);
    g.lineTo(startX + (ARROW_WIDTH * 0.5), startY - ARROW_HEIGHT);
    g.lineTo(startX + ARROW_WIDTH, startY);
    g.lineTo(startX, startY);

    g.endFill();
  }
}
}
