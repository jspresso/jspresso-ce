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

import org.jspresso.framework.state.remote.RemoteCompositeValueState;

/**
 * A tabular component.
 *
 * @author Vincent Vandenschrick
 */
public class RTable extends RCollectionComponent {

  private static final long         serialVersionUID = 4825156764599864408L;

  private String[]                  columnIds;
  private RComponent[]              columns;
  private RComponent[]              columnHeaders;
  private RemoteCompositeValueState rowPrototype;
  private boolean                   horizontallyScrollable;
  private boolean                   sortable;
  private RAction                   sortingAction;
  private boolean                   columnReorderingAllowed;

  /**
   * Constructs a new {@code RTable} instance.
   *
   * @param guid
   *          the guid
   */
  public RTable(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RTable} instance. Only used for serialization
   * support.
   */
  public RTable() {
    // For serialization support
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
   * Gets the columns.
   *
   * @return the columns.
   */
  public RComponent[] getColumns() {
    return columns;
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
   * Gets the horizontallyScrollable.
   *
   * @return the horizontallyScrollable.
   */
  public boolean isHorizontallyScrollable() {
    return horizontallyScrollable;
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
   * Sets the columnIds.
   *
   * @param columnIds
   *          the columnIds to set.
   */
  public void setColumnIds(String... columnIds) {
    this.columnIds = columnIds;
  }

  /**
   * Sets the columns.
   *
   * @param columns
   *          the columns to set.
   */
  public void setColumns(RComponent... columns) {
    this.columns = columns;
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
   * Sets the sortable.
   *
   * @param sortable
   *          the sortable to set.
   */
  public void setSortable(boolean sortable) {
    this.sortable = sortable;
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
   * Gets the columnHeaders.
   *
   * @return the columnHeaders.
   */
  public RComponent[] getColumnHeaders() {
    return columnHeaders;
  }

  /**
   * Sets the columnHeaders.
   *
   * @param columnHeaders
   *          the columnHeaders to set.
   */
  public void setColumnHeaders(RComponent... columnHeaders) {
    this.columnHeaders = columnHeaders;
  }

  /**
   * Gets the rowPrototype.
   *
   * @return the rowPrototype.
   */
  public RemoteCompositeValueState getRowPrototype() {
    return rowPrototype;
  }

  /**
   * Sets the rowPrototype.
   *
   * @param rowPrototype
   *          the rowPrototype to set.
   */
  public void setRowPrototype(RemoteCompositeValueState rowPrototype) {
    this.rowPrototype = rowPrototype;
  }

  /**
   * Is column reordering allowed.
   *
   * @return the boolean
   */
  public boolean isColumnReorderingAllowed() {
    return columnReorderingAllowed;
  }

  /**
   * Sets column reordering allowed.
   *
   * @param columnReorderingAllowed the column reordering allowed
   */
  public void setColumnReorderingAllowed(boolean columnReorderingAllowed) {
    this.columnReorderingAllowed = columnReorderingAllowed;
  }
}
