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
package org.jspresso.framework.gui.remote;

/**
 * A form component.
 *
 * @author Vincent Vandenschrick
 */
public class RForm extends RComponent {

  private static final long serialVersionUID = -5376646056261143239L;

  private int          columnCount;
  private RComponent[] elementLabels;
  private RComponent[] elements;
  private Integer[]    elementWidths;
  private String[]     labelHorizontalPositions;
  private boolean      verticallyScrollable;
  private boolean      widthResizeable;
  private String       labelsPosition;

  /**
   * Constructs a new {@code RForm} instance.
   *
   * @param guid
   *     the guid
   */
  public RForm(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RForm} instance. Only used for serialization
   * support.
   */
  public RForm() {
    // For serialization support
  }

  /**
   * Gets the columnCount.
   *
   * @return the columnCount.
   */
  public int getColumnCount() {
    return columnCount;
  }

  /**
   * Gets the elementLabels.
   *
   * @return the elementLabels.
   */
  public RComponent[] getElementLabels() {
    return elementLabels;
  }

  /**
   * Gets the elements.
   *
   * @return the elements.
   */
  public RComponent[] getElements() {
    return elements;
  }

  /**
   * Gets the elementWidths.
   *
   * @return the elementWidths.
   */
  public Integer[] getElementWidths() {
    return elementWidths;
  }

  /**
   * Gets the labelsPosition.
   *
   * @return the labelsPosition.
   */
  public String getLabelsPosition() {
    return labelsPosition;
  }

  /**
   * Sets the columnCount.
   *
   * @param columnCount
   *     the columnCount to set.
   */
  public void setColumnCount(int columnCount) {
    this.columnCount = columnCount;
  }

  /**
   * Sets the elementLabels.
   *
   * @param elementLabels
   *     the elementLabels to set.
   */
  public void setElementLabels(RComponent... elementLabels) {
    this.elementLabels = elementLabels;
  }

  /**
   * Sets the elements.
   *
   * @param elements
   *     the elements to set.
   */
  public void setElements(RComponent... elements) {
    this.elements = elements;
  }

  /**
   * Sets the elementWidths.
   *
   * @param elementWidths
   *     the elementWidths to set.
   */
  public void setElementWidths(Integer... elementWidths) {
    this.elementWidths = elementWidths;
  }

  /**
   * Sets the labelsPosition.
   *
   * @param labelsPosition
   *     the labelsPosition to set.
   */
  public void setLabelsPosition(String labelsPosition) {
    this.labelsPosition = labelsPosition;
  }

  /**
   * Gets the verticallyScrollable.
   *
   * @return the verticallyScrollable.
   */
  public boolean isVerticallyScrollable() {
    return verticallyScrollable;
  }

  /**
   * Sets the verticallyScrollable.
   *
   * @param verticallyScrollable
   *     the verticallyScrollable to set.
   */
  public void setVerticallyScrollable(boolean verticallyScrollable) {
    this.verticallyScrollable = verticallyScrollable;
  }

  /**
   * Get label horizontal positions.
   *
   * @return the label horizontal positions
   */
  public String[] getLabelHorizontalPositions() {
    return labelHorizontalPositions;
  }

  /**
   * Sets label horizontal positions.
   *
   * @param labelHorizontalPositions
   *     the label horizontal positions
   */
  public void setLabelHorizontalPositions(String... labelHorizontalPositions) {
    this.labelHorizontalPositions = labelHorizontalPositions;
  }

  /**
   * Is width resizeable.
   *
   * @return the boolean
   */
  public boolean isWidthResizeable() {
    return widthResizeable;
  }

  /**
   * Sets width resizeable.
   *
   * @param widthResizeable the width resizeable
   */
  public void setWidthResizeable(boolean widthResizeable) {
    this.widthResizeable = widthResizeable;
  }
}
