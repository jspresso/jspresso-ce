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

qx.Class.define("org.jspresso.framework.view.qx.RComponentTableCellEditor", {
  extend: qx.core.Object, implement: [qx.ui.table.ICellEditorFactory],


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
    __savedDialogIndex: null,
    __editorWidget: null,

    // interface implementation
    createCellEditor: function (cellInfo) {
      var value = cellInfo.value;
      var row = cellInfo.row;
      var col = cellInfo.col;
      /** @type {qx.ui.table.Table} */
      var table = cellInfo.table;

      if (!table.isCellEditable(row, col)) {
        return null;
      }
      var selectionModel = table.getSelectionModel();
      if (selectionModel.getSelectionMode() == qx.ui.table.selection.Model.MULTIPLE_INTERVAL_SELECTION_TOGGLE) {
        selectionModel.setSelectionInterval(row, row);
      }

      var rowState = table.getTableModel().getRowData(row);
      /** @type {org.jspresso.framework.state.remote.RemoteValueState} */
      var cellState = rowState.getChildren().getItem(col + 1);

      this.__cleanCurrentCellBinding();
      this.__currentCellState = cellState;
      var state = this.__rComponent.getState();
      state.setWritable(true);
      if (state.getBindings()) {
        state.removeAllBindings();
      }
      this.__currentBinding = this.__currentCellState.bind("value", state, "value", {
        converter: function (value) {
          return value === "" ? null : value;
        }
      });
      this.__savedDialogIndex = this.__actionHandler.setCurrentViewStateGuid(this.__currentCellState.getGuid(),
          this.__currentCellState.getPermId());
      var editorWidget;
      var focusGainedAction = this.__rComponent.getFocusGainedAction();
      try {
        this.__rComponent.setFocusGainedAction(null);
        editorWidget = this.__viewFactory.createComponent(this.__rComponent, false);
      } finally {
        this.__rComponent.setFocusGainedAction(focusGainedAction);
      }
      this.__editorWidget = editorWidget;
      editorWidget.setMargin(0);
      if (this.__rComponent instanceof org.jspresso.framework.gui.remote.RActionField) {
        editorWidget.setPadding(0);
        var componentsToStyle = editorWidget.getUserData("componentsToStyle");
        if (componentsToStyle && componentsToStyle.length > 0) {
          componentsToStyle[0].setMargin(0);
        }
      }
      editorWidget.addListener("disappear", this.__cleanCurrentCellBinding, this);
      state.addListenerOnce("changeValue", function (e) {
        if (e.getData() === null) {
          this.__currentCellState.setValue(e.getData());
        }
        table.stopEditing();
      }, this);

      var editor;
      var layout;
      if (this.__viewFactory.isMultiline(this.__rComponent)) {
        layout = new qx.ui.layout.Grow();
        editorWidget.setAllowStretchY(true, true);
      } else {
        layout = new qx.ui.layout.VBox().set({
          alignX: "center"
        });
      }
      editor = new qx.ui.container.Composite(layout).set({
        focusable: true
      });
      var background = qx.theme.manager.Color.getInstance().resolve("app-background");
      editor.setBackgroundColor(background);
      if (editorWidget instanceof qx.ui.form.CheckBox) {
        editorWidget.addListenerOnce("appear", function (e) {
          editorWidget.setValue(!editorWidget.getValue());
        });
      } else {
        // propagate focus
        editor.addListener("focus", function () {
          this.__viewFactory.focus(editorWidget);
        }, this);
        editorWidget.setAllowStretchX(true, true);
      }
      editorWidget.setMaxWidth(null);
      editorWidget.setWidth(null);
      editorWidget.setMinWidth(0);
      editor.add(editorWidget);

      if (editorWidget instanceof qx.ui.form.TextArea) {
        // prevent enter to go to Table
        editorWidget.addListener("keypress", function (/** @type {qx.event.type.KeySequence} */evt) {
          // Handle keys that are independent from the modifiers
          var identifier = evt.getKeyIdentifier();
          // Editing mode
          if (evt.getModifiers() == 0 && identifier == "Enter") {
            evt.stopPropagation();
          }
        });
      }
      editor.addListener("keypress", function (e) {
        var timer = qx.util.TimerManager.getInstance();
        var iden = e.getKeyIdentifier();
        if (iden == "Tab") {
          e.stop();
          if (table.isEditing()) {
            table.stopEditing();
          }
          var columnCount = table.getTableModel().getColumnCount();
          var rowCount = table.getTableModel().getRowCount();
          col += (e.isShiftPressed() ? -1 : 1);
          if (col < 0) {
            row -= 1;
            col = columnCount - 1;
          } else if (col > columnCount - 1) {
            row += 1;
            col = 0;
          }
          if (row >= 0 && row < rowCount && col >= 0 && col < columnCount) {
            table.setFocusedCell(col, row, true);
            table.updateContent();
            // Breaks LOVs
            //table.startEditing();
          }
          e.stopPropagation();
        } else if (iden == 'Enter') {
          e.stop();
          if (table.isEditing()) {
            table.stopEditing();
          }
          e.stopPropagation();
        }
      }, this);
      editor.addListener("focusout", function (e) {
        var relatedTarget = e.getRelatedTarget();
        var timer = qx.util.TimerManager.getInstance();
        if (relatedTarget != null && relatedTarget != editor && relatedTarget != table && !qx.ui.core.Widget.contains(
                editor, relatedTarget)) {
          timer.start(function (userData, timerId) {
            if (table.isEditing()) {
              table.stopEditing();
            }
          }, 0, this, null, 0);
        } else if (relatedTarget == table) {
          if (this.__rComponent instanceof org.jspresso.framework.gui.remote.RActionField) {
            // Edition is completely handled by the action field and the value will be updated afterwards.
            table.cancelEditing();
          }
        }
      }, this);
      return editor;
    },

    __flushEditorValue: function () {
      var fieldToFlush;
      if (this.__editorWidget instanceof qx.ui.form.DateField) {
        fieldToFlush = this.__editorWidget;
      } else if (this.__editorWidget instanceof qx.ui.container.Composite) {
        if (this.__editorWidget.getUserData("df")) {
          fieldToFlush = this.__editorWidget.getUserData("df");
        } else if(this.__editorWidget.getUserData("actionsDecorated")) {
          fieldToFlush = this.__editorWidget.getUserData("actionsDecorated");
        }
      } else if (this.__editorWidget instanceof qx.ui.form.TextField) {
        fieldToFlush = this.__editorWidget;
      }
      if (fieldToFlush && fieldToFlush.getValue) {
        // Forces synchronization of the field to flush
        fieldToFlush.fireDataEvent("changeValue", fieldToFlush.getValue());
      }
    },

    // interface implementation
    getCellEditorValue: function (cellEditor) {
      this.__flushEditorValue();
      return this.__rComponent.getState().getValue();
    },

    __cleanCurrentCellBinding: function (e) {
      if (this.__currentCellState && this.__currentBinding) {
        this.__currentCellState.removeBinding(this.__currentBinding);
        this.__currentBinding = null;
        this.__currentCellState = null;
        this.__actionHandler.setCurrentViewStateGuid(null, null, this.__savedDialogIndex);
      }
    }
  }
});
