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

qx.Class.define("org.jspresso.framework.view.qx.AbstractQxViewFactory", {
  extend: qx.core.Object,

  type : "abstract",

  statics: {
    __TEMPLATE_CHAR: "O",
    __FIELD_MAX_CHAR_COUNT: 32,
    __NUMERIC_FIELD_MAX_CHAR_COUNT: 16,
    __COLUMN_MAX_CHAR_COUNT: 12,
    __DATE_CHAR_COUNT: 12,
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
    this.__remotePeerRegistry = remotePeerRegistry;
    this.__actionHandler = actionHandler;
    this.__commandHandler = commandHandler;
  },

  members: {
    /** @type {org.jspresso.framework.util.remote.registry.IRemotePeerRegistry } */
    __remotePeerRegistry: null,
    /** @type {org.jspresso.framework.action.IActionHandler } */
    __actionHandler: null,
    /** @type {org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler } */
    __commandHandler: null,

    /** @type {Array } */
    __dateFormats: null,
    /** @type {Array } */
    __timeFormats: null,
    /** @type {Array } */
    __shortTimeFormats: null,
    /** @type {Array } */
    __dateTimeFormats: null,
    /** @type {Array } */
    __shortDateTimeFormats: null,
    /** @type {String } */
    __datePattern: null,
    /** @type {Integer } */
    __firstDayOfWeek: null,

    /**
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @param registerPeers {Boolean}
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     */
    createComponent: function (remoteComponent, registerPeers) {
      if (!remoteComponent) {
        return new qx.ui.core.Widget();
      }
      if (registerPeers == null) {
        registerPeers = true;
      }

      /**
       * @type {qx.ui.core.Widget}
       */
      var component = this._createCustomComponent(remoteComponent);
      if (component == null) {
        if (remoteComponent instanceof org.jspresso.framework.gui.remote.RActionField) {
          component = this._createActionField(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RActionComponent) {
          component = this._createActionComponent(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RCheckBox) {
          component = this._createCheckBox(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RComboBox) {
          component = this._createComboBox(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RRadioBox) {
          component = this._createRadioBox(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RColorField) {
          component = this._createColorField(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RContainer) {
          component = this._createContainer(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RDateField) {
          component = this._createDateComponent(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RDurationField) {
          component = this._createDurationField(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RImageComponent) {
          component = this._createImageComponent(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RList) {
          component = this._createList(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RNumericComponent) {
          component = this._createNumericComponent(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RSecurityComponent) {
          component = this._createSecurityComponent(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RTable) {
          component = this._createTable(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RForm) {
          component = this._createForm(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RTextComponent) {
          component = this._createTextComponent(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RTimeField) {
          component = this._createTimeField(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RTree) {
          component = this._createTree(remoteComponent);
        }
      }
      if (component == null) {
        component = this._createDefaultComponent();
      }
      remoteComponent.assignPeer(component);
      this._configureToolTip(remoteComponent, component);
      component = this._decorateWithActions(remoteComponent, component);
      component = this._decorateWithBorder(remoteComponent, component);
      this.applyComponentStyle(component, remoteComponent);
      this._applyPreferredSize(remoteComponent, component);
      if (registerPeers) {
        this.__remotePeerRegistry.register(remoteComponent);
      }
      return component;
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     */
    _createDefaultComponent: function () {
      throw new Error("_createDefaultComponent is abstract");
    },

    /**
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @param component {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     */
    _applyPreferredSize: function (remoteComponent, component) {
      throw new Error("_applyPreferredSize is abstract");
    },

    /**
     *
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @param component {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @return {undefined}
     */
    _configureToolTip: function (remoteComponent, component) {
      throw new Error("_configureToolTip is abstract");
    },

    /**
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @param component {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     */
    _decorateWithBorder: function (remoteComponent, component) {
      throw new Error("_decorateWithBorder is abstract");
    },

    applyComponentStyle: function (component, remoteComponent) {
      if (remoteComponent.getForeground()) {
        component.setTextColor(org.jspresso.framework.view.qx.AbstractQxViewFactory._hexColorToQxColor(remoteComponent.getForeground()));
      }
      if (remoteComponent.getBackground()) {
        component.setDecorator("main");
        component.setBackgroundColor(org.jspresso.framework.view.qx.AbstractQxViewFactory._hexColorToQxColor(remoteComponent.getBackground()));
      }
      var rFont = remoteComponent.getFont();
      if (rFont) {
        var compFont = component.getFont();
        component.setFont(org.jspresso.framework.view.qx.AbstractQxViewFactory._fontToQxFont(rFont, compFont));
      }
      if (remoteComponent.getStyleName()) {
        component.setAppearance(remoteComponent.getStyleName());
      }
    },

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

    _getRemotePeerRegistry: function () {
      return this.__remotePeerRegistry;
    },

    _getActionHandler: function () {
      return this.__actionHandler;
    },

    _createCustomComponent: function (remoteComponent) {
      return null;
    },

    /**
     *
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remoteSecurityComponent {org.jspresso.framework.gui.remote.RSecurityComponent}
     */
    _createSecurityComponent: function (remoteSecurityComponent) {
      var securityComponent = new qx.ui.core.Widget();
      return securityComponent;
    },

    /**
     *
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remoteList {org.jspresso.framework.gui.remote.RList}
     */
    _createList: function (remoteList) {
      var list = new qx.ui.form.List();
      if (remoteList.getSelectionMode() == "SINGLE_SELECTION" || remoteList.getSelectionMode()
          == "SINGLE_CUMULATIVE_SELECTION") {
        list.setSelectionMode("single");
      } else {
        list.setSelectionMode("multi");
      }
      /** @type {org.jspresso.framework.state.remote.RemoteCompositeValueState } */
      var state = remoteList.getState();
      var listController = new qx.data.controller.List(state.getChildren(), list, "children[1].value");
      listController.setDelegate(new org.jspresso.framework.view.qx.EnhancedListDelegate());
      listController.setIconPath("iconImageUrl");

      listController.addListener("changeSelection", function (e) {
        /** @type {qx.data.Array } */
        var selectedItems = e.getData();
        /** @type {qx.data.Array } */
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
        /** @type {Array } */
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

        /** @type {qx.data.Array } */
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
            start: 0,
            end: newLength,
            type: "add",
            items: controllerSelectionContent
          }, null);
        }
      }, this);

      if (remoteList.getRowAction()) {
        this.__remotePeerRegistry.register(remoteList.getRowAction());
        list.addListener("dblclick", function (e) {
          this.__actionHandler.execute(remoteList.getRowAction());
        }, this);
      }
      return list;
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remoteImageComponent {org.jspresso.framework.gui.remote.RImageComponent}
     */
    _createImageComponent: function (remoteImageComponent) {
      var imageComponent = new qx.ui.basic.Image();
      imageComponent.setScale(false);
      imageComponent.setAlignX("center");
      imageComponent.setAlignY("middle");

      var state = remoteImageComponent.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(imageComponent, "source", "value");

      if (remoteImageComponent.getAction() != null) {
        this.__remotePeerRegistry.register(remoteImageComponent.getAction());
        imageComponent.addListener("click", function (e) {
          this.__actionHandler.execute(remoteImageComponent.getAction());
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
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
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
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remoteNumericComponent {org.jspresso.framework.gui.remote.RNumericComponent}
     */
    _createNumericComponent: function (remoteNumericComponent) {
      var numericComponent;
      if (remoteNumericComponent instanceof org.jspresso.framework.gui.remote.RDecimalComponent) {
        numericComponent = this._createDecimalComponent(remoteNumericComponent);
      } else if (remoteNumericComponent instanceof org.jspresso.framework.gui.remote.RIntegerField) {
        numericComponent = this._createIntegerField(remoteNumericComponent);
      }
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
            org.jspresso.framework.view.qx.AbstractQxViewFactory.__NUMERIC_FIELD_MAX_CHAR_COUNT);
      } else {
        this._sizeMaxComponentWidth(numericComponent, remoteNumericComponent,
            org.jspresso.framework.view.qx.AbstractQxViewFactory.__NUMERIC_FIELD_MAX_CHAR_COUNT,
            org.jspresso.framework.view.qx.AbstractQxViewFactory.__NUMERIC_FIELD_MAX_CHAR_COUNT);
      }
      this._configureHorizontalAlignment(numericComponent, remoteNumericComponent.getHorizontalAlignment());
      return numericComponent;
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remoteDecimalComponent {org.jspresso.framework.gui.remote.RDecimalComponent}
     */
    _createDecimalComponent: function (remoteDecimalComponent) {
      var decimalComponent;
      if (remoteDecimalComponent instanceof org.jspresso.framework.gui.remote.RDecimalField) {
        decimalComponent = this._createDecimalField(remoteDecimalComponent);
      } else if (remoteDecimalComponent instanceof org.jspresso.framework.gui.remote.RPercentField) {
        decimalComponent = this._createPercentField(remoteDecimalComponent);
      }
      return decimalComponent;
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remoteIntegerField {org.jspresso.framework.gui.remote.RIntegerField}
     */
    _createIntegerField: function (remoteIntegerField) {
      var integerField = this._createFormattedField(remoteIntegerField);
      return integerField;
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remoteDecimalField {org.jspresso.framework.gui.remote.RDecimalField}
     */
    _createDecimalField: function (remoteDecimalField) {
      var decimalField = this._createFormattedField(remoteDecimalField);
      return decimalField;
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remotePercentField {org.jspresso.framework.gui.remote.RPercentField}
     */
    _createPercentField: function (remotePercentField) {
      var percentField = this._createFormattedField(remotePercentField);
      return percentField;
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remoteTimeField {org.jspresso.framework.gui.remote.RTimeField}
     */
    _createTimeField: function (remoteTimeField) {
      var timeField = this._createFormattedField(remoteTimeField);
      this._sizeMaxComponentWidth(timeField, remoteTimeField,
          org.jspresso.framework.view.qx.AbstractQxViewFactory.__TIME_CHAR_COUNT);
      return timeField;
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remoteDurationField {org.jspresso.framework.gui.remote.RDurationField}
     */
    _createDurationField: function (remoteDurationField) {
      var durationField = this._createFormattedField(remoteDurationField);
      return durationField;
    },

    /**
     *
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param rComponent {org.jspresso.framework.gui.remote.RComponent}
     */
    _createFormattedField: function (rComponent) {
      var format = this._createFormat(rComponent);
      var formattedField = new qx.ui.form.TextField();

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
      return formattedField;
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remoteColorField {org.jspresso.framework.gui.remote.RColorField}
     */
    _createColorField: function (remoteColorField) {
      var colorField = new qx.ui.container.Composite();
      colorField.setFocusable(true);
      colorField.setAllowStretchY(false, false);
      colorField.setLayout(new qx.ui.layout.HBox());

      var colorPopup = new qx.ui.control.ColorPopup();
      colorPopup.exclude();

      var colorWidget = new qx.ui.basic.Label();
      colorWidget.setBackgroundColor(org.jspresso.framework.view.qx.AbstractQxViewFactory._hexColorToQxColor(remoteColorField.getDefaultColor()));
      colorWidget.set({
        decorator: "main",
        textAlign: "center",
        alignX: "center",
        alignY: "middle"
      });
      colorWidget.addListener("mousedown", function (e) {
        colorPopup.placeToMouse(e);
        colorPopup.setValue(this.getBackgroundColor());
        colorPopup.show();
      });

      var resetButton = new qx.ui.form.Button();
      resetButton.setIcon("qx/icon/Oxygen/16/actions/dialog-close.png");
      if (!remoteColorField.getResetEnabled()) {
        resetButton.setEnabled(false);
      }
      this.addButtonListener(resetButton, function (e) {
        colorWidget.setBackgroundColor(this.getBackgroundColor());
      });
      resetButton.setAllowStretchX(false, false);
      resetButton.setAllowStretchY(false, false);
      resetButton.setAlignY("middle");

      // colorWidget.addListener("resize", function(e) {
      // var dim = e.getData().height;
      // resetButton.getChildControl("icon").set({
      // scale : true,
      // width : dim - resetButton.getPaddingLeft(),
      // height : dim - resetButton.getPaddingLeft()
      // });
      // });

      // colorWidget.setWidth(resetButton.getWidth());
      this._sizeMaxComponentWidth(colorWidget, remoteColorField);
      colorWidget.setHeight(22/* resetButton.getHeight() */);
      colorWidget.setAllowStretchX(true, true);

      colorPopup.addListener("changeValue", function (e) {
        colorWidget.setBackgroundColor(e.getData());
      });

      var state = remoteColorField.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(colorWidget, "backgroundColor", "value", true, {
        converter: function (modelValue, model) {
          return org.jspresso.framework.view.qx.AbstractQxViewFactory._hexColorToQxColor(modelValue);
        }
      }, {
        converter: function (viewValue, model) {
          return org.jspresso.framework.view.qx.AbstractQxViewFactory._qxColorToHexColor(viewValue);
        }
      });
      modelController.addTarget(colorWidget, "value", "value");
      modelController.addTarget(colorField, "enabled", "writable", false);

      colorField.add(colorWidget, {
        flex: 1
      });
      colorField.add(resetButton);

      return colorField;
    },

    /**
     *
     * @param remoteCheckBox {org.jspresso.framework.gui.remote.RCheckBox}
     * @param checkBox {qx.ui.form.CheckBox|qx.ui.mobile.form.CheckBox}
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
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remoteCheckBox {org.jspresso.framework.gui.remote.RCheckBox}
     */
    _createCheckBox: function (remoteCheckBox) {
      throw new Error("_createCheckBox is abstract");
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remoteComboBox {org.jspresso.framework.gui.remote.RComboBox}
     */
    _createComboBox: function (remoteComboBox) {
      throw new Error("_createComboBox is abstract");
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
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

    _createDateComponent: function (remoteDateField) {
      var dateComponent;
      if (remoteDateField.getType() == "DATE_TIME") {
        dateComponent = this._createDateTimeField(remoteDateField);
      } else {
        dateComponent = this._createDateField(remoteDateField);
      }
      return dateComponent;
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
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
      this._sizeMaxComponentWidth(dateField, remoteDateField,
          org.jspresso.framework.view.qx.AbstractQxViewFactory.__DATE_CHAR_COUNT);
      return dateField;
    },

    _createDateTimeField: function (remoteDateField) {
      var dateTimeField = new qx.ui.container.Composite();
      dateTimeField.setLayout(new qx.ui.layout.HBox());

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
      remoteTimeField.useDateDto(true);
      dateTimeField.add(this.createComponent(remoteTimeField, false));
      this._sizeMaxComponentWidth(dateTimeField, remoteDateField,
          org.jspresso.framework.view.qx.AbstractQxViewFactory.__DATE_CHAR_COUNT
              + org.jspresso.framework.view.qx.AbstractQxViewFactory.__TIME_CHAR_COUNT + 4);
      return dateTimeField;
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remoteActionComponent {org.jspresso.framework.gui.remote.RActionComponent}
     */
    _createActionComponent: function (remoteActionComponent) {
      var actionComponent = this.createAction(remoteActionComponent.getAction());
      return actionComponent;
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
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
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
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
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
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
      if (remoteBorderContainer.getSouth()) {
        var child = this.createComponent(remoteBorderContainer.getSouth());
        borderContainer.add(child, {
          edge: "south"
        });
      }
      if (remoteBorderContainer.getEast()) {
        var child = this.createComponent(remoteBorderContainer.getEast());
        borderContainer.add(child, {
          edge: "east"
        });
      }
      if (remoteBorderContainer.getCenter()) {
        var child = this.createComponent(remoteBorderContainer.getCenter());
        borderContainer.add(child, {
          edge: "center"
        });
      }
      return borderContainer;
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remoteCardContainer {org.jspresso.framework.gui.remote.RCardContainer}
     */
    _createCardContainer: function (remoteCardContainer) {
      var cardContainer = new qx.ui.container.Stack();

      for (var i = 0; i < remoteCardContainer.getCardNames().length; i++) {
        var rCardComponent = remoteCardContainer.getCards()[i];
        var cardName = remoteCardContainer.getCardNames()[i];

        this.addCard(cardContainer, rCardComponent, cardName);
      }

      /** @type {org.jspresso.framework.state.remote.RemoteValueState } */
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
          cardContainer.setSelection([selectedCard]);
        }
      }, this);
      return cardContainer;
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remoteConstrainedGridContainer {org.jspresso.framework.gui.remote.RConstrainedGridContainer}
     */
    _createConstrainedGridContainer: function (remoteConstrainedGridContainer) {
      var constrainedGridContainer = new qx.ui.container.Composite();
      var gridLayout = new qx.ui.layout.Grid();
      constrainedGridContainer.setLayout(gridLayout);

      for (var i = 0; i < remoteConstrainedGridContainer.getCellConstraints().length; i++) {
        /** @type {org.jspresso.framework.util.gui.CellConstraints } */
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
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
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
          row: r,
          column: c
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

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
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
            return org.jspresso.framework.view.qx.AbstractQxViewFactory._hexColorToQxColor(modelValue);
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
            return org.jspresso.framework.view.qx.AbstractQxViewFactory._hexColorToQxColor(modelValue);
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
            return org.jspresso.framework.view.qx.AbstractQxViewFactory._fontToQxFont(modelValue);
          }
        });
      }
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remoteSplitContainer {org.jspresso.framework.gui.remote.RSplitContainer}
     */
    _createSplitContainer: function (remoteSplitContainer) {
      var splitContainer = new qx.ui.splitpane.Pane();

      if (remoteSplitContainer.getOrientation() == "VERTICAL") {
        splitContainer.setOrientation("vertical");
      } else {
        splitContainer.setOrientation("horizontal");
      }

      var component;
      if (remoteSplitContainer.getLeftTop() != null) {
        component = this.createComponent(remoteSplitContainer.getLeftTop());
        var lflex;
        if (remoteSplitContainer.getOrientation() == "VERTICAL") {
          lflex = component.getSizeHint().height;
        } else {
          lflex = component.getSizeHint().width;
        }
        splitContainer.add(component, lflex);
      }
      if (remoteSplitContainer.getRightBottom() != null) {
        component = this.createComponent(remoteSplitContainer.getRightBottom());
        var rflex;
        if (remoteSplitContainer.getOrientation() == "VERTICAL") {
          rflex = component.getSizeHint().height;
        } else {
          rflex = component.getSizeHint().width;
        }
        splitContainer.add(component, rflex);
      }
      return splitContainer;
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remoteTabContainer {org.jspresso.framework.gui.remote.RTabContainer}
     */
    _createTabContainer: function (remoteTabContainer) {
      // view remoteTabContainer may have to be retrieved for late update
      // of cards.
      this.__remotePeerRegistry.register(remoteTabContainer);

      var tabContainer = new qx.ui.tabview.TabView();
      for (var i = 0; i < remoteTabContainer.getTabs().length; i++) {
        /** @type {org.jspresso.framework.gui.remote.RComponent } */
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
        this.__commandHandler.registerCommand(command);
      }, this);

      return tabContainer;
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remoteTextComponent {org.jspresso.framework.gui.remote.RTextComponent}
     */
    _createTextComponent: function (remoteTextComponent) {
      var textComponent;
      if (remoteTextComponent instanceof org.jspresso.framework.gui.remote.RTextArea) {
        textComponent = this._createTextArea(remoteTextComponent);
      } else if (remoteTextComponent instanceof org.jspresso.framework.gui.remote.RHtmlArea) {
        textComponent = this._createHtmlArea(remoteTextComponent);
      } else if (remoteTextComponent instanceof org.jspresso.framework.gui.remote.RPasswordField) {
        textComponent = this._createPasswordField(remoteTextComponent);
      } else if (remoteTextComponent instanceof org.jspresso.framework.gui.remote.RTextField) {
        textComponent = this._createTextField(remoteTextComponent);
      } else if (remoteTextComponent instanceof org.jspresso.framework.gui.remote.RLabel) {
        textComponent = this._createLabel(remoteTextComponent);
      }
      return textComponent;
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
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
      this._sizeMaxComponentWidth(textArea, remoteTextArea);
      return textArea;
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remoteHtmlArea {org.jspresso.framework.gui.remote.RHtmlArea}
     */
    _createHtmlArea: function (remoteHtmlArea) {
      var htmlComponent;
      if (remoteHtmlArea.getReadOnly()) {
        htmlComponent = this._createHtmlText(remoteHtmlArea);
      } else {
        htmlComponent = this._createHtmlEditor(remoteHtmlArea);
      }
      return htmlComponent;
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remoteHtmlArea {org.jspresso.framework.gui.remote.RHtmlArea}
     */
    _createHtmlEditor: function (remoteHtmlArea) {
      var htmlEditor = new qx.ui.embed.HtmlArea(null, null, "blank.html");
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
        toolTipText: this.__actionHandler.translate("change_font_family"),
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
          focusable: false,
          keepFocus: true,
          font: qx.bom.Font.fromString("12px " + entries[i])
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
        toolTipText: this.__actionHandler.translate("change_font_size"),
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
          focusable: false,
          keepFocus: true
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
      var toolbarEntries = [
        {
          bold: {
            text: this.__actionHandler.translate("format_bold"),
            image: "qx/icon/Oxygen/16/actions/format-text-bold.png",
            action: htmlEditor.setBold
          },
          italic: {
            text: this.__actionHandler.translate("format_italic"),
            image: "qx/icon/Oxygen/16/actions/format-text-italic.png",
            action: htmlEditor.setItalic
          },
          underline: {
            text: this.__actionHandler.translate("format_underline"),
            image: "qx/icon/Oxygen/16/actions/format-text-underline.png",
            action: htmlEditor.setUnderline
          },
          strikethrough: {
            text: this.__actionHandler.translate("format_strikethrough"),
            image: "qx/icon/Oxygen/16/actions/format-text-strikethrough.png",
            action: htmlEditor.setStrikeThrough
          },
          removeFormat: {
            text: this.__actionHandler.translate("remove_format"),
            image: "qx/icon/Oxygen/16/actions/edit-clear.png",
            action: htmlEditor.removeFormat
          }
        },

        {
          alignLeft: {
            text: this.__actionHandler.translate("align_left"),
            image: "qx/icon/Oxygen/16/actions/format-justify-left.png",
            action: htmlEditor.setJustifyLeft
          },
          alignCenter: {
            text: this.__actionHandler.translate("align_center"),
            image: "qx/icon/Oxygen/16/actions/format-justify-center.png",
            action: htmlEditor.setJustifyCenter
          },
          alignRight: {
            text: this.__actionHandler.translate("align_right"),
            image: "qx/icon/Oxygen/16/actions/format-justify-right.png",
            action: htmlEditor.setJustifyRight
          },
          alignJustify: {
            text: this.__actionHandler.translate("align_justify"),
            image: "qx/icon/Oxygen/16/actions/format-justify-fill.png",
            action: htmlEditor.setJustifyFull
          }
        },

        {
          fontFamily: {
            custom: this._fontFamilyToolbarEntry
          },
          fontSize: {
            custom: this._fontSizeToolbarEntry
          }
          // ,
          // fontColor: { text: "Set Text Color", image:
          // "demobrowser/demo/icons/htmlarea/format-text-color.png",
          // action: this._fontColorHandler },
          // textBackgroundColor: { text: "Set Text Background Color",
          // image:
          // "demobrowser/demo/icons/htmlarea/format-fill-color.png",
          // action: this._textBackgroundColorHandler }
        },

        {
          indent: {
            text: this.__actionHandler.translate("indent_more"),
            image: "qx/icon/Oxygen/16/actions/format-indent-more.png",
            action: htmlEditor.insertIndent
          },
          outdent: {
            text: this.__actionHandler.translate("indent_less"),
            image: "qx/icon/Oxygen/16/actions/format-indent-less.png",
            action: htmlEditor.insertOutdent
          }
        },

        // {
        // insertImage: { text: "Insert Image", image:
        // "qx/icon/Oxygen/16/actions/insert-image.png", action:
        // this._insertImageHandler },
        // insertLink: { text: "Insert Link", image:
        // "qx/icon/Oxygen/16/actions/insert-link.png", action:
        // this._insertLinkHandler }
        // },

        {
          ol: {
            text: this.__actionHandler.translate("insert_ordered_list"),
            image: "org/jspresso/framework/htmleditor/list-ordered.png",
            action: htmlEditor.insertOrderedList
          },
          ul: {
            text: this.__actionHandler.translate("insert_unordered_list"),
            image: "org/jspresso/framework/htmleditor/list-unordered.png",
            action: htmlEditor.insertUnorderedList
          }
        }
        // ,
        // {
        // undo: { text: this.__actionHandler.translate("undo"), image:
        // "qx/icon/Oxygen/16/actions/edit-undo.png", action:
        // htmlEditor.undo },
        // redo: { text: this.__actionHandler.translate("redo"), image:
        // "qx/icon/Oxygen/16/actions/edit-redo.png", action:
        // htmlEditor.redo }
        // }
      ];
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
              focusable: false,
              keepFocus: true,
              center: true,
              toolTipText: infos.text ? infos.text : ""
            });
            this.addButtonListener(button, infos.action, htmlEditor);
          }
          part.add(button);
        }
      }
      return toolbar;
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remoteHtmlArea {org.jspresso.framework.gui.remote.RHtmlArea}
     */
    _createHtmlText: function (remoteHtmlArea) {
      var htmlText = new qx.ui.basic.Label();
      htmlText.setRich(true);
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
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remoteTextField  {org.jspresso.framework.gui.remote.RTextField}
     */
    _createTextField: function (remoteTextField) {
      throw new Error("_createTextField is abstract");
    },

    /**
     * @param remoteTextField  {org.jspresso.framework.gui.remote.RTextField}
     * @param textField {qx.ui.form.TextField|qx.ui.mobile.form.TextField}
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
          this.__actionHandler.execute(remoteTextField.getCharacterAction(), actionEvent);
        }, this);
      }
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remoteLabel {org.jspresso.framework.gui.remote.RLabel}
     */
    _createLabel: function (remoteLabel) {
      var atom = new qx.ui.basic.Atom();
      var label = atom.getChildControl("label");
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
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remotePasswordField {org.jspresso.framework.gui.remote.RPasswordField}
     */
    _createPasswordField: function (remotePasswordField) {
      throw new Error("_createPasswordField is abstract");
    },

    /**
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param remoteTree {org.jspresso.framework.gui.remote.RTree}
     */
    _createTree: function (remoteTree) {
      throw new Error("_createTree is abstract");
    },

    /**
     *
     * @return {qx.ui.core.Command}
     * @param remoteAction {org.jspresso.framework.gui.remote.RAction}
     */
    createCommand: function (remoteAction) {
      var accel = remoteAction.getAcceleratorAsString();
      if (accel) {
        accel = accel.replace(" ", "+", "g");
      }
      var command = new qx.ui.core.Command(accel);
      this.setIcon(command, remoteAction.getIcon());
      if (remoteAction.getName()) {
        command.setLabel(remoteAction.getName());
      }
      if (remoteAction.getDescription()) {
        command.setToolTipText(remoteAction.getDescription());
      }
      remoteAction.bind("enabled", command, "enabled");
      command.addListener("execute", function (e) {
        this.__actionHandler.execute(remoteAction);
      }, this);
      return command;
    },

    /**
     * @return {qx.ui.form.Button}
     */
    createOkButton: function () {
      var b = new qx.ui.form.Button();
      b.setIcon("qx/icon/Oxygen/22/actions/dialog-ok.png");
      b.setLabel(this.__actionHandler.translate("ok"));
      return b;
    },

    /**
     * @return {qx.ui.form.Button}
     */
    createCancelButton: function () {
      var b = new qx.ui.form.Button();
      b.setIcon("qx/icon/Oxygen/22/actions/dialog-cancel.png");
      b.setLabel(this.__actionHandler.translate("cancel"));
      return b;
    },

    /**
     * @return {qx.ui.form.Button}
     */
    createYesButton: function () {
      var b = new qx.ui.form.Button();
      b.setIcon("qx/icon/Oxygen/22/actions/dialog-ok.png");
      b.setLabel(this.__actionHandler.translate("yes"));
      return b;
    },

    /**
     * @return {qx.ui.form.Button}
     */
    createNoButton: function () {
      var b = new qx.ui.form.Button();
      b.setIcon("qx/icon/Oxygen/22/actions/dialog-close.png");
      b.setLabel(this.__actionHandler.translate("no"));
      return b;
    },

    /**
     *
     * @return {qx.ui.form.Button|qx.ui.mobile.form.Button}
     * @param toolTip {String}
     * @param label {String}
     * @param icon {org.jspresso.framework.gui.remote.RIcon}
     */
    createButton: function (label, toolTip, icon) {
      throw new Error("createButton is abstract");
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
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
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
      var button = new qx.ui.form.SplitButton(menuItems[0].getLabel(), menuItems[0].getIcon(), menu,
          menuItems[0].getCommand());
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
      this.__remotePeerRegistry.register(remoteAction);
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
     *
     * @param component {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @param icon {org.jspresso.framework.gui.remote.RIcon}
     */
    setIcon: function (component, icon) {
      if (icon) {
        component.setIcon(icon.getImageUrlSpec());
      }
    },

    /**
     *
     * @return {qx.ui.form.Button}
     * @param remoteAction {org.jspresso.framework.gui.remote.RAction}
     */
    createAction: function (remoteAction) {
      var button = this.createButton(remoteAction.getName(), remoteAction.getDescription(), remoteAction.getIcon());
      remoteAction.bind("enabled", button, "enabled");
      this.__remotePeerRegistry.register(remoteAction);
      this.addButtonListener(button, function (event) {
        this.__actionHandler.execute(remoteAction);
      }, this);
      if (remoteAction.getStyleName()) {
        button.setAppearance(remoteAction.getStyleName());
      }
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
        cardContainer.setSelection([cardComponent]);
      }
    },

    _modelToViewFieldConverter: function (modelValue, model) {
      if (modelValue == null) {
        return "";
      }
      return modelValue;
    },

    _viewToModelFieldConverter: function (viewValue, model) {
      if (viewValue == "") {
        return null;
      }
      return viewValue;
    },

    _readOnlyFieldConverter: function (writable, model) {
      return !writable;
    },

    /**
     *
     * @return {qx.util.format.IFormat}
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     */
    _createFormat: function (remoteComponent) {
      var format;
      if (remoteComponent instanceof org.jspresso.framework.gui.remote.RDateField) {
        if (remoteComponent.getType() == "DATE_TIME") {
          var dateTimeFormat = new org.jspresso.framework.util.format.DateFormatDecorator();
          if (remoteComponent.getSecondsAware()) {
            if (!this.__dateTimeFormats) {
              this.__dateTimeFormats = this._createDateFormats(this._createDateTimeFormatPatterns());
            }
            dateTimeFormat.setFormatDelegates(this.__dateTimeFormats);
          } else {
            if (!this.__shortDateTimeFormats) {
              this.__shortDateTimeFormats = this._createDateFormats(this._createShortDateTimeFormatPatterns());
            }
            dateTimeFormat.setFormatDelegates(this.__shortDateTimeFormats);
          }
          dateTimeFormat.setRemoteComponent(remoteComponent);
          return dateTimeFormat;
        } else {
          var dateFormat = new org.jspresso.framework.util.format.DateFormatDecorator();
          if (!this.__dateFormats) {
            this.__dateFormats = this._createDateFormats(this._createDateFormatPatterns());
          }
          dateFormat.setFormatDelegates(this.__dateFormats);
          dateFormat.setRemoteComponent(remoteComponent);
          return dateFormat;
        }
      } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RPasswordField) {
        return new org.jspresso.framework.util.format.PasswordFormat();
      } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RTimeField) {
        var timeFormat = new org.jspresso.framework.util.format.DateFormatDecorator();
        if (remoteComponent.getSecondsAware()) {
          if (!this.__timeFormats) {
            this.__timeFormats = this._createDateFormats(this._createTimeFormatPatterns());
          }
          timeFormat.setFormatDelegates(this.__timeFormats);
        } else {
          if (!this.__shortTimeFormats) {
            this.__shortTimeFormats = this._createDateFormats(this._createShortTimeFormatPatterns());
          }
          timeFormat.setFormatDelegates(this.__shortTimeFormats);
        }
        timeFormat.setRemoteComponent(remoteComponent);
        return timeFormat;
      } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RNumericComponent) {
        if (remoteComponent instanceof org.jspresso.framework.gui.remote.RDecimalComponent) {
          if (remoteComponent instanceof org.jspresso.framework.gui.remote.RPercentField) {
            format = new org.jspresso.framework.util.format.ScaledNumberFormat();
            format.setScale(100);
            format.setPostfix(" %");
          } else {
            format = new qx.util.format.NumberFormat();
          }
          if (remoteComponent.getMaxFractionDigit()) {
            format.setMaximumFractionDigits(remoteComponent.getMaxFractionDigit());
            format.setMinimumFractionDigits(remoteComponent.getMaxFractionDigit());
          }
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RIntegerField) {
          format = new qx.util.format.NumberFormat();
          format.setMaximumFractionDigits(0);
        }
      }
      return format;
    },

    _createTimeFormatPatterns: function () {
      var formatPatterns = [];
      formatPatterns.push(qx.locale.Date.getDateTimeFormat("HHmmss", "HH:mm:ss"));
      formatPatterns.push(qx.locale.Date.getDateTimeFormat("HHmm", "HH:mm"));
      formatPatterns.push(qx.locale.Date.getDateTimeFormat("HH", "HH"));
      return formatPatterns;
    },

    _createShortTimeFormatPatterns: function () {
      var formatPatterns = [];
      formatPatterns.push(qx.locale.Date.getDateTimeFormat("HHmm", "HH:mm"));
      formatPatterns.push(qx.locale.Date.getDateTimeFormat("HH", "HH"));
      return formatPatterns;
    },

    _createDateFormatPatterns: function () {
      var formatPatterns = [];
      var defaultFormatPattern = this._getDatePattern();
      formatPatterns.push(defaultFormatPattern);
      for (var i = defaultFormatPattern.length; i > 0; i--) {
        var subPattern = defaultFormatPattern.substring(0, i);
        if (subPattern.charAt(subPattern.length - 1) == "/") {
          formatPatterns.push(subPattern.substring(0, subPattern.length - 1));
        }
      }
      return formatPatterns;
    },

    _createDateTimeFormatPatterns: function () {
      var dateFormatPatterns = this._createDateFormatPatterns();
      var timeFormatPatterns = this._createTimeFormatPatterns();
      var formatPatterns = [];
      for (var i = 0; i < dateFormatPatterns.length; i++) {
        for (var j = 0; j < timeFormatPatterns.length; j++) {
          formatPatterns.push(dateFormatPatterns[i] + " " + timeFormatPatterns[j]);
        }
        formatPatterns.push(dateFormatPatterns[i]);
      }
      return formatPatterns;
    },

    _createShortDateTimeFormatPatterns: function () {
      var dateFormatPatterns = this._createDateFormatPatterns();
      var timeFormatPatterns = this._createShortTimeFormatPatterns();
      var formatPatterns = [];
      for (var i = 0; i < dateFormatPatterns.length; i++) {
        for (var j = 0; j < timeFormatPatterns.length; j++) {
          formatPatterns.push(dateFormatPatterns[i] + " " + timeFormatPatterns[j]);
        }
        formatPatterns.push(dateFormatPatterns[i]);
      }
      return formatPatterns;
    },

    _createDateFormats: function (formatPatterns) {
      var formats = [];
      for (var i = 0; i < formatPatterns.length; i++) {
        formats.push(new qx.util.format.DateFormat(formatPatterns[i]));
      }
      return formats;
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
      component.setWidth(w)
    },

    /**
     *
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
     *
     * @return {undefined}
     * @param datePattern {String}
     */
    setDatePattern: function (datePattern) {
      this.__datePattern = datePattern;
    },

    /**
     *
     * @return {String}
     */
    _getDatePattern: function () {
      return this.__datePattern;
    },

    /**
     *
     * @return {undefined}
     * @param firstDayOfWeek {Integer}
     */
    setFirstDayOfWeek: function (firstDayOfWeek) {
      this.__firstDayOfWeek = firstDayOfWeek;
    },

    /**
     *
     * @return {Integer}
     */
    _getFirstDayOfWeek: function () {
      return this.__firstDayOfWeek;
    },

    /**
     * @param root {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     */
    _findFirstFocusableComponent: function (root) {
      throw new Error("_findFirstFocusableComponent is abstract");
    },

    /**
     * @param root {qx.ui.core.Widget}
     * @return {qx.ui.core.Widget|qx.ui.mobile.core.Widget}
     */
    _findFirstEditableComponent: function (root) {
      if (root instanceof qx.ui.table.Table) {
        if (root.getEnabled()) {
          return root;
        }
      }
      if (root instanceof qx.ui.container.Composite || root instanceof qx.ui.splitpane.Pane || root
          instanceof qx.ui.tabview.TabView) {
        for (var i = 0; i < (/** @type {qx.ui.core.MChildrenHandling } */ root).getChildren().length; i++) {
          var child = (/** @type {qx.ui.core.MChildrenHandling } */ root).getChildren()[i];
          if (child instanceof qx.ui.core.Widget) {
            var editableChild = this._findFirstEditableComponent(/** @type {qx.ui.core.Widget } */ child);
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
     * @param button {qx.ui.form.Button|qx.ui.menu.Button|qx.ui.mobile.form.Button}
     * @param listener {function}
     * @param that {var}
     */
    addButtonListener: function(button, listener, that) {
      throw new Error("addButtonListener is abstract");
    }
  }
});