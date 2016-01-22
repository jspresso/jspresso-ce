/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.charting.descriptor;

import org.jspresso.framework.util.gui.Dimension;

/**
 * Basic implementation of a chart descriptor.
 *
 * @author Vincent Vandenschrick
 */
public abstract class AbstractChartDescriptor implements IChartDescriptor {

  private Integer height;
  private String  title;
  private String  url;
  private Integer width;

  /**
   * {@inheritDoc}
   */
  @Override
  public Dimension getDimension() {
    int w = 600;
    int h = 500;
    if (width != null) {
      w = width;
    }
    if (height != null) {
      h = height;
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
  @Override
  public String getTitle() {
    return title;
  }

  /**
   * Gets the chart Url.
   *
   * @return the chart Url.
   */
  @Override
  public String getUrl() {
    return url;
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
   * Sets the title.
   *
   * @param title
   *          the title to set.
   */
  public void setTitle(String title) {
    this.title = title;
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

}
