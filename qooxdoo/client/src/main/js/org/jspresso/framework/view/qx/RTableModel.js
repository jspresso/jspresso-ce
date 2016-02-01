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

//noinspection FunctionWithInconsistentReturnsJS
/**
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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

qx.Class.define("org.jspresso.framework.view.qx.RTableModel", {
  extend: qx.ui.table.model.Abstract,
  //extend : qx.ui.table.model.Simple,

  construct: function (remoteCompositeValueState, sortable, sortingAction, commandHandler) {
    this.base(arguments);
    this.__state = remoteCompositeValueState;
    this.__sortable = sortable;
    this.__sortingAction = sortingAction;
    this.__commandHandler = commandHandler;
    this.__setupCollectionListeners();
    this.__sortColumnIndex = -1;
    this.__sortAscending = false;
    this.__sortedRows = null;
  },

  statics: {
    _defaultSortComparatorAscending: function (row1, row2) {
      var obj1 = org.jspresso.framework.view.qx.RTableModel._extractCompareValueFromState(row1.getChildren().getItem(arguments.callee.columnIndex
          + 1));
      var obj2 = org.jspresso.framework.view.qx.RTableModel._extractCompareValueFromState(row2.getChildren().getItem(arguments.callee.columnIndex
          + 1));
      if (obj1 instanceof String) {
        obj1 = obj1.toLowerCase();
      } else if (obj1 instanceof org.jspresso.framework.util.lang.DateDto) {
        obj1 = org.jspresso.framework.util.format.DateUtils.fromDateDto(obj1);
      }
      if (obj2 instanceof String) {
        obj2 = obj2.toLowerCase();
      } else if (obj2 instanceof org.jspresso.framework.util.lang.DateDto) {
        obj2 = org.jspresso.framework.util.format.DateUtils.fromDateDto(obj2);
      }
      return (obj1 > obj2) ? 1 : ((obj1 == obj2) ? 0 : -1);
    },

    _defaultSortComparatorDescending: function (row1, row2) {
      var obj1 = org.jspresso.framework.view.qx.RTableModel._extractCompareValueFromState(row1.getChildren().getItem(arguments.callee.columnIndex
          + 1));
      var obj2 = org.jspresso.framework.view.qx.RTableModel._extractCompareValueFromState(row2.getChildren().getItem(arguments.callee.columnIndex
          + 1));
      if (obj1 instanceof String) {
        obj1 = obj1.toLowerCase();
      } else if (obj1 instanceof org.jspresso.framework.util.lang.DateDto) {
        obj1 = org.jspresso.framework.util.format.DateUtils.fromDateDto(obj1);
      }
      if (obj2 instanceof String) {
        obj2 = obj2.toLowerCase();
      } else if (obj2 instanceof org.jspresso.framework.util.lang.DateDto) {
        obj2 = org.jspresso.framework.util.format.DateUtils.fromDateDto(obj2);
      }
      return (obj1 < obj2) ? 1 : ((obj1 == obj2) ? 0 : -1);
    },

    _extractCompareValueFromState: function (cell) {
      if (cell instanceof org.jspresso.framework.state.remote.RemoteFormattedValueState) {
        return cell.getValueAsObject();
      } else {
        return cell.getValue();
      }
    }

  },

  members: {
    /** @type {org.jspresso.framework.state.remote.RemoteCompositeValueState} */
    __state: null,
    /** @type {Integer} */
    __sortColumnIndex: null,
    /** @type {Boolean} */
    __sortAscending: false,
    /** @type {org.jspresso.framework.gui.remote.RAction} */
    __sortingAction: null,
    /** @type {org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler} */
    __commandHandler: null,
    /** @type {Array} */
    __sortedRows: null,
    /** @type {Boolean} */
    __editable: true,
    /** @type {Array} */
    __dynamicToolTipIndices: null,
    /** @type {Object} */
    __dynamicStylesIndices: null,


    // overridden
    getRowCount: function () {
      return this.__state.getChildren().length;
    },

    // overridden
    getValue: function (columnIndex, rowIndex) {
      if (this.getRowData(rowIndex)) {
        var cellState = this.getRowData(rowIndex).getChildren().getItem(columnIndex + 1);
        this.__setupCellListeners(columnIndex, rowIndex, cellState);
        return cellState.getValue();
      }
    },

    // overridden
    setValue: function (columnIndex, rowIndex, value) {
      if (this.getRowData(rowIndex)) {
        var cellState = this.getRowData(rowIndex).getChildren().getItem(columnIndex + 1);
        cellState.setValue(value);
      }
    },

    // overridden
    setDynamicToolTipIndices: function (value) {
      this.__dynamicToolTipIndices = value;
    },

    setDynamicStylesIndices: function (value) {
      this.__dynamicStylesIndices = value;
    },

    // overridden
    getRowData: function (rowIndex) {
      if (this.__sortedRows) {
        return this.__sortedRows[rowIndex];
      } else {
        return this.__state.getChildren().getItem(rowIndex);
      }
    },

    // overridden
    isColumnEditable: function (columnIndex) {
      // editability is handled cell by cell unless whole model is read only.
      return this.__editable;
    },

    // overridden
    isColumnSortable: function (columnIndex) {
      // sorting is handled on server side.
      return this.__sortable;
    },

    // overridden
    sortByColumn: function (columnIndex, ascending) {
      if (!this.__sortable) {
        return;
      }
      var property = this.getColumnId(columnIndex);
      if (!property || property.length == 0 || property.charAt(0) == "#") {
        return;
      }
      this.__sortColumnIndex = columnIndex;
      this.__sortAscending = ascending;
      if (this.__sortingAction) {
        var orderingProperties = {};
        orderingProperties[property] = ascending ? "ASCENDING" : "DESCENDING";
        var sortCommand = new org.jspresso.framework.application.frontend.command.remote.RemoteSortCommand();
        sortCommand.setOrderingProperties(orderingProperties);
        sortCommand.setViewStateGuid(this.__state.getGuid());
        sortCommand.setViewStatePermId(this.__state.getPermId());
        sortCommand.setTargetPeerGuid(this.__sortingAction.getGuid());
        sortCommand.setPermId(this.__sortingAction.getPermId());
        this.__commandHandler.registerCommand(sortCommand);
      } else {
        if (this.__state.getChildren()) {
          var comparator = (ascending ? org.jspresso.framework.view.qx.RTableModel._defaultSortComparatorAscending :
              org.jspresso.framework.view.qx.RTableModel._defaultSortComparatorDescending);
          comparator.columnIndex = columnIndex;
          if (!this.__sortedRows) {
            this.__sortedRows = qx.lang.Array.clone(this.__state.getChildren().toArray());
          }
          this.__sortedRows.sort(comparator);
        }
      }
      this.fireEvent("metaDataChanged");
    },

    // overridden
    getSortColumnIndex: function () {
      return this.__sortColumnIndex;
    },

    // overridden
    isSortAscending: function () {
      return this.__sortAscending;
    },

    clearSorting: function () {
      if (this.__sortColumnIndex != -1) {
        this.__sortColumnIndex = -1;
        this.__sortAscending = false;
        this.__sortedRows = null;
        this.fireEvent("metaDataChanged");
      }
    },

    __setupCollectionListeners: function () {
      this.__state.getChildren().addListener("change", function (e) {
        var data = {
          firstRow: e.getData().start,
          lastRow: e.getData().end,
          firstColumn: 0,
          lastColumn: this.getColumnCount() - 1
        };
        if (this.__sortingAction == null) {
          this.clearSorting();
        }
        this.fireDataEvent("dataChanged", data);
      }, this);
      this.__state.getChildren().addListener("changeLength", function (e) {
        var data = {
          firstRow: 0,
          lastRow: e.getTarget().length - 1,
          firstColumn: 0,
          lastColumn: this.getColumnCount() - 1
        };
        if (this.__sortingAction == null) {
          this.clearSorting();
        }
        this.fireDataEvent("dataChanged", data);
      }, this);
    },

    /**
     *
     * @param columnIndex {Integer}
     * @param rowIndex {Integer}
     * @param cellState {org.jspresso.framework.state.remote.RemoteValueState}
     */
    __setupCellListeners: function (columnIndex, rowIndex, cellState) {
      if (!cellState.getUserData(this.toHashCode())) {
        var refreshCell = function (e) {
          var data = {
            firstRow: rowIndex,
            lastRow: rowIndex,
            firstColumn: columnIndex,
            lastColumn: columnIndex
          };
          this.fireDataEvent("dataChanged", data);
        };
        cellState.addListener("changeValue", refreshCell, this);
        for (var i = 0; i < this.__dynamicStylesIndices[columnIndex].length; i++) {
          cellState.getParent().getChildren().getItem(
              this.__dynamicStylesIndices[columnIndex][i]).addListener("changeValue", refreshCell, this);
        }
        cellState.setUserData(this.toHashCode(), true)
      }
    },

    modelIndexToViewIndex: function (modelIndex) {
      if (this.__sortedRows) {
        return this.__sortedRows.indexOf(this.__state.getChildren().getItem(modelIndex));
      }
      return modelIndex;
    },

    viewIndexToModelIndex: function (viewIndex) {
      if (this.__sortedRows) {
        return this.__state.getChildren().indexOf(this.__sortedRows[viewIndex]);
      }
      return viewIndex;
    },

    modelIndicesToViewIndices: function (modelIndices) {
      if (modelIndices) {
        var viewIndices = modelIndices.map(this.modelIndexToViewIndex, this);
        return viewIndices.sort();
      }
      return modelIndices;
    },

    viewIndicesToModelIndices: function (viewIndices) {
      if (viewIndices) {
        var modelIndices = viewIndices.map(this.viewIndexToModelIndex, this);
        return modelIndices.sort();
      }
      return viewIndices;
    },

    setEditable: function (editable) {
      this.__editable = editable;
    },

    getToolTip: function (column, row) {
      if (row != null && row >= 0) {
        if (column != null && column >= 0) {
          if (this.__dynamicToolTipIndices[column] >= 0) {
            var v = this.getRowData(row).getChildren().getItem(this.__dynamicToolTipIndices[column]).getValue();
            if (v != null && (v instanceof String || typeof(v) === 'string')) {
              return v;
            }
            return null;
          } else if (column == 0) {
            return this.getRowData(row).getValue();
          } else {
            var v = this.getValue(column, row);
            if (v != null && (v instanceof String || typeof(v) === 'string')) {
              return v;
            }
          }
        }
      }
      return null;
    }

  }
});
