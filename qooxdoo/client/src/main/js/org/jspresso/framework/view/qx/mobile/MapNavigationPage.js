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

  construct: function () {
    this.base(arguments, false);
    this._geolocationEnabled = qx.core.Environment.get("html.geolocation");
  },


  members: {
    __mapUri: "http://www.openlayers.org/api/OpenLayers.js",
    // __mapUri: "file:///Applications/envdev/OpenLayers-2.13.1/OpenLayers.debug.js",
    __map: null,
    __markers: null,
    __myPositionMarker: null,
    __mapnikLayer: null,
    __geolocationEnabled: false,
    __bufferredPosition: null,
    __mapDivId: null,


    // overridden
    _initialize: function () {
      this.base(arguments);

      if (this.__geolocationEnabled) {
        this._initGeoLocation();
      }

      this._loadMapLibrary();

      // Listens on window orientation change and resize, and triggers redraw of map.
      // Needed for triggering OpenLayers to use a bigger area, and draw more tiles.
      qx.event.Registration.addListener(window, "orientationchange", this._redrawMap, this);
      qx.event.Registration.addListener(window, "resize", this._redrawMap, this);
    },


    /**
     * Calls a redraw on Mapnik Layer. Needed after orientationChange event
     * and drawing markers.
     */
    _redrawMap: function () {
      if (this.__mapnikLayer !== null) {
        this.__map.updateSize();
        this.__mapnikLayer.redraw();
      }
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

    /**
     * Loads JavaScript library which is needed for the map.
     */
    _loadMapLibrary: function () {
      var req = new qx.bom.request.Script();

      req.onload = function () {
        this.__map = new OpenLayers.Map(this.__mapDivId);
        this.__mapnikLayer = new OpenLayers.Layer.OSM("mapnik", null, {});

        this.__map.addLayer(this.__mapnikLayer);

        this._zoomToDefaultPosition();
      }.bind(this);

      req.open("GET", this.__mapUri);
      req.send();
    },


    /**
     * Zooms the map to a default position.
     * In this case: Paris, France.
     */
    _zoomToDefaultPosition: function () {
      if (this.isVisible()) {
        if (this.__bufferredPosition) {
          this.zoomToPosition(this.__bufferredPosition["longitude"], this.__bufferredPosition["latitude"],
              this.__bufferredPosition["zoom"], this.__bufferredPosition["showMarker"]);
          this.__bufferredPosition = null;
        } else {
          this.zoomToPosition(2.3470, 48.8590, 12);
        }
      }
    },


    /**
     * Zooms the map to a  position.
     * @param longitude {Number} the longitude of the position.
     * @param latitude {Number} the latitude of the position.
     * @param zoom {Integer} zoom level.
     * @param showMarker {Boolean} if a marker should be drawn at the defined position.
     */
    zoomToPosition: function (longitude, latitude, zoom, showMarker) {
      if (this.__map) {
        var fromProjection = new OpenLayers.Projection("EPSG:4326");
        var toProjection = new OpenLayers.Projection("EPSG:900913");
        var mapPosition = new OpenLayers.LonLat(longitude, latitude).transform(fromProjection, toProjection);

        this.__map.setCenter(mapPosition, zoom);

        if (showMarker === true) {
          this.setMarkerOnMap(this.__map, mapPosition);
        }
      } else {
        this.__bufferredPosition = {
          longitude: longitude,
          latitude: latitude,
          zoom: zoom,
          showMarker: showMarker
        }
      }
    },


    /**
     * Draws a marker on the OSM map.
     * @param map {Object} the map object.
     * @param mapPosition {Map} the map position.
     */
    setMarkerOnMap: function (map, mapPosition) {
      if (this.__markers === null) {
        this.__markers = new OpenLayers.Layer.Markers("Markers");
        map.addLayer(this.__markers);
      }

      if (this.__myPositionMarker !== null) {
        this.__markers.removeMarker(this.__myPositionMarker);
      }

      var size = new OpenLayers.Size(21, 25);
      var offset = new OpenLayers.Pixel(-(size.w / 2), -size.h);
      var icon = new OpenLayers.Icon('http://www.openlayers.org/dev/img/marker.png', size, offset);

      this.__myPositionMarker = new OpenLayers.Marker(mapPosition, icon);

      this.__markers.addMarker(this.__myPositionMarker);
    },


    /**
     * Prepares qooxdoo GeoLocation and installs needed listeners.
     */
    _initGeoLocation: function () {
      var geo = qx.bom.GeoLocation.getInstance();
      geo.addListener("position", this._onGeolocationSuccess, this);
      geo.addListener("error", this._onGeolocationError, this);
    },


    /**
     * Callback function when Geolocation did work.
     */
    _onGeolocationSuccess: function (position) {
      this.zoomToPosition(position.getLongitude(), position.getLatitude(), 12, true);

      this._redrawMap();
    },


    /**
     * Callback function when GeoLocation returned an error.
     */
    _onGeolocationError: function () {
      var buttons = [];
      buttons.push(qx.locale.Manager.tr("OK"));
      var title = "Problem with Geolocation";
      var text = "Please activate location services on your browser and device.";
      qx.ui.mobile.dialog.Manager.getInstance().confirm(title, text, function () {
      }, this, buttons);
    },


    /**
     * Retrieves GeoPosition out of qx.bom.GeoLocation and zooms to this point on map.
     */
    moveToCurrentPosition: function () {
      var geo = qx.bom.GeoLocation.getInstance();
      geo.getCurrentPosition(false, 1000, 1000);
    }
  }
});
