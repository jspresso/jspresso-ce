/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.util.wings.WingsUtil;
import org.wings.SCheckBox;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.STable;
import org.wings.table.STableCellRenderer;

/**
 * Renders a table cell using a checkbox.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BooleanTableCellRenderer extends SCheckBox implements
    STableCellRenderer {

  private static final long serialVersionUID = 5944792695339009139L;

  private Color               unselectedForeground;
  private Color               unselectedBackground;

  /**
   * Constructs a new <code>BooleanTableCellRenderer</code> instance.
   */
  public BooleanTableCellRenderer() {
    super();
    setHorizontalAlignment(SConstants.CENTER);
  }

  /**
   * {@inheritDoc}
   */
  public SComponent getTableCellRendererComponent(STable table, Object value,
      boolean isSelected, int row, int column) {
    if (isSelected) {
      super.setForeground(table.getSelectionForeground());
      super.setBackground(table.getSelectionBackground());
    } else {
      if (unselectedForeground != null) {
        super.setForeground(unselectedForeground);
      } else {
        super.setForeground(table.getForeground());
      }
      Color actualBackground = table.getBackground();
      if (unselectedBackground != null) {
        actualBackground = unselectedBackground;
      }
      super.setBackground(WingsUtil.computeEvenOddBackground(actualBackground,
          isSelected, row));
    }
    if (value instanceof IValueConnector) {
      Object connectorValue = ((IValueConnector) value).getConnectorValue();
      setSelected((connectorValue != null && ((Boolean) connectorValue)
          .booleanValue()));
    } else {
      setSelected((value != null && ((Boolean) value).booleanValue()));
    }
    if (table.isCellEditable(row, column)) {
      setEnabled(true);
    } else {
      setEnabled(false);
    }
    return this;
  }

  /**
   * Notification from the <code>UIManager</code> that the look and feel [L&F]
   * has changed. Replaces the current UI object with the latest version from
   * the <code>UIManager</code>.
   */
  @Override
  public void updateCG() {
    super.updateCG();
    setForeground(null);
    setBackground(null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setForeground(Color fg) {
    super.setForeground(fg);
    unselectedForeground = fg;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setBackground(Color bg) {
    super.setBackground(bg);
    unselectedBackground = bg;
  }
}
