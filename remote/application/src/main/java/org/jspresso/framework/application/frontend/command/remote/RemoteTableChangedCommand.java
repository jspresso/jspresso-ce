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
package org.jspresso.framework.application.frontend.command.remote;

/**
 * A command to notify of table columns reordering / resize.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteTableChangedCommand extends RemoteCommand {

  private static final long serialVersionUID = 3637851380452065032L;

  private String            tableId;
  private String[]          columnIds;
  private Integer[]         columnWidths;

  /**
   * Gets the tableId.
   *
   * @return the tableId.
   */
  public String getTableId() {
    return tableId;
  }

  /**
   * Sets the tableId.
   *
   * @param tableId
   *          the tableId to set.
   */
  public void setTableId(String tableId) {
    this.tableId = tableId;
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
  public void setColumnIds(String... columnIds) {
    this.columnIds = columnIds;
  }

  /**
   * Gets the columnWidths.
   *
   * @return the columnWidths.
   */
  public Integer[] getColumnWidths() {
    return columnWidths;
  }

  /**
   * Sets the columnWidths.
   *
   * @param columnWidths
   *          the columnWidths to set.
   */
  public void setColumnWidths(Integer... columnWidths) {
    this.columnWidths = columnWidths;
  }

}
