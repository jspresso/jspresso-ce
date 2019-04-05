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

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Map
 *
 * @author Maxime HAMM Date: 27/01/2018
 */
@SuppressWarnings("WeakerAccess")
public class MapDefinition {

    /**
     * The Points.
     */
    final Set<Point> points;
    /**
     * The Routes.
     */
    final Set<Route> routes;
    /**
     * The Zones.
     */
    final Set<Zone> zones;

    /**
     * Map constructor
     */
    public MapDefinition() {
        this(null, null, null);
    }

    /**
     * Instantiates a new Map definition.
     *
     * @param points the points
     * @param routes the routes
     */
    public MapDefinition(Set<Point> points, Set<Route> routes) {
        this(points, routes, null);
    }

    /**
     * Instantiates a new Map definition.
     *
     * @param points the points
     * @param routes the routes
     * @param zones  the zones
     */
    public MapDefinition(Set<Point> points, Set<Route> routes, Set<Zone> zones) {
        this.points = points!=null ? points : new LinkedHashSet<Point>();
        this.routes = routes != null ? routes : new LinkedHashSet<Route>();
        this.zones = zones !=null ? zones : new LinkedHashSet<Zone>();
    }

    /**
     * Add a point
     *
     * @param point The point to add
     */
    public void addPoint(Point point) {
        points.add(point);
    }

    /**
     * Add a route.
     *
     * @param route The route to add
     */
    public void addRoute(Route route) {
        routes.add(route);
    }

    /**
     * Add a zone.
     *
     * @param zone The route to add
     */
    public void addZone(Zone zone) {
        zones.add(zone);
    }

    /**
     * Gets points.
     *
     * @return The points
     */
    public Set<Point> getPoints() {
        return points;
    }

    /**
     * Gets routes
     *
     * @return The routes
     */
    public Set<Route> getRoutes() {
        return routes;
    }

    /**
     * Gets zones.
     *
     * @return the zones
     */
    public Set<Zone> getZones() {
        return zones;
    }

    /**
     * Build map as Json
     *
     * @return The json map description
     */
    public String buildMap() {
        return MapHelper.buildMap(
                points.toArray(new Point[0]),
                routes.toArray(new Route[0]),
                zones.toArray(new Zone[0]));
    }

    /**
     * Merge the map with another map
     * Points and routes are merged
     *
     * @param map The map to merge
     */
    public void merge(MapDefinition map) {
        merge(map, true, true, true);
    }

    /**
     * Merge the map with another map
     * Points and routes are merged
     *
     * @param map             The map to merge
     * @param includingPoints the including points
     * @param includingRoutes the including routes
     * @param includingZones  the including zones
     */
    public void merge(MapDefinition map, boolean includingPoints, boolean includingRoutes, boolean includingZones) {
        if (includingPoints)
            points.addAll(map.getPoints());
        if (includingRoutes)
            routes.addAll(map.getRoutes());
        if (includingZones)
            zones.addAll(map.getZones());
    }
}


