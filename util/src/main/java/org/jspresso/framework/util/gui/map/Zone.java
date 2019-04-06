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

/**
 * Zone
 *
 * @author Maxime HAMM Date: 04/04/2019
 */
@SuppressWarnings({"WeakerAccess"})
public class Zone {

    /**
     * Route default color
     */
    public static final String DEFAULT_FILL_COLOR = "rgba(0, 0, 255, 0.1)";

    /**
     * The constant DEFAULT_LINE_COLOR.
     */
    public static final String DEFAULT_LINE_COLOR = "#599ac9";

    /**
     * The constant DEFAULT_WIDTH.
     */
    public static final int DEFAULT_WIDTH = 1;

    /**
     * The shape.
     */
    Point[] points;

    /**
     * The Fill color.
     */
    String fillColor;

    /**
     * The Line color.
     */
    String lineColor;

    /**
     * The Line width.
     */
    Integer lineWidth;

    private Point barycenter;
    private Pair<Point, Point> boundaryBox;

    /**
     * Instantiates a new Zone.
     *
     * @param points the shapes as closed routes
     */
    public Zone(Point... points) {
        this.points = points;
    }

    /**
     * Sets points.
     *
     * @param points the points
     */
    public void setPoints(Point... points) {
        this.points = points;
        this.boundaryBox = null;
        this.barycenter = null;
    }

    /**
     * Gets points.
     *
     * @return the points
     */
    public Point[] getPoints() {
        return points;
    }

    /**
     * Gets fill color.
     *
     * @return the fill color
     */
    public String getFillColor() {
        return fillColor !=null ? fillColor : DEFAULT_FILL_COLOR;
    }

    /**
     * Sets fill color.
     *
     * @param fillColor the fill color
     */
    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * Gets line color.
     *
     * @return the line color
     */
    public String getLineColor() {
        return lineColor !=null ? lineColor : DEFAULT_LINE_COLOR;
    }

    /**
     * Sets line color.
     *
     * @param lineColor the line color
     */
    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
    }

    /**
     * Gets line width.
     *
     * @return the line width
     */
    public Integer getLineWidth() {
        return lineWidth !=null ? lineWidth : DEFAULT_WIDTH;
    }

    /**
     * Sets line width.
     *
     * @param lineWidth the line width
     */
    public void setLineWidth(Integer lineWidth) {
        this.lineWidth = lineWidth;
    }

    /**
     * Gets bary center.
     *
     * @return the zone's middle
     */
    public Point getBaryCenter() {
        if (barycenter == null)
            barycenter = MapHelper.getBaryCenter(getPoints());
        return barycenter;
    }

    /**
     * Gets middle.
     *
     * @return the zone's middle
     */
    public Pair<Point, Point> getBoundaryBox() {
        if (boundaryBox == null)
            boundaryBox = MapHelper.getBoundaryBox(0.01, getPoints());
        return boundaryBox;
    }


    /**
     * Sets transparent.
     */
    public void setTransparent() {
        setFillColor("rgba(255, 255, 255, 0)");
    }
}
