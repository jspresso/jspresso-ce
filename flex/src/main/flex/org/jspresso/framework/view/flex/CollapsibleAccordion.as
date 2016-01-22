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
package org.jspresso.framework.view.flex {

import flash.display.DisplayObject;
import flash.events.Event;
import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.containers.Accordion;
import mx.containers.Canvas;
import mx.controls.Button;
import mx.core.Container;
import mx.core.ScrollPolicy;
import mx.core.UIComponent;
import mx.effects.AnimateProperty;
import mx.effects.Move;
import mx.effects.Parallel;
import mx.effects.Sequence;
import mx.effects.easing.Quintic;
import mx.events.EffectEvent;
import mx.events.IndexChangedEvent;

// Default property
[DefaultProperty("content")]

// Events
[Event(name="closeDrawerBegin")]
[Event(name="closeDrawerComplete")]
[Event(name="openDrawerBegin")]
[Event(name="openDrawerComplete")]
[Event(name="change", type="mx.events.IndexChangedEvent")]

//Styles
[Style(name="orientation", type="String", enumeration="left,right")]
[Style(name="initialWidth", type="int")]
[Style(name="openSize", type="int")]

public class CollapsibleAccordion extends Container {
  //Width of component when you click to expand it
  [Bindable]
  public var openSize:Number = 250;

  //make this style
  private var _barSize:Number = 24;

  //Left or right
  [Bindable]
  public var orientation:String = "right";

  [Bindable]
  public var contentStyle:String = "drawerContent";

  [Bindable]
  public var accordianStyle:String = "drawerAccordion";

  [Bindable]
  public var drawerButtonStyle:String = "drawerButton";

  [Bindable]
  public var closeButtonStyle:String = "drawerCloseRight";

  [Bindable]
  public var initialWidth:int;

  //Content array to put item added to the container
  private var accordion:Accordion;
  private var accordionVBox:Canvas;
  private var buttonDict:ArrayCollection;
  private var _barRotate:Number = 90;
  private var _close:Boolean = true;
  private var closeChanged:Boolean = false;
  private var _content:Array;
  private var contentChanged:Boolean = false;
  private var _currentWidth:Number = 24;
  private var _heightPercent:Number = 1;
  private var _selectedIndex:Number = 1;
  private var selectedIndexChanged:Boolean = false;
  //Icons - default
  [Embed(source="../../../../../../resources/assets/images/close.png")]
  private var rightCloseIcon:Class;

  [Embed(source="../../../../../../resources/assets/images/closeleft.png")]
  private var leftCloseIcon:Class;

  private var _historyManagementEnabled:Boolean = false;
  private var _drawerOpAllowed:Boolean;

  public function CollapsibleAccordion() {
    super();
    buttonDict = new ArrayCollection();
    verticalScrollPolicy = ScrollPolicy.OFF;
    horizontalScrollPolicy = ScrollPolicy.OFF;
    _drawerOpAllowed = true;
  }


  public function set content(value:Array):void {
    if (_content) {
      removeAllChildren();
    }
    _content = value;

    invalidateSize();
    contentChanged = true;
  }

  public function get content():Array {
    return _content;
  }

  public function set barRotate(value:Number):void {
    _barRotate = value;
    invalidateProperties();
  }

  public function get barRotate():Number {
    return _barRotate;
  }

  public function set barSize(value:Number):void {
    _barSize = value;
  }

  [Bindable]
  public function get barSize():Number {
    return _barSize;
  }

  public function set close(value:Boolean):void {
    if (_close != value) {
      _close = value;
      closeChanged = true;
      invalidateSize();
      invalidateProperties();
    }
  }

  public function get close():Boolean {
    return _close;
  }

  [Bindable("currentWidthChanged")]
  public function get currentWidth():Number {
    return _currentWidth;
  }

  public function set currentWidth(value:Number):void {
    _currentWidth = value;
    dispatchEvent(new Event('currentWidthChanged'));
    invalidateSize();
    invalidateDisplayList();
  }

  [Bindable("heightPercentChanged")]
  public function get heightPercent():Number {
    return _heightPercent;
  }

  public function set heightPercent(value:Number):void {
    _heightPercent = value;
    dispatchEvent(new Event('heightPercentChanged'));
    invalidateSize();
    invalidateDisplayList();
  }

  public function set selectedIndex(value:Number):void {
    _selectedIndex = value;
    selectedIndexChanged = true;
    invalidateProperties();
  }

  public function get selectedIndex():Number {
    return _selectedIndex;
  }

  override protected function createChildren():void {
    super.createChildren();

    accordionVBox = new Canvas();
    accordionVBox.percentHeight = 100;
    accordionVBox.percentWidth = 100;
    accordionVBox.setStyle("verticalGap", 0);
    accordionVBox.styleName = contentStyle;

    accordion = new Accordion();
    accordion.historyManagementEnabled = _historyManagementEnabled;
    accordion.addEventListener(IndexChangedEvent.CHANGE, onAccordionChange);
    accordion.styleName = accordianStyle;
    accordion.percentHeight = 100;
    accordion.percentWidth = 100;
    accordion.setStyle("headerHeight", barSize);

    if (orientation == "right") {
      accordion.x = -1 * width;
    } else {
      accordion.x = width;
    }

    var closeButton:Button = new Button();
    closeButton.styleName = closeButtonStyle;
    closeButton.width = 21;
    closeButton.height = 21;
    closeButton.toolTip = "Close";
    closeButton.addEventListener(MouseEvent.CLICK, closeDrawer);

    closeButton.setStyle("right", 6);
    closeButton.setStyle("top", barSize / 2 - closeButton.height / 2);

    accordionVBox.addChild(accordion);
    accordionVBox.addChild(closeButton);
    addChild(accordionVBox);
  }

  private function closeDrawer(event:MouseEvent):void {
    if (this._drawerOpAllowed) {
      this._drawerOpAllowed = false;
      close = true;
      //let everyone know we are about to close the drawer
      dispatchEvent(new Event("closeDrawerBegin"));
    }
  }

  private function onAccordionChange(event:IndexChangedEvent):void {
    invalidateDisplayList();

    var eventClone:IndexChangedEvent = event.clone() as IndexChangedEvent;
    dispatchEvent(eventClone);
  }

  override protected function commitProperties():void {
    super.commitProperties();
    if (contentChanged && accordion) {
      contentChanged = false;

      for each (var child:Container in _content) {
        accordion.addChild(child);
        var newButton:Button = new Button();
        newButton.styleName = drawerButtonStyle; //"drawerButton";
        newButton.setStyle("icon", child.icon);
        newButton.label = ( child ).label;
        newButton.labelPlacement = orientation;
        if (orientation == "left") {
          newButton.setStyle("textAlign", "right");
        }
        newButton.data = child;
        newButton.addEventListener(MouseEvent.CLICK, onButtonClick);
        buttonDict.addItem(newButton);
        addChild(newButton);
      }
    }

    if (closeChanged) {
      closeChanged = false;
      var seq:Sequence = new Sequence();
      var parallel:Parallel = new Parallel(this);
      var animateProperty:AnimateProperty = new AnimateProperty(this);
      animateProperty.property = "currentWidth";
      animateProperty.easingFunction = Quintic.easeIn;

      var animateProperty3:AnimateProperty = new AnimateProperty();
      animateProperty3.property = "heightPercent";
      animateProperty3.easingFunction = Quintic.easeIn;
      animateProperty3.fromValue = 0;
      animateProperty3.toValue = 1;

      parallel.addChild(animateProperty3);
      parallel.addChild(animateProperty);

      if (_close) {
        _selectedIndex = -1;
        showButtons(true);

        animateProperty.fromValue = openSize;
        animateProperty.toValue = barSize;

        var moveClose:Move = new Move(accordionVBox);

        if (orientation == "right") {
          moveClose.xBy = -1 * accordionVBox.width;
        } else {
          moveClose.xBy = accordionVBox.width;
        }

        moveClose.duration = 200;
        seq.addChild(moveClose);
        seq.addChild(parallel);
        seq.addEventListener(EffectEvent.EFFECT_END, onCloseEffectEnd);
      } else {
        //animateProperty.easingFunction = Quintic.easeOut;
        //animateProperty3.easingFunction = Quintic.easeOut;
        animateProperty.fromValue = barSize;
        animateProperty.toValue = openSize;

        seq.addChild(parallel);
        var moveOpen:Move = new Move(accordionVBox);
        moveOpen.xTo = 0;
        moveOpen.duration = 200;
        moveOpen.addEventListener(EffectEvent.EFFECT_END, onOpenEffectEnd);
        seq.addChild(moveOpen);
      }

      seq.play();
    }

    if (selectedIndexChanged && accordion) {
      selectedIndexChanged = false;
      if (close) {
        var selectedButton:Button = buttonDict.getItemAt(selectedIndex) as Button;
        accordion.selectedChild = (( selectedButton as Button ).data as Container );
        dispatchEvent(new Event("openDrawerBegin"));
        callLater(openAccordion);
      } else {
        accordion.selectedIndex = selectedIndex;
      }

    }
  }

  private function openAccordion():void {
    close = false;
  }

  private function onCloseEffectEnd(evt:EffectEvent):void {
    //let everyone know we are completely open
    dispatchEvent(new Event("closeDrawerComplete"));
    if (orientation == "left") {
      for (var i:int = 0; i < buttonDict.length; i++) {
        Button(buttonDict[i]).labelPlacement = "left";
        Button(buttonDict[i]).setStyle("textAlign", "right");
      }
    }
    this._drawerOpAllowed = true;
  }

  private function onOpenEffectEnd(event:EffectEvent):void {
    //if this can be animated at all that would be nice
    if (orientation == "left") {
      for (var i:int = 0; i < buttonDict.length; i++) {
        Button(buttonDict[i]).labelPlacement = "right";
        Button(buttonDict[i]).setStyle("textAlign", "left");
      }
    }
    showButtons(false);
    //let everyone know we are completely open
    dispatchEvent(new Event("openDrawerComplete"));
    this._drawerOpAllowed = true;
  }

  private function showButtons(show:Boolean):void {
    for (var i:Number = 0; i < buttonDict.length; i++) {
      ( buttonDict.getItemAt(i) as UIComponent ).visible = show;
    }
  }

  override protected function measure():void {
    super.measure();

    measuredMinWidth = _currentWidth;
    //noinspection JSSuspiciousNameCombination
    measuredMinHeight = _currentWidth;
    measuredWidth = _currentWidth;
    //noinspection JSSuspiciousNameCombination
    measuredHeight = _currentWidth;
  }

  private function onButtonClick(event:MouseEvent):void {
    if (this._drawerOpAllowed) {
      this._drawerOpAllowed = false;
      var oldSelectedIndex:int = accordion.selectedIndex;
      accordion.selectedChild = (( event.currentTarget as Button ).data as Container );
      close = !close;

      var indexEvent:IndexChangedEvent = new IndexChangedEvent(IndexChangedEvent.CHANGE);
      indexEvent.relatedObject = accordion.selectedChild as DisplayObject;
      indexEvent.oldIndex = oldSelectedIndex;
      indexEvent.newIndex = accordion.selectedIndex;
      dispatchEvent(indexEvent);

      dispatchEvent(new Event("openDrawerBegin"));
    }
  }

  override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
    super.updateDisplayList(unscaledWidth, unscaledHeight);
    if (accordionVBox) {
      accordionVBox.setActualSize(unscaledWidth, unscaledHeight);
    }
    if (buttonDict.length > 0) {
      var distanceLeftY:Number;
      var distanceToMoveY:Number;

      var distanceLeftX:Number;
      var distanceToMoveX:Number;

      var rotateLeftX:Number;
      var rotateToMoveX:Number;

      var buttonHeight:Number = barSize;

      //cant ever have less than one so may as well not do this in the loop
      var bh:Number = unscaledHeight / buttonDict.length;

      for (var i:Number = 0; i < buttonDict.length; i++) {
        var button:Button = buttonDict.getItemAt(i) as Button;
        if (close) {
          if (orientation == "right") {
            distanceLeftY = button.y - ( bh * i );
            distanceToMoveY = distanceLeftY * heightPercent;

            distanceLeftX = button.x - barSize;
            distanceToMoveX = distanceLeftX * heightPercent;

            button.move(button.x - distanceToMoveX, button.y - distanceToMoveY);

            if (currentWidth > unscaledHeight / buttonDict.length) {
              button.setActualSize(currentWidth, buttonHeight);
            } else {
              button.setActualSize(unscaledHeight / buttonDict.length, buttonHeight);
            }

            rotateLeftX = button.rotation - 90;
            rotateToMoveX = rotateLeftX * heightPercent;

            button.rotation -= rotateToMoveX;

          } else {

            button.x = 0;
            //work on y.  first it needs to start where we currently are and
            //move down
            distanceLeftY = button.y - ((i + 1) * bh);
            distanceToMoveY = distanceLeftY * heightPercent;

            button.move(0, button.y - distanceToMoveY);
            if (currentWidth > unscaledHeight / buttonDict.length) {
              button.setActualSize(currentWidth, buttonHeight);
            } else {
              button.setActualSize(unscaledHeight / buttonDict.length, buttonHeight);
            }
            if (distanceToMoveY != distanceLeftY) {
              //how far are  we going to have to move?
              //this should be v * heightPercent
              //we want the percentage that we are moving
              //now we know percent we that of 90
              button.rotation = -1 * (90 * distanceToMoveY / distanceLeftY);
            } else {
              button.rotation = -90;
            }
          }
          setActualSize(currentWidth, unscaledHeight);
        } else {
          if (i <= accordion.selectedIndex) {
            distanceLeftY = button.y - ( ( i * barSize )  );
            distanceToMoveY = distanceLeftY * heightPercent;

            distanceLeftX = button.x;
            distanceToMoveX = distanceLeftX * heightPercent;
            button.move(button.x - distanceToMoveX, button.y - distanceToMoveY);

          } else {
            distanceLeftY = button.y - ( unscaledHeight - ( ( buttonDict.length - i ) * barSize ) );
            distanceToMoveY = distanceLeftY * heightPercent;

            distanceLeftX = button.x;
            distanceToMoveX = distanceLeftX * heightPercent;
            button.move(button.x - distanceToMoveX, button.y - distanceToMoveY);
          }

          var distanceLeftWidth:Number = button.width - unscaledWidth;
          var distanceToMoveWidth:Number = distanceLeftWidth * heightPercent;

          if (button.width - distanceToMoveWidth > currentWidth) {
            button.setActualSize(button.width - distanceToMoveWidth, barSize);
          } else {
            button.setActualSize(currentWidth, barSize);
          }

          if (orientation == "right") {
            rotateLeftX = button.rotation;
            rotateToMoveX = rotateLeftX * heightPercent;
            button.rotation -= rotateToMoveX;
          } else {
            rotateLeftX = button.rotation;
            rotateToMoveX = rotateLeftX * heightPercent;
            button.rotation -= rotateToMoveX;
          }

          setActualSize(unscaledWidth, unscaledHeight);
        }
      }
    }
  }

  public function getHeaderAt(index:int):Button {
    if (accordion) {
      return accordion.getHeaderAt(index);
    }
    return null;
  }

  public function get historyManagementEnabled():Boolean {
    return _historyManagementEnabled;
  }

  public function set historyManagementEnabled(value:Boolean):void {
    _historyManagementEnabled = value;
    if (accordion) {
      accordion.historyManagementEnabled = value;
    }
  }

  public function get selectedChild():Container {
    return content[selectedIndex];
  }

  public function set selectedChild(value:Container):void {
    selectedIndex = content.indexOf(value);
  }

  public function getAcccordionSectionByName(name:String):Container {
    if (_content) {
      for (var i:int = 0; i < _content.length; i++) {
        if ((_content[i] as Container).name == name) {
          return _content[i] as Container;
        }
      }
    }
    return null;
  }

  public function addAcccordionSection(child:Container):Container {
    var actualContent:Array = content;
    if (!actualContent) {
      actualContent = [];
    }
    actualContent.push(child);
    content = actualContent;
    return child;
  }

  public function getButtonAt(index:int):Button {
    return buttonDict[index] as Button;
  }
}
}
