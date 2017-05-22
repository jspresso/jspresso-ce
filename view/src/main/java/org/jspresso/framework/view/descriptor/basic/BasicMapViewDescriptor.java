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

import org.jspresso.framework.view.descriptor.IMapViewDescriptor;

/**
 * This descriptor is used to implement a map view.
 *
 * @author Vincent Vandenschrick
 */
public class BasicMapViewDescriptor extends BasicViewDescriptor implements IMapViewDescriptor {

  private String longitudeProperty;
  private String latitudeProperty;
  private String routeProperty;

  /**
   * {@inheritDoc}
   *
   * @return the longitude property
   */
  @Override
  public String getLongitudeProperty() {
    return longitudeProperty;
  }

  /**
   * Sets the longitude property that will bind to the center of the map view.
   *
   * @param longitudeProperty
   *     the longitude property
   */
  public void setLongitudeProperty(String longitudeProperty) {
    this.longitudeProperty = longitudeProperty;
  }

  /**
   * {@inheritDoc}
   *
   * @return the latitude property
   */
  @Override
  public String getLatitudeProperty() {
    return latitudeProperty;
  }

  /**
   * Sets the latitude property that will bind to the center of the map view.
   *
   * @param latitudeProperty
   *     the latitude property
   */
  public void setLatitudeProperty(String latitudeProperty) {
    this.latitudeProperty = latitudeProperty;
  }

  /**
   * {@inheritDoc}
   *
   * @return the route property
   */
  @Override
  public String getRouteProperty() {
    return routeProperty;
  }

  /**
   * Sets route property. This property should return an array of (longitude, latitude) coordinates that will draw a
   * route on the map view. If {@code null}, no route is drawn.
   *
   * @param routeProperty
   *     the route property
   */
  public void setRouteProperty(String routeProperty) {
    this.routeProperty = routeProperty;
  }
}
