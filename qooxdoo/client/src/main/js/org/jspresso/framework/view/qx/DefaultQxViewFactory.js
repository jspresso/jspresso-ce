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

/*
#asset(qx/icon/Oxygen/22/actions/dialog-ok.png)
#asset(qx/icon/Oxygen/22/actions/dialog-close.png)
#asset(qx/icon/Oxygen/22/actions/dialog-cancel.png)
#asset(qx/icon/Oxygen/16/actions/dialog-close.png)
*/
qx.Class.define("org.jspresso.framework.view.qx.DefaultQxViewFactory",
{
  extend : qx.core.Object,
  
  statics :
  {
    __TOOLTIP_ELLIPSIS : "...",
    __TEMPLATE_CHAR : "O",
    __FIELD_MAX_CHAR_COUNT : 32,
    __COLUMN_MAX_CHAR_COUNT : 12,
    __DATE_CHAR_COUNT : 12,
    __TIME_CHAR_COUNT : 6,

    __hexColorToQxColor : function(hexColor) {
      if(hexColor) {
        return "#" + hexColor.substring(4); //ignore alpha
      }
      return hexColor;
    },
    
    __qxColorToHexColor : function(qxColor) {
      if(qxColor) {
        var rgbColor = qx.util.ColorUtil.stringToRgb(qxColor);
        return "0x" + "FF" + qx.util.ColorUtil.rgbToHexString(rgbColor); //ignore alpha
      }
      return qxColor;
    }
  },
  
  construct : function(remotePeerRegistry, actionHandler, commandHandler) {
    this.__remotePeerRegistry = remotePeerRegistry;
    this.__actionHandler = actionHandler;
    this.__commandHandler = commandHandler;
  },
  
  members :
  {
    /**@type org.jspresso.framework.util.remote.registry.IRemotePeerRegistry */
    __remotePeerRegistry : null,
    /**@type org.jspresso.framework.action.IActionHandler */
    __actionHandler : null,
    /**@type org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler*/
    __commandHandler : null,
    __remoteValueSorter : null,
    __timeFormatter : null,
    
    /**
     * @param {org.jspresso.framework.gui.remote.RComponent} remoteComponent
     * @param {Boolean} registerState
     * @return {qx.ui.core.Widget}
     */
    createComponent : function(remoteComponent, registerState) {
      if(registerState == null) {
        registerState = true;
      }
      
      /**
       * @type {qx.ui.core.Widget}
       */
      var component = null;
      if(remoteComponent instanceof org.jspresso.framework.gui.remote.RActionField) {
        component = this.__createActionField(remoteComponent);
      } else if(remoteComponent instanceof org.jspresso.framework.gui.remote.RCheckBox) {
        component = this.__createCheckBox(remoteComponent);
      } else if(remoteComponent instanceof org.jspresso.framework.gui.remote.RComboBox) {
        component = this.__createComboBox(remoteComponent);
      } else if(remoteComponent instanceof org.jspresso.framework.gui.remote.RColorField) {
        component = this.__createColorField(remoteComponent);
      } else if(remoteComponent instanceof org.jspresso.framework.gui.remote.RContainer) {
        component = this.__createContainer(remoteComponent);
      } else if(remoteComponent instanceof org.jspresso.framework.gui.remote.RDateField) {
        component = this.__createDateField(remoteComponent);
      } else if(remoteComponent instanceof org.jspresso.framework.gui.remote.RDurationField) {
        component = this.__createDurationField(remoteComponent);
      } else if(remoteComponent instanceof org.jspresso.framework.gui.remote.RImageComponent) {
        component = this.__createImageComponent(remoteComponent);
      } else if(remoteComponent instanceof org.jspresso.framework.gui.remote.RList) {
        component = this.__createList(remoteComponent);
      } else if(remoteComponent instanceof org.jspresso.framework.gui.remote.RNumericComponent) {
        component = this.__createNumericComponent(remoteComponent);
      } else if(remoteComponent instanceof org.jspresso.framework.gui.remote.RSecurityComponent) {
        component = this.__createSecurityComponent(remoteComponent);
      } else if(remoteComponent instanceof org.jspresso.framework.gui.remote.RTable) {
        component = this.__createTable(remoteComponent);
      } else if(remoteComponent instanceof org.jspresso.framework.gui.remote.RTextComponent) {
        component = this.__createTextComponent(remoteComponent);
      } else if(remoteComponent instanceof org.jspresso.framework.gui.remote.RTimeField) {
        component = this.__createTimeField(remoteComponent);
      } else if(remoteComponent instanceof org.jspresso.framework.gui.remote.RTree) {
        component = this.__createTree(remoteComponent);
      }
      if(remoteComponent.getTooltip() != null) {
        component.setToolTip(new qx.ui.tooltip.ToolTip(remoteComponent.getTooltip()));
      }
      if(!(remoteComponent instanceof org.jspresso.framework.gui.remote.RActionField) && remoteComponent.getActionLists() != null) {
        var toolBar = new qx.ui.toolbar.ToolBar();
        for(var i = 0; i < remoteComponent.getActionLists().length; i++) {
          var actionList = remoteComponent.getActionLists()[i];
          if(actionList.getActions() != null) {
            var part = new qx.ui.toolbar.Part();
            for(var j = 0; j < actionList.getActions().length; j++) {
              part.add(this.createAction(actionList.getActions()[j]));
            }
            toolBar.add(part);
          }
        }
        var surroundingBox = new qx.ui.container.Composite();
        surroundingBox.setLayout(new qx.ui.layout.VBox(2));
        surroundingBox.add(toolBar);
        surroundingBox.add(component, {flex:1});
        component = surroundingBox;
      }
      if(remoteComponent.getBorderType() && remoteComponent.getBorderType() != "NONE") {
        var decorator = new qx.ui.groupbox.GroupBox();
        decorator.setLayout(new qx.ui.layout.Grow());
        if(remoteComponent.getBorderType() == "TITLED") {
          decorator.setLegend(remoteComponent.getLabel());
          this.setIcon(decorator.getChildControl("legend"), remoteComponent.getIcon());
        }
        decorator.add(component);
        component = decorator;
      }
      if(remoteComponent.getForeground()) {
        component.setTextColor(org.jspresso.framework.view.qx.DefaultQxViewFactory.__hexColorToQxColor(remoteComponent.getForeground()));
      }
      if(remoteComponent.getBackground()) {
        component.setBackgroundColor(org.jspresso.framework.view.qx.DefaultQxViewFactory.__hexColorToQxColor(remoteComponent.getBackground()));
      }
      var rFont = remoteComponent.getFont(); 
      if(rFont) {
        var font = new qx.bom.Font();
        var compFont = component.getFont();
        if(!compFont) {
          compFont = qx.theme.manager.Font.getInstance().resolve("default");
        }
        if(rFont.getName()) {
          font.setFamily([rFont.getName()]);
        } else if(compFont) {
          font.setFamily(compFont.getFamily());
        }
        if(rFont.getSize() > 0) {
          font.setSize(rFont.getSize());
        } else if(compFont) {
          font.setSize(compFont.getSize());
        }
        if(rFont.isItalic()) {
          font.setItalic(true);
        }
        if(rFont.isBold()) {
          font.setBold(true);
        }
        component.setFont(font);
      }
      if(registerState) {
        this.__remotePeerRegistry.register(remoteComponent.getState());
      }
      return component;
    },
    
    /**
     * 
     * @param {org.jspresso.framework.gui.remote.RSecurityComponent} remoteSecurityComponent
     * @return {qx.ui.core.Widget}
     */
    __createSecurityComponent : function(remoteSecurityComponent){
      var securityComponent = new qx.ui.core.Widget();
      return securityComponent;
    },
    
    /**
     * 
     * @param {org.jspresso.framework.gui.remote.RList} remoteList
     * @return {qx.ui.core.Widget}
     */
    __createList : function(remoteList) {
      var list = new qx.ui.form.List();
      if(remoteList.getSelectionMode() == "SINGLE_SELECTION") {
        list.setSelectionMode("single");
      } else {
        list.setSelectionMode("multi");
      }
      /**@type org.jspresso.framework.state.remote.RemoteCompositeValueState*/
      var state = remoteList.getState();
      var listController = new qx.data.controller.List(state.getChildren(), list, "value");
      
      listController.addListener("changeSelection", function(e) {
        /**@type qx.data.Array*/
        var selectedItems = e.getData();
        /**@type qx.data.Array*/
        var items = e.getTarget().getModel();
        var selectedIndices = new Array();
        var stateSelection = state.getSelectedIndices();
        if(!stateSelection) {
          stateSelection = new Array()
        }
        for(var i = 0; i < selectedItems.length; i++) {
          selectedIndices.push(items.indexOf(selectedItems.getItem(i)));
        }
        if(!qx.lang.Array.equals(selectedIndices, stateSelection)) {
          if(selectedIndices.length > 0) {
            state.setLeadingIndex(selectedIndices[selectedIndices.length -1])
            state.setSelectedIndices(selectedIndices);
          } else {
            state.setLeadingIndex(-1)
            state.setSelectedIndices(null);
          }
        }
      }, this);
      
      state.addListener("changeSelectedIndices", function(e) {
        var stateSelection = e.getTarget().getSelectedIndices();
        if(!stateSelection) {
          stateSelection = new Array();
        }

        var items = listController.getModel();
        var stateSelectedItems = new Array();
        for(var i = 0; i < stateSelection.length; i++) {
          stateSelectedItems.push(items.getItem(stateSelection[i]));
        }

        var controllerSelection = listController.getSelection();
        if(!qx.lang.Array.equals(stateSelectedItems, controllerSelection)) {
          var oldLength = controllerSelection.getLength();
          var newLength = stateSelectedItems.length;
          controllerSelection.removeAll();
          controllerSelection.append(stateSelectedItems);
          if(newLength > oldLength) {
            controllerSelection.fireDataEvent("change", 
              {start: oldLength, end: newLength - 1, type: "add"}, null
            );
          } else if(newLength < oldLength) {
            controllerSelection.fireDataEvent("change", 
              {start: newLength, end: oldLength - 1, type: "remove"}, null
            );
          }
        }
      }, this);
      if(remoteList.getRowAction()) {
        list.addListener("dblclick", function(e){
          this.__actionHandler.execute(remoteList.getRowAction());
        }, this);
      }
      return list;
    },

    /**
     * @param {org.jspresso.framework.gui.remote.RImageComponent} remoteImageComponent
     * @return {qx.ui.core.Widget}
     */
    __createImageComponent : function(remoteImageComponent) {
      var imageComponent = new qx.ui.basic.Image();
      
      var state = remoteImageComponent.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(imageComponent, "source", "value");
      return imageComponent;
    },

    /**
     * @param {org.jspresso.framework.gui.remote.RTable} remoteTable
     * @return {qx.ui.core.Widget}
     */
    __createTable : function(remoteTable) {
      /**@type org.jspresso.framework.state.remote.RemoteCompositeValueState*/
      var state = remoteTable.getState();
      var tableModel = new org.jspresso.framework.view.qx.RTableModel(state,
                                                                      remoteTable.getSortingAction(),
                                                                      this.__commandHandler);
      tableModel.setEditable(state.isWritable());
      var columnIds = remoteTable.getColumnIds();
      var columnNames = new Array();
      for(var i=0; i < remoteTable.getColumnIds().length; i++) {
        columnNames[i] = remoteTable.getColumns()[i].getLabel();
      }
      tableModel.setColumns(columnNames, columnIds);
      var table = new qx.ui.table.Table(tableModel);

      var columnModel = table.getTableColumnModel();
      for(var i=0; i < remoteTable.getColumnIds().length; i++) {
        var rComponent = remoteTable.getColumns()[i];
        var editor = new org.jspresso.framework.view.qx.RComponentTableCellEditor(this,
                                                                                  rComponent,
                                                                                  this.__actionHandler);
        columnModel.setCellEditorFactory(i, editor);
        var cellRenderer = null;
        if(rComponent instanceof org.jspresso.framework.gui.remote.RCheckBox) {
          cellRenderer = new org.jspresso.framework.view.qx.BooleanTableCellRenderer();
        } else if(rComponent instanceof org.jspresso.framework.gui.remote.RColorField) {
          cellRenderer = new org.jspresso.framework.view.qx.ColorTableCellRenderer();
        } else if(rComponent instanceof org.jspresso.framework.gui.remote.RComboBox) {
          var labels = new Object();
          var icons = new Object();
          for(var j = 0; j < rComponent.getValues().length; j++) {
            var value = rComponent.getValues()[j];
            labels[value] = rComponent.getTranslations()[j];
            icons[value] = rComponent.getIcons()[j];
          }
          cellRenderer = new org.jspresso.framework.view.qx.EnumerationTableCellRenderer(labels,
                                                                                         icons);
        } else if(rComponent instanceof org.jspresso.framework.gui.remote.RActionField
                  && !rComponent.isShowTextField()) {
          cellRenderer = new org.jspresso.framework.view.qx.BinaryTableCellRenderer();
        } else {
          var format = this.__createFormat(rComponent);
          if(format) {
            cellRenderer = new org.jspresso.framework.view.qx.FormattedTableCellRenderer(format);
          }
        }
        if(cellRenderer) {
          columnModel.setDataCellRenderer(i, cellRenderer);
        }
        var tableFont = qx.theme.manager.Font.getInstance().resolve("default");
        var headerWidth = qx.bom.Label.getTextSize(columnNames[i], tableFont.getStyles()).width;
        if(rComponent instanceof org.jspresso.framework.gui.remote.RCheckBox) {
          columnModel.setColumnWidth(i, headerWidth + 16);
        } else {
          var maxColumnWidth = qx.bom.Label.getTextSize(org.jspresso.framework.view.qx.DefaultQxViewFactory.__TEMPLATE_CHAR,
                                               tableFont.getStyles()).width
                               * org.jspresso.framework.view.qx.DefaultQxViewFactory.__COLUMN_MAX_CHAR_COUNT;
          var editorComponent = this.createComponent(rComponent, false);
          var columnWidth = Math.max(
                           Math.min(maxColumnWidth,
                                    editorComponent.getMaxWidth()),
                           headerWidth + 16
                         );
          columnModel.setColumnWidth(i, columnWidth);
        }
      }
      
      table.setHeight(5*table.getRowHeight() + table.getHeaderCellHeight());
      var selectionModel = table.getSelectionModel();
      if(remoteTable.getSelectionMode() == "SINGLE_SELECTION") {
        selectionModel.setSelectionMode(qx.ui.table.selection.Model.SINGLE_SELECTION);
      } else {
        selectionModel.setSelectionMode(qx.ui.table.selection.Model.MULTIPLE_INTERVAL_SELECTION);
      }
      selectionModel.addListener("changeSelection", function(e) {
        if(selectionModel.hasBatchMode()) {
          //TODO notify Qooxdoo batchMode is not working.
          return;
        }
        var leadingIndex = tableModel.viewIndexToModelIndex(selectionModel.getLeadSelectionIndex());
        var selectedRanges = selectionModel.getSelectedRanges();
        
        var selectedIndices = new Array();
        for(var rangeIndex = 0; rangeIndex < selectedRanges.length; rangeIndex++) {
          var range = selectedRanges[rangeIndex];
          for(var i = range.minIndex; i < range.maxIndex +1; i++) {
            selectedIndices.push(i);
          }
        }
        selectedIndices = tableModel.viewIndicesToModelIndices(selectedIndices);
        if(selectedIndices.length == 0) {
          leadingIndex = -1;
        }
        var stateSelection = state.getSelectedIndices()
        if(!stateSelection) {
          stateSelection = new Array();
        }
        if(   !qx.lang.Array.equals(selectedIndices, stateSelection)
           || leadingIndex != state.getLeadingIndex()) {
          if(selectedIndices.length == 0) {
            selectedIndices = null;
          }
          state.setLeadingIndex(leadingIndex);
          state.setSelectedIndices(selectedIndices);
        }
      }, this);
      
      state.addListener("changeSelectedIndices", function(e) {
        var stateSelection = tableModel.modelIndicesToViewIndices(state.getSelectedIndices());
        if(!stateSelection) {
          stateSelection = new Array();
        }

        var selectedRanges = selectionModel.getSelectedRanges();
        var selectedIndices = new Array();
        for(var rangeIndex = 0; rangeIndex < selectedRanges.length; rangeIndex++) {
          var range = selectedRanges[rangeIndex];
          for(var i = range.minIndex; i < range.maxIndex +1; i++) {
            selectedIndices.push(i);
          }
        }

        var stateLeadingIndex = tableModel.modelIndexToViewIndex(state.getLeadingIndex());
        var leadingIndex = selectionModel.getLeadSelectionIndex();
        if(selectedIndices.length == 0) {
          leadingIndex = -1;
        }
        
        if(   !qx.lang.Array.equals(selectedIndices, stateSelection)
           || leadingIndex != stateLeadingIndex) {
          selectionModel.setBatchMode(true);
          selectionModel.clearSelection();
          if(stateSelection.length > 0) {
            var minIndex = -1;
            var maxIndex;
            for(var i = 0; i < stateSelection.length; i++) {
              if(minIndex < 0) {
                minIndex = stateSelection[i];
                maxIndex = minIndex;
              } else {
                var nextSelectedIndex = stateSelection[i];
                if(nextSelectedIndex == maxIndex +1) {
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
          if(stateLeadingIndex >= 0) {
            selectionModel.addSelectionInterval(stateLeadingIndex, stateLeadingIndex);
          }
          selectionModel.setBatchMode(false);
        }
      }, this);
      if(remoteTable.getRowAction()) {
        table.addListener("dblclick", function(e){
          this.__actionHandler.execute(remoteTable.getRowAction());
        }, this);
      }
      return table;
    },

    /**
     * @param {org.jspresso.framework.gui.remote.RNumericComponent} remoteNumericComponent
     * @return {qx.ui.core.Widget}
     */
    __createNumericComponent : function(remoteNumericComponent) {
      var numericComponent;
      if(remoteNumericComponent instanceof org.jspresso.framework.gui.remote.RDecimalComponent) {
        numericComponent = this.__createDecimalComponent(remoteNumericComponent);
      } else if(remoteNumericComponent instanceof org.jspresso.framework.gui.remote.RIntegerField) {
        numericComponent = this.__createIntegerField(remoteNumericComponent);
      }
      if(remoteNumericComponent.getMaxValue()) {
        this.__sizeMaxComponentWidth(numericComponent,
          this.__createFormat(remoteNumericComponent).format(remoteNumericComponent.getMaxValue()).length);
      } else {
        this.__sizeMaxComponentWidth(numericComponent);
      }
      return numericComponent;
    },

    /**
     * @param {org.jspresso.framework.gui.remote.RDecimalComponent} remoteDecimalComponent
     * @return {qx.ui.core.Widget}
     */
    __createDecimalComponent : function(remoteDecimalComponent) {
      var decimalComponent;
      if(remoteDecimalComponent instanceof org.jspresso.framework.gui.remote.RDecimalField) {
        decimalComponent = this.__createDecimalField(remoteDecimalComponent);
      } else if(remoteDecimalComponent instanceof org.jspresso.framework.gui.remote.RPercentField) {
        decimalComponent = this.__createPercentField(remoteDecimalComponent);
      }
      return decimalComponent;
    },
    
    /**
     * @param {org.jspresso.framework.gui.remote.RIntegerField} remoteIntegerField
     * @return {qx.ui.core.Widget}
     */
    __createIntegerField : function(remoteIntegerField) {
      var integerField = this.__createFormattedField(remoteIntegerField);
      return integerField;
    },
    
    /**
     * @param {org.jspresso.framework.gui.remote.RDecimalField} remoteDecimalField
     * @return {qx.ui.core.Widget}
     */
    __createDecimalField : function(remoteDecimalField) {
      var decimalField = this.__createFormattedField(remoteDecimalField);
      return decimalField;
    },
    
    /**
     * @param {org.jspresso.framework.gui.remote.RPercentField} remotePercentField
     * @return {qx.ui.core.Widget}
     */
    __createPercentField : function(remotePercentField) {
      var percentField = this.__createFormattedField(remotePercentField);
      return percentField;
    },
    
    /**
     * @param {org.jspresso.framework.gui.remote.RTimeField} remoteTimeField
     * @return {qx.ui.core.Widget}
     */
    __createTimeField : function(remoteTimeField) {
      var timeField = this.__createFormattedField(remoteTimeField);
      this.__sizeMaxComponentWidth(timeField, org.jspresso.framework.view.qx.DefaultQxViewFactory.__TIME_CHAR_COUNT);
      return timeField;
    },

    /**
     * @param {org.jspresso.framework.gui.remote.RDurationField} remoteDurationField
     * @return {qx.ui.core.Widget}
     */
    __createDurationField : function(remoteDurationField) {
      var durationField = this.__createFormattedField(remoteDurationField);
      return durationField;
    },

    /**
     * 
     * @param {org.jspresso.framework.gui.remote.RComponent} rComponent
     * @return {qx.ui.core.Widget}
     */
    __createFormattedField : function(rComponent) {
      var format = this.__createFormat(rComponent);
      var formattedField = new qx.ui.form.TextField();
      
      var state = rComponent.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(formattedField, "value", "value", true,
       {
          converter : function(modelValue, model) {
            if(modelValue == null) {
              return "";
            }
            var formattedValue = modelValue;
            if(format) {
              formattedValue = format.format(modelValue);
            }
            return formattedValue;
          }
        },
        {
          converter : function(viewValue, model) {
            var parsedValue = viewValue;
            if(format) {
              try {
                parsedValue = format.parse(viewValue);
              } catch(ex) {
                // restore old value.
                parsedValue = state.getValue();
                if(parsedValue) {
                  formattedField.setValue(format.format(parsedValue));
                } else {
                  formattedField.setValue("");
                }
              }
            }
            return parsedValue;
          }
        }
      );
      modelController.addTarget(formattedField, "readOnly", "writable", false,
        {
          converter : this.__readOnlyFieldConverter
        }
      );
      return formattedField;
    },
    
    /**
     * @param {org.jspresso.framework.gui.remote.RColorField} remoteColorField
     * @return {qx.ui.core.Widget}
     */
    __createColorField : function(remoteColorField) {
      var colorField = new qx.ui.container.Composite();
      colorField.setFocusable(true);
      colorField.setAllowStretchY(false, false);
      colorField.setLayout(new qx.ui.layout.HBox());

      var colorPopup = new qx.ui.control.ColorPopup();
      colorPopup.exclude();
      
      var colorWidget = new qx.ui.basic.Label();
      colorWidget.setBackgroundColor(org.jspresso.framework.view.qx.DefaultQxViewFactory.__hexColorToQxColor(remoteColorField.getDefaultColor()));
      colorWidget.set({
        decorator : "main",
        textAlign : "center",
        alignX : "center",
        alignY : "baseline"
      });
      colorWidget.addListener("mousedown", function(e) {
        colorPopup.placeToMouse(e)
        colorPopup.setValue(this.getBackgroundColor());
        colorPopup.show();
      });

      var resetButton = new qx.ui.form.Button();
      resetButton.setIcon("qx/icon/Oxygen/16/actions/dialog-close.png");
      resetButton.addListener("execute", function(e) {
        colorWidget.setBackgroundColor(this.getBackgroundColor());
      });
      resetButton.setAllowStretchX(false, false);
      resetButton.setAllowStretchY(false, false);
      
//      colorWidget.addListener("resize", function(e) {
//        var dim = e.getData().height;
//        resetButton.getChildControl("icon").set({
//          scale : true,
//          width : dim - resetButton.getPaddingLeft(),
//          height : dim - resetButton.getPaddingLeft()
//        });
//      });

      //colorWidget.setWidth(resetButton.getWidth());
      this.__sizeMaxComponentWidth(colorWidget);
      colorWidget.setHeight(22/*resetButton.getHeight()*/);
      colorWidget.setAllowStretchX(true, true);

      colorPopup.addListener("changeValue", function(e) {
        colorWidget.setBackgroundColor(e.getData());
      });
      
      var state = remoteColorField.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(colorWidget, "backgroundColor", "value", true,
        {
          converter : function(modelValue, model) {
            return org.jspresso.framework.view.qx.DefaultQxViewFactory.__hexColorToQxColor(modelValue);
          }
        },
        {
          converter : function(viewValue, model) {
            return org.jspresso.framework.view.qx.DefaultQxViewFactory.__qxColorToHexColor(viewValue);
          }
        }
      );
      modelController.addTarget(colorWidget, "value", "value");
      modelController.addTarget(colorField, "enabled", "writable", false);

      colorField.add(colorWidget, {flex : 1});
      colorField.add(resetButton);
      
      return colorField;
    },

    /**
     * @param {org.jspresso.framework.gui.remote.RCheckBox} remoteCheckBox
     * @return {qx.ui.core.Widget}
     */
    __createCheckBox : function(remoteCheckBox) {
      var checkBox = new qx.ui.form.CheckBox();
      var state = remoteCheckBox.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(checkBox, "value", "value", true,
        {
          converter : function(modelValue, model) {
            if(modelValue == null) {
              return false;
            }
            return modelValue;
          }
        }
      );
      modelController.addTarget(checkBox, "enabled", "writable", false);
      return checkBox;
    },
    
    /**
     * @param {org.jspresso.framework.gui.remote.RComboBox} remoteComboBox
     * @return {qx.ui.core.Widget}
     */
    __createComboBox : function(remoteComboBox) {
      var comboBox = new qx.ui.form.SelectBox();
      comboBox.setAllowStretchY(false, false);
      var iconDim;
      var width = 0;
      for(var i = 0; i < remoteComboBox.getValues().length; i++) {
        var tr = remoteComboBox.getTranslations()[i];
        var li = new qx.ui.form.ListItem(tr/*,
                                         null*/);
        li.setModel(remoteComboBox.getValues()[i]);
        var rIcon = remoteComboBox.getIcons()[i];
        this.setIcon(li, rIcon);
        comboBox.add(li);
        if(!iconDim && rIcon && rIcon.getDimension()) {
          iconDim = rIcon.getDimension();
        }
        if(tr.length > width) {
          width = tr.length;
        }
      }
      width += 7;
      var state = remoteComboBox.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(comboBox, "modelSelection", "value", true,
        {
          converter : function(modelValue, model) {
            return [modelValue];
          }
        },
        {
          converter : function(viewValue, model) {
            var modelValue = null;
            if(viewValue.length > 0) {
              modelValue = viewValue[0];
            } else {
              modelValue = null;
            }
            return modelValue;
          }
        }
      );
      modelController.addTarget(comboBox, "enabled", "writable", false);
      this.__sizeMaxComponentWidth(comboBox, width);
      return comboBox;
    },

    /**
     * @param {org.jspresso.framework.gui.remote.RDateField} remoteDateField
     * @return {qx.ui.core.Widget}
     */
    __createDateField : function(remoteDateField) {
      var dateField = new qx.ui.form.DateField();
      dateField.setAllowStretchY(false, false);
      var dateFormat = this.__createFormat(remoteDateField);
      dateField.setDateFormat(dateFormat);
      var state = remoteDateField.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(dateField, "value", "value", true);
      modelController.addTarget(dateField, "enabled", "writable", false);
      if(remoteDateField.getType() == "DATE_TIME") {
        this.__sizeMaxComponentWidth(dateField,  org.jspresso.framework.view.qx.DefaultQxViewFactory.__DATE_CHAR_COUNT
                                               + org.jspresso.framework.view.qx.DefaultQxViewFactory.__TIME_CHAR_COUNT);
      } else {
        this.__sizeMaxComponentWidth(dateField, org.jspresso.framework.view.qx.DefaultQxViewFactory.__DATE_CHAR_COUNT);
      }
      return dateField;
    },

    /**
     * @param {org.jspresso.framework.gui.remote.RActionField} remoteActionField
     * @return {qx.ui.core.Widget}
     */
    __createActionField : function(remoteActionField) {
      var actionField = new qx.ui.container.Composite();
      actionField.setFocusable(true);
      actionField.setAllowStretchY(false, false);
      actionField.setLayout(new qx.ui.layout.HBox());
      /**@type qx.ui.form.TextField*/
      var textField;
      var mainAction;
      
      if(remoteActionField.isShowTextField()) {
        textField = new qx.ui.form.TextField();
        actionField.add(textField, {flex:1});
        // propagate focus
        actionField.addListener("focus", function() {
          textField.focus();
        });
  
        // propagate active state
        actionField.addListener("activate", function() {
          textField.activate();
        });
        
        this.__sizeMaxComponentWidth(textField);
      }
      for(var i = 0; i < remoteActionField.getActionLists().length; i++) {
        var actionList = remoteActionField.getActionLists()[i];
        for(var j = 0; j < actionList.getActions().length; j++) {
          var actionComponent = this.createAction(actionList.getActions()[j])
          actionField.add(actionComponent);
          if(!mainAction) {
            mainAction = actionList.getActions()[j];
          }
        }
      }

      var state = remoteActionField.getState();
      
      var modelController = new qx.data.controller.Object(state);

      modelController.addTarget(actionField, "enabled", "writable", false);
      if(textField) {
        modelController.addTarget(textField, "readOnly", "writable", false,
          {
            converter : this.__readOnlyFieldConverter
          }
        );
        textField.addListener("changeValue", function(e) {
          /**@type String*/
          var content = e.getData(); 
          if(content && content.length > 0) {
            this.__actionHandler.execute(mainAction, content);
          } else {
            state.setValue(null);
          }
        }, this);
        modelController.addTarget(textField, "value", "value", false,
          {
            converter : this.__modelToViewFieldConverter
          }
        );
      } else {
        state.addListener("changeValue", function(e) {
          if(e.getData()) {
            var border = new qx.ui.decoration.Single(1, "solid", "red");
            actionField.setDecorator(border);
          } else {
            actionField.setDecorator(null);
          }
        }, this);
      }
      return actionField;
    },
    
    /**
     * @param {org.jspresso.framework.gui.remote.RContainer} remoteContainer
     * @return {qx.ui.core.Widget}
     */
    __createContainer : function(remoteContainer) {
      var container;
      if(remoteContainer instanceof org.jspresso.framework.gui.remote.RBorderContainer) {
        container = this.__createBorderContainer(remoteContainer);
      } else if(remoteContainer instanceof org.jspresso.framework.gui.remote.RCardContainer) {
        container = this.__createCardContainer(remoteContainer);
      } else if(remoteContainer instanceof org.jspresso.framework.gui.remote.RConstrainedGridContainer) {
        container = this.__createConstrainedGridContainer(remoteContainer);
      } else if(remoteContainer instanceof org.jspresso.framework.gui.remote.REvenGridContainer) {
        container = this.__createEvenGridContainer(remoteContainer);
      } else if(remoteContainer instanceof org.jspresso.framework.gui.remote.RForm) {
        container = this.__createForm(remoteContainer);
      } else if(remoteContainer instanceof org.jspresso.framework.gui.remote.RSplitContainer) {
        container = this.__createSplitContainer(remoteContainer);
      } else if(remoteContainer instanceof org.jspresso.framework.gui.remote.RTabContainer) {
        container = this.__createTabContainer(remoteContainer);
      }
      return container;
    },

    /**
     * @param {org.jspresso.framework.gui.remote.RBorderContainer} remoteBorderContainer
     * @return {qx.ui.core.Widget}
     */
    __createBorderContainer : function(remoteBorderContainer) {
      var borderContainer = new qx.ui.container.Composite();
      var borderLayout = new qx.ui.layout.Dock();
      borderLayout.setSort("y");
      borderContainer.setLayout(borderLayout);
      if(remoteBorderContainer.getNorth()) {
        var child = this.createComponent(remoteBorderContainer.getNorth());
        borderContainer.add(child, {edge:"north"});
      }
      if(remoteBorderContainer.getWest()) {
        var child = this.createComponent(remoteBorderContainer.getWest());
        borderContainer.add(child, {edge:"west"});
      }
      if(remoteBorderContainer.getSouth()) {
        var child = this.createComponent(remoteBorderContainer.getSouth());
        borderContainer.add(child, {edge:"south"});
      }
      if(remoteBorderContainer.getEast()) {
        var child = this.createComponent(remoteBorderContainer.getEast());
        borderContainer.add(child, {edge:"east"});
      }
      if(remoteBorderContainer.getCenter()) {
        var child = this.createComponent(remoteBorderContainer.getCenter());
        borderContainer.add(child, {edge:"center"});
      }
      return borderContainer;
    },

    /**
     * @param {org.jspresso.framework.gui.remote.RCardContainer} remoteCardContainer
     * @return {qx.ui.core.Widget}
     */
    __createCardContainer : function(remoteCardContainer) {
      var cardContainer = new org.jspresso.framework.view.qx.RStack();
      cardContainer.setGuid(remoteCardContainer.getGuid());
      // view stack may have to be retrieved for late update of cards.
      this.__remotePeerRegistry.register(cardContainer);

      for(var i = 0; i < remoteCardContainer.getCardNames().length; i++) {
        var rCardComponent = remoteCardContainer.getCards()[i]; 
        var cardName = remoteCardContainer.getCardNames()[i];
        
        this.addCard(cardContainer, rCardComponent, cardName);
      }

      /**@type org.jspresso.framework.state.remote.RemoteValueState*/
      var state = remoteCardContainer.getState();
      state.addListener("changeValue", function(e) {
        var selectedCardName = e.getData();
        var children = cardContainer.getChildren();
        var selectedCard;
        for(var i = 0; i < children.length; i++) {
          var child = children[i];
          if(child.getUserData("cardName") == selectedCardName) {
            selectedCard = child;
          }
        }
        if(selectedCard) {
          cardContainer.setSelection([selectedCard]);
        }
      }, this);
      return cardContainer;
    },

    /**
     * @param {org.jspresso.framework.gui.remote.RConstrainedGridContainer} remoteConstrainedGridContainer
     * @return {qx.ui.core.Widget}
     */
    __createConstrainedGridContainer : function(remoteConstrainedGridContainer) {
      var constrainedGridContainer = new qx.ui.container.Composite();
      var gridLayout = new qx.ui.layout.Grid();
      constrainedGridContainer.setLayout(gridLayout);
      
      for(var i = 0; i < remoteConstrainedGridContainer.getCellConstraints().length; i++) {
        /**@type org.jspresso.framework.util.gui.CellConstraints*/
        var cellConstraint = remoteConstrainedGridContainer.getCellConstraints()[i];
        var cellComponent = this.createComponent(remoteConstrainedGridContainer.getCells()[i]);
        constrainedGridContainer.add(cellComponent, {
          row : cellConstraint.getRow(),
          rowSpan : cellConstraint.getHeight(),
          column : cellConstraint.getColumn(),
          colSpan : cellConstraint.getWidth()
        });
        for(var j = cellConstraint.getColumn(); j < cellConstraint.getColumn() + cellConstraint.getWidth(); j++) {
          if(cellConstraint.isWidthResizable()) {
            gridLayout.setColumnFlex(j, 1);
          } else {
            gridLayout.setColumnFlex(j, 0);
          }
        }
        for(var j = cellConstraint.getRow(); j < cellConstraint.getRow() + cellConstraint.getHeight(); j++) {
          if(cellConstraint.isHeightResizable()) {
            gridLayout.setRowFlex(j, 1);
          } else {
            gridLayout.setRowFlex(j, 0);
          }
        }
      }
      return constrainedGridContainer;
    },

    /**
     * @param {org.jspresso.framework.gui.remote.REvenGridContainer} remoteEvenGridContainer
     * @return {qx.ui.core.Widget}
     */
    __createEvenGridContainer : function(remoteEvenGridContainer) {
      var evenGridContainer = new qx.ui.container.Composite();
      var gridLayout = new qx.ui.layout.Grid();
      
      var nbRows;
      var nbCols;

      var r = 0;
      var c = 0;
      
      if(remoteEvenGridContainer.getDrivingDimension() == "ROW") {
        nbRows = remoteEvenGridContainer.getCells().length / remoteEvenGridContainer.getDrivingDimensionCellCount();
        if(remoteEvenGridContainer.getCells().length % remoteEvenGridContainer.getDrivingDimensionCellCount() > 0) {
          nbRows += 1
        }
        nbCols = remoteEvenGridContainer.getDrivingDimensionCellCount();
      } else {
        nbRows = remoteEvenGridContainer.getDrivingDimensionCellCount();
        nbCols = remoteEvenGridContainer.getCells().length / remoteEvenGridContainer.getDrivingDimensionCellCount();
        if(remoteEvenGridContainer.getCells().length % remoteEvenGridContainer.getDrivingDimensionCellCount() > 0) {
          nbCols += 1
        }
      }
      for(var i = 0; i < remoteEvenGridContainer.getCells().length; i++) {
        var cellComponent = this.createComponent(remoteEvenGridContainer.getCells()[i]);
        evenGridContainer.add(cellComponent, {
          row : r,
          column : c
        });
        
        if(remoteEvenGridContainer.drivingDimension == "ROW") {
          c ++;
          if(c == nbCols) {
            c = 0;
            r ++;
          }
        } else if(remoteEvenGridContainer.drivingDimension == "COLUMN") {
          r ++;
          if(r == nbRows) {
            r = 0;
            c ++;
          }
        }
      }
      for(var i = 0; i < nbRows; i++) {
        gridLayout.setRowFlex(i, 1);
      }
      for(var i = 0; i < nbCols; i++) {
        gridLayout.setColumnFlex(i, 1);
      }
      return evenGridContainer;
    },

    /**
     * @param {org.jspresso.framework.gui.remote.RFrom} remoteForm
     * @return {qx.ui.core.Widget}
     */
    __createForm : function(remoteForm) {
      var form = new qx.ui.container.Composite();
      var formLayout = new qx.ui.layout.Grid(5,5);
      form.setLayout(formLayout);
      var columnCount = remoteForm.getColumnCount();
      var col = 0;
      var row = 0;
      var lastRow = 0;
      
      for(var i = 0; i < remoteForm.getElements().length; i++) {
        var elementWidth = remoteForm.getElementWidths()[i];
        var rComponent = remoteForm.getElements()[i];
        var component = this.createComponent(rComponent);
        var componentLabel;
        if(remoteForm.getLabelsPosition() != "NONE") {
          componentLabel = this.createComponent(remoteForm.getElementLabels()[i], false);
        }
        
        if(elementWidth > columnCount) {
          elementWidth = columnCount;
        }
        if(col + elementWidth > columnCount) {
          col = 0;
          row += 1;
        }

        var labelRow;
        var labelCol;
        var labelColSpan;
        var compRow;
        var compCol;
        var compColSpan;
        if(remoteForm.getLabelsPosition() == "ABOVE") {
          labelRow = row * 2;
          labelCol = col;
          labelColSpan = elementWidth;
          compRow = labelRow + 1;
          compCol = labelCol;
          compColSpan = elementWidth;
        } else if(remoteForm.getLabelsPosition() == "ASIDE") {
          labelRow = row;
          labelCol = col * 2;
          labelColSpan = 1;
          compRow = labelRow;
          compCol = labelCol + 1;
          compColSpan = elementWidth * 2 - 1;
        } else if(remoteForm.getLabelsPosition() == "NONE") {
          compRow = row;
          compCol = col;
          compColSpan = elementWidth;
        }
        if(remoteForm.getLabelsPosition() != "NONE") {
          form.add(componentLabel, {row : labelRow,
                                    column : labelCol,
                                    rowSpan : 1,
                                    colSpan : labelColSpan});
        }

        form.add(component, {row : compRow,
                             column : compCol,
                             rowSpan : 1,
                             colSpan : compColSpan});

        col += elementWidth;
        if(   rComponent instanceof org.jspresso.framework.gui.remote.RTable
           || rComponent instanceof org.jspresso.framework.gui.remote.RTextArea
           || rComponent instanceof org.jspresso.framework.gui.remote.RList) {
          formLayout.setRowFlex(compRow, 1);
        }
        formLayout.setColumnFlex(compCol, 1);
        lastRow = compRow;
      }
      for(var i = 0; i <= lastRow; i++) {
        formLayout.setRowAlign(i, "left", "middle");
      }
      return form;
    },

    /**
     * @param {org.jspresso.framework.gui.remote.RSplitContainer} remoteSplitContainer
     * @return {qx.ui.core.Widget}
     */
    __createSplitContainer : function(remoteSplitContainer) {
      var splitContainer = new qx.ui.splitpane.Pane();

      if(remoteSplitContainer.getOrientation() == "VERTICAL") {
        splitContainer.setOrientation("vertical");
      } else {
        splitContainer.setOrientation("horizontal");
      }

      var component;
      if(remoteSplitContainer.getLeftTop() != null) {
        component = this.createComponent(remoteSplitContainer.getLeftTop());
        splitContainer.add(component, 0);
      }
      if(remoteSplitContainer.getRightBottom() != null) {
        component = this.createComponent(remoteSplitContainer.getRightBottom());
        splitContainer.add(component, 1);
      }
      return splitContainer;
    },

    /**
     * @param {org.jspresso.framework.gui.remote.RTabContainer} remoteTabContainer
     * @return {qx.ui.core.Widget}
     */
    __createTabContainer : function(remoteTabContainer) {
      var tabContainer = new qx.ui.tabview.TabView();
      for(var i = 0; i < remoteTabContainer.getTabs().length; i++) {
        /**@type org.jspresso.framework.gui.remote.RComponent*/
        var remoteTab = remoteTabContainer.getTabs()[i];
        var tabComponent = this.createComponent(remoteTab);
        
        var tab = new qx.ui.tabview.Page(remoteTab.getLabel());
        this.setIcon(tab.getChildControl("button"), remoteTab.getIcon());
        tab.setLayout(new qx.ui.layout.Grow());
        tab.add(tabComponent);
        
        tabContainer.add(tab);
      }
      return tabContainer;
    },

    /**
     * @param {org.jspresso.framework.gui.remote.RTextComponent} remoteTextComponent
     * @return {qx.ui.core.Widget}
     */
    __createTextComponent : function(remoteTextComponent) {
      var textComponent;
      if(remoteTextComponent instanceof org.jspresso.framework.gui.remote.RTextArea) {
        textComponent = this.__createTextArea(remoteTextComponent);
      } else if(remoteTextComponent instanceof org.jspresso.framework.gui.remote.RHtmlArea) {
        textComponent = this.__createHtmlArea(remoteTextComponent);
      } else if(remoteTextComponent instanceof org.jspresso.framework.gui.remote.RPasswordField) {
        textComponent = this.__createPasswordField(remoteTextComponent);
      } else if(remoteTextComponent instanceof org.jspresso.framework.gui.remote.RTextField) {
        textComponent = this.__createTextField(remoteTextComponent);
      } else if(remoteTextComponent instanceof org.jspresso.framework.gui.remote.RLabel) {
        textComponent = this.__createLabel(remoteTextComponent);
      }
      return textComponent;
    },

    /**
     * @param {org.jspresso.framework.gui.remote.RTextArea} remoteTextArea
     * @return {qx.ui.core.Widget}
     */
    __createTextArea : function(remoteTextArea) {
      var textArea = new qx.ui.form.TextArea();
      var state = remoteTextArea.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(textArea, "value", "value", true,
        {
          converter : this.__modelToViewFieldConverter
        },
        {
          converter : this.__viewToModelFieldConverter
        }
      );
      modelController.addTarget(textArea, "readOnly", "writable", false,
        {
          converter : this.__readOnlyFieldConverter
        }
      );
      return textArea;
    },

    /**
     * @param {org.jspresso.framework.gui.remote.RHtmlArea} remoteHtmlArea
     * @return {qx.ui.core.Widget}
     */
    __createHtmlArea : function(remoteHtmlArea) {
      var htmlArea = new qx.ui.basic.Label();
      htmlArea.setRich(true);
      var state = remoteHtmlArea.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(htmlArea, "value", "value", false,
        {
          converter : this.__modelToViewFieldConverter
        }
      );
      return htmlArea;
    },
    /**
     * @param {org.jspresso.framework.gui.remote.RTextField} remoteTextField
     * @return {qx.ui.core.Widget}
     */
    __createTextField : function(remoteTextField) {
      var textField = new qx.ui.form.TextField();
      
      if(remoteTextField.getMaxLength() > 0) {
        textField.setMaxLength(remoteTextField.getMaxLength());
        this.__sizeMaxComponentWidth(textField, remoteTextField.getMaxLength());
      } else {
        this.__sizeMaxComponentWidth(textField);
      }
      var state = remoteTextField.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(textField, "value", "value", true,
        {
          converter : this.__modelToViewFieldConverter
        },
        {
          converter : this.__viewToModelFieldConverter
        }
      );
      modelController.addTarget(textField, "readOnly", "writable", false,
        {
          converter : this.__readOnlyFieldConverter
        }
      );
      return textField;
    },

    /**
     * @param {org.jspresso.framework.gui.remote.RLabel} remoteLabel
     * @return {qx.ui.core.Widget}
     */
    __createLabel : function(remoteLabel) {
      var label = new qx.ui.basic.Label();
      var state = remoteLabel.getState();
      if(state) {
        var modelController = new qx.data.controller.Object(state);
        modelController.addTarget(label, "value", "value", false,
          {
            converter : function(modelValue, model) {
              if(org.jspresso.framework.util.html.HtmlUtil.isHtml(modelValue)) {
                label.setRich(true);
              } else {
                label.setRich(false);
              }
              return modelValue;
            }
          }
        );
      } else {
        label.setValue(remoteLabel.getLabel());
        label.setRich(org.jspresso.framework.util.html.HtmlUtil.isHtml(remoteLabel.getLabel()));
      }
      return label;
    },

    /**
     * @param {org.jspresso.framework.gui.remote.RPasswordField} remotePasswordField
     * @return {qx.ui.core.Widget}
     */
    __createPasswordField : function(remotePasswordField) {
      var passwordField = new qx.ui.form.PasswordField();
      
      if(remotePasswordField.getMaxLength() > 0) {
        passwordField.setMaxLength(remotePasswordField.getMaxLength());
      }
      var state = remotePasswordField.getState();
      var modelController = new qx.data.controller.Object(state);
      modelController.addTarget(passwordField, "value", "value", true,
        {
          converter : this.__modelToViewFieldConverter
        },
        {
          converter : this.__viewToModelFieldConverter
        }
      );
      modelController.addTarget(passwordField, "readOnly", "writable", false,
        {
          converter : this.__readOnlyFieldConverter
        }
      );
      this.__sizeMaxComponentWidth(passwordField);
      return passwordField;
    },
    
    /**
     * 
     * @param {org.jspresso.framework.gui.remote.RTree} remoteTree
     * @return {qx.ui.core.Widget}
     */
    __createTree : function(remoteTree) {
      //TODO notify qooxdoo that tree controller doesn't support null children.
      var tree = new qx.ui.tree.Tree();
      var state = remoteTree.getState();
      var treeController = new qx.data.controller.Tree();
      treeController.setChildPath("children");
      treeController.setLabelPath("value");
      treeController.setIconPath("iconImageUrl");
      treeController.setDelegate(
        {
          bindItem : function(controller, treeNode, modelNode) {
            controller.bindProperty(controller.getLabelPath(), "label", controller.getLabelOptions(), treeNode, modelNode);
            controller.bindProperty(controller.getIconPath(), "icon", controller.getIconOptions(), treeNode, modelNode);
            if(modelNode) {
              modelNode.addListener("changeSelectedIndices", function(e) {
                var viewSelection = controller.getSelection();
                var stateSelection = e.getTarget().getSelectedIndices();
                var stateChildren = e.getTarget().getChildren();
                for(var i = 0; i < stateChildren.length; i++) {
                  var child = stateChildren.getItem(i);
                  if(stateSelection && qx.lang.Array.contains(stateSelection, i)) {
                    if(!viewSelection.contains(child)) {
                      if(!treeNode.isOpen()) {
                        treeNode.setOpen(true);
                      }
                      viewSelection.push(child);
                    }
                  } else {
                    if(viewSelection.contains(child)) {
                      viewSelection.remove(child);
                    }
                  }
                }
              }, this);
            }
          }
        }
      );
      treeController.setModel(state);
      treeController.setTarget(tree);
      if(remoteTree.isExpanded()) {
        this.__expandAllChildren(tree, tree.getRoot());
      } else {
        tree.getRoot().setOpen(true);
      }
      
      treeController.addListener("changeSelection", function(e) {
        /**@type qx.data.Array*/
        var selectedItems = e.getData();
        /**@type org.jspresso.framework.state.remote.RemoteCompositeValueState*/
        var rootState = e.getTarget().getModel();
        var deselectedStates = new Array();
        var selectedStates = new Array();
        this.__synchTreeViewSelection(rootState, selectedItems, deselectedStates, selectedStates);
        for(var i = 0; i < deselectedStates.length; i++) {
          var deselectedState = deselectedStates[i];
          deselectedState.setLeadingIndex(-1);
          deselectedState.setSelectedIndices(null);
        }
        for(var i = 0; i < selectedStates.length; i += 3) {
          var selectedState = selectedStates[i];
          selectedState.setLeadingIndex(selectedStates[i+1]);
          selectedState.setSelectedIndices(selectedStates[i+2]);
        }
      }, this);
      return tree;
    },
    
    /**
     * 
     * @param {qx.ui.tree.Tree} tree
     * @param {qx.ui.tree.AbstractTreeItem} selectedItems
     */
    __expandAllChildren : function(tree, treeItem) {
      treeItem.setOpen(true);
      if(treeItem.getChildren() != null) {
        for (var i = 0; i < treeItem.getChildren().length; i++) {
          this.__expandAllChildren(tree, treeItem.getChildren()[i]);
        }
      }
    },
    
    /**
     * 
     * @param {org.jspresso.framework.state.remote.RemoteCompositeValueState} state
     * @param {qx.data.Array} selectedItems
     */
    __synchTreeViewSelection : function(state, selectedItems, deselectedStates, selectedStates) {
      var selectedIndices = new Array();
      var stateChildren = state.getChildren();
      var stateSelectedIndices = state.getSelectedIndices();
      var stateLeadingIndex = state.getLeadingIndex();
      for(var i = 0; i < stateChildren.length; i++) {
        var child = stateChildren.getItem(i);
        if(selectedItems.contains(child)) {
          selectedIndices.push(i);
        }
        this.__synchTreeViewSelection(child, selectedItems, deselectedStates, selectedStates);
      }
      if(   selectedIndices.length == 0) {
        if(   stateSelectedIndices != null
           && stateSelectedIndices.length != 0) {
          deselectedStates.push(state);
        }
      } else {
        var leadingIndex = selectedIndices[selectedIndices.length -1];
        if(   stateSelectedIndices == null
           || !qx.lang.Array.equals(selectedIndices, stateSelectedIndices)
           || stateLeadingIndex != leadingIndex) {
          selectedStates.push(state);
          selectedStates.push(leadingIndex);
          selectedStates.push(selectedIndices);
        }
      }
    },
    
    /**
     * 
     * @param {org.jspresso.framework.gui.remote.RAction} remoteAction
     * @return {qx.event.Command}
     */
    createCommand : function(remoteAction) {
      var command = new qx.event.Command(remoteAction.getAcceleratorAsString());
      command.addListener("execute", function(e) {
        this.__actionHandler.execute(remoteAction);
      }, this);
      return command;
    },

    /**
     * @param {qx.application.AbstractGui} application
     * @return {qx.ui.form.Button}
     */
    createOkButton : function(application) {
      return new qx.ui.form.Button(application.tr("Ok"), "qx/icon/Oxygen/22/actions/dialog-ok.png");
    },

    /**
     * @param {qx.application.AbstractGui} application
     * @return {qx.ui.form.Button}
     */
    createCancelButton : function(application) {
      return new qx.ui.form.Button(application.tr("Cancel"), "qx/icon/Oxygen/22/actions/dialog-cancel.png");
    },
    
    /**
     * @param {qx.application.AbstractGui} application
     * @return {qx.ui.form.Button}
     */
    createYesButton : function(application) {
      return new qx.ui.form.Button(application.tr("Yes"), "qx/icon/Oxygen/22/actions/dialog-ok.png");
    },
    
    /**
     * @param {qx.application.AbstractGui} application
     * @return {qx.ui.form.Button}
     */
    createNoButton : function(application) {
      return new qx.ui.form.Button(application.tr("No"), "qx/icon/Oxygen/22/actions/dialog-close.png");
    },

    /**
     * 
     * @param {String} label
     * @param {String} tooltip
     * @param {org.jspresso.framework.gui.remote.RIcon} icon
     * @return {qx.ui.form.Button}
     */
    createButton : function(label, tooltip, icon) {
      var button = new qx.ui.form.Button();
      this.__completeButton(button, label, tooltip, icon);
      return button;
    },
    
    /**
     * 
     * @param {String} label
     * @param {String} tooltip
     * @param {org.jspresso.framework.gui.remote.RIcon} icon
     * @return {qx.ui.menubar.Button}
     */
    createMenubarButton : function(label, tooltip, icon) {
      var button = new qx.ui.menubar.Button();
      this.__completeButton(button, label, tooltip, icon);
      return button;
    },
    
    /**
     * 
     * @param {String} label
     * @param {String} tooltip
     * @param {org.jspresso.framework.gui.remote.RIcon} icon
     * @return {qx.ui.menu.Button}
     */
    createMenuButton : function(label, tooltip, icon) {
      var button = new qx.ui.menu.Button();
      this.__completeButton(button, label, tooltip, icon);
      return button;
    },

    /**
     * 
     * @param {String} label
     * @param {String} tooltip
     * @param {org.jspresso.framework.gui.remote.RIcon} icon
     * @return void
     */
    __completeButton : function(button, label, tooltip, icon) {
      this.setIcon(button, icon);
      if(label) {
        button.setLabel(label);
      }
      if(tooltip) {
        button.setToolTip(new qx.ui.tooltip.ToolTip(tooltip + this.self(arguments).__TOOLTIP_ELLIPSIS));
      }
    },
    
    /**
     * 
     * @param {qx.ui.core.Widget} component
     * @param {org.jspresso.framework.gui.remote.RIcon} icon
     */
    setIcon : function(component, icon) {
      if(icon) {
        component.setIcon(icon.getImageUrlSpec());
      }
    },

    /**
     * 
     * @param {org.jspresso.framework.gui.remote.RAction} remoteAction
     * @param {Boolean} useLabel
     * @return {qx.ui.form.Button}
     */
    createAction : function (remoteAction, useLabel) {
      if(useLabel == null) {
        useLabel = false;
      }
      var label = null;
      if(useLabel) {
        label = remoteAction.getName();
      }
      var button = this.createButton(label, remoteAction.getDescription(), remoteAction.getIcon());
      remoteAction.bind("enabled", button, "enabled");
      this.__remotePeerRegistry.register(remoteAction);
      button.addListener("execute", function(event) {
        this.__actionHandler.execute(remoteAction);
      }, this);
      return button;
    },
    
    /**
     * @param {qx.ui.container.Stack} cardContainer
     * @param {org.jspresso.framework.gui.remote.RComponent} rCardComponent
     * @param {String} cardName
     * 
     * @return void
     */
    addCard : function(cardContainer, rCardComponent, cardName) {
      var cardComponent = this.createComponent(rCardComponent);
      cardComponent.setUserData("cardName", cardName);
      cardContainer.add(cardComponent);
    },
    
    __modelToViewFieldConverter : function(modelValue, model) {
      if(modelValue == null) {
        return "";
      }
      return modelValue;
    },
    
    __viewToModelFieldConverter : function(viewValue, model) {
      if(viewValue == "") {
        return null;
      }
      return viewValue;
    },
    
    __readOnlyFieldConverter : function(writable, model) {
      return !writable;
    },

    /**
     * 
     * @param {org.jspresso.framework.gui.remote.RComponent} remoteComponent
     * @return {qx.util.format.IFormat}
     */
    __createFormat : function(remoteComponent) {
      var format;
      if(remoteComponent instanceof org.jspresso.framework.gui.remote.RDateField) {
        if(remoteComponent.getType() == "DATE_TIME") {
          format = new qx.util.format.DateFormat(
            qx.locale.Date.getDateFormat("short")
            + " "
            + qx.locale.Date.getDateTimeFormat("HHmmss", "HH:mm:ss")
          );
        } else {
          format = new qx.util.format.DateFormat(qx.locale.Date.getDateFormat("short") + "");
        }
      } else if(remoteComponent instanceof org.jspresso.framework.gui.remote.RTimeField) {
        return new qx.util.format.DateFormat(qx.locale.Date.getDateTimeFormat("HHmmss", "HH:mm:ss"));
      } else if(remoteComponent instanceof org.jspresso.framework.gui.remote.RNumericComponent) {
        if(remoteComponent instanceof org.jspresso.framework.gui.remote.RDecimalComponent) {
          if(remoteComponent instanceof org.jspresso.framework.gui.remote.RPercentField) {
            format = new org.jspresso.framework.util.format.ScaledNumberFormat();
            format.setScale(100);
            format.setPostfix(" %");
          } else {
            format = new qx.util.format.NumberFormat();
          }
          if(remoteComponent.getMaxFractionDigit()) {
            format.setMaximumFractionDigits(remoteComponent.getMaxFractionDigit());
            format.setMinimumFractionDigits(remoteComponent.getMaxFractionDigit());
          }
        } else if(remoteComponent instanceof org.jspresso.framework.gui.remote.RIntegerField) {
          format = qx.util.format.NumberFormat.getIntegerInstance();
        }
      }
      return format;
    },
    
    __sizeMaxComponentWidth : function(component, expectedCharCount, maxCharCount) {
      if(expectedCharCount == null) {
        expectedCharCount = org.jspresso.framework.view.qx.DefaultQxViewFactory.__FIELD_MAX_CHAR_COUNT;
      }
      if(maxCharCount == null) {
        maxCharCount = org.jspresso.framework.view.qx.DefaultQxViewFactory.__FIELD_MAX_CHAR_COUNT;
      } else {
        maxCharCount += 2;
      }
      var charCount = maxCharCount;
      if(expectedCharCount < charCount) {
        charCount = expectedCharCount;
      }
      var compFont = component.getFont();
      if(!compFont) {
        compFont = qx.theme.manager.Font.getInstance().resolve("default");
      }
      var charWidth = qx.bom.Label.getTextSize(org.jspresso.framework.view.qx.DefaultQxViewFactory.__TEMPLATE_CHAR,
                                               compFont.getStyles()).width;
      var w = charWidth * charCount;
      component.setWidth(w);
      component.setMaxWidth(w);
    }

  }
});
