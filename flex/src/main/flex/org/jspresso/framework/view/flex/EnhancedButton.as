package org.jspresso.framework.view.flex {

import mx.controls.Button;
import mx.core.IFlexDisplayObject;
import mx.core.mx_internal;

use namespace mx_internal;

public class EnhancedButton extends Button {

  override mx_internal function viewIconForPhase(tempIconName:String):IFlexDisplayObject {
    var icon:IFlexDisplayObject = super.viewIconForPhase(tempIconName);
    if (icon) {
      if (tempIconName == disabledIconName) {
        icon.alpha = 0.4;
      }
    }
    return icon;
  }
}
}
