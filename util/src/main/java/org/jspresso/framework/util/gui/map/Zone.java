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

import java.io.Serializable;

/**
 * Zone
 *
 * @author Maxime HAMM Date: 04/04/2019
 */
public class Zone implements Serializable {

    /**
     * The shape.
     */
    private Point[] points;

    private Point barycenter;
    private Pair<Point, Point> boundaryBox;

    /**
     * Clone point point.
     *
     * @param clonePoints the clone points
     * @return the point
     */
    public Zone cloneZone(boolean clonePoints) {

        Zone z = clonePoints ? new Zone(MapHelper.clonePoints(points)) : new Zone(points);

        z.barycenter = this.barycenter!=null ? this.barycenter.clonePoint() : null;
        z.boundaryBox = this.boundaryBox!=null ? Pair.of(this.boundaryBox.getLeft().clonePoint(), this.boundaryBox.getRight().clonePoint()) : null;

        return z;
    }

    /**
     * Instantiates a new Zone.
     *
     * @param points the shapes as closed routes
     */
    public Zone(Point... points) {
        this.points = points;
        this.boundaryBox = null;
        this.barycenter = null;
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

}
