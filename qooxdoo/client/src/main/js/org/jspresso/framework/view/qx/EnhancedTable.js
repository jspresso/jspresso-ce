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

qx.Class.define("org.jspresso.framework.view.qx.EnhancedTable", {
  extend: qx.ui.table.Table,

  construct: function (tableModel, custom) {
    this.base(arguments, tableModel, custom);

    var tt = new qx.ui.tooltip.ToolTip("");
    tt.setRich(true);
    this.setToolTip(tt);
    this.addListener("pointermove", this._refineToolTip, this);
  },

  members: {
    _refineToolTip: function (e) {
      var v = null;
      var pageX = e.getDocumentLeft();
      var pageY = e.getDocumentTop();
      var sc = this.getTablePaneScrollerAtPageX(pageX);
      if (sc != null) {
        var tm = this.getTableModel();
        if (tm != null) {
          var row = sc._getRowForPagePos(pageX, pageY);
          var col = sc._getColumnForPageX(pageX);
          /**/
          if (row != null && col != null && row >= 0 && col >= 0) {
            try {
              v = tm.getToolTip(col, row);
            } catch (az) {
              v = "";
            }
          }
        }
      }
      /** @type {qx.ui.tooltip.ToolTip } */
      var tt = this.getToolTip();
      if (v != null && v != "") {
        tt.setLabel(v);
        if (tt.isVisible()) {
          tt.placeToPointer(e);
        }
      } else {
        tt.hide();
      }
    },

    _onKeyPress: function (evt) {
      if (evt.getKeyIdentifier() == "Tab") {
        var newIdentifier;
        if (evt.isShiftPressed()) {
          newIdentifier = "Left";
        } else {
          newIdentifier = "Right";
        }
        evt.init(evt.getNativeEvent(), evt.getTarget(), newIdentifier);
      } else if (/*!evt.getModifiers() &&*/ evt.isPrintable()) {
        this.startEditing()
      }
      this.base(arguments, evt);
    },

    _onSelectionChanged: function (evt) {
      this.base(arguments, evt);
      var selectedRanges = this.getSelectionModel().getSelectedRanges();
      if (selectedRanges) {
        for (var i = 0; i < selectedRanges.length; i++) {
          var data = {
            firstRow: selectedRanges[i].minIndex,
            lastRow: selectedRanges[i].maxIndex,
            firstColumn: 0,
            lastColumn: this.getTableModel().getColumnCount() - 1
          };
          // For the cell to repaint itself and provide different programmatic rendering based on selection.
          this.getTableModel().fireDataEvent("dataChanged", data);
        }
      }
    }

  }
});
