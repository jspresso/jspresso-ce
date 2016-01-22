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

import flash.events.MouseEvent;
import flash.filters.BitmapFilterQuality;
import flash.filters.GlowFilter;

import mx.controls.Button;

import org.jspresso.framework.theme.utils.ThemeFilterUtils;

import org.jspresso.framework.theme.utils.ThemeFilterUtils;

public class TopApplicationButton extends Button {
  private var _glowFilter:GlowFilter;

  public function TopApplicationButton() {
    super();

    _glowFilter = new GlowFilter(0xFFFFFF, 0.75, 2, 2, 1, BitmapFilterQuality.HIGH);
  }

  override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
    super.updateDisplayList(unscaledWidth, unscaledHeight);

    textField.y += 2;
  }

  override protected function rollOverHandler(event:MouseEvent):void {
    super.rollOverHandler(event);

    this.filters = [_glowFilter, ThemeFilterUtils.onePixelDarkShadow()];
  }

  override protected function rollOutHandler(event:MouseEvent):void {
    super.rollOutHandler(event);

    this.filters = [ThemeFilterUtils.onePixelDarkShadow()];
  }
}
}
