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
package org.jspresso.framework.view.descriptor.basic;

import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IMapViewDescriptor;

/**
 * This descriptor is used to implement a map view.
 *
 * @author Vincent Vandenschrick
 */
public class BasicMapViewDescriptor extends BasicViewDescriptor implements IMapViewDescriptor {

  private String             mapContentProperty;
  private Integer            defaultZoom;
  private IDisplayableAction markerAction;
  private IDisplayableAction routeAction;
  private IDisplayableAction zoneAction;

  /**
   * Gets map content property.
   *
   * @return the map content property
   */
  @Override
  public String getMapContentProperty() {
    return mapContentProperty;
  }

  /**
   * Sets map content property. The targeted property should return a Json string that describes the markers and the
   * routes to display on the map.
   *
   * @param mapContentProperty
   *     the map content property
   */
  public void setMapContentProperty(String mapContentProperty) {
    this.mapContentProperty = mapContentProperty;
  }

  /**
   * Gets default zoom.
   *
   * @return the default zoom
   */
  @Override
  public Integer getDefaultZoom() {
    return defaultZoom;
  }

  /**
   * Sets default zoom to display when there is no route and a single marker.
   *
   * @param defaultZoom
   *     the default zoom
   */
  public void setDefaultZoom(Integer defaultZoom) {
    this.defaultZoom = defaultZoom;
  }

  /**
   * Gets marker action.
   *
   * @return the marker action
   */
  @Override
  public IDisplayableAction getMarkerAction() {
    return markerAction;
  }

  /**
   * Sets marker action.
   *
   * @param markerAction
   *     the marker action
   */
  public void setMarkerAction(IDisplayableAction markerAction) {
    this.markerAction = markerAction;
  }

  /**
   * Gets route action.
   *
   * @return the route action
   */
  @Override
  public IDisplayableAction getRouteAction() {
    return routeAction;
  }

  /**
   * Sets route action.
   *
   * @param routeAction
   *     the route action
   */
  public void setRouteAction(IDisplayableAction routeAction) {
    this.routeAction = routeAction;
  }

  /**
   * Gets zone action.
   *
   * @return the zone action
   */
  @Override
  public IDisplayableAction getZoneAction() {
    return zoneAction;
  }

  /**
   * Sets zone action.
   *
   * @param zoneAction
   *     the zone action
   */
  public void setZoneAction(IDisplayableAction zoneAction) {
    this.zoneAction = zoneAction;
  }
}
