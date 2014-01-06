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

qx.Class.define("org.jspresso.framework.view.qx.MobileQxViewFactory", {
  extend: org.jspresso.framework.view.qx.AbstractQxViewFactory,

  construct: function (remotePeerRegistry, actionHandler, commandHandler) {
    this.base(arguments, remotePeerRegistry, actionHandler, commandHandler);
  },

  members: {

    /**
     *
     * @return {qx.ui.mobile.form.Button}
     * @param toolTip {String}
     * @param label {String}
     * @param icon {org.jspresso.framework.gui.remote.RIcon}
     */
    createButton: function (label, toolTip, icon) {
      var button = new qx.ui.mobile.form.Button();
      this._completeButton(button, label, toolTip, icon);
      return button;
    },

    /**
     *
     * @return {undefined}
     * @param icon {org.jspresso.framework.gui.remote.RIcon}
     * @param button {qx.ui.mobile.form.Button}
     * @param label {String}
     * @param toolTip {String}
     */
    _completeButton: function (button, label, toolTip, icon) {
      this.setIcon(button, icon);
      if (label) {
        button.setLabel(label);
      }
    },

    /**
     * @param button {qx.ui.mobile.form.Button}
     * @param listener {function}
     * @param that {var}
     */
    addButtonListener: function(button, listener, that) {
      button.addListener("tap", listener, that);
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteForm {org.jspresso.framework.gui.remote.RForm}
     */
    _createForm: function (remoteForm) {
      var form = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.VBox());
      for (var i = 0; i < remoteForm.getElements().length; i++) {
        var rComponent = remoteForm.getElements()[i];

        var component = /** @type {qx.ui.mobile.core.Widget} */ this.createComponent(rComponent);

        var row = new qx.ui.mobile.container.Composite();
        row.setDefaultCssClass("form-row");
        row.addCssClass("form-row-content");
        if(remoteForm.getLabelsPosition() == "ABOVE"
            || this._isMultiline(rComponent)) {
          row.setLayout(new qx.ui.mobile.layout.VBox());
        } else {
          row.setLayout(new qx.ui.mobile.layout.HBox())
        }
        if(remoteForm.getLabelsPosition() != "NONE") {
          var label = new qx.ui.mobile.form.Label(rComponent.getLabel());
          label.setLabelFor(component.getId());
          row.add(label, {flex:1});
        }
        if(this._isMultiline(rComponent)) {
          row.add(component, {flex:1});
        } else {
          row.add(component);
        }
        form.add(row);
      }
      return form;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     */
    _createDefaultComponent: function () {
      return new qx.ui.mobile.core.Widget();
    },

    /**
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @param component {qx.ui.mobile.core.Widget}
     * @return {undefined}
     */
    _configureToolTip: function (remoteComponent, component) {
      //NO-OP
    },

    /**
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @param component {qx.ui.mobile.core.Widget}
     * @return {qx.ui.mobile.core.Widget}
     */
    _decorateWithBorder: function (remoteComponent, component) {
      return component;
    },

    /**
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @param component {qx.ui.mobile.core.Widget}
     */
    _applyPreferredSize: function (remoteComponent, component) {
      //NO-OP
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteTextField  {org.jspresso.framework.gui.remote.RTextField}
     */
    _createTextField: function (remoteTextField) {
      var textField = new qx.ui.mobile.form.TextField();
      if (remoteTextField.getMaxLength() > 0) {
        textField.setMaxLength(remoteTextField.getMaxLength());
      }
      textField.setPlaceholder(remoteTextField.getLabel());
      this._bindTextField(remoteTextField, textField);
      return textField;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remotePasswordField {org.jspresso.framework.gui.remote.RPasswordField}
     */
    _createPasswordField: function (remotePasswordField) {
      var passwordField = new qx.ui.mobile.form.PasswordField();
      if (remotePasswordField.getMaxLength() > 0) {
        passwordField.setMaxLength(remotePasswordField.getMaxLength());
      }
      passwordField.setPlaceholder(remotePasswordField.getLabel());
      this._bindTextField(remotePasswordField, passwordField);
      return passwordField;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteComboBox {org.jspresso.framework.gui.remote.RComboBox}
     */
    _createComboBox: function (remoteComboBox) {
      var comboBox = new qx.ui.mobile.form.SelectBox();
      comboBox.setPlaceholder(remoteComboBox.getLabel());
      var cbModel = new qx.data.Array();
      for (var i = 0; i < remoteComboBox.getValues().length; i++) {
        if (i == 0 && remoteComboBox.getValues()[i].length > 0) {
          remoteComboBox.getValues().insertAt(0, "");
          cbModel.push(" ");
        }
        cbModel.push(remoteComboBox.getTranslations()[i]);
      }
      comboBox.setModel(cbModel);
      this._bindComboBox(remoteComboBox, comboBox);
      return comboBox;
    },

    /**
     * @param remoteComboBox  {org.jspresso.framework.gui.remote.RComboBox}
     * @param comboBox {qx.ui.mobile.form.SelectBox}
     */
    _bindComboBox: function (remoteComboBox, comboBox) {
      // To workaround the fact that value change is not notified correctly.
      comboBox.addListener("changeSelection", function(evt) {
        comboBox.setValue(evt.getData()["item"]);
      }, this);
      var state = remoteComboBox.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(comboBox, "value", "value", true,
          {
            converter: function (modelValue, model, source, target) {
              return model.getItem(remoteComboBox.getValues().indexOf(modelValue));
            }
          },
          {
            converter: function (modelValue, model, source, target) {
              return remoteComboBox.getValues()[comboBox.getModel().indexOf(modelValue)];
            }
          }
      );
      modelController.addTarget(comboBox, "enabled", "writable", false);
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteCheckBox {org.jspresso.framework.gui.remote.RCheckBox}
     */
    _createCheckBox: function (remoteCheckBox) {
      var checkBox = new qx.ui.mobile.form.CheckBox();
      this._bindCheckBox(remoteCheckBox, checkBox);
      return checkBox;
    },

    /**
     * @param root {qx.ui.mobile.core.Widget}
     * @return {qx.ui.mobile.core.Widget}
     */
    _findFirstFocusableComponent: function (root) {
      if (root instanceof qx.ui.mobile.form.TextField || root instanceof qx.ui.mobile.form.NumberField) {
        if (root.getEnabled()) {
          return root;
        }
      }
      var children;
      if (root instanceof qx.ui.mobile.container.Composite) {
        children = (/** @type {qx.ui.mobile.core.MChildrenHandling} */ root).getChildren();
      } else if(root instanceof qx.ui.mobile.form.renderer.Single) {
        // breaks isolation, but there's no other way...
        children = (/** @type {qx.ui.mobile.form.renderer.Single} */ root)._rows;
      }
      if(children) {
        for (var i = 0; i < children.length; i++) {
          var child = children[i];
          if (child instanceof qx.ui.mobile.core.Widget) {
            var focusableChild = this._findFirstFocusableComponent(/** @type {qx.ui.mobile.core.Widget } */ child);
            if (focusableChild != null) {
              return focusableChild;
            }
          }
        }
      }
      return null;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteTree {org.jspresso.framework.gui.remote.RTree}
     */
    _createTree: function (remoteTree) {

      var treeListModel = org.jspresso.framework.state.remote.RemoteCompositeValueState.flatten(remoteTree.getState(), 0);

      var treeList = new qx.ui.mobile.list.List({
        configureItem: function (item, data, row) {
          item.setTitle(data.state.getValue());
          item.setSubtitle(data.state.getDescription());
          item.setImage(data.state.getIconImageUrl());
          item.setShowArrow(true);
          item.getImageWidget()._setStyle("margin-left", data.level + "rem");
        }
      });
      treeList.setModel(treeListModel);
      var selections = [];
      treeList.addListener("changeSelection", function(evt) {
        var futureDeselections = [];
        var localLevel = 0;
        for(var i = 1; i < treeListModel.length; i++) {
          var lowerNode = treeListModel.getItem(i);
          if(lowerNode.level > localLevel) {
            var currentNode = treeListModel.getItem(i-1);
            if(currentNode.state.getSelectedIndices() != null && currentNode.state.getSelectedIndices().length > 0) {
              futureDeselections.push(currentNode.state);
            }
          }
          localLevel = lowerNode.level;
        }
        var selectedIndex = evt.getData();
        var currentNode = treeListModel.getItem(selectedIndex);
        var localIndex = 0;
        var futureSelections = [];
        for(var i = selectedIndex-1; i >= 0; i--) {
          var upperNode = treeListModel.getItem(i);
          if(upperNode.level < currentNode.level) {
            futureSelections = [{state: upperNode.state, selection: [localIndex]}].concat(futureSelections);
            var j = futureDeselections.indexOf(upperNode.state);
            if(j >= 0) {
              futureDeselections[j] = null;
            }
            currentNode = upperNode;
            localIndex = 0;
          } else {
            localIndex++;
          }
        }
        for(var i = 0; i < futureDeselections.length; i++) {
          if(futureDeselections[i]) {
            futureDeselections[i].setSelectedIndices(null);
            futureDeselections[i].setLeadingIndex(-1);
          }
        }
        for(var i = 0; i < futureSelections.length; i++) {
          if(futureSelections[i]) {
            futureSelections[i].state.setSelectedIndices(futureSelections[i].selection);
            futureSelections[i].state.setLeadingIndex(futureSelections[i].selection[0]);
          }
        }
      }, this);
      return treeList;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     */
    _createCardContainerComponent: function () {
      return new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.Card());
    },

    /**
     *
     * @return {undefined}
     * @param cardContainer {qx.ui.mobile.container.Composite}
     * @param selectedCard  {qx.ui.mobile.core.Widget}
     */
    _selectCard: function (cardContainer, selectedCard) {
      selectedCard.show();
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteBorderContainer {org.jspresso.framework.gui.remote.RBorderContainer}
     */
    _createBorderContainer: function (remoteBorderContainer) {
      var borderContainer = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.VBox());
      if (remoteBorderContainer.getNorth()) {
        var child = this.createComponent(remoteBorderContainer.getNorth());
        borderContainer.add(child);
      }
      var middleContainer = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.HBox());
      borderContainer.add(middleContainer, {
        flex: 1
      });
      if (remoteBorderContainer.getWest()) {
        var child = this.createComponent(remoteBorderContainer.getWest());
        middleContainer.add(child);
      }
      if (remoteBorderContainer.getCenter()) {
        var child = this.createComponent(remoteBorderContainer.getCenter());
        middleContainer.add(child, {
          flex: 1
        });
      }
      if (remoteBorderContainer.getEast()) {
        var child = this.createComponent(remoteBorderContainer.getEast());
        middleContainer.add(child);
      }
      if (remoteBorderContainer.getSouth()) {
        var child = this.createComponent(remoteBorderContainer.getSouth());
        borderContainer.add(child);
      }
      return borderContainer;
    },

    /**
     *
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteSecurityComponent {org.jspresso.framework.gui.remote.RSecurityComponent}
     */
    _createSecurityComponent: function (remoteSecurityComponent) {
      var securityComponent = new qx.ui.mobile.core.Widget();
      return securityComponent;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteActionField {org.jspresso.framework.gui.remote.RActionField}
     */
    _createActionField: function (remoteActionField) {

      /** @type {qx.ui.mobile.form.TextField } */
      var textField;
      if (remoteActionField.getShowTextField()) {
        textField = new qx.ui.mobile.form.TextField();
      }
      var actionField = this._decorateWithAsideActions(textField, remoteActionField, true);
      var state = remoteActionField.getState();
      var modelController = new qx.data.controller.Object(state);
      var mainAction = remoteActionField.getActionLists()[0].getActions()[0];
      if (textField) {
        // propagate focus
        actionField.addListener("focus", function () {
          textField.focus();
        });
        if (remoteActionField.getFieldEditable()) {
          modelController.addTarget(textField, "readOnly", "writable", false, {
            converter: this._readOnlyFieldConverter
          });
        } else {
          textField.setReadOnly(true);
        }
        var triggerAction = function (e) {
          var content = textField.getValue();
          if (content && content.length > 0) {
            if (content != state.getValue()) {
              textField.setValue(state.getValue());
              if (e instanceof qx.event.type.Focus) {
                if (e.getRelatedTarget() && (/** @type {qx.ui.mobile.core.Widget } */ e.getRelatedTarget()) == actionField) {
                  return;
                }
              }
              var actionEvent = new org.jspresso.framework.gui.remote.RActionEvent();
              actionEvent.setActionCommand(content);
              this.__actionHandler.execute(mainAction, actionEvent);
            }
          } else {
            state.setValue(null);
          }
        };
        textField.addListener("blur", triggerAction, this);
        // textField.addListener("changeValue", triggerAction, this);

        modelController.addTarget(textField, "value", "value", false, {
          converter: this._modelToViewFieldConverter
        });
        if (remoteActionField.getCharacterAction()) {
          textField.addListener("input", function (event) {
            var actionEvent = new org.jspresso.framework.gui.remote.RActionEvent();
            actionEvent.setActionCommand(textField.getValue());
            this.__actionHandler.execute(remoteActionField.getCharacterAction(), actionEvent);
          }, this);
        }
      } else {
        state.addListener("changeValue", function (e) {
          if (e.getData()) {
            var border = new qx.ui.decoration.Decorator().set({
              color: "red",
              width: 1,
              style: "solid"
            });
            actionField.setDecorator(border);
          } else {
            actionField.setDecorator(null);
          }
        }, this);
      }
      return actionField;
    },

    /**
     *
     * @param component {qx.ui.mobile.core.Widget}
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @param disableActionsWithField {Boolean}
     * @returns {qx.ui.mobile.core.Widget}
     * @protected
     */
    _decorateWithAsideActions: function (component, remoteComponent, disableActionsWithField) {
      var decorated = component;
      if (remoteComponent.getActionLists()) {
        var actionField = new qx.ui.mobile.container.Composite();
        actionField.setLayout(new qx.ui.mobile.layout.HBox());

        if (component) {
          actionField.add(component, {
            flex: 1
          });
        }
        var modelController;
        if (remoteComponent.getState()) {
          modelController = new qx.data.controller.Object(remoteComponent.getState());
        }
        for (var i = 0; i < remoteComponent.getActionLists().length; i++) {
          var actionList = remoteComponent.getActionLists()[i];
          for (var j = 0; j < actionList.getActions().length; j++) {
            var remoteAction = actionList.getActions()[j];
            var actionComponent = this.createAction(remoteAction);
            actionComponent.setLabel(null);
            actionField.add(actionComponent);
            if (modelController && disableActionsWithField) {
              modelController.addTarget(actionComponent, "enabled", "writable", false);
            }
          }
        }
        decorated = actionField;
      }
      return decorated;
    },

    /**
     * @param component {qx.ui.mobile.core.Widget}
     * @param styleName {String}
     * @return {undefined}
     */
    _applyStyleName: function (component, styleName) {
      if(styleName) {
        component.addCssClass(styleName);
      }
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteTable {org.jspresso.framework.gui.remote.RTable}
     */
    _createTable: function (remoteTable) {
      return new qx.ui.mobile.form.Label("TABLE");
//      /** @type {org.jspresso.framework.state.remote.RemoteCompositeValueState } */
//      var state = remoteTable.getState();
//      var tableModel = new org.jspresso.framework.view.qx.RTableModel(state, remoteTable.getSortable(),
//          remoteTable.getSortingAction(), this.__commandHandler);
//      var modelController = new qx.data.controller.Object(state);
//      modelController.addTarget(tableModel, "editable", "writable", false);
//      var columnIds = remoteTable.getColumnIds();
//      var columnNames = [];
//      var columnToolTips = [];
//      for (var i = 0; i < remoteTable.getColumnIds().length; i++) {
//        columnNames[i] = remoteTable.getColumns()[i].getLabel();
//      }
//      tableModel.setColumns(columnNames, columnIds);
//
//      /** @type {qx.ui.table.Table } */
//      var table;
//      if (remoteTable.getHorizontallyScrollable()) {
//        table = new org.jspresso.framework.view.qx.EnhancedTable(tableModel);
//      } else {
//        // Customize the table column model. We want one that
//        // automatically resize columns.
//        var custom = {
//          tableColumnModel: function (obj) {
//            return new qx.ui.table.columnmodel.Resize(obj);
//          }
//        };
//        table = new org.jspresso.framework.view.qx.EnhancedTable(tableModel, custom);
//      }
//      table.setStatusBarVisible(false);
//      if (!remoteTable.getColumnReorderingAllowed()) {
//        table.getPaneScroller(0)._startMoveHeader = function (moveCol, pageX) {
//        };
//      }
//      var columnModel = table.getTableColumnModel();
//      for (var i = 0; i < remoteTable.getColumnIds().length; i++) {
//        var rColumn = remoteTable.getColumns()[i];
//        var rColumnHeader = remoteTable.getColumnHeaders()[i];
//        var editor = new org.jspresso.framework.view.qx.RComponentTableCellEditor(this, rColumn, this.__actionHandler);
//        columnModel.setCellEditorFactory(i, editor);
//        var bgIndex = -1;
//        var fgIndex = -1;
//        var foIndex = -1;
//        if (rColumn.getBackgroundState()) {
//          bgIndex = remoteTable.getRowPrototype().getChildren().indexOf(rColumn.getBackgroundState());
//        } else if (remoteTable.getBackgroundState()) {
//          bgIndex = remoteTable.getRowPrototype().getChildren().indexOf(remoteTable.getBackgroundState());
//        }
//        if (rColumn.getForegroundState()) {
//          fgIndex = remoteTable.getRowPrototype().getChildren().indexOf(rColumn.getForegroundState());
//        } else if (remoteTable.getForegroundState()) {
//          fgIndex = remoteTable.getRowPrototype().getChildren().indexOf(remoteTable.getForegroundState());
//        }
//        if (rColumn.getFontState()) {
//          foIndex = remoteTable.getRowPrototype().getChildren().indexOf(rColumn.getFontState());
//        } else if (remoteTable.getFontState()) {
//          foIndex = remoteTable.getRowPrototype().getChildren().indexOf(remoteTable.getFontState());
//        }
//        var cellRenderer = null;
//        if (rColumn instanceof org.jspresso.framework.gui.remote.RCheckBox) {
//          cellRenderer = new org.jspresso.framework.view.qx.BooleanTableCellRenderer();
//        } else if (rColumn instanceof org.jspresso.framework.gui.remote.RColorField) {
//          cellRenderer = new org.jspresso.framework.view.qx.ColorTableCellRenderer();
//        } else if (rColumn instanceof org.jspresso.framework.gui.remote.RComboBox) {
//          var labels = {};
//          var icons = {};
//          for (var j = 0; j < rColumn.getValues().length; j++) {
//            var value = rColumn.getValues()[j];
//            labels[value] = rColumn.getTranslations()[j];
//            icons[value] = rColumn.getIcons()[j];
//          }
//          cellRenderer = new org.jspresso.framework.view.qx.EnumerationTableCellRenderer(labels, icons);
//        } else if (rColumn instanceof org.jspresso.framework.gui.remote.RActionField && !rColumn.getShowTextField()) {
//          cellRenderer = new org.jspresso.framework.view.qx.BinaryTableCellRenderer();
//        } else if (rColumn instanceof org.jspresso.framework.gui.remote.RImageComponent) {
//          cellRenderer = new org.jspresso.framework.view.qx.ImageTableCellRenderer();
//          this.__remotePeerRegistry.register(rColumn.getAction());
//          cellRenderer.setAction(rColumn.getAction());
//        } else {
//          var format = this._createFormat(rColumn);
//          cellRenderer = new org.jspresso.framework.view.qx.FormattedTableCellRenderer(table, format);
//          cellRenderer.setUseAutoAlign(false);
//
//          if (rColumn instanceof org.jspresso.framework.gui.remote.RLink) {
//            this.__remotePeerRegistry.register(rColumn.getAction());
//            cellRenderer.setAction(rColumn.getAction());
//          }
//        }
//        if (cellRenderer) {
//          var alignment = null;
//          if (rColumn instanceof org.jspresso.framework.gui.remote.RLabel || rColumn
//              instanceof org.jspresso.framework.gui.remote.RTextField || rColumn
//              instanceof org.jspresso.framework.gui.remote.RNumericComponent) {
//            alignment = rColumn.getHorizontalAlignment();
//          }
//          if (alignment == "LEFT") {
//            alignment = "left";
//          } else if (alignment == "CENTER") {
//            alignment = "center";
//          } else if (alignment == "RIGHT") {
//            alignment = "right";
//          }
//
//          var additionalAttributes = {};
//          if (alignment) {
//            additionalAttributes["text-align"] = alignment;
//          }
//          if (bgIndex >= 0) {
//            additionalAttributes["backgroundIndex"] = bgIndex;
//          } else if (rColumn.getBackground()) {
//            additionalAttributes["background-color"] = org.jspresso.framework.view.qx.AbstractQxViewFactory._hexColorToQxColor(rColumn.getBackground());
//          }
//          if (fgIndex >= 0) {
//            additionalAttributes["foregroundIndex"] = fgIndex;
//          } else if (rColumn.getForeground()) {
//            additionalAttributes["color"] = org.jspresso.framework.view.qx.AbstractQxViewFactory._hexColorToQxColor(rColumn.getForeground());
//          }
//          if (foIndex >= 0) {
//            additionalAttributes["fontIndex"] = foIndex;
//          } else {
//            var rFont = rColumn.getFont();
//            if (rFont) {
//              if (rFont.getItalic()) {
//                additionalAttributes["font-style"] = "italic";
//              }
//              if (rFont.getBold()) {
//                additionalAttributes["font-weight"] = "bold";
//              }
//              if (rFont.getName()) {
//                additionalAttributes["font-family"] = rFont.getName();
//              }
//              if (rFont.getSize() > 0) {
//                additionalAttributes["font-size"] = rFont.getSize() + "px";
//              }
//            }
//          }
//
//          cellRenderer.setAdditionalAttributes(additionalAttributes);
//
//          columnModel.setDataCellRenderer(i, cellRenderer);
//          columnModel.setHeaderCellRenderer(i,
//              new org.jspresso.framework.view.qx.RComponentHeaderRenderer(table, this, rColumnHeader));
//        }
//        var columnWidth;
//        if (rColumn.getPreferredSize() && rColumn.getPreferredSize().getWidth() > 0) {
//          columnWidth = rColumn.getPreferredSize().getWidth();
//        } else {
//          var tableFont = qx.theme.manager.Font.getInstance().resolve("default");
//          var headerWidth = qx.bom.Label.getTextSize(columnNames[i], tableFont.getStyles()).width;
//          if (rColumn instanceof org.jspresso.framework.gui.remote.RCheckBox) {
//            columnWidth = headerWidth + 16;
//          } else {
//            var maxColumnWidth = qx.bom.Label.getTextSize(org.jspresso.framework.view.qx.AbstractQxViewFactory.__TEMPLATE_CHAR,
//                tableFont.getStyles()).width
//                * org.jspresso.framework.view.qx.AbstractQxViewFactory.__COLUMN_MAX_CHAR_COUNT;
//            var editorComponent = this.createComponent(rColumn, false);
//            columnWidth = maxColumnWidth;
//            if (editorComponent.getMaxWidth()) {
//              columnWidth = Math.min(maxColumnWidth, editorComponent.getMaxWidth());
//            }
//            columnWidth = Math.max(columnWidth, headerWidth + 16);
//          }
//        }
//        if (remoteTable.getHorizontallyScrollable()) {
//          columnModel.setColumnWidth(i, columnWidth);
//        } else {
//          columnModel.getBehavior().setWidth(i, columnWidth, columnWidth < 50 ? 0 : columnWidth);
//        }
//        columnToolTips[i] = -1;
//        if (rColumn.getToolTipState()) {
//          columnToolTips[i] = remoteTable.getRowPrototype().getChildren().indexOf(rColumn.getToolTipState());
//        }
//      }
//      tableModel.setDynamicToolTipIndices(columnToolTips);
//      table.addListener("cellClick", function (e) {
//        var col = e.getColumn();
//        var renderer = table.getTableColumnModel().getDataCellRenderer(col);
//        if ((    renderer instanceof org.jspresso.framework.view.qx.FormattedTableCellRenderer || renderer
//            instanceof org.jspresso.framework.view.qx.ImageTableCellRenderer) && renderer.getAction()) {
//          this.__actionHandler.execute(renderer.getAction());
//        }
//      }, this);
//
//      table.setHeight(5 * table.getRowHeight() + table.getHeaderCellHeight());
//      var selectionModel = table.getSelectionModel();
//      if (remoteTable.getSelectionMode() == "SINGLE_SELECTION") {
//        selectionModel.setSelectionMode(qx.ui.table.selection.Model.SINGLE_SELECTION);
//      } else if (remoteTable.getSelectionMode() == "SINGLE_INTERVAL_SELECTION") {
//        selectionModel.setSelectionMode(qx.ui.table.selection.Model.SINGLE_INTERVAL_SELECTION);
//      } else if (remoteTable.getSelectionMode() == "MULTIPLE_INTERVAL_SELECTION") {
//        selectionModel.setSelectionMode(qx.ui.table.selection.Model.MULTIPLE_INTERVAL_SELECTION);
//      } else if (remoteTable.getSelectionMode() == "MULTIPLE_INTERVAL_CUMULATIVE_SELECTION") {
//        selectionModel.setSelectionMode(qx.ui.table.selection.Model.MULTIPLE_INTERVAL_SELECTION_TOGGLE);
//      } else if (remoteTable.getSelectionMode() == "SINGLE_CUMULATIVE_SELECTION") {
//        selectionModel.setSelectionMode(qx.ui.table.selection.Model.SINGLE_SELECTION);
//      } else {
//        selectionModel.setSelectionMode(qx.ui.table.selection.Model.MULTIPLE_INTERVAL_SELECTION);
//      }
//      selectionModel.addListener("changeSelection", function (e) {
//        if (selectionModel.hasBatchMode()) {
//          // TODO notify Qooxdoo batchMode is not working.
//          return;
//        }
//        var leadingIndex = tableModel.viewIndexToModelIndex(selectionModel.getLeadSelectionIndex());
//        var selectedRanges = selectionModel.getSelectedRanges();
//
//        var selectedIndices = [];
//        for (var rangeIndex = 0; rangeIndex < selectedRanges.length; rangeIndex++) {
//          var range = selectedRanges[rangeIndex];
//          for (var i = range.minIndex; i < range.maxIndex + 1; i++) {
//            selectedIndices.push(i);
//          }
//        }
//        selectedIndices = tableModel.viewIndicesToModelIndices(selectedIndices);
//        if (selectedIndices.length == 0) {
//          leadingIndex = -1;
//        }
//        var stateSelection = state.getSelectedIndices();
//        if (!stateSelection) {
//          stateSelection = [];
//        }
//        if (!qx.lang.Array.equals(selectedIndices, stateSelection) || leadingIndex != state.getLeadingIndex()) {
//          if (selectedIndices.length == 0) {
//            selectedIndices = null;
//          }
//          state.setLeadingIndex(leadingIndex);
//          state.setSelectedIndices(selectedIndices);
//        }
//        // workaround to update cell rendering
//        table.updateContent();
//      }, this);
//
//      state.addListener("changeSelectedIndices", function (e) {
//        var stateSelection = tableModel.modelIndicesToViewIndices(state.getSelectedIndices());
//        if (!stateSelection) {
//          stateSelection = [];
//        }
//
//        var selectedRanges = selectionModel.getSelectedRanges();
//        var selectedIndices = [];
//        for (var rangeIndex = 0; rangeIndex < selectedRanges.length; rangeIndex++) {
//          var range = selectedRanges[rangeIndex];
//          for (var i = range.minIndex; i < range.maxIndex + 1; i++) {
//            selectedIndices.push(i);
//          }
//        }
//
//        var stateLeadingIndex = tableModel.modelIndexToViewIndex(state.getLeadingIndex());
//        var leadingIndex = selectionModel.getLeadSelectionIndex();
//        if (selectedIndices.length == 0) {
//          leadingIndex = -1;
//        }
//
//        if (!qx.lang.Array.equals(selectedIndices, stateSelection) || leadingIndex != stateLeadingIndex) {
//          selectionModel.setBatchMode(true);
//          selectionModel.resetSelection();
//          if (stateSelection.length > 0) {
//            var minIndex = -1;
//            var maxIndex = minIndex;
//            for (var i = 0; i < stateSelection.length; i++) {
//              if (minIndex < 0) {
//                minIndex = stateSelection[i];
//                maxIndex = minIndex;
//              } else {
//                var nextSelectedIndex = stateSelection[i];
//                if (nextSelectedIndex == maxIndex + 1) {
//                  maxIndex = nextSelectedIndex;
//                } else {
//                  selectionModel.addSelectionInterval(minIndex, maxIndex);
//                  minIndex = nextSelectedIndex;
//                  maxIndex = minIndex;
//                }
//              }
//            }
//            selectionModel.addSelectionInterval(minIndex, maxIndex);
//          }
//          if (stateLeadingIndex >= 0) {
//            selectionModel.addSelectionInterval(stateLeadingIndex, stateLeadingIndex);
//            table.setFocusedCell(0, stateLeadingIndex, true);
//          }
//          selectionModel.setBatchMode(false);
//        }
//      }, this);
//      if (remoteTable.getRowAction()) {
//        this.__remotePeerRegistry.register(remoteTable.getRowAction());
//        table.addListener("dblclick", function (e) {
//          this.__actionHandler.execute(remoteTable.getRowAction());
//        }, this);
//      }
//      if (remoteTable.getPermId()) {
//        var notifyTableChanged = function (event) {
//          var notificationCommand = new org.jspresso.framework.application.frontend.command.remote.RemoteTableChangedCommand();
//          notificationCommand.setTableId(remoteTable.getPermId());
//          var columnIds = [];
//          var columnWidths = [];
//          for (var ci = 0; ci < table.getTableColumnModel().getOverallColumnCount(); ci++) {
//            columnIds.push(table.getTableModel().getColumnId(table.getTableColumnModel().getOverallColumnAtX(ci)));
//            columnWidths.push(table.getTableColumnModel().getColumnWidth(table.getTableColumnModel().getOverallColumnAtX(ci)));
//          }
//          notificationCommand.setColumnIds(columnIds);
//          notificationCommand.setColumnWidths(columnWidths);
//          //noinspection JSPotentiallyInvalidUsageOfThis
//          this.__commandHandler.registerCommand(notificationCommand);
//        };
//
//        table.getTableColumnModel().addListener("widthChanged", notifyTableChanged, this);
//        table.getTableColumnModel().addListener("orderChanged", notifyTableChanged, this);
//      }
//      return table;
    },

    /**
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @param component {qx.ui.mobile.core.Widget}
     * @return {qx.ui.mobile.core.Widget}
     */
    _decorateWithActions: function (remoteComponent, component) {
      if (remoteComponent instanceof org.jspresso.framework.gui.remote.RTextField || remoteComponent
          instanceof org.jspresso.framework.gui.remote.RDateField || remoteComponent
          instanceof org.jspresso.framework.gui.remote.RNumericComponent || remoteComponent
          instanceof org.jspresso.framework.gui.remote.RLabel || remoteComponent
          instanceof org.jspresso.framework.gui.remote.RTimeField || remoteComponent
          instanceof org.jspresso.framework.gui.remote.RComboBox || remoteComponent
          instanceof org.jspresso.framework.gui.remote.RCheckBox) {
        return this._decorateWithAsideActions(component, remoteComponent, false);
      } else {
        return component;
      }
    }

  }
});
