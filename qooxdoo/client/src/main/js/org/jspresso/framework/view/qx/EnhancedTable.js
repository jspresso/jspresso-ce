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
    tt.addListener("changeVisibility", function (e) {
      if (e.getData() == "visible") {
        var label = tt.getLabel();
        if (label == null || label == "") {
          tt.hide();
        }
      }
    }, this);
    tt.setRich(true);
    this.setToolTip(tt);
    this.addListener("pointermove", this._refineToolTip, this);
  },

  properties: {
    lastFocusedRow: {
      check: "Integer",
      nullable: true
    },
    lastFocusedColumn: {
      check: "Integer",
      nullable: true
    }
  },

  members: {
    __lastHoveredCol: null,
    __lastHoveredRow: null,
    _refineToolTip: function (e) {
      var pageX = e.getDocumentLeft();
      var pageY = e.getDocumentTop();
      var sc = this.getTablePaneScrollerAtPageX(pageX);
      if (sc != null) {
        var tm = this.getTableModel();
        if (tm != null) {
          var row = sc._getRowForPagePos(pageX, pageY);
          var col = sc._getColumnForPageX(pageX);
          if (row != this.__lastHoveredRow || col != this.__lastHoveredCol)  {
            this.__lastHoveredRow = row;
            this.__lastHoveredCol = col;
            //this.debug("Coords [" + row + ", " + col + "]");
            var label = null;
            if (row != null && col != null && row >= 0 && col >= 0) {
              try {
                label = tm.getToolTip(col, row);
              } catch (az) {
                label = null;
              }
            }
            /** @type {qx.ui.tooltip.ToolTip } */
            var tt = this.getToolTip();
            tt.setLabel(label);
          }
        }
      }
    },

    _onKeyPress: function (evt) {
      var keyIdentifier = evt.getKeyIdentifier();
      if (keyIdentifier == "Tab") {
        var newIdentifier;
        if (evt.isShiftPressed()) {
          newIdentifier = "Left";
        } else {
          newIdentifier = "Right";
        }
        evt.init(evt.getNativeEvent(), evt.getTarget(), newIdentifier);
      } else if (!evt.isCtrlOrCommandPressed() && evt.isPrintable()) {
        this.startEditing()
      }
      this.base(arguments, evt);
      if (keyIdentifier == "Left" || keyIdentifier == "Right") {
        // In order to notify column change in same row
        this.getSelectionModel()._fireChangeSelection();
      }
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
    },

    isCellEditable: function (row, column) {
      if (row >= 0 && column >= 0) {
        var tableModel = this.getTableModel();
        var cellEditable = tableModel.isColumnEditable(column);
        if (cellEditable) {
          var rowState = tableModel.getRowData(row);
          /* Editability is only determined at cell level
          if (!rowState.getWritable()) {
            cellEditable = false;
          }
          */
          if (/*cellEditable*/ rowState) {
            /** @type {org.jspresso.framework.state.remote.RemoteValueState} */
            var cellState = rowState.getChildren().getItem(column + 1);
            if (!cellState.getWritable() || !cellState.getReadable()) {
              cellEditable = false;
            }
          }
        }
      }
      return cellEditable;
    }

  }
});
