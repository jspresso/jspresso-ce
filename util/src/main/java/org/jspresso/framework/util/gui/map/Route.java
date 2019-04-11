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

/**
 * Route
 *
 * @author Maxime HAMM
 * @see <a href="https://openlayers.org/en/latest/apidoc/ol.style.Stroke.html">Open layers stroke</a> Date: 27/01/2018
 */
@SuppressWarnings("WeakerAccess")
public class Route extends AbstractData {

    /**
     * Route default color
     */
    public static final String DEFAULT_COLOR = "#c0392b";

    /**
     * Route default width
     */
    public static final int DEFAULT_WIDTH = 3;

    /**
     * The Points.
     */
    Point[] points;

    /**
     * The Color.
     */
    String color;

    /**
     * The Width.
     */
    Integer width;

    /**
     * Clone point point.
     *
     * @return the point
     */
    public Route cloneRoute(boolean clonePoints) {

        Route r = clonePoints ? new Route(MapHelper.clonePoints(points)) : new Route(points);

        r.setId(this.getId());

        r.width = this.width;
        r.color = this.color;
        return r;
    }


    /**
     * Route constructor
     *
     * @param points Array of points
     */
    public Route(Point... points) {
        this.points = points;
    }

    /**
     * Gets route's points
     *
     * @return The points
     */
    public Point[] getPoints() {
        return points;
    }

    /**
     * Sets route color
     *
     * @param color The color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Gets route color
     *
     * @return The color
     */
    public String getColor() {
        return color!=null ? color : DEFAULT_COLOR;
    }

    /**
     * Sets route width
     *
     * @param width The width
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * Gets route width
     *
     * @return The width
     */
    public Integer getWidth() {
        return width !=null ? width : DEFAULT_WIDTH;
    }
}


