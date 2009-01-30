package org.jspresso.framework.view.flex {
  import mx.controls.Image;
  import mx.controls.menuClasses.MenuBarItem;
  import mx.core.IFlexDisplayObject;
  
  import org.jspresso.framework.gui.remote.RIcon;

  public class RIconMenuBarItem extends MenuBarItem {

		private var _image:Image;

		public function RIconMenuBarItem() {
		  _image = new Image();
			addChild(_image);
		}

  	override public function set data(value:Object):void {
  	  if(value) {
  	    var rIcon:RIcon = value["rIcon"];
  	    if(rIcon) {
  	      _image.source = DefaultFlexViewFactory.computeUrl(rIcon.imageUrlSpec);
  	    }
  	  }
  	  super.data = value;
  	}

		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
		  if(icon) {
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