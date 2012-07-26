/**
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 */

qx.Mixin.define("org.jspresso.framework.view.qx.MCellAdditionalStyle",
{
  
  members :
  {
    __attributes : null,
    
    setAdditionalAttributes : function(attrs) {
      this.__attributes = attrs;
    },
    
    _getAdditionalCellStyle : function(cellInfo) {
      if(this.__attributes) {
        var styleString = [];
        for(var key in this.__attributes) {
          if(key == "backgroundIndex" || key == "foregroundIndex") {
            var color = org.jspresso.framework.view.qx.DefaultQxViewFactory
                ._hexColorToQxColor(cellInfo.rowData.getChildren().getItem(this.__attributes[key]).getValue());
            if(color) {
              if(key == "backgroundIndex") {
                styleString.push("background-color", ":", color, ";");
              } else {
                styleString.push("color", ":", color, ";");
              }
            }
          } else if (this.__attributes[key]) {
            if(!cellInfo.selected ||
              ( cellInfo.selected && !("background-color" == key || "color" == key))) {
              styleString.push(key, ":", this.__attributes[key], ";");
            }
          }
        }
        return styleString.join("");
      }
      return "";
    }
  }
});
