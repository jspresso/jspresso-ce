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

import org.jspresso.framework.util.ulc.UlcUtil;

import com.ulcjava.base.application.IRendererComponent;
import com.ulcjava.base.application.ULCTable;
import com.ulcjava.base.application.table.DefaultTableCellRenderer;
import com.ulcjava.base.application.util.Color;

/**
 * A default table cell renderer rendering even and odd rows background slightly
 * differently.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EvenOddTableCellRenderer extends DefaultTableCellRenderer {

  private static final long serialVersionUID = -932556734324432049L;

  private int               column;
  private Color             backgroundBase;

  /**
   * Constructs a new <code>EvenOddTableCellRenderer</code> instance.
   * 
   * @param column
   *          the column this renderer is attached to.
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
    Color actualBackground = table.getBackground();
    if (backgroundBase != null) {
      actualBackground = backgroundBase;
    }
    super.setBackground(UlcUtil.computeEvenOddBackground(actualBackground,
        isSelected, row));
    EvenOddTableCellRenderer comp = (EvenOddTableCellRenderer) super.getTableCellRendererComponent(table, value, isSelected,
        hasFocus, row);
    if (isSelected && hasFocus && table.getModel().isCellEditable(row, column)) {
      comp.superSetBackground(comp.getBackground().brighter());
    }
    return comp;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setBackground(Color c) {
    backgroundBase = c;
    superSetBackground(c);
  }

  private void superSetBackground(Color c) {
    super.setBackground(c);
  }
}
