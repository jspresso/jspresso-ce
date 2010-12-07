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
package org.jspresso.framework.view.wings;

import java.awt.Color;

import org.jspresso.framework.util.wings.WingsUtil;
import org.wings.SComponent;
import org.wings.STable;
import org.wings.table.SDefaultTableCellRenderer;

/**
 * A default table cell renderer rendering even and odd rows background slightly
 * differently.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EvenOddTableCellRenderer extends SDefaultTableCellRenderer {

  private static final long serialVersionUID = -635326662239616998L;

  private Color             backgroundBase;

  /**
   * {@inheritDoc}
   */
  @Override
  public SComponent getTableCellRendererComponent(STable table, Object value,
      boolean isSelected, int row, int column) {
    Color actualBackground = table.getBackground();
    if (backgroundBase != null) {
      actualBackground = backgroundBase;
    }
    super.setBackground(WingsUtil.computeEvenOddBackground(actualBackground,
        isSelected, row));
    return super.getTableCellRendererComponent(table, value, isSelected,
         row, column);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setBackground(Color c) {
    backgroundBase = c;
    super.setBackground(c);
  }
}
