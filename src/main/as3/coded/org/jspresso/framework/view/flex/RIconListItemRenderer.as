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
  import mx.controls.listClasses.ListData;
  import mx.controls.listClasses.ListItemRenderer;
  
  import org.jspresso.framework.gui.remote.RIcon;

  public class RIconListItemRenderer extends ListItemRenderer  {

		private var _image:Image;
		
		private var _labels:Array;
		private var _icons:Array;
		private var _iconTemplate:Class;

		public function RIconListItemRenderer() {
		  _image = new Image();
			addChild(_image);
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
  	  (value as ListData).icon = _iconTemplate;
  	  super.listData = value;
  	}

		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			_image.x = icon.x;
			_image.y = icon.y;
			_image.width = icon.width;
			_image.height = icon.height;
			
			var _selectedIcon:RIcon = _icons[listData.rowIndex] as RIcon;
			if(_selectedIcon != null) {
			  _image.source = DefaultFlexViewFactory.computeUrl(_selectedIcon.imageUrlSpec);
			}
			icon.visible = false;
		}
  }
}