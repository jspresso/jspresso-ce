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

qx.Class.define("org.jspresso.framework.view.qx.RComponentTableCellEditor", {
  extend: qx.core.Object,
  implement: [qx.ui.table.ICellEditorFactory],


  /**
   *
   * @param viewFactory {org.jspresso.framework.view.qx.DefaultQxViewFactory}
   * @param rComponent {org.jspresso.framework.gui.remote.RComponent}
   * @param actionHandler {org.jspresso.framework.action.IActionHandler}
   */
  construct: function (viewFactory, rComponent, actionHandler) {
    this.base(arguments);
    this.__viewFactory = viewFactory;
    this.__rComponent = rComponent;
    this.__actionHandler = actionHandler;
  },


  members: {
    __viewFactory: null,
    __rComponent: null,
    __actionHandler: null,
    __currentBinding: null,
    __currentCellState: null,

    // interface implementation
    createCellEditor: function (cellInfo) {
      var value = cellInfo.value;
      var row = cellInfo.row;
      var col = cellInfo.col;
      /** @type {qx.ui.table.Table} */
      var table = cellInfo.table;
      /** @type {org.jspresso.framework.state.remote.RemoteCompositeValueState} */
      var rowState = table.getTableModel().getRowData(row);
      if (!rowState.getWritable()) {
        return null;
      }
      /** @type {org.jspresso.framework.state.remote.RemoteValueState} */
      var cellState = rowState.getChildren().getItem(col + 1);
      if (!cellState.getWritable()) {
        return null;
      }
      this.__cleanCurrentCellBinding();
      this.__currentCellState = cellState;
      var state = this.__rComponent.getState();
      state.setWritable(true);
      if (state.getBindings()) {
        state.removeAllBindings();
      }
      this.__currentBinding = this.__currentCellState.bind("value", state, "value");
      this.__actionHandler.setCurrentViewStateGuid(this.__currentCellState.getGuid(),
          this.__currentCellState.getPermId());
      var editorWidget = this.__viewFactory.createComponent(this.__rComponent, false);
      editorWidget.addListener("disappear", this.__cleanCurrentCellBinding, this);
      state.addListener("changeValue", function () {
        cellInfo.table.stopEditing();
      }, this);

      var editor;
      if (!(editorWidget instanceof qx.ui.container.Composite) && !(editorWidget instanceof qx.ui.form.TextArea)) {
        editor = new qx.ui.container.Composite(new qx.ui.layout.VBox().set({
          alignX: "center",
          alignY: "middle"
        })).set({
              focusable: true
            });
        // propagate focus
        editor.addListener("focus", function () {
          editorWidget.focus();
        });
        // propagate active state
        editor.addListener("activate", function () {
          editorWidget.activate();
        });
        editorWidget.setAllowStretchY(false, false);
        if (!editorWidget instanceof qx.ui.form.CheckBox) {
          editorWidget.setAllowStretchX(true, true);
        }
        editorWidget.setMaxWidth(null);
        editorWidget.setWidth(null);
        editorWidget.setMinWidth(0);
        //editorWidget.setAlignX("center");
        editorWidget.setAlignY("middle");
        editor.add(editorWidget);
      } else {
        editor = editorWidget;
        if (editorWidget instanceof qx.ui.form.TextArea) {
          // prevent enter to go to Table
          editor.addListener("keypress", function (/** @type {qx.event.type.KeySequence} */evt) {
            // Handle keys that are independent from the modifiers
            var identifier = evt.getKeyIdentifier();
            // Editing mode
            if (evt.getModifiers() == 0 && identifier == "Enter") {
              evt.stopPropagation();
            }
          });
        }
      }
      return editor;
    },

    // interface implementation
    getCellEditorValue: function (cellEditor) {
      return this.__rComponent.getState().getValue();
    },

    __cleanCurrentCellBinding: function (e) {
      if (this.__currentCellState && this.__currentBinding) {
        this.__currentCellState.removeBinding(this.__currentBinding);
        this.__currentBinding = null;
        this.__currentCellState = null;
        this.__actionHandler.setCurrentViewStateGuid(null, null);
      }
    }
  }
});
