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
  extend: org.jspresso.framework.view.qx.FormattedTableCellRenderer,

  construct: function (table, peerRegistry, labels, icons) {
    this.base(arguments, table, peerRegistry);
    this.__labels = labels;
    this.__icons = icons;
  },


  members: {
    __labels: null,
    __icons: null,

    _formatValue: function (cellInfo) {
      return  this.__labels[cellInfo.value] || "";
    },

    _getContentHtml: function (cellInfo) {
      var cellViewState = null;
      if (cellInfo.rowData instanceof org.jspresso.framework.state.remote.RemoteCompositeValueState) {
        cellViewState = cellInfo.rowData.getChildren().getItem(cellInfo.col + 1);
      }
      if (cellViewState && !cellViewState.getReadable()) {
        return "";
      }
      return this.__getImgHtml(cellInfo) + (this.__labels[cellInfo.value] || "");/*this.base(arguments, cellInfo);*/
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
        if (this._table.getRowHeight() < h) {
          this._table.setRowHeight(h + 4);
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
