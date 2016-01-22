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
package org.jspresso.framework.theme.scrollbars {

import flash.display.Graphics;

import mx.skins.halo.ScrollThumbSkin;

public class HScrollThumbSkin extends ScrollThumbSkin {
  public function HScrollThumbSkin() {
    super();
  }

  override public function get measuredWidth():Number {
    return 10;
  }

  override protected function updateDisplayList(w:Number, h:Number):void {
    super.updateDisplayList(w, h);

    var g:Graphics = this.graphics;

    g.clear();

    switch (name) {
      default:
      case "thumbUpSkin":
      {
        g.beginFill(0x000000, 0.35);
        break;
      }

      case "thumbDownSkin":
      case "thumbOverSkin":
      {
        g.beginFill(0x000000, 0.55);
        break;
      }

      case "thumbDisabledSkin":
      {
        g.beginFill(0x000000, 0.15);
        break;
      }
    }

    g.drawRect(0, 2, w - 2, h - 4);
    g.endFill();
  }
}
}
