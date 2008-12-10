package org.jspresso.framework.view
{
  import mx.controls.Image;
  import mx.controls.listClasses.BaseListData;
  
  import org.jspresso.framework.gui.remote.RIcon;
  import org.jspresso.framework.state.remote.RemoteValueState;

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
  	}

  	override protected function computeLabel(cellValueState:RemoteValueState):String {
	    if(cellValueState.value != null) {
  	    return _labels[_values.indexOf(cellValueState.value)];
  	  } else {
        return super.computeLabel(cellValueState);
      }
  	}

		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			_image.x = icon.x;
			_image.y = icon.y;
			_image.width = icon.width;
			_image.height = icon.height;
			
			var _selectedIcon:RIcon = _icons[_labels.indexOf(listData.label)] as RIcon;
			if(_selectedIcon != null) {
			  _image.source = DefaultFlexViewFactory.computeUrl(_selectedIcon.imageUrlSpec);
			}
			icon.visible = false;
		}

  	override protected function refresh(cellLabel:Object):void {
      super.refresh(cellLabel);
      invalidateDisplayList();
  	}
  }
}