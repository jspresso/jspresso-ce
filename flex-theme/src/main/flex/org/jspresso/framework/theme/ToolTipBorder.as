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

import mx.skins.halo.ToolTipBorder;

public class ToolTipBorder extends mx.skins.halo.ToolTipBorder {
  public function ToolTipBorder() {
    super();
  }

  override protected function updateDisplayList(w:Number, h:Number):void {
    super.updateDisplayList(w, h);

    var backgroundColor:uint = this.getStyle("backgroundColor");
    var cornerRadius:Number = this.getStyle("cornerRadius");

    var g:Graphics = this.graphics;

    g.clear();

    g.beginFill(backgroundColor, 1.0);
    g.drawRoundRect(3, 1, w - 6, h - 4, cornerRadius, backgroundColor);

    g.endFill();
  }
}
}
