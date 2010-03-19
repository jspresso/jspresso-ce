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
package org.jspresso.framework.view.swing;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.jspresso.framework.util.swing.SwingUtil;


/**
 * A default table cell renderer rendering even and odd rows background slightly
 * differently.
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
