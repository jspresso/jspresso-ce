/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
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
