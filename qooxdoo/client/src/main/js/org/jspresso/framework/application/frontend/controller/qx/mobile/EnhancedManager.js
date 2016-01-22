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
/**
 * @asset (org/jspresso/framework/mobile/nav-mobile-menu-icon.png)
 */

qx.Class.define("org.jspresso.framework.application.frontend.controller.qx.mobile.EnhancedManager", {

  extend: qx.ui.mobile.page.Manager,

  implement: [
  ],

  statics: {
  },

  /**
   * @param isTablet {Boolean?} flag which triggers the manager to layout for tablet (or big screens/displays) or mobile devices. If parameter is null,
   * qx.core.Environment.get("device.type") is called for decision.
   * @param root {qx.ui.mobile.core.Widget?} widget which should be used as root for this manager.
   */
  construct: function (isTablet, root) {
    this.base(arguments, isTablet, root);
  },

  members: {
    /**
     * Factory method for the master button, which is responsible for showing/hiding masterContainer.
     * @return {qx.ui.mobile.navigationbar.Button}
     */
    _createMasterButton: function () {
      var mb = this.base(arguments);
      mb.setIcon("org/jspresso/framework/mobile/nav-mobile-menu-icon.png");
      mb.setShow("icon");
      return mb;
    }
  }
});
