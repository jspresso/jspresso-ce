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
qx.Class.define("org.jspresso.framework.view.qx.mobile.RepeaterContainer", {
  extend: qx.ui.mobile.container.Composite,

  statics: {
  },

  construct : function(remoteCompositeValueState) {
    this.base(arguments);
    this.setLayout(new qx.ui.mobile.layout.VBox())
    this.__state = remoteCompositeValueState;
    this.__rebindSelection();
  },

  events: {
    /**
     * Fired when the selection is changed.
     */
    changeSelection: "qx.event.type.Data"
  },

  members: {
    /** @type {org.jspresso.framework.state.remote.RemoteCompositeValueState} */
    __state: null,
    __armed: false,

    getModel: function () {
      return this.__state.getChildren();
    },

    __rebindSelection: function() {
      this.__state.addListener("changeLeadingIndex", function (event) {
        if (event.getData() >= 0) {
          if (!this.__armed) {
            this.__armed = true;
          } else {
            this.fireDataEvent("changeSelection", event.getData(), event.getOldData());
          }
        }
      }, this);
    }
  }
});
