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

import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.resources.server.ResourceProviderServlet;

import java.util.Arrays;

/**
 * Point
 *
 * @author Maxime HAMM Date: 27/01/2018
 * @see <a href="https://openlayers.org/en/latest/apidoc/ol.style.Icon.html">Open layers icon</a>
 */
@SuppressWarnings("WeakerAccess")
public class Point extends AbstractData implements ILatLng {

    /**
     * Default icon dimension
     */
    public static final Dimension DEFAULT_IMAGE_DIMENSION = new Dimension(32, 32);

    /**
     * Default icon color
     */
    public static final String DEFAULT_COLOR = "#9F78FF";

    double longitude, latitude;
    String imagePath;
    String imageUrl;
    String htmlDescription;
    Dimension imageDimension;
    String color;

    /**
     * Clone point point.
     *
     * @return the point
     */
    public Point clonePoint() {

        Point p = new Point(longitude, latitude);

        p.setId(this.getId());

        p.imagePath = this.imagePath;
        p.imageUrl = this.imageUrl;
        p.htmlDescription = this.htmlDescription;
        p.imageDimension = this.imageDimension;
        p.color = this.color;

        return p;
    }

    /**
     * Point constructor
     *
     * @param longitude The longitude
     * @param latitude  The latitude
     */
    public Point(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Gets longitude
     *
     * @return The longitude
     */
    @Override
    public double getLongitude() {
        return longitude;
    }

    /**
     * Gets latitude
     *
     * @return The latitude
     */
    @Override
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets resource image path
     *
     * @param imagePath The image path
     */
    public void setImagePath(String imagePath) {

        this.imageUrl = null;

        if (imagePath == null) {
            this.imagePath = null;
            return;
        }

        if (!imagePath.startsWith("/"))
            imagePath = "/" + imagePath;

        this.imagePath = "classpath:" + imagePath;
    }

    /**
     * Sets resource image URL
     *
     * @param imageUrl The image path
     */
    public void setImageUrl(String imageUrl) {

        this.imagePath = null;
        this.imageUrl = imageUrl;
    }

    /**
     * Gets resource image URL
     *
     * @return The resource image path
     */
    public String getImageUrl() {
        if (imageUrl == null && imagePath != null) {
            if (!imagePath.startsWith("classpath:")) {

                String slash = imagePath.startsWith("/") ? "" : "/";
                imagePath = "classpath:" + slash + imagePath;
            }
            imageUrl = ResourceProviderServlet.computeImageResourceDownloadUrl(imagePath, getImageDimension());
        }
        return imageUrl;
    }

    /**
     * Gets html description.
     *
     * @return the html description
     */
    public String getHtmlDescription() {
        return htmlDescription;
    }

    /**
     * Sets html description.
     *
     * @param htmlDescription the html description
     */
    public void setHtmlDescription(String htmlDescription) {
        this.htmlDescription = htmlDescription;
    }

    /**
     * Sets dimension
     *
     * @param imageDimension The dimension
     */
    public void setImageDimension(Dimension imageDimension) {
        this.imageDimension = imageDimension;
    }

    /**
     * Gets dimension
     *
     * @return The dimension
     */
    public Dimension getImageDimension() {
        return imageDimension != null ? imageDimension : DEFAULT_IMAGE_DIMENSION;
    }

    /**
     * Sets point's color
     *
     * @param color The color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Gets point's color
     *
     * @return The color
     */
    public String getColor() {
        return color!=null ? color : DEFAULT_COLOR;
    }

    @Override
    public String toString() {
        return "(" + longitude + "," + latitude + ")" + (id!=null ? " "+id:"");
    }

    /**
     * Same point are equals if latitude and longitude are equals
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Point
            && ((Point) obj).latitude == this.latitude
            && ((Point) obj).longitude == this.longitude;
    }

    /**
     * Return hashcode from latitude and latitude
     * @return The hashcode
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new double[]{latitude + latitude});
    }
}
