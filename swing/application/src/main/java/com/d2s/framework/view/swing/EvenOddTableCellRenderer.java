/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view.swing;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.d2s.framework.util.swing.SwingUtil;

/**
 * A default table cell renderer rendering even and odd rows background slightly
 * differently.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
    if (isSelected && hasFocus && table.getModel().isCellEditable(row, column)) {
      renderer.setBackground(renderer.getBackground().brighter());
      renderer.setForeground(table.getForeground());
    }
    return renderer;
  }

}
