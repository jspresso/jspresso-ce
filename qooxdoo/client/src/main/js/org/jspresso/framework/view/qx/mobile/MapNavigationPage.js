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
 * Mobile page showing an OpenStreetMap map.
 *
 * @ignore(OpenLayers.*)
 * @asset(qx/mobile/css/*)
 */
qx.Class.define("org.jspresso.framework.view.qx.mobile.MapNavigationPage", {
  extend: qx.ui.mobile.page.NavigationPage,

  include: [org.jspresso.framework.view.qx.MMapMixin],

  construct: function () {
    this.base(arguments, false);
  },


  members: {
    __mapDivId: null,

    // overridden
    _initialize: function () {
      this.base(arguments);
      this._initializeMap();
      // Listens on window orientation change and resize, and triggers redraw of map.
      // Needed for triggering OpenLayers to use a bigger area, and draw more tiles.
      qx.event.Registration.addListener(window, "orientationchange", this.__redrawMap, this);
      qx.event.Registration.addListener(window, "resize", this.__redrawMap, this);
      this.addListenerOnce("changeVisibility", this.__redrawMap, this);
    },

    // overridden
    _createContent: function () {
      return null;
    },

    // overridden
    _createScrollContainer: function () {
      // MapContainer
      var layout = new qx.ui.mobile.layout.VBox().set({
        alignX: "center",
        alignY: "middle"
      });

      var mapContainer = new qx.ui.mobile.container.Composite(layout);
      this.__mapDivId = mapContainer.getId();

      return mapContainer;
    },

    _getMapDomTarget: function () {
      return this.__mapDivId;
    },

    showMap: function () {
      var scroll = this._getScrollContainer();
      if (scroll) {
        scroll.show();
      }
    },

    hideMap: function () {
      var scroll = this._getScrollContainer();
      if (scroll) {
        this._getScrollContainer().hide();
      }
    }
  }
});
