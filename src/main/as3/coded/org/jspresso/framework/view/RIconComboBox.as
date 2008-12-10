package org.jspresso.framework.view
{
  import mx.collections.ArrayCollection;
  import mx.controls.ComboBox;
  import mx.controls.Image;
  
  import org.jspresso.framework.gui.remote.RIcon;
    
  public class RIconComboBox extends ComboBox {
      
    private var iconImage:Image;
		private var _labels:Array;
		private var _icons:Array;
    private var _rIcon:RIcon;
    
    public function RIconComboBox() {
      iconImage = new Image();
      addChild(iconImage);
    }
    
    public function set labels(value:Array):void {
      _labels = value;
    }

    public function set icons(value:Array):void {
      _icons = value;
    }

    override protected function updateDisplayList(unscaledWidth:Number,
                                                  unscaledHeight:Number):void {
      super.updateDisplayList(unscaledWidth, unscaledHeight);
      iconImage.width = textInput.height - getStyle("cornerRadius");
      iconImage.height = iconImage.width;
      iconImage.x = getStyle("cornerRadius");
      iconImage.y = (height - iconImage.height)/2;
      textInput.x = iconImage.width + getStyle("cornerRadius");
    }
    
    public function set rIcon(_icon:RIcon):void {
      if(_icon != _rIcon) {
        if(_icon != null) {
          _rIcon = _icon;
          iconImage.source = DefaultFlexViewFactory.computeUrl(_icon.imageUrlSpec);
        } else {
          iconImage.source = null;
        }
      }
    }
    
    override public function set measuredWidth(value:Number):void {
      super.measuredWidth = value + (iconImage.width + getStyle("cornerRadius"));
    }
    
    override public function set selectedItem(sItem:Object):void {
      super.selectedItem = sItem;
      if(dataProvider != null && sItem != null) {
        var index:int = (dataProvider as ArrayCollection).getItemIndex(sItem);
        updateTextAndIcon(index);
      }
    }
    
    override public function set selectedIndex(value:int):void {
      super.selectedIndex = value;
      updateTextAndIcon(value);
    }
    
    private function updateTextAndIcon(index:int):void {
        if(index != -1 && _icons != null) {
          rIcon = _icons[index];
          if(text != _labels[index]) {
            text = _labels[index];
          }
        }
    }
  }
}