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
 *
 * @asset(qx/icon/Oxygen/16/actions/dialog-close.png)
 * @asset(qx/icon/Oxygen/16/actions/format-*.png)
 * @asset(qx/icon/Oxygen/16/actions/edit-*.png)
 * @asset(qx/icon/Oxygen/16/actions/insert-image.png)
 * @asset(qx/icon/Oxygen/16/actions/insert-link.png)
 * @asset(qx/icon/Oxygen/16/actions/insert-text.png)
 *
 * @asset(org/jspresso/framework/htmleditor/list-*.png)
 */

qx.Class.define("org.jspresso.framework.view.qx.DefaultQxViewFactory", {
  extend: org.jspresso.framework.view.qx.AbstractQxViewFactory,

  construct: function (remotePeerRegistry, actionHandler, commandHandler) {
    this.base(arguments, remotePeerRegistry, actionHandler, commandHandler);
  },

  members: {

    /**
     *
     * @return {qx.ui.form.Button}
     * @param toolTip {String}
     * @param label {String}
     * @param icon {org.jspresso.framework.gui.remote.RIcon}
     */
    createButton: function (label, toolTip, icon) {
      var button = new qx.ui.form.Button();
      this._completeButton(button, label, toolTip, icon);
      return button;
    },

    /**
     *
     * @return {undefined}
     * @param icon {org.jspresso.framework.gui.remote.RIcon}
     * @param button {qx.ui.form.Button | qx.ui.menu.Button}
     * @param label {String}
     * @param toolTip {String}
     */
    _completeButton: function (button, label, toolTip, icon) {
      this.setIcon(button, icon);
      if (label) {
        button.setLabel(label);
      }
      if (toolTip) {
        button.setToolTip(new qx.ui.tooltip.ToolTip(toolTip));
        //to unblock tooltips on menu buttons
        button.setBlockToolTip(false);
      }
    },

    /**
     * @param button {qx.ui.form.Button|qx.ui.menu.Button}
     * @param listener {function}
     * @param that {var}
     */
    addButtonListener: function(button, listener, that) {
      button.addListener("execute", listener, that);
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteForm {org.jspresso.framework.gui.remote.RForm}
     */
    _createForm: function (remoteForm) {
      var form = new qx.ui.container.Composite();
      var formLayout = new qx.ui.layout.Grid(5, 5);
      var columnCount = remoteForm.getColumnCount();
      var col = 0;
      var row = 0;
      var extraRowOffset = 0;
      var lastRow = 0;
      var lastCol = 0;

      form.setLayout(formLayout);

      for (var i = 0; i < remoteForm.getElements().length; i++) {
        var elementWidth = remoteForm.getElementWidths()[i];
        var labelHorizontalPosition = remoteForm.getLabelHorizontalPositions()[i];
        var rComponent = remoteForm.getElements()[i];
        var component = this.createComponent(rComponent);
        /** @type {qx.ui.basic.Label } */
        var componentLabel;
        var labelRow;
        var labelCol;
        var labelColSpan;
        var compRow;
        var compCol;
        var compColSpan;
        var compRowSpan;

        this._bindDynamicToolTip(component, rComponent);
        this._bindDynamicBackground(component, rComponent);
        this._bindDynamicForeground(component, rComponent);
        this._bindDynamicFont(component, rComponent);

        if (remoteForm.getLabelsPosition() != "NONE") {
          componentLabel = /** @type {qx.ui.basic.Label } */ this.createComponent(remoteForm.getElementLabels()[i],
              false);
        }

        if (elementWidth > columnCount) {
          elementWidth = columnCount;
        }
        if (col + elementWidth > columnCount) {
          col = 0;
          row += (1 + extraRowOffset);
          extraRowOffset = 0;
        }

        if (remoteForm.getLabelsPosition() == "ABOVE") {
          labelRow = row * 2;
          labelCol = col;
          labelColSpan = elementWidth;
          compRow = labelRow + 1;
          compCol = labelCol;
          compColSpan = elementWidth;
        } else if (remoteForm.getLabelsPosition() == "ASIDE") {
          labelRow = row;
          labelColSpan = 1;
          compRow = labelRow;
          compColSpan = elementWidth * 2 - 1;
          if (labelHorizontalPosition == "RIGHT") {
            compCol = col * 2;
            labelCol = compCol + compColSpan;
          } else {
            labelCol = col * 2;
            compCol = labelCol + 1;
          }
        } else if (remoteForm.getLabelsPosition() == "NONE") {
          compRow = row;
          compCol = col;
          compColSpan = elementWidth;
        }
        if (remoteForm.getLabelsPosition() != "NONE") {
          if (remoteForm.getLabelsPosition() == "ASIDE") {
            if (labelHorizontalPosition == "RIGHT") {
              componentLabel.setAlignX("left");
            } else {
              componentLabel.setAlignX("right");
            }
            componentLabel.setAlignY("middle");
          } else {
            componentLabel.setAlignX("left");
            componentLabel.setAlignY("bottom");
          }
          componentLabel.setAllowStretchX(false);
          componentLabel.setAllowStretchY(false);
          form.add(componentLabel, {
            row: labelRow,
            column: labelCol,
            rowSpan: 1,
            colSpan: labelColSpan
          });
        }
        component.setAllowShrinkX(true);
        if (remoteForm.getLabelsPosition() == "ASIDE") {
          if (labelHorizontalPosition == "RIGHT") {
            component.setAlignX("right");
          } else {
            component.setAlignX("left");
          }
        } else {
          component.setAlignX("left");
        }
        component.setAlignY("middle");
        if (rComponent instanceof org.jspresso.framework.gui.remote.RLabel) {
          component.setAllowGrowX(true);
        } else {
          component.setAllowGrowX(false);
        }
        if (this._isMultiline(rComponent)) {
          compRowSpan = 2;
          extraRowOffset = 1;
          formLayout.setRowFlex(compRow + 1, 1);
          if (compColSpan > 1) {
            component.setMaxWidth(null);
          }
        } else {
          compRowSpan = 1;
        }

        form.add(component, {
          row: compRow,
          column: compCol,
          rowSpan: compRowSpan,
          colSpan: compColSpan
        });

        if (compColSpan > 1 && component.getWidth() == null/*to cope with preferred width*/) {
          component.setAllowGrowX(true);
        }
        col += elementWidth;
        formLayout.setColumnFlex(compCol, 1);
        lastRow = compRow;
        if (Math.max(compCol + compColSpan, labelCol + labelColSpan) > lastCol) {
          lastCol = Math.max(compCol + compColSpan, labelCol + labelColSpan);
        }
      }
      var filler = new qx.ui.core.Widget();
      filler.setMinWidth(0);
      filler.setWidth(0);
      filler.setMinHeight(0);
      filler.setHeight(0);
      filler.setAllowStretchX(true);
      filler.setAllowStretchY(false);
      form.add(filler, {
        row: 0,
        column: lastCol,
        rowSpan: lastRow + 1,
        colSpan: 1
      });
      formLayout.setColumnFlex(lastCol, 10);
      // Special toolTip handling
      var state = remoteForm.getState();
      var modelController = new qx.data.controller.Object(state);
      var toolTip = new qx.ui.tooltip.ToolTip();
      toolTip.setRich(true);
      modelController.addTarget(toolTip, "label", "value", false, {
        converter: this._modelToViewFieldConverter
      });
      form.setToolTip(toolTip);
      var decoratedForm = form;
      if (remoteForm.getVerticallyScrollable()) {
        var scrollContainer = new qx.ui.container.Scroll();
        scrollContainer.setScrollbarX("off");
        scrollContainer.setScrollbarY("auto");
        scrollContainer.add(form);
        scrollContainer.setWidth(form.getWidth());
        scrollContainer.setHeight(form.getHeight());
        decoratedForm = scrollContainer;
      }
      return decoratedForm;
    },

    /**
     * @return {qx.ui.core.Widget}
     */
    _createDefaultComponent: function () {
      return new qx.ui.core.Widget();
    },

    /**
     *
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @param component {qx.ui.core.Widget}
     * @return {undefined}
     */
    _configureToolTip: function (remoteComponent, component) {
      if (remoteComponent.getToolTip() != null && component.getToolTip() == null) {
        var toolTip = new qx.ui.tooltip.ToolTip(remoteComponent.getToolTip());
        toolTip.setRich(true);
        component.setToolTip(toolTip);
      }
    },

    /**
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @param component {qx.ui.core.Widget}
     * @return {qx.ui.core.Widget}
     */
    _decorateWithBorder: function (remoteComponent, component) {
      var decorator = component;
      if (remoteComponent.getBorderType() && remoteComponent.getBorderType() != "NONE") {
        if (remoteComponent.getBorderType() == "TITLED") {
          decorator = new collapsablepanel.Panel(remoteComponent.getLabel());
          this.setIcon(decorator.getChildControl("bar"), remoteComponent.getIcon())
        } else {
          decorator = new qx.ui.groupbox.GroupBox();
          decorator.setLegend(remoteComponent.getLabel());
          this.setIcon(decorator.getChildControl("legend"), remoteComponent.getIcon());
        }
        decorator.setLayout(new qx.ui.layout.Grow());
        decorator.add(component);
      }
      return decorator;
    },

    /**
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @param component {qx.ui.core.Widget}
     */
    _applyPreferredSize: function (remoteComponent, component) {
      var preferredSize = remoteComponent.getPreferredSize();
      if (preferredSize) {
        if (preferredSize.getWidth() > 0) {
          component.setWidth(preferredSize.getWidth());
        }
        if (preferredSize.getHeight() > 0) {
          component.setHeight(preferredSize.getHeight());
        }
      }
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteTextField  {org.jspresso.framework.gui.remote.RTextField}
     */
    _createTextField: function (remoteTextField) {
      var textField = new qx.ui.form.TextField();
      if (window.clipboardData) {
        // We are in IE
        textField.addListener("appear", function (appearEvent) {
          var input = textField.getContentElement().getDomElement();
          input["onpaste"] = function (pasteEvent) {
            var cbData = window.clipboardData.getData("Text");
            if (cbData) {
              cbData = cbData.replace(/(\r|\n)+/g, " ");
              window.clipboardData.setData("Text", cbData);
            }
          }
        }, this);
      }

      if (remoteTextField.getMaxLength() > 0) {
        textField.setMaxLength(remoteTextField.getMaxLength());
        this._sizeMaxComponentWidth(textField, remoteTextField, remoteTextField.getMaxLength() + 2);
      } else {
        this._sizeMaxComponentWidth(textField, remoteTextField);
      }
      this._configureHorizontalAlignment(textField, remoteTextField.getHorizontalAlignment());
      this._bindTextField(remoteTextField, textField);
      return textField;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remotePasswordField {org.jspresso.framework.gui.remote.RPasswordField}
     */
    _createPasswordField: function (remotePasswordField) {
      var passwordField = new qx.ui.form.PasswordField();
      if (remotePasswordField.getMaxLength() > 0) {
        passwordField.setMaxLength(remotePasswordField.getMaxLength());
      }
      this._sizeMaxComponentWidth(passwordField, remotePasswordField);
      this._bindTextField(remotePasswordField, passwordField);
      return passwordField;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteComboBox {org.jspresso.framework.gui.remote.RComboBox}
     */
    _createComboBox: function (remoteComboBox) {
      if (remoteComboBox.getReadOnly()) {
        var atom = new qx.ui.basic.Atom();
        var state = remoteComboBox.getState();

        var labels = {};
        var icons = {};
        for (var i = 0; i < remoteComboBox.getValues().length; i++) {
          labels[remoteComboBox.getValues()[i]] = remoteComboBox.getTranslations()[i];
          icons[remoteComboBox.getValues()[i]] = remoteComboBox.getIcons()[i];
        }

        var synchAtomValue = function (value) {
          if (value) {
            /** @type {String } */
            var label = labels[value];
            var icon = icons[value];
            if (label) {
              atom.setLabel(label);
            } else {
              atom.setLabel("");
            }
            if (icon) {
              atom.setIcon(icon.getImageUrlSpec());
            } else {
              atom.setIcon("");
            }
          } else {
            atom.setLabel("");
            atom.setIcon("");
          }
        };
        synchAtomValue(state.getValue());

        state.addListener("changeValue", function (e) {
          synchAtomValue(e.getData());
        }, this);
        return atom;
      } else {
        var comboBox = new qx.ui.form.SelectBox();
        comboBox.setAllowStretchY(false, false);
        var iconDim;
        var width = 0;
        for (var i = 0; i < remoteComboBox.getValues().length; i++) {
          if (i == 0 && remoteComboBox.getValues()[i].length > 0) {
            // Qx combos do not support null values
            var fallbackLi = new qx.ui.form.ListItem(" ");
            fallbackLi.setModel("");
            this.setIcon(fallbackLi, null);
            comboBox.add(fallbackLi);
          }
          var tr = remoteComboBox.getTranslations()[i];
          var li = new qx.ui.form.ListItem(tr);
          li.setModel(remoteComboBox.getValues()[i]);
          var rIcon = remoteComboBox.getIcons()[i];
          this.setIcon(li, rIcon);
          comboBox.add(li);
          if (!iconDim && rIcon && rIcon.getDimension()) {
            iconDim = rIcon.getDimension();
          }
          if (tr.length > width) {
            width = tr.length;
          }
        }
        this._sizeMaxComponentWidth(comboBox, remoteComboBox, width);
        var extraWidth = 25;
        if (iconDim) {
          extraWidth += iconDim.getWidth();
        }
        comboBox.setMaxWidth(comboBox.getMaxWidth() + extraWidth);
        comboBox.setWidth(comboBox.getMaxWidth());
        comboBox.setMinWidth(comboBox.getMaxWidth());
        this._bindComboBox(remoteComboBox, comboBox);
        return comboBox;
      }
    },

    /**
     * @param remoteComboBox  {org.jspresso.framework.gui.remote.RComboBox}
     * @param comboBox {qx.ui.form.SelectBox}
     */
    _bindComboBox: function (remoteComboBox, comboBox) {
      var state = remoteComboBox.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(comboBox, "modelSelection", "value", false, {
        converter: function (modelValue, model) {
          return [modelValue];
        }
      });
      comboBox.getModelSelection().addListener("change", function (e) {
        var modelSelection = e.getTarget();
        if (modelSelection.length > 0) {
          state.setValue(modelSelection.getItem(0));
        } else {
          state.setValue(null);
        }
      });
      modelController.addTarget(comboBox, "enabled", "writable", false);
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteCheckBox {org.jspresso.framework.gui.remote.RCheckBox}
     */
    _createCheckBox: function (remoteCheckBox) {
      var checkBox = new qx.ui.form.CheckBox();
      if (remoteCheckBox.getTriState()) {
        checkBox.setTriState(true);
      }
      this._sizeMaxComponentWidth(checkBox, remoteCheckBox, 2, 2);
      this._bindCheckBox(remoteCheckBox, checkBox);
      return checkBox;
    },

    /**
     * @param root {qx.ui.core.Widget}
     * @return {qx.ui.core.Widget}
     */
    _findFirstFocusableComponent: function (root) {
      if (root instanceof qx.ui.form.TextField || root instanceof qx.ui.form.CheckBox || root
          instanceof qx.ui.form.SelectBox || root instanceof qx.ui.form.TextArea || root instanceof qx.ui.form.DateField
          || root instanceof qx.ui.table.Table) {
        if (root.getEnabled()) {
          return root;
        }
      }
      var r;
      if (root instanceof qx.ui.container.Composite || root instanceof qx.ui.splitpane.Pane || root
          instanceof qx.ui.tabview.TabView) {
        r = /** @type {qx.ui.core.MChildrenHandling } */ root;
        for (var i = 0; i < r.getChildren().length; i++) {
          var child = r.getChildren()[i];
          if (child instanceof qx.ui.core.Widget) {
            var focusableChild = this._findFirstFocusableComponent(/** @type {qx.ui.core.Widget } */ child);
            if (focusableChild != null) {
              return focusableChild;
            }
          }
        }
      }
      return null;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteTree {org.jspresso.framework.gui.remote.RTree}
     */
    _createTree: function (remoteTree) {
      var tree = new qx.ui.tree.Tree();
      var state = remoteTree.getState();
      var treeController = new qx.data.controller.Tree();
      treeController.setChildPath("children");
      treeController.setLabelPath("value");
      treeController.setIconPath("iconImageUrl");
      treeController.setDelegate({
        createItem: function () {
          var item = new qx.ui.tree.TreeFolder();
          item.getChildControl("label").setRich(true);
          return item;
        },
        bindItem: function (controller, treeNode, modelNode) {
          controller.bindProperty(controller.getLabelPath(), "label", controller.getLabelOptions(), treeNode,
              modelNode);
          controller.bindProperty(controller.getIconPath(), "icon", controller.getIconOptions(), treeNode, modelNode);
          if (modelNode) {
            modelNode.addListener("changeSelectedIndices", function (e) {
              /** @type {qx.data.Array } */
              var viewSelection = controller.getSelection();
              var stateSelection = e.getTarget().getSelectedIndices();
              var stateChildren = e.getTarget().getChildren();
              var selIndex = 0;
              for (var i = 0; i < stateChildren.length; i++) {
                var child = stateChildren.getItem(i);
                if (stateSelection && qx.lang.Array.contains(stateSelection, i)) {
                  if (!viewSelection.contains(child)) {
                    if (!treeNode.getOpen()) {
                      treeNode.setOpen(true);
                    }
                    // viewSelection.push(child);
                    if (selIndex == 0/*
                     * ||
                     * tree.getSelectionMode() ==
                     * "multi" ||
                     * tree.getSelectionMode() ==
                     * "additive"
                     */) {
                      viewSelection.setItem(selIndex, child);
                      selIndex++;
                    }
                  }
                } else {
                  if (viewSelection.contains(child)) {
                    viewSelection.remove(child);
                  }
                }
              }
            }, this);
          }
        }
      });
      treeController.setModel(state);
      treeController.setTarget(tree);
      if (remoteTree.getExpanded()) {
        this._expandAllChildren(tree, tree.getRoot());
      } else {
        tree.getRoot().setOpen(true);
      }

      treeController.addListener("changeSelection", function (e) {
        /** @type {qx.data.Array } */
        var selectedItems = e.getData();
        /** @type {org.jspresso.framework.state.remote.RemoteCompositeValueState } */
        var rootState = e.getTarget().getModel();
        var deselectedStates = [];
        var selectedStates = [];
        this._synchTreeViewSelection(rootState, selectedItems, deselectedStates, selectedStates);
        for (var i = 0; i < deselectedStates.length; i++) {
          var deselectedState = deselectedStates[i];
          deselectedState.setLeadingIndex(-1);
          deselectedState.setSelectedIndices(null);
        }
        for (var i = 0; i < selectedStates.length; i += 3) {
          var selectedState = selectedStates[i];
          selectedState.setLeadingIndex(selectedStates[i + 1]);
          selectedState.setSelectedIndices(selectedStates[i + 2]);
        }
      }, this);
      if (remoteTree.getRowAction()) {
        this.__remotePeerRegistry.register(remoteTree.getRowAction());
        tree.addListener("dblclick", function (e) {
          this.__actionHandler.execute(remoteTree.getRowAction());
        }, this);
      }
      return tree;
    },

    /**
     *
     * @param tree {qx.ui.tree.Tree}
     * @param treeItem {qx.ui.tree.core.AbstractItem}
     */
    _expandAllChildren: function (tree, treeItem) {
      treeItem.setOpen(true);
      if (treeItem.getChildren() != null) {
        for (var i = 0; i < treeItem.getChildren().length; i++) {
          this._expandAllChildren(tree, treeItem.getChildren()[i]);
        }
      }
    },

    /**
     *
     * @param state {org.jspresso.framework.state.remote.RemoteCompositeValueState}
     * @param selectedItems {qx.ui.tree.core.AbstractItem[]}
     * @param selectedStates {org.jspresso.framework.state.remote.RemoteCompositeValueState[]}
     * @param deselectedStates {org.jspresso.framework.state.remote.RemoteCompositeValueState[]}
     */
    _synchTreeViewSelection: function (state, selectedItems, deselectedStates, selectedStates) {
      var selectedIndices = [];
      var stateChildren = state.getChildren();
      var stateSelectedIndices = state.getSelectedIndices();
      var stateLeadingIndex = state.getLeadingIndex();
      for (var i = 0; i < stateChildren.length; i++) {
        var child = stateChildren.getItem(i);
        if (selectedItems.contains(child)) {
          selectedIndices.push(i);
        }
        this._synchTreeViewSelection(child, selectedItems, deselectedStates, selectedStates);
      }
      if (selectedIndices.length == 0) {
        if (stateSelectedIndices != null && stateSelectedIndices.length != 0) {
          deselectedStates.push(state);
        }
      } else {
        var leadingIndex = selectedIndices[selectedIndices.length - 1];
        if (stateSelectedIndices == null || !qx.lang.Array.equals(selectedIndices, stateSelectedIndices)
            || stateLeadingIndex != leadingIndex) {
          selectedStates.push(state);
          selectedStates.push(leadingIndex);
          selectedStates.push(selectedIndices);
        }
      }
    },

    /**
     * @return {qx.ui.core.Widget}
     */
    _createCardContainerComponent: function () {
      return new qx.ui.container.Stack();
    },

    /**
     *
     * @return {undefined}
     * @param cardContainer {qx.ui.container.Stack}
     * @param selectedCard  {qx.ui.core.Widget}
     */
    _selectCard: function (cardContainer, selectedCard) {
      cardContainer.setSelection([selectedCard]);
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteBorderContainer {org.jspresso.framework.gui.remote.RBorderContainer}
     */
    _createBorderContainer: function (remoteBorderContainer) {
      var borderContainer = new qx.ui.container.Composite();
      var borderLayout = new qx.ui.layout.Dock();
      borderLayout.setSort("y");
      borderContainer.setLayout(borderLayout);
      if (remoteBorderContainer.getNorth()) {
        var child = this.createComponent(remoteBorderContainer.getNorth());
        borderContainer.add(child, {
          edge: "north"
        });
      }
      if (remoteBorderContainer.getWest()) {
        var child = this.createComponent(remoteBorderContainer.getWest());
        borderContainer.add(child, {
          edge: "west"
        });
      }
      if (remoteBorderContainer.getCenter()) {
        var child = this.createComponent(remoteBorderContainer.getCenter());
        borderContainer.add(child, {
          edge: "center"
        });
      }
      if (remoteBorderContainer.getEast()) {
        var child = this.createComponent(remoteBorderContainer.getEast());
        borderContainer.add(child, {
          edge: "east"
        });
      }
      if (remoteBorderContainer.getSouth()) {
        var child = this.createComponent(remoteBorderContainer.getSouth());
        borderContainer.add(child, {
          edge: "south"
        });
      }
      return borderContainer;
    },

    /**
     *
     * @return {qx.ui.core.Widget}
     * @param remoteSecurityComponent {org.jspresso.framework.gui.remote.RSecurityComponent}
     */
    _createSecurityComponent: function (remoteSecurityComponent) {
      var securityComponent = new qx.ui.core.Widget();
      return securityComponent;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteActionField {org.jspresso.framework.gui.remote.RActionField}
     */
    _createActionField: function (remoteActionField) {

      /** @type {qx.ui.form.TextField } */
      var textField;
      if (remoteActionField.getShowTextField()) {
        textField = new qx.ui.form.TextField();
        this._sizeMaxComponentWidth(textField, remoteActionField);
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

        // propagate active state
        actionField.addListener("activate", function () {
          textField.activate();
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
                if (e.getRelatedTarget() && (/** @type {qx.ui.core.Widget } */ e.getRelatedTarget()) == actionField) {
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
     * @param component {qx.ui.core.Widget}
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @param disableActionsWithField {Boolean}
     * @returns {qx.ui.core.Widget}
     * @protected
     */
    _decorateWithAsideActions: function (component, remoteComponent, disableActionsWithField) {
      var decorated = component;
      if (remoteComponent.getActionLists()) {
        var actionField = new qx.ui.container.Composite();
        actionField.setFocusable(true);
        actionField.setAllowStretchY(false, false);
        actionField.setLayout(new qx.ui.layout.HBox());

        if (component) {
          component.setAlignY("middle");
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
            //actionComponent.setFocusable(false);
            actionComponent.setAllowStretchY(false, false);
            actionComponent.setAlignY("middle");
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
     * @param component {qx.ui.core.Widget}
     * @param styleName {String}
     * @return {undefined}
     */
    _applyStyleName: function (component, styleName) {
      if(styleName) {
        component.setAppearance(styleName);
      }
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteTable {org.jspresso.framework.gui.remote.RTable}
     */
    _createTable: function (remoteTable) {
      /** @type {org.jspresso.framework.state.remote.RemoteCompositeValueState } */
      var state = remoteTable.getState();
      var tableModel = new org.jspresso.framework.view.qx.RTableModel(state, remoteTable.getSortable(),
          remoteTable.getSortingAction(), this.__commandHandler);
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(tableModel, "editable", "writable", false);
      var columnIds = remoteTable.getColumnIds();
      var columnNames = [];
      var columnToolTips = [];
      for (var i = 0; i < remoteTable.getColumnIds().length; i++) {
        columnNames[i] = remoteTable.getColumns()[i].getLabel();
      }
      tableModel.setColumns(columnNames, columnIds);

      /** @type {qx.ui.table.Table } */
      var table;
      if (remoteTable.getHorizontallyScrollable()) {
        table = new org.jspresso.framework.view.qx.EnhancedTable(tableModel);
      } else {
        // Customize the table column model. We want one that
        // automatically resize columns.
        var custom = {
          tableColumnModel: function (obj) {
            return new qx.ui.table.columnmodel.Resize(obj);
          }
        };
        table = new org.jspresso.framework.view.qx.EnhancedTable(tableModel, custom);
      }
      table.setStatusBarVisible(false);
      if (!remoteTable.getColumnReorderingAllowed()) {
        table.getPaneScroller(0)._startMoveHeader = function (moveCol, pageX) {
        };
      }
      var columnModel = table.getTableColumnModel();
      for (var i = 0; i < remoteTable.getColumnIds().length; i++) {
        var rColumn = remoteTable.getColumns()[i];
        var rColumnHeader = remoteTable.getColumnHeaders()[i];
        var editor = new org.jspresso.framework.view.qx.RComponentTableCellEditor(this, rColumn, this.__actionHandler);
        columnModel.setCellEditorFactory(i, editor);
        var bgIndex = -1;
        var fgIndex = -1;
        var foIndex = -1;
        if (rColumn.getBackgroundState()) {
          bgIndex = remoteTable.getRowPrototype().getChildren().indexOf(rColumn.getBackgroundState());
        } else if (remoteTable.getBackgroundState()) {
          bgIndex = remoteTable.getRowPrototype().getChildren().indexOf(remoteTable.getBackgroundState());
        }
        if (rColumn.getForegroundState()) {
          fgIndex = remoteTable.getRowPrototype().getChildren().indexOf(rColumn.getForegroundState());
        } else if (remoteTable.getForegroundState()) {
          fgIndex = remoteTable.getRowPrototype().getChildren().indexOf(remoteTable.getForegroundState());
        }
        if (rColumn.getFontState()) {
          foIndex = remoteTable.getRowPrototype().getChildren().indexOf(rColumn.getFontState());
        } else if (remoteTable.getFontState()) {
          foIndex = remoteTable.getRowPrototype().getChildren().indexOf(remoteTable.getFontState());
        }
        var cellRenderer = null;
        if (rColumn instanceof org.jspresso.framework.gui.remote.RCheckBox) {
          cellRenderer = new org.jspresso.framework.view.qx.BooleanTableCellRenderer();
        } else if (rColumn instanceof org.jspresso.framework.gui.remote.RColorField) {
          cellRenderer = new org.jspresso.framework.view.qx.ColorTableCellRenderer();
        } else if (rColumn instanceof org.jspresso.framework.gui.remote.RComboBox) {
          var labels = {};
          var icons = {};
          for (var j = 0; j < rColumn.getValues().length; j++) {
            var value = rColumn.getValues()[j];
            labels[value] = rColumn.getTranslations()[j];
            icons[value] = rColumn.getIcons()[j];
          }
          cellRenderer = new org.jspresso.framework.view.qx.EnumerationTableCellRenderer(labels, icons);
        } else if (rColumn instanceof org.jspresso.framework.gui.remote.RActionField && !rColumn.getShowTextField()) {
          cellRenderer = new org.jspresso.framework.view.qx.BinaryTableCellRenderer();
        } else if (rColumn instanceof org.jspresso.framework.gui.remote.RImageComponent) {
          cellRenderer = new org.jspresso.framework.view.qx.ImageTableCellRenderer();
          this.__remotePeerRegistry.register(rColumn.getAction());
          cellRenderer.setAction(rColumn.getAction());
        } else {
          var format = this._createFormat(rColumn);
          cellRenderer = new org.jspresso.framework.view.qx.FormattedTableCellRenderer(table, format);
          cellRenderer.setUseAutoAlign(false);

          if (rColumn instanceof org.jspresso.framework.gui.remote.RLink) {
            this.__remotePeerRegistry.register(rColumn.getAction());
            cellRenderer.setAction(rColumn.getAction());
          }
        }
        if (cellRenderer) {
          var alignment = null;
          if (rColumn instanceof org.jspresso.framework.gui.remote.RLabel || rColumn
              instanceof org.jspresso.framework.gui.remote.RTextField || rColumn
              instanceof org.jspresso.framework.gui.remote.RNumericComponent) {
            alignment = rColumn.getHorizontalAlignment();
          }
          if (alignment == "LEFT") {
            alignment = "left";
          } else if (alignment == "CENTER") {
            alignment = "center";
          } else if (alignment == "RIGHT") {
            alignment = "right";
          }

          var additionalAttributes = {};
          if (alignment) {
            additionalAttributes["text-align"] = alignment;
          }
          if (bgIndex >= 0) {
            additionalAttributes["backgroundIndex"] = bgIndex;
          } else if (rColumn.getBackground()) {
            additionalAttributes["background-color"] = org.jspresso.framework.view.qx.AbstractQxViewFactory._hexColorToQxColor(rColumn.getBackground());
          }
          if (fgIndex >= 0) {
            additionalAttributes["foregroundIndex"] = fgIndex;
          } else if (rColumn.getForeground()) {
            additionalAttributes["color"] = org.jspresso.framework.view.qx.AbstractQxViewFactory._hexColorToQxColor(rColumn.getForeground());
          }
          if (foIndex >= 0) {
            additionalAttributes["fontIndex"] = foIndex;
          } else {
            var rFont = rColumn.getFont();
            if (rFont) {
              if (rFont.getItalic()) {
                additionalAttributes["font-style"] = "italic";
              }
              if (rFont.getBold()) {
                additionalAttributes["font-weight"] = "bold";
              }
              if (rFont.getName()) {
                additionalAttributes["font-family"] = rFont.getName();
              }
              if (rFont.getSize() > 0) {
                additionalAttributes["font-size"] = rFont.getSize() + "px";
              }
            }
          }

          cellRenderer.setAdditionalAttributes(additionalAttributes);

          columnModel.setDataCellRenderer(i, cellRenderer);
          columnModel.setHeaderCellRenderer(i,
              new org.jspresso.framework.view.qx.RComponentHeaderRenderer(table, this, rColumnHeader));
        }
        var columnWidth;
        if (rColumn.getPreferredSize() && rColumn.getPreferredSize().getWidth() > 0) {
          columnWidth = rColumn.getPreferredSize().getWidth();
        } else {
          var tableFont = qx.theme.manager.Font.getInstance().resolve("default");
          var headerWidth = qx.bom.Label.getTextSize(columnNames[i], tableFont.getStyles()).width;
          if (rColumn instanceof org.jspresso.framework.gui.remote.RCheckBox) {
            columnWidth = headerWidth + 16;
          } else {
            var maxColumnWidth = qx.bom.Label.getTextSize(org.jspresso.framework.view.qx.AbstractQxViewFactory.__TEMPLATE_CHAR,
                tableFont.getStyles()).width
                * org.jspresso.framework.view.qx.AbstractQxViewFactory.__COLUMN_MAX_CHAR_COUNT;
            var editorComponent = this.createComponent(rColumn, false);
            columnWidth = maxColumnWidth;
            if (editorComponent.getMaxWidth()) {
              columnWidth = Math.min(maxColumnWidth, editorComponent.getMaxWidth());
            }
            columnWidth = Math.max(columnWidth, headerWidth + 16);
          }
        }
        if (remoteTable.getHorizontallyScrollable()) {
          columnModel.setColumnWidth(i, columnWidth);
        } else {
          columnModel.getBehavior().setWidth(i, columnWidth, columnWidth < 50 ? 0 : columnWidth);
        }
        columnToolTips[i] = -1;
        if (rColumn.getToolTipState()) {
          columnToolTips[i] = remoteTable.getRowPrototype().getChildren().indexOf(rColumn.getToolTipState());
        }
      }
      tableModel.setDynamicToolTipIndices(columnToolTips);
      table.addListener("cellClick", function (e) {
        var col = e.getColumn();
        var renderer = table.getTableColumnModel().getDataCellRenderer(col);
        if ((    renderer instanceof org.jspresso.framework.view.qx.FormattedTableCellRenderer || renderer
            instanceof org.jspresso.framework.view.qx.ImageTableCellRenderer) && renderer.getAction()) {
          this.__actionHandler.execute(renderer.getAction());
        }
      }, this);

      table.setHeight(5 * table.getRowHeight() + table.getHeaderCellHeight());
      var selectionModel = table.getSelectionModel();
      if (remoteTable.getSelectionMode() == "SINGLE_SELECTION") {
        selectionModel.setSelectionMode(qx.ui.table.selection.Model.SINGLE_SELECTION);
      } else if (remoteTable.getSelectionMode() == "SINGLE_INTERVAL_SELECTION") {
        selectionModel.setSelectionMode(qx.ui.table.selection.Model.SINGLE_INTERVAL_SELECTION);
      } else if (remoteTable.getSelectionMode() == "MULTIPLE_INTERVAL_SELECTION") {
        selectionModel.setSelectionMode(qx.ui.table.selection.Model.MULTIPLE_INTERVAL_SELECTION);
      } else if (remoteTable.getSelectionMode() == "MULTIPLE_INTERVAL_CUMULATIVE_SELECTION") {
        selectionModel.setSelectionMode(qx.ui.table.selection.Model.MULTIPLE_INTERVAL_SELECTION_TOGGLE);
      } else if (remoteTable.getSelectionMode() == "SINGLE_CUMULATIVE_SELECTION") {
        selectionModel.setSelectionMode(qx.ui.table.selection.Model.SINGLE_SELECTION);
      } else {
        selectionModel.setSelectionMode(qx.ui.table.selection.Model.MULTIPLE_INTERVAL_SELECTION);
      }
      selectionModel.addListener("changeSelection", function (e) {
        if (selectionModel.hasBatchMode()) {
          // TODO notify Qooxdoo batchMode is not working.
          return;
        }
        var leadingIndex = tableModel.viewIndexToModelIndex(selectionModel.getLeadSelectionIndex());
        var selectedRanges = selectionModel.getSelectedRanges();

        var selectedIndices = [];
        for (var rangeIndex = 0; rangeIndex < selectedRanges.length; rangeIndex++) {
          var range = selectedRanges[rangeIndex];
          for (var i = range.minIndex; i < range.maxIndex + 1; i++) {
            selectedIndices.push(i);
          }
        }
        selectedIndices = tableModel.viewIndicesToModelIndices(selectedIndices);
        if (selectedIndices.length == 0) {
          leadingIndex = -1;
        }
        var stateSelection = state.getSelectedIndices();
        if (!stateSelection) {
          stateSelection = [];
        }
        if (!qx.lang.Array.equals(selectedIndices, stateSelection) || leadingIndex != state.getLeadingIndex()) {
          if (selectedIndices.length == 0) {
            selectedIndices = null;
          }
          state.setLeadingIndex(leadingIndex);
          state.setSelectedIndices(selectedIndices);
        }
        // workaround to update cell rendering
        table.updateContent();
      }, this);

      state.addListener("changeSelectedIndices", function (e) {
        var stateSelection = tableModel.modelIndicesToViewIndices(state.getSelectedIndices());
        if (!stateSelection) {
          stateSelection = [];
        }

        var selectedRanges = selectionModel.getSelectedRanges();
        var selectedIndices = [];
        for (var rangeIndex = 0; rangeIndex < selectedRanges.length; rangeIndex++) {
          var range = selectedRanges[rangeIndex];
          for (var i = range.minIndex; i < range.maxIndex + 1; i++) {
            selectedIndices.push(i);
          }
        }

        var stateLeadingIndex = tableModel.modelIndexToViewIndex(state.getLeadingIndex());
        var leadingIndex = selectionModel.getLeadSelectionIndex();
        if (selectedIndices.length == 0) {
          leadingIndex = -1;
        }

        if (!qx.lang.Array.equals(selectedIndices, stateSelection) || leadingIndex != stateLeadingIndex) {
          selectionModel.setBatchMode(true);
          selectionModel.resetSelection();
          if (stateSelection.length > 0) {
            var minIndex = -1;
            var maxIndex = minIndex;
            for (var i = 0; i < stateSelection.length; i++) {
              if (minIndex < 0) {
                minIndex = stateSelection[i];
                maxIndex = minIndex;
              } else {
                var nextSelectedIndex = stateSelection[i];
                if (nextSelectedIndex == maxIndex + 1) {
                  maxIndex = nextSelectedIndex;
                } else {
                  selectionModel.addSelectionInterval(minIndex, maxIndex);
                  minIndex = nextSelectedIndex;
                  maxIndex = minIndex;
                }
              }
            }
            selectionModel.addSelectionInterval(minIndex, maxIndex);
          }
          if (stateLeadingIndex >= 0) {
            selectionModel.addSelectionInterval(stateLeadingIndex, stateLeadingIndex);
            table.setFocusedCell(0, stateLeadingIndex, true);
          }
          selectionModel.setBatchMode(false);
        }
      }, this);
      if (remoteTable.getRowAction()) {
        this.__remotePeerRegistry.register(remoteTable.getRowAction());
        table.addListener("dblclick", function (e) {
          this.__actionHandler.execute(remoteTable.getRowAction());
        }, this);
      }
      if (remoteTable.getPermId()) {
        var notifyTableChanged = function (event) {
          var notificationCommand = new org.jspresso.framework.application.frontend.command.remote.RemoteTableChangedCommand();
          notificationCommand.setTableId(remoteTable.getPermId());
          var columnIds = [];
          var columnWidths = [];
          for (var ci = 0; ci < table.getTableColumnModel().getOverallColumnCount(); ci++) {
            columnIds.push(table.getTableModel().getColumnId(table.getTableColumnModel().getOverallColumnAtX(ci)));
            columnWidths.push(table.getTableColumnModel().getColumnWidth(table.getTableColumnModel().getOverallColumnAtX(ci)));
          }
          notificationCommand.setColumnIds(columnIds);
          notificationCommand.setColumnWidths(columnWidths);
          //noinspection JSPotentiallyInvalidUsageOfThis
          this.__commandHandler.registerCommand(notificationCommand);
        };

        table.getTableColumnModel().addListener("widthChanged", notifyTableChanged, this);
        table.getTableColumnModel().addListener("orderChanged", notifyTableChanged, this);
      }
      return table;
    },

    /**
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @param component {qx.ui.core.Widget}
     * @return {qx.ui.core.Widget}
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
        return this._decorateWithToolbars(component, remoteComponent);
      }
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
        var surroundingBox = new qx.ui.container.Composite();
        surroundingBox.setLayout(new qx.ui.layout.VBox(2));
        var slideBar;
        if (toolBar) {
          slideBar = new qx.ui.container.SlideBar();
          slideBar.add(toolBar);
          surroundingBox.add(slideBar);
        }
        surroundingBox.add(component, {
          flex: 1
        });
        if (secondaryToolBar) {
          slideBar = new qx.ui.container.SlideBar();
          slideBar.add(secondaryToolBar);
          surroundingBox.add(slideBar);
        }
        decorated = surroundingBox;
      }
      return decorated;
    },

    _createDefaultToolBar: function (remoteComponent, component) {
      return null;
    },

    _createToolBar: function (remoteComponent, component) {
      return this.createToolBarFromActionLists(remoteComponent.getActionLists());
    },

    _createSecondaryToolBar: function (remoteComponent, component) {
      return this.createToolBarFromActionLists(remoteComponent.getSecondaryActionLists());
    },

    createToolBarFromActionLists: function (actionLists) {
      if (actionLists && actionLists.length > 0) {
        var toolBar = new qx.ui.toolbar.ToolBar();
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
            var part = new qx.ui.toolbar.Part();
            if (actionList.getCollapsable()) {
              var splitButton = this.createSplitButton(actionList);
              if (splitButton) {
                part.add(splitButton);
              }
            } else {
              for (var j = 0; j < actionList.getActions().length; j++) {
                part.add(this.createAction(actionList.getActions()[j]));
              }
            }
            toolBar.add(part);
          }
        }
      }
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param rComponent {org.jspresso.framework.gui.remote.RComponent}
     */
    _createFormattedField: function (rComponent) {
      var formattedField = new qx.ui.form.TextField();
      this._bindFormattedField(formattedField, rComponent);
      return formattedField;
    },

    /**
     *
     * @param expectedCharCount {Integer}
     * @param component {qx.ui.core.Widget}
     * @param maxCharCount {Integer}
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @return {undefined}
     */
    _sizeMaxComponentWidth: function (component, remoteComponent, expectedCharCount, maxCharCount) {
      var w;
      this.applyComponentStyle(component, remoteComponent);
      if (expectedCharCount == null) {
        expectedCharCount = org.jspresso.framework.view.qx.AbstractQxViewFactory.__FIELD_MAX_CHAR_COUNT;
      }
      if (maxCharCount == null) {
        maxCharCount = org.jspresso.framework.view.qx.AbstractQxViewFactory.__FIELD_MAX_CHAR_COUNT;
      }
      var charCount = maxCharCount;
      if (expectedCharCount < charCount) {
        charCount = expectedCharCount;
      }
      charCount += 2;
      var compFont = component.getFont();
      if (!compFont) {
        compFont = qx.theme.manager.Font.getInstance().resolve("default");
      }
      var charWidth = qx.bom.Label.getTextSize(org.jspresso.framework.view.qx.AbstractQxViewFactory.__TEMPLATE_CHAR,
          compFont.getStyles()).width;
      w = charWidth * charCount;
      if (remoteComponent.getPreferredSize() && remoteComponent.getPreferredSize().getWidth() > w) {
        w = remoteComponent.getPreferredSize().getWidth();
      }
      component.setMaxWidth(w);
      component.setWidth(w);
    },

    /**
     * @return {undefined}
     * @param component {qx.ui.core.Widget}
     * @param alignment {String}
     */
    _configureHorizontalAlignment: function (component, alignment) {
      if (alignment == "LEFT") {
        component.setTextAlign("left");
      } else if (alignment == "CENTER") {
        component.setTextAlign("center");
      } else if (alignment == "RIGHT") {
        component.setTextAlign("right");
      }
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteLabel {org.jspresso.framework.gui.remote.RLabel}
     */
    _createLabel: function (remoteLabel) {
      var atom = new qx.ui.basic.Atom();
      var label = atom.getLabel();
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
          atom.addListener("click", function (event) {
            this.__actionHandler.execute(remoteLabel.getAction());
          }, this);
        } else {
          modelController.addTarget(atom, "label", "value", false, {
            converter: function (modelValue, model) {
              if (org.jspresso.framework.util.html.HtmlUtil.isHtml(modelValue)) {
                atom.setRich(true);
              } else {
                atom.setRich(false);
              }
              return modelValue;
            }
          });
        }
      } else {
        atom.setLabel(remoteLabel.getLabel());
        atom.setRich(org.jspresso.framework.util.html.HtmlUtil.isHtml(remoteLabel.getLabel()));
      }
      this._configureHorizontalAlignment(label, remoteLabel.getHorizontalAlignment());
      if (remoteLabel.getIcon()) {
        atom.setIcon(remoteLabel.getIcon().getImageUrlSpec());
      }
      return atom;
    }







  }
});
