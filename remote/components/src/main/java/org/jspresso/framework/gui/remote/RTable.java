/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
 * A tabular component.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RTable extends RCollectionComponent {

  private static final long serialVersionUID = 4825156764599864408L;

  private RComponent[]      columns;
  private String[]          columnIds;
  private RAction           sortingAction;
  private boolean           horizontallyScrollable;
  private boolean           sortable;

  /**
   * Constructs a new <code>RTable</code> instance. Only used for GWT
   * serialization support.
   */
  protected RTable() {
    // For GWT support
  }

  /**
   * Constructs a new <code>RTable</code> instance.
   * 
   * @param guid
   *          the guid
   */
  public RTable(String guid) {
    super(guid);
  }

  /**
   * Gets the columns.
   * 
   * @return the columns.
   */
  public RComponent[] getColumns() {
    return columns;
  }

  /**
   * Sets the columns.
   * 
   * @param columns
   *          the columns to set.
   */
  public void setColumns(RComponent[] columns) {
    this.columns = columns;
  }

  /**
   * Gets the sortingAction.
   * 
   * @return the sortingAction.
   */
  public RAction getSortingAction() {
    return sortingAction;
  }

  /**
   * Sets the sortingAction.
   * 
   * @param sortingAction
   *          the sortingAction to set.
   */
  public void setSortingAction(RAction sortingAction) {
    this.sortingAction = sortingAction;
  }

  /**
   * Gets the columnIds.
   * 
   * @return the columnIds.
   */
  public String[] getColumnIds() {
    return columnIds;
  }

  /**
   * Sets the columnIds.
   * 
   * @param columnIds
   *          the columnIds to set.
   */
  public void setColumnIds(String[] columnIds) {
    this.columnIds = columnIds;
  }

  /**
   * Gets the horizontallyScrollable.
   * 
   * @return the horizontallyScrollable.
   */
  public boolean isHorizontallyScrollable() {
    return horizontallyScrollable;
  }

  /**
   * Sets the horizontallyScrollable.
   * 
   * @param horizontallyScrollable
   *          the horizontallyScrollable to set.
   */
  public void setHorizontallyScrollable(boolean horizontallyScrollable) {
    this.horizontallyScrollable = horizontallyScrollable;
  }

  /**
   * Gets the sortable.
   * 
   * @return the sortable.
   */
  public boolean isSortable() {
    return sortable;
  }

  /**
   * Sets the sortable.
   * 
   * @param sortable
   *          the sortable to set.
   */
  public void setSortable(boolean sortable) {
    this.sortable = sortable;
  }
}
