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

import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.util.ulc.UlcUtil;

import com.ulcjava.base.application.ClientContext;
import com.ulcjava.base.application.IRendererComponent;
import com.ulcjava.base.application.ULCCheckBox;
import com.ulcjava.base.application.ULCTable;
import com.ulcjava.base.application.border.ULCAbstractBorder;
import com.ulcjava.base.application.border.ULCEmptyBorder;
import com.ulcjava.base.application.table.ITableCellRenderer;
import com.ulcjava.base.application.util.Color;
import com.ulcjava.base.shared.IDefaults;

/**
 * Renders a table cell using a checkbox.
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
public class BooleanTableCellRenderer extends ULCCheckBox implements
    ITableCellRenderer {

  private static final ULCAbstractBorder NO_FOCUS_BORDER  = new ULCEmptyBorder(
                                                              1, 1, 1, 1);
  private static final long              serialVersionUID = 5944792695339009139L;

  /**
   * Constructs a new <code>BooleanTableCellRenderer</code> instance.
   */
  public BooleanTableCellRenderer() {
    super();
    setHorizontalAlignment(IDefaults.CENTER);
    setBorderPainted(true);
  }

  /**
   * {@inheritDoc}
   */
  public IRendererComponent getTableCellRendererComponent(ULCTable table,
      Object value, boolean isSelected, boolean hasFocus, int row) {
    if (isSelected) {
      super.setForeground(table.getSelectionForeground());
      super.setBackground(table.getSelectionBackground());
    } else {
      super.setForeground(table.getForeground());
      UlcUtil.alternateEvenOddBackground(this, table, isSelected, row);
    }
    if (value instanceof IValueConnector) {
      Object connectorValue = ((IValueConnector) value).getConnectorValue();
      setSelected((connectorValue != null && ((Boolean) connectorValue)
          .booleanValue()));
    } else {
      setSelected((value != null && ((Boolean) value).booleanValue()));
    }
    if (hasFocus) {
      ULCAbstractBorder border = null;
      if (isSelected) {
        border = ClientContext
            .getBorder("Table.focusSelectedCellHighlightBorder");
      }
      if (border == null) {
        border = ClientContext.getBorder("Table.focusCellHighlightBorder");
      }
      setBorder(border);
      if (!isSelected) {
        Color col;
        col = ClientContext.getColor("Table.focusCellForeground");
        if (col != null) {
          super.setForeground(col);
        }
        col = ClientContext.getColor("Table.focusCellBackground");
        if (col != null) {
          super.setBackground(col);
        }
      }
    } else {
      setBorder(NO_FOCUS_BORDER);
    }
    return this;
  }
}
