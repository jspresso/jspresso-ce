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
package org.jspresso.framework.theme.buttons {

public class ArrowNextButtonSkin extends NoBorderButtonSkin {
  public function ArrowNextButtonSkin() {
    super();
  }

  override protected function updateDisplayList(w:Number, h:Number):void {
    super.updateDisplayList(w, h);

    var arrowWidth:uint = 5;
    var arrowHeight:uint = 7;
    var startXPoint:Number = (w - arrowWidth) * 0.5;
    var startYPoint:Number = (h - arrowHeight) * 0.5;

    graphics.lineStyle(1.0, 0x66767B, 1.0);
    graphics.moveTo(startXPoint, startYPoint);
    graphics.lineTo(startXPoint + arrowWidth, startYPoint + (arrowHeight * 0.5));
    graphics.lineTo(startXPoint, startYPoint + arrowHeight);
  }
}
}
