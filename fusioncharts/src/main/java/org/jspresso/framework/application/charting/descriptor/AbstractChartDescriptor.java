/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.charting.descriptor;

import org.jspresso.framework.util.gui.Dimension;

/**
 * Basic implementation of a chart descriptor.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractChartDescriptor implements IChartDescriptor {

  private String  url;
  private Integer width;
  private Integer height;
  private String  title;

  /**
   * Gets the chart Url.
   * 
   * @return the chart Url.
   */
  public String getUrl() {
    return url;
  }

  /**
   * Sets the chart Url.
   * 
   * @param url
   *          the chart Url to set.
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Sets the chart Width.
   * 
   * @param width
   *          the chart Width to set.
   */
  public void setWidth(Integer width) {
    this.width = width;
  }

  /**
   * Sets the chart Height.
   * 
   * @param height
   *          the chart Height to set.
   */
  public void setHeight(Integer height) {
    this.height = height;
  }

  /**
   * {@inheritDoc}
   */
  public Dimension getDimension() {
    int w = 600;
    int h = 500;
    if (width != null) {
      w = width.intValue();
    }
    if (height != null) {
      h = height.intValue();
    }
    Dimension dim = new Dimension();
    dim.setWidth(w);
    dim.setHeight(h);
    return dim;
  }

  /**
   * Gets the title.
   * 
   * @return the title.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title.
   * 
   * @param title
   *          the title to set.
   */
  public void setTitle(String title) {
    this.title = title;
  }

}
