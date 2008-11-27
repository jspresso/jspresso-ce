/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RForm extends RContainer {

  private int                columnCount;
  private String             labelsPosition;
  private Integer[]      elementWidths;

  private RComponent[]   elements;

  /**
   * Constructs a new <code>RForm</code> instance.
   * 
   * @param guid
   *          the guid
   */
  public RForm(String guid) {
    super(guid);
  }

  /**
   * Sets the elements.
   * 
   * @param elements
   *          the elements to set.
   */
  public void setElements(RComponent[] elements) {
    this.elements = elements;
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
   * Gets the columnCount.
   * 
   * @return the columnCount.
   */
  public int getColumnCount() {
    return columnCount;
  }

  
  /**
   * Sets the columnCount.
   * 
   * @param columnCount the columnCount to set.
   */
  public void setColumnCount(int columnCount) {
    this.columnCount = columnCount;
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
   * Sets the labelsPosition.
   * 
   * @param labelsPosition the labelsPosition to set.
   */
  public void setLabelsPosition(String labelsPosition) {
    this.labelsPosition = labelsPosition;
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
   * Sets the elementWidths.
   * 
   * @param elementWidths the elementWidths to set.
   */
  public void setElementWidths(Integer[] elementWidths) {
    this.elementWidths = elementWidths;
  }

}
