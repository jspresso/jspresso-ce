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

  construct: function (label, layout) {
    this.base(arguments, label, layout);
  },

  properties: {

    collapsible: {
      check: "Boolean",
      init: true,
      event: "changeCollapsible"
    }
  },

  members: {

    // overridden
    // Condition toggle state based on collapsible flag.
    _createChildControlImpl: function (id) {
      var control = this.base(arguments, id);

      if (id == "bar") {
        control.removeListener("click", this.toggleValue, this);
        control.addListener("click", function (e) {
          if (this.getCollapsible()) {
            // do not close an open panel that belongs to an accordion.
            if (!this.getGroup() || !this.getValue()) {
              this.toggleValue();
            }
          }
        }, this);
      }
      return control;
    }

  }
});
