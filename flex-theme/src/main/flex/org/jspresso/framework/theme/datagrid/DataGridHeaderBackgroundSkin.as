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
package org.jspresso.framework.theme.datagrid {

import flash.display.GradientType;
import flash.display.Graphics;
import flash.geom.Matrix;

import mx.skins.halo.DataGridHeaderBackgroundSkin;

public class DataGridHeaderBackgroundSkin extends mx.skins.halo.DataGridHeaderBackgroundSkin {
  public function DataGridHeaderBackgroundSkin() {
    super();
  }

  override protected function updateDisplayList(w:Number, h:Number):void {
    super.updateDisplayList(w, h);

    var g:Graphics = this.graphics;
    var m:Matrix = new Matrix();
    m.createGradientBox(w, h, Math.PI / 2);

    g.clear();
    g.beginGradientFill(GradientType.LINEAR, [0xFFFFFF, 0xF0F0F0], [1.0, 1.0], [0, 255], m);
    g.drawRect(0, 0, w, h);
    // stroke
    g.beginFill(0xDADADA, 1.0);
    g.drawRect(0, h - 1, w, 1);
    g.endFill();
  }
}
}
