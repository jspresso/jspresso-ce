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

import mx.collections.ArrayCollection;
import mx.controls.ComboBox;
import mx.controls.Image;

import org.jspresso.framework.gui.remote.RIcon;

public class RIconComboBox extends ComboBox {

  private var iconImage:Image;
  private var _labels:Array;
  private var _icons:Array;
  private var _rIcon:RIcon;
  private var _showIcon:Boolean;

  public function RIconComboBox() {
    iconImage = new CachedImage();
    addChild(iconImage);
  }

  public function set labels(value:Array):void {
    _labels = value;
  }

  public function set icons(value:Array):void {
    _icons = value;
    if (value) {
      var iconWidth:Number = 0;
      var iconHeight:Number = 0;
      for (var i:int = 0; i < value.length; i++) {
        var rIcon:RIcon = value[i];
        if (rIcon && rIcon.dimension) {
          if (rIcon.dimension.width > iconWidth) {
            iconWidth = rIcon.dimension.width;
          }
          if (rIcon.dimension.height > iconHeight) {
            iconHeight = rIcon.dimension.height;
          }
        }
      }
      iconImage.width = iconWidth;
      iconImage.height = iconHeight;
    }
  }

  public function set showIcon(value:Boolean):void {
    _showIcon = value;
  }

  public function get showIcon():Boolean {
    return _showIcon;
  }

  override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
    super.updateDisplayList(unscaledWidth, unscaledHeight);
    if (_showIcon) {
      if (_rIcon != null) {
        iconImage.source = _rIcon.imageUrlSpec;
      } else {
        iconImage.source = null;
      }
      var cornerRadius:Number = getStyle("cornerRadius");
      iconImage.x = cornerRadius + 2;
      iconImage.y = (height - iconImage.height) / 2;
      textInput.x = iconImage.width + cornerRadius;
    }
  }

  override public function set selectedItem(sItem:Object):void {
    super.selectedItem = sItem;
    if (dataProvider != null) {
      var index:int = 0;
      if (sItem != null) {
        index = (dataProvider as ArrayCollection).getItemIndex(sItem);
      } else {
        index = (dataProvider as ArrayCollection).getItemIndex("");
      }
      updateTextAndIcon(index);
    }
  }

  override public function set selectedIndex(value:int):void {
    super.selectedIndex = value;
    updateTextAndIcon(value);
  }

  private function updateTextAndIcon(index:int):void {
    if (index != -1) {
      if (_icons) {
        _rIcon = _icons[index];
      }
      if (_labels && text != _labels[index]) {
        text = _labels[index];
      }
    } else {
      _rIcon = null;
      text = "";
    }
  }

  override protected function measure():void {
    super.measure();
    if (_rIcon && _rIcon.dimension) {
      if (_rIcon.dimension.width > measuredWidth) {
        measuredWidth += _rIcon.dimension.width;
      }
      var cornerRadius:Number = getStyle("cornerRadius");
      var borderThickness:Number = getStyle("borderThickness");
      measuredHeight = Math.max(_rIcon.dimension.height + (cornerRadius + borderThickness) * 2, measuredHeight);
    }
  }
}
}
