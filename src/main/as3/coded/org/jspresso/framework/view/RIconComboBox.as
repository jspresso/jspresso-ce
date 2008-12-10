package org.jspresso.framework.view
{
  import mx.controls.ComboBox;
  import mx.controls.Image;
  
  import org.jspresso.framework.gui.remote.RIcon;
    
  public class RIconComboBox extends ComboBox {
      
    private var iconImage:Image;
    private var _rIcon:RIcon;
    
    public function RIconComboBox() {
      iconImage = new Image();
      addChild(iconImage);
    }
    
    override protected function updateDisplayList(unscaledWidth:Number,
                                                  unscaledHeight:Number):void {
      super.updateDisplayList(unscaledWidth, unscaledHeight);
      iconImage.x = getStyle("cornerRadius");
      iconImage.y = (height - iconImage.height)/2;
      textInput.x = iconImage.width + getStyle("cornerRadius");
    }
    
    public function set rIcon(_icon:RIcon):void {
      if(_icon != _rIcon) {
        if(_icon != null) {
          _rIcon = _icon;
          iconImage.width = _icon.width;
          iconImage.height = _icon.height;
          iconImage.source = DefaultFlexViewFactory.computeUrl(_icon.imageUrlSpec);
        } else {
          iconImage.width = 0;
          iconImage.height = 0;
          iconImage.source = null;
        }
      }
    }
    
    override public function set measuredWidth(value:Number):void {
      super.measuredWidth = value + (iconImage.width + getStyle("cornerRadius"));
    }
  }
}