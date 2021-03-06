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
package org.jspresso.framework.view.descriptor;

import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * This public interface is implemented by cartographic view descriptors.
 *
 * @author Vincent Vandenschrick
 */
public interface IMapViewDescriptor extends IViewDescriptor {

  /**
   * the &quot;zoom&quot; constant
   */
  public final String ZOOM = "zoom";

  /**
   * Gets map content property.
   *
   * @return the map content property
   */
  String getMapContentProperty();

  /**
   * Gets default zoom.
   *
   * @return the default zoom
   */
  Integer getDefaultZoom();

  /**
   * Gets marker action.
   *
   * @return the marker action
   */
  IDisplayableAction getMarkerAction();

  /**
   * Gets route action.
   *
   * @return the route action
   */
  IDisplayableAction getRouteAction();

  /**
   * Gets zone action.
   *
   * @return the zone action
   */
  IDisplayableAction getZoneAction();
}
