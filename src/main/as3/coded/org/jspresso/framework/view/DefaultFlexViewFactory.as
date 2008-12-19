package org.jspresso.framework.view {
  import com.benstucki.utilities.IconUtility;
  
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
  import mx.controls.ComboBox;
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
  import mx.core.Application;
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
  import mx.formatters.DateFormatter;
  import mx.formatters.Formatter;
  
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
  import org.jspresso.framework.util.gui.CellConstraints;
  
  public class DefaultFlexViewFactory {

   [Embed(source="mx/controls/Image.png")]
   private var _iconTemplate:Class;

   private static const TOOLTIP_ELLIPSIS:String = "...";

    private var _remoteValueSorter:RemoteValueSorter;

    public function DefaultFlexViewFactory() {
      _remoteValueSorter= new RemoteValueSorter();
    }
    
    public function createComponent(remoteComponent:RComponent):UIComponent {
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
        component = createDateField(remoteComponent as RDateField);
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
      component.id = remoteComponent.guid;
      if(!(remoteComponent is RActionField) && remoteComponent.actionLists != null) {
        var toolBar:ApplicationControlBar = new ApplicationControlBar();
        toolBar.percentWidth = 100.0;
        toolBar.setStyle("fillAlphas",[0.5,0.5]);
        toolBar.setStyle("fillColors",[0xBBBBBB,0x666666]);
        for(var i:int = 0; i < remoteComponent.actionLists.length; i++) {
          var actionList:RActionList = remoteComponent.actionLists[i] as RActionList;
          if(actionList.actions != null) {
            for(var j:int = 0; j < actionList.actions.length; j++) {
              toolBar.addChild(createButton(actionList.actions[j]));
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
      return component;
    }
    
    private function createContainer(remoteContainer:RContainer):Container {
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
      return container;
    }

    private function createNumericComponent(remoteNumericComponent:RNumericComponent):UIComponent {
      var numericComponent:UIComponent;
      if(remoteNumericComponent is RDecimalComponent) {
        numericComponent = createDecimalComponent(remoteNumericComponent as RDecimalComponent);
      } else if(remoteNumericComponent is RIntegerField) {
        numericComponent = createIntegerField(remoteNumericComponent as RIntegerField);
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
      } else if(remoteTextComponent is RTextComponent) {
        textComponent = createTextField(remoteTextComponent as RTextField);
      }
      return textComponent;
    }

    private function createTree(remoteTree:RTree):Tree {
      var tree:Tree = new Tree();
      bindTree(tree, remoteTree.state as RemoteCompositeValueState); 
      tree.labelField = "value";
      tree.dataTipField = "description";
      tree.itemRenderer = new ClassFactory(RemoteValueTreeItemRenderer);
      tree.dataProvider = remoteTree.state;
      return tree;
    }

    private function bindTree(tree:Tree, rootState:RemoteCompositeValueState):void {
      var updateModel:Function = function (selectedItems:Array):void {
        clearStateSelection(rootState);
        for(var i:int=0; i < selectedItems.length; i++) {
          var node:RemoteValueState = selectedItems[i] as RemoteValueState;
          var parentNode:RemoteCompositeValueState = tree.getParentItem(node);
          if(parentNode.selectedIndices == null) {
            parentNode.selectedIndices = new Array();
          }
          var childIndex:int = parentNode.children.getItemIndex(node);
          parentNode.selectedIndices.push(childIndex);
          parentNode.leadingIndex = childIndex;
        }
      };
      BindingUtils.bindSetter(updateModel, tree, "selectedItems");
    }
    
    private function clearStateSelection(remoteState:RemoteCompositeValueState):void {
      remoteState.selectedIndices = null
      remoteState.leadingIndex = -1;
      if(remoteState.children != null) {
        for(var i:int = 0; i < remoteState.children.length; i++) {
          if(remoteState.children[i] is RemoteCompositeValueState) {
            clearStateSelection(remoteState.children[i] as RemoteCompositeValueState);
          }
        }
      }
    }

    private function createImageComponent(remoteImageComponent:RImageComponent):Image {
      var imageComponent:Image = new Image();
      return imageComponent;
    }
    
    private function createActionField(remoteActionField:RActionField):HBox {
      var actionField:HBox = new HBox();
      var textField:TextInput = new TextInput();
      bindTextInput(textField, remoteActionField.state);
      actionField.percentWidth = 100.0;
      textField.percentWidth = 100.0;
      textField.name = "tf";
      actionField.addChild(textField);
      for(var i:int = 0; i < remoteActionField.actionLists.length; i++) {
        var actionList:RActionList = remoteActionField.actionLists[i] as RActionList;
        for(var j:int = 0; j < actionList.actions.length; j++) {
          actionField.addChild(createButton(actionList.actions[i]));
        }
      }
      var focusIn:Function = function(event:FocusEvent):void {
        var tf:UIComponent = (event.currentTarget as Container).getChildByName("tf") as UIComponent;
        tf.setFocus();
      };
      actionField.addEventListener(FocusEvent.FOCUS_IN, focusIn);
      //actionField.addEventListener(FocusEvent.MOUSE_FOCUS_CHANGE, focusIn);
      return actionField;
    }
    
    private function createColorField(remoteColorField:RColorField):HBox {
      var colorField:HBox = new HBox();
      var colorPicker:ColorPicker = new ColorPicker();
      colorPicker.name = "cc";
      bindColorPicker(colorPicker, remoteColorField.state);
      colorField.addChild(colorPicker);
      var resetButton:Button = new Button();
	    resetButton.setStyle("icon", IconUtility.getClass(resetButton
	                               , computeUrl("classpath:org/jspresso/framework/application/images/reset-48x48.png") 
                                 , 16, 16));
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
    
    private function createCheckBox(remoteCheckBox:RCheckBox):CheckBox {
      var checkBox:CheckBox = new CheckBox();
      bindCheckBox(checkBox, remoteCheckBox.state);
      return checkBox;
    }

    private function bindCheckBox(checkBox:CheckBox, remoteState:RemoteValueState):void {
      BindingUtils.bindProperty(checkBox, "selected", remoteState, "value");
      BindingUtils.bindProperty(remoteState, "value", checkBox, "selected");
    }

    private function createComboBox(remoteComboBox:RComboBox):ComboBox {
      var comboBox:RIconComboBox = new RIconComboBox();
      comboBox.dataProvider = remoteComboBox.values;
      comboBox.labels = remoteComboBox.translations;
      comboBox.icons = remoteComboBox.icons;
      bindComboBox(comboBox, remoteComboBox);

      var itemRenderer:ClassFactory = new ClassFactory(RIconListItemRenderer);
      itemRenderer.properties = {labels:remoteComboBox.translations, icons:remoteComboBox.icons, iconTemplate:_iconTemplate};
      comboBox.itemRenderer = itemRenderer;
      
      return comboBox;
    }

    private function bindComboBox(comboBox:RIconComboBox, remoteComboBox:RComboBox):void {
      BindingUtils.bindProperty(comboBox, "selectedItem", remoteComboBox.state, "value");
      BindingUtils.bindProperty(remoteComboBox.state, "value", comboBox, "selectedItem");
    }

    private function createBorderContainer(remoteBorderContainer:RBorderContainer):Grid {
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

    private function createCardContainer(remoteCardContainer:RCardContainer):ViewStack {
      var cardContainer:ViewStack = new ViewStack();
      cardContainer.resizeToContent = true;
      
      for(var i:int = 0; i < remoteCardContainer.cardNames.length; i++) {
        var cardCanvas:Canvas = new Canvas();
        cardCanvas.percentWidth = 100.0;
        cardCanvas.percentHeight = 100.0;
        cardCanvas.id = remoteCardContainer.cardNames[i] as String;
        cardContainer.addChild(cardCanvas);

        var cardComponent:UIComponent = createComponent(remoteCardContainer.cards[i] as RComponent);
        cardComponent.percentWidth = 100.0;
        cardComponent.percentHeight = 100.0;
        cardCanvas.addChild(cardComponent);
      }
      return cardContainer;
    }

    private function createConstrainedGridContainer(remoteConstrainedGridContainer:RConstrainedGridContainer):Grid {
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
      for(i = 0; j < constrainedGridContainer.getChildren().length; i++) {
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

    private function createEvenGridContainer(remoteEvenGridContainer:REvenGridContainer):Grid {
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

    private function createForm(remoteForm:RForm):Grid {
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
      } else {
        labelsRow = componentsRow;
      }
      form.addChild(componentsRow);
      for(var i:int = 0; i < remoteForm.elements.length; i++) {
        var elementWidth:int = remoteForm.elementWidths[i] as int;
        var rComponent:RComponent = remoteForm.elements[i] as RComponent;
        var component:UIComponent = createComponent(rComponent);
        var componentLabel:Label = new Label();
        var labelCell:GridItem = new GridItem();
        var componentCell:GridItem = new GridItem();

        componentLabel.text = rComponent.label;

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
          } else {
            labelsRow = componentsRow;
          }
          form.addChild(componentsRow);
          col = 0;
        }
        
        if(remoteForm.labelsPosition == "ABOVE") {
          labelCell.colSpan = elementWidth;
          componentCell.colSpan = elementWidth;
        } else {
          labelCell.setStyle("horizontalAlign","right");
          componentCell.colSpan = (elementWidth * 2) - 1;
        }

        if(   rComponent is RTable
           || rComponent is RTextArea
           || rComponent is RList) {
          componentsRow.percentHeight = 100.0;         
        }   

        labelsRow.addChild(labelCell);

        componentCell.percentWidth=100.0;
        componentCell.percentHeight=100.0;
        componentsRow.addChild(componentCell);
        
        labelCell.addChild(componentLabel);

        component.percentWidth = 100.0;
        component.percentHeight = 100.0;
        componentCell.addChild(component);
        
        col += elementWidth;
      }
      return form;
    }

    private function createSplitContainer(remoteSplitContainer:RSplitContainer):DividedBox {
      var splitContainer:DividedBox = new DividedBox();
      var component:UIComponent;
      if(remoteSplitContainer.orientation == "VERTICAL") {
        splitContainer.direction = BoxDirection.VERTICAL;
      } else {
        splitContainer.direction = BoxDirection.HORIZONTAL;
      }
      if(remoteSplitContainer.leftTop != null) {
        component = createComponent(remoteSplitContainer.leftTop);
        component.percentWidth = 100.0;
        component.percentHeight = 100.0;
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

    private function createTabContainer(remoteTabContainer:RTabContainer):TabNavigator {
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

    private function createDateField(remoteDateField:RDateField):DateField {
      var dateField:DateField = new DateField();
      dateField.editable = true;
      bindDateField(dateField, remoteDateField.state);
      return dateField;
    }

    private function bindDateField(dateField:DateField, remoteState:RemoteValueState):void {
      BindingUtils.bindProperty(dateField, "selectedDate", remoteState, "value");
      BindingUtils.bindProperty(remoteState, "value", dateField, "selectedDate");
    }

    private function createDecimalField(remoteDecimalField:RDecimalField):TextInput {
      var decimalField:TextInput = new TextInput();
      bindTextInput(decimalField, remoteDecimalField.state);
      return decimalField;
    }

    private function createIntegerField(remoteIntegerField:RIntegerField):TextInput {
      var integerField:TextInput = new TextInput();
      bindTextInput(integerField, remoteIntegerField.state);
      return integerField;
    }

    private function createPercentField(remotePercentField:RPercentField):TextInput {
      var percentField:TextInput = new TextInput();
      bindTextInput(percentField, remotePercentField.state);
      return percentField;
    }
    
    private function createDurationField(remoteDurationField:RDurationField):TextInput {
      var durationField:TextInput = new TextInput();
      bindTextInput(durationField, remoteDurationField.state);
      return durationField;
    }

    private function createList(remoteList:RList):List {
      var list:List = new List();
      return list;
    }

    private function createPasswordField(remotePasswordField:RPasswordField):TextInput {
      var passwordField:TextInput = new TextInput();
      bindTextInput(passwordField, remotePasswordField.state);
      passwordField.displayAsPassword = true;
      return passwordField;
    }

    private function createSecurityComponent(remoteSecurityComponent:RSecurityComponent):Canvas {
      var securityComponent:Canvas = new Canvas();
      return securityComponent;
    }

    private function createTable(remoteTable:RTable):DataGrid/*UIComponent*/ {
      var table:DataGrid = new DoubleClickDataGrid();
      var columns:Array = new Array();
      
      for(var i:int=0; i < remoteTable.columns.length; i++) {
        var rColumn:RComponent = remoteTable.columns[i] as RComponent;
        if(rColumn.state == null) {
          rColumn.state = new RemoteValueState();
        }
        var column:DataGridColumn = new DataGridColumn();
        column.headerText = rColumn.label;
        column.width = 100.0;
        var itemRenderer:ClassFactory;
        if(rColumn is RComboBox) {
          itemRenderer = new ClassFactory(EnumerationDgItemRenderer);
          itemRenderer.properties = {values:(rColumn as RComboBox).values,
                                     labels:(rColumn as RComboBox).translations,
                                     icons :(rColumn as RComboBox).icons,
                                     iconTemplate:_iconTemplate};
        } else if(rColumn is RCheckBox) {
          itemRenderer = new ClassFactory(UIComponentDgItemRenderer);
          itemRenderer.properties = {viewFactory:this,
                                     remoteComponent:rColumn,
                                     index:i+1};
          column.rendererIsEditor = true;              
        } else {
          itemRenderer = new ClassFactory(RemoteValueDgItemRenderer);
          itemRenderer.properties = {formatter:createFormatter(rColumn)};
        }
        column.itemRenderer = itemRenderer
        
        if(!(rColumn is RCheckBox)) {
          var itemEditor:ClassFactory = new ClassFactory(RemoteValueDgItemEditor);
          itemEditor.properties = {editor:createComponent(rColumn),
                                   state:rColumn.state,
                                   index:i+1};
          column.itemEditor = itemEditor;
        }
        column.editorDataField = "state";
        
        column.sortCompareFunction = _remoteValueSorter.compareStrings;
        columns.push(column);
      }
      
      table.columns = columns;
      if(remoteTable.selectionMode == "SINGLE_SELECTION") {
        table.allowMultipleSelection = false;
      } else {
        table.allowMultipleSelection = true;
      }
      table.editable = true;
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
        } else {
          // could be finer.
          tableModel.removeAll();
          for each (item in (event.currentTarget as ArrayCollection).source) {
            tableModel.addItem(item);
          }
        }
      });
      bindTable(table, remoteTable.state as RemoteCompositeValueState);
      return table;

//      var test:HBox = new HBox();
//      test.addChild(table);
//      var testB:Button = new Button();
//      testB.addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {
//        var cellState:RemoteValueState = (((remoteTable.state as RemoteCompositeValueState).children[0] as RemoteCompositeValueState).children[11] as RemoteValueState);
//        cellState.value = !cellState.value;
//      });
//      testB.label = "test";
//      var testB2:Button = new Button();
//      testB2.addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {
//        var row:RemoteCompositeValueState = (remoteTable.state as RemoteCompositeValueState).children[0] as RemoteCompositeValueState;
//        var newRow:RemoteCompositeValueState = new RemoteCompositeValueState();
//        newRow.children = new ArrayCollection();
//        for(var c:int = 0; c < row.children.length; c++) {
//          var newCell:RemoteValueState = new RemoteValueState()
//          newCell.value = "test " + c
//          newRow.children.addItem(newCell);
//        }
//        (remoteTable.state as RemoteCompositeValueState).children.addItem(newRow);
//      });
//      testB2.label = "test2";
//      test.addChild(testB);
//      test.addChild(testB2);
//      return test;
    }
    
    private function bindTable(table:DataGrid, state:RemoteCompositeValueState):void {
      table.addEventListener(DataGridEvent.HEADER_RELEASE, function(event:DataGridEvent):void {
        _remoteValueSorter.sortColumnIndex = event.columnIndex;
      });
      BindingUtils.bindSetter(function(selectedItems:Array):void {
        if(selectedItems != null) {
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
          state.selectedIndices = null;
          state.leadingIndex = -1;
        }
      }, table, "selectedItems", true);
      table.addEventListener(DataGridEvent.ITEM_EDIT_END, function(event:DataGridEvent):void {
        if (event.reason != DataGridEventReason.CANCELLED) {
          var table:DataGrid = event.currentTarget as DataGrid;
          if(table.itemEditorInstance is RemoteValueDgItemEditor) {
            var currentEditor:RemoteValueDgItemEditor = table.itemEditorInstance as RemoteValueDgItemEditor;
            var state:RemoteValueState = currentEditor.state;
            var row:RemoteCompositeValueState = (table.dataProvider as ArrayCollection)[event.rowIndex] as RemoteCompositeValueState; 
            var cell:RemoteValueState = row.children[event.columnIndex +1] as RemoteValueState;
            
            cell.value = state.value;
          }
        }
      });
      table.addEventListener(DataGridEvent.ITEM_EDIT_BEGINNING, function(event:DataGridEvent):void {
        var rowCollection:ArrayCollection = (event.currentTarget as DataGrid).dataProvider as ArrayCollection;
        var cellValueState:RemoteValueState = (rowCollection[event.rowIndex] as RemoteCompositeValueState).children[event.columnIndex +1] as RemoteValueState; 
        if(!cellValueState.writable) {
    	    event.preventDefault();
    	  }
    	});
      table.addEventListener(DataGridEvent.ITEM_FOCUS_IN, function(event:DataGridEvent):void {
        ((event.currentTarget as DataGrid).itemEditorInstance as UIComponent).setFocus();
      });
    }

    private function createTextArea(remoteTextArea:RTextArea):TextArea {
      var textArea:TextArea = new TextArea();
      bindTextArea(textArea, remoteTextArea.state);
      return textArea;
    }

    private function createTextField(remoteTextField:RTextField):TextInput {
      var textField:TextInput = new TextInput();
      bindTextInput(textField, remoteTextField.state);
      return textField;
    }

    private function createTimeField(remoteTimeField:RTimeField):TextInput {
      var timeField:TextInput = new TextInput();
      bindTextInput(timeField, remoteTimeField.state);
      return timeField;
    }
    
    private function createButton(remoteAction:RAction):Button {
      var button:Button = new Button();
	    button.setStyle("icon", getIconForComponent(button, remoteAction.icon));
		  button.toolTip=remoteAction.description + TOOLTIP_ELLIPSIS;
      return button;
    }
    
    private function bindTextInput(textInput:TextInput, remoteState:RemoteValueState):void {
      BindingUtils.bindProperty(textInput, "text", remoteState, "value");
      var updateModel:Function = function (event:Event):void {
        remoteState.value = (event.currentTarget as TextInput).text;
      };
      textInput.addEventListener(FlexEvent.ENTER,updateModel);
      textInput.addEventListener(FocusEvent.FOCUS_OUT,updateModel);
    }
    
    private function bindTextArea(textArea:TextArea, remoteState:RemoteValueState):void {
      BindingUtils.bindProperty(textArea, "text", remoteState, "value");
      var updateModel:Function = function (event:Event):void {
        remoteState.value = (event.currentTarget as TextArea).text;
      };
      textArea.addEventListener(FocusEvent.FOCUS_OUT,updateModel);
    }

    private function bindColorPicker(colorPicker:ColorPicker, remoteState:RemoteValueState):void {
      BindingUtils.bindProperty(colorPicker, "selectedColor", remoteState, "value");
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

    private function getIconForComponent(component:UIComponent, rIcon:RIcon):Class {
      if(rIcon != null) {
        return IconUtility.getClass(component, computeUrl(rIcon.imageUrlSpec) 
                                    , rIcon.width, rIcon.height);
      }
      return null;
    }
    
    internal static function computeUrl(imageUrlSpec:String):String {
      return getContextRoot() + "/download?localUrl=" + imageUrlSpec;
    }
    
    internal static function getContextRoot():String {
      return Application.application.url.substring(0,Application.application.url.lastIndexOf("/"));
    }

    private function createFormatter(remoteComponent:RComponent):Formatter {
      if(remoteComponent is RDateField) {
        return new DateFormatter();
      }
      return null;
    }
  }
}