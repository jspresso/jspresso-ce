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
package org.jspresso.framework.flex.view {

import flash.display.DisplayObject;

import mx.containers.ApplicationControlBar;
import mx.containers.Panel;
import mx.containers.TabNavigator;
import mx.controls.Button;
import mx.controls.PopUpButton;
import mx.core.ClassFactory;
import mx.core.UIComponent;
import mx.managers.PopUpManager;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler;
import org.jspresso.framework.flex.components.JsoButton;
import org.jspresso.framework.flex.components.JsoComboBox;
import org.jspresso.framework.flex.components.JsoPanel;
import org.jspresso.framework.flex.components.JsoPopUpButton;
import org.jspresso.framework.flex.components.JsoResizablePanel;
import org.jspresso.framework.flex.components.JsoRichTextEditor;
import org.jspresso.framework.flex.components.JsoTabNavigator;
import org.jspresso.framework.flex.components.JsoTree;
import org.jspresso.framework.flex.components.JsoTreeItemRenderer;
import org.jspresso.framework.flex.components.TopApplicationButton;
import org.jspresso.framework.flex.components.TopApplicationPopUpButton;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.RIcon;
import org.jspresso.framework.util.remote.registry.IRemotePeerRegistry;
import org.jspresso.framework.view.flex.DefaultFlexViewFactory;
import org.jspresso.framework.view.flex.EnhancedRichTextEditor;
import org.jspresso.framework.view.flex.RIconComboBox;
import org.jspresso.framework.view.flex.SelectionTrackingTree;

public class ThemedFlexViewFactory extends DefaultFlexViewFactory {

  public function ThemedFlexViewFactory(remotePeerRegistry:IRemotePeerRegistry, actionHandler:IActionHandler,
                                              commandHandler:IRemoteCommandHandler) {
    super(remotePeerRegistry, actionHandler, commandHandler);
  }

  public override function createButtonComponent(topApplicationButton:Boolean = false):Button {
    var button:Button;
    if (topApplicationButton) {
      button = new TopApplicationButton();
      button.styleName = "top";
    } else {
      button = new JsoButton();
    }
    return button
  }

  public override function createPopUpButtonComponent(topApplicationButton:Boolean = false):PopUpButton {
    var button:PopUpButton;
    if (topApplicationButton) {
      button = new TopApplicationPopUpButton();
      button.styleName = "top";
    } else {
      button = new JsoPopUpButton();
    }
    return button
  }

  public override function createPanelComponent():Panel {
    var panel:JsoPanel = new JsoPanel();
    //panel.grayFactor = 0.6;
    return panel;
  }

  public override function createResizableDialog(dialogParent:DisplayObject):Panel {
    var dialog:JsoResizablePanel = PopUpManager.createPopUp(dialogParent, JsoResizablePanel, true) as JsoResizablePanel;
    dialog.resizable = true;
    return dialog;
  }

  public override function createTreeComponent():SelectionTrackingTree {
    return new JsoTree();
  }

  public override function createTreeItemRenderer():ClassFactory {
    var renderer:ClassFactory = new ClassFactory(JsoTreeItemRenderer);
    return renderer;
  }

  public override function createRichTextEditorComponent():EnhancedRichTextEditor {
    return new JsoRichTextEditor();
  }

  public override function createComboBoxComponent():RIconComboBox {
    var comboBox:JsoComboBox = new JsoComboBox();
    comboBox.styleName = "borderComboBox";
    return comboBox;
  }

  public override function createTabNavigatorComponent():TabNavigator {
    return new JsoTabNavigator();
  }

  public override function createDialogButton(label:String, toolTip:String, icon:RIcon):Button {
    var button:Button = super.createDialogButton(label, toolTip, icon);
    button.styleName = "borderButton";
    return button;
  }

  protected override function createToolBar(remoteComponent:RComponent, component:UIComponent):ApplicationControlBar {
    var controlBar:ApplicationControlBar = super.createToolBar(remoteComponent, component);
    controlBar.styleName = "toolBar";
    return controlBar;
  }

  protected override function createSecondaryToolBar(remoteComponent:RComponent,
                                                     component:UIComponent):ApplicationControlBar {
    var controlBar:ApplicationControlBar = super.createSecondaryToolBar(remoteComponent, component);
    controlBar.styleName = "toolBar";
    return controlBar;
  }
}

}
