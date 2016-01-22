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

qx.Class.define("org.jspresso.framework.view.qx.BooleanTableCellRenderer", {
  extend: qx.ui.table.cellrenderer.Boolean,
  include: [org.jspresso.framework.view.qx.MCellAdditionalStyle],


  construct: function () {
    this.base(arguments);
  },


  members: {
    // overridden
    _identifyImage: function (cellInfo) {
      //noinspection RedundantIfStatementJS
      if (cellInfo.value) {
        cellInfo.value = true;
      } else {
        cellInfo.value = false;
      }
      return this.base(arguments, cellInfo);
    },

    _getCellStyle: function (cellInfo) {
      var superStyle = this.base(arguments, cellInfo);
      superStyle += this._getAdditionalCellStyle(cellInfo);
      var pattern = /padding-top:[\d|\.]*px;/g;
      return superStyle.replace(pattern, "") + "padding-top:7px;"
    }
  }
});
