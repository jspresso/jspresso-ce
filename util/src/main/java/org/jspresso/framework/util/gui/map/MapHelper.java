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

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jspresso.framework.util.exception.NestedRuntimeException;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.resources.server.ResourceProviderServlet;

import java.util.*;

/**
 * Helper for map building
 *
 * @author Maxime HAMM
 * Date: 27/01/2018
 */
@SuppressWarnings("WeakerAccess")
public class MapHelper {

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
     * The constant ROUTES_KEY is &quot;routes&quot;.
     */
    public static final String ROUTES_KEY = "routes";
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
     */
    public static String buildMarkers(Point... points) {
        return buildMap(points, null);
    }

    /**
     * Build routes.
     *
     * @param routes One or more couple of longitude and latitude
     */
    public static String buildMap(Point[] points, Route[] routes) {
        try {

            JSONObject mapContent = new JSONObject();
            if (points != null) {

                List<JSONObject> keys = new ArrayList<>();
                for (Point p : points) {

                    JSONObject marker = new JSONObject();
                    marker.put(MARKER_COORD_KEY, Arrays.asList(p.getLongitude(), p.getLatitude()));

                    if (p.getImagePath() != null) {
                        JSONObject image = new JSONObject();
                        image.put("src", getImageUrl(p.getImagePath(), p.getImageDimension()));

                        if (p.getColor() != null) {
                            image.put("color", p.getColor());
                        }

                        if (p.getOptions()!=null) {
                            applyOptions(p, image);
                        }

                        marker.put(MARKER_IMAGE_KEY, image);
                    }


                    keys.add(marker);
                }
                mapContent.put(MARKERS_KEY, keys);
            }

            if (routes != null) {

                List<JSONObject> routesList = new ArrayList<>();
                for (Route route : routes) {

                    double[][] routePath = convertRoutes(route)[0];
                    List<List<Double>> routeAsList = new ArrayList<>();
                    for (double[] aRoute : routePath) {
                        routeAsList.add(Arrays.asList(aRoute[0], aRoute[1]));
                    }

                    JSONObject json = new JSONObject();
                    json.put(ROUTE_PATH_KEY, routeAsList);
                    JSONObject routeStyle = new JSONObject();
                    routeStyle.put("color", route.getColor());
                    routeStyle.put("width", route.getWidth());
                    if (route.getOptions()!=null) {
                        applyOptions(route, json);
                    }

                    json.put(ROUTE_STYLE_KEY, routeStyle);

                    routesList.add(json);
                }
                mapContent.put(ROUTES_KEY, routesList);
            }

            return mapContent.toString(2);

        } catch (JSONException ex) {
            throw new NestedRuntimeException(ex);
        }
    }

    /**
     * Gets image URL from resource path
     * @param path The resource path
     * @return The url
     */
    public static String getImageUrl(String path, Dimension dimension) {

        if (!path.startsWith("classpath:")) {

            String slash = path.startsWith("/") ? "" : "/";
            path = "classpath:" + slash + path;
        }
        return ResourceProviderServlet.computeImageResourceDownloadUrl(path, dimension);
    }

    private static void applyOptions(AbstractData data, JSONObject json) throws JSONException {

        for (Map.Entry<String, Object> entry : data.getOptions().entrySet()) {

            Object value = entry.getValue();
            if (value instanceof Collection) {
                json.put(entry.getKey(), (Collection)value);
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
