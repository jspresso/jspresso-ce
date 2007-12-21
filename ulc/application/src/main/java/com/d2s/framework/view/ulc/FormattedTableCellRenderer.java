/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view.ulc;

import com.ulcjava.base.application.IRendererComponent;
import com.ulcjava.base.application.ULCTable;
import com.ulcjava.base.application.datatype.IDataType;

/**
 * A table cell renderer based on a formatter.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class FormattedTableCellRenderer extends EvenOddTableCellRenderer {

  private static final long serialVersionUID = 1315159518376960498L;
  private IDataType         dataType;

  /**
   * Constructs a new <code>FormattedTableCellRenderer</code> instance.
   * 
   * @param column
   *            the column this renderer is attached to.
   * @param dataType
   *            the dataType used to format object values.
   */
  public FormattedTableCellRenderer(int column, IDataType dataType) {
    super(column);
    this.dataType = dataType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IRendererComponent getTableCellRendererComponent(ULCTable table,
      Object value, boolean isSelected, boolean hasFocus, int row) {
    setDataType(dataType);
    return super.getTableCellRendererComponent(table, value, isSelected,
        hasFocus, row);
  }
}
