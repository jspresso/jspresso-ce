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

package org.jspresso.framework.view.flex {
  import actionscriptdatetimelibrary.DateTimeField;
  import actionscriptdatetimelibrary.TimeStepper;
  
  import flash.display.DisplayObject;
  import flash.events.Event;
  import flash.events.FocusEvent;
  import flash.events.MouseEvent;
  
  import mx.binding.utils.BindingUtils;
  import mx.collections.ArrayCollection;
  import mx.containers.ApplicationControlBar;
  import mx.containers.BoxDirection;
  import mx.containers.Canvas;
  import mx.containers.DividedBox;
  import mx.containers.Grid;
  import mx.containers.GridItem;
  import mx.containers.GridRow;
  import mx.containers.HBox;
  import mx.containers.Panel;
  import mx.containers.TabNavigator;
  import mx.containers.VBox;
  import mx.containers.ViewStack;
  import mx.controls.Button;
  import mx.controls.CheckBox;
  import mx.controls.ColorPicker;
  import mx.controls.DataGrid;
  import mx.controls.DateField;
  import mx.controls.Image;
  import mx.controls.Label;
  import mx.controls.List;
  import mx.controls.TextArea;
  import mx.controls.TextInput;
  import mx.controls.Tree;
  import mx.controls.VRule;
  import mx.controls.dataGridClasses.DataGridColumn;
  import mx.core.ClassFactory;
  import mx.core.Container;
  import mx.core.ScrollPolicy;
  import mx.core.UIComponent;
  import mx.events.CollectionEvent;
  import mx.events.CollectionEventKind;
  import mx.events.ColorPickerEvent;
  import mx.events.DataGridEvent;
  import mx.events.DataGridEventReason;
  import mx.events.FlexEvent;
  import mx.events.PropertyChangeEvent;
  import mx.formatters.DateFormatter;
  import mx.formatters.Formatter;
  import mx.formatters.NumberBase;
  import mx.formatters.NumberBaseRoundType;
  import mx.formatters.NumberFormatter;
  
  import org.jspresso.framework.action.IActionHandler;
  import org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler;
  import org.jspresso.framework.application.frontend.command.remote.RemoteSortCommand;
  import org.jspresso.framework.gui.remote.RAction;
  import org.jspresso.framework.gui.remote.RActionField;
  import org.jspresso.framework.gui.remote.RActionList;
  import org.jspresso.framework.gui.remote.RBorderContainer;
  import org.jspresso.framework.gui.remote.RCardContainer;
  import org.jspresso.framework.gui.remote.RCheckBox;
  import org.jspresso.framework.gui.remote.RColorField;
  import org.jspresso.framework.gui.remote.RComboBox;
  import org.jspresso.framework.gui.remote.RComponent;
  import org.jspresso.framework.gui.remote.RConstrainedGridContainer;
  import org.jspresso.framework.gui.remote.RContainer;
  import org.jspresso.framework.gui.remote.RDateField;
  import org.jspresso.framework.gui.remote.RDecimalComponent;
  import org.jspresso.framework.gui.remote.RDecimalField;
  import org.jspresso.framework.gui.remote.RDurationField;
  import org.jspresso.framework.gui.remote.REvenGridContainer;
  import org.jspresso.framework.gui.remote.RForm;
  import org.jspresso.framework.gui.remote.RIcon;
  import org.jspresso.framework.gui.remote.RImageComponent;
  import org.jspresso.framework.gui.remote.RIntegerField;
  import org.jspresso.framework.gui.remote.RLabel;
  import org.jspresso.framework.gui.remote.RList;
  import org.jspresso.framework.gui.remote.RNumericComponent;
  import org.jspresso.framework.gui.remote.RPasswordField;
  import org.jspresso.framework.gui.remote.RPercentField;
  import org.jspresso.framework.gui.remote.RSecurityComponent;
  import org.jspresso.framework.gui.remote.RSplitContainer;
  import org.jspresso.framework.gui.remote.RTabContainer;
  import org.jspresso.framework.gui.remote.RTable;
  import org.jspresso.framework.gui.remote.RTextArea;
  import org.jspresso.framework.gui.remote.RTextComponent;
  import org.jspresso.framework.gui.remote.RTextField;
  import org.jspresso.framework.gui.remote.RTimeField;
  import org.jspresso.framework.gui.remote.RTree;
  import org.jspresso.framework.state.remote.RemoteCompositeValueState;
  import org.jspresso.framework.state.remote.RemoteValueState;
  import org.jspresso.framework.util.array.ArrayUtil;
  import org.jspresso.framework.util.format.NumberParser;
  import org.jspresso.framework.util.format.Parser;
  import org.jspresso.framework.util.format.PercentFormatter;
  import org.jspresso.framework.util.format.PercentParser;
  import org.jspresso.framework.util.gui.CellConstraints;
  import org.jspresso.framework.util.remote.registry.IRemotePeerRegistry;
  
  public class DefaultFlexViewFactory {

    [Embed(source="mx/controls/Image.png")]
    private var _iconTemplate:Class;

    [Embed(source="/assets/images/reset-16x16.png")]
    private var _resetIcon:Class;

    private static const TOOLTIP_ELLIPSIS:String = "...";
    private static const TEMPLATE_CHAR:String = "O";
    private static const FIELD_MAX_CHAR_COUNT:int = 32;
    private static const COLUMN_MAX_CHAR_COUNT:int = 12;
    private static const DATE_CHAR_COUNT:int = 12;
    private static const TIME_CHAR_COUNT:int = 6;

    private var _remotePeerRegistry:IRemotePeerRegistry;
    private var _actionHandler:IActionHandler;
    private var _commandHandler:IRemoteCommandHandler;
    private var _remoteValueSorter:RemoteValueSorter;
    private var _timeFormatter:DateFormatter;

    public function DefaultFlexViewFactory(remotePeerRegistry:IRemotePeerRegistry,
                                           actionHandler:IActionHandler,
                                           commandHandler:IRemoteCommandHandler) {
      _remotePeerRegistry = remotePeerRegistry;
      _actionHandler = actionHandler;
      _commandHandler = commandHandler;
      _remoteValueSorter = new RemoteValueSorter();
      _timeFormatter = new DateFormatter();
      _timeFormatter.formatString = "JJ:NN:SS"
    }
    
    public function createComponent(remoteComponent:RComponent, registerState:Boolean=true):UIComponent {
      var component:UIComponent;
      if(remoteComponent is RActionField) {
        component = createActionField(remoteComponent as RActionField);
      } else if(remoteComponent is RCheckBox) {
        component = createCheckBox(remoteComponent as RCheckBox);
      } else if(remoteComponent is RComboBox) {
        component = createComboBox(remoteComponent as RComboBox);
      } else if(remoteComponent is RColorField) {
        component = createColorField(remoteComponent as RColorField);
      } else if(remoteComponent is RContainer) {
        component = createContainer(remoteComponent as RContainer);
      } else if(remoteComponent is RDateField) {
        component = createDateComponent(remoteComponent as RDateField);
      } else if(remoteComponent is RDurationField) {
        component = createDurationField(remoteComponent as RDurationField);
      } else if(remoteComponent is RImageComponent) {
        component = createImageComponent(remoteComponent as RImageComponent);
      } else if(remoteComponent is RList) {
        component = createList(remoteComponent as RList);
      } else if(remoteComponent is RNumericComponent) {
        component = createNumericComponent(remoteComponent as RNumericComponent);
      } else if(remoteComponent is RPasswordField) {
        component = createPasswordField(remoteComponent as RPasswordField);
      } else if(remoteComponent is RSecurityComponent) {
        component = createSecurityComponent(remoteComponent as RSecurityComponent);
      } else if(remoteComponent is RTable) {
        component = createTable(remoteComponent as RTable);
      } else if(remoteComponent is RTextComponent) {
        component = createTextComponent(remoteComponent as RTextComponent);
      } else if(remoteComponent is RTimeField) {
        component = createTimeField(remoteComponent as RTimeField);
      } else if(remoteComponent is RTree) {
        component = createTree(remoteComponent as RTree);
      }
      if(!(component is Tree)) {
        component.minWidth = 0;
      }
      component.id = remoteComponent.guid;
      if(remoteComponent.tooltip != null) {
        component.toolTip = remoteComponent.tooltip;
      }
      if(!(remoteComponent is RActionField) && remoteComponent.actionLists != null) {
        var toolBar:ApplicationControlBar = new ApplicationControlBar();
        toolBar.percentWidth = 100.0;
        toolBar.setStyle("fillAlphas",[0.5,0.5]);
        toolBar.setStyle("fillColors",[0xBBBBBB,0x666666]);
        toolBar.setStyle("horizontalGap",2);
        for(var i:int = 0; i < remoteComponent.actionLists.length; i++) {
          var actionList:RActionList = remoteComponent.actionLists[i] as RActionList;
          if(actionList.actions != null) {
            for(var j:int = 0; j < actionList.actions.length; j++) {
              toolBar.addChild(createAction(actionList.actions[j]));
            }
            if(i < remoteComponent.actionLists.length - 1) {
              var separator:VRule = new VRule();
              separator.height = 20;
              separator.maxHeight = 20;
              toolBar.addChild(separator);
            }
          }
        }
        var surroundingBox:VBox = new VBox();
        component.percentWidth = 100.0;
        component.percentHeight = 100.0;
        surroundingBox.addChild(toolBar);
        surroundingBox.addChild(component);
        surroundingBox.horizontalScrollPolicy = ScrollPolicy.OFF;
        surroundingBox.verticalScrollPolicy = ScrollPolicy.OFF;
        component = surroundingBox;
      }
      if(remoteComponent.borderType == "TITLED") {
        var decorator:Panel = new Panel();
        decorator.percentWidth = component.percentWidth;
        decorator.percentHeight = component.percentHeight;
        component.percentWidth = 100.0;
        component.percentHeight = 100.0;
        decorator.addChild(component);
        decorator.title = remoteComponent.label;
        decorator.titleIcon = getIconForComponent(decorator, remoteComponent.icon);
        decorator.setStyle("borderAlpha",1);
        decorator.setStyle("borderThicknessLeft", 3);
        decorator.setStyle("borderThicknessRight", 3);
        decorator.horizontalScrollPolicy = ScrollPolicy.OFF;
        decorator.verticalScrollPolicy = ScrollPolicy.OFF;
        component = decorator;
      } else if(remoteComponent.borderType == "SIMPLE") {
        component.setStyle("borderStyle","solid");
        component.setStyle("borderThickness", 3);
      }
      if(remoteComponent.foreground) {
        component.setStyle("color", remoteComponent.foreground);
      }
      if(remoteComponent.background) {
        component.setStyle("backgroundColor", remoteComponent.background);
      }
      if(remoteComponent.font) {
        if(remoteComponent.font.name) {
          component.setStyle("fontFamily", remoteComponent.font.name);
        }
        if(remoteComponent.font.size > 0) {
          component.setStyle("fontSize", remoteComponent.font.size);
        }
        if(remoteComponent.font.italic) {
          component.setStyle("fontStyle", "italic");
        }
        if(remoteComponent.font.bold) {
          component.setStyle("fontWeight", "bold");
        }
      }
      if(registerState) {
        _remotePeerRegistry.register(remoteComponent.state);
      }
      return component;
    }
    
    private function createContainer(remoteContainer:RContainer):UIComponent {
      var container:Container;
      if(remoteContainer is RBorderContainer) {
        container = createBorderContainer(remoteContainer as RBorderContainer);
      } else if(remoteContainer is RCardContainer) {
        container = createCardContainer(remoteContainer as RCardContainer);
      } else if(remoteContainer is RConstrainedGridContainer) {
        container = createConstrainedGridContainer(remoteContainer as RConstrainedGridContainer);
      } else if(remoteContainer is REvenGridContainer) {
        container = createEvenGridContainer(remoteContainer as REvenGridContainer);
      } else if(remoteContainer is RForm) {
        container = createForm(remoteContainer as RForm);
      } else if(remoteContainer is RSplitContainer) {
        container = createSplitContainer(remoteContainer as RSplitContainer);
      } else if(remoteContainer is RTabContainer) {
        container = createTabContainer(remoteContainer as RTabContainer);
      }
      container.horizontalScrollPolicy = ScrollPolicy.OFF;
      container.verticalScrollPolicy = ScrollPolicy.OFF;
      return container;
    }

    private function createNumericComponent(remoteNumericComponent:RNumericComponent):UIComponent {
      var numericComponent:UIComponent;
      if(remoteNumericComponent is RDecimalComponent) {
        numericComponent = createDecimalComponent(remoteNumericComponent as RDecimalComponent);
      } else if(remoteNumericComponent is RIntegerField) {
        numericComponent = createIntegerField(remoteNumericComponent as RIntegerField);
      }
      if(remoteNumericComponent.maxValue) {
        sizeMaxComponentWidth(numericComponent,
          createFormatter(remoteNumericComponent).format(remoteNumericComponent.maxValue).length);
      }
      return numericComponent;
    }

    private function createDecimalComponent(remoteDecimalComponent:RDecimalComponent):UIComponent {
      var decimalComponent:UIComponent;
      if(remoteDecimalComponent is RDecimalField) {
        decimalComponent = createDecimalField(remoteDecimalComponent as RDecimalField);
      } else if(remoteDecimalComponent is RPercentField) {
        decimalComponent = createPercentField(remoteDecimalComponent as RPercentField);
      }
      return decimalComponent;
    }

    private function createTextComponent(remoteTextComponent:RTextComponent):UIComponent {
      var textComponent:UIComponent;
      if(remoteTextComponent is RTextArea) {
        textComponent = createTextArea(remoteTextComponent as RTextArea);
      } else if(remoteTextComponent is RTextField) {
        textComponent = createTextField(remoteTextComponent as RTextField);
      } else if(remoteTextComponent is RLabel) {
        textComponent = createLabel(remoteTextComponent as RLabel);
      }
      return textComponent;
    }

    private function createTree(remoteTree:RTree):UIComponent {
      var tree:Tree = new SelectionTrackingTree();
      tree.labelField = "value";
      tree.dataTipField = "description";
      tree.itemRenderer = new ClassFactory(RemoteValueTreeItemRenderer);
      tree.dataProvider = remoteTree.state;
      tree.minWidth = 200;
      tree.horizontalScrollPolicy = ScrollPolicy.AUTO;
      tree.verticalScrollPolicy = ScrollPolicy.AUTO;
      bindTree(tree, remoteTree.state as RemoteCompositeValueState);
      return tree;
    }

    private function bindTree(tree:Tree, rootState:RemoteCompositeValueState):void {
      var updateModel:Function = function (selectedItems:Array):void {
        var parentsOfSelectedNodes:Array = new Array();
        var i:int;
        var node:Object;
        var parentNode:RemoteCompositeValueState;
        for(i=0; i < selectedItems.length; i++) {
          node = selectedItems[i];
          parentNode = tree.getParentItem(node);
          if(parentNode != null && parentsOfSelectedNodes.indexOf(parentNode) == -1) {
            parentsOfSelectedNodes.push(parentNode);
          }
        }
        clearStateSelection(rootState, parentsOfSelectedNodes);
        for(i=0; i < selectedItems.length; i++) {
          node = selectedItems[i];
          parentNode = tree.getParentItem(node);
          if(parentNode != null) {
            var selectedIndices:Array = new Array();
            if(parentNode.selectedIndices != null) {
              selectedIndices.concat(parentNode.selectedIndices);
            }
            var childIndex:int = parentNode.children.getItemIndex(node);
            selectedIndices.push(childIndex);
            parentNode.leadingIndex = childIndex;
            parentNode.selectedIndices = selectedIndices;
          }
        }
      };
      BindingUtils.bindSetter(updateModel, tree, "selectedItems", true);
    }
    
    private function clearStateSelection(remoteState:RemoteCompositeValueState, excludedNodes:Array):void {
      if(excludedNodes.indexOf(remoteState) == -1) {
        remoteState.selectedIndices = null
        remoteState.leadingIndex = -1;
      }
      if(remoteState.children != null) {
        for(var i:int = 0; i < remoteState.children.length; i++) {
          if(remoteState.children[i] is RemoteCompositeValueState) {
            clearStateSelection(remoteState.children[i] as RemoteCompositeValueState, excludedNodes);
          }
        }
      }
    }

    private function createImageComponent(remoteImageComponent:RImageComponent):UIComponent {
      var imageComponent:Image = new Image();
      bindImage(imageComponent, remoteImageComponent.state);
      return imageComponent;
    }
    
    private function bindImage(imageComponent:Image, remoteState:RemoteValueState):void {
      var updateView:Function = function (value:Object):void {
        imageComponent.source = value;
      };
      BindingUtils.bindSetter(updateView, remoteState, "value", true);
    }
    
    private function createActionField(remoteActionField:RActionField):UIComponent {
      var actionField:HBox = new HBox();
      actionField.horizontalScrollPolicy = ScrollPolicy.OFF;
      actionField.verticalScrollPolicy = ScrollPolicy.OFF;
      var textField:TextInput;
      if(remoteActionField.showTextField) {
        textField = new TextInput();
        actionField.percentWidth = 100.0;
        textField.percentWidth = 100.0;
        textField.name = "tf";
        actionField.addChild(textField);
        sizeMaxComponentWidth(textField);
      }
      for(var i:int = 0; i < remoteActionField.actionLists.length; i++) {
        var actionList:RActionList = remoteActionField.actionLists[i] as RActionList;
        for(var j:int = 0; j < actionList.actions.length; j++) {
          var actionComponent:UIComponent = createAction(actionList.actions[j])
          actionField.addChild(actionComponent);
        }
      }
      bindActionField(actionField, textField, remoteActionField.state, (remoteActionField.actionLists[0] as RActionList).actions[0]);
      return actionField;
    }
    
    private function bindActionField(actionField:UIComponent, textInput:TextInput
                                     , remoteState:RemoteValueState, action:RAction):void {
      
      BindingUtils.bindProperty(actionField, "enabled", remoteState, "writable");
      
      var updateView:Function = function (value:Object):void {
        if(textInput) {
          if(value == null) {
            textInput.text = null;
          } else {
            textInput.text = value.toString();
          }
        } else {
          if(value == null) {
            actionField.setStyle("borderStyle","none");
          } else {
            actionField.setStyle("borderStyle","solid");
            actionField.setStyle("borderThickness", 1);
            actionField.setStyle("borderColor", "0xFF0000");
          }
        }
      };
      BindingUtils.bindSetter(updateView, remoteState, "value", true);
  
      if(textInput) {
        var triggerAction:Function = function (event:Event):void {
          var inputText:String = (event.currentTarget as TextInput).text;
          if(!inputText || inputText.length == 0) {
            remoteState.value = null;
          } else if(inputText != remoteState.value) {
            _actionHandler.execute(action, inputText);
          }
        };
        textInput.addEventListener(FlexEvent.ENTER,triggerAction);
        textInput.addEventListener(FocusEvent.MOUSE_FOCUS_CHANGE,triggerAction);
        textInput.addEventListener(FocusEvent.KEY_FOCUS_CHANGE,triggerAction);
      }
    }
    
    private function createColorField(remoteColorField:RColorField):UIComponent {
      var colorField:HBox = new HBox();
      var colorPicker:ColorPicker = new ColorPicker();
      colorPicker.name = "cc";
      bindColorPicker(colorPicker, remoteColorField.state);
      colorField.addChild(colorPicker);
      var resetButton:Button = new Button();
//	    resetButton.setStyle("icon", IconFactory.getClass(resetButton
//	                               , computeUrl("classpath:org/jspresso/framework/application/images/reset-48x48.png") 
//                                 , 16, 16));
      resetButton.setStyle("icon", _resetIcon);
      colorField.addChild(resetButton);
      resetButton.addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {
        remoteColorField.state.value = remoteColorField.defaultColor;
      });
      var focusIn:Function = function(event:FocusEvent):void {
        var cc:UIComponent = (event.currentTarget as Container).getChildByName("cc") as UIComponent;
        cc.setFocus();
      };
      colorField.addEventListener(FocusEvent.FOCUS_IN, focusIn);
      colorPicker.editable = true;
      return colorField;
    }
    
    private function createCheckBox(remoteCheckBox:RCheckBox):UIComponent {
      var checkBox:CheckBox = new CheckBox();
      bindCheckBox(checkBox, remoteCheckBox.state);
      return checkBox;
    }

    private function bindCheckBox(checkBox:CheckBox, remoteState:RemoteValueState):void {
      BindingUtils.bindProperty(checkBox, "selected", remoteState, "value", true);
      BindingUtils.bindProperty(remoteState, "value", checkBox, "selected", true);
      BindingUtils.bindProperty(checkBox, "enabled", remoteState, "writable");
    }

    private function createComboBox(remoteComboBox:RComboBox):UIComponent {
      var comboBox:RIconComboBox = new RIconComboBox();
      comboBox.dataProvider = remoteComboBox.values;
      comboBox.labels = remoteComboBox.translations;
      comboBox.icons = remoteComboBox.icons;
      bindComboBox(comboBox, remoteComboBox);

      var itemRenderer:ClassFactory = new ClassFactory(RIconListItemRenderer);
      itemRenderer.properties = {labels:remoteComboBox.translations, icons:remoteComboBox.icons, iconTemplate:_iconTemplate};
      comboBox.itemRenderer = itemRenderer;
      
      sizeMaxComponentWidth(comboBox, 10);
      return comboBox;
    }

    private function bindComboBox(comboBox:RIconComboBox, remoteComboBox:RComboBox):void {
      BindingUtils.bindProperty(comboBox, "selectedItem", remoteComboBox.state, "value", true);
      BindingUtils.bindProperty(remoteComboBox.state, "value", comboBox, "selectedItem", true);
      BindingUtils.bindProperty(comboBox, "enabled", remoteComboBox.state, "writable");
    }

    private function createBorderContainer(remoteBorderContainer:RBorderContainer):Container {
      var borderContainer:Grid = new Grid();
      var row:GridRow;
      var cell:GridItem;
      var cellComponent:UIComponent;
      
      var nbCols:int = 1;
      if(remoteBorderContainer.west != null) {
        nbCols++;
      }
      if(remoteBorderContainer.east != null) {
        nbCols++;
      }
      
      if(remoteBorderContainer.north != null) {
        row = new GridRow();
        row.percentWidth = 100.0;
        borderContainer.addChild(row);
        
        cell = new GridItem();
        cell.colSpan = nbCols;
        cell.percentWidth = 100.0;
        cell.percentHeight = 100.0;
        row.addChild(cell);
        
        cellComponent = createComponent(remoteBorderContainer.north);
        cellComponent.percentWidth = 100.0;
        cellComponent.percentHeight = 100.0;
        cell.addChild(cellComponent);
      }
      if(   remoteBorderContainer.west != null
         || remoteBorderContainer.center != null
         || remoteBorderContainer.east != null) {
        row = new GridRow();
        row.percentWidth = 100.0;
        row.percentHeight = 100.0;
        borderContainer.addChild(row);
        if(remoteBorderContainer.west != null) {
          cell = new GridItem();
          cell.percentHeight = 100.0;
          row.addChild(cell);
          
          cellComponent = createComponent(remoteBorderContainer.west);
          cellComponent.percentWidth = 100.0;
          cellComponent.percentHeight = 100.0;
          cell.addChild(cellComponent);
        }
        if(remoteBorderContainer.center != null) {
          cell = new GridItem();
          cell.percentHeight = 100.0;
          cell.percentWidth = 100.0;
          row.addChild(cell);
          
          cellComponent = createComponent(remoteBorderContainer.center);
          cellComponent.percentWidth = 100.0;
          cellComponent.percentHeight = 100.0;
          cell.addChild(cellComponent);
        }
        if(remoteBorderContainer.east != null) {
          cell = new GridItem();
          cell.percentHeight = 100.0;
          row.addChild(cell);
          
          cellComponent = createComponent(remoteBorderContainer.east);
          cellComponent.percentWidth = 100.0;
          cellComponent.percentHeight = 100.0;
          cell.addChild(cellComponent);
        }
      }
      if(remoteBorderContainer.south != null) {
        
        row = new GridRow();
        row.percentWidth = 100.0;
        borderContainer.addChild(row);
        
        cell = new GridItem();
        cell.colSpan = nbCols;
        cell.percentWidth = 100.0;
        cell.percentHeight = 100.0;
        row.addChild(cell);
        
        cellComponent = createComponent(remoteBorderContainer.south);
        cellComponent.percentWidth = 100.0;
        cellComponent.percentHeight = 100.0;
        cell.addChild(cellComponent);
      }

      return borderContainer;
    }

    private function createCardContainer(remoteCardContainer:RCardContainer):Container {
      var cardContainer:RViewStack = new RViewStack(remoteCardContainer.guid);
      // view stack may have to be retrieved for late update of cards.
      _remotePeerRegistry.register(cardContainer);
      //cardContainer.resizeToContent = true;
      
      for(var i:int = 0; i < remoteCardContainer.cardNames.length; i++) {
        var rCardComponent:RComponent = remoteCardContainer.cards[i] as RComponent; 
        var cardName:String = remoteCardContainer.cardNames[i] as String;
        
        addCard(cardContainer, rCardComponent, cardName);
      }
      bindCardContainer(cardContainer, remoteCardContainer.state);

      return cardContainer;
    }
    
    public function addCard(cardContainer:ViewStack, rCardComponent:RComponent, cardName:String):void {
      var cardCanvas:Canvas = new Canvas();
      cardCanvas.percentWidth = 100.0;
      cardCanvas.percentHeight = 100.0;
      cardCanvas.horizontalScrollPolicy = ScrollPolicy.OFF;
      cardCanvas.verticalScrollPolicy = ScrollPolicy.OFF;
      cardCanvas.name = cardName;
      cardContainer.addChild(cardCanvas);
  
      var cardComponent:UIComponent = createComponent(rCardComponent);
      cardComponent.percentWidth = 100.0;
      cardComponent.percentHeight = 100.0;
      cardCanvas.addChild(cardComponent);
    }

    private function bindCardContainer(cardContainer:ViewStack, remoteState:RemoteValueState):void {
      var selectCard:Function = function (value:Object):void {
        if(value == null) {
          //TODO check why null
          //cardContainer.selectedChild = null;
        } else {
          var selectedCard:Container = cardContainer.getChildByName(value as String) as Container;
          if(selectedCard) {
            cardContainer.selectedChild = selectedCard;
          }
        }
      };
      BindingUtils.bindSetter(selectCard, remoteState, "value", true);
    }

    private function createConstrainedGridContainer(remoteConstrainedGridContainer:RConstrainedGridContainer):Container {
      var constrainedGridContainer:Grid = new Grid();
      
      var i:int;
      var j:int;
      var nbCols:int = 0;
      var row:GridRow;
      var cell:GridItem;
      var cellConstraints:CellConstraints;
      
      // Compute and add rows and columns.
      for(i = 0; i < remoteConstrainedGridContainer.cellConstraints.length; i++) {
        cellConstraints = remoteConstrainedGridContainer.cellConstraints[i] as CellConstraints;
        if(cellConstraints.row + cellConstraints.height > constrainedGridContainer.getChildren().length) {
          for(j = cellConstraints.row + cellConstraints.height - constrainedGridContainer.getChildren().length - 1; j >= 0; j--) {
            row = new GridRow();
            row.percentWidth = 100.0;
            constrainedGridContainer.addChild(row);
          }
        }
        if(cellConstraints.column + cellConstraints.width > nbCols) {
          nbCols = cellConstraints.column + cellConstraints.width;
        }
      }
      // Add cells
      for(i = 0; i < constrainedGridContainer.getChildren().length; i++) {
        row = constrainedGridContainer.getChildAt(i) as GridRow;
        for(j = 0; j < nbCols; j++) {
          row.addChild(new GridItem());
        }
      }
      
      // Add cell components
      for(i = 0; i < remoteConstrainedGridContainer.cellConstraints.length; i++) {
        cellConstraints = remoteConstrainedGridContainer.cellConstraints[i] as CellConstraints;
        row = constrainedGridContainer.getChildAt(cellConstraints.row) as GridRow;
        cell = row.getChildAt(cellConstraints.column) as GridItem;
        cell.colSpan = cellConstraints.width;
        if(cellConstraints.widthResizable) {
          cell.percentWidth = 100.0;
        }
        cell.rowSpan = cellConstraints.height;
        if(cellConstraints.heightResizable) {
          cell.percentHeight = 100.0;
          // Make last spanning row resizable
          (constrainedGridContainer.getChildAt(cellConstraints.row + cellConstraints.height - 1) as GridRow).percentHeight=100.0;
        }
        
        var cellComponent:UIComponent = createComponent(remoteConstrainedGridContainer.cells[i] as RComponent);
        cellComponent.percentWidth = 100.0;
        cellComponent.percentHeight = 100.0;
        cell.addChild(cellComponent);
      }

      // Cleanup unused cells
      for(i = 0; i < constrainedGridContainer.getChildren().length; i++) {
        row = constrainedGridContainer.getChildAt(i) as GridRow;
        for(j = row.getChildren().length - 1; j >= 0; j--) {
          cell = row.getChildAt(j) as GridItem;
          if(cell.getChildren().length == 0) {
            // Cell is empty
            row.removeChildAt(j);
          }
        }
      }

      return constrainedGridContainer;
    }

    private function createEvenGridContainer(remoteEvenGridContainer:REvenGridContainer):Container {
      var evenGridContainer:Grid = new Grid();
      
      var nbRows:int;
      var nbCols:int;
      var i:int;

      var row:int;
      var col:int;
      var gridRow:GridRow;
      var cell:GridItem;
      var cellComponent:UIComponent;
      
      if(remoteEvenGridContainer.drivingDimension == "ROW") {
        nbRows = remoteEvenGridContainer.cells.length / remoteEvenGridContainer.drivingDimensionCellCount;
        if(remoteEvenGridContainer.cells.length % remoteEvenGridContainer.drivingDimensionCellCount > 0) {
          nbRows += 1
        }
        nbCols = remoteEvenGridContainer.drivingDimensionCellCount;
      } else {
        nbRows = remoteEvenGridContainer.drivingDimensionCellCount;
        nbCols = remoteEvenGridContainer.cells.length / remoteEvenGridContainer.drivingDimensionCellCount;
        if(remoteEvenGridContainer.cells.length % remoteEvenGridContainer.drivingDimensionCellCount > 0) {
          nbCols += 1
        }
      }
      for(i = 0; i < nbRows; i++) {
        evenGridContainer.addChild(new GridRow());
      }
      for(i = 0; i < remoteEvenGridContainer.cells.length; i++) {

        gridRow = evenGridContainer.getChildAt(row) as GridRow;

        cell = new GridItem();
        cell.percentHeight = 100.0;
        cell.percentWidth = 100.0;
        gridRow.addChild(cell);

        cellComponent = createComponent(remoteEvenGridContainer.cells[i] as RComponent);
        cellComponent.percentWidth = 100.0;
        cellComponent.percentHeight = 100.0;
        cell.addChild(cellComponent);
        
        if(remoteEvenGridContainer.drivingDimension == "ROW") {
          col ++;
          if(col == nbCols) {
            col = 0;
            row ++;
          }
        } else if(remoteEvenGridContainer.drivingDimension == "COLUMN") {
          row ++;
          if(row == nbRows) {
            row = 0;
            col ++;
          }
        }
      }
      return evenGridContainer;
    }

    private function createForm(remoteForm:RForm):Container {
      var form:Grid = new Grid();
      var col:int = 0;
      var labelsRow:GridRow;
      var componentsRow:GridRow;
      
      componentsRow = new GridRow();
      componentsRow.percentWidth = 100.0;
      if(remoteForm.labelsPosition == "ABOVE") {
        labelsRow = new GridRow();
        labelsRow.percentWidth = 100.0;
        form.addChild(labelsRow);
      } else if(remoteForm.labelsPosition == "ASIDE") {
        labelsRow = componentsRow;
      } else if(remoteForm.labelsPosition == "NONE") {
        labelsRow = componentsRow;
      }
      form.addChild(componentsRow);
      for(var i:int = 0; i < remoteForm.elements.length; i++) {
        var elementWidth:int = remoteForm.elementWidths[i] as int;
        var rComponent:RComponent = remoteForm.elements[i] as RComponent;
        var rComponentLabel:RComponent = remoteForm.elementLabels[i] as RComponent;
        var component:UIComponent = createComponent(rComponent);
        var componentLabel:UIComponent;
        var labelCell:GridItem;
        
        if(remoteForm.labelsPosition != "NONE") {
          componentLabel = createComponent(rComponentLabel, false);
          labelCell = new GridItem();
        }

        var componentCell:GridItem = new GridItem();

        if(elementWidth > remoteForm.columnCount) {
          elementWidth = remoteForm.columnCount;
        }
        if(col + elementWidth > remoteForm.columnCount) {
          componentsRow = new GridRow();
          componentsRow.percentWidth = 100.0;
          if(remoteForm.labelsPosition == "ABOVE") {
            labelsRow = new GridRow();
            labelsRow.percentWidth = 100.0;
            form.addChild(labelsRow);
          } else if(remoteForm.labelsPosition == "ASIDE") {
            labelsRow = componentsRow;
          } else if(remoteForm.labelsPosition == "NONE") {
            labelsRow = componentsRow;
          }
          form.addChild(componentsRow);
          col = 0;
        }
        
        if(remoteForm.labelsPosition == "ABOVE") {
          labelCell.colSpan = elementWidth;
          componentCell.colSpan = elementWidth;
        } else if(remoteForm.labelsPosition == "ASIDE") {
          labelCell.setStyle("verticalAlign","middle");
          componentCell.colSpan = (elementWidth * 2) - 1;
        } else if(remoteForm.labelsPosition == "NONE") {
          componentCell.colSpan = elementWidth;
        }

        if(   rComponent is RTable
           || rComponent is RTextArea
           || rComponent is RList) {
          componentsRow.percentHeight = 100.0;         
        }   

        if(remoteForm.labelsPosition != "NONE") {
          labelsRow.addChild(labelCell);
          labelCell.addChild(componentLabel);
        }

        componentCell.percentWidth=100.0;
        componentCell.percentHeight=100.0;
        componentsRow.addChild(componentCell);
        
        component.percentWidth = 100.0;
        component.percentHeight = 100.0;
        componentCell.addChild(component);
        
        col += elementWidth;
      }
      return form;
    }

    private function createSplitContainer(remoteSplitContainer:RSplitContainer):Container {
      var splitContainer:DividedBox = new DividedBox();
      //splitContainer.resizeToContent = true;

      var component:UIComponent;
      if(remoteSplitContainer.orientation == "VERTICAL") {
        splitContainer.direction = BoxDirection.VERTICAL;
      } else {
        splitContainer.direction = BoxDirection.HORIZONTAL;
      }
      if(remoteSplitContainer.leftTop != null) {
        component = createComponent(remoteSplitContainer.leftTop);
        if(remoteSplitContainer.orientation == "VERTICAL") {
          component.percentWidth = 100.0;
        } else {
          component.percentHeight = 100.0;
        }
        splitContainer.addChild(component);
      }
      if(remoteSplitContainer.rightBottom != null) {
        component = createComponent(remoteSplitContainer.rightBottom);
        component.percentWidth = 100.0;
        component.percentHeight = 100.0;

        splitContainer.addChild(component);
      }
      return splitContainer;
    }

    private function createTabContainer(remoteTabContainer:RTabContainer):Container {
      var tabContainer:TabNavigator = new TabNavigator();
      for(var i:int = 0; i < remoteTabContainer.tabs.length; i++) {
        var rTab:RComponent = remoteTabContainer.tabs[i] as RComponent;
        
        var tabCanvas:Canvas = new Canvas();
        tabCanvas.percentWidth = 100.0;
        tabCanvas.percentHeight = 100.0;
        tabCanvas.label = rTab.label;
        tabCanvas.horizontalScrollPolicy = ScrollPolicy.OFF;
        tabCanvas.verticalScrollPolicy = ScrollPolicy.OFF;
        tabContainer.addChild(tabCanvas);
        
        if(rTab.tooltip != null) {
    		  tabCanvas.toolTip = rTab.tooltip + TOOLTIP_ELLIPSIS;
        }

        var tabContent:UIComponent = createComponent(rTab);
        tabContent.percentWidth = 100.0;
        tabContent.percentHeight = 100.0;
        tabCanvas.addChild(tabContent);
      }
      var creationComplete:Function = function (event:FlexEvent):void {
        if(event.target is TabNavigator) {
          var tabContainer:TabNavigator = event.target as TabNavigator;
          for(var tabIndex:int = 0; tabIndex < tabContainer.getChildren().length; tabIndex ++) {
            var tabButton:Button = tabContainer.getTabAt(tabIndex);
      		  tabButton.setStyle("icon", getIconForComponent(tabButton, (remoteTabContainer.tabs[tabIndex] as RComponent).icon));
      		}
      		tabContainer.removeEventListener(FlexEvent.CREATION_COMPLETE, creationComplete);
        }
      };
      tabContainer.addEventListener(FlexEvent.CREATION_COMPLETE, creationComplete);
      return tabContainer;
    }
    
    private function createDateComponent(remoteDateField:RDateField):UIComponent {
      var dateComponent:UIComponent; 
      if(remoteDateField.type == "DATE_TIME") {
        dateComponent = createDateTimeField(remoteDateField);
      } else {
        dateComponent = createDateField(remoteDateField);
      }
      return dateComponent;
    }

    private function createDateField(remoteDateField:RDateField):UIComponent {
      var dateField:DateField = new DateField();
      dateField.editable = true;
      sizeMaxComponentWidth(dateField, DATE_CHAR_COUNT);
      bindDateField(dateField, remoteDateField.state);
      return dateField;
    }

    private function bindDateField(dateField:DateField, remoteState:RemoteValueState):void {
      BindingUtils.bindProperty(dateField, "selectedDate", remoteState, "value", true);
      BindingUtils.bindProperty(dateField, "enabled", remoteState, "writable");
      var updateModel:Function = function (event:Event):void {
        if(event is FocusEvent) {
          var currentTarget:UIComponent = (event as FocusEvent).currentTarget as UIComponent;
          var relatedObject:DisplayObject = (event as FocusEvent).relatedObject as DisplayObject;
          
          if(currentTarget == dateField
            && !dateField.contains(relatedObject)
            && !dateField.dropdown.contains(relatedObject)) {
            // do not listen to inner focus events.
            remoteState.value = dateField.selectedDate;
          }
        }
      };
      dateField.addEventListener(FlexEvent.ENTER,updateModel);
      dateField.addEventListener(FocusEvent.MOUSE_FOCUS_CHANGE,updateModel);
      dateField.addEventListener(FocusEvent.KEY_FOCUS_CHANGE,updateModel);
    }

    private function createDateTimeField(remoteDateField:RDateField):UIComponent {
      var dateTimeField:DateTimeField = new DateTimeField();
      dateTimeField.editable = true;
      dateTimeField.showTime = true;
      sizeMaxComponentWidth(dateTimeField, DATE_CHAR_COUNT + TIME_CHAR_COUNT);
      bindDateTimeField(dateTimeField, remoteDateField.state);
      return dateTimeField;
    }

    private function bindDateTimeField(dateTimeField:DateTimeField, remoteState:RemoteValueState):void {
      BindingUtils.bindProperty(dateTimeField, "selectedDateTime", remoteState, "value", true);
      BindingUtils.bindProperty(dateTimeField, "enabled", remoteState, "writable");
      var updateModel:Function = function (event:Event):void {
        if(event is FocusEvent) {
          var currentTarget:UIComponent = (event as FocusEvent).currentTarget as UIComponent;
          var relatedObject:DisplayObject = (event as FocusEvent).relatedObject as DisplayObject;
          
          if(currentTarget == dateTimeField
            && !dateTimeField.contains(relatedObject)
            && !dateTimeField.dropdownDateTime.contains(relatedObject)) {
            remoteState.value = dateTimeField.selectedDateTime;
          }
        }
      };
      dateTimeField.addEventListener(FlexEvent.ENTER,updateModel);
      dateTimeField.addEventListener(FocusEvent.MOUSE_FOCUS_CHANGE,updateModel);
      dateTimeField.addEventListener(FocusEvent.KEY_FOCUS_CHANGE,updateModel);
    }

    private function createTimeField(remoteTimeField:RTimeField):UIComponent {
      var timeStepper:TimeStepper = new TimeStepper();
      bindTimeStepper(timeStepper, remoteTimeField.state);
      return timeStepper;
    }
    
    private function bindTimeStepper(timeStepper:TimeStepper, remoteState:RemoteValueState):void {
      BindingUtils.bindProperty(timeStepper, "timeValue", remoteState, "value", true);
      BindingUtils.bindProperty(timeStepper, "enabled", remoteState, "writable");
      sizeMaxComponentWidth(timeStepper, TIME_CHAR_COUNT);
      var updateModel:Function = function(event:Event):void {
        if(event is FocusEvent) {
          var currentTarget:UIComponent = (event as FocusEvent).currentTarget as UIComponent;
          var relatedObject:DisplayObject = (event as FocusEvent).relatedObject as DisplayObject;
          
          if(currentTarget == timeStepper && !timeStepper.contains(relatedObject)) {
            remoteState.value = timeStepper.timeValue;
          }
        }
      };
      timeStepper.addEventListener(FlexEvent.ENTER,updateModel);
      timeStepper.addEventListener(FocusEvent.MOUSE_FOCUS_CHANGE,updateModel);
      timeStepper.addEventListener(FocusEvent.KEY_FOCUS_CHANGE,updateModel);
    }

    private function createDecimalField(remoteDecimalField:RDecimalField):UIComponent {
      var decimalField:TextInput = new TextInput();
      var decimalFormatter:NumberFormatter = createFormatter(remoteDecimalField) as NumberFormatter;
      bindTextInput(decimalField, remoteDecimalField.state,
                    decimalFormatter, createParser(remoteDecimalField));
      decimalField.restrict = "0-9" + decimalFormatter.decimalSeparatorTo;
      return decimalField;
    }

    private function createIntegerField(remoteIntegerField:RIntegerField):UIComponent {
      var integerField:TextInput = new TextInput();
      var integerFormatter:NumberFormatter = createFormatter(remoteIntegerField) as NumberFormatter;
      bindTextInput(integerField, remoteIntegerField.state,
                    integerFormatter, createParser(remoteIntegerField));
      integerField.restrict = "0-9";
      return integerField;
    }

    private function createPercentField(remotePercentField:RPercentField):UIComponent {
      var percentField:TextInput = new TextInput();
      var percentFormatter:NumberFormatter = createFormatter(remotePercentField) as NumberFormatter; 
      bindTextInput(percentField, remotePercentField.state,
                    percentFormatter, createParser(remotePercentField));
      percentField.restrict = "0-9" + percentFormatter.decimalSeparatorTo + PercentFormatter.PERCENT_SUFFIX;
      return percentField;
    }
    
    private function createDurationField(remoteDurationField:RDurationField):UIComponent {
      var durationField:TextInput = new TextInput();
      bindTextInput(durationField, remoteDurationField.state,
                    createFormatter(remoteDurationField), createParser(remoteDurationField));
      return durationField;
    }

    private function createList(remoteList:RList):List {
      var list:List = new List();
      list.horizontalScrollPolicy = ScrollPolicy.AUTO;
      list.verticalScrollPolicy = ScrollPolicy.AUTO;
      if(remoteList.selectionMode == "SINGLE_SELECTION") {
        list.allowMultipleSelection = false;
      } else {
        list.allowMultipleSelection = true;
      }
      
      var itemRenderer:ClassFactory = new ClassFactory(RemoteValueDgItemRenderer);
      list.itemRenderer = itemRenderer;

      list.dataProvider = (remoteList.state as RemoteCompositeValueState).children;
      bindList(list, remoteList.state as RemoteCompositeValueState);
      if(remoteList.rowAction) {
        list.addEventListener(MouseEvent.DOUBLE_CLICK, function(event:MouseEvent):void {
          _actionHandler.execute(remoteList.rowAction);
        });
      }
      return list;
    }

    private function bindList(list:List, state:RemoteCompositeValueState):void {
      BindingUtils.bindSetter(function(selectedIndices:Array):void {
        if(selectedIndices != null && selectedIndices.length > 0) {
          // work on items to translate indices independently of table sorting state.
          var selectedItems:Array = new Array(selectedIndices.length);
          for(var i:int = 0; i < selectedIndices.length; i++) {
            selectedItems[i] = state.children.getItemAt(selectedIndices[i]);
          }
          if(!ArrayUtil.areUnorderedArraysEqual(list.selectedItems, selectedItems)) {
            list.selectedItems = selectedItems;
          }
        } else {
          list.selectedIndex = -1;
          list.selectedIndices = [];
        }
      }, state, "selectedIndices", true);

      BindingUtils.bindSetter(function(selectedItems:Array):void {
        if(selectedItems != null && selectedItems.length > 0) {
          // work on items to translate indices independently of table sorting state.
          var translatedSelectedIndices:Array = new Array(selectedItems.length);
          for(var i:int = 0; i < selectedItems.length; i++) {
            translatedSelectedIndices[i] = state.children.getItemIndex(selectedItems[i]);
          }
          if(translatedSelectedIndices.length > 0) {
            state.leadingIndex = translatedSelectedIndices[0];
          } else {
            state.leadingIndex = -1;
          }
          translatedSelectedIndices.sort(Array.NUMERIC);
          state.selectedIndices = translatedSelectedIndices;
        } else {
          state.leadingIndex = -1;
          state.selectedIndices = null;
        }
      }, list, "selectedItems", true);
    }

    private function createPasswordField(remotePasswordField:RPasswordField):UIComponent {
      var passwordField:TextInput = new TextInput();
      bindTextInput(passwordField, remotePasswordField.state);
      passwordField.displayAsPassword = true;
      sizeMaxComponentWidth(passwordField);
      return passwordField;
    }

    private function createSecurityComponent(remoteSecurityComponent:RSecurityComponent):UIComponent {
      var securityComponent:Canvas = new Canvas();
      return securityComponent;
    }

    private function createTable(remoteTable:RTable):UIComponent {
      var table:DoubleClickDataGrid = new DoubleClickDataGrid();
      var columns:Array = new Array();
      
      table.regenerateStyleCache(false);
      for(var i:int=0; i < remoteTable.columns.length; i++) {
        var rColumn:RComponent = remoteTable.columns[i] as RComponent;
        if(rColumn.state == null) {
          rColumn.state = new RemoteValueState();
        }
        var column:DataGridColumn = new DataGridColumn();
        column.headerText = rColumn.label;
        var itemRenderer:ClassFactory;
        if(rColumn is RComboBox) {
          itemRenderer = new ClassFactory(EnumerationDgItemRenderer);
          itemRenderer.properties = {values:(rColumn as RComboBox).values,
                                     labels:(rColumn as RComboBox).translations,
                                     icons :(rColumn as RComboBox).icons,
                                     iconTemplate:_iconTemplate,
                                     index:i+1};
        } else if( rColumn is RCheckBox
               || (rColumn is RActionField && !(rColumn as RActionField).showTextField)) {
          itemRenderer = new ClassFactory(UIComponentDgItemRenderer);
          itemRenderer.properties = {viewFactory:this,
                                     remoteComponent:rColumn,
                                     index:i+1};
        } else {
          itemRenderer = new ClassFactory(RemoteValueDgItemRenderer);
          itemRenderer.properties = {formatter:createFormatter(rColumn),
                                     index:i+1};
        }
        column.itemRenderer = itemRenderer
        
        var headerRenderer:ClassFactory = new ClassFactory(DgHeaderItemRenderer);
        headerRenderer.properties = {index:i+1};
        column.headerRenderer = headerRenderer;
        
        var itemEditor:ClassFactory = new ClassFactory(RemoteValueDgItemEditor);
        rColumn.state.writable = true;
        var editorComponent:UIComponent = createComponent(rColumn, false);
        itemEditor.properties = {editor:editorComponent,
                                 state:rColumn.state,
                                 index:i+1};
        column.itemEditor = itemEditor;
        if(rColumn is RCheckBox) {
          column.width = table.measureText(column.headerText).width + 16;
        } else {
          column.width = Math.max(
                           Math.min(table.measureText(TEMPLATE_CHAR).width * COLUMN_MAX_CHAR_COUNT,
                                    editorComponent.maxWidth),
                           table.measureText(column.headerText).width + 16
                         );
        }
        editorComponent.maxWidth = UIComponent.DEFAULT_MAX_WIDTH;
        column.editorDataField = "state";
        
        if(!remoteTable.sortingAction) {
          if(rColumn is RCheckBox) {
            column.sortCompareFunction = _remoteValueSorter.compareBooleans;
          } else if(rColumn is RNumericComponent) {
            column.sortCompareFunction = _remoteValueSorter.compareNumbers;
          } else if(rColumn is RDateField) {
            column.sortCompareFunction = _remoteValueSorter.compareDates;
          } else {
            column.sortCompareFunction = _remoteValueSorter.compareStrings;
          }
        }
        columns.push(column);
      }
      
      table.columns = columns;
      if(remoteTable.sortingAction) {
        table.customSort = true;
      }
      if(remoteTable.selectionMode == "SINGLE_SELECTION") {
        table.allowMultipleSelection = false;
      } else {
        table.allowMultipleSelection = true;
      }
      table.editable = remoteTable.state.writable;
      table.horizontalScrollPolicy = ScrollPolicy.AUTO;
      table.verticalScrollPolicy = ScrollPolicy.AUTO;
      
      // Clone array collection to avoid re-ordering items in original collection when sorting.
      var tableModel:ArrayCollection = new ArrayCollection((remoteTable.state as RemoteCompositeValueState).children.toArray());
      table.dataProvider = tableModel;
      (remoteTable.state as RemoteCompositeValueState).children.addEventListener(CollectionEvent.COLLECTION_CHANGE, function(event:CollectionEvent):void {
        var item:Object;
        if(event.kind == CollectionEventKind.ADD) {
          for each (item in event.items) {
            tableModel.addItem(item);
          }
        } else if(event.kind == CollectionEventKind.REMOVE) {
          for each (item in event.items) {
            tableModel.removeItemAt(tableModel.getItemIndex(item));
          }
        } else if(event.kind == CollectionEventKind.REPLACE) {
          for each (item in event.items) {
            var oldItem:Object = (item as PropertyChangeEvent).oldValue;
            var newItem:Object = (item as PropertyChangeEvent).newValue;
            tableModel.setItemAt(newItem,
                                 tableModel.getItemIndex(oldItem));
          }
        } else if(event.kind == CollectionEventKind.RESET) {
          // could be finer.
          tableModel.removeAll();
          for each (item in (event.currentTarget as ArrayCollection).source) {
            tableModel.addItem(item);
          }
        }
      });
      bindTable(table, remoteTable);
      if(remoteTable.rowAction) {
        table.addEventListener(MouseEvent.DOUBLE_CLICK, function(event:MouseEvent):void {
          _actionHandler.execute(remoteTable.rowAction);
        });
      }
      return table;
    }
    
    private function bindTable(table:DoubleClickDataGrid, remoteTable:RTable):void {
      var state:RemoteCompositeValueState = remoteTable.state as RemoteCompositeValueState;
      if(remoteTable.sortingAction) {
        table.addEventListener(DataGridEvent.HEADER_RELEASE, function(event:DataGridEvent):void {
          event.preventDefault();
          var column:DataGridColumn = table.columns[event.columnIndex];
          column.sortDescending = !column.sortDescending;
          table.displaySort(event.columnIndex, column.sortDescending);
          var property:String = remoteTable.columnIds[((column.itemRenderer as ClassFactory).properties["index"] as int) - 1];
          var orderingProperties:Object = new Object();
          orderingProperties[property] = column.sortDescending ? "DESCENDING" : "ASCENDING";
          var sortCommand:RemoteSortCommand = new RemoteSortCommand();
          sortCommand.orderingProperties = orderingProperties;
          sortCommand.viewStateGuid = remoteTable.state.guid;
          sortCommand.targetPeerGuid = remoteTable.sortingAction.guid;
          _commandHandler.registerCommand(sortCommand);
        });
      } else {
        table.addEventListener(DataGridEvent.HEADER_RELEASE, function(event:DataGridEvent):void {
          _remoteValueSorter.sortColumnIndex = (event.itemRenderer as DgHeaderItemRenderer).index;
        });
      }
      BindingUtils.bindSetter(function(selectedItems:Array):void {
        if(selectedItems != null && selectedItems.length > 0) {
          // work on items to translate indices independently of table sorting state.
          var translatedSelectedIndices:Array = new Array(selectedItems.length);
          for(var i:int = 0; i < selectedItems.length; i++) {
            translatedSelectedIndices[i] = state.children.getItemIndex(selectedItems[i]);
          }
          if(translatedSelectedIndices.length > 0) {
            state.leadingIndex = translatedSelectedIndices[0];
          } else {
            state.leadingIndex = -1;
          }
          if(!ArrayUtil.areUnorderedArraysEqual(translatedSelectedIndices, state.selectedIndices)) {
            translatedSelectedIndices.sort(Array.NUMERIC);
            state.selectedIndices = translatedSelectedIndices;
          }
        } else {
          state.leadingIndex = -1;
          state.selectedIndices = null;
        }
      }, table, "selectedItems", true);
      BindingUtils.bindSetter(function(selectedIndices:Array):void {
        if(selectedIndices != null && selectedIndices.length > 0) {
          // work on items to translate indices independently of table sorting state.
          var selectedItems:Array = new Array(selectedIndices.length);
          for(var i:int = 0; i < selectedIndices.length; i++) {
            if(selectedIndices[i] > -1) {
              selectedItems[i] = state.children.getItemAt(selectedIndices[i]);
            }
          }
          if(!ArrayUtil.areUnorderedArraysEqual(table.selectedItems, selectedItems)) {
            table.selectedItems = selectedItems;
          }
        } else {
          table.selectedIndex = -1;
          table.selectedIndices = [];
        }
      }, state, "selectedIndices", true);
      
      table.addEventListener(DataGridEvent.ITEM_EDIT_END, function(event:DataGridEvent):void {
    	  _actionHandler.setCurrentViewStateGuid(null);
        if (event.reason != DataGridEventReason.CANCELLED) {
          var table:DataGrid = event.currentTarget as DataGrid;
          if(table.itemEditorInstance is RemoteValueDgItemEditor) {
            var currentEditor:RemoteValueDgItemEditor = table.itemEditorInstance as RemoteValueDgItemEditor;
            var state:RemoteValueState = currentEditor.state;
            var row:RemoteCompositeValueState = (table.dataProvider as ArrayCollection)[event.rowIndex] as RemoteCompositeValueState; 
            var cell:RemoteValueState = row.children[currentEditor.index] as RemoteValueState;

            cell.value = state.value;
          }
        }
      });
      table.addEventListener(DataGridEvent.ITEM_EDIT_BEGINNING, function(event:DataGridEvent):void {
        var dg:DataGrid = event.currentTarget as DataGrid;
        var column:DataGridColumn = dg.columns[event.columnIndex]; 
        var rowCollection:ArrayCollection = dg.dataProvider as ArrayCollection;
        var cellValueState:RemoteValueState = (rowCollection[event.rowIndex] as RemoteCompositeValueState)
            .children[(column.itemRenderer as ClassFactory).properties["index"] as int] as RemoteValueState;
        if(!cellValueState.writable) {
    	    event.preventDefault();
    	  }
    	});
      table.addEventListener(DataGridEvent.ITEM_EDIT_BEGIN, function(event:DataGridEvent):void {
        var dg:DataGrid = event.currentTarget as DataGrid;
        var column:DataGridColumn = dg.columns[event.columnIndex]; 
        var rowCollection:ArrayCollection = dg.dataProvider as ArrayCollection;
        var cellValueState:RemoteValueState = (rowCollection[event.rowIndex] as RemoteCompositeValueState)
            .children[(column.itemRenderer as ClassFactory).properties["index"] as int] as RemoteValueState;
    	  _actionHandler.setCurrentViewStateGuid(cellValueState.guid);
    	});
      table.addEventListener(DataGridEvent.ITEM_FOCUS_IN, function(event:DataGridEvent):void {
        ((event.currentTarget as DataGrid).itemEditorInstance as UIComponent).setFocus();
      });
    }

    private function createTextArea(remoteTextArea:RTextArea):UIComponent {
      var textArea:TextArea = new TextArea();
      if(remoteTextArea.maxLength > 0) {
        textArea.maxChars = remoteTextArea.maxLength;
      }
      bindTextArea(textArea, remoteTextArea.state);
      return textArea;
    }

    private function createLabel(remoteLabel:RLabel):UIComponent {
      var uiComponent:UIComponent;
      if(remoteLabel.multiLine) {
        var textArea:TextArea = new TextArea();
        textArea.editable = false;
        textArea.wordWrap = false;
        if(remoteLabel.maxLength > 0) {
          textArea.maxChars = remoteLabel.maxLength;
        }
        uiComponent = textArea;
      } else {
        var label:Label = new Label();
        if(remoteLabel.maxLength > 0) {
          sizeMaxComponentWidth(label, remoteLabel.maxLength);
        } else {
          sizeMaxComponentWidth(label);
        }
        if(!remoteLabel.state && remoteLabel.label) {
          if(isHtml(remoteLabel.label)) {
              label.text = null;
              label.htmlText = remoteLabel.label;
          } else {
            label.htmlText = null;
            label.text = remoteLabel.label;
          }
        }
        uiComponent = label;
      }
      if(remoteLabel.state) {
        bindLabel(uiComponent, remoteLabel.state);
      }
      return uiComponent;
    }

    private function bindLabel(label:UIComponent, remoteState:RemoteValueState):void {
      if(label is Label) {
        var updateLabel:Function = function (value:Object):void {
          if(value == null) {
            (label as Label).text = null;
            (label as Label).htmlText = null;
          } else {
            if(isHtml(value.toString())) {
              (label as Label).text = null;
              (label as Label).htmlText = value.toString();
            } else {
              (label as Label).htmlText = null;
              (label as Label).text = value.toString();
            }
          }
        };
        BindingUtils.bindSetter(updateLabel, remoteState, "value", true);
      } else if(label is TextArea) {
        var updateTextArea:Function = function (value:Object):void {
          if(value == null) {
            (label as TextArea).text = null;
            (label as TextArea).htmlText = null;
          } else {
            if(isHtml(value.toString())) {
              (label as TextArea).text = null;
              (label as TextArea).htmlText = value.toString();
            } else {
              (label as TextArea).htmlText = null;
              (label as TextArea).text = value.toString();
            }
          }
        };
        BindingUtils.bindSetter(updateTextArea, remoteState, "value", true);
      }
    }
    
    private function createTextField(remoteTextField:RTextField):UIComponent {
      var textField:TextInput = new TextInput();
      if(remoteTextField.maxLength > 0) {
        textField.maxChars = remoteTextField.maxLength;
        sizeMaxComponentWidth(textField, remoteTextField.maxLength);
      } else {
        sizeMaxComponentWidth(textField);
      }
      bindTextInput(textField, remoteTextField.state);
      return textField;
    }

    public function createAction(remoteAction:RAction, useLabel:Boolean=false):Button {
      var label:String = null;
      if(useLabel) {
        label = remoteAction.name;
      }
      var button:Button = createButton(label, remoteAction.description, remoteAction.icon);
		  //BindingUtils.bindProperty(button, "enabled", remoteAction, "enabled", true);
      var updateButtonState:Function = function (enabled:Boolean):void {
        button.enabled = enabled;
      };
      BindingUtils.bindSetter(updateButtonState, remoteAction, "enabled", true);
		  _remotePeerRegistry.register(remoteAction);
		  button.addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {
		    _actionHandler.execute(remoteAction);
		  });
      return button;
    }
    
    public function createButton(label:String, tooltip:String, icon:RIcon):Button {
      var button:Button = new Button();
      if(icon) {
	      button.setStyle("icon", getIconForComponent(button, icon));
	    }
	    if(label) {
		    button.label = label;
	    } else if(icon) {
	      button.regenerateStyleCache(false);
	      var cornerRadius:Number = button.getStyle("cornerRadius") as Number;
	      button.width = icon.dimension.width + cornerRadius;
	      button.height = icon.dimension.height + cornerRadius;
	    }
	    if(tooltip) {
		    button.toolTip = tooltip + TOOLTIP_ELLIPSIS;
		  }
      return button;
    }

    private function bindTextInput(textInput:TextInput, remoteState:RemoteValueState,
                                   formatter:Formatter = null, parser:Parser = null):void {
      
      BindingUtils.bindProperty(textInput, "editable", remoteState, "writable");

      var updateView:Function = function (value:Object):void {
        if(value == null) {
          textInput.text = null;
        } else {
          if(formatter != null) {
            textInput.text = formatter.format(value);
          } else {
            textInput.text = value.toString();
          }
        }
      };
      BindingUtils.bindSetter(updateView, remoteState, "value", true);

      var updateModel:Function = function (event:Event):void {
        var inputText:String = (event.currentTarget as TextInput).text;
        if(inputText == null || inputText.length == 0) {
          remoteState.value = null;
        } else {
          if(parser != null) {
            remoteState.value = parser.parse(inputText);
          } else {
            remoteState.value = inputText;
          }
        }
      };
      textInput.addEventListener(FlexEvent.ENTER,updateModel);
      textInput.addEventListener(FocusEvent.MOUSE_FOCUS_CHANGE,updateModel);
      textInput.addEventListener(FocusEvent.KEY_FOCUS_CHANGE,updateModel);
    }
    
    private function bindTextArea(textArea:TextArea, remoteState:RemoteValueState):void {
      BindingUtils.bindProperty(textArea, "text", remoteState, "value", true);
      BindingUtils.bindProperty(textArea, "editable", remoteState, "writable");
      var updateModel:Function = function (event:Event):void {
        remoteState.value = (event.currentTarget as TextArea).text;
      };
      textArea.addEventListener(FocusEvent.MOUSE_FOCUS_CHANGE,updateModel);
      textArea.addEventListener(FocusEvent.KEY_FOCUS_CHANGE,updateModel);
    }

    private function bindColorPicker(colorPicker:ColorPicker, remoteState:RemoteValueState):void {
      BindingUtils.bindProperty(colorPicker, "selectedColor", remoteState, "value", true);
      BindingUtils.bindProperty(colorPicker, "enabled", remoteState, "writable");
      var updateModel:Function = function (event:Event):void {
        var currentAlpha:String;
        if(remoteState.value != null) {
          currentAlpha = (remoteState.value as String).substr(2,2);
        } else {
          currentAlpha = "00";
        }
        remoteState.value = "0x" + currentAlpha
                            + (event.currentTarget as ColorPicker).selectedColor.toString(16);
      };
      colorPicker.addEventListener(ColorPickerEvent.CHANGE,updateModel);
    }

    public function getIconForComponent(component:UIComponent, rIcon:RIcon):Class {
      if(rIcon != null) {
        return IconFactory.getClass(component, rIcon.imageUrlSpec 
                                    , rIcon.dimension.width, rIcon.dimension.height);
      }
      return null;
    }
    
    private function createFormatter(remoteComponent:RComponent):Formatter {
      if(remoteComponent is RDateField) {
        var dateFormatter:DateFormatter = new DateFormatter();        
        if((remoteComponent as RDateField).type == "DATE_TIME") {
          dateFormatter.formatString = dateFormatter.formatString + " " + _timeFormatter.formatString; 
        }
        return dateFormatter;
      } else if(remoteComponent is RTimeField) {
        return _timeFormatter;
      } else if(remoteComponent is RNumericComponent) {
        var numberFormatter:NumberFormatter
        if(remoteComponent is RPercentField) {
          numberFormatter = new PercentFormatter();
        } else {
          numberFormatter = new NumberFormatter();
        }
        numberFormatter.rounding = NumberBaseRoundType.NEAREST;
        if(remoteComponent is RDecimalComponent) {
          numberFormatter.precision = (remoteComponent as RDecimalComponent).maxFractionDigit;
        } else if(remoteComponent is RIntegerField) {
          numberFormatter.precision = 0;
        }
        return numberFormatter;
      }
      return null;
    }

    private function createParser(remoteComponent:RComponent):Parser {
      if(remoteComponent is RNumericComponent) {
        var numberParser:NumberParser;
        if(remoteComponent is RPercentField) {
          numberParser = new PercentParser();
        } else {
          numberParser = new NumberParser();
        }
        var formatter:NumberFormatter = createFormatter(remoteComponent) as NumberFormatter;
        var numberBase:NumberBase = new NumberBase(formatter.decimalSeparatorTo,
                                                   formatter.thousandsSeparatorTo,
                                                   formatter.decimalSeparatorFrom,
                                                   formatter.decimalSeparatorFrom);
        numberParser.numberBase = numberBase;
        numberParser.precision = formatter.precision as uint;
        return numberParser;
      }
      return null;
    }
    
    private function sizeMaxComponentWidth(component:UIComponent, expectedCharCount:int=FIELD_MAX_CHAR_COUNT, maxCharCount:int=FIELD_MAX_CHAR_COUNT):void {
      component.regenerateStyleCache(false);
      var charCount:int = maxCharCount;
      if(expectedCharCount < charCount) {
        charCount = expectedCharCount;
      }
      component.maxWidth = component.measureText(TEMPLATE_CHAR).width * charCount;
    }
    
    public function get iconTemplate():Class  {
      return _iconTemplate;
    }
    
    public function isHtml(content:String):Boolean {
      if(content) {
        return content.toLowerCase().indexOf("<html>") > -1;
      }
      return false;
    }
  }
}