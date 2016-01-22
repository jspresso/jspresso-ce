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
package org.openscales.core.layer {

import flash.display.Sprite;
import flash.events.Event;
import flash.events.TimerEvent;
import flash.filters.ColorMatrixFilter;
import flash.utils.Timer;

import org.openscales.core.Map;
import org.openscales.core.basetypes.Resolution;
import org.openscales.core.events.LayerEvent;
import org.openscales.core.events.MapEvent;
import org.openscales.core.layer.originator.DataOriginator;
import org.openscales.core.security.ISecurity;
import org.openscales.core.security.events.SecurityEvent;
import org.openscales.geometry.Geometry;
import org.openscales.geometry.basetypes.Bounds;
import org.openscales.geometry.basetypes.Location;
import org.openscales.geometry.basetypes.Pixel;
import org.openscales.geometry.basetypes.Size;
import org.openscales.proj4as.Proj4as;
import org.openscales.proj4as.ProjProjection;

/**
 * @eventType org.openscales.core.events.LayerEvent.LAYER_CHANGED_ORIGINATORS
 */
[Event(name="openscales.layerChangeOriginators", type="org.openscales.core.events.LayerEvent")]

/**
 * @eventType org.openscales.core.events.LayerEvent.LAYER_PROJECTION_CHANGED
 */
[Event(name="openscales.layerProjectionChanged", type="org.openscales.core.events.LayerEvent")]

/**
 * @eventType org.openscales.core.events.LayerEvent.LAYER_VISIBLE_CHANGED
 */
[Event(name="openscales.layerVisibilityChanged", type="org.openscales.core.events.LayerEvent")]

/**
 * @eventType org.openscales.core.events.LayerEvent.LAYER_OPACITY_CHANGED
 */
[Event(name="openscales.layerOpacityChanged", type="org.openscales.core.events.LayerEvent")]

/**
 * @eventType org.openscales.core.events.LayerEvent.LAYER_LOAD_START
 */
[Event(name="openscales.layerloadstart", type="org.openscales.core.events.LayerEvent")]

/**
 * @eventType org.openscales.core.events.LayerEvent.LAYER_LOAD_END
 */
[Event(name="openscales.layerloadend", type="org.openscales.core.events.LayerEvent")]

/**
 * @eventType org.openscales.core.events.LayerEvent.LAYER_DISPLAY_IN_LAYERMANAGER_CHANGED
 */
[Event(name="openscales.displayInLayerManagerChanged", type="org.openscales.core.events.LayerEvent")]


/**
 * A Layer displays raster (image) of vector datas on the map, usually loaded from a remote datasource.
 * Unit of the baseLayer is managed by the projection.
 * To access : ProjProjection.getProjProjection(layer.projection).projParams.units
 *
 * @author Bouiaw
 */ public class Layer extends Sprite {
  public static const DEFAULT_DPI:Number = 92;

  public static const DEFAULT_NOMINAL_RESOLUTION:Resolution = new Resolution(1.40625, Geometry.DEFAULT_SRS_CODE);
  public static const RESOLUTION_TOLERANCE:Number = 0.000001;
  public static const DEFAULT_NUM_ZOOM_LEVELS:uint = 18;
  public static const DEFAULT_PROJECTION:ProjProjection = ProjProjection.getProjProjection(Geometry.DEFAULT_SRS_CODE);

  private var _identifier:String;
  private var _displayedName:String;
  private var _abstract:String;
  private var _map:Map = null;
  protected var _projection:ProjProjection = null;
  private var _availableProjections:Vector.<String> = null;
  private var _dpi:Number = Layer.DEFAULT_DPI;
  private var _resolutions:Array = null;
  private var _maxExtent:Bounds = null;
  private var _minResolution:Resolution = null;
  private var _maxResolution:Resolution = null;
  private var _proxy:String = null;
  private var _isFixed:Boolean = false;
  private var _security:ISecurity = null;
  private var _loading:Boolean = false;
  protected var _autoResolution:Boolean = true;
  private var _selected:Boolean = false;
  private var _metaData:Object = null;
  private var _constraints:Vector.<Constraint> = null;
  private var _aggregate:Aggregate = null;
  protected var _initialized:Boolean = false;

  protected var _resolutionChanged:Boolean = false;
  protected var _centerChanged:Boolean = false;
  protected var _projectionChanged:Boolean = false;
  protected var _mapReload:Boolean = false;
  private var _available:Boolean = false;

  private var _grayScaleTimer:Timer = null;
  private var _grayScaleFilter:ColorMatrixFilter = null;

  /**
   * Layer constructor
   *
   * @param identifier A unique identifier for this layer (if empty string or null, value will be 'NewLayer_'+new Date().time).
   * @param visibility A boolean to indicate if the layer is set visible or not (if not passed, default value is true)
   */
  public function Layer(identifier:String, visibility:Boolean = true) {
    //this.name = identifier;
    if (!identifier || identifier == "")this._identifier = "NewLayer_"
        + new Date().time; else this._identifier = identifier;

    this._displayedName = this._identifier;
    this.name = this._identifier;

    this.visible = visibility;
    this.doubleClickEnabled = true;
    this._projection = Layer.DEFAULT_PROJECTION;
    this.generateResolutions();

    this._originators = new Vector.<DataOriginator>();

    // Build default grayscale matrix
    var matrix:Array = [];
    matrix = matrix.concat([0.21, 0.72, 0.125, 0, 0]);// red
    matrix = matrix.concat([0.21, 0.72, 0.125, 0, 0]);// green
    matrix = matrix.concat([0.21, 0.72, 0.125, 0, 0]);// blue
    matrix = matrix.concat([0, 0, 0, 1, 0]);// alpha
    this._grayScaleFilter = new ColorMatrixFilter(matrix);
  }

  /**
   * This method tells if the layer is available for the specified bounds and resolution
   * @param Bounds Bounds to intersect the layers bboxes with
   * @param Resolution Resolution to intersect the layer max et min extent with
   */
  public function isAvailableForBounds(bounds:Bounds, resolution:Resolution):Boolean {

    var reprojectedResolution:Number = resolution.reprojectTo(this.projection).value;

    if (!hasMultiBBoxes()) {
      return (this.maxExtent.getIntersection(bounds) != null && (reprojectedResolution <= this.maxResolution.value)
      && (reprojectedResolution >= this.minResolution.value));
    } else {
      var constraint:Constraint;
      var multiBBoxConstraint:MultiBoundingBoxConstraint;
      var bbox:Bounds;
      var WGS84Resolution:Number = resolution.reprojectTo("WGS84").value;
      var multiBboxMaxRes:Resolution;
      var multiBboxMinRes:Resolution;
      for each(constraint in this.constraints) {
        if (!constraint is MultiBoundingBoxConstraint) continue;
        multiBBoxConstraint = constraint as MultiBoundingBoxConstraint;

        for each(bbox in multiBBoxConstraint.bboxes) {
          if (bbox.getIntersection(bounds) != null && (WGS84Resolution <= multiBBoxConstraint.maxResolution.value)
              && (WGS84Resolution >= multiBBoxConstraint.minResolution.value)) return true;
        }
      }

      return false;
    }
  }

  /**
   * The boolean that say if the layer is available or not (according to map)
   *
   * Computed by the checkAvailability method
   * The setter is for internal use, if you set it mannualy it may not be displayed properly
   */
  public function get available():Boolean {
    return this._available;
  }

  /**
   * @private
   */
  public function set available(value:Boolean):void {
    var oldAvailable:Boolean = this._available;
    this._available = value;
    if (value != oldAvailable && this.map) {
      var evt:LayerEvent = new LayerEvent(LayerEvent.LAYER_AVAILABILITY_CHANGED, this);
      this.map.dispatchEvent(evt);
    }
  }

  /**
   * This method return the new availability of the layer.
   * Override this method if you want to change the way the availability is computed
   */
  protected function checkAvailability():Boolean {
    if (map)
      return isAvailableForBounds(this._map.extent, this._map.resolution); else
      return false;
  }

  /**
   * This method tells if the layer supports a projection given in parameter according to the availableProjections
   * set for this layer.
   *
   * @param compareProj A ProjProjection object or a String representing the SRS code of the projection (eg.: "EPSG:4326")
   */
  public function supportsProjection(compareProj:Object):Boolean {
    var proj1:ProjProjection = ProjProjection.getProjProjection(compareProj);
    if (!proj1)
      return false;
    //Parsing available projections for the layer
    if (this.availableProjections) {
      var proj2:ProjProjection;
      for each(var proj:String in this.availableProjections) {
        proj2 = ProjProjection.getProjProjection(proj);
        if (ProjProjection.isEquivalentProjection(proj1, proj2)) {
          if (!ProjProjection.isEquivalentProjection(this.projection, proj1)) {
            //Changing layer projection
            this.setProjection(proj2);
            return true;
          } else {
            //Layer projection is already Map projection
            return true;
          }
        }
      }
    } else {
      //Available projections is not set for this layer
      //try simple comparison
      if (ProjProjection.isEquivalentProjection(this.projection, proj1)) {
        return true;
      }
    }

    return false;
  }

  /**
   * @private
   * The url use for the layer request if necessary.
   * @default null
   */
  protected var _url:String = null;

  /**
   * @private
   * The list of originators for the layer.
   * @default null
   */
  protected var _originators:Vector.<DataOriginator> = null;

  /**
   * @private
   * Indicates if the layer should be displayed in the LayerSwitcher List or not
   * @default true
   */
  protected var _displayInLayerManager:Boolean = true;


  /**
   * Generate resolutions array for a nominal resolution (higher one) and a number of zoom levels.
   * The array is generated with the following principle : resolutions[i] = resolutions[i-1] / 2
   */
  public function generateResolutions(numZoomLevels:uint = Layer.DEFAULT_NUM_ZOOM_LEVELS,
                                      nominalResolution:Number = NaN):void {

    if (isNaN(nominalResolution)) {
      if (this.projection == Layer.DEFAULT_PROJECTION) {
        nominalResolution = Layer.DEFAULT_NOMINAL_RESOLUTION.value;
      } else {
        if (ProjProjection.getProjProjection(this.projection)) {
          nominalResolution = Proj4as.unit_transform(Layer.DEFAULT_PROJECTION, this.projection,
                                                     Layer.DEFAULT_NOMINAL_RESOLUTION.value);
        } else {
          setProjection(Layer.DEFAULT_PROJECTION);
          nominalResolution = Layer.DEFAULT_NOMINAL_RESOLUTION.value;
        }
      }
    }
    // numZoomLevels must be strictly greater than zero
    if (numZoomLevels == 0) {
      numZoomLevels = 1;
    }
    // Generate default resolutions
    this._resolutions = [];
    this._resolutions.push(nominalResolution);
    var i:int = 1;
    for (i; i < numZoomLevels; ++i) {
      this._resolutions.push(this.resolutions[i - 1] / 2);
    }
    this._resolutions.sort(Array.NUMERIC | Array.DESCENDING);

    this._autoResolution = true;
  }

  /**
   * Detroy the map, including removing all event listeners
   */
  public function destroy():void {
    this.removeEventListenerFromMap();
    this.map = null;
  }

  /**
   * Remove map related event listeners
   */
  public function removeEventListenerFromMap():void {
    if (this.map != null) {
      map.removeEventListener(SecurityEvent.SECURITY_INITIALIZED, onSecurityInitialized);
      map.removeEventListener(MapEvent.PROJECTION_CHANGED, onMapProjectionChanged);
      map.removeEventListener(MapEvent.CENTER_CHANGED, onMapCenterChanged);
      map.removeEventListener(MapEvent.RESOLUTION_CHANGED, onMapResolutionChanged);
      map.removeEventListener(MapEvent.MOVE_END, onMapMove);
      map.removeEventListener(MapEvent.RESIZE, onMapResize);
      map.removeEventListener(MapEvent.RELOAD, onMapReload);
      map.removeEventListener(MapEvent.MAX_EXTENT_CHANGED, onMaxExtentChanged);
      map.removeEventListener(Event.ENTER_FRAME, onEnterFrame);
      map.removeEventListener(MapEvent.MAX_EXTENT_CHANGED, onMapMaxExtentChanged);
    }
  }

  /**
   * This function is call when the MapEvent.RESIZE
   * Call the redraw method to fully actualize the layer
   * Override this method if you want a specific behaviour in your layer
   * when the size of the map is changed
   */
  protected function onMapResize(e:MapEvent):void {
    this.redraw(true);
  }

  /**
   * This function is call when the MapEvent.MAX_EXTENT_CHANGED
   * Call the redraw method to fully actualize the layer
   * Override this method if you want a specific behaviour in your layer
   * when the MaxExtent of the map is changed
   */
  protected function onMaxExtentChanged(e:MapEvent):void {
    this.redraw(true);
  }

  /**
   * Set the map where this layer is attached.
   * Here we take care to bring over any of the necessary default properties from the map.
   */
  public function set map(map:Map):void {
    if (this.map != null) {
      removeEventListenerFromMap();
    }

    this._map = map;
    if (this.map) {
      if (_projection == null) {
        this._projection = this._map.projection;
        this.generateResolutions();
      }
      this.map.addEventListener(SecurityEvent.SECURITY_INITIALIZED, onSecurityInitialized);
      this.map.addEventListener(MapEvent.PROJECTION_CHANGED, onMapProjectionChanged);
      this.map.addEventListener(MapEvent.CENTER_CHANGED, onMapCenterChanged);
      this.map.addEventListener(MapEvent.RESOLUTION_CHANGED, onMapResolutionChanged);
      this.map.addEventListener(MapEvent.MOVE_END, onMapMove);
      this.map.addEventListener(MapEvent.RELOAD, onMapReload);
      this.map.addEventListener(MapEvent.RESIZE, onMapResize);
      this.map.addEventListener(MapEvent.MAX_EXTENT_CHANGED, onMaxExtentChanged);
      this.map.addEventListener(Event.ENTER_FRAME, onEnterFrame);
      this.map.addEventListener(MapEvent.MAX_EXTENT_CHANGED, onMapMaxExtentChanged);
      if (!this.maxExtent) {
        this.setMaxExtent(this.map.maxExtent);
      }
    }
    this.available = this.checkAvailability();
  }

  /**
   * Bind the redraw of the layers on the flash onEnterFrame
   */
  protected function onEnterFrame(event:Event):void {
    this.redraw();
  }

  /**
   * Return a reference to the map where belong this layer
   */
  public function get map():Map {
    return this._map;
  }

  /**
   * This function is call when the MapEvent.PROJECTION_CHANGED
   * Override this method if you want a specific behaviour in your layer
   * when the projection of the map is changed
   */
  protected function onMapProjectionChanged(event:MapEvent):void {
    this.available = this.checkAvailability();
    this._projectionChanged = true;
  }

  /**
   * This function is called when the MapEvent.RESOLUTION_CHANGED is dispatched
   * Override this method if you want a specific behaviour in your layer
   * when the resolution of the map is changed
   */
  protected function onMapResolutionChanged(event:MapEvent):void {
    this._resolutionChanged = true;
  }

  /**
   * This function is called when the MapEvent.MAX_EXTENT_CHANGED is dispatched
   * Override this method if you want a specific behaviour in your layer
   * when the map max extent changed.
   */
  protected function onMapMaxExtentChanged(event:MapEvent):void {
    this.available = this.checkAvailability();
  }


  /**
   * This function is called when the MapEvent.RELOAD
   * Override this method if you want a specific behaviour in your layer
   * when the map ask for a reload.
   */
  protected function onMapReload(event:MapEvent):void {
    this.available = this.checkAvailability();
    this._mapReload = true;
  }

  /**
   * This function is called when the MapEvent.CENTER_CHANGED is dispatched
   * Call the redraw function to check if the layer can be displayed
   * Override this method if you want a specific behaviour in your layer
   * when the center is changed
   */
  protected function onMapCenterChanged(event:MapEvent):void {
    this._centerChanged = true;
  }

  protected function onSecurityInitialized(e:SecurityEvent):void {
    this.redraw(true);
  }

  protected function onMapMove(e:MapEvent):void {
    if (this._grayScaleTimer != null || this.filters.length > 0) {
      if (this._grayScaleTimer != null)
        this._grayScaleTimer.removeEventListener(TimerEvent.TIMER, this.onGrayScaleTimerEnd);

      this._grayScaleTimer = new Timer(500, 0);
      // change the display when the delay is spent
      this._grayScaleTimer.addEventListener(TimerEvent.TIMER, this.onGrayScaleTimerEnd);
      this._grayScaleTimer.start();
      this.filters = [];
      this.cacheAsBitmap = false;
    }

    this.redraw(e.zoomChanged);
  }

  private function onGrayScaleTimerEnd(e:TimerEvent):void {
    this.filters = [this._grayScaleFilter];
  }

  /**
   * Indicates the dpi used to calculate resolution and scale upon this layer
   */
  public function get dpi():Number {
    return _dpi;
  }

  /**
   * @Private
   */
  public function set dpi(value:Number):void {
    _dpi = value;
  }

  /**
   * A Bounds object which represents the location bounds of the current extent display on the map.
   */
  public function get extent():Bounds {
    if (this._map)
      return this._map.extent;
    return null;
  }

  /**
   * Return a LonLat which is the passed-in map Pixel, translated into
   * lon/lat by the layer.
   */
  public function getLocationFromMapPx(viewPortPx:Pixel):Location {
    var lonlat:Location = null;
    if (viewPortPx != null) {
      var size:Size = this.map.size;
      var center:Location = this.map.center;
      if (center) {
        var res:Number = this.map.resolution.value;

        var delta_x:Number = viewPortPx.x - (size.w / 2);
        var delta_y:Number = viewPortPx.y - (size.h / 2);

        lonlat = new Location(center.lon + delta_x * res, center.lat - delta_y * res, this.projection);
      }
    }
    return lonlat;
  }

  /**
   * Return a Pixel which is the passed-in LonLat,translated into map pixels.
   */
  public function getMapPxFromLocation(lonlat:Location):Pixel {
    var px:Pixel = null;
    var b:Bounds = this.extent;
    if (lonlat != null && b) {
      var resolution:Number = this.map.resolution.value;
      if (resolution)
        px = new Pixel(Math.round((lonlat.lon - b.left) / resolution), Math.round((b.top - lonlat.lat) / resolution));
    }
    return px;
  }

  /**
   * Force the layer to compute his available boolean
   */
  public function forceAvailabilityChecking():void {
    this.available = this.checkAvailability();
  }

  /**
   * Clear the layer graphics
   */
  public function clear():void {

  }

  /**
   * Reset layer data
   */
  public function reset():void {
  }

  /**
   * Reset layer data
   */
  protected function draw():void {
    // nothing to do for a generic layer
  }

  /**
   * Is this layer currently displayed ?
   */
  public function get displayed():Boolean {
    return this.visible && this.inRange && this.extent;
  }

  /**
   * Check if the layer can be drawn according to the map parameters. If the layer can be drawn
   * it will draw itself.
   *  It will set the available parameter to expose if the layer is drawn or not.
   */
  public function redraw(fullRedraw:Boolean = false):void {
    if (this.map == null)
      return;
    if (this._aggregate) {
      this.visible = this._aggregate.shouldIBeVisible(this, this.map.resolution);
    }
  }

  /**
   * Add a new originator for the layer
   * @param originator Informations of this new originator (with or without constraints)
   * If no constraint is defined, one default constraint is made with the current extent, minResolution and maxResolution of the layer
   */
  public function addOriginator(originator:DataOriginator):void {
    // Is the input originator valid ?
    if (!originator) {
      return;
    }

    var i:uint = 0;
    var j:uint = this._originators.length;
    for (; i < j; ++i) {
      if (originator == this._originators[i]) {
        return;
      }
    }
    // If the constraint is a new constraint, register it
    if (i == j) {
      this._originators.push(originator);
    }
    // Event Originator_list_changed
    if (this._map) {
      this._map.dispatchEvent(new LayerEvent(LayerEvent.LAYER_CHANGED_ORIGINATORS, this));
    }
  }

  public function getLayerPxFromMapPx(mapPx:Pixel):Pixel {
    return new Pixel(mapPx.x - this.x, mapPx.y - this.y);
  }

  public function getMapPxFromLayerPx(layerPx:Pixel):Pixel {
    return new Pixel(layerPx.x + this.x, layerPx.y + this.y);
  }

  /**
   * Tells whether this layer defines multiple bounding boxes
   *
   * @return Boolean True if the layer has multiple bboxes, false otherwise
   */
  public function hasMultiBBoxes():Boolean {
    if (!this.constraints || this.constraints.length == 0) return false;

    var constraint:Constraint = null;

    for each(constraint in this.constraints) {
      if (constraint is MultiBoundingBoxConstraint) return true;
    }

    return false;
  }

  /**
   * Is this layer currently in range, based on its min and max resolutions
   */
  public function get inRange():Boolean {
    var inRange:Boolean = false;
    if (this.map) {
      if (!hasMultiBBoxes()) {
        var resolutionProjected:Resolution = this.map.resolution;

        if (this.projection != this.map.projection) {
          resolutionProjected = this.map.resolution.reprojectTo(this.projection);
        }
        inRange = ((resolutionProjected.value >= this.minResolution.value) && (resolutionProjected.value
        <= this.maxResolution.value));
      } else {
        var WGS84MapRes:Resolution = this.map.resolution.reprojectTo("WGS84");
        var constraint:Constraint = null;
        var multiBBoxConstraint:MultiBoundingBoxConstraint = null;
        var bbox:Bounds = null;
        for each(constraint in this.constraints) {
          if (!constraint is MultiBoundingBoxConstraint) continue;
          multiBBoxConstraint = constraint as MultiBoundingBoxConstraint;
          for each(bbox in multiBBoxConstraint.bboxes) {
            if ((WGS84MapRes.value <= multiBBoxConstraint.maxResolution.value) && (WGS84MapRes.value
                >= multiBBoxConstraint.minResolution.value)) {
              return true;
            }
          }
        }
      }
    }
    return inRange;
  }

  /**
   * Return layer URL
   */
  public function getURL(bounds:Bounds):String {
    return null;
  }

  /**
   * Current layer position in the display list
   */
  public function get zindex():int {
    return this.parent.getChildIndex(this);
  }

  public function set zindex(value:int):void {
    this.parent.setChildIndex(this, value);
  }

  /**
   * Minimal valid resolution for this layer.
   */
  public function get minResolution():Resolution {

    var minRes:Resolution = this._minResolution;

    if (!this._minResolution || isNaN(this._minResolution.value) || !isFinite(this._minResolution.value)) {
      if (this.resolutions && (this.resolutions.length > 0)) {
        minRes = new Resolution(this.resolutions[this.resolutions.length - 1], this.projection);
        return minRes;
      }
    }
    return this._minResolution;
  }

  /**
   * @private
   */
  public function set minResolution(value:Resolution):void {

    value = (value as Resolution);

    if (this.projection != null) {
      if (value.projection != this.projection)
        value = value.reprojectTo(this.projection);
    }

    this._minResolution = value as Resolution;
    this.available = this.checkAvailability();
  }

  /**
   * Maximal valid resolution for this layer.
   */
  public function get maxResolution():Resolution {

    var maxRes:Resolution = this._maxResolution;

    if (!this._maxResolution || isNaN(this._maxResolution.value) || !isFinite(this._maxResolution.value)) {
      if (this.resolutions && (this.resolutions.length > 0)) {
        maxRes = new Resolution(this.resolutions[0], this.projection);
        return maxRes;
      }
    }

    return this._maxResolution;
  }

  /**
   * @private
   */
  public function set maxResolution(value:Resolution):void {

    value = (value as Resolution);

    if (this.projection != null) {
      if (value.projection != this.projection)
        value = value.reprojectTo(this.projection);
    }
    this._maxResolution = value as Resolution;
    this.available = this.checkAvailability();
  }

  /**
   * Number of zoom levels (resolutions array length)
   */
  public function get numZoomLevels():Number {
    return this.resolutions.length;
  }

  /**
   * Maximum extent for this layer. No data outside the extent will be displayed. To define this value, use the setMaxExtent method.
   */
  public function get maxExtent():Bounds {
    return this._maxExtent;
  }

  /**
   * Defines the maximal extent of the layer
   *
   * @param value A Bounds object or a String representing bounds (the format is defined in the Bounds.getBoundsFromString doc)
   * @see Bounds.getBoundsFromString
   */
  public function setMaxExtent(value:Object):void {
    var bounds:Bounds = null;
    if (value is String) {
      bounds = Bounds.getBoundsFromString(value as String);
    } else if (value is Bounds) {
      bounds = value as Bounds;
    } else return;
    if ((this.projection) && bounds.projection != this.projection) {
      /*if (this.projection.srsCode == "EPSG:2154" || this.projection.srsCode == "IGNF:LAMB93")
       bounds = bounds.reprojectTo(this.projection);
       else*/
      bounds = bounds.preciseReprojectBounds(this.projection);
    }
    if (bounds)
      this._maxExtent = bounds;
    this.available = this.checkAvailability()
  }

  /**
   * A list of map resolutions (map units per pixel) in descending
   * order. If this is not set in the layer constructor, it will be set
   * based on other resolution related properties (maxExtent, maxResolution, etc.).
   */
  public function get resolutions():Array {
    return this._resolutions;
  }

  public function set resolutions(value:Array):void {
    this._resolutions = value;
    if (this._resolutions == null || this._resolutions.length == 0) {
      this.generateResolutions();
    } else {
      this._autoResolution = false;
    }
    this._resolutions.sort(Array.NUMERIC | Array.DESCENDING);
  }

  /**
   * The projection of the map. This is the display projection of the map
   * If a layer is not in the same projection as the projection of the map
   * he will not be displayed.
   *
   * To set this value, use the setProjection method
   *
   * @default Geometry.DEFAULT_SRS_CODE
   */
  public function get projection():ProjProjection {
    return this._projection;
  }

  /**
   * Defines the projection of the layer.
   *
   * @param value A ProjProjection object or a String representing the SRS code of the projection (eg.: "EPSG:4326")
   */
  public function setProjection(value:Object):void {
    var event:LayerEvent = null;
    if (value != null) {
      var proj:ProjProjection = ProjProjection.getProjProjection(value);
      if (!proj)
        return;

      this._projection = proj;

      if (this.maxExtent) {
        this._maxExtent = this.maxExtent.preciseReprojectBounds(this._projection);
      }

      event = new LayerEvent(LayerEvent.LAYER_PROJECTION_CHANGED, this);
    }

    if (this._autoResolution) {
      this.generateResolutions();
    }

    if (this.maxResolution) {
      //Allows the maxResolution to be reprojected with the new layer projection
      this.maxResolution = this.maxResolution;
    }

    if (this.minResolution) {
      //Allows the minResolution to be reprojected with the new layer projection
      this.minResolution = this.minResolution;
    }

    if (event)
      this.dispatchEvent(event.clone());
    if (this.map && event)
      this.map.dispatchEvent(event.clone());

    this.available = this.checkAvailability();
  }

  /**
   * Define the layer available projections by its SRS codes. To defines this value use the setAvailableProjections method
   */
  public function get availableProjections():Vector.<String> {
    return this._availableProjections;
  }

  /**
   * Defines the availables projection of the layer.
   *
   * @param value a Vector of String or a comma separated list of projection SRS codes
   */
  public function setAvailableProjections(value:Object):void {

    var projections:Vector.<String> = new Vector.<String>();

    if (value is Vector.<String>) {
      var vector:Vector.<String> = value as Vector.<String>;
      var lght:int = vector.length;
      for (var i:uint = 0; i < lght; ++i) {
        if (!vector[i])continue;
        if (vector[i] != "") projections.push((vector[i] as String).toUpperCase());
      }

    } else if (value is String) {

      var array:Array = (value as String).split(",");
      for (var j:uint = 0; j < array.length; j++) {
        if (array[j] != "")projections.push((array[j] as String).toUpperCase());
      }
    }

    this._availableProjections = projections;
  }


  /**
   * Whether or not the layer is a fixed layer.
   * Fixed layers cannot be controlled by users
   */
  public function get isFixed():Boolean {
    return this._isFixed;
  }

  /**
   * @Private
   */
  public function set isFixed(value:Boolean):void {
    this._isFixed = value;
  }

  /**
   * Proxy (usually a PHP, Python, or Java script) used to request remote servers like
   * WFS servers in order to allow crossdomain requests. Remote servers can be used without
   * proxy script by using crossdomain.xml file like http://openscales.org/crossdomain.xml
   *
   * There is 3 cases :
   *  - proxy is explicitly defined
   *  - proxy is explicitly defined to "" => no proxy will be used
   *  - proxy is null => use the proxy of the map
   */
  public function get proxy():String {
    if (this._proxy == null && map && map.proxy) {
      return map.getProxy(this.url);
    }
    return this._proxy;
  }

  public function set proxy(value:String):void {
    this._proxy = value;
  }

  /**
   * Security manager associated to this layer
   */
  public function get security():ISecurity {
    return this._security;
  }

  public function set security(value:ISecurity):void {
    this._security = value;
  }

  /**
   * Define if this layer is visible (displayed) or not
   */
  public override function set visible(value:Boolean):void {
    super.visible = value;
    if (this.map != null) {
      var event:LayerEvent = new LayerEvent(LayerEvent.LAYER_VISIBLE_CHANGED, this);
      this.map.dispatchEvent(event);
    }
  }

  /**
   * Whether or not the layer is loading data
   */
  public function get loadComplete():Boolean {
    return (!this._loading);
  }

  /**
   * Used to set loading status of layer
   */
  protected function set loading(value:Boolean):void {
    if (value == true && this._loading == false && this.map != null) {
      _loading = value;
      this.map.dispatchEvent(new LayerEvent(LayerEvent.LAYER_LOAD_START, this));
    }

    if (value == false && this._loading == true && this.map != null) {
      _loading = value;
      this.map.dispatchEvent(new LayerEvent(LayerEvent.LAYER_LOAD_END, this));
    }
  }

  /**
   * opacity of the layer (between 0 and 1)
   */
  override public function set alpha(value:Number):void {

    if (value < 0)
      value = 0;
    if (value > 1)
      value = 1;

    var event:LayerEvent = new LayerEvent(LayerEvent.LAYER_OPACITY_CHANGED, this);
    event.oldOpacity = this.alpha;
    super.alpha = value;
    event.newOpacity = this.alpha;
    if (this._map != null) {
      this._map.dispatchEvent(event);
    }
  }

  //GAB
  public function get selected():Boolean {
    return _selected;
  }

  public function set selected(value:Boolean):void {
    _selected = value;
  }

/*
  /!**
   * Allows to add custom metadata about the layer
   *!/
  public function get metaData():Object {
    return _metaData;
  }

  /!**
   * @private
   *!/
  public function set metaData(value:Object):void {
    _metaData = value;
  }
*/

  /**
   * The url use for the layer request if necessary.
   */
  public function get url():String {
    return this._url;
  }

  /**
   * @private
   */
  public function set url(value:String):void {
    this._url = value;
  }

  /**
   * The list of originators for the layer.
   */
  public function get originators():Vector.<DataOriginator> {
    return _originators;
  }

  /**
   * @private
   * Take * in paramteter instead of Vector.<DataOriginator> to allowed several way of definition
   * (string and Vector.<DataOriginator> usefull for layer or FxLayer)
   */
  public function set originators(originators:Vector.<DataOriginator>):void {
    this._originators = (originators as Vector.<DataOriginator>);
    if (this._map) {
      this._map.dispatchEvent(new LayerEvent(LayerEvent.LAYER_CHANGED_ORIGINATORS, this));
    }
  }

  /**
   * Indicates if the layer should be displayed in the LayerManager List or not
   * @default true
   */
  public function get displayInLayerManager():Boolean {
    return this._displayInLayerManager;
  }

  /**
   * @private
   */
  public function set displayInLayerManager(value:Boolean):void {
    if (value != this._displayInLayerManager) {
      this._displayInLayerManager = value;
      if (this._map) {
        this._map.dispatchEvent(new LayerEvent(LayerEvent.LAYER_DISPLAY_IN_LAYERMANAGER_CHANGED, this));
      }
    }
  }


  /**
   * A list of constraints for the layer
   */
  public function get constraints():Vector.<Constraint> {
    return _constraints;
  }

  /**
   * @private
   */
  public function set constraints(value:Vector.<Constraint>):void {
    _constraints = value;
  }

  /**
   * The aggregate which contains this layer, null most of the time.
   */
  public function get aggregate():Aggregate {
    return _aggregate;
  }

  /**
   * @private
   */
  public function set aggregate(value:Aggregate):void {
    _aggregate = value;
  }

  /**
   * The identifier for the layer
   */
  [Bindable]
  public function get identifier():String {
    return _identifier;
  }

  /**
   * @private
   */
  public function set identifier(value:String):void {
    _identifier = value;
  }

  /**
   * Name and identifier of the layer.
   *
   * @deprecated You shall not more use this to set layer's name. Use <code>identifier</code> and <code>displayedName</code> instead
   */
  [Bindable]
  override public function get name():String {
    return super.name;
  }

  /**
   * @private
   */
  override public function set name(value:String):void {
    _identifier = value;
    _displayedName = value;
    super.name = value;
  }


  /**
   * The human readable name of the layer (could also be an i18n key)
   *
   * @default value of identifier property
   */
  [Bindable]
  public function get displayedName():String {
    return _displayedName;
  }

  /**
   * @private
   */
  public function set displayedName(value:String):void {
    _displayedName = value;
  }

  /**
   * Description (in plain text) of this layer
   *
   * @defaut null
   */
  public function get abstract():String {
    return _abstract;
  }

  /**
   * @private
   */
  public function set abstract(value:String):void {
    _abstract = value;
  }

  /**
   * Color filter of the Layer for grayscale mode
   *
   * @defaut null
   */
  public function get grayScaleFilter():ColorMatrixFilter {
    return _grayScaleFilter;
  }

  /**
   * @private
   */
  public function set grayScaleFilter(value:ColorMatrixFilter):void {
    _grayScaleFilter = value;
  }

  public function setGrayScale(active:Boolean):void {
    if (this._grayScaleTimer != null) {
      this._grayScaleTimer.removeEventListener(TimerEvent.TIMER, this.onGrayScaleTimerEnd);
      this._grayScaleTimer = null;
    }
    if (active == this.getGrayScale())
      return;

    if (active) {
      this.filters = [this._grayScaleFilter];
    } else {
      this.filters = [];
    }

    this._map.dispatchEvent(new LayerEvent(LayerEvent.LAYER_CHANGED, this));
  }

  public function getGrayScale():Boolean {
    return this.filters && this.filters.length > 0;
  }

}
}

