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

qx.Class.define("org.jspresso.framework.view.qx.EnhancedCollapsiblePanel", {
  extend: collapsablepanel.Panel,

  statics: {
    __MARKER_CHAR : "\u25B9"
  },

  construct: function (label, layout) {
    this.base(arguments, label, layout);
  },

  properties: {

    collapsible: {
      check: "Boolean",
      init: true,
      apply: "_applyCollapsible",
      event: "changeCollapsible"
    },

    displayMarker: {
      check: "Boolean",
      init: true,
      nullable: true,
      apply: "_applyDisplayMarker"
    }
  },

  members: {

    addToBar: function(widget) {
      this.getChildControl("barContainer").add(widget);
    },

    _applyValue: function (value, old) {
      this.getChildControl("barContainer");
      this._applyCaption(this.getCaption());
      this.base(arguments, value, old);
    },

    _applyCaption: function (caption) {
      if (this.isDisplayMarker() && this.isCollapsible()) {
        var refinedCaption;
        if (this.getValue()) {
          refinedCaption = caption;
        } else {
          refinedCaption = caption + " " + org.jspresso.framework.view.qx.EnhancedCollapsiblePanel.__MARKER_CHAR;
        }
        this.base(arguments, refinedCaption);
      } else {
        this.base(arguments, caption);
      }
    },

    _applyCollapsible: function (collapsible) {
      this._applyCaption(this.getCaption());
    },

    _applyDisplayMarker: function (displayMarker) {
      this._applyCaption(this.getCaption());
    },

    // overridden
    // Condition toggle state based on collapsible flag.
    _createChildControlImpl: function (id) {
      var control;
      switch (id) {
        case "barContainer":
          control = new qx.ui.container.Composite(new qx.ui.layout.HBox());
          control.add(this.getChildControl("bar"), {flex: 0});
          this._add(control, {flex: 0});
          break;
        case "bar":
          control = new qx.ui.basic.Atom(this.getCaption());
          control.addListener("click", function (e) {
            if (this.getCollapsible()) {
              // do not close an open panel that belongs to an accordion.
              if (!this.getGroup() || !this.getValue()) {
                this.toggleValue();
              }
            }
          }, this);
          break;
      }
      return control || this.base(arguments, id);
    }
  }
});
