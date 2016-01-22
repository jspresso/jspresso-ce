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

import flash.display.BitmapData;
import flash.display.Loader;
import flash.display.LoaderInfo;
import flash.events.Event;
import flash.geom.Matrix;
import flash.net.URLRequest;
import flash.system.LoaderContext;
import flash.utils.Dictionary;

import mx.containers.Panel;
import mx.controls.alertClasses.AlertForm;
import mx.core.BitmapAsset;
import mx.core.UIComponent;

/**
 * Provides a workaround for using run-time loaded graphics in styles and properties which require a Class reference
 */
public class IconFactory extends BitmapAsset {

  private static var componentToLoaderMap:Dictionary;
  private static var bitmapCache:Dictionary;

  /**
   * Used to associate run-time graphics with a target
   * @param target A reference to the component associated with this icon
   * @param source A url to a JPG, PNG or GIF file you wish to be loaded and displayed
   * @param width Defines the width of the graphic when displayed
   * @param height Defines the height of the graphic when displayed
   * @return A reference to the IconUtility class which may be treated as a BitmapAsset
   * @example &lt;mx:Button id="button" icon="{IconUtility.getClass(button, 'http://www.yourdomain.com/images/test.jpg')}" /&gt;
   */
  public static function getClass(target:UIComponent, source:String, width:Number = NaN, height:Number = NaN):Class {
    if (!componentToLoaderMap) {
      componentToLoaderMap = new Dictionary(false);
    }
    if (!bitmapCache) {
      bitmapCache = new Dictionary(false);
    }

    var loader:Loader = bitmapCache[source];
    if (!loader) {
      loader = new Loader();
      loader.load(new URLRequest(source as String), new LoaderContext(true));
      bitmapCache[source] = loader;
    }
    componentToLoaderMap[target] = { source: loader, width: width, height: height };
    return IconFactory;
  }

  /**
   * @private
   */
  public function IconFactory():void {
    addEventListener(Event.ADDED, addedHandler, false, 0, true);
  }

  private function addedHandler(event:Event):void {
    if (parent) {
      if (parent is AlertForm) {
        getData(parent);
      } else if (parent.parent is Panel) {
        getData(parent.parent);
      } else {
        getData(parent);
      }
    }
  }

  private function getData(object:Object):void {
    var data:Object = componentToLoaderMap[object];
    if (data) {
      var source:Object = data.source;
      if (data.width > 0 && data.height > 0) {
        bitmapData = new BitmapData(data.width, data.height, true, 0x00FFFFFF);
      }
      if (source is Loader) {
        var loader:Loader = source as Loader;
        if (!loader.content) {
          loader.contentLoaderInfo.addEventListener(Event.COMPLETE, completeHandler, false, 0, true);
        } else {
          displayLoader(loader);
        }
      }
    }
  }

  private function displayLoader(loader:Loader):void {
    if (!bitmapData) {
      bitmapData = new BitmapData(loader.content.width, loader.content.height, true, 0x00FFFFFF);
    }
    bitmapData.draw(loader, new Matrix(bitmapData.width / loader.width, 0, 0, bitmapData.height / loader.height, 0, 0));
    if (parent is UIComponent) {
      var component:UIComponent = parent as UIComponent;
      component.invalidateSize();
    }
  }

  private function completeHandler(event:Event):void {
    if (event && event.target && event.target is LoaderInfo) {
      displayLoader(event.target.loader as Loader);
    }
  }

}
}
