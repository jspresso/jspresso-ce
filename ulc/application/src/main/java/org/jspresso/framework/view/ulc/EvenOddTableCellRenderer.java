/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.ulc;

import org.jspresso.framework.util.ulc.UlcUtil;

import com.ulcjava.base.application.IRendererComponent;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.ULCTable;
import com.ulcjava.base.application.table.DefaultTableCellRenderer;

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

  private static final long serialVersionUID = -932556734324432049L;

  private int               column;

  /**
   * Constructs a new <code>EvenOddTableCellRenderer</code> instance.
   * 
   * @param column
   *            the column this renderer is attached to.
   */
  public EvenOddTableCellRenderer(int column) {
    this.column = column;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IRendererComponent getTableCellRendererComponent(ULCTable table,
      Object value, boolean isSelected, boolean hasFocus, int row) {
    IRendererComponent renderer = super.getTableCellRendererComponent(table,
        value, isSelected, hasFocus, row);
    UlcUtil.alternateEvenOddBackground((ULCComponent) renderer, table,
        isSelected, row);
    if (isSelected && hasFocus && table.getModel().isCellEditable(row, column)) {
      ((ULCComponent) renderer).setBackground(((ULCComponent) renderer)
          .getBackground().brighter());
      ((ULCComponent) renderer).setForeground(table.getForeground());
    }
    return renderer;
  }
}
