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
package org.jspresso.framework.flex.controller {

import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;

import mx.containers.Canvas;
import mx.containers.HBox;
import mx.containers.VBox;
import mx.containers.ViewStack;
import mx.controls.Button;
import mx.controls.ButtonBar;
import mx.controls.ButtonLabelPlacement;
import mx.controls.Image;
import mx.controls.Label;
import mx.core.Application;
import mx.core.UIComponent;
import mx.events.ItemClickEvent;
import mx.rpc.remoting.mxml.RemoteObject;

import org.jspresso.framework.application.frontend.controller.flex.DefaultFlexController;
import org.jspresso.framework.flex.view.ThemedFlexViewFactory;
import org.jspresso.framework.theme.header.HeaderSeparator;
import org.jspresso.framework.theme.utils.ThemeFilterUtils;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RActionList;
import org.jspresso.framework.view.flex.CollapsibleAccordion;
import org.jspresso.framework.view.flex.DefaultFlexViewFactory;

public class ThemedFlexController extends DefaultFlexController {

  [Embed(source="../../../../../../resources/images/logo.png", mimeType="image/png")]
  private static const _logo:Class;

  public function ThemedFlexController(remoteController:RemoteObject, userLanguage:String) {
    super(remoteController, userLanguage);
  }

  override protected function createViewFactory():DefaultFlexViewFactory {
    return new ThemedFlexViewFactory(this, this, this);
  }

  protected override function createWorkspaceViewStack():ViewStack {
    var wsViewStack:ViewStack = super.createWorkspaceViewStack();
    wsViewStack.styleName = "centralViewStack";
    return wsViewStack;
  }

  protected override function createWorkspaceAccordion(workspaceNames:Array,
                                                       workspaceActions:RActionList):CollapsibleAccordion {
    var wsAccordion:CollapsibleAccordion = super.createWorkspaceAccordion(workspaceNames, workspaceActions);
    wsAccordion.accordianStyle = "leftAccordion";
    wsAccordion.drawerButtonStyle = "leftAccordionVerticalHeader";
    wsAccordion.closeButtonStyle = "accordionCloseButton";
    wsAccordion.barSize = 30;
    return wsAccordion;
  }

  protected override function assembleApplicationContent(navigationAccordion:CollapsibleAccordion,
                                                         mainViewStack:ViewStack, exitAction:RAction,
                                                         navigationActions:Array, actions:Array, secondaryActions:Array,
                                                         helpActions:Array):UIComponent {
    var applicationFrame:Application = Application.application as Application;
    var mainContainer:VBox = new VBox();
    mainContainer.percentWidth = 100.0;
    mainContainer.percentHeight = 100.0;
    mainContainer.setStyle("verticalGap", 0);

    var header:Canvas = new Canvas();
    mainContainer.addChild(header);
    header.height = 55;
    header.percentWidth = 100.0;
    header.height = 55;

    header.styleName = "header";
    var brand:Image = new Image();
    header.addChild(brand);
    brand.x = 40;
    brand.setStyle("verticalCenter", 0);
    brand.source = _logo;

    var appNameNextPrevHBox:HBox = new HBox();
    header.addChild(appNameNextPrevHBox);
    appNameNextPrevHBox.x = 300;
    appNameNextPrevHBox.percentHeight = 100.0;
    appNameNextPrevHBox.styleName = "appNameNextPrevHBox";

    // No need to install the navigation actions since browser correctly handle them

    var appName:Label = new Label();
    appNameNextPrevHBox.addChild(appName);
    appName.styleName = "applicationName";
    appName.filters = [ThemeFilterUtils.onePixelDarkShadow()];
    BindingUtils.bindProperty(appName, "text", this, "name");

    var centeredHBox:HBox = new HBox();
    header.addChild(centeredHBox);
    centeredHBox.styleName = "topCenteredHBox";
    centeredHBox.height = 35;
    if (actions != null) {
      for (var i:int = 0; i < actions.length; i++) {
        var popupB:Button = getViewFactory().createPopupButton(actions[i] as RActionList, applicationFrame, true);
        popupB.styleName = "top";
        centeredHBox.addChild(popupB);
        if (i < actions.length - 1) {
          centeredHBox.addChild(new HeaderSeparator());
        }
      }
    }

    var rightHBox:HBox = new HBox();
    header.addChild(rightHBox);
    rightHBox.height = 35;
    rightHBox.setStyle("right", 35);
    rightHBox.styleName = "topRightHBox";

    var statusHB:HBox = new HBox();
    rightHBox.addChild(statusHB);
    var modeStatusBar:Label = getStatusBar();
    modeStatusBar.percentWidth = 100.0;
    statusHB.styleName = "statusMode";
    statusHB.addChild(modeStatusBar);

    if (helpActions != null && helpActions.length > 0) {
      rightHBox.addChild(new HeaderSeparator());
      for (i = 0; i < helpActions.length; i++) {
        var helpPopupB:Button = getViewFactory().createPopupButton(helpActions[i] as RActionList, applicationFrame,
                                                                   true);
        helpPopupB.styleName = "top";
        rightHBox.addChild(helpPopupB);
        if (i < helpActions.length - 1) {
          rightHBox.addChild(new HeaderSeparator());
        }
      }
    }

    rightHBox.addChild(new HeaderSeparator());

    var exitButton:Button = new Button();
    rightHBox.addChild(exitButton);
    exitButton.styleName = "quitButton";
    exitButton.width = 24;
    exitButton.labelPlacement = ButtonLabelPlacement.TOP;
    exitButton.label = null;
    exitButton.addEventListener(MouseEvent.CLICK, function (event:MouseEvent):void {
      execute(exitAction);
    });

    mainViewStack.styleName = "centralViewStack";

    var split:UIComponent = assembleSplittedSection(navigationAccordion, mainViewStack);
    split.percentWidth = 100.0;
    split.percentHeight = 100.0;
    mainContainer.addChild(split);
    return mainContainer;
  }

  protected override function assembleSplittedSection(navigationAccordion:CollapsibleAccordion,
                                                      mainViewStack:ViewStack):UIComponent {
    var split:UIComponent = super.assembleSplittedSection(navigationAccordion, mainViewStack);
    navigationAccordion.percentHeight = 100.0;
    return split;
  }
}
}

import mx.core.IFactory;
import mx.core.UIComponent;

internal class UIComponentSingletonFactory implements IFactory {
  private var _instance:UIComponent;

  public function UIComponentSingletonFactory(instance:UIComponent) {
    this._instance = instance;
  }

  public function newInstance():* {
    return this._instance;
  }


}
