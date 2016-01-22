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

import flash.display.Graphics;

import mx.skins.halo.DataGridHeaderSeparator;

public class DataGridHeaderSeparator extends mx.skins.halo.DataGridHeaderSeparator {

  public function DataGridHeaderSeparator() {
    super();
  }

  override public function get measuredWidth():Number {
    return 1;
  }

  override protected function updateDisplayList(w:Number, h:Number):void {

    super.updateDisplayList(w, h);

    var g:Graphics = this.graphics;
    g.clear();

    g.lineStyle(1, getStyle("verticalGridLineColor"));
    g.moveTo(1.5, h * 2 / 3);
    g.lineTo(1.5, h - 1);
  }
}
}


