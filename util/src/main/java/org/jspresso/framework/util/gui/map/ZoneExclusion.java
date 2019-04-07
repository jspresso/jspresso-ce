package org.jspresso.framework.util.gui.map;

import static org.jspresso.framework.util.gui.map.Zone.DEFAULT_LINE_COLOR;
import static org.jspresso.framework.util.gui.map.Zone.DEFAULT_WIDTH;

/**
 * The type Zone exclusion.
 */
@SuppressWarnings({"WeakerAccess"})
public class ZoneExclusion {

    /**
     * The shape.
     */
    Point[] points;

    /**
     * The Line color.
     */
    String lineColor;

    /**
     * The Line width.
     */
    Integer lineWidth;

    /**
     * Clone point point.
     *
     * @return the point
     */
    public ZoneExclusion cloneZoneExclusion(boolean clonePoints) {

        ZoneExclusion z = clonePoints ? new ZoneExclusion(MapHelper.clonePoints(points)) : new ZoneExclusion(points);

        z.lineColor = this.lineColor;
        z.lineWidth = this.lineWidth;
        return z;
    }

    /**
     * Instantiates a new Zone exclusion.
     *
     * @param points the shapes as closed routes
     */
    public ZoneExclusion(Point... points) {
        this.points = points;
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
     * Get points point [ ].
     *
     * @return the point [ ]
     */
    public Point[] getPoints() {
        return points;
    }
}
