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

    setDataProvider: function (value) {
      if (this.__dataProvider) {
        this.__dataProvider.removeListener("change", this.__rebindDataProvider, this);
        this.__dataProvider = null;
      }

      this.__dataProvider = value;

      if (this.__dataProvider) {
        this.__dataProvider.addListener("change", this.__rebindDataProvider, this);
      }
      this.__rebindDataProvider();
    },

    __rebindDataProvider: function (evt) {
      this.__container.removeAll();
      for (var i = 0; i < this.__dataProvider.length; i++) {
        var state = this.__dataProvider.getItem(i);
        var component = this.__componentTank[state.getGuid()];
        if (!component) {
          var stateMapping = {};
          this.__mapStates(this.__remoteRepeater.getViewPrototype(), state, stateMapping);
          this.__remoteRepeater.transferToState(stateMapping);
          component = this.__viewFactory.createComponent(this.__remoteRepeater.getRepeated(), false);
          this.__componentTank[state.getGuid()] = component;
          component.addListener("tap", function (evt) {
            var index = this.__container.getChildren().indexOf(evt.getCurrentTarget());
            if (index >= 0) {
              this.__remoteRepeater.getState().setSelectedIndices([index]);
              this.__remoteRepeater.getState().setLeadingIndex(index);
            } else {
              this.__remoteRepeater.getState().setSelectedIndices([]);
              this.__remoteRepeater.getState().setLeadingIndex(-1);
            }
          }, this);
          if (this.__remoteRepeater.getRowAction()) {
            component.addListener("dbltap", function (evt) {
              this.__actionHandler.execute(this.__remoteRepeater.getRowAction());
            }, this);
          }
        }
        this.__container.add(component);
      }
    },

    __mapStates: function (fromState, toState, stateMapping) {
      stateMapping[fromState.getGuid()] = toState;
      if (fromState instanceof org.jspresso.framework.state.remote.RemoteCompositeValueState) {
        var fromChildren = fromState.getChildren();
        for (var i = 0; i < fromChildren.length; i++) {
          var toChildren = toState.getChildren();
          if (i < toChildren.length) {
            this.__mapStates(fromChildren.getItem(i), toChildren.getItem(i), stateMapping);
          }
        }
      }
    }

  }
});
