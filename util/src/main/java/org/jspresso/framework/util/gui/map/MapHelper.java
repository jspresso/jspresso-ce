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
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jspresso.framework.util.exception.NestedRuntimeException;
import org.jspresso.framework.util.gui.ColorHelper;
import org.jspresso.framework.util.html.HtmlHelper;

import java.util.*;

/**
 * Helper for map building
 *
 * @author Maxime HAMM Date: 27/01/2018
 */
@SuppressWarnings("WeakerAccess")
public class MapHelper {

    /**
     * The constant MARKER_ID is &quot;id&quot;.
     */
    public static final String MARKER_ID = "id";

    /**
     * The constant MARKERS_KEY is &quot;markers&quot;.
     */
    public static final String MARKERS_KEY = "markers";

    /**
     * The constant MARKER_IMAGE_KEY is &quot;image&quot;.
     */
    public static final String MARKER_IMAGE_KEY = "image";

    /**
     * The constant MARKER_COORD_KEY is &quot;coord&quot;.
     */
    public static final String MARKER_COORD_KEY = "coord";

    /**
     * The constant MARKER_DESCRIPTION is &quot;htmlDescription&quot;.
     */
    public static final String MARKER_DESCRIPTION = "htmlDescription";

    /**
     * The constant ROUTES_KEY is &quot;routes&quot;.
     */
    public static final String ROUTES_KEY = "routes";

    /**
     * The constant ZONES_KEY is &quot;zones&quot;.
     */
    public static final String ZONES_KEY = "zones";

    /**
     * The constant ROUTE_PATH_KEY is &quot;path&quot;.
     */
    public static final String ROUTE_PATH_KEY = "path";

    /**
     * The constant ROUTE_STYLE_KEY is &quot;style&quot;.
     */
    public static final String ROUTE_STYLE_KEY = "style";

    /**
     * Red mark icon
     */
    public static final String RED_MARK = "org/jspresso/contrib/images/map/marker-red.svg";

    /**
     * Green mark icon
     */
    public static final String GREEN_MARK = "org/jspresso/contrib/images/map/marker-green.svg";

    /**
     * Build markers.
     *
     * @param points One or more points
     * @return the string
     */
    public static String buildMarkers(Point... points) {
        return buildMap(points, null);
    }

    /**
     * Build routes.
     *
     * @param points the points
     * @param routes One or more couple of longitude and latitude
     * @return the string
     */
    public static String buildMap(Point[] points, Route[] routes) {
        return buildMap(points, routes, null);
    }

