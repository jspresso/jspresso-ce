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
          var zoneNodes = [];
          var jsonZone = zones[i];
          var jsonZoneCoord = jsonZone.shape ? jsonZone.shape : jsonZone;
          for (var j = 0; j < jsonZoneCoord.length; j++) {
            var zoneNode = ol.proj.fromLonLat(jsonZoneCoord[j]);
            zoneNodes.push(zoneNode);
          }
          var zonePolygon = new ol.Feature({
            geometry: new ol.geom.Polygon([zoneNodes])
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
      /*
                  var geojsonObject = {
                    'type': 'FeatureCollection',
                    'crs': {
                      'type': 'name',
                      'properties': {
                        'name': 'EPSG:3857'
                      }
                    },
                    'features': [{
                      'type': 'Feature',
                      'geometry': {
                        'type': 'Polygon',
                        'coordinates': [[ol.proj.fromLonLat([2.3319, 48.81701]), ol.proj.fromLonLat([2.33247, 48.81825]),
                                         ol.proj.fromLonLat([2.29219, 48.82715]), ol.proj.fromLonLat([2.28359, 48.8308]),
                                         ol.proj.fromLonLat([2.27906, 48.83249]), ol.proj.fromLonLat([2.27542, 48.82951]),
                                         ol.proj.fromLonLat([2.2728, 48.82792]), ol.proj.fromLonLat([2.26754, 48.82779]),
                                         ol.proj.fromLonLat([2.26722, 48.83154]), ol.proj.fromLonLat([2.27005, 48.83301]),
                                         ol.proj.fromLonLat([2.2673, 48.83466]), ol.proj.fromLonLat([2.2628, 48.83392]),
                                         ol.proj.fromLonLat([2.25514, 48.83481]), ol.proj.fromLonLat([2.25171, 48.83882]),
                                         ol.proj.fromLonLat([2.25123, 48.8429]), ol.proj.fromLonLat([2.25248, 48.84548]),
                                         ol.proj.fromLonLat([2.25062, 48.84555]), ol.proj.fromLonLat([2.24237, 48.84766]),
                                         ol.proj.fromLonLat([2.23919, 48.85004]), ol.proj.fromLonLat([2.22566, 48.85302]),
                                         ol.proj.fromLonLat([2.22422, 48.85352]), ol.proj.fromLonLat([2.22452, 48.85654]),
                                         ol.proj.fromLonLat([2.22822, 48.86518]), ol.proj.fromLonLat([2.23174, 48.86907]),
                                         ol.proj.fromLonLat([2.23545, 48.87059]), ol.proj.fromLonLat([2.24109, 48.87243]),
                                         ol.proj.fromLonLat([2.24273, 48.87356]), ol.proj.fromLonLat([2.24569, 48.87643]),
                                         ol.proj.fromLonLat([2.25481, 48.87408]), ol.proj.fromLonLat([2.25541, 48.87426]),
                                         ol.proj.fromLonLat([2.25847, 48.88039]), ol.proj.fromLonLat([2.27749, 48.87797]),
                                         ol.proj.fromLonLat([2.27995, 48.87857]), ol.proj.fromLonLat([2.2809, 48.8828]),
                                         ol.proj.fromLonLat([2.28567, 48.88657]), ol.proj.fromLonLat([2.29151, 48.88948]),
                                         ol.proj.fromLonLat([2.29505, 48.88987]), ol.proj.fromLonLat([2.29863, 48.89172]),
                                         ol.proj.fromLonLat([2.30782, 48.89606]), ol.proj.fromLonLat([2.31246, 48.89778]),
                                         ol.proj.fromLonLat([2.31852, 48.89963]), ol.proj.fromLonLat([2.31988, 48.90046]),
                                         ol.proj.fromLonLat([2.32063, 48.9008]), ol.proj.fromLonLat([2.32726, 48.90107]),
                                         ol.proj.fromLonLat([2.34839, 48.90153]), ol.proj.fromLonLat([2.37029, 48.90165]),
                                         ol.proj.fromLonLat([2.38515, 48.90201]), ol.proj.fromLonLat([2.39033, 48.90101]),
                                         ol.proj.fromLonLat([2.3949, 48.89844]), ol.proj.fromLonLat([2.39762, 48.89458]),
                                         ol.proj.fromLonLat([2.39844, 48.89094]), ol.proj.fromLonLat([2.39891, 48.88563]),
                                         ol.proj.fromLonLat([2.4002, 48.88383]), ol.proj.fromLonLat([2.40409, 48.8814]),
                                         ol.proj.fromLonLat([2.40946, 48.8801]), ol.proj.fromLonLat([2.41239, 48.876]),
                                         ol.proj.fromLonLat([2.41394, 48.87083]), ol.proj.fromLonLat([2.41402, 48.86804]),
                                         ol.proj.fromLonLat([2.41366, 48.863]), ol.proj.fromLonLat([2.41471, 48.85865]),
                                         ol.proj.fromLonLat([2.41634, 48.84924]), ol.proj.fromLonLat([2.41574, 48.84531]),
                                         ol.proj.fromLonLat([2.41357, 48.83826]), ol.proj.fromLonLat([2.41224, 48.83454]),
                                         ol.proj.fromLonLat([2.4135, 48.83372]), ol.proj.fromLonLat([2.41734, 48.83419]),
                                         ol.proj.fromLonLat([2.42214, 48.83579]), ol.proj.fromLonLat([2.42093, 48.83933]),
                                         ol.proj.fromLonLat([2.41953, 48.84145]), ol.proj.fromLonLat([2.41987, 48.84345]),
                                         ol.proj.fromLonLat([2.42184, 48.84444]), ol.proj.fromLonLat([2.42453, 48.84189]),
                                         ol.proj.fromLonLat([2.43354, 48.84105]), ol.proj.fromLonLat([2.43718, 48.84089]),
                                         ol.proj.fromLonLat([2.43796, 48.84467]), ol.proj.fromLonLat([2.44074, 48.84441]),
                                         ol.proj.fromLonLat([2.44075, 48.84596]), ol.proj.fromLonLat([2.44653, 48.84575]),
                                         ol.proj.fromLonLat([2.44641, 48.84493]), ol.proj.fromLonLat([2.45789, 48.84349]),
                                         ol.proj.fromLonLat([2.46226, 48.84254]), ol.proj.fromLonLat([2.46522, 48.84111]),
                                         ol.proj.fromLonLat([2.46971, 48.83658]), ol.proj.fromLonLat([2.46896, 48.83392]),
                                         ol.proj.fromLonLat([2.46569, 48.83181]), ol.proj.fromLonLat([2.46496, 48.83044]),
                                         ol.proj.fromLonLat([2.46457, 48.82766]), ol.proj.fromLonLat([2.46618, 48.82733]),
                                         ol.proj.fromLonLat([2.46504, 48.82409]), ol.proj.fromLonLat([2.46286, 48.82018]),
                                         ol.proj.fromLonLat([2.4627, 48.81906]), ol.proj.fromLonLat([2.459, 48.81724]),
                                         ol.proj.fromLonLat([2.45723, 48.81698]), ol.proj.fromLonLat([2.45333, 48.81716]),
                                         ol.proj.fromLonLat([2.4498, 48.81788]), ol.proj.fromLonLat([2.44186, 48.81795]),
                                         ol.proj.fromLonLat([2.43746, 48.81818]), ol.proj.fromLonLat([2.43745, 48.81911]),
                                         ol.proj.fromLonLat([2.43491, 48.81967]), ol.proj.fromLonLat([2.42923, 48.82362]),
                                         ol.proj.fromLonLat([2.42624, 48.82413]), ol.proj.fromLonLat([2.41996, 48.82408]),
                                         ol.proj.fromLonLat([2.41652, 48.82474]), ol.proj.fromLonLat([2.41114, 48.82489]),
                                         ol.proj.fromLonLat([2.4077, 48.82635]), ol.proj.fromLonLat([2.40493, 48.82844]),
                                         ol.proj.fromLonLat([2.40247, 48.82954]), ol.proj.fromLonLat([2.39396, 48.82743]),
                                         ol.proj.fromLonLat([2.38901, 48.82512]), ol.proj.fromLonLat([2.38153, 48.82242]),
                                         ol.proj.fromLonLat([2.38076, 48.8217]), ol.proj.fromLonLat([2.37361, 48.81934]),
                                         ol.proj.fromLonLat([2.36293, 48.81608]), ol.proj.fromLonLat([2.35615, 48.81598]),
                                         ol.proj.fromLonLat([2.35239, 48.81856]), ol.proj.fromLonLat([2.34717, 48.8161]),
                                         ol.proj.fromLonLat([2.33371, 48.81677]), ol.proj.fromLonLat([2.3319, 48.81701])]]
                      }
                    }]
                  };
                  var geoJSON = new ol.format.GeoJSON();
                  var shapesSources = new ol.source.Vector({
                    features: geoJSON.readFeatures(geojsonObject)
                  });
                  var shapesLayer = new ol.layer.Vector({
                    source: shapesSources
                  });
                  this.__map.addLayer(shapesLayer);
            */
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
