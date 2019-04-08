/*
 * Copyright (c) 2005-2017 Vincent Vandenschrick. All rights reserved.
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
 * A Widget showing an OpenStreetMap map.
 */
qx.Mixin.define("org.jspresso.framework.view.qx.MMapMixin", {

  construct: function () {
    this.__geolocationEnabled = qx.core.Environment.get("html.geolocation");
  },

  events: {
    changeZoom: "qx.event.type.Data"
  },

  members: {
    __map: null,
    __geolocationEnabled: false,
    __markersLayer: null,
    __markersSource: null,
    __routesLayer: null,
    __routesSource: null,
    __zonesLayer: null,
    __zonesSource: null,
    __overlays: [],


    _initializeMap: function () {
      if (this.__geolocationEnabled) {
        this.__initGeoLocation();
      }
      this.__createMap();
    },


    /**
     * Calls a redraw of the map. Needed after orientationChange event
     * and drawing markers.
     */
    _redrawMap: function () {
      if (this.__map !== null) {
        qx.event.Timer.once(function () {
          this.__map.updateSize();
        }, this, 10);
      }
    },

    __createMap: function () {
      this.__map = new ol.Map({target: this._getMapDomTarget()});

      var osmLayer = new ol.layer.Tile({
        source: new ol.source.OSM()
      });
      this.__map.addLayer(osmLayer);

      var zoneStyle = new ol.style.Style({
        stroke: new ol.style.Stroke({
          color: '#3dadff',
          width: 4
        })
      });
      var zoneFeature = new ol.Feature({
        geometry: new ol.geom.Polygon([[0, 0]])
      });
      this.__zonesSource = new ol.source.Vector({
        features: [zoneFeature]
      });
      this.__zonesLayer = new ol.layer.Vector({
        source: this.__zonesSource,
        style: zoneStyle
      });
      this.__map.addLayer(this.__zonesLayer);

      var markerStyle = new ol.style.Style({
        image: new ol.style.Icon(/** @type {olx.style.IconOptions} */ {
          anchor: [0.5, 1],
          anchorXUnits: 'fraction',
          anchorYUnits: 'fraction',
          opacity: 0.65,
          src: qx.util.ResourceManager.getInstance().toUri("org/jspresso/framework/map_marker.svg")
        })
      });
      var markerFeature = new ol.Feature({
        geometry: new ol.geom.Point(ol.proj.fromLonLat([0, 0]))
      });
      this.__markersSource = new ol.source.Vector({
        features: [markerFeature]
      });
      this.__markersLayer = new ol.layer.Vector({
        source: this.__markersSource,
        style: markerStyle
      });
      this.__map.addLayer(this.__markersLayer);

      var routeStyle = new ol.style.Style({
        stroke: new ol.style.Stroke({
          color: '#3dadff',
          width: 4
        })
      });
      var routeFeature = new ol.Feature({
        geometry: new ol.geom.LineString([[0, 0]])
      });
      this.__routesSource = new ol.source.Vector({
        features: [routeFeature]
      });
      this.__routesLayer = new ol.layer.Vector({
        source: this.__routesSource,
        style: routeStyle
      });
      this.__map.addLayer(this.__routesLayer);

      var mapView = new ol.View({
        center: ol.proj.fromLonLat([0, 0]),
        zoom: 12
      });
      //mapView.on("change:resolution", function (event) {
      this.__map.on("moveend", function (event) {
        this.fireDataEvent("changeZoom", Math.round(mapView.getZoom()));
      }, this);
      /*
            var markerSelect = new ol.interaction.Select();
            this.__map.addInteraction(markerSelect);
            markerSelect.on('select', function (evt) {
              if (evt.selected && evt.selected.length) {
                alert("toto");
              }
            });
            this.__map.on('pointermove', function (evt) {
              this.__map.getTargetElement().style.cursor = this.__map.hasFeatureAtPixel(evt.pixel) ? 'pointer' : '';
            }, this);
      */
      this.__map.setView(mapView);
    },

    _drawMarkers: function (markers, extendsCoordinates) {
      var overlayIndex = 0;
      if (markers && markers.length > 0) {
        var markersFeatures = [];
        for (var i = 0; i < markers.length; i++) {
          var jsonMarker = markers[i];
          var markerNode = ol.proj.fromLonLat(jsonMarker.coord ? jsonMarker.coord : jsonMarker);
          var marker = new ol.Feature({
            geometry: new ol.geom.Point(markerNode)
          });
          if (jsonMarker.image) {
            marker.setStyle(new ol.style.Style({
              image: new ol.style.Icon(jsonMarker.image)
            }));
          }
          if (jsonMarker.htmlDescription) {
            var overlay;
            var popup;
            if (this.__overlays.length - 1 < overlayIndex) {
              popup = new qx.dom.Element.create("div", {
                style: 'font-family:sans-serif;'
              });
              qx.dom.Element.insertEnd(popup, this._getMapDomTarget());
              overlay = new ol.Overlay({
                element: popup,
                autoPan: true,
                offset: [-10, 5],
                autoPanAnimation: {
                  duration: 250
                },
                map: this._map
              });
              this.__overlays.push(overlay);
            } else {
              overlay = this.__overlays[overlayIndex];
              popup = overlay.getElement();
            }
            qx.dom.Element.empty(popup);
            var popupContent = qx.dom.Element.create("div");
            qx.dom.Element.insertEnd(popupContent, popup);
            popupContent.outerHTML = "<div>" + jsonMarker.htmlDescription + "</div>";
            overlay.setPosition(ol.proj.fromLonLat(jsonMarker.coord ? jsonMarker.coord : jsonMarker));
            this.__map.addOverlay(overlay);
            overlayIndex++;
          }
          markersFeatures.push(marker);
          extendsCoordinates.push(markerNode);
        }
        this.__markersSource.clear(true);
        this.__markersSource.addFeatures(markersFeatures);
        this.__markersLayer.setVisible(true);
      } else {
        this.__markersLayer.setVisible(false);
      }
    },

    _drawZones: function (zones, extendsCoordinates) {
      if (zones && zones.length > 0) {
        var zonesFeatures = [];
        for (var i = 0; i < zones.length; i++) {
          var simplifiedInput = false;
          var zoneShape = [];
          var jsonZone = zones[i];
          var jsonZoneCoordinates = jsonZone.shape ? jsonZone.shape : jsonZone;
          for (var j = 0; j < jsonZoneCoordinates.length; j++) {
            var coordinatesOrInnerShape = jsonZoneCoordinates[j];
            if (coordinatesOrInnerShape.length == 2) {
              simplifiedInput = true;
              zoneShape.push(ol.proj.fromLonLat(coordinatesOrInnerShape));
            } else {
              var zoneInnerShape = [];
              for (var k = 0; k < coordinatesOrInnerShape.length; k++) {
                zoneInnerShape.push(ol.proj.fromLonLat(coordinatesOrInnerShape[k]));
              }
              zoneShape.push(zoneInnerShape);
            }
          }
          if (simplifiedInput) {
            zoneShape = [zoneShape];
          }
          var zonePolygon = new ol.Feature({
            geometry: new ol.geom.Polygon(zoneShape)
          });
          if (jsonZone.style) {
            var zoneStyle = {};
            if (jsonZone.style.stroke) {
              zoneStyle["stroke"] = new ol.style.Stroke(jsonZone.style.stroke)
            }
            if (jsonZone.style.fill) {
              zoneStyle["fill"] = new ol.style.Fill(jsonZone.style.fill)
            }
            zonePolygon.setStyle(new ol.style.Style(zoneStyle));

          }
          zonesFeatures.push(zonePolygon);
        }
        this.__zonesSource.clear(true);
        this.__zonesSource.addFeatures(zonesFeatures);
        this.__zonesLayer.setVisible(true);
      } else {
        this.__zonesLayer.setVisible(false);
      }
    },

    _drawRoutes: function (routes, extendsCoordinates) {
      if (routes && routes.length > 0) {
        var routesFeatures = [];
        for (var i = 0; i < routes.length; i++) {
          var routeNodes = [];
          var jsonRoute = routes[i];
          var jsonRouteCoord = jsonRoute.path ? jsonRoute.path : jsonRoute;
          for (var j = 0; j < jsonRouteCoord.length; j++) {
            var routeNode = ol.proj.fromLonLat(jsonRouteCoord[j]);
            routeNodes.push(routeNode);
            extendsCoordinates.push(routeNode);
          }
          var routeSegment = new ol.Feature({
            geometry: new ol.geom.LineString(routeNodes)
          });
          if (jsonRoute.style) {
            routeSegment.setStyle(new ol.style.Style({
              stroke: new ol.style.Stroke(jsonRoute.style)
            }));
          }
          routesFeatures.push(routeSegment);
        }
        this.__routesSource.clear(true);
        this.__routesSource.addFeatures(routesFeatures);
        this.__routesLayer.setVisible(true);
      } else {
        this.__routesLayer.setVisible(false);
      }
    },

    drawMapContent: function (markers, routes, zones, defaultZoom) {
      if (this.__map) {
        var extendsCoordinates = [];
        // cleanup overlays;
        for (var i = 0; i < this.__overlays.length; i++) {
          this.__map.removeOverlay(this.__overlays[i]);
        }

        this._drawZones(zones, extendsCoordinates);
        this._drawMarkers(markers, extendsCoordinates);
        this._drawRoutes(routes, extendsCoordinates);

        if (extendsCoordinates.length > 0) {
          var view = this.__map.getView();
          if (extendsCoordinates.length > 1) {
            view.fit(ol.extent.boundingExtent(extendsCoordinates), this.__map.getSize());
          } else {
            view.setCenter(extendsCoordinates[0]);
            view.setZoom(defaultZoom ? defaultZoom : 12);
          }
        }
      }
    },

    zoomToPosition: function (lonLat, zoom) {
      if (this.__map) {
        var view = this.__map.getView();
        if (lonLat) {
          view.setCenter(ol.proj.fromLonLat(lonLat));
        }
        if (zoom) {
          view.setZoom(zoom);
        }
      }
    },

    /**
     * Prepares qooxdoo GeoLocation and installs needed listeners.
     */
    __initGeoLocation: function () {
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
