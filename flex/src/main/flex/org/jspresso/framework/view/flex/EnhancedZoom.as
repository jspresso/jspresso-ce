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

import flash.events.Event;
import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.controls.Alert;

import org.jspresso.framework.action.IActionHandler;

import org.openscales.core.control.Zoom;
import org.openscales.core.control.ui.Button;
import org.openscales.geometry.basetypes.Location;
import org.openscales.geometry.basetypes.Pixel;

public class EnhancedZoom extends Zoom {

  private var _actionHandler:IActionHandler;

  public function EnhancedZoom(actionHandler:IActionHandler, position:Pixel = null) {
    this._actionHandler = actionHandler;
    BindingUtils.bindSetter(updateMapPosition, _actionHandler, "userGeoLocation", true);
    super(position);
  }

  override public function click(evt:Event):void {
    if (!(evt.type == MouseEvent.CLICK)) return;

    var btn:Button = evt.currentTarget as Button;
    // TODO : refactor to change the zoom behaviour
    switch (btn.name) {
      case "zoomin":
        this.map.zoomIn();
        break;
      case "zoomout":
        this.map.zoomOut();
        break;
      case "zoomworld":
        this._actionHandler.queryUserGeoLocation();
        break;
    }
  }

  protected function updateMapPosition(geoLoc:Object):void {
    if (geoLoc && geoLoc.coords) {
      map.center = new Location(geoLoc.coords.longitude, geoLoc.coords.latitude/*, "EPSG:4326"*/);
    }
  }
}
}
