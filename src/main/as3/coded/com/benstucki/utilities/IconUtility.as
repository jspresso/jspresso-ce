/*********************************************************************************************************************************
 
 Copyright (c) 2007 Ben Stucki
 
 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the
 Software is furnished to do so, subject to the following conditions:
 
 The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 
*********************************************************************************************************************************/
package com.benstucki.utilities
{
	import flash.display.BitmapData;
	import flash.display.Loader;
	import flash.display.LoaderInfo;
	import flash.events.Event;
	import flash.geom.Matrix;
	import flash.net.URLRequest;
	import flash.system.LoaderContext;
	import flash.utils.Dictionary;
	
	import mx.containers.Panel;
	import mx.controls.Alert;
	import mx.core.BitmapAsset;
	import mx.core.UIComponent;

  import mx.controls.alertClasses.AlertForm;
  	
	/**
	 * Provides a workaround for using run-time loaded graphics in styles and properties which require a Class reference
	 */
	public class IconUtility extends BitmapAsset
	{
		
		private static var dictionary:Dictionary;
		
		/**
		 * Used to associate run-time graphics with a target
		 * @param target A reference to the component associated with this icon
		 * @param source A url to a JPG, PNG or GIF file you wish to be loaded and displayed
		 * @param width Defines the width of the graphic when displayed
		 * @param height Defines the height of the graphic when displayed
		 * @return A reference to the IconUtility class which may be treated as a BitmapAsset
		 * @example &lt;mx:Button id="button" icon="{IconUtility.getClass(button, 'http://www.yourdomain.com/images/test.jpg')}" /&gt;
		 */
		public static function getClass( target:UIComponent, source:String, width:Number = NaN, height:Number = NaN ):Class {
			if(!dictionary) {
				dictionary = new Dictionary(false);
			}
			//if(source is String) {
				var loader:Loader = new Loader();
				loader.load(new URLRequest(source as String), new LoaderContext(true));
				//source = loader;
			//}
			dictionary[target] = { source:loader, width:width, height:height };
			return IconUtility;
		}
		
		/**
		 * @private
		 */
		public function IconUtility():void {
			addEventListener(Event.ADDED, addedHandler, false, 0, true)
		}
		
		private function addedHandler(event:Event):void {
			if(parent) {
//				if(parent is AccordionHeader) {
//					var header:AccordionHeader = parent as AccordionHeader;
//					getData(header.data);
//				} else if(parent is Tab) {
//					var tab:Tab = parent as Tab;
//					getData(tab.data);
//				} else {
       if(parent is AlertForm) {
         getData(parent);
       } else if(parent.parent is Panel) {
         getData(parent.parent);
       } else {
         getData(parent);
       }
			}
		}
		
		private function getData(object:Object):void {
			var data:Object = dictionary[object];
			if(data) {
				var source:Object = data.source;
				if(data.width > 0 && data.height > 0) {
					bitmapData = new BitmapData(data.width, data.height, true, 0x00FFFFFF);
				}
				if(source is Loader) {
					var loader:Loader = source as Loader;
					if(!loader.content) {
						loader.contentLoaderInfo.addEventListener(Event.COMPLETE, completeHandler, false, 0, true);
					} else {
						displayLoader(loader);
					}
				}
			}
		}
		
		private function displayLoader( loader:Loader ):void {
			if(!bitmapData) {
				bitmapData = new BitmapData(loader.content.width, loader.content.height, true, 0x00FFFFFF);
			}
			bitmapData.draw(loader, new Matrix(bitmapData.width/loader.width, 0, 0, bitmapData.height/loader.height, 0, 0));
			if(parent is UIComponent) {
				var component:UIComponent = parent as UIComponent;
				component.invalidateSize();
			}
		}
		
		private function completeHandler(event:Event):void {
			if(event && event.target && event.target is LoaderInfo) {
				displayLoader(event.target.loader as Loader);
			}
		}
		
	}
}