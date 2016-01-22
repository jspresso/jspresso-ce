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
package org.jspresso.framework.flex.components {

import flash.display.GradientType;
import flash.display.Graphics;
import flash.display.Sprite;

import mx.controls.listClasses.IListItemRenderer;

import org.jspresso.framework.view.flex.SelectionTrackingTree;

public class JsoTree extends SelectionTrackingTree {
  public function JsoTree() {
    super();
  }

  override protected function drawHighlightIndicator(indicator:Sprite, x:Number, y:Number, width:Number, height:Number,
                                                     color:uint, itemRenderer:IListItemRenderer):void {

    super.drawHighlightIndicator(indicator, x, y, width, height, color, itemRenderer);

    var g:Graphics = indicator.graphics;
    g.clear();
    if (isItemSelected(itemRenderer.data)) {
      g.beginGradientFill(GradientType.LINEAR, [0xE5522D, 0xF88647], [1.0, 1.0], [0, 255]);
    } else {
      g.beginFill(color);
    }
    g.moveTo(0, 0);
    g.lineTo(unscaledWidth - 10, 0);
    g.lineTo(unscaledWidth, height * 0.5);
    g.lineTo(unscaledWidth - 10, height);
    g.lineTo(0, height);
    g.lineTo(0, 0);
    g.endFill();
  }

  override protected function drawSelectionIndicator(indicator:Sprite, x:Number, y:Number, width:Number, height:Number,
                                                     color:uint, itemRenderer:IListItemRenderer):void {
    super.drawSelectionIndicator(indicator, x, y, width, height, color, itemRenderer);

    var g:Graphics = indicator.graphics;
    g.clear();
    g.beginGradientFill(GradientType.LINEAR, [0xE5522D, 0xF88647], [1.0, 1.0], [0, 255]);
    g.moveTo(0, 0);
    g.lineTo(unscaledWidth - 10, 0);
    g.lineTo(unscaledWidth, height * 0.5);
    g.lineTo(unscaledWidth - 10, height);
    g.lineTo(0, height);
    g.lineTo(0, 0);
    g.endFill();
  }
}
}
