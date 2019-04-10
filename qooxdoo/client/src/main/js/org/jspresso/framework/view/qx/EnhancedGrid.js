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

qx.Class.define("org.jspresso.framework.view.qx.EnhancedGrid", {
  extend: qx.ui.layout.Grid,


  construct: function (spacingX, spacingY) {
    this.base(arguments, spacingX, spacingY);
  },

  members: {

    /**
     * Overriden to return a 0 flex value for collapsed panels
     */
    getRowFlex: function (row) {
      if (this.getColumnCount() == 1) {
        var children = this._getLayoutChildren();
        for (var i = 0, l = children.length; i < l; i++) {
          var child = children[i];
          var props = child.getLayoutProperties();
          if (props.row === row) {
            if (child instanceof collapsablepanel.Panel && !child.getValue()) {
              return 0;
            }
            break;
          }
        }
      }
      return this.base(arguments, row);
    }
  }
});
