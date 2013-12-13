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
 * @asset(qx/icon/Oxygen/22/actions/dialog-ok.png)
 * @asset(qx/icon/Oxygen/22/actions/dialog-close.png)
 * @asset(qx/icon/Oxygen/22/actions/dialog-cancel.png)
 * @asset(qx/icon/Oxygen/16/actions/dialog-close.png)
 *
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
        if (rComponent instanceof org.jspresso.framework.gui.remote.RTable || rComponent
            instanceof org.jspresso.framework.gui.remote.RTextArea || rComponent
            instanceof org.jspresso.framework.gui.remote.RList || rComponent
            instanceof org.jspresso.framework.gui.remote.RHtmlArea) {
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
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
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
    }


  }
});
