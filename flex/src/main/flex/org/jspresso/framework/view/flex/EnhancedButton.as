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
