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

qx.Class.define("org.jspresso.framework.view.qx.DefaultQxViewFactory", {
  extend: org.jspresso.framework.view.qx.AbstractQxViewFactory,

  statics: {
    __TEMPLATE_CHAR: "0",
    __FIELD_MAX_CHAR_COUNT: 32,
    __NUMERIC_FIELD_MAX_CHAR_COUNT: 16,
    __COLUMN_MAX_CHAR_COUNT: 12,
    __DATE_CHAR_COUNT: 10,
    __TIME_CHAR_COUNT: 6,

    _hexColorToQxColor: function (hexColor) {
      if (hexColor) {
        var offset;
        if (hexColor.length == 10) {
          offset = 4;
        } else {
          offset = 2;
        }
        return "#" + hexColor.substring(offset); // ignore alpha
      }
      return hexColor;
    },

    _qxColorToHexColor: function (qxColor) {
      if (qxColor) {
        var rgbColor = qx.util.ColorUtil.stringToRgb(qxColor);
        return "0x" + "FF" + qx.util.ColorUtil.rgbToHexString(rgbColor).substring(1);
        // ignore alpha
      }
      return qxColor;
    },

    _fontToQxFont: function (rFont, defaultFont) {
      var font = new qx.bom.Font();
      if (!defaultFont) {
        defaultFont = qx.theme.manager.Font.getInstance().resolve("default");
      }
      if (!rFont) {
        return defaultFont;
      }
      if (rFont.getName()) {
        font.setFamily([rFont.getName()]);
      } else if (defaultFont) {
        font.setFamily(defaultFont.getFamily());
      }
      if (rFont.getSize() > 0) {
        font.setSize(rFont.getSize());
      } else if (defaultFont) {
        font.setSize(defaultFont.getSize());
      }
      if (rFont.getItalic()) {
        font.setItalic(true);
      }
      if (rFont.getBold()) {
        font.setBold(true);
      }
      return font;
    }

  },

  construct: function (remotePeerRegistry, actionHandler, commandHandler) {
    this.base(arguments, remotePeerRegistry, actionHandler, commandHandler);
  },

  members: {
    /**
     * @return {qx.ui.core.Widget}
     */
    _createEmptyWidget: function () {
      return new qx.ui.core.Widget();
    },

    /**
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @return {qx.ui.core.Widget}
     */
    _createCustomComponent: function (remoteComponent) {
      var component = null;
      if (remoteComponent instanceof org.jspresso.framework.gui.remote.RTable) {
        component = this._createTable(remoteComponent);
      }
      return component;
    },

    /**
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @param registerPeers {Boolean}
     * @return {qx.ui.core.Widget}
     */
    createComponent: function (remoteComponent, registerPeers) {
      var component = this.base(arguments, remoteComponent, registerPeers);
      if (remoteComponent && component) {
        this._configureToolTip(remoteComponent, component);
        component = this._decorateWithActions(remoteComponent, component);
        component = this._decorateWithBorder(remoteComponent, component);
        this._applyPreferredSize(remoteComponent, component);
      }
      return component;
    },

    /**
     * @param component {qx.ui.core.Widget}
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @return {undefined}
     */
    applyComponentStyle: function (component, remoteComponent) {
      if (remoteComponent.getForeground()) {
        component.setTextColor(
            org.jspresso.framework.view.qx.DefaultQxViewFactory._hexColorToQxColor(remoteComponent.getForeground()));
      }
      if (remoteComponent.getBackground()) {
        component.setDecorator("main");
        component.setBackgroundColor(
            org.jspresso.framework.view.qx.DefaultQxViewFactory._hexColorToQxColor(remoteComponent.getBackground()));
      }
      var rFont = remoteComponent.getFont();
      if (rFont) {
        var compFont = component.getFont();
        component.setFont(org.jspresso.framework.view.qx.DefaultQxViewFactory._fontToQxFont(rFont, compFont));
      }
      this._applyStyleName(component, remoteComponent.getStyleName());
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

    _createDefaultToolBar: function (remoteComponent, component) {
      return null;
    },

    _createToolBar: function (remoteComponent, component) {
      return this.createToolBarFromActionLists(remoteComponent.getActionLists());
    },

    _createSecondaryToolBar: function (remoteComponent, component) {
      return this.createToolBarFromActionLists(remoteComponent.getSecondaryActionLists());
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteImageComponent {org.jspresso.framework.gui.remote.RImageComponent}
     */
    _createImageComponent: function (remoteImageComponent) {
      var imageComponent = new qx.ui.basic.Image();
      imageComponent.setScale(false);
      imageComponent.setAlignX("left");
      imageComponent.setAlignY("middle");

      var state = remoteImageComponent.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(imageComponent, "source", "value");

      if (remoteImageComponent.getAction() != null) {
        this._getRemotePeerRegistry().register(remoteImageComponent.getAction());
        imageComponent.addListener("tap", function (e) {
          this._getActionHandler().execute(remoteImageComponent.getAction());
        }, this);
      }

      if (remoteImageComponent.getScrollable()) {
        var scrollContainer = new qx.ui.container.Scroll();
        scrollContainer.add(imageComponent);
        imageComponent = scrollContainer;
      } else {
        var borderContainer = new qx.ui.container.Composite();
        var borderLayout = new qx.ui.layout.Dock();
        borderLayout.setSort("y");
        borderContainer.setLayout(borderLayout);
        borderContainer.add(imageComponent, {
          edge: "center"
        });
        imageComponent = borderContainer;
      }
      return imageComponent;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteNumericComponent {org.jspresso.framework.gui.remote.RNumericComponent}
     */
    _createNumericComponent: function (remoteNumericComponent) {
      var numericComponent = this.base(arguments, remoteNumericComponent);
      var maxChars = -1;
      if (remoteNumericComponent.getMinValue() != null && remoteNumericComponent.getMaxValue() != null) {
        var formatter = this._createFormat(remoteNumericComponent);
        maxChars = Math.max(formatter.format(remoteNumericComponent.getMaxValue()).length,
            formatter.format(remoteNumericComponent.getMinValue()).length);
        if (numericComponent instanceof qx.ui.form.AbstractField) {
          numericComponent.setMaxLength(maxChars);
        }
      }
      if (maxChars > 0) {
        this._sizeMaxComponentWidth(numericComponent, remoteNumericComponent, maxChars,
            org.jspresso.framework.view.qx.DefaultQxViewFactory.__NUMERIC_FIELD_MAX_CHAR_COUNT);
      } else {
        this._sizeMaxComponentWidth(numericComponent, remoteNumericComponent,
            org.jspresso.framework.view.qx.DefaultQxViewFactory.__NUMERIC_FIELD_MAX_CHAR_COUNT,
            org.jspresso.framework.view.qx.DefaultQxViewFactory.__NUMERIC_FIELD_MAX_CHAR_COUNT);
      }
      this._configureHorizontalAlignment(numericComponent, remoteNumericComponent.getHorizontalAlignment());
      return numericComponent;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteIntegerField {org.jspresso.framework.gui.remote.RIntegerField}
     */
    _createIntegerField: function (remoteIntegerField) {
      var integerField = this._createFormattedField(remoteIntegerField);
      return integerField;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteDecimalField {org.jspresso.framework.gui.remote.RDecimalField}
     */
    _createDecimalField: function (remoteDecimalField) {
      var decimalField = this._createFormattedField(remoteDecimalField);
      return decimalField;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remotePercentField {org.jspresso.framework.gui.remote.RPercentField}
     */
    _createPercentField: function (remotePercentField) {
      var percentField = this._createFormattedField(remotePercentField);
      return percentField;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteTimeField {org.jspresso.framework.gui.remote.RTimeField}
     */
    _createTimeField: function (remoteTimeField) {
      var timeField = this._createFormattedField(remoteTimeField);
      this._sizeMaxComponentWidth(timeField, remoteTimeField,
          org.jspresso.framework.view.qx.DefaultQxViewFactory.__TIME_CHAR_COUNT);
      return timeField;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteDurationField {org.jspresso.framework.gui.remote.RDurationField}
     */
    _createDurationField: function (remoteDurationField) {
      var durationField = this._createFormattedField(remoteDurationField);
      return durationField;
    },

    /**
     * @param formattedField {qx.ui.form.TextField}
     * @param rComponent {org.jspresso.framework.gui.remote.RComponent}
     */
    _bindFormattedField: function (formattedField, rComponent) {
      var format = this._createFormat(rComponent);
      var state = rComponent.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(formattedField, "value", "value", true, {
        converter: function (modelValue, model) {
          if (modelValue == null) {
            return "";
          }
          var formattedValue = modelValue;
          if (format) {
            formattedValue = format.format(modelValue);
          }
          return formattedValue;
        }
      }, {
        converter: function (viewValue, model) {
          var parsedValue = viewValue;
          if (viewValue == null || viewValue.length == 0) {
            parsedValue = null;
          } else if (format) {
            try {
              parsedValue = format.parse(viewValue);
            } catch (ex) {
              // restore old value.
              parsedValue = state.getValue();
              if (parsedValue) {
                formattedField.setValue(format.format(parsedValue));
              } else {
                formattedField.setValue("");
              }
            }
          }
          return parsedValue;
        }
      });
      modelController.addTarget(formattedField, "readOnly", "writable", false, {
        converter: this._readOnlyFieldConverter
      });
    },

    /**
     *
     * @param remoteCheckBox {org.jspresso.framework.gui.remote.RCheckBox}
     * @param checkBox {qx.ui.form.CheckBox}
     */
    _bindCheckBox: function (remoteCheckBox, checkBox) {
      var state = remoteCheckBox.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(checkBox, "value", "value", true, {
        converter: function (modelValue, model) {
          if (typeof checkBox.getTriState == 'function' && !checkBox.getTriState()) {
            if (modelValue == null) {
              return false;
            }
          }
          return modelValue;
        }
      });
      modelController.addTarget(checkBox, "enabled", "writable", false);
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteRadioBox {org.jspresso.framework.gui.remote.RRadioBox}
     */
    _createRadioBox: function (remoteRadioBox) {
      var radioBox = new qx.ui.form.RadioButtonGroup();
      if (remoteRadioBox.getOrientation() == "HORIZONTAL") {
        radioBox.setLayout(new qx.ui.layout.HBox(4))
      } else {
        radioBox.setLayout(new qx.ui.layout.VBox(4));
      }
      radioBox.setAllowStretchY(false, false);
      for (var i = 0; i < remoteRadioBox.getValues().length; i++) {
        var rb = new qx.ui.form.RadioButton(remoteRadioBox.getTranslations()[i]);
        rb.setModel(remoteRadioBox.getValues()[i]);
        radioBox.add(rb);
      }

      var state = remoteRadioBox.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(radioBox, "modelSelection", "value", false, {
        converter: function (modelValue, model) {
          return [modelValue];
        }
      });
      radioBox.getModelSelection().addListener("change", function (e) {
        var modelSelection = e.getTarget();
        if (modelSelection.length > 0) {
          state.setValue(modelSelection.getItem(0));
        } else {
          state.setValue(null);
        }
      });
      modelController.addTarget(radioBox, "enabled", "writable", false);
      return radioBox;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteDateField {org.jspresso.framework.gui.remote.RDateField}
     */
    _createDateTimeField: function (remoteDateField) {
      var dateTimeField = new qx.ui.container.Composite(new qx.ui.layout.HBox());
      var oldType = remoteDateField.getType();
      try {
        remoteDateField.setType("DATE");
        dateTimeField.add(this._createDateField(remoteDateField));
      } catch (e) {
        throw e;
      } finally {
        remoteDateField.setType(oldType);
      }

      var remoteTimeField = new org.jspresso.framework.gui.remote.RTimeField();
      remoteTimeField.setBackground(remoteDateField.getBackground());
      remoteTimeField.setBorderType(remoteDateField.getBorderType());
      remoteTimeField.setFont(remoteDateField.getFont());
      remoteTimeField.setForeground(remoteDateField.getForeground());
      remoteTimeField.setGuid(remoteDateField.getGuid());
      remoteTimeField.setState(remoteDateField.getState());
      remoteTimeField.setToolTip(remoteDateField.getToolTip());
      remoteTimeField.setSecondsAware(remoteDateField.getSecondsAware());
      remoteTimeField.setMillisecondsAware(remoteDateField.getMillisecondsAware());
      remoteTimeField.useDateDto(true);
      dateTimeField.add(this.createComponent(remoteTimeField, false));
      this._sizeMaxComponentWidth(dateTimeField, remoteDateField,
          org.jspresso.framework.view.qx.DefaultQxViewFactory.__DATE_CHAR_COUNT
          + org.jspresso.framework.view.qx.DefaultQxViewFactory.__TIME_CHAR_COUNT + 4);
      return dateTimeField;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteActionComponent {org.jspresso.framework.gui.remote.RActionComponent}
     */
    _createActionComponent: function (remoteActionComponent) {
      var actionComponent = this.createAction(remoteActionComponent.getAction());
      return actionComponent;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteContainer {org.jspresso.framework.gui.remote.RContainer}
     */
    _createContainer: function (remoteContainer) {
      var container;
      if (remoteContainer instanceof org.jspresso.framework.gui.remote.RBorderContainer) {
        container = this._createBorderContainer(remoteContainer);
      } else if (remoteContainer instanceof org.jspresso.framework.gui.remote.RCardContainer) {
        container = this._createCardContainer(remoteContainer);
      } else if (remoteContainer instanceof org.jspresso.framework.gui.remote.RConstrainedGridContainer) {
        container = this._createConstrainedGridContainer(remoteContainer);
      } else if (remoteContainer instanceof org.jspresso.framework.gui.remote.REvenGridContainer) {
        container = this._createEvenGridContainer(remoteContainer);
      } else if (remoteContainer instanceof org.jspresso.framework.gui.remote.RSplitContainer) {
        container = this._createSplitContainer(remoteContainer);
      } else if (remoteContainer instanceof org.jspresso.framework.gui.remote.RTabContainer) {
        container = this._createTabContainer(remoteContainer);
      }
      return container;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteCardContainer {org.jspresso.framework.gui.remote.RCardContainer}
     */
    _createCardContainer: function (remoteCardContainer) {
      var cardContainer = this._createCardContainerComponent();

      for (var i = 0; i < remoteCardContainer.getCardNames().length; i++) {
        var rCardComponent = remoteCardContainer.getCards()[i];
        var cardName = remoteCardContainer.getCardNames()[i];

        this.addCard(cardContainer, rCardComponent, cardName);
      }

      /** @type {org.jspresso.framework.state.remote.RemoteValueState} */
      var state = remoteCardContainer.getState();
      state.addListener("changeValue", function (e) {
        var selectedCardName = e.getData();
        var children = cardContainer.getChildren();
        var selectedCard;
        for (var i = 0; i < children.length; i++) {
          var child = children[i];
          if (child.getUserData("cardName") == selectedCardName) {
            selectedCard = child;
          }
        }
        if (selectedCard) {
          this._selectCard(cardContainer, selectedCard);
        }
      }, this);
      return cardContainer;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteConstrainedGridContainer {org.jspresso.framework.gui.remote.RConstrainedGridContainer}
     */
    _createConstrainedGridContainer: function (remoteConstrainedGridContainer) {
      var constrainedGridContainer = new qx.ui.container.Composite();
      var gridLayout = new qx.ui.layout.Grid();
      constrainedGridContainer.setLayout(gridLayout);

      for (var i = 0; i < remoteConstrainedGridContainer.getCellConstraints().length; i++) {
        /** @type {org.jspresso.framework.util.gui.CellConstraints} */
        var cellConstraint = remoteConstrainedGridContainer.getCellConstraints()[i];
        var cellComponent = this.createComponent(remoteConstrainedGridContainer.getCells()[i]);
        constrainedGridContainer.add(cellComponent, {
          row: cellConstraint.getRow(),
          rowSpan: cellConstraint.getHeight(),
          column: cellConstraint.getColumn(),
          colSpan: cellConstraint.getWidth()
        });
        for (var j = cellConstraint.getColumn(); j < cellConstraint.getColumn() + cellConstraint.getWidth(); j++) {
          if (cellConstraint.getWidthResizable()) {
            gridLayout.setColumnFlex(j, 1);
          } else {
            gridLayout.setColumnFlex(j, 0);
          }
        }
        for (var j = cellConstraint.getRow(); j < cellConstraint.getRow() + cellConstraint.getHeight(); j++) {
          if (cellConstraint.getHeightResizable()) {
            gridLayout.setRowFlex(j, 1);
          } else {
            gridLayout.setRowFlex(j, 0);
          }
        }
      }
      return constrainedGridContainer;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteEvenGridContainer {org.jspresso.framework.gui.remote.REvenGridContainer}
     */
    _createEvenGridContainer: function (remoteEvenGridContainer) {
      var evenGridContainer = new qx.ui.container.Composite();
      var gridLayout = new qx.ui.layout.Grid();
      evenGridContainer.setLayout(gridLayout);

      var nbRows;
      var nbCols;

      var r = 0;
      var c = 0;

      if (remoteEvenGridContainer.getDrivingDimension() == "ROW") {
        nbCols = remoteEvenGridContainer.getDrivingDimensionCellCount();
        nbRows = remoteEvenGridContainer.getCells().length / nbCols;
        if (remoteEvenGridContainer.getCells().length % nbCols > 0) {
          nbRows += 1
        }
      } else {
        nbRows = remoteEvenGridContainer.getDrivingDimensionCellCount();
        nbCols = remoteEvenGridContainer.getCells().length / nbRows;
        if (remoteEvenGridContainer.getCells().length % nbRows > 0) {
          nbCols += 1
        }
      }
      for (var i = 0; i < remoteEvenGridContainer.getCells().length; i++) {
        var cellComponent = this.createComponent(remoteEvenGridContainer.getCells()[i]);
        evenGridContainer.add(cellComponent, {
          row: r, column: c
        });

        if (remoteEvenGridContainer.getDrivingDimension() == "ROW") {
          c++;
          if (c == nbCols) {
            c = 0;
            r++;
          }
        } else if (remoteEvenGridContainer.getDrivingDimension() == "COLUMN") {
          r++;
          if (r == nbRows) {
            r = 0;
            c++;
          }
        }
      }
      for (var i = 0; i < nbRows; i++) {
        gridLayout.setRowFlex(i, 1);
      }
      for (var i = 0; i < nbCols; i++) {
        gridLayout.setColumnFlex(i, 1);
      }
      return evenGridContainer;
    },

    _bindDynamicToolTip: function (component, rComponent) {
      var toolTipState = rComponent.getToolTipState();
      if (toolTipState) {
        this._getRemotePeerRegistry().register(toolTipState);
        var modelController = new qx.data.controller.Object(toolTipState);
        var toolTip = new qx.ui.tooltip.ToolTip();
        toolTip.setRich(true);
        modelController.addTarget(toolTip, "label", "value", false, {
          converter: this._modelToViewFieldConverter
        });
        component.setToolTip(toolTip);
      }
    },

    _bindDynamicBackground: function (component, rComponent) {
      var backgroundState = rComponent.getBackgroundState();
      if (backgroundState) {
        // To allow for having the background color displayed instead of the decorator.
        component.setDecorator("main");
        this._getRemotePeerRegistry().register(backgroundState);
        var modelController = new qx.data.controller.Object(backgroundState);
        modelController.addTarget(component, "backgroundColor", "value", false, {
          converter: function (modelValue, model) {
            return org.jspresso.framework.view.qx.DefaultQxViewFactory._hexColorToQxColor(modelValue);
          }
        });
      }
    },

    _bindDynamicForeground: function (component, rComponent) {
      var foregroundState = rComponent.getForegroundState();
      if (foregroundState) {
        this._getRemotePeerRegistry().register(foregroundState);
        var modelController = new qx.data.controller.Object(foregroundState);
        modelController.addTarget(component, "textColor", "value", false, {
          converter: function (modelValue, model) {
            return org.jspresso.framework.view.qx.DefaultQxViewFactory._hexColorToQxColor(modelValue);
          }
        });
      }
    },

    _bindDynamicFont: function (component, rComponent) {
      var fontState = rComponent.getFontState();
      if (fontState) {
        this._getRemotePeerRegistry().register(fontState);
        var modelController = new qx.data.controller.Object(fontState);
        modelController.addTarget(component, "font", "value", false, {
          converter: function (modelValue, model) {
            return org.jspresso.framework.view.qx.DefaultQxViewFactory._fontToQxFont(modelValue);
          }
        });
      }
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteTextArea {org.jspresso.framework.gui.remote.RTextArea}
     */
    _createTextArea: function (remoteTextArea) {
      var textArea = new qx.ui.form.TextArea();
      var state = remoteTextArea.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(textArea, "value", "value", true, {
        converter: this._modelToViewFieldConverter
      }, {
        converter: this._viewToModelFieldConverter
      });
      modelController.addTarget(textArea, "readOnly", "writable", false, {
        converter: this._readOnlyFieldConverter
      });
      textArea.setWidth(null);
      return textArea;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteHtmlArea {org.jspresso.framework.gui.remote.RHtmlArea}
     */
    _createHtmlArea: function (remoteHtmlArea) {
      var htmlComponent = this.base(arguments, remoteHtmlArea);
      htmlComponent.setWidth(null);
      return htmlComponent;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteHtmlArea {org.jspresso.framework.gui.remote.RHtmlArea}
     */
    _createHtmlEditor: function (remoteHtmlArea) {
      var htmlEditor = new htmlarea.HtmlArea(null, null, "blank.html");
      htmlEditor.setDecorator("main");
      var state = remoteHtmlArea.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(htmlEditor, "value", "value", false, {
        converter: this._modelToViewFieldConverter
      });
      htmlEditor.addListenerOnce("ready", function (event) {
        this.setValue(state.getValue());
      });
      htmlEditor.addListener("focusOut", function (event) {
        state.setValue(this.getComputedValue());
      });
      modelController.addTarget(htmlEditor, "enabled", "writable", false);

      var vb = new qx.ui.layout.VBox(0);
      var vbContainer = new qx.ui.container.Composite(vb);
      vbContainer.add(htmlEditor, {
        flex: 1
      });
      vbContainer.add(this._createHtmlEditorToolBar(htmlEditor));
      return vbContainer;
    },

    /**
     * Creates the "font-family" toolbar dropdown
     *
     * @return {qx.ui.form.SelectBox} select box button
     */
    _fontFamilyToolbarEntry: function (htmlEditor) {
      var button = new qx.ui.form.SelectBox;
      button.set({
        toolTipText: this._getActionHandler().translate("change_font_family"),
        focusable: false,
        keepFocus: true,
        width: 120,
        height: 16,
        margin: [4, 0]
      });
      button.add(new qx.ui.form.ListItem(""));

      var entries = ["Tahoma", "Verdana", "Times New Roman", "Arial", "Arial Black", "Courier New", "Courier",
                     "Georgia", "Impact", "Comic Sans MS", "Lucida Console"];

      var entry;
      for (var i = 0, j = entries.length; i < j; i++) {
        entry = new qx.ui.form.ListItem(entries[i]);
        entry.set({
          focusable: false, keepFocus: true, font: qx.bom.Font.fromString("12px " + entries[i])
        });
        button.add(entry);
      }

      button.addListener("changeSelection", function (e) {
        var value = e.getData()[0].getLabel();
        if (value != "") {
          this.setFontFamily(value);
          button.setSelection([button.getChildren()[0]]);
        }
      }, htmlEditor);

      return button;
    },

    /**
     * Creates the "font-size" toolbar dropdown
     *
     * @return {qx.ui.form.SelectBox} select box button
     */
    _fontSizeToolbarEntry: function (htmlEditor) {
      var button = new qx.ui.form.SelectBox;
      button.set({
        toolTipText: this._getActionHandler().translate("change_font_size"),
        focusable: false,
        keepFocus: true,
        width: 50,
        height: 16,
        margin: [4, 0]
      });
      button.add(new qx.ui.form.ListItem(""));

      var entry;
      for (var i = 1; i <= 7; i++) {
        entry = new qx.ui.form.ListItem(i + "");
        entry.set({
          focusable: false, keepFocus: true
        });
        button.add(entry);
      }

      button.addListener("changeSelection", function (e) {
        var value = e.getData()[0].getLabel();
        if (value != "") {
          this.setFontSize(value);
          button.setSelection([button.getChildren()[0]]);
        }
      }, htmlEditor);

      return button;
    },

    _createHtmlEditorToolBar: function (htmlEditor) {
      var toolbarEntries = [{
        bold: {
          text: this._getActionHandler().translate("format_bold"),
          image: "org/jspresso/framework/htmleditor/format-text-bold.png",
          action: htmlEditor.setBold
        }, italic: {
          text: this._getActionHandler().translate("format_italic"),
          image: "org/jspresso/framework/htmleditor/format-text-italic.png",
          action: htmlEditor.setItalic
        }, underline: {
          text: this._getActionHandler().translate("format_underline"),
          image: "org/jspresso/framework/htmleditor/format-text-underline.png",
          action: htmlEditor.setUnderline
        }, strikethrough: {
          text: this._getActionHandler().translate("format_strikethrough"),
          image: "org/jspresso/framework/htmleditor/format-text-strikethrough.png",
          action: htmlEditor.setStrikeThrough
        }, removeFormat: {
          text: this._getActionHandler().translate("remove_format"),
          image: "org/jspresso/framework/htmleditor/edit-clear.png",
          action: htmlEditor.removeFormat
        }
      },

        {
          alignLeft: {
            text: this._getActionHandler().translate("align_left"),
            image: "org/jspresso/framework/htmleditor/format-justify-left.png",
            action: htmlEditor.setJustifyLeft
          }, alignCenter: {
          text: this._getActionHandler().translate("align_center"),
          image: "org/jspresso/framework/htmleditor/format-justify-center.png",
          action: htmlEditor.setJustifyCenter
        }, alignRight: {
          text: this._getActionHandler().translate("align_right"),
          image: "org/jspresso/framework/htmleditor/format-justify-right.png",
          action: htmlEditor.setJustifyRight
        }, alignJustify: {
          text: this._getActionHandler().translate("align_justify"),
          image: "org/jspresso/framework/htmleditor/format-justify-fill.png",
          action: htmlEditor.setJustifyFull
        }
        },

        {
          fontFamily: {
            custom: this._fontFamilyToolbarEntry
          }, fontSize: {
          custom: this._fontSizeToolbarEntry
        }
        },

        {
          indent: {
            text: this._getActionHandler().translate("indent_more"),
            image: "org/jspresso/framework/htmleditor/format-indent-more.png",
            action: htmlEditor.insertIndent
          }, outdent: {
          text: this._getActionHandler().translate("indent_less"),
          image: "org/jspresso/framework/htmleditor/format-indent-less.png",
          action: htmlEditor.insertOutdent
        }
        }, {
          ol: {
            text: this._getActionHandler().translate("insert_ordered_list"),
            image: "org/jspresso/framework/htmleditor/list-ordered.png",
            action: htmlEditor.insertOrderedList
          }, ul: {
            text: this._getActionHandler().translate("insert_unordered_list"),
            image: "org/jspresso/framework/htmleditor/list-unordered.png",
            action: htmlEditor.insertUnorderedList
          }
        }];
      var toolbar = new qx.ui.toolbar.ToolBar();
      toolbar.setDecorator("main");

      // Put together toolbar entries
      var button;
      for (var i = 0, j = toolbarEntries.length; i < j; i++) {
        var part = new qx.ui.toolbar.Part;
        toolbar.add(part);

        for (var entry in toolbarEntries[i]) {
          //noinspection JSUnfilteredForInLoop
          var infos = toolbarEntries[i][entry];

          if (infos.custom) {
            button = infos.custom.call(this, htmlEditor);
          } else {
            button = new qx.ui.toolbar.Button(null, infos.image);
            button.set({
              focusable: false, keepFocus: true, center: true, toolTipText: infos.text ? infos.text : ""
            });
            this.addButtonListener(button, infos.action, htmlEditor);
          }
          part.add(button);
        }
      }
      return toolbar;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteHtmlArea {org.jspresso.framework.gui.remote.RHtmlArea}
     */
    _createHtmlText: function (remoteHtmlArea) {
      var htmlText = new qx.ui.basic.Label();
      htmlText.setRich(true);
      htmlText.setSelectable(true);
      var state = remoteHtmlArea.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(htmlText, "value", "value", false, {
        converter: this._modelToViewFieldConverter
      });
      var scrollPane = new qx.ui.container.Scroll();
      scrollPane.add(htmlText);
      if (remoteHtmlArea.getHorizontallyScrollable()) {
        htmlText.setAllowStretchX(false, false);
        htmlText.setWrap(false);
        scrollPane.setScrollbarX("on");
      } else {
        htmlText.setWrap(true);
        scrollPane.setScrollbarX("off");
      }
      if (remoteHtmlArea.getVerticallyScrollable()) {
        scrollPane.setScrollbarY("auto");
      } else {
        scrollPane.setScrollbarY("off");
      }
      return scrollPane;
    },

    /**
     * @param remoteTextField  {org.jspresso.framework.gui.remote.RTextField}
     * @param textField {qx.ui.form.TextField}
     */
    _bindTextField: function (remoteTextField, textField) {
      var state = remoteTextField.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(textField, "value", "value", true, {
        converter: this._modelToViewFieldConverter
      }, {
        converter: this._viewToModelFieldConverter
      });
      modelController.addTarget(textField, "readOnly", "writable", false, {
        converter: this._readOnlyFieldConverter
      });
      if (remoteTextField.getCharacterAction()) {
        textField.addListener("input", function (event) {
          var actionEvent = new org.jspresso.framework.gui.remote.RActionEvent();
          actionEvent.setActionCommand(textField.getValue());
          this._getActionHandler().execute(remoteTextField.getCharacterAction(), actionEvent);
        }, this);
      }
    },

    /**
     *
     * @return {qx.ui.command.Command}
     * @param remoteAction {org.jspresso.framework.gui.remote.RAction}
     */
    createCommand: function (remoteAction) {
      var accel = remoteAction.getAcceleratorAsString();
      if (accel) {
        accel = accel.replace(/ /g, "+");
      }
      var command = new qx.ui.command.Command(accel);
      this.setIcon(command, remoteAction.getIcon());
      if (remoteAction.getName()) {
        command.setLabel(remoteAction.getName());
      }
      if (remoteAction.getDescription()) {
        command.setToolTipText(remoteAction.getDescription());
      }
      remoteAction.bind("enabled", command, "enabled");
      command.addListener("execute", function (e) {
        this._getActionHandler().execute(remoteAction);
      }, this);
      return command;
    },

    /**
     *
     * @return {qx.ui.menubar.Button}
     * @param toolTip {String}
     * @param label {String}
     * @param icon {org.jspresso.framework.gui.remote.RIcon}
     */
    createMenubarButton: function (label, toolTip, icon) {
      var button = new qx.ui.menubar.Button();
      this._completeButton(button, label, toolTip, icon);
      return button;
    },

    /**
     *
     * @return {qx.ui.core.Widget}
     * @param actionList {org.jspresso.framework.gui.remote.RActionList}
     */
    createSplitButton: function (actionList) {
      var actions = actionList.getActions();
      if (actions == null || actions.length == 0) {
        return null;
      }
      if (actions.length == 1) {
        return this.createAction(actions[0], true);
      }
      var menuItems = this.createMenuItems(actions);
      var menu = new qx.ui.menu.Menu();
      for (var i = 0; i < menuItems.length; i++) {
        menu.add(menuItems[i]);
      }
      var button = new qx.ui.form.SplitButton(menuItems[0].getLabel(), menuItems[0].getIcon(), menu, menuItems[0].getCommand());
      return button;
    },

    /**
     *
     * @return {Array}
     * @param actions {org.jspresso.framework.gui.remote.RAction[]}
     */
    createMenuItems: function (actions) {
      var menuItems = [];
      for (var i = 0; i < actions.length; i++) {
        var menuButton = this.createMenuButton(actions[i]);
        var command = this.createCommand(actions[i]);
        menuButton.setCommand(command);
        menuItems.push(menuButton);
      }
      return menuItems;
    },

    /**
     *
     * @return {qx.ui.menu.Button}
     * @param remoteAction {org.jspresso.framework.gui.remote.RAction}
     */
    createMenuButton: function (remoteAction) {
      var button = new qx.ui.menu.Button();
      remoteAction.bind("enabled", button, "enabled");
      this._getRemotePeerRegistry().register(remoteAction);
      this._completeButton(button, remoteAction.getName(), remoteAction.getDescription(), remoteAction.getIcon());
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
     * @return {qx.ui.form.Button}
     * @param remoteAction {org.jspresso.framework.gui.remote.RAction}
     */
    createAction: function (remoteAction) {
      var button = this.createButton(remoteAction.getName(), remoteAction.getDescription(), remoteAction.getIcon());
      this._bindButton(button, remoteAction);
      this._applyStyleName(button, remoteAction.getStyleName());
      return button;
    },

    /**
     *
     * @return {qx.ui.form.Button}
     * @param remoteAction {org.jspresso.framework.gui.remote.RAction}
     */
    createToolBarAction: function (remoteAction) {
      var button = this.createToolBarButton(remoteAction.getName(), remoteAction.getDescription(),
          remoteAction.getIcon());
      this._bindButton(button, remoteAction);
      this._applyStyleName(button, remoteAction.getStyleName());
      return button;
    },

    /**
     *
     * @return {undefined}
     * @param rCardComponent  {org.jspresso.framework.gui.remote.RCardContainer}
     * @param cardContainer {qx.ui.container.Stack}
     * @param cardName {String}
     */
    addCard: function (cardContainer, rCardComponent, cardName) {
      var children = cardContainer.getChildren();
      var existingCard;
      for (var i = 0; i < children.length; i++) {
        var child = children[i];
        if (child.getUserData("cardName") == cardName) {
          existingCard = child;
        }
      }
      if (!existingCard) {
        var cardComponent = this.createComponent(rCardComponent);
        cardComponent.setUserData("cardName", cardName);
        cardContainer.add(cardComponent);
        this._selectCard(cardContainer, cardComponent);
      }
    },

    /**
     * @param root {qx.ui.core.Widget}
     * @return {qx.ui.core.Widget}
     */
    _findFirstEditableComponent: function (root) {
      if (root instanceof qx.ui.table.Table) {
        if (root.getEnabled()) {
          return root;
        }
      }
      if (root instanceof qx.ui.container.Composite || root instanceof qx.ui.splitpane.Pane || root
          instanceof qx.ui.tabview.TabView) {
        for (var i = 0; i < (/** @type {qx.ui.core.MChildrenHandling} */ root).getChildren().length; i++) {
          var child = (/** @type {qx.ui.core.MChildrenHandling} */ root).getChildren()[i];
          if (child instanceof qx.ui.core.Widget) {
            var editableChild = this._findFirstEditableComponent(/** @type {qx.ui.core.Widget} */ child);
            if (editableChild != null) {
              return editableChild;
            }
          }
        }
      }
      return null;
    },

    focus: function (component) {
      var focusableChild = this._findFirstFocusableComponent(component);
      if (focusableChild) {
        focusableChild.focus();
      }
    },

    edit: function (component) {
      var editableChild = this._findFirstEditableComponent(component);
      if (editableChild instanceof qx.ui.table.Table) {
        editableChild.startEditing();
      }
    },

    /**
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
     * @return {qx.ui.form.Button}
     * @param toolTip {String}
     * @param label {String}
     * @param icon {org.jspresso.framework.gui.remote.RIcon}
     */
    createToolBarButton: function (label, toolTip, icon) {
      var button = this.createButton(label, toolTip, icon);
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
     * @param button {qx.ui.form.Button | qx.ui.menu.Button}
     * @param listener {function}
     * @param that {var}
     */
    addButtonListener: function (button, listener, that) {
      button.addListener("execute", listener, that);
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteForm {org.jspresso.framework.gui.remote.RForm}
     */
    _createForm: function (remoteForm) {
      var form = new qx.ui.container.Composite();
      form.setPadding(2);
      var formLayout = new qx.ui.layout.Grid(0, 0);
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
        /** @type {qx.ui.basic.Label} */
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
          componentLabel = /** @type {qx.ui.basic.Label} */ this.createComponent(remoteForm.getElementLabels()[i],
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
              componentLabel.setMarginRight(10);
            } else {
              componentLabel.setAlignX("right");
              componentLabel.setMarginLeft(10);
            }
            componentLabel.setAlignY("middle");
          } else {
            componentLabel.setAlignX("left");
            componentLabel.setAlignY("bottom");
          }
          componentLabel.setAllowStretchX(false, false);
          componentLabel.setAllowStretchY(false, false);
          if (componentLabel.getLabel() == "") {
            formLayout.setColumnFlex(labelCol - 1, 0);
            compColSpan++;
            compCol--;
          } else {
            form.add(componentLabel, {
              row: labelRow, column: labelCol, rowSpan: 1, colSpan: labelColSpan
            });
          }
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
        } else {
          compRowSpan = 1;
        }
        if (component instanceof qx.ui.container.Composite) {
          component.setPadding(2);
        } else if (rComponent instanceof org.jspresso.framework.gui.remote.RLabel) {
          component.setMargin([2, 6]);
        } else {
          component.setMargin(2);
        }
        form.add(component, {
          row: compRow, column: compCol, rowSpan: compRowSpan, colSpan: compColSpan
        });
        if (compColSpan > 1 && component.getMaxWidth() > 0
            && (formLayout.getColumnMaxWidth(compCol) == Infinity || formLayout.getColumnMaxWidth(compCol) < component.getMaxWidth())) {
          formLayout.setColumnMaxWidth(compCol, component.getMaxWidth());
        }

        if ((compColSpan > 1 || compColSpan == columnCount) && component.getWidth()
            == null/*to cope with preferred width*/) {
          component.setAllowGrowX(true);
        }
        col += elementWidth;
        formLayout.setColumnFlex(compCol, 1);
        lastRow = compRow;
        if (Math.max(compCol + compColSpan, labelCol + labelColSpan) > lastCol) {
          lastCol = Math.max(compCol + compColSpan, labelCol + labelColSpan);
        }
      }
      // Special toolTip handling
      var state = remoteForm.getState();
      var modelController = new qx.data.controller.Object(state);
      var toolTip = new qx.ui.tooltip.ToolTip();
      toolTip.setRich(true);
      modelController.addTarget(toolTip, "label", "value", false, {
        converter: this._modelToViewFieldConverter
      });
      form.setToolTip(toolTip);
      // Before decoration happen
      this._applyPreferredSize(remoteForm, form);
      // Since it's not resizeable anymore
      form.setMinWidth(form.getWidth());

      var decoratedForm = form;
      if (remoteForm.getVerticallyScrollable()) {
        var scrollContainer = new qx.ui.container.Scroll();
        scrollContainer.setScrollbarX("off");
        scrollContainer.setScrollbarY("auto");
        scrollContainer.add(form);
        scrollContainer.setMinWidth(form.getMinWidth());
        scrollContainer.setWidth(form.getWidth());
        scrollContainer.setHeight(form.getHeight());
        decoratedForm = scrollContainer;
      }
      if (!remoteForm.getWidthResizeable()) {
        var lefter = new qx.ui.container.Composite(new qx.ui.layout.Dock());
        lefter.add(decoratedForm, {edge: "west"});
        decoratedForm = lefter;
      }
      return decoratedForm;
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
          decorator = new org.jspresso.framework.view.qx.EnhancedCollapsiblePanel(remoteComponent.getLabel());
          this.setIcon(decorator.getChildControl("bar"), remoteComponent.getIcon())
        } else {
          decorator = new qx.ui.container.Composite();
          decorator.setAppearance("bordered-container");
        }
        decorator.setLayout(new qx.ui.layout.Grow());
        decorator.setAllowGrowX(component.getAllowGrowX());
        decorator.setAllowShrinkX(component.getAllowShrinkX());
        decorator.setAllowGrowY(component.getAllowGrowY());
        decorator.setAllowShrinkY(component.getAllowShrinkY());
        component.setAllowStretchX(true, true);
        component.setAllowStretchY(true, true);
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
     * @return {qx.ui.form.TextField}
     */
    _createTextFieldComponent: function () {
      var tf = new qx.ui.form.TextField();
      tf.addListener("focus", function () {
        tf.selectAllText();
        var oldValue = tf.getValue();
        new qx.util.DeferredCall(function () {
          var newValue = tf.getValue();
          if (newValue && oldValue && newValue == oldValue) {
            tf.selectAllText();
          }
        }).schedule();
      });
      return tf;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteTextField  {org.jspresso.framework.gui.remote.RTextField}
     */
    _createTextField: function (remoteTextField) {
      var textField = this._createTextFieldComponent();
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
            /** @type {String} */
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
        var maxTr = "";
        for (var i = 0; i < remoteComboBox.getValues().length; i++) {
          /* // Seems that there is no problem wit recent versions of qooxdoo
           if (i == 0 && remoteComboBox.getValues()[i].length > 0) {
           // Qx combos do not support null values
           var fallbackLi = new qx.ui.form.ListItem(String.fromCharCode(0x00A0));
           fallbackLi.setModel("");
           this.setIcon(fallbackLi, null);
           comboBox.add(fallbackLi);
           }
           */
          var tr = remoteComboBox.getTranslations()[i];
          if (tr == " ") {
            tr = String.fromCharCode(0x00A0);
          }
          var li = new qx.ui.form.ListItem(tr);
          li.setModel(remoteComboBox.getValues()[i]);
          var rIcon = remoteComboBox.getIcons()[i];
          this.setIcon(li, rIcon);
          comboBox.add(li);
          if (!iconDim && rIcon && rIcon.getDimension()) {
            iconDim = rIcon.getDimension();
          }
          if (tr.length > maxTr.length) {
            maxTr = tr;
          }
        }
        this._sizeMaxComponentWidthFromText(comboBox, remoteComboBox, maxTr);
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
        if (root.isFocusable()) {
          return root;
        }
      }
      var r;
      if (root instanceof qx.ui.container.Composite || root instanceof qx.ui.splitpane.Pane || root
          instanceof qx.ui.tabview.TabView) {
        r = /** @type {qx.ui.core.MChildrenHandling} */ root;
        for (var i = 0; i < r.getChildren().length; i++) {
          var child = r.getChildren()[i];
          if (child instanceof qx.ui.core.Widget) {
            var focusableChild = this._findFirstFocusableComponent(/** @type {qx.ui.core.Widget} */ child);
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
     * @param remoteMap {org.jspresso.framework.gui.remote.RMap}
     */
    _createMap: function (remoteMap) {
      var map = new org.jspresso.framework.view.qx.MapComponent();
      var state = remoteMap.getState();
      var longitudeState = state.getChildren().getItem(0);
      var latitudeState = state.getChildren().getItem(1);
      var updateMapLocation = function () {
        var longitude = longitudeState.getValue();
        var latitude = latitudeState.getValue();
        if (longitude != null && latitude != null) {
          map.zoomToPosition(longitude, latitude, 12, true);
        }
      };
      longitudeState.addListener("changeValue", updateMapLocation, this);
      latitudeState.addListener("changeValue", updateMapLocation, this);
      return map;
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
      if (remoteTree.getDisplayIcon()) {
        treeController.setIconPath("iconImageUrl");
      }
      treeController.setDelegate({
        createItem: function () {
          var item = new qx.ui.tree.TreeFolder();
          if (remoteTree.getStyleName()) {
            item.setAppearance(remoteTree.getStyleName() + "-folder");
          }
          item.getChildControl("label").setRich(true);
          return item;
        }, bindItem: function (controller, treeNode, modelNode) {
          controller.bindProperty(controller.getLabelPath(), "label", controller.getLabelOptions(), treeNode,
              modelNode);
          controller.bindProperty(controller.getIconPath(), "icon", controller.getIconOptions(), treeNode, modelNode);
          if (modelNode) {
            modelNode.addListener("changeSelectedIndices", function (e) {
              if (controller.getUserData("blockSelectionEvents")) {
                return;
              }
              var blockSelectionEvents = controller.getUserData("blockSelectionEvents");
              try {
                controller.setUserData("blockSelectionEvents", true);
                /** @type {qx.data.Array} */
                var viewSelection = controller.getSelection();
                var stateSelection = e.getTarget().getSelectedIndices();
                var stateChildren = e.getTarget().getChildren();
                var selIndex = 0;
                var newViewSelection = new qx.data.Array();
                for (var i = 0; i < stateChildren.length; i++) {
                  var child = stateChildren.getItem(i);
                  if (stateSelection && qx.lang.Array.contains(stateSelection, i)) {
                    if (!treeNode.getOpen()) {
                      treeNode.setOpen(true);
                    }
                    newViewSelection.push(child);
                  }
                }
                controller.setSelection(newViewSelection);
              } finally {
                controller.setUserData("blockSelectionEvents", blockSelectionEvents);
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
        if (treeController.getUserData("blockSelectionEvents")) {
          return;
        }
        var blockSelectionEvents = treeController.getUserData("blockSelectionEvents");
        try {
          treeController.setUserData("blockSelectionEvents", true);
          /** @type {qx.data.Array} */
          var selectedItems = e.getData();
          /** @type {org.jspresso.framework.state.remote.RemoteCompositeValueState} */
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
        } finally {
          treeController.setUserData("blockSelectionEvents", blockSelectionEvents);
        }
      }, this);

      if (remoteTree.getRowAction()) {
        this._getRemotePeerRegistry().register(remoteTree.getRowAction());
        tree.addListener("dbltap", function (e) {
          this._getActionHandler().execute(remoteTree.getRowAction());
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
     * @return {qx.ui.core.Widget}
     */
    _createDefaultComponent: function () {
      return new qx.ui.core.Widget();
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteActionField {org.jspresso.framework.gui.remote.RActionField}
     */
    _createActionField: function (remoteActionField) {

      /** @type {qx.ui.form.TextField} */
      var textField;
      if (remoteActionField.getShowTextField()) {
        textField = this._createTextFieldComponent();
        this._sizeMaxComponentWidth(textField, remoteActionField);
      }
      var actionField = this._decorateWithAsideActions(textField, remoteActionField, true);
      var state = remoteActionField.getState();
      var modelController = new qx.data.controller.Object(state);
      var mainAction = remoteActionField.getActionLists()[0].getActions()[0];
      if (textField) {
        // propagate focus
        actionField.addListener("focus", function (e) {
          if (textField.isFocusable()) {
            textField.focus();
          }
        });

        // propagate active state
        // This is a very bad idea since it produces an infinite loop.
        /*
         actionField.addListener("activate", function (e) {
         textField.activate();
         });
         */

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
                if (e.getRelatedTarget() && (/** @type {qx.ui.core.Widget} */ e.getRelatedTarget()) == actionField) {
                  return;
                }
              }
              var actionEvent = new org.jspresso.framework.gui.remote.RActionEvent();
              actionEvent.setActionCommand(content);
              this._getActionHandler().execute(mainAction, actionEvent);
            }
          } else {
            state.setValue(null);
          }
        };
        textField.addListener("blur", triggerAction, this);
        textField.addListener("keypress", function (e) {
          if (e.getKeyIdentifier() == "Enter") {
            triggerAction.call(this, e);
          }
        }, this);

        modelController.addTarget(textField, "value", "value", false, {
          converter: this._modelToViewFieldConverter
        });
        if (remoteActionField.getCharacterAction()) {
          textField.addListener("input", function (event) {
            var actionEvent = new org.jspresso.framework.gui.remote.RActionEvent();
            actionEvent.setActionCommand(textField.getValue());
            this._getActionHandler().execute(remoteActionField.getCharacterAction(), actionEvent);
          }, this);
        }
      } else {
        state.addListener("changeValue", function (e) {
          if (e.getData()) {
            var border = new qx.ui.decoration.Decorator().set({
              color: "red", width: 1, style: "solid"
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
     * @param component {qx.ui.core.Widget}
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @param disableActionsWithField {Boolean}
     * @returns {qx.ui.core.Widget}
     * @protected
     */
    _decorateWithAsideActions: function (component, remoteComponent, disableActionsWithField) {
      var decorated = component;
      if (remoteComponent.getActionLists()) {
        var actionField = new qx.ui.container.Composite(new qx.ui.layout.HBox(0));
        actionField.setFocusable(true);
        actionField.setAllowStretchY(false, false);

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
      if (styleName) {
        component.setAppearance(styleName);
      }
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteTable {org.jspresso.framework.gui.remote.RTable}
     */
    _createTable: function (remoteTable) {
      /** @type {org.jspresso.framework.state.remote.RemoteCompositeValueState} */
      var state = remoteTable.getState();
      var tableModel = new org.jspresso.framework.view.qx.RTableModel(state, remoteTable.getSortable(), remoteTable.getSortingAction(), this._getCommandHandler());
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(tableModel, "editable", "writable", false);
      var columnIds = remoteTable.getColumnIds();
      var columnLabels = [];
      var columnToolTips = [];
      for (var i = 0; i < remoteTable.getColumnIds().length; i++) {
        var columnLabel = remoteTable.getColumns()[i].getLabel();
        columnLabel = org.jspresso.framework.util.html.HtmlUtil.replaceNewlines(columnLabel);
        columnLabels[i] = columnLabel;
      }
      tableModel.setColumns(columnLabels, columnIds);

      /** @type {qx.ui.table.Table} */
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
      table.highlightFocusedRow(false);
      table.setResetSelectionOnHeaderTap(false);
      if (!remoteTable.getColumnReorderingAllowed()) {
        table.getPaneScroller(0)._startMoveHeader = function (moveCol, pageX) {
        };
      }
      table.addListener("cellTap", function (e) {
        if (columnModel.getDataCellRenderer(e.getColumn())
            instanceof org.jspresso.framework.view.qx.BooleanTableCellRenderer) {
          this.startEditing();
        }
      }, table);
      var dynamicStylesIndices = {};
      var columnModel = table.getTableColumnModel();
      for (var i = 0; i < remoteTable.getColumnIds().length; i++) {
        dynamicStylesIndices[i] = [];
        var rColumn = remoteTable.getColumns()[i];
        var rColumnHeader = remoteTable.getColumnHeaders()[i];
        var editor = new org.jspresso.framework.view.qx.RComponentTableCellEditor(this, rColumn, this._getActionHandler());
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
          cellRenderer = new org.jspresso.framework.view.qx.EnumerationTableCellRenderer(table, labels, icons);
        } else if (rColumn instanceof org.jspresso.framework.gui.remote.RActionField && !rColumn.getShowTextField()) {
          cellRenderer = new org.jspresso.framework.view.qx.BinaryTableCellRenderer();
        } else if (rColumn instanceof org.jspresso.framework.gui.remote.RImageComponent) {
          cellRenderer = new org.jspresso.framework.view.qx.ImageTableCellRenderer();
          this._getRemotePeerRegistry().register(rColumn.getAction());
          cellRenderer.setAction(rColumn.getAction());
        } else {
          var format = this._createFormat(rColumn);
          cellRenderer = new org.jspresso.framework.view.qx.FormattedTableCellRenderer(table, format);
          cellRenderer.setUseAutoAlign(false);

          if (rColumn instanceof org.jspresso.framework.gui.remote.RLink) {
            this._getRemotePeerRegistry().register(rColumn.getAction());
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
            dynamicStylesIndices[i].push(bgIndex);
          } else if (rColumn.getBackground()) {
            additionalAttributes["background-color"] = org.jspresso.framework.view.qx.DefaultQxViewFactory._hexColorToQxColor(
                rColumn.getBackground());
          }
          if (fgIndex >= 0) {
            additionalAttributes["foregroundIndex"] = fgIndex;
            dynamicStylesIndices[i].push(fgIndex);
          } else if (rColumn.getForeground()) {
            additionalAttributes["color"] = org.jspresso.framework.view.qx.DefaultQxViewFactory._hexColorToQxColor(
                rColumn.getForeground());
          }
          if (foIndex >= 0) {
            additionalAttributes["fontIndex"] = foIndex;
            dynamicStylesIndices[i].push(foIndex);
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
        tableModel.setDynamicStylesIndices(dynamicStylesIndices);
        var columnWidth;
        if (rColumn.getPreferredSize() && rColumn.getPreferredSize().getWidth() > 0) {
          columnWidth = rColumn.getPreferredSize().getWidth();
        } else {
          var tableFont = qx.theme.manager.Font.getInstance().resolve("default");
          var headerWidth = qx.bom.Label.getTextSize(columnLabels[i], tableFont.getStyles()).width;
          if (rColumn instanceof org.jspresso.framework.gui.remote.RCheckBox) {
            columnWidth = headerWidth + 16;
          } else {
            var maxColumnWidth = qx.bom.Label.getTextSize(
                    org.jspresso.framework.view.qx.DefaultQxViewFactory.__TEMPLATE_CHAR, tableFont.getStyles()).width
                * org.jspresso.framework.view.qx.DefaultQxViewFactory.__COLUMN_MAX_CHAR_COUNT;
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
      table.addListener("cellTap", function (e) {
        var col = e.getColumn();
        var renderer = table.getTableColumnModel().getDataCellRenderer(col);
        if ((    renderer instanceof org.jspresso.framework.view.qx.FormattedTableCellRenderer || renderer
            instanceof org.jspresso.framework.view.qx.ImageTableCellRenderer) && renderer.getAction()) {
          this._getActionHandler().execute(renderer.getAction());
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
        if (selectedIndices) {
          selectedIndices.sort(function (a, b) {
            return a - b;
          });
        }

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
      }, this);

      state.addListener("changeSelectedIndices", function (e) {
        var stateSelection = tableModel.modelIndicesToViewIndices(state.getSelectedIndices());
        if (!stateSelection) {
          stateSelection = [];
        }
        stateSelection.sort(function (a, b) {
          return a - b;
        });

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
          try {
            selectionModel.setBatchMode(true);
            selectionModel.resetSelection();
            table.setFocusedCell(null, null);
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
          } finally {
            selectionModel.setBatchMode(false);
          }
        }
      }, this);

      if (remoteTable.getRowAction()) {
        this._getRemotePeerRegistry().register(remoteTable.getRowAction());
        table.addListener("cellDbltap", function (e) {
          var row = e.getRow();
          var column = e.getColumn();
          var cellState = tableModel.getRowData(row).getChildren().getItem(column + 1);
          if (!tableModel.isColumnEditable(column) || !cellState.isWritable()) {
            this._getActionHandler().execute(remoteTable.getRowAction());
          }
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
            columnWidths.push(
                table.getTableColumnModel().getColumnWidth(table.getTableColumnModel().getOverallColumnAtX(ci)));
          }
          notificationCommand.setColumnIds(columnIds);
          notificationCommand.setColumnWidths(columnWidths);
          //noinspection JSPotentiallyInvalidUsageOfThis
          this._getCommandHandler().registerCommand(notificationCommand);
        };

        table.getTableColumnModel().addListener("widthChanged", notifyTableChanged, this);
        table.getTableColumnModel().addListener("orderChanged", notifyTableChanged, this);
      }
      return table;
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
        var surroundingBox = new qx.ui.container.Composite(new qx.ui.layout.VBox())
        surroundingBox.setPadding(2);
        var slideBar;
        if (toolBar) {
          slideBar = new qx.ui.container.SlideBar();
          slideBar.add(toolBar, {flex: 1});
          surroundingBox.add(slideBar);
        }
        surroundingBox.add(component, {
          flex: 1
        });
        if (secondaryToolBar) {
          slideBar = new qx.ui.container.SlideBar();
          slideBar.add(secondaryToolBar, {flex: 1});
          surroundingBox.add(slideBar);
        }
        decorated = surroundingBox;
      }
      return decorated;
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
                part.add(this.createToolBarAction(actionList.getActions()[j]));
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
      var formattedField = this._createTextFieldComponent();
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
      if (expectedCharCount == null || expectedCharCount < 0) {
        expectedCharCount = org.jspresso.framework.view.qx.DefaultQxViewFactory.__FIELD_MAX_CHAR_COUNT;
      }
      if (maxCharCount == null || maxCharCount < 0) {
        maxCharCount = org.jspresso.framework.view.qx.DefaultQxViewFactory.__FIELD_MAX_CHAR_COUNT;
      }
      var charCount = maxCharCount;
      if (expectedCharCount < charCount) {
        charCount = expectedCharCount;
      }
      charCount += 2;
      var templateText = "";
      for (var i = 0; i < charCount; i++) {
        templateText += org.jspresso.framework.view.qx.DefaultQxViewFactory.__TEMPLATE_CHAR;
      }
      this._sizeMaxComponentWidthFromText(component, remoteComponent, templateText);
    },

    _sizeMaxComponentWidthFromText: function (component, remoteComponent, text) {
      var compFont = component.getFont();
      if (!compFont) {
        compFont = qx.theme.manager.Font.getInstance().resolve("default");
      }
      var w = qx.bom.Label.getTextSize(text, compFont.getStyles()).width;
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
      var label = atom.getChildControl("label");
      var state = remoteLabel.getState();
      if (state) {
        atom.setAppearance("dynamicatom");
        atom.getChildControl("label").setSelectable(true);
        var modelController = new qx.data.controller.Object(state);
        if (remoteLabel instanceof org.jspresso.framework.gui.remote.RLink && remoteLabel.getAction()) {
          this._getRemotePeerRegistry().register(remoteLabel.getAction());
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
            this._getActionHandler().execute(remoteLabel.getAction());
          }, this);
        } else {
          atom.setRich(true);
          modelController.addTarget(atom, "label", "value", false, {
            converter: function (modelValue, model) {
              return modelValue;
            }
          });
        }
        // Do not size dynamic labels
        // this._sizeMaxComponentWidth(atom, remoteLabel, remoteLabel.getMaxLength())
      } else {
        var labelText = remoteLabel.getLabel();
        labelText = org.jspresso.framework.util.html.HtmlUtil.replaceNewlines(labelText);
        atom.setLabel(labelText);
        atom.setRich(org.jspresso.framework.util.html.HtmlUtil.isHtml(labelText));
      }
      this._configureHorizontalAlignment(label, remoteLabel.getHorizontalAlignment());
      if (remoteLabel.getIcon()) {
        atom.setIcon(remoteLabel.getIcon().getImageUrlSpec());
      }
      return atom;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteDateField {org.jspresso.framework.gui.remote.RDateField}
     */
    _createDateField: function (remoteDateField) {
      var dateField = new qx.ui.form.DateField();
      dateField.setAllowStretchY(false, false);
      var dateFormat = this._createFormat(remoteDateField);
      dateField.setDateFormat(dateFormat);
      var state = remoteDateField.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(dateField, "value", "value", true, {
        converter: function (modelValue, model) {
          if (modelValue instanceof org.jspresso.framework.util.lang.DateDto) {
            return org.jspresso.framework.util.format.DateUtils.fromDateDto(modelValue);
          }
          if (modelValue === undefined) {
            modelValue = null;
          }
          if (!modelValue) {
            dateField.resetValue();
          }
          return modelValue;
        }
      }, {
        converter: function (viewValue, model) {
          if (viewValue != null) {
            return org.jspresso.framework.util.format.DateUtils.fromDate(viewValue);
          }
          if (viewValue === undefined) {
            viewValue = null;
          }
          if (!viewValue) {
            dateField.resetValue();
          }
          return viewValue;
        }
      });
      modelController.addTarget(dateField, "enabled", "writable", false);
      var ps = remoteDateField.getPreferredSize();
      remoteDateField.setPreferredSize(null);
      this._sizeMaxComponentWidth(dateField, remoteDateField,
          org.jspresso.framework.view.qx.DefaultQxViewFactory.__DATE_CHAR_COUNT);
      remoteDateField.setPreferredSize(ps);
      return dateField;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteColorField {org.jspresso.framework.gui.remote.RColorField}
     */
    _createColorField: function (remoteColorField) {
      var colorField = new qx.ui.container.Composite(new qx.ui.layout.HBox())
      colorField.setFocusable(true);
      colorField.setAllowStretchY(false, false);

      var colorPopup = new qx.ui.control.ColorPopup();
      colorPopup.exclude();

      var colorWidget = new qx.ui.basic.Label();
      colorWidget.setBackgroundColor(
          org.jspresso.framework.view.qx.DefaultQxViewFactory._hexColorToQxColor(remoteColorField.getDefaultColor()));
      colorWidget.set({
        appearance: "textfield", textAlign: "center", alignX: "center", alignY: "middle"
      });
      colorWidget.addListener("tap", function (e) {
        colorPopup.placeToPointer(e);
        colorPopup.setValue(this.getBackgroundColor());
        colorPopup.show();
      });

      var resetButton = new qx.ui.form.Button();
      resetButton.setIcon("org/jspresso/framework/reset-small.png");
      if (!remoteColorField.getResetEnabled()) {
        resetButton.setEnabled(false);
      }
      this.addButtonListener(resetButton, function (e) {
        colorWidget.setBackgroundColor(this.getBackgroundColor());
      });
      resetButton.setAllowStretchX(false, false);
      resetButton.setAllowStretchY(false, false);
      resetButton.setAlignY("middle");

      // colorWidget.setWidth(resetButton.getWidth());
      this._sizeMaxComponentWidth(colorWidget, remoteColorField);
      colorWidget.setAllowStretchX(true, true);

      colorPopup.addListener("changeValue", function (e) {
        colorWidget.setBackgroundColor(e.getData());
      });

      var state = remoteColorField.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(colorWidget, "backgroundColor", "value", true, {
        converter: function (modelValue, model) {
          return org.jspresso.framework.view.qx.DefaultQxViewFactory._hexColorToQxColor(modelValue);
        }
      }, {
        converter: function (viewValue, model) {
          return org.jspresso.framework.view.qx.DefaultQxViewFactory._qxColorToHexColor(viewValue);
        }
      });
      modelController.addTarget(colorWidget, "value", "value");
      modelController.addTarget(colorField, "enabled", "writable", false);

      colorField.add(colorWidget, {
        flex: 1
      });
      colorField.add(resetButton);

      colorWidget.setHeight(24);
      resetButton.setWidth(24);
      resetButton.setHeight(24);

      return colorField;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteTabContainer {org.jspresso.framework.gui.remote.RTabContainer}
     */
    _createTabContainer: function (remoteTabContainer) {
      // view remoteTabContainer may have to be retrieved for late update
      // of cards.
      this._getRemotePeerRegistry().register(remoteTabContainer);

      var tabContainer = new qx.ui.tabview.TabView();
      for (var i = 0; i < remoteTabContainer.getTabs().length; i++) {
        /** @type {org.jspresso.framework.gui.remote.RComponent} */
        var remoteTab = remoteTabContainer.getTabs()[i];
        var tabComponent = this.createComponent(remoteTab);

        var tab = new qx.ui.tabview.Page(remoteTab.getLabel());
        this.setIcon(tab.getChildControl("button"), remoteTab.getIcon());
        tab.setLayout(new qx.ui.layout.Grow());
        tab.add(tabComponent);

        tabContainer.add(tab);
      }
      remoteTabContainer.addListener("changeSelectedIndex", function (event) {
        tabContainer.setSelection([tabContainer.getChildren()[event.getData()]]);
      });
      tabContainer.addListener("changeSelection", function (event) {
        var index = tabContainer.indexOf(event.getData()[0]);
        remoteTabContainer.setSelectedIndex(index);
        var command = new org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand();
        command.setTargetPeerGuid(remoteTabContainer.getGuid());
        command.setPermId(remoteTabContainer.getPermId());
        command.setLeadingIndex(index);
        this._getCommandHandler().registerCommand(command);
      }, this);
      tabContainer.setSelection([tabContainer.getChildren()[remoteTabContainer.getSelectedIndex()]]);
      return tabContainer;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteSplitContainer {org.jspresso.framework.gui.remote.RSplitContainer}
     */
    _createSplitContainer: function (remoteSplitContainer) {
      var splitContainer = new qx.ui.splitpane.Pane();
      if (remoteSplitContainer.getOrientation() == "VERTICAL") {
        splitContainer.setOrientation("vertical");
      } else {
        splitContainer.setOrientation("horizontal");
      }

      splitContainer.addListenerOnce("appear", function (appearEvent) {
        var ltComponent, rbComponent, wrapper;
        var ltSize, rbSize, splitSize;
        if (remoteSplitContainer.getLeftTop() != null) {
          ltComponent = this.createComponent(remoteSplitContainer.getLeftTop());
          if (remoteSplitContainer.getOrientation() == "VERTICAL") {
            ltSize = ltComponent.getSizeHint().height;
            splitSize = splitContainer.getBounds().height;
          } else {
            ltSize = ltComponent.getSizeHint().width;
            splitSize = splitContainer.getBounds().width;
          }
          wrapper = new qx.ui.container.Composite(new qx.ui.layout.Grow());
          ltComponent.syncAppearance();
          wrapper.setPadding([ltComponent.getMarginTop(), ltComponent.getMarginRight(), ltComponent.getMarginBottom(),
                              ltComponent.getMarginLeft()]);
          wrapper.add(ltComponent);
          ltComponent = wrapper;
        }
        if (remoteSplitContainer.getRightBottom() != null) {
          rbComponent = this.createComponent(remoteSplitContainer.getRightBottom());
          if (remoteSplitContainer.getOrientation() == "VERTICAL") {
            rbSize = rbComponent.getSizeHint().height;
            splitSize = splitContainer.getBounds().height;
          } else {
            rbSize = rbComponent.getSizeHint().width;
            splitSize = splitContainer.getBounds().width;
          }
          wrapper = new qx.ui.container.Composite(new qx.ui.layout.Grow());
          rbComponent.syncAppearance();
          wrapper.setPadding([rbComponent.getMarginTop(), rbComponent.getMarginRight(), rbComponent.getMarginBottom(),
                              rbComponent.getMarginLeft()]);
          wrapper.add(rbComponent);
          rbComponent = wrapper;
        }
        if ((ltSize + rbSize) < splitSize) {
          splitContainer.add(ltComponent, 0);
          splitContainer.add(rbComponent, 1);
        } else {
          splitContainer.add(ltComponent, ltSize);
          splitContainer.add(rbComponent, rbSize);
        }
      }, this);
      return splitContainer;
    },

    /**
     *
     * @return {qx.ui.core.Widget}
     * @param remoteList {org.jspresso.framework.gui.remote.RList}
     */
    _createList: function (remoteList) {
      var list = new qx.ui.form.List();
      list.setAppearance("list");
      if (remoteList.getSelectionMode() == "SINGLE_SELECTION" || remoteList.getSelectionMode()
          == "SINGLE_CUMULATIVE_SELECTION") {
        list.setSelectionMode("single");
      } else {
        list.setSelectionMode("multi");
      }
      /** @type {org.jspresso.framework.state.remote.RemoteCompositeValueState} */
      var state = remoteList.getState();
      var listController = new qx.data.controller.List(state.getChildren(), list, "children[1].value");
      listController.setDelegate(new org.jspresso.framework.view.qx.EnhancedListDelegate());
      if (remoteList.getDisplayIcon()) {
        listController.setIconPath("iconImageUrl");
      }

      listController.addListener("changeSelection", function (e) {
        /** @type {qx.data.Array} */
        var selectedItems = e.getData();
        /** @type {qx.data.Array} */
        var items = e.getTarget().getModel();
        var selectedIndices = [];
        var stateSelection = state.getSelectedIndices();
        if (!stateSelection) {
          stateSelection = []
        }
        for (var i = 0; i < selectedItems.length; i++) {
          selectedIndices.push(items.indexOf(selectedItems.getItem(i)));
        }
        if (!qx.lang.Array.equals(selectedIndices, stateSelection)) {
          if (selectedIndices.length > 0) {
            state.setLeadingIndex(selectedIndices[selectedIndices.length - 1]);
            state.setSelectedIndices(selectedIndices);
          } else {
            state.setLeadingIndex(-1);
            state.setSelectedIndices(null);
          }
        }
      }, this);

      state.addListener("changeSelectedIndices", function (e) {
        /** @type {Array} */
        var stateSelection = e.getTarget().getSelectedIndices();
        if (!stateSelection) {
          stateSelection = [];
        }

        var items = listController.getModel();
        var stateSelectedItems = [];
        var i;
        for (i = 0; i < stateSelection.length; i++) {
          stateSelectedItems.push(items.getItem(stateSelection[i]));
        }

        /** @type {qx.data.Array} */
        var controllerSelection = listController.getSelection();
        var controllerSelectionContent = controllerSelection.toArray();

        if (!qx.lang.Array.equals(stateSelectedItems, controllerSelectionContent)) {

          var oldLength = controllerSelectionContent.length;
          var newLength = stateSelectedItems.length;

          for (i = 0; i < stateSelectedItems.length; i++) {
            controllerSelectionContent[i] = stateSelectedItems[i];
          }
          controllerSelectionContent.length = newLength;
          controllerSelection.length = newLength;
          controllerSelection.fireEvent("changeLength", qx.event.type.Event);
          controllerSelection.fireDataEvent("change", {
            start: 0, end: newLength, type: "add", items: controllerSelectionContent
          }, null);
        }
      }, this);

      if (remoteList.getRowAction()) {
        this._getRemotePeerRegistry().register(remoteList.getRowAction());
        list.addListener("dbltap", function (e) {
          this._getActionHandler().execute(remoteList.getRowAction());
        }, this);
      }
      return list;
    },

    /**
     * @return {Boolean}
     * @param rComponent {org.jspresso.framework.gui.remote.RComponent}
     */
    _isMultiline: function (rComponent) {
      return rComponent instanceof org.jspresso.framework.gui.remote.RTable || rComponent
          instanceof org.jspresso.framework.gui.remote.RTextArea || rComponent
          instanceof org.jspresso.framework.gui.remote.RList || rComponent
          instanceof org.jspresso.framework.gui.remote.RHtmlArea;
    },

    /**
     * @return {Boolean}
     * @param rComponent {org.jspresso.framework.gui.remote.RComponent}
     */
    _isFixedWidth: function (rComponent) {
      return rComponent instanceof org.jspresso.framework.gui.remote.RDateField || rComponent
          instanceof org.jspresso.framework.gui.remote.RTimeField || rComponent
          instanceof org.jspresso.framework.gui.remote.RComboBox || rComponent
          instanceof org.jspresso.framework.gui.remote.RCheckBox || rComponent
          instanceof org.jspresso.framework.gui.remote.RColorField || rComponent
          instanceof org.jspresso.framework.gui.remote.RLabel;
    }

  }
});
