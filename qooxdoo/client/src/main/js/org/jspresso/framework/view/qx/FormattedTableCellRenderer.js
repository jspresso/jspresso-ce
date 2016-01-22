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

qx.Class.define("org.jspresso.framework.view.qx.FormattedTableCellRenderer", {
  extend: qx.ui.table.cellrenderer.Default,
  include: [org.jspresso.framework.view.qx.MCellAdditionalStyle],

  construct: function (table, format) {
    this.base(arguments);
    this.__table = table;
    this.__format = format;
  },

  members: {
    __format: null,
    __table: null,
    __action: null,

    _formatValue: function (cellInfo) {
      if (this.__format && cellInfo.value) {
        return this.__format.format(cellInfo.value);
      }
      return this.base(arguments, cellInfo);
    },

    _getContentHtml: function (cellInfo) {
      if (!org.jspresso.framework.util.html.HtmlUtil.isHtml(cellInfo.value) && (typeof(cellInfo.value) == "string"
          || cellInfo.value instanceof String)) {
        if (cellInfo.value.indexOf("\n") >= 0) {
          cellInfo.value = org.jspresso.framework.util.html.HtmlUtil.toHtml(
              org.jspresso.framework.util.html.HtmlUtil.replaceNewlines(cellInfo.value)
          );
        }
      }
      if (org.jspresso.framework.util.html.HtmlUtil.isHtml(cellInfo.value) && this.__table.getRowHeight() < 150) {
        var contentHeight = qx.bom.Label.getHtmlSize(cellInfo.value).height;
        if (contentHeight > 150) {
          contentHeight = 150;
        }
        if (this.__table.getRowHeight() < contentHeight) {
          this.__table.setRowHeight(contentHeight + 4);
        }
      }
      if (this.__action) {
        // Action is handled in the cell click event
        return "<u>" + this.base(arguments, cellInfo) + "</u>";
      } else {
        if (org.jspresso.framework.util.html.HtmlUtil.isHtml(cellInfo.value)) {
          return cellInfo.value;
        }
        return this.base(arguments, cellInfo);
      }
    },

    setAction: function (action) {
      this.__action = action;
    },

    getAction: function () {
      return this.__action;
    },

    _getCellStyle: function (cellInfo) {
      var superStyle = this.base(arguments, cellInfo);
      return superStyle + this._getAdditionalCellStyle(cellInfo);
    }
  }
});
