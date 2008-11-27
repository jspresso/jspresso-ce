package org.jspresso.framework.view {
  import mx.containers.BoxDirection;
  import mx.containers.Canvas;
  import mx.containers.DividedBox;
  import mx.containers.Grid;
  import mx.containers.GridItem;
  import mx.containers.GridRow;
  import mx.containers.HBox;
  import mx.containers.TabNavigator;
  import mx.containers.ViewStack;
  import mx.controls.CheckBox;
  import mx.controls.ComboBox;
  import mx.controls.DataGrid;
  import mx.controls.DateField;
  import mx.controls.Image;
  import mx.controls.Label;
  import mx.controls.List;
  import mx.controls.TextArea;
  import mx.controls.TextInput;
  import mx.controls.Tree;
  import mx.controls.dataGridClasses.DataGridColumn;
  import mx.core.Container;
  import mx.core.UIComponent;
  
  import org.jspresso.framework.gui.remote.RActionField;
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
  import org.jspresso.framework.util.gui.CellConstraints;
  
  public class DefaultFlexViewFactory {

    public function DefaultFlexViewFactory() {
    }
    
    public function createComponent(remoteComponent:RComponent):UIComponent {
      var component:UIComponent;
      if(remoteComponent is RActionField) {
        component = createActionField(remoteComponent as RActionField);
      } else if(remoteComponent is RCheckBox) {
        component = createCheckBox(remoteComponent as RCheckBox);
      } else if(remoteComponent is RColorField) {
        component = createColorField(remoteComponent as RColorField);
      } else if(remoteComponent is RContainer) {
        component = createContainer(remoteComponent as RContainer);
      } else if(remoteComponent is RDateField) {
        component = createDateField(remoteComponent as RDateField);
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
      return tree;
    }

    private function createImageComponent(remoteImageComponent:RImageComponent):Image {
      var imageComponent:Image = new Image();
      return imageComponent;
    }
    
    private function createActionField(remoteActionField:RActionField):HBox {
      var actionField:HBox = new HBox();
      return actionField;
    }
    
    private function createColorField(remoteColorField:RColorField):HBox {
      var colorField:HBox = new HBox();
      return colorField;
    }

    private function createCheckBox(remoteCheckBox:RCheckBox):CheckBox {
      var checkBox:CheckBox = new CheckBox();
      return checkBox;
    }

    private function createComboBox(remoteComboBox:RComboBox):ComboBox {
      var comboBox:ComboBox = new ComboBox();
      return comboBox;
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
        tabContainer.addChild(tabCanvas);
        
        var tab:UIComponent = createComponent(rTab);
        tab.percentWidth = 100.0;
        tab.percentHeight = 100.0;
        tabCanvas.addChild(tab);
      }
      return tabContainer;
    }

    private function createDateField(remoteDateField:RDateField):DateField {
      var dateField:DateField = new DateField();
      return dateField;
    }

    private function createDecimalField(remoteDecimalField:RDecimalField):TextInput {
      var decimalField:TextInput = new TextInput();
      return decimalField;
    }

    private function createIntegerField(remoteIntegerField:RIntegerField):TextInput {
      var integerField:TextInput = new TextInput();
      return integerField;
    }

    private function createPercentField(remotePercentField:RPercentField):TextInput {
      var percentField:TextInput = new TextInput();
      return percentField;
    }
    
    private function createDurationField(remoteDurationField:RDurationField):TextInput {
      var durationField:TextInput = new TextInput();
      return durationField;
    }

    private function createList(remoteList:RList):List {
      var list:List = new List();
      return list;
    }

    private function createPasswordField(remotePasswordField:RPasswordField):TextInput {
      var passwordField:TextInput = new TextInput();
      passwordField.displayAsPassword = true;
      return passwordField;
    }

    private function createSecurityComponent(remoteSecurityComponent:RSecurityComponent):Canvas {
      var securityComponent:Canvas = new Canvas();
      return securityComponent;
    }

    private function createTable(remoteTable:RTable):DataGrid {
      var table:DataGrid = new DataGrid();
      var columns:Array = new Array();
      
      for(var i:int=0; i < remoteTable.columns.length; i++) {
        var rColumn:RComponent = remoteTable.columns[i] as RComponent;
        var column:DataGridColumn = new DataGridColumn();
        column.headerText = rColumn.label;
        columns.push(column);
      }
      table.columns = columns;
      return table;
    }

    private function createTextArea(remoteTextArea:RTextArea):TextArea {
      var textArea:TextArea = new TextArea();
      return textArea;
    }

    private function createTextField(remoteTextField:RTextField):TextInput {
      var textField:TextInput = new TextInput();
      return textField;
    }

    private function createTimeField(remoteTimeField:RTimeField):TextInput {
      var timeField:TextInput = new TextInput();
      return timeField;
    }
  }
}