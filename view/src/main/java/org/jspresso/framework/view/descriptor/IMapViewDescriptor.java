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

/**
 * This public interface is implemented by cartographic view descriptors.
 *
 * @author Vincent Vandenschrick
 */
public interface IMapViewDescriptor extends IViewDescriptor {

  /**
   * The constant MARKERS_KEY is &quot;markers&quot;.
   */
  public final String MARKERS_KEY = "markers";
  /**
   * The constant MARKER_IMAGE_KEY is &quot;image&quot;.
   */
  public final String MARKER_IMAGE_KEY = "image";
  /**
   * The constant MARKER_COORD_KEY is &quot;coord&quot;.
   */
  public final String MARKER_COORD_KEY = "coord";

  /**
   * The constant ROUTES_KEY is &quot;routes&quot;.
   */
  public final String ROUTES_KEY = "routes";
  /**
   * The constant ROUTE_PATH_KEY is &quot;path&quot;.
   */
  public final String ROUTE_PATH_KEY = "path";
  /**
   * The constant ROUTE_STYLE_KEY is &quot;style&quot;.
   */
  public final String ROUTE_STYLE_KEY = "style";

  /**
   * Gets map content property.
   *
   * @return the map content property
   */
  String getMapContentProperty();
}
