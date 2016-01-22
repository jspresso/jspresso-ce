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

qx.Class.define("org.jspresso.framework.view.qx.EnhancedListDelegate", {
  extend: qx.core.Object,

  members: {

    /**
     * @param item
     *            {qx.ui.core.Widget}
     */
    configureItem: function (item) {
      var toolTip = new qx.ui.tooltip.ToolTip();
      toolTip.setRich(true);
      item.setToolTip(toolTip);
      item.setRich(true);
    },

    /**
     * @param controller
     *            {qx.data.controller.List}
     * @param item
     *            {qx.ui.core.Widget}
     * @param id
     *            {integer}
     */
    bindItem: function (controller, item, id) {
      controller.bindDefaultProperties(item, id);
      controller.bindProperty("value", "toolTip.label", null, item, id);
    }
  }
});
