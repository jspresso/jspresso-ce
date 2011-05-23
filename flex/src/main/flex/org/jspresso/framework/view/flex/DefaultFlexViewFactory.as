/**
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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

  import flash.display.DisplayObject;
  import flash.events.Event;
  import flash.events.FocusEvent;
  import flash.events.MouseEvent;
  import flash.events.TextEvent;
  
  import flexlib.containers.ButtonScrollingCanvas;
  
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
  import mx.controls.Menu;
  import mx.controls.PopUpButton;
  import mx.controls.Text;
  import mx.controls.TextArea;
  import mx.controls.TextInput;
  import mx.controls.Tree;
  import mx.controls.VRule;
  import mx.controls.dataGridClasses.DataGridColumn;
  import mx.core.ClassFactory;
  import mx.core.Container;
  import mx.core.IFlexDisplayObject;
  import mx.core.ScrollPolicy;
  import mx.core.UIComponent;
  import mx.events.CollectionEvent;
  import mx.events.CollectionEventKind;
  import mx.events.ColorPickerEvent;
  import mx.events.DataGridEvent;
  import mx.events.DataGridEventReason;
  import mx.events.FlexEvent;
  import mx.events.IndexChangedEvent;
  import mx.events.ListEvent;
  import mx.events.MenuEvent;
  import mx.events.PropertyChangeEvent;
  import mx.formatters.Formatter;
  import mx.formatters.NumberBase;
  import mx.formatters.NumberBaseRoundType;
  import mx.formatters.NumberFormatter;
  import mx.graphics.SolidColor;
  import mx.resources.ResourceManager;
  import mx.utils.ObjectUtil;
  
  import org.jspresso.framework.action.IActionHandler;
  import org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler;
  import org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteSortCommand;
  import org.jspresso.framework.application.frontend.command.remote.RemoteTableChangedCommand;
  import org.jspresso.framework.gui.remote.RAction;
  import org.jspresso.framework.gui.remote.RActionComponent;
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
  import org.jspresso.framework.gui.remote.RHtmlArea;
  import org.jspresso.framework.gui.remote.RIcon;
  import org.jspresso.framework.gui.remote.RImageComponent;
  import org.jspresso.framework.gui.remote.RIntegerField;
  import org.jspresso.framework.gui.remote.RLabel;
  import org.jspresso.framework.gui.remote.RLink;
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
  import org.jspresso.framework.state.remote.RemoteFormattedValueState;
  import org.jspresso.framework.state.remote.RemoteValueState;
  import org.jspresso.framework.util.array.ArrayUtil;
  import org.jspresso.framework.util.format.DateFormatter;
  import org.jspresso.framework.util.format.DateUtils;
  import org.jspresso.framework.util.format.NumberParser;
  import org.jspresso.framework.util.format.Parser;
  import org.jspresso.framework.util.format.PasswordFormatter;
  import org.jspresso.framework.util.format.PercentFormatter;
  import org.jspresso.framework.util.format.PercentParser;
  import org.jspresso.framework.util.format.TimeParser;
  import org.jspresso.framework.util.gui.CellConstraints;
  import org.jspresso.framework.util.html.HtmlUtil;
  import org.jspresso.framework.util.lang.DateDto;
  import org.jspresso.framework.util.remote.registry.IRemotePeerRegistry;
  import org.sepy.ui.CheckBoxExtended;
  
  public class DefaultFlexViewFactory {

    [Embed(source="mx/controls/Image.png")]
    private var _iconTemplate:Class;

    [Embed(source="/assets/images/reset-16x16.png")]
    private var _resetIcon:Class;

    private static const TOOLTIP_ELLIPSIS:String = "...";
    private static const TEMPLATE_CHAR:String = "O";
    private static const FIELD_MAX_CHAR_COUNT:int = 32;
    private static const NUMERIC_FIELD_MAX_CHAR_COUNT:int = 16;
    private static const COLUMN_MAX_CHAR_COUNT:int = 20;
    private static const DATE_CHAR_COUNT:int = 10;
    private static const TIME_CHAR_COUNT:int = 6;

    private var _remotePeerRegistry:IRemotePeerRegistry;
    private var _actionHandler:IActionHandler;
    private var _commandHandler:IRemoteCommandHandler;
    private var _remoteValueSorter:RemoteValueSorter;
    private var _timeFormatter:DateFormatter;
    private var _passwordFormatter:PasswordFormatter;
    
    private var _lastActionTimestamp:Date = new Date();

    public function DefaultFlexViewFactory(remotePeerRegistry:IRemotePeerRegistry,
                                           actionHandler:IActionHandler,
                                           commandHandler:IRemoteCommandHandler) {
      _remotePeerRegistry = remotePeerRegistry;
      _actionHandler = actionHandler;
      _commandHandler = commandHandler;
      _remoteValueSorter = new RemoteValueSorter();
      _timeFormatter = new DateFormatter();
      _passwordFormatter = new PasswordFormatter();
      _timeFormatter.formatString = "JJ:NN:SS"
    }
    
    public function createComponent(remoteComponent:RComponent, registerState:Boolean=true):UIComponent {
      var component:UIComponent = createCustomComponent(remoteComponent);
      if(component == null) {
        if(remoteComponent is RActionField) {
          component = createActionField(remoteComponent as RActionField);
        } else if(remoteComponent is RActionComponent) {
          component = createActionComponent(remoteComponent as RActionComponent);
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
      }
      if(component == null) {
        component = new Canvas();
      }
      if(!(component is Tree)) {
        component.minWidth = 0;
      }
      component.id = remoteComponent.guid;
      if(remoteComponent.tooltip != null) {
        component.toolTip = remoteComponent.tooltip;
      }
      component = decorateWithActions(remoteComponent, component);
      if(remoteComponent.borderType == "TITLED") {
        var decorator:Panel = new Panel();
        decorator.percentWidth = component.percentWidth;
        decorator.percentHeight = component.percentHeight;
        component.percentWidth = 100.0;
        component.percentHeight = 100.0;
        decorator.addChild(component);
        decorator.title = remoteComponent.label;
        decorator.titleIcon = getIconForComponent(decorator, remoteComponent.icon);
        decorator.setStyle("borderAlpha", 1);
        decorator.setStyle("borderThicknessLeft", 2);
        decorator.setStyle("borderThicknessRight", 2);
        decorator.setStyle("borderThicknessBottom", 2);
        decorator.horizontalScrollPolicy = ScrollPolicy.OFF;
        decorator.verticalScrollPolicy = ScrollPolicy.OFF;
        component = decorator;
      } else if(remoteComponent.borderType == "SIMPLE") {
        component.setStyle("borderStyle","solid");
      }
      applyComponentStyle(component, remoteComponent);
      if(remoteComponent.preferredSize) {
        component.addEventListener(FlexEvent.CREATION_COMPLETE, function(e:FlexEvent):void {
          if(remoteComponent.preferredSize.width > 0) {
            component.width = remoteComponent.preferredSize.width;
          }
          if(remoteComponent.preferredSize.height > 0) {
            component.height = remoteComponent.preferredSize.height;
          }
        });
      }
      if(registerState) {
        getRemotePeerRegistry().register(remoteComponent.state);
      }
      return component;
    }
    
    protected function applyComponentStyle(component:*, remoteComponent:RComponent):void {
      if(remoteComponent.foreground) {
        component.setStyle("color", remoteComponent.foreground);
        if(component is IFlexDisplayObject) {
          (component as IFlexDisplayObject).alpha = getAlphaFromArgb(remoteComponent.foreground);
        }
      }
      if(remoteComponent.background) {
        component.setStyle("backgroundColor", remoteComponent.background);
        component.setStyle("backgroundAlpha", getAlphaFromArgb(remoteComponent.background));
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
    }
    
    protected function getColorFromArgb(argb:String):SolidColor {
      var color:uint = parseInt(argb.substr(argb.length - 6, 6), 16);
      return new SolidColor(color, getAlphaFromArgb(argb));
    }

    protected function getAlphaFromArgb(argb:String):Number {
      if(argb && argb.length == 10) {
        var alpha:Number = parseInt(argb.substr(2,2), 16);
        return alpha / 255; 
      }
      return 1.0;
    }
    
    protected function decorateWithActions(remoteComponent:RComponent, component:UIComponent):UIComponent {
      var toolBar:ApplicationControlBar;
      var secondaryToolBar:ApplicationControlBar;
      if(!(remoteComponent is RActionField) && remoteComponent.actionLists != null) {
        toolBar = createToolBar(remoteComponent, component);
      } else {
        toolBar = createDefaultToolBar(remoteComponent, component);
      }
      if(remoteComponent.secondaryActionLists != null && remoteComponent.secondaryActionLists.length > 0) {
        secondaryToolBar = createSecondaryToolBar(remoteComponent, component);
      }
      if(toolBar || secondaryToolBar) {
        var surroundingBox:VBox = new VBox();
        component.percentWidth = 100.0;
        component.percentHeight = 100.0;
        
        if(toolBar) {
          surroundingBox.addChild(decorateWithSlideBar(toolBar));
        }
        surroundingBox.addChild(component);
        if(secondaryToolBar) {
          surroundingBox.addChild(decorateWithSlideBar(secondaryToolBar));
        }
        surroundingBox.horizontalScrollPolicy = ScrollPolicy.OFF;
        surroundingBox.verticalScrollPolicy = ScrollPolicy.OFF;
        return surroundingBox;
      }
      return component;
    }
    
    public function decorateWithSlideBar(component:UIComponent):ButtonScrollingCanvas {
      var slideBar:ButtonScrollingCanvas = new ButtonScrollingCanvas();
      slideBar.addChild(component);
      slideBar.percentWidth = 100.0;
      slideBar.buttonWidth = 10;
      slideBar.startScrollingEvent= MouseEvent.MOUSE_OVER;
      slideBar.stopScrollingEvent= MouseEvent.MOUSE_OUT;
      component.addEventListener(FlexEvent.CREATION_COMPLETE, function(event:FlexEvent):void {
        slideBar.height = (event.currentTarget as UIComponent).measuredHeight;
        slideBar.minHeight = slideBar.height;
        slideBar.maxHeight = slideBar.height;
      });
      return slideBar;
    }

    protected function createDefaultToolBar(remoteComponent:RComponent, component:UIComponent):ApplicationControlBar {
      return null;
    }
    
    protected function createToolBar(remoteComponent:RComponent, component:UIComponent):ApplicationControlBar {
      return createToolBarFromActionLists(remoteComponent.actionLists);
    }
    
    protected function createSecondaryToolBar(remoteComponent:RComponent, component:UIComponent):ApplicationControlBar {
      return createToolBarFromActionLists(remoteComponent.secondaryActionLists);
    }
    
    public function createToolBarFromActionLists(actionLists:Array):ApplicationControlBar {
      var toolBar:ApplicationControlBar = new ApplicationControlBar();
      toolBar.percentWidth = 100.0;
      toolBar.setStyle("fillAlphas",[0.5,0.5]);
      toolBar.setStyle("fillColors",[0xBBBBBB,0x666666]);
      toolBar.setStyle("horizontalGap",2);
      installActionLists(toolBar, actionLists);
      return toolBar;
    }
    
    public function installActionLists(toolBar:ApplicationControlBar, actionLists:Array):void {
      if(actionLists != null) {
        for(var i:int = 0; i < actionLists.length; i++) {
          var actionList:RActionList = actionLists[i] as RActionList;
          if(actionList.collapsable) {
            var popupButton:Button = createPopupButton(actionList);
            if(popupButton != null) {
              toolBar.addChild(popupButton);
            }
          } else {
            if(actionList.actions != null) {
              for(var j:int = 0; j < actionList.actions.length; j++) {
                toolBar.addChild(createAction(actionList.actions[j]));
              }
            }
          }
          if(i < actionLists.length - 1) {
            addSeparator(toolBar);
          }
        }
      }
    }

    public function addSeparator(toolBar:ApplicationControlBar, size:int=20):void {
      var separator:VRule = new VRule();
      separator.height = size;
      separator.maxHeight = size;
      toolBar.addChild(separator);
    }

    protected function getRemotePeerRegistry():IRemotePeerRegistry {
      return _remotePeerRegistry;
    }
    
    protected function getActionHandler():IActionHandler {
      return _actionHandler;
    }
    
    protected function createCustomComponent(remoteComponent:RComponent):UIComponent {
      return null;
    }
    
    protected function createContainer(remoteContainer:RContainer):UIComponent {
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

    protected function createNumericComponent(remoteNumericComponent:RNumericComponent):UIComponent {
      var numericComponent:UIComponent;
      if(remoteNumericComponent is RDecimalComponent) {
        numericComponent = createDecimalComponent(remoteNumericComponent as RDecimalComponent);
      } else if(remoteNumericComponent is RIntegerField) {
        numericComponent = createIntegerField(remoteNumericComponent as RIntegerField);
      }
      if(remoteNumericComponent.maxValue) {
        sizeMaxComponentWidth(numericComponent,remoteNumericComponent,
          createFormatter(remoteNumericComponent).format(remoteNumericComponent.maxValue).length,
          NUMERIC_FIELD_MAX_CHAR_COUNT);
      } else {
        sizeMaxComponentWidth(numericComponent,remoteNumericComponent,
          NUMERIC_FIELD_MAX_CHAR_COUNT,
          NUMERIC_FIELD_MAX_CHAR_COUNT);
      }
      configureHorizontalAlignment(numericComponent, remoteNumericComponent.horizontalAlignment);
      return numericComponent;
    }

    protected function createDecimalComponent(remoteDecimalComponent:RDecimalComponent):UIComponent {
      var decimalComponent:UIComponent;
      if(remoteDecimalComponent is RDecimalField) {
        decimalComponent = createDecimalField(remoteDecimalComponent as RDecimalField);
      } else if(remoteDecimalComponent is RPercentField) {
        decimalComponent = createPercentField(remoteDecimalComponent as RPercentField);
      }
      return decimalComponent;
    }

    protected function createTextComponent(remoteTextComponent:RTextComponent):UIComponent {
      var textComponent:UIComponent;
      if(remoteTextComponent is RTextArea) {
        textComponent = createTextArea(remoteTextComponent as RTextArea);
      } else if(remoteTextComponent is RHtmlArea) {
        textComponent = createHtmlArea(remoteTextComponent as RHtmlArea);
      } else if(remoteTextComponent is RPasswordField) {
        textComponent = createPasswordField(remoteTextComponent as RPasswordField);
      } else if(remoteTextComponent is RTextField) {
        textComponent = createTextField(remoteTextComponent as RTextField);
      } else if(remoteTextComponent is RLabel) {
        textComponent = createLabel(remoteTextComponent as RLabel);
      }
      return textComponent;
    }

    protected function createTree(remoteTree:RTree):UIComponent {
      var tree:SelectionTrackingTree = new SelectionTrackingTree();
      tree.labelField = "value";
      tree.dataTipField = "description";
      tree.dataDescriptor = new RCVSDataDescriptor();
      tree.itemRenderer = new ClassFactory(RemoteValueTreeItemRenderer);
      tree.dataProvider = remoteTree.state;
      tree.minWidth = 200;
      tree.horizontalScrollPolicy = ScrollPolicy.AUTO;
      tree.verticalScrollPolicy = ScrollPolicy.AUTO;
      bindTree(tree, remoteTree.state as RemoteCompositeValueState);
      if(remoteTree.expanded) {
        tree.addEventListener(FlexEvent.CREATION_COMPLETE, function(event:FlexEvent):void {
            expandItem(tree, remoteTree.state as RemoteCompositeValueState, true);
          });
        //        tree.addEventListener(FlexEvent.UPDATE_COMPLETE, function(event:FlexEvent):void {
        //            expandItem(tree, remoteTree.state as RemoteCompositeValueState, true);
        //          });
      } else {
        tree.addEventListener(FlexEvent.CREATION_COMPLETE, function(event:FlexEvent):void {
            expandItem(tree, remoteTree.state as RemoteCompositeValueState, false);
          });
      }
      if(remoteTree.rowAction) {
        getRemotePeerRegistry().register(remoteTree.rowAction);
        tree.doubleClickEnabled = true;
        tree.addEventListener(ListEvent.ITEM_DOUBLE_CLICK, function(event:ListEvent):void {
          _actionHandler.execute(remoteTree.rowAction);
        });
      }
      return tree;
    }
    
    protected function expandItem(tree:SelectionTrackingTree, remoteState:RemoteCompositeValueState, recurse:Boolean):void {
      tree.expandItem(remoteState, true, true, true);
      if(recurse) {
        if(remoteState.children != null) {
          tree.fixListeners(remoteState.children);
          for(var i:int = 0; i < remoteState.children.length; i++) {
            if(remoteState.children[i] is RemoteCompositeValueState) {
              expandItem(tree,remoteState.children[i], recurse); 
            }
          }
        }
      }
    }

    protected function bindTree(tree:SelectionTrackingTree, rootState:RemoteCompositeValueState):void {
      var updateModel:Function = function (selectedItems:Array):void {
        var parentsOfSelectedNodes:Array = new Array();
        var i:int;
        var node:Object;
        var parentNode:RemoteCompositeValueState;
        for(i=0; i < selectedItems.length; i++) {
          node = selectedItems[i];
          parentNode = tree.getParentItem(node);
          if(parentNode == null && !tree.showRoot) {
            parentNode = rootState
          }
          if(parentNode != null && parentsOfSelectedNodes.indexOf(parentNode) == -1) {
            parentsOfSelectedNodes.push(parentNode);
          }
        }
        var oldSelectionTrackingEnabled:Boolean = tree.selectionTrackingEnabled;
        try {
          tree.selectionTrackingEnabled = false;
          clearStateSelection(rootState, parentsOfSelectedNodes);
          for(i=0; i < selectedItems.length; i++) {
            node = selectedItems[i];
            parentNode = tree.getParentItem(node);
            if(parentNode == null && !tree.showRoot) {
              parentNode = rootState
            }
            if(parentNode != null) {
              var selectedIndices:Array = new Array();
              if(parentNode.selectedIndices != null) {
                selectedIndices.concat(parentNode.selectedIndices);
              }
              var childIndex:int = parentNode.children.getItemIndex(node);
              if(childIndex >= 0) {
                selectedIndices.push(childIndex);
                parentNode.leadingIndex = childIndex;
                parentNode.selectedIndices = selectedIndices;
              }
            }
          }
        } finally {
          tree.selectionTrackingEnabled = oldSelectionTrackingEnabled;
        }
      };
      BindingUtils.bindSetter(updateModel, tree, "selectedItems", true);
    }
    
    protected function clearStateSelection(remoteState:RemoteCompositeValueState, excludedNodes:Array):void {
      if(excludedNodes.indexOf(remoteState) == -1) {
        remoteState.leadingIndex = -1;
        remoteState.selectedIndices = null
      }
      if(remoteState.children != null) {
        for(var i:int = 0; i < remoteState.children.length; i++) {
          if(remoteState.children[i] is RemoteCompositeValueState) {
            clearStateSelection(remoteState.children[i] as RemoteCompositeValueState, excludedNodes);
          }
        }
      }
    }

    protected function createImageComponent(remoteImageComponent:RImageComponent):UIComponent {
      var imageComponent:Image = new Image();
      imageComponent.scaleContent = false;
      bindImage(imageComponent, remoteImageComponent.state);
      if(remoteImageComponent.scrollable) {
        imageComponent.setStyle("horizontalAlign","left");
        imageComponent.setStyle("verticalAlign","top");
        var scrollPane:Canvas = new Canvas();
        scrollPane.addChild(imageComponent);
        return scrollPane;
      } else {
        imageComponent.setStyle("horizontalAlign","center");
        imageComponent.setStyle("verticalAlign","middle");
      }
      return imageComponent;
    }
    
    protected function bindImage(imageComponent:Image, remoteState:RemoteValueState):void {
      var updateView:Function = function (value:Object):void {
        imageComponent.source = value;
      };
      BindingUtils.bindSetter(updateView, remoteState, "value", true);
    }
    
    protected function createActionComponent(remoteActionComponent:RActionComponent):UIComponent {
      var actionComponent:Button = createAction(remoteActionComponent.action);
      return actionComponent;
    }
    
    protected function createActionField(remoteActionField:RActionField):UIComponent {
      var actionField:HBox = new HBox();
      actionField.setStyle("verticalAlign","middle");
      actionField.regenerateStyleCache(false);
      var hGap:int = actionField.getStyle("horizontalGap");
      actionField.horizontalScrollPolicy = ScrollPolicy.OFF;
      actionField.verticalScrollPolicy = ScrollPolicy.OFF;
      var textField:TextInput;
      if(remoteActionField.showTextField) {
        textField = new TextInput();
        actionField.percentWidth = 100.0;
        textField.percentWidth = 100.0;
        textField.name = "tf";
        actionField.addChild(textField);
        sizeMaxComponentWidth(textField, remoteActionField);
      }
      var actionComponents:Array = new Array();
      for(var i:int = 0; i < remoteActionField.actionLists.length; i++) {
        var actionList:RActionList = remoteActionField.actionLists[i] as RActionList;
        for(var j:int = 0; j < actionList.actions.length; j++) {
          var actionComponent:Button = createAction(actionList.actions[j]);
          actionComponent.addEventListener(FlexEvent.CREATION_COMPLETE, function(event:FlexEvent):void {
            actionComponent.width = actionComponent.height;
          });
          actionComponent.focusEnabled = false;
          actionField.addChild(actionComponent);
          actionComponents.push(actionComponent);
        }
      }
      bindActionField(actionField, textField, remoteActionField.state, (remoteActionField.actionLists[0] as RActionList).actions[0], actionComponents);
      return actionField;
    }
    
    protected function bindActionField(actionField:UIComponent, textInput:TextInput
                                     , remoteState:RemoteValueState, action:RAction
                                     , actionComponents:Array):void {
      
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
        BindingUtils.bindProperty(textInput, "editable", remoteState, "writable");
        var triggerAction:Function = function (event:Event):void {
          var tf:TextInput = (event.currentTarget as TextInput);
          var inputText:String = tf.text;
          if(!inputText || inputText.length == 0) {
            remoteState.value = null;
          } else if(inputText != remoteState.value) {
            if(remoteState.value == null) {
              tf.text = null;
            } else {
              tf.text = remoteState.value.toString();
            }
            if(event is FocusEvent
              && (event as FocusEvent).relatedObject
              // Not a key focus event
              && ((event as FocusEvent).relatedObject.parent == actionField && (event as FocusEvent).keyCode == 0)) {
              return;
            }
            _actionHandler.execute(action, inputText);
          }
        };
        textInput.addEventListener(FlexEvent.ENTER,triggerAction);
        textInput.addEventListener(FocusEvent.KEY_FOCUS_CHANGE,triggerAction);
        textInput.addEventListener(FocusEvent.MOUSE_FOCUS_CHANGE,triggerAction);
      }
      for each (var actionComponent:UIComponent in actionComponents) {
        BindingUtils.bindProperty(actionComponent, "enabled", remoteState, "writable");
      }
    }
    
    protected function createColorField(remoteColorField:RColorField):UIComponent {
      var colorField:HBox = new HBox();
      var colorPicker:ColorPicker = new ColorPicker();
      colorPicker.name = "cc";
      bindColorPicker(colorPicker, remoteColorField.state);
      colorField.addChild(colorPicker);
      var resetButton:Button = new EnhancedButton();
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
    
    protected function createCheckBox(remoteCheckBox:RCheckBox):UIComponent {
      var checkBox:UIComponent;
      if(remoteCheckBox.triState) {
        checkBox = new CheckBoxExtended();
        (checkBox as CheckBoxExtended).allow3StateForUser = true;
        bindCheckBoxExtended(checkBox as CheckBoxExtended, remoteCheckBox.state);
      } else {
        checkBox = new CheckBox();
        bindCheckBox(checkBox as CheckBox, remoteCheckBox.state);
      }
      return checkBox;
    }

    protected function bindCheckBox(checkBox:CheckBox, remoteState:RemoteValueState):void {
      BindingUtils.bindProperty(checkBox, "selected", remoteState, "value", true);
      BindingUtils.bindProperty(remoteState, "value", checkBox, "selected", true);
      BindingUtils.bindProperty(checkBox, "enabled", remoteState, "writable");
    }

    protected function bindCheckBoxExtended(checkBox:CheckBoxExtended, remoteState:RemoteValueState):void {
      var updateView:Function = function (value:Object):void {
        if(value == null) {
          checkBox.middle = true;
          checkBox.selected = false;
        } else {
          checkBox.middle = false;
          checkBox.selected = value as Boolean;
        }
      };
      BindingUtils.bindSetter(updateView, remoteState, "value", true);

      var updateModel:Function = function (event:Event):void {
        if(checkBox.middle) {
          remoteState.value = null;
        } else {
          remoteState.value = checkBox.selected;
        }
      };
      checkBox.addEventListener(MouseEvent.CLICK, updateModel);

      BindingUtils.bindProperty(checkBox, "enabled", remoteState, "writable");
    }

    protected function createComboBox(remoteComboBox:RComboBox):UIComponent {
      var hasIcon:Boolean = false;
      for each(var icon:RIcon in remoteComboBox.icons) {
        if(icon) {
          hasIcon = true;
          break;
        }
      }
      if(remoteComboBox.readOnly) {
        var label:RIconLabel = new RIconLabel();
        var labels:Object = new Object();
        var icons:Object = new Object();
        
        for(var i:int = 0; i < remoteComboBox.values.length; i++) {
          labels[remoteComboBox.values[i] as String] = remoteComboBox.translations[i];
          icons[remoteComboBox.values[i] as String] = remoteComboBox.icons[i];
        }
        label.labels = labels;
        label.icons = icons;
        label.showIcon = hasIcon;
        
        BindingUtils.bindProperty(label, "value", remoteComboBox.state, "value", true);
        return label;
      } else {
        var comboBox:RIconComboBox = new RIconComboBox();
        comboBox.dataProvider = remoteComboBox.values;
        comboBox.labels = remoteComboBox.translations;
        comboBox.icons = remoteComboBox.icons;
        comboBox.showIcon = hasIcon;
        bindComboBox(comboBox, remoteComboBox);
  
        var itemRenderer:ClassFactory = new ClassFactory(RIconListItemRenderer);
        itemRenderer.properties = {labels:remoteComboBox.translations, icons:remoteComboBox.icons, iconTemplate:_iconTemplate, showIcon:hasIcon};
        comboBox.itemRenderer = itemRenderer;
        
        var width:int = 0;
        for each(var tr:String in remoteComboBox.translations) {
          if(tr.length > width) {
            width = tr.length;
          }
        }
        sizeMaxComponentWidth(comboBox, remoteComboBox, width);
        if(hasIcon) {
          comboBox.maxWidth += 45;
        } else {
          comboBox.maxWidth += 25;
        }
        return comboBox;
      }
    }

    protected function bindComboBox(comboBox:RIconComboBox, remoteComboBox:RComboBox):void {
      BindingUtils.bindProperty(comboBox, "selectedItem", remoteComboBox.state, "value", true);
      BindingUtils.bindProperty(remoteComboBox.state, "value", comboBox, "selectedItem", true);
      BindingUtils.bindProperty(comboBox, "enabled", remoteComboBox.state, "writable");
    }

    protected function createBorderContainer(remoteBorderContainer:RBorderContainer):Container {
      var borderContainer:Grid = new Grid();
      borderContainer.setStyle("horizontalGap",2);
      borderContainer.setStyle("verticalGap",2);

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

      // NORTH
      if(remoteBorderContainer.north != null) {
        row = new GridRow();
        row.percentWidth = 100.0;
        borderContainer.addChild(row);
        
        cell = new GridItem();
        cell.horizontalScrollPolicy  = ScrollPolicy.OFF;
        cell.verticalScrollPolicy  = ScrollPolicy.OFF;
        cell.colSpan = nbCols;
        cell.percentWidth = 100.0;
        if(remoteBorderContainer.north != null) {
          cellComponent = createComponent(remoteBorderContainer.north);
          cell.minHeight = cellComponent.minHeight;
          if(cellComponent.height > 0) {
            cell.height = cellComponent.height;
            cell.setStyle("paddingLeft",2);
            cell.setStyle("paddingRight",2);
            cell.height += 4;
          }
          if(cellComponent.maxHeight > 0) {
            cell.maxHeight = cellComponent.maxHeight;
            cell.setStyle("paddingLeft",2);
            cell.setStyle("paddingRight",2);
            cell.maxHeight += 4;
          }
          cellComponent.percentWidth = 100.0;
          cellComponent.percentHeight = 100.0;
          cell.addChild(cellComponent);
        }
        row.addChild(cell);
      }
      
      // WEST, CENTER, EAST
      row = new GridRow();
      row.percentWidth = 100.0;
      row.percentHeight = 100.0;
      borderContainer.addChild(row);
  
      if(remoteBorderContainer.west != null) {
        cellComponent = createComponent(remoteBorderContainer.west);
        cell = new GridItem();
        cell.horizontalScrollPolicy  = ScrollPolicy.OFF;
        cell.verticalScrollPolicy  = ScrollPolicy.OFF;
        cell.setStyle("horizontalAlign", "left");
        cell.percentHeight = 100.0;
        cell.minWidth = cellComponent.minWidth;
        if(cellComponent.width > 0) {
          cell.width = cellComponent.width;
          cell.setStyle("paddingLeft",2);
          cell.width += 2;
        }
        if(cellComponent.maxWidth > 0) {
          cell.maxWidth = cellComponent.maxWidth;
          cell.setStyle("paddingLeft",2);
          cell.maxWidth += 2;
        }
        cellComponent.percentWidth = 100.0;
        cellComponent.percentHeight = 100.0;
        cell.addChild(cellComponent);
        row.addChild(cell);
      }
  
      cell = new GridItem();
      cell.horizontalScrollPolicy  = ScrollPolicy.OFF;
      cell.verticalScrollPolicy  = ScrollPolicy.OFF;
      cell.percentHeight = 100.0;
      cell.percentWidth = 100.0;
      if(remoteBorderContainer.center != null) {
        cellComponent = createComponent(remoteBorderContainer.center);
        cellComponent.percentWidth = 100.0;
        cellComponent.percentHeight = 100.0;
        cell.addChild(cellComponent);
      }
      row.addChild(cell);
  
      if(remoteBorderContainer.east != null) {
        cellComponent = createComponent(remoteBorderContainer.east);
        cell = new GridItem();
        cell.horizontalScrollPolicy  = ScrollPolicy.OFF;
        cell.verticalScrollPolicy  = ScrollPolicy.OFF;
        cell.setStyle("horizontalAlign", "right");
        cell.percentHeight = 100.0;
        cell.minWidth = cellComponent.minWidth;
        if(cellComponent.width > 0) {
          cell.width = cellComponent.width;
          cell.setStyle("paddingRight",2);
          cell.width += 2;
        }
        if(cellComponent.maxWidth > 0) {
          cell.maxWidth = cellComponent.maxWidth;
          cell.setStyle("paddingRight",2);
          cell.maxWidth += 2;
        }
        cellComponent.percentWidth = 100.0;
        cellComponent.percentHeight = 100.0;
        cell.addChild(cellComponent);
        row.addChild(cell);
      }
      
      if(remoteBorderContainer.south != null) {
        // SOUTH
        row = new GridRow();
        row.percentWidth = 100.0;
        borderContainer.addChild(row);
        cell = new GridItem();
        cell.horizontalScrollPolicy  = ScrollPolicy.OFF;
        cell.verticalScrollPolicy  = ScrollPolicy.OFF;
        cell.colSpan = nbCols;
        cell.percentWidth = 100.0;
        if(remoteBorderContainer.south != null) {
          cellComponent = createComponent(remoteBorderContainer.south);
          cell.minHeight = cellComponent.minHeight;
          if(cellComponent.height > 0) {
            cell.height = cellComponent.height;
            cell.setStyle("paddingLeft",2);
            cell.setStyle("paddingRight",2);
            cell.height += 4;
          }
          if(cellComponent.maxHeight > 0) {
            cell.maxHeight = cellComponent.maxHeight;
            cell.setStyle("paddingLeft",2);
            cell.setStyle("paddingRight",2);
            cell.maxHeight += 4;
          }
          cellComponent.percentWidth = 100.0;
          cellComponent.percentHeight = 100.0;
          cell.addChild(cellComponent);
        }
        row.addChild(cell);
      }

      return borderContainer;
    }

    protected function createCardContainer(remoteCardContainer:RCardContainer):Container {
      var cardContainer:RViewStack = new RViewStack(remoteCardContainer.guid);
      // view stack may have to be retrieved for late update of cards.
      getRemotePeerRegistry().register(cardContainer);
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
      var existingCard:Container = cardContainer.getChildByName(cardName) as Container;
      if(existingCard == null) {
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
    }

    protected function bindCardContainer(cardContainer:ViewStack, remoteState:RemoteValueState):void {
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

    protected function createConstrainedGridContainer(remoteConstrainedGridContainer:RConstrainedGridContainer):Container {
      var constrainedGridContainer:Grid = new Grid();
      constrainedGridContainer.setStyle("horizontalGap",2);
      constrainedGridContainer.setStyle("verticalGap",2);
      
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
          cell = new GridItem();
          cell.horizontalScrollPolicy  = ScrollPolicy.OFF;
          cell.verticalScrollPolicy  = ScrollPolicy.OFF;
          row.addChild(cell);
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

    protected function createEvenGridContainer(remoteEvenGridContainer:REvenGridContainer):Container {
      var evenGridContainer:Grid = new Grid();
      evenGridContainer.setStyle("horizontalGap",2);
      evenGridContainer.setStyle("verticalGap",2);
      
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
        var gr:GridRow = new GridRow();
        gr.percentHeight = 100.0;
        gr.percentWidth = 100.0;
        evenGridContainer.addChild(gr);
      }
      for(i = 0; i < remoteEvenGridContainer.cells.length; i++) {

        gridRow = evenGridContainer.getChildAt(row) as GridRow;

        cell = new GridItem();
        cell.horizontalScrollPolicy  = ScrollPolicy.OFF;
        cell.verticalScrollPolicy  = ScrollPolicy.OFF;
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

    protected function createForm(remoteForm:RForm):Container {
      var form:Grid = new Grid();
      var col:int = 0;
      var labelsRow:GridRow;
      var componentsRow:GridRow;
      
      form.styleName = "contentPane";
      
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
        
        if(rComponent is RSecurityComponent) {
          sizeMaxComponentWidth(component, rComponent);
        }
        
        if(remoteForm.labelsPosition != "NONE") {
          componentLabel = createComponent(rComponentLabel, false);
          componentLabel.maxWidth = NaN;
          labelCell = new GridItem();
          labelCell.horizontalScrollPolicy  = ScrollPolicy.OFF;
          labelCell.verticalScrollPolicy  = ScrollPolicy.OFF;
        }

        var componentCell:GridItem = new GridItem();
        componentCell.horizontalScrollPolicy  = ScrollPolicy.OFF;
        componentCell.verticalScrollPolicy  = ScrollPolicy.OFF;
        componentCell.setStyle("verticalAlign","middle");
        
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
          labelCell.setStyle("horizontalAlign","right");
          // labelCell.setStyle("borderStyle","solid");
          componentCell.colSpan = (elementWidth * 2) - 1;
        } else if(remoteForm.labelsPosition == "NONE") {
          componentCell.colSpan = elementWidth;
        }

        if(remoteForm.labelsPosition != "NONE") {
          labelsRow.addChild(labelCell);
          if((componentLabel as Label).text.length > 0) {
            labelCell.addChild(componentLabel);
            // makes alignment wrong
            //if(remoteForm.labelsPosition == "ASIDE") {
            //  labelCell.maxWidth = componentLabel.maxWidth;
            //}
          } else {
            if(remoteForm.labelsPosition == "ASIDE") {
              labelCell.maxWidth = NaN;
            }
          }
        }

        componentCell.percentWidth=100.0;
        componentCell.percentHeight=100.0;
        componentCell.minWidth = 0;
        if(  rComponent is RTable
          || rComponent is RTextArea
          || rComponent is RList
          || rComponent is RHtmlArea) {
          component.percentWidth = 100.0;
          component.percentHeight = 100.0;
          componentsRow.percentHeight = 100.0;
          if(   componentCell.colSpan > 1
             || componentCell.colSpan == remoteForm.columnCount) {
            componentCell.maxWidth = NaN;
            component.maxWidth = NaN;
          }
        } else if(component.maxWidth > 0 && component.maxWidth < 1000) {
          component.percentWidth = 100.0;
          if((col + elementWidth < remoteForm.columnCount)
            || !(rComponent is RComboBox)) {
            componentCell.maxWidth = component.maxWidth;
            componentCell.width = component.maxWidth;
          } else {
            //Allow last cell to grow if it's a ComboBox
            component.percentWidth = 100.0;
          }
        }
        if(component.minWidth > 0) {
          componentCell.minWidth = component.minWidth;
        } else {
          component.minWidth = 0;
        }
        componentsRow.addChild(componentCell);
        componentCell.addChild(component);
        
        col += elementWidth;
      }
      
      // to deal with resizing problems
      var resizerRow:GridRow = new GridRow();
      resizerRow.percentWidth = 100.0;
      for(i = 0; i < remoteForm.columnCount; i++){
        var gi:GridItem = new GridItem();
        resizerRow.addChild(gi);
        if(remoteForm.labelsPosition == "ASIDE") {
          gi = new GridItem();
          gi.percentWidth = 100.0;
          resizerRow.addChild(gi);
        } else {
          gi.percentWidth = 100.0;
        }
      }
      form.addChild(resizerRow);
      
        
      form.setStyle("paddingLeft", 2);
      form.setStyle("paddingRight", 2);
      form.setStyle("paddingTop", 2);
      form.setStyle("paddingBottom", 2);
      return form;
    }

    protected function createSplitContainer(remoteSplitContainer:RSplitContainer):Container {
      var splitContainer:DividedBox = new DividedBox();
      splitContainer.resizeToContent = true;
      splitContainer.liveDragging = true;

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
        if(remoteSplitContainer.orientation == "VERTICAL") {
          component.percentWidth = 100.0;
        } else {
          component.percentHeight = 100.0;
        }

        splitContainer.addChild(component);
      }
      splitContainer.addEventListener(FlexEvent.CREATION_COMPLETE, function(event:FlexEvent):void {
        var splitC:DividedBox = event.currentTarget as DividedBox;
        var leftTop:UIComponent = (splitC.getChildAt(0) as UIComponent);
        var rightBottom:UIComponent = (splitC.getChildAt(1) as UIComponent);
        if(leftTop && rightBottom) {
          if(remoteSplitContainer.orientation == "VERTICAL") {
            if(leftTop.height > 0) {
              leftTop.percentHeight = leftTop.height;
            } else {
              leftTop.percentHeight = leftTop.measuredHeight;
            }
            if(rightBottom.height > 0) {
              rightBottom.percentHeight = rightBottom.height;
            } else {
              rightBottom.percentHeight = rightBottom.measuredHeight;
            }
          } else {
            if(leftTop.width > 0) {
              leftTop.percentWidth = leftTop.width;
            } else {
              leftTop.percentWidth = leftTop.measuredWidth;
            }
            if(rightBottom.width > 0) {
              rightBottom.percentWidth = rightBottom.width;
            } else {
              rightBottom.percentWidth = rightBottom.measuredWidth;
            }
          }
        }
      });
      return splitContainer;
    }

    protected function createTabContainer(remoteTabContainer:RTabContainer):Container {
      getRemotePeerRegistry().register(remoteTabContainer);
      var tabContainer:TabNavigator = new TabNavigator();
      tabContainer.historyManagementEnabled = false;
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
      BindingUtils.bindProperty(tabContainer, "selectedIndex", remoteTabContainer, "selectedIndex", true);
      BindingUtils.bindSetter(function(index:int):void {
        remoteTabContainer.selectedIndex = index;
        var command:RemoteSelectionCommand = new RemoteSelectionCommand();
        command.targetPeerGuid = remoteTabContainer.guid;
        command.permId = remoteTabContainer.permId;
        command.leadingIndex = index;
        _commandHandler.registerCommand(command);
      }, tabContainer, "selectedIndex",true);
      return tabContainer;
    }
    
    protected function createDateComponent(remoteDateField:RDateField):UIComponent {
      var dateComponent:UIComponent; 
      if(remoteDateField.type == "DATE_TIME") {
        dateComponent = createDateTimeField(remoteDateField);
      } else {
        dateComponent = createDateField(remoteDateField);
      }
      return dateComponent;
    }

    protected function createDateField(remoteDateField:RDateField):UIComponent {
      var dateField:DateField = new DateField();
      dateField.formatString = ResourceManager.getInstance().getString("Common_messages", "date_format");
      dateField.parseFunction = DateUtils.parseDate;
      dateField.editable = true;
      sizeMaxComponentWidth(dateField, remoteDateField, DATE_CHAR_COUNT);
      bindDateField(dateField, remoteDateField);
      return dateField;
    }

    protected function bindDateField(dateField:DateField, remoteDateField:RDateField):void {
      var remoteState:RemoteValueState = remoteDateField.state;
      var updateView:Function = function (value:Object):void {
        if(value == null) {
          dateField.selectedDate = null;
        } else {
          if(value is DateDto) {
            var valueAsDateDto:DateDto = value as DateDto;
            dateField.selectedDate = new Date(valueAsDateDto.year,
              valueAsDateDto.month,
              valueAsDateDto.date,
              valueAsDateDto.hour,
              valueAsDateDto.minute,
              valueAsDateDto.second
            );
          } else {
            dateField.selectedDate = value as Date;
          }
        }
      };
      BindingUtils.bindSetter(updateView, remoteState, "value", true);
      BindingUtils.bindProperty(dateField, "enabled", remoteState, "writable");
      var updateModel:Function = function (event:Event):void {
        if(dateField.text == "") {
          dateField.selectedDate = null;
          remoteState.value = null;
        } else {
          var processEvent:Boolean = true;
          if(event is FocusEvent) {
            var currentTarget:UIComponent = (event as FocusEvent).currentTarget as UIComponent;
            var relatedObject:DisplayObject = (event as FocusEvent).relatedObject as DisplayObject;
            
            if(currentTarget != dateField
              || dateField.contains(relatedObject)
              || dateField.dropdown.contains(relatedObject)) {
              // do not listen to inner focus events.
              processEvent = false;
            }
          }
          if(processEvent) {
            var parsedDate:Date = DateUtils.parseDate(dateField.text, dateField.formatString);
            var selectedDate:Date = dateField.selectedDate;
            if(ObjectUtil.compare(parsedDate,selectedDate) == 0) {
              if(remoteState.value) {
                var currentAsDate:Date;
                if(remoteState.value is DateDto) {
                  var currentAsDateDto:DateDto = remoteState.value as DateDto;
                  currentAsDate = new Date(currentAsDateDto.year,
                    currentAsDateDto.month,
                    currentAsDateDto.date,
                    currentAsDateDto.hour,
                    currentAsDateDto.minute,
                    currentAsDateDto.second
                  );
                } else {
                  currentAsDate = remoteState.value as Date;
                }
                // copy the existing time portion
                selectedDate.setHours(currentAsDate.getHours(), currentAsDate.getMinutes(), currentAsDate.getSeconds(), currentAsDate.getMilliseconds());
              }
              if(remoteDateField.timezoneAware) {
                remoteState.value = selectedDate;
              } else {
                var dateDto:DateDto = new DateDto();
                dateDto.year = selectedDate.fullYear;
                dateDto.month = selectedDate.month;
                dateDto.date = selectedDate.date;
                dateDto.hour = selectedDate.hours;
                dateDto.minute = selectedDate.minutes;
                dateDto.second = selectedDate.seconds;
                remoteState.value = dateDto;
              }
            } else {
              // rollback text update
              var ti:TextInput = (dateField.getChildAt(2) as TextInput);
              if(ti) {
                var valueAsDate:Date;
                if(remoteState.value is DateDto) {
                  var valueAsDateDto:DateDto = remoteState.value as DateDto;
                  valueAsDate = new Date(valueAsDateDto.year,
                    valueAsDateDto.month,
                    valueAsDateDto.date,
                    valueAsDateDto.hour,
                    valueAsDateDto.minute,
                    valueAsDateDto.second
                  );
                } else {
                  valueAsDate = remoteState.value as Date;
                }
                ti.text = DateField.dateToString(valueAsDate, dateField.formatString);
              }
            }
          }
        }
      };
      dateField.addEventListener(FlexEvent.ENTER,updateModel);
      dateField.addEventListener(FocusEvent.MOUSE_FOCUS_CHANGE,updateModel);
      dateField.addEventListener(FocusEvent.KEY_FOCUS_CHANGE,updateModel);
    }

    protected function createDateTimeField(remoteDateField:RDateField):UIComponent {
      var dateTimeField:HBox = new HBox();
      dateTimeField.horizontalScrollPolicy = ScrollPolicy.OFF;
      dateTimeField.verticalScrollPolicy = ScrollPolicy.OFF;
      dateTimeField.setStyle("horizontalGap",2);
      dateTimeField.setStyle("verticalGap",0);
      dateTimeField.setStyle("paddingTop",2);
      dateTimeField.setStyle("paddingBottom",2);
      dateTimeField.setStyle("paddingLeft",2);
      dateTimeField.setStyle("paddingRight",2);

      var dateField:UIComponent = createDateField(remoteDateField)
      dateField.percentWidth = 100.0;
      
      var remoteTimeField:RTimeField = new RTimeField();
      remoteTimeField.background = remoteDateField.background;
      remoteTimeField.borderType = remoteDateField.borderType;
      remoteTimeField.font = remoteDateField.font;
      remoteTimeField.foreground = remoteDateField.foreground;
      remoteTimeField.guid = remoteDateField.guid;
      remoteTimeField.state = remoteDateField.state;
      remoteTimeField.tooltip = remoteDateField.tooltip;
      
      var timeField:UIComponent = createComponent(remoteTimeField, false);
      timeField.percentWidth = 100.0;
      
      dateTimeField.addChild(dateField);
      dateTimeField.addChild(timeField);

      dateTimeField.maxWidth = dateField.maxWidth + timeField.maxWidth;
      
      return dateTimeField;
    }

    protected function createTimeField(remoteTimeField:RTimeField):UIComponent {
      var timeField:TextInput = new TextInput();
      sizeMaxComponentWidth(timeField, remoteTimeField, TIME_CHAR_COUNT);
      bindTextInput(timeField, remoteTimeField.state,
        createFormatter(remoteTimeField), createParser(remoteTimeField));
      return timeField;
    }

    protected function createDecimalField(remoteDecimalField:RDecimalField):UIComponent {
      var decimalField:TextInput = new TextInput();
      var decimalFormatter:NumberFormatter = createFormatter(remoteDecimalField) as NumberFormatter;
      bindTextInput(decimalField, remoteDecimalField.state,
                    decimalFormatter, createParser(remoteDecimalField));
      decimalField.restrict = "0-9" + decimalFormatter.decimalSeparatorTo + decimalFormatter.decimalSeparatorFrom;
      return decimalField;
    }

    protected function createIntegerField(remoteIntegerField:RIntegerField):UIComponent {
      var integerField:TextInput = new TextInput();
      var integerFormatter:NumberFormatter = createFormatter(remoteIntegerField) as NumberFormatter;
      bindTextInput(integerField, remoteIntegerField.state,
                    integerFormatter, createParser(remoteIntegerField));
      integerField.restrict = "0-9";
      return integerField;
    }

    protected function createPercentField(remotePercentField:RPercentField):UIComponent {
      var percentField:TextInput = new TextInput();
      var percentFormatter:NumberFormatter = createFormatter(remotePercentField) as NumberFormatter; 
      bindTextInput(percentField, remotePercentField.state,
                    percentFormatter, createParser(remotePercentField));
      percentField.restrict = "0-9" + percentFormatter.decimalSeparatorTo  + percentFormatter.decimalSeparatorFrom + PercentFormatter.PERCENT_SUFFIX;
      return percentField;
    }
    
    protected function createDurationField(remoteDurationField:RDurationField):UIComponent {
      var durationField:TextInput = new TextInput();
      bindTextInput(durationField, remoteDurationField.state,
                    createFormatter(remoteDurationField), createParser(remoteDurationField));
      return durationField;
    }

    protected function createList(remoteList:RList):List {
      var list:EnhancedList = new EnhancedList();
      list.horizontalScrollPolicy = ScrollPolicy.AUTO;
      list.verticalScrollPolicy = ScrollPolicy.AUTO;
      if(remoteList.selectionMode == "SINGLE_SELECTION") {
        list.allowMultipleSelection = false;
      } else {
        list.allowMultipleSelection = true;
        if(   remoteList.selectionMode == "SINGLE_INTERVAL_CUMULATIVE_SELECTION"
           || remoteList.selectionMode == "MULTIPLE_INTERVAL_CUMULATIVE_SELECTION") {
          list.cumulativeSelection = true;
        }
      }
      
      var itemRenderer:ClassFactory = new ClassFactory(RemoteValueListItemRenderer);
      itemRenderer.properties = {iconTemplate:_iconTemplate};
      //new ClassFactory(RemoteValueDgItemRenderer);
      list.itemRenderer = itemRenderer;

      list.dataProvider = (remoteList.state as RemoteCompositeValueState).children;
      bindList(list, remoteList.state as RemoteCompositeValueState);
      if(remoteList.rowAction) {
        getRemotePeerRegistry().register(remoteList.rowAction);
        list.doubleClickEnabled = true;
        list.addEventListener(ListEvent.ITEM_DOUBLE_CLICK, function(event:ListEvent):void {
          _actionHandler.execute(remoteList.rowAction);
        });
      }
      return list;
    }

    protected function bindList(list:List, state:RemoteCompositeValueState):void {
      BindingUtils.bindSetter(function(selectedItems:Array):void {
        if(selectedItems != null && selectedItems.length > 0) {
          // work on items to translate indices independently of list sorting state.
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
      }, list, "selectedItems", false);
      BindingUtils.bindSetter(function(selectedIndices:Array):void {
        if(selectedIndices != null && selectedIndices.length > 0) {
          // work on items to translate indices independently of list sorting state.
          var selectedItems:Array = new Array(selectedIndices.length);
          for(var i:int = 0; i < selectedIndices.length; i++) {
            if(selectedIndices[i] > -1) {
              if(state.children.length > selectedIndices[i]) {
                selectedItems[i] = state.children.getItemAt(selectedIndices[i]);
              } else {
                // there is a desynch that will be fixed by another selection command.
                return;
              }
            }
          }
          if(!ArrayUtil.areUnorderedArraysEqual(list.selectedItems, selectedItems)) {
            list.selectedItems = selectedItems;
          }
        } else {
          list.selectedItem = null;
        }
      }, state, "selectedIndices", true);
    }

    protected function createPasswordField(remotePasswordField:RPasswordField):UIComponent {
      var passwordField:TextInput = new TextInput();
      bindTextInput(passwordField, remotePasswordField.state);
      passwordField.displayAsPassword = true;
      sizeMaxComponentWidth(passwordField, remotePasswordField);
      return passwordField;
    }

    protected function createSecurityComponent(remoteSecurityComponent:RSecurityComponent):UIComponent {
      var securityComponent:Canvas = new Canvas();
      return securityComponent;
    }

    protected function createTable(remoteTable:RTable):UIComponent {
      var table:EnhancedDataGrid = new EnhancedDataGrid();
      table.showDataTips = true;
      
      var columns:Array = new Array();
      
      table.regenerateStyleCache(false);
      
      if(remoteTable.selectionMode == "SINGLE_SELECTION") {
        table.allowMultipleSelection = false;
      } else {
        table.allowMultipleSelection = true;
        if(   remoteTable.selectionMode == "SINGLE_INTERVAL_CUMULATIVE_SELECTION"
           || remoteTable.selectionMode == "MULTIPLE_INTERVAL_CUMULATIVE_SELECTION") {
          table.cbMultiSelection = true;
        }
        if(table.cbMultiSelection) {
          var selectionColumn:DataGridColumn = new DataGridColumn();
          selectionColumn.itemRenderer = new ClassFactory(SelectionCheckBoxRenderer);
          selectionColumn.width = 20;
          selectionColumn.sortable = false;
          selectionColumn.draggable = false;
          selectionColumn.dataField = "guid";
          selectionColumn.headerText = " ";
          columns.push(selectionColumn);
        }
      }
      
      for(var i:int=0; i < remoteTable.columns.length; i++) {
        var rColumn:RComponent = remoteTable.columns[i] as RComponent;
        if(rColumn.state == null) {
          rColumn.state = new RemoteValueState();
        }
        var editorComponent:UIComponent = createComponent(rColumn, false);

        var column:DataGridColumn = new DataGridColumn();
        column.headerText = rColumn.label;
        applyComponentStyle(column, rColumn);
        var itemRenderer:ClassFactory;
        if(rColumn is RComboBox) {
          var hasIcon:Boolean = false;
          for each(var icon:RIcon in (rColumn as RComboBox).icons) {
            if(icon) {
              hasIcon = true;
              break;
            }
          }
          itemRenderer = new ClassFactory(EnumerationDgItemRenderer);
          itemRenderer.properties = {values:(rColumn as RComboBox).values,
                                     labels:(rColumn as RComboBox).translations,
                                     icons :(rColumn as RComboBox).icons,
                                     iconTemplate:_iconTemplate,
                                     showIcon:hasIcon,
                                     index:i+1};
        } else if( rColumn is RCheckBox
               || (rColumn is RActionField && !(rColumn as RActionField).showTextField)) {
          itemRenderer = new ClassFactory(UIComponentDgItemRenderer);
          itemRenderer.properties = {viewFactory:this,
                                     remoteComponent:rColumn,
                                     index:i+1};
        } else {
          var columnAction:RAction = null;
          if(rColumn is RLink) {
            columnAction = (rColumn as RLink).action;
            getRemotePeerRegistry().register(columnAction);
          }
          var alignment:String = "left";
          if (rColumn is RLabel) {
            alignment = (rColumn as RLabel).horizontalAlignment;
          } else if (rColumn is RTextField) {
            alignment = (rColumn as RTextField).horizontalAlignment;
          } else if (rColumn is RNumericComponent) {
            alignment = (rColumn as RNumericComponent).horizontalAlignment;
          }
          if(alignment == "LEFT") {
            alignment = "left";
          } else if(alignment == "CENTER") {
            alignment = "center";
          } else if(alignment == "RIGHT") {
            alignment = "right";
          }
          column.setStyle("textAlign", alignment);
          itemRenderer = new ClassFactory(RemoteValueDgItemRenderer);
          itemRenderer.properties = {formatter:createFormatter(rColumn),
                                     index:i+1,
                                     action:columnAction,
                                     actionHandler:getActionHandler()};
        }
        column.itemRenderer = itemRenderer
        
        var itemEditor:ClassFactory = new ClassFactory(RemoteValueDgItemEditor);
        rColumn.state.writable = true;
        itemEditor.properties = {editor:editorComponent,
          state:rColumn.state,
            index:i+1};
        column.itemEditor = itemEditor;

        var headerRenderer:ClassFactory = new ClassFactory(DgHeaderItemRenderer);
        headerRenderer.properties = {index:i+1, toolTip:editorComponent.toolTip};
        column.headerRenderer = headerRenderer;
        
        if(rColumn.preferredSize != null && rColumn.preferredSize.width > 0) {
          column.width = rColumn.preferredSize.width;
        } else {
          if(rColumn is RCheckBox) {
            column.width = table.measureText(column.headerText).width + 16;
          } else {
            column.width = Math.max(
                             Math.min(table.measureText(TEMPLATE_CHAR).width * COLUMN_MAX_CHAR_COUNT,
                                      editorComponent.maxWidth),
                             table.measureText(column.headerText).width + 16
                           );
          }
        }
        editorComponent.maxWidth = UIComponent.DEFAULT_MAX_WIDTH;
        column.editorDataField = "state";
        
        if(remoteTable.sortable && !remoteTable.sortingAction) {
          if(rColumn is RCheckBox) {
            column.sortCompareFunction = _remoteValueSorter.compareBooleans;
          } else if(rColumn is RNumericComponent) {
            column.sortCompareFunction = _remoteValueSorter.compareNumbers;
          } else if(rColumn is RDateField) {
            column.sortCompareFunction = _remoteValueSorter.compareDates;
          } else if(rColumn.state is RemoteFormattedValueState) {
            column.sortCompareFunction = _remoteValueSorter.compareFormatted;
          } else {
            column.sortCompareFunction = _remoteValueSorter.compareStrings;
          }
        }
        columns.push(column);
      }
      if(remoteTable.sortable) {
        if(remoteTable.sortingAction) {
          table.customSort = true;
        }
      } else {
        table.sortableColumns = false;
      }

      table.columns = columns;
      if(remoteTable.horizontallyScrollable) {
        table.horizontalScrollPolicy = ScrollPolicy.AUTO;
      } else {
        table.horizontalScrollPolicy = ScrollPolicy.OFF;
      }
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
        getRemotePeerRegistry().register(remoteTable.rowAction);
        table.doubleClickEnabled = true;
        table.addEventListener(ListEvent.ITEM_DOUBLE_CLICK, function(event:ListEvent):void {
          _actionHandler.execute(remoteTable.rowAction);
        });
      }
      if(remoteTable.permId) {
        var notifyTableChanged:Function = function(e:Event):void {
          var notificationCommand:RemoteTableChangedCommand = new RemoteTableChangedCommand();
          notificationCommand.tableId = remoteTable.permId;
          var columnIds:Array = new Array();
          var columnWidths:Array = new Array();
          for each (var dgColumn:DataGridColumn in table.columns) {
            var columnRenderer:ClassFactory = dgColumn.itemRenderer as ClassFactory;
            // watch out checkbox selection column...
            if(columnRenderer.properties && columnRenderer.properties["index"]) {
              columnIds.push(remoteTable.columnIds[(columnRenderer.properties["index"] as int) - 1]);
              columnWidths.push(dgColumn.width);
            }
          }
          notificationCommand.columnIds = columnIds;
          notificationCommand.columnWidths = columnWidths;
          _commandHandler.registerCommand(notificationCommand);
        };
        
        table.addEventListener(DataGridEvent.COLUMN_STRETCH, notifyTableChanged); 
        table.addEventListener(IndexChangedEvent.HEADER_SHIFT, notifyTableChanged); 
      }
      return table;
    }
    
    protected function bindTable(table:EnhancedDataGrid, remoteTable:RTable):void {
      var state:RemoteCompositeValueState = remoteTable.state as RemoteCompositeValueState;
      BindingUtils.bindProperty(table, "editable", state, "writable");
      if(remoteTable.sortable) {
        if(remoteTable.sortingAction) {
          table.addEventListener(DataGridEvent.HEADER_RELEASE, function(event:DataGridEvent):void {
            event.preventDefault();
            var column:DataGridColumn = table.columns[event.columnIndex];
            var property:String;
            var columnRenderer:ClassFactory = column.itemRenderer as ClassFactory;
            // watch out checkbox selection column...
            if(columnRenderer.properties && columnRenderer.properties["index"]) {
              property = remoteTable.columnIds[(columnRenderer.properties["index"] as int) - 1];
            }
            if(!property || property.length == 0 || property.charAt(0) == "#") {
              // do not sort
              return;
            }
            column.sortDescending = !column.sortDescending;
            table.displaySort(event.columnIndex, column.sortDescending);
            if(state.children.length > 1) {
              var orderingProperties:Object = new Object();
              orderingProperties[property] = column.sortDescending ? "DESCENDING" : "ASCENDING";
              var sortCommand:RemoteSortCommand = new RemoteSortCommand();
              sortCommand.orderingProperties = orderingProperties;
              sortCommand.viewStateGuid = remoteTable.state.guid;
              sortCommand.viewStatePermId = remoteTable.state.permId;
              sortCommand.targetPeerGuid = remoteTable.sortingAction.guid;
              sortCommand.permId = remoteTable.sortingAction.permId;
              _commandHandler.registerCommand(sortCommand);
            }
          });
        } else {
          table.addEventListener(DataGridEvent.HEADER_RELEASE, function(event:DataGridEvent):void {
            _remoteValueSorter.sortColumnIndex = (event.itemRenderer as DgHeaderItemRenderer).index;
          });
        }
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
              if(state.children.length > selectedIndices[i]) {
                selectedItems[i] = state.children.getItemAt(selectedIndices[i]);
              } else {
                // there is a desynch that will be fixed by another selection command.
                return;
              }
            }
          }
          if(!ArrayUtil.areUnorderedArraysEqual(table.selectedItems, selectedItems)) {
            table.selectedItems = selectedItems;
          }
        } else {
          table.selectedItem = null;
        }
      }, state, "selectedIndices", true);
      
      table.addEventListener(DataGridEvent.ITEM_EDIT_END, function(event:DataGridEvent):void {
        var table:DataGrid = event.currentTarget as DataGrid;
    	  _actionHandler.setCurrentViewStateGuid(table, null, null);
        if (event.reason != DataGridEventReason.CANCELLED) {
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
        if(event.itemRenderer is SelectionCheckBoxRenderer) {
          event.preventDefault();
          return;
        }
        var dg:DataGrid = event.currentTarget as DataGrid;
        var column:DataGridColumn = dg.columns[event.columnIndex]; 
        var rowCollection:ArrayCollection = dg.dataProvider as ArrayCollection;
        var rowValueState:RemoteCompositeValueState = rowCollection[event.rowIndex] as RemoteCompositeValueState;
        if(!rowValueState.writable) {
          event.preventDefault();
        } else {
          var cellValueState:RemoteValueState;
          var columnRenderer:ClassFactory = column.itemRenderer as ClassFactory;
          // watch out checkbox selection column...
          if(columnRenderer.properties && columnRenderer.properties["index"]) {
            cellValueState = rowValueState
              .children[columnRenderer.properties["index"] as int] as RemoteValueState;
            if(!cellValueState.writable) {
        	    event.preventDefault();
        	  }
          }
        }
    	});
      table.addEventListener(DataGridEvent.ITEM_EDIT_BEGIN, function(event:DataGridEvent):void {
        var dg:DataGrid = event.currentTarget as DataGrid;
        var column:DataGridColumn = dg.columns[event.columnIndex]; 
        var rowCollection:ArrayCollection = dg.dataProvider as ArrayCollection;
        var cellValueState:RemoteValueState;
        var columnRenderer:ClassFactory = column.itemRenderer as ClassFactory;
        // watch out checkbox selection column...
        if(columnRenderer.properties && columnRenderer.properties["index"]) {
          cellValueState = (rowCollection[event.rowIndex] as RemoteCompositeValueState)
            .children[columnRenderer.properties["index"] as int] as RemoteValueState;
          _actionHandler.setCurrentViewStateGuid(dg, cellValueState.guid, cellValueState.permId);
        }
    	});
      table.addEventListener(DataGridEvent.ITEM_FOCUS_IN, function(event:DataGridEvent):void {
        ((event.currentTarget as DataGrid).itemEditorInstance as UIComponent).setFocus();
      });
    }

    protected function createTextArea(remoteTextArea:RTextArea):UIComponent {
      var textArea:TextArea = new TextArea();
      if(remoteTextArea.maxLength > 0) {
        textArea.maxChars = remoteTextArea.maxLength;
      }
      bindTextArea(textArea, remoteTextArea.state);
      sizeMaxComponentWidth(textArea, remoteTextArea);
      return textArea;
    }

    protected function bindTextArea(textArea:TextArea, remoteState:RemoteValueState):void {
      BindingUtils.bindProperty(textArea, "text", remoteState, "value", true);
      BindingUtils.bindProperty(textArea, "editable", remoteState, "writable");
      var updateModel:Function = function (event:Event):void {
        remoteState.value = (event.currentTarget as TextArea).text;
      };
      textArea.addEventListener(FocusEvent.MOUSE_FOCUS_CHANGE,updateModel);
      textArea.addEventListener(FocusEvent.KEY_FOCUS_CHANGE,updateModel);
    }

    protected function createHtmlArea(remoteHtmlArea:RHtmlArea):UIComponent {
      var htmlComponent:UIComponent;
      if(remoteHtmlArea.readOnly) {
        htmlComponent = createHtmlText(remoteHtmlArea);
      } else {
        htmlComponent = createHtmlEditor(remoteHtmlArea);
      }
      return htmlComponent;
    }

    protected function createHtmlEditor(remoteHtmlArea:RHtmlArea):UIComponent {
      var htmlEditor:EnhancedRichTextEditor = new EnhancedRichTextEditor();
      htmlEditor.setStyle("headerHeight",0);
      bindHtmlEditor(htmlEditor, remoteHtmlArea.state);
      return htmlEditor;
    }
    
    protected function bindHtmlEditor(htmlEditor:EnhancedRichTextEditor, remoteState:RemoteValueState):void {
      BindingUtils.bindProperty(htmlEditor, "xhtmlText", remoteState, "value", true);
      BindingUtils.bindProperty(htmlEditor, "enabled", remoteState, "writable");
      var updateModel:Function = function (event:Event):void {
        remoteState.value = (event.currentTarget as EnhancedRichTextEditor).xhtmlText;
      };
      htmlEditor.addEventListener(FocusEvent.MOUSE_FOCUS_CHANGE,updateModel);
      htmlEditor.addEventListener(FocusEvent.KEY_FOCUS_CHANGE,updateModel);
      //htmlEditor.addEventListener(Event.CHANGE,updateModel);
    }

    protected function createHtmlText(remoteHtmlArea:RHtmlArea):UIComponent {
      var htmlText:TextArea = new TextArea();
      htmlText.editable = false;
      htmlText.setStyle("borderStyle","none");
      htmlText.setStyle("focusAlpha",0);
      bindHtmlText(htmlText, remoteHtmlArea.state);
      return htmlText;
    }

    protected function bindHtmlText(htmlText:TextArea, remoteState:RemoteValueState):void {
      var updateText:Function = function (value:Object):void {
        if(value == null) {
          htmlText.htmlText = null;
        } else {
          htmlText.htmlText = HtmlUtil.convertFromXHtml(value.toString());
        }
      };
      BindingUtils.bindSetter(updateText, remoteState, "value", true);
    }

    protected function createLabel(remoteLabel:RLabel):UIComponent {
      var label:Label;
      if(remoteLabel.state) {
        label = new Text();
      } else {
        label = new Label();
      }
      if(!remoteLabel.state && remoteLabel.label) {
        if(HtmlUtil.isHtml(remoteLabel.label)) {
          label.text = null;
          label.htmlText = HtmlUtil.convertHtmlEntities(remoteLabel.label);
        } else {
          label.htmlText = null;
          label.text = remoteLabel.label;
          if(label.text != null) {
            sizeMaxComponentWidth(label, remoteLabel, label.text.length + 5);
          }
        }
      }
      configureHorizontalAlignment(label, remoteLabel.horizontalAlignment);
      if(remoteLabel.state) {
        bindLabel(label, remoteLabel);
      }
      return label;
    }

    protected function bindLabel(label:Label, remoteLabel:RLabel):void {
      var remoteState:RemoteValueState = remoteLabel.state;
      var updateLabel:Function;
      if(remoteLabel is RLink && (remoteLabel as RLink).action != null) {
        getRemotePeerRegistry().register((remoteLabel as RLink).action);
        updateLabel = function (value:Object):void {
          if(value == null) {
            label.htmlText = null;
          } else {
            label.htmlText = "<u><a href='event:action'>" + value.toString() + "</a></u>";
          }
        };
        label.selectable = true;
        label.addEventListener("link", function(evt:TextEvent):void {
          getActionHandler().execute((remoteLabel as RLink).action);
        });
      } else {
         updateLabel = function (value:Object):void {
          if(value == null) {
            label.text = null;
            label.htmlText = null;
          } else {
            if(HtmlUtil.isHtml(value.toString())) {
              label.text = null;
              label.htmlText = HtmlUtil.convertHtmlEntities(value.toString());
            } else {
              label.htmlText = null;
              label.text = value.toString();
            }
          }
        };
      }
      BindingUtils.bindSetter(updateLabel, remoteState, "value", true);
    }
    
    protected function createTextField(remoteTextField:RTextField):UIComponent {
      var textField:TextInput = new TextInput();
      if(remoteTextField.maxLength > 0) {
        textField.maxChars = remoteTextField.maxLength;
        sizeMaxComponentWidth(textField, remoteTextField, remoteTextField.maxLength +2);
      } else {
        sizeMaxComponentWidth(textField, remoteTextField);
      }
      configureHorizontalAlignment(textField, remoteTextField.horizontalAlignment);
      bindTextInput(textField, remoteTextField.state);
      return textField;
    }

    public function createAction(remoteAction:RAction):Button {
      var button:Button = createButton(remoteAction.name, remoteAction.description, remoteAction.icon);
		  //BindingUtils.bindProperty(button, "enabled", remoteAction, "enabled", true);
      var updateButtonState:Function = function (enabled:Boolean):void {
        button.enabled = enabled;
      };
      BindingUtils.bindSetter(updateButtonState, remoteAction, "enabled", true);
		  getRemotePeerRegistry().register(remoteAction);
		  button.addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {
        if ((new Date()).time - _lastActionTimestamp.time < 400) {
          return;
        }
        _lastActionTimestamp = new Date();
        _actionHandler.execute(remoteAction);
		  });
      return button;
    }
    
    public function createButton(label:String, tooltip:String, icon:RIcon):Button {
      var button:Button = new EnhancedButton();
      configureButton(button, label, tooltip, icon);
      return button;
    }
    
    protected function configureButton(button:Button, label:String, tooltip:String, icon:RIcon):void {
      button.setStyle("icon", null);
      if(icon) {
        button.setStyle("icon", getIconForComponent(button, icon));
      }
      if(label) {
        button.label = label;
      }
      if(tooltip) {
        button.toolTip = tooltip + TOOLTIP_ELLIPSIS;
      }
    }

    protected function bindTextInput(textInput:TextInput, remoteState:RemoteValueState,
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
        var inputText:String = textInput.text;
        if(inputText == null || inputText.length == 0) {
          remoteState.value = null;
        } else {
          if(parser != null) {
            remoteState.value = parser.parse(inputText, remoteState.value);
            textInput.text = formatter.format(remoteState.value);
          } else {
            remoteState.value = inputText;
          }
        }
      };
      textInput.addEventListener(FlexEvent.ENTER,updateModel);
      textInput.addEventListener(FocusEvent.MOUSE_FOCUS_CHANGE,updateModel);
      textInput.addEventListener(FocusEvent.KEY_FOCUS_CHANGE,updateModel);
    }
    
    protected function bindColorPicker(colorPicker:ColorPicker, remoteState:RemoteValueState):void {
      BindingUtils.bindProperty(colorPicker, "selectedColor", remoteState, "value", true);
      BindingUtils.bindProperty(colorPicker, "enabled", remoteState, "writable");
      var updateModel:Function = function (event:Event):void {
        var currentAlpha:String;
        if(remoteState.value != null) {
          currentAlpha = (remoteState.value as String).substr(2,2);
        } else {
          currentAlpha = "FF";
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
    
    protected function createFormatter(remoteComponent:RComponent):Formatter {
      if(remoteComponent is RDateField) {
        var dateFormatter:DateFormatter = new DateFormatter();
        dateFormatter.formatString = ResourceManager.getInstance().getString("Common_messages", "date_format");
        if((remoteComponent as RDateField).type == "DATE_TIME") {
          dateFormatter.formatString = dateFormatter.formatString + " " + _timeFormatter.formatString; 
        }
        return dateFormatter;
      } else if(remoteComponent is RTimeField) {
        return _timeFormatter;
      } else if(remoteComponent is RPasswordField) {
        return _passwordFormatter;
      } else if(remoteComponent is RNumericComponent) {
        var numberFormatter:NumberFormatter
        if(remoteComponent is RPercentField) {
          numberFormatter = new PercentFormatter();
        } else {
          numberFormatter = new NumberFormatter();
        }
        numberFormatter.decimalSeparatorFrom = ".";
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

    protected function createParser(remoteComponent:RComponent):Parser {
      if(remoteComponent is RNumericComponent) {
        var numberParser:NumberParser;
        if(remoteComponent is RPercentField) {
          numberParser = new PercentParser();
        } else {
          numberParser = new NumberParser();
        }
        var formatter:NumberFormatter = createFormatter(remoteComponent) as NumberFormatter;
        var numberBase:NumberBase = new NumberBase(formatter.decimalSeparatorFrom,
                                                   formatter.thousandsSeparatorFrom,
                                                   formatter.decimalSeparatorTo,
                                                   formatter.thousandsSeparatorTo);
        numberParser.numberBase = numberBase;
        numberParser.precision = formatter.precision as uint;
        return numberParser;
      } else if(remoteComponent is RTimeField) {
        var timeParser:TimeParser = new TimeParser();
        return timeParser;
      }
      return null;
    }
    
    protected function sizeMaxComponentWidth(component:UIComponent, remoteComponent:RComponent, expectedCharCount:int=FIELD_MAX_CHAR_COUNT, maxCharCount:int=FIELD_MAX_CHAR_COUNT):void {
      var w:int;
      applyComponentStyle(component, remoteComponent);
      component.regenerateStyleCache(false);
      var charCount:int = maxCharCount;
      if(expectedCharCount < charCount) {
        charCount = expectedCharCount;
      }
      charCount += 2;
      w = component.measureText(TEMPLATE_CHAR).width * charCount;
      if(remoteComponent.preferredSize && remoteComponent.preferredSize.width > w) {
        w = remoteComponent.preferredSize.width;
      }
      component.maxWidth = w;
    }
    
    public function get iconTemplate():Class  {
      return _iconTemplate;
    }

    public function createMenus(actionLists:Array, useSeparator:Boolean):Array {
      var menus:Array = new Array();
      if(actionLists != null) {
        var menu:Object;
        for each (var actionList:RActionList in actionLists) {
          if (!useSeparator || menus.length == 0) {
            menu = createMenu(actionList);
            menus.push(menu);
          } else {
            var separator:Object = new Object();
            separator["type"] = "separator";
            menu["children"].push(separator);
            for each (var menuItem:Object in createMenuItems(actionList)) {
              menu["children"].push(menuItem);
            }
          }
        }
      }
      return menus;
    }

    public function createPopupButton(actionList:RActionList):Button {
      if(actionList.actions == null || actionList.actions.length == 0) {
        return null;
      }
      if(actionList.actions.length == 1) {
        return createAction(actionList.actions[0]);
      }
      var dp:Object = createMenuItems(actionList);
      var menu:Menu = new Menu();
      menu.dataProvider = dp;
      menu.itemRenderer = new ClassFactory(RIconMenuItemRenderer);
      var popupButton:PopUpButton = new PopUpButton();
      popupButton.popUp = menu;
      var menuHandler:Function = function(event:MenuEvent):void  {
        if (event.item["data"] is RAction) {
          var action:RAction = event.item["data"] as RAction;
          getActionHandler().execute(action, null);
        }        
      };
      var defaultAction:RAction = actionList.actions[0];
      configureButton(popupButton, defaultAction.name, defaultAction.description, defaultAction.icon);
      popupButton.data = defaultAction;
      popupButton.addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {
        getActionHandler().execute((event.currentTarget as PopUpButton).data as RAction);
      });
      menu.addEventListener(MenuEvent.ITEM_CLICK, menuHandler);
      return popupButton;
    }
    
    public function createMenu(actionList:RActionList):Object {
      var menu:Object = new Object();
      menu["label"] = actionList.name;
      menu["description"] = actionList.description;
      menu["data"] = actionList;
      if(actionList.icon) {
        menu["icon"] = iconTemplate;
        menu["rIcon"] = actionList.icon;
      }
      
      var menuItems:Array = new Array();
      for each (var menuItem:Object in createMenuItems(actionList)) {
        menuItems.push(menuItem);
      }
      menu["children"] = menuItems;
      return menu;
    }
    
    protected function createMenuItems(actionList:RActionList):Array {
      var menuItems:Array = new Array();
      for each(var action:RAction in actionList.actions) {
        menuItems.push(createMenuItem(action));
      }
      return menuItems;
    }
    
    protected function createMenuItem(action:RAction):Object {
      var menuItem:Object = new Object();
      menuItem["label"] = action.name;
      menuItem["description"] = action.description;
      menuItem["data"] = action;
      if(action.icon) {
        menuItem["icon"] = iconTemplate;
        menuItem["rIcon"] = action.icon;
      }
      var updateMenuItemState:Function = function (enabled:Boolean):void {
        menuItem["enabled"] = enabled;
      };
      BindingUtils.bindSetter(updateMenuItemState, action, "enabled", true);
      _remotePeerRegistry.register(action);
      return menuItem;
    }
    
    protected function configureHorizontalAlignment(component:UIComponent, alignment:String):void {
      if(alignment == "LEFT") {
        component.setStyle("textAlign","left");
      } else if(alignment == "CENTER") {
        component.setStyle("textAlign","center");
      } else if(alignment == "RIGHT") {
        component.setStyle("textAlign","right");
      }
    }
  }
}