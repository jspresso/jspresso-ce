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

qx.Class.define("org.jspresso.framework.view.qx.RComponentHeaderRenderer", {
  extend: qx.ui.table.headerrenderer.Default,


  /**
   *
   * @param viewFactory {org.jspresso.framework.view.qx.DefaultQxViewFactory}
   * @param rComponent {org.jspresso.framework.gui.remote.RComponent}
   * @param table {qx.ui.table.Table}
   */
  construct: function (table, viewFactory, rComponent) {
    this.base(arguments);
    this.__table = table;
    this.__viewFactory = viewFactory;
    this.__rComponent = rComponent;
  },


  members: {
    __table: null,
    __viewFactory: null,
    __rComponent: null,

    createHeaderCell: function (cellInfo) {
      var widget = new org.jspresso.framework.view.qx.MultilineHeaderCell();
      this.updateHeaderCell(cellInfo, widget);
      this.__table.syncAppearance();
      this.__viewFactory.applyComponentStyle(widget, this.__rComponent);
      var heightHint = widget.getSizeHint()["height"];
      if (this.__table.getHeaderCellHeight() < heightHint) {
        this.__table.setHeaderCellHeight(heightHint);
      }
      if (this.__rComponent.getIcon()) {
        widget.setIcon(this.__rComponent.getIcon().getImageUrlSpec());
      }
      return widget;
    }
  }
});
