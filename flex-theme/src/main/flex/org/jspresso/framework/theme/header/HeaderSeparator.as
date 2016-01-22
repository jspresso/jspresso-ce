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
import flash.display.Sprite;

import mx.core.UIComponent;

public class HeaderSeparator extends UIComponent {
  private var shapeContainer:Sprite;

  public function HeaderSeparator() {
    super();

    this.percentHeight = 100;
  }

  override public function get measuredWidth():Number {
    return 2;
  }

  override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
    super.updateDisplayList(unscaledWidth, unscaledHeight);

    if (!shapeContainer) {
      shapeContainer = new Sprite();
      this.addChild(shapeContainer);
    }
    var g:Graphics = this.graphics;

    g.clear();
    g.beginFill(0x000000, 0.25);
    g.drawRect(0, 0, 1, unscaledHeight);
    g.beginFill(0xFFFFFF, 0.25);
    g.drawRect(1, 0, 1, unscaledHeight);
    g.endFill();
  }
}
}
