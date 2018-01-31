/*
 * Copyright (c) 2005-2018 Maxime Hamm. All rights reserved.
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
package org.jspresso.framework.util.gui.map;

import java.util.HashSet;
import java.util.Set;

/**
 * Map
 *
 * @author Maxime HAMM
 * Date: 27/01/2018
 */
@SuppressWarnings("WeakerAccess")
public class MapDefinition {

    Set<Point> points;
    Set<Route> routes;

    /**
     * Map constructor
     */
    public MapDefinition() {
        this.points = new HashSet<>();
        this.routes = new HashSet<>();
    }

    public MapDefinition(Set<Point> points, Set<Route> routes) {
        this.points = points;
        this.routes = routes;
    }

    /**
     * Add a point
     * @param point The point to add
     */
    public void addPoint(Point point) {
        points.add(point);
    }

    /**
     * Add a route
     * @param route The route to add
     */
    public void addRoute(Route route) {
        routes.add(route);
    }

    /**
     * Gets points
     * @return The points
     */
    public Set<Point> getPoints() {
        return points;
    }

    /**
     * Gets routes
     * @return The routes
     */
    public Set<Route> getRoutes() {
        return routes;
    }

    /**
     * Build map as Json
     * @return The json map description
     */
    public String buildMap() {
        return MapHelper.buildMap(points.toArray(new Point[0]), routes.toArray(new Route[0]));
    }

    /**
     * Merge the map with another map
     * Points and routes are merged
     * @param map The map to merge
     */
    public void merge(MapDefinition map) {
        routes.addAll(map.getRoutes());
        points.addAll(map.getPoints());
    }
}


