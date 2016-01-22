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

package org.jspresso.framework.view.flex {

import mx.controls.Image;
import mx.controls.menuClasses.MenuBarItem;

import org.jspresso.framework.gui.remote.RIcon;

public class RIconMenuBarItem extends MenuBarItem {

  private var _image:Image;

  public function RIconMenuBarItem() {
    _image = new CachedImage();
    addChild(_image);
  }

  override public function set data(value:Object):void {
    if (value) {
      var rIcon:RIcon = value["rIcon"];
      if (rIcon) {
        _image.source = rIcon.imageUrlSpec;
      }
    }
    super.data = value;
  }

  override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
    super.updateDisplayList(unscaledWidth, unscaledHeight);
    if (icon) {
      _image.x = icon.x;
      _image.y = icon.y;
      _image.width = icon.width;
      _image.height = icon.height;

      icon.visible = false;
      _image.visible = true;
    } else {
      _image.visible = false;
    }
  }
}
}