    /**
     * Build routes.
     *
     * @param points the points
     * @param routes One or more couple of longitude and latitude
     * @param shapes the shapes
     * @return the string
     */
    public static String buildMap(Point[] points, Route[] routes, Shape[] shapes) {
        try {
            JSONObject mapContent = new JSONObject();

            //
            // Points
            if (points != null) {

                List<JSONObject> keys = new ArrayList<>();
                for (Point p : points) {

                    JSONObject marker = new JSONObject();
                    if (p.getId()!=null)
                        marker.put(MARKER_ID, p.getId());

                    marker.put(MARKER_COORD_KEY, Arrays.asList(p.getLongitude(), p.getLatitude()));
                    String imageUrl = p.getImageUrl();
                    if (imageUrl != null) {
                        JSONObject image = new JSONObject();
                        image.put("src", imageUrl);
                        if (p.getColor() != null) {
                            image.put("color", p.getColor());
                        }
                        if (p.getOptions() != null) {
                            applyOptions(p, image);
                        }
                        marker.put(MARKER_IMAGE_KEY, image);
                    }

                    String htmlDescription = p.getHtmlDescription();
                    if (htmlDescription != null) {

                        marker.put(MARKER_DESCRIPTION, htmlDescription);
                    }

                    keys.add(marker);
                }
                mapContent.put(MARKERS_KEY, keys);
            }

            //
            // Routes
            if (routes != null) {

                List<JSONObject> routesList = new ArrayList<>();
                for (Route route : routes) {

                    double[][] routePath = convertRoutes(route)[0];

                    List<List<Double>> routeAsList = new ArrayList<>();
                    for (double[] aRoute : routePath) {
                        routeAsList.add(Arrays.asList(aRoute[0], aRoute[1]));
                    }

                    JSONObject json = new JSONObject();
                    if (route.getId()!=null)
                        json.put(MARKER_ID, route.getId());

                    json.put(ROUTE_PATH_KEY, routeAsList);
                    JSONObject routeStyle = new JSONObject();
                    routeStyle.put("color", route.getColor());
                    routeStyle.put("width", route.getWidth());
                    if (route.getOptions() != null) {
                        applyOptions(route, routeStyle);
                    }
                    json.put(ROUTE_STYLE_KEY, routeStyle);
                    routesList.add(json);
                }
                mapContent.put(ROUTES_KEY, routesList);
            }

            //
            // Shapes
            if (shapes != null) {

                List<JSONObject> zonesList = new ArrayList<>();
                for (Shape shape : shapes) {

                    // Style
                    JSONObject style = new JSONObject(); {

                        JSONObject stroke = new JSONObject();
                        stroke.put("color", shape.getLineColor());
                        stroke.put("width", shape.getLineWidth());
                        style.put("stroke", stroke);

                        JSONObject fill = new JSONObject();
                        fill.put("color", shape.getFillColor());
                        style.put("fill", fill);
                    }

                    // The zone
                    JSONArray jzone = new JSONArray(
                            buildZonePoints(shape.getZone()));

                    // add exclusion
                    if (!shape.getExclusions().isEmpty()) {

                        // Add one more dimension
                        int i=0;
                        {
                            JSONArray j = new JSONArray();
                            j.put(i++, jzone);
                            jzone = j;
                        }

                        // load exclusions
                        for (Zone exclusion : shape.getExclusions()) {

                            JSONArray jexclusion = new JSONArray(buildZonePoints(exclusion));
                            jzone.put(i++, jexclusion);
                        }
                    }

                    // consolidate
                    JSONObject all = new JSONObject();
                    all.put("style", style);
                    all.put("shape", jzone);

                    if (shape.getId()!=null)
                        all.put(MARKER_ID, shape.getId());

                    zonesList.add(all);
                }
                mapContent.put(ZONES_KEY, zonesList);
            }

            return mapContent.toString(0);

        } catch (JSONException ex) {
            throw new NestedRuntimeException(ex);
        }
    }

    private static List<JSONArray> buildZonePoints(Zone zone) throws JSONException {

        List<JSONArray> jpoints = new ArrayList<>();
        for (Point p : zone.getPoints()) {

            JSONArray jpoint = new JSONArray(2);
            jpoint.put(0, p.getLongitude());
            jpoint.put(1, p.getLatitude());
            jpoints.add(jpoint);
        }

        return jpoints;
    }

    /**
     * Gets point label.
     *
     * @param label the label
     * @return the point label
     */
    public static String getPointLabel(String label) {
        return getPointLabel(label, new PointLabelParams());
    }

    /**
     * Gets point label.
     *
     * @param label  the label
     * @param params the params
     * @return the point label
     */
    public static String getPointLabel(
            String label,
            PointLabelParams params) {

        label = HtmlHelper.escapeForHTML(label, true);

        String actionName = null;
        if (params.useAtionHyperlink) {
            if (params.actionParameter != null)
                actionName = "executeAction(\"" + params.actionParameter + "\")";
            else
                actionName += "executeAction()";
        }

        String foreground = params.hexColor != null ? params.hexColor : "0879c2";
        String background = params.backgroundHexColor;
        int size = params.size != null ? params.size : 2;

        final String style;
        if (background != null) {

            if (background.length() != 8) {
                style = " style=\"background-color:#" + background + ";\"";
            }
            else {
                int[] rgba = ColorHelper.fromHexString("0x" + background);
                style = " style=\"background-color:rgba("
                        + rgba[0] + ", "
                        + rgba[1] + ", "
                        + rgba[2] + ", "
                        + rgba[3] * 1.0d / 255 + ");\"";
            }
        }
        else {
            style = "";
        }

        if (actionName != null)
            label = "<a href='" + actionName + "'>" + label + "</a>";

        return "<p" + style + ">" +
                "<font color=\"#" + foreground + "\" size=\"" + size + "\"><b>&nbsp;"
                + label
                + "&nbsp;</b></font></p>";
    }

