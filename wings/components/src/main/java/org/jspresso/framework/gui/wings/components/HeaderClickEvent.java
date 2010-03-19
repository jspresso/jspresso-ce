/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.gui.wings.components;

import java.awt.AWTEvent;

/**
 * The event representing a table header click.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class HeaderClickEvent extends AWTEvent {

  private static final long serialVersionUID = 8470997493776930280L;
  private int               column           = -1;
  private static final int  HEADER_CLICKED   = 12001;

  /**
   * Constructs a new <code>HeaderClickEvent</code> instance.
   * 
   * @param source
   *          the STable from wich the event originates.
   * @param column
   *          the header column the event occured on.
   */
  public HeaderClickEvent(Object source, int column) {
    super(source, HEADER_CLICKED);
    this.column = column;
  }

  /**
   * Gets the column.
   * 
   * @return the column.
   */
  public int getColumn() {
    return column;
  }
}
