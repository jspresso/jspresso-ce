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

public class CheckBoxIcon extends mx.skins.halo.CheckBoxIcon {
  private static const BOX_SIZE:uint = 12;

  public function CheckBoxIcon() {
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
        g.drawRect(0, 0, BOX_SIZE, BOX_SIZE);
        // fill
        g.beginFill(0xFFFFFF, 1.0);
        g.drawRect(1, 1, BOX_SIZE - 2, BOX_SIZE - 2);

        tickColor = 0x616161;

        break;
      }

      case "overIcon":
      case "selectedOverIcon":
      {
        // stroke
        g.beginFill(0x888888, 1.0);
        g.drawRect(0, 0, BOX_SIZE, BOX_SIZE);
        // fill
        g.beginFill(0xFFFFFF, 1.0);
        g.drawRect(1, 1, BOX_SIZE - 2, BOX_SIZE - 2);

        tickColor = 0x00789C;

        break;
      }

      case "downIcon":
      case "selectedDownIcon":
      {
        // stroke
        g.beginFill(0x888888, 1.0);
        g.drawRect(0, 0, BOX_SIZE, BOX_SIZE);
        // fill
        g.beginFill(0xE5E5E5, 1.0);
        g.drawRect(1, 1, BOX_SIZE - 2, BOX_SIZE - 2);

        tickColor = 0x00789C;

        break;
      }

      case "disabledIcon":
      case "selectedDisabledIcon":
      {
        // stroke
        g.beginFill(0xCCCCCC, 1.0);
        g.drawRect(0, 0, BOX_SIZE, BOX_SIZE);
        // fill
        g.beginFill(0xFFFFFF, 1.0);
        g.drawRect(1, 1, BOX_SIZE - 2, BOX_SIZE - 2);

        tickColor = 0xA9A9A9;

        break;
      }
    }

    // Draw the checkmark symbol.
    if (name.indexOf("selected") != -1) {
      g.beginFill(tickColor, 1.0);

      g.moveTo(2, 5);

      g.lineTo(5, 10);
      g.lineTo(6, 10);

      g.lineTo(13, 2);
      g.lineTo(12, 0);

      g.lineTo(6, 7);

      g.lineTo(4, 4);
      g.lineTo(2, 5);
      g.endFill();
    }
  }
}
}