    /**
     * Gets middle of two points.
     *
     * @param a the first point
     * @param b the second point
     * @return the middle point
     */
    public static Point getBaryCenter(Point a, Point b) {

        double lat1 = Math.toRadians(a.getLatitude());
        double lat2 = Math.toRadians(b.getLatitude());
        double lon1 = Math.toRadians(a.getLongitude());

        double dLon = Math.toRadians(b.getLongitude() - a.getLongitude());
        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);

        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

        return new Point(Math.toDegrees(lon3), Math.toDegrees(lat3));
    }

    /**
     * Gets bary center of points.
     *
     * @param points the points
     * @return the middle point
     */
    public static Point getBaryCenter(Point... points) {

        if (points == null || points.length==0)
            return null;

        double lg;
        double lt;
        double maxlat = 0, minlat = 0, maxlon = 0, minlon = 0;
        int i = 0;
        for (Point p : points) {

            lt = p.getLongitude();
            lg = p.getLatitude();
            if (i == 0) {
                maxlat = lt;
                minlat = lt;
                maxlon = lg;
                minlon = lg;
            }
            else {
                if (maxlat < lt)
                    maxlat = lt;
                if (minlat > lt)
                    minlat = lt;
                if (maxlon < lg)
                    maxlon = lg;
                if (minlon > lg)
                    minlon = lg;
            }
            i++;
        }
        lt = (maxlat + minlat) / 2;
        lg = (maxlon + minlon) / 2;
        return new Point(lt, lg);
    }

    /**
     * Gets boundary box containing shapes.
     *
     * @param shapes the shapes
     * @return the boundary box
     */
    public static Pair<Point, Point> getBoundaryBox(Shape... shapes) {

        Set<Point> points = new HashSet<>();
        for (Shape s: shapes) {

            Pair<Point, Point> box = s.getZone().getBoundaryBox();
            points.add(box.getLeft());
            points.add(box.getRight());
        }

        return getBoundaryBox(0d, points.toArray(new Point[0]));
    }

    /**
     * Gets boundary box containing zones.
     *
     * @param zones the zones
     * @return the boundary box
     */
    public static Pair<Point, Point> getBoundaryBox(Zone... zones) {

        Set<Point> points = new HashSet<>();
        for (Zone z: zones) {
            Pair<Point, Point> box = z.getBoundaryBox();
            points.add(box.getLeft());
            points.add(box.getRight());
        }

        return getBoundaryBox(0d, points.toArray(new Point[0]));
    }

    /**
     * Gets boundary box containing points.
     *
     * @param padding the padding
     * @param points  the points
     * @return the boundary box
     */
    public static Pair<Point, Point> getBoundaryBox(double padding, Point... points) {

        if (points == null || points.length==0)
            return null;

        double west = 0.0;
        double east = 0.0;
        double north = 0.0;
        double south = 0.0;

        for (int lc = 0; lc < points.length; lc++)
        {
            Point loc = points[lc];

            double latitude = loc.getLongitude();
            double longitude = loc.getLatitude();

            if (lc == 0) {
                north = latitude;
                south = latitude;
                west = longitude;
                east = longitude;
            }
            else { if (latitude > north) {
                    north = latitude;
                }
                else if (latitude < south) {
                    south = latitude;
                }
                if (longitude < west) {
                    west = longitude;
                }
                else if (longitude > east) {
                    east = longitude;
                }
            }
        }

        north = north + padding;
        south = south - padding;
        west = west - padding;
        east = east + padding;

        Point left = new Point(north, east);
        left.setImagePath(null);

        Point right = new Point(south, west);
        right.setImagePath(null);

        return Pair.of(left, right);
    }

    /**
     * Clone points point [ ].
     *
     * @param points the points
     * @return the point [ ]
     */
    static Point[] clonePoints(Point[] points) {

        if (points==null)
            return null;

        Point[] clone = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            clone[i] = points[i].clonePoint();
        }
        return clone;
    }

    /**
     * Clone shapes shape [ ].
     *
     * @param shapes      the shapes
     * @param clonePoints the clone points
     * @return the shape [ ]
     */
    static Shape[] cloneShapes(Shape[] shapes, boolean clonePoints) {

        if (shapes==null)
            return null;

        Shape[] clone = new Shape[shapes.length];
        for (int i = 0; i < shapes.length; i++) {
            clone[i] = shapes[i].cloneShape(clonePoints);
        }
        return clone;
    }

    /**
     * Clone zones zone [ ].
     *
     * @param zones       the zones
     * @param clonePoints the clone points
     * @return the zone [ ]
     */
    static Zone[] cloneZones(Zone[] zones, boolean clonePoints) {

        if (zones==null)
            return null;

        Zone[] clone = new Zone[zones.length];
        for (int i = 0; i < zones.length; i++) {
            clone[i] = zones[i].cloneZone(clonePoints);
        }
        return clone;
    }

    /**
     * Clone routes route [ ].
     *
     * @param routes      the routes
     * @param clonePoints the clone points
     * @return the route [ ]
     */
    static Route[] cloneRoutes(Route[] routes, boolean clonePoints) {

        if (routes==null)
            return null;

        Route[] clone = new Route[routes.length];
        for (int i = 0; i < routes.length; i++) {
            clone[i] = routes[i].cloneRoute(clonePoints);
        }
        return clone;
    }


    /**
     * The type Point label params.
     */
    public static class PointLabelParams {

        /**
         * The Label foreground hex color.
         */
        public String hexColor;

        /**
         * The Label hex color.
         */
        public String backgroundHexColor;

        /**
         * The Label size.
         */
        public Integer size;

        /**
         * Use hyperlink.
         */
        public boolean useAtionHyperlink;

        /**
         * The Action parameter.
         */
        public String actionParameter;
    }


    private static void applyOptions(AbstractData data, JSONObject json) throws JSONException {
        for (Map.Entry<String, Object> entry : data.getOptions().entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Collection) {
                json.put(entry.getKey(), (Collection) value);
            }
            else if (value instanceof int[]) {
                List<Integer> ints = new ArrayList<>();
                for (int i : (int[]) value) {
                    ints.add(i);
                }
                json.put(entry.getKey(), ints);
            }
            else if (value instanceof Integer) {
                json.put(entry.getKey(), (int) value);
            }
            else if (value instanceof Boolean) {
                json.put(entry.getKey(), (boolean) value);
            }
            else if (value instanceof Double) {
                json.put(entry.getKey(), (double) value);
            }
            else {
                json.put(entry.getKey(), value);
            }
        }
    }

    private static double[][] convertPoints(Point... points) {
        double[][] markers = new double[points.length][];
        for (int i = 0; i < points.length; i++) {
            markers[i] = new double[]{points[i].longitude, points[i].latitude};
        }
        return markers;
    }

    private static double[][][] convertRoutes(Route... routes) {
        double[][][] routes2 = new double[routes.length][][];
        for (int i = 0; i < routes.length; i++) {
            routes2[i] = convertPoints(routes[i].points);
        }
        return routes2;
    }
}
