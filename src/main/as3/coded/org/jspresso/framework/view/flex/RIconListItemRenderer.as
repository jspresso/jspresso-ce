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