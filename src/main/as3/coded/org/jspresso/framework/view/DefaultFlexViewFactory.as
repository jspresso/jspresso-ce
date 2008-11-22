package org.jspresso.framework.view {
  import mx.containers.Canvas;
  import mx.containers.DividedBox;
  import mx.containers.Grid;
  import mx.containers.HBox;
  import mx.containers.TabNavigator;
  import mx.containers.ViewStack;
  import mx.controls.CheckBox;
  import mx.controls.ComboBox;
  import mx.controls.DataGrid;
  import mx.controls.DateField;
  import mx.controls.Image;
  import mx.controls.List;
  import mx.controls.TextArea;
  import mx.controls.TextInput;
  import mx.controls.Tree;
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

    private function createBorderContainer(remoteComboBox:RBorderContainer):Grid {
      var borderContainer:Grid = new Grid();
      return borderContainer;
    }

    private function createCardContainer(remoteCardContainer:RCardContainer):ViewStack {
      var cardContainer:ViewStack = new ViewStack();
      return cardContainer;
    }

    private function createConstrainedGridContainer(remoteConstrainedGridContainer:RConstrainedGridContainer):Grid {
      var constrainedGridContainer:Grid = new Grid();
      return constrainedGridContainer;
    }

    private function createEvenGridContainer(remoteEvenGridContainer:REvenGridContainer):Grid {
      var evenGridContainer:Grid = new Grid();
      return evenGridContainer;
    }

    private function createForm(remoteForm:RForm):Grid {
      var form:Grid = new Grid();
      return form;
    }

    private function createSplitContainer(remoteSplitContainer:RSplitContainer):DividedBox {
      var splitContainer:DividedBox = new DividedBox();
      return splitContainer;
    }

    private function createTabContainer(remoteTabContainer:RTabContainer):TabNavigator {
      var tabContainer:TabNavigator = new TabNavigator();
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
      return passwordField;
    }

    private function createSecurityComponent(remoteSecurityComponent:RSecurityComponent):Canvas {
      var securityComponent:Canvas = new Canvas();
      return securityComponent;
    }

    private function createTable(remoteTable:RTable):DataGrid {
      var table:DataGrid = new DataGrid();
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