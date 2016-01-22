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
package org.jspresso.framework.theme.header {

import flash.display.Graphics;

import mx.skins.halo.ButtonSkin;

public class CircleButtonSkin extends ButtonSkin {
  private static const BUTTON_RADIUS:Number = 12;

  public function CircleButtonSkin() {
    super();
  }

  override protected function updateDisplayList(w:Number, h:Number):void {
    super.updateDisplayList(w, h);

    var alphaValue:Number = (name.indexOf("over") != -1) ? 0.60 : 0.25;

    var color:uint = this.getStyle("color");
    if (!color) {
      color = 0x000000;
    }

    var g:Graphics = this.graphics;

    g.clear();
    // background
    g.beginFill(color, alphaValue);
    g.drawCircle(BUTTON_RADIUS, BUTTON_RADIUS, BUTTON_RADIUS);

    g.endFill();
  }
}
}
