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

qx.Class.define("org.jspresso.framework.view.qx.EnumerationTableCellRenderer", {
  extend: qx.ui.table.cellrenderer.Default,
  include: [org.jspresso.framework.view.qx.MCellAdditionalStyle],

  construct: function (table, labels, icons) {
    this.base(arguments);
    this.__table = table;
    this.__labels = labels;
    this.__icons = icons;
  },


  members: {
    __table: null,
    __labels: null,
    __icons: null,

    _getContentHtml: function (cellInfo) {
      return this.__getImgHtml(cellInfo) + (this.__labels[cellInfo.value] || "");
    },

    __getImgHtml: function (cellInfo) {
      var rIcon = this.__icons[cellInfo.value];
      if (rIcon) {
        var w = 12;
        var h = 12;
        if (rIcon.getDimension()) {
          w = rIcon.getDimension().getWidth();
          h = rIcon.getDimension().getHeight();
        }
        var css = qx.bom.element.Style.compile({
          position: "relative",
          width: w + "px",
          height: h + "px",
          "margin-right": "4px",
          "margin-left": "-1px",
          "vertical-align": "middle",
          "margin-top": cellInfo.styleHeight / (-5) + "px"
        });
        if (this.__table.getRowHeight() < h) {
          this.__table.setRowHeight(h + 4);
        }
        return '<img src="' + rIcon.getImageUrlSpec() + '" style="' + css + '"/>';
      }
      return "";
    },

    _getCellStyle: function (cellInfo) {
      var superStyle = this.base(arguments, cellInfo);
      return superStyle + this._getAdditionalCellStyle(cellInfo);
    }
  }
});
