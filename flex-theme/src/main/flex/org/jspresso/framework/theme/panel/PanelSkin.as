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
package org.jspresso.framework.theme.panel {

import flash.display.Graphics;

import mx.skins.halo.PanelSkin;

public class PanelSkin extends mx.skins.halo.PanelSkin {
  public function PanelSkin() {
    super();
  }

  override protected function updateDisplayList(w:Number, h:Number):void {
    super.updateDisplayList(w, h);

    var backgroundColor:uint = getStyle("backgroundColor");
    var borderColor:uint = getStyle("borderColor");
    var cornerRadius:Number = getStyle("cornerRadius");
    var innerRadius:Number = Math.max(0, cornerRadius - 1);

    var headerHeight:Number = getStyle("headerHeight");

    var g:Graphics = this.graphics;

    g.clear();

    g.beginFill(borderColor, 1.0);
    g.drawRoundRect(0, headerHeight, w, h - headerHeight, cornerRadius, cornerRadius);
    g.beginFill(backgroundColor, 1.0);
    g.drawRoundRect(1, headerHeight + 1, w - 2, h - headerHeight - 2, innerRadius, innerRadius);

    g.endFill();
  }
}
}
