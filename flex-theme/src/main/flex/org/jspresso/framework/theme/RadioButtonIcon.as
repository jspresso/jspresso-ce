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

import flash.display.Graphics;

import mx.skins.halo.CheckBoxIcon;

public class RadioButtonIcon extends mx.skins.halo.CheckBoxIcon {
  private static const BOX_SIZE:uint = 7;
  private static const DRAWING_POS:uint = 5;

  public function RadioButtonIcon() {
    super();
  }

  override protected function updateDisplayList(w:Number, h:Number):void {
    super.updateDisplayList(w, h);

    var g:Graphics = graphics;
    var tickColor:uint;

    g.clear();

    switch (name) {
      case "upIcon":
      case "selectedUpIcon":
      {
        // stroke
        g.beginFill(0xB3B3B3, 1.0);
        g.drawCircle(DRAWING_POS, DRAWING_POS, BOX_SIZE);
        // fill
        g.beginFill(0xFFFFFF, 1.0);
        g.drawCircle(DRAWING_POS, DRAWING_POS, BOX_SIZE - 1);

        tickColor = 0x616161;

        break;
      }

      case "overIcon":
      case "selectedOverIcon":
      {
        // stroke
        g.beginFill(0x888888, 1.0);
        g.drawCircle(DRAWING_POS, DRAWING_POS, BOX_SIZE);
        // fill
        g.beginFill(0xFFFFFF, 1.0);
        g.drawCircle(DRAWING_POS, DRAWING_POS, BOX_SIZE - 1);

        tickColor = 0x00789C;

        break;
      }

      case "downIcon":
      case "selectedDownIcon":
      {
        // stroke
        g.beginFill(0x888888, 1.0);
        g.drawCircle(DRAWING_POS, DRAWING_POS, BOX_SIZE);
        // fill
        g.beginFill(0xE5E5E5, 1.0);
        g.drawCircle(DRAWING_POS, DRAWING_POS, BOX_SIZE - 1);

        tickColor = 0x00789C;

        break;
      }

      case "disabledIcon":
      case "selectedDisabledIcon":
      {
        // stroke
        g.beginFill(0xCCCCCC, 1.0);
        g.drawCircle(DRAWING_POS, DRAWING_POS, BOX_SIZE);
        // fill
        g.beginFill(0xFFFFFF, 1.0);
        g.drawCircle(DRAWING_POS, DRAWING_POS, BOX_SIZE - 1);

        tickColor = 0xA9A9A9;

        break;
      }
    }

    // Draw the checkmark symbol.
    if (name.indexOf("selected") != -1) {
      g.beginFill(tickColor, 1.0);
      g.drawCircle(DRAWING_POS, DRAWING_POS, BOX_SIZE - 4);
      g.endFill();
    }
  }
}
}
