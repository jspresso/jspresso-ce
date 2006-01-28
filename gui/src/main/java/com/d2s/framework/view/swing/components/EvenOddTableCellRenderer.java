/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.swing.components;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.d2s.framework.util.swing.SwingUtil;

/**
 * A default table cell renderer rendering even and odd rows background slightly
 * differently.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EvenOddTableCellRenderer extends DefaultTableCellRenderer {

  private static final long serialVersionUID = -635326662239616998L;

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
    Component renderer = super.getTableCellRendererComponent(table, value,
        isSelected, hasFocus, row, column);
    SwingUtil.alternateEvenOddBackground(renderer, table, isSelected, row);
    return renderer;
  }

}
