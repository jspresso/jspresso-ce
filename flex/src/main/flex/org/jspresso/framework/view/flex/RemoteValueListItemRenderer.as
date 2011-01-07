/**
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
  
  import org.jspresso.framework.state.remote.RemoteCompositeValueState;

  public class RemoteValueListItemRenderer extends RemoteValueDgItemRenderer {

		private var image:Image;
		private var _iconTemplate:Class;

		public function RemoteValueListItemRenderer() {
		  image = new Image();
			addChild(image);
		}

    public function set iconTemplate(value:Class):void {
      _iconTemplate = value;
    }

  	override public function set listData(value:BaseListData):void {
  	  super.listData = value;
  	  listDataIcon = _iconTemplate;
  	}

		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			if(data is RemoteCompositeValueState && (data as RemoteCompositeValueState).iconImageUrl) {
				image.x = icon.x;
				image.y = icon.y;
				image.width = icon.width;
				image.height = icon.height;
				var iconImageUrl:String = (data as RemoteCompositeValueState).iconImageUrl;
				if(iconImageUrl) {
				  image.source = iconImageUrl;
				}
				image.visible = true;
				if(icon) {
				  icon.visible = false;
				}
			} else {
				image.visible = false;
				if(icon) {
				  icon.visible = true;
				}
			}
		}
  }
}