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

import mx.controls.Image;
import mx.controls.listClasses.ListBase;

import org.jspresso.framework.theme.utils.ThemeFilterUtils;

import org.jspresso.framework.view.flex.RemoteValueTreeItemRenderer;

[Style(name="grayFactor", type="Number", inherit="no")]

public class JsoTreeItemRenderer extends RemoteValueTreeItemRenderer {

  public function JsoTreeItemRenderer() {
    super();
    this.percentWidth = 100;
  }

  override public function set data(value:Object):void {
    super.data = value;
  }

  override protected function commitProperties():void {
    super.commitProperties();

    icon.visible = false;

    if (label) {
      label.y += 1;
    }

    invalidateDisplayList();
  }

  override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
    super.updateDisplayList(unscaledWidth, unscaledHeight);

    var image:Image = getChildAt(0) as Image;
    var grayFactor:Number = getStyle("grayFactor");
    if (owner is ListBase && ListBase(owner).isItemSelected(data)) {
      if (image && grayFactor) {
        if (grayFactor > 0) {
          ThemeFilterUtils.ungray(image);
        } else {
          ThemeFilterUtils.gray(image, grayFactor);
        }
      }
      label.styleName = "treeJustBold";
    } else {
      if (image && grayFactor) {
        if (grayFactor > 0) {
          ThemeFilterUtils.gray(image, grayFactor);
        } else {
          ThemeFilterUtils.ungray(image);
        }
      }
      label.styleName = "treeJustNormal";
    }
  }
}
}
