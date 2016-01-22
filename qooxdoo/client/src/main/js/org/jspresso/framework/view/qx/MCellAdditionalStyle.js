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

qx.Mixin.define("org.jspresso.framework.view.qx.MCellAdditionalStyle", {

  members: {
    __attributes: null,

    setAdditionalAttributes: function (attrs) {
      this.__attributes = attrs;
    },

    _getAdditionalCellStyle: function (cellInfo) {
      var styleString = ["padding-top:" + (cellInfo.styleHeight / 5) + "px;"];
      if (this.__attributes) {
        for (var key in this.__attributes) {
          if (key == "backgroundIndex" || key == "foregroundIndex") {
            if (!cellInfo.selected) {
              //noinspection JSUnfilteredForInLoop
              var color = org.jspresso.framework.view.qx.DefaultQxViewFactory._hexColorToQxColor(
                  cellInfo.rowData.getChildren().getItem(this.__attributes[key]).getValue());
              if (color) {
                if (key == "backgroundIndex") {
                  styleString.push("background-color:", color, ";");
                } else {
                  styleString.push("color:", color, ";");
                }
              }
            }
          } else if (key == "fontIndex") {
            //noinspection JSUnfilteredForInLoop
            var rFont = cellInfo.rowData.getChildren().getItem(this.__attributes[key]).getValue();
            if (rFont) {
              if (rFont.getItalic()) {
                styleString.push("font-style:italic;");
              }
              if (rFont.getBold()) {
                styleString.push("font-weight:bold;");
              }
              if (rFont.getName()) {
                styleString.push("font-family:", rFont.getName(), ";");
              }
              if (rFont.getSize() > 0) {
                styleString.push("font-size:", rFont.getSize(), "px;");
              }
            }
          } else {
            //noinspection JSUnfilteredForInLoop
            if (this.__attributes[key]) {
              if (!cellInfo.selected || !("background-color" == key || "color" == key)) {
                //noinspection JSUnfilteredForInLoop
                styleString.push(key, ":", this.__attributes[key], ";");
              }
            }
          }
        }
      }
      return styleString.join("");
    }
  }
});
