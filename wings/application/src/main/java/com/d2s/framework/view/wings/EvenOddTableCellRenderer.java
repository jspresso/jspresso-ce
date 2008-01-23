/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view.wings;

import org.wings.SComponent;
import org.wings.STable;
import org.wings.table.SDefaultTableCellRenderer;

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
public class EvenOddTableCellRenderer extends SDefaultTableCellRenderer {

  private static final long serialVersionUID = -635326662239616998L;

  /**
   * {@inheritDoc}
   */
  @Override
  public SComponent getTableCellRendererComponent(STable table, Object value,
      boolean isSelected, int row, int column) {
    // SComponent renderer = super.getTableCellRendererComponent(table, value,
    // isSelected, row, column);
    // WingsUtil.alternateEvenOddBackground(renderer, table, isSelected, row);
    // if (isSelected && table.getModel().isCellEditable(row, column)) {
    // renderer.setBackground(renderer.getBackground().brighter());
    // renderer.setForeground(table.getForeground());
    // }
    return super.getTableCellRendererComponent(table, value, isSelected, row,
        column);
  }

}
