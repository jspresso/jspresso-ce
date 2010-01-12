/**
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 */

package org.jspresso.framework.view.flex {
  import mx.controls.Image;
  import mx.controls.listClasses.BaseListData;
  
  import org.jspresso.framework.gui.remote.RIcon;

  public class EnumerationDgItemRenderer extends RemoteValueDgItemRenderer  {

		private var _image:Image;
		
		private var _values:Array;
		private var _labels:Array;
		private var _icons:Array;
		private var _iconTemplate:Class;

		public function EnumerationDgItemRenderer() {
		  _image = new Image();
			addChild(_image);
		}

    public function set values(value:Array):void {
      _values = value;
    }

    public function set labels(value:Array):void {
      _labels = value;
    }

    public function set icons(value:Array):void {
      _icons = value;
    }

    public function set iconTemplate(value:Class):void {
      _iconTemplate = value;
    }

  	override public function set listData(value:BaseListData):void {
  	  value.label = _labels[value.rowIndex];
  	  super.listData = value;
  	  listDataIcon = _iconTemplate;
  	  computeIcon(value.rowIndex);
  	}

  	override protected function computeLabel(cellValue:Object):String {
  	  var valIndex:int = _values.indexOf(cellValue);
  	  computeIcon(valIndex);
	    if(cellValue != null) {
  	    return _labels[valIndex];
  	  } else {
        return super.computeLabel(cellValue);
      }
  	}
  	
  	private function computeIcon(iconIndex:int):void {
			var _selectedIcon:RIcon = _icons[iconIndex] as RIcon;
			if(_selectedIcon != null) {
			  _image.source = _selectedIcon.imageUrlSpec;
			} else {
			  _image.source = null;
			}
  	}

		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			_image.x = icon.x;
			_image.y = icon.y;
			_image.width = icon.width;
			_image.height = icon.height;
			icon.visible = false;
		}

  	override protected function refresh(cellLabel:Object):void {
      super.refresh(cellLabel);
      invalidateDisplayList();
  	}
  }
}