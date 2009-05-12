/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.gui.wings.components;

import java.awt.AWTEvent;

/**
 * The event representing a table header click.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
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
