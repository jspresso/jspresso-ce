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

import flash.events.Event;
import flash.events.MouseEvent;

import mx.containers.Panel;
import mx.controls.Button;
import mx.core.ScrollPolicy;
import mx.core.mx_internal;
import mx.effects.Resize;
import mx.events.EffectEvent;
import mx.logging.ILogger;
import mx.logging.Log;

use namespace mx_internal;

// --------------------------------------
// Events
// --------------------------------------
/**
 *  Dispatched when the user collapses the panel.
 *
 *  @eventType minimize
 */
[Event(name="minimize", type="flash.events.Event")]
/**
 *  Dispatched when the user expands the panel.
 *
 *  @eventType restore
 */
[Event(name="restore", type="flash.events.Event")]
// --------------------------------------
// Styles
// --------------------------------------
/**
 *  The collapse effect duration.
 *
 *  @default 250
 */
[Style(name="collapseDuration", type="Number", inherit="no")]
public class CollapsiblePanel extends Panel {
  // --------------------------------------------------------------------------
  //
  // Class constants
  //
  // --------------------------------------------------------------------------
  // --------------------------------------------------------------------------
  //
  // Class variables
  //
  // --------------------------------------------------------------------------
  /**
   * @private
   * Logger for this class.
   */
  private static var logger:ILogger = Log.getLogger("org.jspresso.framework.view.flex.CollapsiblePanel");

  // --------------------------------------------------------------------------
  //
  // Variables
  //
  // --------------------------------------------------------------------------
  //noinspection JSFieldCanBeLocal
  /**
   * @private
   * Height of the component before collapse.
   */
  private var expandedHeight:Number;
  /**
   * @private
   * The transition effect from collapsed to expanded and back.
   */
  private var tween:Resize;
  /**
   * @private
   * The original verticalScrollPolicy.
   */
  private var originalVScrollPolicy:String;

  private var _isCollapsible:Boolean = true;

  // --------------------------------------------------------------------------
  //
  // Constructor
  //
  // --------------------------------------------------------------------------
  /**
   *  Constructor.
   */
  public function CollapsiblePanel() {
    super();
    tween = new Resize(this);
    tween.addEventListener(EffectEvent.EFFECT_END, tween_effectEndHandler);
  }

  // --------------------------------------------------------------------------
  //
  // Overridden properties
  //
  // --------------------------------------------------------------------------
  // ----------------------------------
  // collapseButtonStyleFilters
  // ----------------------------------
  //noinspection JSFieldCanBeLocal
  /**
   * @private
   * Storage for the collapseButtonStyleFilters property.
   */
  private static var _collapseButtonStyleFilters:Object = {"collapseButtonUpSkin": "collapseButtonUpSkin", "collapseButtonOverSkin": "collapseButtonOverSkin", "collapseButtonDownSkin": "collapseButtonDownSkin", "collapseButtonDisabledSkin": "collapseButtonDisabledSkin", "collapseButtonSkin": "collapseButtonSkin", "repeatDelay": "repeatDelay", "repeatInterval": "repeatInterval"};

  /**
   *  The set of styles to pass from the Panel to the collapse button.
   *  @see mx.styles.StyleProxy
   */
  protected function get collapseButtonStyleFilters():Object {
    return _collapseButtonStyleFilters;
  }

  // ----------------------------------
  // collapsed
  // ----------------------------------
  /**
   * @private
   * Storage for the collapsed property.
   */
  private var _collapsed:Boolean = false;
  /**
   * @private
   * Dirty flag for the collapse property.
   */
  private var collapsedChanged:Boolean = false;

  /**
   * If <code>true</code>, the component is in its minimized state.
   */
  public function get collapsed():Boolean {
    return _collapsed;
  }

  /**
   * @private
   */
  public function set collapsed(value:Boolean):void {
    if (_collapsed == value) {
      return;
    }

    _collapsed = value;
    collapsedChanged = true;

    invalidateSize();
    invalidateDisplayList();
  }

  // --------------------------------------------------------------------------
  //
  // Overridden methods
  //
  // --------------------------------------------------------------------------
  /**
   * @private
   */
  override protected function createChildren():void {
    // Prevent the panel from wasting cycles/memory on the close button
    if (!closeButton) {
      closeButton = new Button();
    }

    super.createChildren();

    // Configure the titleBar to receive click events.
    if (titleTextField) {
      titleTextField.addEventListener(MouseEvent.CLICK, titleTextField_clickHandler);
    }
  }

  /**
   * @private
   */
  override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
    super.updateDisplayList(unscaledWidth, unscaledHeight);

    if (collapsedChanged) {
      collapsedChanged = false;

      if (_collapsed) {
        // Store expanded values
        originalVScrollPolicy = verticalScrollPolicy;
        expandedHeight = unscaledHeight;

        verticalScrollPolicy = ScrollPolicy.OFF;
        tween.heightTo = getHeaderHeight();
      } else {
        tween.heightTo = expandedHeight;
      }

      if (tween.isPlaying) {
        tween.stop();
      }

      if (getStyle("collapseDuration")) {
        tween.duration = getStyle("collapseDuration") as Number;
      }

      //tween.hideChildrenTargets = [this];
      tween.play();
    }
  }

  /**
   * @private
   * Handles user click on the header text.
   */
  private function titleTextField_clickHandler(event:MouseEvent):void {
    if (!(enabled && isCollapsible)) {
      return;
    }

    collapsed = !_collapsed;

    if (_collapsed) {
      dispatchEvent(new Event("minimize"));
    } else {
      dispatchEvent(new Event("restore"));
    }
  }

  private function tween_effectEndHandler(event:EffectEvent):void {
    verticalScrollPolicy = originalVScrollPolicy;
  }


  /**
   * Gets whether the panel is actually collapsible. This is true by default.
   */
  public function get isCollapsible():Boolean {
    return _isCollapsible;
  }

  /**
   * Sets whether the panel is actually collapsible. This is true by default.
   */
  public function set isCollapsible(value:Boolean):void {
    _isCollapsible = value;
  }
}
}
