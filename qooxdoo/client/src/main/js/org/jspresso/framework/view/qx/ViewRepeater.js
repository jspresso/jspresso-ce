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

qx.Class.define("org.jspresso.framework.view.qx.ViewRepeater", {
  extend: qx.core.Object,

  construct: function (container, remoteRepeater, viewFactory, actionHandler) {
    this.base(arguments);
    this.__container = container;
    this.__remoteRepeater = remoteRepeater;
    this.__viewFactory = viewFactory;
    this.__actionHandler = actionHandler;
    this.__componentTank = {};
  },

  members: {
    /** @type {qx.ui.container.Composite} */
    __container: null,
    /** @type {org.jspresso.framework.gui.remote.RRepeater} */
    __remoteRepeater: null,
    /** @type {org.jspresso.framework.view.qx.AbstractQxViewFactory} */
    __viewFactory: null,
    /** @type {org.jspresso.framework.action.IActionHandler} */
    __actionHandler: null,
    /** @type {qx.data.Array} */
    __dataProvider: null,
    /** @type {Object} */
    __componentTank: null,
    /** @type {Boolean} */
    __forceRebind: true,

    setDataProvider: function (value) {
      if (this.__dataProvider) {
        this.__dataProvider.removeListener("change", this.__rebindDataProvider, this);
        this.__dataProvider = null;
      }

      this.__dataProvider = value;

      if (this.__dataProvider) {
        this.__dataProvider.addListener("change", this.__rebindDataProvider, this);
      }
    },

    __rebindDataProvider: function (evt) {
      this.__container.removeAll();
      for (var i = 0; i < this.__dataProvider.length; i++) {
        var state = this.__dataProvider.getItem(i);
        var component = this.__componentTank[state.getGuid()];
        this.__container.add(component);
      }
    },

    addRepeated: function (newSections) {
      var rebind = this.__componentTank.length == 0;
      for (var i = 0; i < newSections.length; i++) {
        var newRemoteSection = newSections[i];
        var newSection = this.__viewFactory.createComponent(newRemoteSection);
        this.__componentTank[newRemoteSection.getState().getGuid()] = newSection;
        newSection.addListener("tap", function (evt) {
          var index = this.__container.getChildren().indexOf(evt.getCurrentTarget());
          if (index >= 0) {
            this.__remoteRepeater.getState().setLeadingIndex(index);
            this.__remoteRepeater.getState().setSelectedIndices([index]);
          } else {
            this.__remoteRepeater.getState().setLeadingIndex(-1);
            this.__remoteRepeater.getState().setSelectedIndices([]);
          }
        }, this);
        if (this.__remoteRepeater.getRowAction()) {
          newSection.addListener("dbltap", function (evt) {
            this.__actionHandler.execute(this.__remoteRepeater.getRowAction());
          }, this);
        }
      }
      if (this.__forceRebind) {
        this.__forceRebind = false;
        this.__rebindDataProvider();
      }
    }
  }
});
