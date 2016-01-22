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

import flash.display.Bitmap;
import flash.events.Event;
import flash.system.ApplicationDomain;
import flash.system.LoaderContext;
import flash.system.Security;
import flash.system.SecurityDomain;
import flash.utils.Dictionary;

import mx.controls.Image;

public class CachedImage extends Image {
  static private var imageCache:Dictionary = new Dictionary(true);

  public function CachedImage() {
    super();

    // Maybe useful, was necessary for the first Flash 10.1 Beta (thanks Adobe ;-(
    if (Security.sandboxType != Security.LOCAL_TRUSTED) {
      var ctx:LoaderContext = new LoaderContext();

      ctx.checkPolicyFile = true;
      ctx.applicationDomain = ApplicationDomain.currentDomain;
      ctx.securityDomain = SecurityDomain.currentDomain;

      this.loaderContext = ctx;
    }

    this.addEventListener(Event.COMPLETE, onImageComplete);
  }

  private function onImageComplete(event:Event):void {
    var image:Image = event.target as Image;

    if (!imageCache.hasOwnProperty(image.source as String)) {
      imageCache[this.source] = Bitmap(this.content).bitmapData;
    }
  }

  override public function set source(value:Object):void {
    if (imageCache.hasOwnProperty(value as String)) {
      super.source = new Bitmap(imageCache[value], 'auto', true);
    } else {
      super.source = value;
    }
  }
}
}
