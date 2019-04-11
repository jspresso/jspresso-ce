package org.jspresso.framework.util.gui.map;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The type Shape.
 */
public class Shape extends AbstractData {

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

    private Zone zone;
    private Set<Zone> exclusions;

    /**
     * The Fill color.
     */
    private String fillColor;

    /**
     * The Line color.
     */
    private String lineColor;

    /**
     * The Line width.
     */
    private Integer lineWidth;

    /**
     * Clone point point.
     *
     * @param clonePoints the clone points
     * @return the point
     */
    public Shape cloneShape(boolean clonePoints) {

        Shape s = new Shape();

        s.setId(this.getId());

        s.fillColor = this.fillColor;
        s.lineColor = this.lineColor;
        s.lineWidth = this.lineWidth;

        s.zone = MapHelper.cloneZones(new Zone[]{this.zone}, clonePoints)[0];
        s.exclusions = new LinkedHashSet<>(Arrays.asList(MapHelper.cloneZones(this.exclusions.toArray(new Zone[0]), clonePoints)));

        return s;
    }

    /**
     * Gets zone.
     *
     * @return the zone
     */
    public Zone getZone() {
        return zone;
    }

    /**
     * Gets exclusions.
     *
     * @return the exclusions
     */
    public Set<Zone> getExclusions() {
        return exclusions;
    }

    /**
     * Add exclusion.
     *
     * @param zone the zone
     */
    public void addExclusion(Zone zone) {
        exclusions.add(zone);
    }

    /**
     * Instantiates a new Shape.
     */
    public Shape(Zone zone) {
        this.zone = zone;
        exclusions = new LinkedHashSet<>();
    }

    private Shape() {
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
     * Gets fill color.
     *
     * @return the fill color
     */
    public String getFillColor() {
        return fillColor !=null ? fillColor : DEFAULT_FILL_COLOR;
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
     * Sets transparent.
     */
    public void setTransparent() {
        setFillColor("rgba(255, 255, 255, 0)");
    }

    /**
     * Gets middle.
     *
     * @return the zone's middle
     */
    public Pair<Point, Point> getBoundaryBox() {
        return getZone().getBoundaryBox();
    }

}
