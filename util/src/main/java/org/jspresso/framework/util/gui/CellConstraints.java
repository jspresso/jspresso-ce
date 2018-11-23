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
package org.jspresso.framework.util.gui;

import java.io.Serializable;

/**
 * Simple holder of cell constraints.
 *
 * @author Vincent Vandenschrick
 */
public class CellConstraints implements Serializable {

  private static final long serialVersionUID = -7074571604039727117L;

  private int                  row;
  private int                  column;
  private int                  width;
  private int                  height;
  private boolean              widthResizable;
  private boolean              heightResizable;
  private Boolean              fillWidth;
  private Boolean              fillHeight;
  private EHorizontalAlignment horizontalAlignment;
  private EVerticalAlignment   verticalAlignment;

  /**
   * Constructs a new {@code CellConstraints} instance.
   */
  public CellConstraints() {
    width = 1;
    height = 1;
    widthResizable = true;
    heightResizable = true;
    horizontalAlignment = EHorizontalAlignment.LEFT;
    verticalAlignment = EVerticalAlignment.MIDDLE;
  }

  /**
   * Constructs a new {@code CellConstraints} instance.
   *
   * @param row
   *     the row.
   * @param column
   *     the column.
   * @param width
   *     the width.
   * @param height
   *     the height.
   * @param widthResizable
   *     is resizable in width ?
   * @param heightResizable
   *     is resizable in height ?
   */
  public CellConstraints(int row, int column, int width, int height, boolean widthResizable, boolean heightResizable) {
    super();
    this.row = row;
    this.column = column;
    this.width = width;
    this.height = height;
    this.widthResizable = widthResizable;
    this.heightResizable = heightResizable;
  }

  /**
   * Gets the column.
   *
   * @return the column.
   */
  public int getColumn() {
    return column;
  }

  /**
   * Gets the height.
   *
   * @return the height.
   */
  public int getHeight() {
    return height;
  }

  /**
   * Gets the row.
   *
   * @return the row.
   */
  public int getRow() {
    return row;
  }

  /**
   * Gets the width.
   *
   * @return the width.
   */
  public int getWidth() {
    return width;
  }

  /**
   * Gets the heightResizable.
   *
   * @return the heightResizable.
   */
  public boolean isHeightResizable() {
    return heightResizable;
  }

  /**
   * Gets the widthResizable.
   *
   * @return the widthResizable.
   */
  public boolean isWidthResizable() {
    return widthResizable;
  }

  /**
   * Sets the column.
   *
   * @param column
   *     the column to set.
   */
  public void setColumn(int column) {
    this.column = column;
  }

  /**
   * Sets the height.
   *
   * @param height
   *     the height to set.
   */
  public void setHeight(int height) {
    this.height = height;
  }

  /**
   * Sets the heightResizable.
   *
   * @param heightResizable
   *     the heightResizable to set.
   */
  public void setHeightResizable(boolean heightResizable) {
    this.heightResizable = heightResizable;
  }

  /**
   * Sets the row.
   *
   * @param row
   *     the row to set.
   */
  public void setRow(int row) {
    this.row = row;
  }

  /**
   * Sets the width.
   *
   * @param width
   *     the width to set.
   */
  public void setWidth(int width) {
    this.width = width;
  }

  /**
   * Sets the widthResizable.
   *
   * @param widthResizable
   *     the widthResizable to set.
   */
  public void setWidthResizable(boolean widthResizable) {
    this.widthResizable = widthResizable;
  }

  /**
   * Is fill width boolean.
   *
   * @return the boolean
   */
  public boolean isFillWidth() {
    if (fillWidth == null) {
      fillWidth = widthResizable;
    }
    return fillWidth;
  }

  /**
   * Sets fill width.
   *
   * @param fillWidth
   *     the fill width
   */
  public void setFillWidth(boolean fillWidth) {
    this.fillWidth = fillWidth;
  }

  /**
   * Is fill height boolean.
   *
   * @return the boolean
   */
  public boolean isFillHeight() {
    if (fillHeight == null) {
      fillHeight = heightResizable;
    }
    return fillHeight;
  }

  /**
   * Sets fill height.
   *
   * @param fillHeight
   *     the fill height
   */
  public void setFillHeight(boolean fillHeight) {
    this.fillHeight = fillHeight;
  }

  /**
   * Gets horizontal alignment.
   *
   * @return the horizontal alignment
   */
  public EHorizontalAlignment getHorizontalAlignment() {
    return horizontalAlignment;
  }

  /**
   * Sets horizontal alignment.
   *
   * @param horizontalAlignment
   *     the horizontal alignment
   */
  public void setHorizontalAlignment(EHorizontalAlignment horizontalAlignment) {
    this.horizontalAlignment = horizontalAlignment;
  }

  /**
   * Gets vertical alignment.
   *
   * @return the vertical alignment
   */
  public EVerticalAlignment getVerticalAlignment() {
    return verticalAlignment;
  }

  /**
   * Sets vertical alignment.
   *
   * @param verticalAlignment
   *     the vertical alignment
   */
  public void setVerticalAlignment(EVerticalAlignment verticalAlignment) {
    this.verticalAlignment = verticalAlignment;
  }
}
