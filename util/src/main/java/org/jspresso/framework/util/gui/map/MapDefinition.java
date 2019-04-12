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

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Map
 *
 * @author Maxime HAMM Date: 27/01/2018
 */
@SuppressWarnings("WeakerAccess")
public class MapDefinition implements Serializable {

    public static final Logger LOG = LoggerFactory.getLogger(MapDefinition.class);

    /**
     * The Points.
     */
    final List<Point> points;
    /**
     * The Routes.
     */
    final List<Route> routes;
    /**
     * The Shapes.
     */
    final List<Shape> shapes;

    /**
     * Clone map map definition.
     *
     * @return the map definition
     */
    public MapDefinition cloneMap(boolean clonePoints) {
        if (clonePoints)
            return new MapDefinition(
                    new ArrayList<>(Arrays.asList(MapHelper.clonePoints(points.toArray(new Point[0])))),
                    new ArrayList<>(Arrays.asList(MapHelper.cloneRoutes(routes.toArray(new Route[0]), clonePoints))),
                    new ArrayList<>(Arrays.asList(MapHelper.cloneShapes(shapes.toArray(new Shape[0]), clonePoints))));
        else
            return new MapDefinition(
                    new ArrayList<>(points),
                    new ArrayList<>(routes),
                    new ArrayList<>(shapes));
    }

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
    public MapDefinition(List<Point> points, List<Route> routes) {
        this(points, routes, null);
    }

    /**
     * Instantiates a new Map definition.
     *
     * @param points the points
     * @param routes the routes
     * @param shapes  the shapes
     */
    public MapDefinition(List<Point> points, List<Route> routes, List<Shape> shapes) {
        this.points = points!=null ? points : new ArrayList<Point>();
        this.routes = routes != null ? routes : new ArrayList<Route>();
        this.shapes = shapes !=null ? shapes : new ArrayList<Shape>();
    }

    /**
     * Add a point
     *
     * NB : If another point already exists at same location,
     * the point will ne added oonly if it's ID is different as different
     *
     * @param point The point to add
     */
    public void addPoint(Point point) {
        points.add(point);
    }

    /**
     * Add point if not exists.
     *
     * @param point the point
     */
    public void addPointIfNotExists(Point point) {
        if (!points.contains(point))
            points.add(point);
    }

    /**
     * Merge point point.
     *
     * @param point the point
     * @return the point
     */
    public Point mergePoint(Point point) {

        final Point merged;
        int i = points.indexOf(point);
        if (i >= 0) {

            Point p = points.get(i);
            if (p.getId() == null)
                p.setId(point.getId());

            if (p.getHtmlDescription() == null)
                p.setHtmlDescription(point.getHtmlDescription());

            if (p.getImageUrl() == null)
                p.setImageUrl(point.getImageUrl());

            merged = p;
        }
        else {
            points.add(point);
            merged = point;
        }

        return merged;
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
     * Add a shape.
     *
     * @param shape The route to add
     */
    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    /**
     * Gets points.
     *
     * @return The points
     */
    public List<Point> getPoints() {
        return points;
    }

    /**
     * Gets routes
     *
     * @return The routes
     */
    public List<Route> getRoutes() {
        return routes;
    }

    /**
     * Gets shapes.
     *
     * @return the shapes
     */
    public List<Shape> getShapes() {
        return shapes;
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
                shapes.toArray(new Shape[0]));
    }

    /**
     * Merge the map with another map
     * Points and routes are merged
     *
     * @param map The map to merge
     */
    public void merge(MapDefinition map) {
        if (map==null)
            return;
        merge(map, true, true, true);
    }

    /**
     * Merge the map with another map
     * Points and routes are merged
     *
     * @param map             The map to merge
     * @param includingPoints the including points
     * @param includingRoutes the including routes
     * @param includingShapes  the including shapes
     */
    public void merge(MapDefinition map, boolean includingPoints, boolean includingRoutes, boolean includingShapes) {
        if (includingPoints) {
            for (Point p :map.getPoints()) {
                mergePoint(p);
            }
        }
        if (includingRoutes)
            routes.addAll(map.getRoutes());
        if (includingShapes)
            shapes.addAll(map.getShapes());
    }

    /**
     * Gets middle.
     *
     * @return the zone's middle
     */
    public Pair<Point, Point> getBoundaryBox() {
        return MapHelper.getBoundaryBox(this.getShapes().toArray(new Shape[0]));
    }
}


