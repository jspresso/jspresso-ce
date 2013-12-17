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
      var form = new qx.ui.mobile.form.Form();
      for (var i = 0; i < remoteForm.getElements().length; i++) {
        var rComponent = remoteForm.getElements()[i];
        var component = /** @type {qx.ui.form.IForm} */ this.createComponent(rComponent);

        form.add(component, rComponent.getLabel());
      }
      return new qx.ui.mobile.form.renderer.Single(form);
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
    }




  }
});
