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

import mx.skins.halo.DataGridSortArrow;

public class DataGridSortArrow extends mx.skins.halo.DataGridSortArrow {
  public function DataGridSortArrow() {
    super();
  }

  override protected function updateDisplayList(w:Number, h:Number):void {
    super.updateDisplayList(w, h);

    var g:Graphics = graphics;

    g.clear();
    g.beginFill(0x3D3D3D, 1.0);
    g.moveTo(0, 0);
    g.lineTo(w, 0);
    g.lineTo(w / 2, h);
    g.lineTo(0, 0);
    g.endFill();
  }
}

}
