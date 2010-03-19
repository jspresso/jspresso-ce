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
package org.jspresso.framework.view.ulc;

import com.ulcjava.base.application.IRendererComponent;
import com.ulcjava.base.application.ULCTable;
import com.ulcjava.base.application.datatype.IDataType;

/**
 * A table cell renderer based on a formatter.
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
