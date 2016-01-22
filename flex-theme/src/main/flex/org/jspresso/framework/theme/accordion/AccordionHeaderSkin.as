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
package org.jspresso.framework.theme.accordion {

import flash.display.Graphics;

import mx.skins.halo.AccordionHeaderSkin;

public class AccordionHeaderSkin extends mx.skins.halo.AccordionHeaderSkin {
  private static const ARROW_WIDTH:uint = 8;
  private static const ARROW_HEIGHT:uint = 6;

  public function AccordionHeaderSkin() {
    super();
  }

  override protected function updateDisplayList(w:Number, h:Number):void {
    super.updateDisplayList(w, h);

    var g:Graphics = this.graphics;

    g.clear();

    // hit zone
    g.beginFill(0xCCFF33, 0.0);
    g.drawRect(0, 0, w, h);

    // top stroke
    g.beginFill(0xFFFFFF, 1.0);
    g.drawRect(1, 0, w - 2, 1);
    // bottom stroke
    g.beginFill(0xD9DAD8, 1.0);
    g.drawRect(1, h - 1, w - 2, 1);
    // arrow
    var startX:Number = 15;
    var startY:Number;
    g.beginFill(getStyle("color"), 1.0);
    if (name.toLowerCase().indexOf("selected") == -1) {
      startY = (h - ARROW_WIDTH) * 0.5;
      g.moveTo(startX, startY);
      g.lineTo(startX + ARROW_HEIGHT, startY + (ARROW_WIDTH * 0.5));
      g.lineTo(startX, startY + ARROW_WIDTH);
      g.lineTo(startX, startY);
    } else {
      startY = (h - ARROW_HEIGHT) * 0.5;
      g.moveTo(startX, startY);
      g.lineTo(startX + ARROW_WIDTH, startY);
      g.lineTo(startX + (ARROW_WIDTH * 0.5), startY + ARROW_HEIGHT);
      g.lineTo(startX, startY);
    }
    g.endFill();
  }
}
}
