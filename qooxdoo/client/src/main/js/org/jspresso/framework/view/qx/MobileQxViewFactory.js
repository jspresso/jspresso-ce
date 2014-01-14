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
     * @return {qx.ui.mobile.toolbar.Button}
     * @param toolTip {String}
     * @param label {String}
     * @param icon {org.jspresso.framework.gui.remote.RIcon}
     */
    createToolBarButton: function (label, toolTip, icon) {
      var button = new qx.ui.mobile.toolbar.Button();
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
            futureDeselections[i].setLeadingIndex(-1);
            futureDeselections[i].setSelectedIndices(null);
          }
        }
        for(var i = 0; i < futureSelections.length ; i++) {
          if(futureSelections[i]) {
            futureSelections[i].state.setLeadingIndex(futureSelections[i].selection[0]);
            futureSelections[i].state.setSelectedIndices(futureSelections[i].selection);
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
      var borderContainer = this._createVBoxContainer();
      if (remoteBorderContainer.getNorth()) {
        var child = this.createComponent(remoteBorderContainer.getNorth());
        borderContainer.add(child);
      }
      var middleContainer = this._createHBoxContainer();
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
        var actionField = this._createHBoxContainer();

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
      var tableListModel = remoteTable.getState().getChildren();

      var tableList = new qx.ui.mobile.list.List({
        configureItem: function (item, data, row) {
          item.setTitle(data.getValue());
          item.setSubtitle(data.getDescription());
          item.setImage(data.getIconImageUrl());
          item.setShowArrow(true);
        }
      });

      tableList.setModel(tableListModel);

      tableList.addListener("changeSelection", function(evt) {
        var selectedIndex = evt.getData();
        remoteTable.getState().setLeadingIndex(selectedIndex);
        remoteTable.getState().setSelectedIndices([selectedIndex]);
      }, this);
      return tableList;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param rComponent {org.jspresso.framework.gui.remote.RComponent}
     */
    _createFormattedField: function (rComponent) {
      var formattedField = new qx.ui.mobile.form.TextField();
      this._bindFormattedField(formattedField, rComponent);
      return formattedField;
    },

    /**
     *
     * @param expectedCharCount {Integer}
     * @param component {qx.ui.mobile.core.Widget}
     * @param maxCharCount {Integer}
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @return {undefined}
     */
    _sizeMaxComponentWidth: function (component, remoteComponent, expectedCharCount, maxCharCount) {
      // NO-OP
    },

    /**
     * @return {undefined}
     * @param component {qx.ui.mobile.core.Widget}
     * @param alignment {String}
     */
    _configureHorizontalAlignment: function (component, alignment) {
      // NO-OP
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     * @param remoteLabel {org.jspresso.framework.gui.remote.RLabel}
     */
    _createLabel: function (remoteLabel) {
      var atom = new qx.ui.mobile.basic.Atom();
      var label = atom.getLabelWidget();
      var state = remoteLabel.getState();
      if (state) {
        var modelController = new qx.data.controller.Object(state);
        if (remoteLabel instanceof org.jspresso.framework.gui.remote.RLink && remoteLabel.getAction()) {
          this.__remotePeerRegistry.register(remoteLabel.getAction());
          atom.setRich(true);
          modelController.addTarget(atom, "label", "value", false, {
            converter: function (modelValue, model) {
              if (modelValue) {
                return "<u><a href='javascript:'>" + modelValue + "</a></u>";
              }
              return modelValue;
            }
          });
          atom.addListener("tap", function (event) {
            this.__actionHandler.execute(remoteLabel.getAction());
          }, this);
        } else {
          modelController.addTarget(atom, "label", "value", false, {
            converter: function (modelValue, model) {
              return modelValue;
            }
          });
        }
      } else {
        atom.setLabel(remoteLabel.getLabel());
      }
      this._configureHorizontalAlignment(label, remoteLabel.getHorizontalAlignment());
      if (remoteLabel.getIcon()) {
        atom.setIcon(remoteLabel.getIcon().getImageUrlSpec());
      }
      return atom;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     */
    _createHBoxContainer: function () {
      var hboxContainer = new qx.ui.mobile.container.Composite();
      hboxContainer.setLayout(new qx.ui.mobile.layout.HBox());
      return hboxContainer;
    },

    /**
     * @return {qx.ui.mobile.core.Widget}
     */
    _createVBoxContainer: function () {
      var hboxContainer = new qx.ui.mobile.container.Composite();
      hboxContainer.setLayout(new qx.ui.mobile.layout.VBox());
      return hboxContainer;
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remoteDateField {org.jspresso.framework.gui.remote.RDateField}
     */
    _createDateField: function (remoteDateField) {
      var dateField = this._createFormattedField(remoteDateField);
      return dateField;
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remoteColorField {org.jspresso.framework.gui.remote.RColorField}
     */
    _createColorField: function (remoteColorField) {
      var colorField = this._createFormattedField(remoteColorField);
      return colorField;
    },

    _decorateWithToolbars: function (component, remoteComponent) {
      var decorated = component;
      var toolBar;
      var secondaryToolBar;
      if (!(remoteComponent instanceof org.jspresso.framework.gui.remote.RActionField)
          && remoteComponent.getActionLists() != null) {
        toolBar = this._createToolBar(remoteComponent, component);
      } else {
        toolBar = this._createDefaultToolBar(remoteComponent, component);
      }
      if (remoteComponent.getSecondaryActionLists()) {
        secondaryToolBar = this._createSecondaryToolBar(remoteComponent, component);
      }
      if (toolBar || secondaryToolBar) {
        var surroundingBox = this._createVBoxContainer();
        if (toolBar) {
          surroundingBox.add(toolBar);
        }
        surroundingBox.add(component, {
          flex: 1
        });
        if (secondaryToolBar) {
          surroundingBox.add(secondaryToolBar);
        }
        decorated = surroundingBox;
      }
      return decorated;
    },

    createToolBarFromActionLists: function (actionLists) {
      if (actionLists && actionLists.length > 0) {
        var toolBar = new qx.ui.mobile.toolbar.ToolBar();
        this.installActionLists(toolBar, actionLists);
        return toolBar;
      }
      return null;
    },

    installActionLists: function (toolBar, actionLists) {
      if (actionLists) {
        for (var i = 0; i < actionLists.length; i++) {
          var actionList = actionLists[i];
          if (actionList.getActions() != null) {
            for (var j = 0; j < actionList.getActions().length; j++) {
              toolBar.add(this.createToolBarAction(actionList.getActions()[j]));
            }
            toolBar.add(new qx.ui.mobile.toolbar.Separator());
          }
        }
      }
    }











  }
});
