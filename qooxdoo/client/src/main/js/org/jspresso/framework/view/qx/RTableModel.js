/**
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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

qx.Class.define("org.jspresso.framework.view.qx.RTableModel",
{
  extend : qx.ui.table.model.Abstract,
  //extend : qx.ui.table.model.Simple,
  
  construct : function(remoteCompositeValueState, sortable, sortingAction, commandHandler)
  {
    this.base(arguments);
    this.__state = remoteCompositeValueState;
    this.__sortable = sortable;
    this.__sortingAction = sortingAction;
    this.__commandHandler = commandHandler;
    this.__setupCollectionListeners();
    this.__sortColumnIndex = -1;
    this.__sortAscending = null;
    this.__sortedRows = null;
  },

  statics :
  {
    _defaultSortComparatorAscending : function(row1, row2)
    {
      var obj1 = row1.getChildren().getItem(arguments.callee.columnIndex + 1).getValue();
      var obj2 = row2.getChildren().getItem(arguments.callee.columnIndex + 1).getValue();
      if(obj1 instanceof String) {
        obj1 = obj1.toLowerCase();
      }
      if(obj2 instanceof String) {
        obj2 = obj2.toLowerCase();
      }
      return (obj1 > obj2) ? 1 : ((obj1 == obj2) ? 0 : -1);
    },

    _defaultSortComparatorDescending : function(row1, row2)
    {
      var obj1 = row1.getChildren().getItem(arguments.callee.columnIndex + 1).getValue();
      var obj2 = row2.getChildren().getItem(arguments.callee.columnIndex + 1).getValue();
      if(obj1 instanceof String) {
        obj1 = obj1.toLowerCase();
      }
      if(obj2 instanceof String) {
        obj2 = obj2.toLowerCase();
      }
      return (obj1 < obj2) ? 1 : ((obj1 == obj2) ? 0 : -1);
    }

  },

  members :
  {
    /**@type org.jspresso.framework.state.remote.RemoteCompositeValueState*/
    __state : null,
    /**@type Integer*/
    __sortColumnIndex : null,
    /**@type Boolean*/
    __sortAscending : null,
    /**@type org.jspresso.framework.gui.remote.RAction*/
    __sortingAction : null,
    /**@type org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler*/
    __commandHandler : null,
    /**@type Array*/
    __sortedRows : null,
    /**@type Boolean*/
    __editable : true,
    
    // overridden
    getRowCount : function() {
      return this.__state.getChildren().length;
    },

    // overridden
    getValue : function(columnIndex, rowIndex) {
      var cellState = this.getRowData(rowIndex)
                          .getChildren().getItem(columnIndex +1);
      this.__setupCellListeners(columnIndex, rowIndex, cellState);
      return cellState.getValue();
    },
    
    // overridden
    setValue : function(columnIndex, rowIndex, value) {
      var cellState = this.getRowData(rowIndex)
                          .getChildren().getItem(columnIndex +1); 
      cellState.setValue(value);
    },
    
    // overridden
    getRowData : function(rowIndex) {
      if(this.__sortedRows) {
        return this.__sortedRows[rowIndex];
      } else {
        return this.__state.getChildren().getItem(rowIndex);
      }
    },

    // overridden
    isColumnEditable : function(columnIndex) {
      // editability is handled cell by cell unless whole model is read only.
      return this.__editable;
    },

    // overridden
    isColumnSortable : function(columnIndex) {
      // sorting is handled on server side.
      return this.__sortable;
    },
    
    // overridden
    sortByColumn : function(columnIndex, ascending) {
    	if(!this.__sortable) {
    		return;
    	}
      this.__sortColumnIndex = columnIndex;
      this.__sortAscending = ascending;
      if(this.__sortingAction) {
      	if(this.getRowCount() > 1) {
	        var orderingProperties = new Object();
	        orderingProperties[this.getColumnId(columnIndex)] = ascending ? "ASCENDING": "DESCENDING";
	        var sortCommand = new org.jspresso.framework.application.frontend.command.remote.RemoteSortCommand();
	        sortCommand.setOrderingProperties(orderingProperties);
	        sortCommand.setViewStateGuid(this.__state.getGuid(), this.__state.getAutomationId());
          sortCommand.setTargetPeerGuid(this.__sortingAction.getGuid());
          sortCommand.setAutomationId(this.__sortingAction.getAutomationId());
	        this.__commandHandler.registerCommand(sortCommand);
      	}
      } else {
        if(this.__state.getChildren()) {
          var comparator =
            (ascending
             ? org.jspresso.framework.view.qx.RTableModel._defaultSortComparatorAscending
             : org.jspresso.framework.view.qx.RTableModel._defaultSortComparatorDescending);
          comparator.columnIndex = columnIndex;
          if(! this.__sortedRows) {
            this.__sortedRows = qx.lang.Array.clone(this.__state.getChildren().toArray());
          }
          this.__sortedRows.sort(comparator);
        }
      }
      this.fireEvent("metaDataChanged");
    },

    // overridden
    getSortColumnIndex : function() {
      return this.__sortColumnIndex;
    },

    // overridden
    isSortAscending : function() {
      return this.__sortAscending;
    },

    clearSorting : function() {
      if (this.__sortColumnIndex != -1) {
        this.__sortColumnIndex = -1;
        this.__sortAscending = null;
        this.__sortedRows = null;
        this.fireEvent("metaDataChanged");
      }
    },

    __setupCollectionListeners : function() {
      this.__state.getChildren().addListener("change", function(e) {
        var data = {
          firstRow    : e.getData().start,
          lastRow     : e.getData().end,
          firstColumn : 0,
          lastColumn  : this.getColumnCount() - 1
        };
        if(this.__sortingAction == null) {
        	this.clearSorting();
        }
        this.fireDataEvent("dataChanged", data);
      }, this);
      this.__state.getChildren().addListener("changeLength", function(e) {
        var data = {
          firstRow    : 0,
          lastRow     : e.getTarget().length - 1,
          firstColumn : 0,
          lastColumn  : this.getColumnCount() - 1
        };
        if(this.__sortingAction == null) {
          this.clearSorting();
        }
        this.fireDataEvent("dataChanged", data);
      }, this);
    },
    
    /**
     * 
     * @param {Integer} columnIndex
     * @param {Integer} rowIndex
     * @param {org.jspresso.framework.state.remote.RemoteValueState} cellState
     */
    __setupCellListeners : function(columnIndex, rowIndex, cellState) {
      if(!cellState.getUserData(this.toHashCode())) {
        cellState.addListener("changeValue", function(e) {
          var data =
          {
            firstRow    : rowIndex,
            lastRow     : rowIndex,
            firstColumn : columnIndex,
            lastColumn  : columnIndex
          };
          this.fireDataEvent("dataChanged", data);
        }, this);
        cellState.setUserData(this.toHashCode(), true)
      }
    },
    
    modelIndexToViewIndex : function(modelIndex) {
      if(this.__sortedRows) {
        return this.__sortedRows.indexOf(this.__state.getChildren().getItem(modelIndex));
      }
      return modelIndex;
    },
    
    viewIndexToModelIndex : function(viewIndex) {
      if(this.__sortedRows) {
        return this.__state.getChildren().indexOf(this.__sortedRows[viewIndex]);
      }
      return viewIndex;
    },
    
    modelIndicesToViewIndices : function(modelIndices) {
      if(modelIndices) {
        var viewIndices = modelIndices.map(this.modelIndexToViewIndex, this);
        return viewIndices.sort();
      }
      return modelIndices;
    },

    viewIndicesToModelIndices : function(viewIndices) {
      if(viewIndices) {
        var modelIndices = viewIndices.map(this.viewIndexToModelIndex, this);
        return modelIndices.sort();
      }
      return viewIndices;
    },
    
    setEditable : function(editable) {
      this.__editable = editable;
    }
  }
});
