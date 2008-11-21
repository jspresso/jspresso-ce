package org.jspresso.framework.view {
  import mx.containers.DividedBox;
  import mx.containers.Grid;
  import mx.containers.HBox;
  import mx.containers.TabNavigator;
  import mx.containers.ViewStack;
  import mx.controls.ComboBox;
  import mx.controls.DateField;
  import mx.controls.Image;
  import mx.controls.Tree;
  
  import org.jspresso.framework.gui.remote.RActionField;
  import org.jspresso.framework.gui.remote.RBorderContainer;
  import org.jspresso.framework.gui.remote.RCardContainer;
  import org.jspresso.framework.gui.remote.RColorField;
  import org.jspresso.framework.gui.remote.RComboBox;
  import org.jspresso.framework.gui.remote.RConstrainedGridContainer;
  import org.jspresso.framework.gui.remote.RDateField;
  import org.jspresso.framework.gui.remote.RDecimalField;
  import org.jspresso.framework.gui.remote.REvenGridContainer;
  import org.jspresso.framework.gui.remote.RForm;
  import org.jspresso.framework.gui.remote.RImageComponent;
  import org.jspresso.framework.gui.remote.RSplitContainer;
  import org.jspresso.framework.gui.remote.RTabContainer;
  import org.jspresso.framework.gui.remote.RTree;
  
  public class DefaultFlexViewFactory {

    public function DefaultFlexViewFactory() {
    }
    
    protected function createTree(remoteTree:RTree):Tree {
      var tree:Tree = new Tree();
      tree.id = remoteTree.guid;
      return tree;
    }

    protected function createImageComponent(remoteImageComponent:RImageComponent):Image {
      var imageComponent:Image = new Image();
      imageComponent.id = remoteImageComponent.guid;
      return imageComponent;
    }
    
    protected function createActionField(remoteActionField:RActionField):HBox {
      var actionField:HBox = new HBox();
      actionField.id = remoteActionField.guid;
      return actionField;
    }
    
    protected function createColorField(remoteColorField:RColorField):HBox {
      var colorField:HBox = new HBox();
      colorField.id = remoteColorField.guid;
      return colorField;
    }

    protected function createComboBox(remoteComboBox:RComboBox):ComboBox {
      var comboBox:ComboBox = new ComboBox();
      comboBox.id = remoteComboBox.guid;
      return comboBox;
    }

    protected function createBorderContainer(remoteComboBox:RBorderContainer):Grid {
      var borderContainer:Grid = new Grid();
      borderContainer.id = remoteComboBox.guid;
      return borderContainer;
    }

    protected function createCardContainer(remoteCardContainer:RCardContainer):ViewStack {
      var cardContainer:ViewStack = new ViewStack();
      cardContainer.id = remoteCardContainer.guid;
      return cardContainer;
    }

    protected function createConstrainedGridContainer(remoteConstrainedGridContainer:RConstrainedGridContainer):Grid {
      var constrainedGridContainer:Grid = new Grid();
      constrainedGridContainer.id = remoteConstrainedGridContainer.guid;
      return constrainedGridContainer;
    }

    protected function createEvenGridContainer(remoteEvenGridContainer:REvenGridContainer):Grid {
      var evenGridContainer:Grid = new Grid();
      evenGridContainer.id = remoteEvenGridContainer.guid;
      return evenGridContainer;
    }

    protected function createForm(remoteForm:RForm):Grid {
      var form:Grid = new Grid();
      form.id = remoteForm.guid;
      return form;
    }

    protected function createSplitContainer(remoteSplitContainer:RSplitContainer):DividedBox {
      var splitContainer:DividedBox = new DividedBox();
      splitContainer.id = remoteSplitContainer.guid;
      return splitContainer;
    }

    protected function createTabContainer(remoteTabContainer:RTabContainer):TabNavigator {
      var tabContainer:TabNavigator = new TabNavigator();
      tabContainer.id = remoteTabContainer.guid;
      return tabContainer;
    }

    protected function createDateField(remoteDateField:RDateField):DateField {
      var dateField:DateField = new DateField();
      dateField.id = remoteDateField.guid;
      return dateField;
    }

    protected function createDecimalField(remoteDecimalField:RDecimalField):DateField {
      var dateField:DateField = new DateField();
      dateField.id = remoteDecimalField.guid;
      return dateField;
    }
  }
}